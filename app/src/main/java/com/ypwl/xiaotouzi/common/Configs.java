package com.ypwl.xiaotouzi.common;

import com.ypwl.xiaotouzi.im.YConfigs;

/**
 * function : 通用配置类<br/>
 * 1、开发模式状态配置<br/>
 * 2、文件夹配置 文件夹配置的结束统一带有 文件分隔符 "/"，如 晓投资存储根目录文件夹配置"xiaotouzi/", 避免使用时再次加入文件分隔符"/"
 * <br/>
 * Created by lzj on 2015/11/2.
 */
public final class Configs {

    /** 当前APK模式 0-开发测试模式；1-上线模式 */
    public static int STATE = 1;

    /** 调试开关 */
    public static boolean DEBUG = true;

    static {
        if (0 == STATE) {//开发测试环境
            DEBUG = true;
        } else if (1 == STATE) {//生产上线环境
            DEBUG = false;
        } else {
            throw new RuntimeException("STATE is wrong!!!");
        }
        YConfigs.initConfigs(DEBUG);
    }

    /** 根目录（相对地址） */
    public static final String XTZ_BASE_PATH = "/xiaotouzi/";
    /** 临时文件目录 */
    public static final String XTZ_BASE_TEMP_PATH = XTZ_BASE_PATH + "temp/";
    /** 公共图片文件目录 */
    public static final String XTZ_BASE_PICTURE_PATH = XTZ_BASE_PATH + "picture/";

    /** 用于更新的apk文件名称 */
    public static final String APKNAME = "xiaotouzi.apk";
    /** 用于更新的apk临时文件名称 */
    public static final String APKNAMETEMP = "xiaotouzi.tmp";

    /** 未登录时 临时token */
    public static final String TOKEN_TEMP = "";//"change here token to server\'s temp token";//TODO change here
}
