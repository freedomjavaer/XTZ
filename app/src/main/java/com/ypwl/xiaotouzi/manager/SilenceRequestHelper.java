package com.ypwl.xiaotouzi.manager;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.umeng.message.UmengRegistrar;
import com.ypwl.xiaotouzi.bean.AppUpdateBean;
import com.ypwl.xiaotouzi.bean.CommonBean;
import com.ypwl.xiaotouzi.bean.SplashBean;
import com.ypwl.xiaotouzi.common.Configs;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.common.DeviceInfo;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.interf.IRequestCallback;
import com.ypwl.xiaotouzi.manager.net.NetHelper;
import com.ypwl.xiaotouzi.utils.CacheUtils;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.LogUtil;
import com.ypwl.xiaotouzi.utils.PackageManagerUtil;
import com.ypwl.xiaotouzi.utils.SignUtil;
import com.ypwl.xiaotouzi.utils.StorageUtil;
import com.ypwl.xiaotouzi.utils.StringUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * function : 静默网络请求交互
 * </p>
 * Created by lzj on 2015/11/30
 */
public class SilenceRequestHelper {
    protected final String TAG = SilenceRequestHelper.class.getSimpleName();
    private static SilenceRequestHelper instance;

    public static SilenceRequestHelper getInstance() {
        if (null == instance) {
            synchronized (SilenceRequestHelper.class) {
                if (null == instance) {
                    instance = new SilenceRequestHelper();
                }
            }
        }
        return instance;
    }

    private SilenceRequestHelper() {
    }


    /** 上传设备token用于消息推送 */
    public void uploadDeviceToken() {
        String device_token = UmengRegistrar.getRegistrationId(UIUtil.getContext());
        LogUtil.e(TAG, "device_token : " + device_token);
        if (StringUtil.isEmptyOrNull(GlobalUtils.token)
                || StringUtil.isEmptyOrNull(device_token)) {
            return;
        }
        String url = StringUtil.format(URLConstant.UPLOAD_DEVICE_TOKEN, GlobalUtils.token, device_token);
        NetHelper.getSilence(url, new InvalideCallback());
    }

