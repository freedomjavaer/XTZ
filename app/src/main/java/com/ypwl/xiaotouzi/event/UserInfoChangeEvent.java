package com.ypwl.xiaotouzi.event;

/**
 * function : 用户信息发送改变事件
 * </p>
 * Created by lzj on 2015/11/7.
 */
public class UserInfoChangeEvent {
    public boolean infoChange = false;

    public UserInfoChangeEvent(boolean infoChange) {
        this.infoChange = infoChange;
    }

    @Override
    public String toString() {
        return "UserInfoChangeEvent{" +
                "infoChange=" + infoChange +
                '}';
    }
}
