package com.ypwl.xiaotouzi.bean;

import java.util.List;

/**
 * 收件箱实体
 *
 * Created by PDK on 2016/3/28.
 */
public class MessageBoxBean {


    /**
     * status : 0
     * list : [{"stand_id":"7","content":"helloworld","addtime":"1458897110"},{"stand_id":"6","content":"地方的","addtime":"1458897023"},{"stand_id":"5","content":"hello","addtime":"1458896943"},{"stand_id":"4","content":"测试","addtime":"1458888458"},{"stand_id":"2","content":"推广测试","addtime":"1458281052"},{"stand_id":"3","content":"测试","addtime":"1458281052"}]
     * ret_msg :
     */

    private int status;
    private String ret_msg;
    /**
     * stand_id : 7
     * content : helloworld
     * addtime : 1458897110
     */

    private List<ListEntity> list;

    public void setStatus(int status) {
        this.status = status;
    }

    public void setRet_msg(String ret_msg) {
        this.ret_msg = ret_msg;
    }

    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public int getStatus() {
        return status;
    }

    public String getRet_msg() {
        return ret_msg;
    }

    public List<ListEntity> getList() {
        return list;
    }

    public static class ListEntity {
        private String stand_id;
        private String content;
        private long addtime;
        private int status;
        private int type;
        private String name;

        public String getStand_id() {
            return stand_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setStand_id(String stand_id) {
            this.stand_id = stand_id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public long getAddtime() {
            return addtime;
        }

        public void setAddtime(long addtime) {
            this.addtime = addtime;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

    }
}
