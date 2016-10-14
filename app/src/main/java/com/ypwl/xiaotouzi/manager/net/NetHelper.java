package com.ypwl.xiaotouzi.manager.net;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.ImageView;

import com.ypwl.xiaotouzi.common.Configs;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.interf.IRequestCallback;
import com.ypwl.xiaotouzi.manager.JsonHelper;
import com.ypwl.xiaotouzi.ui.activity.LoginActivity;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.LogUtil;
import com.ypwl.xiaotouzi.utils.NetworkUtils;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * function : 网络请求类（单例模式）.所有请求均是异步操作，回调接口运行在UI线程.
 *
 * <p>Created by lzj on 2015/11/3.</p>
 */
@SuppressWarnings({"unused", "unchecked"})
public class NetHelper {
    private static final String TAG = NetHelper.class.getSimpleName();
    private volatile static NetHelper instance = new NetHelper();
    private Map<String, IRequestCallback> mCallbackMap;

    public static NetHelper getInstance() {
        return instance;
    }

    //--------------------static method-----start

    /** GET */
    public static void get(String url, IRequestCallback iRequestCallback) {
        getInstance().get(NET_TAG(), url, iRequestCallback);
    }

    /** GET-Silence */
    public static void getSilence(String url, IRequestCallback iRequestCallback) {
        getInstance().getSilent(NET_TAG(), url, iRequestCallback);
    }

    /** POST */
    public static void post(String url, Map<String, String> paramMap, IRequestCallback iRequestCallback) {
        getInstance().post(NET_TAG(), url, paramMap, iRequestCallback);
    }

    /** POST-Silence */
    public static void postSilence(String url, Map<String, String> paramMap, IRequestCallback iRequestCallback) {
        getInstance().postSilence(NET_TAG(), url, paramMap, iRequestCallback);
    }

    /** download */
    public static void download(String url, String filePath, IRequestCallback iRequestCallback) {
        getInstance().download(NET_TAG(), url, filePath, iRequestCallback);
    }

    /** download-Silence */
    public static void downloadSilence(String url, String filePath, IRequestCallback iRequestCallback) {
        getInstance().downloadSilence(NET_TAG(), url, filePath, iRequestCallback);
    }

    /** upload */
    public static void upload(String url, Map<String, String> paramMap, Map<String, File> fileMap, IRequestCallback iRequestCallback) {
        getInstance().upload(NET_TAG(), url, paramMap, fileMap, iRequestCallback);
    }

    /** upload-Silence */
    public static void uploadSilence(String url, Map<String, String> paramMap, Map<String, File> fileMap, IRequestCallback iRequestCallback) {
        getInstance().uploadSilence(NET_TAG(), url, paramMap, fileMap, iRequestCallback);
    }

    /** loadImg https url */
    public static void loadImg(String url, ImageView view, int placeholderResId) {
        OkHttpClientWrap.getInstance().loadImg(url, view, placeholderResId, NET_TAG());
    }
    //--------------------static method-----end

    /**
     * 统一httpGet请求入口
     *
     * @param tag              请求类型标记,用于用户处理网络请求比如取消请求
     * @param url              请求完整地址url
     * @param iRequestCallback 回调接口
     */
    public void get(String tag, String url, IRequestCallback iRequestCallback) {
        if (filterBeforeRequest(tag, url, iRequestCallback)) {
            return;
        }
        if (null == mCallbackMap.get(tag)) {
            mCallbackMap.put(tag, iRequestCallback);
        }
        LogUtil.e(TAG, "get : tag = " + tag + " url : " + url);
        OkHttpClientWrap.getInstance().get(url, new NetResponseCallback(tag), tag);
    }

    /**
     * 静默访问，未作任何处理,需要自己做一些处理动作，比如网络是否可用、数据异常等等,多用于不考虑服务器返回结果的情况下
     *
     * @param tag              请求标记
     * @param url              请求地址
     * @param iRequestCallback 请求回调
     */
    public void getSilent(String tag, String url, IRequestCallback iRequestCallback) {
        if (filterBeforeRequestSilence(tag, url, iRequestCallback)) {
            return;
        }
        if (null == mCallbackMap.get(tag)) {
            mCallbackMap.put(tag, iRequestCallback);
        }
        LogUtil.e(TAG, "getSilent : tag = " + tag + " url : " + url);
        OkHttpClientWrap.getInstance().get(url, new SilenceNetResponseCallback(tag), tag);
    }

