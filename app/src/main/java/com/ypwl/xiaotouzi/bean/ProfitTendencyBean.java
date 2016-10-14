package com.ypwl.xiaotouzi.bean;

import java.util.List;

/**
 * 收益趋势实体
 * Created by PDK on 2016/1/28.
 */
public class ProfitTendencyBean {

    /**
     * status : 0
     * list : [{"p_name":"你我贷","project_name":"20160126184527","money":"555.00","rate":"25.00","b_rate":"25.00","time":"1437840000","type":"1"},{"p_name":"鑫合汇","project_name":"日益升-RYS2015102301","money":"100.00","rate":"22.71","b_rate":"10.00","time":"1445563710","type":"1"},{"p_name":"鑫合汇","project_name":"日益升-RYS2015102301","money":"100.00","rate":"25.00","b_rate":"10.00","time":"1446739200","type":"0"},{"p_name":"鑫合汇","project_name":"日益升-RYS2015110601","money":"100.00","rate":"22.71","b_rate":"10.00","time":"1446796164","type":"1"},{"p_name":"鑫合汇","project_name":"日益升-RYS2015110601","money":"100.00","rate":"25.00","b_rate":"10.00","time":"1447084800","type":"0"},{"p_name":"诚汇通","project_name":"20151125111249","money":"100.00","rate":"23.47","b_rate":"15.00","time":"1448507569","type":"1"},{"p_name":"你我贷","project_name":"20160126184527","money":"555.00","rate":"15.00","b_rate":"25.00","time":"1451059200","type":"0"},{"p_name":"宜人贷","project_name":"20151225075643","money":"52,698.00","rate":"12.01","b_rate":"12.00","time":"1451087803","type":"1"},{"p_name":"PPmoney","project_name":"20160122九块九","money":"6,464.00","rate":"11.24","b_rate":"5.00","time":"1453547191","type":"1"}]
     * ret_msg :
     */

    private int status;
    private String ret_msg;
    /**
     * p_name : 你我贷
     * project_name : 20160126184527
     * money : 555.00
     * rate : 25.00
     * b_rate : 25.00
     * time : 1437840000
     * type : 1
     */

    private List<ListEntity> list;

    public void setStatus(int status) {
        this.status = status;
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

    public String getRet_msg() {
        return ret_msg;
    }

    public List<ListEntity> getList() {
        return list;
    }

    public static class ListEntity {
        private String p_name;
        private String project_name;
        private String money;
        private String rate;
        private String b_rate;
        private String time;
        private String type;

        public void setP_name(String p_name) {
            this.p_name = p_name;
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

        public void setB_rate(String b_rate) {
            this.b_rate = b_rate;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public void setType(String type) {
            this.type = type;
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

        public String getRate() {
            return rate;
        }

        public String getB_rate() {
            return b_rate;
        }

        public String getTime() {
            return time;
        }

        public String getType() {
            return type;
        }
    }

}
