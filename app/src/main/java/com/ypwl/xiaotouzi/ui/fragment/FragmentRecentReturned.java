package com.ypwl.xiaotouzi.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.squareup.otto.Subscribe;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.adapter.RecentBackMoneyAdapter;
import com.ypwl.xiaotouzi.base.BaseFragment;
import com.ypwl.xiaotouzi.bean.CommonBean;
import com.ypwl.xiaotouzi.bean.RecentBackMoneyBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.common.UmengEventID;
import com.ypwl.xiaotouzi.event.AccountsKeptEvent;
import com.ypwl.xiaotouzi.event.BidDeletedEvent;
import com.ypwl.xiaotouzi.event.BidStatusChangeEvent;
import com.ypwl.xiaotouzi.event.BindAccountEvent;
import com.ypwl.xiaotouzi.event.InvestPlatformRefreshEvent;
import com.ypwl.xiaotouzi.event.LoginStateEvent;
import com.ypwl.xiaotouzi.interf.INetRequestListener;
import com.ypwl.xiaotouzi.manager.EventHelper;
import com.ypwl.xiaotouzi.manager.InvestStatusChangeHelper;
import com.ypwl.xiaotouzi.manager.UmengEventHelper;
import com.ypwl.xiaotouzi.netprotocol.ChangeReturnedStatusProtocol;
import com.ypwl.xiaotouzi.netprotocol.RecentBackMoneyProtocol;
import com.ypwl.xiaotouzi.ui.activity.AllBackMoneyActivity;
import com.ypwl.xiaotouzi.utils.NetworkUtils;
import com.ypwl.xiaotouzi.utils.PopuViewUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.Util;
import com.ypwl.xiaotouzi.utils.ViewUtil;
import com.ypwl.xiaotouzi.view.CustomXRefreshHeaderView;
import com.ypwl.xiaotouzi.view.dialog.lib.KProgressHUD;
import com.ypwl.xiaotouzi.view.stickylistheaders.StickyListHeadersListView;

import java.util.List;

/**
 * function : 我的投资--回款
 * <p/>
 * Created by tengtao on 2016/3/22.
 */
public class FragmentRecentReturned extends BaseFragment implements View.OnClickListener, RecentBackMoneyAdapter.IChangeStatusListener {

    private XRefreshView mContentLayout;
    private TextView mTvTotalMoney,mTvTotalPoint;//待回总额小数点前后两个部分
    private LinearLayout mTvHeaderAllReturned;
    private RecentBackMoneyProtocol mBackMoneyProtocol;
    private View view;
    private LinearLayout mViewStubEmpty;//空数据视图
    private RecentBackMoneyAdapter mBackMoneyAdapter;
    private StickyListHeadersListView mListView;

