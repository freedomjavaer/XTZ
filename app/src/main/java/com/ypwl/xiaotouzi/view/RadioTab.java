package com.ypwl.xiaotouzi.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.utils.UIUtil;

import java.util.List;

/**
 * function : Tab切换.
 * <p/>
 * Created by lzj on 2016/1/22.
 */
@SuppressWarnings("unused")
public class RadioTab extends RadioGroup implements CompoundButton.OnCheckedChangeListener {
    private OnTabCheckedListener mOnTabCheckedListener;
    private int mResIdTabLeft = R.drawable.rb_selector_radiotab_bg_left;
    private int mResIdTabMiddle = R.drawable.rb_selector_radiotab_bg_middle;
    private int mResIdTabRight = R.drawable.rb_selector_radiotab_bg_right;
    private ColorStateList mResCsl = getResources().getColorStateList(R.color.rb_selector_color_radiotab_txt);
    private OnTabCheckedForGroupListener mOnTabCheckedForGroupListener;
    private float mTabTitleTextSize = -1;
    private int mMinWidth = -1;

    public RadioTab(Context context) {
        this(context, null);
    }

    public RadioTab(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        setOrientation(RadioGroup.HORIZONTAL);
    }

    /**
     * 设置tab文字颜色状态选择器.
     *
     * @param colorResId eg:R.color.rb_selector_color_radiotab_txt
     */
    public RadioTab setTabTitleTextColorBackgroundResource(int colorResId) {
        this.mResCsl = getResources().getColorStateList(colorResId);
        return this;
    }

    /**
     * 设置左边tab背景颜色状态选择器.
     *
     * @param resId eg:R.drawable.rb_selector_radiotab_bg_left
     */
    public RadioTab setTabLeftBackgroundResource(int resId) {
        this.mResIdTabLeft = resId;
        return this;
    }

    /**
     * 设置中间tab背景颜色状态选择器.
     *
     * @param resId eg:R.drawable.rb_selector_radiotab_bg_middle
     */
    public RadioTab setTabMiddleBackgroundResource(int resId) {
        this.mResIdTabMiddle = resId;
        return this;
    }

    /**
     * 设置右边tab背景颜色状态选择器.
     *
     * @param resId eg:R.color.rb_selector_radiotab_bg_right
     */
    public RadioTab setTabRightBackgroundResource(int resId) {
        this.mResIdTabRight = resId;
        return this;
    }

    /**
     * set Listener for tab check change
     *
     * @param listener a OnTabCheckedListener
     */
    public RadioTab setOnTabCheckedListener(OnTabCheckedListener listener) {
        this.mOnTabCheckedListener = listener;
        return this;

    }

    public RadioTab setOnTabCheckedForGroupListener(OnTabCheckedForGroupListener listener) {
        this.mOnTabCheckedForGroupListener = listener;
        return this;

    }

    /**
     * set Tab title text's Size
     * <p/>
     * 在 addTabs 之前设置才会生效
     *
     * @param size Tab title text's Size,Unit is SP
     */
    public RadioTab setTabTitleTextSize(float size) {
        this.mTabTitleTextSize = size;
        return this;
    }

    /**
     * set Tab title text's minWidth
     * <p/>
     * 在 addTabs 之前设置才会生效
     *
     * @param minWidth
     * @return
     */
    public RadioTab setTabTitleMinWidth(int minWidth) {
        this.mMinWidth = minWidth;
        return this;
    }

    public void addTabs(List<String> tabList) {
        addTabs(tabList, false);
    }

    public void addTabs(List<String> tabList, boolean notPaddingTopAndBottom) {
        addTabs(tabList, notPaddingTopAndBottom, -1);
    }

    /**
     * add tabs title into layout
     *
     * @param tabList                title list
     * @param notPaddingTopAndBottom 是否需要设置条目tab的左右padding值
     * @param paddingLeftAndRight    条目tab的左右padding值
     */
    public void addTabs(List<String> tabList, boolean notPaddingTopAndBottom, int paddingLeftAndRight) {
        if (tabList == null || tabList.size() == 0) {
            throw new RuntimeException("tabList can not be null or empty!!!");
        }
        LayoutParams params = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
        params.gravity = Gravity.CENTER;
        for (int x = 0; x < tabList.size(); x++) {
            RadioButton radioButton = (RadioButton) UIUtil.inflate(R.layout.view_radiotab_item);
            radioButton.setTag(x);
            radioButton.setText(tabList.get(x));
            radioButton.setTextColor(mResCsl);
            radioButton.setChecked(false);
            if (mMinWidth != -1) {
                radioButton.setMinWidth(mMinWidth);
            }
            if (mTabTitleTextSize != -1) {
                radioButton.setTextSize(mTabTitleTextSize);
            }
            if (notPaddingTopAndBottom) {
                int paddingL = paddingLeftAndRight == -1 ? radioButton.getPaddingLeft() : paddingLeftAndRight;
                int paddingR = paddingLeftAndRight == -1 ? radioButton.getPaddingRight() : paddingLeftAndRight;
                radioButton.setPadding(paddingL, 0, paddingR, 0);
            }
            radioButton.setOnCheckedChangeListener(this);
            radioButton.setBackgroundResource(mResIdTabMiddle);
            if (x == 0) {
                radioButton.setBackgroundResource(mResIdTabLeft);
            }
            if (x == tabList.size() - 1) {
                radioButton.setBackgroundResource(mResIdTabRight);
            }
            addView(radioButton, params);
        }
        this.onCheckedChanged((CompoundButton) getChildAt(0), true);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int childCount = getChildCount();
        for (int x = 0; x < childCount; x++) {
            CompoundButton child = (CompoundButton) getChildAt(x);
            child.setChecked(false);
        }
        buttonView.setChecked(isChecked);
        if (isChecked && mOnTabCheckedListener != null) {
            currentTabPosition = (int) buttonView.getTag();
            mOnTabCheckedListener.onChecked(buttonView, buttonView.getText(), (Integer) buttonView.getTag());
        }
        if (isChecked && mOnTabCheckedForGroupListener != null) {
            currentTabPosition = (int) buttonView.getTag();
            mOnTabCheckedForGroupListener.onChecked(this, buttonView, buttonView.getText(), (Integer) buttonView.getTag());
        }
    }

    /**
     * 设置需要显示的按钮
     *
     * @param position
     */
    public void setSelect(int position) {
        for (int x = 0; x < getChildCount(); x++) {
            CompoundButton child = (CompoundButton) getChildAt(x);
            if (x == position) {
                child.setChecked(true);
            } else {
                child.setChecked(false);
            }
        }
        if (mOnTabCheckedListener != null) {
            currentTabPosition = position;
            mOnTabCheckedListener.onChecked(getChildAt(position), ((RadioButton) getChildAt(position)).getText(), position);
        }
        if (mOnTabCheckedForGroupListener != null) {
            currentTabPosition = position;
            mOnTabCheckedForGroupListener.onChecked(this, getChildAt(position), ((RadioButton) getChildAt(position)).getText(), position);
        }
    }

    private int currentTabPosition = 0;

    /** 获取当前选中的tab位置 */
    public int getCurrentSelectedTabposition() {
        return this.currentTabPosition;
    }

    /**
     * Listener for tab check change
     */
    public interface OnTabCheckedListener {
        void onChecked(View view, CharSequence checkedTitle, int checkedIndex);
    }

    /**
     * Listener for tab check change
     */
    public interface OnTabCheckedForGroupListener {
        void onChecked(View viewgroup, View view, CharSequence checkedTitle, int checkedIndex);
    }

}
