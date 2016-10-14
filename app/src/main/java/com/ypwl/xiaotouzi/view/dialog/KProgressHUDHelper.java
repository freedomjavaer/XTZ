package com.ypwl.xiaotouzi.view.dialog;

import android.app.Activity;

import com.ypwl.xiaotouzi.view.dialog.lib.KProgressHUD;


/**
 * function: KProgressHUD IOS风格对话框帮助类.
 *
 * <p>Created by lzj on 2016/3/2.</p>
 */
public class KProgressHUDHelper {

    /**
     * 创建通用加载中等候进度框--通用型
     *
     * @param activity 当前栈顶Activity
     */
    public static KProgressHUD createLoading(Activity activity) {
        return KProgressHUD.create(activity).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCancellable(true);
    }

    /**
     * 创建加载中等候进度框--包含文字提醒
     *
     * @param activity 当前栈顶Activity
     */
    public static KProgressHUD createLoading(Activity activity, String msg) {
        return KProgressHUD.create(activity).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCancellable(true)
                .setLabel(msg);
    }

    /**
     * 创建加载中等候进度框--包含文字提醒以及详情信息
     *
     * @param activity 当前栈顶Activity
     */
    public static KProgressHUD createLoading(Activity activity, String msg, String detailMsg) {
        return KProgressHUD.create(activity).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCancellable(true)
                .setLabel(msg).setDetailsLabel(detailMsg);
    }

    /**
     * 创建加载中等候进度框--圆饼状--显示加载进度
     *
     * @param activity 当前栈顶Activity
     */
    public static KProgressHUD createLoadingProgressPie(Activity activity) {
        return KProgressHUD.create(activity).setStyle(KProgressHUD.Style.PIE_DETERMINATE).setCancellable(false);

    }

    /**
     * 创建加载中等候进度框--圆圈状--显示加载进度
     *
     * @param activity 当前栈顶Activity
     */
    public static KProgressHUD createLoadingProgressRing(Activity activity) {
        return KProgressHUD.create(activity).setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE).setCancellable(false);

    }

    /**
     * 创建加载中等候进度框--横向bar--显示加载进度.
     *
     * <p>setMaxProgress设置最大值,setProgress设置进度值，setLabel设置提示信息比如进度百分比</p>
     *
     * @param activity 当前栈顶Activity
     */
    public static KProgressHUD createLoadingProgressBar(Activity activity) {
        return KProgressHUD.create(activity).setStyle(KProgressHUD.Style.BAR_DETERMINATE).setCancellable(false);

    }
}
