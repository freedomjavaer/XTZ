package com.ypwl.xiaotouzi.utils;

import com.ypwl.xiaotouzi.common.Configs;

/**
 * <p>日志输出<p/>
 * Created by lzj on 2015/11/2.
 */
@SuppressWarnings("unused")
public final class LogUtil {

    public static void w(String tag, String content) {
        if (Configs.DEBUG) {
            android.util.Log.w(tag, content);
        }
    }

    public static void w(final String tag, Object... objs) {
        if (Configs.DEBUG) {
            android.util.Log.w(tag, getInfo(objs));
        }
    }

    public static void i(String tag, String content) {
        if (Configs.DEBUG) {
            android.util.Log.i(tag, content);
        }
    }

    public static void i(final String tag, Object... objs) {
        if (Configs.DEBUG) {
            android.util.Log.i(tag, getInfo(objs));
        }
    }

    public static void d(String tag, String content) {
        if (Configs.DEBUG) {
            android.util.Log.d(tag, content);
        }
    }

    public static void d(final String tag, Object... objs) {
        if (Configs.DEBUG) {
            android.util.Log.d(tag, getInfo(objs));
        }
    }

    public static void e(String tag, String content) {
        if (Configs.DEBUG) {
            android.util.Log.e(tag, content);
        }
    }

    public static void e(String tag, String content, Throwable e) {
        if (Configs.DEBUG) {
            android.util.Log.e(tag, content, e);
        }
    }

    public static void e(final String tag, Object... objs) {
        if (Configs.DEBUG) {
            android.util.Log.e(tag, getInfo(objs));
        }
    }

    private static String getInfo(Object... objs) {
        StringBuilder sb = new StringBuilder();
        if (null == objs) {
            sb.append("no mesage.");
        } else {
            for (Object object : objs) {
                sb.append(object);
            }
        }
        return sb.toString();
    }

    public static void sysOut(Object msg) {
        if (Configs.DEBUG) {
            System.out.println(msg);
        }
    }

    public static void sysErr(Object msg) {
        if (Configs.DEBUG) {
            System.err.println(msg);
        }
    }
}
