package com.ypwl.xiaotouzi.utils;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.base.BaseException;
import com.ypwl.xiaotouzi.bean.LoginBean;
import com.ypwl.xiaotouzi.common.Configs;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.common.ServerStatus;
import com.ypwl.xiaotouzi.event.LoginStateEvent;
import com.ypwl.xiaotouzi.exception.CommonException;
import com.ypwl.xiaotouzi.manager.EventHelper;
import com.ypwl.xiaotouzi.manager.JsonHelper;
import com.ypwl.xiaotouzi.manager.SilenceRequestHelper;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * function : 通用工具类.<br/>
 * Created by lzj on 2015/11/3.
 */
public class Util {
    private static final String TAG = Util.class.getSimpleName();

    /**
     * 检测网络是否可用，给予提示
     */
    public static boolean netIsOk() {
        if (NetworkUtils.isNetworkConnected(UIUtil.getContext())) {
            return true;
        } else {
            UIUtil.showToastShort(UIUtil.getString(R.string.hint_net_problem));
            return false;
        }
    }

    /**
     * 检测网络请求返回结果是否OK
     *
     * @param status 网络响应返回码
     * @return 根据对应状态码返回对应的异常(该异常包含类型及信息)，如果OK的话返回null
     */
    public static BaseException accessIsOK(int status) {
        BaseException be = null;
        switch (status) {
            case ServerStatus.SERVER_STATUS_INVALID_EMAIL:// 邮箱不合法
                be = new CommonException(status, "邮箱不合法");
                break;
        }
        LogUtil.e(TAG, "-->  interceptAccess : status =" + status + " , CommonException = " + (be != null ? be.toString() : null));
        return be;
    }

    /**
     * 检测是否合法登录.
     */
    public static LoginBean legalLogin() {
        String loginInfo = CacheUtils.getString(Const.KEY_LOGIN_USER, null);

        //本地没有缓存用户信息-->未登录，内存token设置为临时token
        if (TextUtils.isEmpty(loginInfo)) {
            GlobalUtils.token = Configs.TOKEN_TEMP;
            return null;
        }

        LoginBean loginBean = JsonHelper.parseObject(loginInfo, LoginBean.class);

        //本地缓存了用户信息，不能解析为用户信息数据对象-->未登录，内存token设置为临时token
        if (loginBean == null) {
            GlobalUtils.token = Configs.TOKEN_TEMP;
            return null;
        }

        String cacheUserToken = loginBean.getToken();

        //用户信息数据对象中的token为空-->未登录，内存token设置为临时token
        if (TextUtils.isEmpty(cacheUserToken)) {
            GlobalUtils.token = Configs.TOKEN_TEMP;
            return null;
        }

        //以上条件都不满足-->已登录，内存token设置为用户token
        GlobalUtils.token = cacheUserToken;
        return loginBean;
    }

    /** 抹掉用户登录信息，让用户处于未登录状态 */
    public static void clearLoginInfo() {
        SilenceRequestHelper.getInstance().notifyServerUserLogout();
        CacheUtils.putString(Const.KEY_LOGIN_USER, null);
        legalLogin();
        EventHelper.post(new LoginStateEvent(false));
    }

    /**
     * TextView设置spannable
     */
    public static void setSpannable(TextView textView, String s, int color) {
        setSpannable(textView, s, 0, s.length(), color);
    }

    public static void setSpannable(TextView textView, String s, int start, int color) {
        setSpannable(textView, s, start, s.length(), color);
    }

    public static void setSpannableSize(TextView textView, String s, int start, int size) {
        setSpannableSize(textView, s, start, s.length(), size);
    }

    /***
     * TextView设置spannable
     *
     * @param textView TextView控件
     * @param s        字符串
     * @param start    开始position
     * @param end      结束position
     * @param color    颜色
     */
    public static void setSpannable(TextView textView, String s, int start, int end, int color) {
        if (s == null) {
            LogUtil.i(TAG, "error:" + "s=" + s);
            return;
        }
        if (start < 0 || end <= start) {
            LogUtil.i(TAG, "end=" + end + ",start=" + start + ",end should more than start!");
            return;
        }
        SpannableStringBuilder style = new SpannableStringBuilder(s);
        style.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        textView.setText(style);
    }


