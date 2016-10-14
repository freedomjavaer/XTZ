package com.ypwl.xiaotouzi.manager;

import com.umeng.analytics.MobclickAgent;
import com.ypwl.xiaotouzi.common.Configs;
import com.ypwl.xiaotouzi.utils.UIUtil;

import java.util.Map;

/**
 * function : 推送计数事件到umeng管理后台.
 *
 * Created by lzj on 2016/1/29.
 */
public class UmengEventHelper {

    /**
     * 上传计数事件到umeng管理后台(异步处理)
     *
     * @param eventId 事件ID
     */
    public static void onEvent(String eventId) {
        if (!Configs.DEBUG) {
            MobclickAgent.onEvent(UIUtil.getContext(), eventId);
        }
    }

    /**
     * 上传参数集合事件到umeng管理后台(异步处理)
     *
     * @param eventId 事件ID
     * @param map     事件参数map
     */
    public static void onEventMap(String eventId, Map<String, String> map) {
        if (!Configs.DEBUG) {
            MobclickAgent.onEvent(UIUtil.getContext(), eventId, map);
        }
    }
}
