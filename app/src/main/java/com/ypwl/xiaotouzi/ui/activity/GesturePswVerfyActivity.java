package com.ypwl.xiaotouzi.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.bean.LoginBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.event.GpswChangeEvent;
import com.ypwl.xiaotouzi.event.LoginStateEvent;
import com.ypwl.xiaotouzi.manager.EventHelper;
import com.ypwl.xiaotouzi.manager.JsonHelper;
import com.ypwl.xiaotouzi.manager.SilenceRequestHelper;
import com.ypwl.xiaotouzi.utils.CacheUtils;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.Util;
import com.ypwl.xiaotouzi.view.getsturepassword.GestureContentView;
import com.ypwl.xiaotouzi.view.getsturepassword.GestureDrawline;

/**
 * 验证手势密码
 *
 * Created by PDK on 2016/3/16.
 */
public class GesturePswVerfyActivity extends BaseActivity implements View.OnClickListener {

    private TextView mVerifyHint;
    private GestureContentView mGestureContentView;
    private String mCurrGpswType;
    private int count = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_password_verify);
        mCurrGpswType = getIntent().getStringExtra(Const.KEY_INTENT_JUMP_BASE_DATA);
        initView();
    }

    private void initView() {
        //title
        findView(R.id.layout_title_view).setBackgroundColor(Color.TRANSPARENT);
        View backLayout = findView(R.id.layout_title_back);
        TextView title = findView(R.id.tv_title);
        backLayout.setOnClickListener(this);
        TextView backWord = findView(R.id.tv_title_back);
        ((ImageView) findView(R.id.iv_title_back)).setImageResource(R.mipmap.btn_008);
        backWord.setTextColor(UIUtil.getColor(R.color.white));
        if (Const.GPSW_CHANGE.equals(mCurrGpswType)) {
            backLayout.setVisibility(View.VISIBLE);
            backWord.setText("设置");
            title.setText("请输入旧密码");
        } else if (Const.GPSW_CHECK.equals(mCurrGpswType)) {
            backLayout.setVisibility(View.GONE);
            title.setText("校验手势密码");
        } else if (Const.GPSW_CHECK_CLOSE.equals(mCurrGpswType)) {
            backLayout.setVisibility(View.GONE);
            title.setText("校验手势密码");
        } else {
            backLayout.setVisibility(View.GONE);
            title.setText("请输入手势密码");
        }
        title.setTextColor(getResources().getColor(R.color.h));
        findView(R.id.v_title_divider_line).setVisibility(View.GONE);

        //content
        mVerifyHint = (TextView) findViewById(R.id.verify_hint);
        findViewById(R.id.text_forget_gesture).setOnClickListener(this);

        String mCachePsw = CacheUtils.getString(Const.KEY_GESTURE_PASSWORD, "");
        FrameLayout gestureContainer = (FrameLayout) findViewById(R.id.verify_gesture_container);
        mGestureContentView = new GestureContentView(this, true, mCachePsw, mGestureCallBack);
        mGestureContentView.setParentView(gestureContainer);
    }

    private GestureDrawline.GestureCallBack mGestureCallBack = new GestureDrawline.GestureCallBack() {

        @Override
        public void onGestureCodeInput(String inputCode) {
        }

        @Override
        public void checkedSuccess() {
            if (Const.GPSW_CHANGE.equals(mCurrGpswType)) {
                startActivity(GesturePswEditActivity.class, Const.GPSW_CHANGE);
                finish();
            } else if (Const.GPSW_CHECK.equals(mCurrGpswType)) {
                startActivity(MainActivity.class);
                finish();
            } else if (Const.GPSW_CHECK_CLOSE.equals(mCurrGpswType)) {
                CacheUtils.putBoolean(Const.KEY_GESTURE_SWITCHER_STATE, false);
                CacheUtils.putString(Const.KEY_GESTURE_PASSWORD, null);
                EventHelper.post(new GpswChangeEvent(false));
                finish();
            }
        }

        @Override
        public void checkedFail() {
            --count;
            mGestureContentView.clearDrawlineState(500L);
            mVerifyHint.setVisibility(View.VISIBLE);

            if (count == 0){
                startActivity(MainActivity.class);
                startActivity(LoginActivity.class);
                UIUtil.showToastShort("密码错误次数过多，请重新登录");
                CacheUtils.putBoolean(Const.KEY_GESTURE_SWITCHER_STATE, false);
                CacheUtils.putString(Const.KEY_GESTURE_PASSWORD, null);
                CacheUtils.putString(Const.KEY_LOGIN_USER, null);
                finish();
                return;
            }

            if (count < 4){
                mVerifyHint.setText("密码错误," + "您还可以输入" + count + "次");
                mVerifyHint.setTextColor(getResources().getColor(R.color.red));
            }else {
                mVerifyHint.setText(Html.fromHtml("<font color='#c70c1e'>密码错误</font>"));
            }

            mVerifyHint.startAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.shake));//颤抖吧

        }

    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_title_back://back
                finish();
                break;
            case R.id.text_forget_gesture://忘记手势密码
                startActivity(LoginActivity.class);
                break;
        }
    }

    @Subscribe
    public void onLoginStateEvent(LoginStateEvent event) {
        if (event.hasLogin) {//登录成功了，删除本地手势密码，通知服务端清除手势密码，跳转到首页
            LoginBean loginBean = Util.legalLogin();
            if (loginBean != null) {
                loginBean.setG_password(null);
                loginBean.setG_status(0);
                CacheUtils.putString(Const.KEY_LOGIN_USER, JsonHelper.toJSONString(loginBean));
            }
            CacheUtils.putBoolean(Const.KEY_GESTURE_SWITCHER_STATE, false);
            CacheUtils.putString(Const.KEY_GESTURE_PASSWORD, null);
            EventHelper.post(new GpswChangeEvent(false));
            SilenceRequestHelper.getInstance().notifyServerGPswClose();
            if (Const.GPSW_CHECK.equals(mCurrGpswType)) {//进入APP校验手势密码
                startActivity(MainActivity.class);
            } else {
                finish();
            }
        }

//        else {//未登录，
//            finish();
//        }
    }

}
