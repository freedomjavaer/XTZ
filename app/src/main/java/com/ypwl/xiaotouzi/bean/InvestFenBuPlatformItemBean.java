package com.ypwl.xiaotouzi.bean;

/**
 * function :投资分析--投资分布--平台  条目数据对象
 * <p/>
 * Created by tengtao on 2016/4/11.
 */
public class InvestFenBuPlatformItemBean {
    private String money;
    private String number;
    private String p_name;
    private String percent;
    private String pid;
    private String type;
    private String return_name;//还款方式

    private String aid;
    private String bid_money;
    private String project_name;
    private String data_p_name;
    private String rate;
    private String time_limit;//投资期限
    private String time_type;//期限类型

    private int showStatus;//0：正常，1：打开

    public int getShowStatus() {
        return showStatus;
    }

    public void setShowStatus(int showStatus) {
        this.showStatus = showStatus;
    }
    public String getReturn_name() {
        return return_name;
    }

    public void setReturn_name(String return_name) {
        this.return_name = return_name;
    }

    public String getData_p_name() {
        return data_p_name;
    }

    public void setData_p_name(String data_p_name) {
        this.data_p_name = data_p_name;
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

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getAid() {
        return aid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getBid_money() {
        return bid_money;
    }

    public void setBid_money(String bid_money) {
        this.bid_money = bid_money;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }
}
