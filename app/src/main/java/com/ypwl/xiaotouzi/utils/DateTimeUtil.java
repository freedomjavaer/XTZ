package com.ypwl.xiaotouzi.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期时间工具类<br/>
 * <br/>
 * 2015年6月17日 - 下午5:08:09
 *
 * @author lizhijun
 */
@SuppressWarnings("unused")
public class DateTimeUtil {

    /** 日期格式：yyyy-MM-dd HH:mm:ss **/
    public static final String DF_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    /** 日期格式：yyyy-MM-dd HH:mm **/
    public static final String DF_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    /** 日期格式：yyyy-MM-dd **/
    public static final String DF_YYYY_MM_DD = "yyyy-MM-dd";
    /** 日期格式 yyyyMMdd */
    public static final String DF_YMD = "yyyyMMdd";
    /** 日期格式 yyyy/MM/dd */
    public static final String DF_YYYY_MM_DD_SPRIT = "yyyy/MM/dd";
    /** 日期格式：yyyy-MM **/
    public static final String DF_YYYY_MM = "yyyy-MM";
    /** 日期格式：yyyy-MM-dd **/
    public static final String DF_YYYY_MM_DD_DOT = "yyyy.MM.dd";
    /** 日期格式：HH:mm:ss **/
    public static final String DF_HH_MM_SS = "HH:mm:ss";
    /** 日期格式：HH:mm **/
    public static final String DF_HH_MM = "HH:mm";
    /** 日期格式：MM月dd日 **/
    public static final String DF_MM_YUE_DD = "MM月dd日";
    /** 日期格式：MM月dd HH:mm **/
    public static final String DF_MM_DD_HH_MM = "MM月dd日 HH:mm";
    /** 日期格式：yyyy年MM月dd日 **/
    public static final String DF_YYYYMMDD = "yyyy年MM月dd日";
    /** 日期格式：yyyy年 **/
    public static final String DF_YYYY = "yyyy年";
    /** 日期格式：MM月dd日 **/
    public static final String DF_MM_DD = "MM-dd";
    /** 日期格式：yyyy/MM/dd **/
    public static final String BID_DF_YYYY_MM_DD = "yyyy/MM/dd";

    public final static long minute = 60 * 1000;// 1分钟
    public final static long hour = 60 * minute;// 1小时
    public final static long day = 24 * hour;// 1天
    public final static long month = 31 * day;// 月
    public final static long year = 12 * month;// 年

    /** Log输出标识 **/
    private static final String TAG = DateTimeUtil.class.getSimpleName();

    /** 获取当前时间,单位为秒 */
    public static long getCurrentDateTimeSeconds() {
        return getCurrentDate().getTime() / 1000;
    }

    /** 获取当前时间,单位为毫秒 */
    public static long getCurrentDateTimeMills() {
        return getCurrentDate().getTime();
    }

    /**
     * 获取间隔时间，最大单位为天，最小为分钟
     *
     * @param currentMillis
     * @param targreMillis
     * @return
     */
    public static String getIntervalTimeDayMin(long currentMillis, long targreMillis) {
        long minute = 60 * 1000;// 1分钟
        long hour = 60 * minute;// 1小时
        long day = 24 * hour;// 1天
        long interval = currentMillis - targreMillis;
        if (interval > day) {
            return (interval / day) + "天前";
        } else if (interval > hour) {
            return (interval / hour) + "小时前";
        } else if (interval > minute) {
            return (interval / minute) + "分钟前";
        } else {
//            return (interval / 1000) + "秒前";
            return "刚刚";
        }
    }

    /**
     * 获取间隔时间(小时)
     *
     * @param currentDate 参考时间(当前时间)
     * @param otherDate   比较时间 (>参考时间)
     * @return
     */
    public static String getIntervalTimeHour(Date currentDate, Date otherDate) {
        String intervalTime = "0";
        if (currentDate == null || otherDate == null) {
            return intervalTime;
        }
        long diff = otherDate.getTime() - currentDate.getTime();
        if (diff > hour) {
            intervalTime = (diff / hour) + "";
        }
        return intervalTime + "小时";
    }

