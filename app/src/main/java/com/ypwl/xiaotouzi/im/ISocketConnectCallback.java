package com.ypwl.xiaotouzi.im;

import com.ypwl.xiaotouzi.im.socketio.client.Socket;

/**
 * function:Socket连接回调接口
 *
 * <br/>
 * Created by lzj on 2016/4/5.
 */
public interface ISocketConnectCallback {
    /**
     * 注册监听
     */
    void registEvent(Socket socket);

    /**
     * 反注册监听
     */
    void unregistEvent(Socket socket);

    /**
     * 连接成功上的
     */
    void onConnected();

    /**
     * 连接失败
     */
    void onConnectFailure();
}
