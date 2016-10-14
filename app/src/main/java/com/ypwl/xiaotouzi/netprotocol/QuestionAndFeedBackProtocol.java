package com.ypwl.xiaotouzi.netprotocol;

import com.ypwl.xiaotouzi.base.BaseNetProtocol;
import com.ypwl.xiaotouzi.bean.QuestionAndFeedBackBean;
import com.ypwl.xiaotouzi.common.URLConstant;

import java.util.Map;

/**
 * function : 常见问题和反馈网络请求
 * <p/>
 * Created by tengtao on 2016/5/3.
 */
public class QuestionAndFeedBackProtocol extends BaseNetProtocol<QuestionAndFeedBackBean> {

    @Override
    protected String getInterfacePath() {
        return URLConstant.QUESTION_AND_FEEDBACK;
    }

    @Override
    protected Map<String, String> getInterfaceParams() {
        return null;
    }
}