    /**
     * 获取间隔时间(天)
     *
     * @param currentDate 参考时间(当前时间)
     * @param otherDate   比较时间 (>参考时间)
     * @return
     */
    public static String getIntervalTimeDay(Date currentDate, Date otherDate) {
        String intervalTime = "0";
        if (currentDate == null || otherDate == null) {
            return intervalTime;
        }
        long diff = otherDate.getTime() - currentDate.getTime();
        if (Math.abs(diff) >= day) {
            intervalTime = (Math.abs(diff) / day) + "";
            return intervalTime + "天" + (diff > 0 ? "后" : "前");
        } else {
            return "今天";
        }
    }

    /**
     * 获取间隔时间
     *
     * @param currentDate 参考时间(当前时间)
     * @param otherDate   比较时间 (>参考时间)
     */
    @SuppressWarnings("ConstantConditions")
    public static String getIntervalTime(Date currentDate, Date otherDate) {
        String intervalTime = null;
        if (currentDate == null || otherDate == null) {
            return null;
        }
        long diff = otherDate.getTime() - currentDate.getTime();
        if (diff > year) {
            intervalTime += (diff / year) + "年";
            diff = diff % year;
        }
        if (diff > month) {
            intervalTime += (diff / month) + "个月";
            diff = diff % month;
        }
        if (diff > day) {
            intervalTime += (diff / day) + "天";
            diff = diff % day;
        }
        if (diff > hour) {
            intervalTime += (diff / hour) + "小时";
            diff = diff % hour;
        }
        if (diff > minute) {
            intervalTime += (diff / minute) + "分钟";
        }
        return intervalTime;
    }


    /**
     * 用于消息间隔时间显示提示
     */
    public static String formatDiffTimeForIMMsg(long preTime, long newTime) {
        int limitTime = 5;
        long diffTime = newTime - preTime;
        if (diffTime <= 0) {
            return null;
        }
        String result = null;
        if (isSameDay(preTime, newTime)) {
            if (isBeforeYesterday(newTime)) {//超过昨天的 显示：MM月dd日 HH:mm
                result = formatDateTime(newTime, DF_MM_DD_HH_MM);
            } else if (isYesterday(newTime) && diffTime > minute * limitTime) {//昨天的 显示：昨天 HH:mm
                result = "昨天 " + formatDateTime(newTime, DF_HH_MM);
            } else if (diffTime > minute * limitTime) {//今天内超过五分钟间隔的 显示：HH:mm
                result = formatDateTime(newTime, DF_HH_MM);
            }
        } else {//不是同一天的，显示：MM月dd日 HH:mm
            if (isToday(newTime)) {
                result = formatDateTime(newTime, DF_HH_MM);
            } else {
                result = formatDateTime(newTime, DF_MM_DD_HH_MM);
            }
        }
        return result;
    }

    /**
     * 用于收件箱日期显示提示
     */
    public static String formatMsgBoxDate(long time) {
        String result;
        if (isToday(time)) {//今天之内 显示：HH:mm
            result = formatDateTime(time, DF_HH_MM);
        } else if (isYesterday(time)) {//昨天的 显示：昨天
            result = "昨天";
        } else if (isSameYear(time,getCurrentDateTimeMills())) {//今年的 显示：MM-DD
            result = formatDateMMDD(time);
        } else {//超过昨天的 显示：MM月dd日
            result = formatDateTime(time, DF_YYYY_MM_DD);
        }
        return result;
    }

