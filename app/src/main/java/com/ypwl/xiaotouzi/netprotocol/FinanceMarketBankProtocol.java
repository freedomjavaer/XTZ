package com.ypwl.xiaotouzi.netprotocol;

import com.ypwl.xiaotouzi.base.BaseNetProtocol;
import com.ypwl.xiaotouzi.bean.FinanceMarketBankBean;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.StringUtil;

import java.util.Map;

/**
 * function : 金融超市---银行列表网络协议
 * <p>
 * Created by tengtao on 2016/4/14.
 */
public class FinanceMarketBankProtocol extends BaseNetProtocol<FinanceMarketBankBean> {

    @Override
    protected String getInterfacePath() {
        return StringUtil.format(URLConstant.FINANCE_MARKET_BANK_LIST, GlobalUtils.token);
    }

    @Override
    protected Map<String, String> getInterfaceParams() {
        return null;
    }
}
