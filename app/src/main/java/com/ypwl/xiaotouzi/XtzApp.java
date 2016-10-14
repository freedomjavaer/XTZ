package com.ypwl.xiaotouzi;

import android.app.Activity;
import android.app.Application;
import android.os.Looper;

import com.umeng.message.PushAgent;
import com.ypwl.xiaotouzi.common.Configs;
import com.ypwl.xiaotouzi.common.DeviceInfo;
import com.ypwl.xiaotouzi.handler.CrashHandler;
import com.ypwl.xiaotouzi.handler.PushMsgNotificationClickHandler;
import com.ypwl.xiaotouzi.handler.PushMsgNotificationHandler;
import com.ypwl.xiaotouzi.manager.ShareAuthManager;
import com.ypwl.xiaotouzi.manager.net.OkHttpClientWrap;
import com.ypwl.xiaotouzi.utils.StorageUtil;

import java.lang.ref.WeakReference;
import java.util.Stack;

/**
 * function : 应用程序入口.<br/>
 * Created by lzj on 2015/11/2.
 */
@SuppressWarnings("unused")
public class XtzApp extends Application {
    private static final String TAG = XtzApp.class.getSimpleName();
    private static XtzApp mContext;
    private static android.os.Handler mMainThreadHandler;
    private static Looper mMainThreadLooper;
    private static Thread mMainThread;
    private static int mMainThreadId;

    /*** 寄存整个应用Activity **/
    private final Stack<WeakReference<Activity>> mActivitys = new Stack<>();

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        mMainThreadHandler = new android.os.Handler();
        mMainThreadLooper = getMainLooper();
        mMainThread = Thread.currentThread();
        mMainThreadId = android.os.Process.myTid();

        ShareAuthManager.init();//初始化umeng分享及授权相关

        //友盟推送服务
        PushAgent.getInstance(mContext).setDebugMode(Configs.DEBUG);
        PushAgent.getInstance(mContext).setMessageHandler(new PushMsgNotificationHandler());//处理推送消息
        //PushAgent.getInstance(mContext).enable();
        PushAgent.getInstance(mContext).setNotificationClickHandler(new PushMsgNotificationClickHandler());//通知点击事件

        CrashHandler.init(mContext);// 异常捕获初始化


        if (!Configs.DEBUG) {
            OkHttpClientWrap.getInstance().initSSLAccess();//SSL双向验证
        }
        new LoadInitDataTask().execute((Void) null);// 异步加载初始数据
    }

    /** 使用异步加载初始配置数据 */
    private class LoadInitDataTask extends android.os.AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            // 在这里加载一些不及时使用或者耗时的初始化数据.比如初始化UIL、各类SDK等等
            DeviceInfo.init(mContext);// 初始化设备信息
            StorageUtil.clearTempDir(mContext);//清理临时文件夹
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
        }
    }

    /**
     * 获取全局上下文
     *
     * @return the mContext
     */
    public static XtzApp getApplication() {
        return mContext;
    }

    /**
     * 获取主线程Handler
     *
     * @return the mMainThreadHandler
     */
    public static android.os.Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    /**
     * 获取主线程轮询器
     *
     * @return the mMainThreadLooper
     */
    public static Looper getMainThreadLooper() {
        return mMainThreadLooper;
    }

    /**
     * 获取主线程
     *
     * @return the mMainThread
     */
    public static Thread getMainThread() {
        return mMainThread;
    }

    /**
     * 获取主线程ID
     *
     * @return the mMainThreadId
     */
    public static int getMainThreadId() {
        return mMainThreadId;
    }

    /************ Application中存放的Activity操作（压栈/出栈）API（开始） ***********************/
    /**
     * 将Activity压入Application栈
     *
     * @param task 将要压入栈的Activity对象
     */
    public void pushTask(WeakReference<Activity> task) {
        mActivitys.push(task);
    }

    /**
     * 将传入的Activity对象从栈中移除
     *
     * @param task 将要移除栈的Activity对象
     */
    public void removeTask(WeakReference<Activity> task) {
        mActivitys.remove(task);
    }

    /**
     * 获取栈顶的activity
     */
    public Activity getTopActivity() {
        int size = mActivitys.size();
        return mActivitys.get(size - 1).get();
    }

    /**
     * 关闭某个activity
     *
     * @param activityCls 指定activity的类 eg：MainActivity.class
     * @deprecated 在同个业务流程中使用，不要随意关闭其他业务界面
     */
    @Deprecated
    public void finishActivity(Class<? extends Activity> activityCls) {
        int end = mActivitys.size();
        for (int i = end - 1; i >= 0; i--) {
            Activity cacheActivity = mActivitys.get(i).get();
            if (cacheActivity.getClass().getSimpleName().equals(activityCls.getSimpleName())
                    && !cacheActivity.isFinishing()) {
                cacheActivity.finish();
                removeTask(i);
            }
        }
    }

    /**
     * 根据指定位置从栈中移除Activity
     *
     * @param taskIndex Activity栈索引
     */
    public void removeTask(int taskIndex) {
        if (mActivitys.size() > taskIndex)
            mActivitys.remove(taskIndex);
    }

    /** 将栈中Activity移除至栈顶 */
    public void removeToTop() {
        int end = mActivitys.size();
        int start = 1;
        for (int i = end - 1; i >= start; i--) {
            if (!mActivitys.get(i).get().isFinishing()) {
                mActivitys.get(i).get().finish();
            }
        }
    }

    /** 移除全部（用于整个应用退出） */
    public void removeAll() {
        for (WeakReference<Activity> task : mActivitys) {
            if (!task.get().isFinishing()) {
                task.get().finish();
            }
        }
    }

    /*************** Application中存放的Activity操作（压栈/出栈）API（结束） ************************/
}
