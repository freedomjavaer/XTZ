package com.ypwl.xiaotouzi.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * function : SharedPreferences管理类.<br/>
 * Modify by lzj on 2015/11/4.
 */
public class CacheUtils {
    private final static String SP_NAME = "xtz";
    private static SharedPreferences sp;

    private static SharedPreferences getSp(Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        return sp;
    }

    /**
     * 获取boolean 数据
     */
    public static boolean getBoolean(String key, boolean defValue) {
        return getSp(UIUtil.getContext()).getBoolean(key, defValue);
    }

    /**
     * 存boolean缓存
     */
    public static void putBoolean(String key, boolean value) {
        getSp(UIUtil.getContext()).edit().putBoolean(key, value).apply();
    }

    /**
     * 获取boolean 数据
     */
    public static boolean getBoolean(String spFileName, String key, boolean defValue) {
        return UIUtil.getContext().getSharedPreferences(spFileName, Context.MODE_PRIVATE).getBoolean(key, defValue);
    }

    /**
     * 存boolean缓存
     */
    public static void putBoolean(String spFileName, String key, boolean value) {
        UIUtil.getContext().getSharedPreferences(spFileName, Context.MODE_PRIVATE).edit().putBoolean(key, value).apply();
    }

    /**
     * 获取String 数据
     */
    public static String getString(String key, String defValue) {
        return getSp(UIUtil.getContext()).getString(key, defValue);
    }

    /**
     * 存String缓存
     */
    public static void putString(String key, String value) {
        getSp(UIUtil.getContext()).edit().putString(key, value).apply();
    }

    /**
     * 获取String 数据
     */
    public static String getString(String spFileName, String key, String defValue) {
        return UIUtil.getContext().getSharedPreferences(spFileName, Context.MODE_PRIVATE).getString(key, defValue);
    }

    /**
     * 存String缓存
     */
    public static void putString(String spFileName, String key, String value) {
        UIUtil.getContext().getSharedPreferences(spFileName, Context.MODE_PRIVATE).edit().putString(key, value).apply();
    }

    /**
     * 获取Long 数据
     */
    public static Long getLong(String key, Long defValue) {
        return getSp(UIUtil.getContext()).getLong(key, defValue);
    }

    /**
     * 存long缓存
     */
    public static void putLong(String key, Long value) {
        getSp(UIUtil.getContext()).edit().putLong(key, value).apply();
    }


    /**
     * 获取Long 数据
     */
    public static Long getLong(String spFileName, String key, Long defValue) {
        return UIUtil.getContext().getSharedPreferences(spFileName, Context.MODE_PRIVATE).getLong(key, defValue);
    }

    /**
     * 存long缓存
     */
    public static void putLong(String spFileName, String key, Long value) {
        UIUtil.getContext().getSharedPreferences(spFileName, Context.MODE_PRIVATE).edit().putLong(key, value).apply();
    }

    /**
     * 存int缓存
     */
    public static void putInt(String key, int value) {
        getSp(UIUtil.getContext()).edit().putInt(key, value).apply();
    }

    /**
     * 取int缓存
     */
    public static int getInt(String key, int defValue) {
        return getSp(UIUtil.getContext()).getInt(key, defValue);
    }


    /**
     * 存int缓存
     */
    public static void putInt(String spFileName, String key, int value) {
        UIUtil.getContext().getSharedPreferences(spFileName, Context.MODE_PRIVATE).edit().putInt(key, value).apply();
    }

    /**
     * 取int缓存
     */
    public static int getInt(String spFileName, String key, int defValue) {
        return UIUtil.getContext().getSharedPreferences(spFileName, Context.MODE_PRIVATE).getInt(key, defValue);
    }
}
