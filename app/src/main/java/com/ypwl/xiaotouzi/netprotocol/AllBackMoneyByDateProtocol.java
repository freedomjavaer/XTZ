package com.ypwl.xiaotouzi.netprotocol;

import com.ypwl.xiaotouzi.base.BaseNetProtocol;
import com.ypwl.xiaotouzi.bean.AllBackByDateBean;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.StringUtil;

import java.util.Map;

/**
 * function : 全部回款--日期分组数据请求
 * <p/>
 * Created by tengtao on 2016/3/24.
 */
public class AllBackMoneyByDateProtocol extends BaseNetProtocol<AllBackByDateBean> {

    @Override
    protected String getInterfacePath() {
        return StringUtil.format(URLConstant.ALL_BACK_MONEY_BY_DATE, GlobalUtils.token);
    }

    @Override
    protected Map<String, String> getInterfaceParams() {
        return null;
    }
}
