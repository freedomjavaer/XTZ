package com.ypwl.xiaotouzi.netprotocol;

import com.ypwl.xiaotouzi.base.BaseNetProtocol;
import com.ypwl.xiaotouzi.bean.SingleBidBackMoneyBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.interf.INetRequestListener;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.StringUtil;

import java.util.Map;

/**
 * function : 单个标的回款详情网络请求
 * <p/>
 * Created by tengtao on 2016/3/28.
 */
public class SingleBidBackMoneyProtocol extends BaseNetProtocol<SingleBidBackMoneyBean> {
    private String aid;
    @Override
    protected String getInterfacePath() {
        return StringUtil.format(URLConstant.SINGLE_BID_BACK_MONEY_DETAIL, GlobalUtils.token,aid);
    }

    @Override
    protected Map<String, String> getInterfaceParams() {
        return null;
    }

    public void loadData(String aid, INetRequestListener<SingleBidBackMoneyBean> listener){
        this.aid = aid;
        loadData(listener, Const.REQUEST_GET);
    }
}
