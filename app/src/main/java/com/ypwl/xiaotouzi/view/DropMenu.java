package com.ypwl.xiaotouzi.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.utils.UIUtil;

import java.util.List;

/**
 * function : 下落式筛选菜单
 *
 * <p>Created by lzj on 2016/3/18.</p>
 */
@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class DropMenu extends LinearLayout {

    //tab布局的宽度
    private int size_tab_layout_width = ViewGroup.LayoutParams.MATCH_PARENT;
    //tab布局的高度
    private int size_tab_layout_heigth = ViewGroup.LayoutParams.WRAP_CONTENT;
    //popView布局的宽度
    private int size_popview_layout_width = FrameLayout.LayoutParams.MATCH_PARENT;
    // menuTab组下划线颜色
    private int color_underline = 0xfffafafa;
    // TAB分割线颜色
    private int color_divider = 0xfffafafa;
    // tab选中颜色
    private int color_text_selected = 0xff3f51b5;
    // tab未选中颜色
    private int color_text_unselected = 0xff111111;
    // tab组背景色
    private int color_tabs_background = 0xffffffff;
    // 遮罩颜色
    private int color_mask = 0x88888888;
    // tab字体大小
    private int size_tab_text = 14;
    // tab选中图标
    private int icon_selected = -1;
    // tab未选中图标
    private int icon_unselected = -1;
    //icon指示器的位置,默认在文字右边
    private int icon_position = 3;
    // Tabs组布局容器
    private LinearLayout tabsLayout;
    // tab内容视图层，包含内容窗口和底部暗色遮罩
    private FrameLayout contentViewContainer;
    // 弹出菜单父布局
    private FrameLayout popupViewContainer;
    // 暗色遮罩视图
    private View maskView;
    // Tabs组布局容器里选中的tab位置，-1表示未选中
    private int current_tab_position = -1;
    // 动画时长
    private static final long AINM_TIME = 200;
    private boolean isShowing = false;

    public DropMenu(Context context) {
        this(context, null);
    }

    public DropMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DropMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOrientation(VERTICAL);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DropDownMenu);
            size_tab_layout_width = a.getDimensionPixelSize(R.styleable.DropDownMenu_dm_size_tab_layout_width, size_tab_layout_width);
            size_tab_layout_heigth = a.getDimensionPixelSize(R.styleable.DropDownMenu_dm_size_tab_layout_heigth, size_tab_layout_heigth);
            size_popview_layout_width = a.getDimensionPixelSize(R.styleable.DropDownMenu_dm_size_popview_layout_width, size_popview_layout_width);
            color_underline = a.getColor(R.styleable.DropDownMenu_dm_color_underline, color_underline);
            color_divider = a.getColor(R.styleable.DropDownMenu_dm_color_divider, color_divider);
            color_text_selected = a.getColor(R.styleable.DropDownMenu_dm_color_text_selected, color_text_selected);
            color_text_unselected = a.getColor(R.styleable.DropDownMenu_dm_color_text_unselected, color_text_unselected);
            color_tabs_background = a.getColor(R.styleable.DropDownMenu_dm_color_tabs_backgound, color_tabs_background);
            color_mask = a.getColor(R.styleable.DropDownMenu_dm_color_mask, color_mask);
            size_tab_text = a.getDimensionPixelSize(R.styleable.DropDownMenu_dm_size_text, size_tab_text);
            icon_selected = a.getResourceId(R.styleable.DropDownMenu_dm_icon_selected, icon_selected);
            icon_unselected = a.getResourceId(R.styleable.DropDownMenu_dm_icon_unselected, icon_unselected);
            icon_position = a.getInt(R.styleable.DropDownMenu_dm_icon_position, icon_position);
            a.recycle();
        }

        // 加入tabs容器布局
        tabsLayout = new LinearLayout(context);
        tabsLayout.setOrientation(HORIZONTAL);
        tabsLayout.setBackgroundColor(color_tabs_background);
        LayoutParams params = new LayoutParams(size_tab_layout_width, size_tab_layout_heigth);
        params.gravity = Gravity.CENTER;
        tabsLayout.setLayoutParams(params);
        addView(tabsLayout);

        // tabs容器布局添加下划线
        View underLine = new View(context);
        underLine.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpTpPx(1.0f)));
        underLine.setBackgroundColor(color_underline);
        addView(underLine);

        // 加入tab内容视图
        contentViewContainer = new FrameLayout(context);
        contentViewContainer.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        addView(contentViewContainer);
    }

    /**
     * 设置下落Menu数据
     *
     * @param tabTextsList   tab文本集合
     * @param popupViewsList tab对应的弹窗内容视图
     * @param contentView    用户定义内容视图
     */
    public void setDropMenu(List<String> tabTextsList, List<View> popupViewsList, View contentView) {
        if (tabTextsList == null || popupViewsList == null) {
            throw new IllegalArgumentException("params not match,tabTextsList and popupViewsList should not be null");
        }
        if (tabTextsList.size() != popupViewsList.size()) {
            throw new IllegalArgumentException("params not match, tabTextsList.size() should be equal popupViewsList.size()");
        }

        for (int i = 0; i < tabTextsList.size(); i++) {
            addTab(tabTextsList, i);
        }
        if (contentView != null) {
            contentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            contentViewContainer.addView(contentView);
        }

        maskView = new View(getContext());
        maskView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        maskView.setBackgroundColor(color_mask);
        maskView.setClickable(true);
        contentViewContainer.addView(maskView);
        maskView.setVisibility(GONE);

        popupViewContainer = new FrameLayout(getContext());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(size_popview_layout_width, FrameLayout.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        // TODO: 2016/6/1 边际阴影图对齐顶部
        layoutParams.topMargin = -UIUtil.dip2px(3);
        popupViewContainer.setLayoutParams(layoutParams);
        popupViewContainer.setVisibility(GONE);
        contentViewContainer.addView(popupViewContainer);

        for (int i = 0; i < popupViewsList.size(); i++) {
            if (popupViewsList.get(i) == null) {
                continue;
            }
            View popView = popupViewsList.get(i);
            popView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            popupViewContainer.addView(popView, i);
        }
    }

    private void addTab(List<String> tabTexts, int i) {
        ImageView icon = new ImageView(getContext());
        icon.setImageDrawable(getIconDrawable(icon_unselected));
        icon.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        TextView tab = new TextView(getContext());
        tab.setTag(icon);
        tab.setSingleLine();
        tab.setEllipsize(TextUtils.TruncateAt.END);
        tab.setGravity(Gravity.CENTER);
        tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, size_tab_text);
        tab.setTextColor(color_text_unselected);
        tab.setText(tabTexts.get(i));
        tab.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));


        final LinearLayout singleTabLayout = new LinearLayout(getContext());
        singleTabLayout.setGravity(Gravity.CENTER);
        switch (icon_position) {
            case 1://左
                singleTabLayout.setOrientation(HORIZONTAL);
                singleTabLayout.addView(icon);
                singleTabLayout.addView(tab);
                break;
            case 2://上
                singleTabLayout.setOrientation(VERTICAL);
                singleTabLayout.addView(icon);
                singleTabLayout.addView(tab);
                break;
            case 3://右
                singleTabLayout.setOrientation(HORIZONTAL);
                singleTabLayout.addView(tab);
                singleTabLayout.addView(icon);
                break;
            case 4://下
                singleTabLayout.setOrientation(VERTICAL);
                singleTabLayout.addView(tab);
                singleTabLayout.addView(icon);
                break;
        }
        singleTabLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switchMenu(singleTabLayout);
            }
        });

        LayoutParams layoutParams = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        layoutParams.gravity = Gravity.CENTER;
        tabsLayout.addView(singleTabLayout, layoutParams);

        // 添加tab直接的分割线
        if (i < tabTexts.size() - 1) {
            View view = new View(getContext());
            view.setLayoutParams(new LayoutParams(dpTpPx(1f), ViewGroup.LayoutParams.MATCH_PARENT));
            view.setBackgroundColor(color_divider);
            tabsLayout.addView(view);
        }
    }

    /** 设置当前Tab位置 */
    public void setTabCurrentPosition(int currentTabPosition) {
        this.current_tab_position = currentTabPosition;
    }

    /**
     * 设置tab文字
     *
     * @param tabText 当前tab要显示的文字
     */
    public void setTabText(String tabText) {
        if (current_tab_position != -1) {
            TextView tab = null;
            LinearLayout tabLayout = (LinearLayout) tabsLayout.getChildAt(current_tab_position);
            if (tabLayout == null) {
                return;
            }
            for (int x = 0; x < tabLayout.getChildCount(); x++) {
                View childInTabLayout = tabLayout.getChildAt(x);
                if (childInTabLayout instanceof TextView) {
                    tab = (TextView) childInTabLayout;
                }
            }
            if (tab == null) {
                return;
            }
            tab.setText(tabText);
        }
    }

    /** 设置外部点击是否可以关闭窗体 */
    public void setCancleOutside(boolean cancle) {
        if (cancle) {
            maskView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeMenu();
                }
            });
        }
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        if (current_tab_position != -1) {
            TextView tab = null;
            LinearLayout tabLayout = (LinearLayout) tabsLayout.getChildAt(current_tab_position);
            for (int x = 0; x < tabLayout.getChildCount(); x++) {
                View childInTabLayout = tabLayout.getChildAt(x);
                if (childInTabLayout instanceof TextView) {
                    tab = (TextView) childInTabLayout;
                }
            }
            if (tab == null) {
                return;
            }
            tab.setTextColor(color_text_unselected);
            setTextViewCompoundDrawable(tab, false);
            popupViewContainer.setVisibility(View.GONE);
            TranslateAnimation animPopViewOut = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT,
                    0, Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, -1.0f);
            animPopViewOut.setDuration(AINM_TIME);
            popupViewContainer.setAnimation(animPopViewOut);
            maskView.setVisibility(GONE);
            AlphaAnimation animMaskOut = new AlphaAnimation(1.0f, 0f);
            animMaskOut.setDuration(AINM_TIME);
            maskView.setAnimation(animMaskOut);
            current_tab_position = -1;
            isShowing = false;
        }
    }

    /** 菜单是否下拉展开状态 */
    public boolean isShowing() {
        return isShowing;
    }

    /** 切换菜单 */
    private void switchMenu(View target) {
        for (int i = 0; i < tabsLayout.getChildCount(); i = i + 2) {
            TextView tab = null;
            LinearLayout tabLayout = (LinearLayout) tabsLayout.getChildAt(i);
            for (int x = 0; x < tabLayout.getChildCount(); x++) {
                View childInTabLayout = tabLayout.getChildAt(x);
                if (childInTabLayout instanceof TextView) {
                    tab = (TextView) childInTabLayout;
                }
            }
            if (tab == null) {
                return;
            }
            if (target == tabLayout) {
                if (current_tab_position == i) {
                    closeMenu();
                } else {
                    if (current_tab_position == -1) {
                        popupViewContainer.setVisibility(View.VISIBLE);
                        TranslateAnimation animPopViewIn = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT,
                                0, Animation.RELATIVE_TO_PARENT, -1.0f, Animation.RELATIVE_TO_PARENT, 0);
                        animPopViewIn.setDuration(AINM_TIME);
                        popupViewContainer.setAnimation(animPopViewIn);
                        maskView.setVisibility(VISIBLE);
                        AlphaAnimation animMaskIn = new AlphaAnimation(0f, 1.0f);
                        animMaskIn.setDuration(AINM_TIME);
                        maskView.setAnimation(animMaskIn);
                        popupViewContainer.getChildAt(i / 2).setVisibility(View.VISIBLE);
                    } else {
                        popupViewContainer.getChildAt(i / 2).setVisibility(View.VISIBLE);
                    }
                    setTabCurrentPosition(i);
                    tab.setTextColor(color_text_selected);
                    setTextViewCompoundDrawable(tab, true);
                    isShowing = true;
                }
            } else {
                tab.setTextColor(color_text_unselected);
                setTextViewCompoundDrawable(tab, false);
                popupViewContainer.getChildAt(i / 2).setVisibility(View.GONE);
            }
        }
    }

    /**
     * 设置TextView周边的图片
     *
     * @param tab           tab的TextView
     * @param selectedState true-选中状态，false-未选中状态
     */
    private void setTextViewCompoundDrawable(TextView tab, boolean selectedState) {
        if (icon_unselected == -1 || icon_selected == -1) {
            return;
        }
        ImageView icon = (ImageView) tab.getTag();
        icon.setImageDrawable(getIconDrawable(selectedState ? icon_selected : icon_unselected));
    }

    @SuppressWarnings("deprecation")
    private Drawable getIconDrawable(int id) {
        return getResources().getDrawable(id);
    }

    private int dpTpPx(float value) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, dm) + 0.5);
    }
}
