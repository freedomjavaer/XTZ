package com.ypwl.xiaotouzi.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;

import java.util.List;

/**
 * 项目名:	XTZ
 * 包名:	com.ypwl.xiaotouzi.utils
 * 类名:	PopuViewUtil
 * 作者:	罗霄
 * 创建时间:	2016/4/1 18:12
 * <p/>
 * 描述:	弹窗工具类（基于 Dialog ）
 * <p/>
 * svn版本:	$Revision: 15312 $
 * 更新人:	$Author: pengdakai $
 * 更新时间:	$Date: 2016-06-02 15:28:29 +0800 (周四, 02 六月 2016) $
 * 更新描述:
 */
public class PopuViewUtil implements View.OnClickListener {

    private int mTitleSize;
    private int defaultTitleSize = 14;
    private List<String> mData;
    private Dialog mPopuView;
    private OnClickCountsListener mListener;
    private Context mContext;
    private View mLayout;
    private TextView mTvTitle;
    private LinearLayout mContainer;
    private Window dialogWindow;
    private View mTvTitleLine;
    private TextView mTvCancel;
    private int mTitlePadding;
    private int defaultTitlePadding = 10;

    private PopuViewUtil(Context context){
        this.mContext = context;
        initView();
    }

    /**
     * 创建基本文字内容弹窗
     * @param context 上下文
     * @param data 文字数据集合
     * @param listener  文字条目的点击事件监听
     */
    public PopuViewUtil(Context context, List<String> data, OnClickCountsListener listener) {
        this(context);

        this.mListener = listener;
        this.mData = data;

        setDataView();
    }

    /**
     * 创建自定义布局弹窗，需要在外部自己做事件监听
     * @param context 上下文
     * @param layoutView 自定义布局
     */
    public PopuViewUtil(Context context, View layoutView){
        this(context);

        mContainer.addView(layoutView);
    }

    public void dismiss() {
        if (mPopuView != null) {
            mPopuView.dismiss();
        }
    }

    public void show() {
        if (mPopuView != null) {
            mPopuView.show();
        }
    }

    /** Sets whether this dialog is cancelable with the BACK key. */
    public void setCancelable(boolean flag){
        if (mPopuView != null) {
            mPopuView.setCancelable(flag);
        }
    }

    /**
     * Sets whether this dialog is canceled when touched outside the window's bounds. If setting to true, the dialog is set to be cancelable if not already set.
     * @param cancel Whether the dialog should be canceled when touched outside the window.
     */
    public void setCanceledOnTouchOutside(boolean cancel){
        if (mPopuView != null) {
            mPopuView.setCanceledOnTouchOutside(cancel);
        }
    }

    /**
     * 设置弹窗显示位置
     * @param gravity 默认 Gravity.BOTTOM
     */
    public void setGravity(int gravity){
        if (null != dialogWindow){
            dialogWindow.setGravity(gravity);
        }
    }

    /** 设置弹窗有关的动画效果 */
    public void setWindowAnimations(int resId){
        if (null != dialogWindow){
            dialogWindow.setWindowAnimations(resId);
        }
    }

    /** 设置窗口背后位置变暗与否 */
    public void isWindowDimmed(boolean flag){
        if (null != dialogWindow){
            if (flag){
                dialogWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }else {
                dialogWindow.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
        }
    }

    /**
     * 设置弹窗背景资源
     * @param resId 默认为透明色
     */
    public void setBackground(int resId){
        if (null != dialogWindow){
            dialogWindow.setBackgroundDrawableResource(resId);
        }
    }

    /** 创建一个 dialog */
    private Dialog getPopuView() {

        Dialog dialog = new Dialog(mContext);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题，否则会影响高度计算，一定要在setContentView之前调用，终于明白有一个设置theme的构造函数的目的了

        dialogWindow = dialog.getWindow();

        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);

        dialogWindow.setGravity(Gravity.BOTTOM);

        dialogWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialogWindow.setWindowAnimations(R.style.AnimationChangeInvestStatus);

        dialog.setContentView(mLayout);

        return dialog;
    }

    /** 设置标题的显示状态 */
    public void setTitleVisibility(int visibility){
        if (null != mTvTitle){
            mTvTitle.setVisibility(visibility);
            mTvTitleLine.setVisibility(visibility);
            toggleFirstChildViewBg();
        }
    }

