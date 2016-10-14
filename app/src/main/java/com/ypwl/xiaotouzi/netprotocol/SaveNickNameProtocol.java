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
 * function : 保存昵称的网络请求
 * <p/>
 * Created by tengtao on 2016/3/21.
 */
public class SaveNickNameProtocol extends BaseNetProtocol<CommonBean> {

    private String newUserName;

    @Override
    protected String getInterfacePath() {
        return StringUtil.format(URLConstant.USER_INFO_UPDATE, GlobalUtils.token, GlobalUtils.toURLEncoded(newUserName));
    }

    @Override
    protected Map<String, String> getInterfaceParams() {
        return null;
    }

    public void loadData(String nickName,INetRequestListener<CommonBean> listener){
        this.newUserName = nickName;
        loadData(listener,Const.REQUEST_GET);
    }

}
