package com.ypwl.xiaotouzi.ui.helper;

/**
 * function:登录交互接口
 *
 * <br/>
 * Created by lzj on 2016/4/18.
 */
public interface LoginView {
    void loginLoading();

    void authorizeFailed();

    void loginSuccess();

    void loginFailed();
}
