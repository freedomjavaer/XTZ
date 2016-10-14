package com.ypwl.xiaotouzi.netprotocol;

import com.ypwl.xiaotouzi.base.BaseNetProtocol;
import com.ypwl.xiaotouzi.bean.CommonBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.interf.INetRequestListener;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.StringUtil;

import java.util.Map;

/**
 * function : 更改回款状态请求
 * <p>
 * Created by tengtao on 2016/3/23.
 */
public class ChangeReturnedStatusProtocol extends BaseNetProtocol<CommonBean> {

    private String rids;
    private String type;
    @Override
    protected String getInterfacePath() {
        return StringUtil.format(URLConstant.CHANGING_THE_STATUS_OF_PAYMENT, GlobalUtils.token,rids,type);
    }

    @Override
    protected Map<String, String> getInterfaceParams() {
        return null;
    }

    public void loadData(String rids, String type, INetRequestListener<CommonBean> listener){
        this.rids = rids;
        this.type = type;
        loadData(listener, Const.REQUEST_GET);
    }
}
