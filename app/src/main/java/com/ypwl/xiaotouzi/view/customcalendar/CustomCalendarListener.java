package com.ypwl.xiaotouzi.view.customcalendar;

/**
 * function : 日历接口
 * <p/>
 * Created by tengtao on 2016/4/22.
 */
public interface CustomCalendarListener {

    /**选择的日期*/
    void onDayOfMonthSelected(int year, int month, int day);

    /**月改变回调 */
    void onMonthChanged(int year, int month);
}
