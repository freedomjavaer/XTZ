package com.ypwl.xiaotouzi.bean;

import java.util.List;

/**
 * function:自己发送的消息对象
 *
 * <p>Created by lzj on 2016/3/22.</p>
 */
public class OnlineSendMsgBean {

    /**
     * message : tut啦啦啦啦啦
     * sendtime : 1458612389000
     * toid : [{"userid":"123","username":"lzj"},{"userid":"123","username":"lzj"}]
     */

    private String message;
    private long sendtime;
    /**
     * userid : 123
     * username : lzj
     */

    private List<ToidEntity> toid;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSendtime(long sendtime) {
        this.sendtime = sendtime;
    }

    public void setToid(List<ToidEntity> toid) {
        this.toid = toid;
    }

    public String getMessage() {
        return message;
    }

    public long getSendtime() {
        return sendtime;
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
