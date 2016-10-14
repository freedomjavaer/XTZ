package com.ypwl.xiaotouzi.manager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;

import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.utils.Log;
import com.ypwl.xiaotouzi.XtzApp;
import com.ypwl.xiaotouzi.bean.ThirdAuthUserInfoBean;
import com.ypwl.xiaotouzi.interf.IThirdAuthCallback;
import com.ypwl.xiaotouzi.utils.CacheUtils;
import com.ypwl.xiaotouzi.utils.LogUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.view.dialog.KProgressHUDHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * function :分享及授权管理类
 * <p/>
 * note:在使用的activity的onActivityResult方法里调用{@code ShareAuthManager.getInstance().onActivityResult}
 * <p/>
 * Created by lzj on 2016/5/3
 */
public class ShareAuthManager {
    private static final String TAG = "ShareAuthManager";

    private static final String SP_FILENAME_ThirdAuth = "ThirdAuth";

    private volatile static ShareAuthManager instance = new ShareAuthManager();

    public static synchronized ShareAuthManager getInstance() {
        return instance;
    }

    /** 平台：微信好友(微信) */
    public static final int PLATFORM_Weixin = 0;
    /** 平台：微信朋友圈 */
    public static final int PLATFORM_Weixin_Circle = 1;
    /** 平台：新浪微博 */
    public static final int PLATFORM_Sina = 2;
    /** 平台：QQ */
    public static final int PLATFORM_QQ = 3;
    /** 平台：短信 */
    public static final int PLATFORM_SMS = 4;

    /**
     * 初始化配置信息，在application的onCreated方法中调用
     */
    public static void init() {
        //微信 appid appsecret
        PlatformConfig.setWeixin("wxdbf1ff43bd409820", "55ee7321e6dcf33cbbcf0fe41330c575");
        //新浪微博 appkey appsecret
        PlatformConfig.setSinaWeibo("3034957155", "a1ab910dc7795284f5f533db4dd66416");
        // QQ和Qzone appid appkey
        PlatformConfig.setQQZone("1104911726", "Dgd3yVbzPqmC7YSr");

        //others
        Config.isloadUrl = true;//开启回流率统计
        Log.LOG = false;
        Config.IsToastTip = false;
    }

    private ShareAuthManager() {
    }

    /**
     * 启动分享
     *
     * @param activity     分享页面当前activity
     * @param platform     分享平台 {@link ShareAuthManager#PLATFORM_Weixin}
     *                     , {@link ShareAuthManager#PLATFORM_Weixin_Circle}
     *                     , {@link ShareAuthManager#PLATFORM_Sina}
     *                     , {@link ShareAuthManager#PLATFORM_QQ}
     *                     , {@link ShareAuthManager#PLATFORM_SMS}
     * @param shareTitle   分享标题文本
     * @param shareContent 分享内容文本
     * @param shareUrl     分享链接
     */
    public static void share(Activity activity, int platform, String shareTitle, String shareContent, String shareUrl) {
        getInstance().showShare(activity, platform, shareTitle, shareContent, shareUrl);
    }


    /**
     * 授权认证
     *
     * @param activity           授权页面当前activity
     * @param platform           授权平台 {@link ShareAuthManager#PLATFORM_Weixin}
     *                           , {@link ShareAuthManager#PLATFORM_Sina}
     *                           , {@link ShareAuthManager#PLATFORM_QQ}
     * @param iThirdAuthCallback 授权结果回调,成功则包含三方登录用户信息的数据对象
     */

    public static void auth(final Activity activity, int platform, final IThirdAuthCallback iThirdAuthCallback) {
        getInstance().authDoing(activity, platform, iThirdAuthCallback);
    }

    /**
     * 授权认证删除(暂时忽略操作结果状态),包括删除微信 新浪 QQ的认证状态
     */

    public static void authDelete() {
        getInstance().authDeleteDoing();
    }

