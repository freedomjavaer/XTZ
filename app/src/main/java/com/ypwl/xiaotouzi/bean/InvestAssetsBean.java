package com.ypwl.xiaotouzi.bean;

import java.util.List;

/**
 * 我的投资---资产实体
 *
 * Created by PDK on 2016/3/24.
 */
public class InvestAssetsBean {


    /**
     * fprofit : 0.00
     * fwannual : 0.00
     * list : [{"data":[{"aid":"11181","is_auto":"0","money":"1,678.00","p_logo":"logo_5721d8eea75b3.png","p_name":"有利网","pid":"24","profit":"97.88","project_name":"2016-05-14","rate":"14.00","scapital":"1,678.00","starttime":"1463155200","stotal":"1,775.88"}],"is_auto":"0","money":"1,678.00","num":"1","p_logo":"logo_5721d8eea75b3.png","p_name":"有利网","pid":"24","profit":"97.88","scapital":"1,678.00","stotal":"1,775.88"},{"data":[{"aid":"11180","is_auto":"0","money":"1,000.00","p_logo":"logo_55ee57c7866dc.jpg","p_name":"红岭创投","pid":"2","profit":"46.67","project_name":"2016-05-14","rate":"14.00","scapital":"1,000.00","starttime":"1463155200","stotal":"1,046.67"}],"is_auto":"0","money":"1,000.00","num":"1","p_logo":"logo_55ee57c7866dc.jpg","p_name":"红岭创投","pid":"2","profit":"46.67","scapital":"1,000.00","stotal":"1,046.67"},{"data":[{"aid":"11178","is_auto":"0","money":"1,000.00","p_logo":"logo_5721d8ba923c2.png","p_name":"人人贷","pid":"22","profit":"27.50","project_name":"2016-05-14","rate":"11.00","scapital":"1,000.00","starttime":"1463155200","stotal":"1,027.50"}],"is_auto":"0","money":"1,000.00","num":"1","p_logo":"logo_5721d8ba923c2.png","p_name":"人人贷","pid":"22","profit":"27.50","scapital":"1,000.00","stotal":"1,027.50"},{"data":[{"aid":"11179","is_auto":"0","money":"1,000.00","p_logo":"logo_5721d94117aa0.png","p_name":"宜人贷","pid":"100","profit":"9.17","project_name":"2016-05-14","rate":"11.00","scapital":"1,000.00","starttime":"1463155200","stotal":"1,009.17"}],"is_auto":"0","money":"1,000.00","num":"1","p_logo":"logo_5721d94117aa0.png","p_name":"宜人贷","pid":"100","profit":"9.17","scapital":"1,000.00","stotal":"1,009.17"},{"data":[],"is_auto":"1","money":"0.00","num":"0","p_logo":"logo_55ee583fc3efd.jpg","p_name":"诚汇通","pid":"1","profit":"0.00","scapital":"0.00","stotal":"0.00"}]
     * money : 4,678.00
     * ret_msg :
     * scapital : 4,678.00
     * sprofit : 181.22
     * status : 0
     * stotal : 4,859.22
     * swannual : 12.72
     * tprofit : 181.22
     * wannual : 12.72
     */

    private String fprofit;
    private String fwannual;
    private String money;
    private String ret_msg;
    private String scapital;
    private String sprofit;
    private int status;
    private String stotal;
    private String swannual;
    private String tprofit;
    private String wannual;
    /**
     * data : [{"aid":"11181","is_auto":"0","money":"1,678.00","p_logo":"logo_5721d8eea75b3.png","p_name":"有利网","pid":"24","profit":"97.88","project_name":"2016-05-14","rate":"14.00","scapital":"1,678.00","starttime":"1463155200","stotal":"1,775.88"}]
     * is_auto : 0
     * money : 1,678.00
     * num : 1
     * p_logo : logo_5721d8eea75b3.png
     * p_name : 有利网
     * pid : 24
     * profit : 97.88
     * scapital : 1,678.00
     * stotal : 1,775.88
     */

