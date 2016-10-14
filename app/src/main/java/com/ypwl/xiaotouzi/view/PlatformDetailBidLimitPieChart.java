package com.ypwl.xiaotouzi.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.bean.PlatformDetailBean;

import java.util.ArrayList;

public class PlatformDetailBidLimitPieChart extends LinearLayout {

    private ImageView noDataView;
    private PieChart mPieChart;
    /**
     * 饼状图的颜色，依次是：天标-->1月标-->2月标-->3月标-->4-6月标-->天标-->6月以上标
     */
    private String[] pieColors = new String[]{"#cbff3d","#5ad4ff","#8cacff","#62e6b9","#ff8f8f","#cbff3d","#ffa77a"};

    public PlatformDetailBidLimitPieChart(Context context) {
        this(context, null);
    }

    public PlatformDetailBidLimitPieChart(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        inflate(getContext(), R.layout.item_page_deadline, this);
        this.noDataView = (ImageView) findViewById(R.id.pie_chart_no_data_deadline);
        this.mPieChart = (PieChart) findViewById(R.id.piechart_platform_detail_bid_limit);
    }

    public void loadData(PlatformDetailBean bean){
        noDataView.setVisibility(bean.getPie_time().size()==0?VISIBLE:GONE);
        mPieChart.setVisibility(bean.getPie_time().size()==0?GONE:VISIBLE);
        createPieChart(mPieChart,bean);
    }

    private  void createPieChart(PieChart mPieChart,PlatformDetailBean pieData) {
        mPieChart.setHoleColorTransparent(true);
        mPieChart.setHoleRadius(60f);  //半径
        mPieChart.setTransparentCircleRadius(64f); // 半透明圈
        //pieChart.setHoleRadius(0)  //实心圆
        mPieChart.setDescription("");
        mPieChart.setUsePercentValues(true);  //显示成百分比
        //        pieChart.setDrawCenterText(false);  //饼状图中间可以添加文字
        mPieChart.setDrawHoleEnabled(false);
        mPieChart.setRotationAngle(90); // 初始旋转角度
        mPieChart.setRotationEnabled(false); // 可以手动旋转
        //        pieChart.setCenterText("Quarterly Revenue");  //饼状图中间的文字
        Legend mLegend = mPieChart.getLegend();  //设置比例图
        mLegend.setEnabled(false);
        mLegend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);  //最右边显示
        //        pieChart.animateXY(1000, 1000);  //设置动画
        //给饼状图设置数据
        //先判断传入的是什么类型的数据
        ArrayList<String> xValues = new ArrayList<>();  //xVals用来表示每个饼块上的内容
        ArrayList<Entry> yValues = new ArrayList<>();  //yVals用来表示封装每个饼块的实际数据
        float totalTimeData = 0 ;
        for (PlatformDetailBean.Time_Data data : pieData.pie_time){
            totalTimeData += Float.parseFloat(data.value);
        }

        //传入的数据是近三月标的期限
        if (pieData.pie_time.size() > 0) {
            for (int i = 0; i < pieData.pie_time.size(); i++) {
                PlatformDetailBean.Time_Data time_data = pieData.pie_time.get(i);
                xValues.add("");
                if (Float.parseFloat(time_data.value) / totalTimeData > 0.01) {
                    yValues.add(new Entry(Float.parseFloat(time_data.value), i));
                }
            }
        } else {
            xValues.add("");
            yValues.add(new Entry(0, 0));
        }

        //y轴的集合
        PieDataSet pieDataSet = new PieDataSet(yValues, "");
        pieDataSet.setSliceSpace(0f); //设置个饼状图之间的距离
        pieDataSet.setDrawValues(true);

        // 饼图颜色
        ArrayList<Integer> colors = new ArrayList<>();
        if (pieData.pie_time.size() == 0){
            colors.add(Color.parseColor("#FF888888"));//没有数据的灰色
        }else {
            int size = pieData.getPie_time().size();
            for(int i=0;i<size;i++){
                String name = pieData.getPie_time().get(i).key;
                if("天标".equals(name)){
                    colors.add(Color.parseColor(pieColors[0]));
                }else {
                    int index = Integer.parseInt(name.substring(0, 1));
                    colors.add(Color.parseColor(pieColors[index]));
                }
            }
        }

        pieDataSet.setColors(colors);

        //设置数据
        PieData data = new PieData(xValues, pieDataSet);
        data.setValueTextSize(10);
        data.setValueTextColor(Color.WHITE);
        data.setValueFormatter(new PercentFormatter());
        mPieChart.setData(data);
        mPieChart.invalidate();
    }
}
