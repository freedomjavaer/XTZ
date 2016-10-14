package com.ypwl.xiaotouzi.utils;

import java.text.DecimalFormat;

/**
 * function : 字符串工具类<br/>
 * Created by lzj on 2015/11/2.
 */
@SuppressWarnings("unused")
public class StringUtil {

    /** 判断字符串是否为空 */
    public static boolean isEmptyOrNull(String str) {
        boolean result = false;
        if ("".equals(str) || null == str || "null".equals(str)) {
            result = true;
        }
        return result;
    }

    /** 获取字符串的长度 */
    public static int getStringLength(String str) {
        int result = 0;
        if (!isEmptyOrNull(str)) {
            result = str.length();
        }
        return result;
    }

    /** 格式化字符串 */
    public static String format(int strResId, Object... args) {
        return format(UIUtil.getString(strResId), args);
    }

    /** 格式化字符串 */
    public static String format(String formatStr, Object... args) {
        return String.format(formatStr, args);
    }

    /** 获取字符串首字 */
    public static String getFirstWord(String str) {
        return str.substring(0, 1);
    }

    /** 去除字符串中的逗号 */
    public static String getFitString(String str) {
        return str.replaceAll(",", "");
    }

    /** 每三位数字增加一个逗号 */
    public static String formatTosepara(float data) {
        DecimalFormat df = new DecimalFormat("#,###.00");
        return df.format(data);
    }

}
