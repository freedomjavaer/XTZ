package com.ypwl.xiaotouzi.im.push;

import com.ypwl.xiaotouzi.im.YConfigs;
import com.ypwl.xiaotouzi.im.YEvent;
import com.ypwl.xiaotouzi.im.socketio.client.IO;
import com.ypwl.xiaotouzi.im.socketio.client.Socket;
import com.ypwl.xiaotouzi.utils.LogUtil;

import java.net.URISyntaxException;

import io.socket.emitter.Emitter;

/**
 * function:socket实例管理类(推送).
 *
 * <p>Created by lzj on 2016/3/17.</p>
 */
public class YSocketPushHelper {
    private String TAG = "ymessage:" + YSocketPushHelper.class.getSimpleName();
    private static volatile YSocketPushHelper instance = new YSocketPushHelper();
    private Socket mSocket = null;

    public static YSocketPushHelper getInstance() {
        return instance;
    }

    private YSocketPushHelper() {
    }

    /**
     * 初始化socket,连入服务端并处于长连接状态
     */
    public synchronized Socket initScoket() {
        try {
            if (mSocket != null) {
                offListener();
            }
            IO.Options options = new IO.Options();
            options.forceNew = true;
            mSocket = IO.socket(YConfigs.SOCKET_ADDRESS, options);
            mSocket.setNsp("/");
            onListener();
            mSocket.connect();
            LogUtil.e(TAG, "initScoket socket start to connect...");
        } catch (URISyntaxException e) {
            LogUtil.e(TAG, "initScoket URISyntaxException : " + e.getMessage());
        }
        return mSocket;
    }


    /**
     * 断开连接
     */
    public synchronized void disconnect() {
        if (mSocket != null) {
            offListener();
            mSocket.disconnect();
        }
    }

    /**
     * 开启事件监听器
     */
    private void onListener() {
        mSocket.on(YEvent.EVENT_CONNECT_ERROR, onError);//连接错误
        mSocket.on(YEvent.EVENT_CONNECT_TIMEOUT, onError);//连接超时
        mSocket.on(YEvent.EVENT_CONNECT, onConnectOK);//连接成功
        YPushAgent.getInstance().registEvent(mSocket);
    }

    /**
     * 关闭事件监听器
     */
    private void offListener() {
        mSocket.off(YEvent.EVENT_CONNECT_ERROR, onError);//连接错误
        mSocket.off(YEvent.EVENT_CONNECT_TIMEOUT, onError);//连接超时
        mSocket.off(YEvent.EVENT_CONNECT, onConnectOK);//连接成功
        YPushAgent.getInstance().unregistEvent(mSocket);
    }

    /**
     * 连接成功
     */
    private Emitter.Listener onConnectOK = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtil.e(TAG, "onConnectOK...");
            YPushAgent.getInstance().onConnected();
        }
    };

    /**
     * 连接出错
     */
    private Emitter.Listener onError = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            LogUtil.e(TAG, "onError...");
            YPushAgent.getInstance().onConnectFailure();
        }
    };
}
