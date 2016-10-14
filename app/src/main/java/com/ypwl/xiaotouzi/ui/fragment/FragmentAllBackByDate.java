package com.ypwl.xiaotouzi.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.nineoldandroids.animation.ValueAnimator;
import com.squareup.otto.Subscribe;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.adapter.AllBackByDateAdapter;
import com.ypwl.xiaotouzi.adapter.StackChartRecycleAdapter;
import com.ypwl.xiaotouzi.base.BaseFragment;
import com.ypwl.xiaotouzi.bean.AllBackByDateBean;
import com.ypwl.xiaotouzi.bean.AllBackItemBean;
import com.ypwl.xiaotouzi.bean.CommonBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.common.UmengEventID;
import com.ypwl.xiaotouzi.event.AccountsKeptEvent;
import com.ypwl.xiaotouzi.event.BidDeletedEvent;
import com.ypwl.xiaotouzi.event.BidStatusChangeEvent;
import com.ypwl.xiaotouzi.event.InvestPlatformRefreshEvent;
import com.ypwl.xiaotouzi.event.LoginStateEvent;
import com.ypwl.xiaotouzi.interf.INetRequestListener;
import com.ypwl.xiaotouzi.manager.EventHelper;
import com.ypwl.xiaotouzi.manager.InvestStatusChangeHelper;
import com.ypwl.xiaotouzi.manager.UmengEventHelper;
import com.ypwl.xiaotouzi.netprotocol.AllBackMoneyByDateProtocol;
import com.ypwl.xiaotouzi.netprotocol.ChangeReturnedStatusProtocol;
import com.ypwl.xiaotouzi.ui.activity.CalendarBackMoneyActivity;
import com.ypwl.xiaotouzi.utils.NetworkUtils;
import com.ypwl.xiaotouzi.utils.PopuViewUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.Util;
import com.ypwl.xiaotouzi.utils.ViewUtil;
import com.ypwl.xiaotouzi.view.CustomSwipeToRefresh;
import com.ypwl.xiaotouzi.view.InvestBackStackChartView;
import com.ypwl.xiaotouzi.view.dialog.lib.KProgressHUD;
import com.ypwl.xiaotouzi.view.stickylistheaders.StickyListHeadersListView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * function : 全部回款--日期分组
 * <p/>
 * Created by tengtao on 2016/3/24.
 */
public class FragmentAllBackByDate extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, AllBackByDateAdapter.IChangeStatusListener, StackChartRecycleAdapter.OnStackBarSelectedListener {
    private boolean isStatusChange = true;
    private View mNoDataView;
    private CustomSwipeToRefresh mContentLayout;
    private AllBackMoneyByDateProtocol mByDateProtocol;
    private StickyListHeadersListView mListView;
    private AllBackByDateAdapter mByDateAdapter;
    private InvestBackStackChartView mStackChartView;
    private List<AllBackItemBean> mList = new ArrayList<>();
    private List<AllBackItemBean> mReturnedList = new ArrayList<>();

    private String currRid;//当前回款id
    private String currStatus;//当前状态
    private KProgressHUD mDialogLoading;
    private int position;
    private int divider;
    private int mHeight;
    private DecimalFormat df;
    private boolean	isOpened = true;// 默认显示柱状图
    private List<AllBackByDateBean.RdataEntity> rdata = new ArrayList<>();//已回款堆积图数据
    private List<AllBackByDateBean.SdataEntity> sdata = new ArrayList<>();//待收款堆积图数据

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_all_back_money_by_date,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        df = new DecimalFormat("###0.00");
        mDialogLoading = KProgressHUD.create(getActivity());
        mNoDataView = view.findViewById(R.id.layout_no_data_view);
        mContentLayout = (CustomSwipeToRefresh) view.findViewById(R.id.layout_data_content);
//        mContentLayout.setOnRefreshListener(this);
        ViewUtil.showContentLayout(Const.LAYOUT_LOADING, mNoDataView, mContentLayout);

        mListView = (StickyListHeadersListView) view.findViewById(R.id.sticky_list_view);
        mByDateAdapter = new AllBackByDateAdapter(getActivity());
        mByDateAdapter.setIChangeStatusListener(this);
        mListView.setAdapter(mByDateAdapter);

        mStackChartView = (InvestBackStackChartView) view.findViewById(R.id.stack_chart_container);

