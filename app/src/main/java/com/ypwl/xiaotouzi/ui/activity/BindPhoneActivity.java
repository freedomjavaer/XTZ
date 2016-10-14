package com.ypwl.xiaotouzi.ui.activity;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.bean.LoginBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.common.ServerStatus;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.event.UserInfoChangeEvent;
import com.ypwl.xiaotouzi.interf.IRequestCallback;
import com.ypwl.xiaotouzi.manager.EventHelper;
import com.ypwl.xiaotouzi.manager.net.NetHelper;
import com.ypwl.xiaotouzi.utils.CacheUtils;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.StringUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.Util;
import com.ypwl.xiaotouzi.view.dialog.KProgressHUDHelper;
import com.ypwl.xiaotouzi.view.dialog.lib.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * function : 绑定手机号.
 * <p/>
 * Modify by lzj on 2015/11/7.
 */
public class BindPhoneActivity extends BaseActivity implements View.OnClickListener {
    private EditText mPhoneNumber;//手机号
    private EditText mCheckCode;//验证码
    private TextView mCheckCodeGetBtn;//获取验证码按钮
    private EditText mPassword;//密码
    private ImageView mPasswordDisplayBtn;//密码是否可见
    private int mTimeForNextCheckCode = 60;
    private String mServerBackCheckCodeId;
    private boolean mPasswordIsHide = true;
    private KProgressHUD mDialogLoading;
    private boolean canGetCheckCode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone);

        initView();
    }

    private void initView() {
        //title
        findViewById(R.id.layout_title_back).setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_title_back)).setText("个人资料");
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText("绑定手机号");
        //content
        mPhoneNumber = (EditText) findViewById(R.id.bind_phone_number);
        mCheckCode = (EditText) findViewById(R.id.bind_phone_check_code);
        mCheckCodeGetBtn = (TextView) findViewById(R.id.bind_phone_check_code_get_btn);
        mCheckCodeGetBtn.setOnClickListener(this);
        mPassword = (EditText) findViewById(R.id.bind_phone_psw);
        mPasswordDisplayBtn = (ImageView) findViewById(R.id.bind_phone_psw_display);
        mPasswordDisplayBtn.setOnClickListener(this);
        findViewById(R.id.bind_phone_submmit).setOnClickListener(this);
        //dialog
        mDialogLoading = KProgressHUDHelper.createLoading(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_title_back://返回
                finish();
                break;
            case R.id.bind_phone_check_code_get_btn://获取验证码
                getCheckCode();
                break;
            case R.id.bind_phone_psw_display://显示或隐藏密码
                switchPswShow();
                break;
            case R.id.bind_phone_submmit://完成提交信息
                submmitData();
                break;
        }
    }

    private boolean is_psw_hide = true;

    private void switchPswShow() {
        TransformationMethod transformationMethod = is_psw_hide ?
                HideReturnsTransformationMethod.getInstance() : PasswordTransformationMethod.getInstance();
        mPasswordDisplayBtn.setImageResource(is_psw_hide ? R.mipmap.pass_show : R.mipmap.pass_hide);
        mPassword.setTransformationMethod(transformationMethod);
        mPassword.setSelection(mPassword.getText().toString().length());
        is_psw_hide = !is_psw_hide;
    }

    /** 完成，提交数据 */
    private void submmitData() {
        final String phoneNumber = mPhoneNumber.getText().toString().trim();
        String userInputCheckCode = mCheckCode.getText().toString().trim();
        String password = mPassword.getText().toString();
        if (StringUtil.isEmptyOrNull(phoneNumber) || !GlobalUtils.isTelNum(phoneNumber)) {
            UIUtil.shakeView(mPhoneNumber);
            return;
        }
        if (StringUtil.isEmptyOrNull(mServerBackCheckCodeId)) {
            UIUtil.shakeView(mCheckCodeGetBtn);
            return;
        }
        if (StringUtil.isEmptyOrNull(userInputCheckCode)) {
            UIUtil.shakeView(mCheckCode);
            return;
        }
        if (StringUtil.isEmptyOrNull(password)) {
            UIUtil.shakeView(mPassword);
            return;
        }
        String url = StringUtil.format(URLConstant.USER_INFO_SUBMMIT_BIND_PHONE, GlobalUtils.token, phoneNumber, userInputCheckCode, mServerBackCheckCodeId, password);
        NetHelper.get(url, new IRequestCallback<String>() {
            @Override
            public void onStart() {
                mDialogLoading.show();
            }

            @Override
            public void onFailure(Exception e) {
                mDialogLoading.dismiss();
                UIUtil.showToastShort(getString(R.string.bind_phone_hint_failed));
            }

            @Override
            public void onSuccess(String jsonStr) {
                mDialogLoading.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    int status = jsonObject.optInt(Const.JSON_KEY_status, -1);
                    switch (status) {
                        case ServerStatus.SERVER_STATUS_OK:
                            UIUtil.showToastShort(getString(R.string.bind_phone_hint_success));
                            LoginBean loginBean = Util.legalLogin();
                            if (loginBean != null) {
                                loginBean.setPhone(phoneNumber);
                                CacheUtils.putString(Const.KEY_LOGIN_USER, JSON.toJSONString(loginBean));
                            }
                            EventHelper.post(new UserInfoChangeEvent(true));//发送用户信息发送变化事件
                            finish();
                            break;
                        case ServerStatus.SERVER_STATUS_EXPIRE_RAND_CODE://验证码已失效
                            UIUtil.showToastShort(getString(R.string.reset_psw_hint_check_code_invalid));
                            break;
                        case ServerStatus.SERVER_STATUS_INVALID_RAND_CODE://验证码不正确
                            UIUtil.showToastShort(getString(R.string.reset_psw_hint_check_code_wrong));
                            break;
                        default:
                            break;
                    }
                } catch (JSONException e) {
                    this.onFailure(e);
                }
            }
        });
    }


    /** 获取验证码 */
    private void getCheckCode() {
        String phoneNumber = mPhoneNumber.getText().toString().trim();
        if (StringUtil.isEmptyOrNull(phoneNumber) || !GlobalUtils.isTelNum(phoneNumber)) {
            UIUtil.shakeView(mPhoneNumber);
            return;
        }
        if (!canGetCheckCode) {
            canGetCheckCode = false;
            return;
        }
        String url = StringUtil.format(URLConstant.CHECK_CODE_GET_BY_PHONE_NUMBER, GlobalUtils.token, phoneNumber);
        NetHelper.get(url, new IRequestCallback<String>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onFailure(Exception e) {
                canGetCheckCode = true;
                UIUtil.showToastShort(getString(R.string.reset_psw_hint_get_check_code_failed));
            }

            @Override
            public void onSuccess(String jsonStr) {
                canGetCheckCode = true;
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    int status = jsonObject.optInt(Const.JSON_KEY_status, -1);
                    switch (status) {
                        case ServerStatus.SERVER_STATUS_OK:
                            UIUtil.showToastShort(getString(R.string.reset_psw_hint_sms_send_success));
                            timingForNextSend();
                            mServerBackCheckCodeId = jsonObject.getString(Const.JSON_KEY_smsid);
                            break;
                        case ServerStatus.SERVER_STATUS_INVALID_MOBILE://无效手机号
                            UIUtil.showToastShort(getString(R.string.reset_psw_hint_invalid_phone_number));
                            break;
                        case ServerStatus.SERVER_STATUS_MOBILE_EXISTS://手机号已存在
                            UIUtil.showToastShort(getString(R.string.regist_hint_phone_exist));
                            break;
                        case ServerStatus.SERVER_STATUS_TOO_MANY_SMS_REQUEST://短信请求过于频繁
                            UIUtil.showToastShort(getString(R.string.reset_psw_hint_sms_gap_time_too_short));
                            break;
                        case ServerStatus.SERVER_STATUS_SEND_MSM_FAILED://发送短信失败
                            UIUtil.showToastShort(getString(R.string.reset_psw_hint_sms_send_failed));
                            break;
                        default:
                            this.onFailure(null);
                            break;
                    }
                } catch (JSONException e) {
                    this.onFailure(e);
                }
            }
        });
    }

    /** 下次发送验证码计时 */
    private void timingForNextSend() {
        final Timer timer = new Timer();
        TimerTask mTimerTask = new TimerTask() {
            @Override
            public void run() {
                UIUtil.post(new Runnable() {
                    @Override
                    public void run() {
                        mCheckCodeGetBtn.setText(String.format(getString(R.string.reset_psw_x_after_can_resend), mTimeForNextCheckCode));
                        mCheckCodeGetBtn.setEnabled(false);
                    }
                });
                mTimeForNextCheckCode--;
                if (0 == mTimeForNextCheckCode) {
                    this.cancel();
                    timer.cancel();
                    timer.purge();
                    UIUtil.post(new Runnable() {
                        @Override
                        public void run() {
                            mTimeForNextCheckCode = 60;
                            mCheckCodeGetBtn.setText(getString(R.string.reset_psw_can_resend));
                            mCheckCodeGetBtn.setEnabled(true);
                            canGetCheckCode = true;
                        }
                    });
                }
            }
        };
        timer.schedule(mTimerTask, 0, 1000);
    }

    @Override
    protected void onDestroy() {
        mDialogLoading = null;
        super.onDestroy();
    }
}
