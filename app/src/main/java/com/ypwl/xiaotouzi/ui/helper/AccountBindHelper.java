package com.ypwl.xiaotouzi.ui.helper;

import android.app.Activity;

import com.ypwl.xiaotouzi.bean.CommonBean;
import com.ypwl.xiaotouzi.bean.LoginBean;
import com.ypwl.xiaotouzi.bean.ThirdAuthUserInfoBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.interf.IRequestCallback;
import com.ypwl.xiaotouzi.interf.IThirdAuthCallback;
import com.ypwl.xiaotouzi.manager.JsonHelper;
import com.ypwl.xiaotouzi.manager.ShareAuthManager;
import com.ypwl.xiaotouzi.manager.net.NetHelper;
import com.ypwl.xiaotouzi.utils.CacheUtils;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.StringUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.Util;

/**
 * function:账户绑定帮助类
 *
 * <br/>
 * Created by lzj on 2016/5/3.
 */
public class AccountBindHelper {

    private AccountBindView mAccountBindView;

    public AccountBindHelper(AccountBindView accountBindView) {
        this.mAccountBindView = accountBindView;
        mAccountBindView.refreshViewData();
    }

    public void bindAccount(Activity activity, final int platformType, final String hintTypeMsg) {
        mAccountBindView.loading();
        int platform;
        switch (platformType) {
            case Const.LOGIN_TYPE_QQ:
                platform = ShareAuthManager.PLATFORM_QQ;
                break;
            case Const.LOGIN_TYPE_SINA_WEIBO:
                platform = ShareAuthManager.PLATFORM_Sina;
                break;
            case Const.LOGIN_TYPE_WECHAT:
                platform = ShareAuthManager.PLATFORM_Weixin;
                break;
            default:
                throw new IllegalArgumentException("bind type IllegalArgument...");
        }
        ShareAuthManager.auth(activity, platform, new IThirdAuthCallback() {
            @Override
            public void onFailed() {
                mAccountBindView.bindChangeFailed("授权认证失败");
            }

            @Override
            public void onSuccess(ThirdAuthUserInfoBean bean) {
                String url = StringUtil.format(URLConstant.BIND_THIRD_PLATFORM, platformType, bean.openid,
                        GlobalUtils.toURLEncoded(bean.username), GlobalUtils.token);
                NetHelper.get(url, new IRequestCallback<CommonBean>() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onFailure(Exception e) {
                        mAccountBindView.bindChangeFailed("绑定" + hintTypeMsg + "失败");
                    }

                    @Override
                    public void onSuccess(CommonBean bean) {
                        switch (bean.getStatus()) {
                            case 0:
                                UIUtil.showToastShort(hintTypeMsg + "绑定成功");
                                LoginBean loginBean = Util.legalLogin();
                                if (loginBean == null) {
                                    mAccountBindView.bindChangeFailed("请重新登录");
                                } else {
                                    switch (platformType) {
                                        case Const.LOGIN_TYPE_QQ:
                                            loginBean.setBindQQFlag(1);
                                            break;
                                        case Const.LOGIN_TYPE_SINA_WEIBO:
                                            loginBean.setBindSinaFlag(1);
                                            break;
                                        case Const.LOGIN_TYPE_WECHAT:
                                            loginBean.setBindWeixinFlag(1);
                                            break;
                                        default:
                                            mAccountBindView.bindChangeFailed("绑定类型不确定");
                                            return;
                                    }
                                    CacheUtils.putString(Const.KEY_LOGIN_USER, JsonHelper.toJSONString(loginBean));
                                    mAccountBindView.bindChangeSuccess();
                                }
                                break;
                            case 1601:
                                mAccountBindView.bindChangeFailed("第三方平台type不合法");
                                break;
                            case 1605:
                                mAccountBindView.bindChangeFailed("昵称为空");
                                break;
                            case 1607:
                                UIUtil.showToastShort("QQ");
                                mAccountBindView.bindChangeFailed(hintTypeMsg + "号已作为独立帐号登录,无法绑定");
                                break;
                            case 1014:
                                mAccountBindView.bindChangeFailed("没有绑定手机，已是最后一个第三方登录的方式，不允许解绑");
                                break;
                            default:
                                this.onFailure(null);
                                break;
                        }
                    }
                });
            }
        });

    }

    public void cancleBindAccount(final int platformType, final String hintTypeMsg) {
        mAccountBindView.loading();
        LoginBean loginBean = Util.legalLogin();
        if (loginBean == null) {
            mAccountBindView.bindChangeFailed("需要重新登录");
            return;
        }
        String thirdAuthName;
        switch (platformType) {
            case Const.LOGIN_TYPE_QQ:
                thirdAuthName = loginBean.getQq_nick();
                break;
            case Const.LOGIN_TYPE_SINA_WEIBO:
                thirdAuthName = loginBean.getWb_nick();
                break;
            case Const.LOGIN_TYPE_WECHAT:
                thirdAuthName = loginBean.getWx_nick();
                break;
            default:
                mAccountBindView.bindChangeFailed("绑定类型不确定");
                return;
        }
        String url = StringUtil.format(URLConstant.BIND_THIRD_PLATFORM, platformType, 0,
                GlobalUtils.toURLEncoded(thirdAuthName), GlobalUtils.token);
        NetHelper.get(url, new IRequestCallback<CommonBean>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onFailure(Exception e) {
                mAccountBindView.bindChangeFailed("解绑" + hintTypeMsg + "失败");
            }

            @Override
            public void onSuccess(CommonBean bean) {
                if (bean.getStatus() == 0) {
                    UIUtil.showToastShort(hintTypeMsg + "解绑成功");
                    LoginBean loginBean = Util.legalLogin();
                    if (loginBean == null) {
                        mAccountBindView.bindChangeFailed("请重新登录");
                        return;
                    }
                    switch (platformType) {
                        case Const.LOGIN_TYPE_QQ:
                            loginBean.setBindQQFlag(0);
                            break;
                        case Const.LOGIN_TYPE_SINA_WEIBO:
                            loginBean.setBindSinaFlag(0);
                            break;
                        case Const.LOGIN_TYPE_WECHAT:
                            loginBean.setBindWeixinFlag(0);
                            break;
                        default:
                            mAccountBindView.bindChangeFailed("绑定类型不确定");
                            return;
                    }
                    CacheUtils.putString(Const.KEY_LOGIN_USER, JsonHelper.toJSONString(loginBean));
                    mAccountBindView.bindChangeSuccess();
                } else {
                    this.onFailure(null);
                }
            }
        });
    }

    public void onDestory() {
        if (mAccountBindView != null) {
            mAccountBindView = null;
        }
    }
}
