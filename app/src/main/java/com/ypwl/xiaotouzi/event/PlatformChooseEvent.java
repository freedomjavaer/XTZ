package com.ypwl.xiaotouzi.event;

/**
 * function :
 * <p/>
 * Created by tengtao on 2016/5/6.
 */
public class PlatformChooseEvent {

    private String p_name;//平台名称
    private String pid;//平台id
    private boolean isSave;//是否保存记录
    private boolean isTally;//是否为记账选择

    public PlatformChooseEvent(String p_name,String pid,boolean isSave, boolean isTally){
        this.p_name = p_name;
        this.pid = pid;
        this.isSave = isSave;
        this.isTally = isTally;
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

    public boolean isSave() {
        return isSave;
    }

    public void setSave(boolean save) {
        isSave = save;
    }

    public boolean isTally() {
        return isTally;
    }

    public void setTally(boolean tally) {
        isTally = tally;
    }
}
