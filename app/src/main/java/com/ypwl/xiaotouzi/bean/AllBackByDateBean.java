package com.ypwl.xiaotouzi.bean;

import java.util.List;

/**
 * function : 所有回款--日期分组
 * <p/>
 * Created by tengtao on 2016/3/24.
 */
public class AllBackByDateBean {

    /**
     * list : [{"addtime":"1441036800","data":[{"aid":"1328","capital":"7.97","p_name":"诚汇通","period":"1","period_total":"6","profit":"0.89","project_name":"物流公司资金周转（第4标）过夜标","return_time":"1442505600","rid":"28088","status":"1","total":"8.86"}],"money":"10.53","type":"1"}]
     * rdata : [{"addtime":"1441036800","money":"10.53"},{"addtime":"1443628800","money":"10.51"},{"addtime":"1446307200","money":"686.57"},{"addtime":"1448899200","money":"465.47"},{"addtime":"1451577600","money":"144.19"},{"addtime":"1454256000","money":"2,726.56"},{"addtime":"1456761600","money":"27.27"}]
     * ret_msg :
     * sdata : [{"addtime":"1454256000","money":"41,432.93","overdue":"11,264.66"}]
     * status : 0
     * stotal : 1,165,450,887.69
     */

    private String ret_msg;
    private int status;
    private String stotal;
    /**
     * addtime : 1441036800
     * data : [{"aid":"1328","capital":"7.97","p_name":"诚汇通","period":"1","period_total":"6","profit":"0.89","project_name":"物流公司资金周转（第4标）过夜标","return_time":"1442505600","rid":"28088","status":"1","total":"8.86"}]
     * money : 10.53
     * type : 1
     */

    private List<ListEntity> list;
    /**
     * addtime : 1441036800
     * money : 10.53
     */

    private List<RdataEntity> rdata;
    /**
     * addtime : 1454256000
     * money : 41,432.93
     * overdue : 11,264.66
     */

    private List<SdataEntity> sdata;

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

    public void setRdata(List<RdataEntity> rdata) {
        this.rdata = rdata;
    }

    public void setSdata(List<SdataEntity> sdata) {
        this.sdata = sdata;
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

    public List<RdataEntity> getRdata() {
        return rdata;
    }

    public List<SdataEntity> getSdata() {
        return sdata;
    }

    public static class ListEntity {
        private String datetime;
        private String money;
        private String type;
        /**
         * aid : 1328
         * capital : 7.97
         * p_name : 诚汇通
         * period : 1
         * period_total : 6
         * profit : 0.89
         * project_name : 物流公司资金周转（第4标）过夜标
         * return_time : 1442505600
         * rid : 28088
         * status : 1
         * total : 8.86
         */

        private List<DataEntity> data;

        public void setDatetime(String datetime) {
            this.datetime = datetime;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public void setType(String type) {
            this.type = type;
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

        public String getType() {
            return type;
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

    public static class RdataEntity {
        private String datetime;
        private String money;

        public void setDatetime(String datetime) {
            this.datetime = datetime;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getDatetime() {
            return datetime;
        }

        public String getMoney() {
            return money;
        }
    }

    public static class SdataEntity {
        private String datetime;
        private String money;
        private String overdue;

        public void setDatetime(String datetime) {
            this.datetime = datetime;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public void setOverdue(String overdue) {
            this.overdue = overdue;
        }

        public String getDatetime() {
            return datetime;
        }

        public String getMoney() {
            return money;
        }

        public String getOverdue() {
            return overdue;
        }
    }
}
