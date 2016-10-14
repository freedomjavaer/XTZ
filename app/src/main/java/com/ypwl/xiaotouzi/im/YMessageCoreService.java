package com.ypwl.xiaotouzi.im;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


/**
 * function : 用于开启较长生命以及较高优先级的socket初始化服务(目前仅使用于长连接推送服务)--保留服务
 * <p/>
 * Created by lzj on 2015/1/31.
 */
public class YMessageCoreService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onStart(Intent intent, int startId) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
