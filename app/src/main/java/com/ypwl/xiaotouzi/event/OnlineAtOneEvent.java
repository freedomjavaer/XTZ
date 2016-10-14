package com.ypwl.xiaotouzi.event;

/**
 * function: 同城在线聊天@用户事件
 *
 * <p>Created by lzj on 2016/3/22.</p>
 */
public class OnlineAtOneEvent {
    public String userName;
    public String userid;

    public OnlineAtOneEvent(String userName, String userid) {
        this.userName = userName;
        this.userid = userid;
    }
}
