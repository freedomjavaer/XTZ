package com.ypwl.xiaotouzi.common;

/**
 * 接口地址常量池,分为开发测试环境和线上生成环境，根据Configs中的STATE决定当前环境<br/>
 * <br/>
 * Created by lzj on 2015/11/2.
 * <p/>
 * Modify by lzj on 2016/1/28. : 用于取代之前URLConstant
 */
public final class URLConstant {

    /** Host地址 -- 安全双向证书认证接口 : JSON数据请求交互 */
    public static String URL_BASE_S = "https://www.xtz168.com:8443/";

    /** Host地址 -- webview页面地址 */
    public static String URL_BASE_W = "http://www.xtz168.com/";

    /** Host地址 -- cdn资源地址 : 用于静态资源请求，如图片加载 */
    public static String URL_BASE_C = "http://www.xtz168.com/";

    //-----------------------------------------------------以下是正式环境业务接口
    /** 第三方账户注册兼登录 */
    public static String USER_LOGIN_AND_REGIST_BY_THIRD_PLATFORM = URL_BASE_S + "index.php?m=content&c=user&a=thirdRegAndLogin&app_type=2&type=%d&openid=%s&nickname=%s&avatar_url=%s";
    /** 用户登录 */
    public static String USER_LOGIN = URL_BASE_S + "index.php?m=content&c=user&a=login&app_type=2&phone=%s&password=%s&return_url=%s";
    /** 找回密码的获取验证码 */
    public static String RESET_PSW_GET_CHECK_CODE = URL_BASE_S + "index.php?m=content&c=user&a=sendRestCode&phone=%s";
    /** 找回密码的下一步 */
    public static String RESET_PSW_NEXT_STEP = URL_BASE_S + "index.php?m=content&c=user&a=verifyRestCode&phone=%s&rand_code=%s&smsid=%s";
    /** 修改密码为新密码 */
    public static String SUBMMIT_NEW_PSW = URL_BASE_S + "index.php?m=content&c=user&a=updPassword&token=%s&password=%s";
    /** 注册验证码 */
    public static String USER_REGIST_GET_CHECK_CODE = URL_BASE_S + "index.php?m=content&c=user&a=sendRegCode&phone=%s";
    /** 用户注册 提交 */
    public static String USER_REGIST_SUBMMIT = URL_BASE_S + "index.php?m=content&c=user&a=reg&phone=%s&password=%s&rand_code=%s&smsid=%s&invite_code=%s";
    /** 设置用户device_token,将umeng生成的device_token上传到我们服务器 */
    public static String UPLOAD_DEVICE_TOKEN = URL_BASE_S + "index.php?m=content&c=user&a=setDeviceToken&token=%s&device_token=%s";
    /** 获取活动列表 */
    public static String USER_HUODONG_LIEBIAO = URL_BASE_S + "index.php?m=content&c=activity&a=activityList&token=%s";
    /** 保存用户在app中的消息推送设置 */
    public static String SETTING_PUSH_BACK_MONEY_STATE = URL_BASE_S + "index.php?m=content&c=user&a=savePushStatus&token=%s&appRecommendFlag=%s&safeFlag=%s&returnedMoneyFlag=%s";
    /** 获取分享信息--app分享 */
    public static String SHARE_GET_APP_SHARE_URL = URL_BASE_S + "index.php?m=content&c=user&a=getShareUrl&type=0&token=%s";
    /** 头像上传 */
    public static String UPLOAD_USER_AVATAR = URL_BASE_S + "index.php?m=content&c=user&a=saveAvatar&token=%s";
    /** 更新个人资料 */
    public static String USER_INFO_UPDATE = URL_BASE_S + "index.php?m=content&c=user&a=setInfo&token=%s&nickname=%s";
    /** 获取手机验证码 */
    public static String CHECK_CODE_GET_BY_PHONE_NUMBER = URL_BASE_S + "index.php?m=content&c=user&a=sendBindCode&token=%s&phone=%s";
    /** 提交个人数据-电话绑定 */
    public static String USER_INFO_SUBMMIT_BIND_PHONE = URL_BASE_S + "index.php?m=content&c=user&a=bindPhone&token=%s&phone=%s&rand_code=%s&smsid=%s&password=%s";
    /** 第三方绑定 */
    public static String BIND_THIRD_PLATFORM = URL_BASE_S + "index.php?m=content&c=user&a=bind&type=%s&openid=%s&nickname=%s&token=%s";
    /** 投标详情 */
    public static String BID_DETAIL = URL_BASE_S + "index.php?m=content&c=account&a=bidDetails&token=%s&aid=%s";
    /** 网贷平台详情 */
    public static String PLATFORM_DETAIL = URL_BASE_S + "index.php?m=content&c=platform&a=getPlatformDetail&pid=%s";
    /** 平台深度数据 */
    public static String PLATFORM_DEEP_DATA = URL_BASE_S + "index.php?m=content&c=platform&a=getPlatformDepth&pid=%s&type1=%s&type2=%s&date_type=%s";
    /** 关注平台 */
    public static String PLATFORM_CELLECT = URL_BASE_S + "index.php?m=content&c=platform&a=follow&token=%s&pid=%s";
    /** 取消关注平台 */
    public static String PLATFORM_CANCEL_CELLECT = URL_BASE_S + "index.php?m=content&c=platform&a=noFollow&token=%s&pid=%s";
    /** 我的关注 */
    public static String MY_FOCUS = URL_BASE_S + "index.php?m=content&c=platform&a=myFollow&token=%s";
    /** 更改回款状态 */
    public static String CHANGING_THE_STATUS_OF_PAYMENT = URL_BASE_S + "index.php?m=content&c=account&a=updateReturnStatus&token=%s&rids=%s&type=%s";
    /** 批量删除投资 */
    public static String INVEST_DELETE_IN_BATCH = URL_BASE_S + "index.php?m=content&c=account&a=accountDel&token=%s&aids=%s";
    /** 我的投资--点击条目获取对应平台的投资列表 */
    public static String INVEST_LIST = URL_BASE_S + "index.php?m=content&c=account&a=investList&token=%s&pid=%s&type=%s&is_auto=%s&page=%s";
    /** 近期日历回款 */
    public static String RECENT_RETURN_MONEY = URL_BASE_S + "index.php?m=content&c=account&a=receivableList&token=%s&date=%s";
    /** 网贷平台默认数据 */
    public static String ALL_PLATFORM_DEFAULT = URL_BASE_S + "index.php?m=content&c=platform&a=getPlatData&type=%s&order=%s&page=%s";
    /** 平台搜索 */
    public static String SEARCH_PLATFORM = URL_BASE_S + "index.php?m=content&c=platform&a=searchPlat&pname=%s";
    /** 自动记账平台选择 */
    public static String AUTO_TALLY_PLATFORM_CHOOSE = URL_BASE_S + "index.php?m=content&c=platform&a=getPlatformAuto&token=%s";
    /** 绑定账号 */
    public static String BIND_PLATFORM_ACCOUNT = URL_BASE_W + "index.php?m=content&c=platform&a=autoBindPage&token=%s&pid=%s";
    /** 同步自动记账 */
    public static String SYNC_AUTO_TALLY = URL_BASE_S + "index.php?m=content&c=account&a=autoSync&token=%s&pid=%s&code=%s";
    /** 解绑自动记账 */
    public static String REMOVE_BIND_AUTO_TALLY = URL_BASE_S + "index.php?m=content&c=account&a=autoUnbind&token=%s&pids=%s";
    /** 选择平台 */
    public static String PLAT_CHOOSE = URL_BASE_S + "index.php?m=content&c=platform&a=platformList&token=%s&type=%s";
    /** 平台对比 */
    public static String PLAT_COMPARE = URL_BASE_S + "index.php?m=content&c=platform&a=platformContrast&token=%s&pids=%s&date_type=%s";
    /** 选择平台数据搜索 */
    public static String PLAT_CHOOSE_DATA_SEARCH = URL_BASE_S + "index.php?m=content&c=platform&a=platformSearchList&token=%s&search_name=%s&type=%s";
    /** 删除该标 */
    public static String BID_DETAIL_DELETE = URL_BASE_S + "index.php?m=content&c=account&a=accountDel&token=%s&aids=%s";
    /** 用户退出登录 */
    public static String USER_LOGOUT = URL_BASE_S + "index.php?m=content&c=user&a=logout&token=%s";
    /** 上传登录后的统计数据 */
    public static String UPLOAD_SERVER_DATA_AFTER_LOGIN = URL_BASE_S + "index.php?m=content&c=user&a=saveDeviceInfo";
    /** 上传设备信息的统计数据 */
    public static String UPLOAD_DEVICE_INFO_DATA = URL_BASE_S + "index.php?m=content&c=app&a=appstat";
    /** 主页活动展示 */
    public static String MAIN_HUODONG_SHOW = URL_BASE_S + "index.php?m=content&c=activity&a=float";
    /** 发布话题 */
    public static String TOPIC_PUBLISH_NEW = URL_BASE_S + "index.php?m=content&c=post&a=addPost";
    /** 投资分析---资产概览 */
    public static String PROPERTY_OVERVIEW = URL_BASE_S + "index.php?m=content&c=account&a=assetKL&token=%s&option=%s";
    /** 投资分析---对比分析---年化率 */
    public static String COMPARE_ANALYZE_ANNUAL_RATE = URL_BASE_S + "index.php?m=content&c=account&a=contrastRate&token=%s&type=%s&option=%s&page=%s";
    /** 帖子详情 */
    public static String XTZ_POST_DETAILS = URL_BASE_S + "index.php?m=content&c=post&a=postDetails&post_id=%s&page=%s";
    /** 修改帖子头像、昵称 */
    public static String POST_NICKNAME_ICON = URL_BASE_S + "index.php?m=content&c=user&a=changePortrait";
    /** 回复帖子 */
    public static String POST_REPLY = URL_BASE_S + "index.php?m=content&c=post&a=reply";
    /** 投资分析---对比分析---投资金额 */
    public static String COMPARE_ANALYZE_INVEST_MONEY = URL_BASE_S + "index.php?m=content&c=account&a=contrastInvest&token=%s&type=%s&option=%s&page=%s";
    /** 投资分析---对比分析---预期收益 */
    public static String COMPARE_ANALYZE_EXPECT_PROFIT = URL_BASE_S + "index.php?m=content&c=account&a=contrastProfit&token=%s&type=%s&option=%s&page=%s";
    /** 投资分析---占比统计 */
    public static String SCALE_STATISTICS = URL_BASE_S + "index.php?m=content&c=account&a=contrastTJ&token=%s&option=%s";
    /** 投资分析---历史投资---收益趋势 */
    public static String PROFIT_TENDENCY = URL_BASE_S + "index.php?m=content&c=account&a=profitTrend&token=%s";
    /** APP更新信息查询 */
    public static String APP_UPDATE_INFO = URL_BASE_S + "index.php?m=content&c=app&a=apkChkUpdate&channel_id=%s&version_code=%s";
    /** 设置修改手势密码 */
    public static String GESTURE_PSW_CHANGE = URL_BASE_S + "index.php?m=content&c=user&a=setGesturesPassword&token=%s&g_status=%s&g_password=%s";
    /** 晓投资主页 */
    public static String XTZ_NET_CREDIT_NEWS = URL_BASE_S + "index.php?m=content&c=wdnews&a=wdNewsList&page=%s&token=%s";
    /** 网贷新闻详情 */
    public static String NET_CREDIT_NEWS_DETAIL = URL_BASE_S + "index.php?m=content&c=wdnews&a=newsDetails&token=%s&news_id=%s&version=%s&page=%s";
    /** 评论新闻 */
    public static String NEWS_DETAIL_REPLY = URL_BASE_S + "index.php?m=content&c=wdnews&a=reply";
    /** 投友圈 */
    public static String TOU_YOU_QUAN = URL_BASE_S + "index.php?m=content&c=post&a=hotPostList&token=%s&page=%s";
    /** 常见问题和反馈 */
    public static String QUESTTION_FEEDBACK = URL_BASE_W + "index.php?m=content&c=app&a=familiarQuestion";
    /** 点赞 */
    public static String NOTE_FAVOUR = URL_BASE_S + "index.php?m=content&c=post&a=addFavour&token=%s&post_id=%s";
    /** 投友圈--消息 */
    public static String TYQ_MESSAGE = URL_BASE_S + "index.php?m=content&c=message&a=postMessage&token=%s&page=%s";
    /** 问题反馈 */
    public static String QUESTTION_FEEDBACK_MESSAGE = URL_BASE_S + "index.php?m=content&c=user&a=addAdvice";
    /** 投友圈--我的帖子 */
    public static String MY_POST_LIST = URL_BASE_S + "index.php?m=content&c=post&a=myPostList&token=%s&page=%s";
    /** 我的投资--近期list列表回款 */
    public static String RECENT_BACK_MONEY = URL_BASE_S + "index.php?m=content&c=account&a=recentlyRmoney&token=%s";
    /** 我的投资--资产 */
    public static String INVEST_ASSESTS = URL_BASE_S + "index.php?m=content&c=account&a=assetDetails&token=%s";
    /** 所有回款--日期分组 */
    public static String ALL_BACK_MONEY_BY_DATE = URL_BASE_S + "index.php?m=content&c=account&a=allRmoneyByDate&token=%s";
    /** 保存记账 */
    public static String KEEP_ACCOUNTS = URL_BASE_S + "index.php?m=content&c=account&a=save&token=%s&aid=%s&pid=%s&project_name=%s&starttime=%s&money=%s&return_type=%s&time_limit=%s&time_type=%s&rate=%s&rate_type=%s&tender_award=%s&tender_award_type=%s&extra_award=%s&extra_award_type=%s&cost=%s&remark=%s&custom_pname=%s&is_year=%s&is_thirty=%s&award_capital=%s&award_capital_type=%s&cash_cost=%s&auto_payment=%s&standard_year=%s";
    /** 保存记账--还款方式 */
    public static String KEEP_ACCOUNTS_REPAYMENT_METHOD = URL_BASE_S + "index.php?m=content&c=account&a=returnTypeList&token=%s";
    /** 保存记账--根据aid获取相应信息 */
    public static String KEEP_ACCOUNTS_GET_DATA_FROM_AID = URL_BASE_S + "index.php?m=content&c=account&a=infoInput&token=%s&aid=%s";
    /** 我的投资--资产--平台详情 */
    public static String MYINVEST_PLATFORM_DETAIL = URL_BASE_S + "index.php?m=content&c=account&a=platformBidDetails&token=%s&pid=%s&option=%s&page=%s&is_auto=%s";
    /** 我的投资--全部回款：按平台分标的 */
    public static String INVEST_BY_PLATFORM_BID = URL_BASE_S + "index.php?m=content&c=account&a=allRmoneyByPlatform&token=%s&option=%s";
    /** 收件箱 */
    public static String MESSAGE_BOX = URL_BASE_S + "index.php?m=content&c=message&a=standMessage&token=%s&page=%s";
    /** 单个标的回款详情 */
    public static String SINGLE_BID_BACK_MONEY_DETAIL = URL_BASE_S + "index.php?m=content&c=account&a=bidRmoneyDetails&token=%s&aid=%s";
    /** 我的投资--资产--标的详情 */
    public static String MYINVEST_BID_DETAIL = URL_BASE_S + "index.php?m=content&c=account&a=newBidDetails&token=%s&aid=%s&version=%s";
    /** 我的投资--资产--历史资产 */
    public static String MYINVEST_VNVEST_HISTORY = URL_BASE_S + "index.php?m=content&c=account&a=hostoryInvestment&token=%s&option=%s";
    /** 启动页-图片获取 */
    public static String SPLASH_IMG = URL_BASE_S + "index.php?m=content&c=app&a=startImg&app_type=2&scale=%s";
    /** 收件箱详情 */
    public static String MESSAGE_DETAIL = URL_BASE_S + "index.php?m=content&c=message&a=markMessage&token=%s&type=%s&stand_id=%s";
    /** 流水资产 */
    public static String CONTINUAL_TALLY = URL_BASE_S + "index.php?m=content&c=account&a=assetFlow&token=%s&all=%s&invest=%s&all_return=%s&last_return=%s&starttime=%s&endtime=%s&pid=%s";
    /** 流水资产 -- 获取平台列表 */
    public static String CONTINUAL_TALLY_PLATFORM_LIST = URL_BASE_S + "index.php?m=content&c=account&a=investPlatform&token=%s";
    /** 投资分析--投资分布 */
    public static String INVEST_ANALYSIS_BY_FENBU = URL_BASE_S + "index.php?m=content&c=account&a=investmentAnalyze&token=%s&option=%s";
    /** 投资分析--收益排行 */
    public static String INVEST_ANALYSIS_RANGE_PROFIT = URL_BASE_S + "index.php?m=content&c=account&a=revRanking&token=%s&status=%s&type=%s&option=%s";
    /** 金融超市---银行列表 */
    public static String FINANCE_MARKET_BANK_LIST = URL_BASE_S + "index.php?m=content&c=bank&a=bankList&token=%s";
    /** 金融超市--信用卡列表 */
    public static String FINANCE_CREDIT_CARD_LIST = URL_BASE_S + "index.php?m=content&c=bank&a=creditList&token=%s";
    /** 金融超市--基金 */
    public static String FINANCE_FUNDS_INVEST = URL_BASE_S + "index.php?m=content&c=finance&a=foundList&token=%s";
    /** 金融超市---保险 */
    public static String FINANCE_FUNDS_INSURANCE = URL_BASE_W + "index.php?m=content&c=finance&a=insuranceList&token=%s";
    /** 金融超市--精选列表 */
    public static String FINANCE_CHOICE_LIST = URL_BASE_S + "index.php?m=content&c=borrow&a=choiceness&page=%s&token=%s";
    /** 金融超市--平台列表 */
    public static String FINANCE_PLATFORM_LIST = URL_BASE_S + "index.php?m=content&c=borrow&a=platformList&page=%s&token=%s";
    /** 金融超市-- 融资 */
    public static String FINANCE_PLATFORM_RONGZI = URL_BASE_S + "index.php?m=content&c=finance&a=financeList&token=%s";
    /** 金融超市-- 标的详情 */
    public static String FINANCE_PLATFORM_TARGET_DEATAIL = URL_BASE_S + "index.php?m=content&c=borrow&a=bidDetails&pid=%s&project_id=%s&token=%s";
    /** 金融超市--平台--标的列表 */
    public static String FINANCE_PLATFORM_TARGET_LIST = URL_BASE_S + "index.php?m=content&c=borrow&a=bidList&pid=%s&page=%s&token=%s";
    /** 晓钱包 */
    public static String MY_INFO_WALLET = URL_BASE_S + "index.php?m=content&c=user&a=xWallet&token=%s";
    /** 提现 */
    public static String MY_INFO_WALLET_GET_MONEY = URL_BASE_S + "index.php?m=content&c=user&a=applyCash&token=%s&name=%s&bank=%s&account=%s&money=%s";
    /** 金融超市-- 基金详情 */
    public static String FINANCE_PLATFORM_FUNDS_DETAIL = URL_BASE_W + "index.php?m=content&c=finance&a=jumpPro&pid=%s";
    /** 金融超市-- 客户预约 */
    public static String FINANCE_PLATFORM_CLIENT_BOOK = URL_BASE_S + "index.php?m=content&c=finance&a=userReserve&name=%s&token=%s&type=%s";

