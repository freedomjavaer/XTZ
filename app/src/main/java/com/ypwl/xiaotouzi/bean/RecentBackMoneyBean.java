package com.ypwl.xiaotouzi.bean;

import java.util.List;

/**
 * function : 我的投资--近期回款数据对象
 * <p/>
 * Created by tengtao on 2016/3/22.
 */
public class RecentBackMoneyBean  {


    /**
     * list : [{"addtime":"2016-01","data":[{"aid":"2743","capital":"0.00","p_name":"PPmoney","period":"1","period_total":"1","profit":"14.61","project_name":"体验标(GZ160123152507)","return_time":"1453996800","rid":"33930","status":"1","total":"14.61"}],"money":"14.61"},{"addtime":"2016-02","data":[{"aid":"1511","capital":"0.00","p_name":"红岭创投","period":"1","period_total":"6","profit":"4.50","project_name":"资金周转","return_time":"1454601600","rid":"31355","status":"1","total":"4.50"},{"aid":"1507","capital":"0.00","p_name":"红岭创投","period":"3","period_total":"6","profit":"0.42","project_name":"资金周转6个月_1511052004","return_time":"1454688000","rid":"31327","status":"1","total":"0.42"},{"aid":"1508","capital":"0.00","p_name":"红岭创投","period":"3","period_total":"6","profit":"0.42","project_name":"资金周转6个月_1511052004","return_time":"1454688000","rid":"31333","status":"1","total":"0.42"},{"aid":"1509","capital":"0.00","p_name":"红岭创投","period":"3","period_total":"6","profit":"0.42","project_name":"资金周转6个月_1511052004","return_time":"1454688000","rid":"31339","status":"1","total":"0.42"},{"aid":"1510","capital":"0.00","p_name":"红岭创投","period":"3","period_total":"12","profit":"0.42","project_name":"资金周转12个月_1511052037","return_time":"1454688000","rid":"31345","status":"1","total":"0.42"},{"aid":"1367","capital":"11,009.61","p_name":"星际","period":"1","period_total":"5","profit":"254.63","project_name":"20160118","return_time":"1455724800","rid":"30865","status":"2","total":"11,264.24"},{"aid":"1506","capital":"0.00","p_name":"红岭创投","period":"6","period_total":"15","profit":"0.53","project_name":"上海微商贷","return_time":"1455811200","rid":"31315","status":"1","total":"0.53"},{"aid":"1436","capital":"99.24","p_name":"点融网","period":"1","period_total":"22","profit":"3.71","project_name":"合乎","return_time":"1455897600","rid":"31205","status":"1","total":"102.95"},{"aid":"1531","capital":"10,058.98","p_name":"点融网","period":"1","period_total":"11","profit":"92.59","project_name":"20160122","return_time":"1456070400","rid":"31373","status":"1","total":"10,151.57"},{"aid":"2160","capital":"10.91","p_name":"规范化合合分分个","period":"1","period_total":"5","profit":"0.23","project_name":"20160127","return_time":"1456502400","rid":"33744","status":"1","total":"11.14"},{"aid":"3530","capital":"20,000.00","p_name":"陆金所","period":"1","period_total":"1","profit":"5.56","project_name":"20160226","return_time":"1456588800","rid":"35854","status":"1","total":"20,005.56"}],"money":"161.80"}]
     * ret_msg :
     * status : 0
     * stotal : 5,153.79
     */

    private String ret_msg;
    private int status;
    private String stotal;
    /**
     * addtime : 2016-01
     * data : [{"aid":"2743","capital":"0.00","p_name":"PPmoney","period":"1","period_total":"1","profit":"14.61","project_name":"体验标(GZ160123152507)","return_time":"1453996800","rid":"33930","status":"1","total":"14.61"}]
     * money : 14.61
     */

    private List<ListEntity> list;

    public void setRet_msg(String ret_msg) {
        this.ret_msg = ret_msg;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setStotal(String stotal) {
        this.stotal = stotal;
    }

    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public String getRet_msg() {
        return ret_msg;
    }

    public int getStatus() {
        return status;
    }

    public String getStotal() {
        return stotal;
    }

    public List<ListEntity> getList() {
        return list;
    }

    public static class ListEntity {
        private String datetime;
        private String money;
        /**
         * aid : 2743
         * capital : 0.00
         * p_name : PPmoney
         * period : 1
         * period_total : 1
         * profit : 14.61
         * project_name : 体验标(GZ160123152507)
         * return_time : 1453996800
         * rid : 33930
         * status : 1
         * total : 14.61
         */

        private List<DataEntity> data;

        public void setDatetime(String datetime) {
            this.datetime = datetime;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public void setData(List<DataEntity> data) {
            this.data = data;
        }

        public String getDatetime() {
            return datetime;
        }

        public String getMoney() {
            return money;
        }

        public List<DataEntity> getData() {
            return data;
        }

        public static class DataEntity {
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

            private String is_auto;

            public String getIs_auto() {
                return is_auto;
            }

            public void setIs_auto(String is_auto) {
                this.is_auto = is_auto;
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
    }
}
