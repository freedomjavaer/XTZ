package com.ypwl.xiaotouzi.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.XtzApp;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.event.LoginStateEvent;
import com.ypwl.xiaotouzi.manager.EventHelper;
import com.ypwl.xiaotouzi.ui.activity.LoginActivity;
import com.ypwl.xiaotouzi.ui.activity.MainActivity;
import com.ypwl.xiaotouzi.view.dialog.CustomDialog;


/**
 * function : 视图工具类.
 * <p/>
 * Created by lzj on 2015/11/8.
 */
@SuppressWarnings("unused")
public class ViewUtil {
    /**
     * 显示对应状态视图
     *
     * @param viewState  视图状态(四种互斥) :<br/>
     *                   加载中视图 : <code>Const.LAYOUT_LOADING</code><br/>
     *                   空数据视图 : <code>Const.LAYOUT_EMPTY</code><br/>
     *                   出错啦视图 : <code>Const.LAYOUT_ERROR</code><br/>
     *                   数据视图 : <code>Const.LAYOUT_DATA</code>
     * @param dataView   数据视图布局
     * @param noDataView 无数据视图布局
     */
    public static void showContentLayout(int viewState, View noDataView, View dataView) {
        showContentLayout(viewState, noDataView, dataView, 0, null, null);
    }


    /**
     * 显示对应状态视图
     *
     * @param viewState       视图状态(四种互斥) :<br/>
     *                        加载中视图 : <code>Const.LAYOUT_LOADING</code><br/>
     *                        空数据视图 : <code>Const.LAYOUT_EMPTY</code><br/>
     *                        出错啦视图 : <code>Const.LAYOUT_ERROR</code><br/>
     *                        数据视图 : <code>Const.LAYOUT_DATA</code>
     * @param dataView        数据视图布局
     * @param noDataView      无数据视图布局
     * @param onClickListener 无数据视图时点击监听器(eg : 网络出错点击重新加载)
     */
    public static void showContentLayout(int viewState, View noDataView, View dataView, OnClickListener onClickListener) {
        showContentLayout(viewState, noDataView, dataView, 0, null, onClickListener);
    }

    /**
     * 显示对应状态视图
     *
     * @param viewState  视图状态(四种互斥) :<br/>
     *                   加载中视图 : <code>Const.LAYOUT_LOADING</code><br/>
     *                   空数据视图 : <code>Const.LAYOUT_EMPTY</code><br/>
     *                   出错啦视图 : <code>Const.LAYOUT_ERROR</code><br/>
     *                   数据视图 : <code>Const.LAYOUT_DATA</code>
     * @param dataView   数据视图布局
     * @param noDataView 无数据视图布局
     * @param hintStr    无数据视图布局中央图片下方提示文字
     */
    public static void showContentLayout(int viewState, View noDataView, View dataView, String hintStr) {
        showContentLayout(viewState, noDataView, dataView, 0, hintStr, null);
    }

    /**
     * 显示对应状态视图
     *
     * @param viewState       视图状态(四种互斥) :<br/>
     *                        加载中视图 : <code>Const.LAYOUT_LOADING</code><br/>
     *                        空数据视图 : <code>Const.LAYOUT_EMPTY</code><br/>
     *                        出错啦视图 : <code>Const.LAYOUT_ERROR</code><br/>
     *                        数据视图 : <code>Const.LAYOUT_DATA</code>
     * @param dataView        数据视图布局
     * @param noDataView      无数据视图布局
     * @param hintStr         无数据视图布局中央图片下方提示文字
     * @param onClickListener 无数据视图时点击监听器(eg : 网络出错点击重新加载)
     */
    public static void showContentLayout(int viewState, View noDataView, View dataView, String hintStr, OnClickListener onClickListener) {
        showContentLayout(viewState, noDataView, dataView, 0, hintStr, onClickListener);
    }

