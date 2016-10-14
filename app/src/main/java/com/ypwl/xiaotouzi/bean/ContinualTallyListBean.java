package com.ypwl.xiaotouzi.bean;

/**
 * 项目名:	XTZ
 * 包名:	com.ypwl.xiaotouzi.bean
 * 类名:	ContinualTallyListBean
 * 作者:	罗霄
 * 创建时间:	2016/4/12 10:38
 * <p/>
 * 描述:	流水资产列表
 * <p/>
 * svn版本:	$Revision: 13887 $
 * 更新人:	$Author: luoxiao $
 * 更新时间:	$Date: 2016-04-13 13:06:45 +0800 (周三, 13 四月 2016) $
 * 更新描述:	${TODO}
 */
public class ContinualTallyListBean {

//        type	账单类型
//        aid	    投资ID
//        pid	    平台ID
//        p_name	平台名称
//        time	时间
//        project_name	项目名称
//        money	投资金额
//        period	第几期
//        period_total	总期数

        private String type;
        private String aid;
        private String pid;
        private String p_name;
        private String time;
        private String project_name;
        private String money;
        private String period;
        private String period_total;

        public String getType() {
                return type;
        }

        public void setType(String type) {
                this.type = type;
        }

        public String getAid() {
                return aid;
        }

        public void setAid(String aid) {
                this.aid = aid;
        }

        public String getPid() {
                return pid;
        }

        public void setPid(String pid) {
                this.pid = pid;
        }

        public String getP_name() {
                return p_name;
        }

        public void setP_name(String p_name) {
                this.p_name = p_name;
        }

        public String getTime() {
                return time;
        }

        public void setTime(String time) {
                this.time = time;
        }

        public String getProject_name() {
                return project_name;
        }

        public void setProject_name(String project_name) {
                this.project_name = project_name;
        }

        public String getMoney() {
                return money;
        }

        public void setMoney(String money) {
                this.money = money;
        }

        public String getPeriod() {
                return period;
        }

        public void setPeriod(String period) {
                this.period = period;
        }

        public String getPeriod_total() {
                return period_total;
        }

        public void setPeriod_total(String period_total) {
                this.period_total = period_total;
        }
}
