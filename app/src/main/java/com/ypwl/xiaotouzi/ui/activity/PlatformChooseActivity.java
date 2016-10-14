package com.ypwl.xiaotouzi.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.squareup.otto.Subscribe;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.adapter.ItemPlatformSearchAdapter;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.bean.HandTallyBean;
import com.ypwl.xiaotouzi.bean.PlatformChooseBean;
import com.ypwl.xiaotouzi.bean.PlatformSearchBean;
import com.ypwl.xiaotouzi.blockview.PlatformChooseFollwBlock;
import com.ypwl.xiaotouzi.blockview.PlatformChooseHistoryBlock;
import com.ypwl.xiaotouzi.blockview.PlatformChooseHotBlock;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.common.ServerStatus;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.event.IsCollectEvent;
import com.ypwl.xiaotouzi.event.JiZhangChooseEvent;
import com.ypwl.xiaotouzi.event.PlatformChooseClearHistoryEvent;
import com.ypwl.xiaotouzi.event.PlatformChooseEvent;
import com.ypwl.xiaotouzi.interf.INetRequestListener;
import com.ypwl.xiaotouzi.interf.IRequestCallback;
import com.ypwl.xiaotouzi.manager.net.NetHelper;
import com.ypwl.xiaotouzi.netprotocol.PlatformChooseProtocol;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.NetworkUtils;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.ViewUtil;
import com.ypwl.xiaotouzi.view.ContainsEmojiEditText;
import com.ypwl.xiaotouzi.view.dialog.lib.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 平台对比选择页面
 * tengtao
 */
