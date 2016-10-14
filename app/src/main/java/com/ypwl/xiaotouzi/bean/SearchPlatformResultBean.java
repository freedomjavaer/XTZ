package com.ypwl.xiaotouzi.bean;

import java.util.List;

/**
 * function : 平台搜索 数据模式对象.
 * <p/>
 * Created by lzj on 2015/11/9.
 */
public class SearchPlatformResultBean {

    /**
     * status : 0
     * data : [
     * {"pid":"1","p_name":"诚汇通","p_logo":"logo_55ee583fc3efd.jpg","amount":"264.30","bidder_num":"295","average_rate":"21.83"},
     * {"pid":"1745","p_name":"诚投在线","p_logo":"logo_55e5690038718.jpg","amount":"20.00","bidder_num":"24","average_rate":"13.50"},
     * {"pid":"1980","p_name":"国诚金融","p_logo":"logo_55dd5810edfaf.jpg","amount":"159.79","bidder_num":"210","average_rate":"15.32"}
     * ]
     */

    private int status;
    private List<DataEntity> data;

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
        private String pid;
        private String p_name;

        private String date;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
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
    }
}
