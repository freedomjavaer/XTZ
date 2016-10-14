package com.ypwl.xiaotouzi.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.squareup.otto.Subscribe;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.adapter.ContinualTallyAdapter;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.bean.ContinualTallyForFilterBean;
import com.ypwl.xiaotouzi.bean.ContinualTallyListBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.common.ServerStatus;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.event.AccountsKeptEvent;
import com.ypwl.xiaotouzi.event.AutoTallyPlatformRefreshEvent;
import com.ypwl.xiaotouzi.event.ContinualTallyForFilterEvent;
import com.ypwl.xiaotouzi.interf.IRequestCallback;
import com.ypwl.xiaotouzi.manager.net.NetHelper;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.ViewUtil;
import com.ypwl.xiaotouzi.view.LoadMoreListView;

import java.util.List;

/**
 * 项目名:	XTZ
 * 包名:	com.ypwl.xiaotouzi.ui.activity
 * 类名:	ContinualTallyActivity
 * 作者:	罗霄
 * 创建时间:	2016/4/11 11:49
 * <p/>
 * 描述:	流水资产页面
 * <p/>
 * svn版本:	$Revision: 16041 $
 * 更新人:	$Author: pengdakai $
 * 更新时间:	$Date: 2016-09-26 17:02:46 +0800 (周一, 26 九月 2016) $
 * 更新描述:
 */
public class ContinualTallyActivity extends BaseActivity implements View.OnClickListener, LoadMoreListView.IListViewRefreshListener, AdapterView.OnItemClickListener {

    private LinearLayout mLlBack;
    private LoadMoreListView mLvLoadMore;
    private TextView mTvTitleLeft;
    private TextView mTvTitleContent;
    private TextView mTvTitleRight;
    private String mLastPageName;
    private ContinualTallyAdapter continualTallyAdapter;
    private View mNoDataView;
    private ContinualTallyForFilterBean mBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_continual_tally);

        mLastPageName = getIntent().getStringExtra(Const.KEY_INTENT_JUMP_FROM_DATA);

        initView();

        initListener();

        initData();

    }

    /** 填充数据 */
    private void fillData(List<ContinualTallyListBean> mData, final boolean isAdd) {
        if (null != mData && mData.size() != 0) {
            continualTallyAdapter.notifyDataSetChanged(mData, isAdd);
            mLvLoadMore.setLoadMoreEnable(mData.size() >= 10);

            ViewUtil.showContentLayout(Const.LAYOUT_DATA, mNoDataView, mLvLoadMore);
        } else {
            ViewUtil.showContentLayout(Const.LAYOUT_EMPTY, mNoDataView, mLvLoadMore, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getData(isAdd);
                }
            });
        }
        /** 设置筛选按钮可编辑状态 */
        //mTvTitleRight.setEnabled(null != mData && mData.size() > 0);
    }

    /** 获取数据 */
    private void getData(final boolean isAdd) {

        String url = String.format(URLConstant.CONTINUAL_TALLY, mBean.getToken(), mBean.getAll(), mBean.getInvest(), mBean.getAll_return(), mBean.getLast_return(), mBean.getStarttime().equalsIgnoreCase("-1") ? "0" : mBean.getStarttime(), TextUtils.isEmpty(mBean.getEndtime()) ? String.valueOf(System.currentTimeMillis() / 1000) : mBean.getEndtime(), mBean.getPid());

        NetHelper.get(url, new IRequestCallback<String>() {
                    @Override
                    public void onStart() {
                        ViewUtil.showContentLayout(Const.LAYOUT_LOADING, mNoDataView, mLvLoadMore);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mNoDataView, mLvLoadMore, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getData(isAdd);
                            }
                        });
                    }

                    @Override
                    public void onSuccess(String jsonStr) {
                        ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mNoDataView, mLvLoadMore, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getData(isAdd);
                            }
                        });
                        try {
                            JSONObject jsonObject = JSON.parseObject(jsonStr);
                            int status = jsonObject.getIntValue(Const.JSON_KEY_status);
                            String ret_msg = jsonObject.getString("ret_msg");

                            switch (status) {
                                case ServerStatus.SERVER_STATUS_OK:

                                    fillData(JSON.parseArray(jsonObject.getString("list"), ContinualTallyListBean.class), isAdd);

                                    break;
                                default:
                                    UIUtil.showToastShort("" + ret_msg);
                                    break;
                            }

                        } catch (Exception e) {
                            this.onFailure(e);
                        }
                    }
                }

        );

    }

    private void initListener() {
        mLlBack.setOnClickListener(this);
        mTvTitleRight.setOnClickListener(this);
        mLvLoadMore.setOnRefreshListener(this);

        mLvLoadMore.setOnItemClickListener(this);
    }

    private void initData() {

        if (!TextUtils.isEmpty(mLastPageName)) {
            mTvTitleLeft.setText(mLastPageName);
        }

        mTvTitleContent.setText(UIUtil.getString(R.string.continual_tally_title_name));
        mTvTitleRight.setText(UIUtil.getString(R.string.continual_tally_title_right));

        continualTallyAdapter = new ContinualTallyAdapter(mActivity);

        mLvLoadMore.setAdapter(continualTallyAdapter);

        mBean = new ContinualTallyForFilterBean();
        getData(false);
    }

    private void initView() {
        mLlBack = findView(R.id.layout_title_back);
        mTvTitleLeft = findView(R.id.tv_title_back);
        mTvTitleContent = findView(R.id.tv_title);
        mTvTitleRight = findView(R.id.tv_title_txt_right);
        mTvTitleRight.setVisibility(View.VISIBLE);

        mLvLoadMore = findView(R.id.activity_continual_tally_lv_loadmorelistview);

        mNoDataView = findView(R.id.layout_no_data_view);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_title_back:
                finish();
                break;
            case R.id.tv_title_txt_right:
                Intent intent = new Intent(this, ContinualTallyForFilterActivity.class);
                intent.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA, mBean);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onRefreshLoadMore() {
        mLvLoadMore.stopLoadMore();
        mLvLoadMore.setDefaultText(getString(R.string.loadmore_status_nomore));
    }

    @Subscribe
    public void onRefreshFilter(ContinualTallyForFilterEvent event) {
        if (!isFinishing() && null != event && null != event.mBean) {
            this.mBean = event.mBean;
            getData(false);
        }
    }

    /** 编辑保存标的 */
    @Subscribe
    public void onAccountsKeptEvent(AccountsKeptEvent event) {
        if (!isFinishing() && null != event) {
            getData(false);
        }
    }

    /** 平台更新事件 */
    @Subscribe
    public void onAutoTallyPlatformRefreshEvent(AutoTallyPlatformRefreshEvent event){
        if (event != null && !isFinishing()) {
            getData(false);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra(Const.KEY_INTENT_JUMP_FROM_DATA, UIUtil.getString(R.string.continual_tally_title_name));
        intent.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA, continualTallyAdapter.getPositionBean(position - 1).getAid());
        intent.setClass(mActivity, LlcBidDetailActivity.class);
        startActivity(intent);
    }
}
