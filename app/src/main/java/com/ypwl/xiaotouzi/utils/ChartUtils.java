package com.ypwl.xiaotouzi.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.adapter.AnnualRateTendencyAdapter;
import com.ypwl.xiaotouzi.bean.PlatformDeepDataBean;
import com.ypwl.xiaotouzi.bean.PlatformDetailBean;
import com.ypwl.xiaotouzi.bean.ProfitTendencyBean;
import com.ypwl.xiaotouzi.common.UmengEventID;
import com.ypwl.xiaotouzi.manager.UmengEventHelper;
import com.ypwl.xiaotouzi.ui.activity.PlatformDetailDeepDataActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 生成图表的工具类
 * Created by PDK on 2015/11/9.
 */
public class ChartUtils {

    //存储Y轴数据
    private static List<String> dataList = new ArrayList<>();
    private static List<Integer> dataList1 = new ArrayList<>();
    //存储日期数据
    private static List<String> dateList = new ArrayList<>();
    private static List<String> deepDataDateList = new ArrayList<>();

    private static InvestMarkerView myMarkerView_1;
    private static BorrowMarkerView myMarkerView_2;
    private static int xIndex;//选中图表条目的索引
    private static RelativeLayout mInvestContainer;
    private static RelativeLayout mBorrowContainer;
    private static RelativeLayout mCombinedContainer;
    private static RelativeLayout mDeepDataContainer;

    public static BarDataSet compareAnalyzedataSet;
    public static BarDataSet profitTendencyBarSet;

    private static String[] dataType = new String[]{
            "成交量:%s万元", "平均利率:%s%%", "历史待还:%s万元", "资金净流入:%s万元", "投资人数:%s人", "借款人数:%s人", "人均投资金额:%s万元",
            "人均借款金额:%s万元", "借款标数:%s个", "平均借款期限:%s个月", "待收投资人数:%s人", "待还借款人数:%s人"
    };

    /**投资分析------占比统计需要的颜色值*/
    public static String[] pieColors = new String[]{"#7078ca","#4ccdfe","#8ccf69","#feb55c","#fb7154","#fe2199","#e81dff","#b228ff","#5e2dff"
            ,"#3086ff","#216af9","#36ffe7","#64ff36","#fffa2d","#fea81d","#d5ff29","#ff5017","#7409b1","#247900","#1a016c","#c592ca"
            ,"#391873","#3ac3a5","#46c804","#d1608c","#dc2d54","#6d3751","#79cfe6","#b1b664","#f4ae8a","#c4a040","#2b3904","#f0180d"
            ,"#219cf9","#496e89","#e9c42e","#ffdf92","#29ec9e","#cde091","#8bafdf","#dda200","#f8f8ec","#2ae16d","#6366cd","#e2b2a6"
            ,"#b3d0f0","#a84639","#697488","#c6733d","#6f666b","#6bc4ff","#e5cdda","#9cc492","#00b8ff","#fda2e0"};
    private static int[] colors;


    /**
     * 平台详情柱状图工具
     *
     * @param activity
     *          平台详情activity
     * @param mPlatformName
     *          平台名字
     * @param mPlatformPid
     *          平台id
     * @param barChart
     *          柱形图对象
     * @param platformDetailDataBean
     *          平台详情数据实体
     * @param tag
     *          数据类型标记
     */
    public static void createBarChart(final Activity activity, final String mPlatformName, final String mPlatformPid, final BarChart barChart, PlatformDetailBean platformDetailDataBean, final int tag) {
        //取消右边Y轴网格线
        barChart.getXAxis().setDrawGridLines(false);
        //设置图表不允许缩放
        barChart.setScaleEnabled(false);
        //将X轴上的坐标值放于底部
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        //隐藏右侧Y轴
        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false);
        //设置背景颜色为白色
        barChart.setBackgroundColor(Color.WHITE);
        barChart.setDrawGridBackground(false);

        /**图表具体设置*/
        ArrayList<BarEntry> yValues = new ArrayList<>();//显示条目
        ArrayList<String> xValues = new ArrayList<>();//X坐标轴

