package com.ypwl.xiaotouzi.netprotocol;

import com.ypwl.xiaotouzi.base.BaseNetProtocol;
import com.ypwl.xiaotouzi.bean.HistoryAssetsByPlatformProtocolBean;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.StringUtil;

import java.util.Map;

/**
 * function : 历史资产：按平台请求网络数据
 * <p/>
 * Created by tengtao on 2016/3/28.
 */
public class HistoryAssetesByPlatformProtocol extends BaseNetProtocol<HistoryAssetsByPlatformProtocolBean> {
    @Override
    protected String getInterfacePath() {
        return StringUtil.format(URLConstant.MYINVEST_VNVEST_HISTORY, GlobalUtils.token,1);
    }

    @Override
    protected Map<String, String> getInterfaceParams() {
        return null;
    }
}
