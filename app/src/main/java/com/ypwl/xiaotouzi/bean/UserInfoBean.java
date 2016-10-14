package com.ypwl.xiaotouzi.bean;

/**
 * Created by MYQ on 2015/10/5.
 * 授权用户信息信息java表
 */
public class UserInfoBean {
    private String userIcon;
    private String userName;
    private Gender userGender;
    private String userNote;

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Gender getUserGender() {
        return userGender;
    }

    public void setUserGender(Gender userGender) {
        this.userGender = userGender;
    }

    public String getUserNote() {
        return userNote;
    }

    public void setUserNote(String userNote) {
        this.userNote = userNote;
    }

    public enum Gender {BOY, GIRL}
}