    /**
     * 将日期格式化成友好的字符串：几分钟前、几小时前、几天前、几月前、几年前、刚刚
     */
    public static String formatFriendly(Date date) {
        if (date == null) {
            return null;
        }
        long diff = new Date().getTime() - date.getTime();
        long r;
        if (diff > year) {
            r = (diff / year);
            return r + "年前";
        }
        if (diff > month) {
            r = (diff / month);
            return r + "个月前";
        }
        if (diff > day) {
            r = (diff / day);
            return r + "天前";
        }
        if (diff > hour) {
            r = (diff / hour);
            return r + "个小时前";
        }
        if (diff > minute) {
            r = (diff / minute);
            return r + "分钟前";
        }
        return "刚刚";
    }

    /**
     * 将当前时间之前的日期格式化成友好的字符串：当天：刚刚、几分钟前、几小时前；其他为yyyy年MM月dd日
     */
    public static String formatFriendly(long time) {
        long diff = new Date().getTime() - time;
        long r;
        if (diff > year) {
            return formatDateTime(new Date(time), DF_YYYYMMDD);
        }
        if (diff > day) {
            return formatDateTime(new Date(time), DF_MM_DD);
        }
        if (diff > hour) {
            r = (diff / hour);
            return r + "小时前";
        }
        if (diff > minute) {
            r = (diff / minute);
            return r + "分钟前";
        }
        return "刚刚";
    }

    /**
     * 判断指定时间是否为昨天之前的日期
     */
    public static boolean isBeforeYesterday(long time) {
        return time < new Date().getTime() - (day * 2);
    }

    /**
     * 判断指定时间是否为昨天
     */
    public static boolean isYesterday(long time) {
        Date newDate = new Date(time);
        String dateTime = formatDateTime(newDate, DF_YYYY_MM_DD);
        String nowTime = formatDateTime(new Date().getTime() - day, DF_YYYY_MM_DD);
        return !(StringUtil.isEmptyOrNull(dateTime) || StringUtil.isEmptyOrNull(nowTime)) && dateTime.equalsIgnoreCase(nowTime);
    }

    /**
     * 判断指定时间是否为当天
     */
    public static boolean isToday(long time) {
        Date newDate = new Date(time);
        String dateTime = formatDateTime(newDate, DF_YYYY_MM_DD);
        String nowTime = formatDateTime(new Date(), DF_YYYY_MM_DD);
        return !(StringUtil.isEmptyOrNull(dateTime) || StringUtil.isEmptyOrNull(nowTime)) && dateTime.equalsIgnoreCase(nowTime);
    }

    /**
     * 判断指定时间是否为同一天
     */
    public static boolean isSameDay(long time1, long time2) {
        Date date1 = new Date(time1);
        Date date2 = new Date(time2);
        String dateStr1 = formatDateTime(date1, DF_YYYY_MM_DD);
        String dateStr2 = formatDateTime(date2, DF_YYYY_MM_DD);
        return !(StringUtil.isEmptyOrNull(dateStr1) || StringUtil.isEmptyOrNull(dateStr2)) && dateStr1.equalsIgnoreCase(dateStr2);
    }

    /**
     * 判断指定时间是否为同一年
     */
    public static boolean isSameYear(long time1, long time2) {
        Date date1 = new Date(time1);
        Date date2 = new Date(time2);
        String dateStr1 = formatDateTime(date1, DF_YYYY_MM_DD);
        String dateStr2 = formatDateTime(date2, DF_YYYY_MM_DD);
        return !(StringUtil.isEmptyOrNull(dateStr1) || StringUtil.isEmptyOrNull(dateStr2)) && dateStr1.substring(0,3).equalsIgnoreCase(dateStr2.substring(0,3));
    }

    /**
     * 将日期以yyyy-MM-dd HH:mm:ss格式化
     */
    public static String formatDateTime(long dateL) {
        SimpleDateFormat sdf = new SimpleDateFormat(DF_YYYY_MM_DD_HH_MM_SS);
        Date date = new Date(dateL);
        LogUtil.e(TAG, "--------- sdf.format(date)--------" + sdf.format(date) + "------datal------" + dateL);
        return sdf.format(date);

    }

