package com.ypwl.xiaotouzi.bean;

import java.util.List;

/**
 * 选择对比平台
 */
public class PlatformChooseBean {
    private int status;
    private List<Follow> follow;
    private List<Hot> hot;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Follow> getFollow() {
        return follow;
    }

    public void setFollow(List<Follow> follow) {
        this.follow = follow;
    }

    public List<Hot> getHot() {
        return hot;
    }

    public void setHot(List<Hot> hot) {
        this.hot = hot;
    }

    public static class Follow{
        private String pid;
        private String p_name;

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

    public static class Hot{
        private String pid;
        private String p_name;

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
