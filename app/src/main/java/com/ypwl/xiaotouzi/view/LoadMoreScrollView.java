package com.ypwl.xiaotouzi.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * function : 滚动加载更多scrollview
 * <p>
 * Created by tengtao on 2016/4/5.
 */
public class LoadMoreScrollView extends ScrollView {

    private long millis;
    // 滚动监听者
    private OnScrollChangedListener onScrollChangedListener;

    public LoadMoreScrollView(Context context) {
        super(context);
    }

    public LoadMoreScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadMoreScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (null == onScrollChangedListener) {
            return;
        }
        long now = System.currentTimeMillis();
        // 通知监听者当前滚动的具体信息
        onScrollChangedListener.onScrollChanged(l, t, oldl, oldt);
        if (now - millis > 1000l) {
            // 滚动到底部（前提：从不是底部滚动到底部）
            if ((this.getHeight() + oldt) != this.getTotalVerticalScrollRange()
                    && (this.getHeight() + t) == this.getTotalVerticalScrollRange()) {
                onScrollChangedListener.onScrollBottom(); // 通知监听者滚动到底部
                millis = now;
            }
        }
        // 滚动到顶部（前提：从不是顶部滚动到顶部）
        if (oldt != 0 && t == 0) {
            onScrollChangedListener.onScrollTop(); // 通知监听者滚动到顶部
        }
    }

    public OnScrollChangedListener getOnScrollChangedListener() {
        return onScrollChangedListener;
    }

    public void setOnScrollChangedListener(OnScrollChangedListener onScrollChangedListener) {
        this.onScrollChangedListener = onScrollChangedListener;
    }

    /**
     * 获得滚动总长度
     *
     * @return
     */
    public int getTotalVerticalScrollRange() {
        return this.computeVerticalScrollRange();
    }

    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        return 0; // 禁止ScrollView在子控件的布局改变时自动滚动
    }

    public interface OnScrollChangedListener  {
        /**
         * 监听滚动变化
         *
         * @param l
         * @param t
         * @param oldl
         * @param oldt
         */
        void onScrollChanged(int l, int t, int oldl, int oldt);

        /**
         * 监听滚动到顶部
         */
        void onScrollTop();

        /**
         * 监听滚动到底部
         */
        void onScrollBottom();

    }
}
