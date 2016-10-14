package com.ypwl.xiaotouzi.bean;

import java.util.List;

/**
 * function : 日历--最近回款数据模式对象.
 * <p/>
 * Created by lzj on 2015/11/9.
 */
public class CalendarBackMoneyBean {

    /**
     * status : 0
     * list : [
     * {"rid":"71","pid":"8","capital":"400.00","interest":"48.00","award":"0.00", "total":"448.00",
     * "period":"2","period_total":"3","return_time":"1444689900","p_name":"投哪网","p_logo":"logo_559f7d997c13b.jpg"}
     * ,
     * {"rid":"680","pid":"10","capital":"900.00","interest":"108.00","award":"0.00","total":"1,008.00",
     * "period":"2","period_total":"4","return_time":"1444691900","p_name":"小牛在线","p_logo":"logo_559f7ee418db0.jpg"}
     * ]
     */

    private int status;
    private List<ListEntity> list;

    public void setStatus(int status) {
        this.status = status;
    }

    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public int getStatus() {
        return status;
    }

    public List<ListEntity> getList() {
        return list;
    }

    public static class ListEntity {
        private String aid;
        private String rid;
        private String pid;
        private String p_logo;
        private String p_name;
        private String project_name;
        private String return_type;
        private String capital;
        private String interest;
        private String award;
        private String total;
        private String profit;
        private String period;
        private String period_total;
        private String return_time;
        private int is_auto;
        private int status;

        public int getStatus() {
            return status;
        }
        public String getProfit() {
            return profit;
        }

        public void setProfit(String profit) {
            this.profit = profit;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getAid() {
            return aid;
        }

        public void setAid(String aid) {
            this.aid = aid;
        }

        public String getRid() {
            return rid;
        }

        public void setRid(String rid) {
            this.rid = rid;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getP_logo() {
            return p_logo;
        }

        public void setP_logo(String p_logo) {
            this.p_logo = p_logo;
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

        public String getReturn_type() {
            return return_type;
        }

        public void setReturn_type(String return_type) {
            this.return_type = return_type;
        }

        public String getCapital() {
            return capital;
        }

        public void setCapital(String capital) {
            this.capital = capital;
        }

        public String getInterest() {
            return interest;
        }

        public void setInterest(String interest) {
            this.interest = interest;
        }

        public String getAward() {
            return award;
        }

        public void setAward(String award) {
            this.award = award;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

        public String getPeriod_total() {
            return period_total;
        }

        public void setPeriod_total(String period_total) {
            this.period_total = period_total;
        }

        public String getReturn_time() {
            return return_time;
        }

        public void setReturn_time(String return_time) {
            this.return_time = return_time;
        }

        public int getIs_auto() {
            return is_auto;
        }

        public void setIs_auto(int is_auto) {
            this.is_auto = is_auto;
        }
    }
}
