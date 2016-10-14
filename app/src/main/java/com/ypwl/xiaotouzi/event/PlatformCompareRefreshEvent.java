package com.ypwl.xiaotouzi.event;

/**
 * @author tengtao
 * @time ${DATA} 14:39
 * @des ${对比平台刷新事件}
 */
public class PlatformCompareRefreshEvent {

    public boolean isExecuteRefresh = false;
    /**
     * 0：表示只刷新关注相关数据
     * 1：表示刷新全部数据
     */
    public int scope;

    public PlatformCompareRefreshEvent(boolean isExecuteRefresh, int scope) {
        this.isExecuteRefresh = isExecuteRefresh;
        this.scope = scope;
    }
}
