package com.ypwl.xiaotouzi.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.squareup.otto.Subscribe;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.bean.LoginBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.event.GpswChangeEvent;
import com.ypwl.xiaotouzi.interf.IRequestCallback;
import com.ypwl.xiaotouzi.manager.SilenceRequestHelper;
import com.ypwl.xiaotouzi.manager.net.NetHelper;
import com.ypwl.xiaotouzi.utils.CacheUtils;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.StringUtil;
import com.ypwl.xiaotouzi.utils.Util;

/**
 * 设置页面------回款提醒设置以及手势密码的设置
 *
 * Created by PDK on 2016/3/15.
 * <br/>
 * Modify by lzj on 2016/4/29.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private boolean mCurrBackMoneyHintState;
    private ImageView mSwicherBackMoneyHint;
    private ImageView mSwicherGpsw;
    private View mLayoutItemChangeGpsw;
    private boolean mCurrGpswState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }

    private void initView() {
        //title
        findView(R.id.layout_title_back).setOnClickListener(this);
        ((TextView) findView(R.id.tv_title_back)).setText("更多");
        ((TextView) findView(R.id.tv_title)).setText("设置");

        //content
        mSwicherBackMoneyHint = findView(R.id.layout_item_backmoney_hint_switch);
        mSwicherGpsw = findView(R.id.layout_item_psw_switch);
        mLayoutItemChangeGpsw = findView(R.id.layout_item_change_getsture_password);
        mSwicherBackMoneyHint.setOnClickListener(this);
        mSwicherGpsw.setOnClickListener(this);
        mLayoutItemChangeGpsw.setOnClickListener(this);

        //show default view
        refreshViewData();
    }

    private void refreshViewData() {
        LoginBean loginBean = Util.legalLogin();
        if (loginBean == null) {
            finish();
            return;
        }
        //回款提醒状态 1-打开 0-关闭
        mCurrBackMoneyHintState = loginBean.getReturnedMoneyFlag() == 1;
        mSwicherBackMoneyHint.setImageResource(mCurrBackMoneyHintState ? R.mipmap.btn_046_select : R.mipmap.btn_046);

        //初始化手势密码显示
        mCurrGpswState = CacheUtils.getBoolean(Const.KEY_GESTURE_SWITCHER_STATE, false);
        mSwicherGpsw.setImageResource(mCurrGpswState ? R.mipmap.btn_046_select : R.mipmap.btn_046);
        mLayoutItemChangeGpsw.setVisibility(mCurrGpswState ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_title_back://返回上一级
                finish();
                break;
            case R.id.layout_item_backmoney_hint_switch://回款提醒开关
                changeBackMoneySwitch();
                break;
            case R.id.layout_item_psw_switch://手势密码开关
                //note: 已经设置并打开状态->校验密码成功后关闭 : 未设置状态->创建新密码
                String type = mCurrGpswState ? Const.GPSW_CHECK_CLOSE : Const.GPSW_CREATE;
                startActivity(mCurrGpswState ? GesturePswVerfyActivity.class : GesturePswEditActivity.class, type);
                break;
            case R.id.layout_item_change_getsture_password://修改手势密码
                startActivity(GesturePswVerfyActivity.class, Const.GPSW_CHANGE);
                break;
        }

    }

    /** 切换回款提醒开关 */
    private void changeBackMoneySwitch() {
        LoginBean loginBean = Util.legalLogin();
        if (loginBean == null) {
            return;
        }
        int localState = mCurrBackMoneyHintState ? 0 : 1;
        loginBean.setReturnedMoneyFlag(localState);
        CacheUtils.putString(Const.KEY_LOGIN_USER, JSON.toJSONString(loginBean));
        refreshViewData();

        String url = StringUtil.format(URLConstant.SETTING_PUSH_BACK_MONEY_STATE, GlobalUtils.token, 0, 0, localState);
        NetHelper.get(url, new IRequestCallback<String>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onFailure(Exception e) {

            }

            @Override
            public void onSuccess(String jsonStr) {
            }
        });
    }

    @Subscribe
    public void onEventWhenGpswChange(GpswChangeEvent event) {
        if (!isFinishing()) {
            refreshViewData();
            if (!event.gpswState) {//关闭手势密码,通知服务器
                SilenceRequestHelper.getInstance().notifyServerGPswClose();
            }
        }
    }

}
