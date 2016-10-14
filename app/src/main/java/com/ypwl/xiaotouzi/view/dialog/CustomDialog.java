package com.ypwl.xiaotouzi.view.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.common.DeviceInfo;
import com.ypwl.xiaotouzi.utils.UIUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * function : 自定义对话框,支持：alert信息提示，自定义内容视图，loading视图，显示GIF视图，条目单选，条目多选.
 *
 * <p>Created by lzj on 2016/1/11.</p>
 */
@SuppressWarnings("unused")
public class CustomDialog extends Dialog {

    public CustomDialog(Activity context) {
        super(context);
    }

    public CustomDialog(Activity context, int theme) {
        super(context, theme);
    }

    public interface OnMultiChoiceClickListener {
        void onClick(int which, boolean isChecked, Boolean[] selecteStateIntArray);
    }

    /**
     * 信息提示对话框
     * <br/>
     * 默认居于屏幕中间，底部含确认、取消按钮，有标题,各控件可定制
     */
    public static class AlertBuilder {
        private CustomDialog dialog;
        private Activity mActivity;
        private LayoutInflater mLayoutInflater;

        private View mView;
        private TextView uiTitleText, uiContentText;
        private ImageView uiTitleIcon;
        private LinearLayout uiLayoutContent;
        private OnClickListener positiveClickListener, negativeClickListener;

        /** 对话框背景资源id */
        private int dialogBgResId = -1;
        /** 对话框背景颜色 */
        private int dialogBgColor = -1;
        /** 标题栏显示状态，默认显示 */
        private int titleLayoutVisibility = View.VISIBLE;
        /** 标题栏背景资源id */
        private int titleBgResId = -1;
        /** 标题icon */
        private int titleIcon = -1;
        /** 标题文本内容 */
        private CharSequence titleText;
        /** 标题文本颜色 */
        private int titleTextColor = -1;
        /** 标题和内容分割线显示状态，默认隐藏 */
        private int titleDividerlineVisibility = View.GONE;
        /** 内容文本 */
        private CharSequence contentText;
        /** 内容文本大小 */
        private float contentTextSize = -1;
        /** 内容文本颜色 */
        private int contentTextColor = -1;
        /** 内容文本对其方式，默认左上角 */
        private int contentTextGravity = Gravity.START | Gravity.TOP;
        /** 自定义对话框内容视图 */
        private View customContentView;
        /** positive按钮文本 */
        private CharSequence positiveBtnText;
        /** positive按钮文本颜色 */
        private int positiveBtnTextColor = -1;
        /** positive按钮背景资源 */
        private int positiveBtnBgResId = -1;
        /** negative按钮文本 */
        private CharSequence negativeBtnText;
        /** negative按钮文本颜色 */
        private int negativeBtnTextColor = -1;
        /** negative按钮背景资源 */
        private int negativeBtnBgResId = -1;
        /** dialog是否可以界外触摸取消 */
        private boolean canceledOnTouchOutside = true;
        /** dialog是否可以取消 */
        private boolean canceled = true;
        /** dialog进入和退出动画 */
        private int dialogInOutAnimResId = -1;
        /** gif资源内容图片 */
        private int customContentViewGifResId = -1;
        /** 对话框窗体背景灰暗程度 0.0f~1.0f 越大越暗 */
        private float dialogWindowBgDimAcount = 2f;
        /** dialog在屏幕的显示位置 */
        private int dialogGravity = Gravity.CENTER;
        /** 单选对话框: 单选条目数组 */
        private CharSequence[] singleChoiceItems = null;
        /** 单选对话框: 默认选中条目position */
        private int singleChoiceDefaultItem = -1;
        /** 单选对话框: 切换选择监听 */
        private OnClickListener singleChoiceOnClickListener;
        /** 多选对话框: 多选条目数组 */
        private CharSequence[] multiChoiceItems = null;
        /** 多选对话框: 默认数组选中状态 */
        private Boolean[] multiChoiceDefaultItems = null;
        /** 多选对话框: 切换选择监听 */
        private OnMultiChoiceClickListener multiChoiceClickListener;
        /** 对话框形状：是否设置为正方形 */
        private boolean dialogRectangle = false;
        /** 对话框自定义的布局 */
        private int mLayout = -1;

