package com.ypwl.xiaotouzi.base;

import android.app.Service;
import android.content.Intent;

import com.ypwl.xiaotouzi.utils.LogUtil;


/**
 * android 系统中的四大组件之一Service基类<br/>
 * Created by lzj on 2015/11/2.
 */
public abstract class BaseService extends Service {

    /** 日志输出标志 **/
    protected final String TAG = this.getClass().getSimpleName();

    @Override
    public void onCreate() {
        LogUtil.d(TAG, TAG + "-->onCreate()");
        super.onCreate();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onStart(Intent intent, int startId) {
        LogUtil.d(TAG, TAG + "-->onStart()");
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        LogUtil.d(TAG, TAG + "-->onDestroy()");
        super.onDestroy();
    }

}
