package com.ypwl.xiaotouzi.ui.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.andview.refreshview.XRefreshView;
import com.squareup.otto.Subscribe;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.adapter.AssetsListViewAdapter;
import com.ypwl.xiaotouzi.adapter.AssetsViewPagerAdapter;
import com.ypwl.xiaotouzi.base.BaseFragment;
import com.ypwl.xiaotouzi.bean.InvestAssetsBean;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.event.AccountsKeptEvent;
import com.ypwl.xiaotouzi.event.BidDeletedEvent;
import com.ypwl.xiaotouzi.event.BidStatusChangeEvent;
import com.ypwl.xiaotouzi.event.BindAccountEvent;
import com.ypwl.xiaotouzi.event.ExitOutEvent;
import com.ypwl.xiaotouzi.event.InvestPlatformRefreshEvent;
import com.ypwl.xiaotouzi.event.LoginStateEvent;
import com.ypwl.xiaotouzi.interf.IRequestCallback;
import com.ypwl.xiaotouzi.manager.net.NetHelper;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.LogUtil;
import com.ypwl.xiaotouzi.utils.StringUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.Util;
import com.ypwl.xiaotouzi.view.CustomSwipeToRefresh;
import com.ypwl.xiaotouzi.view.CustomXRefreshHeaderView;
import com.ypwl.xiaotouzi.view.dialog.lib.KProgressHUD;
import com.ypwl.xiaotouzi.view.stickylistheaders.ExpandableStickyListHeadersListView;
import com.ypwl.xiaotouzi.view.stickylistheaders.StickyListHeadersListView;

import java.util.List;

/**
 * function : 我的投资--资产
 * <p/>
 * Created by tengtao on 2016/3/22.
 */
public class FragmentInvestAssets extends BaseFragment {

