package com.ypwl.xiaotouzi.interf;

import com.ypwl.xiaotouzi.bean.ThirdAuthUserInfoBean;

/**
 * function :三方平台认证回调接口
 * <p/>
 * Created by tengtao on 2016/5/3.
 */
public interface IThirdAuthCallback {

    /** 请求--完成 */
    void onFailed();

    /** 请求--成功/失败 */
    void onSuccess(ThirdAuthUserInfoBean bean);
}