        public AlertBuilder setDialogBgResId(int dialogBgResId) {
            this.dialogBgResId = dialogBgResId;
            return this;
        }

        public AlertBuilder setDialogBgColor(int dialogBgColor) {
            this.dialogBgColor = dialogBgColor;
            return this;
        }

        public AlertBuilder setDialogRectangle(boolean dialogRectangle) {
            this.dialogRectangle = dialogRectangle;
            return this;
        }

        public AlertBuilder setTitleLayoutVisibility(int titleLayoutVisibility) {
            this.titleLayoutVisibility = titleLayoutVisibility;
            return this;
        }

        public AlertBuilder setTitleBgResId(int titleBgResId) {
            this.titleBgResId = titleBgResId;
            return this;
        }

        public AlertBuilder setTitleIcon(int titleIcon) {
            this.titleIcon = titleIcon;
            return this;
        }

        public AlertBuilder setTitleText(CharSequence titleText) {
            this.titleText = titleText;
            return this;
        }

        public AlertBuilder setTitleText(int titleTextResId) {
            this.titleText = mActivity.getString(titleTextResId);
            return this;
        }

        public AlertBuilder setTitleTextColor(int titleTextColor) {
            this.titleTextColor = titleTextColor;
            return this;
        }

        public AlertBuilder setTitleDividerlineVisibility(int titleDividerlineVisibility) {
            this.titleDividerlineVisibility = titleDividerlineVisibility;
            return this;
        }

        public AlertBuilder setContentText(CharSequence contentText) {
            this.contentText = contentText;
            return this;
        }

        public AlertBuilder setContentTextSize(float contentTextSize) {
            this.contentTextSize = contentTextSize;
            return this;
        }

        public AlertBuilder setContentTextColor(int contentTextColor) {
            this.contentTextColor = contentTextColor;
            return this;
        }

        public AlertBuilder setContentTextGravity(int contentTextGravity) {
            this.contentTextGravity = contentTextGravity;
            return this;
        }

        public AlertBuilder setCustomContentView(View customContentView) {
            this.customContentView = customContentView;
            return this;
        }

        public AlertBuilder setPositiveBtn(CharSequence positiveBtnText, OnClickListener listener) {
            this.positiveBtnText = positiveBtnText;
            this.positiveClickListener = listener;
            return this;
        }

        public AlertBuilder setPositiveBtnTextColor(int positiveBtnTextColor) {
            this.positiveBtnTextColor = positiveBtnTextColor;
            return this;
        }

        public AlertBuilder setPositiveBtnBgResId(int positiveBtnBgResId) {
            this.positiveBtnBgResId = positiveBtnBgResId;
            return this;
        }


        public AlertBuilder setNegativeBtn(CharSequence negativeBtnText, OnClickListener listener) {
            this.negativeBtnText = negativeBtnText;
            this.negativeClickListener = listener;
            return this;
        }

        public AlertBuilder setNegativeBtnTextColor(int negativeBtnTextColor) {
            this.negativeBtnTextColor = negativeBtnTextColor;
            return this;
        }

        public AlertBuilder setNegativeBtnBgResId(int negativeBtnBgResId) {
            this.negativeBtnBgResId = negativeBtnBgResId;
            return this;
        }

        public AlertBuilder setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
            this.canceledOnTouchOutside = canceledOnTouchOutside;
            return this;
        }

        public AlertBuilder setCanceled(boolean canceled) {
            this.canceled = canceled;
            return this;
        }

        public AlertBuilder setDialogInOutAnimResId(int dialogInOutAnimResId) {
            this.dialogInOutAnimResId = dialogInOutAnimResId;
            return this;
        }

        public AlertBuilder setCustomContentViewGif(int customContentViewGifResId) {
            this.customContentViewGifResId = customContentViewGifResId;
            return this;
        }

        public AlertBuilder setDialogWindowBgDimAcount(float dialogWindowBgDimAcount) {
            this.dialogWindowBgDimAcount = dialogWindowBgDimAcount;
            return this;
        }

        public AlertBuilder setDialogGravity(int dialogGravity) {
            this.dialogGravity = dialogGravity;
            return this;
        }

        public AlertBuilder setSingleChoiceItems(CharSequence[] items, int checkedItem, OnClickListener listener) {
            this.singleChoiceItems = items;
            this.singleChoiceDefaultItem = checkedItem;
            this.singleChoiceOnClickListener = listener;
            return this;
        }

