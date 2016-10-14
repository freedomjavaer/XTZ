package com.ypwl.xiaotouzi.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * function : 图片浏览处理了异常
 * <p/>
 * Created by lzj on 2016/1/23.
 */
public class ImageBrowserViewPager extends ViewPager {
    public ImageBrowserViewPager(Context context) {
        super(context);
    }

    public ImageBrowserViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }
}
