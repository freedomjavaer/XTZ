package com.ypwl.xiaotouzi.netprotocol;

import com.ypwl.xiaotouzi.base.BaseNetProtocol;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.interf.INetRequestListener;
import com.ypwl.xiaotouzi.bean.NetCreditNewsDetailBean;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.PackageManagerUtil;
import com.ypwl.xiaotouzi.utils.StringUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.Util;

import java.util.Map;

/**
 * function :网贷新闻详情网络数据请求类
 * <p/>
 * Created by tengtao on 2016/3/15.
 */
public class NetCreditNewsDetailProtocol extends BaseNetProtocol<NetCreditNewsDetailBean> {
    private String news_id;
    private int mPage = 1;

    @Override
    protected String getInterfacePath() {
        String token = Util.legalLogin()==null?"": GlobalUtils.token;
        String version = PackageManagerUtil.getPackageVersionName(UIUtil.getContext());
        return StringUtil.format(URLConstant.NET_CREDIT_NEWS_DETAIL,token,news_id,version,mPage);
    }

    @Override
    protected Map<String, String> getInterfaceParams() {
        return null;
    }

    public void loadDataByPage(String news_id, int page, INetRequestListener listener){
        this.news_id = news_id;
        this.mPage = page;
        loadData(listener,Const.REQUEST_GET);
    }
}
