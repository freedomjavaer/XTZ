package com.ypwl.xiaotouzi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.callback.IFooterCallBack;
import com.andview.refreshview.callback.IHeaderCallBack;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.view.dialog.GifView;

/**
 * 项目名:	XRefreshView-master
 * 包名:	com.andview.example.ui
 * 类名:	TestHeaderView
 * 作者:	罗霄
 * 创建时间:	2016/5/24 16:08
 * <p/>
 * 描述:	TODO
 * <p/>
 * svn版本:	$Revision: 15097 $
 * 更新人:	$Author: luoxiao $
 * 更新时间:	$Date: 2016-05-25 17:14:08 +0800 (周三, 25 五月 2016) $
 * 更新描述:	$Message$
 */
public class CustomXRefreshFootView extends LinearLayout implements IFooterCallBack {

    public CustomXRefreshFootView(Context context) {
        super(context);

        initView();
    }

    public CustomXRefreshFootView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CustomXRefreshFootView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View headerView = View.inflate(getContext(), R.layout.layout_head_new, this);
        GifView  mGifView = (GifView) headerView.findViewById(R.id.layout_head_new_gif);
        mGifView.setMovieResource(R.mipmap.loading_4);
    }


    @Override
    public void callWhenNotAutoLoadMore(XRefreshView.XRefreshViewListener xRefreshViewListener) {

    }

    @Override
    public void onStateReady() {
    }

    @Override
    public void onStateRefreshing() {
    }

    @Override
    public void onStateFinish(boolean hidefooter) {

    }

    @Override
    public void onStateComplete() {

    }

    @Override
    public void show(final boolean show) {
    }

    @Override
    public boolean isShowing() {
        return false;
    }

    @Override
    public int getFooterHeight() {
        return getMeasuredHeight();
    }
}