    private List<ListEntity> list;

    public String getFprofit() {
        return fprofit;
    }

    public void setFprofit(String fprofit) {
        this.fprofit = fprofit;
    }

    public String getFwannual() {
        return fwannual;
    }

    public void setFwannual(String fwannual) {
        this.fwannual = fwannual;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getRet_msg() {
        return ret_msg;
    }

    public void setRet_msg(String ret_msg) {
        this.ret_msg = ret_msg;
    }

    public String getScapital() {
        return scapital;
    }

    public void setScapital(String scapital) {
        this.scapital = scapital;
    }

    public String getSprofit() {
        return sprofit;
    }

    public void setSprofit(String sprofit) {
        this.sprofit = sprofit;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStotal() {
        return stotal;
    }

    public void setStotal(String stotal) {
        this.stotal = stotal;
    }

    public String getSwannual() {
        return swannual;
    }

    public void setSwannual(String swannual) {
        this.swannual = swannual;
    }

    public String getTprofit() {
        return tprofit;
    }

    public void setTprofit(String tprofit) {
        this.tprofit = tprofit;
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

    public static class ListEntity {
        private String is_auto;
        private String money;
        private String num;
        private String p_logo;
        private String p_name;
        private String pid;
        private String profit;
        private String scapital;
        private String stotal;
        /**
         * aid : 11181
         * is_auto : 0
         * money : 1,678.00
         * p_logo : logo_5721d8eea75b3.png
         * p_name : 有利网
         * pid : 24
         * profit : 97.88
         * project_name : 2016-05-14
         * rate : 14.00
         * scapital : 1,678.00
         * starttime : 1463155200
         * stotal : 1,775.88
         */

        private List<DataEntity> data;

        public String getIs_auto() {
            return is_auto;
        }

        public void setIs_auto(String is_auto) {
            this.is_auto = is_auto;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
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

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getProfit() {
            return profit;
        }

        public void setProfit(String profit) {
            this.profit = profit;
        }

        public String getScapital() {
            return scapital;
        }

        public void setScapital(String scapital) {
            this.scapital = scapital;
        }

        public String getStotal() {
            return stotal;
        }

        public void setStotal(String stotal) {
            this.stotal = stotal;
        }

        public List<DataEntity> getData() {
            return data;
        }

        public void setData(List<DataEntity> data) {
            this.data = data;
        }

        public static class DataEntity {
            private String aid;
            private String is_auto;
            private String money;
            private String p_logo;
            private String p_name;
            private String pid;
            private String profit;
            private String project_name;
            private String rate;
            private String scapital;
            private String starttime;
            private String stotal;

            public String getAid() {
                return aid;
            }

            public void setAid(String aid) {
                this.aid = aid;
            }

            public String getIs_auto() {
                return is_auto;
            }

            public void setIs_auto(String is_auto) {
                this.is_auto = is_auto;
            }

            public String getMoney() {
                return money;
            }

            public void setMoney(String money) {
                this.money = money;
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

            public String getPid() {
                return pid;
            }

            public void setPid(String pid) {
                this.pid = pid;
            }

            public String getProfit() {
                return profit;
            }

            public void setProfit(String profit) {
                this.profit = profit;
            }

            public String getProject_name() {
                return project_name;
            }

            public void setProject_name(String project_name) {
                this.project_name = project_name;
            }

            public String getRate() {
                return rate;
            }

            public void setRate(String rate) {
                this.rate = rate;
            }

            public String getScapital() {
                return scapital;
            }

            public void setScapital(String scapital) {
                this.scapital = scapital;
            }

            public String getStarttime() {
                return starttime;
            }

            public void setStarttime(String starttime) {
                this.starttime = starttime;
            }

            public String getStotal() {
                return stotal;
            }

            public void setStotal(String stotal) {
                this.stotal = stotal;
            }
        }
    }
}
