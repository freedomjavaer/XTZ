package com.ypwl.xiaotouzi.bean;

import java.util.List;

/**
 * function :历史咨询（默认）
 * Created by llc on 2016/3/28 14:00
 * Email：licailuo@qq.com
 */
public class InvestHistoryBean {


    /**
     * status : 0
     * tprofit : 156.75
     * wannual : 36.27
     * list : [{"aid":"4266","project_name":"汽车连锁维修店资金周转(到期还本第3标)","is_auto":"1","money":"100.71","rate":"16.00","profit":"1.34","pid":"1","return_time":"1456243200","p_name":"诚汇通"},{"aid":"4263","project_name":"物流公司资金周转（第4标）过夜标","is_auto":"1","money":"50.00","rate":"21.40","profit":"3.92","pid":"1","return_time":"1455724800","p_name":"诚汇通"},{"aid":"3895","project_name":"2015-12-8","is_auto":"0","money":"111.00","rate":"1.00","profit":"1.11","pid":"646","return_time":"1452182400","p_name":"中大财富"},{"aid":"3892","project_name":"2015-11-8","is_auto":"0","money":"100.00","rate":"1.00","profit":"1.00","pid":"792","return_time":"1449504000","p_name":"点融网"},{"aid":"4264","project_name":"【活动标】电子科技公司资金周转（到期还本第9标）","is_auto":"1","money":"70.00","rate":"20.40","profit":"3.57","pid":"1","return_time":"1447776000","p_name":"诚汇通"},{"aid":"3893","project_name":"2015-10-1","is_auto":"0","money":"100.00","rate":"15.00","profit":"3.03","pid":"1000033","return_time":"1446307200","p_name":"啊我"},{"aid":"3880","project_name":"2015-10-1","is_auto":"0","money":"100.00","rate":"1.00","profit":"3.00","pid":"100","return_time":"1446307200","p_name":"宜人贷"},{"aid":"3907","project_name":"2015-8-3","is_auto":"0","money":"100.00","rate":"1.00","profit":"3.00","pid":"1000035","return_time":"1441209600","p_name":"@123"},{"aid":"3896","project_name":"2015-8-3","is_auto":"0","money":"100.00","rate":"1.00","profit":"2.00","pid":"1000034","return_time":"1441209600","p_name":"什"},{"aid":"3891","project_name":"2015-8-2","is_auto":"0","money":"100.00","rate":"1.00","profit":"1.00","pid":"2","return_time":"1441123200","p_name":"红岭创投"},{"aid":"3894","project_name":"2015-7-1","is_auto":"0","money":"100.00","rate":"1.00","profit":"100.17","pid":"2057","return_time":"1438358400","p_name":"企额贷"},{"aid":"3882","project_name":"看看咯","is_auto":"0","money":"1,000.00","rate":"1.00","profit":"21.02","pid":"100","return_time":"1422720000","p_name":"宜人贷"},{"aid":"3869","project_name":"测试","is_auto":"0","money":"100.00","rate":"720.00","profit":"7.19","pid":"66","return_time":"1420041600","p_name":"陆金所"},{"aid":"3875","project_name":"为啥","is_auto":"0","money":"200.00","rate":"2.00","profit":"4.01","pid":"22","return_time":"1412092800","p_name":"人人贷"},{"aid":"3904","project_name":"2014-8-5","is_auto":"0","money":"15.00","rate":"360.00","profit":"1.39","pid":"698","return_time":"1409846400","p_name":"点点理财"}]
     * ret_msg :
     */

    private int status;
    private String tprofit;
    private String wannual;
    private String ret_msg;
    /**
     * aid : 4266
     * project_name : 汽车连锁维修店资金周转(到期还本第3标)
     * is_auto : 1
     * money : 100.71
     * rate : 16.00
     * profit : 1.34
     * pid : 1
     * return_time : 1456243200
     * p_name : 诚汇通
     */

    private List<ListEntity> list;

    public void setStatus(int status) {
        this.status = status;
    }

    public void setTprofit(String tprofit) {
        this.tprofit = tprofit;
    }

    public void setWannual(String wannual) {
        this.wannual = wannual;
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

    public String getTprofit() {
        return tprofit;
    }

    public String getWannual() {
        return wannual;
    }

    public String getRet_msg() {
        return ret_msg;
    }

    public List<ListEntity> getList() {
        return list;
    }

    public static class ListEntity {
        private String aid;
        private String project_name;
        private String is_auto;
        private String money;
        private String rate;
        private String profit;
        private String pid;
        private String return_time;
        private String p_name;

        public void setAid(String aid) {
            this.aid = aid;
        }

        public void setProject_name(String project_name) {
            this.project_name = project_name;
        }

        public void setIs_auto(String is_auto) {
            this.is_auto = is_auto;
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

        public void setPid(String pid) {
            this.pid = pid;
        }

        public void setReturn_time(String return_time) {
            this.return_time = return_time;
        }

        public void setP_name(String p_name) {
            this.p_name = p_name;
        }

        public String getAid() {
            return aid;
        }

        public String getProject_name() {
            return project_name;
        }

        public String getIs_auto() {
            return is_auto;
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

        public String getPid() {
            return pid;
        }

        public String getReturn_time() {
            return return_time;
        }

        public String getP_name() {
            return p_name;
        }
    }
}
