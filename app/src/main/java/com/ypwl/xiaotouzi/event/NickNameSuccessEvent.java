package com.ypwl.xiaotouzi.event;

/**
 * 昵称修改成功的事件
 *
 * Created by PDK on 2016/5/20.
 */
public class NickNameSuccessEvent {

    public String nickName;

    public NickNameSuccessEvent(String nickName) {
        this.nickName = nickName;
    }
}
