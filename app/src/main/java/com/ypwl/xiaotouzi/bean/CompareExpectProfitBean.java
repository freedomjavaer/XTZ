package com.ypwl.xiaotouzi.bean;

import java.util.List;

/**
 * 对比分析---预期收益实体
 *
 * Created by PDK on 2016/1/26.
 */
public class CompareExpectProfitBean {


    /**
     * status : 0
     * data : ["21806.22","9465.66","5925.30","4639.79","4219.18","3336.09","2496.27","2043.32","962.99","899.81","825.54","296.85"]
     * list : [{"p_name":"中投瑞银投资","project_name":"忸怩","money":"21806.22"},{"p_name":"和信贷","project_name":"牛","money":"9465.66"},{"p_name":"1688易贷","project_name":"你以为","money":"5925.30"},{"p_name":"比亮贷","project_name":"迷途","money":"4639.79"},{"p_name":"诚汇通","project_name":"我将","money":"4219.18"},{"p_name":"互利网龙宝宝","project_name":"工具","money":"3336.09"},{"p_name":"天翼贷","project_name":"谜语","money":"2496.27"},{"p_name":"有利网","project_name":"你想想","money":"2043.32"},{"p_name":"翡翠贷","project_name":"不气咯","money":"962.99"},{"p_name":"万晶聚金融","project_name":"哦五","money":"899.81"},{"p_name":"提钱网","project_name":"钮钴禄","money":"825.54"},{"p_name":"红岭创投","project_name":"经验","money":"296.85"}]
     * ret_msg :
     */

    private int status;
    private String ret_msg;
    private List<String> data;
    /**
     * p_name : 中投瑞银投资
     * project_name : 忸怩
     * money : 21806.22
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
