package com.ypwl.xiaotouzi.event;

import com.ypwl.xiaotouzi.bean.MyInvestPlatformDetailBean;

/**
 * function:刷新我的投资-资产-平台详情顶部数据事件.
 *
 * <p>Created by lzj on 2016/3/24.</p>
 */
public class MyInvestPlatformDetailRefreshEvent {

    public MyInvestPlatformDetailBean bean;

    public MyInvestPlatformDetailRefreshEvent(MyInvestPlatformDetailBean bean) {
        this.bean = bean;
    }
}
