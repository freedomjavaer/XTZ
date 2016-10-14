package com.ypwl.xiaotouzi.bean;

import java.util.List;

/**
 * 投资分析---收益排行实体
 *
 * Created by PDK on 2016/4/12.
 */
public class ProfitRangeBean {


    /**
     * list : [{"aid":"3909","p_name":"前海大众金融","profit":"4,073.81","project_name":"我和无咯服务业哦合租我哦哦勾引","rate":"14.00"},{"aid":"3890","p_name":"宜人贷","profit":"2,228.10","project_name":"MP3窃玉偷香","rate":"5.00"},{"aid":"3885","p_name":"红岭创投","profit":"1,567.25","project_name":"定许诺铸剑为犁咯","rate":"12.00"},{"aid":"3889","p_name":"点融网","profit":"1,087.28","project_name":"PK第一lz饿了","rate":"12.00"},{"aid":"4410","p_name":"365天天利","profit":"680.00","project_name":"互联网金融","rate":"16.00"},{"aid":"3883","p_name":"红岭创投","profit":"575.38","project_name":"or自由的","rate":"9.00"},{"aid":"4357","p_name":"点融网","profit":"400.02","project_name":"XP妙语连珠","rate":"8.00"},{"aid":"4358","p_name":"宜人贷","profit":"400.00","project_name":"MSN和我联系lz咯","rate":"12.00"},{"aid":"3897","p_name":"有利网","profit":"336.54","project_name":"我大结局了","rate":"7.00"},{"aid":"4413","p_name":"中投瑞银投资","profit":"324.99","project_name":"破dll我可饿","rate":"13.00"},{"aid":"4273","p_name":"诚汇通","profit":"6.71","project_name":"保洁公司资金周转（第5标）过夜标","rate":"20.50"},{"aid":"4271","p_name":"诚汇通","profit":"3.92","project_name":"物流公司资金周转（第4标）过夜标","rate":"21.40"},{"aid":"4272","p_name":"诚汇通","profit":"3.57","project_name":"【活动标】电子科技公司资金周转（到期还本第9标）","rate":"20.40"},{"aid":"4274","p_name":"诚汇通","profit":"1.34","project_name":"汽车连锁维修店资金周转(到期还本第3标)","rate":"16.00"}]
     * ret_msg :
     * status : 0
     */

    private String ret_msg;
    private int status;
    /**
     * aid : 3909
     * p_name : 前海大众金融
     * profit : 4,073.81
     * project_name : 我和无咯服务业哦合租我哦哦勾引
     * rate : 14.00
     */

    private List<ListEntity> list;

    public void setRet_msg(String ret_msg) {
        this.ret_msg = ret_msg;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public List<ListEntity> getList() {
        return list;
    }

    public static class ListEntity {
        private String pid;
        private String aid;
        private String p_name;
        private String profit;
        private String project_name;
        private String rate;

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public void setAid(String aid) {
            this.aid = aid;
        }

        public void setP_name(String p_name) {
            this.p_name = p_name;
        }

        public void setProfit(String profit) {
            this.profit = profit;
        }

        public void setProject_name(String project_name) {
            this.project_name = project_name;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getAid() {
            return aid;
        }

        public String getP_name() {
            return p_name;
        }

        public String getProfit() {
            return profit;
        }

        public String getProject_name() {
            return project_name;
        }

        public String getRate() {
            return rate;
        }
    }
}
