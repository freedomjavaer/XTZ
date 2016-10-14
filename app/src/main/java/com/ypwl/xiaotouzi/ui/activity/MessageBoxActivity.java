package com.ypwl.xiaotouzi.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.adapter.MessageBoxAdapter;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.bean.MessageBoxBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.interf.UIAdapterListener;
import com.ypwl.xiaotouzi.manager.SilenceRequestHelper;
import com.ypwl.xiaotouzi.manager.db.MessageBoxDbOpenHelper;
import com.ypwl.xiaotouzi.utils.ViewUtil;
import com.ypwl.xiaotouzi.view.LoadMoreListView;

/**
 * 收件箱
 * <p/>
 * Created by PDK on 2016/3/18.
 * <p/>
 * Modify by lzj on 2016/4/5.
 */
public class MessageBoxActivity extends BaseActivity implements View.OnClickListener, LoadMoreListView.IListViewRefreshListener, AdapterView.OnItemClickListener {

    private LoadMoreListView mListView;
    private View mLayoutNoDataView;
    private LinearLayout mLayoutContent;
    private MessageBoxAdapter mAdapter;
    private TextView mTvBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_box);
        initView();
    }

    private void initView() {
        //title
        TextView mTitle = (TextView) findViewById(R.id.tv_title);
        mTitle.setText(R.string.message_box_title);
        findViewById(R.id.layout_title_back).setOnClickListener(this);
        mTvBack = (TextView) findViewById(R.id.tv_title_back);
        mTvBack.setText("晓投资");

        mLayoutNoDataView = findViewById(R.id.layout_no_data_view);
        mLayoutContent = (LinearLayout) findViewById(R.id.layout_content);

        mListView = (LoadMoreListView) findViewById(R.id.message_box_listview);
        mListView.setOnRefreshListener(this);
        mListView.setLoadMoreEnable(true);
        mListView.setOnItemClickListener(this);

        ViewUtil.showContentLayout(Const.LAYOUT_LOADING, mLayoutNoDataView, mLayoutContent);
        mAdapter = new MessageBoxAdapter(new MyUIAdapterListener());
        mListView.setAdapter(mAdapter);
        mAdapter.asyncRequestData();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MessageBoxBean.ListEntity item = mAdapter.getItem(position - 1);//LoadMoreListview增加了头部item,对应位置需要减1
        if (item == null) {
            return;
        }
        startActivity(MessageDetailActivity.class, item.getContent());

        //区分是否为系统消息，是则插入到本地数据库, 否则标记已读通知服务端
        if (item.getStatus() == 0) {//仅仅操作未读的
            if (item.getType() == 0) {//系统推送
                MessageBoxDbOpenHelper.MessageBoxDbHelper.getInstance().insert(item.getStand_id());
            } else {
                SilenceRequestHelper.getInstance().notifyServerMsgHasRead(item.getStand_id(),item.getType());
            }
        }
        // 本地标为已读
        item.setStatus(1);
        mAdapter.notifyDataSetChanged();

    }

    /** 适配器与UI通信接口 */
    public class MyUIAdapterListener implements UIAdapterListener {

        @Override
        public void isLoading() {

        }

        @Override
        public void dataCountChanged(int count) {
            if (count == 0) {
                ViewUtil.showContentLayout(Const.LAYOUT_EMPTY, mLayoutNoDataView, mLayoutContent);
            } else if (count == Const.LOADED_ERROR) {//加载出错
                ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mLayoutNoDataView, mLayoutContent, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAdapter.asyncRequestData();
                    }
                });
            } else if (count == Const.LOADED_NO_MORE) {
                mListView.stopLoadMore();
                mListView.setDefaultText(getString(R.string.loadmore_status_nomore));
            }
        }

        @Override
        public void loadFinished(int count) {
            mListView.stopLoadMore();
            ViewUtil.showContentLayout(count > 0 ? Const.LAYOUT_DATA : Const.LAYOUT_EMPTY, mLayoutNoDataView, mLayoutContent);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_title_back:
                finish();
                break;
        }
    }

    @Override
    public void onRefreshLoadMore() {
        mAdapter.loadMore();
    }

}
