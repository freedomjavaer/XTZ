package com.ypwl.xiaotouzi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Scroller;

/**
 * 带加载更多的listview,条目数量不满一屏时加载更多功能不可用，超出一屏后起效.
 * </p>
 * Created by lzj on 2015/11/24.
 */
public class LoadMoreListView extends ListView implements OnScrollListener {
    /** 底部加载更多控件 */
    private LoadMoreListViewFooter mFooterView;
    /** 底部是否处于可刷新状态 */
    private boolean mIsFooterReady = false;
    /** 是否可以上拉加载更多 */
    private boolean mEnablePullLoad = true;
    /** 是否正在上拉加载中 */
    private boolean mPullLoading;
    /** 回滚器. used for scroll back */
    private Scroller mScroller;
    /** 回滚监听 */
    private OnScrollListener mScrollListener;
    /** 当前回滚的类型对象.header or footer. */
    private ScrollBack mScrollBack;
    /** 加载更多监听器 */
    private IListViewRefreshListener mListViewListener;
    /** 回滚时长 */
    private final static int SCROLL_DURATION = 400;
    /** 上拉放手开始加载更多的最少距离,默认至少50个像素 */
    private final static int PULL_LOAD_MORE_DELTA = 100;
    /** 偏移比例 */
    private final static float OFFSET_RADIO = 1.8f;
    /** list列表所有项数量，用于检测是否到达listview的底部 */
    private int mTotalItemCount;
    /** 触摸时Y值记录 */
    private float mLastY = -1;
    /** 装填头部布局的线性布局，可添加自定义头部控件 */
    private LinearLayout mHeaderLayout;

    /**
     * 回滚类型feature
     */
    private enum ScrollBack {
        FOOTER
    }

    public LoadMoreListView(Context context) {
        super(context);
        initWithContext(context);
    }

    public LoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWithContext(context);
    }

    public LoadMoreListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initWithContext(context);
    }

    private void initWithContext(Context context) {
        mScroller = new Scroller(context, new DecelerateInterpolator());
        super.setOnScrollListener(this);

        // header View
        mHeaderLayout = new LinearLayout(context);
        mHeaderLayout.setOrientation(LinearLayout.VERTICAL);
        mHeaderLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        addHeaderView(mHeaderLayout);

        // footer View
        mFooterView = new LoadMoreListViewFooter(context);
    }

    /**
     * 添加自定义头部控件
     */
    public void addCustomView(View customView) {
        if (null == customView) {
            return;
        }
        mHeaderLayout.addView(customView, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    /** 设置底部默认状态下的文字 */
    public void setDefaultText(String msg) {
        if (mFooterView != null) {
            mFooterView.setDefaultText(msg);
        }
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
    }

    /**
     * enable or disable load more feature.
     */
    public void setLoadMoreEnable(boolean enable) {
        mEnablePullLoad = enable;
        if (!mEnablePullLoad) {
            mFooterView.hideLoadMoreLayout();
            mFooterView.setOnClickListener(null);
        } else {
            mPullLoading = false;
            mFooterView.showLoadMoreLayout();
            mFooterView.setState(LoadMoreListViewFooter.STATE.DEFAULT);
            // both "pull up" and "click" will invoke load more.
            mFooterView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFooterView.setEnabled(false);
                    startLoadMore();
                }
            });
        }
    }

    /**
     * start loadmore.
     */
    private void startLoadMore() {
        if (!mIsFooterReady) {
            return;
        }
        mPullLoading = true;
        mFooterView.setState(LoadMoreListViewFooter.STATE.LOADING);
        if (mListViewListener != null) {
            mListViewListener.onRefreshLoadMore();
        }
    }

    /**
     * stop load more, reset footer view. invoke after load more finish
     */
    public void stopLoadMore() {
        if (!mIsFooterReady) {
            return;
        }
        if (mPullLoading) {
            mPullLoading = false;
            mFooterView.setState(LoadMoreListViewFooter.STATE.DEFAULT);
        }
        mFooterView.setEnabled(true);
    }

    public boolean isLoading() {
        return mFooterView.isLoading();
    }

    /**
     * 更新底部高度
     */
    private void updateFooterHeight(float delta) {
        int height = mFooterView.getBottomMargin() + (int) delta;
        if (mEnablePullLoad && !mPullLoading) {
            if (height > PULL_LOAD_MORE_DELTA) {
                mFooterView.setState(LoadMoreListViewFooter.STATE.READY);
            } else {
                mFooterView.setState(LoadMoreListViewFooter.STATE.DEFAULT);
            }
        }
        mFooterView.setBottomMargin(height);
    }

    /**
     * 重置底部高度
     */
    private void resetFooterHeight() {
        int bottomMargin = mFooterView.getBottomMargin();
        if (bottomMargin > 0) {
            mScrollBack = ScrollBack.FOOTER;
            mScroller.startScroll(0, bottomMargin, 0, -bottomMargin, SCROLL_DURATION);
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!mIsFooterReady) {
            return super.onTouchEvent(ev);
        }
        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();
                if (getLastVisiblePosition() == mTotalItemCount - 1 && (mFooterView.getBottomMargin() > 0 || deltaY < 0)) {// 上拉加载更多
                    // last item, already pulled up or want to pull up.
                    updateFooterHeight(-deltaY / OFFSET_RADIO);
                }
                break;
            case MotionEvent.ACTION_UP:
                mLastY = -1;
                if (getLastVisiblePosition() == mTotalItemCount - 1) {
                    if (mEnablePullLoad && mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA) {
                        startLoadMore();
                    }
                    resetFooterHeight();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            if (mScrollBack == ScrollBack.FOOTER) {
                mFooterView.setBottomMargin(mScroller.getCurrY());
            }
            postInvalidate();
            invokeOnScrolling();
        }
        super.computeScroll();
    }

    /**
     * 执行滚动监听接口
     */
    private void invokeOnScrolling() {
        if (mScrollListener instanceof OnXScrollListener) {
            OnXScrollListener l = (OnXScrollListener) mScrollListener;
            l.onXScrolling(this);
        }
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        mScrollListener = l;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mScrollListener != null) {
            mScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // send to user's listener
        mTotalItemCount = totalItemCount;
        if (mScrollListener != null) {
            mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
        if (totalItemCount > visibleItemCount) {//有第二页
            // make sure mFooterView is the last footer view, and only add once.
            if (!mIsFooterReady) {
                mIsFooterReady = true;
                addFooterView(mFooterView);
            }
        } else {
            mIsFooterReady = false;
            if (mFooterView == null) {
                mFooterView = new LoadMoreListViewFooter(getContext());
            }
            removeFooterView(mFooterView);
        }
    }

    /**
     * 设置刷新监听
     */
    public void setOnRefreshListener(IListViewRefreshListener l) {
        setLoadMoreEnable(true);//默认点击可以进行加载更多
        mListViewListener = l;
    }

    /**
     * you can listen ListView.OnScrollListener or this one. it will invoke onXScrolling when footer scroll back.
     */
    public interface OnXScrollListener extends OnScrollListener {
        void onXScrolling(View view);
    }

    /**
     * 加载更多监听接口
     */
    public interface IListViewRefreshListener {
        void onRefreshLoadMore();
    }

}
