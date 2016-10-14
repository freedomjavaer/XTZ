package com.ypwl.xiaotouzi.view.customcalendar;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;

import java.util.Map;

/**
 * function : 自定义日历
 * <p/>
 * Created by tengtao on 2016/4/23.
 */
public class CustomCalendar extends LinearLayout implements OnCurrentYearAndMonthListener {
    private TextView mTvYearMonth;
    private DynamicRecyclerCalendar mDynamicCalendarView;
    private VerticalViewPagerCalendar mVerticalViewPagerCalendar;
    private int mType = 1;
    /** 显示viewpager日历 */
    public static final int TYPE_VIEWPAGER = 1;
    /** 显示recyclerview日历 */
    public static final int TYPE_RECYCLERVIEW = 2;

    public CustomCalendar(Context context) {
        this(context, null);
    }

    public CustomCalendar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomCalendar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /** 初始化 */
    private void init(Context context) {
        View.inflate(context, R.layout.layout_custom_calendar, this);
        mTvYearMonth = (TextView) findViewById(R.id.tv_custom_calendar_year_month);
        /** 初始化recyclerview日历 */
        mDynamicCalendarView = (DynamicRecyclerCalendar) findViewById(R.id.custom_dynamic_calendar_view);
        mDynamicCalendarView.setOnCurrentYearAndMonthListener(this);
        /** 初始化viewpager日历 */
        mVerticalViewPagerCalendar = (VerticalViewPagerCalendar) findViewById(R.id.vertical_view_pager);
        mVerticalViewPagerCalendar.setOnCurrentYearAndMonthListener(this);
    }

    /** 设置日历显示类型 */
    public void setCalendarType(int type){
        this.mType = type;
        mDynamicCalendarView.setVisibility(mType == TYPE_RECYCLERVIEW?VISIBLE:GONE);
        mVerticalViewPagerCalendar.setVisibility(mType == TYPE_RECYCLERVIEW?GONE:VISIBLE);
    }

    /**
     * 设置监听
     * @param listener
     */
    public void setDynamicCalendarListener(CustomCalendarListener listener){
        if(mType == TYPE_RECYCLERVIEW){
            mDynamicCalendarView.setDynamicCalendarListener(listener);
        }else{
            mVerticalViewPagerCalendar.setDynamicCalendarListener(listener);
        }
    }

    /** 设置已标记的日期 */
    public void setSelectedData(Map<String,Integer>  backInfos){
        if(mType == TYPE_RECYCLERVIEW){
            mDynamicCalendarView.setSelectedData(backInfos);
        }else{
            mVerticalViewPagerCalendar.setSelectedData(backInfos);
        }
    }

    @Override
    public void onCurrentYearMonth(String yearAndMonth) {
        mTvYearMonth.setText(yearAndMonth);
    }

    /**设置过去的月数 */
    public void setPreviousMonthCount(int count){
        if(mType == TYPE_RECYCLERVIEW){
            mDynamicCalendarView.setPreviousMonthCount(count);
        }else{
            mVerticalViewPagerCalendar.setPreviousMonthCount(count);
        }
    }
}
