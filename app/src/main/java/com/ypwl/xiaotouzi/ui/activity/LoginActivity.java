package com.ypwl.xiaotouzi.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.event.LoginStateEvent;
import com.ypwl.xiaotouzi.manager.EventHelper;
import com.ypwl.xiaotouzi.manager.ShareAuthManager;
import com.ypwl.xiaotouzi.ui.helper.LoginHelper;
import com.ypwl.xiaotouzi.ui.helper.LoginView;
import com.ypwl.xiaotouzi.utils.StringUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.ViewUtil;
import com.ypwl.xiaotouzi.view.dialog.KProgressHUDHelper;
import com.ypwl.xiaotouzi.view.dialog.lib.KProgressHUD;

/**
 * function : 登录界面.
 * <br/>
 * Modify by lzj on 2015/11/4.
 * <br/>
 * Modify by lzj on 2016/4/18.
 */
@SuppressWarnings("unchecked")
public class LoginActivity extends BaseActivity implements View.OnClickListener, LoginView {

    public static final String FROM_TEMP_TOKEN_LOGIN = "FROM_TEMP_TOKEN_LOGIN";

    private EditText mInputPhonenumber;
    private EditText mInputPassword;
    private KProgressHUD mLoading;
    private LoginHelper mLoginHelper;
    private String fromWhere = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_login);
        this.overridePendingTransition(R.anim.login_register_bottom_in, R.anim.login_register_bottom_in_out);
        setSwipeBackActivityEnable(false);
        initViews();
        fromWhere = getIntent().getStringExtra(Const.KEY_INTENT_JUMP_BASE_DATA);
        mLoginHelper = new LoginHelper(this);
    }

    private void initViews() {
        //title
        findView(R.id.layout_title_back).setVisibility(View.GONE);
        ImageView btnClose = findView(R.id.iv_title_right_image);
        btnClose.setVisibility(View.VISIBLE);
        btnClose.setImageResource(R.mipmap.icon_login_close);
        btnClose.setOnClickListener(this);
        findView(R.id.layout_title_view).setBackgroundColor(UIUtil.getColor(R.color.white));
        findView(R.id.v_title_divider_line).setVisibility(View.GONE);

        //content
        mInputPhonenumber = findView(R.id.input_phonenumber);
        mInputPassword = findView(R.id.input_password);
        findView(R.id.btn_login).setOnClickListener(this);
        findView(R.id.btn_forget_psw).setOnClickListener(this);
        findView(R.id.btn_user_regist).setOnClickListener(this);
        findView(R.id.login_by_weibo).setOnClickListener(this);
        findView(R.id.login_by_weixin).setOnClickListener(this);
        findView(R.id.login_by_qq).setOnClickListener(this);

        mLoading = KProgressHUDHelper.createLoading(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_right_image://close
                onBackPressed();
                break;
            case R.id.btn_login:// login by phone
                loginByPhoneNumber();
                break;
            case R.id.btn_forget_psw:// forget_psw
                startActivity(GetBackPasswordActivity.class);
                break;
            case R.id.btn_user_regist:// user_regist
                startActivityForResult(new Intent(this, RegisterActivity.class), 1);
                break;
            case R.id.login_by_weibo:// login_by_weibo
                mLoginHelper.loginByThirdPlatform(this, Const.LOGIN_TYPE_SINA_WEIBO);
                break;
            case R.id.login_by_weixin:// login_by_weixin
                mLoginHelper.loginByThirdPlatform(this, Const.LOGIN_TYPE_WECHAT);
                break;
            case R.id.login_by_qq:// login_by_qq
                mLoginHelper.loginByThirdPlatform(this, Const.LOGIN_TYPE_QQ);
                break;
        }
    }

    private void loginByPhoneNumber() {
        String phoneNumber = mInputPhonenumber.getText().toString().trim();
        String password = mInputPassword.getText().toString().trim();

        if (StringUtil.isEmptyOrNull(phoneNumber)) {
            UIUtil.shakeView(mInputPhonenumber);
            return;
        }
        if (StringUtil.isEmptyOrNull(password)) {
            UIUtil.shakeView(mInputPassword);
            return;
        }
        mLoginHelper.loginByPhoneNumber(phoneNumber, password);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ShareAuthManager.getInstance().onActivityResult(this, requestCode, resultCode, data);
        if (resultCode == Const.CODE_RESULT_NEEDREFRESH) {//注册成功返回进行登录
            if (data == null) {
                return;
            }
            String phone = data.getStringExtra("phone");
            String password = data.getStringExtra("password");
            if (StringUtil.isEmptyOrNull(phone) || StringUtil.isEmptyOrNull(password)) {
                return;
            }
            mInputPhonenumber.setText(phone);
            mInputPassword.setText(password);
            mLoginHelper.loginByPhoneNumber(phone, password);
        }
    }

    @Override
    public void loginLoading() {
        mLoading.show();
    }

    @Override
    public void authorizeFailed() {
        mLoading.dismiss();
        UIUtil.showToastShort("授权失败");
    }

    @Override
    public void loginSuccess() {
        mLoading.dismiss();
        //登录成功 通知已经打开的页面更新登录状态依赖的视图数据
        EventHelper.post(new LoginStateEvent(true));
        //关闭登录页面
        if (FROM_TEMP_TOKEN_LOGIN.equals(fromWhere)) {// 访客模式
            ViewUtil.jump2Main(this, null);
        }

        finish();
    }

    @Override
    public void loginFailed() {
        mLoading.dismiss();
    }


    @Override
    public void onBackPressed() {
        if (mLoading != null && mLoading.isShowing()) {
            mLoading.dismiss();
            return;
        }
        EventHelper.post(new LoginStateEvent(false));
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (mLoading != null) {
            mLoading.dismiss();
            mLoading = null;
        }
        if (mLoginHelper != null) {
            mLoginHelper.onDestory();
            mLoginHelper = null;
        }
        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(0, R.anim.login_register_bottom_out);//activity下滑出去
    }
}
