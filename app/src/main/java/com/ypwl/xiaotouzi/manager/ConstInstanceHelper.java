package com.ypwl.xiaotouzi.manager;

/**
 * function:进程存活期间需要持久化的常量数据实例帮助类
 *
 * <p>Created by lzj on 2016/3/24.</p>
 */
public class ConstInstanceHelper {
    protected final String TAG = ConstInstanceHelper.class.getSimpleName();

    private static volatile ConstInstanceHelper instance = new ConstInstanceHelper();

    public static ConstInstanceHelper getInstance() {
        return instance;
    }

    private ConstInstanceHelper() {
    }

    public void release() {
        tempChatMsg = null;
    }

    public static String tempChatMsg = null;

    /** 临时持久化聊天未发送xinx */
    public static void setTempChatMsg(String chatMsg) {
        tempChatMsg = chatMsg;
    }


}
