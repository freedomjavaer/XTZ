package com.ypwl.xiaotouzi.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;


/**
 * ListView底部加载更多.
 * </p>
 * Created by lzj on 2015/11/24.
 */
public class LoadMoreListViewFooter extends LinearLayout {
    private Context mContext;
    private View mContentView;
    private View mProgressBar;
    private TextView mTextViewHint;
    private TextView mProgressLayoutText;
    private LinearLayout mProgressLayout;
    private boolean mIsLoading = false;

    /**
     * 刷新状态 : <code>DEFAULT</code>,<code>READY</code>,<code>LOADING</code>
     */
    public enum STATE {
        /** 默认状态 */
        DEFAULT,
        /** 释放加载状态 */
        READY,
        /** 加载中状态 */
        LOADING
    }

    public LoadMoreListViewFooter(Context context) {
        super(context);
        initView(context);
    }

    public LoadMoreListViewFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    @SuppressLint("InflateParams")
    private void initView(Context context) {
        mContext = context;
        LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.loadmore_listview_footer, null);
        addView(moreView);
        moreView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        mContentView = moreView.findViewById(R.id.listview_refresh_footer_content);
        mProgressLayout = (LinearLayout) moreView.findViewById(R.id.listview_refresh_footer_content_progresslayout);
        mProgressBar = moreView.findViewById(R.id.listview_refresh_footer_content_progresslayout_progressbar);
        mProgressLayoutText = (TextView) moreView.findViewById(R.id.listview_refresh_footer_content_progresslayout_text);
        mTextViewHint = (TextView) moreView.findViewById(R.id.listview_refresh_footer_content_hint_text);
    }

    /**
     * 设置当前底部加载更多的布局显示状态
     */
    public void setState(STATE state) {
        mIsLoading = false;
        mProgressLayout.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        mProgressLayoutText.setVisibility(View.INVISIBLE);
        mTextViewHint.setVisibility(View.INVISIBLE);
        if (state == STATE.READY) {// 松开刷新
            mTextViewHint.setVisibility(View.VISIBLE);
            mTextViewHint.setText("松开载入更多");
        } else if (state == STATE.LOADING) {// 加载中
            mIsLoading = true;
            mProgressLayout.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressLayoutText.setVisibility(View.VISIBLE);
        } else {// 默认
            mTextViewHint.setVisibility(View.VISIBLE);
            mTextViewHint.setText("上拉加载更多");
        }
    }

    /** 是否处于加载更多中 */
    public boolean isLoading() {
        return mIsLoading;
    }

    /**
     * 设置底部布局的bottomMargin
     */
    public void setBottomMargin(int height) {
        if (height < 0)
            return;
        LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
        lp.bottomMargin = height;
        mContentView.setLayoutParams(lp);
    }

    /**
     * 获取底部布局的bottomMargin
     */
    public int getBottomMargin() {
        LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
        return lp.bottomMargin;
    }

    /**
     * default status
     */
    public void defaultState() {
        mTextViewHint.setVisibility(View.VISIBLE);
        mProgressLayout.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mProgressLayoutText.setVisibility(View.GONE);
    }

    /**
     * loading status
     */
    public void loadingState() {
        mTextViewHint.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressLayout.setVisibility(View.VISIBLE);
        mProgressLayoutText.setVisibility(View.VISIBLE);
    }

    /**
     * hide footer when disable pull load more
     */
    public void hideLoadMoreLayout() {
        LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
        lp.height = 0;
        mContentView.setLayoutParams(lp);
    }

    /**
     * show footer when enable pull load more
     */
    public void showLoadMoreLayout() {
        LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
        lp.height = LayoutParams.WRAP_CONTENT;
        mContentView.setLayoutParams(lp);
    }

    /** 设置底部默认状态下的文字 */
    public void setDefaultText(String str) {
        mTextViewHint.setText(str);
    }

}
