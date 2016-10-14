package com.ypwl.xiaotouzi.view.customcalendar;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.utils.LogUtil;

import java.security.InvalidParameterException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * function : 每个月视图对象
 * <p/>
 * Created by tengtao on 2016/4/23.
 */
public class DynamicMonthView extends View{
    public static final String VIEW_PARAMS_HEIGHT = "height";//整个view高度
    public static final String VIEW_PARAMS_MONTH = "month";//当前月
    public static final String VIEW_PARAMS_YEAR = "year";//当前年
    public static final String VIEW_PARAMS_SELECTED_DAY = "selected_day";//选中的天
    public static final String VIEW_PARAMS_SELECTED_MONTH = "selected_month";//选中的月
    public static final String VIEW_PARAMS_SELECTED_YEAR = "selected_year";//选中的年
    public static final String VIEW_PARAMS_WEEK_START = "week_start";//一周的第一天
    //每个月选中的日
    public static final String VIEW_PARAMS_DAY_OF_MONTH_SELECTED = "view_params_day_of_month_selected";

    protected static int DEFAULT_HEIGHT = 30;//默认高度
    protected static final int DEFAULT_NUM_ROWS = 6;//默认行数
    protected static int DAY_SELECTED_CIRCLE_SIZE;//默认选中圆大小
    protected static int DAY_SELECTED_RED_POINT_CIRCLE_SIZE;//选中红点圆大小
    protected static int DAY_SEPARATOR_WIDTH = 1;//默认分隔符宽度
    protected static int MINI_DAY_NUMBER_TEXT_SIZE;//默认字体的最小大小
    protected static int MINI_DAY_RED_NUMBER_TEXT_SIZE;
    protected static int MIN_HEIGHT = 10;//最小高度
    protected static int MONTH_HEADER_SIZE;//默认顶部头大小
    protected static int PADDING_BOTTOM;//底部padding值
    protected int mPadding = 0;//设定的padding值
    protected Paint mMonthNumPaint,mSelectedCirclePaint,mCurrentCirclePaint,mSelectedRedCirclePaint;//文字 选中圆 今天圆 选中红点圆画笔
    //相关颜色值
    protected int mCurrentDayTextColor,mDayNumColor,mMonthTitleBGColor,mPreviousDayColor,
            mSelectedDaysColor,mWeekendsColor,mCurrentDayColor,mSelectedRedBgColor;

    protected boolean mHasToday = false;//是否是今天
    //选中的年月日
    protected int mSelectedDay = -1,mSelectedMonth = -1,mSelectedYear = -1;
    protected int mToday = -1;//今天
    protected int mNumDays = 7;//星期的天数
    protected int mNumCells = mNumDays;//每个月的天数
    protected int mWeekStart = 1;//每个星期的第一天
    private int mDayOfWeekStart = 0;//获取是星期几
    protected Boolean mDrawRect;//标记是否画矩形框
    protected int mRowHeight = DEFAULT_HEIGHT;//行高
    protected int mWidth;//行宽
    protected int mYear,mMonth;
    final Time today;//今天
    private final Calendar mCalendar;
    private final Boolean isPrevDayEnabled;//标记今天以前是否可点击
    private int mNumRows = DEFAULT_NUM_ROWS;//每行的天数
    private OnDayClickListener mOnDayClickListener;//点击事件
    private Map<String,Integer> selectedMaps = new HashMap<>();

