package com.ypwl.xiaotouzi.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.utils.LogUtil;
import com.ypwl.xiaotouzi.utils.StringUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;

/**
 * function : 通用Web页面展示界面.
 * <br/>
 * Created by lzj on 2016/3/28.
 */
public class CommonWebPageActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTitleUI;
    private ProgressBar mLoadingProgressBar;
    private WebView mWebView;
    private String mUrl;
    private String mFromWhere,mTitle;
    private TextView mTvTitleLeft;
    private boolean mIsShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_webpage);
        Intent intent = getIntent();
        mUrl = intent.getStringExtra(Const.KEY_INTENT_JUMP_BASE_DATA);
        mFromWhere = intent.getStringExtra(Const.KEY_INTENT_JUMP_FROM_DATA);
        mTitle = intent.getStringExtra(Const.KEY_INTENT_JUMP_BASE_DATA_1);

        //mIsShow = getIntent().getBooleanExtra(Const.KEY_INTENT_JUMP_BASE_DATA + "ISSHOW", false);

        if (StringUtil.isEmptyOrNull(mUrl)) {
            UIUtil.showToastShort("地址链接错误!!!");
            finish();
            return;
        }
        initView();
    }

    private void initView() {
        mTvTitleLeft = findView(R.id.tv_title_txt_left);
        mTvTitleLeft.setText("关闭");
        mTvTitleLeft.setOnClickListener(this);
        showCloseView(mIsShow);
        //title
        findView(R.id.layout_title_back).setOnClickListener(this);
        ((TextView) findView(R.id.tv_title_back)).setText(mFromWhere);
        mTitleUI = findView(R.id.tv_title);
        mTitleUI.setText(mTitle);
        //content
        mLoadingProgressBar = (ProgressBar) findViewById(R.id.pb_loading);
        mWebView = (WebView) findViewById(R.id.wv);

        //初始化WebView并加载数据
        initWebViewAndLoadData();
    }

    private void showCloseView(boolean isShow) {
        mTvTitleLeft.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    /** 初始化WebView并加载数据 */
    @SuppressLint("SetJavaScriptEnabled")
    private void initWebViewAndLoadData() {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);//启用js脚本
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//禁用缓存
        webSettings.setAllowFileAccess(true);//设置可以访问文件
        webSettings.setBuiltInZoomControls(true); //设置支持缩放
        webSettings.setAllowContentAccess(false);
        webSettings.setDomStorageEnabled(true);

        //mWebView.requestFocus();
        //webSettings.setAllowContentAccess(true);
        //webSettings.setLoadWithOverviewMode(true);
        //webSettings.setUseWideViewPort(true);
        //webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//自适应屏幕

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtil.e(TAG, "overrideUrlLoading : " + url);
                if ("xtznative://buySuccess".equalsIgnoreCase(url)){
                    finish();
                    return true;
                }
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });



        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                mTitleUI.setText(TextUtils.isEmpty(title) ? mFromWhere : title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (0 == newProgress || 100 == newProgress) {
                    mLoadingProgressBar.setVisibility(View.GONE);
                    return;
                }
                mLoadingProgressBar.setVisibility(View.VISIBLE);
                mLoadingProgressBar.setProgress(newProgress);
            }
        });



        LogUtil.e(TAG, "url : " + mUrl);
        mWebView.loadUrl(mUrl);
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_title_back://返回
//                this.onBackPressed();
//                break;
            case R.id.tv_title_txt_left:
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();
            return;
        }
        super.onBackPressed();
    }

}