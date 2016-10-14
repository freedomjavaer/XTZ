package com.ypwl.xiaotouzi.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.event.InvestAnalysisFenBuSelectedEvent;
import com.ypwl.xiaotouzi.manager.EventHelper;
import com.ypwl.xiaotouzi.utils.ChartUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * function : 投资分析：投资分布--饼状图
 * <p/>
 * Created by tengtao on 2016/4/11.
 */
public class InvestAnalysisFenbuPieChart extends RelativeLayout {

    private PieChart mPieChart;
    private TextView mTvTypeName,mTvValue1,mTvValue2,mTvReturnedType;
    private int mCurrHighlightIndex=-1;
    private int mWidth, mHeight;

    public InvestAnalysisFenbuPieChart(Context context) {
        super(context);
        init(context);
    }

    public InvestAnalysisFenbuPieChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public InvestAnalysisFenbuPieChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View.inflate(context, R.layout.layout_invest_fen_bu_platform_pie_chart_view,this);
        mPieChart = (PieChart) findViewById(R.id.invest_fenbu_platform_piechart);
        mTvTypeName = (TextView) findViewById(R.id.tv_type_name);
        mTvValue1 = (TextView) findViewById(R.id.tv_type_value1);
        mTvValue2 = (TextView) findViewById(R.id.tv_type_value2);
        mTvReturnedType = (TextView) findViewById(R.id.tv_invest_fenbu_returned_type);
    }

    public void initData(List<String> names, List<String> values, String centerText, int currentFragmenType){

        mPieChart.setHoleColorTransparent(true);
        mPieChart.setHoleRadius(60f);  //半径,百分比
        mPieChart.setTransparentCircleRadius(64f); // 半透明圈百分比
        mPieChart.setDescription("");
        mPieChart.setUsePercentValues(true);  //显示成百分比
        mPieChart.setDrawCenterText(false);  //饼状图中间可以添加文字

        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setRotationAngle(-90); // 初始旋转角度
        mPieChart.setRotationEnabled(false); // 可以手动旋转
        Legend mLegend = mPieChart.getLegend();  //不设置比例图
        mLegend.setEnabled(false);
        mTvReturnedType.setVisibility(currentFragmenType==3?VISIBLE:GONE);
        //中心文字
        switch (currentFragmenType){
            case 0://平台
                mTvTypeName.setText("投资总额");
                if(centerText.contains(".")){
                    int i = centerText.indexOf(".");
                    mTvValue1.setText(centerText.substring(0,i));
                    mTvValue2.setText(centerText.substring(i,centerText.length()));
                }else{
                    mTvValue1.setText(centerText);
                    mTvValue2.setText(".00");
                }
                break;
            case 1://周期
                mTvTypeName.setText("平均周期");
                if(centerText.contains(".")){
                    int i = centerText.indexOf(".");
                    mTvValue1.setText(centerText.substring(0,i));
                    mTvValue2.setText(centerText.substring(i,centerText.length())+"天");
                }else{
                    mTvValue1.setText(centerText);
                    mTvValue2.setText(".0天");
                }
                break;
            case 2://年化率
                mTvTypeName.setText("平均年化率");
                if(centerText.contains(".")){
                    int i = centerText.indexOf(".");
                    mTvValue1.setText(centerText.substring(0,i));
                    mTvValue2.setText(centerText.substring(i,centerText.length())+"%");
                }else{
                    mTvValue1.setText(centerText);
                    mTvValue2.setText(".0%");
                }
                break;
            case 3://还款方式
                mTvTypeName.setVisibility(GONE);
                mTvValue1.setVisibility(GONE);
                mTvValue2.setVisibility(GONE);
                break;
        }

        //pieChart.animateXY(1000, 1000);  //设置动画
        //给饼状图设置数据
        ArrayList<String> xValues = new ArrayList<>();  //xVals用来表示每个饼块上的内容
        ArrayList<Entry> yValues = new ArrayList<>();  //yVals用来表示封装每个饼块的实际数据
        // 饼图颜色
        ArrayList<Integer> colors = new ArrayList<>();
        for (int i=0;i<values.size();i++) {
            colors.add(Color.parseColor(ChartUtils.pieColors[i]));
            xValues.add("");
            yValues.add(new Entry(Float.parseFloat(values.get(i).replace(",","")), i));
        }
        //y轴的集合
        PieDataSet pieDataSet = new PieDataSet(yValues, "");
        pieDataSet.setSliceSpace(0f); //设置个饼状图之间的距离
        pieDataSet.setDrawValues(false);//不显示描述
        pieDataSet.setColors(colors);

        //设置数据
        PieData data = new PieData(xValues, pieDataSet);
        mPieChart.setData(data);
        mPieChart.invalidate();

        mPieChart.setOnChartValueSelectedListener(mChartValueSelectedListener);
    }

    private OnChartValueSelectedListener mChartValueSelectedListener = new OnChartValueSelectedListener() {
        @Override
        public void onValueSelected(Entry entry, int i, Highlight highlight) {
            int xIndex = entry.getXIndex();
            /** 通过列表点起的，再次点击为不选中 */
            if(mCurrHighlightIndex==xIndex){
                mPieChart.highlightValue(mCurrHighlightIndex, -1);
                mCurrHighlightIndex = -1;
            }else{
                mCurrHighlightIndex = xIndex;
                mPieChart.highlightValue(mCurrHighlightIndex, 0);
            }
            mPieChart.invalidate();
            EventHelper.post(new InvestAnalysisFenBuSelectedEvent(xIndex));
        }

        @Override
        public void onNothingSelected() {
            mCurrHighlightIndex = -1;
            EventHelper.post(new InvestAnalysisFenBuSelectedEvent(-1));
        }
    };


    /** 选择高亮 */
    public void highlight(int position, boolean isHigh) {
        mCurrHighlightIndex = isHigh ? position : -1;
        mPieChart.highlightValue(position, isHigh ? 0 : -1);
        mPieChart.invalidate();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mWidth = this.getWidth();
                mHeight = this.getHeight();
                break;
            case MotionEvent.ACTION_UP:
                int mPieChartWidth = mPieChart.getWidth();//饼状图宽度
                //点击点：与中心点的x、y方向上的差值--->与中心点的距离
                float dx = ev.getX() - mWidth / 2;
                float dy = ev.getY() - mHeight / 2;
                double sqrt = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
                //空洞的半径
                double dl = mPieChartWidth * 0.6 / 2;
//                LogUtil.e("InvestAnalysisFenbuPiechart","piechartwidth="+mPieChartWidth+",设定界限=="+dl+",差值sqrt=="+sqrt);
                if(sqrt < dl){
                    return true;//触摸空洞内拦截不分发
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
