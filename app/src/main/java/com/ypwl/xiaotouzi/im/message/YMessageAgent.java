package com.ypwl.xiaotouzi.im.message;

import android.support.annotation.NonNull;

import com.ypwl.xiaotouzi.bean.LoginBean;
import com.ypwl.xiaotouzi.bean.OnlineMsgBean;
import com.ypwl.xiaotouzi.bean.OnlineSendMsgBean;
import com.ypwl.xiaotouzi.im.ISocketConnectCallback;
import com.ypwl.xiaotouzi.im.YEvent;
import com.ypwl.xiaotouzi.im.socketio.client.Socket;
import com.ypwl.xiaotouzi.manager.JsonHelper;
import com.ypwl.xiaotouzi.utils.LogUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.socket.emitter.Emitter;

/**
 * function:socket管理类(用于即时聊天).
 *
 * <p>Created by lzj on 2016/3/21.</p>
 */
public class YMessageAgent implements ISocketConnectCallback {
    private static final String TAG = "ymessage:" + YMessageAgent.class.getSimpleName();
    private static volatile YMessageAgent instance = new YMessageAgent();
    private static volatile String NSP;
    private Socket mSocket;

    public static YMessageAgent getInstance() {
        return instance;
    }

    private YMessageAgent() {
    }

    /**
     * 设置名称空间并开启socket服务
     */
    public void setNspAndEnable(@NonNull String nsp) {
        LogUtil.e(TAG, "setNspAndEnable : " + nsp);
        NSP = nsp;
        YSocketMessageHelper.getInstance().initScoket(nsp);
    }

    public void disconnect() {
        YSocketMessageHelper.getInstance().disconnect();
    }

    @Override
    public void registEvent(Socket socket) {
        mSocket = socket;
        mSocket.on(YEvent.EVENT_NSP_SUCCESS, onNspSuccess);//上传个人信息进行登录成功反馈事件
        mSocket.on(YEvent.EVENT_SEND_SUCCESS, onSendMsgSuccess);//客户端发送消息成功通知
        mSocket.on(YEvent.EVENT_RECEIVE_MSG, onReceiveMsg);//服务端下发消息
        mSocket.on(YEvent.EVENT_ALL_MSG, onGetAllMsg);//查询所有的消息返回
        mSocket.on(YEvent.EVENT_MY_MSG, onGetMyMsg);//查询我的发言所有的消息返回
        mSocket.on(YEvent.EVENT_TO_ME, onGetAmeMsg);//查询@我所有的消息返回
    }

    @Override
    public void unregistEvent(Socket socket) {
        mSocket.off(YEvent.EVENT_NSP_SUCCESS, onNspSuccess);
        mSocket.off(YEvent.EVENT_SEND_SUCCESS, onSendMsgSuccess);
        mSocket.off(YEvent.EVENT_RECEIVE_MSG, onReceiveMsg);
        mSocket.off(YEvent.EVENT_ALL_MSG, onGetAllMsg);
        mSocket.off(YEvent.EVENT_MY_MSG, onGetMyMsg);
        mSocket.off(YEvent.EVENT_TO_ME, onGetAmeMsg);
    }

