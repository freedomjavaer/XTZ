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

public class PlatformDetailTotalMoneyPieChart extends LinearLayout {

    private ImageView noDataView;
    private PieChart mPieChart;
    /**
     * 近三月标的的金额颜色，依次：其他-->0-10万-->10-100万-->100-1000万-->1000万以上
     */
    private String[] pieColors = new String[]{"#cbff3d","#5ad4ff","#8cacff","#62e6b9","#ff8f8f"};

    public PlatformDetailTotalMoneyPieChart(Context context) {
        this(context, null);
    }

    public PlatformDetailTotalMoneyPieChart(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        inflate(getContext(), R.layout.item_page_totalmoney, this);
        this.noDataView = (ImageView) findViewById(R.id.pie_chart_no_data_totalmoney);
        this.mPieChart = (PieChart) findViewById(R.id.piechart_platform_detail_total_money);
    }

    public void loadData(PlatformDetailBean bean){
        noDataView.setVisibility(bean.getPie_money().size()==0?VISIBLE:GONE);
        mPieChart.setVisibility(bean.getPie_money().size()==0?GONE:VISIBLE);
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
        float totalMoneyData = 0;
        for (PlatformDetailBean.Money_Data data : pieData.pie_money){
            totalMoneyData += Float.parseFloat(data.value);
        }

        //传入的数据是近三月标的金额
        if (pieData.pie_money.size() > 0) {
            for (int i = 0; i < pieData.pie_money.size(); i++) {
                PlatformDetailBean.Money_Data money_data = pieData.pie_money.get(i);
                xValues.add("");
                if (Float.parseFloat(money_data.value) / totalMoneyData > 0.01) {
                    yValues.add(new Entry(Float.parseFloat(money_data.value), i));
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

        if (pieData.pie_money.size() == 0){
            colors.add(Color.parseColor("#FF888888"));
        }else {
            int size = pieData.pie_money.size();
            for(int i=0;i<size;i++){
                String name = pieData.pie_money.get(i).key;
                int index;
                if(name.contains("-")){
                    String[] split = name.split("-");
                    index = split[0].length();
                }else{
                    index = 4;
                }
                colors.add(Color.parseColor(pieColors[index]));
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
