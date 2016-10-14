package com.ypwl.xiaotouzi.event;

/**
 * function:记账平台选择事件
 * <p/>
 * Created by tengtao on 2015/12/4.
 */
public class JiZhangChooseEvent {
    public boolean hasChoose;
    public String company_name;
    public String company_pid;
    public String is_auto;

    public JiZhangChooseEvent(boolean hasChoose, String company_name, String company_pid,String is_auto){
        this.hasChoose = hasChoose;
        this.company_name = company_name;
        this.company_pid = company_pid;
        this.is_auto = is_auto;
    }
}
