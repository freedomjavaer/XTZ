package com.ypwl.xiaotouzi.im;

/**
 * function : Constant pool.
 *
 * Created by lzj on 2016/1/31.
 */
public class YConst {

    /** intentFileter - action : receive_message */
    public static final String ACTION_INTENT_RECEIVE_MESSAGE = "com.ypwl.ymessage.intent.action.Message.RECEIVE";
    /** intentFileter - category : receive_message */
    public static final String CATEGORY_INTENT_RECEIVE_MESSAGE = "com.ypwl.ymessage.intent.category.Message.RECEIVE";
    /** key - intent jump key-messge */
    public static final String KEY_PUSH_MSG = "key_push_msg";

    /** key - 本地缓存pushToken */
    public static final String KEY_PUSHTOKEN = "pushToken";
    /** jsonkey - 接收消息类型解析json格式好的type */
    public static final String JSONKEY_TYPE = "type";

}
