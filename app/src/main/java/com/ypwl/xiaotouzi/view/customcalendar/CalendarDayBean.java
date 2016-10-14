package com.ypwl.xiaotouzi.view.customcalendar;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * function : 日历的天数据对象
 * <p/>
 * Created by tengtao on 2016/4/24.
 */
public class CalendarDayBean implements Serializable {
    private Calendar calendar = Calendar.getInstance();
    int day,month,year;

    public CalendarDayBean() {
        setTime(System.currentTimeMillis());
    }

    public CalendarDayBean(int year, int month, int day) {
        setDay(year, month, day);
    }

    public CalendarDayBean(long timeInMillis) {
        setTime(timeInMillis);
    }

    public CalendarDayBean(Calendar calendar) {
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    private void setTime(long timeInMillis) {
        if (calendar == null) {
            calendar = Calendar.getInstance();
        }
        calendar.setTimeInMillis(timeInMillis);
        month = this.calendar.get(Calendar.MONTH);
        year = this.calendar.get(Calendar.YEAR);
        day = this.calendar.get(Calendar.DAY_OF_MONTH);
    }

    public void set(CalendarDayBean calendarDay) {
        year = calendarDay.year;
        month = calendarDay.month;
        day = calendarDay.day;
    }

    public void setDay(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public Date getDate() {
        if (calendar == null) {
            calendar = Calendar.getInstance();
        }
        calendar.set(year, month, day);
        return calendar.getTime();
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{ year: ");
        stringBuilder.append(year);
        stringBuilder.append(", month: ");
        stringBuilder.append(month);
        stringBuilder.append(", day: ");
        stringBuilder.append(day);
        stringBuilder.append(" }");

        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof CalendarDayBean) {
            CalendarDayBean other = (CalendarDayBean) o;
            if (other.year == this.year
                    && other.month == this.month
                    && other.day == this.day) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return calendar.hashCode() + 17 * day + 31 * month + year;
    }
}
