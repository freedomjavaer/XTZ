package com.ypwl.xiaotouzi.netprotocol;

import com.ypwl.xiaotouzi.base.BaseNetProtocol;
import com.ypwl.xiaotouzi.bean.MyFocusBean;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.StringUtil;

import java.util.Map;

/**
 * function : 我的关注数据网络请求
 * <p>
 * Created by tengtao on 2016/4/22.
 */
public class MyHasFollowedProtocol extends BaseNetProtocol<MyFocusBean> {
    @Override
    protected String getInterfacePath() {
        return StringUtil.format(URLConstant.MY_FOCUS, GlobalUtils.token);
    }

    @Override
    protected Map<String, String> getInterfaceParams() {
        return null;
    }
}
