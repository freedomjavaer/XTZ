package com.ypwl.xiaotouzi.interf;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * function:网络请求回调接口(支持javaBean对象返回)
 *
 * <br/>
 * Created by lzj on 2016/4/1.
 */
public abstract class IRequestCallback<T> {

    public Type mClassType;

    public IRequestCallback() {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        mClassType = ((ParameterizedType) superclass).getActualTypeArguments()[0];
    }


    /** 请求访问开始和进行中... */
    public abstract void onStart();

    /** 请求访问失败 */
    public abstract void onFailure(Exception e);

    /** 成功返回数据 */
    public abstract void onSuccess(T t);

    /** 文件上传下载 进度回调 */
    public void onProgressUpdate(long writedSize, long totalSize, boolean completed) {
    }
}