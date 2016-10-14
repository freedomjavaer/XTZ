package com.ypwl.xiaotouzi.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.bean.CommonBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.event.GpswChangeEvent;
import com.ypwl.xiaotouzi.interf.IRequestCallback;
import com.ypwl.xiaotouzi.manager.EventHelper;
import com.ypwl.xiaotouzi.manager.net.NetHelper;
import com.ypwl.xiaotouzi.utils.CacheUtils;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.LogUtil;
import com.ypwl.xiaotouzi.utils.StringUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.view.dialog.lib.KProgressHUD;
import com.ypwl.xiaotouzi.view.getsturepassword.GestureContentView;
import com.ypwl.xiaotouzi.view.getsturepassword.GestureDrawline;
import com.ypwl.xiaotouzi.view.getsturepassword.GesturePatternHintView;

/**
 * 手势密码设置及修改页面
 * <p/>
 * Created by PDK on 2016/3/16.
 */
public class GesturePswEditActivity extends BaseActivity implements View.OnClickListener {

    private GesturePatternHintView mGesturePatternHintView;
    private TextView mHint;
    private GestureContentView mGestureContentView;
    private boolean mIsFirstInput = true;
    private String mFirstPassword;
    private KProgressHUD mDialogLoading;
    private String mCurrGpswType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_password_edit);
        mCurrGpswType = getIntent().getStringExtra(Const.KEY_INTENT_JUMP_BASE_DATA);
        initView();
    }

    private void initView() {
        //title
        findView(R.id.layout_title_view).setBackgroundColor(Color.TRANSPARENT);

        findView(R.id.layout_title_back).setOnClickListener(this);
        ((ImageView) findView(R.id.iv_title_back)).setImageResource(R.mipmap.btn_008);
        TextView backWord = findView(R.id.tv_title_back);
        backWord.setTextColor(UIUtil.getColor(R.color.white));
        backWord.setText("设置");

        TextView title = findView(R.id.tv_title);
        int titleResId = Const.GPSW_CHANGE.equals(mCurrGpswType) ? R.string.my_info_change_getsture_password : R.string.my_info_set_getsture_password;
        title.setText(titleResId);
        title.setTextColor(getResources().getColor(R.color.h));

        TextView titleRight = findView(R.id.tv_title_txt_right);
        titleRight.setVisibility(View.VISIBLE);
        titleRight.setText(R.string.reset_getsture_password);
        titleRight.setTextColor(getResources().getColor(R.color.h));
        titleRight.setOnClickListener(this);

        findView(R.id.v_title_divider_line).setVisibility(View.GONE);

        //content
        mGesturePatternHintView = findView(R.id.gesture_pattern_hint_view);
        mHint = findView(R.id.hint);

        FrameLayout gestureContainer = (FrameLayout) findViewById(R.id.gesture_container);
        mGestureContentView = new GestureContentView(this, false, "_", mGestureCallBack);
        mGestureContentView.setParentView(gestureContainer);
        mGesturePatternHintView.setPath("");
    }

    private GestureDrawline.GestureCallBack mGestureCallBack = new GestureDrawline.GestureCallBack() {

        @Override
        public void onGestureCodeInput(String inputCode) {
            LogUtil.e(TAG, "onGestureCodeInput : inputCode=" + inputCode);
            if (!isInputPassValidate(inputCode)) {
                mHint.setText(Html.fromHtml("<font color='#c70c1e'>至少链接4个点, 请重新输入</font>"));
                mGestureContentView.clearDrawlineState(0L);
                return;
            }
            if (mIsFirstInput) {
                mFirstPassword = inputCode;
                mGesturePatternHintView.setPath(inputCode);
                mGestureContentView.clearDrawlineState(0L);
                mHint.setText(getString(R.string.gp_set_gesture_pattern_again));
            } else {
                if (inputCode.equals(mFirstPassword)) {
                    handleWhenSuccess();
                } else {
                    mHint.setText(Html.fromHtml("<font color='#c70c1e'>与上一次绘制不一致，请重新绘制</font>"));
                    mHint.startAnimation(AnimationUtils.loadAnimation(GesturePswEditActivity.this, R.anim.shake));
                    mGestureContentView.clearDrawlineState(500L); // 保持绘制的线，0.5秒后清除
                }
            }
            mIsFirstInput = false;
        }

        @Override
        public void checkedSuccess() {
        }

        @Override
        public void checkedFail() {
        }
    };

    /** 处理成功设置手势密码后的响应 */
    private void handleWhenSuccess() {
        if (mDialogLoading == null) {
            mDialogLoading = KProgressHUD.create(this);
        }
        mDialogLoading.show();
        mGestureContentView.clearDrawlineState(0L);

        String url = StringUtil.format(URLConstant.GESTURE_PSW_CHANGE, GlobalUtils.token, 1, mFirstPassword);
        NetHelper.get(url, new IRequestCallback<CommonBean>() {

            @Override
            public void onStart() {
            }

            @Override
            public void onFailure(Exception e) {
                mDialogLoading.dismiss();
                UIUtil.showToastShort("设置失败，请重新确认");
            }

            @Override
            public void onSuccess(CommonBean bean) {
                mDialogLoading.dismiss();
                if (bean.getStatus() == 0) {
                    UIUtil.showToastShort("设置成功");
                    CacheUtils.putBoolean(Const.KEY_GESTURE_SWITCHER_STATE, true);
                    CacheUtils.putString(Const.KEY_GESTURE_PASSWORD, mFirstPassword);
                    EventHelper.post(new GpswChangeEvent(true));
                    finish();
                } else {
                    onFailure(null);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_title_back:
                finish();
                break;
            case R.id.tv_title_txt_right:
                mIsFirstInput = true;
                mFirstPassword = null;
                mGesturePatternHintView.setPath("");
                mHint.setText(R.string.gp_set_gesture_pattern);
                break;
        }

    }

    /** 校验至少四个点 */
    private boolean isInputPassValidate(String inputPassword) {
        return !(TextUtils.isEmpty(inputPassword) || inputPassword.length() < 4);
    }


}
