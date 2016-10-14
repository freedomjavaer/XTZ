package com.ypwl.xiaotouzi.bean;

import java.util.List;

/**
 * function : 单个标的回款详情网络请求数据对象
 * <p/>
 * Created by tengtao on 2016/3/28.
 */
public class SingleBidBackMoneyBean {


    /**
     * status : 0
     * p_name : 星际
     * project_name : 20160118
     * return_name : 等额本息
     * rmoney : 22,528.48
     * rcapital : 22,069.69
     * rprofit : 458.79
     * smoney : 33,792.72
     * scapital : 33,485.31
     * sprofit : 307.41
     * list : [{"rid":"30865","capital":"11009.61","profit":"254.63","total":"11264.24","period":"1","period_total":"5","return_time":"1455724800","status":"1"},{"rid":"30866","capital":"11060.08","profit":"204.16","total":"11264.24","period":"2","period_total":"5","return_time":"1458230400","status":"1"},{"rid":"30867","capital":"11110.77","profit":"153.47","total":"11264.24","period":"3","period_total":"5","return_time":"1460908800","status":"0"},{"rid":"30868","capital":"11161.69","profit":"102.55","total":"11264.24","period":"4","period_total":"5","return_time":"1463500800","status":"0"},{"rid":"30869","capital":"11212.85","profit":"51.39","total":"11264.24","period":"5","period_total":"5","return_time":"1466179200","status":"0"}]
     * ret_msg :
     */

    private int status;
    private String p_name;
    private String project_name;
    private String return_name;
    private String rmoney;
    private String rcapital;
    private String rprofit;
    private String smoney;
    private String scapital;
    private String sprofit;
    private String ret_msg;

    private String is_auto;
    private String update_time;
    private String pid;

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getIs_auto() {
        return is_auto;
    }

    public void setIs_auto(String is_auto) {
        this.is_auto = is_auto;
    }
    /**
     * rid : 30865
     * capital : 11009.61
     * profit : 254.63
     * total : 11264.24
     * period : 1
     * period_total : 5
     * return_time : 1455724800
     * status : 1
     */

    private List<ListEntity> list;

    public void setStatus(int status) {
        this.status = status;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public void setReturn_name(String return_name) {
        this.return_name = return_name;
    }

    public void setRmoney(String rmoney) {
        this.rmoney = rmoney;
    }

    public void setRcapital(String rcapital) {
        this.rcapital = rcapital;
    }

    public void setRprofit(String rprofit) {
        this.rprofit = rprofit;
    }

    public void setSmoney(String smoney) {
        this.smoney = smoney;
    }

    public void setScapital(String scapital) {
        this.scapital = scapital;
    }

    public void setSprofit(String sprofit) {
        this.sprofit = sprofit;
    }

    public void setRet_msg(String ret_msg) {
        this.ret_msg = ret_msg;
    }

    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public int getStatus() {
        return status;
    }

    public String getP_name() {
        return p_name;
    }

    public String getProject_name() {
        return project_name;
    }

    public String getReturn_name() {
        return return_name;
    }

    public String getRmoney() {
        return rmoney;
    }

    public String getRcapital() {
        return rcapital;
    }

    public String getRprofit() {
        return rprofit;
    }

    public String getSmoney() {
        return smoney;
    }

    public String getScapital() {
        return scapital;
    }

    public String getSprofit() {
        return sprofit;
    }

    public String getRet_msg() {
        return ret_msg;
    }

    public List<ListEntity> getList() {
        return list;
    }

    public static class ListEntity {
        private String rid;
        private String capital;
        private String profit;
        private String total;
        private String period;
        private String period_total;
        private String return_time;
        private String status;

        public void setRid(String rid) {
            this.rid = rid;
        }

        public void setCapital(String capital) {
            this.capital = capital;
        }

        public void setProfit(String profit) {
            this.profit = profit;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

        public void setPeriod_total(String period_total) {
            this.period_total = period_total;
        }

        public void setReturn_time(String return_time) {
            this.return_time = return_time;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getRid() {
            return rid;
        }

        public String getCapital() {
            return capital;
        }

        public String getProfit() {
            return profit;
        }

        public String getTotal() {
            return total;
        }

        public String getPeriod() {
            return period;
        }

        public String getPeriod_total() {
            return period_total;
        }

        public String getReturn_time() {
            return return_time;
        }

        public String getStatus() {
            return status;
        }
    }
}
