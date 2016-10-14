package com.ypwl.xiaotouzi.ui.helper;

import com.umeng.message.PushAgent;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.im.push.YPushAgent;
import com.ypwl.xiaotouzi.manager.SilenceRequestHelper;
import com.ypwl.xiaotouzi.utils.CacheUtils;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.PackageManagerUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.Util;

/**
 * function:Splash页面帮助类，主要负责业务逻辑处理，不负责UI操作,UI操作通过Splash回调给activity
 *
 * <br/>
 * Created by lzj on 2016/4/18.
 */
public class SplashHelper {

    private SplashView mSplashView;

    public SplashHelper(SplashView splashView) {
        this.mSplashView = splashView;
    }

    public void initWhenSplash() {
        //标记本地缓存中是否更新了APP
        long lastVersionCode = CacheUtils.getLong(Const.KEY_LAST_VERSION_CODE, 0L);
        long currentVersionCode = PackageManagerUtil.getPackageVersionCode(UIUtil.getContext());
        CacheUtils.putBoolean(Const.KEY_APP_HAS_UPDATED, currentVersionCode > lastVersionCode);
        CacheUtils.putLong(Const.KEY_LAST_VERSION_CODE, currentVersionCode);

        PushAgent.getInstance(UIUtil.getContext()).enable();//开启umeng推送
        //初始化socket,开启长连接服务，开启推送，开启消息收发
        if (Util.legalLogin() != null) {//若是登录状态,设置最新的pushToken并开启socket
            YPushAgent.getInstance().setPushTokenAndEnable(GlobalUtils.token);
        }
        SilenceRequestHelper.getInstance().uploadDeviceInfoForStatistics();//自己服务器统计数据
        SilenceRequestHelper.getInstance().checkSplashFromNet();//检查更新Splash图片
    }

    public void initWhenNoNeedGpsw() {
        mSplashView.setImgSplash();
        mSplashView.delayEnterMain();
    }

    public void onDestory() {
        if (mSplashView != null) {
            mSplashView = null;
        }
    }
}
