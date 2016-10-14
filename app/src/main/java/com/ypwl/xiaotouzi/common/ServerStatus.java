package com.ypwl.xiaotouzi.common;

/**
 * function : 服务器请求返回结果状态.<br/>
 * Created by lzj on 2015/11/3.
 */
@SuppressWarnings("unused")
public class ServerStatus {
    public static final int SERVER_STATUS_OK = 0; // 访问正常
    public static final int SERVER_STATUS_ERROR_DEFAULT = -1; // 默认访问出错

    public static final int SERVER_STATUS_INVALID_EMAIL = 1002; // 邮箱不合法
    public static final int SERVER_STATUS_PWD_TOO_SHORT = 1003; // 密码小于6位
    public static final int SERVER_STATUS_PWD_TOO_LONG = 1004; // 密码大于16位
    public static final int SERVER_STATUS_EMAIL_EXISTS = 1005; // 该邮箱已存在
    public static final int SERVER_STATUS_EMAIL_NOT_EXISTS = 1006; // 该邮箱不存在
    public static final int SERVER_STATUS_SEND_EMAIL_FAILED = 1007; // 发送重置密码邮件失败w
    public static final int SERVER_STATUS_INVALID_MOBILE = 1008; // 无效的手机号
    public static final int SERVER_STATUS_MOBILE_NOT_EXISTS = 1009; // 该手机号未绑定
    public static final int SERVER_STATUS_MOBILE_EXISTS = 1010; // 该手机号已存在
    public static final int SERVER_STATUS_TOO_MANY_SMS_REQUEST = 1011; // 短信请求过于频繁
    public static final int SERVER_STATUS_SEND_MSM_FAILED = 1012; // 发送短信失败
    public static final int SERVER_STATUS_EXPIRE_RAND_CODE = 1013; // 验证码已过期
    public static final int SERVER_STATUS_LAST_ONE_BIND = 1014; // 已经是最后一个登录方式，不允许解绑
    public static final int SERVER_STATUS_LAST_ONE_BIND2 = 1015; // 已经是最后一个登录方式，不允许解绑

    public static final int SERVER_STATUS_PHONE_OR_PWD_INVALID = 1102; // 账户或密码错误

    public static final int SERVER_STATUS_INVALID_TOKEN = 1202; // token非法
    public static final int SERVER_STATUS_USER_NOT_EXISTS = 1203; // 用户不存在

    public static final int SERVER_STATUS_UPD_USER_INFO_FAILED = 1301; // 修改用户个人资料失败

    public static final int SERVER_STATUS_USER_HAS_PLATFORM = 1401; // 用户已经添加过该平台
    public static final int SERVER_STATUS_INVALID_PLATFORM = 1402; // 无效的pid
    public static final int SERVER_STATUS_USER_HASNT_PLATFORM = 1403; // 用户未关注该平台

    public static final int SERVER_STATUS_INVALID_RAND_CODE = 1501; // 验证码无效或过期
    public static final int SERVER_STATUS_SEND_MSG_TPL_FAILED = 1502; // 发送短信模板失败
    public static final int SERVER_STATUS_SEND_MSG_FAILED = 1503; // 短信发送系统错误

    public static final int SERVER_STATUS_INVALID_THIRD_TYPE = 1601; // 第三方平台type不合法
    public static final int SERVER_STATUS_INVALID_OPENID1 = 1602;//无效的第三方openid
    public static final int SERVER_STATUS_INVALID_NICKNAME = 1605;//昵称无效
    public static final int SERVER_STATUS_OPENID_IS_USED = 1607;//账号已被绑定

    public static final int SERVER_STATUS_AVATAR_TOO_BIG = 1701; // 上传图片大小过大
    public static final int SERVER_STATUS_INVALID_IMG_TYPE = 1702; // 上传图片类型不合法
    public static final int SERVER_STATUS_UPLOAD_IMG_ERR = 1703; // 服务器接收文件失败
    public static final int SERVER_STATUS_UPLOAD_IMG_EMPTY = 1704; // 上传图片为空

    public static final int SERVER_STATUS_USER_NOT_ADD_PLATFORM = 1706; // 用户没有关注的平台
    public static final int SERVER_STATUS_INVALID_DEVICE_TOKEN = 1707; // device_token为空

