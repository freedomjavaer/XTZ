package com.ypwl.xiaotouzi.netprotocol;

import com.ypwl.xiaotouzi.base.BaseNetProtocol;
import com.ypwl.xiaotouzi.bean.InvestAnalysisByFenbuBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.interf.INetRequestListener;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.StringUtil;

import java.util.Map;

/**
 * function : 投资分析--投资分布--网络请求
 * <p/>
 * Created by tengtao on 2016/4/11.
 */
public class InvestAnalysisByFenbuProtocol extends BaseNetProtocol<InvestAnalysisByFenbuBean> {

    private String type;
    @Override
    protected String getInterfacePath() {
        return StringUtil.format(URLConstant.INVEST_ANALYSIS_BY_FENBU, GlobalUtils.token, type);
    }

    @Override
    protected Map<String, String> getInterfaceParams() {
        return null;
    }

    public void loadData(String type, INetRequestListener listener){
        this.type = type;
        loadData(listener, Const.REQUEST_GET);
    }

}
