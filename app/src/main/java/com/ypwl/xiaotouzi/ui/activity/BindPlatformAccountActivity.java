package com.ypwl.xiaotouzi.ui.activity;

import android.annotation.SuppressLint;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.event.BindAccountEvent;
import com.ypwl.xiaotouzi.manager.EventHelper;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.StringUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;

/**
 * @author tengtao
 * @time 2015/11/11 21:12
 * @des ${自动记账绑定帐号页面}
 */
public class BindPlatformAccountActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTvTitle;
    private ProgressBar mBindAccountProgressBar;
    private WebView mBindAccountWebView;
    private String pid;
    private String p_name;
    private Skip2DetailTask mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_platform_account);
        initView();
        initData();
    }

    private void initView() {
        pid = getIntent().getStringExtra("pid");
        p_name = getIntent().getStringExtra("p_name");
        findViewById(R.id.layout_title_back).setOnClickListener(this);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mBindAccountProgressBar = (ProgressBar) findViewById(R.id.pb_bind_account);
        mBindAccountWebView = (WebView) findViewById(R.id.wv_bind_account);
        mTvTitle.setText("绑定帐号");
        if (mTask == null) {
            mTask = new Skip2DetailTask();
        }
    }

    private void initData() {
        String url = String.format(URLConstant.BIND_PLATFORM_ACCOUNT, GlobalUtils.token, pid);
        requestData(url);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void requestData(String url) {
        if (StringUtil.isEmptyOrNull(GlobalUtils.token)) {
            UIUtil.showToastShort("用户信息有误");
            finish();
            return;
        }
        WebSettings settings = mBindAccountWebView.getSettings();
        settings.setJavaScriptEnabled(true);//启用js脚本
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);//禁用缓存
        settings.setAllowFileAccess(true);//设置可以访问文件
        settings.setBuiltInZoomControls(false); //设置支持缩放

        //设置Web视图
        mBindAccountWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                /**加载晓投资路径*/
                if (url.contains("xtznative://") && url.contains("back")) {//不跳转，返回
                    mTask.postDelayed(mTask, 200);
//                    BindPlatformAccountActivity.this.onBackPressed();//返回
                } else if (url.contains("xtznative://") && url.contains("gourl?url")) {//跳转url
                    mBindAccountWebView.loadUrl(url.split("=")[1]);
                }else{
                    /**加载合作方的网页路径*/
                    mBindAccountWebView.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });

        mBindAccountWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (0 == newProgress || 100 == newProgress) {
                    mBindAccountProgressBar.setVisibility(View.GONE);
                    return;
                }
                mBindAccountProgressBar.setVisibility(View.VISIBLE);
                mBindAccountProgressBar.setProgress(newProgress);
            }
        });
        mBindAccountWebView.loadUrl(url);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_title_back:
                this.onBackPressed();
                break;
            default:
                break;
        }
    }

    /** 跳转到平台投资详情 */
    private class Skip2DetailTask extends Handler implements Runnable {
        @Override
        public void run() {
            if(isFinishing()) {
                EventHelper.post(new BindAccountEvent(true));//通知绑定成功,并且页面已关闭
            }else{
                EventHelper.post(new BindAccountEvent(false));//通知绑定成功，页面没有关闭
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(this.isFinishing()){return;}
        if (mBindAccountWebView != null && mBindAccountWebView.canGoBack()) {
            mBindAccountWebView.goBack();
            return;
        }
        super.onBackPressed();
    }

    @Subscribe
    public void onBindAccountEvent(BindAccountEvent event){
        if(event!=null && !isFinishing()){
            finish();
        }
    }
}
