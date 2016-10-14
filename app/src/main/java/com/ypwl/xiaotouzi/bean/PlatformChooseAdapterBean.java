package com.ypwl.xiaotouzi.bean;

/**
 * function:
 * <p/>
 * Created by tengtao on 2015/11/28.
 */
public class PlatformChooseAdapterBean {

    private String pid;
    private String p_name;
    private String date;
    private boolean delete;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }
}
