package com.ypwl.xiaotouzi.bean;

import java.util.List;

/**
 * function : 全部回款：按平台分类，在投标的和结束标的的网络数据对象
 * <p>
 * Created by tengtao on 2016/3/25.
 */
public class InvestByPlatformBidBean {

    private int status;
    private String ret_msg;

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
        private String money;
        private int num;

        /**
         * project_name : 物流公司资金周转（第4标）过夜标
         * aid : 1328
         * money : 53.91
         * pid : 1
         * return_time : 1455724800
         * p_name : 诚汇通
         */

        private List<DataEntity> data;

        public void setP_name(String p_name) {
            this.p_name = p_name;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public void setData(List<DataEntity> data) {
            this.data = data;
        }

        public String getP_name() {
            return p_name;
        }

        public String getMoney() {
            return money;
        }

        public int getNum() {
            return num;
        }

        public List<DataEntity> getData() {
            return data;
        }

        public static class DataEntity {
            private String project_name;
            private String aid;
            private String money;
            private String pid;
            private String return_time;
            private String p_name;

            public void setProject_name(String project_name) {
                this.project_name = project_name;
            }

            public void setAid(String aid) {
                this.aid = aid;
            }

            public void setMoney(String money) {
                this.money = money;
            }

            public void setPid(String pid) {
                this.pid = pid;
            }

            public void setReturn_time(String return_time) {
                this.return_time = return_time;
            }

            public void setP_name(String p_name) {
                this.p_name = p_name;
            }

            public String getProject_name() {
                return project_name;
            }

            public String getAid() {
                return aid;
            }

            public String getMoney() {
                return money;
            }

            public String getPid() {
                return pid;
            }

            public String getReturn_time() {
                return return_time;
            }

            public String getP_name() {
                return p_name;
            }

            private String p_money;
            private int p_num;
            private boolean showContent;//是否显示

            public boolean isShowContent() {
                return showContent;
            }

            public void setShowContent(boolean showContent) {
                this.showContent = showContent;
            }

            public String getP_money() {
                return p_money;
            }

            public void setP_money(String p_money) {
                this.p_money = p_money;
            }

            public int getP_num() {
                return p_num;
            }

            public void setP_num(int p_num) {
                this.p_num = p_num;
            }
        }
    }
}
