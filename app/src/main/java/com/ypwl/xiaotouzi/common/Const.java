package com.ypwl.xiaotouzi.common;

/**
 * function : 常量池<br/>
 * Created by lzj on 2015/11/2.
 */
public final class Const {

    /** SharedPreferences文件名-->默认 */
    public static final String SP_FILENAME_XTZ = "xiaotouzi";

    /******************** 系统常量Key *********************/
    /** sp key ：登录用户信息字符串 */
    public static final String KEY_LOGIN_USER = "login_user";
    /** sp key ：活动信息 */
    public static final String HUODONG_INFO = "huodong_info";
    /** sp key ：是否已经上传自己服务器登录后的统计数据 */
    public static String KEY_HAS_UPLOAD_SERVER_DATA_AFTER_LOGIN = "key_has_upload_server_data_after_login";
    /** sp key ：是否已经上传自己服务器设备信息的统计数据 */
    public static String KEY_HAS_UPLOAD_SERVER_DATA_DEVICES_INFO = "key_has_upload_server_data_devices_info";
    /** key : 上版本号存放 */
    public static final String KEY_LAST_VERSION_CODE = "key_last_version_code";
    /** key : APP更新了 */
    public static final String KEY_APP_HAS_UPDATED = "key_app_has_updated";
    /** key : 帖子有新的回复提醒 */
    public static final String KEY_NOTE_REPLY_NEW = "key_note_reply_new";
    /** key : 本地缓存聊天室名称空间 */
    public static final String KEY_NSP_CHAT = "key_nsp_chat";
    /** key : 启动页图片地址 */
    public static final String IMG_SPLASH = "img_splash";

    /** key-->app更新信息:记录忽略更新日期 */
    public static final String KEY_DATE_OF_UPDATE_IGNORE = "key_date_of_update_ignore";
    /** key-->app更新信息: APP强制更新下载中 */
    public static final String KEY_UPDATE_FORCING_DOWNLOADING = "key_update_forcing_downloading";


    /******************** 页面跳转Key *********************/
    /** Intent跳转数据传递Key-->基本数据类型 */
    public static final String KEY_INTENT_JUMP_BASE_DATA = "key_intent_jump_base_data";
    /** Intent跳转数据传递Key-->基本数据类型 */
    public static final String KEY_INTENT_JUMP_BASE_DATA_1 = "key_intent_jump_base_data_1";
    /** key ：手势密码开关 */
    public static final String KEY_GESTURE_SWITCHER_STATE = "key_gesture_switcher_state";
    /** key ：手势密码字符串 */
    public static final String KEY_GESTURE_PASSWORD = "key_gesture_password";
    /** key : 页面跳转来源key */
    public static final String KEY_INTENT_JUMP_FROM_DATA = "key_intent_jump_from_data";


    /******************** Exception type *********************/
    /** Exception基准 */
    public static final int EXCEPTION_BASE = 0x100;
    /** Exception类型--> json数据或格式错误 */
    public static final int EXCEPTION_JSON_WRONG = EXCEPTION_BASE + 1;
    /** Exception类型--> 网络出错 */
    public static final int EXCEPTION_NET_ERROR = EXCEPTION_BASE + 2;

    /******************** json数据Key *********************/
    /** json_Key-->返回结果状态: status */
    public static final String JSON_KEY_status = "status";
    /** json_Key-->返回结果错误信息: ret_msg */
    public static final String JSON_KEY_ret_msg = "ret_msg";
    /** json_Key-->token: token */
    public static final String JSON_KEY_token = "token";
    /** json_Key-->忘记密码中服务器返回的验证码id: smsid */
    public static final String JSON_KEY_smsid = "smsid";
    /** json_Key-->分享_title : title */
    public static final String JSON_KEY_title = "title";
    /** json_Key-->分享_content : content */
    public static final String JSON_KEY_content = "content";
    /** json_Key-->分享_url : url */
    public static final String JSON_KEY_url = "url";
    /** json_Key-->上传头像返回头像的url : avatarUrl */
    public static final String JSON_KEY_avatarUrl = "avatarUrl";
    /** 自动记账同步验证码 */
    public static final String JSON_KEY_AUTH_CODE_imgdata = "imgdata";


    /******************** 网络请求标记tag *********************/
    /** 网络请求类型标记-->关注平台详情信息 */
    public static final String NET_TAG_REQUEST_GET_DETAIL_COLLECT = "net_tag_request_get_detail_collect";
    /** 网络请求类型标记-->取消关注平台详情信息 */
    public static final String NET_TAG_REQUEST_GET_DETAIL_CANCEL_COLLECT = "net_tag_request_get_detail_cancel__collect";
    /** 自动记账-->解除绑定 */
    public static final String NET_TAG_REQUEST_AUTO_TALLY_REMOVE_BINDING = "net_tag_request_auto_tally_remove_binding";
    /** 自动记账同步 */
    public static final String NET_TAG_REQUEST_SYNC_AUTO_TALLY_DATA = "net_tag_request_sync_auto_tally_data";


    /******************** 其他常量 *********************/
    /** 登录类型:手机、QQ、微信、新浪微博 */
    public static final String LOGIN_TYPE = "login_type";
    /** 登录类型 ： 手机号登录 */
    public static final int LOGIN_TYPE_PHONE = 0;
    /** 登录类型 ： 新浪微博登录 */
    public static final int LOGIN_TYPE_SINA_WEIBO = 3;
    /** 登录类型 ： 微信登录 */
    public static final int LOGIN_TYPE_WECHAT = 2;
    /** 登录类型 ： QQ登录 */
    public static final int LOGIN_TYPE_QQ = 1;

