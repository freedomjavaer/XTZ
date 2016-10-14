package com.ypwl.xiaotouzi.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.adapter.NetCreditNewsItemDetailAdapter;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.bean.NetCreditNewsDetailBean;
import com.ypwl.xiaotouzi.bean.NewsReplyBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.interf.INetRequestListener;
import com.ypwl.xiaotouzi.netprotocol.NetCreditNewsDetailProtocol;
import com.ypwl.xiaotouzi.netprotocol.NewsReplyProtocol;
import com.ypwl.xiaotouzi.utils.DateTimeUtil;
import com.ypwl.xiaotouzi.utils.NetworkUtils;
import com.ypwl.xiaotouzi.utils.StringUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.Util;
import com.ypwl.xiaotouzi.utils.ViewUtil;
import com.ypwl.xiaotouzi.view.LoadMoreScrollView;
import com.ypwl.xiaotouzi.view.MeasuredListView;
import com.ypwl.xiaotouzi.view.dialog.lib.KProgressHUD;

import java.util.List;

/**
 * function :网贷新闻详情
 * <p/>
 * Created by tengtao on 2016/3/15.
 */
public class NetCreditNewsDetailActivity extends BaseActivity implements View.OnClickListener, NetCreditNewsItemDetailAdapter.INewsReplyListener {

