package com.ypwl.xiaotouzi.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.adapter.StackChartRecycleAdapter;
import com.ypwl.xiaotouzi.bean.AllBackByDateBean;
import com.ypwl.xiaotouzi.bean.StackBarChartBean;
import com.ypwl.xiaotouzi.utils.DateTimeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * function : 投资回款：堆积图自定义view
 * <p/>
 * Created by tengtao on 2016/3/25.
 */
public class InvestBackStackChartView extends LinearLayout {
    private boolean isScrollToDivider = true;
    private RecyclerView mRecyclerView;
    private TextView mTvBottomYear;
    private StackChartRecycleAdapter mRecycleAdapter;
    private List<StackBarChartBean> mList = new ArrayList<>();
    private double maxMoney = 0;
    private int toPosition = 0;

    public InvestBackStackChartView(Context context) {
        super(context);
        init(context);
    }

    public InvestBackStackChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public InvestBackStackChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        View.inflate(context, R.layout.layout_invest_back_money_stack_chart_view,this);
        mTvBottomYear = (TextView) findViewById(R.id.tv_all_back_year);
        mRecyclerView = (RecyclerView) findViewById(R.id.all_back_stack_chart);
        mRecycleAdapter = new StackChartRecycleAdapter(context);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(HORIZONTAL);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mRecycleAdapter);
        mRecyclerView.addOnScrollListener(mScrollListener);
    }

    public void setToPosition(int toPosition){
        this.toPosition = toPosition;
    }


    /** 滚动至初始位置 */
    public void selectedCurrentMonth(){
        if(mRecyclerView!=null){
            ((LinearLayoutManager)mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(toPosition, 0);
        }
    }

    public void setScrollToDivider(boolean scrollToDivider){
        this.isScrollToDivider = scrollToDivider;
    }

    /**
     * @param rDatas 已收回款数据
     * @param sDatas 待收回款数据
     */
    public void updateView(List<AllBackByDateBean.RdataEntity> rDatas,List<AllBackByDateBean.SdataEntity> sDatas){
        mList.clear();
        maxMoney = 0;
        if(rDatas!=null && rDatas.size()>0){
            for(int i = 0; i<rDatas.size();i++){
                AllBackByDateBean.RdataEntity rdataEntity = rDatas.get(i);
                StackBarChartBean bean = new StackBarChartBean();
                bean.setDatetime(rdataEntity.getDatetime());
                String money1 = rdataEntity.getMoney();
                bean.setMoney(money1);
                double m1=0;
                try{
                    m1 = Double.parseDouble(money1.replace(",",""));
                }catch (Exception e){e.printStackTrace();}
                if(m1>maxMoney){
                    maxMoney = m1;
                }
                bean.setOverdue("0");
                bean.setType("1");
                bean.setSelected(false);
                mList.add(bean);
            }
        }
        if(sDatas!=null && sDatas.size()>0){
            for(int j = 0; j <sDatas.size();j++){
                AllBackByDateBean.SdataEntity sdataEntity = sDatas.get(j);
                StackBarChartBean bean = new StackBarChartBean();
                bean.setDatetime(sdataEntity.getDatetime());
                String money2 = sdataEntity.getMoney();
                bean.setMoney(money2);
                double m2=0;
                try{
                    m2 = Double.parseDouble(money2.replace(",",""));
                }catch (Exception e){e.printStackTrace();}
                if(m2>maxMoney){
                    maxMoney = m2;
                }
                bean.setOverdue(sdataEntity.getOverdue());
                bean.setType("2");
                bean.setSelected(false);
                mList.add(bean);
            }
        }
        mRecycleAdapter.loadData(mList,maxMoney);
        if(isScrollToDivider){
            ((LinearLayoutManager)mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(toPosition, 0);
        }
    }

    /** 不需分类显示的堆积图 */
    public void updateView(List<StackBarChartBean> list){
        mList.clear();
        maxMoney = 0;
        mList.addAll(list);
        if(list!=null && list.size()>0){
            for(int i = 0; i<list.size();i++){
                String money = list.get(i).getMoney();
                double m1=0;
                try{
                    m1 = Double.parseDouble(money.replace(",",""));
                }catch (Exception e){e.printStackTrace();}
                if(m1>maxMoney){
                    maxMoney = m1;
                }
            }
        }
        mRecycleAdapter.loadData(mList,maxMoney);
        if(isScrollToDivider){
            ((LinearLayoutManager)mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(toPosition, 0);
        }
    }

    private RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
            int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
            String datetime = mList.get(firstVisiblePosition).getDatetime();
            String[] strings = handleDate(datetime);
            boolean needMove;
            if(firstVisiblePosition+1<mList.size()) {
                String otherDate = mList.get(firstVisiblePosition + 1).getDatetime();
                String[] otherStrings = handleDate(otherDate);
                needMove = !otherStrings[0].equalsIgnoreCase(strings[0]);
            }else{
                needMove = false;
            }
            View childAt = mRecyclerView.getChildAt(0);
            if(strings[1].equalsIgnoreCase("12") || needMove){
                if(childAt!=null){
                    mTvBottomYear.layout(childAt.getLeft(),mTvBottomYear.getTop(),
                            mTvBottomYear.getWidth()+childAt.getLeft(),mTvBottomYear.getBottom());
                }
            }else{
                mTvBottomYear.layout(0,mTvBottomYear.getTop(), mTvBottomYear.getWidth(),mTvBottomYear.getBottom());
            }
            mTvBottomYear.setText(strings[0]+"年");
        }
    };

    /**
     * 处理时间戳
     * @param time Unix时间戳
     * @return 年月日的数组
     */
    private String[] handleDate(String time) {
        long millisTime = 0;
        try {
            millisTime = Long.parseLong(time) * 1000;
        } catch (Exception e) {
            e.printStackTrace();
        }
        String formatDateTime = DateTimeUtil.formatDateTime(millisTime, "yyyy-MM-dd");
        return formatDateTime.split("-");
    }

    public void setOnStackBarSelectedListener(StackChartRecycleAdapter.OnStackBarSelectedListener listener){
        if(mRecycleAdapter!=null){
            mRecycleAdapter.setOnStackBarSelectedListener(listener);
        }
    }
}
