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
 * function : 取消关注平台网络请求
 * <p>
 * Created by tengtao on 2016/4/22.
 */
public class PlatformCancelFollowProtocol extends BaseNetProtocol<CommonBean> {
    private String mPid;
    @Override
    protected String getInterfacePath() {
        return StringUtil.format(URLConstant.PLATFORM_CANCEL_CELLECT, GlobalUtils.token, mPid);
    }

    @Override
    protected Map<String, String> getInterfaceParams() {
        return null;
    }

    public void loadData(String pid, INetRequestListener listener){
        this.mPid = pid;
        loadData(listener, Const.REQUEST_GET);
    }
}
