package com.ypwl.xiaotouzi.netprotocol;

import com.ypwl.xiaotouzi.base.BaseNetProtocol;
import com.ypwl.xiaotouzi.bean.PlatformCompareBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.interf.INetRequestListener;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.StringUtil;

import java.util.Map;

/**
 * function : 平台对比数据网络请求
 * <p>
 * Created by tengtao on 2016/4/22.
 */
public class PlatformCompareProtocol extends BaseNetProtocol<PlatformCompareBean> {

    private String mType;//请求对比类型：周、月
    private String mPids;//对比平台id拼接字符串
    @Override
    protected String getInterfacePath() {
        return StringUtil.format(URLConstant.PLAT_COMPARE, GlobalUtils.token, mPids, mType);
    }

    @Override
    protected Map<String, String> getInterfaceParams() {
        return null;
    }

    public void loadData(String pids, String type, INetRequestListener listener){
        this.mPids = pids;
        this.mType = type;
        loadData(listener, Const.REQUEST_GET);
    }
}
