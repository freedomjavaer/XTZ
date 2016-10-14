package com.ypwl.xiaotouzi.ui.activity;

import android.os.Bundle;
import android.widget.ImageView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.ui.helper.SplashHelper;
import com.ypwl.xiaotouzi.ui.helper.SplashView;
import com.ypwl.xiaotouzi.utils.CacheUtils;
import com.ypwl.xiaotouzi.utils.ImgLoadUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.Util;

/**
 * function : app闪屏界面.
 * <br/>
 * Modify by lzj on 2015/11/3.
 * <br/>
 * Modify by lzj on 2016/4/18. change to SplashView mode
 */
public class SplashActivity extends BaseActivity implements SplashView {

    private SplashHelper mSplashHelper;

    @Override
    protected boolean enableSystemBarManager() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwipeBackActivityEnable(false);

        mSplashHelper = new SplashHelper(this);
        mSplashHelper.initWhenSplash();

        setContentView(R.layout.activity_splash);

        mSplashHelper.initWhenNoNeedGpsw();

    }

    //检查是否需要校验手势密码
    public void checkGesturePsw() {
        if (null != Util.legalLogin()
                && CacheUtils.getBoolean(Const.KEY_GESTURE_SWITCHER_STATE, false)
                && CacheUtils.getString(Const.KEY_GESTURE_PASSWORD, null) != null) {
            startActivity(GesturePswVerfyActivity.class, Const.GPSW_CHECK);
        }else {
            startActivity(MainActivity.class);
        }
        finish();
    }

    @Override
    public void setImgSplash() {
        ImgLoadUtil.loadImgByPath(CacheUtils.getString(Const.IMG_SPLASH, null), (ImageView) findView(R.id.img_splash), R.drawable.bg_splash_top_default);
    }

    @Override
    public void delayEnterMain() {
        UIUtil.postDelayed(mDelayEnterRunnabe, 3000);
    }

    private Runnable mDelayEnterRunnabe = new Runnable() {
        @Override
        public void run() {
            checkGesturePsw();
        }
    };

    @Override
    protected void onDestroy() {
        UIUtil.removeCallbacksFromMainLooper(mDelayEnterRunnabe);
        mDelayEnterRunnabe = null;
        if (mSplashHelper != null) {
            mSplashHelper.onDestory();
            mSplashHelper = null;
        }
        super.onDestroy();
    }

}
