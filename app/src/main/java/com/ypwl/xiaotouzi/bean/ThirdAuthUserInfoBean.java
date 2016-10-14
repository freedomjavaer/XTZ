package com.ypwl.xiaotouzi.bean;


/**
 * function : 第三方登录平台用户信息.
 * <p/>
 * Created by lzj on 2016/5/3.
 */
public class ThirdAuthUserInfoBean {
    public String openid;
    public String username;
    public String avatarUrl;

    public ThirdAuthUserInfoBean() {
    }

    public ThirdAuthUserInfoBean(String openid, String username, String avatarUrl) {
        this.openid = openid;
        this.username = username;
        this.avatarUrl = avatarUrl;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    @Override
    public String toString() {
        return "{" +
                "openid='" + openid + '\'' +
                ", username='" + username + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                '}';
    }
}
