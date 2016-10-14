package com.ypwl.xiaotouzi.bean;

import java.util.List;

/**
 * function : 投资分析--投资分布--数据网络请求对象
 * <p/>
 * Created by tengtao on 2016/4/11.
 */
public class InvestAnalysisByFenbuBean {


    private String ret_msg;
    private int status;
    private String total;
    private String period;
    private String wannual;

    private List<ListBean> list;

    public String getRet_msg() {
        return ret_msg;
    }

    public void setRet_msg(String ret_msg) {
        this.ret_msg = ret_msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getWannual() {
        return wannual;
    }

    public void setWannual(String wannual) {
        this.wannual = wannual;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private String money;
        private String number;
        private String p_name;
        private String percent;
        private String pid;
        private String type;
        private String return_name;//还款方式

        public boolean isShow() {
            return isShow;
        }

        public void setShow(boolean show) {
            isShow = show;
        }

        private boolean isShow;

        private List<DataBean> data;

        public String getReturn_name() {
            return return_name;
        }

        public void setReturn_name(String return_name) {
            this.return_name = return_name;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getP_name() {
            return p_name;
        }

        public void setP_name(String p_name) {
            this.p_name = p_name;
        }

        public String getPercent() {
            return percent;
        }

        public void setPercent(String percent) {
            this.percent = percent;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            private String aid;
            private String money;
            private String project_name;
            private String p_name;
            private String rate;
            private String time_limit;//投资期限
            private String time_type;//期限类型

            public String getAid() {
                return aid;
            }

            public void setAid(String aid) {
                this.aid = aid;
            }

            public String getMoney() {
                return money;
            }

            public void setMoney(String money) {
                this.money = money;
            }

            public String getProject_name() {
                return project_name;
            }

            public void setProject_name(String project_name) {
                this.project_name = project_name;
            }
            public String getTime_limit() {
                return time_limit;
            }

            public void setTime_limit(String time_limit) {
                this.time_limit = time_limit;
            }

            public String getP_name() {
                return p_name;
            }

            public void setP_name(String p_name) {
                this.p_name = p_name;
            }

            public String getRate() {
                return rate;
            }

            public void setRate(String rate) {
                this.rate = rate;
            }

            public String getTime_type() {
                return time_type;
            }

            public void setTime_type(String time_type) {
                this.time_type = time_type;
            }
        }
    }
}
