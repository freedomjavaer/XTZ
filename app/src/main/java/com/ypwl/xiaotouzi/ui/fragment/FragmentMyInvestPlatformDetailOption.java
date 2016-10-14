package com.ypwl.xiaotouzi.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.adapter.MyInvestPlatformDetailListAdapter;
import com.ypwl.xiaotouzi.base.BaseFragment;
import com.ypwl.xiaotouzi.bean.MyInvestPlatformDetailBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.event.AccountsKeptEvent;
import com.ypwl.xiaotouzi.event.BidDeletedEvent;
import com.ypwl.xiaotouzi.event.PlatformSyncFinishedNotificationEvent;
import com.ypwl.xiaotouzi.interf.UIAdapterListener;
import com.ypwl.xiaotouzi.ui.activity.MyInvestPlatformDetailActivity;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.ViewUtil;
import com.ypwl.xiaotouzi.view.LoadMoreListView;

/**
 * function: 我的投资-在投标的，结束投标的
 * <p/>
 * Created by lzj on 2016/3/24.
 */
public class FragmentMyInvestPlatformDetailOption extends BaseFragment implements LoadMoreListView.IListViewRefreshListener {

    private static final String PARAM_FRAGMENT_TAG = "param_fragment_tag";
    private static final String PARAM_TOPIC_PID = "param_topic_pid";
    private static final String PARAM_IS_AUTO = "param_is_auto";
    private String mCurrentFragmenType;
    private String mCurrentPid;
    private int mIsAuto;
    private View mLayoutNoDataView, mLayoutContent;
    private TextView mTotalBid, mTotalProfit, mApr;
    private TextView mTotalBidTitle, mTotalProfitTitle, mAprTitle;
    private LoadMoreListView mListview;
    private MyInvestPlatformDetailListAdapter mAdapter;
    private boolean isListViewTop = true;

    public static FragmentMyInvestPlatformDetailOption newInstance(@NonNull String fragmentType, @NonNull String pid, int isAuto) {
        FragmentMyInvestPlatformDetailOption fragment = new FragmentMyInvestPlatformDetailOption();
        Bundle args = new Bundle();
        args.putString(PARAM_FRAGMENT_TAG, fragmentType);
        args.putString(PARAM_TOPIC_PID, pid);
        args.putInt(PARAM_IS_AUTO, isAuto);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCurrentFragmenType = getArguments().getString(PARAM_FRAGMENT_TAG);
            mCurrentPid = getArguments().getString(PARAM_TOPIC_PID);
            mIsAuto = getArguments().getInt(PARAM_IS_AUTO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_myinvest_platformdetail_option, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mLayoutNoDataView = findView(view, R.id.layout_no_data_view);
        mLayoutContent = findView(view, R.id.layout_content);
        View headerViewForList = UIUtil.inflate(R.layout.fragment_myinvest_platformdetail_option_child_listview_header);
        mTotalBid = findView(headerViewForList, R.id.list_totalbid);
        mTotalProfit = findView(headerViewForList, R.id.list_totalprofit);
        mApr = findView(headerViewForList, R.id.list_apr);
        mTotalBidTitle = findView(headerViewForList, R.id.list_totalbid_title);
        mTotalProfitTitle = findView(headerViewForList, R.id.list_totalprofit_title);
        mAprTitle = findView(headerViewForList, R.id.list_apr_title);
        mListview = findView(view, R.id.listview);
        mListview.addCustomView(headerViewForList);
        mListview.setOnRefreshListener(this);
        mListview.setLoadMoreEnable(true);
        mListview.setOnScrollListener(mListViewOnScrollListener);
        mAdapter = new MyInvestPlatformDetailListAdapter(getActivity(), new MyUIAdapterListener(), mCurrentFragmenType, mCurrentPid,mIsAuto);
        mListview.setAdapter(mAdapter);

        ViewUtil.showContentLayout(Const.LAYOUT_LOADING, mLayoutNoDataView, mLayoutContent);//默认显示加载中视图
        mAdapter.asyncLoadData();
    }

    public boolean isListViewTop() {
        return isListViewTop;
    }


    /** 适配器与UI通信接口 */
    public class MyUIAdapterListener implements UIAdapterListener {

        @Override
        public void isLoading() {
        }

        @Override
        public void dataCountChanged(int count) {
            if (count == 0) {
                ViewUtil.showContentLayout(Const.LAYOUT_EMPTY, mLayoutNoDataView, mLayoutContent);
            } else if (count == Const.LOADED_ERROR) {//加载出错
                ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mLayoutNoDataView, mLayoutContent, "出错了\n点击重试", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mAdapter != null) {
                            mAdapter.asyncLoadData();
                        }
                    }
                });
            } else if (count == Const.LOADED_NO_MORE) {
                mListview.stopLoadMore();
                mListview.setDefaultText(getString(R.string.loadmore_status_nomore));
            }
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void loadFinished(int count) {
            mListview.stopLoadMore();
            MyInvestPlatformDetailBean dataBean = mAdapter.getDataBean();
            if (count > 0 && dataBean != null) {
                if (MyInvestPlatformDetailActivity.FRAGMENT_TAG_ING.equals(mCurrentFragmenType)) {//在投标的

                    mTotalBid.setTextColor(UIUtil.getColor(R.color.pd_focus_red));
                    mTotalBid.setText(dataBean.getStotal());
                    mTotalBidTitle.setText("待回总额");

                    mTotalProfit.setText(dataBean.getScapital());
                    mTotalProfitTitle.setText("待回本金");

                    mApr.setText(dataBean.getIncome());
                    mAprTitle.setText("在投标收益");

                } else {//结束投标的
                    mTotalBid.setTextColor(UIUtil.getColor(R.color.pd_focus_green));
                    mTotalBid.setText(dataBean.getMoney());
                    mTotalBidTitle.setText("投资");

                    mTotalProfit.setText(dataBean.getIncome());
                    mTotalProfitTitle.setText("收益");

                    mApr.setText(dataBean.getRate() + "%");
                    mAprTitle.setText("年化率");
                }
            }
            ViewUtil.showContentLayout(count > 0 ? Const.LAYOUT_DATA : Const.LAYOUT_EMPTY, mLayoutNoDataView, mLayoutContent);
        }
    }

    @Override
    public void onRefreshLoadMore() {
        if (mAdapter != null) {
            mAdapter.loadMore();
        }
    }

    private AbsListView.OnScrollListener mListViewOnScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            isListViewTop = firstVisibleItem == 0;
        }
    };

    @Subscribe
    public void onEventWhenBidDelete(BidDeletedEvent event) {
        requestInitData();
    }

    @Subscribe
    public void onEventWhenBidHasEditedInKeepAccount(AccountsKeptEvent event) {
        requestInitData();
    }

    @Subscribe
    public void onEventWhenPlatformSyncFinished(PlatformSyncFinishedNotificationEvent event) {
        requestInitData();
    }

    private void requestInitData() {
        if (!isDetached() && mAdapter != null) {
            mAdapter.initPage(1);
            mAdapter.asyncLoadData();
        }
    }

}
