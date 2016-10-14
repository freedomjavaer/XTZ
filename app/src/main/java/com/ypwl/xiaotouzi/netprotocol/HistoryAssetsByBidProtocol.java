package com.ypwl.xiaotouzi.netprotocol;

import com.ypwl.xiaotouzi.base.BaseNetProtocol;
import com.ypwl.xiaotouzi.bean.InvestHistoryBean;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.StringUtil;

import java.util.Map;

/**
 * function : 历史资产：按标的分类的网络请求
 * <p/>
 * Created by tengtao on 2016/3/28.
 */
public class HistoryAssetsByBidProtocol extends BaseNetProtocol<InvestHistoryBean> {

    @Override
    protected String getInterfacePath() {
        return StringUtil.format(URLConstant.MYINVEST_VNVEST_HISTORY, GlobalUtils.token,"0");
    }

    @Override
    protected Map<String, String> getInterfaceParams() {
        return null;
    }

}