    /** 登录注册后上传 MAC、IMEI、UUID */
    public void uploadDeviceInfoAfterLogin() {
        if (StringUtil.isEmptyOrNull(GlobalUtils.token)) {
            return;
        }
        boolean hasUpdateApp = CacheUtils.getBoolean(Const.KEY_APP_HAS_UPDATED, false);
        boolean hasUploadSuccess = CacheUtils.getBoolean(Const.KEY_HAS_UPLOAD_SERVER_DATA_AFTER_LOGIN, false);
        if (hasUploadSuccess && !hasUpdateApp) {//已经上传成功并且没有更新APP，不需要上传了
            LogUtil.e(TAG, "no need uploadDeviceInfoAfterLogin!!!");
            return;
        }
        //未上传成功或者更新了APP，需要再次上传
        LogUtil.e(TAG, "uploadDeviceInfoAfterLogin!!!");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> paramMap = new HashMap<>();
                paramMap.put("token", GlobalUtils.token);
                paramMap.put("mac_code", DeviceInfo.deviceMAC);
                paramMap.put("imei_code", DeviceInfo.deviceIMEI);
                paramMap.put("uuid_code", DeviceInfo.deviceUUID);
                String sign = SignUtil.signDeviceInfoAfterLogin(DeviceInfo.deviceUUID, DeviceInfo.deviceIMEI, DeviceInfo.deviceMAC, GlobalUtils.token);
                paramMap.put("sign", sign);
                NetHelper.postSilence(URLConstant.UPLOAD_SERVER_DATA_AFTER_LOGIN, paramMap, new IRequestCallback<CommonBean>() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onFailure(Exception e) {
                        CacheUtils.putBoolean(Const.KEY_HAS_UPLOAD_SERVER_DATA_AFTER_LOGIN, false);
                    }

                    @Override
                    public void onSuccess(CommonBean bean) {
                        CacheUtils.putBoolean(Const.KEY_HAS_UPLOAD_SERVER_DATA_AFTER_LOGIN, bean.getStatus() == 0);
                    }
                });
            }
        }).start();
    }

    /** 上传自己服务器统计数据 */
    public void uploadDeviceInfoForStatistics() {
        boolean hasUpdateApp = CacheUtils.getBoolean(Const.KEY_APP_HAS_UPDATED, false);
        boolean hasUploadSuccess = CacheUtils.getBoolean(Const.KEY_HAS_UPLOAD_SERVER_DATA_DEVICES_INFO, false);
        if (hasUploadSuccess && !hasUpdateApp) {//已经上传成功并且没有更新APP，不需要上传了
            LogUtil.e(TAG, "no need uploadDeviceInfoForStatistics!!!");
            return;
        }
        //未上传成功或者更新了APP，需要再次上传
        LogUtil.e(TAG, "uploadDeviceInfoForStatistics!!!");
        new Thread(new Runnable() {
            @SuppressWarnings("deprecation")
            @Override
            public void run() {
                String uuid = DeviceInfo.deviceUUID;
                String chid = String.valueOf(MyChannelHelper.getMyChannel());
                String version = PackageManagerUtil.getPackageVersionName(UIUtil.getContext());
                String brand = Build.BRAND.trim();
                String model = Build.MODEL.trim();
                String os_version = Build.VERSION.SDK.trim();
                Map<String, String> paramMap = new HashMap<>();
                paramMap.put("uuid", uuid);
                paramMap.put("chid", chid);
                paramMap.put("version", version);
                paramMap.put("brand", brand);
                paramMap.put("model", model);
                paramMap.put("os_version", os_version);
                String sign = SignUtil.signDeviceInfoForStatictics(uuid, chid, version, brand, model, os_version);
                paramMap.put("sign", sign);
                NetHelper.postSilence(URLConstant.UPLOAD_DEVICE_INFO_DATA, paramMap, new IRequestCallback<CommonBean>() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onFailure(Exception e) {
                        CacheUtils.putBoolean(Const.KEY_HAS_UPLOAD_SERVER_DATA_DEVICES_INFO, false);
                    }

                    @Override
                    public void onSuccess(CommonBean bean) {
                        CacheUtils.putBoolean(Const.KEY_HAS_UPLOAD_SERVER_DATA_DEVICES_INFO, bean.getStatus() == 0);
                    }
                });
            }
        }).start();
    }

    /** 通知服务端用户退出登录 */
    public void notifyServerUserLogout() {
        //退出登录网络请求
        String url = String.format(URLConstant.USER_LOGOUT, GlobalUtils.token);
        NetHelper.getSilence(url, new InvalideCallback());
    }

    /** 检测app是否需要更新 */
    public void checkAppIsNeedUpdate(final AppUpdateCheckCallBack callBack) {
        String channelIdStr = String.valueOf(MyChannelHelper.getMyChannel());
        String versionCodeStr = String.valueOf(PackageManagerUtil.getPackageVersionCode(UIUtil.getContext()));
        String url = StringUtil.format(URLConstant.APP_UPDATE_INFO, channelIdStr, versionCodeStr);
        NetHelper.getSilence(url, new IRequestCallback<AppUpdateBean>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onFailure(Exception e) {
                callBack.onCompleted(false, new AppUpdateBean());
            }

            @Override
            public void onSuccess(AppUpdateBean appUpdateBean) {
                boolean needUpdate = true;
                //返回数据的状态不是正常状态，不需更新
                if (appUpdateBean.getStatus() != 0) {
                    needUpdate = false;
                }
                //服务端状态不是1(建议更新)或2(强制更新),不需更新
                if (appUpdateBean.getApkUpdateState() == 0) {
                    needUpdate = false;
                }
                //本地apk版本号已经是最新的,不需更新
                int currPackageVersionCode = PackageManagerUtil.getPackageVersionCode(UIUtil.getContext());
                if (currPackageVersionCode >= appUpdateBean.getApkNewVersionCode()) {
                    needUpdate = false;
                }

                //如果本地以及存在可以更新的安装包，则赋值安装包路径
                File exitedApkFile = new File(StorageUtil.getXtzCacheRootPath(UIUtil.getContext()) + Configs.APKNAME);
                if (exitedApkFile.exists() && exitedApkFile.length() > 0) {
                    PackageInfo packageinfo = UIUtil.getContext().getPackageManager().getPackageArchiveInfo(exitedApkFile.getAbsolutePath(), PackageManager.GET_ACTIVITIES);
                    if (packageinfo != null && packageinfo.versionCode > currPackageVersionCode
                            && packageinfo.versionCode > appUpdateBean.getApkNewVersionCode()) {
                        appUpdateBean.setApkNewpath(exitedApkFile.getAbsolutePath());
                    }
                }
                callBack.onCompleted(needUpdate, appUpdateBean);
            }
        });
    }

    public interface AppUpdateCheckCallBack {

        void onCompleted(boolean needUpdate, AppUpdateBean appUpdateBean);
    }

    public void checkSplashFromNet() {
        String url = StringUtil.format(URLConstant.SPLASH_IMG, DeviceInfo.ScreenDensity);
        NetHelper.getSilence(url, new IRequestCallback<SplashBean>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onFailure(Exception e) {
                CacheUtils.putString(Const.IMG_SPLASH, null);
            }

            @Override
            public void onSuccess(SplashBean splashBean) {
                if (splashBean.getStatus() != 0) {
                    CacheUtils.putString(Const.IMG_SPLASH, null);
                    return;
                }
                CacheUtils.putString(Const.IMG_SPLASH, splashBean.getImg());
            }
        });
    }

    /** 静默通知服务端消息已读 */
    public void notifyServerMsgHasRead(String stand_id, int type) {
        String url = StringUtil.format(URLConstant.MESSAGE_DETAIL,GlobalUtils.token,type,stand_id);
        NetHelper.getSilence(url, new InvalideCallback());
    }

    /** 静默通知服务端手势密码关闭 */
    public void notifyServerGPswClose() {
        String url = StringUtil.format(URLConstant.GESTURE_PSW_CHANGE, GlobalUtils.token, 0, null);
        NetHelper.getSilence(url, new InvalideCallback());
    }


    /** 无效回调 仅作为参数使用 */
    private class InvalideCallback extends IRequestCallback<String> {

        @Override
        public void onStart() {
        }

        @Override
        public void onFailure(Exception e) {
        }

        @Override
        public void onSuccess(String jsonStr) {
        }
    }
}
