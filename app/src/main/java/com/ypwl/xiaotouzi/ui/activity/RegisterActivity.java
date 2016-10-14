package com.ypwl.xiaotouzi.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.common.ServerStatus;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.interf.IRequestCallback;
import com.ypwl.xiaotouzi.manager.UmengEventHelper;
import com.ypwl.xiaotouzi.manager.net.NetHelper;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.StringUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.view.dialog.CustomDialog;
import com.ypwl.xiaotouzi.view.dialog.lib.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;


/**
 * function : 注册界面.<br/>
 * Modify by lzj on 2015/11/5.
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private EditText mInputPhonenumber;
    private EditText mInputCheckCode;
    private TextView mBtnGetCheckCode;
    private EditText mInputPassword;
    private ImageView mBtnSwitchPswShow;
    private EditText mInputInviteCode;
    private CheckBox mCbAllowProtocol;
    private int mTimeForNextCheckCode = 60;
    private String mServerBackCheckCodeId;
    private KProgressHUD mLoading;
    private boolean canGetCheckCode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
    }

    private void initViews() {
        //title
        findView(R.id.layout_title_back).setOnClickListener(this);
        ((ImageView)findViewById(R.id.iv_title_back)).setImageResource(R.mipmap.arrow_to_left);
        findView(R.id.tv_title_back).setVisibility(View.GONE);
//        ((TextView) findView(R.id.tv_title)).setText("注册");
        ImageView btnClose = findView(R.id.iv_title_right_image);
        btnClose.setVisibility(View.VISIBLE);
        btnClose.setImageResource(R.mipmap.icon_login_close);
        btnClose.setOnClickListener(this);
        findView(R.id.layout_title_view).setBackgroundColor(UIUtil.getColor(R.color.white));
        findView(R.id.v_title_divider_line).setVisibility(View.GONE);

        //content
        mInputPhonenumber = findView(R.id.input_phonenumber);
        mInputCheckCode = findView(R.id.input_check_code);
        mBtnGetCheckCode = findView(R.id.btn_get_check_code);
        mInputPassword = findView(R.id.input_password);
        mBtnSwitchPswShow = findView(R.id.btn_switch_psw_show);
        mInputInviteCode = findView(R.id.input_invite_code);
        mCbAllowProtocol = findView(R.id.cb_allow_protocol);
        mBtnGetCheckCode.setOnClickListener(this);
        mBtnSwitchPswShow.setOnClickListener(this);
        findView(R.id.btn_show_protocol).setOnClickListener(this);
        findView(R.id.btn_register).setOnClickListener(this);
        //findView(R.id.btn_has_account).setOnClickListener(this);

        mInputInviteCode.addTextChangedListener(mInputInviteCodeTextWatcher);

        mLoading = KProgressHUD.create(this);
    }

    private TextWatcher mInputInviteCodeTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //UmengEventHelper.onEvent(UmengEventID.InputInvitationCodeRegister);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_right_image://关闭
            case R.id.layout_title_back://关闭
                finish();
                break;
            case R.id.btn_get_check_code:// 获取验证码
                getCheckCode();
                break;
            case R.id.btn_switch_psw_show:// 显示或隐藏密码
                switchPswShow();
                break;
            case R.id.btn_show_protocol:// 注册协议
                showProtocolDialog();
                break;
            case R.id.btn_register:// 注册
                gotoRegist();
                break;
//            case R.id.btn_has_account:// 已经有账号
//                finish();
//                break;
        }
    }


    /** 获取验证码 */
    private void getCheckCode() {
        String phone = mInputPhonenumber.getText().toString().trim();
        if (StringUtil.isEmptyOrNull(phone) || !GlobalUtils.isTelNum(phone)) {
            UIUtil.showToastShort(getString(R.string.reset_psw_hint_input_phone_number));
            UIUtil.shakeView(mInputPhonenumber);
            return;
        }
        if (!canGetCheckCode) {
            canGetCheckCode = false;
            return;
        }
        String url = StringUtil.format(URLConstant.USER_REGIST_GET_CHECK_CODE, GlobalUtils.toURLEncoded(phone));
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
                            mServerBackCheckCodeId = jsonObject.getString(Const.JSON_KEY_smsid);
                            timingForNextSend();
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

    private boolean is_psw_hide = true;

    private void switchPswShow() {
        TransformationMethod transformationMethod = is_psw_hide ?
                HideReturnsTransformationMethod.getInstance() : PasswordTransformationMethod.getInstance();
        mBtnSwitchPswShow.setImageResource(is_psw_hide ? R.mipmap.pass_show : R.mipmap.pass_hide);
        mInputPassword.setTransformationMethod(transformationMethod);
        mInputPassword.setSelection(mInputPassword.getText().toString().length());
        is_psw_hide = !is_psw_hide;
    }

    /** 用户协议声明信息框 */
    private void showProtocolDialog() {
        new CustomDialog.AlertBuilder(this)
                .setTitleText(getString(R.string.register_protocol_title))
                .setContentText(getString(R.string.register_user_protocol))
                .setContentTextSize(14.5f)
                .setPositiveBtn("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mCbAllowProtocol.setChecked(true);
                    }
                })
                .create().show();
    }


    /** 注册 */
    private void gotoRegist() {
        String phoneNumber = mInputPhonenumber.getText().toString().trim();
        String userInputCheckCode = mInputCheckCode.getText().toString().trim();
        String password = mInputPassword.getText().toString();
        String inviteCode = mInputInviteCode.getText().toString().trim();
        if (!GlobalUtils.isTelNum(phoneNumber)) {
            UIUtil.shakeView(mInputPhonenumber);
            return;
        }
        if (StringUtil.isEmptyOrNull(mServerBackCheckCodeId)) {
            UIUtil.shakeView(mInputCheckCode);
            return;
        }
        if (StringUtil.isEmptyOrNull(userInputCheckCode)) {
            UIUtil.shakeView(mInputCheckCode);
            return;
        }
        if (StringUtil.isEmptyOrNull(password)) {
            UIUtil.shakeView(mInputPassword);
            return;
        }
        if (!mCbAllowProtocol.isChecked()) {
            UIUtil.shakeView(findView(R.id.layou_user_protocol));
            return;
        }
        if (password.length() < 6) {
            UIUtil.showToastShort("密码长度过短，请至少设置6位");
            UIUtil.shakeView(mInputPassword);
            return;
        }
        mLoading.show();
        String url = StringUtil.format(URLConstant.USER_REGIST_SUBMMIT, phoneNumber, GlobalUtils.toURLEncoded(password),
                userInputCheckCode, mServerBackCheckCodeId, inviteCode);
        NetHelper.get(url, new RegistSubmmitCallBack());
        UmengEventHelper.onEvent("PhoneRegister");
    }

    /** 注册提交回调接口 */
    private class RegistSubmmitCallBack extends IRequestCallback<String> {
        @Override
        public void onStart() {
        }

        @Override
        public void onFailure(Exception e) {
            mLoading.dismiss();
            UIUtil.showToastShort(getString(R.string.regist_hint_regist_failed));
        }

        @Override
        public void onSuccess(String jsonStr) {
            mLoading.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);
                int status = jsonObject.optInt(Const.JSON_KEY_status, -1);
                switch (status) {
                    case ServerStatus.SERVER_STATUS_OK:
                        String phoneNumber = mInputPhonenumber.getText().toString().trim();
                        String password = mInputPassword.getText().toString().trim();
                        Intent data = new Intent();
                        data.putExtra("phone", phoneNumber);
                        data.putExtra("password", password);
                        setResult(Const.CODE_RESULT_NEEDREFRESH, data);
                        finish();
                        break;
                    case ServerStatus.SERVER_STATUS_INVALID_MOBILE://无效手机号
                        UIUtil.showToastShort(getString(R.string.reset_psw_hint_invalid_phone_number));
                        break;
                    case ServerStatus.SERVER_STATUS_MOBILE_EXISTS://手机号已存在
                        UIUtil.showToastShort(getString(R.string.regist_hint_phone_exist));
                        break;
                    case ServerStatus.SERVER_STATUS_PWD_TOO_SHORT://密码少于6位
                        UIUtil.showToastShort(getString(R.string.submmit_new_psw_hint_psw_too_short));
                        break;
                    case ServerStatus.SERVER_STATUS_PWD_TOO_LONG://密码大于16位
                        UIUtil.showToastShort(getString(R.string.submmit_new_psw_hint_psw_too_long));
                        break;
                    case ServerStatus.SERVER_STATUS_EXPIRE_RAND_CODE://验证码已失效
                        UIUtil.showToastShort(getString(R.string.reset_psw_hint_check_code_invalid));
                        break;
                    case ServerStatus.SERVER_STATUS_INVALID_RAND_CODE://验证码不正确
                        UIUtil.showToastShort(getString(R.string.reset_psw_hint_check_code_wrong));
                        break;
                    default:
                        this.onFailure(null);
                        break;
                }
            } catch (JSONException e) {
                this.onFailure(e);
            }
        }
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
                        mBtnGetCheckCode.setText(String.format(getString(R.string.reset_psw_x_after_can_resend), mTimeForNextCheckCode));
                        mBtnGetCheckCode.setEnabled(false);
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
                            mBtnGetCheckCode.setText(getString(R.string.reset_psw_can_resend));
                            mBtnGetCheckCode.setEnabled(true);
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
        if (mLoading != null) {
            mLoading.dismiss();
            mLoading = null;
        }
        mInputInviteCode.removeTextChangedListener(mInputInviteCodeTextWatcher);
        mInputInviteCodeTextWatcher = null;
        super.onDestroy();
    }
}
