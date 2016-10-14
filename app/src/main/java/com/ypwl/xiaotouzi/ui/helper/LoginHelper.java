package com.ypwl.xiaotouzi.ui.helper;

import android.app.Activity;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.bean.LoginBean;
import com.ypwl.xiaotouzi.bean.ThirdAuthUserInfoBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.common.ServerStatus;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.common.UmengEventID;
import com.ypwl.xiaotouzi.im.message.YMessageAgent;
import com.ypwl.xiaotouzi.im.push.YPushAgent;
import com.ypwl.xiaotouzi.interf.IRequestCallback;
import com.ypwl.xiaotouzi.interf.IThirdAuthCallback;
import com.ypwl.xiaotouzi.manager.JsonHelper;
import com.ypwl.xiaotouzi.manager.ShareAuthManager;
import com.ypwl.xiaotouzi.manager.SilenceRequestHelper;
import com.ypwl.xiaotouzi.manager.UmengEventHelper;
import com.ypwl.xiaotouzi.manager.net.NetHelper;
import com.ypwl.xiaotouzi.utils.CacheUtils;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.StringUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.Util;


/**
 * function:登录逻辑业务处理帮助类
 *
 * <br/>
 * Created by lzj on 2016/4/18.
 */
public class LoginHelper {

    private LoginView mLoginView;

    public LoginHelper(LoginView loginView) {
        this.mLoginView = loginView;
    }

    public void loginByPhoneNumber(String phoneNumber, String password) {
        CacheUtils.putInt(Const.LOGIN_TYPE, Const.LOGIN_TYPE_PHONE);
        String url = String.format(URLConstant.USER_LOGIN, phoneNumber, GlobalUtils.toURLEncoded(password), "");//第三个参数是可选字段return_url
        NetHelper.get(url, new IRequestCallback<LoginBean>() {
            @Override
            public void onStart() {
                mLoginView.loginLoading();
                handleDataBeforeLoginStart();
            }

            @Override
            public void onFailure(Exception e) {
                mLoginView.loginFailed();
            }

            @Override
            public void onSuccess(LoginBean bean) {
                switch (bean.getStatus()) {
                    case ServerStatus.SERVER_STATUS_OK:
                        UIUtil.showToastShort(UIUtil.getString(R.string.login_regist_hint_login_success));
                        handleDataAfterLoginSuccess(bean);
                        mLoginView.loginSuccess();
                        break;
                    case ServerStatus.SERVER_STATUS_PHONE_OR_PWD_INVALID: // 手机号或密码错误
                        UIUtil.showToastShort(UIUtil.getString(R.string.login_hint_error_phonenumber_or_password));
                        mLoginView.loginFailed();
                        break;
                    case ServerStatus.SERVER_STATUS_INVALID_OS_TYPE: // 手机类型错误
                        UIUtil.showToastShort(UIUtil.getString(R.string.login_hint_error_os_type));
                        mLoginView.loginFailed();
                        break;
                    default:
                        mLoginView.loginFailed();
                        break;
                }
            }
        });
    }

    public void loginByThirdPlatform(Activity activity, final int thirdPlatformType) {
        mLoginView.loginLoading();
        String umengEventId;
        final int platform;
        switch (thirdPlatformType) {
            case Const.LOGIN_TYPE_SINA_WEIBO:
                umengEventId = "SinaLogin";
                platform = ShareAuthManager.PLATFORM_Sina;
                break;
            case Const.LOGIN_TYPE_WECHAT:
                umengEventId = "WechatLogin";
                platform = ShareAuthManager.PLATFORM_Weixin;
                break;
            case Const.LOGIN_TYPE_QQ:
                umengEventId = "QQLogin";
                platform = ShareAuthManager.PLATFORM_QQ;
                break;
            default:
                throw new RuntimeException();
        }
        UmengEventHelper.onEvent(UmengEventID.ThirdLogin);
        UmengEventHelper.onEvent(umengEventId);
        CacheUtils.putInt(Const.LOGIN_TYPE, thirdPlatformType);
        ShareAuthManager.auth(activity, platform, new IThirdAuthCallback() {
            @Override
            public void onFailed() {
                mLoginView.authorizeFailed();
            }

            @Override
            public void onSuccess(ThirdAuthUserInfoBean bean) {
                switch (thirdPlatformType) {
                    case Const.LOGIN_TYPE_SINA_WEIBO:
                        thirdRegisterAndLogin(Const.LOGIN_TYPE_SINA_WEIBO, bean.openid, bean.username, bean.avatarUrl);
                        break;
                    case Const.LOGIN_TYPE_WECHAT:
                        thirdRegisterAndLogin(Const.LOGIN_TYPE_WECHAT, bean.openid, bean.username, bean.avatarUrl);
                        break;
                    case Const.LOGIN_TYPE_QQ:
                        thirdRegisterAndLogin(Const.LOGIN_TYPE_QQ, bean.openid, bean.username, bean.avatarUrl);

                        break;
                    default:
                        mLoginView.authorizeFailed();
                        break;
                }
            }
        });
    }

