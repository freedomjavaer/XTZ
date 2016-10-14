package com.ypwl.xiaotouzi.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.adapter.SearchPlatformListAdapter;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.bean.SearchPlatformResultBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.common.ServerStatus;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.interf.IRequestCallback;
import com.ypwl.xiaotouzi.manager.UmengEventHelper;
import com.ypwl.xiaotouzi.manager.db.NetPlatformSearchHistoryDbOpenHelper;
import com.ypwl.xiaotouzi.manager.net.NetHelper;
import com.ypwl.xiaotouzi.utils.StringUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.view.ContainsEmojiEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * function : 网贷平台搜索页面
 * <p/>
 * Created by tengtao on 2015/12/19.
 */
public class NetPlatformSearchActivity extends BaseActivity implements TextWatcher, AdapterView.OnItemClickListener {
    private ProgressBar mPbLoading;
    private ContainsEmojiEditText mEtSearch;
    private TextView mTvCancel;
    private ListView mLvSearchResult;
    private LinearLayout mLlEmptyResult;
    private SearchPlatformListAdapter mSearchAdapter;
    private boolean isLoading;//是否正在加载
    private List<SearchPlatformResultBean.DataEntity> list = new ArrayList<>();
    private NetPlatformSearchHistoryDbOpenHelper.HistoryDbHelper mPlatformSearchHistoryDbHelper;//网贷平台搜索历史数据
    private List<SearchPlatformResultBean.DataEntity> dbHistoryList;//历史记录数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);//显示输入法
        this.overridePendingTransition(R.anim.trans_activity_close_gradient_in_short, R.anim.trans_activity_close_gradient_out_short);
        setContentView(R.layout.activity_net_platform_search);
        mPlatformSearchHistoryDbHelper = NetPlatformSearchHistoryDbOpenHelper.HistoryDbHelper.getInstance(mActivity);
        initView();
        initHistoryData();
    }

    private void initView() {
        mLlEmptyResult = (LinearLayout) findViewById(R.id.ll_search_platform_empty_info);
        mEtSearch = (ContainsEmojiEditText) findViewById(R.id.platform_search);
        mEtSearch.addTextChangedListener(this);
        mTvCancel = (TextView) findViewById(R.id.tv_net_platform_search_cancel);
        mLvSearchResult = (ListView) findViewById(R.id.lv_net_platform_search_result);
        mLvSearchResult.setDividerHeight(0);
        mSearchAdapter = new SearchPlatformListAdapter(this);
        mLvSearchResult.setAdapter(mSearchAdapter);
        mPbLoading = (ProgressBar) findViewById(R.id.layout_hint_pb_loading);
        mLvSearchResult.setOnItemClickListener(this);
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);//隐藏输入法
                finish();
            }
        });
    }

    /** 初始化历史记录数据 */
    private void initHistoryData() {
        dbHistoryList = mPlatformSearchHistoryDbHelper.queryAll();
        mSearchAdapter.loadData(dbHistoryList);
    }


    /** 搜索文本框输入监听 */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (StringUtil.isEmptyOrNull(s.toString())) {
            mSearchAdapter.loadData(dbHistoryList);//不输入搜索字段，显示历史记录
            return;
        }
        String ss = s.toString();
        if (mEtSearch.containsEmoji(ss.substring(ss.length() - count, ss.length()))) {
            return;
        }

        String url = StringUtil.format(URLConstant.SEARCH_PLATFORM, s.toString());
        NetHelper.get(url, new SearchDataCallBack());
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    /** 搜索结果点击事件 */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (isLoading) {
            return;
        }
        String s = mEtSearch.getText().toString().trim();
        Intent intent = new Intent(mActivity, com.ypwl.xiaotouzi.ui.activity.PlatformDetailActivity.class);
        Bundle bundle = new Bundle();
        String pid = "";
        String p_name = "";
        if (list.size() > 0 && s.length() > 0) {
            pid = list.get(position).getPid();
            p_name = list.get(position).getP_name();
            //保存历史记录数据库
            saveData2Db(pid, p_name);
            //统计关键字
            HashMap<String, String> map = new HashMap<>();
            map.put("keyWord", p_name);
            UmengEventHelper.onEventMap("SearchPlatformKeyWord", map);
        } else {
            pid = dbHistoryList.get(position).getPid();
            p_name = dbHistoryList.get(position).getP_name();
        }
        bundle.putString("pid", pid);
        bundle.putString("p_name", p_name);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    /** 搜索获取数据回调接口 */
    private class SearchDataCallBack extends IRequestCallback<String> {
        @Override
        public void onStart() {
            mPbLoading.setVisibility(View.VISIBLE);
            isLoading = true;
        }

        @Override
        public void onFailure(Exception e) {
            mPbLoading.setVisibility(View.GONE);
            isLoading = false;
            UIUtil.showToastShort("获取数据失败");
        }

        @Override
        public void onSuccess(String jsonStr) {
            mPbLoading.setVisibility(View.GONE);
            isLoading = false;
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);
                int status = jsonObject.optInt(Const.JSON_KEY_status, -1);
                switch (status) {
                    case ServerStatus.SERVER_STATUS_OK:
                        SearchPlatformResultBean bean = JSON.parseObject(jsonStr, SearchPlatformResultBean.class);
                        if (bean == null || bean.getData() == null) {
                            this.onFailure(null);
                            return;
                        }
                        mLlEmptyResult.setVisibility(bean.getData().size() > 0 ? View.GONE : View.VISIBLE);
                        if (bean.getData().size() == 0) {
                            UIUtil.showToastShort("未搜索到相关数据");
                            return;
                        }
                        list.clear();
                        list.addAll(bean.getData());
                        mSearchAdapter.loadData(bean.getData());
                        break;
                    default:
                        this.onFailure(null);
                        break;
                }
            } catch (JSONException e) {
                this.onFailure(e);
            }
        }
    }

    /**
     * 保存网贷平台搜索历史记录
     *
     * @param pid    平台id
     * @param p_name 平台名称
     */
    private void saveData2Db(String pid, String p_name) {
        if (mPlatformSearchHistoryDbHelper.queryByPid(pid)) {//包含就删除
            mPlatformSearchHistoryDbHelper.deleteByPid(pid);
        }
        if (dbHistoryList.size() == 15) {//总量保持15条
            mPlatformSearchHistoryDbHelper.deleteByPid(dbHistoryList.get(14).getPid());//删除时间最远的一条
        }
        mPlatformSearchHistoryDbHelper.insert(pid, p_name, System.currentTimeMillis() + "");
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.trans_activity_close_gradient_in_short, R.anim.trans_activity_close_gradient_out_short);
    }
}