    /***
     * TextView设置spannable
     *
     * @param textView TextView控件
     * @param s        字符串
     * @param start    开始position
     * @param end      结束position
     * @param size     字体大小
     */
    public static void setSpannableSize(TextView textView, String s, int start, int end, int size) {
        if (s == null) {
            LogUtil.i(TAG, "error:" + "s=" + s);
            return;
        }
        if (start < 0 || end <= start) {
            LogUtil.i(TAG, "end=" + end + ",start=" + start + ",end should more than start!");
            return;
        }
        SpannableStringBuilder style = new SpannableStringBuilder(s);
        style.setSpan(new AbsoluteSizeSpan(size), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        textView.setText(style);
    }


    /**
     * http请求的参数进行URL编码
     */
    public static String URLEncode(String paramString) {
        if (StringUtil.isEmptyOrNull(paramString)) {
            return paramString;
        }
        try {
            String str = new String(paramString.getBytes(), "UTF-8");
            str = URLEncoder.encode(str, "UTF-8");
            return str;
        } catch (Exception localException) {
            localException.printStackTrace();
        }
        return paramString;
    }

    /**
     * URL解码
     */
    public static String URLDecode(String encodedString) {
        if (StringUtil.isEmptyOrNull(encodedString)) {
            return encodedString;
        }
        try {
            String str = new String(encodedString.getBytes(), "UTF-8");
            str = URLDecoder.decode(str, "UTF-8");
            return str;
        } catch (Exception localException) {
            localException.printStackTrace();
        }
        return encodedString;
    }

    /**
     * 浮点类型保留两位小数，四舍五入
     *
     * @param d
     * @return
     */
    public static Double interceptDouble(double d) {
        BigDecimal bd = new BigDecimal(d);
        return bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /** 给金钱添加分位符 */
    public static String markOperatorForMoney(String money) {
        if (money.contains(",")) {
            money = money.replace(",", "");
        }
        if (money.contains(".")) {
            int i = money.indexOf(".");
            return addOperator(money.substring(0, i)) + money.substring(i, money.length());
        }
        return addOperator(money);
    }

    private static String addOperator(String money) {
        if (StringUtil.isEmptyOrNull(money)) {
            return "";
        }
        int length = money.length();
        if (length < 4) {
            return money;
        }
        int extra = length % 3;
        int start = 0;
        int end = extra != 0 ? extra : 3;
        List<String> list = new ArrayList<>();
        while (end <= length) {
            String substring = money.substring(start, end);
            list.add(substring);
            start = end;
            end += 3;
        }
        String s = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            s = s + "," + list.get(i);
        }
        return s;
    }

    /**
     * 获取汉字拼音首字母
     */
    static final int GB_SP_DIFF = 160;
    // 存放国标一级汉字不同读音的起始区位码
    static final int[] secPosValueList = {1601, 1637, 1833, 2078, 2274, 2302,
            2433, 2594, 2787, 3106, 3212, 3472, 3635, 3722, 3730, 3858, 4027,
            4086, 4390, 4558, 4684, 4925, 5249, 5600};
    // 存放国标一级汉字不同读音的起始区位码对应读音
    static final char[] firstLetter = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
            'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'w', 'x',
            'y', 'z'};

    public static String getSpells(String characters) {
        StringBuffer buffer = new StringBuffer();
        if (!StringUtil.isEmptyOrNull(characters)) {
            char ch = characters.charAt(0);
            // 判断是否为汉字，如果左移7为为0就不是汉字，否则是汉字
            if ((ch >> 7) == 0) {
            } else {
                char spell = getFirstLetter(ch);
                buffer.append(String.valueOf(spell));
            }
        }
        return buffer.toString();
    }

    // 获取一个汉字的首字母
    public static Character getFirstLetter(char ch) {

        byte[] uniCode = null;
        try {
            uniCode = String.valueOf(ch).getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        if (uniCode[0] < 128 && uniCode[0] > 0) { // 非汉字
            return null;
        } else {
            return convert(uniCode);
        }
    }

    /**
     * 获取一个汉字的拼音首字母。 GB码两个字节分别减去160，转换成10进制码组合就可以得到区位码
     * 例如汉字“你”的GB码是0xC4/0xE3，分别减去0xA0（160）就是0x24/0x43
     * 0x24转成10进制就是36，0x43是67，那么它的区位码就是3667，在对照表中读音为‘n’
     */
    static char convert(byte[] bytes) {
        char result = '-';
        int secPosValue = 0;
        int i;
        for (i = 0; i < bytes.length; i++) {
            bytes[i] -= GB_SP_DIFF;
        }
        secPosValue = bytes[0] * 100 + bytes[1];
        for (i = 0; i < 23; i++) {
            if (secPosValue >= secPosValueList[i]
                    && secPosValue < secPosValueList[i + 1]) {
                result = firstLetter[i];
                break;
            }
        }
        return result;
    }
}
