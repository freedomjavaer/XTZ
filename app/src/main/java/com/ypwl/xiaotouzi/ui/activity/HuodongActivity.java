package com.ypwl.xiaotouzi.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.adapter.HuodongAdapter;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.interf.UIAdapterListener;
import com.ypwl.xiaotouzi.utils.LogUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.ViewUtil;
import com.ypwl.xiaotouzi.view.CustomXRefreshHeaderView;

/**
 * function : 优惠活动页面
 * <p/>
 * Modify by lzj on 2015/11/8.
 */
public class HuodongActivity extends BaseActivity implements View.OnClickListener{
    private View mLayoutNoDataView;
//    private SwipeRefreshLayout mLayoutContent;
    private XRefreshView mLayoutContent;
    private HuodongAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huodong);
//        GlobalUtils.hasRedPoint = false;

        initView();
    }

    private void initView() {
        findViewById(R.id.layout_title_back).setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText("优惠活动");

        ((TextView)findView(R.id.tv_title_back)).setText(UIUtil.getString(R.string.main_tab_text_myinfo));

        mLayoutNoDataView = findViewById(R.id.layout_no_data_view);
//        mLayoutContent = (SwipeRefreshLayout) findViewById(R.id.layout_content);
//        mLayoutContent.setOnRefreshListener(this);
        mLayoutContent = findView(R.id.layout_content);
        mLayoutContent.setMoveForHorizontal(true);
        mLayoutContent.setCustomHeaderView(new CustomXRefreshHeaderView(this));
        mLayoutContent.setPullLoadEnable(false);
        mLayoutContent.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh() {
                refreshContentData();
            }
        });

        ListView listViewHuodong = (ListView) findViewById(R.id.huodong_listview);
        mAdapter = new HuodongAdapter(mActivity, new HuodongUIAdapterListener());
        listViewHuodong.setAdapter(mAdapter);
        ViewUtil.showContentLayout(Const.LAYOUT_LOADING, mLayoutNoDataView, mLayoutContent);//默认显示加载中视图
        mAdapter.asyncLoadData();
    }

    /** UI刷新回调 与adapter进行沟通 */
    private class HuodongUIAdapterListener implements UIAdapterListener {
        @Override
        public void isLoading() {
        }

        @Override
        public void dataCountChanged(int count) {
            LogUtil.e(TAG, " dataCountChanged : count =  " + count);
            mLayoutContent.stopRefresh();//关闭刷新进度圈
            if (-1 == count) {//错误反馈
                ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mLayoutNoDataView, mLayoutContent, "出错啦!点击重新加载", mOnclickListener);
                return;
            }
            if (count == 0) {//没有数据
                ViewUtil.showContentLayout(Const.LAYOUT_EMPTY, mLayoutNoDataView, mLayoutContent, "暂时没有数据，点击重新加载", mOnclickListener);
            } else {//显示数据
                ViewUtil.showContentLayout(Const.LAYOUT_DATA, mLayoutNoDataView, mLayoutContent);
            }
        }

        @Override
        public void loadFinished(int count) {
            //do nothing now
        }
    }


    /** 重新加载网络数据事件 */
    private View.OnClickListener mOnclickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            refreshContentData();
        }
    };

//    @Override
//    public void onRefresh() {
//        mLayoutContent.setRefreshing(true);
//        refreshContentData();
//    }

    /** 重新加载数据 */
    private void refreshContentData() {
        if (mAdapter == null) {
            mAdapter = new HuodongAdapter(mActivity, new HuodongUIAdapterListener());
        }
        mAdapter.asyncLoadData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_title_back://返回
                finish();
                break;
        }
    }
}
