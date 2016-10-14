package com.ypwl.xiaotouzi.im;

import com.ypwl.xiaotouzi.common.Configs;

/**
 * function ： 消息推送框架配置信息
 *
 * Created by lzj on 2016/1/31.
 */
public class YConfigs {

    /** 调试开关 */
    public static boolean DEBUG = Configs.DEBUG;

    /** socket服务端地址 : 测试 ,  上线 */
    public static String SOCKET_ADDRESS = "http://www.xtz168.com:8801/";

    public static void initConfigs(boolean debug) {
        DEBUG = debug;
        if (DEBUG) {
            SOCKET_ADDRESS = "http://183.239.156.219:8801/";
        }
    }

    public static final String SP_FILENAME = "sp_ymessage";

}