    @Override
    public void onConnected() {
        LogUtil.e(TAG, " ------>onConnectOK");
        if (NSP == null) {
            LogUtil.e(TAG, " ------>onConnectOK ... NSP is null");
            return;
        }
        try {
            LoginBean loginBean = Util.legalLogin();
            if (loginBean != null) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("token", loginBean.getToken());
                jsonObject.put("username", loginBean.getNickname());
                jsonObject.put("headimage", loginBean.getAvatarUrl());
                LogUtil.e(TAG, "onConnected : emit = " + jsonObject.toString());
                sendEventMsg(YEvent.EVENT_UP_USERINFO, jsonObject);
            } else {
                throw new JSONException("user not login!!!");
            }
        } catch (JSONException e) {
            LogUtil.e(TAG, "onConnected : Exception=" + e.getMessage());
            UIUtil.post(new Runnable() {
                @Override
                public void run() {
                    if (iMessageCallBack != null) {
                        iMessageCallBack.loginChatRoomFailed();
                    }
                }
            });
        }
    }

    @Override
    public void onConnectFailure() {
        LogUtil.e(TAG, "onConnectFailure()...");
        UIUtil.post(new Runnable() {
            @Override
            public void run() {
                if (iMessageCallBack != null) {
                    iMessageCallBack.connectError();
                }
            }
        });
    }

    /**
     * 加入名称空间成功(即登录聊天室成功)
     */
    private Emitter.Listener onNspSuccess = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            LogUtil.e(TAG, " ------>onNspSuccess");
            UIUtil.post(new Runnable() {
                @Override
                public void run() {
                    if (iMessageCallBack != null) {
                        iMessageCallBack.loginChatroomSuccess();
                    }
                }
            });
        }
    };

    /**
     * 发送消息
     */
    public void sendMsg(@NonNull OnlineSendMsgBean sendMsgBean) {
        try {
            JSONObject msgObj = new JSONObject();
            msgObj.put("message", sendMsgBean.getMessage());
            msgObj.put("sendtime", sendMsgBean.getSendtime());
            List<OnlineSendMsgBean.ToidEntity> toid = sendMsgBean.getToid();
            if (toid == null || toid.size() == 0) {
                msgObj.put("toid", new JSONArray());
            } else {
                JSONArray toidArray = new JSONArray();
                for (OnlineSendMsgBean.ToidEntity toidEntity : toid) {
                    JSONObject toidObj = new JSONObject();
                    toidObj.put("userid", toidEntity.getUserid());
                    toidObj.put("username", toidEntity.getUsername());
                    toidArray.put(toidObj);
                }
                msgObj.put("toid", toidArray);
            }
            LogUtil.e(TAG, "sendMsg : msg=" + msgObj.toString());
            sendEventMsg(YEvent.EVENT_SEND_MSG, msgObj);
        } catch (JSONException e) {
            LogUtil.e(TAG, "sendMsg : Exception=" + e.getMessage());
        }
    }

    /**
     * 发送消息成功通知
     */
    private Emitter.Listener onSendMsgSuccess = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            LogUtil.e(TAG, " ------>onSendMsgSuccess");
        }
    };

    /**
     * 请求分类消息
     */
    public void requestCategoryMsg(int categoryId, long sendTime) {
        LogUtil.e(TAG, "type:" + categoryId);
        try {
            JSONObject msgObj = new JSONObject();
            msgObj.put("type", categoryId);
            msgObj.put("sendtime", sendTime);
            LogUtil.e(TAG, "requestCategoryMsg : msg=" + msgObj.toString());
            sendEventMsg(YEvent.EVENT_GET_MSG, msgObj);
        } catch (JSONException e) {
            LogUtil.e(TAG, "requestCategoryMsg : Exception=" + e.getMessage());
            e.printStackTrace();
        }
    }

    /** 发送事件消息到服务端 */
    private void sendEventMsg(final String event, final Object... args) {
        if (NSP == null || !mSocket.connected()) {
            LogUtil.e(TAG, "NSP=null or mSocket disconnected!!!");
            return;
        }
        mSocket.setNsp(NSP);
        mSocket.emit(event, args);
    }

    /** 消息类型--接收服务端下发消息 */
    private static final int MSG_TYPE_onReceiveMsg = 0;
    /** 消息类型--获取所有消息 */
    private static final int MSG_TYPE_onGetAllMsg = 1;
    /** 消息类型--获取我的发言 */
    private static final int MSG_TYPE_onGetMyMsg = 2;
    /** 消息类型--获取@我的消息 */
    private static final int MSG_TYPE_onGetAmeMsg = 3;

    /**
     * 接收到服务端的下发消息
     */
    private Emitter.Listener onReceiveMsg = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            LogUtil.e(TAG, " ------>onReceiveMsg");
            deliverMsg(args[0], MSG_TYPE_onReceiveMsg);
        }
    };

    /**
     * 查询所有的消息返回
     */
    private Emitter.Listener onGetAllMsg = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            LogUtil.e(TAG, " ------>onGetAllMsg");
            deliverMsg(args[0], MSG_TYPE_onGetAllMsg);
        }
    };

    /**
     * 查询我的发言所有的消息返回
     */
    private Emitter.Listener onGetMyMsg = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            LogUtil.e(TAG, " ------>onGetMyMsg");
            deliverMsg(args[0], MSG_TYPE_onGetMyMsg);
        }
    };

    /**
     * 查询@我所有的消息返回
     */
    private Emitter.Listener onGetAmeMsg = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            LogUtil.e(TAG, " ------>onGetAmeMsg");
            deliverMsg(args[0], MSG_TYPE_onGetAmeMsg);
        }
    };

    /**
     * 向UI分发消息 :仅四种类：单条消息，请求分类的多条消息集合
     */
    private synchronized void deliverMsg(final Object obj, final int type) {
        LogUtil.e(TAG, "deliverMsg : json=" + obj.toString());
        try {
            if (iMessageCallBack != null) {
                UIUtil.post(new Runnable() {
                    @Override
                    public void run() {
                        OnlineMsgBean onlineMsgBean;
                        switch (type) {
                            case MSG_TYPE_onReceiveMsg:
                                iMessageCallBack.onReceiveMsg(JsonHelper.parseObject(obj.toString(), OnlineMsgBean.MsgEntity.class));
                                break;
                            case MSG_TYPE_onGetAllMsg:
                                onlineMsgBean = JsonHelper.parseObject(obj.toString(), OnlineMsgBean.class);
                                if (onlineMsgBean != null) {
                                    iMessageCallBack.onGetAllMsg(onlineMsgBean.getMsg());
                                }
                                break;
                            case MSG_TYPE_onGetMyMsg:
                                onlineMsgBean = JsonHelper.parseObject(obj.toString(), OnlineMsgBean.class);
                                if (onlineMsgBean != null) {
                                    iMessageCallBack.onGetMyMsg(onlineMsgBean.getMsg());
                                }
                                break;
                            case MSG_TYPE_onGetAmeMsg:
                                onlineMsgBean = JsonHelper.parseObject(obj.toString(), OnlineMsgBean.class);
                                if (onlineMsgBean != null) {
                                    iMessageCallBack.onGetAmeMsg(onlineMsgBean.getMsg());
                                }
                                break;
                        }
                    }
                });
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "deliverMsg: Exception=" + e.getMessage());
        }
    }

    private IMessageCallBack iMessageCallBack;

    public void setMessageCallBack(IMessageCallBack iMessageCallBack) {
        this.iMessageCallBack = iMessageCallBack;
    }

    public void removeMessageCallBack() {
        if (iMessageCallBack != null) {
            iMessageCallBack = null;
        }
    }

    public interface IMessageCallBack {
        void connectError();

        void loginChatRoomFailed();

        void loginChatroomSuccess();

        void onReceiveMsg(OnlineMsgBean.MsgEntity msgEntity);

        void onGetAllMsg(List<OnlineMsgBean.MsgEntity> msgEntityList);

        void onGetMyMsg(List<OnlineMsgBean.MsgEntity> msgEntityList);

        void onGetAmeMsg(List<OnlineMsgBean.MsgEntity> msgEntityList);
    }
}
