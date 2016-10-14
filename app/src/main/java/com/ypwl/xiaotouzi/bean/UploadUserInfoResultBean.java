package com.ypwl.xiaotouzi.bean;

/**
 * function : 上传个人信息返回结果数据对象.
 * <p/>
 * Created by lzj on 2016/1/27.
 */
public class UploadUserInfoResultBean {
    private int status;
    private String ret_msg;
    private String avatarUrl;
    private String nickname;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRet_msg() {
        return ret_msg;
    }

    public void setRet_msg(String ret_msg) {
        this.ret_msg = ret_msg;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
