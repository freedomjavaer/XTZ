package com.ypwl.xiaotouzi.bean;

import java.util.List;

/**
 * @author tengtao
 * @time ${DATA} 16:08
 * @des ${自动记账平台选择对象}
 *
 */
public class AutoTallyPlatformBean {
    private String status;
    private List<Entity> list;

    public List<Entity> getList() {
        return list;
    }

    public void setList(List<Entity> list) {
        this.list = list;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class Entity{
        private String pid;
        private String p_name;
        private String p_logo;
        private String is_bind;//是否绑定
        private String p_letters;//首字母

        public String getP_letters() {
            return p_letters;
        }

        public void setP_letters(String p_letters) {
            this.p_letters = p_letters;
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

        public String getP_logo() {
            return p_logo;
        }

        public void setP_logo(String p_logo) {
            this.p_logo = p_logo;
        }

        public String getIs_bind() {
            return is_bind;
        }

        public void setIs_bind(String is_bind) {
            this.is_bind = is_bind;
        }
    }
}
