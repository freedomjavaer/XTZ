package com.ypwl.xiaotouzi.manager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tengtao
 * @time ${DATA} 16:20
 * @des ${平台对比单例类}
 */
public class PlatformCompareManager {

    public static List<String> hasChooseCompare;

    public static List<String> getList() {
        if (null == hasChooseCompare) {
            synchronized (PlatformCompareManager.class) {
                if (null == hasChooseCompare) {
                    hasChooseCompare = new ArrayList<>();
                }
            }
        }
        return hasChooseCompare;
    }
}
