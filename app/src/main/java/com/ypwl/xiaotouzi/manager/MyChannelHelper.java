package com.ypwl.xiaotouzi.manager;

import com.umeng.analytics.AnalyticsConfig;
import com.ypwl.xiaotouzi.utils.LogUtil;
import com.ypwl.xiaotouzi.utils.SignUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;

import java.util.HashMap;
import java.util.Map;


/**
 * function : 渠道号管理帮助类
 * </p>
 * Created by lzj on 2016/1/6
 */
public class MyChannelHelper {
    private static final String TAG = SignUtil.class.getSimpleName();

    private static Map<String, Integer> mChannelMap;

    static {
        initData();
    }

    private synchronized static void initData() {
        LogUtil.e(TAG, "initMap");
        mChannelMap = new HashMap<>();
        mChannelMap.put("360", 1);
        mChannelMap.put("baidu", 2);
        mChannelMap.put("yingyongbao", 3);
        mChannelMap.put("wandoujia", 4);
        mChannelMap.put("anzhi", 5);
        mChannelMap.put("xiaomi", 6);
        mChannelMap.put("huawei", 7);
        mChannelMap.put("meizu", 8);
        mChannelMap.put("lianxiang", 9);
        mChannelMap.put("sogou", 10);
        mChannelMap.put("oppo", 11);
        mChannelMap.put("jinli", 12);
        mChannelMap.put("leshi", 13);
        mChannelMap.put("ppzhushou", 14);
        mChannelMap.put("anzhuoshichang", 15);
        mChannelMap.put("91zhushou", 16);
        mChannelMap.put("google", 17);
        mChannelMap.put("other", 18);
    }

    /** 获取渠道号id */
    public static int getMyChannel() {
        int result = 0;
        if (mChannelMap == null) {
            initData();
        }
        String umengChannel = AnalyticsConfig.getChannel(UIUtil.getContext());
        LogUtil.e(TAG, "umengChannel : " + umengChannel);
        for (Map.Entry<String, Integer> entry : mChannelMap.entrySet()) {
            if (entry.getKey().equals(umengChannel)) {
                result = entry.getValue();
                break;
            }
        }
        LogUtil.e(TAG, "getMyChannel : " + result);
        return result;
    }
}
