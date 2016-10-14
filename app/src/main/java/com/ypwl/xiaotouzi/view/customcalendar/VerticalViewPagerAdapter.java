package com.ypwl.xiaotouzi.view.customcalendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.utils.LogUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * function :用viewpager做日历容器
 * <p/>
 * Created by tengtao on 2016/4/27.
 */
public class VerticalViewPagerAdapter extends PagerAdapter implements DynamicMonthView.OnDayClickListener{

    private Context mContext;
    private TypedArray typedArray;
    protected static final int MONTHS_IN_YEAR = 12;
    private Calendar mCalendar;
    private CustomCalendarListener mCalendarListener;
    private final Integer firstMonth;
    private List<CalendarDayBean> list = new ArrayList<>();
    private Map<String,Integer> selectedMaps = new HashMap<>();
    private int previousMonthCount;


    public VerticalViewPagerAdapter(Context context, CustomCalendarListener listener, TypedArray typedArray, int previousMonthCount){
        this.mContext = context;
        this.typedArray = typedArray;
        this.previousMonthCount = previousMonthCount;
        mCalendarListener = listener;
        mCalendar = Calendar.getInstance();
        firstMonth = typedArray.getInt(R.styleable.DynamicCalendarView_first_Month, 0);
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        DynamicMonthView dynamicMonthView = new DynamicMonthView(mContext,typedArray);
        dynamicMonthView.setOnDayClickListener(this);
        HashMap<String, Object> drawingParams = new HashMap<String, Object>();
        //获取年月
        int month = (firstMonth + (position % MONTHS_IN_YEAR)) % MONTHS_IN_YEAR;
        int year = position / MONTHS_IN_YEAR - previousMonthCount / MONTHS_IN_YEAR + mCalendar.get(Calendar.YEAR);

        dynamicMonthView.reuse();//重置

        int selectedDay = -1;
        int selectedMonth = -1;
        int selectedYear = -1;

        if (list.size()>0) {
            selectedDay = list.get(0).day;
            selectedMonth = list.get(0).month;
            selectedYear = list.get(0).year;
        }
        drawingParams.put(DynamicMonthView.VIEW_PARAMS_SELECTED_YEAR, selectedYear);
        drawingParams.put(DynamicMonthView.VIEW_PARAMS_SELECTED_MONTH, selectedMonth);
        drawingParams.put(DynamicMonthView.VIEW_PARAMS_SELECTED_DAY, selectedDay);
        //必须设置当前年月
        drawingParams.put(DynamicMonthView.VIEW_PARAMS_YEAR, year);
        drawingParams.put(DynamicMonthView.VIEW_PARAMS_MONTH, month);
        drawingParams.put(DynamicMonthView.VIEW_PARAMS_WEEK_START, mCalendar.getFirstDayOfWeek());
        drawingParams.put(DynamicMonthView.VIEW_PARAMS_DAY_OF_MONTH_SELECTED, selectedMaps);
        dynamicMonthView.setMonthParams(drawingParams);

        dynamicMonthView.invalidate();
        container.addView(dynamicMonthView);
        dynamicMonthView.setId(position);
        return dynamicMonthView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((DynamicMonthView)object);
    }


    /**设置选中数据 */
    public void refreshSelected(Map<String,Integer> selectedInfos, DynamicMonthView currentChildView){
        selectedMaps.clear();
        selectedMaps.putAll(selectedInfos);
        LogUtil.e("viewpageradapter","更新数据");
        if(currentChildView!=null)
            currentChildView.setSelectedMaps(selectedMaps);
    }

    /** 标记某一天 */
    protected void onDayTapped(CalendarDayBean calendarDay) {
        mCalendarListener.onDayOfMonthSelected(calendarDay.year, calendarDay.month, calendarDay.day);
        list.clear();
        list.add(calendarDay);//选中某一天
        notifyDataSetChanged();
    }

    /** 选中某一天 */
    @Override
    public void onDayClick(DynamicMonthView dynamicMonthView, CalendarDayBean calendarDay) {
        //TODO:判断某一天是否有回款
        if (calendarDay != null) {
            LogUtil.e("VerticalViewPagerAdapter:", calendarDay.year + ":" + (calendarDay.month+1) + ":" + calendarDay.day);
            onDayTapped(calendarDay);
        }
    }

    public long getYearAndMonth(int position){
        Calendar instance = Calendar.getInstance();
        int month = (firstMonth + (position % MONTHS_IN_YEAR)) % MONTHS_IN_YEAR;
        int year = position / MONTHS_IN_YEAR - previousMonthCount / MONTHS_IN_YEAR + mCalendar.get(Calendar.YEAR);
        instance.set(Calendar.MONTH, month);
        instance.set(Calendar.YEAR, year);
        long millis = instance.getTimeInMillis();
        return millis;
    }
}