    /**
     * 将日期以yyyy-MM-dd 格式化
     */
    public static String formatDate(long dateL) {
        SimpleDateFormat sdf = new SimpleDateFormat(DF_YYYY_MM_DD);
        Date date = new Date(dateL * 1000);
        LogUtil.e(TAG, "--------- sdf.format(date)--------" + sdf.format(date) + "------datal------" + dateL);
        return sdf.format(date);
    }

    /**
     * 将日期以MM-dd 格式化
     */
    public static String formatDateMMDD(long dateL) {
        SimpleDateFormat sdf = new SimpleDateFormat(DF_MM_DD);
        Date date = new Date(dateL);
        LogUtil.e(TAG, "--------- sdf.format(date)--------" + sdf.format(date) + "------datal------" + dateL);
        return sdf.format(date);
    }


    /**
     * 将日期以yyyy-MM-dd HH:mm:ss格式化
     */
    public static String formatDateTime(long dateL, String formater) {
        SimpleDateFormat sdf = new SimpleDateFormat(formater);
        return sdf.format(new Date(dateL));
    }

    /**
     * 将日期以yyyy-MM-dd HH:mm:ss格式化
     */
    public static String formatDateTime(Date date, String formater) {
        SimpleDateFormat sdf = new SimpleDateFormat(formater);
        return sdf.format(date);
    }

    /**
     * 将日期字符串转成日期
     *
     * @param strDate 字符串日期
     * @return java.util.date日期类型
     */
    public static Date parseDate(String strDate, String formater) {
        DateFormat dateFormat = new SimpleDateFormat(formater);
        Date returnDate = null;
        try {
            returnDate = dateFormat.parse(strDate);
        } catch (ParseException e) {
            Log.w(TAG, "parseDate failed !");

        }
        return returnDate;

    }

    /**
     * 获取系统当前日期
     *
     * @return
     */
    public static Date getCurrentDate() {
        return new Date();
    }

    /**
     * 验证日期是否比当前日期早
     *
     * @param target1 比较时间1
     * @param target2 比较时间2
     * @return true 则代表target1比target2晚或等于target2，否则比target2早
     */
    public static boolean compareDate(Date target1, Date target2) {
        boolean flag = false;
        try {
            String target1DateTime = DateTimeUtil.formatDateTime(target1, DF_YYYY_MM_DD_HH_MM_SS);
            String target2DateTime = DateTimeUtil.formatDateTime(target2, DF_YYYY_MM_DD_HH_MM_SS);
            if (target1DateTime.compareTo(target2DateTime) <= 0) {
                flag = true;
            }
        } catch (Exception e1) {
            Log.w(TAG, "Exception: " + e1.getMessage());
        }
        return flag;
    }

    /**
     * 对日期进行增加操作
     *
     * @param target 需要进行运算的日期
     * @param hour   小时
     * @return
     */
    public static Date addDateTime(Date target, double hour) {
        if (null == target || hour < 0) {
            return target;
        }
        return new Date(target.getTime() + (long) (hour * 60 * 60 * 1000));
    }

    /**
     * 对日期进行相减操作
     *
     * @param target 需要进行运算的日期
     * @param hour   小时
     * @return
     */
    public static Date subDateTime(Date target, double hour) {
        if (null == target || hour < 0) {
            return target;
        }
        return new Date(target.getTime() - (long) (hour * 60 * 60 * 1000));
    }

    /**
     * 对年份日期进行处理
     *
     * @param date 传入需要处理的时间
     * @return 返回处理后的时间
     */
    public static String processDate(String date) {
        int index = date.indexOf("/");
        String proDate = date.substring(index + 1);
        return proDate;
    }

    /**
     * 对年份日期进行处理
     *
     * @param date 传入需要处理的时间
     * @return 返回处理后的时间
     */
    public static String dateCapture(String date) {
        int index = date.indexOf("/");
        String proDate = date.substring(0,index);
        return proDate;
    }




}
