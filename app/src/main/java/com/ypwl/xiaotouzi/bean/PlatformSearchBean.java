package com.ypwl.xiaotouzi.bean;

import java.util.List;

/**
 * function:
 * <p/>
 * Created by tengtao on 2015/11/30.
 */
public class PlatformSearchBean {

    public String status;
    public List<HandTallyBean> list;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<HandTallyBean> getList() {
        return list;
    }

    public void setList(List<HandTallyBean> list) {
        this.list = list;
    }
}
