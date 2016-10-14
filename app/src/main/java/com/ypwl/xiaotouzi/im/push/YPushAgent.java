package com.ypwl.xiaotouzi.im.push;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.ypwl.xiaotouzi.im.ISocketConnectCallback;
import com.ypwl.xiaotouzi.im.YConfigs;
import com.ypwl.xiaotouzi.im.YConst;
import com.ypwl.xiaotouzi.im.YEvent;
import com.ypwl.xiaotouzi.im.YMessage;
import com.ypwl.xiaotouzi.im.socketio.client.Socket;
import com.ypwl.xiaotouzi.utils.LogUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicBoolean;

import io.socket.emitter.Emitter;

/**
 * function ： 长连接推送API入口.
 * <p/>
 * Created by lzj on 2016/1/31.
 */
public class YPushAgent implements ISocketConnectCallback {
    private static final String TAG = "ymessage:" + YPushAgent.class.getSimpleName();
    private static Context mContext = UIUtil.getContext();
    private static final String NSP = "/";
    private static volatile YPushAgent instance = new YPushAgent();
    private Socket mSocket;
    private SharedPreferences mSp;
    private String mPushToken;
    private AtomicBoolean mHasReconnect;

    public static YPushAgent getInstance() {
        return instance;
    }

    private YPushAgent() {
        mHasReconnect = new AtomicBoolean(false);
        mSp = mContext.getSharedPreferences(YConfigs.SP_FILENAME, Context.MODE_PRIVATE);
    }

    /**
     * 设置pushtoken并开启推送
     */
    public void setPushTokenAndEnable(String pushToken) {
        LogUtil.e(TAG, "setPushTokenAndEnable : " + pushToken);
        mPushToken = pushToken;
        YSocketPushHelper.getInstance().initScoket();
    }

    public void disconnect() {
        YSocketPushHelper.getInstance().disconnect();
    }

    public boolean connected() {
        if (mSocket != null) {
            return mSocket.connected();
        }
        return false;
    }

    @Override
    public void registEvent(Socket socket) {
        mSocket = socket;
        socket.on(YEvent.EVENT_NEW_MESSAGE, onNewPushMessage);//新消息-帖子回复提醒
        socket.on(YEvent.EVENT_REMOTE_LOGIN, onRemoteLogin);//新消息-异地登录
    }

    @Override
    public void unregistEvent(Socket socket) {
        socket.off(YEvent.EVENT_NEW_MESSAGE, onNewPushMessage);
        socket.off(YEvent.EVENT_REMOTE_LOGIN, onRemoteLogin);
    }

    @Override
    public void onConnected() {
//        if (mHasReconnect.get()) {//强制上传,出现连接断开重新连接上之后
//            mPushToken = mSp.getString(YConst.KEY_PUSHTOKEN, null);
//        } else {
//            if (mPushToken.equals(mSp.getString(YConst.KEY_PUSHTOKEN, ""))) {
//                LogUtil.e(TAG, "onConnected()...has upload same pushToken no more update!!!");
//                return;
//            }
//        }
        LogUtil.e(TAG, "onConnected()...uptoken=" + mPushToken);
        if (mPushToken == null || mPushToken.length() == 0) {
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", mPushToken);
            LogUtil.e(TAG, "uploadPushToken : uploadTokenStr=" + jsonObject.toString());
            mSocket.setNsp(NSP);
            mSocket.emit(YEvent.EVENT_UP_TOKEN, jsonObject);
            mSp.edit().putString(YConst.KEY_PUSHTOKEN, mPushToken).apply();
            mHasReconnect.getAndSet(false);
        } catch (JSONException e) {
            LogUtil.e(TAG, "uploadPushToken : Exception=" + e.getMessage());
        }
    }

    @Override
    public void onConnectFailure() {
        LogUtil.e(TAG, "onConnectFailure()...");
        mHasReconnect.getAndSet(true);
    }

    /**
     * 新消息-帖子回复提醒
     */
    private Emitter.Listener onNewPushMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            try {
                JSONObject json = (JSONObject) args[0];
                LogUtil.e(TAG, "onNewPushMessage : json=" + json.toString());
                String msg = json.getString(YConst.JSONKEY_TYPE);
                if (msg != null && msg.length() > 0) {
                    YMessage yMessage = new YMessage();
                    yMessage.setType(YMessage.TYPE_PUSH_MESSAGE);
                    yMessage.setMessage(msg);
                    distributeMessage(yMessage);
                }
            } catch (Exception e) {
                LogUtil.e(TAG, "onNewPushMessage : Exception=" + e.getMessage());
                e.printStackTrace();
            }
        }
    };

    /**
     * 新消息-异地登录
     */
    private Emitter.Listener onRemoteLogin = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                JSONObject json = (JSONObject) args[0];
                LogUtil.e(TAG, "remote_login message : json=" + json.toString());
                YMessage yMessage = new YMessage();
                yMessage.setType(YMessage.TYPE_REMOTE_LOGIN);
                distributeMessage(yMessage);
            } catch (Exception e) {
                LogUtil.e(TAG, "message : Exception=" + e.getMessage());
                e.printStackTrace();
            }
        }
    };

    /**
     * 广播分发消息
     */
    private void distributeMessage(YMessage yMessage) {
        Intent recevier = new Intent();
        recevier.setAction(YConst.ACTION_INTENT_RECEIVE_MESSAGE);
        recevier.addCategory(YConst.CATEGORY_INTENT_RECEIVE_MESSAGE);
        recevier.putExtra(YConst.KEY_PUSH_MSG, yMessage);
        mContext.sendBroadcast(recevier);
    }
}
