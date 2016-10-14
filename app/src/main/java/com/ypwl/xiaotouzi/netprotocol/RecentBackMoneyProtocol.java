package com.ypwl.xiaotouzi.netprotocol;

import com.ypwl.xiaotouzi.base.BaseNetProtocol;
import com.ypwl.xiaotouzi.bean.RecentBackMoneyBean;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.StringUtil;

import java.util.Map;

/**
 * function : 我的投资--近期回款列表网络数据请求
 * <p>
 * Created by tengtao on 2016/3/22.
 */
public class RecentBackMoneyProtocol extends BaseNetProtocol<RecentBackMoneyBean> {

    @Override
    protected String getInterfacePath() {
        return StringUtil.format(URLConstant.RECENT_BACK_MONEY, GlobalUtils.token);
    }

    @Override
    protected Map<String, String> getInterfaceParams() {
        return null;
    }
}
