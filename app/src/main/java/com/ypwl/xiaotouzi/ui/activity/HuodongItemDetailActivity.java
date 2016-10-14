package com.ypwl.xiaotouzi.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.ypwl.xiaotouzi.bean.ShareBean;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.interf.IRequestCallback;
import com.ypwl.xiaotouzi.manager.ShareAuthManager;
import com.ypwl.xiaotouzi.manager.net.NetHelper;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.ShareUtil;
import com.ypwl.xiaotouzi.utils.StringUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.view.dialog.KProgressHUDHelper;
import com.ypwl.xiaotouzi.view.dialog.lib.KProgressHUD;

/**
 * function : 活动详情页面.
 * <p/>
 * Modify by lzj on 2015/11/12.
 */
public class HuodongItemDetailActivity extends BaseActivity implements View.OnClickListener {
    private ProgressBar mLoadingProgressBar;
    private WebView mWebView;
    /** 当前分享平台 */
//    private int mCurrSharePlatform;
    private KProgressHUD mLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lucky_draw);
        initView();
    }

    private void initView() {
        //title
        findViewById(R.id.layout_title_back).setOnClickListener(this);
        ((TextView) findView(R.id.tv_title_back)).setText("优惠活动");
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText(getIntent().getStringExtra("title"));
        //content
        mLoadingProgressBar = (ProgressBar) findViewById(R.id.pb_loading);
        mWebView = (WebView) findViewById(R.id.wv);

        //初始化WebView并加载数据
        initWebViewAndLoadData();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebViewAndLoadData() {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);//设置WebView属性，能够执行Javascript脚本
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setBuiltInZoomControls(false); // 设置出现缩放工具
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//禁用缓存
        //设置Web视图
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("xtznative://gologin")) {
                    startActivity(LoginActivity.class);
                    finish();
                } else if (url.contains("xtznative://share")) {
                    mLoading = KProgressHUDHelper.createLoading(mActivity);
                    ShareUtil.share(mActivity, url, mLoading);
                }
//                if (url.contains("xtznative://share") && url.contains("plname=xlwb")) {//新浪微博的分享
//                    mCurrSharePlatform = ShareAuthManager.PLATFORM_Sina;
//                    launchShare();
//                } else if (url.contains("xtznative://share") && url.contains("plname=wxhy")) { //微信好友的分享
//                    mCurrSharePlatform = ShareAuthManager.PLATFORM_Weixin;
//                    launchShare();
//                } else if (url.contains("xtznative://share") && url.contains("plname=qqhy")) {  //QQ好友的分享
//                    mCurrSharePlatform = ShareAuthManager.PLATFORM_QQ;
//                    launchShare();
//                } else if (url.contains("xtznative://share") && url.contains("plname=wxpyq")) {//微信朋友圈的分享
//                    mCurrSharePlatform = ShareAuthManager.PLATFORM_Weixin_Circle;
//                    launchShare();
//                }
                else if ("xtznative://back".equals(url)) { //返回
                    HuodongItemDetailActivity.this.onBackPressed();
                } else if (url.contains("xtznative://gourl")) {//跳转至url
                    String[] urlArray = url.split("url=");
                    url = urlArray[1];
                    mWebView.loadUrl(url);
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
        });
        //加载需要显示的网页
        mWebView.loadUrl(getIntent().getStringExtra("url"));

    }

    /** 分享邀请码 */
//    private void launchShare() {
//        if (mLoading == null) {
//            mLoading = KProgressHUDHelper.createLoading(this);
//        }
//        mLoading.show();
//        String url = StringUtil.format(URLConstant.SHARE_GET_APP_SHARE_URL, GlobalUtils.token);
//        NetHelper.get(url, new IRequestCallback<ShareBean>() {
//            @Override
//            public void onStart() {
//            }
//
//            @Override
//            public void onFailure(Exception e) {
//                mLoading.dismiss();
//                UIUtil.showToastShort("获取分享信息失败");
//            }
//
//            @Override
//            public void onSuccess(ShareBean shareBean) {
//                mLoading.dismiss();
//                if (shareBean.getStatus() == 0) {
//                    ShareAuthManager.share(HuodongItemDetailActivity.this, mCurrSharePlatform,
//                            shareBean.getTitle(), shareBean.getContent(), shareBean.getUrl());
//                } else {
//                    onFailure(null);
//                }
//            }
//        });
//    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ShareAuthManager.getInstance().onActivityResult(this, requestCode, resultCode, data);
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