    /**　设置弹窗标题　（设置标题后，默认打开显示）*/
    public void setTitleContent(String str){
        if (null != mTvTitle){
            mTvTitle.setTextSize(mTitleSize==0?defaultTitleSize:mTitleSize);
            mTvTitle.setText(str);
            mTitlePadding = mTitlePadding==0?defaultTitlePadding:mTitlePadding;
            mTvTitle.setPadding(mTitlePadding,mTitlePadding,mTitlePadding,mTitlePadding);
            setTitleVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置头部标题字体大小
     * @param titleSize
     */
    public void setTitleSize(int titleSize){
        this.mTitleSize = titleSize;
        mTvTitle.setTextSize(mTitleSize);
    }

    /**
     * 设置头部标题布局padding值
     * @param titlePadding
     */
    public void setTitlePadding(int titlePadding){
        this.mTitlePadding = titlePadding;
        mTvTitle.setPadding(mTitlePadding,mTitlePadding,mTitlePadding,mTitlePadding);
    }

    /**　设置取消按钮显示状态 */
    public void setCancelButtonVisibility(int visibility){
        if (null != mTvCancel){
            mTvCancel.setVisibility(visibility);
        }
    }

    /** 根据 mTvTitle 的是否显示调整第一个 childView 的背景 */
    private void toggleFirstChildViewBg(){
        if (null != mTvTitle && null != mContainer){
            TextView childView = (TextView) mContainer.getChildAt(0);
            int childCount = mContainer.getChildCount();
            if (View.GONE == mTvTitle.getVisibility()){
                if (childCount > 1){
                    childView.setBackgroundResource(R.drawable.base_popuview_bg_item_top);
                }else if (childCount > 0){
                    childView.setBackgroundResource(R.drawable.base_popuview_bg_item_single);
                }
            }else {
                if (childCount > 1){
                    childView.setBackgroundColor(Color.WHITE);
                }else if (childCount > 0){
                    childView.setBackgroundResource(R.drawable.base_popuview_bg_item_bottom);
                }
            }
        }
    }

    private void initView() {

        mLayout = View.inflate(mContext, R.layout.base_popuview, null);
        mContainer = (LinearLayout) mLayout.findViewById(R.id.base_popuview_ll_container);
        mTvCancel = (TextView) mLayout.findViewById(R.id.base_popuview_tv_cancel);
        mTvTitle = (TextView) mLayout.findViewById(R.id.base_popuview_tv_title);
        mTvTitleLine = mLayout.findViewById(R.id.base_popuview_title_line);
        mTvCancel.setTag(-1);
        mTvCancel.setOnClickListener(this);

        mPopuView = getPopuView();
    }

    /** 设置基本文字内容 */
    private void setDataView() {
        if (null == mData) {
            return;
        }

        for (int i = 0; i < mData.size(); i++) {
            TextView textView = new TextView(mContext);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, UIUtil.dip2px(50));
            textView.setLayoutParams(layoutParams);
            textView.setTextColor(UIUtil.getColor(R.color.popup_text_item));
            textView.setTextSize(16);
            textView.setGravity(Gravity.CENTER);
            textView.setText(mData.get(i));
            textView.setTag(i);
            textView.setOnClickListener(this);

            if (i != mData.size() - 1) {
                textView.setBackgroundColor(Color.WHITE);
            } else {
                textView.setBackgroundResource(R.drawable.base_popuview_bg_item_bottom);
            }

            /** 添加item*/
            mContainer.addView(textView);

            /** 添加分割线 */
            if (i != mData.size() - 1) {
                View view = new View(mContext);
                view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));
                view.setBackgroundColor(UIUtil.getColor(R.color.popup_line));
                mContainer.addView(view);
            }
        }

        toggleFirstChildViewBg();
    }

    @Override
    public void onClick(View v) {
        Integer tag = (Integer) v.getTag();
        if (tag == -1) {
            dismiss();
            return;
        }
        if (null != mListener && null != mData){
            mListener.onClick(tag, mData.get(tag).trim());
        }
        dismiss();
    }

    /** 基本文字内容部分，条目点击事件监听 */
    public interface OnClickCountsListener {
        void onClick(int position, String str);
    }
}
