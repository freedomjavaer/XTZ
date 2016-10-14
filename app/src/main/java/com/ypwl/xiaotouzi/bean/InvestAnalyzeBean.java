package com.ypwl.xiaotouzi.bean;

import java.util.List;

/**
 * 投资分析数据实体
 *
 * Created by PDK on 2015/12/30.
 */
public class InvestAnalyzeBean {

    /**
     * status : 0
     * starttime : 2014-12
     * finishtime : 2015-12
     * total : 46,466.07
     * investnum : 20
     * smallmoney : 16
     * middlemoney : 1
     * bigmoney : 3
     * limits : 13
     * limitm : 3
     * limitb : 4
     * acinterest : 10
     * acapital : 0
     * dueinterest : 6
     * mpmd : 2
     * qthkfs : 2
     * list : [{"pid":"4","p_name":"前海理想金融","iamount":"16,666"},{"pid":"2724","p_name":"链家理财","iamount":"10,000"},{"pid":"2","p_name":"红岭创投","iamount":"19,800"}]
     * wannual : 19.44
     * rprofit : 19025.85
     * stotal : 230343.53
     */

    private int status;
    private String starttime;
    private String finishtime;
    private String total;
    private int investnum;
    private String smallmoney;
    private String middlemoney;
    private String bigmoney;
    private int limits;
    private int limitm;
    private int limitb;
    private String acinterest;
    private String acapital;
    private String dueinterest;
    private String mpmd;
    private String qthkfs;
    private String wannual;
    private String rprofit;
    private String stotal;
    /**
     * pid : 4
     * p_name : 前海理想金融
     * iamount : 16,666
     */

    private List<ListEntity> list;

    public void setStatus(int status) {
        this.status = status;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public void setFinishtime(String finishtime) {
        this.finishtime = finishtime;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public void setInvestnum(int investnum) {
        this.investnum = investnum;
    }

    public void setSmallmoney(String smallmoney) {
        this.smallmoney = smallmoney;
    }

    public void setMiddlemoney(String middlemoney) {
        this.middlemoney = middlemoney;
    }

    public void setBigmoney(String bigmoney) {
        this.bigmoney = bigmoney;
    }

    public void setLimits(int limits) {
        this.limits = limits;
    }

    public void setLimitm(int limitm) {
        this.limitm = limitm;
    }

    public void setLimitb(int limitb) {
        this.limitb = limitb;
    }

    public void setAcinterest(String acinterest) {
        this.acinterest = acinterest;
    }

    public void setAcapital(String acapital) {
        this.acapital = acapital;
    }

    public void setDueinterest(String dueinterest) {
        this.dueinterest = dueinterest;
    }

    public void setMpmd(String mpmd) {
        this.mpmd = mpmd;
    }

    public void setQthkfs(String qthkfs) {
        this.qthkfs = qthkfs;
    }

    public void setWannual(String wannual) {
        this.wannual = wannual;
    }

    public void setRprofit(String rprofit) {
        this.rprofit = rprofit;
    }

    public void setStotal(String stotal) {
        this.stotal = stotal;
    }

    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public int getStatus() {
        return status;
    }

    public String getStarttime() {
        return starttime;
    }

    public String getFinishtime() {
        return finishtime;
    }

    public String getTotal() {
        return total;
    }

    public int getInvestnum() {
        return investnum;
    }

    public String getSmallmoney() {
        return smallmoney;
    }

    public String getMiddlemoney() {
        return middlemoney;
    }

    public String getBigmoney() {
        return bigmoney;
    }

    public String getLimits() {
        return limits + "";
    }

    public String getLimitm() {
        return limitm + "";
    }

    public String getLimitb() {
        return limitb + "";
    }

    public String getAcinterest() {
        return acinterest;
    }

    public String getAcapital() {
        return acapital;
    }

    public String getDueinterest() {
        return dueinterest;
    }

    public String getMpmd() {
        return mpmd;
    }

    public String getQthkfs() {
        return qthkfs;
    }

    public String getWannual() {
        return wannual;
    }

    public String getRprofit() {
        return rprofit;
    }

    public String getStotal() {
        return stotal;
    }

    public List<ListEntity> getList() {
        return list;
    }

    public static class ListEntity {
        private String pid;
        private String p_name;
        private String iamount;

        public void setPid(String pid) {
            this.pid = pid;
        }

        public void setP_name(String p_name) {
            this.p_name = p_name;
        }

        public void setIamount(String iamount) {
            this.iamount = iamount;
        }

        public String getPid() {
            return pid;
        }

        public String getP_name() {
            return p_name;
        }

        public String getIamount() {
            return iamount;
        }
    }
}
