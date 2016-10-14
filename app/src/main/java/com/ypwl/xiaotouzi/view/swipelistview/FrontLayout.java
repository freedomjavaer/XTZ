package com.ypwl.xiaotouzi.view.swipelistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * function : 前景层视图(getView数据展示层)
 * </p>
 * Created by lzj on 2015/11/23.
 */
public class FrontLayout extends RelativeLayout {

    private SwipeLayoutInterface mISwipeLayout;

    public FrontLayout(Context context) {
        super(context);
    }

    public FrontLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FrontLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setSwipeLayout(SwipeLayoutInterface mSwipeLayout) {
        this.mISwipeLayout = mSwipeLayout;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mISwipeLayout.getCurrentStatus() != SwipeLayout.Status.Close || super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mISwipeLayout.getCurrentStatus() == SwipeLayout.Status.Close) {
            return super.onTouchEvent(event);
        } else {
            if (event.getActionMasked() == MotionEvent.ACTION_UP) {
                mISwipeLayout.close();
            }
            return true;
        }
    }

}
