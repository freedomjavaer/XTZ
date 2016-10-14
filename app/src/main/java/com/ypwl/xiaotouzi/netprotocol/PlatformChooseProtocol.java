package com.ypwl.xiaotouzi.netprotocol;

import com.ypwl.xiaotouzi.base.BaseNetProtocol;
import com.ypwl.xiaotouzi.bean.PlatformChooseBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.StringUtil;

import java.util.Map;

/**
 * function : 平台选择数据网络请求
 * <p/>
 * Created by tengtao on 2016/5/5.
 */
public class PlatformChooseProtocol extends BaseNetProtocol<PlatformChooseBean> {
    //获取数据信息，1表示来源平台对比,0来源于记账
    @Override
    protected String getInterfacePath() {
        return StringUtil.format(URLConstant.PLAT_CHOOSE, GlobalUtils.token, Const.PLATFORM_CHOOSE_REQUEST_FROM);
    }

    @Override
    protected Map<String, String> getInterfaceParams() {
        return null;
    }
}
