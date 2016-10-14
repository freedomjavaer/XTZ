package com.ypwl.xiaotouzi.netprotocol;

import com.ypwl.xiaotouzi.base.BaseNetProtocol;
import com.ypwl.xiaotouzi.bean.LoginBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.interf.INetRequestListener;
import com.ypwl.xiaotouzi.bean.XtzNetCreditNewsBean;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.Util;

import java.util.Map;

/**
 * function :晓投资主页网络数据请求
 * <p/>
 * Created by tengtao on 2016/3/15.
 */
public class XtzHomePagerProtocol extends BaseNetProtocol<XtzNetCreditNewsBean> {

    private int mPage = 1;

    @Override
    protected String getInterfacePath() {
        LoginBean loginBean = Util.legalLogin();
        return String.format(URLConstant.XTZ_NET_CREDIT_NEWS, mPage,loginBean!=null? GlobalUtils.token:"");
    }

    @Override
    protected Map<String, String> getInterfaceParams() {
        return null;
    }

    public void loadDataByPage(int page,INetRequestListener listener){
        this.mPage = page;
        loadData(listener, Const.REQUEST_GET);
    }

}