    public static final int SERVER_STATUS_INVALID_OS_TYPE = 1708; // os_type类型不正确
    public static final int SERVER_STATUS_LESS_DRAW_NUM = 1709; // 剩余抽奖次数不足

    public static final int SERVER_STATUS_ADD_ACCOUNT_FAILED = 3001;//保存账单失败
    public static final int SERVER_STATUS_UPD_ACCOUNT_FAILED = 3002;//更新账单失败
    public static final int SERVER_STATUS_ACCOUNT_NOT_EXISTS = 3003;

    public static final int SERVER_STATUS_INVALID_MONEY = 3004;//金额无效
    public static final int SERVER_STATUS_INVALID_RATE = 3005;//利率无效
    public static final int SERVER_STATUS_INVALID_TIME_LIMIT = 3006;//投资期限无效
    public static final int SERVER_STATUS_INVALID_AID = 3007;
    public static final int SERVER_STATUS_INVALID_PID = 3008;//pid无效
    public static final int SERVER_STATUS_USER_NOT_HAVE_THE_PLATFORM = 3009;
    public static final int SERVER_STATUS_INVALID_TIME_TYPE = 3010;//期限类型无效
    public static final int SERVER_STATUS_STATUS_CANNOT_CHANGE = 3011; //回款状态不能再改
    public static final int SERVER_STATUS_MUST_RETURN_PREVIOUS = 3012; //先回上一期才能回这一期
    public static final int SERVER_STATUS_ACCOUNT_HAS_RETURN = 3013; //已回款的账单不能再编辑

    /** platform **/
    public static final int SERVER_STATUS_PLATFORM_NOT_EXISTS = 2001; // 平台不存在
    public static final int SERVER_STATUS_PLATFORM_META_HAS_NEW_VER = 2002; // 平台元数据有更新版本
    public static final int SERVER_STATUS_INVALID_SORT_TYPE = 2003; // 无效的sort_type
    public static final int SERVER_STATUS_INVALID_SORT_CITY = 2004; // 无效的sort_city
    public static final int SERVER_STATUS_EMPTY_TRIAL_PLATFORM = 2005; // 未录入试用平台数据
    public static final int SERVER_STATUS_EMPTY_HOME_PLATFORM = 2006; // 未录入首页平台

    public static final int SERVER_STATUS_INVALID_SORT_TIME = 2007; // 无效的sort_time值
    public static final int SERVER_STATUS_INVALID_SORT_PING_RATE = 2008; // 无效的sort_ping_rate值
    public static final int SERVER_STATUS_INVALID_SORT_RATE = 2009; // 无效的sort_rate

    public static final int SERVER_STATUS_INVALID_HOT_SEARCH_NUM = 2010; // 热门搜索数量必须大于0

    public static final int SERVER_STATUS_ERR_PARAMS_NUM = 2011; // 一次只能传一个筛选排序
    public static final int SERVER_STATUS_BASE_LIST_NOT_DATA = 2012; // 拉取的数据为空

    /** 自动记账同步 */
    public static final int SERVER_STATUS_AUTO_TALLY_SYNC_OFTEN = 4003; // 同步操作太频繁
    public static final int SERVER_STATUS_AUTO_TALLY_AUTH_CODE = 4004;// 输入验证码

    /** activity **/
    public static final int SERVER_STATUS_NO_EXIST_USER = 6001;//用户不存在
    public static final int SERVER_STATUS_PARAMS_WRONG = 6002;//参数错误
    public static final int SERVER_STATUS_INVITE_SELF_ERROR = 6003;//不能自己的邀请码
    public static final int SERVER_STATUS_DATABASE_WRONG = 6004; //数据库错误
    public static final int SERVER_STATUS_NO_EXIST_INVITE_OR_USED = 6005;//邀请码不存在或已经使用过邀请码
    public static final int SERVER_STATUS_INVALID_SIGN = 6006; //非法签名
    public static final int SERVER_STATUS_NULL_PACKETS = 6007;//不存在红包信息
    public static final int SERVER_STATUS_HONGBAO_OPEN_FAILED = 6008; //红包打开失败

    public static final int SERVER_STATUS_SMS_SEND_FAILED = 6009;//短信发送失败
    public static final int SERVER_STATUS_CURRENT_NO_ACTIVE_DATA = 6010;//当前没有活动数据

    public static final int SERVER_STATUS_DB_ERROR = 9000; //数据库错误
}
