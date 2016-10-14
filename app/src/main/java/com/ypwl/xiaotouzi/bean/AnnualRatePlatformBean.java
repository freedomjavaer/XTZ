package com.ypwl.xiaotouzi.bean;

import java.util.List;

/**
 * 对比分析---年化率实体(平台模式)
 *
 * Created by PDK on 2016/1/26.
 */
public class AnnualRatePlatformBean {


    /**
     * status : 0
     * data : ["20.00","18.00","16.00","15.00","15.00","15.00","15.00","15.00","15.00","14.00","14.00","12.00"]
     * list : [{"p_name":"中投瑞银投资","project_name":"忸怩","avg_profit":"20.00"},{"p_name":"万晶聚金融","project_name":"哦五","avg_profit":"18.00"},{"p_name":"天翼贷","project_name":"谜语","avg_profit":"16.00"},{"p_name":"诚汇通","project_name":"我将","avg_profit":"15.00"},{"p_name":"提钱网","project_name":"钮钴禄","avg_profit":"15.00"},{"p_name":"翡翠贷","project_name":"不气咯","avg_profit":"15.00"},{"p_name":"和信贷","project_name":"牛","avg_profit":"15.00"},{"p_name":"有利网","project_name":"你想想","avg_profit":"15.00"},{"p_name":"红岭创投","project_name":"经验","avg_profit":"15.00"},{"p_name":"比亮贷","project_name":"迷途","avg_profit":"14.00"},{"p_name":"1688易贷","project_name":"你以为","avg_profit":"14.00"},{"p_name":"互利网龙宝宝","project_name":"工具","avg_profit":"12.00"}]
     * ret_msg :
     */

    private int status;
    private String ret_msg;
    private List<String> data;
    /**
     * p_name : 中投瑞银投资
     * project_name : 忸怩
     * avg_profit : 20.00
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
        private String avg_profit;

        public void setP_name(String p_name) {
            this.p_name = p_name;
        }

        public void setProject_name(String project_name) {
            this.project_name = project_name;
        }

        public void setAvg_profit(String avg_profit) {
            this.avg_profit = avg_profit;
        }

        public String getP_name() {
            return p_name;
        }

        public String getProject_name() {
            return project_name;
        }

        public String getAvg_profit() {
            return avg_profit;
        }
    }
}
