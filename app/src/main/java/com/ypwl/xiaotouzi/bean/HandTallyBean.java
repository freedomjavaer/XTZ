package com.ypwl.xiaotouzi.bean;

/**
 * function:手动记账bean
 * <p/>
 * Created by tengtao on 2015/12/24.
 */
public class HandTallyBean {
    public String pid;
    public String p_name;
    public String is_auto;
    public int is_bind;

    public int getIs_bind() {
        return is_bind;
    }

    public void setIs_bind(int is_bind) {
        this.is_bind = is_bind;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public String getIs_auto() {
        return is_auto;
    }

    public void setIs_auto(String is_auto) {
        this.is_auto = is_auto;
    }
}
