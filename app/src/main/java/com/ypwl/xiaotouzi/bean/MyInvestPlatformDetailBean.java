package com.ypwl.xiaotouzi.bean;

import java.util.List;

/**
 * function : 我的投资-资产-平台详情数据对象
 * <p/>
 * Created by lzj on 2016/3/24.
 */
public class MyInvestPlatformDetailBean {

    private int status;
    private String ret_msg;
    private String pid;
    private String p_name;
    private String stotal;
    private String profit;
    private String wannual;
    private String money;
    private String income;
    private String rate;
    private int is_auto;
    private long update_time;
    private List<ListEntity> list;
    private String scapital;

    public String getScapital() {
        return scapital;
    }

    public void setScapital(String scapital) {
        this.scapital = scapital;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public int getIs_auto() {
        return is_auto;
    }

    public void setIs_auto(int is_auto) {
        this.is_auto = is_auto;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRet_msg() {
        return ret_msg;
    }

    public void setRet_msg(String ret_msg) {
        this.ret_msg = ret_msg;
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

    public String getStotal() {
        return stotal;
    }

    public void setStotal(String stotal) {
        this.stotal = stotal;
    }

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }

    public String getWannual() {
        return wannual;
    }

    public void setWannual(String wannual) {
        this.wannual = wannual;
    }

    public List<ListEntity> getList() {
        return list;
    }

    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public long getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(long update_time) {
        this.update_time = update_time;
    }

    public static class ListEntity {
        private String aid;
        private String project_name;
        private String money;
        private String profit;
        private String rate;
        private String stotal;
        private long starttime;
        private String scapital;
        private int is_auto;

        public int getIs_auto() {
            return is_auto;
        }

        public void setIs_auto(int is_auto) {
            this.is_auto = is_auto;
        }

        public String getScapital() {
            return scapital;
        }

        public void setScapital(String scapital) {
            this.scapital = scapital;
        }

        public String getAid() {
            return aid;
        }

        public void setAid(String aid) {
            this.aid = aid;
        }

        public String getProject_name() {
            return project_name;
        }

        public void setProject_name(String project_name) {
            this.project_name = project_name;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getProfit() {
            return profit;
        }

        public void setProfit(String profit) {
            this.profit = profit;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getStotal() {
            return stotal;
        }

        public void setStotal(String stotal) {
            this.stotal = stotal;
        }

        public long getStarttime() {
            return starttime;
        }

        public void setStarttime(long starttime) {
            this.starttime = starttime;
        }
    }
}
