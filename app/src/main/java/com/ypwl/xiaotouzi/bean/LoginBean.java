package com.ypwl.xiaotouzi.bean;

/**
 * function : 登录用户信息模式对象.<br/>
 * Modify by lzj on 2015/11/4.
 */
@SuppressWarnings("unused")
public class LoginBean {

    /**
     * qq_nick :
     * returnedMoneyFlag : 1
     * bindWeixinFlag : 0
     * avatarUrl :
     * openid : AF9EBD8C-E908-D56E-80D6-24D0E041CD98
     * bindSinaFlag : 0
     * bindQQFlag : 0
     * sign : 487f916f74a0d6db111bf0ac81f010414e080558
     * token : 55f66e4ae310b
     * appRecommendFlag : 1
     * nickname :
     * wx_nick :
     * wb_nick :
     * safeFlag : 1
     * is_invite_used : 1
     * userid : 用户唯一id
     * status : 0
     */
    private String qq_nick;
    private int returnedMoneyFlag;
    private int bindWeixinFlag;
    private String avatarUrl;
    private int bindSinaFlag;
    private int bindQQFlag;
    private String token;
    private int appRecommendFlag;
    private String nickname;
    private String wx_nick;
    private String wb_nick;
    private int safeFlag;
    private int status;

    private String activityTitle;
    private String activityUrl;
    private String phone;
    private String invite_code;
    private String g_password;
    private int g_status;

    private String userid;


    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }


    public int getG_status() {
        return g_status;
    }

    public void setG_status(int g_status) {
        this.g_status = g_status;
    }

    public String getG_password() {
        return g_password;
    }

    public void setG_password(String g_password) {
        this.g_password = g_password;
    }

    public void setInvite_code(String invite_code) {
        this.invite_code = invite_code;
    }

    public void setQq_nick(String qq_nick) {
        this.qq_nick = qq_nick;
    }

    public void setReturnedMoneyFlag(int returnedMoneyFlag) {
        this.returnedMoneyFlag = returnedMoneyFlag;
    }

    public void setBindWeixinFlag(int bindWeixinFlag) {
        this.bindWeixinFlag = bindWeixinFlag;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }

    public void setActivityUrl(String activityUrl) {
        this.activityUrl = activityUrl;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setBindSinaFlag(int bindSinaFlag) {
        this.bindSinaFlag = bindSinaFlag;
    }

    public void setBindQQFlag(int bindQQFlag) {
        this.bindQQFlag = bindQQFlag;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setAppRecommendFlag(int appRecommendFlag) {
        this.appRecommendFlag = appRecommendFlag;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setWx_nick(String wx_nick) {
        this.wx_nick = wx_nick;
    }

    public void setWb_nick(String wb_nick) {
        this.wb_nick = wb_nick;
    }

    public void setSafeFlag(int safeFlag) {
        this.safeFlag = safeFlag;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInvite_code() {
        return invite_code;
    }

    public String getQq_nick() {
        return qq_nick;
    }

    public int getReturnedMoneyFlag() {
        return returnedMoneyFlag;
    }

    public int getBindWeixinFlag() {
        return bindWeixinFlag;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getPhone() {
        return phone;
    }

    public String getActivityUrl() {
        return activityUrl;
    }

    public String getActivityTitle() {
        return activityTitle;
    }


    public int getBindSinaFlag() {
        return bindSinaFlag;
    }

    public int getBindQQFlag() {
        return bindQQFlag;
    }

    public String getToken() {
        return token;
    }

    public int getAppRecommendFlag() {
        return appRecommendFlag;
    }

    public String getNickname() {
        return nickname;
    }

    public String getWx_nick() {
        return wx_nick;
    }

    public String getWb_nick() {
        return wb_nick;
    }

    public int getSafeFlag() {
        return safeFlag;
    }

    public int getStatus() {
        return status;
    }
}
