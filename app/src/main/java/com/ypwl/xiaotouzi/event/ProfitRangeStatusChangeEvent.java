package com.ypwl.xiaotouzi.event;

/**
 * 收益排行状态改变
 *
 * Created by PDK on 2016/4/13.
 */
public class ProfitRangeStatusChangeEvent {

    private int status;
    private int type;
    public ProfitRangeStatusChangeEvent(int status,int type) {
        this.status = status;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {

        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
