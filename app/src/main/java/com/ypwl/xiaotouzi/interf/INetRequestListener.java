package com.ypwl.xiaotouzi.interf;

/**
 * function :请求数据监听
 * <p/>
 * Created by tengtao on 2016/3/15.
 */
public interface INetRequestListener<T> {

    /** 请求--完成 */
    void netRequestCompleted();
    /** 请求--成功/失败 */
    void netRequestSuccess(T bean,boolean isSuccess);
}
