package com.ypwl.xiaotouzi.bean;

import java.util.List;

/**
 * function : 主页新闻javabean
 * <p/>
 * Created by tengtao on 2016/3/15.
 */
public class XtzNetCreditNewsBean {

    /**
     * banner : [{"image":"up_files/banner/banner_56ca6ba99c175.png","title":"邀请抽抽抽","url":"http://www.xtz168.com/content/activity-double.html"}]
     * blist : [{"add_interest":"0.00","p_logo":"up_files/platform_image/1/logo_55ee583fc3efd.jpg","p_name":"诚汇通","pid":"1","project_id":"1","project_name":"测试一","rate":"12.00","time_limit":"3","time_type":"1"}]
     * fix_banner : [{"image":"up_files/banner/banner_56ca6be94d02c.png","title":"固定banner图","url":"http://www.xtz169.com/content/activity-double.html"}]
     * list : [{"click":"340","image":"up_files/wdnews/201603/news_56fcce24677df.png","intro":"2015年，凭借电商、支付宝转账和余额宝等产品，支付宝在交易额快速增长的同时继 续保持交易规模行业第一的态势。","news_id":"52","title":"移动支付市场松动 支付宝占比份额缩水"}]
     * status : 0
     */

    private int status;
    /**
     * image : up_files/banner/banner_56ca6ba99c175.png
     * title : 邀请抽抽抽
     * url : http://www.xtz168.com/content/activity-double.html
     */

    private List<BannerBean> banner;
    /**
     * add_interest : 0.00
     * p_logo : up_files/platform_image/1/logo_55ee583fc3efd.jpg
     * p_name : 诚汇通
     * pid : 1
     * project_id : 1
     * project_name : 测试一
     * rate : 12.00
     * time_limit : 3
     * time_type : 1
     */

    private List<BlistBean> blist;
    /**
     * image : up_files/banner/banner_56ca6be94d02c.png
     * title : 固定banner图
     * url : http://www.xtz169.com/content/activity-double.html
     */

    private List<FixBannerBean> fix_banner;
    /**
     * click : 340
     * image : up_files/wdnews/201603/news_56fcce24677df.png
     * intro : 2015年，凭借电商、支付宝转账和余额宝等产品，支付宝在交易额快速增长的同时继 续保持交易规模行业第一的态势。
     * news_id : 52
     * title : 移动支付市场松动 支付宝占比份额缩水
     */

    private List<ListBean> list;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<BannerBean> getBanner() {
        return banner;
    }

    public void setBanner(List<BannerBean> banner) {
        this.banner = banner;
    }

    public List<BlistBean> getBlist() {
        return blist;
    }

    public void setBlist(List<BlistBean> blist) {
        this.blist = blist;
    }

    public List<FixBannerBean> getFix_banner() {
        return fix_banner;
    }

    public void setFix_banner(List<FixBannerBean> fix_banner) {
        this.fix_banner = fix_banner;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class BannerBean {
        private String image;
        private String title;
        private String url;

        private String type;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class BlistBean {
        private String add_interest;
        private String p_logo;
        private String p_name;
        private String pid;
        private String project_id;
        private String project_name;
        private String rate;
        private String time_limit;
        private String time_type;
        private String at_type;//加息类型

        public String getAdd_interest() {
            return add_interest;
        }

        public void setAdd_interest(String add_interest) {
            this.add_interest = add_interest;
        }

        public String getP_logo() {
            return p_logo;
        }

        public void setP_logo(String p_logo) {
            this.p_logo = p_logo;
        }

        public String getP_name() {
            return p_name;
        }

        public void setP_name(String p_name) {
            this.p_name = p_name;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getProject_id() {
            return project_id;
        }

        public void setProject_id(String project_id) {
            this.project_id = project_id;
        }

        public String getProject_name() {
            return project_name;
        }

        public void setProject_name(String project_name) {
            this.project_name = project_name;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getTime_limit() {
            return time_limit;
        }

        public void setTime_limit(String time_limit) {
            this.time_limit = time_limit;
        }

        public String getTime_type() {
            return time_type;
        }

        public void setTime_type(String time_type) {
            this.time_type = time_type;
        }

        public String getAt_type() {
            return at_type;
        }

        public void setAt_type(String at_type) {
            this.at_type = at_type;
        }
    }

    public static class FixBannerBean {
        private String image;
        private String title;
        private String url;

        private String type;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class ListBean {
        private String click;
        private String image;
        private String intro;
        private String news_id;
        private String title;
        private String addtime;
        private String source;

        public String getClick() {
            return click;
        }

        public void setClick(String click) {
            this.click = click;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public String getNews_id() {
            return news_id;
        }

        public void setNews_id(String news_id) {
            this.news_id = news_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
