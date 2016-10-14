package com.ypwl.xiaotouzi.netprotocol;

import com.ypwl.xiaotouzi.base.BaseNetProtocol;
import com.ypwl.xiaotouzi.bean.NewsReplyBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.interf.INetRequestListener;
import com.ypwl.xiaotouzi.ui.activity.NetCreditNewsDetailActivity;
import com.ypwl.xiaotouzi.utils.GlobalUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * function : 新闻回复
 * <p/>
 * Created by tengtao on 2016/3/18.
 */
public class NewsReplyProtocol extends BaseNetProtocol<NewsReplyBean> {

    private String news_id;
    private String content;
    private String toid;
    private int replyType = NetCreditNewsDetailActivity.REPLY_NEWS;

    public NewsReplyProtocol(String news_id,String content,String toid){
        this.news_id = news_id;
        this.content = content;
        this.toid = toid;
    }
    @Override
    protected String getInterfacePath() {
        return URLConstant.NEWS_DETAIL_REPLY;
    }

    @Override
    protected Map<String, String> getInterfaceParams() {
        Map<String, String> params = new HashMap<>();
        params.put("token", GlobalUtils.token);
        params.put("news_id", news_id);
        params.put("content", content);
        if(replyType==NetCreditNewsDetailActivity.REPLY_COMMENT){
            params.put("toid",toid);
        }
        return params;
    }

    public void loadData(int replyType,INetRequestListener listener){
        this.replyType = replyType;
        loadData(listener, Const.REQUEST_POST);
    }
}