        /**判断使用投资人数据还是借款人数据*/
        if (tag == 0) {
            //使用投资人数据
            myMarkerView_1 = new InvestMarkerView(activity, R.layout.chart_marker_view);
            myMarkerView_1.setMinimumHeight(500);
            barChart.setMarkerView(myMarkerView_1);
            dateList.clear();

            if (platformDetailDataBean.y_tzrs.size() > 0) {
                for (int i = 0; i < 15; i++) {
                    yValues.add(new BarEntry(Float.parseFloat(platformDetailDataBean.y_tzrs.get(i)), i));
                    dateList.add(platformDetailDataBean.x_date.get(i));
                    if (i == 0) {
                        xValues.add(platformDetailDataBean.x_date.get(i));
                    } else {
                        String str = platformDetailDataBean.x_date.get(i);
                        xValues.add(processDate(str));
                    }
                }
            }
        } else if (tag == 1) {
            //使用借款人数据
            myMarkerView_2 = new BorrowMarkerView(activity, R.layout.chart_marker_view);
            myMarkerView_2.setMinimumHeight(500);
            barChart.setMarkerView(myMarkerView_2);
            dateList.clear();

            if (platformDetailDataBean.y_jkrs.size() > 0) {
                for (int i = 0; i < 15; i++) {
                    yValues.add(new BarEntry(Float.parseFloat(platformDetailDataBean.y_jkrs.get(i)), i));
                    dateList.add(platformDetailDataBean.x_date.get(i));
                    if (i == 0) {
                        xValues.add(platformDetailDataBean.x_date.get(i));
                    } else {
                        String str = platformDetailDataBean.x_date.get(i);
                        xValues.add(processDate(str));
                    }
                }
            }
        }
        barChart.setOnTouchListener(new View.OnTouchListener() {
            private long mUpTime;
            private long mDownTime;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mDownTime = System.currentTimeMillis();
                        mInvestContainer.setVisibility(View.GONE);
                        mBorrowContainer.setVisibility(View.GONE);
                        break;
                    case MotionEvent.ACTION_UP:
                        mUpTime = System.currentTimeMillis();
                        if (mUpTime - mDownTime < 150) {
                            mInvestContainer.setVisibility(View.GONE);
                            mBorrowContainer.setVisibility(View.GONE);
                            Intent intent = new Intent(activity, PlatformDetailDeepDataActivity.class);
                            intent.putExtra("platformname", mPlatformName);
                            intent.putExtra("pid", mPlatformPid);
                            if (tag == 0) {
                                intent.putExtra("position1", "4");
                                intent.putExtra("position2", "0");
                            } else {
                                intent.putExtra("position1", "5");
                                intent.putExtra("position2", "0");
                            }
                            activity.startActivity(intent);
                            UmengEventHelper.onEvent(UmengEventID.NetPlatformDetailDeepData);
                        }
                        mInvestContainer.setVisibility(View.GONE);
                        mBorrowContainer.setVisibility(View.GONE);
                        break;
                }
                return false;
            }
        });

        barChart.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                barChart.getParent().requestDisallowInterceptTouchEvent(true);
                mInvestContainer.setVisibility(View.VISIBLE);
                mBorrowContainer.setVisibility(View.VISIBLE);
                return false;
            }
        });

        //如果数据为空，图表触摸事件不生效
        if (platformDetailDataBean.y_tzrs.size() == 0){
            barChart.setTouchEnabled(false);
        }
        if (platformDetailDataBean.y_jkrs.size() == 0){
            barChart.setTouchEnabled(false);
        }
        //设置数据集
        BarDataSet dataSet = new BarDataSet(yValues, "");
        //设置柱状图条目的颜色
        if (tag == 0){
            dataSet.setColor(Color.parseColor("#55acff"));
        }else {
            dataSet.setColor(Color.parseColor("#52c4ff"));
        }
        dataSet.setHighLightAlpha(0);
        //封装数据集
        BarData data = new BarData(xValues, dataSet);

        //取消描述
        Legend mLegend = barChart.getLegend();
        mLegend.setEnabled(false);

        //图表描述
        barChart.setDescription("");
        //给图表设置数据
        barChart.setData(data);
        //取消柱状图顶部文字
        barChart.setDrawValueAboveBar(false);
        barChart.getBarData().setDrawValues(false);
        //设置Y方向上动画animateY(int time);
        barChart.animateY(500);
        barChart.invalidate();
    }

    /**
     *  平台详情（柱状折线混合图工具）
     *
     * @param activity
     *          平台详情activity
     * @param mPlatformName
     *          平台名字
     * @param mPlatformPid
     *          平台id
     * @param combinedChart
     *          混合图对象
     * @param platformDetailDataBean
     *          平台详情数据实体
     */
    public static void createCombinedBarChart(final Activity activity, final String mPlatformName, final String mPlatformPid, final CombinedChart combinedChart, PlatformDetailBean platformDetailDataBean) {

        //设置描述信息
        combinedChart.setDescription("");
        //设置背景颜色
        combinedChart.setBackgroundColor(Color.WHITE);
        combinedChart.setDrawGridBackground(false);
        //设置柱状图没有阴影
        combinedChart.setDrawBarShadow(false);
        //设置图表不允许缩放
        combinedChart.setScaleEnabled(false);

        //取消描述信息
        Legend mLengend = combinedChart.getLegend();
        mLengend.setEnabled(false);

        //取消网格
        combinedChart.getAxisRight().setDrawGridLines(false);
        //combinedChart.getAxisLeft().setDrawGridLines(false);
        combinedChart.getXAxis().setDrawGridLines(false);

        //将X轴上的坐标值放于底部
        XAxis xAxis = combinedChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        CombinedDetailMarkerView markerView = new CombinedDetailMarkerView(activity, R.layout.chart_marker_view);
        markerView.setMinimumHeight(500);
        combinedChart.setMarkerView(markerView);

        combinedChart.setOnTouchListener(new View.OnTouchListener() {

            private long mUpTime;
            private long mDownTime;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mDownTime = System.currentTimeMillis();
                        mCombinedContainer.setVisibility(View.GONE);
                        break;
                    case MotionEvent.ACTION_UP:
                        mUpTime = System.currentTimeMillis();
                        if (mUpTime - mDownTime < 150) {
                            mCombinedContainer.setVisibility(View.GONE);
                            Intent intent = new Intent(activity, PlatformDetailDeepDataActivity.class);
                            intent.putExtra("platformname", mPlatformName);
                            intent.putExtra("pid", mPlatformPid);
                            intent.putExtra("position1", "0");
                            intent.putExtra("position2", "1");
                            activity.startActivity(intent);
                            UmengEventHelper.onEvent(UmengEventID.NetPlatformDetailDeepData);
                        }
                        mCombinedContainer.setVisibility(View.GONE);
                        break;
                }
                return false;
            }

        });

        combinedChart.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                combinedChart.getParent().requestDisallowInterceptTouchEvent(true);
                mCombinedContainer.setVisibility(View.VISIBLE);
                return false;
            }
        });
        //如果数据为空，图表触摸事件不生效
        if (platformDetailDataBean.y_cjl.size() == 0 || platformDetailDataBean.y_ll.size() == 0){
            combinedChart.setTouchEnabled(false);
        }

        //设置Y轴方向的动画
        combinedChart.animateY(500);

        /**设置折线图数据*/
        LineData lineData = new LineData();

        ArrayList<Entry> yLineValues = new ArrayList<>();//Y轴数据
        //设置利率数据
        for (int i = 0; i < 15; i++) {
            if (platformDetailDataBean.y_cjl != null && platformDetailDataBean.y_cjl.size() > 0) {
                yLineValues.add(new Entry(Float.parseFloat(platformDetailDataBean.y_ll.get(i)), i));
            }
        }

        //设置折线图数据集
        LineDataSet lineSet = new LineDataSet(yLineValues, "");
        lineSet.setColor(Color.parseColor("#3db5e6"));
        lineSet.setLineWidth(2.5f);
        lineSet.setCircleColor(Color.parseColor("#FF52C3FE"));
        lineSet.setCircleSize(4f);
        lineSet.setDrawHighlightIndicators(false);//取消指示线

        //lineSet.setFillColor(Color.rgb(240, 238, 70));
        //lineSet.setValueTextSize(10f);
        //lineSet.setValueTextColor(Color.BLACK);
        //以右边Y轴为准
        lineSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
        lineData.addDataSet(lineSet);
        //设置数值不可见
        lineData.setDrawValues(false);

        /**设置柱形图数据*/
        BarData barData = new BarData();

        ArrayList<BarEntry> yBarValues = new ArrayList<>();//Y轴数据
        dataList.clear();
        //设置成交量数据
        for (int i = 0; i < 15; i++) {
            if (platformDetailDataBean.y_cjl != null && platformDetailDataBean.y_cjl.size() > 0) {
                dataList.add(platformDetailDataBean.y_cjl.get(i));
                yBarValues.add(new BarEntry(Float.parseFloat(platformDetailDataBean.y_cjl.get(i)), i));
            }
        }
        //设置柱形图数据集
        BarDataSet barSet = new BarDataSet(yBarValues, "");
        barSet.setColor(Color.parseColor("#338fe6"));
        barSet.setHighLightAlpha(0);//取消柱状图高亮
        barSet.setValueTextSize(10f);
        //以左边Y轴为准
        barSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        barData.addDataSet(barSet);
        //柱形图主体顶部文字不可见
        barData.setDrawValues(false);

        /**处理日期数据*/
        String[] recentlyTime = new String[15];
        dateList.clear();
        for (int i = 0; i < 15; i++) {
            dateList.add(platformDetailDataBean.x_date.get(i));
            if (i == 0) {
                if (platformDetailDataBean.x_date != null && platformDetailDataBean.x_date.size() > 0) {
                    recentlyTime[i] = platformDetailDataBean.x_date.get(i);
                }
            } else {
                if (platformDetailDataBean.x_date != null && platformDetailDataBean.x_date.size() > 0) {
                    recentlyTime[i] = processDate(platformDetailDataBean.x_date.get(i));
                }
            }
        }

        //加载数据
        CombinedData allData = new CombinedData(recentlyTime);
        allData.setData(lineData);
        allData.setData(barData);
        combinedChart.setData(allData);
        combinedChart.invalidate();
    }

    /**
     * 收益趋势混合图
     *
     * @param combinedChart
     * @param bean
     */
    public static void createCombinedChart(Context context,CombinedChart combinedChart,ProfitTendencyBean bean, final RecyclerView mRecycleView){

        //设置描述信息
        combinedChart.setDescription("");
        //设置背景颜色
        combinedChart.setBackgroundColor(Color.WHITE);
        combinedChart.setDrawGridBackground(false);
        //设置柱状图没有阴影
        combinedChart.setDrawBarShadow(false);
        //设置图表不允许缩放
        combinedChart.setScaleEnabled(false);

        //取消描述信息
        Legend mLengend = combinedChart.getLegend();
        mLengend.setEnabled(false);

        //取消网格
        combinedChart.getAxisRight().setDrawGridLines(false);
        //combinedChart.getAxisLeft().setDrawGridLines(false);
        combinedChart.getXAxis().setDrawGridLines(false);

        //将X轴上的坐标值放于底部
        XAxis xAxis = combinedChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        //隐藏左边Y轴
        YAxis axis1 = combinedChart.getAxis(YAxis.AxisDependency.LEFT);
        axis1.setTextColor(context.getResources().getColor(R.color.unchoosedcolor));//轴标签字体颜色
        axis1.setGridColor(context.getResources().getColor(R.color.unchoosedcolor));//轴对应的栅格线颜色
        axis1.setDrawAxisLine(false);
        //隐藏右边Y轴
        YAxis axis2 = combinedChart.getAxis(YAxis.AxisDependency.RIGHT);
        axis2.setTextColor(context.getResources().getColor(R.color.unchoosedcolor));//轴标签字体颜色
        axis2.setGridColor(context.getResources().getColor(R.color.unchoosedcolor));//轴对应的栅格线颜色
        axis2.setDrawAxisLine(false);

        //设置Y轴方向的动画
        combinedChart.animateY(500);

        /**设置折线图数据*/
        LineData lineData = new LineData();
        ArrayList<Entry> yLineValues = new ArrayList<>();//Y轴数据
        for (int i = 0; i < bean.getList().size(); i++){
            yLineValues.add(new Entry(Float.parseFloat(bean.getList().get(i).getRate().replace(",","")), i));
        }

        //设置折线图数据集
        LineDataSet lineSet = new LineDataSet(yLineValues, "");
        lineSet.setDrawHorizontalHighlightIndicator(false);//取消横向指示线
        lineSet.setDrawVerticalHighlightIndicator(false);//取消纵向指示线
        lineSet.setHighLightColor(Color.parseColor("#3db5e6"));//#3db5e6浅蓝    #3399ff深蓝
        lineSet.setColor(Color.parseColor("#3DB5E6"));
        lineSet.setLineWidth(2.5f);
        lineSet.setCircleColor(Color.parseColor("#3DB5E6"));//  #FF52C3FE
        lineSet.setCircleSize(4f);
        //以右边Y轴为准
        lineSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
        lineData.addDataSet(lineSet);
        //设置数值不可见
        lineData.setDrawValues(false);

        /**设置柱形图数据*/
        BarData barData = new BarData();
        ArrayList<BarEntry> yBarValues = new ArrayList<>();//Y轴数据
        dataList.clear();
        for (int i = 0; i < bean.getList().size(); i++) {
            dataList.add(bean.getList().get(i).getRate());
            yBarValues.add(new BarEntry(Float.parseFloat(StringUtil.getFitString(bean.getList().get(i).getMoney())), i));

        }
        if (bean.getList().size() < 15){
            for (int i = bean.getList().size(); i < 15; i++) {
                yBarValues.add(new BarEntry(0, i));
            }
        }

        //设置柱形图数据集
        profitTendencyBarSet = new BarDataSet(yBarValues, "");
        //profitTendencyBarSet.setColor(Color.parseColor("#7f47d047"));//  #3de6c0
        profitTendencyBarSet.setHighLightAlpha(0);//取消柱状图高亮
        profitTendencyBarSet.setValueTextSize(10f);
        //以左边Y轴为准
        profitTendencyBarSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        if (bean.getList().size() < 15) {
            colors = new int[15];
            for (int i = 0; i < 15; i++) {
                colors[i] = Color.parseColor("#7f47d047");
                for (int j = 0; j < bean.getList().size(); j++) {
                    if ("0".equals(bean.getList().get(j).getType())) {
                        colors[j] = Color.parseColor("#7fFE5E5E");
                    }
                }
            }
        } else {
            colors = new int[bean.getList().size()];
            for (int i = 0; i < bean.getList().size(); i++) {
                if ("0".equals(bean.getList().get(i).getType())) {
                    colors[i] = Color.parseColor("#7fFE5E5E");
                }else {
                    colors[i] = Color.parseColor("#7f47d047");
                }
            }
        }

        profitTendencyBarSet.setColors(colors);
        profitTendencyBarSet.setDrawValues(false);

        barData.addDataSet(profitTendencyBarSet);
        //柱形图主体顶部文字不可见
        barData.setDrawValues(false);

        /**处理日期数据*/
        String[] recentlyTime = null;
        if (bean.getList().size() < 15){
            recentlyTime = new String[15];
        }else {
            recentlyTime = new String[bean.getList().size()];
        }
        dateList.clear();
        for (int i = 0; i < bean.getList().size(); i++) {
            if (i == 0) {
                if (bean.getList().size() > 0){
                    recentlyTime[i] = GlobalUtils.dateFormat(bean.getList().get(i).getTime());
                }

            } else {
                if (bean.getList().size() > 0) {
                    recentlyTime[i] = handleDate(GlobalUtils.dateFormat(bean.getList().get(i).getTime()));
                }
            }
            dateList.add(GlobalUtils.dateFormat(bean.getList().get(i).getTime()));
        }

        if (bean.getList().size() < 15){
            for (int i = bean.getList().size(); i < 15; i++) {
                recentlyTime[i] = "";
            }
        }

        //加载数据
        CombinedData allData = new CombinedData(recentlyTime);
        allData.setData(lineData);
        allData.setData(barData);
        combinedChart.setData(allData);
        combinedChart.setVisibleXRange(8, 16);
        combinedChart.invalidate();

        ProfitTendencyMarkerView markerView = new ProfitTendencyMarkerView(context,R.layout.chart_marker_view);
        combinedChart.setMarkerView(markerView);

        combinedChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry entry, int i, Highlight highlight) {
                int xIndex = entry.getXIndex();
                mRecycleView.smoothScrollToPosition(xIndex);
                AnnualRateTendencyAdapter adapter = (AnnualRateTendencyAdapter) mRecycleView.getAdapter();
                adapter.selectItem(xIndex);
                profitTendencyBarSet.setHighLightAlpha(80);
            }

            @Override
            public void onNothingSelected() {
                AnnualRateTendencyAdapter adapter = (AnnualRateTendencyAdapter) mRecycleView.getAdapter();
                adapter.selectItem(-1);
            }

        });

    }

    /**
     *
     *  深度数据图表工具
     *
     * @param activity
     *          深度数据activity
     * @param combinedChart
     *          深度数据混合图对象
     * @param platformDeepDataBean
     *          深度数据实体
     * @param mList
     *          存储数据类型标记的集合
     * @param mDateList
     *          日期数据
     */
    public static void createCombinedBarChart(Activity activity, final CombinedChart combinedChart, PlatformDeepDataBean platformDeepDataBean,List<Integer> mList,List<Integer> mDateList,LinearLayout mPieChartRightTitle) {

        //用于判断存储的是哪种类型的数据
        dataList1 = mList;

        //设置描述信息
        combinedChart.setDescription("");
        //设置背景颜色
        combinedChart.setBackgroundColor(Color.WHITE);
        combinedChart.setDrawGridBackground(false);
        //设置柱状图没有阴影
        combinedChart.setDrawBarShadow(false);
        //设置图表不允许X,Y轴方向缩放
        combinedChart.setScaleYEnabled(false);
        combinedChart.setScaleXEnabled(false);
        //拖拽滚动时,手放开是否会持续滚动
        combinedChart.setDragDecelerationEnabled(true);

        //初始化显示最后几条数据
        combinedChart.moveViewToX(1000);

        //取消描述信息
        Legend mLengend = combinedChart.getLegend();
        mLengend.setEnabled(false);

        //取消网格
        combinedChart.getAxisRight().setDrawGridLines(false);
        //combinedChart.getAxisLeft().setDrawGridLines(false);
        combinedChart.getXAxis().setDrawGridLines(false);

        //将X轴上的坐标值放于底部
        XAxis xAxis = combinedChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setSpaceBetweenLabels(8);
        //Y轴的设置
        YAxis axisRight = combinedChart.getAxisRight();
        axisRight.setEnabled(true);

        //设置Y轴方向的动画
        combinedChart.animateY(1000);

        DeepDataMarkerView markerView = new DeepDataMarkerView(activity,R.layout.chart_marker_view);
        markerView.setMinimumHeight(5000);

        combinedChart.setMarkerView(markerView);
        //combinedChart.setViewPortOffsets(50,0,0,0);

        /**设置折线图数据*/
        LineData lineData = new LineData();

        ArrayList<Entry> yLineValues = new ArrayList<>();//Y轴数据
        final LineDataSet lineSet = new LineDataSet(yLineValues, "");
        lineSet.setColor(Color.parseColor("#3399ff"));//#FF52C3FE
        lineSet.setLineWidth(2.5f);
        lineSet.setCircleColor(Color.parseColor("#3399ff"));//折线圆点
        lineSet.setCircleSize(4f);
        lineSet.setFillColor(Color.rgb(240, 238, 70));
        lineSet.setDrawHorizontalHighlightIndicator(false);//取消横向指示线
        lineSet.setDrawVerticalHighlightIndicator(false);//取消纵向指示线
        lineSet.setHighlightLineWidth(2);//指示线的宽度
        lineSet.setHighLightColor(Color.parseColor("#3db5e6"));//#3db5e6浅蓝    #3399ff深蓝

        //右边Y轴标题的设置
        mPieChartRightTitle.setVisibility(View.VISIBLE);
        //设置利率数据
        if (platformDeepDataBean.y2 != null) {
            for (int i = 0; i < platformDeepDataBean.y2.size(); i++) {

                if (platformDeepDataBean.y2.size() > 0) {
                    yLineValues.add(new Entry(Float.parseFloat(platformDeepDataBean.y2.get(i).replace(",","")), i));
                }
            }
        }else{
            if (mDateList.get(0) == 0){
                for (int i = 0; i < 365; i++) {
                    yLineValues.add(new Entry(0, i));
                    lineSet.setCircleSize(0);
                    lineSet.setLineWidth(0);
                    axisRight.setEnabled(false);
                    mPieChartRightTitle.setVisibility(View.GONE);
                }
            }else if (mDateList.get(0) == 1){
                for (int i = 0; i < 52; i++) {
                    yLineValues.add(new Entry(0, i));
                    lineSet.setCircleSize(0);
                    lineSet.setLineWidth(0);
                    axisRight.setEnabled(false);
                    mPieChartRightTitle.setVisibility(View.GONE);
                }
            }else if (mDateList.get(0) == 2){
                for (int i = 0; i < 12; i++) {
                    yLineValues.add(new Entry(0, i));
                    lineSet.setCircleSize(0);
                    lineSet.setLineWidth(0);
                    axisRight.setEnabled(false);
                    mPieChartRightTitle.setVisibility(View.GONE);
                }
            }
        }

        //lineSet.setValueTextSize(10f);
        //lineSet.setValueTextColor(Color.BLACK);
        //以右边Y轴为准
        lineSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
        lineData.addDataSet(lineSet);
        //设置数值不可见
        lineData.setDrawValues(false);

        /**设置柱形图数据*/
        BarData barData = new BarData();

        ArrayList<BarEntry> yBarValues = new ArrayList<>();//Y轴数据
        dataList.clear();
        //设置成交量数据
        for (int i = 0; i < platformDeepDataBean.y1.size(); i++) {
            if (platformDeepDataBean.y1.size() > 0) {
                dataList.add(platformDeepDataBean.y1.get(i));
                yBarValues.add(new BarEntry(Float.parseFloat(platformDeepDataBean.y1.get(i).replace(",","")), i));
            }
        }
        //设置柱形图数据集
        final BarDataSet barSet = new BarDataSet(yBarValues, "");
        barSet.setColor(Color.parseColor("#3db5e6"));
        barSet.setHighLightAlpha(0);//取消柱状图高亮
        barSet.setValueTextSize(8f);
        //以左边Y轴为准
        barSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        barData.addDataSet(barSet);
        //柱形图主体顶部文字不可见
        barData.setDrawValues(false);
        barData.setGroupSpace(500f);

        combinedChart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mDeepDataContainer.setVisibility(View.VISIBLE);
                    lineSet.setDrawVerticalHighlightIndicator(true);
                    barSet.setHighLightAlpha(120);

                }
                return false;
            }
        });

        /**处理日期数据*/
        String[] recentlyTime = new String[platformDeepDataBean.x_date.size()];
        deepDataDateList.clear();
        for (int i = 0; i < platformDeepDataBean.x_date.size(); i++) {
            deepDataDateList.add(platformDeepDataBean.x_date.get(i));
            recentlyTime[i] = platformDeepDataBean.x_date.get(i);
        }

        //加载数据
        CombinedData allData = new CombinedData(recentlyTime);
        allData.setData(lineData);
        allData.setData(barData);
        combinedChart.setData(allData);
        combinedChart.setVisibleXRange(12, 25);
        //combinedChart.setDrawBorders(true);//设置边框
        //combinedChart.invalidate();

    }

    /**
     * 对年份日期进行处理
     *
     * @param date 传入需要处理的时间
     * @return 返回处理后的时间
     */
    public static String processDate(String date) {
        int index = date.indexOf("-");
        String proDate = date.substring(index + 1);
        return proDate;
    }

    /**
     * 对年份日期进行处理
     *
     * @param date 传入需要处理的时间
     * @return 返回处理后的时间
     */
    public static String handleDate(String date) {
        int index = date.indexOf("/");
        String proDate = date.substring(index + 1);
        return proDate;
    }

    private static class InvestMarkerView extends MarkerView {

        private TextView tvContent;

        public InvestMarkerView(Context context, int layoutResource) {
            super(context, layoutResource);
            tvContent = (TextView) findViewById(R.id.tvContent);
            mInvestContainer = (RelativeLayout) findViewById(R.id.rl_chart_markerview);
        }

        @Override
        public void refreshContent(Entry entry, Highlight highlight) {
            int n = (int) entry.getVal();
            xIndex = entry.getXIndex();
            String date = dateList.get(xIndex);
            tvContent.setText("日期：" + date + "\n投资人数：" + n + "人");
            tvContent.setTextSize(10f);
            tvContent.setTextColor(Color.BLACK);
        }

        @Override
        public int getXOffset() {
            return -(getWidth() / 2);
        }

        @Override
        public int getYOffset() {
            return -getHeight();
        }

        @Override
        public void draw(Canvas canvas, float posx, float posy) {
            posx += getXOffset();
            posy = 0;
            if (posx > UIUtil.dip2px(270)) {
                if (posx > UIUtil.dip2px(290)) {
                    mInvestContainer.setBackgroundResource(R.drawable.markerview_2);
                } else {
                    mInvestContainer.setBackgroundResource(R.drawable.markerview_1);
                }
                posx = UIUtil.dip2px(267);
            } else {
                mInvestContainer.setBackgroundResource(R.drawable.markerview_1);
            }

            canvas.translate(posx, posy);
            draw(canvas);
            canvas.translate(-posx, -posy);
        }
    }

    private static class BorrowMarkerView extends MarkerView {

        private TextView tvContent;

        public BorrowMarkerView(Context context, int layoutResource) {
            super(context, layoutResource);
            tvContent = (TextView) findViewById(R.id.tvContent);
            mBorrowContainer = (RelativeLayout) findViewById(R.id.rl_chart_markerview);
        }

        @Override
        public void refreshContent(Entry entry, Highlight highlight) {
            int n = (int) entry.getVal();
            int xIndex = entry.getXIndex();
            String date = dateList.get(xIndex);
            tvContent.setText("日期：" + date + "\n借款人数：" + n + "人");
            tvContent.setTextSize(10f);
            tvContent.setTextColor(Color.BLACK);
        }

        @Override
        public int getXOffset() {
            return -(getWidth() / 2);
        }

        @Override
        public int getYOffset() {
            return -getHeight();
        }

        @Override
        public void draw(Canvas canvas, float posx, float posy) {
            posx += getXOffset();
            posy = 0;
            if (posx > UIUtil.dip2px(270)) {
                if (posx > UIUtil.dip2px(290)) {
                    mBorrowContainer.setBackgroundResource(R.drawable.markerview_2);
                }else {
                    mBorrowContainer.setBackgroundResource(R.drawable.markerview_1);
                }
                posx = UIUtil.dip2px(267);
            }else {
                mBorrowContainer.setBackgroundResource(R.drawable.markerview_1);
            }

            canvas.translate(posx, posy);
            draw(canvas);
            canvas.translate(-posx, -posy);
        }

    }

    private static class CombinedDetailMarkerView extends MarkerView {

        private TextView tvContent;

        public CombinedDetailMarkerView(Context context, int layoutResource) {
            super(context, layoutResource);
            tvContent = (TextView) findViewById(R.id.tvContent);
            mCombinedContainer = (RelativeLayout) findViewById(R.id.rl_chart_markerview);
        }

        @Override
        public void refreshContent(Entry entry, Highlight highlight) {
            float profit =  entry.getVal();
            int xIndex = entry.getXIndex();
            float turnover = Float.parseFloat(dataList.get(xIndex).replace(",",""));
            //int turnover = Integer.parseInt(data + "");
            if(xIndex<0){xIndex=0;}
            if(xIndex>dataList.size()-1){xIndex = dataList.size()-1;}
            String date = dateList.get(xIndex);
            tvContent.setText("日期：" + date + "\n成交量：" + turnover + "万\n利率：" + profit + "%");
            tvContent.setTextSize(9f);
            tvContent.setTextColor(Color.BLACK);
        }

        @Override
        public int getXOffset() {
            return -(getWidth() / 2);
        }

        @Override
        public int getYOffset() {
            return -getHeight();
        }

        @Override
        public void draw(Canvas canvas, float posx, float posy) {
            posx += getXOffset();
            posy = 0;
            if (posx > UIUtil.dip2px(270)) {
                if (posx > UIUtil.dip2px(285)) {
                    mCombinedContainer.setBackgroundResource(R.drawable.markerview_2);
                }else {
                    mCombinedContainer.setBackgroundResource(R.drawable.markerview_1);
                }
                posx = UIUtil.dip2px(270);
            }else {
                mCombinedContainer.setBackgroundResource(R.drawable.markerview_1);
            }

            canvas.translate(posx, posy);
            draw(canvas);
            canvas.translate(-posx, -posy);
        }

    }


    private static class ProfitTendencyMarkerView extends MarkerView {

        private TextView tvContent;
        private Context context;

        public ProfitTendencyMarkerView(Context context, int layoutResource) {
            super(context, layoutResource);
            this.context = context;
            tvContent = (TextView) findViewById(R.id.tvContent);
            mCombinedContainer = (RelativeLayout) findViewById(R.id.rl_chart_markerview);
        }

        @Override
        public void refreshContent(Entry entry, Highlight highlight) {
            float profit =  entry.getVal();
            int xIndex = entry.getXIndex();
            if(xIndex<0){xIndex=0;}
            if(xIndex>dataList.size()-1){xIndex = dataList.size()-1;}
            String date = dateList.get(xIndex);
            String str = "日期：" + date +  "\n加权年化率：" + profit + "%";
            tvContent.setTextSize(9f);
            Util.setSpannable(tvContent,str,20,context.getResources().getColor(R.color.gesture_bg_color));
        }

        @Override
        public int getXOffset() {
            return -(getWidth() / 2);
        }

        @Override
        public int getYOffset() {
            return -getHeight();
        }

        @Override
        public void draw(Canvas canvas, float posx, float posy) {
            posx += getXOffset();
            posy = 0;
            if (posx > UIUtil.dip2px(270)) {
                if (posx > UIUtil.dip2px(285)) {
                    mCombinedContainer.setBackgroundResource(R.drawable.markerview_2);
                }else {
                    mCombinedContainer.setBackgroundResource(R.drawable.markerview_1);
                }
                posx = UIUtil.dip2px(270);
            }else {
                mCombinedContainer.setBackgroundResource(R.drawable.markerview_1);
            }

            canvas.translate(posx, posy);
            draw(canvas);
            canvas.translate(-posx, -posy);
        }

    }

    private static class DeepDataMarkerView extends MarkerView {

        private TextView tvContent;

        public DeepDataMarkerView(Activity context, int layoutResource) {
            super(context, layoutResource);
            tvContent = (TextView) findViewById(R.id.tvContent);
            mDeepDataContainer = (RelativeLayout) findViewById(R.id.rl_chart_markerview);
        }

        @Override
        public void refreshContent(Entry entry, Highlight highlight) {
            int xIndex = entry.getXIndex();
            String date = deepDataDateList.get(xIndex);//日期
            if (dataList1.get(1) == 0){
                float data1 =  Float.parseFloat(dataList.get(xIndex).replace(",",""));
                if (dataList1.get(0) - 1 == 4 || dataList1.get(0) - 1 == 5 || dataList1.get(0) - 1 == 8|| dataList1.get(0) - 1 == 9|| dataList1.get(0) - 1 == 10|| dataList1.get(0) - 1 == 11){
                    tvContent.setText("日期：" + date + "\n" + StringUtil.format(dataType[dataList1.get(0) - 1], String.valueOf(dataList.get(xIndex))));
                }else{
                    tvContent.setText("日期：" + date + "\n" + StringUtil.format(dataType[dataList1.get(0) - 1], String.valueOf(data1)));
                }

            }else {
                float data1 = Float.parseFloat(dataList.get(xIndex).replace(",",""));//左边Y轴数据
                float data2 = entry.getVal();//右边Y轴数据
                if ((dataList1.get(0) - 1 == 4 || dataList1.get(0) - 1 == 5|| dataList1.get(0) - 1 == 8|| dataList1.get(0) - 1 == 9|| dataList1.get(0) - 1 == 10|| dataList1.get(0) - 1 == 11) && (dataList1.get(1) - 1 != 4 && dataList1.get(1) - 1 != 5&& dataList1.get(1) - 1 != 8&& dataList1.get(1) - 1 != 9&& dataList1.get(1) - 1 != 10&& dataList1.get(1) - 1 != 11)) {
                    //只有左边坐标轴是借款人数或者投资人数的数据……
                    LogUtil.e("data2  =" + data2);
                    tvContent.setText("日期：" + date + "\n" + StringUtil.format(dataType[dataList1.get(0) - 1], String.valueOf(dataList.get(xIndex))) + "\n" + StringUtil.format(dataType[dataList1.get(1) - 1], String.valueOf(data2)));

                }else if ((dataList1.get(0) - 1 != 4 && dataList1.get(0) - 1 != 5 && dataList1.get(0) - 1 != 8&& dataList1.get(0) - 1 != 9&& dataList1.get(0) - 1 != 10&& dataList1.get(0) - 1 != 11) && (dataList1.get(1) - 1 == 4 || dataList1.get(1) - 1 == 5|| dataList1.get(1) - 1 == 8|| dataList1.get(1) - 1 == 9|| dataList1.get(1) - 1 == 10|| dataList1.get(1) - 1 == 11)) {
                    //只有右边坐标轴是借款人数或者投资人数的数据……
                    LogUtil.e("右边Y轴date2数据为：" + data2);
                    String d = (data2 + "").substring(0, (data2 + "").length() - 2);

                    tvContent.setText("日期：" + date + "\n" + StringUtil.format(dataType[dataList1.get(0) - 1], String.valueOf(data1)) + "\n" + StringUtil.format(dataType[dataList1.get(1) - 1], String.valueOf(d)));
                }else if ((dataList1.get(0) - 1 == 4 || dataList1.get(0) - 1 == 5|| dataList1.get(0) - 1 == 8|| dataList1.get(0) - 1 == 9|| dataList1.get(0) - 1 == 10|| dataList1.get(0) - 1 == 11) && (dataList1.get(1) - 1 == 4 || dataList1.get(1) - 1 == 5|| dataList1.get(1) - 1 == 8|| dataList1.get(1) - 1 == 9|| dataList1.get(1) - 1 == 10|| dataList1.get(1) - 1 == 11)){
                    //左右两边都是借款人数或者投资人数的数据……
                    LogUtil.e("左右两边date2数据为：" + data2);
                    String d = (data2 + "").substring(0, (data2 + "").length() - 2);

                    tvContent.setText("日期：" + date + "\n" + StringUtil.format(dataType[dataList1.get(0) - 1], String.valueOf(dataList.get(xIndex))) + "\n" + StringUtil.format(dataType[dataList1.get(1) - 1], String.valueOf(d)));

                }else {
                    tvContent.setText("日期：" + date + "\n" + StringUtil.format(dataType[dataList1.get(0) - 1], String.valueOf(data1)) + "\n" + StringUtil.format(dataType[dataList1.get(1) - 1], String.valueOf(data2)));
                }
            }
            tvContent.setTextSize(9f);
            tvContent.setTextColor(Color.BLACK);
        }

        @Override
        public int getXOffset() {
            return -(getWidth() / 2);
        }

        @Override
        public int getYOffset() {
            return -getHeight();
        }

        @Override
        public void draw(Canvas canvas, float posx, float posy) {
            posx += getXOffset();
            posy = 0;
            if (posx > UIUtil.dip2px(471)) {
                if (posx > UIUtil.dip2px(481)) {
                    mDeepDataContainer.setBackgroundResource(R.drawable.markerview_2);
                }else {
                    mDeepDataContainer.setBackgroundResource(R.drawable.markerview_1);
                }
                posx = UIUtil.dip2px(468);
            }else {
                mDeepDataContainer.setBackgroundResource(R.drawable.markerview_1);
            }

            canvas.translate(posx, posy);
            draw(canvas);
            canvas.translate(-posx, -posy);
        }

    }

}
