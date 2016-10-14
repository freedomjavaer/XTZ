package com.ypwl.xiaotouzi.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.ypwl.xiaotouzi.XtzApp;
import com.ypwl.xiaotouzi.utils.animation.Animation;
import com.ypwl.xiaotouzi.utils.animation.ShakeAnimation;

import java.lang.reflect.Field;

/**
 * UI工具类：包括常用功能：获取全局的上下文、主线程相关、主线程任务执行、资源id获取、资源获取、像数大小转换、自定义Toast<br/>
 * Created by lzj on 2015/11/2.
 */
@SuppressWarnings("unused")
public class UIUtil {
    private static final String TAG = UIUtil.class.getSimpleName();
    private static Toast mToast;

    /** 获取全局上下文 */
    public static Context getContext() {
        return XtzApp.getApplication();
    }

    /** 获取主线程 */
    public static Thread getMainThread() {
        return XtzApp.getMainThread();
    }

    /** 获取主线程ID */
    public static long getMainThreadId() {
        return XtzApp.getMainThreadId();
    }


    /** 获取主线程消息轮询器 */
    public static android.os.Looper getMainLooper() {
        return XtzApp.getMainThreadLooper();
    }

    /** 获取主线程的handler */
    public static Handler getHandler() {
        return XtzApp.getMainThreadHandler();
    }

    /** 在主线程中延时一定时间执行runnable */
    public static boolean postDelayed(Runnable runnable, long delayMillis) {
        return getHandler().postDelayed(runnable, delayMillis);
    }

    /** 在主线程执行runnable */
    public static boolean post(Runnable runnable) {
        return getHandler().post(runnable);
    }

    /** 从主线程looper里面移除runnable */
    public static void removeCallbacksFromMainLooper(Runnable runnable) {
        getHandler().removeCallbacks(runnable);
    }

    /** 判断当前的线程是否为主线程 */
    public static boolean isRunInMainThread() {
        return android.os.Process.myTid() == getMainThreadId();
    }

    /** 在主线程中运行任务 */
    public static void runInMainThread(Runnable runnable) {
        if (isRunInMainThread()) {
            runnable.run();
        } else {
            UIUtil.post(runnable);
        }
    }

    /** ----------------------根据资源名获取资源id------start------------------------- */
    private static Context mActivity = getContext();
    private static Class<?> CDrawable = null;
    private static Class<?> CLayout = null;
    private static Class<?> CId = null;
    private static Class<?> CAnim = null;
    private static Class<?> CStyle = null;
    private static Class<?> CString = null;
    private static Class<?> CArray = null;

