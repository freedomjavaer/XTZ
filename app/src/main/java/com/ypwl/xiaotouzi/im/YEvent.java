package com.ypwl.xiaotouzi.im;


import com.ypwl.xiaotouzi.im.socketio.client.Socket;

/**
 * function ： 消息事件名称常量池
 *
 * Created by lzj on 2016/1/31.
 */
@SuppressWarnings("unused")
public class YEvent {
    /** Called on a connection. */
    public static final String EVENT_CONNECT = Socket.EVENT_CONNECT;
    /** Called on a disconnection. */
    public static final String EVENT_DISCONNECT = Socket.EVENT_DISCONNECT;
    /** Called on a connecting error. */
    public static final String EVENT_ERROR = Socket.EVENT_ERROR;
    /** Called on a connection error. */
    public static final String EVENT_CONNECT_ERROR = Socket.EVENT_CONNECT_ERROR;
    /** Called on a connection timeout. */
    public static final String EVENT_CONNECT_TIMEOUT = Socket.EVENT_CONNECT_TIMEOUT;
    /** Called on a connection reconnect. */
    public static final String EVENT_RECONNECT = Socket.EVENT_RECONNECT;
    /** Called on a connection reconnect_error. */
    public static final String EVENT_RECONNECT_ERROR = Socket.EVENT_RECONNECT_ERROR;
    /** Called on a connection reconnect_failed. */
    public static final String EVENT_RECONNECT_FAILED = Socket.EVENT_RECONNECT_FAILED;
    /** Called on a connection reconnect_attempt. */
    public static final String EVENT_RECONNECT_ATTEMPT = Socket.EVENT_RECONNECT_ATTEMPT;
    /** Called on a connection reconnecting. */
    public static final String EVENT_RECONNECTING = Socket.EVENT_RECONNECTING;
    /** Called on a message. */
    public static final String EVENT_MESSAGE = "message";
    /** Called on add client user self into chatroom. */
    public static final String EVENT_ADD_USER = "add user";
    /** Called on client user login. */
    public static final String EVENT_LOGIN = "login";
    /** Called on other user is typing. */
    public static final String EVENT_TYPING = "typing";
    /** Called on other user stop_typing. */
    public static final String EVENT_STOP_TYPING = "stop typing";
    /** Called on user_joined. */
    public static final String EVENT_USER_JOINED = "user joined";
    /** Called on user_left. */
    public static final String EVENT_USER_LEFT = "user left";

    ///-----------added for xtz
    /** Called on upload a push token. */
    public static final String EVENT_UP_TOKEN = "up_token";
    /** Called on a message in. */
    public static final String EVENT_NEW_MESSAGE = "new_msg";
    /** called on other place login */
    public static final String EVENT_REMOTE_LOGIN = "remote_login";

    ///------message即时聊天
    /** called on self login chatroom success */
    public static final String EVENT_NSP_SUCCESS = "nsp_success";
    /** called on self login chatroom for upload self user info */
    public static final String EVENT_UP_USERINFO = "up_userinfo";

    /** called on message received */
    public static final String EVENT_RECEIVE_MSG = "receive_msg";
    /** called on send message */
    public static final String EVENT_SEND_MSG = "send_msg";
    /** called on send message success */
    public static final String EVENT_SEND_SUCCESS = "send_success";

    /** called on request get msg */
    public static final String EVENT_GET_MSG = "get_msg";
    /** called on get all_msg(all msg) */
    public static final String EVENT_ALL_MSG = "all_msg";
    /** called on get my_msg(myword msg) */
    public static final String EVENT_MY_MSG = "my_msg";
    /** called on get to_msg(@me msg) */
    public static final String EVENT_TO_ME = "to_me";

}