public class PlatformChooseActivity extends BaseActivity implements TextWatcher {
    private KProgressHUD mPbLoading;
    private View mLayoutNoDataView;
    private FrameLayout mFollowContainer,mHistoryContainer,mHotRecommend;
    private FrameLayout mLayoutContent;
    private PlatformChooseFollwBlock mFollwBlock;//我的关注模块
    private PlatformChooseHistoryBlock mHistoryBlock;//历史记录模块
    private PlatformChooseHotBlock mHotBlock;//热门推荐模块
    private PlatformChooseProtocol mPlatformChooseProtocol;//平台数据请求
    private ListView mLvSearch;
    private ItemPlatformSearchAdapter searchAdapter;
    private ContainsEmojiEditText mEtSearch;
    private String ss;//搜索字段
    private List<HandTallyBean> searchData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_platform_choose);
        mPbLoading = KProgressHUD.create(this);
        initView();
        initData();
    }

    private void initView() {
        mHotRecommend = (FrameLayout) findViewById(R.id.layout_hot_recommend_container);
        mFollowContainer = (FrameLayout) findViewById(R.id.layout_follow_container);
        mHistoryContainer = (FrameLayout) findViewById(R.id.layout_history_container);
        mFollwBlock = new PlatformChooseFollwBlock();
        mHistoryBlock = new PlatformChooseHistoryBlock();
        mHotBlock = new PlatformChooseHotBlock();

        mLvSearch = (ListView) findViewById(R.id.lv_search_result);
        searchAdapter = new ItemPlatformSearchAdapter();
        mLvSearch.setDividerHeight(0);
        mLvSearch.setAdapter(searchAdapter);
        mEtSearch = (ContainsEmojiEditText) findViewById(R.id.et_choose_platform_search);
        mEtSearch.addTextChangedListener(this);
        findViewById(R.id.iv_back_arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mLayoutNoDataView = findViewById(R.id.layout_no_data_view);
        mLayoutContent = (FrameLayout) findViewById(R.id.layout_platform_choose_content);
        ViewUtil.showContentLayout(Const.LAYOUT_LOADING, mLayoutNoDataView, mLayoutContent);
    }

    private void initData() {
        if(mPlatformChooseProtocol==null)
            mPlatformChooseProtocol = new PlatformChooseProtocol();
        mPlatformChooseProtocol.loadData(mNetRequestListener, Const.REQUEST_GET);
    }

    /**平台选择，数据获取回调接口*/
    private INetRequestListener mNetRequestListener = new INetRequestListener<PlatformChooseBean>() {

        @Override
        public void netRequestCompleted() {
            if(!NetworkUtils.isNetworkConnected(UIUtil.getContext())){
                ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mLayoutNoDataView, mLayoutContent, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        initData();
                    }
                });
            }
        }

        @Override
        public void netRequestSuccess(PlatformChooseBean bean, boolean isSuccess) {
            if(bean!=null && isSuccess){
                List<PlatformChooseBean.Follow> follow = bean.getFollow();
                List<PlatformChooseBean.Hot> hot = bean.getHot();
                boolean b = mHistoryBlock.setDataFromDb();
                if ((hot!=null && hot.size()==0) && (follow!=null && follow.size()==0) && !b) {
                    ViewUtil.showContentLayout(Const.LAYOUT_EMPTY, mLayoutNoDataView, mLayoutContent);
                } else {
                    if(follow!=null && follow.size()>0){
                        mFollowContainer.setVisibility(View.VISIBLE);
                        mFollowContainer.addView(mFollwBlock.getBlockView());
                        mFollwBlock.setData(bean);
                    }
                    if(b){
                        mHistoryContainer.setVisibility(View.VISIBLE);
                        mHistoryContainer.addView(mHistoryBlock.getBlockView());
                    }
                    if(hot!=null && hot.size()>0){
                        mHotRecommend.setVisibility(View.VISIBLE);
                        mHotRecommend.addView(mHotBlock.getBlockView());
                        mHotBlock.setData(bean);
                    }
                    ViewUtil.showContentLayout(Const.LAYOUT_DATA, mLayoutNoDataView, mLayoutContent);
                }
            }else{
                ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mLayoutNoDataView, mLayoutContent, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        initData();
                    }
                });
            }
        }
    };

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        ss = s.toString();
        if(mEtSearch.containsEmoji(ss.substring(ss.length()-count,ss.length()))){return;}
        if(ss.length()>0){
            //type ：1 记账搜索，2 平台对比搜索
            String url = String.format(URLConstant.PLAT_CHOOSE_DATA_SEARCH, GlobalUtils.token,ss,Const.PLATFORM_CHOOSE_REQUEST_FROM+1);
            NetHelper.get(url, new SearchTextChangeListener());
        }else{
            mLvSearch.setVisibility(View.GONE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /** 平台选择-->数据搜索回调接口 */
    private class SearchTextChangeListener extends IRequestCallback<String> {
        @Override
        public void onStart() {
            mPbLoading.show();
        }

        @Override
        public void onFailure(Exception e) {
            mPbLoading.dismiss();
            UIUtil.showToastShort(getString(R.string.platform_compare_net_error));
        }

        @Override
        public void onSuccess(String jsonStr) {
            mPbLoading.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);
                int status = jsonObject.optInt(Const.JSON_KEY_status, -1);
                switch (status) {
                    case ServerStatus.SERVER_STATUS_OK:
                        PlatformSearchBean result = JSON.parseObject(jsonStr, PlatformSearchBean.class);
                        List<HandTallyBean> list = result.getList();
                        searchData.clear();
                        searchData.addAll(list);
                        showSearchResult();
                        break;
                    default:
                        this.onFailure(null);
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /** 显示搜索结果 */
    private void showSearchResult(){
        if(searchData!=null && searchData.size()>0){
            searchAdapter.updataListView(mActivity, searchData);
            mLvSearch.setVisibility(View.VISIBLE);
        }else{
            mLvSearch.setVisibility(View.GONE);
        }
    }

    /** 清空历史记录 */
    @Subscribe
    public void onPlatformChooseClearHistoryEvent(PlatformChooseClearHistoryEvent event){
        if(!isFinishing()){
            mHistoryContainer.removeAllViews();
            mHistoryContainer.setVisibility(View.GONE);
        }
    }

    /** 平台关注更新 */
    @Subscribe
    public void onIsCollectEvent(IsCollectEvent event){
        if(!isFinishing()){
            initData();
        }
    }

    /**记账选择事件 */
    @Subscribe
    public void onJiZhangChooseEvent(JiZhangChooseEvent event){
        if(!isFinishing() && event.hasChoose){
            finish();
        }
    }

    /** 模块平台选择通知事件 */
    @Subscribe
    public void onPlatformChooseEvent(PlatformChooseEvent event){
        if(!isFinishing()){
            if(event.isTally()) {
                Intent intent = new Intent(this, KeepAccountsActivity.class);
                intent.putExtra("company_name", event.getP_name());
                intent.putExtra("company_pid", event.getPid());
                setResult(RESULT_OK, intent);
            }
            finish();
            if(event.isSave() && mHistoryBlock!=null){
                mHistoryBlock.savePlatform(event.getPid(),event.getP_name());//保存数据
            }
        }
    }
}
