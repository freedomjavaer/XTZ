package com.ypwl.xiaotouzi.ui.activity;

import android.annotation.SuppressLint;
import android.net.http.SslError;
import android.os.Bundle;
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
import com.ypwl.xiaotouzi.utils.LogUtil;
import com.ypwl.xiaotouzi.utils.ShareUtil;
import com.ypwl.xiaotouzi.view.dialog.KProgressHUDHelper;
import com.ypwl.xiaotouzi.view.dialog.lib.KProgressHUD;

/**
 * function :晓投资banner详情页面
 * <p/>
 * Created by tengtao on 2016/1/25.
 */
public class XtzBannerDetailActivity extends BaseActivity implements View.OnClickListener {
    private ProgressBar mLoadingProgressBar;
    private WebView mWebView;
    private TextView mTitle;
    private KProgressHUD mLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xtz_banner_detail);
        initView();
    }

    private void initView() {
        //title
        findViewById(R.id.layout_title_back).setOnClickListener(this);
        ((TextView)findView(R.id.tv_title_back)).setText("晓投资");
        mTitle = (TextView) findViewById(R.id.tv_title);
        mTitle.setText(getIntent().getStringExtra("title"));
        //content
        mLoadingProgressBar = (ProgressBar) findViewById(R.id.pb_loading);
        mWebView = (WebView) findViewById(R.id.wv_xtz_banner_detail);
        mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);//关闭硬件加速器
        //初始化WebView并加载数据
        initWebViewAndLoadData();
    }

    /** 初始化WebView并加载数据 */
    @SuppressLint("SetJavaScriptEnabled")
    private void initWebViewAndLoadData() {
//        if (StringUtil.isEmptyOrNull(GlobalUtils.token)) {
//            UIUtil.showToastShort("用户信息有误");
//            finish();
//            return;
//        }
        String url = getIntent().getStringExtra("url");
        LogUtil.e(TAG, "url = " + url);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);//启用js脚本
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//禁用缓存
        webSettings.setAllowFileAccess(true);//设置可以访问文件
        webSettings.setBuiltInZoomControls(false); //设置支持缩放

        //设置Web视图
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("xtznative://gologin")) {
                    startActivity(LoginActivity.class);
                    finish();
                } else if (url.contains("xtznative://share")){
                    mLoading = KProgressHUDHelper.createLoading(mActivity);
                    ShareUtil.share(mActivity, url, mLoading);
                }else if (url.contains("xtznative://") && url.contains("back")) {//不跳转，返回
                    XtzBannerDetailActivity.this.onBackPressed();//返回
                } else if (url.contains("xtznative://") && url.contains("gourl?url")) {//跳转url
                    //xtznative://gourl?url=https://www.chenghuitong.net  (后面的URL是可变的)
                    mWebView.loadUrl(url.split("=")[1]);
                } else {
                    mWebView.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (0 == newProgress || 100 == newProgress) {
                    mLoadingProgressBar.setVisibility(View.GONE);
                    return;
                }
                mLoadingProgressBar.setVisibility(View.VISIBLE);
                mLoadingProgressBar.setProgress(newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                mTitle.setText(title);
            }
        });

        mWebView.loadUrl(url);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_title_back://返回
                this.onBackPressed();
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

    @Override
    protected void onDestroy() {
        if (mLoading != null) {
            mLoading.dismiss();
            mLoading = null;
        }
        super.onDestroy();
    }
}