    private UMShareAPI mShareAPI;

    private void authDoing(final Activity activity, int platform, final IThirdAuthCallback iThirdAuthCallback) {
        SHARE_MEDIA share_media;
        switch (platform) {
            case PLATFORM_Weixin:
                share_media = SHARE_MEDIA.WEIXIN;
                break;
            case PLATFORM_Sina:
                share_media = SHARE_MEDIA.SINA;
                break;
            case PLATFORM_QQ:
                share_media = SHARE_MEDIA.QQ;
                break;
            default:
                throw new IllegalArgumentException("please choose assigned platform!");
        }

        mShareAPI = UMShareAPI.get(activity);
        if (!mShareAPI.isInstall(activity, share_media)) {
            UIUtil.showToastShort("您当前未安装" + share_media);
            iThirdAuthCallback.onFailed();
            return;
        }
        Activity topActivity = XtzApp.getApplication().getTopActivity();
        if (topActivity != null) {
            Dialog primaryTargetDialog = KProgressHUDHelper.createLoading(topActivity).getPrimaryTargetDialog();
            if (primaryTargetDialog != null) {
                Config.dialog = primaryTargetDialog;
            }
        }
        mShareAPI.doOauthVerify(activity, share_media, new UMAuthListener() {
            @Override
            public void onComplete(final SHARE_MEDIA platform, int action, Map<String, String> data) {
                final StringBuilder sb = new StringBuilder("data: ");
                if (data != null) {
                    for (Map.Entry<String, String> entry : data.entrySet()) {
                        sb.append(entry.getKey()).append("=").append(entry.getValue()).append("; ");
                    }
                    CacheUtils.putString(SP_FILENAME_ThirdAuth, "data-all", sb.toString());
                }

                mShareAPI.getPlatformInfo(activity, platform, new UMAuthListener() {
                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                        if (map == null) {
                            iThirdAuthCallback.onFailed();
                            return;
                        }
                        String openidKey, usernameKey, avatarUrlKey;
                        if (share_media == SHARE_MEDIA.WEIXIN) {
                            openidKey = "openid";
                            usernameKey = "nickname";
                            avatarUrlKey = "headimgurl";
                        } else if (share_media == SHARE_MEDIA.SINA) {
                            openidKey = "id";
                            usernameKey = "screen_name";
                            avatarUrlKey = "profile_image_url";
                        } else {
                            openidKey = "openid";
                            usernameKey = "screen_name";
                            avatarUrlKey = "profile_image_url";
                        }

                        ThirdAuthUserInfoBean bean = new ThirdAuthUserInfoBean();
                        if (share_media == SHARE_MEDIA.SINA) {
                            String result = map.get("result");
                            CacheUtils.putString(SP_FILENAME_ThirdAuth, "map-all", result);
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                bean.openid = jsonObject.optString(openidKey);
                                bean.username = jsonObject.optString(usernameKey);
                                bean.avatarUrl = jsonObject.optString(avatarUrlKey);
                                CacheUtils.putString(SP_FILENAME_ThirdAuth, "user-info", bean.toString());
                                iThirdAuthCallback.onSuccess(bean);
                            } catch (JSONException e) {
                                iThirdAuthCallback.onFailed();
                            }
                        } else {
                            StringBuilder sb = new StringBuilder("map: ");
                            for (Map.Entry<String, String> entry : map.entrySet()) {
                                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("; ");
                                if (openidKey.equals(entry.getKey())) {
                                    bean.openid = entry.getValue();
                                }
                                if (usernameKey.equals(entry.getKey())) {
                                    bean.username = entry.getValue();
                                }
                                if (avatarUrlKey.equals(entry.getKey())) {
                                    bean.avatarUrl = entry.getValue();
                                }
                            }
                            CacheUtils.putString(SP_FILENAME_ThirdAuth, "map-all", sb.toString());
                            CacheUtils.putString(SP_FILENAME_ThirdAuth, "user-info", bean.toString());
                            iThirdAuthCallback.onSuccess(bean);
                        }
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                        iThirdAuthCallback.onFailed();
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media, int i) {
                        iThirdAuthCallback.onFailed();
                        LogUtil.e(TAG,"第三方登录被取消了---getPlatformInfo");
                    }
                });
            }

            @Override
            public void onError(SHARE_MEDIA platform, int action, Throwable t) {
                iThirdAuthCallback.onFailed();
            }

            @Override
            public void onCancel(SHARE_MEDIA platform, int action) {
                iThirdAuthCallback.onFailed();
                LogUtil.e(TAG,"第三方登录被取消了---doOauthVerify");
            }
        });
    }

    private void authDeleteDoing() {
        Activity topActivity = XtzApp.getApplication().getTopActivity();
        if (topActivity == null) {
            return;
        }
        mShareAPI = UMShareAPI.get(topActivity);
        mShareAPI.deleteOauth(topActivity, SHARE_MEDIA.WEIXIN, mInvalideUMAuthListener);
        mShareAPI.deleteOauth(topActivity, SHARE_MEDIA.SINA, mInvalideUMAuthListener);
        mShareAPI.deleteOauth(topActivity, SHARE_MEDIA.QQ, mInvalideUMAuthListener);
    }

    private UMAuthListener mInvalideUMAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
        }
    };

    private static UMImage IMAGE_SHARE = new UMImage(UIUtil.getContext(), "http://www.xtz168.com/statics/xtz_wap/img/xtz.png");


    private void showShare(Activity activity, int platform, String shareTitle, String shareContent, String shareUrl) {
        SHARE_MEDIA share_media;
        ShareAction shareAction = new ShareAction(activity);
        switch (platform) {
            case PLATFORM_Weixin:
                share_media = SHARE_MEDIA.WEIXIN;
                shareAction.withMedia(IMAGE_SHARE);
                break;
            case PLATFORM_Weixin_Circle:
                share_media = SHARE_MEDIA.WEIXIN_CIRCLE;
                shareAction.withMedia(IMAGE_SHARE);
                break;
            case PLATFORM_Sina:
                share_media = SHARE_MEDIA.SINA;
                shareAction.withMedia(IMAGE_SHARE);
                break;
            case PLATFORM_QQ:
                share_media = SHARE_MEDIA.QQ;
                shareAction.withMedia(IMAGE_SHARE);
                break;
            case PLATFORM_SMS:
                share_media = SHARE_MEDIA.SMS;
                shareContent = shareContent + " " + shareUrl;
                break;
            default:
                throw new IllegalArgumentException("please choose assigned platform!");
        }
        mShareAPI = UMShareAPI.get(activity);
        if (!mShareAPI.isInstall(activity, share_media) && share_media != SHARE_MEDIA.SMS) {
            UIUtil.showToastShort("您当前未安装" + share_media);
            return;
        }

        Activity topActivity = XtzApp.getApplication().getTopActivity();
        if (topActivity != null) {
            Dialog primaryTargetDialog = KProgressHUDHelper.createLoading(topActivity).getPrimaryTargetDialog();
            if (primaryTargetDialog != null) {
                Config.dialog = primaryTargetDialog;
            }
        }

        shareAction.setPlatform(share_media)
                .withTitle(shareTitle)
                .withText(shareContent)
                .withTargetUrl(shareUrl)
                .setCallback(mInvalideUMShareListener)
                .share();
    }

    private UMShareListener mInvalideUMShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA share_media) {

        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {

        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {

        }
    };

    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        LogUtil.e(TAG, "onActivityResult: " + activity.getLocalClassName() + " ,requestCode=" + requestCode + " ,resultCode=" + resultCode);
        UMShareAPI.get(activity).onActivityResult(requestCode, resultCode, data);
    }
}
