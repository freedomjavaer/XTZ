package com.ypwl.xiaotouzi.bean;

import java.util.List;

/**
 * function:同城在线接收消息数据对象.
 *
 * <p>Created by lzj on 2016/3/22.</p>
 */
public class OnlineMsgBean {

    /**
     * fromme : 0
     * headimage : http://p2p.kinimi.com/up_files/avatar/434.png?rand=1454320217?rand=1458286094
     * message : tut啦啦啦啦啦
     * sendtime : 1458612389000
     * toid : [{"userid":"123","username":"lzj"},{"userid":"123","username":"lzj"}]
     * userid : 434
     * username : lzj
     */

    private List<MsgEntity> msg;

    public void setMsg(List<MsgEntity> msg) {
        this.msg = msg;
    }

    public List<MsgEntity> getMsg() {
        return msg;
    }

    public static class MsgEntity {
        private int fromme;
        private String headimage;
        private String message;
        private long sendtime;
        private String userid;
        private String username;
        /**
         * userid : 123
         * username : lzj
         */

        private List<ToidEntity> toid;

        public void setFromme(int fromme) {
            this.fromme = fromme;
        }

        public void setHeadimage(String headimage) {
            this.headimage = headimage;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public void setSendtime(long sendtime) {
            this.sendtime = sendtime;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setToid(List<ToidEntity> toid) {
            this.toid = toid;
        }

        public int getFromme() {
            return fromme;
        }

        public String getHeadimage() {
            return headimage;
        }

        public String getMessage() {
            return message;
        }

        public long getSendtime() {
            return sendtime;
        }

        public String getUserid() {
            return userid;
        }

        public String getUsername() {
            return username;
        }

        public List<ToidEntity> getToid() {
            return toid;
        }

        public static class ToidEntity {
            private String userid;
            private String username;

            public void setUserid(String userid) {
                this.userid = userid;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getUserid() {
                return userid;
            }

            public String getUsername() {
                return username;
            }
        }
    }
}
