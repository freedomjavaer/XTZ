package com.ypwl.xiaotouzi.ui.helper;

/**
 * function:账户绑定交互
 *
 * <br/>
 * Created by lzj on 2016/5/3.
 */
public interface AccountBindView {

    void refreshViewData();

    void loading();

    void bindChangeFailed(String msg);

    void bindChangeSuccess();
}
