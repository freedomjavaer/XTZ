package com.ypwl.xiaotouzi.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.adapter.PlatformTargetsAdapter;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.bean.BannerBean;
import com.ypwl.xiaotouzi.bean.PlatformTargetsBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.common.ServerStatus;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.common.UmengEventID;
import com.ypwl.xiaotouzi.interf.IRequestCallback;
import com.ypwl.xiaotouzi.manager.BannerHelper;
import com.ypwl.xiaotouzi.manager.JsonHelper;
import com.ypwl.xiaotouzi.manager.UmengEventHelper;
import com.ypwl.xiaotouzi.manager.net.NetHelper;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.LogUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.Util;
import com.ypwl.xiaotouzi.utils.ViewUtil;
import com.ypwl.xiaotouzi.view.CustomXRefreshHeaderView;
import com.ypwl.xiaotouzi.view.LoadMoreListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名:	XTZ
 * 包名:	com.ypwl.xiaotouzi.ui.activity
 * 类名:	FinanceSupermarketOfPlatformTargets
 * 作者:	罗霄
 * 创建时间:	2016/4/19 15:36
 * <p/>
 * 描述:	金融超市 -- 平台标的列表页面
 * <p/>
 * svn版本:	$Revision: 16041 $
 * 更新人:	$Author: pengdakai $
 * 更新时间:	$Date: 2016-09-26 17:02:46 +0800 (周一, 26 九月 2016) $
 * 更新描述:	$Message$
 */
public class FinanceSupermarketOfPlatformTargetsActivity extends BaseActivity implements LoadMoreListView.IListViewRefreshListener, AdapterView.OnItemClickListener, View.OnClickListener {

    /**
     * 轮播图
     */
    private BannerHelper mBannerHelper;

    private LinearLayout mLlBack;
    private TextView mTvTitleLeft;
    private TextView mTvTitleContent;
    private TextView mTvTitleRight;
    private XRefreshView mSwipeToRefresh;
    private LoadMoreListView mLvLoadMore;
    private View mNoDataView;
    private String pid;
    private String mLastPageName;
    private String p_name;

    private PlatformTargetsAdapter platformTargetsAdapter;
    private String url;

