package com.ypwl.xiaotouzi.ui.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.interf.IRequestCallback;
import com.ypwl.xiaotouzi.manager.net.NetHelper;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.LogUtil;
import com.ypwl.xiaotouzi.utils.StringUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.view.dialog.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 保险页面
 *
 * Created by PDK on 2016/4/18.
 */
public class InsuranceActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTitle;
    private TextView mTitleTV;
    private WebView mWebView;
    private ProgressBar mLoadingProgressBar;
    private String url;
    private CustomDialog mDialog;
    private TextView mBook;
    private LinearLayout mDataContentView;
    private View mNoDataView;
    private FrameLayout mWVContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance);
        initView();
        initData();

    }

    private void initData() {
        //title
        mTitle.setText("海外投资");
        mTitleTV.setText("金融超市");

        //content
        url = StringUtil.format(URLConstant.FINANCE_FUNDS_INSURANCE, GlobalUtils.token);
        initWebViewAndLoadData();

    }

    boolean ISVISIBLE = false;

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebViewAndLoadData() {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);//启用js脚本
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//禁用缓存
        webSettings.setAllowFileAccess(true);//设置可以访问文件
        webSettings.setBuiltInZoomControls(true); //设置支持缩放
        //设置Web视图
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //mWVContainer.setVisibility(View.VISIBLE);
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                //mWVContainer.setVisibility(View.GONE);
                //ViewUtil.showContentLayout(Const.LAYOUT_ERROR,mNoDataView,mDataContentView);
                UIUtil.showToastShort("网络异常");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);


            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                LogUtil.e(TAG, "onProgressChanged :   newProgress = " + newProgress);
                if (newProgress == 100){

                    if (mWebView.canGoBack()){
                        ISVISIBLE = true;
                    }else {
                        ISVISIBLE = false;
                    }

                    if (ISVISIBLE){
                        mBook.setVisibility(View.VISIBLE);
                    }

                    //ViewUtil.showContentLayout(Const.LAYOUT_DATA,mNoDataView,mDataContentView);
                }
                if (0 == newProgress || 100 == newProgress) {
                    mLoadingProgressBar.setVisibility(View.GONE);

                    return;
                }
                mLoadingProgressBar.setVisibility(View.VISIBLE);
                mLoadingProgressBar.setProgress(newProgress);
            }
        });
        mWebView.loadUrl(url);

    }


    private void initView() {
        mTitle =   findView(R.id.tv_title);
        mTitleTV = findView(R.id.tv_title_back);
        mWebView = findView(R.id.wv_insurance);
        mLoadingProgressBar = findView(R.id.pb_loading);
        mBook = findView(R.id.tv_book);
        mWVContainer = (FrameLayout) findViewById(R.id.wv_container);

        //mDataContentView = (LinearLayout) findViewById(R.id.ll_data_content_view);
        //mNoDataView = findViewById(R.id.layout_no_data_view);

        findView(R.id.tv_book).setOnClickListener(this);

        findView(R.id.layout_title_back).setOnClickListener(this);

        //ViewUtil.showContentLayout(Const.LAYOUT_LOADING,mNoDataView,mDataContentView);

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack(); // goBack()表示返回WebView的上一页面
            mBook.setVisibility(View.GONE);
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.layout_title_back:
                if (mWebView.canGoBack()) {
                    mWebView.goBack(); // goBack()表示返回WebView的上一页面
                    mBook.setVisibility(View.GONE);
                }else {
                    finish();
                }
                break;
            case R.id.tv_book:
                showDialog();
                break;
        }
    }

    /** 弹出对话框提示是否退出登录 */
    private void showDialog() {
        if (mDialog == null) {
            mDialog = new CustomDialog.AlertBuilder(mActivity)
                    .setTitleText("")
                    .setContentText(getString(R.string.insurance_book_project))
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
        String url = StringUtil.format(URLConstant.FINANCE_PLATFORM_CLIENT_BOOK,"", GlobalUtils.token,2);
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


}
