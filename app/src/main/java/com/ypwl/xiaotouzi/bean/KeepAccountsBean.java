package com.ypwl.xiaotouzi.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @项目名: XTZ
 * @包名: com.ypwl.xiaotouzi.bean
 * @类名: KeepAccountsBean
 * @作者: 罗霄
 * @创建时间: 2016/3/26 12:50
 * @描述: 记账页面的数据模版类
 * @svn版本: $$REV$$
 * @更新人: $$AUTHOR$$
 * @更新时间: $2016/3/26$$
 * @更新描述:
 */
public class KeepAccountsBean {

//    aid	投资ID		新增传0，编辑传对应ID
//    pid	平台ID		如果是新的自定义平台，传0
//    project_name	项目名称
//    starttime	起息时间戳
//    money	投资金额
//    return_type	还款方式		1等额本息
//    2等额本金
//    3到期还本息
//    4按月付息，到期还本
//    time_limit	投资期限
//    time_type	投资期限类型		1月，2天
//    rate	投资利率
//    rate_type	投资利率类型		1年化，2日化
//    tender_award	投资奖励
//    tender_award_type	投资奖励类型		1投标回，2每期回，3到期回
//    extra_award	额外奖金
//    extra_award_type	额外奖金类型		1投标回，2每期回
//    cost	管理费
//    remark	自定义备注
//    custom_pname	自定义平台名称		pid为0时，需要传这个值。pid大于0时，传空字符串。
//    p_name	平台名称
//    is_year	一年按365天		0：否，1：是
//    is_thirty	30天算一个月		0：否，1:是
//    award_capital	奖励本金
//    award_capital_type	奖励本金类型		1：百分比，2元
//    cash_cost	取现手续费		元
//    auto_payment	是否自动还款		0：否，1：是
//    standard_year  是否一年按360天		0：否，1：是

    private String p_name;
    private String aid;
    private String pid;
    private String project_name;
    private String starttime;
    private String money;
    private String return_type;
    private String time_limit;
    private String time_type;
    private String rate;
    private String rate_type;
    private String tender_award;
    private String tender_award_type;
    private String extra_award;
    private String extra_award_type;
    private String cost;
    private String remark;
    private String custom_pname;
    private String is_year;
    private String is_thirty;
    private String award_capital;
    private String award_capital_type;
    private String cash_cost;
    private String auto_payment;
    private String standard_year;
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStandard_year() {
        return standard_year;
    }

    public void setStandard_year(String standard_year) {
        this.standard_year = standard_year;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getReturn_type() {
        return return_type;
    }

    public void setReturn_type(String return_type) {
        this.return_type = return_type;
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

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getRate_type() {
        return rate_type;
    }

    public void setRate_type(String rate_type) {
        this.rate_type = rate_type;
    }

    public String getTender_award() {
        return tender_award;
    }

    public void setTender_award(String tender_award) {
        this.tender_award = tender_award;
    }

    public String getTender_award_type() {
        return tender_award_type;
    }

    public void setTender_award_type(String tender_award_type) {
        this.tender_award_type = tender_award_type;
    }

    public String getExtra_award() {
        return extra_award;
    }

    public void setExtra_award(String extra_award) {
        this.extra_award = extra_award;
    }

    public String getExtra_award_type() {
        return extra_award_type;
    }

    public void setExtra_award_type(String extra_award_type) {
        this.extra_award_type = extra_award_type;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCustom_pname() {
        return custom_pname;
    }

    public void setCustom_pname(String custom_pname) {
        this.custom_pname = custom_pname;
    }

    public String getIs_year() {
        return is_year;
    }

    public void setIs_year(String is_year) {
        this.is_year = is_year;
    }

    public String getIs_thirty() {
        return is_thirty;
    }

    public void setIs_thirty(String is_thirty) {
        this.is_thirty = is_thirty;
    }

    public String getAward_capital() {
        return award_capital;
    }

    public void setAward_capital(String award_capital) {
        this.award_capital = award_capital;
    }

    public String getAward_capital_type() {
        return award_capital_type;
    }

    public void setAward_capital_type(String award_capital_type) {
        this.award_capital_type = award_capital_type;
    }

    public String getCash_cost() {
        return cash_cost;
    }

    public void setCash_cost(String cash_cost) {
        this.cash_cost = cash_cost;
    }

    public String getAuto_payment() {
        return auto_payment;
    }

    public void setAuto_payment(String auto_payment) {
        this.auto_payment = auto_payment;
    }


}
