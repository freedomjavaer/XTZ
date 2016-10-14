package com.ypwl.xiaotouzi.bean;

/**
 * 项目名:	XTZ
 * 包名:	com.ypwl.xiaotouzi.bean
 * 类名:	ChoiceBean
 * 作者:	罗霄
 * 创建时间:	2016/4/18 14:37
 * <p/>
 * 描述:	金融超市--精选数据javabean
 * <p/>
 * svn版本:	$Revision: 15120 $
 * 更新人:	$Author: luoxiao $
 * 更新时间:	$Date: 2016-05-26 14:42:19 +0800 (周四, 26 五月 2016) $
 * 更新描述:	$Message$
 */
public class ChoiceBean {

    /**
     * pid : 1
     * project_id : 1
     * p_name : 诚汇通
     * project_name : 测试一
     * rate : 12.00
     * time_limit : 3
     * time_type : 1
     * at_type : "0" 加息类型 0：没有加息 1：首投加息 2：加息
     * add_interest : 0.00
     * p_logo
     */

    private String project_id;
    private String p_name;
    private String project_name;
    private String rate;
    private String time_limit;
    private String time_type;
    private String add_interest;
    private String at_type;
    private String pid;
    private String p_logo;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getAt_type() {
        return at_type;
    }

    public void setAt_type(String at_type) {
        this.at_type = at_type;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getTime_limit() {
        return time_limit;
    }

    public void setTime_limit(String time_limit) {
        this.time_limit = time_limit;
    }

    public String getTime_type() {
        return time_type;
    }

    public void setTime_type(String time_type) {
        this.time_type = time_type;
    }

    public String getAdd_interest() {
        return add_interest;
    }

    public void setAdd_interest(String add_interest) {
        this.add_interest = add_interest;
    }

    public String getP_logo() {
        return p_logo;
    }

    public void setP_logo(String p_logo) {
        this.p_logo = p_logo;
    }
}