    /**
     * 显示对应状态视图
     *
     * @param viewState  视图状态(四种互斥) :<br/>
     *                   加载中视图 : <code>Const.LAYOUT_LOADING</code><br/>
     *                   空数据视图 : <code>Const.LAYOUT_EMPTY</code><br/>
     *                   出错啦视图 : <code>Const.LAYOUT_ERROR</code><br/>
     *                   数据视图 : <code>Const.LAYOUT_DATA</code>
     * @param dataView   数据视图布局
     * @param noDataView 无数据视图布局
     * @param imgResId   无数据视图布局中央图片
     * @param hintStr    无数据视图布局中央图片下方提示文字
     */
    public static void showContentLayout(int viewState, final View noDataView, View dataView, int imgResId, String hintStr) {
        showContentLayout(viewState, noDataView, dataView, imgResId, hintStr, null);
    }

    /**
     * 显示对应状态视图
     *
     * @param viewState       视图状态(四种互斥) :<br/>
     *                        加载中视图 : <code>Const.LAYOUT_LOADING</code><br/>
     *                        空数据视图 : <code>Const.LAYOUT_EMPTY</code><br/>
     *                        出错啦视图 : <code>Const.LAYOUT_ERROR</code><br/>
     *                        数据视图 : <code>Const.LAYOUT_DATA</code>
     * @param dataView        数据视图布局
     * @param noDataView      无数据视图布局
     * @param imgResId        无数据视图布局中央图片
     * @param hintStr         无数据视图布局中央图片下方提示文字
     * @param onClickListener 无数据视图时点击监听器(eg : 网络出错点击重新加载)
     */
    public static void showContentLayout(int viewState, final View noDataView, final View dataView, final int imgResId, final String hintStr, final OnClickListener onClickListener) {
        ImageView defaultImg = (ImageView) noDataView.findViewById(R.id.layout_no_data_view_img);
        ProgressBar progressBar = (ProgressBar) noDataView.findViewById(R.id.layout_no_data_view_layout_hint_pb_loading);
        TextView displayMsg = (TextView) noDataView.findViewById(R.id.layout_no_data_view_layout_hint_msg);
        switch (viewState) {
            case Const.LAYOUT_LOADING:// 显示加载中视图
                dataView.setVisibility(View.GONE);
                noDataView.setVisibility(View.VISIBLE);
                defaultImg.setBackgroundResource(imgResId > 0 ? imgResId : R.mipmap.cat);
                progressBar.setVisibility(View.VISIBLE);
//                displayMsg.setTextColor(Color.parseColor("#3DB5E6"));
                defaultImg.setVisibility(View.GONE);
                displayMsg.setText(hintStr == null ? noDataView.getContext().getString(R.string.loading_hint_ing) : hintStr);
                break;
            case Const.LAYOUT_EMPTY:// 显示空数据视图
                dataView.setVisibility(View.GONE);
                noDataView.setVisibility(View.VISIBLE);
                defaultImg.setVisibility(View.VISIBLE);
                defaultImg.setBackgroundResource(imgResId > 0 ? imgResId : R.mipmap.cat);
                progressBar.setVisibility(View.GONE);
                displayMsg.setTextColor(Color.parseColor("#3DB5E6"));
                displayMsg.setText(hintStr == null ? noDataView.getContext().getString(R.string.loading_hint_empty) : hintStr);
                if (onClickListener != null) {
                    noDataView.findViewById(R.id.layout_no_data_view_layout_hint).setOnClickListener(
                            new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showContentLayout(Const.LAYOUT_LOADING, noDataView, dataView, imgResId, null, null);
                                    onClickListener.onClick(v);
                                }
                            });
                }
                break;
            case Const.LAYOUT_ERROR:// 显示出错视图
                dataView.setVisibility(View.GONE);
                noDataView.setVisibility(View.VISIBLE);
                defaultImg.setVisibility(View.VISIBLE);
                defaultImg.setBackgroundResource(imgResId > 0 ? imgResId : R.mipmap.cat);
                progressBar.setVisibility(View.GONE);
                displayMsg.setTextColor(Color.parseColor("#3DB5E6"));
                displayMsg.setText(hintStr == null ? noDataView.getContext().getString(R.string.loading_hint_error) : hintStr);
                if (onClickListener != null) {
                    noDataView.findViewById(R.id.layout_no_data_view_layout_hint).setOnClickListener(
                            new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showContentLayout(Const.LAYOUT_LOADING, noDataView, dataView, imgResId, null, null);
                                    onClickListener.onClick(v);
                                }
                            });
                }
                break;
            case Const.LAYOUT_DATA:// 显示数据视图
                noDataView.setVisibility(View.GONE);
                dataView.setVisibility(View.VISIBLE);
                break;
            default:
                throw new IllegalArgumentException("viewState wrong!");
        }
    }

    /**
     * 清空activity栈，并回到主页Main
     *
     * @param tag 指定跳转主页后显示哪个fragment页面 <br/>
     *            <code>MainActivity.FRAGMENT_TAG_PALTFORMS : 网贷平台页面</code>
     *            <code>MainActivity.FRAGMENT_TAG_MYINVEST : 我的记账页面</code>
     *            <code>MainActivity.FRAGMENT_TAG_MORE : 我的页面</code>
     */
    public static void jump2Main(Activity activity, String tag) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA, tag);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }

    /** for test funny **/
    public static void deadLine(Activity activity) {
        CustomDialog dialog = new CustomDialog.AlertBuilder(activity)
                .setTitleText(UIUtil.getString(R.string.app_name)).setContentText("该功能需要等发工资后才能开发！")
                .setPositiveBtn("好滴", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UIUtil.showToastShort("快去叫老板发工资！！！");
                    }
                }).create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    /** 获取半透明黑色背景 */
    public static GradientDrawable getAlphaBlackBgShape() {
        GradientDrawable shape = new GradientDrawable();
        shape.setColor(UIUtil.getColor(R.color.black));
        shape.setStroke(1, UIUtil.getColor(R.color.black));
        shape.setAlpha(200);
        return shape;
    }

    /** 重新登录 */
    public static void againLoginAndRegister(Activity activity) {
        if (activity == null) {
            return;
        }
        final boolean[] positive = {false};
        //初始化登录信息
        CacheUtils.putString(Const.KEY_LOGIN_USER, null);
        //清空相关数据
        EventHelper.post(new LoginStateEvent(false));
        //跳转到注册登录页面
        CustomDialog dialog = new CustomDialog.AlertBuilder(activity)
                .setTitleText(R.string.app_name)
                .setContentText("您的账号在其他设备上登录，请注意账号安全!")
                .setContentTextGravity(Gravity.CENTER)
                .setPositiveBtn("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        positive[0] = true;
                        dialog.dismiss();
                        GlobalUtils.token = null;//重置内存中的token
                        XtzApp.getApplication().removeAll();
                        Intent intent1 = new Intent(UIUtil.getContext(), MainActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        UIUtil.getContext().startActivity(intent1);
                        Intent intent = new Intent(UIUtil.getContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        UIUtil.getContext().startActivity(intent);
                    }
                })
                .setCanceled(false)
                .create();
        dialog.setCancelable(false);
        dialog.show();
//        dialog.setCanceledOnTouchOutside(false);//失去焦点不会消失
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (!positive[0]) {
                    GlobalUtils.token = null;
                    XtzApp.getApplication().removeAll();
                    Intent intent1 = new Intent(UIUtil.getContext(), MainActivity.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    UIUtil.getContext().startActivity(intent1);
                    Intent intent = new Intent(UIUtil.getContext(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    UIUtil.getContext().startActivity(intent);
                }
            }
        });
        dialog.show();
    }
}
