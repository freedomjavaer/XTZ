package com.ypwl.xiaotouzi.ui.activity;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

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

/**
 * function : 重新设置密码.<br/>
 * Modify by lzj on 2015/11/5.
 */
public class SetNewPasswordActivity extends BaseActivity implements View.OnClickListener {
    private String mToken;
    private EditText mPassword, mRePassword;
    private ImageView mSwitchShowPsw, mSwitchShowRepsw;
    private KProgressHUD mDialogLoading;
    private boolean is_psw_hide = true;
    private boolean is_repsw_hide = true;

    @Override
    protected boolean enableSystemBarManager() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);

        mToken = getIntent().getStringExtra(Const.KEY_INTENT_JUMP_BASE_DATA);
        if (StringUtil.isEmptyOrNull(mToken)) {
            UIUtil.showToastShort(getString(R.string.submmit_new_psw_hint_no_token));
            this.finish();
            return;
        }
        initView();
    }

    private void initView() {
        //title
        findView(R.id.layout_title_back).setOnClickListener(this);
        findView(R.id.tv_title_back).setVisibility(View.GONE);
//        ((TextView) findView(R.id.tv_title)).setText("设置新密码");
        ImageView btnClose = findView(R.id.iv_title_right_image);
        btnClose.setVisibility(View.VISIBLE);
        btnClose.setImageResource(R.mipmap.icon_login_close);
        btnClose.setOnClickListener(this);
        findView(R.id.layout_title_view).setBackgroundColor(UIUtil.getColor(R.color.white));
        findView(R.id.v_title_divider_line).setVisibility(View.GONE);

        mPassword = findView(R.id.set_new_password_tv_1);
        mRePassword = findView(R.id.set_new_password_tv_2);
        mSwitchShowPsw = findView(R.id.btn_switch_show_psw);
        mSwitchShowRepsw = findView(R.id.btn_switch_show_repsw);
        mSwitchShowPsw.setOnClickListener(this);
        mSwitchShowRepsw.setOnClickListener(this);
        findView(R.id.set_new_password_btn).setOnClickListener(this);

        mDialogLoading = KProgressHUDHelper.createLoading(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_right_image://关闭
                setResult(Const.CODE_RESULT_NEEDREFRESH, null);
                finish();
                break;
            case R.id.layout_title_back://返回
                finish();
                break;
            case R.id.btn_switch_show_psw:// 切换密码隐藏显示
                TransformationMethod transformationMethod = is_psw_hide ?
                        HideReturnsTransformationMethod.getInstance() : PasswordTransformationMethod.getInstance();
                mSwitchShowPsw.setImageResource(is_psw_hide ? R.mipmap.pass_show : R.mipmap.pass_hide);
                mPassword.setTransformationMethod(transformationMethod);
                mPassword.setSelection(mPassword.getText().toString().length());
                is_psw_hide = !is_psw_hide;
                break;
            case R.id.btn_switch_show_repsw:// 切换确认密码隐藏显示
                TransformationMethod reTransformationMethod = is_repsw_hide ?
                        HideReturnsTransformationMethod.getInstance() : PasswordTransformationMethod.getInstance();
                mSwitchShowRepsw.setImageResource(is_repsw_hide ? R.mipmap.pass_show : R.mipmap.pass_hide);
                mRePassword.setTransformationMethod(reTransformationMethod);
                mRePassword.setSelection(mRePassword.getText().toString().length());
                is_repsw_hide = !is_repsw_hide;
                break;
            case R.id.set_new_password_btn://重置密码提交信息
                submmitNewPassword();
                break;
        }
    }

    /** 重置密码提交 */
    private void submmitNewPassword() {
        String psw = mPassword.getText().toString().trim();
        String rePsw = mRePassword.getText().toString().trim();
        if (StringUtil.isEmptyOrNull(psw)) {
            UIUtil.shakeView(mPassword);
            return;
        }
        if (StringUtil.isEmptyOrNull(rePsw)) {
            UIUtil.shakeView(mRePassword);
            return;
        }
        if (!psw.equals(rePsw)) {
            UIUtil.showToastShort(getString(R.string.submmit_new_psw_hint_psw_not_agreed));
            return;
        }
        if (psw.length() < 6) {
            UIUtil.showToastShort("密码长度过短，请至少设置6位");
            return;
        }
        mDialogLoading.show();
        String url = String.format(URLConstant.SUBMMIT_NEW_PSW, mToken, GlobalUtils.toURLEncoded(psw));
        NetHelper.get(url, new IRequestCallback<String>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onFailure(Exception e) {
                mDialogLoading.dismiss();
                UIUtil.showToastShort(getString(R.string.submmit_new_psw_hint_psw_submmit_error));
            }

            @Override
            public void onSuccess(String jsonStr) {
                mDialogLoading.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    int status = jsonObject.optInt(Const.JSON_KEY_status, -1);
                    switch (status) {
                        case ServerStatus.SERVER_STATUS_OK://修改成功,进入登录界面
                            UIUtil.showToastShort(getString(R.string.submmit_new_psw_hint_success));
                            setResult(Const.CODE_RESULT_NEEDREFRESH, null);
                            SetNewPasswordActivity.this.finish();
                            break;
                        case ServerStatus.SERVER_STATUS_INVALID_TOKEN://token非法
                            UIUtil.showToastShort(getString(R.string.submmit_new_psw_hint_token_illegal));
                            break;
                        case ServerStatus.SERVER_STATUS_PWD_TOO_SHORT://密码少于6位
                            UIUtil.showToastShort(getString(R.string.submmit_new_psw_hint_psw_too_short));
                            break;
                        case ServerStatus.SERVER_STATUS_PWD_TOO_LONG://密码大于16位
                            UIUtil.showToastShort(getString(R.string.submmit_new_psw_hint_psw_too_long));
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

    @Override
    protected void onDestroy() {
        if (mDialogLoading != null) {
            mDialogLoading.dismiss();
            mDialogLoading = null;
        }
        super.onDestroy();
    }
}