    private int mCurrPage = 1;
    private TextView mTvTitle,mTvReplyTarget,mBtnSubmit,mTvBack,mTvNewsTitle,mTvNewsAuthor,mTvNewsAddTime;
    private NetCreditNewsDetailProtocol mNewsDetailProtocol;
    private String news_id;
    private String toid;
    private boolean isFirstIn = true;
    private LoadMoreScrollView mScrollView;
    private WebView mWebView;
    private MeasuredListView mListView;
    private NetCreditNewsItemDetailAdapter mNewsDetailAdapter;
    private EditText mEditText;
    private KProgressHUD mDialogLoading;
    private boolean isLoadingMore = false;
    //空数据试图页面
    private View mNoDataView;
    public static final int REPLY_NEWS = 1;
    public static final int REPLY_COMMENT = 2;
    private int replyType = REPLY_NEWS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        news_id = getIntent().getStringExtra(Const.KEY_INTENT_JUMP_BASE_DATA);
        if (StringUtil.isEmptyOrNull(news_id)) {
            UIUtil.showToastShort("相关新闻不存在");
            finish();
            return;
        }
        setContentView(R.layout.activity_netcredit_news_detail);
        mDialogLoading = KProgressHUD.create(mActivity);
        initView();
        initData();
    }

    private void initView() {
        mNoDataView = findViewById(R.id.layout_no_data_view);
        mScrollView = (LoadMoreScrollView) findViewById(R.id.scrollView_net_credit_news);

        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvBack = (TextView) findViewById(R.id.tv_title_back);
        mTvBack.setText("晓投资");
        mEditText = (EditText) findViewById(R.id.edittext_reply_content);
        mTvReplyTarget = (TextView) findViewById(R.id.tv_reply_target);
        mBtnSubmit = (TextView) findViewById(R.id.tv_reply);
        mListView = (MeasuredListView) findViewById(R.id.listview_news_detail_reply);
        mTvNewsTitle = (TextView) findViewById(R.id.tv_netcredit_news_title);
        mTvNewsAuthor = (TextView) findViewById(R.id.tv_netcredit_news_author);
        mTvNewsAddTime = (TextView) findViewById(R.id.tv_netcredit_news_addtime);

        mWebView = (WebView) findViewById(R.id.webview_net_credit_news);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);//禁用缓存
        settings.setAllowFileAccess(true);//设置可以访问文件
        mWebView.setBackgroundColor(Color.TRANSPARENT);

        findViewById(R.id.layout_title_back).setOnClickListener(this);
        mBtnSubmit.setOnClickListener(this);
        mScrollView.setOnScrollChangedListener(new OnSimpleScrollChangedListener());
        ViewUtil.showContentLayout(Const.LAYOUT_LOADING, mNoDataView, mScrollView);
    }
    private void initData() {
        mTvTitle.setText("网贷新闻");
        mNewsDetailAdapter = new NetCreditNewsItemDetailAdapter(mActivity);
        mListView.setAdapter(mNewsDetailAdapter);
        mNewsDetailAdapter.setINewsReplyListener(this);

        mNewsDetailProtocol = new NetCreditNewsDetailProtocol();
        mNewsDetailProtocol.loadDataByPage(news_id, mCurrPage, mNetRequestListener);
    }

    private INetRequestListener mNetRequestListener = new INetRequestListener<NetCreditNewsDetailBean>() {
        @Override
        public void netRequestCompleted() {
            if (null != mDialogLoading){
                mDialogLoading.dismiss();
            }
            isLoadingMore = false;
            if(!NetworkUtils.isNetworkConnected(UIUtil.getContext())){
                ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mNoDataView, mScrollView, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mNewsDetailProtocol.loadDataByPage(news_id, mCurrPage, mNetRequestListener);
                    }
                });
            }
        }

        @Override
        public void netRequestSuccess(NetCreditNewsDetailBean bean,boolean isSuccess) {
            if (bean != null && isSuccess) {
                if(mCurrPage==1 && isFirstIn){
                    initNewsContent(bean);
                }
                refreshReply(bean.getList());
                ViewUtil.showContentLayout(Const.LAYOUT_DATA, mNoDataView, mScrollView);
            }else{
                ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mNoDataView, mScrollView, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mNewsDetailProtocol.loadDataByPage(news_id, mCurrPage, mNetRequestListener);
                    }
                });
            }
        }
    };

    /** 加载webveiw内容 */
    @SuppressLint("SetJavaScriptEnabled")
    private void initNewsContent(NetCreditNewsDetailBean newsBean){
        List<NetCreditNewsDetailBean.ContentBean> contentBean = newsBean.getContent();
        if(contentBean!=null && contentBean.size()>0){
            NetCreditNewsDetailBean.ContentBean cBean = contentBean.get(0);
            mTvNewsTitle.setText(cBean.getTitle());
            mTvNewsAuthor.setText(cBean.getSource());
            long addTime=0;
            try{
                addTime = Long.parseLong(cBean.getAddtime())*1000;
            }catch (Exception e){e.printStackTrace();}
            mTvNewsAddTime.setText(DateTimeUtil.formatDateTime(addTime,DateTimeUtil.DF_YYYYMMDD));
            String content = cBean.getContent();
            String header = "<style>img{max-width:100%;height:auto;}</style>";
            mWebView.loadData(header+content, "text/html; charset=UTF-8", null);
        }
    }

    /** 更新回复 */
    private void refreshReply(List<NetCreditNewsDetailBean.ListBean> list) {
        if (mCurrPage == 1) {//第一次加载
            mListView.setVisibility(list.size()==0?View.GONE:View.VISIBLE);
            mNewsDetailAdapter.loadData(list, false);
            if (mNewsDetailAdapter.getCount() > 0 && !isFirstIn) {
                mListView.smoothScrollToPosition(0);
            }
        } else {//加载更多
            if (list.size() > 0) {
                mNewsDetailAdapter.loadData(list, true);
            } else {
                mCurrPage--;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_title_back://返回
                finish();
                break;
            case R.id.tv_reply://评论
                isFirstIn = false;
                if (Util.legalLogin() == null) {
                    startActivity(LoginActivity.class);
                    return;
                }
                sendComment();
                break;
        }
    }

    /** 滚动监听事件，加载更多 */
    public class OnSimpleScrollChangedListener implements LoadMoreScrollView.OnScrollChangedListener {

        @Override
        public void onScrollChanged(int l, int t, int oldl, int oldt) {
        }

        @Override
        public void onScrollTop() {
        }

        @Override
        public void onScrollBottom() {
            if(!isLoadingMore){
                isLoadingMore = true;
                ++mCurrPage;
//                mDialogLoading.show();
                mNewsDetailProtocol.loadDataByPage(news_id, mCurrPage, mNetRequestListener);
            }
        }
    }

    /** 提交评论 */
    private void sendComment() {
        String content = mEditText.getText().toString();
        if (StringUtil.isEmptyOrNull(content)) {
            mEditText.startAnimation(AnimationUtils.loadAnimation(UIUtil.getContext(), R.anim.shake));
            return;
        }
        mBtnSubmit.setEnabled(false);
        NewsReplyProtocol newsReplyProtocol = new NewsReplyProtocol(news_id,content,toid);
        mDialogLoading.show();
        newsReplyProtocol.loadData(replyType, new INetRequestListener<NewsReplyBean>() {
            @Override
            public void netRequestCompleted() {
                if (null != mDialogLoading){
                    mDialogLoading.dismiss();
                }
                mBtnSubmit.setEnabled(true);
            }

            @Override
            public void netRequestSuccess(NewsReplyBean bean, boolean isSuccess) {
                if (isSuccess && bean != null) {
                    refreshDataAfterComment();
                    replyType = REPLY_NEWS;
                    mTvReplyTarget.setVisibility(View.GONE);
                    mTvReplyTarget.setText("");
                } else {
                    UIUtil.showToastShort("评论失败");
                }
            }
        });
    }

    /** 提交评论后刷新视图 */
    private void refreshDataAfterComment() {
        mEditText.setText("");
        UIUtil.postDelayed(new Runnable() {
            @Override
            public void run() {
                mCurrPage = 1;
                mNewsDetailProtocol.loadDataByPage(news_id, mCurrPage, mNetRequestListener);
            }
        }, 500);
    }


    @Override
    protected void onDestroy() {
        mDialogLoading = null;
        mNewsDetailProtocol = null;
        super.onDestroy();
    }

    /**回复某一条评论*/
    @Override
    public void newsTargetReply(String nickName,String userId) {
        this.toid = userId;
        replyType = REPLY_COMMENT;
        mTvReplyTarget.setText("@"+nickName+":");
        mTvReplyTarget.setVisibility(View.VISIBLE);
    }
}