    /** 金融超市-- 标的详情 -- 立即投资 */
    public static String FINANCE_PLATFORM_TARGET_DEATAIL_BUTTON = URL_BASE_W + "index.php?m=content&c=borrow&a=invest&token=%s&pid=%s&project_id=%s";
    /** 金融超市-- 标的详情 -- 立即投资 -- 判断是否绑定 */
    public static String FINANCE_PLATFORM_TARGET_DEATAIL_BUTTON_ISBIND = URL_BASE_S + "index.php?m=content&c=borrow&a=checkBind&token=%s&pid=%s";
    /** 金融超市-- 标的详情 -- 立即投资 -- 弹窗 */
    public static String FINANCE_PLATFORM_TARGET_DEATAIL_BUTTON_POPU = URL_BASE_W + "index.php?m=content&c=borrow&a=bindPlatform&token=%s&project_id=%s&pid=%s&type=%s";

    /** 常见问题和反馈 */
    public static String QUESTION_AND_FEEDBACK = URL_BASE_S + "index.php?m=content&c=app&a=newFamiliarQuestion";


    /**
     * 开发测试环境
     */
    private static void initTestUrl() {
        URL_BASE_S = "http://183.239.156.219:82/";
        URL_BASE_W = "http://183.239.156.219:82/";
        URL_BASE_C = "http://183.239.156.219:82/";

        //-----------------------------------------------------以下是测试环境业务接口
        USER_LOGIN_AND_REGIST_BY_THIRD_PLATFORM = URL_BASE_S + "index.php?m=content&c=user&a=thirdRegAndLogin&app_type=2&type=%d&openid=%s&nickname=%s&avatar_url=%s";
        USER_LOGIN = URL_BASE_S + "index.php?m=content&c=user&a=login&app_type=2&phone=%s&password=%s&return_url=%s";
        RESET_PSW_GET_CHECK_CODE = URL_BASE_S + "index.php?m=content&c=user&a=sendRestCode&phone=%s";
        RESET_PSW_NEXT_STEP = URL_BASE_S + "index.php?m=content&c=user&a=verifyRestCode&phone=%s&rand_code=%s&smsid=%s";
        SUBMMIT_NEW_PSW = URL_BASE_S + "index.php?m=content&c=user&a=updPassword&token=%s&password=%s";
        USER_REGIST_GET_CHECK_CODE = URL_BASE_S + "index.php?m=content&c=user&a=sendRegCode&phone=%s";
        USER_REGIST_SUBMMIT = URL_BASE_S + "index.php?m=content&c=user&a=reg&phone=%s&password=%s&rand_code=%s&smsid=%s&invite_code=%s";
        UPLOAD_DEVICE_TOKEN = URL_BASE_S + "index.php?m=content&c=user&a=setDeviceToken&token=%s&device_token=%s";
        USER_HUODONG_LIEBIAO = URL_BASE_S + "index.php?m=content&c=activity&a=activityList&token=%s";
        SETTING_PUSH_BACK_MONEY_STATE = URL_BASE_S + "index.php?m=content&c=user&a=savePushStatus&token=%s&appRecommendFlag=%s&safeFlag=%s&returnedMoneyFlag=%s";
        SHARE_GET_APP_SHARE_URL = URL_BASE_S + "index.php?m=content&c=user&a=getShareUrl&type=0&token=%s";
        UPLOAD_USER_AVATAR = URL_BASE_S + "index.php?m=content&c=user&a=saveAvatar&token=%s";
        USER_INFO_UPDATE = URL_BASE_S + "index.php?m=content&c=user&a=setInfo&token=%s&nickname=%s";
        CHECK_CODE_GET_BY_PHONE_NUMBER = URL_BASE_S + "index.php?m=content&c=user&a=sendBindCode&token=%s&phone=%s";
        USER_INFO_SUBMMIT_BIND_PHONE = URL_BASE_S + "index.php?m=content&c=user&a=bindPhone&token=%s&phone=%s&rand_code=%s&smsid=%s&password=%s";
        BIND_THIRD_PLATFORM = URL_BASE_S + "index.php?m=content&c=user&a=bind&type=%s&openid=%s&nickname=%s&token=%s";
        BID_DETAIL = URL_BASE_S + "index.php?m=content&c=account&a=bidDetails&token=%s&aid=%s";
        PLATFORM_DETAIL = URL_BASE_S + "index.php?m=content&c=platform&a=getPlatformDetail&pid=%s";
        PLATFORM_CELLECT = URL_BASE_S + "index.php?m=content&c=platform&a=follow&token=%s&pid=%s";
        PLATFORM_CANCEL_CELLECT = URL_BASE_S + "index.php?m=content&c=platform&a=noFollow&token=%s&pid=%s";
        MY_FOCUS = URL_BASE_S + "index.php?m=content&c=platform&a=myFollow&token=%s";
        CHANGING_THE_STATUS_OF_PAYMENT = URL_BASE_S + "index.php?m=content&c=account&a=updateReturnStatus&token=%s&rids=%s&type=%s";
        INVEST_DELETE_IN_BATCH = URL_BASE_S + "index.php?m=content&c=account&a=accountDel&token=%s&aids=%s";
        INVEST_LIST = URL_BASE_S + "index.php?m=content&c=account&a=investList&token=%s&pid=%s&type=%s&is_auto=%s&page=%s";
        PLATFORM_DEEP_DATA = URL_BASE_S + "index.php?m=content&c=platform&a=getPlatformDepth&pid=%s&type1=%s&type2=%s&date_type=%s";
        RECENT_RETURN_MONEY = URL_BASE_S + "index.php?m=content&c=account&a=receivableList&token=%s&date=%s";
        AUTO_TALLY_PLATFORM_CHOOSE = URL_BASE_S + "index.php?m=content&c=platform&a=getPlatformAuto&token=%s";
        ALL_PLATFORM_DEFAULT = URL_BASE_S + "index.php?m=content&c=platform&a=getPlatData&type=%s&order=%s&page=%s";
        SEARCH_PLATFORM = URL_BASE_S + "index.php?m=content&c=platform&a=searchPlat&pname=%s";
        BIND_PLATFORM_ACCOUNT = URL_BASE_W + "index.php?m=content&c=platform&a=autoBindPage&token=%s&pid=%s";
        SYNC_AUTO_TALLY = URL_BASE_S + "index.php?m=content&c=account&a=autoSync&token=%s&pid=%s&code=%s";
        REMOVE_BIND_AUTO_TALLY = URL_BASE_S + "index.php?m=content&c=account&a=autoUnbind&token=%s&pids=%s";
        PLAT_CHOOSE = URL_BASE_S + "index.php?m=content&c=platform&a=platformList&token=%s&type=%s";
        PLAT_COMPARE = URL_BASE_S + "index.php?m=content&c=platform&a=platformContrast&token=%s&pids=%s&date_type=%s";
        PLAT_CHOOSE_DATA_SEARCH = URL_BASE_S + "index.php?m=content&c=platform&a=platformSearchList&token=%s&search_name=%s&type=%s";
        BID_DETAIL_DELETE = URL_BASE_S + "index.php?m=content&c=account&a=accountDel&token=%s&aids=%s";
        USER_LOGOUT = URL_BASE_S + "index.php?m=content&c=user&a=logout&token=%s";
        UPLOAD_SERVER_DATA_AFTER_LOGIN = URL_BASE_S + "index.php?m=content&c=user&a=saveDeviceInfo";
        UPLOAD_DEVICE_INFO_DATA = URL_BASE_S + "index.php?m=content&c=app&a=appstat";
        MAIN_HUODONG_SHOW = URL_BASE_S + "index.php?m=content&c=activity&a=float";
        TOPIC_PUBLISH_NEW = URL_BASE_S + "index.php?m=content&c=post&a=addPost";
        PROPERTY_OVERVIEW = URL_BASE_S + "index.php?m=content&c=account&a=assetKL&token=%s&option=%s";
        COMPARE_ANALYZE_ANNUAL_RATE = URL_BASE_S + "index.php?m=content&c=account&a=contrastRate&token=%s&type=%s&option=%s&page=%s";
        XTZ_POST_DETAILS = URL_BASE_S + "index.php?m=content&c=post&a=postDetails&post_id=%s&page=%s";
        POST_NICKNAME_ICON = URL_BASE_S + "index.php?m=content&c=user&a=changePortrait";
        POST_REPLY = URL_BASE_S + "index.php?m=content&c=post&a=reply";
        COMPARE_ANALYZE_INVEST_MONEY = URL_BASE_S + "index.php?m=content&c=account&a=contrastInvest&token=%s&type=%s&option=%s&page=%s";
        COMPARE_ANALYZE_EXPECT_PROFIT = URL_BASE_S + "index.php?m=content&c=account&a=contrastProfit&token=%s&type=%s&option=%s&page=%s";
        SCALE_STATISTICS = URL_BASE_S + "index.php?m=content&c=account&a=contrastTJ&token=%s&option=%s";
        PROFIT_TENDENCY = URL_BASE_S + "index.php?m=content&c=account&a=profitTrend&token=%s";
        APP_UPDATE_INFO = URL_BASE_S + "index.php?m=content&c=app&a=apkChkUpdate&channel_id=%s&version_code=%s";
        XTZ_NET_CREDIT_NEWS = URL_BASE_S + "index.php?m=content&c=wdnews&a=wdNewsList&page=%s&token=%s";
        NET_CREDIT_NEWS_DETAIL = URL_BASE_S + "index.php?m=content&c=wdnews&a=newsDetails&token=%s&news_id=%s&version=%s&page=%s";
        NEWS_DETAIL_REPLY = URL_BASE_S + "index.php?m=content&c=wdnews&a=reply";
        TOU_YOU_QUAN = URL_BASE_S + "index.php?m=content&c=post&a=hotPostList&token=%s&page=%s";
        NOTE_FAVOUR = URL_BASE_S + "index.php?m=content&c=post&a=addFavour&token=%s&post_id=%s";
        GESTURE_PSW_CHANGE = URL_BASE_S + "index.php?m=content&c=user&a=setGesturesPassword&token=%s&g_status=%s&g_password=%s";
        TYQ_MESSAGE = URL_BASE_S + "index.php?m=content&c=message&a=postMessage&token=%s&page=%s";
        QUESTTION_FEEDBACK = URL_BASE_W + "index.php?m=content&c=app&a=familiarQuestion";
        QUESTTION_FEEDBACK_MESSAGE = URL_BASE_S + "index.php?m=content&c=user&a=addAdvice";
        MY_POST_LIST = URL_BASE_S + "index.php?m=content&c=post&a=myPostList&token=%s&page=%s";
        RECENT_BACK_MONEY = URL_BASE_S + "index.php?m=content&c=account&a=recentlyRmoney&token=%s";
        INVEST_ASSESTS = URL_BASE_S + "index.php?m=content&c=account&a=assetDetails&token=%s";
        ALL_BACK_MONEY_BY_DATE = URL_BASE_S + "index.php?m=content&c=account&a=allRmoneyByDate&token=%s";
        MYINVEST_PLATFORM_DETAIL = URL_BASE_S + "index.php?m=content&c=account&a=platformBidDetails&token=%s&pid=%s&option=%s&page=%s&is_auto=%s";
        INVEST_BY_PLATFORM_BID = URL_BASE_S + "index.php?m=content&c=account&a=allRmoneyByPlatform&token=%s&option=%s";
        MESSAGE_BOX = URL_BASE_S + "index.php?m=content&c=message&a=standMessage&token=%s&page=%s";
        SINGLE_BID_BACK_MONEY_DETAIL = URL_BASE_S + "index.php?m=content&c=account&a=bidRmoneyDetails&token=%s&aid=%s";
        MYINVEST_BID_DETAIL = URL_BASE_S + "index.php?m=content&c=account&a=newBidDetails&token=%s&aid=%s&version=%s";
        MYINVEST_VNVEST_HISTORY = URL_BASE_S + "index.php?m=content&c=account&a=hostoryInvestment&token=%s&option=%s";
        KEEP_ACCOUNTS = URL_BASE_S + "index.php?m=content&c=account&a=save&token=%s&aid=%s&pid=%s&project_name=%s&starttime=%s&money=%s&return_type=%s&time_limit=%s&time_type=%s&rate=%s&rate_type=%s&tender_award=%s&tender_award_type=%s&extra_award=%s&extra_award_type=%s&cost=%s&remark=%s&custom_pname=%s&is_year=%s&is_thirty=%s&award_capital=%s&award_capital_type=%s&cash_cost=%s&auto_payment=%s&standard_year=%s";
        KEEP_ACCOUNTS_REPAYMENT_METHOD = URL_BASE_S + "index.php?m=content&c=account&a=returnTypeList&token=%s";
        KEEP_ACCOUNTS_GET_DATA_FROM_AID = URL_BASE_S + "index.php?m=content&c=account&a=infoInput&token=%s&aid=%s";
        SPLASH_IMG = URL_BASE_S + "index.php?m=content&c=app&a=startImg&app_type=2&scale=%s";
        MESSAGE_DETAIL = URL_BASE_S + "index.php?m=content&c=message&a=markMessage&token=%s&type=%s&stand_id=%s";
        CONTINUAL_TALLY = URL_BASE_S + "index.php?m=content&c=account&a=assetFlow&token=%s&all=%s&invest=%s&all_return=%s&last_return=%s&starttime=%s&endtime=%s&pid=%s";
        CONTINUAL_TALLY_PLATFORM_LIST = URL_BASE_S + "index.php?m=content&c=account&a=investPlatform&token=%s";

        INVEST_ANALYSIS_BY_FENBU = URL_BASE_S + "index.php?m=content&c=account&a=investmentAnalyze&token=%s&option=%s";
        INVEST_ANALYSIS_RANGE_PROFIT = URL_BASE_S + "index.php?m=content&c=account&a=revRanking&token=%s&status=%s&type=%s&option=%s";
        FINANCE_MARKET_BANK_LIST = URL_BASE_S + "index.php?m=content&c=bank&a=bankList&token=%s";
        FINANCE_CREDIT_CARD_LIST = URL_BASE_S + "index.php?m=content&c=bank&a=creditList&token=%s";
        FINANCE_FUNDS_INVEST = URL_BASE_S + "index.php?m=content&c=finance&a=foundList&token=%s";
        FINANCE_FUNDS_INSURANCE = URL_BASE_W + "index.php?m=content&c=finance&a=insuranceList&token=%s";

        FINANCE_CHOICE_LIST = URL_BASE_S + "index.php?m=content&c=borrow&a=choiceness&page=%s&token=%s";

        FINANCE_PLATFORM_LIST = URL_BASE_S + "index.php?m=content&c=borrow&a=platformList&page=%s&token=%s";

        FINANCE_PLATFORM_RONGZI = URL_BASE_S + "index.php?m=content&c=finance&a=financeList&token=%s";

        FINANCE_PLATFORM_TARGET_LIST = URL_BASE_S + "index.php?m=content&c=borrow&a=bidList&pid=%s&page=%s&token=%s";

        MY_INFO_WALLET = URL_BASE_S + "index.php?m=content&c=user&a=xWallet&token=%s";
        MY_INFO_WALLET_GET_MONEY = URL_BASE_S + "index.php?m=content&c=user&a=applyCash&token=%s&name=%s&bank=%s&account=%s&money=%s";
        FINANCE_PLATFORM_FUNDS_DETAIL = URL_BASE_W + "index.php?m=content&c=finance&a=jumpPro&pid=%s";
        FINANCE_PLATFORM_CLIENT_BOOK = URL_BASE_S + "index.php?m=content&c=finance&a=userReserve&name=%s&token=%s&type=%s";

        FINANCE_PLATFORM_TARGET_DEATAIL = URL_BASE_S + "index.php?m=content&c=borrow&a=bidDetails&pid=%s&project_id=%s&token=%s";

        FINANCE_PLATFORM_TARGET_DEATAIL_BUTTON = URL_BASE_S + "index.php?m=content&c=borrow&a=invest&token=%s&pid=%s&project_id=%s";
        FINANCE_PLATFORM_TARGET_DEATAIL_BUTTON_ISBIND = URL_BASE_S + "index.php?m=content&c=borrow&a=checkBind&token=%s&pid=%s";
        FINANCE_PLATFORM_TARGET_DEATAIL_BUTTON_POPU = URL_BASE_S + "index.php?m=content&c=borrow&a=bindPlatform&token=%s&project_id=%s&pid=%s&type=%s";
        QUESTION_AND_FEEDBACK = URL_BASE_S + "index.php?m=content&c=app&a=newFamiliarQuestion";


    }

    static {
        if (Configs.DEBUG) {// 开发测试环境
            initTestUrl();
        }
    }
}