    private ExpandableStickyListHeadersListView mAssetsListViewData;
    private ViewPager mVPAssetHeaderContent;
    private LinearLayout mPointContainer;
    private XRefreshView mSwipeRefresh;
    private int currHightPosition;
    private LinearLayout mNoDataView;
    private KProgressHUD kProgressHUD;
    private AssetsListViewAdapter mListViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_invest_assets, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        mSwipeRefresh = (CustomSwipeToRefresh) view.findViewById(R.id.srl_invest_assets);
//        mSwipeRefresh.setOnRefreshListener(this);
        mSwipeRefresh = findView(view, R.id.srl_invest_assets);
        mSwipeRefresh.setMoveForHorizontal(true);
        mSwipeRefresh.setCustomHeaderView(new CustomXRefreshHeaderView(getContext()));
        mSwipeRefresh.setPullLoadEnable(false);
        mSwipeRefresh.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh() {

                if (Util.legalLogin() == null) {
                    mSwipeRefresh.stopRefresh();
                    return;
                }
                initData();
            }

        });

        //listview
        mAssetsListViewData = (ExpandableStickyListHeadersListView) view.findViewById(R.id.invest_assets_content);
        //header
        View headerView = LayoutInflater.from(getContext()).inflate(R.layout.header_invest_assest, null);
        mVPAssetHeaderContent = (ViewPager) headerView.findViewById(R.id.vp_assets_header_content);
        mPointContainer = (LinearLayout) headerView.findViewById(R.id.ll_invest_assets_point_container);
        mNoDataView = (LinearLayout) headerView.findViewById(R.id.ll_no_data_view);

        mAssetsListViewData.addHeaderView(headerView);
        kProgressHUD = KProgressHUD.create(getContext());
        kProgressHUD.show();
        initData();
    }

    private void initData() {
        //获取数据
        String url = StringUtil.format(URLConstant.INVEST_ASSESTS, GlobalUtils.token);
        NetHelper.get(url, mCallBack);
    }

    IRequestCallback mCallBack = new IRequestCallback<InvestAssetsBean>() {

        @Override
        public void onStart() {
        }

        @Override
        public void onFailure(Exception e) {
            mSwipeRefresh.stopRefresh();
            kProgressHUD.dismiss();
        }

        @Override
        public void onSuccess(final InvestAssetsBean bean) {
            mSwipeRefresh.stopRefresh();
            mSwipeRefresh.setEnabled(false);
            UIUtil.postDelayed(new Runnable() {
                @Override
                public void run() {
                    List<InvestAssetsBean.ListEntity> list = bean.getList();
                    if (list != null && list.size() > 0) {
                        refreshData(bean);
                        mNoDataView.setVisibility(View.GONE);
                    } else {
                        mNoDataView.setVisibility(View.VISIBLE);
                        refreshData(bean);
                    }
                    kProgressHUD.dismiss();
                    mSwipeRefresh.setEnabled(true);
                }
            }, mSwipeRefresh.getScrollDuring() + 50);

        }
    };

    /** 刷新数据 */
    private void refreshData(InvestAssetsBean bean) {
        //viewpager
        initViewPagerPoint();
        AssetsViewPagerAdapter mViewPagerAdapter = new AssetsViewPagerAdapter(getContext(), bean);
        mVPAssetHeaderContent.setAdapter(mViewPagerAdapter);
        mVPAssetHeaderContent.addOnPageChangeListener(mViewPagerChangeListener);

        //listview
        mListViewAdapter = new AssetsListViewAdapter(getContext(), getActivity(), bean);
        mAssetsListViewData.setAdapter(mListViewAdapter);

        mAssetsListViewData.setBeginHideAll(true);//取反

        mAssetsListViewData.setOnHeaderClickListener(new StickyListHeadersListView.OnHeaderClickListener() {
            @Override
            public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {
                //获取第position个在listview中的位置
                int positionForSection = mListViewAdapter.getPositionForSection((int) headerId);
                if (currHightPosition != headerId) {//新点击的header--->先关闭原来的
                    mAssetsListViewData.collapse(currHightPosition);
                    mAssetsListViewData.expand(headerId);
                    mListViewAdapter.changeShowStatus(positionForSection, 1);
                } else {
                    if (mAssetsListViewData.isHeaderCollapsed(headerId)) {
                        mAssetsListViewData.expand(headerId);
                        mListViewAdapter.changeShowStatus(positionForSection, 1);
                    } else {
                        mAssetsListViewData.collapse(headerId);
                        mListViewAdapter.changeShowStatus(positionForSection, 0);
                    }
                }
                currHightPosition = (int) headerId;
                //list选择
                mAssetsListViewData.setSelection(positionForSection);
            }
        });

    }


    /** 初始化ViewPager的点 */
    private void initViewPagerPoint() {
        mPointContainer.removeAllViews();
        for (int i = 0; i < 3; i++) {//添加ViewPager里的点
            View v_point = new View(getActivity());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(UIUtil.dip2px(7), UIUtil.dip2px(7));
            if (i == 0) {
                v_point.setBackgroundResource(R.drawable.invest_assets_point_choosed);
            } else {
                lp.leftMargin = UIUtil.dip2px(10);//设置点之间的间距
                v_point.setBackgroundResource(R.drawable.invest_analyze_point_unchoosed);
            }
            v_point.setLayoutParams(lp);

            mPointContainer.addView(v_point);
        }
    }

    private ViewPager.OnPageChangeListener mViewPagerChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            //动态改变点的颜色
            for (int i = 0; i < mPointContainer.getChildCount(); i++) {
                if (i == position % 4) {
                    mPointContainer.getChildAt(i).setBackgroundResource(R.drawable.invest_assets_point_choosed);
                } else {
                    mPointContainer.getChildAt(i).setBackgroundResource(R.drawable.invest_analyze_point_unchoosed);
                }
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }

    };

    /** 删除标接收的消息 */
    @Subscribe
    public void onDeleteBid(BidDeletedEvent event) {
        if (event != null && !getActivity().isFinishing()) {
            initData();
        }
    }

    /** 平台绑定成功的消息 */
    @Subscribe
    public void onBindPlatformEvent(BindAccountEvent event) {
        if (event != null && !getActivity().isFinishing()) {
            initData();
        }
    }

    /** 平台解除成功的消息 */
    @Subscribe
    public void onCancelPlatformEvent(InvestPlatformRefreshEvent event) {
        if (event != null && !getActivity().isFinishing()) {
            initData();
        }
    }

    /** 增加标接收的消息 */
    @Subscribe
    public void onAddBidEvent(AccountsKeptEvent event) {
        if (event != null && !getActivity().isFinishing()) {
            initData();
        }
    }

    /** 登录成功 */
    @Subscribe
    public void onLoginStateEvent(LoginStateEvent event) {
        if (event != null && !getActivity().isFinishing() && event.hasLogin) {
            initData();
        }
    }

    @Subscribe
    public void onBidStatusChangeEvent(BidStatusChangeEvent event) {
        if (event != null && !getActivity().isFinishing()) {
            initData();
        }
    }

    @Subscribe
    public void onBidStatusChangeEvent(BidDeletedEvent event) {
        if (event != null && !getActivity().isFinishing()) {
            initData();
        }
    }

    /** 用户退出登录状态 */
    @Subscribe
    public void onExitOutEvent(ExitOutEvent event) {
        if (null != event) {
            LogUtil.e(TAG, "成功退出");
            mListViewAdapter.listData.clear();
            mListViewAdapter.notifyDataSetChanged();
        }
    }


//    @Override
//    public void onRefresh() {
//        if (Util.legalLogin() == null) {
//            mSwipeRefresh.setRefreshing(false);
//            return;
//        }
//        mSwipeRefresh.setRefreshing(true);
//        initData();
//    }

}
