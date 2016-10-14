package com.ypwl.xiaotouzi.bean;

/**
 * 项目名:	XTZ
 * 包名:	com.ypwl.xiaotouzi.bean
 * 类名:	TargetDetailBean
 * 作者:	罗霄
 * 创建时间:	2016/4/20 16:07
 * <p/>
 * 描述:	金融超市 -- 标的详情页面
 * <p/>
 * svn版本:	$Revision: 14164 $
 * 更新人:	$Author: luoxiao $
 * 更新时间:	$Date: 2016-04-20 17:58:38 +0800 (周三, 20 四月 2016) $
 * 更新描述:	$Message$
 */
public class TargetDetailBean {

    /**
     * status : 0
     * project_name : null
     * rate : null
     * time_limit : null
     * time_type : null
     * return_name : null
     * at_type : 0
     * add_interest : 0
     * description : null
     * ret_msg :
     */

    private int status;
    private String project_name;
    private String rate;
    private String time_limit;
    private String time_type;
    private String return_name;
    private String at_type;
    private String add_interest;
    private String description;
    private String ret_msg;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public String getReturn_name() {
        return return_name;
    }

    public void setReturn_name(String return_name) {
        this.return_name = return_name;
    }

    public String getAt_type() {
        return at_type;
    }

    public void setAt_type(String at_type) {
        this.at_type = at_type;
    }

    public String getAdd_interest() {
        return add_interest;
    }

    public void setAdd_interest(String add_interest) {
        this.add_interest = add_interest;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRet_msg() {
        return ret_msg;
    }

    public void setRet_msg(String ret_msg) {
        this.ret_msg = ret_msg;
    }
}
