package com.ypwl.xiaotouzi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.andview.refreshview.callback.IHeaderCallBack;
import com.ypwl.xiaotouzi.R;
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
 * svn版本:	$Revision: 15395 $
 * 更新人:	$Author: pengdakai $
 * 更新时间:	$Date: 2016-06-15 18:04:01 +0800 (周三, 15 六月 2016) $
 * 更新描述:	$Message$
 */
public class CustomXRefreshHeaderView extends LinearLayout implements IHeaderCallBack {
    public CustomXRefreshHeaderView(Context context) {
        super(context);

        initView();
    }

    public CustomXRefreshHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CustomXRefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View headerView = View.inflate(getContext(), R.layout.layout_head_new, this);

        GifView mGifView = (GifView) headerView.findViewById(R.id.layout_head_new_gif);
        mGifView.setMovieResource(R.mipmap.loading_4);

    }

    /**
     * hide footer when disable pull load more
     */
    public void hide() {
        setVisibility(View.GONE);
    }

    public void show() {
        setVisibility(View.VISIBLE);
    }

    @Override
    public void onStateNormal() {

    }

    @Override
    public void onStateReady() {

    }

    @Override
    public void onStateRefreshing() {

    }

    @Override
    public void onStateFinish() {

    }

    @Override
    public void onHeaderMove(double offset, int offsetY, int deltaY) {

    }

    @Override
    public void setRefreshTime(long lastRefreshTime) {

    }

    @Override
    public int getHeaderHeight() {
        return getMeasuredHeight();
    }
}
