package com.ypwl.xiaotouzi.bean;

import java.util.List;

/**
 * 对比分析---投资金额实体
 *
 * Created by PDK on 2016/1/26.
 */
    public class CompareInvestMoneyBean {

        /**
         * status : 0
         * data : ["69488.00","64982.00","49712.00","46864.00","43789.00","38176.00","32886.00","31578.00","28764.00","25469.00","17943.00","6581.00"]
         * list : [{"p_name":"1688易贷","project_name":"你以为","money":"69488.00"},{"p_name":"有利网","project_name":"你想想","money":"64982.00"},{"p_name":"比亮贷","project_name":"迷途","money":"49712.00"},{"p_name":"诚汇通","project_name":"我将","money":"46864.00"},{"p_name":"互利网龙宝宝","project_name":"工具","money":"43789.00"},{"p_name":"和信贷","project_name":"牛","money":"38176.00"},{"p_name":"提钱网","project_name":"钮钴禄","money":"32886.00"},{"p_name":"中投瑞银投资","project_name":"忸怩","money":"31578.00"},{"p_name":"天翼贷","project_name":"谜语","money":"28764.00"},{"p_name":"翡翠贷","project_name":"不气咯","money":"25469.00"},{"p_name":"万晶聚金融","project_name":"哦五","money":"17943.00"},{"p_name":"红岭创投","project_name":"经验","money":"6581.00"}]
         * ret_msg :
         */

    private int status;
    private String ret_msg;
    private List<String> data;
    /**
     * p_name : 1688易贷
     * project_name : 你以为
     * money : 69488.00
     */

    private List<ListEntity> list;

    public void setStatus(int status) {
        this.status = status;
    }

    public void setRet_msg(String ret_msg) {
        this.ret_msg = ret_msg;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public int getStatus() {
        return status;
    }

    public String getRet_msg() {
        return ret_msg;
    }

    public List<String> getData() {
        return data;
    }

    public List<ListEntity> getList() {
        return list;
    }

    public static class ListEntity {
        private String p_name;
        private String project_name;
        private String money;

        public void setP_name(String p_name) {
            this.p_name = p_name;
        }

        public void setProject_name(String project_name) {
            this.project_name = project_name;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getP_name() {
            return p_name;
        }

        public String getProject_name() {
            return project_name;
        }

        public String getMoney() {
            return money;
        }
    }
}
