package com.ypwl.xiaotouzi.bean;

import java.util.List;


/**
 * function : 优惠活动所有数据bean
 * <p/>
 * Modify by lzj on 2015/11/8.
 */
public class HuodongInfosBean {
    private int status;
    private int num;
    private List<HuodongInfo> list;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public List<HuodongInfo> getList() {
        return list;
    }

    public void setList(List<HuodongInfo> list) {
        this.list = list;
    }

    public static class HuodongInfo {
        private String id;
        private String title;
        private String img;
        private String url;
        private int reddot;
        private int disabled;
        private long endtime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getReddot() {
            return reddot;
        }

        public void setReddot(int reddot) {
            this.reddot = reddot;
        }

        public int getDisabled() {
            return disabled;
        }

        public void setDisabled(int disabled) {
            this.disabled = disabled;
        }

        public long getEndtime() {
            return endtime;
        }

        public void setEndtime(long endtime) {
            this.endtime = endtime;
        }
    }
}
