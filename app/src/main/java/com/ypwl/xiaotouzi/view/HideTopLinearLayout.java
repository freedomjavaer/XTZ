package com.ypwl.xiaotouzi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;

import java.util.NoSuchElementException;

/**
 * function : 缩放顶部的线性布局
 * <br/>
 * topViewId must be layout_top;
 * <br/>
 * Created by lzj on 2016/3/25.
 */
@SuppressWarnings("unused")
public class HideTopLinearLayout extends LinearLayout {
    private View mTopView;
    /** header的高度  单位：px */
    private int mOriginalTopViewHeight;
    private int mTopViewHeight;
    private int mTouchSlop;
    private boolean mInitDataSucceed = false;
    private OnGiveUpTouchEventListener mGiveUpTouchEventListener;
    private boolean mIsSticky = true;

    public HideTopLinearLayout(Context context) {
        super(context);
    }

    public HideTopLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus && (mTopView == null)) {
            initData();
        }
    }

    private void initData() {
        int topViewId = getResources().getIdentifier("layout_top", "id", getContext().getPackageName());
        if (topViewId != 0) {
            mTopView = findViewById(topViewId);
            mOriginalTopViewHeight = mTopView.getMeasuredHeight();
            mTopViewHeight = mOriginalTopViewHeight;
            mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
            mTouchSlop = 5;
            if (mTopViewHeight > 0) {
                mInitDataSucceed = true;
            }
        } else {
            throw new NoSuchElementException("Did your view with id \"layout_top\" exists?");
        }
    }

    // 分别记录上次滑动的坐标(onInterceptTouchEvent)
    private int mLastXIntercept = 0;
    private int mLastYIntercept = 0;
    // 分别记录上次滑动的坐标
    private int mLastY = 0;
    private boolean mDisallowInterceptTouchEventOnHeader = true;
    public static final int STATUS_EXPANDED = 1;
    public static final int STATUS_COLLAPSED = 2;
    private int mStatus = STATUS_EXPANDED;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        int intercepted = 0;
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastXIntercept = x;
                mLastYIntercept = y;
                mLastY = y;
                intercepted = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastXIntercept;
                int deltaY = y - mLastYIntercept;
                if (mDisallowInterceptTouchEventOnHeader && y <= mTopViewHeight) {
                    intercepted = 0;
                } else if (Math.abs(deltaY) <= Math.abs(deltaX)) {
                    intercepted = 0;
                } else if (mStatus == STATUS_EXPANDED && deltaY <= -mTouchSlop) {
                    intercepted = 1;
                } else if (mGiveUpTouchEventListener != null) {
                    if (mGiveUpTouchEventListener.giveUpTouchEvent(event) && deltaY >= mTouchSlop) {
                        intercepted = 1;
                    }
                }
                mLastYIntercept = y;
                mLastXIntercept = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
                intercepted = 0;
                mLastXIntercept = mLastYIntercept = 0;
                break;
        }
        return intercepted != 0 && mIsSticky;
    }

    private int deltayOntouchCopy;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mIsSticky) {
            return true;
        }
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaY = y - mLastY;
                deltayOntouchCopy = y - mLastY;
                if (deltaY > 30) {
                    deltaY = 20;
                }
                mTopViewHeight += deltaY;
                setTopViewHeight(mTopViewHeight);
                break;
            case MotionEvent.ACTION_UP:
                int destHeight;
                if (deltayOntouchCopy < 0) {//上滑松手 闭合顶部控件
                    destHeight = 0;
                    mStatus = STATUS_COLLAPSED;
                } else {//下滑松手，打开顶部控件
                    destHeight = mOriginalTopViewHeight;
                    mStatus = STATUS_EXPANDED;
                }
                // 慢慢滑向终点
                this.smoothSetTopViewHeight(mTopViewHeight, destHeight, 300);
                break;
        }
        mLastY = y;
        return true;
    }

    /** 平滑式设置顶部View的高度 */
    public void smoothSetTopViewHeight(int from, int to, long duration) {
        smoothSetTopViewHeight(from, to, duration, false);
    }

    /** 平滑设置header高度 */
    public void smoothSetTopViewHeight(final int from, final int to, long duration, final boolean modifyOriginalTopViewHeight) {
        final int frameCount = (int) (duration / 1000f * 30) + 1;
        final float partation = (to - from) / (float) frameCount;
        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < frameCount; i++) {
                    final int height;
                    if (i == frameCount - 1) {
                        height = to;
                    } else {
                        height = (int) (from + partation * i);
                    }
                    post(new Runnable() {
                        public void run() {
                            setTopViewHeight(height);
                        }
                    });
                    try {
                        sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (modifyOriginalTopViewHeight) {
                    setOriginalTopViewHeight(to);
                }
            }
        }.start();
    }

    /** 设置原始顶部View的高度 */
    public void setOriginalTopViewHeight(int originalTopViewHeight) {
        mOriginalTopViewHeight = originalTopViewHeight;
    }

    /** 设置顶部view高度 */
    public void setTopViewHeight(int height, boolean modifyOriginalHeaderHeight) {
        if (modifyOriginalHeaderHeight) {
            setOriginalTopViewHeight(height);
        }
        setTopViewHeight(height);
    }

    private void setTopViewHeight(int height) {
        if (!mInitDataSucceed) {
            initData();
        }
//        LogUtil.e("lzj", "setTopViewHeight height=" + height);

        if (height <= 0) {
            height = 0;
        } else if (height > mOriginalTopViewHeight) {
            height = mOriginalTopViewHeight;
        }
        if (height == 0) {
            mStatus = STATUS_COLLAPSED;
        } else {
            mStatus = STATUS_EXPANDED;
        }
        if (mTopView != null && mTopView.getLayoutParams() != null) {
            mTopView.getLayoutParams().height = height;
            mTopView.requestLayout();
            mTopViewHeight = height;
        }
    }


    /** 获取缩放控件的高度 */
    public int getTopViewHeight() {
        return mTopViewHeight;
    }

    /** 对外暴露触摸监听的接口 */
    public interface OnGiveUpTouchEventListener {
        boolean giveUpTouchEvent(MotionEvent event);
    }

    /** 设置是否粘性伸缩 */
    public void setSticky(boolean isSticky) {
        mIsSticky = isSticky;
    }

    /** 头部view的触摸事件的设置 */
    public void requestDisallowInterceptTouchEventOnHeader(boolean disallowIntercept) {
        mDisallowInterceptTouchEventOnHeader = disallowIntercept;
    }

    /** 设置顶部布局放弃触摸事件监听 */
    public void setOnGiveUpTouchEventListener(OnGiveUpTouchEventListener l) {
        mGiveUpTouchEventListener = l;
    }

}