    /** activity返回result状态 */
    public static final int CODE_RESULT_NEEDREFRESH = 999;

    /** 手势密码 动作类型 ： 创建新密码 */
    public static final String GPSW_CREATE = "GPSW_CREATE";
    /** 手势密码 动作类型 ： 校验密码 */
    public static final String GPSW_CHECK = "GPSW_CHECK";
    /** 手势密码 动作类型 ： 校验密码并关闭密码 */
    public static final String GPSW_CHECK_CLOSE = "GPSW_CHECK_CLOSE";
    /** 手势密码 动作类型 ： 修改旧密码 */
    public static final String GPSW_CHANGE = "GPSW_CHANGE";

    /******************** 视图状态常量 *********************/
    /** 视图状态 ： 加载中 */
    public static final int LAYOUT_LOADING = 0;
    /** 视图状态 ： 无数据空视图 */
    public static final int LAYOUT_EMPTY = 1;
    /** 视图状态 ： 出错 */
    public static final int LAYOUT_ERROR = 2;
    /** 视图状态 ： 数据视图 */
    public static final int LAYOUT_DATA = 3;
    /** 加载完成通知状态： 加载完毕 */
    public static final int LOADED_NO_MORE = -1;
    /** 加载完成通知状态： 出错 */
    public static final int LOADED_ERROR = -2;

    /************************** 网贷平台接口参数 ************************************************/
    /** 接口参数--type : 成交量 */
    public static final int PARAM_TYPE_CHENGJIAO = 1;
    /** 接口参数--type : 投资人数 */
    public static final int PARAM_TYPE_TOUZIRENSHU = 2;
    /** 接口参数--type : 利率 */
    public static final int PARAM_TYPE_LILV = 3;
    /** 接口参数--type : 评级 */
    public static final int PARAM_TYPE_PINGJI = 4;
    /** 接口参数--order :  升序 */
    public static final int PARAM_ORDER_SHENG = 1;
    /** 接口参数--type : 降序 */
    public static final int PARAM_ORDER_JIANG = 2;
    /** 接口参数--guarantee : 托管 */
    public static final int PARAM_GUARANTEE_TUOGUAN = 1;
    /** 接口参数--guarantee : 担保 */
    public static final int PARAM_GUARANTEE_DANBAO = 2;
    /** 接口参数--video_atc：视频认证 */
    public static final int PARAM_VIDEO_ATC = 1;
    /** 接口参数--background : 银行 */
    public static final int PARAM_BACKGROUND_YINGHANG = 1;
    /** 接口参数--background : 风投 */
    public static final int PARAM_BACKGROUND_FENGTOU = 2;
    /** 接口参数--background : 国资 */
    public static final int PARAM_BACKGROUND_GUOZI = 3;
    /** 接口参数--background : 上市 */
    public static final int PARAM_BACKGROUND_SHANGSHI = 4;
    /** 接口参数--btype : 抵押 */
    public static final int PARAM_BTYPE_DIYA = 1;
    /** 接口参数--btype : 信用 */
    public static final int PARAM_BTYPE_XINYONG = 2;
    /** 接口参数--btype : 票据 */
    public static final int PARAM_BTYPE_PIAOJU = 3;
    /** 接口参数--btype : 债券转让 */
    public static final int PARAM_BTYPE_ZAIQUAN = 4;
    /** 接口参数--district : 北京 */
    public static final int PARAM_DISTRICT_BEIJING = 110000;
    /** 接口参数--district : 上海 */
    public static final int PARAM_DISTRICT_SHANGHAI = 310000;
    /** 接口参数--district : 广东 */
    public static final int PARAM_DISTRICT_GUANGDONG = 440000;
    /** 接口参数--district : 浙江 */
    public static final int PARAM_DISTRICT_ZEJIANG = 330000;
    /** 接口参数--district : 江苏 */
    public static final int PARAM_DISTRICT_JIANGSU = 320000;
    /** 接口参数--district : 山东 */
    public static final int PARAM_DISTRICT_SHANDONG = 370000;
    /** 接口参数--district : 其他 */
    public static final int PARAM_DISTRICT_ELSE = 78;


    /**
     * 平台选择请求来源
     * 默认来源于记账
     * 0：记账，1：平台对比，2：其他
     */
    public static int PLATFORM_CHOOSE_REQUEST_FROM = 0;

    /**
     * 计算器结果tag
     */
    public static String CAL_SJSY = "cal_sjsy";//实际收益
    public static String CAL_SJLL = "cal_sjll";//实际利率
    public static String CAL_TBJL = "cal_tbjl";//投标奖励
    public static String CAL_LXGL = "cal_lxgl";//利息管理费
    public static String CAL_JGQD = "cal_jgqd";//结果清单

    /**
     * 网络请求类型
     */
    public static int REQUEST_POST = 1000;
    public static int REQUEST_GET = 1001;

    /**
     * 进入到单个标的回款详情的路径
     * 0：表示从回款进入
     * 1：表示从标的详情进入
     */
    public static int JUMP_TO_SINGLE_BID_FROM_TYPE = 0;
}