    private String currRid;//当前回款id
    private String currStatus;//当前状态
    private KProgressHUD mDialogLoading;
    private int position;
    private String totalMoney;//待回总额
    private boolean isStatusChange = true;
    private LinearLayout mLlBackOptionSelect;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_invest_returned_money, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mDialogLoading = KProgressHUD.create(getActivity());
//        mContentLayout = (SwipeRefreshLayout) view.findViewById(R.id.layout_content_container);
//        mContentLayout.setOnRefreshListener(this);
        mContentLayout = findView(view, R.id.layout_content_container);
        mContentLayout.setMoveForHorizontal(true);
        mContentLayout.setCustomHeaderView(new CustomXRefreshHeaderView(getContext()));
        mContentLayout.setPullLoadEnable(false);
        mContentLayout.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh() {

                if (Util.legalLogin() == null) {
                    mContentLayout.stopRefresh();
                    return;
                }
                mBackMoneyProtocol.loadData(mINetRequestListener, Const.REQUEST_GET);
                UIUtil.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mContentLayout.stopRefresh();
                    }
                }, 1000 * 10);

            }

        });


        //滚动顶部条
        mLlBackOptionSelect = (LinearLayout) view.findViewById(R.id.ll_back_option_select);
        view.findViewById(R.id.ll_recent_all_back).setOnClickListener(this);
        //空数据试图
        mViewStubEmpty = (LinearLayout) view.findViewById(R.id.viewstub_empty_data);
        mViewStubEmpty.setVisibility(View.GONE);
        mViewStubEmpty.findViewById(R.id.ll_invest_header_all_returned).setOnClickListener(this);
        //数据试图
        mListView = (StickyListHeadersListView) view.findViewById(R.id.invest_recent_returned_list);
        View headerView = View.inflate(getActivity(), R.layout.layout_invest_recent_back_list_header_view, null);
        mTvTotalMoney = (TextView) headerView.findViewById(R.id.tv_invest_returned_money_total);
        mTvTotalPoint = (TextView) headerView.findViewById(R.id.tv_invest_returned_money_point);
        mTvHeaderAllReturned = (LinearLayout) headerView.findViewById(R.id.ll_invest_header_all_returned);
        mTvHeaderAllReturned.setOnClickListener(this);
        mListView.addHeaderView(headerView);

        mBackMoneyAdapter = new RecentBackMoneyAdapter(getActivity());
        mBackMoneyAdapter.setIChangeStatusListener(this);
        mListView.setAdapter(mBackMoneyAdapter);
        mListView.setOnScrollListener(mOnScrollListener);

        /** 设置header显示的偏移量 */
        mListView.setStickyHeaderTopOffset(UIUtil.dip2px(35));
        /** 设置header显示状态的监听，并做相应的操作 */
        mListView.setOnHeaderShowStateListener(new StickyListHeadersListView.OnHeaderShowStateListener() {
            @Override
            public void OnHeaderShowState(boolean mIsShow) {
                mLlBackOptionSelect.setVisibility(mIsShow ? View.VISIBLE : View.GONE);
            }
        });

        mBackMoneyProtocol = new RecentBackMoneyProtocol();
        mBackMoneyProtocol.loadData(mINetRequestListener, Const.REQUEST_GET);

        ViewUtil.showContentLayout(Const.LAYOUT_LOADING, mViewStubEmpty, mContentLayout);
    }

    /** 请求数据 */
    private INetRequestListener mINetRequestListener = new INetRequestListener<RecentBackMoneyBean>() {
        @Override
        public void netRequestCompleted() {
            mContentLayout.stopRefresh();
            if(!NetworkUtils.isNetworkConnected(UIUtil.getContext())){
                ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mViewStubEmpty, mContentLayout, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBackMoneyProtocol.loadData(mINetRequestListener, Const.REQUEST_GET);
                    }
                });
            }
        }

        @Override
        public void netRequestSuccess(RecentBackMoneyBean bean, boolean isSuccess) {
            if(bean!=null && isSuccess){
                totalMoney = bean.getStotal();
                if(totalMoney.contains(".")){
                    String[] split = totalMoney.split("\\.");
                    mTvTotalMoney.setText(split[0]);
                    mTvTotalPoint.setText("."+split[1]);
                    mTvTotalMoney.setTextColor(getResources().getColor(R.color.finance_supermarket_yellow));
                    mTvTotalPoint.setTextColor(getResources().getColor(R.color.finance_supermarket_yellow));
                }else{
                    mTvTotalMoney.setText(totalMoney);
                    mTvTotalPoint.setText(".00");
                    mTvTotalMoney.setTextColor(getResources().getColor(R.color.finance_supermarket_yellow));
                    mTvTotalPoint.setTextColor(getResources().getColor(R.color.finance_supermarket_yellow));
                }
                if(bean.getList().size()==0){
                    showEmptyData();//显示空数据试图
                }else{
                    showDataList(bean.getList());//显示数据试图
                }
            }else{
                ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mViewStubEmpty, mContentLayout, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBackMoneyProtocol.loadData(mINetRequestListener, Const.REQUEST_GET);
                    }
                });
            }
        }
    };

    /** 没有近期回款 */
    private void showEmptyData(){
        mViewStubEmpty.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.GONE);
        mLlBackOptionSelect.setVisibility(View.GONE);
        ViewUtil.showContentLayout(Const.LAYOUT_EMPTY, mViewStubEmpty, mContentLayout);
    }

    /** 展示数据清单 */
    private void showDataList(List<RecentBackMoneyBean.ListEntity> list){
        mListView.setVisibility(View.VISIBLE);
        mViewStubEmpty.setVisibility(View.GONE);
        mBackMoneyAdapter.loadData(list);
        ViewUtil.showContentLayout(Const.LAYOUT_DATA, mViewStubEmpty, mContentLayout);
    }

    private AbsListView.OnScrollListener mOnScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if(firstVisibleItem==0 && view.getChildAt(firstVisibleItem).getTop()==0){
                mContentLayout.setEnabled(true);
            }else{
                mContentLayout.setEnabled(false);
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_invest_header_all_returned:
            case R.id.ll_recent_all_back:
                startActivity(AllBackMoneyActivity.class);
                UmengEventHelper.onEvent(UmengEventID.MyInvestAllBackMoneyButton);
                break;
        }
    }

    /**显示底部弹窗*/
    @Override
    public void onChangeStatus(String rids, String status, final int position) {
        currRid = rids;
        currStatus = status;
        this.position = position;
        PopuViewUtil dialog = InvestStatusChangeHelper.getInstance().show(getActivity(), status, new InvestStatusChangeHelper.IStatusSelectedListener() {
            @Override
            public void onStatusSelected(final String status) {
                if(currStatus.equals(status)){return;}
                ChangeReturnedStatusProtocol mStatusProtocol = new ChangeReturnedStatusProtocol();
                mDialogLoading.show();
                mStatusProtocol.loadData(currRid, status, new INetRequestListener<CommonBean>() {
                    @Override
                    public void netRequestCompleted() {
                        mDialogLoading.dismiss();
                    }

                    @Override
                    public void netRequestSuccess(CommonBean bean, boolean isSuccess) {
                        if (bean != null && isSuccess) {
                            mBackMoneyAdapter.changeStatus(position, currStatus, status, totalMoney);
                            isStatusChange = false;
                            EventHelper.post(new BidStatusChangeEvent());
                        } else {
                            UIUtil.showToastShort("更改失败");
                        }
                    }
                });
            }
        });
        dialog.show();
//        InvestStatusChangeView view = new InvestStatusChangeView(getActivity());
//        //背景变换
//        final WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
//        lp.alpha = 0.8f;
//        getActivity().getWindow().setAttributes(lp);
//        view.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                lp.alpha = 1f;
//                getActivity().getWindow().setAttributes(lp);
//            }
//        });
//        view.show(status);
//        view.setStatusSelectedListener(this);
//        //设置layout在PopupWindow中显示的位置
//        view.showAtLocation(getActivity().findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /** 改变总额 */
    @Override
    public void onChangeTotalMoney(String total) {
        totalMoney = total;
        if(totalMoney.contains(".")){
            String[] split = totalMoney.split("\\.");
            mTvTotalMoney.setText(split[0]);
            mTvTotalPoint.setText("."+split[1]);
            mTvTotalMoney.setTextColor(getResources().getColor(R.color.finance_supermarket_yellow));
            mTvTotalPoint.setTextColor(getResources().getColor(R.color.finance_supermarket_yellow));
        }else{
            mTvTotalMoney.setText(totalMoney);
            mTvTotalPoint.setText(".00");
            mTvTotalMoney.setTextColor(getResources().getColor(R.color.finance_supermarket_yellow));
            mTvTotalPoint.setTextColor(getResources().getColor(R.color.finance_supermarket_yellow));

        }
    }

    /** 更新数据 */
//    @Override
//    public void onRefresh() {
//        if (Util.legalLogin() == null) {
//            mContentLayout.setRefreshing(false);
//            return;
//        }
//        mContentLayout.setRefreshing(true);
//        mBackMoneyProtocol.loadData(mINetRequestListener, Const.REQUEST_GET);
//        UIUtil.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mContentLayout.setRefreshing(false);
//            }
//        }, 1000 * 10);
//    }

    /** 记账完成，刷新近期回款 */
    @Subscribe
    public void onAccountsKeptEvent(AccountsKeptEvent event){
        if(event!=null && !isDetached()){
            mBackMoneyProtocol.loadData(mINetRequestListener, Const.REQUEST_GET);
        }
    }

    /** 绑定自动记账平台事件 */
    @Subscribe
    public void onBindAccountEvent(BindAccountEvent event){
        if(event!=null && !isDetached()){
            mBackMoneyProtocol.loadData(mINetRequestListener, Const.REQUEST_GET);
        }
    }

    /**投资平台更新事件 */
    @Subscribe
    public void onInvestPlatformRefreshEvent(InvestPlatformRefreshEvent event){
        if(event!=null && !isDetached()){
            mBackMoneyProtocol.loadData(mINetRequestListener, Const.REQUEST_GET);
        }
    }

    /**回款状态更改事件*/
    @Subscribe
    public void onBidStatusChangeEvent(BidStatusChangeEvent event){
        if(event!=null && !isDetached() && isStatusChange){
            mBackMoneyProtocol.loadData(mINetRequestListener, Const.REQUEST_GET);
        }
        isStatusChange = true;
    }

    /**登录状态更改事件 */
    @Subscribe
    public void onLoginStateEvent(LoginStateEvent event){
        if(event!=null && !isDetached() && event.hasLogin){
            mBackMoneyProtocol.loadData(mINetRequestListener, Const.REQUEST_GET);
        }
    }

    /**标的删除事件 */
    @Subscribe
    public void onBidDeletedEvent(BidDeletedEvent event){
        if(event!=null && !isDetached()){
            mBackMoneyProtocol.loadData(mINetRequestListener, Const.REQUEST_GET);
        }
    }
}