    static {
        try {
            CDrawable = Class.forName(mActivity.getPackageName() + ".R$drawable");
            CLayout = Class.forName(mActivity.getPackageName() + ".R$layout");
            CId = Class.forName(mActivity.getPackageName() + ".R$id");
            CAnim = Class.forName(mActivity.getPackageName() + ".R$anim");
            CStyle = Class.forName(mActivity.getPackageName() + ".R$style");
            CString = Class.forName(mActivity.getPackageName() + ".R$string");
            CArray = Class.forName(mActivity.getPackageName() + ".R$array");
        } catch (ClassNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /** 获取指定名称的drawable...资源ID */
    public static int getDrawableId(String resName) {
        return getResId(CDrawable, resName);
    }

    /** 获取指定名称的layout文件资源ID */
    public static int getLayoutId(String resName) {
        return getResId(CLayout, resName);
    }

    /** 获取指定名称的id资源ID */
    public static int getIdId(String resName) {
        return getResId(CId, resName);
    }

    /** 获取指定名称的anim资源ID */
    public static int getAnimId(String resName) {
        return getResId(CAnim, resName);
    }

    /** 获取指定名称的style资源ID */
    public static int getStyleId(String resName) {
        return getResId(CStyle, resName);
    }

    /** 获取指定名称的string资源ID */
    public static int getStringId(String resName) {
        return getResId(CString, resName);
    }

    /** 获取指定名称的array资源ID */
    public static int getArrayId(String resName) {
        return getResId(CArray, resName);
    }

    private static int getResId(Class<?> resClass, String resName) {
        if (resClass == null) {
            Log.e(TAG, "getRes(null," + resName + ")");
            throw new IllegalArgumentException(
                    "ResClass is not initialized. Please make sure you have added neccessary resources. Also make sure you have "
                            + mActivity.getPackageName() + ".R$* configured in obfuscation. field=" + resName);
        }
        try {
            Field field = resClass.getField(resName);
            return field.getInt(resName);
        } catch (Exception e) {
            Log.i(TAG, "getRes(" + resClass.getName() + ", " + resName + ")");
            Log.i(TAG, "Error getting resource. Make sure you have copied all resources (res/) from SDK to your project. ");
            Log.i(TAG, e.getMessage());
        }
        return -1;
    }

    /** ----------------------根据资源名获取资源id------end------------------------- */

    /** ----------------------根据资源id获取资源------start------------------------- */

    /** 填充layout布局文件 */
    public static View inflate(int resId) {
        return LayoutInflater.from(getContext()).inflate(resId, null);
    }

    /** 获取资源 */
    public static Resources getResources() {
        return getContext().getResources();
    }

    /** 获取文字 */
    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    /** 获取文字数组 */
    public static String[] getStringArray(int resId) {
        return getResources().getStringArray(resId);
    }

    /** 获取dimen */
    public static int getDimens(int resId) {
        return getResources().getDimensionPixelSize(resId);
    }

    /** 获取drawable */
    @SuppressWarnings("deprecation")
    public static Drawable getDrawable(int resId) {
        return getResources().getDrawable(resId);
    }

    /** 获取Bitmap */
    public static Bitmap getBitmap(int resId) {
        return BitmapFactory.decodeResource(getResources(), resId);
    }

    /** 获取颜色 */
    public static int getColor(int resId) {
        return getResources().getColor(resId);
    }

    /** 获取颜色选择器 */
    public static ColorStateList getColorStateList(int resId) {
        return getResources().getColorStateList(resId);
    }

    /** ----------------------根据资源id获取资源------end------------------------- */

    /** ----------------------px与dip相互转换------start------------------------- */
    /** dip转换为px */
    public static int dip2px(int dip) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    /** px转换为dip */
    public static int px2dip(int px) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /** ----------------------px与dip相互转换------end------------------------- */

    /** ----------------------toast封装------start------------------------- */

    /** 弹出长时间Toast */
    public static void showToastLong(String msg) {
        buildToast(msg, Toast.LENGTH_LONG).show();
    }

    /** 弹出长时间Toast */
    public static void showToastLong(final String msg, final String bgColor) {
        runInMainThread(new Runnable() {
            @Override
            public void run() {
                buildToast(msg, Toast.LENGTH_LONG, bgColor).show();
            }
        });
    }

    /** 弹出长时间Toast */
    public static void showToastLong(final String msg, final String bgColor, final int textSp) {
        runInMainThread(new Runnable() {
            @Override
            public void run() {
                buildToast(msg, Toast.LENGTH_LONG, bgColor, textSp).show();
            }
        });
    }

    /** 弹出短时间Toast */
    public static void showToastShort(final String msg) {
        runInMainThread(new Runnable() {
            @Override
            public void run() {
                buildToast(msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** 弹出短时间Toast */
    public static void showToastShort(final String msg, final String bgColor) {
        runInMainThread(new Runnable() {
            @Override
            public void run() {
                buildToast(msg, Toast.LENGTH_SHORT, bgColor).show();
            }
        });
    }

    /** 弹出短时间Toast */
    public static void showToastShort(final String msg, final String bgColor, final int textSp) {
        runInMainThread(new Runnable() {
            @Override
            public void run() {
                buildToast(msg, Toast.LENGTH_SHORT, bgColor, textSp).show();
            }
        });
    }

    private static Toast buildToast(String msg, int duration) {
        return buildToast(msg, duration, "#000000", 16);
    }

    private static Toast buildToast(String msg, int duration, String bgColor) {
        return buildToast(msg, duration, bgColor, 16);
    }

    private static Toast buildToast(String msg, int duration, String bgColor, int textSp) {
        return buildToast(msg, duration, bgColor, textSp, 10);
    }

    /**
     * 构造Toast
     *
     * @param msg          消息
     * @param duration     显示时间
     * @param bgColor      背景颜色
     * @param textSp       文字大小
     * @param cornerRadius 四边圆角弧度
     * @return Toast
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("ShowToast")
    private static Toast buildToast(String msg, int duration, String bgColor, int textSp, int cornerRadius) {
        if (null != mToast) {
            mToast.cancel();
        }
        mToast = new Toast(getContext());
        mToast.setDuration(duration);
        mToast.setGravity(Gravity.BOTTOM, 0, 100);
        // 设置Toast文字
        TextView tv = new TextView(getContext());
        int dpPadding = dip2px(10);
        tv.setPadding(dpPadding, dpPadding, dpPadding, dpPadding);
        tv.setGravity(Gravity.CENTER);
        tv.setText(msg);
//        tv.setTextColor(Color.parseColor("#3DB5E6"));
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSp);
        // Toast文字TextView容器
        LinearLayout mLayout = new LinearLayout(getContext());
        GradientDrawable shape = new GradientDrawable();
        shape.setColor(Color.parseColor(bgColor));
        shape.setCornerRadius(cornerRadius);
        shape.setStroke(1, Color.parseColor(bgColor));
        shape.setAlpha(180);
        mLayout.setBackgroundDrawable(shape);
        mLayout.setOrientation(LinearLayout.VERTICAL);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        mLayout.setLayoutParams(params);
        mLayout.setGravity(Gravity.CENTER);
        mLayout.addView(tv);
        // 将自定义View覆盖Toast的View
        mToast.setView(mLayout);

        return mToast;
    }


    /**
     * 构造Toast(在中间显示)
     *
     * @param msg          消息
     * @return Toast
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("ShowToast")
    public static Toast createMyToast(String msg) {
        if (null != mToast) {
            mToast.cancel();
        }
        mToast = new Toast(getContext());
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER, 0, 100);
        // 设置Toast文字
        TextView tv = new TextView(getContext());
        int dpPadding = dip2px(10);
        tv.setPadding(dpPadding, dpPadding, dpPadding, dpPadding);
        tv.setGravity(Gravity.CENTER);
        tv.setText(msg);
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        // Toast文字TextView容器
        LinearLayout mLayout = new LinearLayout(getContext());
        GradientDrawable shape = new GradientDrawable();
        shape.setColor(Color.parseColor("#000000"));
        shape.setCornerRadius(10);
        shape.setStroke(1, Color.parseColor("#000000"));
        shape.setAlpha(180);
        mLayout.setBackgroundDrawable(shape);
        mLayout.setOrientation(LinearLayout.VERTICAL);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        mLayout.setLayoutParams(params);
        mLayout.setGravity(Gravity.CENTER);
        mLayout.addView(tv);
        // 将自定义View覆盖Toast的View
        mToast.setView(mLayout);

        return mToast;
    }



    /** ----------------------toast封装------end------------------------- */


    /** 颤抖控件 */
    public static void shakeView(View view) {
//        view.startAnimation(AnimationUtils.loadAnimation(UIUtil.getContext(), R.anim.shake));
        new ShakeAnimation(view).setDuration(Animation.DURATION_DEFAULT).setNumOfShakes(2).animate();
    }
}
