package com.ypwl.xiaotouzi.event;

/**
 * function : 登录成功事件
 * </p>
 * Created by lzj on 2015/11/6.
 */
public class LoginStateEvent {
    /**
     * true:已登录，false：未登录
     */
    public boolean hasLogin = false;

    public LoginStateEvent(boolean hasLogin) {
        this.hasLogin = hasLogin;
    }

    @Override
    public String toString() {
        return "LoginStateEvent{" +
                "hasLogin=" + hasLogin +
                '}';
    }
}
