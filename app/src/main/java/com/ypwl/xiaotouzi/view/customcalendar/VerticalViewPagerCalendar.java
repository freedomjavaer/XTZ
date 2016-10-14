package com.ypwl.xiaotouzi.view.customcalendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.utils.DateTimeUtil;
import com.ypwl.xiaotouzi.utils.LogUtil;

import java.util.Calendar;
import java.util.Map;

/**
 * function :垂直滑动viewpager
 * <p/>
 * Created by tengtao on 2016/4/27.
 */
public class VerticalViewPagerCalendar extends ViewPager {

    private CustomCalendarListener mCalendarListener;
    private VerticalViewPagerAdapter mAdapter;
    private TypedArray typedArray;
    private int previousMonthCount;
    private String mCurrentYearMonth="";
    private OnCurrentYearAndMonthListener mListener;

    public VerticalViewPagerCalendar(Context context) {
        super(context);
        init();
    }

    public VerticalViewPagerCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.DynamicCalendarView);
        previousMonthCount = typedArray.getInt(R.styleable.DynamicCalendarView_previousDateCount, 12);
        init();
    }

    private void init() {
        setPageTransformer(false, new DefaultTransformer());
        setOverScrollMode(OVER_SCROLL_NEVER);
        addOnPageChangeListener(mOnPageChangeListener);
    }

    /** 设置日历改变监听事件 */
    public void setDynamicCalendarListener(CustomCalendarListener listener) {
        this.mCalendarListener = listener;
        if (mAdapter == null) {
            mAdapter = new VerticalViewPagerAdapter(getContext(), mCalendarListener, typedArray,previousMonthCount);
        }
        setAdapter(mAdapter);
        setCurrentItem(previousMonthCount + Calendar.getInstance().get(Calendar.MONTH));
    }

    /** 页面切换效果 */
    public class DefaultTransformer implements ViewPager.PageTransformer {
        @Override
        public void transformPage(View view, float position) {
            float alpha = 0;
            if (0 <= position && position <= 1) {
                alpha = 1 - position;
            } else if (-1 < position && position < 0) {
                alpha = position + 1;
            }
            view.setAlpha(alpha);
            view.setTranslationX(view.getWidth() * -position);
            float yPosition = position * view.getHeight();
            view.setTranslationY(yPosition);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean intercept = super.onInterceptTouchEvent(swapTouchEvent(event));
        //If not intercept, touch event should not be swapped.
        swapTouchEvent(event);
        return intercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(swapTouchEvent(ev));
    }

    /** 事件处理 */
    private MotionEvent swapTouchEvent(MotionEvent event) {
        float width = getWidth();
        float height = getHeight();
        float swappedX = (event.getY() / height) * width;
        float swappedY = (event.getX() / width) * height;
        event.setLocation(swappedX, swappedY);
        return event;
    }


    private OnPageChangeListener mOnPageChangeListener = new OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(final int position) {
            /**滚动的当前年月*/
            long millis = mAdapter.getYearAndMonth(position);
            String formatDateTime = DateTimeUtil.formatDateTime(millis,"yyyy-MM-dd");
            String yearAndMonth = formatDateRange(millis);
            if(!mCurrentYearMonth.equalsIgnoreCase(yearAndMonth)){
                mCurrentYearMonth = yearAndMonth;
                LogUtil.e("当前选中的年月：",yearAndMonth);
                String[] split = formatDateTime.split("-");
                int year=0;
                int month=0;
                try {
                    year = Integer.parseInt(split[0]);
                    month = Integer.parseInt(split[1]);
                }catch (Exception e){e.printStackTrace();}
                mCalendarListener.onMonthChanged(year,month);
            }
            mListener.onCurrentYearMonth(yearAndMonth);
            /**改变高度*/
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    //通过id获取当前view的高度
                    int firstItemHeight = 0;
                    View viewById = findViewById(position);
                    if(viewById != null) {
                        firstItemHeight = viewById.getMeasuredHeight();
                    }
                    LogUtil.e("VerticalViewPager",firstItemHeight+":"+position);
                    if (getMeasuredHeight() != firstItemHeight) {
                        ResizeAnimation resizeAnimation = new ResizeAnimation(VerticalViewPagerCalendar.this, firstItemHeight);
                        resizeAnimation.setDuration(300);
                        startAnimation(resizeAnimation);
                    }
                }
            }, 200);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    /**设置往期的月数*/
    public void setPreviousMonthCount(int count){
        this.previousMonthCount = count;
    }

    /**设置选中数据 */
    public void setSelectedData(Map<String,Integer> backInfos){
        int currentItem = getCurrentItem();
        DynamicMonthView viewById = (DynamicMonthView) findViewById(currentItem);
        mAdapter.refreshSelected(backInfos,viewById);
    }

    /**当前年月接口*/
    public void setOnCurrentYearAndMonthListener(OnCurrentYearAndMonthListener listener){
        this.mListener = listener;
    }

    /** 格式化年月 */
    private String formatDateRange(long millis){
        int flags = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_NO_MONTH_DAY;
        return DateUtils.formatDateRange(getContext(), millis, millis, flags);
    }
}
