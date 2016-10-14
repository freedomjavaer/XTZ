package com.ypwl.xiaotouzi.exception;


import com.ypwl.xiaotouzi.base.BaseException;

/**
 * function : 通用异常用于传递消息.<br/>
 * Created by lzj on 2015/11/3.
 */
@SuppressWarnings("unused")
public class CommonException extends BaseException {

    public CommonException(int type) {
        super(type);
    }

    public CommonException(int type, String detailMessage) {
        super(type, detailMessage);
    }

    public CommonException(int type, Throwable throwable) {
        super(type, throwable);
    }

    public CommonException(int type, String detailMessage, Throwable throwable) {
        super(type, detailMessage, throwable);
    }

    @Override
    public String toString() {
        return "CommonException [type=" + type + "]";
    }

}
