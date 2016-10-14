package com.ypwl.xiaotouzi.bean;

/**
 * function : 新闻回复和投友圈回复对象
 * <p/>
 * Created by tengtao on 2016/3/18.
 */
public class NewsReplyBean {

    private int status;
    private String ret_msg;

    public void setStatus(int status) {
        this.status = status;
    }

    public void setRet_msg(String ret_msg) {
        this.ret_msg = ret_msg;
    }

    public int getStatus() {
        return status;
    }

    public String getRet_msg() {
        return ret_msg;
    }
}
