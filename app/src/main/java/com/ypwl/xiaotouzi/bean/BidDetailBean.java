package com.ypwl.xiaotouzi.bean;

import java.util.List;

/**
 * function : 标详情 数据模式对象.
 * <p/>
 * Created by lzj on 2015/11/9.
 */
public class BidDetailBean {
    /**
     * status : 0
     * pid : 2
     * p_name : 红岭创投
     * p_logo : logo_55ee57c7866dc.jpg
     * project_name : 上海微商贷
     * starttime : 1439877585
     * return_type : 4
     * rate : 12.50
     * rate_type : 1
     * time_limit : 15
     * time_type : 1
     * remark : 15081810471335596310720017371525
     * money : 50.00
     * rward : 0.00
     * rcapital : 0.00
     * rprofit : 7.57
     * rinterest : 2.36
     * rtotal : 2.36
     * stotal : 55.21
     * is_auto : 1
     * list : [{"return_time":"1442592000","capital":"0.00","profit":"0.48","total":"0.48","status":"1","smoney":"57.09"},{"return_time":"1445184000","capital":"0.00","profit":"0.46","total":"0.46","status":"1","smoney":"56.63"},{"return_time":"1447862400","capital":"0.00","profit":"0.48","total":"0.48","status":"1","smoney":"56.15"},{"return_time":"1450454400","capital":"0.00","profit":"0.46","total":"0.46","status":"1","smoney":"55.69"},{"return_time":"1453132800","capital":"0.00","profit":"0.48","total":"0.48","status":"1","smoney":"55.21"},{"return_time":"1455811200","capital":"0.00","profit":"0.53","total":"0.53","status":"0","smoney":"54.68"},{"return_time":"1458316800","capital":"0.00","profit":"0.50","total":"0.50","status":"0","smoney":"54.18"},{"return_time":"1460995200","capital":"0.00","profit":"0.53","total":"0.53","status":"0","smoney":"53.65"},{"return_time":"1463587200","capital":"0.00","profit":"0.51","total":"0.51","status":"0","smoney":"53.14"},{"return_time":"1466265600","capital":"0.00","profit":"0.53","total":"0.53","status":"0","smoney":"52.61"},{"return_time":"1468857600","capital":"0.00","profit":"0.51","total":"0.51","status":"0","smoney":"52.10"},{"return_time":"1471536000","capital":"0.00","profit":"0.53","total":"0.53","status":"0","smoney":"51.57"},{"return_time":"1474214400","capital":"0.00","profit":"0.53","total":"0.53","status":"0","smoney":"51.04"},{"return_time":"1476806400","capital":"0.00","profit":"0.51","total":"0.51","status":"0","smoney":"50.53"},{"return_time":"1479484800","capital":"50.00","profit":"0.53","total":"50.53","status":"0","smoney":"0.00"}]
     * tb_status : 1454200840
     * ret_msg :
     */

    private int status;
    private String pid;
    private String p_name;
    private String p_logo;
    private String project_name;
    private String starttime;
    private String return_type;
    private String rate;
    private String rate_type;
    private String time_limit;
    private String time_type;
    private String remark;
    private String money;
    private String rward;
    private String rcapital;
    private String rprofit;
    private String rinterest;
    private String rtotal;
    private String stotal;
    private String is_auto;
    private String tb_status;
    private String ret_msg;
    /**
     * return_time : 1442592000
     * capital : 0.00
     * profit : 0.48
     * total : 0.48
     * status : 1
     * smoney : 57.09
     */

    private List<ListEntity> list;

    public void setStatus(int status) {
        this.status = status;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public void setP_logo(String p_logo) {
        this.p_logo = p_logo;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public void setReturn_type(String return_type) {
        this.return_type = return_type;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public void setRate_type(String rate_type) {
        this.rate_type = rate_type;
    }

    public void setTime_limit(String time_limit) {
        this.time_limit = time_limit;
    }

    public void setTime_type(String time_type) {
        this.time_type = time_type;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public void setRward(String rward) {
        this.rward = rward;
    }

    public void setRcapital(String rcapital) {
        this.rcapital = rcapital;
    }

    public void setRprofit(String rprofit) {
        this.rprofit = rprofit;
    }

    public void setRinterest(String rinterest) {
        this.rinterest = rinterest;
    }

    public void setRtotal(String rtotal) {
        this.rtotal = rtotal;
    }

    public void setStotal(String stotal) {
        this.stotal = stotal;
    }

    public void setIs_auto(String is_auto) {
        this.is_auto = is_auto;
    }

    public void setTb_status(String tb_status) {
        this.tb_status = tb_status;
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

    public String getPid() {
        return pid;
    }

    public String getP_name() {
        return p_name;
    }

    public String getP_logo() {
        return p_logo;
    }

    public String getProject_name() {
        return project_name;
    }

    public String getStarttime() {
        return starttime;
    }

    public String getReturn_type() {
        return return_type;
    }

    public String getRate() {
        return rate;
    }

    public String getRate_type() {
        return rate_type;
    }

    public String getTime_limit() {
        return time_limit;
    }

    public String getTime_type() {
        return time_type;
    }

    public String getRemark() {
        return remark;
    }

    public String getMoney() {
        return money;
    }

    public String getRward() {
        return rward;
    }

    public String getRcapital() {
        return rcapital;
    }

    public String getRprofit() {
        return rprofit;
    }

    public String getRinterest() {
        return rinterest;
    }

    public String getRtotal() {
        return rtotal;
    }

    public String getStotal() {
        return stotal;
    }

    public String getIs_auto() {
        return is_auto;
    }

    public String getTb_status() {
        return tb_status;
    }

    public String getRet_msg() {
        return ret_msg;
    }

    public List<ListEntity> getList() {
        return list;
    }

    public static class ListEntity {
        private String return_time;
        private String capital;
        private String profit;
        private String total;
        private String status;
        private String smoney;

        public void setReturn_time(String return_time) {
            this.return_time = return_time;
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

        public void setStatus(String status) {
            this.status = status;
        }

        public void setSmoney(String smoney) {
            this.smoney = smoney;
        }

        public String getReturn_time() {
            return return_time;
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

        public String getStatus() {
            return status;
        }

        public String getSmoney() {
            return smoney;
        }
    }
}
