package com.ypwl.xiaotouzi.netprotocol;

import com.ypwl.xiaotouzi.base.BaseNetProtocol;
import com.ypwl.xiaotouzi.bean.InvestByPlatformBidBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.interf.INetRequestListener;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.StringUtil;

import java.util.Map;

/**
 * function : 全部回款：按平台分类，在投标的和结束标的的网络数据请求
 * <p>
 * Created by tengtao on 2016/3/25.
 */
public class InvestByPlatformBidProtocol extends BaseNetProtocol<InvestByPlatformBidBean> {

    private String option;//0待回，1已回
    @Override
    protected String getInterfacePath() {
        return StringUtil.format(URLConstant.INVEST_BY_PLATFORM_BID, GlobalUtils.token,option);
    }

    @Override
    protected Map<String, String> getInterfaceParams() {
        return null;
    }

    public void loadData(String type,INetRequestListener<InvestByPlatformBidBean> listener){
        this.option = type;
        loadData(listener, Const.REQUEST_GET);
    }
}