        view.findViewById(R.id.tv_all_back_money_today).setOnClickListener(this);//今天
        view.findViewById(R.id.tv_all_back_money_calendar).setOnClickListener(this);//日历还款

        mByDateProtocol = new AllBackMoneyByDateProtocol();
        mByDateProtocol.loadData(mRequestListener,Const.REQUEST_GET);
        mListView.setOnScrollListener(mScrollListener);
    }

    private INetRequestListener mRequestListener = new INetRequestListener<AllBackByDateBean>() {
        @Override
        public void netRequestCompleted() {
            mContentLayout.setRefreshing(false);
            if(!NetworkUtils.isNetworkConnected(UIUtil.getContext())){
                ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mNoDataView, mContentLayout, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mByDateProtocol.loadData(mRequestListener,Const.REQUEST_GET);
                    }
                });
            }
        }

        @Override
        public void netRequestSuccess(AllBackByDateBean bean, boolean isSuccess) {
            if(bean!=null && isSuccess){
                if(bean.getList().size()>0){
                    refreshData(bean);
                }else{
                    ViewUtil.showContentLayout(Const.LAYOUT_EMPTY, mNoDataView, mContentLayout, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mByDateProtocol.loadData(mRequestListener,Const.REQUEST_GET);
                        }
                    });
                }
            }else{
                ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mNoDataView, mContentLayout, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mByDateProtocol.loadData(mRequestListener, Const.REQUEST_GET);
                    }
                });
            }
        }
    };

    /** 更新数据 */
    private void refreshData(AllBackByDateBean bean){
        rdata.clear();
        rdata.addAll(bean.getRdata());
        sdata.clear();
        sdata.addAll(bean.getSdata());
        mStackChartView.setToPosition(rdata.size());
        mStackChartView.updateView(rdata, sdata);
        List<AllBackItemBean> allBackItemBeans = handleData(bean.getList());//处理数据
        if(allBackItemBeans!=null){
            mByDateAdapter.loadDate(mList, divider, bean.getStotal());
            mListView.setSelection(divider+1);
            ViewUtil.showContentLayout(Const.LAYOUT_DATA,mNoDataView,mContentLayout);
        }
        mStackChartView.setOnStackBarSelectedListener(this);
    }

    private AbsListView.OnScrollListener mScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if(firstVisibleItem ==0 && view.getChildAt(firstVisibleItem).getTop()==0){
//                mContentLayout.setEnabled(true);
            }else{
                mContentLayout.setEnabled(false);
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_all_back_money_today://今天
                mListView.setSelection(divider+1);
                mStackChartView.setToPosition(rdata.size());
                mStackChartView.selectedCurrentMonth();//图标滚动到分界线待回月
                UmengEventHelper.onEvent(UmengEventID.AllBackMoneyTodayButton);
                break;
            case R.id.tv_all_back_money_calendar://还款日历
                startActivity(CalendarBackMoneyActivity.class);
                UmengEventHelper.onEvent(UmengEventID.AllBackMoneyByCalendar);
                break;
        }
    }

    /** 刷新 */
    @Override
    public void onRefresh() {
//        if (Util.legalLogin() == null) {
//            mContentLayout.setRefreshing(false);
//            return;
//        }
//        mContentLayout.setRefreshing(true);
//        mByDateProtocol.loadData(mRequestListener, Const.REQUEST_GET);
//        UIUtil.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mContentLayout.setRefreshing(false);
//            }
//        }, 1000 * 10);
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
                            isStatusChange = false;
                            EventHelper.post(new BidStatusChangeEvent());
                            mByDateAdapter.changeStatus(position, currStatus, status);
                            changeStackBarChart(position, currStatus, status);
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
//        view.show(status);//根据状态显示不同选项的弹窗
//        view.setStatusSelectedListener(this);
//        //设置layout在PopupWindow中显示的位置
//        view.showAtLocation(getActivity().findViewById(R.id.all_back_main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * 改变堆积图
     * @param position 改变回款状态的item位置
     * @param currStatus 改变之前的回款状态
     * @param status 改变之后的回款状态
     */
    private void changeStackBarChart(int position, String currStatus, String status){
        boolean hasBacked = "1".equalsIgnoreCase(status);//是否改为已回
        String total = mList.get(position).getTotal();
        int[] mSectionIndices = mByDateAdapter.getSectionIndicesResult();
        int currMonth=0;
        for(int i=0;i<mSectionIndices.length;i++){//找出改变的item所属的月份
            if (position < mSectionIndices[i]) {
                currMonth = i - 1;
                break;
            }
        }
        if(position>=mSectionIndices[mSectionIndices.length-1]){//点击的是最后一个月条目
            currMonth = mSectionIndices.length-1;
        }
        if (mSectionIndices[currMonth] < divider) {//改变了已回款
            AllBackByDateBean.RdataEntity rdataEntity = rdata.get(currMonth);
            String money = rdataEntity.getMoney();
            Double currMoney = Double.parseDouble(money.replace(",", ""));
            Double currTotal = Double.parseDouble(total.replace(",", ""));
            if (hasBacked) {
                rdataEntity.setMoney(Util.markOperatorForMoney(df.format(currMoney + currTotal)));
            } else if ("1".equalsIgnoreCase(currStatus)) {//从已回改其他状态
                rdataEntity.setMoney(Util.markOperatorForMoney(df.format(currMoney - currTotal)));
            }
        } else if(mSectionIndices[currMonth] > divider) {//改变了待收款
            AllBackByDateBean.SdataEntity sdataEntity = sdata.get(currMonth-rdata.size()-1);
            String money = sdataEntity.getMoney();
            Double currMoney = Double.parseDouble(money.replace(",", ""));
            Double currTotal = Double.parseDouble(total.replace(",", ""));
            if (hasBacked) {//其他-->已回
                if(currStatus.equalsIgnoreCase("2")){
                    Double overMoney = Double.parseDouble(sdataEntity.getOverdue().replace(",", ""));
                    sdataEntity.setOverdue(Util.markOperatorForMoney(df.format(overMoney - currTotal)));
                }
                sdataEntity.setMoney(Util.markOperatorForMoney(df.format(currMoney - currTotal)));
            } else if ("1".equalsIgnoreCase(currStatus)) {//从已回改其他状态
                sdataEntity.setMoney(Util.markOperatorForMoney(df.format(currMoney + currTotal)));
                if(status.equalsIgnoreCase("2")){
                    Double overMoney = Double.parseDouble(sdataEntity.getOverdue().replace(",", ""));
                    sdataEntity.setOverdue(Util.markOperatorForMoney(df.format(overMoney + currTotal)));
                }
            }else{
                if(status.equalsIgnoreCase("2")){
                    Double overMoney = Double.parseDouble(sdataEntity.getOverdue().replace(",", ""));
                    sdataEntity.setOverdue(Util.markOperatorForMoney(df.format(overMoney + currTotal)));
                }else if(currStatus.equalsIgnoreCase("2")){
                    Double overMoney = Double.parseDouble(sdataEntity.getOverdue().replace(",", ""));
                    sdataEntity.setOverdue(Util.markOperatorForMoney(df.format(overMoney - currTotal)));
                }
            }
        }
        mStackChartView.setToPosition(currMonth - (position > divider ? 1 : 0));
        mStackChartView.updateView(rdata, sdata);
    }

    /** 处理数据 */
    private List<AllBackItemBean> handleData(final List<AllBackByDateBean.ListEntity> data){
        if(data.size()==0){return null;}
        mList.clear();
        mReturnedList.clear();
        if(rdata.size()==0){
            AllBackItemBean bean = new AllBackItemBean();
            bean.setDatetime("0");
            bean.setMoney("0.00");
            bean.setType("1");
            bean.setIsEmptyMonth(true);
            mList.add(bean);
        }
        for (int i = 0; i < data.size(); i++) {
            AllBackByDateBean.ListEntity listEntity = data.get(i);
            String money = listEntity.getMoney();//当月总额
            Double m = 0.00;
            try{
                m = Double.parseDouble(money.replace(",", ""));
            }catch (Exception e){e.printStackTrace();}
            if (m == 0) {//当前月没有标的返回，添加一组空数据
                AllBackItemBean bean = new AllBackItemBean();
                bean.setDatetime(listEntity.getDatetime());
                bean.setMoney("0.00");
                bean.setIsEmptyMonth(true);
                bean.setType(listEntity.getType());
                mList.add(bean);
                if (listEntity.getType().equals("1")) {
                    mReturnedList.add(bean);
                }
            } else {
                List<AllBackByDateBean.ListEntity.DataEntity> dataEntitys = listEntity.getData();
                for (int j = 0; j < dataEntitys.size(); j++) {
                    AllBackByDateBean.ListEntity.DataEntity dataEntity = dataEntitys.get(j);
                    AllBackItemBean bean = new AllBackItemBean();
                    bean.setDatetime(listEntity.getDatetime());
                    bean.setMoney(money);
                    bean.setType(listEntity.getType());
                    bean.setAid(dataEntity.getAid());
                    bean.setRid(dataEntity.getRid());
                    bean.setP_name(dataEntity.getP_name());
                    bean.setProject_name(dataEntity.getProject_name());
                    bean.setCapital(dataEntity.getCapital());
                    bean.setProfit(dataEntity.getProfit());
                    bean.setTotal(dataEntity.getTotal());
                    bean.setPeriod(dataEntity.getPeriod());
                    bean.setPeriod_total(dataEntity.getPeriod_total());
                    bean.setReturn_time(dataEntity.getReturn_time());
                    bean.setStatus(dataEntity.getStatus());
                    bean.setIs_auto(dataEntity.getIs_auto());
                    bean.setIsEmptyMonth(false);
                    mList.add(bean);
                    if (listEntity.getType().equals("1")) {
                        mReturnedList.add(bean);
                    }
                }
            }
            if (i == rdata.size() - 1) {//表示已处理完已回款项
                AllBackItemBean bean = new AllBackItemBean();
                bean.setDatetime("0");
                bean.setMoney("0.00");
                bean.setType("1");
                bean.setIsEmptyMonth(true);
                mList.add(bean);
            }
        }
        divider = mReturnedList.size();
        return mList;
    }

    /**
     * 图标和下面的listview关联滚动，数据list滚动到分界位置
     * @param position 柱状图的位置
     */
    @Override
    public void onStackBarSelected(int position) {
        int[] sectionIndicesResult = mByDateAdapter.getSectionIndicesResult();
        if (sectionIndicesResult != null && position < sectionIndicesResult.length)
            mListView.setSelection(sectionIndicesResult[position]>=divider?
                    sectionIndicesResult[position+1]:sectionIndicesResult[position]);
    }

    /** 隐藏和显示柱状图 */
    public void toggleHideStackBarChart(){
        mStackChartView.measure(0, 0);// 期望测量
        mHeight = mStackChartView.getMeasuredHeight();
        if (isOpened) {
            changeHightAnimate(mHeight, 0);// 打开--->关闭,改变高度
            UmengEventHelper.onEvent(UmengEventID.AllBackMoneyHideBarChartButton);
        }else {
            changeHightAnimate(0, mHeight);// 关闭---->打开
            UmengEventHelper.onEvent(UmengEventID.AllBackMoneyShowBarChartButton);
        }
        isOpened = !isOpened;// 状态改变
    }

    /**
     * 动态改变对象高度
     * @param start 起始高度
     * @param end 结束高度
     */
    private void changeHightAnimate(int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.setDuration(300);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                int value = (Integer) animator.getAnimatedValue();
                ViewGroup.LayoutParams params = mStackChartView.getLayoutParams();
                params.height = value;
                mStackChartView.setLayoutParams(params);
            }
        });
        animator.start();
    }

    /** 删除标的 */
    @Subscribe
    public void onBidDeletedEvent(BidDeletedEvent event){
        if(event!=null && !isDetached()){
            mByDateProtocol.loadData(mRequestListener,Const.REQUEST_GET);
        }
    }

    /** 保存记账 */
    @Subscribe
    public void onAccountsKeptEvent(AccountsKeptEvent event){
        if(event!=null && !isDetached()){
            mByDateProtocol.loadData(mRequestListener,Const.REQUEST_GET);
        }
    }

    @Subscribe
    public void onInvestPlatformRefreshEvent(InvestPlatformRefreshEvent event){
        if(event!=null && !isDetached()){
            mByDateProtocol.loadData(mRequestListener,Const.REQUEST_GET);
        }
    }

    /** 登录成功 */
    @Subscribe
    public void onLoginStateEvent(LoginStateEvent event){
        if(event!=null && !isDetached() && event.hasLogin){
            mByDateProtocol.loadData(mRequestListener,Const.REQUEST_GET);
        }
    }

    @Subscribe
    public void onBidStatusChangeEvent(BidStatusChangeEvent event){
        if(event!=null && !isDetached() && isStatusChange){
            mByDateProtocol.loadData(mRequestListener,Const.REQUEST_GET);
        }
        isStatusChange = true;
    }
}
