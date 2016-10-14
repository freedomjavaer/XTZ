package com.ypwl.xiaotouzi.view.customcalendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.View;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.utils.DateTimeUtil;
import com.ypwl.xiaotouzi.utils.LogUtil;

import java.util.Calendar;
import java.util.Map;

/**
 * function : 动态改变高度的日历
 * <p/>
 * Created by tengtao on 2016/4/22.
 */
public class DynamicRecyclerCalendar extends RecyclerView {

    private CustomCalendarListener mCalendarListener;
    private DynamicRecyclerCalendarAdapter mAdapter;
    private TypedArray typedArray;
    private int previousMonthCount;
    private String mCurrentYearMonth="";
    private OnCurrentYearAndMonthListener mListener;

    public DynamicRecyclerCalendar(Context context) {
        this(context, null);
    }

    public DynamicRecyclerCalendar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DynamicRecyclerCalendar(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (!isInEditMode()) {//非编辑状态
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.DynamicCalendarView);
            previousMonthCount = typedArray.getInt(R.styleable.DynamicCalendarView_previousDateCount, 12);
            setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            this.setOverScrollMode(OVER_SCROLL_NEVER);
            init(context);
        }
    }

    /**初始化*/
    private void init(Context context){
        LinearLayoutManager manager = new LinearLayoutManager(context);
        setLayoutManager(manager);
        setHorizontalScrollBarEnabled(false);
        setVerticalScrollBarEnabled(false);
        addOnScrollListener(mScrollListener);
        setFadingEdgeLength(0);
    }

    /** 设置监听器 */
    public void setDynamicCalendarListener(CustomCalendarListener listener){
        this.mCalendarListener = listener;
        if (mAdapter == null) {
            mAdapter = new DynamicRecyclerCalendarAdapter(getContext(), mCalendarListener, typedArray, previousMonthCount);
        }
        setAdapter(mAdapter);
        //默认加载往前1年
        int i = previousMonthCount + Calendar.getInstance().get(Calendar.MONTH);
        scrollToPosition(i);
        /**初次加载*/
        postDelayed(new Runnable() {
            @Override
            public void run() {
                View childAt = getChildAt(0);
                if(childAt!=null){
                    int firstItemHeight = childAt.getMeasuredHeight();
                    if (getMeasuredHeight() != firstItemHeight) {
                        ResizeAnimation resizeAnimation = new ResizeAnimation(DynamicRecyclerCalendar.this, firstItemHeight);
                        resizeAnimation.setDuration(300);
                        startAnimation(resizeAnimation);
                    }
                }
            }
        }, 100);
        long yearAndMonth = mAdapter.getYearAndMonth(i);
        mListener.onCurrentYearMonth(formatDateRange(yearAndMonth));
        String formatYearMonth = DateTimeUtil.formatDateTime(yearAndMonth,"yyyy-MM-dd");
        String[] splits = formatYearMonth.split("-");
        int year=0;
        int month=0;
        try {
            year = Integer.parseInt(splits[0]);
            month = Integer.parseInt(splits[1]);
        }catch (Exception e){e.printStackTrace();}
        mCalendarListener.onMonthChanged(year,month);//改变月的回调
    }

    /** 滚动监听器 */
    private OnScrollListener mScrollListener = new OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            final View child = recyclerView.getChildAt(0);
            if (child == null) {
                return;
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == SCROLL_STATE_IDLE) {//滚动闲置状态
                View firstChild = getChildAt(0);
                float offset = - firstChild.getY();
                int childAdapterPosition;
                if (offset > firstChild.getMeasuredHeight() / 2) {
                    stopScroll();
                    smoothScrollBy(0, (int) (getChildAt(1).getY()));
                    childAdapterPosition = recyclerView.getChildAdapterPosition(getChildAt(1));
                } else {
                    stopScroll();
                    smoothScrollBy(0, -(int) offset);
                    childAdapterPosition = recyclerView.getChildAdapterPosition(getChildAt(0));
                }
                /**滚动的当前年月*/
                long millis = mAdapter.getYearAndMonth(childAdapterPosition);
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
                        int firstItemHeight = getChildAt(0).getMeasuredHeight();
                        if (getMeasuredHeight() != firstItemHeight) {
                            ResizeAnimation resizeAnimation = new ResizeAnimation(DynamicRecyclerCalendar.this, firstItemHeight);
                            resizeAnimation.setDuration(300);
                            startAnimation(resizeAnimation);
                        }
                    }
                }, 400);
            }
        }
    };

    /**设置往期的月数*/
    public void setPreviousMonthCount(int count){
        this.previousMonthCount = count;
    }

    /**设置选中数据 */
    public void setSelectedData(Map<String,Integer> backInfos){
        mAdapter.refreshSelected(backInfos);
    }

    /** 当前年月监听接口 */
    public void setOnCurrentYearAndMonthListener(OnCurrentYearAndMonthListener listener){
        this.mListener = listener;
    }

    /** 格式化年月 */
    private String formatDateRange(long millis){
        int flags = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_NO_MONTH_DAY;
        return DateUtils.formatDateRange(getContext(), millis, millis, flags);
    }
}