    /**
     * 统一httpPost请求入口
     *
     * @param tag              请求类型标记
     * @param url              请求完整地址url
     * @param iRequestCallback 回调接口
     * @param paramMap         参数集合
     */
    public void post(String tag, String url, Map<String, String> paramMap, IRequestCallback iRequestCallback) {
        if (filterBeforeRequest(tag, url, iRequestCallback)) {
            return;
        }
        if (null == mCallbackMap.get(tag)) {
            mCallbackMap.put(tag, iRequestCallback);
        }
        LogUtil.e(TAG, "post : tag = " + tag + " url : " + url);
        OkHttpClientWrap.getInstance().post(url, paramMap, new NetResponseCallback(tag), tag);
    }


    /**
     * POST静默访问，未作任何处理,需要自己做一些处理动作，比如网络是否可用、数据异常等等,多用于不考虑服务器返回结果的情况下
     *
     * @param tag              请求标记
     * @param url              请求地址
     * @param iRequestCallback 请求回调
     */
    public void postSilence(String tag, String url, Map<String, String> paramMap, IRequestCallback iRequestCallback) {
        if (filterBeforeRequestSilence(tag, url, iRequestCallback)) {
            return;
        }
        if (null == mCallbackMap.get(tag)) {
            mCallbackMap.put(tag, iRequestCallback);
        }
        LogUtil.e(TAG, "postSilence : tag = " + tag + " url : " + url);
        OkHttpClientWrap.getInstance().post(url, paramMap, new SilenceNetResponseCallback(tag), tag);
    }

    /**
     * 统一download请求入口
     *
     * @param tag              请求标记
     * @param url              请求地址
     * @param filePath         下载完成后文件保存的文件路径
     * @param iRequestCallback 请求回调
     */
    public void download(String tag, String url, String filePath, IRequestCallback iRequestCallback) {
        if (filterBeforeRequest(tag, url, iRequestCallback)) {
            return;
        }
        if (null == mCallbackMap.get(tag)) {
            mCallbackMap.put(tag, iRequestCallback);
        }
        LogUtil.e(TAG, "download : tag = " + tag + " url : " + url);
        OkHttpClientWrap.getInstance().download(url, filePath, new FileDownloadCallback(tag), tag);
    }

    /**
     * 静默 统一download请求入口
     *
     * @param tag              请求标记
     * @param url              请求地址
     * @param filePath         下载完成后文件保存的文件路径
     * @param iRequestCallback 请求回调
     */
    public void downloadSilence(String tag, String url, String filePath, IRequestCallback iRequestCallback) {
        if (filterBeforeRequestSilence(tag, url, iRequestCallback)) {
            return;
        }
        if (null == mCallbackMap.get(tag)) {
            mCallbackMap.put(tag, iRequestCallback);
        }
        LogUtil.e(TAG, "downloadSilence : tag = " + tag + " url : " + url);
        OkHttpClientWrap.getInstance().download(url, filePath, new FileDownloadCallback(tag), tag);
    }

    /**
     * 统一upload请求入口
     *
     * @param tag              请求标记
     * @param url              请求地址
     * @param paramMap         上传携带的请求参数
     * @param fileMap          上传文件集合
     * @param iRequestCallback 请求回调
     */
    public void upload(String tag, String url, Map<String, String> paramMap, Map<String, File> fileMap, IRequestCallback iRequestCallback) {
        if (filterBeforeRequest(tag, url, iRequestCallback)) {
            return;
        }
        if (null == mCallbackMap.get(tag)) {
            mCallbackMap.put(tag, iRequestCallback);
        }
        LogUtil.e(TAG, "upload : tag = " + tag + " url : " + url);
        OkHttpClientWrap.getInstance().upload(url, paramMap, fileMap, new UploadResponseCallback(tag), tag);
    }

    /**
     * 静默 统一upload请求入口
     *
     * @param tag              请求标记
     * @param url              请求地址
     * @param paramMap         上传携带的请求参数
     * @param fileMap          上传文件集合
     * @param iRequestCallback 请求回调
     */
    public void uploadSilence(String tag, String url, Map<String, String> paramMap, Map<String, File> fileMap, IRequestCallback iRequestCallback) {
        if (filterBeforeRequestSilence(tag, url, iRequestCallback)) {
            return;
        }
        if (null == mCallbackMap.get(tag)) {
            mCallbackMap.put(tag, iRequestCallback);
        }
        LogUtil.e(TAG, "uploadSilence : tag = " + tag + " url : " + url);
        OkHttpClientWrap.getInstance().upload(url, paramMap, fileMap, new UploadResponseCallback(tag), tag);
    }