    public DynamicMonthView(Context context, TypedArray typedArray) {
        super(context);
        Resources resources = context.getResources();
        mCalendar = Calendar.getInstance();
        today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        mCurrentDayTextColor = typedArray.getColor(R.styleable.DynamicMonthView_colorCurrentDay, resources.getColor(R.color.normal_day));
        mDayNumColor = typedArray.getColor(R.styleable.DynamicMonthView_colorNormalDay, resources.getColor(R.color.normal_day));
        //今天以前天颜色
        mPreviousDayColor = typedArray.getColor(R.styleable.DynamicMonthView_colorPreviousDay, resources.getColor(R.color.normal_day));
        //选中的颜色
        mSelectedRedBgColor = typedArray.getColor(R.styleable.DynamicMonthView_colorSelectedRedBackgroud,resources.getColor(R.color.selected_day_red_backgroud));
        mSelectedDaysColor = typedArray.getColor(R.styleable.DynamicMonthView_colorSelectedDayBackground, resources.getColor(R.color.selected_day_background));
        mMonthTitleBGColor = typedArray.getColor(R.styleable.DynamicMonthView_colorSelectedDayText, resources.getColor(R.color.selected_day_text));
        mDrawRect = typedArray.getBoolean(R.styleable.DynamicMonthView_drawRoundRect, false);
        mWeekendsColor = resources.getColor(R.color.weekends_day);//周末颜色
        mCurrentDayColor = resources.getColor(R.color.current_day_background);//当前天的颜色
        //字体大小
        MINI_DAY_NUMBER_TEXT_SIZE = typedArray.getDimensionPixelSize(R.styleable.DynamicMonthView_textSizeDay, resources.getDimensionPixelSize(R.dimen.text_size_day));
        //红点字大小
        MINI_DAY_RED_NUMBER_TEXT_SIZE = typedArray.getDimensionPixelSize(R.styleable.DynamicMonthView_redTextSizeDay,resources.getDimensionPixelSize(R.dimen.red_text_size_day));
        //头部字体大小
        MONTH_HEADER_SIZE = typedArray.getDimensionPixelOffset(R.styleable.DynamicMonthView_headerMonthHeight, resources.getDimensionPixelOffset(R.dimen.header_month_height));
        //选中的圆半径
        DAY_SELECTED_CIRCLE_SIZE = typedArray.getDimensionPixelSize(R.styleable.DynamicMonthView_selectedDayRadius, resources.getDimensionPixelOffset(R.dimen.selected_day_radius));
        //选中红点圆半径
        DAY_SELECTED_RED_POINT_CIRCLE_SIZE = typedArray.getDimensionPixelSize(R.styleable.DynamicMonthView_selectedDayRedPointRadius, resources.getDimensionPixelOffset(R.dimen.selected_day_red_point_radius));
        //底部间距
        PADDING_BOTTOM = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, resources.getDisplayMetrics());
        //行高
        mRowHeight = ((typedArray.getDimensionPixelSize(R.styleable.DynamicMonthView_calendarHeight, resources.getDimensionPixelOffset(R.dimen.calendar_height)) - MONTH_HEADER_SIZE) / 6);
        //今天以前是否可编辑,默认可编辑
        isPrevDayEnabled = typedArray.getBoolean(R.styleable.DynamicMonthView_enablePreviousDay, true);
        initView();//初始化画笔
    }

    /** 画笔初始化 */
    protected void initView() {
        //红点画笔
        mSelectedRedCirclePaint = new Paint();
        mSelectedRedCirclePaint.setFakeBoldText(true);
        mSelectedRedCirclePaint.setAntiAlias(true);
        mSelectedRedCirclePaint.setColor(mSelectedRedBgColor);
        mSelectedRedCirclePaint.setTextAlign(Paint.Align.CENTER);
        mSelectedRedCirclePaint.setStyle(Paint.Style.FILL);
        //选中的画笔
        mSelectedCirclePaint = new Paint();
        mSelectedCirclePaint.setFakeBoldText(true);
        mSelectedCirclePaint.setAntiAlias(true);
        mSelectedCirclePaint.setColor(mSelectedDaysColor);
        mSelectedCirclePaint.setTextAlign(Paint.Align.CENTER);
        mSelectedCirclePaint.setStyle(Paint.Style.FILL);
        //今天的画笔
        mCurrentCirclePaint = new Paint();
        mCurrentCirclePaint.setFakeBoldText(true);
        mCurrentCirclePaint.setAntiAlias(true);
        mCurrentCirclePaint.setColor(mCurrentDayColor);
        mCurrentCirclePaint.setTextAlign(Paint.Align.CENTER);
        mCurrentCirclePaint.setStyle(Paint.Style.FILL);
        //正常日的画笔
        mMonthNumPaint = new Paint();
        mMonthNumPaint.setAntiAlias(true);
        mMonthNumPaint.setTextSize(MINI_DAY_NUMBER_TEXT_SIZE);
        mMonthNumPaint.setStyle(Paint.Style.FILL);
        mMonthNumPaint.setTextAlign(Paint.Align.CENTER);
        mMonthNumPaint.setFakeBoldText(false);
    }

    /** 重置 */
    public void reuse() {
        mNumRows = DEFAULT_NUM_ROWS;
        requestLayout();//请求重新布局
    }

    /** 设置相关数据 */
    public void setMonthParams(HashMap<String, Object> params) {
        if (!params.containsKey(VIEW_PARAMS_MONTH) && !params.containsKey(VIEW_PARAMS_YEAR)) {
            //没有设置年月
            throw new InvalidParameterException("You must specify month and year for this view");
        }
        setTag(params);
        //获取标记数据
        if(params.containsKey(VIEW_PARAMS_DAY_OF_MONTH_SELECTED)){
            Map<String,Integer> maps = (Map<String, Integer>) params.get(VIEW_PARAMS_DAY_OF_MONTH_SELECTED);
            selectedMaps.clear();
            selectedMaps.putAll(maps);
        }
        //动态设置行高
        if (params.containsKey(VIEW_PARAMS_HEIGHT)) {
            mRowHeight = (int) params.get(VIEW_PARAMS_HEIGHT);
            if (mRowHeight < MIN_HEIGHT) {
                mRowHeight = MIN_HEIGHT;
            }
        }
        //选中的天
        if (params.containsKey(VIEW_PARAMS_SELECTED_DAY)) {
            mSelectedDay = (int) params.get(VIEW_PARAMS_SELECTED_DAY);
        }
        //选中的月
        if (params.containsKey(VIEW_PARAMS_SELECTED_MONTH)) {
            mSelectedMonth = (int) params.get(VIEW_PARAMS_SELECTED_MONTH);
        }
        //选中的年
        if (params.containsKey(VIEW_PARAMS_SELECTED_YEAR)) {
            mSelectedYear = (int) params.get(VIEW_PARAMS_SELECTED_YEAR);
        }
        //当前年月
        mMonth = (int) params.get(VIEW_PARAMS_MONTH);
        mYear = (int) params.get(VIEW_PARAMS_YEAR);

        mCalendar.set(Calendar.MONTH, mMonth);
        mCalendar.set(Calendar.YEAR, mYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, 1);
        mDayOfWeekStart = mCalendar.get(Calendar.DAY_OF_WEEK);

        if (params.containsKey(VIEW_PARAMS_WEEK_START)) {
            mWeekStart = (int) params.get(VIEW_PARAMS_WEEK_START);
        } else {
            mWeekStart = mCalendar.getFirstDayOfWeek();
        }
        //是否包含今天
        mHasToday = false;
        mToday = -1;//今天
        mNumCells = CalendarUtils.getDaysInMonth(mMonth, mYear);
        for (int i = 0; i < mNumCells; i++) {
            final int day = i + 1;
            if (sameDay(day, today)) {
                mHasToday = true;
                mToday = day;
            }
        }
        //计算行数
        mNumRows = calculateNumRows();
    }

    /**计算月所需要的行数*/
    private int calculateNumRows() {
        int offset = findDayOffset();
        int dividend = (offset + mNumCells) / mNumDays;
        int remainder = (offset + mNumCells) % mNumDays;
        return (dividend + (remainder > 0 ? 1 : 0));
    }

    /**当前月的其实位置偏移量*/
    private int findDayOffset() {
        return (mDayOfWeekStart < mWeekStart ? (mDayOfWeekStart + mNumDays) : mDayOfWeekStart) - mWeekStart;
    }

    /** 获取年月字符串 */
    public String getMonthAndYearString() {
        int flags = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_NO_MONTH_DAY;
        long millis = mCalendar.getTimeInMillis();
        return DateUtils.formatDateRange(getContext(), millis, millis, flags);
    }

    /** 判断是否同一天 */
    private boolean sameDay(int monthDay, Time time) {
        return (mYear == time.year) && (mMonth == time.month) && (monthDay == time.monthDay);
    }

    /** 判断是否今天以前 */
    private boolean prevDay(int monthDay, Time time) {
        return ((mYear < time.year)) || (mYear == time.year && mMonth < time.month) || (mMonth == time.month && monthDay < time.monthDay);
    }

    /** 画一个月的全部天数 */
    protected void drawMonthNums(Canvas canvas) {
        //竖直方向位置
        int y = (mRowHeight + MINI_DAY_NUMBER_TEXT_SIZE) / 2 - DAY_SEPARATOR_WIDTH + MONTH_HEADER_SIZE;
        //单元(天)的两边间距
        int paddingDay = (mWidth - 2 * mPadding) / (2 * mNumDays);
        //偏移天数
        int dayOffset = findDayOffset();
        int day = 1;//第一天开始

        while (day <= mNumCells) {
            int x = paddingDay * (1 + dayOffset * 2) + mPadding;
            /** 标记选中的 */
//            if (mMonth == mSelectedMonth && mSelectedDay == day && mSelectedYear == mYear) {
            String selectedKey = mYear+"-"+addZeroIfHasNot(mMonth+1)+"-"+addZeroIfHasNot(day);
            if(selectedMaps.containsKey(selectedKey)){
                Integer number = selectedMaps.get(selectedKey);
                if (mDrawRect) {//画圆角矩形
                    RectF rectF = new RectF(x - DAY_SELECTED_CIRCLE_SIZE, (y - MINI_DAY_NUMBER_TEXT_SIZE / 3) - DAY_SELECTED_CIRCLE_SIZE,
                            x + DAY_SELECTED_CIRCLE_SIZE, (y - MINI_DAY_NUMBER_TEXT_SIZE / 3) + DAY_SELECTED_CIRCLE_SIZE);
                    canvas.drawRoundRect(rectF, 10.0f, 10.0f, mSelectedCirclePaint);
                } else {//画圆
                    canvas.drawCircle(x, y - MINI_DAY_NUMBER_TEXT_SIZE / 3, DAY_SELECTED_CIRCLE_SIZE, mSelectedCirclePaint);
                    //画红点
                    canvas.drawCircle(x + (float)(Math.sin(Math.PI * 60 / 180) * DAY_SELECTED_CIRCLE_SIZE),
                            y - MINI_DAY_NUMBER_TEXT_SIZE / 3 - (float)(Math.sin(Math.PI * 30 / 180) * DAY_SELECTED_CIRCLE_SIZE)- MINI_DAY_RED_NUMBER_TEXT_SIZE / 3,
                            DAY_SELECTED_RED_POINT_CIRCLE_SIZE,mSelectedRedCirclePaint);
                    //设置红点里面的数字大小和颜色
                    mMonthNumPaint.setTextSize(MINI_DAY_RED_NUMBER_TEXT_SIZE);
                    mMonthNumPaint.setColor(mMonthTitleBGColor);
                    canvas.drawText(String.format("%d", number), x + (float) (Math.sin(Math.PI * 60 / 180) * DAY_SELECTED_CIRCLE_SIZE),
                            y - MINI_DAY_NUMBER_TEXT_SIZE / 3 - (float) (Math.sin(Math.PI * 30 / 180) * DAY_SELECTED_CIRCLE_SIZE), mMonthNumPaint);
                }
            } else {
                if (mHasToday && (mToday == day)) {//标记今天日期
                    canvas.drawCircle(x, y - MINI_DAY_NUMBER_TEXT_SIZE / 3, DAY_SELECTED_CIRCLE_SIZE, mCurrentCirclePaint);
                }
            }
            //重新设置字体大小
            mMonthNumPaint.setTextSize(MINI_DAY_NUMBER_TEXT_SIZE);
            //周末字体颜色
            if (dayOffset == 0 || dayOffset == mNumDays - 1) {
                mMonthNumPaint.setColor(mWeekendsColor);
            } else {
                mMonthNumPaint.setColor(mDayNumColor);
            }
            //选中字体颜色
//            if (mMonth == mSelectedMonth && mSelectedDay == day && mSelectedYear == mYear)
            if(selectedMaps.containsKey(selectedKey))
                mMonthNumPaint.setColor(mMonthTitleBGColor);
            //今天以前的样式和颜色
            if (!isPrevDayEnabled && prevDay(day, today) && today.month == mMonth && today.year == mYear) {
                mMonthNumPaint.setColor(mPreviousDayColor);
                mMonthNumPaint.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
            }
            canvas.drawText(String.format("%d", day), x, y, mMonthNumPaint);
            dayOffset++;//往右移一位
            if (dayOffset == mNumDays) {//到末尾偏移量重置,并行高增加
                dayOffset = 0;
                y += mRowHeight;
            }
            day++;
        }
    }

    /**
     * 获取指定位置的日历对象
     * @param x  x轴位置
     * @param y  y轴位置
     * @return  自定义日历对象
     */
    public CalendarDayBean getDayFromLocation(float x, float y) {
        int padding = mPadding;
        if ((x < padding) || (x > mWidth - mPadding)) {
            return null;
        }
        int yDay = (int) (y - MONTH_HEADER_SIZE) / mRowHeight;
        int day = 1 + ((int) ((x - padding) * mNumDays / (mWidth - padding - mPadding)) - findDayOffset()) + yDay * mNumDays;
        if (mMonth > 11 || mMonth < 0 || CalendarUtils.getDaysInMonth(mMonth, mYear) < day || day < 1)
            return null;
        return new CalendarDayBean(mYear, mMonth, day);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawMonthNums(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),
                mRowHeight * mNumRows + MONTH_HEADER_SIZE + PADDING_BOTTOM);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
    }

    /**触摸事件*/
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            CalendarDayBean calendarDay = getDayFromLocation(event.getX(), event.getY());
            if (calendarDay != null) {
                onDayClick(calendarDay);
            }
        }
        return true;
    }

    /** 选择某一天 */
    private void onDayClick(CalendarDayBean calendarDay) {
        //今天以前是否可被点击
        boolean pre = isPrevDayEnabled || !((calendarDay.month == today.month) &&
                (calendarDay.year == today.year) && calendarDay.day < today.monthDay);
        if (mOnDayClickListener != null && pre) {
            LogUtil.e("DynamicMonthView-->onDayClick",calendarDay.year+":"+(calendarDay.month+1)+":"+calendarDay.day);
            mOnDayClickListener.onDayClick(this, calendarDay);
        }
    }

    /**长度为1，前面就添加0*/
    private String addZeroIfHasNot(Integer number) {
        if (number < 10) {
            return "0" + number;
        }
        return "" + number;
    }

    /** 选中某一天的事件 */
    public void setOnDayClickListener(OnDayClickListener onDayClickListener) {
        mOnDayClickListener = onDayClickListener;
    }

    /** 选择某一天的接口 */
    public interface OnDayClickListener {
        void onDayClick(DynamicMonthView dynamicMonthView, CalendarDayBean calendarDay);
    }

    /** viewpager设置数据 */
    public void setSelectedMaps(Map<String,Integer> maps){
        selectedMaps.clear();
        selectedMaps.putAll(maps);
        invalidate();
    }
}
