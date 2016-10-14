package com.ypwl.xiaotouzi.bean;

import java.util.List;

/**
 * function : 历史资产：按平台分类数据对象
 * <p/>
 * Created by tengtao on 2016/3/28.
 */
public class HistoryAssetsByPlatformProtocolBean {


    /**
     * status : 0
     * tprofit : 26.23
     * wannual : 1.00
     * list : [{"p_name":"有利网","profit":"26.23","rate":"1.00","data":[{"aid":"3819","project_name":"测试","money":"188,888.00","rate":"1.00","profit":"26.23","pid":"24","return_time":"1454774400","p_letters":"Y","p_name":"有利网"}]}]
     * ret_msg :
     */

    private int status;
    private String tprofit;
    private String wannual;
    private String ret_msg;


    /**
     * p_name : 有利网
     * profit : 26.23
     * rate : 1.00
     * data : [{"aid":"3819","project_name":"测试","money":"188,888.00","rate":"1.00","profit":"26.23","pid":"24","return_time":"1454774400","p_letters":"Y","p_name":"有利网"}]
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
        private String p_name;
        private String profit;
        private String rate;
        private String sortLetters;
        /**
         * aid : 3819
         * project_name : 测试
         * money : 188,888.00
         * rate : 1.00
         * profit : 26.23
         * pid : 24
         * return_time : 1454774400
         * p_letters : Y
         * p_name : 有利网
         */
        public String getSortLetters() {
            return sortLetters;
        }
        public void setSortLetters(String sortLetters) {
            this.sortLetters = sortLetters;
        }
        private List<DataEntity> data;

        public void setP_name(String p_name) {
            this.p_name = p_name;
        }

        public void setProfit(String profit) {
            this.profit = profit;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public void setData(List<DataEntity> data) {
            this.data = data;
        }

        public String getP_name() {
            return p_name;
        }

        public String getProfit() {
            return profit;
        }

        public String getRate() {
            return rate;
        }

        public List<DataEntity> getData() {
            return data;
        }

        public static class DataEntity {
            private String aid;
            private String project_name;
            private String money;
            private String rate;
            private String profit;
            private String pid;
            private String return_time;
            private String p_letters;
            private String p_name;
            private int is_auto;

            public int getIs_auto() {
                return is_auto;
            }

            public void setIs_auto(int is_auto) {
                this.is_auto = is_auto;
            }

            public void setAid(String aid) {
                this.aid = aid;
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

            public void setPid(String pid) {
                this.pid = pid;
            }

            public void setReturn_time(String return_time) {
                this.return_time = return_time;
            }

            public void setP_letters(String p_letters) {
                this.p_letters = p_letters;
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

            public String getP_letters() {
                return p_letters;
            }

            public String getP_name() {
                return p_name;
            }
        }
    }
}
