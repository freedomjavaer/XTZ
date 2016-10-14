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
 * function : 删除标的网络请求
 * <p/>
 * Created by tengtao on 2016/3/28.
 */
public class DeleteBidProtocol extends BaseNetProtocol<CommonBean> {
    private String aids;
    @Override
    protected String getInterfacePath() {
        return StringUtil.format(URLConstant.BID_DETAIL_DELETE, GlobalUtils.token,aids);
    }

    @Override
    protected Map<String, String> getInterfaceParams() {
        return null;
    }

    public void loadData(String aids,INetRequestListener listener){
        this.aids = aids;
        loadData(listener, Const.REQUEST_GET);
    }
}
