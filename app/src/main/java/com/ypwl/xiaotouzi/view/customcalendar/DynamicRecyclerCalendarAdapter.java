package com.ypwl.xiaotouzi.view.customcalendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.utils.LogUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * function :日历月份适配器
 * <p/>
 * Created by tengtao on 2016/4/22.
 */
public class DynamicRecyclerCalendarAdapter extends RecyclerView.Adapter<DynamicRecyclerCalendarAdapter.ViewHolder> implements DynamicMonthView.OnDayClickListener {
    protected static final int MONTHS_IN_YEAR = 12;
    private Context mContext;
    private Calendar mCalendar;
    private CustomCalendarListener mCalendarListener;
    private final TypedArray typedArray;
    private final Integer firstMonth;
    //    private final Integer lastMonth;
    private Map<String,Integer> selectedMaps = new HashMap<>();
    private List<CalendarDayBean> list = new ArrayList<>();
    private int previousMonthCount;

    public DynamicRecyclerCalendarAdapter(Context context, CustomCalendarListener listener, TypedArray typedArray, int previousMonthCount) {
        this.mCalendarListener = listener;
        this.mContext = context;
        this.typedArray = typedArray;
        this.previousMonthCount = previousMonthCount;
        list.clear();
        mCalendar = Calendar.getInstance();
        firstMonth = typedArray.getInt(R.styleable.DynamicCalendarView_first_Month, 0);
//        lastMonth = typedArray.getInt(R.styleable.DynamicCalendarView_last_Month, 11);
        if (typedArray.getBoolean(R.styleable.DynamicMonthView_currentDaySelected, false)) {
            //标记当前天
            if (typedArray.getBoolean(R.styleable.DynamicMonthView_currentDaySelected, false))
                onDayTapped(new CalendarDayBean(System.currentTimeMillis()));//标记当前天,默认选中当天
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final DynamicMonthView dynamicMonthView = new DynamicMonthView(mContext, typedArray);
        return new ViewHolder(dynamicMonthView, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final DynamicMonthView dynamicMonthView = holder.dynamicMonthView;
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
    }

    @Override
    public int getItemCount() {
        int itemCount = 99 * MONTHS_IN_YEAR;
        return itemCount;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public DynamicMonthView dynamicMonthView;
        public ViewHolder(View itemView, DynamicMonthView.OnDayClickListener onDayClickListener) {
            super(itemView);
            dynamicMonthView = (DynamicMonthView) itemView;
            dynamicMonthView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            dynamicMonthView.setClickable(true);
            dynamicMonthView.setOnDayClickListener(onDayClickListener);
        }
    }

    public void refreshSelected(Map<String,Integer> selectedInfos){
        selectedMaps.clear();
        selectedMaps.putAll(selectedInfos);
        notifyDataSetChanged();
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
            LogUtil.e("DynamicRecyclerCalendarAdapter:", calendarDay.year + ":" + (calendarDay.month+1) + ":" + calendarDay.day);
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
