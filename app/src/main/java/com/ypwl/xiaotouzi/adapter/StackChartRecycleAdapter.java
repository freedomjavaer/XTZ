package com.ypwl.xiaotouzi.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.bean.StackBarChartBean;
import com.ypwl.xiaotouzi.utils.DateTimeUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * function : 全部回款：堆积图数据适配器
 * <p/>
 * Created by tengtao on 2016/3/25.
 */
public class StackChartRecycleAdapter extends RecyclerView.Adapter<StackChartRecycleAdapter.ChartViewHolder> {

    private final int TYPE_HIDE_MONEY = 1;
    private final int TYPE_SHOW_MONEY = 2;
    private List<StackBarChartBean> mList = new ArrayList<>();
    private double mMaxMoney = 0;//最大回款额
    private OnStackBarSelectedListener mBarSelectedListener;
    private int hideMoneyIndex = 1;

    private Context mContext;
    public StackChartRecycleAdapter(Context context){
        this.mContext = context;
    }

    public void loadData(List<StackBarChartBean> list,  double maxMoney){
        this.mMaxMoney = maxMoney;
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public ChartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(mContext,R.layout.item_invest_back_money_stack_chart_bar,null);
        return new ChartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ChartViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        StackBarChartBean bean = mList.get(position);
        holder.mTvMoney.setVisibility(itemViewType == TYPE_HIDE_MONEY ? View.INVISIBLE : View.VISIBLE);
        holder.mTvMoney.setText(itemViewType == TYPE_HIDE_MONEY ? "" : bean.getMoney());
        String datetime = bean.getDatetime();
        String[] strings = handleDate(datetime);
        holder.mTvMonth.setText(strings[1] + "月");
        holder.mTvYear.setText(strings[0]+"年");
        if(position>0){
            String[] preStrings = handleDate(mList.get(position - 1).getDatetime());
            holder.mTvYear.setVisibility(preStrings[0].equals(strings[0])?View.INVISIBLE:View.VISIBLE);
        }else{
            holder.mTvYear.setVisibility(View.VISIBLE);
        }

        boolean equals = "1".equals(bean.getType());//是否为已收
        holder.mOverdue.setVisibility(equals?View.GONE:View.VISIBLE);
        //设置全部回款和逾期
        String money = bean.getMoney();
        String overdue = bean.getOverdue();
        //总额
        double m = 0;
        try {
            m = Double.parseDouble(money.replace(",",""));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //逾期
        double over = 0;
        try {
            over = Double.parseDouble(overdue.replace(",",""));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //已回部分
        double total =  m  / mMaxMoney;
        if(bean.isSelected()){
            holder.mNumberBar.setBackgroundColor(Color.parseColor(equals ? "#7C7C7C" : "#2B7DE1"));
        }else{
            holder.mNumberBar.setBackgroundColor(Color.parseColor(equals ? "#CCCCCC" : "#3DB5E6"));
        }
        RelativeLayout.LayoutParams lp= (RelativeLayout.LayoutParams) holder.mNumberBar.getLayoutParams();
        lp.width = UIUtil.dip2px(16);
        int h = (int) ((total * UIUtil.dip2px(105)) + 0.5);
        h = h > 10 ? h : 10;
        if (total > 0 && m > over) {
            int i = (int) (h * (m - over) / m);
            lp.height = i > 3 ? i : 3;
        } else {
            lp.height = 0;
        }
        holder.mNumberBar.setLayoutParams(lp);
        //逾期部分
        double ov = over / mMaxMoney;
        RelativeLayout.LayoutParams overlp = (RelativeLayout.LayoutParams) holder.mOverdue.getLayoutParams();
        overlp.width = UIUtil.dip2px(16);
        if (ov > 0) {
            int j = (int) (h * over / m);
            overlp.height = j > 3 ? j : 3;
        } else {
            overlp.height = 0;
        }
        holder.mOverdue.setLayoutParams(overlp);
        //传递位置
        holder.mLayoutBarChart.setTag(R.id.stack_chart_bar,position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 2 == hideMoneyIndex && position != mList.size() - 1) {
            return TYPE_HIDE_MONEY;
        } else {
            return TYPE_SHOW_MONEY;
        }
    }

    public class ChartViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvYear,mTvMoney,mTvMonth;
        private View mNumberBar,mOverdue;
        private RelativeLayout mLayoutBarChart;

        public ChartViewHolder(View itemView) {
            super(itemView);
            mTvYear = (TextView) itemView.findViewById(R.id.tv_chart_year);
            mTvMoney = (TextView) itemView.findViewById(R.id.tv_chart_money);
            mTvMonth = (TextView) itemView.findViewById(R.id.tv_chart_month);
            mNumberBar = itemView.findViewById(R.id.stack_chart_bar);
            mOverdue = itemView.findViewById(R.id.stack_chart_bar_overdue);
            mLayoutBarChart = (RelativeLayout) itemView.findViewById(R.id.layout_stack_bar_chart);
            mLayoutBarChart.setOnClickListener(mListener);
        }
    }

    private View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag(R.id.stack_chart_bar);
            mBarSelectedListener.onStackBarSelected(position);
            changeSelectedBar(position);
        }
    };

    /**选中某一条bar */
    private void changeSelectedBar(int position){
        for(int i=0;i<mList.size();i++){
            mList.get(i).setSelected(false);
        }
        mList.get(position).setSelected(true);
        /** 改变显示金额 */
        hideMoneyIndex = (position+1) % 2;
        notifyDataSetChanged();
    }

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

    public interface  OnStackBarSelectedListener{
        void onStackBarSelected(int position);
    }

    public void setOnStackBarSelectedListener(OnStackBarSelectedListener listener){
        this.mBarSelectedListener = listener;
    }
}
