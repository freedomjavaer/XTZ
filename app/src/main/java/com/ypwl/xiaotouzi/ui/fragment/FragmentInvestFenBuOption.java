package com.ypwl.xiaotouzi.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.squareup.otto.Subscribe;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.adapter.InvestAnalysisFenbuAdapter;
import com.ypwl.xiaotouzi.base.BaseFragment;
import com.ypwl.xiaotouzi.bean.InvestAnalysisByFenbuBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.event.AccountsKeptEvent;
import com.ypwl.xiaotouzi.event.BidDeletedEvent;
import com.ypwl.xiaotouzi.event.InvestAnalysisFenBuSelectedEvent;
import com.ypwl.xiaotouzi.interf.INetRequestListener;
import com.ypwl.xiaotouzi.netprotocol.InvestAnalysisByFenbuProtocol;
import com.ypwl.xiaotouzi.utils.NetworkUtils;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.ViewUtil;
import com.ypwl.xiaotouzi.view.InvestAnalysisFenbuPieChart;
import com.ypwl.xiaotouzi.view.stickylistheaders.ExpandableStickyListHeadersListView;
import com.ypwl.xiaotouzi.view.stickylistheaders.StickyListHeadersListView;

import java.util.ArrayList;
import java.util.List;

/**
 * function :我的投资分析--排行--平台
 * <p/>
 * Created by tengtao on 2016/4/11.
 */
public class FragmentInvestFenBuOption extends BaseFragment {
    private static final String FRAGMENT_TAG = "fragment_tag";
    private int mCurrentFragmenType;//当前fragment类型
    private View mNoDataView;
    private LinearLayout mLayoutData;
    private InvestAnalysisFenbuPieChart mPieChart;
    private ExpandableStickyListHeadersListView mListView;
    private InvestAnalysisFenbuAdapter mFenbuAdapter;
    private InvestAnalysisByFenbuProtocol mFenbuByPlatformProtocol;
    private List<String> mDataNames = new ArrayList<>();//piechart的数据名称
    private List<String> mDataValues = new ArrayList<>();//piechart的数据值
    private int currHightPosition;

    public static FragmentInvestFenBuOption newInstance(@NonNull String fragmentType) {
        FragmentInvestFenBuOption fragment = new FragmentInvestFenBuOption();
        Bundle args = new Bundle();
        args.putString(FRAGMENT_TAG, fragmentType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String currentFragmenType = getArguments().getString(FRAGMENT_TAG);
            switch (currentFragmenType){
                case FragmentInvestAnalysisByFenBu.FRAGMENT_TAG_FENBU_ANALYSIS_BY_PLATFORM://平台
                    mCurrentFragmenType = 0;
                    break;
                case FragmentInvestAnalysisByFenBu.FRAGMENT_TAG_FENBU_ANALYSIS_BY_CYCLE://周期
                    mCurrentFragmenType = 1;
                    break;
                case FragmentInvestAnalysisByFenBu.FRAGMENT_TAG_FENBU_ANALYSIS_BY_ANNUALIZE://年化率
                    mCurrentFragmenType = 2;
                    break;
                case FragmentInvestAnalysisByFenBu.FRAGMENT_TAG_FENBU_ANALYSIS_BY_RETURNED_TYPE://还款方式
                    mCurrentFragmenType = 3;
                    break;
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_invest_analysis_by_fen_bu_detail,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mNoDataView = view.findViewById(R.id.layout_no_data_view);
        mLayoutData = (LinearLayout) view.findViewById(R.id.ll_content_layout);
        ViewUtil.showContentLayout(Const.LAYOUT_LOADING,mNoDataView,mLayoutData);

        mPieChart = (InvestAnalysisFenbuPieChart) view.findViewById(R.id.invest_analysis_fenbu_pie_chart);
        mListView = (ExpandableStickyListHeadersListView) view.findViewById(R.id.invest_analysis_by_fenbu_listview);
        mFenbuAdapter = new InvestAnalysisFenbuAdapter(getActivity(),mCurrentFragmenType);
        mListView.setAdapter(mFenbuAdapter);
        mListView.setBeginHideAll(true);//取反

        mListView.setOnHeaderClickListener(new StickyListHeadersListView.OnHeaderClickListener() {
            @Override
            public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {
                //获取第position个在listview中的位置
                int positionForSection = mFenbuAdapter.getPositionForSection((int)headerId);
                if(currHightPosition != headerId){//新点击的header--->先关闭原来的
                    mListView.collapse(currHightPosition);
                    mListView.expand(headerId);
                    mPieChart.highlight((int)headerId,true);
                    mFenbuAdapter.changeShowStatus(positionForSection,1);
                }else{
                    if(mListView.isHeaderCollapsed(headerId)){
                        mListView.expand(headerId);
                        mPieChart.highlight((int)headerId,true);
                        mFenbuAdapter.changeShowStatus(positionForSection,1);
                    }else {
                        mListView.collapse(headerId);
                        mPieChart.highlight((int)headerId,false);
                        mFenbuAdapter.changeShowStatus(positionForSection,0);
                    }
                }
                currHightPosition = (int) headerId;
                //list选择
                mListView.setSelection(positionForSection);
            }
        });
        mFenbuByPlatformProtocol = new InvestAnalysisByFenbuProtocol();
        initData();
    }

    private void initData(){
        mFenbuByPlatformProtocol.loadData(mCurrentFragmenType+"",mRequestListener);
    }

    private INetRequestListener mRequestListener = new INetRequestListener<InvestAnalysisByFenbuBean>() {
        @Override
        public void netRequestCompleted() {
            if(!NetworkUtils.isNetworkConnected(UIUtil.getContext())){
                ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mNoDataView, mLayoutData, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        initData();
                    }
                });
            }
        }