    private int mPage = 1;
    //一页20条数据
    private final int mDefItemCount = 20;
    private Intent intent;
    private View mHeaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_finance_supermarket_of_platform_targets);

        p_name = getIntent().getStringExtra(Const.KEY_INTENT_JUMP_BASE_DATA + "name");
        pid = getIntent().getStringExtra(Const.KEY_INTENT_JUMP_BASE_DATA);
        mLastPageName = getIntent().getStringExtra(Const.KEY_INTENT_JUMP_FROM_DATA);

        initView();

        initListener();

        initData();
    }

    /**
     * 初始化banner
     */
    public void onInitBanner(List<PlatformTargetsBean.BannerEntity> bannerList) {
        //LogUtil.e(TAG,bannerList.get(0).getUrl());
        if (null == bannerList || bannerList.size() == 0) {
            mHeaderView.setVisibility(View.GONE);
            return;
        }
        mHeaderView.setVisibility(View.VISIBLE);
        List<BannerBean> dataList = new ArrayList<>();
        for (int i = 0; i < bannerList.size(); i++) {
            dataList.add(new BannerBean(R.mipmap.pic_021, bannerList.get(i).getImage(), "", bannerList.get(i).getUrl(),""));
        }
        mBannerHelper.startBanner(dataList, new BannerHelper.OnItemClickListener() {
            @Override
            public void onItemClick(BannerBean bean) {
                if (Util.legalLogin() == null) {
                    startActivity(LoginActivity.class);
                    return;
                }

                if(!TextUtils.isEmpty(bean.getDetailurl())){
                    LogUtil.e(TAG,TextUtils.isEmpty(bean.getDetailurl()));
                    intent = new Intent(mActivity, XtzBannerDetailActivity.class);
                    intent.putExtra("title", bean.getDesc());
                    intent.putExtra("url", bean.getDetailurl().startsWith("http://") ? bean.getDetailurl() : "http://" + bean.getDetailurl());
                    startActivity(intent);
                }

            }
        });
    }

    private void initView() {
        /** 初始化轮播图 */
        mBannerHelper = BannerHelper.getInstance(mActivity);
        mHeaderView = View.inflate(mActivity, R.layout.layout_banner, null);
        mBannerHelper.init(mHeaderView);

        mLlBack = findView(R.id.layout_title_back);
        mTvTitleLeft = findView(R.id.tv_title_back);
        mTvTitleContent = findView(R.id.tv_title);
        mTvTitleRight = findView(R.id.tv_title_txt_right);

        mSwipeToRefresh = findView(R.id.activity_finance_supermarket_of_platform_targets_swipe_refresh);
        mSwipeToRefresh.setMoveForHorizontal(true);
        mSwipeToRefresh.setCustomHeaderView(createHeaderView());
        mSwipeToRefresh.setPullLoadEnable(false);
        mSwipeToRefresh.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh() {
                mPage = 1;
                getData(mPage, false);
                UIUtil.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeToRefresh.stopRefresh();
                    }
                }, 10 * 1000);

            }

        });

        mLvLoadMore = findView(R.id.activity_finance_supermarket_of_platform_targets_lv_loadmorelistview);
        mNoDataView = findView(R.id.layout_no_data_view);

        mLvLoadMore.addCustomView(mHeaderView);
    }

    private void initData() {

        mTvTitleLeft.setText(TextUtils.isEmpty(mLastPageName) ? "" : mLastPageName);
        mTvTitleContent.setText(TextUtils.isEmpty(p_name) ? "" : p_name);
        mTvTitleRight.setText(UIUtil.getString(R.string.finance_supermarket_target_list_title_right));
        mTvTitleRight.setVisibility(View.VISIBLE);


        platformTargetsAdapter = new PlatformTargetsAdapter(mActivity);
        mLvLoadMore.setAdapter(platformTargetsAdapter);

        ViewUtil.showContentLayout(Const.LAYOUT_LOADING, mNoDataView, mLvLoadMore);
        getData(mPage, false);
    }

    /**
     * 填充数据
     */
    private void fillData(List<PlatformTargetsBean.ListEntity> mData, List<PlatformTargetsBean.BannerEntity> banner, final int page, final boolean isAdd) {

        if (1 == mPage) {
            onInitBanner(banner);
        }

        if (null != mData ) {

            if (TextUtils.isEmpty(p_name)){
                p_name = mData.get(0).getP_name();
                mTvTitleContent.setText(TextUtils.isEmpty(p_name) ? "" : p_name);
            }
            platformTargetsAdapter.notifyDataSetChanged(mData, isAdd);
            mLvLoadMore.setLoadMoreEnable(mData.size() >= mDefItemCount);
            ViewUtil.showContentLayout(Const.LAYOUT_DATA, mNoDataView, mLvLoadMore);
        } else {
            if (1 == page) {
                ViewUtil.showContentLayout(Const.LAYOUT_EMPTY, mNoDataView, mLvLoadMore, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getData(page, isAdd);
                    }
                });
            }
        }
    }

    /**
     * 获取数据
     */
    private void getData(final int page, final boolean isAdd) {

        url = String.format(URLConstant.FINANCE_PLATFORM_TARGET_LIST, pid, page, GlobalUtils.token);

        NetHelper.get(url, new IRequestCallback<String>() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onFailure(Exception e) {
                        mLvLoadMore.stopLoadMore();
                        mSwipeToRefresh.stopRefresh();
                        ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mNoDataView, mLvLoadMore, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getData(page, isAdd);
                            }
                        });
                    }

                    @Override
                    public void onSuccess(String jsonStr) {
                        mLvLoadMore.stopLoadMore();
                        mSwipeToRefresh.stopRefresh();
                        try {
                            PlatformTargetsBean platformTargetsBean = JsonHelper.parseObject(jsonStr, PlatformTargetsBean.class);

                            switch (platformTargetsBean.getStatus()) {
                                case ServerStatus.SERVER_STATUS_OK:

                                    List<PlatformTargetsBean.ListEntity> list = platformTargetsBean.getList();

                                    fillData(list, platformTargetsBean.getBanner(), page, isAdd);
                                    if (null != list && list.size() == mDefItemCount) {
                                        mPage++;
                                    } else {
                                        mLvLoadMore.setDefaultText(getString(R.string.loadmore_status_nomore));
                                    }

                                    break;
                                default:
                                    UIUtil.showToastShort("" + platformTargetsBean.getRet_msg());
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
        mTvTitleRight.setOnClickListener(this);
        mLlBack.setOnClickListener(this);
        mLvLoadMore.setOnRefreshListener(this);
        mLvLoadMore.setOnItemClickListener(this);
//        mSwipeToRefresh.setOnRefreshListener(this);
    }

    @Override
    public void onRefreshLoadMore() {
        getData(mPage, true);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PlatformTargetsBean.ListEntity item = (PlatformTargetsBean.ListEntity) platformTargetsAdapter.getItem(position - 1);

        intent = new Intent();
        intent.putExtra(Const.KEY_INTENT_JUMP_FROM_DATA, p_name);
        intent.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA + "p_name", item.getP_name());
        intent.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA + "pid", pid);
        intent.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA, item.getProject_id());
        intent.setClass(mActivity, FinanceSupermarketOfTargetDetailActivity.class);
        startActivity(intent);
        UmengEventHelper.onEvent(UmengEventID.FsmPlatformBidDetail);
    }

//    @Override
//    public void onRefresh() {
//        mPage = 1;
//        mSwipeToRefresh.setRefreshing(true);
//        getData(mPage, false);
//        UIUtil.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mSwipeToRefresh.setRefreshing(false);
//            }
//        }, 10 * 1000);
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_title_back:
                finish();
                break;
            case R.id.tv_title_txt_right:
                intent = new Intent();
                intent.putExtra(Const.KEY_INTENT_JUMP_FROM_DATA, p_name);
                intent.putExtra("p_name", p_name);
                intent.putExtra("pid", pid);
                intent.setClass(mActivity, PlatformDetailActivity.class);
                startActivity(intent);
                break;
        }
    }

    private View createHeaderView() {
        return new CustomXRefreshHeaderView(this);
    }
}
