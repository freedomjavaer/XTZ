package com.ypwl.xiaotouzi.bean;

import java.util.List;

/**
 * Created by Pater on 2015/9/25.
 */
public class AllPlatformBean {

    /**
     * status : 0
     * data : [
     * {"pid":"2","p_name":"红岭创投","p_logo":"logo_559f77a17019c.jpg","amount":"245688.69","bidder_num":"80486","average_rate":"10.24"},
     * {"pid":"241","p_name":"鑫合汇","p_logo":"logo_55b5f79d50bb6.jpg","amount":"61309.64","bidder_num":"24052","average_rate":"9.86"} ,
     * {"pid":"43","p_name":"PPmoney","p_logo":"logo_55a4b416d3474.jpg","amount":"52130.71","bidder_num":"43330","average_rate":"9.15"} ,
     * {"pid":"1557","p_name":"汇付四海","p_logo":"logo_55ae0325d080a.jpg","amount":"50643.00","bidder_num":"193","average_rate":"6.88"}
     * ]
     */


    public int status;
    public List<DataEntity> data;

    public void setStatus(int status) {
        this.status = status;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public static class DataEntity {
        /**
         * pid : 2
         * p_name : 红岭创投
         * p_logo : logo_559f77a17019c.jpg
         * amount : 245688.69
         * bidder_num : 80486
         * average_rate : 10.24
         */

        public String pid;
        public String p_name;
        public String p_logo;
        public String amount;
        public String bidder_num;
        public String average_rate;
        public List<VideoInfoBean> video_url;
        public String level;//评级

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public void setP_name(String p_name) {
            this.p_name = p_name;
        }

        public void setP_logo(String p_logo) {
            this.p_logo = p_logo;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public void setBidder_num(String bidder_num) {
            this.bidder_num = bidder_num;
        }

        public void setAverage_rate(String average_rate) {
            this.average_rate = average_rate;
        }

        public void setVideoInfo(List<com.ypwl.xiaotouzi.bean.VideoInfoBean> video_url) {
            this.video_url = video_url;
        }

        public String getPid() {
            return pid;
        }

        public String getP_name() {
            return p_name;
        }

        public String getP_logo() {
            return p_logo;
        }

        public String getAmount() {
            return amount;
        }

        public String getBidder_num() {
            return bidder_num;
        }

        public String getAverage_rate() {
            return average_rate;
        }

        public List<VideoInfoBean> getVideoInfo() {
            return video_url;
        }
    }
}