        @Override
        public void netRequestSuccess(InvestAnalysisByFenbuBean bean, boolean isSuccess) {
            if(bean!=null && isSuccess){
                List<InvestAnalysisByFenbuBean.ListBean> list = bean.getList();
                boolean showData = needShowData(bean);
                if(showData){
                    initPieChart(bean);
                    mFenbuAdapter.loadData(list);
                    ViewUtil.showContentLayout(Const.LAYOUT_DATA,mNoDataView,mLayoutData);
                }else{
                    ViewUtil.showContentLayout(Const.LAYOUT_EMPTY, mNoDataView, mLayoutData, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            initData();
                        }
                    });
                }
            }else{
                ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mNoDataView, mLayoutData, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        initData();
                    }
                });
            }
        }
    };

    /**判断是否有效数据 */
    private boolean needShowData(InvestAnalysisByFenbuBean bean){
        if(mCurrentFragmenType==1){//周期
            double period = 0.0;
            try{
                period = Double.parseDouble(bean.getPeriod());
            }catch (Exception e){}
            return period!=0;
        }else if(mCurrentFragmenType==2){//年化率
            double wannual = 0.0;
            try{
                wannual = Double.parseDouble(bean.getWannual());
            }catch (Exception e){}
            return  wannual!=0;
        }else{
            List<InvestAnalysisByFenbuBean.ListBean> listBeen = bean.getList();
            return listBeen!=null && listBeen.size()>0;
        }
    }

    /** 初始化piechart */
    private void initPieChart(InvestAnalysisByFenbuBean bean){
        mDataNames.clear();
        mDataValues.clear();
        List<InvestAnalysisByFenbuBean.ListBean> list = bean.getList();
        String centerText="";
        switch (mCurrentFragmenType){
            case 0://投资总额
                centerText = bean.getTotal();
                for(int i=0;i<list.size();i++){
                    InvestAnalysisByFenbuBean.ListBean listBean = list.get(i);
                    mDataNames.add(listBean.getP_name());
                    mDataValues.add(listBean.getPercent());
                }
                break;
            case 1://平均周期
                centerText = bean.getPeriod();
                for(int i=0;i<list.size();i++){
                    InvestAnalysisByFenbuBean.ListBean listBean = list.get(i);
                    mDataNames.add(listBean.getType());
                    mDataValues.add(listBean.getPercent());
                }
                break;
            case 2://平均年化率
                centerText = bean.getWannual();
                for(int i=0;i<list.size();i++){
                    InvestAnalysisByFenbuBean.ListBean listBean = list.get(i);
                    mDataNames.add(listBean.getType());
                    mDataValues.add(listBean.getPercent());
                }
                break;
            case 3://还款方式
                for(int i=0;i<list.size();i++){
                    InvestAnalysisByFenbuBean.ListBean listBean = list.get(i);
                    mDataNames.add(listBean.getType());
                    mDataValues.add(listBean.getPercent());
                }
                break;
        }
        mPieChart.initData(mDataNames, mDataValues, centerText, mCurrentFragmenType);
    }

    @Subscribe
    public void onInvestAnalysisFenBuSelectedEvent(InvestAnalysisFenBuSelectedEvent event){
        if(event!=null && !isDetached()){
            int position = event.getPosition();//第几个分类
            if(position==-1){
                return;
            }
            //获取第position个在listview中的位置
            final int positionForSection = mFenbuAdapter.getPositionForSection(position);
//            if(position != -1){
//                if(currHightPosition == position){
//                    if(mListView.isHeaderCollapsed(position)){//是否关闭
//                        mListView.expand(position);
//                        mFenbuAdapter.changeShowStatus(positionForSection,1);
//                    }else {
//                        mListView.collapse(position);
//                        mFenbuAdapter.changeShowStatus(positionForSection,0);
//                    }
//                }else{
//                    mListView.collapse(currHightPosition);
//                    mListView.expand(position);
//                    mFenbuAdapter.changeShowStatus(positionForSection,1);
//                }
//                currHightPosition = position;
//                mListView.setSelection(positionForSection);//位置position
//                UIUtil.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mFenbuAdapter.selectItem(positionForSection);
//                    }
//                },50);
//            }else{
//                mListView.setSelection(positionForSection);
//                mListView.collapse(currHightPosition);
//                mFenbuAdapter.changeShowStatus(positionForSection,0);
//            }
            mListView.setSelection(positionForSection);//位置position
            UIUtil.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mFenbuAdapter.selectItem(positionForSection);
                }
            },50);
        }
    }

    /**恢复初始状态*/
    public void resetStarus(){
        if(mListView!=null) {
            mListView.collapse(currHightPosition);
            mFenbuAdapter.changeShowStatus(currHightPosition, 0);
        }
        if(mFenbuAdapter!=null)
            mFenbuAdapter.selectItem(-1);
        if(mPieChart!=null)
            mPieChart.highlight(currHightPosition,false);
        currHightPosition = -1;
    }

    /**标的删除事件*/
    @Subscribe
    public void onBidDeletedEvent(BidDeletedEvent event){
        if(event!=null && !isDetached()){
            initData();
        }
    }

    /**记账保存事件*/
    @Subscribe
    public void onAccountsKeptEvent(AccountsKeptEvent event){
        if(event!=null && !isDetached()){
            initData();
        }
    }
}
