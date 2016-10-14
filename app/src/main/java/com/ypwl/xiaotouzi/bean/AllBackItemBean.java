package com.ypwl.xiaotouzi.bean;

/**
 * function : 全部回款--日期list的item自定义数据对象
 * <p/>
 * Created by tengtao on 2016/3/24.
 */
public class AllBackItemBean {

    private String datetime;
    private String money;
    private String type;
    private String aid;
    private String capital;
    private String p_name;
    private String period;
    private String period_total;
    private String profit;
    private String project_name;
    private String return_time;
    private String rid;
    private String status;
    private String total;


    public Boolean getIsEmptyMonth() {
        return isEmptyMonth;
    }

    public void setIsEmptyMonth(Boolean isEmptyMonth) {
        this.isEmptyMonth = isEmptyMonth;
    }

    private Boolean isEmptyMonth;

    private String is_auto;

    public String getIs_auto() {
        return is_auto;
    }

    public void setIs_auto(String is_auto) {
        this.is_auto = is_auto;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public void setPeriod_total(String period_total) {
        this.period_total = period_total;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public void setReturn_time(String return_time) {
        this.return_time = return_time;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getDatetime() {
        return datetime;
    }

    public String getMoney() {
        return money;
    }

    public String getType() {
        return type;
    }

    public String getAid() {
        return aid;
    }

    public String getCapital() {
        return capital;
    }

    public String getP_name() {
        return p_name;
    }

    public String getPeriod() {
        return period;
    }

    public String getPeriod_total() {
        return period_total;
    }

    public String getProfit() {
        return profit;
    }

    public String getProject_name() {
        return project_name;
    }

    public String getReturn_time() {
        return return_time;
    }

    public String getRid() {
        return rid;
    }

    public String getStatus() {
        return status;
    }

    public String getTotal() {
        return total;
    }
}
