package com.ypwl.xiaotouzi.bean;

import java.util.List;

/**
 * 项目名:	XTZ
 * 包名:	com.ypwl.xiaotouzi.bean
 * 类名:	PlatformTargetsBean
 * 作者:	罗霄
 * 创建时间:	2016/4/19 17:57
 * <p/>
 * 描述:	金融超市 -- 平台标的列表数据javabean
 * <p/>
 * svn版本:	$Revision: 14164 $
 * 更新人:	$Author: luoxiao $
 * 更新时间:	$Date: 2016-04-20 17:58:38 +0800 (周三, 20 四月 2016) $
 * 更新描述:	$Message$
 */
public class PlatformTargetsBean {


    /**
     * status : 0
     * banner : [{"image":"up_files/platform_image/banner/ceshi.jpg","url":"www.xtz168.com"},{"image":"up_files/platform_image/banner/ceshi2.jpg","url":"www.xtz168.com"}]
     * list : [{"project_id":"1","p_name":"诚汇通","project_name":"测试一","rate":"12.00","time_limit":"3","time_type":"1","add_interest":"0.00"},{"project_id":"2","p_name":"诚汇通","project_name":"测试一","rate":"12.00","time_limit":"3","time_type":"1","add_interest":"0.00"},{"project_id":"3","p_name":"诚汇通","project_name":"测试一","rate":"12.00","time_limit":"3","time_type":"1","add_interest":"0.00"},{"project_id":"4","p_name":"诚汇通","project_name":"测试一","rate":"12.00","time_limit":"3","time_type":"1","add_interest":"0.00"}]
     * ret_msg :
     */

    private int status;
    private String ret_msg;
    /**
     * image : up_files/platform_image/banner/ceshi.jpg
     * url : www.xtz168.com
     */

    private List<BannerEntity> banner;
    /**
     * project_id : 1
     * p_name : 诚汇通
     * project_name : 测试一
     * rate : 12.00
     * time_limit : 3
     * time_type : 1
     * add_interest : 0.00
     * at_type : "0" 加息类型 0：没有加息 1：首投加息 2：加息
     */

    private List<ListEntity> list;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRet_msg() {
        return ret_msg;
    }

    public void setRet_msg(String ret_msg) {
        this.ret_msg = ret_msg;
    }

    public List<BannerEntity> getBanner() {
        return banner;
    }

    public void setBanner(List<BannerEntity> banner) {
        this.banner = banner;
    }

    public List<ListEntity> getList() {
        return list;
    }

    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public static class BannerEntity {
        private String image;
        private String url;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class ListEntity {
        private String project_id;
        private String p_name;
        private String project_name;
        private String rate;
        private String time_limit;
        private String time_type;
        private String add_interest;
        private String at_type;

        public String getAt_type() {
            return at_type;
        }

        public void setAt_type(String at_type) {
            this.at_type = at_type;
        }

        public String getProject_id() {
            return project_id;
        }

        public void setProject_id(String project_id) {
            this.project_id = project_id;
        }

        public String getP_name() {
            return p_name;
        }

        public void setP_name(String p_name) {
            this.p_name = p_name;
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

        public String getAdd_interest() {
            return add_interest;
        }

        public void setAdd_interest(String add_interest) {
            this.add_interest = add_interest;
        }
    }
}
