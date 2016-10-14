package com.ypwl.xiaotouzi.bean;

/**
 * 资产实体里抽取改善的实体
 *
 * Created by PDK on 2016/3/25.
 */
public class AssetsAdapterBean {

    private String pid;
    private String p_name;
    private String project_name;
    private String total_money;
    private String money;
    private String rate;
    private String total_profit;
    private String profit;
    private String stotal;
    private String totalStotal;
    private String starttime;
    private String aid;
    private String is_auto;
    private String scapital;

    public String getTotalStotal() {
        return totalStotal;
    }

    public void setTotalStotal(String totalStotal) {
        this.totalStotal = totalStotal;
    }

    private String platformStotal;
    private String p_logo;
    private String num;

    public String getP_logo() {
        return p_logo;
    }

    public void setP_logo(String p_logo) {
        this.p_logo = p_logo;
    }

    private int showStatus;//0：正常，1：打开

    public String getPlatformStotal() {
        return platformStotal;
    }

    public void setPlatformStotal(String platformStotal) {
        this.platformStotal = platformStotal;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getScapital() {

        return scapital;
    }


    public void setScapital(String scapital) {
        this.scapital = scapital;
    }

    public int getShowStatus() {
        return showStatus;
    }

    public void setShowStatus(int showStatus) {
        this.showStatus = showStatus;
    }


    public String getIs_auto() {
        return is_auto;
    }

    public void setIs_auto(String is_auto) {
        this.is_auto = is_auto;
    }

    public String getTotal_profit() {
        return total_profit;
    }

    public void setTotal_profit(String total_profit) {
        this.total_profit = total_profit;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public void setTotal_money(String total_money){
        this.total_money = total_money;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }

    public void setStotal(String stotal) {
        this.stotal = stotal;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getPid() {
        return pid;
    }

    public String getP_name() {
        return p_name;
    }

    public String getProject_name() {
        return project_name;
    }

    public String getMoney() {
        return money;
    }

    public String getRate() {
        return rate;
    }

    public String getProfit() {
        return profit;
    }

    public String getStotal() {
        return stotal;
    }

    public String getTotal_money() {
        return total_money;
    }

    public String getStarttime() {
        return starttime;
    }

}
