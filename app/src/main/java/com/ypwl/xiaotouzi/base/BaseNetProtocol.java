package com.ypwl.xiaotouzi.base;

import com.alibaba.fastjson.JSON;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.interf.INetRequestListener;
import com.ypwl.xiaotouzi.interf.IRequestCallback;
import com.ypwl.xiaotouzi.manager.net.NetHelper;
import com.ypwl.xiaotouzi.utils.StringUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * function :GET网络请求数据基类
 * <p/>
 * Created by tengtao on 2016/3/15.
 */

public abstract class BaseNetProtocol<T> {
    /** 接口地址 */
    protected abstract String getInterfacePath();

    /** post请求参数 */
    protected abstract Map<String,String> getInterfaceParams();

    /**
     * 解析json对象
     *
     * @param json  json数据
     * @return 返回解析传入的javabean对象
     */
    protected T parseJson(String json)
    {
        if(StringUtil.isEmptyOrNull(json)){return null;}
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        Type t = type.getActualTypeArguments()[0];
        return JSON.parseObject(json, t);
    }

    /** 加载网络数据 */
    public BaseNetProtocol loadData(INetRequestListener<T> listener,int requestType){
        if(requestType == Const.REQUEST_POST) {
            requestByPost(listener);
        }else {
            requestByGet(listener);
        }
        return this;
    }

    /** Post请求网络数据 */
    private void requestByPost(final INetRequestListener<T> listener){
        NetHelper.post(getInterfacePath(), getInterfaceParams(), new IRequestCallback<String>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onFailure(Exception e) {
                if (listener != null) {
                    listener.netRequestCompleted();
                    listener.netRequestSuccess(null, false);
                }
            }

            @Override
            public void onSuccess(String object) {
                if (listener != null) {
                    listener.netRequestCompleted();
                    listener.netRequestSuccess(parseJson(object), true);
                }
            }
        });
    }

    /** Get请求网络数据 */
    private void requestByGet(final INetRequestListener<T> listener){
        NetHelper.get(getInterfacePath(), new IRequestCallback<String>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onFailure(Exception e) {
                if (listener != null) {
                    listener.netRequestCompleted();
                    listener.netRequestSuccess(null, false);
                }
            }

            @Override
            public void onSuccess(String object) {
                if (listener != null) {
                    listener.netRequestCompleted();
                    listener.netRequestSuccess(parseJson(object), true);
                }
            }
        });
    }
}