    /** 网络请求拦截回调  ,UI Thread */
    private class NetResponseCallback extends IRequestCallback<String> {
        private String tag;

        public NetResponseCallback(String tag) {
            this.tag = tag;
        }

        @Override
        public void onStart() {
            LogUtil.e(TAG, "NetResponseCallback----> onStart");
            if (mCallbackMap.get(tag) != null) {
                mCallbackMap.get(tag).onStart();
            }
        }

        @Override
        public void onSuccess(String response) {
            LogUtil.e(TAG, "NetResponseCallback----> onSuccess : " + response);
            if (null == response) {
                this.onFailure(new RuntimeException("onSuccess : response is null."));
                return;
            }

            IRequestCallback callBack = mCallbackMap.get(tag);
            if (callBack == null) {
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(response);
                int status = jsonObject.optInt("status", -1);
                if (status == 1202) {
                    reLogin();
                    this.onFailure(new RuntimeException("token is invalid,please relogin!"));
                    return;
                }
                if (callBack.mClassType == String.class) {
                    callBack.onSuccess(response);
                } else {
                    Object parseObject = JsonHelper.parseObjectType(response, callBack.mClassType);
                    if (parseObject == null) {
                        callBack.onFailure(new RuntimeException("onSuccess : parseObject is null."));
                    } else {
                        callBack.onSuccess(parseObject);
                    }
                }
            } catch (JSONException e) {
                if (callBack.mClassType == String.class) {
                    callBack.onSuccess(response);
                } else {
                    callBack.onFailure(e);
                }
            }
            removeRequestCallback(tag);
        }

        @Override
        public void onFailure(Exception e) {
            LogUtil.e(TAG, "NetResponseCallback----> onError " + e.getMessage());
            if (mCallbackMap.get(tag) != null) {
                mCallbackMap.get(tag).onFailure(e);
                removeRequestCallback(tag);
            }
        }
    }

    private void reLogin() {
        Intent intent = new Intent(UIUtil.getContext(), LoginActivity.class);
        if (Configs.TOKEN_TEMP.equals(GlobalUtils.token)) {//访客模式操作数据登录
            UIUtil.showToastShort("请注册登录后进行操作体验");
            intent.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA, LoginActivity.FROM_TEMP_TOKEN_LOGIN);
        } else {//登录用户token失效或是异地登录 需要重新登录
            UIUtil.showToastShort("请重新登录你的账号");
            Util.clearLoginInfo();
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        UIUtil.getContext().startActivity(intent);
    }

    /** 静默网络请求拦截回调  ,UI Thread */
    private class SilenceNetResponseCallback extends IRequestCallback<String> {
        private String tag;

        public SilenceNetResponseCallback(String tag) {
            this.tag = tag;
        }

        @Override
        public void onStart() {
            LogUtil.e(TAG, "SilenceNetResponseCallback----> onStart");
            if (mCallbackMap.get(tag) != null) {
                mCallbackMap.get(tag).onStart();
            }
        }

        @Override
        public void onSuccess(String response) {
            LogUtil.e(TAG, "SilenceNetResponseCallback----> onSuccess : " + response);
            IRequestCallback callBack = mCallbackMap.get(tag);
            if (callBack == null) {
                return;
            }
            if (callBack.mClassType == String.class) {
                callBack.onSuccess(response);
            } else {
                Object parseObject = JsonHelper.parseObjectType(response, callBack.mClassType);
                if (parseObject == null) {
                    callBack.onFailure(new RuntimeException("SilenceNetResponseCallback : onSuccess : parseObject is null."));
                } else {
                    callBack.onSuccess(parseObject);
                }
            }
            removeRequestCallback(tag);
        }

        @Override
        public void onFailure(Exception e) {
            LogUtil.e(TAG, "SilenceNetResponseCallback----> onError " + e.getMessage());
            if (mCallbackMap.get(tag) != null) {
                mCallbackMap.get(tag).onFailure(e);
                removeRequestCallback(tag);
            }
        }
    }


    /** 文件下载拦截回调  ,UI Thread */
    private class FileDownloadCallback extends IRequestCallback<File> {
        private String tag;

        public FileDownloadCallback(String tag) {
            this.tag = tag;
        }

        @Override
        public void onStart() {
            LogUtil.e(TAG, "FileDownloadCallback----> onStart");
            if (mCallbackMap.get(tag) != null) {
                mCallbackMap.get(tag).onStart();
            }
        }

        @Override
        public void onProgressUpdate(long writedSize, long totalSize, boolean completed) {
            IRequestCallback callback = mCallbackMap.get(tag);
            if (callback != null) {
                callback.onProgressUpdate(writedSize, totalSize, completed);
            }
        }

