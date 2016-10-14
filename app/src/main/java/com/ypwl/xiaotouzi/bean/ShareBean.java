package com.ypwl.xiaotouzi.bean;


/**
 * function : 分享信息数据对象.
 * <p/>
 * Created by lzj on 2016/5/4.
 */
public class ShareBean {
    private int status;
    private String ret_msg;
    private String title;
    private String content;
    private String url;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
