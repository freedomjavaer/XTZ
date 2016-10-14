package com.ypwl.xiaotouzi.bean;

import java.util.List;

/**
 * 基金列表
 * Created by PDK on 2016/4/18.
 */
public class FundsInvestBean {


    /**
     * code : 20
     * err : 获取成功
     * data : [{"product_id":"32","product_name":"前海基石1号供应链金融投资基金","category":"equity","threshold":"100","progress":"new","commission_rate":"0","start_time":"2015-11-30 00:00:00","end_time":"0000-00-00 00:00:00","total_amount":"5000万元","unit":"10万元","period":"6个月或12个月","min_period":"6个月","favor_count":"18","promotion":"1","display":"1","issuer_id":"18","payment_method":"月度付息","max_profit":"12.5","feature":"年化收益12.5%，多重保障，安全理财","update_time":"2016-03-21 15:53:09"},{"product_id":"39","product_name":"恒宇天泽-黄河一号1期定增基金","category":"equity","threshold":"100","progress":"new","commission_rate":"0","start_time":"2015-11-20 00:00:00","end_time":"0000-00-00 00:00:00","total_amount":"3000万","unit":"10","period":"18个月（本基金成立后不开放申购及赎回）","min_period":"18个月","favor_count":"4","promotion":"0","display":"1","issuer_id":"24","payment_method":"到期兑付","max_profit":"纯浮动收益","feature":"纯浮动收益（业绩基准10%+超额收益的83%）","update_time":"2016-03-24 12:59:31"},{"product_id":"40","product_name":"恒宇天泽-嵩山1号1期基础设施投资基金","category":"equity","threshold":"100","progress":"new","commission_rate":"0","start_time":"2015-11-20 00:00:00","end_time":"0000-00-00 00:00:00","total_amount":"10000万","unit":"10万","period":"15个月","min_period":"15个月","favor_count":"1","promotion":"0","display":"1","issuer_id":"24","payment_method":"按季付息","max_profit":"9.5","feature":"","update_time":"2015-12-07 12:53:55"},{"product_id":"41","product_name":"恒宇天泽-恒山2号地产投资基金","category":"equity","threshold":"100","progress":"new","commission_rate":"0","start_time":"2015-11-20 00:00:00","end_time":"0000-00-00 00:00:00","total_amount":"55000万","unit":"10万","period":"不超过8个月","min_period":"8个月","favor_count":"1","promotion":"0","display":"1","issuer_id":"24","payment_method":"按季付息","max_profit":"10","feature":"","update_time":"2015-12-21 16:37:15"}]
     */

    private int code;
    private String err;
    /**
     * product_id : 32
     * product_name : 前海基石1号供应链金融投资基金
     * category : equity
     * threshold : 100
     * progress : new
     * commission_rate : 0
     * start_time : 2015-11-30 00:00:00
     * end_time : 0000-00-00 00:00:00
     * total_amount : 5000万元
     * unit : 10万元
     * period : 6个月或12个月
     * min_period : 6个月
     * favor_count : 18
     * promotion : 1
     * display : 1
     * issuer_id : 18
     * payment_method : 月度付息
     * max_profit : 12.5
     * feature : 年化收益12.5%，多重保障，安全理财
     * update_time : 2016-03-21 15:53:09
     */

    private List<DataEntity> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public static class DataEntity {
        private String product_id;
        private String product_name;
        private String category;
        private String threshold;
        private String progress;
        private String commission_rate;
        private String start_time;
        private String end_time;
        private String total_amount;
        private String unit;
        private String period;
        private String min_period;
        private String favor_count;
        private String promotion;
        private String display;
        private String issuer_id;
        private String payment_method;
        private String max_profit;
        private String feature;
        private String update_time;
        private String pro_status;

        public String getPro_status() {
            return pro_status;
        }

        public void setPro_status(String pro_status) {
            this.pro_status = pro_status;
        }

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public String getProduct_name() {
            return product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getThreshold() {
            return threshold;
        }

        public void setThreshold(String threshold) {
            this.threshold = threshold;
        }

        public String getProgress() {
            return progress;
        }

        public void setProgress(String progress) {
            this.progress = progress;
        }

        public String getCommission_rate() {
            return commission_rate;
        }

        public void setCommission_rate(String commission_rate) {
            this.commission_rate = commission_rate;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(String total_amount) {
            this.total_amount = total_amount;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

        public String getMin_period() {
            return min_period;
        }

        public void setMin_period(String min_period) {
            this.min_period = min_period;
        }

        public String getFavor_count() {
            return favor_count;
        }

        public void setFavor_count(String favor_count) {
            this.favor_count = favor_count;
        }

        public String getPromotion() {
            return promotion;
        }

        public void setPromotion(String promotion) {
            this.promotion = promotion;
        }

        public String getDisplay() {
            return display;
        }

        public void setDisplay(String display) {
            this.display = display;
        }

        public String getIssuer_id() {
            return issuer_id;
        }

        public void setIssuer_id(String issuer_id) {
            this.issuer_id = issuer_id;
        }

        public String getPayment_method() {
            return payment_method;
        }

        public void setPayment_method(String payment_method) {
            this.payment_method = payment_method;
        }

        public String getMax_profit() {
            return max_profit;
        }

        public void setMax_profit(String max_profit) {
            this.max_profit = max_profit;
        }

        public String getFeature() {
            return feature;
        }

        public void setFeature(String feature) {
            this.feature = feature;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }
    }
}
