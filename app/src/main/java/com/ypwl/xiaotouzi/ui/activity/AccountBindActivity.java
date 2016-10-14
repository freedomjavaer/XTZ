package com.ypwl.xiaotouzi.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.bean.LoginBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.manager.ShareAuthManager;
import com.ypwl.xiaotouzi.ui.helper.AccountBindHelper;
import com.ypwl.xiaotouzi.ui.helper.AccountBindView;
import com.ypwl.xiaotouzi.utils.CacheUtils;
import com.ypwl.xiaotouzi.utils.StringUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.Util;
import com.ypwl.xiaotouzi.view.dialog.CustomDialog;
import com.ypwl.xiaotouzi.view.dialog.KProgressHUDHelper;
import com.ypwl.xiaotouzi.view.dialog.lib.KProgressHUD;

/**
 * Created by Administrator on 2015/9/22.
 * 绑定帐号
 * <p/>
 * 1.等同第三方登录,获取第三方信息
 * 2.将获取的第三方信息作为参数访问特定的接口
 * 3.返回成功后,再次获取登录信息,保存本地登录信息
 *
 * Modify by lzj on 2016/5/3.
 * <p>重构三方平台账号绑定,替换shareSDK为umeng授权</p>
 */
public class AccountBindActivity extends BaseActivity implements View.OnClickListener, AccountBindView {

    private View mLayoutQQ, mLayoutSina, mLayoutWexin;
    private TextView mStateQQ, mStateSina, mStateWexin;
    private AccountBindHelper mAccountBindHelper;
    private CustomDialog mDialog;
    private KProgressHUD mLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_binding);

        ((TextView) findView(R.id.tv_title_back)).setText("个人资料");
        ((TextView) findView(R.id.tv_title)).setText("绑定社交账号");
        findView(R.id.layout_title_back).setOnClickListener(this);

        mLayoutQQ = findView(R.id.layout_bind_qq);
        mLayoutSina = findView(R.id.layout_bind_sina);
        mLayoutWexin = findView(R.id.layout_bind_weixin);

        mStateQQ = findView(R.id.layout_bind_qq_state);
        mStateSina = findView(R.id.layout_bind_sina_state);
        mStateWexin = findView(R.id.layout_bind_weixin_state);

        mAccountBindHelper = new AccountBindHelper(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_title_back:// back
                finish();
                break;
            case R.id.layout_bind_qq:// qq bind change
                preCheckBindState(Const.LOGIN_TYPE_QQ);
                break;
            case R.id.layout_bind_sina:// sina bind change
                preCheckBindState(Const.LOGIN_TYPE_SINA_WEIBO);
                break;
            case R.id.layout_bind_weixin:// weixin bind change
                preCheckBindState(Const.LOGIN_TYPE_WECHAT);
                break;
        }
    }

    private void preCheckBindState(int type) {
        LoginBean loginBean = Util.legalLogin();
        if (loginBean == null) {
            startActivity(LoginActivity.class);
            finish();
            return;
        }

        String hintTypeMsg;
        int currBindState;
        switch (type) {
            case Const.LOGIN_TYPE_QQ:
                hintTypeMsg = "QQ";
                currBindState = loginBean.getBindQQFlag();
                break;
            case Const.LOGIN_TYPE_SINA_WEIBO:
                hintTypeMsg = "微博";
                currBindState = loginBean.getBindSinaFlag();
                break;
            case Const.LOGIN_TYPE_WECHAT:
                hintTypeMsg = "微信";
                currBindState = loginBean.getBindWeixinFlag();
                break;
            default:
                throw new IllegalArgumentException("bind type IllegalArgument...");
        }
        int currLoginType = CacheUtils.getInt(Const.LOGIN_TYPE, -1);
        if (currLoginType == type && currBindState > 0) {
            UIUtil.showToastShort(hintTypeMsg + "登录中，无法解除绑定");
            return;
        }

        if (currBindState == 1) {//解绑
            showDialogAskSure(type, hintTypeMsg);
            return;
        }
        //绑定账号
        mAccountBindHelper.bindAccount(this, type, hintTypeMsg);
    }

    /** 对话框提示是否取消绑定 */
    @SuppressWarnings("unused")
    private void showDialogAskSure(final int type, final String hintTypeMsg) {
        mDialog = new CustomDialog.AlertBuilder(this)
                .setDialogLayout(R.layout.cd_dialog_content_other)
                .setContentText("是否要解除与" + hintTypeMsg + "账号的绑定")
                .setContentTextGravity(Gravity.CENTER)
                .setPositiveBtn("确定解绑", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mAccountBindHelper.cancleBindAccount(type, hintTypeMsg);
                    }
                })
                .setNegativeBtn("不解绑", null)
                .create();
        mDialog.show();
    }

    @Override
    public void refreshViewData() {
        if (isFinishing()) {
            return;
        }
        LoginBean loginBean = Util.legalLogin();
        if (loginBean == null) {
            startActivity(LoginActivity.class);
            finish();
            return;
        }

        boolean unbindedQQ = loginBean.getBindQQFlag() == 0;
        boolean unbindedSina = loginBean.getBindSinaFlag() == 0;
        boolean unbindedWeixin = loginBean.getBindWeixinFlag() == 0;

//        mLayoutQQ.setOnClickListener(unbindedQQ ? this : null);
//        mLayoutQQ.setClickable(unbindedQQ);
        mLayoutQQ.setOnClickListener(this);
        mStateQQ.setText(unbindedQQ ? "未绑定" : "已绑定");
        mStateQQ.setTextColor(UIUtil.getColor(unbindedQQ?R.color.d:R.color.a));

//        mLayoutSina.setOnClickListener(unbindedSina ? this : null);
//        mLayoutSina.setClickable(unbindedSina);
        mLayoutSina.setOnClickListener(this);
        mStateSina.setText(unbindedSina ? "未绑定" : "已绑定");
        mStateSina.setTextColor(UIUtil.getColor(unbindedSina?R.color.d:R.color.a));

//        mLayoutWexin.setOnClickListener(unbindedWeixin ? this : null);
//        mLayoutWexin.setClickable(unbindedWeixin);
        mLayoutWexin.setOnClickListener(this);
        mStateWexin.setText(unbindedWeixin ? "未绑定" : "已绑定");
        mStateWexin.setTextColor(UIUtil.getColor(unbindedWeixin?R.color.d:R.color.a));
    }

    @Override
    public void loading() {
        if (mLoading == null) {
            mLoading = KProgressHUDHelper.createLoading(this);
        }
        mLoading.show();
    }

    @Override
    public void bindChangeFailed(String msg) {
        mLoading.dismiss();
        UIUtil.showToastShort(StringUtil.isEmptyOrNull(msg) ? "处理失败" : msg);
    }

    @Override
    public void bindChangeSuccess() {
        mLoading.dismiss();
        refreshViewData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ShareAuthManager.getInstance().onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        if (mAccountBindHelper != null) {
            mAccountBindHelper.onDestory();
            mAccountBindHelper = null;
        }
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
        if (mLoading != null) {
            mLoading.dismiss();
            mLoading = null;
        }
        super.onDestroy();
    }
}
