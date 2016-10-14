package com.ypwl.xiaotouzi.bean;

import java.util.List;

/**
 * 我的关注对象
 */
public class MyFocusBean {
    private int status;
    private List<FocusEntity> list;

    public void setStatus(int status) {
        this.status = status;
    }

    public void setList(List<FocusEntity> list) {
        this.list = list;
    }

    public int getStatus() {
        return status;
    }

    public List<FocusEntity> getList() {
        return list;
    }

    public static class FocusEntity{
        private String p_name;//平台名称
        private String p_logo;//平台图标
        private String pid;//平台pid
        private boolean selected;


        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public void setP_name(String p_name){
            this.p_name = p_name;
        }
        public void setP_logo(String p_logo){
            this.p_logo = p_logo;
        }
        public void setPid(String pid){
            this.pid = pid;
        }
        public String getP_name(){
            return p_name;
        }
        public String getP_logo(){
            return p_logo;
        }
        public String getPid(){
            return pid;
        }
    }
}
