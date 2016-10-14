package com.ypwl.xiaotouzi.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.common.ServerStatus;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.interf.IRequestCallback;
import com.ypwl.xiaotouzi.manager.net.NetHelper;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.StringUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.view.dialog.KProgressHUDHelper;
import com.ypwl.xiaotouzi.view.dialog.lib.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * function : 找回密码.<br/>
 * Modify by lzj on 2015/11/5.
 */
public class GetBackPasswordActivity extends BaseActivity implements View.OnClickListener {

    private EditText mPhoneNumber;
    private EditText mCheckCode;
    private TextView mBtnGetCheckCode;
    private String mServerBackCheckCodeId;
    private int mTimeForNextCheckCode = 60;
    private KProgressHUD mDialogLoading;
    private boolean canGetCheckCode = true;//防止快速点击多次获取验证码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_back_password);
        initView();
    }

    private void initView() {
        //title
        findView(R.id.layout_title_back).setOnClickListener(this);
        findView(R.id.tv_title_back).setVisibility(View.GONE);
        ((ImageView)findView(R.id.iv_title_back)).setImageResource(R.mipmap.arrow_to_left);
//        ((TextView) findView(R.id.tv_title)).setText("找回密码");
        ImageView btnClose = findView(R.id.iv_title_right_image);
        btnClose.setVisibility(View.VISIBLE);
        btnClose.setImageResource(R.mipmap.icon_login_close);
        btnClose.setOnClickListener(this);
        findView(R.id.layout_title_view).setBackgroundColor(UIUtil.getColor(R.color.white));
        findView(R.id.v_title_divider_line).setVisibility(View.GONE);

        //content
        mPhoneNumber = findView(R.id.get_back_password_phone);
        mCheckCode = findView(R.id.get_back_check_code);
        mBtnGetCheckCode = findView(R.id.get_back_password_get_compare);
        mBtnGetCheckCode.setOnClickListener(this);
        findViewById(R.id.get_back_password_next).setOnClickListener(this);
        mDialogLoading = KProgressHUDHelper.createLoading(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_right_image://关闭
            case R.id.layout_title_back://返回
                finish();
                break;
            case R.id.get_back_password_get_compare://获取验证码
                if (canGetCheckCode) {
                    getCheckCode();
                }
                break;
            case R.id.get_back_password_next://下一步
                nextStep();
                break;
        }
    }

    /** 获取验证码 */
    private void getCheckCode() {
        String phoneNumber = mPhoneNumber.getText().toString().trim();
        if (StringUtil.isEmptyOrNull(phoneNumber)) {
            UIUtil.shakeView(mPhoneNumber);
            return;
        }
        canGetCheckCode = false;

        String url = StringUtil.format(URLConstant.RESET_PSW_GET_CHECK_CODE, phoneNumber);
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
                            timingForNextSend(); //倒计时
                            break;
                        case ServerStatus.SERVER_STATUS_INVALID_MOBILE://无效手机号
                            UIUtil.showToastShort(getString(R.string.reset_psw_hint_invalid_phone_number));
                            break;
                        case ServerStatus.SERVER_STATUS_MOBILE_NOT_EXISTS://手机号未绑定
                            UIUtil.showToastShort(getString(R.string.reset_psw_hint_phone_number_not_bind));
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

    /** 下一步 */
    private void nextStep() {
        String phoneNumber = mPhoneNumber.getText().toString().trim();
        String userInputCheckCode = mCheckCode.getText().toString().trim();
        if (StringUtil.isEmptyOrNull(phoneNumber)) {
            UIUtil.shakeView(mPhoneNumber);
            return;
        }
        if (StringUtil.isEmptyOrNull(mServerBackCheckCodeId)) {
            UIUtil.shakeView(mCheckCode);
            return;
        }
        if (StringUtil.isEmptyOrNull(userInputCheckCode)) {
            UIUtil.shakeView(mCheckCode);
            return;
        }
        String url = String.format(URLConstant.RESET_PSW_NEXT_STEP, GlobalUtils.toURLEncoded(phoneNumber), GlobalUtils.toURLEncoded(userInputCheckCode), GlobalUtils.toURLEncoded(mServerBackCheckCodeId));
        NetHelper.get(url, new IRequestCallback<String>() {
            @Override
            public void onStart() {
                mDialogLoading.show();
            }

            @Override
            public void onFailure(Exception e) {
                UIUtil.showToastShort(getString(R.string.reset_psw_hint_check_failed));
            }

            @Override
            public void onSuccess(String jsonStr) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    int status = jsonObject.optInt(Const.JSON_KEY_status, -1);
                    switch (status) {
                        case ServerStatus.SERVER_STATUS_OK://{"status":0,"token":"55f69a79c2724"}
                            String token = jsonObject.getString(Const.JSON_KEY_token);//验证成功时才有此字段
                            Intent intent = new Intent(mActivity, SetNewPasswordActivity.class);
                            intent.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA, token);
                            startActivityForResult(intent, 1);
                            break;
                        case ServerStatus.SERVER_STATUS_EXPIRE_RAND_CODE://验证码已失效
                            UIUtil.showToastShort(getString(R.string.reset_psw_hint_check_code_invalid));
                            break;
                        case ServerStatus.SERVER_STATUS_INVALID_RAND_CODE://验证码不正确
                            UIUtil.showToastShort(getString(R.string.reset_psw_hint_check_code_wrong));
                            break;
                    }
                } catch (JSONException e) {
                    this.onFailure(e);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Const.CODE_RESULT_NEEDREFRESH) {
            GetBackPasswordActivity.this.finish();
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
        if (mDialogLoading != null) {
            mDialogLoading.dismiss();
            mDialogLoading = null;
        }
        super.onDestroy();
    }
}