        public AlertBuilder setMultiChoiceItems(CharSequence[] items, Boolean[] checkedItems, OnMultiChoiceClickListener listener) {
            this.multiChoiceItems = items;
            this.multiChoiceDefaultItems = checkedItems;
            this.multiChoiceClickListener = listener;
            return this;
        }

        public AlertBuilder(Activity activity) {
            this.mActivity = activity;
            mLayoutInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public AlertBuilder setDialogLayout(int mlayout){
            this.mLayout = mlayout;
            return this;
        }

        @SuppressLint("InflateParams")
        public CustomDialog create() {
            dialog = new CustomDialog(mActivity, R.style.CD_Dialog_Theme);

            if (mLayout == -1){
                mView = mLayoutInflater.inflate(R.layout.cd_dialog_content, null);
            }else {
                mView = mLayoutInflater.inflate(mLayout, null);
            }

            //对话框背景资源
            LinearLayout uiDialogLayout = (LinearLayout) mView.findViewById(R.id.cd_layout_dialog);
            if (dialogBgResId != -1) {
                uiDialogLayout.setBackgroundResource(dialogBgResId);
            } else if (dialogBgColor != -1) {
                uiDialogLayout.setBackgroundColor(dialogBgColor);
            }
            int dialogWidth = DeviceInfo.ScreenWidthPixels - UIUtil.dip2px(60);
            if (dialogWidth < 0) {
                dialogWidth = DeviceInfo.ScreenWidthPixels - 50;//极小分辨率屏幕,一般是没有了这种的
            }
            int dialogHeight = dialogRectangle ? dialogWidth : LayoutParams.WRAP_CONTENT;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dialogWidth, dialogHeight);
            layoutParams.bottomMargin = UIUtil.dip2px(10);
            layoutParams.topMargin = UIUtil.dip2px(10);
            uiDialogLayout.setLayoutParams(layoutParams);

            if (mLayout == -1){
                //标题
                LinearLayout uiLayoutTitle = (LinearLayout) mView.findViewById(R.id.cd_layout_title);
                //标题栏显示状态
                uiLayoutTitle.setVisibility(titleLayoutVisibility);
                //标题栏背景资源
                if (titleBgResId != -1) {
                    uiLayoutTitle.setBackgroundResource(titleBgResId);
                }
                //标题icon
                uiTitleIcon = (ImageView) mView.findViewById(R.id.cd_title_icon);
                if (titleIcon != -1) {
                    uiTitleIcon.setVisibility(View.VISIBLE);
                    uiTitleIcon.setImageResource(titleIcon);
                }
                //标题文本内容
                uiTitleText = (TextView) mView.findViewById(R.id.cd_title_text);
                if (titleText != null && titleText.length() > 0) {
                    uiTitleText.setText(titleText);
                }
                //标题文本颜色
                if (titleTextColor != -1) {
                    uiTitleText.setTextColor(titleTextColor);
                }
                //标题分割线显示状态
                mView.findViewById(R.id.cd_dividerline_title).setVisibility(titleDividerlineVisibility);
            }


            // 对话框内容
            uiLayoutContent = (LinearLayout) mView.findViewById(R.id.cd_layout_content);
            if (contentText != null) {//默认状态
                uiContentText = ((TextView) mView.findViewById(R.id.cd_content_message));
                uiContentText.setText(contentText);
                if (contentTextSize != -1) {
                    uiContentText.setTextSize(contentTextSize);
                }
                if (contentTextColor != -1) {
                    uiContentText.setTextColor(contentTextColor);
                }
                uiContentText.setGravity(contentTextGravity);
            } else if (singleChoiceItems != null) {//单选
                uiLayoutContent.removeAllViews();
                uiLayoutContent.addView(initSingleChoiceItemsDialogContent(), new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            } else if (multiChoiceItems != null) {//多选
                uiLayoutContent.removeAllViews();
                uiLayoutContent.addView(initMultiChoiceItemsDialogContent(), new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            } else if (customContentView != null) {//自定义内容视图
                uiLayoutContent.removeAllViews();
                uiLayoutContent.addView(customContentView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            } else if (customContentViewGifResId != -1) {//自定义显示gif
                GifView gifView = new GifView(mActivity);
                gifView.setMovieResource(customContentViewGifResId);
                uiLayoutContent.removeAllViews();
                uiLayoutContent.setGravity(Gravity.CENTER);
                uiLayoutContent.addView(gifView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            }

            //底部按钮
            //底部按钮:positive
            TextView uiBtnPositive = (TextView) mView.findViewById(R.id.cd_btn_positive);
            if (positiveBtnText != null && positiveBtnText.length() > 0) {
                uiBtnPositive.setText(positiveBtnText);
                if (positiveBtnTextColor != -1) {
                    uiBtnPositive.setTextColor(positiveBtnTextColor);
                }
                if (positiveBtnBgResId != -1) {
                    uiBtnPositive.setBackgroundResource(positiveBtnBgResId);
                }
                uiBtnPositive.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (positiveClickListener != null) {
                            positiveClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                        }
                    }
                });
            } else {
                uiBtnPositive.setVisibility(View.GONE);
            }
            //底部按钮:negative
            TextView uiBtnNegative = (TextView) mView.findViewById(R.id.cd_btn_negative);
            if (negativeBtnText != null && negativeBtnText.length() > 0) {
                uiBtnNegative.setText(negativeBtnText);
                if (negativeBtnTextColor != -1) {
                    uiBtnNegative.setTextColor(negativeBtnTextColor);
                }
                if (negativeBtnBgResId != -1) {
                    uiBtnNegative.setBackgroundResource(negativeBtnBgResId);
                }
                uiBtnNegative.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (negativeClickListener != null) {
                            negativeClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                        }
                        dialog.cancel();
                    }
                });
            } else {
                uiBtnNegative.setVisibility(View.GONE);
            }

            dialog.setContentView(mView);// 添加视图
            dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
            dialog.setCancelable(canceled);

            // 位置
            Window window = dialog.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
            if (dialogWindowBgDimAcount != 2f && dialogWindowBgDimAcount >= 0f && dialogWindowBgDimAcount <= 1.0f) {
                lp.dimAmount = dialogWindowBgDimAcount;
            }
            window.setGravity(dialogGravity); // 设置dialog显示的位置
            if (dialogInOutAnimResId != -1) {
                window.setWindowAnimations(dialogInOutAnimResId); // 添加动画
            } else {
                window.setWindowAnimations(R.style.CD_Dialog_Anim); // 添加动画
            }
            return dialog;
        }

        /** 初始化单选框视图 */
        private View initSingleChoiceItemsDialogContent() {
            final RadioGroup radioGroup = new RadioGroup(mView.getContext());
            radioGroup.setOrientation(LinearLayout.VERTICAL);
            CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    for (int x = 0; x < singleChoiceItems.length; x++) {
                        RadioButton radioButton = (RadioButton) radioGroup.getChildAt(x);
                        if (radioButton != null) {
                            radioButton.setChecked(false);
                        }
                    }
                    buttonView.setChecked(isChecked);
                    if (singleChoiceOnClickListener != null) {
                        singleChoiceOnClickListener.onClick(dialog, (int) buttonView.getTag());
                    }
                }
            };
            for (int x = 0; x < singleChoiceItems.length; x++) {
                RadioButton radioButton = new RadioButton(mView.getContext());
                radioButton.setText(singleChoiceItems[x]);
                radioButton.setChecked(false);
                radioButton.setTag(x);
                radioButton.setOnCheckedChangeListener(onCheckedChangeListener);
                if (x == singleChoiceDefaultItem) {
                    radioButton.setChecked(true);
                }
                radioGroup.addView(radioButton, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            }

            return radioGroup;
        }

        /** 初始化多选框视图 */
        private View initMultiChoiceItemsDialogContent() {
            final List<Boolean> tempList = new ArrayList<>();
            final RadioGroup radioGroup = new RadioGroup(mView.getContext());
            radioGroup.setOrientation(LinearLayout.VERTICAL);
            CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    tempList.clear();
                    for (int x = 0; x < multiChoiceItems.length; x++) {
                        CheckBox checkBox = (CheckBox) radioGroup.getChildAt(x);
                        if (checkBox != null) {
                            tempList.add(checkBox.isChecked());
                        }
                    }
                    buttonView.setChecked(isChecked);
                    if (multiChoiceClickListener != null) {
                        multiChoiceClickListener.onClick((int) buttonView.getTag(), isChecked, tempList.toArray(new Boolean[tempList.size()]));
                    }
                }
            };
            radioGroup.removeAllViews();
            for (int x = 0; x < multiChoiceItems.length; x++) {
                CheckBox checkBox = new CheckBox(mView.getContext());
                checkBox.setText(multiChoiceItems[x]);
                checkBox.setChecked(false);
                checkBox.setTag(x);
                checkBox.setOnCheckedChangeListener(onCheckedChangeListener);
                radioGroup.addView(checkBox, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            }
            for (int x = 0; x < multiChoiceDefaultItems.length; x++) {
                CheckBox checkBox = (CheckBox) radioGroup.getChildAt(x);
                if (checkBox != null) {
                    checkBox.setChecked(multiChoiceDefaultItems[x]);
                }
            }
            return radioGroup;
        }



    }


    /**
     * 信息提示对话框
     * <br/>
     * 默认居于屏幕底部弹出，底部含取消按钮，有标题,各控件可定制
     */
    public static class TipsBuilder {
        private CustomDialog dialog;
        private Activity mActivity;
        private LayoutInflater mLayoutInflater;

        private View mView;
        private TextView uiTitleText, uiContentText;
        private ImageView uiTitleIcon;
        private LinearLayout uiLayoutContent;
        private OnClickListener positiveClickListener, negativeClickListener;

        /** 对话框背景资源id */
        private int dialogBgResId = -1;
        /** 对话框背景颜色 */
        private int dialogBgColor = -1;
        /** 对话框宽度全屏 */
        private boolean dialogWidthMatch = false;
        /** 标题栏显示状态，默认显示 */
        private int titleLayoutVisibility = View.GONE;
        /** 标题栏背景资源id */
        private int titleBgResId = -1;
        /** 标题icon */
        private int titleIcon = -1;
        /** 标题文本内容 */
        private CharSequence titleText;
        /** 标题文本颜色 */
        private int titleTextColor = -1;
        /** 标题和内容分割线显示状态，默认隐藏 */
        private int titleDividerlineVisibility = View.GONE;
        /** 内容文本 */
        private CharSequence contentText;
        /** 内容文本大小 */
        private float contentTextSize = -1;
        /** 内容文本颜色 */
        private int contentTextColor = -1;
        /** 内容文本对其方式，默认左上角 */
        private int contentTextGravity = Gravity.CENTER;
        /** 自定义对话框内容视图 */
        private View customContentView;
        /** negative按钮文本 */
        private CharSequence negativeBtnText;
        /** negative按钮文本颜色 */
        private int negativeBtnTextColor = -1;
        /** negative按钮背景资源 */
        private int negativeBtnBgResId = -1;
        /** dialog是否可以界外触摸取消 */
        private boolean canceledOnTouchOutside = true;
        /** dialog是否可以取消 */
        private boolean canceled = true;
        /** dialog进入和退出动画 */
        private int dialogInOutAnimResId = -1;
        /** gif资源内容图片 */
        private int customContentViewGifResId = -1;
        /** 对话框窗体背景灰暗程度 0.0f~1.0f 越大越暗 */
        private float dialogWindowBgDimAcount = 2f;
        /** dialog在屏幕的显示位置 */
        private int dialogGravity = Gravity.BOTTOM;
        /** 单选对话框: 单选条目数组 */
        private CharSequence[] singleChoiceItems = null;
        /** 单选对话框: 默认选中条目position */
        private int singleChoiceDefaultItem = -1;
        /** 单选对话框: 切换选择监听 */
        private OnClickListener singleChoiceOnClickListener;
        /** 多选对话框: 多选条目数组 */
        private CharSequence[] multiChoiceItems = null;
        /** 多选对话框: 默认数组选中状态 */
        private Boolean[] multiChoiceDefaultItems = null;
        /** 多选对话框: 切换选择监听 */
        private OnMultiChoiceClickListener multiChoiceClickListener;
        /**表示是否填充状态栏*/
        private boolean isTransparent;

        public TipsBuilder setDialogBgResId(int dialogBgResId) {
            this.dialogBgResId = dialogBgResId;
            return this;
        }

        public TipsBuilder setDialogBgColor(int dialogBgColor) {
            this.dialogBgColor = dialogBgColor;
            return this;
        }

        public TipsBuilder setDialogWidthMatch(boolean dialogWidthMatch) {
            this.dialogWidthMatch = dialogWidthMatch;
            return this;
        }

        public TipsBuilder setTitleLayoutVisibility(int titleLayoutVisibility) {
            this.titleLayoutVisibility = titleLayoutVisibility;
            return this;
        }

        public TipsBuilder setTitleBgResId(int titleBgResId) {
            this.titleBgResId = titleBgResId;
            return this;
        }

        public TipsBuilder setTitleIcon(int titleIcon) {
            this.titleIcon = titleIcon;
            return this;
        }

        public TipsBuilder setTitleText(CharSequence titleText) {
            this.titleText = titleText;
            return this;
        }

        public TipsBuilder setTitleText(int titleTextResId) {
            this.titleText = mActivity.getString(titleTextResId);
            return this;
        }

        public TipsBuilder setTitleTextColor(int titleTextColor) {
            this.titleTextColor = titleTextColor;
            return this;
        }

        public TipsBuilder setTitleDividerlineVisibility(int titleDividerlineVisibility) {
            this.titleDividerlineVisibility = titleDividerlineVisibility;
            return this;
        }

        public TipsBuilder setContentText(CharSequence contentText) {
            this.contentText = contentText;
            return this;
        }

        public TipsBuilder setContentTextSize(float contentTextSize) {
            this.contentTextSize = contentTextSize;
            return this;
        }

        public TipsBuilder setContentTextColor(int contentTextColor) {
            this.contentTextColor = contentTextColor;
            return this;
        }

        public TipsBuilder setContentTextGravity(int contentTextGravity) {
            this.contentTextGravity = contentTextGravity;
            return this;
        }

        public TipsBuilder setCustomContentView(View customContentView) {
            this.customContentView = customContentView;
            return this;
        }

        public TipsBuilder setNegativeBtn(CharSequence negativeBtnText, OnClickListener listener) {
            this.negativeBtnText = negativeBtnText;
            this.negativeClickListener = listener;
            return this;
        }

        public TipsBuilder setNegativeBtnTextColor(int negativeBtnTextColor) {
            this.negativeBtnTextColor = negativeBtnTextColor;
            return this;
        }

        public TipsBuilder setNegativeBtnBgResId(int negativeBtnBgResId) {
            this.negativeBtnBgResId = negativeBtnBgResId;
            return this;
        }

        public TipsBuilder setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
            this.canceledOnTouchOutside = canceledOnTouchOutside;
            return this;
        }

        public TipsBuilder setCanceled(boolean canceled) {
            this.canceled = canceled;
            return this;
        }

        public TipsBuilder setDialogInOutAnimResId(int dialogInOutAnimResId) {
            this.dialogInOutAnimResId = dialogInOutAnimResId;
            return this;
        }

        public TipsBuilder setCustomContentViewGif(int customContentViewGifResId) {
            this.customContentViewGifResId = customContentViewGifResId;
            return this;
        }

        public TipsBuilder setDialogWindowBgDimAcount(float dialogWindowBgDimAcount) {
            this.dialogWindowBgDimAcount = dialogWindowBgDimAcount;
            return this;
        }

        public TipsBuilder setDialogGravity(int dialogGravity) {
            this.dialogGravity = dialogGravity;
            return this;
        }

        public TipsBuilder setSingleChoiceItems(CharSequence[] items, int checkedItem, OnClickListener listener) {
            this.singleChoiceItems = items;
            this.singleChoiceDefaultItem = checkedItem;
            this.singleChoiceOnClickListener = listener;
            return this;
        }

        public TipsBuilder setMultiChoiceItems(CharSequence[] items, Boolean[] checkedItems, OnMultiChoiceClickListener listener) {
            this.multiChoiceItems = items;
            this.multiChoiceDefaultItems = checkedItems;
            this.multiChoiceClickListener = listener;
            return this;
        }

        public TipsBuilder(Activity activity) {
            this.mActivity = activity;
            mLayoutInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public TipsBuilder setWindowTitleTran(boolean isTransparent){
            this.isTransparent = isTransparent;
            return this;
        }

        @SuppressLint("InflateParams")
        public CustomDialog create() {
            dialog = new CustomDialog(mActivity, R.style.CD_Dialog_Theme);
            mView = mLayoutInflater.inflate(R.layout.cd_dialog_content_tips, null);

            //对话框宽度
            LinearLayout uiDialogLayout = (LinearLayout) mView.findViewById(R.id.cd_layout_dialog);
            int dialogWidth = DeviceInfo.ScreenWidthPixels - UIUtil.dip2px(60);
            if (dialogWidth < 0) {
                dialogWidth = DeviceInfo.ScreenWidthPixels - 50;//极小分辨率屏幕,一般是没有了这种的
            }
            if (dialogWidthMatch) {
                dialogWidth = DeviceInfo.ScreenWidthPixels;
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dialogWidth, LayoutParams.WRAP_CONTENT);
            uiDialogLayout.setLayoutParams(layoutParams);

            //对话框背景资源(上部内容和底部取消按钮)
            LinearLayout uiDialogLayoutContent = (LinearLayout) mView.findViewById(R.id.cd_layout_dialog_content);
            LinearLayout uiDialogLayoutBtns = (LinearLayout) mView.findViewById(R.id.cd_layout_btns);
            if (dialogBgResId != -1) {
                uiDialogLayoutContent.setBackgroundResource(dialogBgResId);
                uiDialogLayoutBtns.setBackgroundResource(dialogBgResId);
            } else if (dialogBgColor != -1) {
                uiDialogLayoutContent.setBackgroundColor(dialogBgColor);
                uiDialogLayoutBtns.setBackgroundColor(dialogBgColor);
            }

            //标题
            LinearLayout uiLayoutTitle = (LinearLayout) mView.findViewById(R.id.cd_layout_title);
            //标题栏显示状态
            uiLayoutTitle.setVisibility(titleLayoutVisibility);
            //标题栏背景资源
            if (titleBgResId != -1) {
                uiLayoutTitle.setBackgroundResource(titleBgResId);
            }
            //标题icon
            uiTitleIcon = (ImageView) mView.findViewById(R.id.cd_title_icon);
            if (titleIcon != -1) {
                uiTitleIcon.setVisibility(View.VISIBLE);
                uiTitleIcon.setImageResource(titleIcon);
            }
            //标题文本内容
            uiTitleText = (TextView) mView.findViewById(R.id.cd_title_text);
            if (titleText != null && titleText.length() > 0) {
                uiTitleText.setText(titleText);
            }
            //标题文本颜色
            if (titleTextColor != -1) {
                uiTitleText.setTextColor(titleTextColor);
            }
            //标题分割线显示状态
            mView.findViewById(R.id.cd_dividerline_title).setVisibility(titleDividerlineVisibility);

            // 对话框内容
            uiLayoutContent = (LinearLayout) mView.findViewById(R.id.cd_layout_content);
            if (contentText != null) {//默认状态
                uiContentText = ((TextView) mView.findViewById(R.id.cd_content_message));
                uiContentText.setText(contentText);
                if (contentTextSize != -1) {
                    uiContentText.setTextSize(contentTextSize);
                }
                if (contentTextColor != -1) {
                    uiContentText.setTextColor(contentTextColor);
                }
                uiContentText.setGravity(contentTextGravity);
            } else if (singleChoiceItems != null) {//单选
                uiLayoutContent.removeAllViews();
                uiLayoutContent.addView(initSingleChoiceItemsDialogContent(), new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            } else if (multiChoiceItems != null) {//多选
                uiLayoutContent.removeAllViews();
                uiLayoutContent.addView(initMultiChoiceItemsDialogContent(), new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            } else if (customContentView != null) {//自定义内容视图
                uiLayoutContent.removeAllViews();
                uiLayoutContent.addView(customContentView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            } else if (customContentViewGifResId != -1) {//自定义显示gif
                GifView gifView = new GifView(mActivity);
                gifView.setMovieResource(customContentViewGifResId);
                uiLayoutContent.removeAllViews();
                uiLayoutContent.setGravity(Gravity.CENTER);
                uiLayoutContent.addView(gifView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            }

            //底部按钮
            //底部按钮:negative
            TextView uiBtnNegative = (TextView) mView.findViewById(R.id.cd_btn_negative);
            if (negativeBtnText != null && negativeBtnText.length() > 0) {
                uiBtnNegative.setText(negativeBtnText);
                if (negativeBtnTextColor != -1) {
                    uiBtnNegative.setTextColor(negativeBtnTextColor);
                }
                if (negativeBtnBgResId != -1) {
                    uiBtnNegative.setBackgroundResource(negativeBtnBgResId);
                }
                uiBtnNegative.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (negativeClickListener != null) {
                            negativeClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                        }
                        dialog.cancel();
                    }
                });
            } else {
                uiBtnNegative.setVisibility(View.GONE);
                mView.findViewById(R.id.cd_layout_btns).setVisibility(View.GONE);
            }

            dialog.setContentView(mView);// 添加视图
            dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
            dialog.setCancelable(canceled);

            // 位置
            Window window = dialog.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            //填充状态栏
            if (isTransparent){
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

            }

            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
            if (dialogWindowBgDimAcount != 2f && dialogWindowBgDimAcount >= 0f && dialogWindowBgDimAcount <= 1.0f) {
                lp.dimAmount = dialogWindowBgDimAcount;
            }
            window.setGravity(dialogGravity); // 设置dialog显示的位置
            if (dialogInOutAnimResId != -1) {
                window.setWindowAnimations(dialogInOutAnimResId); // 添加动画
            } else {
                window.setWindowAnimations(R.style.CD_Dialog_Anim_Tips); // 添加动画
            }
            return dialog;
        }

        /** 初始化单选框视图 */
        private View initSingleChoiceItemsDialogContent() {
            final RadioGroup radioGroup = new RadioGroup(mView.getContext());
            radioGroup.setOrientation(LinearLayout.VERTICAL);
            CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    for (int x = 0; x < singleChoiceItems.length; x++) {
                        RadioButton radioButton = (RadioButton) radioGroup.getChildAt(x);
                        if (radioButton != null) {
                            radioButton.setChecked(false);
                        }
                    }
                    buttonView.setChecked(isChecked);
                    if (singleChoiceOnClickListener != null) {
                        singleChoiceOnClickListener.onClick(dialog, (int) buttonView.getTag());
                    }
                }
            };
            for (int x = 0; x < singleChoiceItems.length; x++) {
                RadioButton radioButton = new RadioButton(mView.getContext());
                radioButton.setText(singleChoiceItems[x]);
                radioButton.setChecked(false);
                radioButton.setTag(x);
                radioButton.setOnCheckedChangeListener(onCheckedChangeListener);
                if (x == singleChoiceDefaultItem) {
                    radioButton.setChecked(true);
                }
                radioGroup.addView(radioButton, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            }

            return radioGroup;
        }

        /** 初始化多选框视图 */
        private View initMultiChoiceItemsDialogContent() {
            final List<Boolean> tempList = new ArrayList<>();
            final RadioGroup radioGroup = new RadioGroup(mView.getContext());
            radioGroup.setOrientation(LinearLayout.VERTICAL);
            CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    tempList.clear();
                    for (int x = 0; x < multiChoiceItems.length; x++) {
                        CheckBox checkBox = (CheckBox) radioGroup.getChildAt(x);
                        if (checkBox != null) {
                            tempList.add(checkBox.isChecked());
                        }
                    }
                    buttonView.setChecked(isChecked);
                    if (multiChoiceClickListener != null) {
                        multiChoiceClickListener.onClick((int) buttonView.getTag(), isChecked, tempList.toArray(new Boolean[tempList.size()]));
                    }
                }
            };
            radioGroup.removeAllViews();
            for (int x = 0; x < multiChoiceItems.length; x++) {
                CheckBox checkBox = new CheckBox(mView.getContext());
                checkBox.setText(multiChoiceItems[x]);
                checkBox.setChecked(false);
                checkBox.setTag(x);
                checkBox.setOnCheckedChangeListener(onCheckedChangeListener);
                radioGroup.addView(checkBox, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            }
            for (int x = 0; x < multiChoiceDefaultItems.length; x++) {
                CheckBox checkBox = (CheckBox) radioGroup.getChildAt(x);
                if (checkBox != null) {
                    checkBox.setChecked(multiChoiceDefaultItems[x]);
                }
            }
            return radioGroup;
        }
    }

}