        @Override
        public void onSuccess(File file) {
            if (null == file) {
                this.onFailure(new RuntimeException("onSuccess : file is null."));
                return;
            }

            IRequestCallback callback = mCallbackMap.get(tag);
            if (callback == null) {
                return;
            }
            LogUtil.e(TAG, "FileDownloadCallback----> onSuccess: file=" + file.getAbsolutePath() + " , size=" + file.length());
            callback.onSuccess(file);
            removeRequestCallback(tag);
        }

        @Override
        public void onFailure(Exception e) {
            LogUtil.e(TAG, "FileDownloadCallback----> onError " + e.getMessage());
            if (mCallbackMap.get(tag) != null) {
                mCallbackMap.get(tag).onFailure(e);
                removeRequestCallback(tag);
            }
        }
    }

    /** 上传文件拦截回调  ,UI Thread */
    private class UploadResponseCallback extends IRequestCallback<String> {
        private String tag;

        public UploadResponseCallback(String tag) {
            this.tag = tag;
        }

        @Override
        public void onStart() {
            LogUtil.e(TAG, "UploadResponseCallback----> onStart");
            if (mCallbackMap.get(tag) != null) {
                mCallbackMap.get(tag).onStart();
            }
        }

        @Override
        public void onProgressUpdate(long writedSize, long totalSize, boolean completed) {
            IRequestCallback callBack = mCallbackMap.get(tag);
            if (callBack == null) {
                return;
            }
            callBack.onProgressUpdate(writedSize, totalSize, completed);
        }

        @Override
        public void onSuccess(String response) {
            LogUtil.e(TAG, "UploadResponseCallback----> onSuccess : " + response);
            IRequestCallback callBack = mCallbackMap.get(tag);
            if (callBack == null) {
                return;
            }
            if (callBack.mClassType == String.class) {
                callBack.onSuccess(response);
            } else {
                Object parseObject = JsonHelper.parseObjectType(response, callBack.mClassType);
                if (parseObject == null) {
                    callBack.onFailure(new RuntimeException("UploadResponseCallback : onSuccess : parseObject is null."));
                } else {
                    callBack.onSuccess(parseObject);
                }
            }
            removeRequestCallback(tag);
        }

        @Override
        public void onFailure(Exception e) {
            LogUtil.e(TAG, "UploadResponseCallback----> onError " + e.getMessage());
            if (mCallbackMap.get(tag) != null) {
                mCallbackMap.get(tag).onFailure(e);
                removeRequestCallback(tag);
            }
        }
    }

    /** 请求前进行过滤检测处理 */
    private boolean filterBeforeRequest(String tag, String url, IRequestCallback iRequestCallback) {
        if (TextUtils.isEmpty(tag)) {
            UIUtil.showToastShort("网络请求标记不能为空");
            iRequestCallback.onFailure(null);
            return true;
        }
        if (!NetworkUtils.isNetworkConnected(UIUtil.getContext())) {
            UIUtil.showToastShort("网络似乎有问题");
            iRequestCallback.onFailure(null);
            return true;
        }
        if (TextUtils.isEmpty(url)) {
            UIUtil.showToastShort("请求地址不能为空");
            iRequestCallback.onFailure(null);
            return true;
        }
        if (url.startsWith("null")) {
            UIUtil.showToastShort("未获取到动态域名");
            iRequestCallback.onFailure(null);
            return true;
        }
        return false;
    }

    /** 请求前进行过滤检测处理(for 静默) */
    private boolean filterBeforeRequestSilence(String tag, String url, IRequestCallback iRequestCallback) {
        if (!NetworkUtils.isNetworkConnected(UIUtil.getContext())) {
            iRequestCallback.onFailure(null);
            return true;
        }
        if (TextUtils.isEmpty(url)) {
            iRequestCallback.onFailure(null);
            return true;
        }
        if (url.startsWith("null")) {
            iRequestCallback.onFailure(null);
            return true;
        }
        return false;
    }


    /** 移除指定类型的回调接口 */
    public void removeRequestCallback(String tag) {
        IRequestCallback iRequestCallBack = mCallbackMap.get(tag);
        if (null != iRequestCallBack) {
            mCallbackMap.remove(tag);
            OkHttpClientWrap.getInstance().cancelTag(tag);
        }
    }

    private NetHelper() {
        mCallbackMap = new ConcurrentHashMap<>();
    }

    /** 产生唯一标记tag */
    private static String NET_TAG() {
        return String.valueOf(System.currentTimeMillis());
    }

}
