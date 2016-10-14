package com.ypwl.xiaotouzi.ui.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.Gravity;
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
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.interf.IRequestCallback;
import com.ypwl.xiaotouzi.manager.net.NetHelper;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.StringUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.view.dialog.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 基金详情
 *
 * Created by PDK on 2016/4/22.
 */
public class FundsDetailActivity extends BaseActivity implements View.OnClickListener{

    private TextView mTitleUI;
    private ProgressBar mLoadingProgressBar;
    private WebView mWebView;
    private String mUrl;
    private CustomDialog mDialog;
    private String productName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_funds_detail_webpage);
        mUrl = getIntent().getStringExtra(Const.KEY_INTENT_JUMP_BASE_DATA);
        productName = getIntent().getStringExtra(Const.KEY_INTENT_JUMP_FROM_DATA);

        if (StringUtil.isEmptyOrNull(mUrl)) {
            UIUtil.showToastShort("地址链接错误!!!");
            finish();
            return;
        }
        initView();
    }

    private void initView() {
        //title
        TextView mTitleBackTV = (TextView) findViewById(R.id.tv_title_back);
        findView(R.id.layout_title_back).setOnClickListener(this);
        mTitleUI = findView(R.id.tv_title);
        mTitleUI.setText("高端投资");
        mTitleBackTV.setText("高端投资");


        findViewById(R.id.tv_book).setOnClickListener(this);
        //content
        mLoadingProgressBar = (ProgressBar) findViewById(R.id.pb_loading);
        mWebView = (WebView) findViewById(R.id.wv);

        //初始化WebView并加载数据
        initWebViewAndLoadData();
    }

    /** 初始化WebView并加载数据 */
    @SuppressLint("SetJavaScriptEnabled")
    private void initWebViewAndLoadData() {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);//启用js脚本
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//禁用缓存
        webSettings.setAllowFileAccess(true);//设置可以访问文件
        webSettings.setBuiltInZoomControls(true); //设置支持缩放

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
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

        mWebView.loadUrl(mUrl);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_title_back://返回
                this.onBackPressed();
                break;
            case R.id.tv_book://预约
                showDialog();
                break;
        }
    }

    /** 弹出对话框提示是否退出登录 */
    private void showDialog() {
        if (mDialog == null) {
            mDialog = new CustomDialog.AlertBuilder(mActivity)
                    .setTitleText("")
                    .setContentText(getString(R.string.book_project))
                    .setContentTextGravity(Gravity.CENTER)
                    .setPositiveBtn(getString(R.string.btn_text_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mDialog.dismiss();
                            //网络请求预约
                            requestBook();
                        }
                    })
                    .setNegativeBtn(getString(R.string.btn_text_cancle), null)
                    .create();
        }
        mDialog.show();
    }

    /**预约项目*/
    private void requestBook() {
        String url = StringUtil.format(URLConstant.FINANCE_PLATFORM_CLIENT_BOOK,productName, GlobalUtils.token,1);
        NetHelper.get(url, new IRequestCallback<String>() {

            @Override
            public void onStart() {

            }

            @Override
            public void onFailure(Exception e) {
                UIUtil.showToastShort("网络异常，预约失败，请稍后重试");
            }

            @Override
            public void onSuccess(String jsonStr) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    int status = jsonObject.optInt("status");
                    String retMsg = jsonObject.optString("ret_msg");
                    if (status == 0){
                        UIUtil.showToastShort("预约成功");
                    }else if (status == 120){
                        UIUtil.showToastShort("手机未绑定");
                    }else if (status == 9000){
                        UIUtil.showToastShort(retMsg);
                    }else if (status == 5003){
                        UIUtil.showToastShort(retMsg);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

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