    private void thirdRegisterAndLogin(int thirdPlatformType, String openid, String nickname, String avatar_url) {
        String url = String.format(URLConstant.USER_LOGIN_AND_REGIST_BY_THIRD_PLATFORM,
                thirdPlatformType, openid, GlobalUtils.toURLEncoded(nickname), avatar_url);
        NetHelper.get(url, new IRequestCallback<LoginBean>() {
            @Override
            public void onStart() {
                mLoginView.loginLoading();
                handleDataBeforeLoginStart();
            }

            @Override
            public void onFailure(Exception e) {
                mLoginView.loginFailed();
            }

            @Override
            public void onSuccess(LoginBean bean) {
                switch (bean.getStatus()) {
                    case ServerStatus.SERVER_STATUS_OK:
                        UIUtil.showToastShort(UIUtil.getString(R.string.login_regist_hint_login_success));
                        handleDataAfterLoginSuccess(bean);
                        mLoginView.loginSuccess();
                        break;
                    case ServerStatus.SERVER_STATUS_INVALID_THIRD_TYPE://第三方平台type不合法
                        UIUtil.showToastShort(UIUtil.getString(R.string.login_regist_hint_login_error_third_platform_type));
                        mLoginView.loginFailed();
                        break;
                    case ServerStatus.SERVER_STATUS_INVALID_OPENID1://openid为空
                        UIUtil.showToastShort(UIUtil.getString(R.string.login_regist_hint_login_error_openid_null));
                        mLoginView.loginFailed();
                        break;
                    case ServerStatus.SERVER_STATUS_INVALID_NICKNAME://昵称为空
                        UIUtil.showToastShort(UIUtil.getString(R.string.login_regist_hint_login_error_nickname_null));
                        mLoginView.loginFailed();
                        break;
                    case ServerStatus.SERVER_STATUS_INVALID_OS_TYPE://手机类型错误
                        UIUtil.showToastShort(UIUtil.getString(R.string.login_regist_hint_login_error_os_type));
                        mLoginView.loginFailed();
                        break;
                    default:
                        mLoginView.loginFailed();
                        break;
                }
            }
        });
    }

    private void handleDataBeforeLoginStart() {
        YPushAgent.getInstance().disconnect();
        YMessageAgent.getInstance().disconnect();
    }

    private void handleDataAfterLoginSuccess(LoginBean bean) {
        CacheUtils.putString(Const.KEY_LOGIN_USER, JsonHelper.toJSONString(bean));//存储到本地
        Util.legalLogin();//缓存静态全局变量token
        SilenceRequestHelper.getInstance().uploadDeviceToken();//上传设备token用于消息推送
        SilenceRequestHelper.getInstance().uploadDeviceInfoAfterLogin();//上传登录后的设备信息
        YPushAgent.getInstance().setPushTokenAndEnable(GlobalUtils.token);//开启推送

        CacheUtils.putBoolean(Const.KEY_GESTURE_SWITCHER_STATE, bean.getG_status() == 1);//存储服务端手势密码开关状态
        if (!StringUtil.isEmptyOrNull(bean.getG_password()) && bean.getG_password().length() >= 4) {
            CacheUtils.putString(Const.KEY_GESTURE_PASSWORD, bean.getG_password());//存储服务端手势密码
        } else {
            CacheUtils.putString(Const.KEY_GESTURE_PASSWORD, null);
        }
    }

    public void onDestory() {
        if (mLoginView != null) {
            mLoginView = null;
        }
    }
}
