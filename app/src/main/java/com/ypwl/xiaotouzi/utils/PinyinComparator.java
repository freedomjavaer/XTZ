package com.ypwl.xiaotouzi.utils;

import com.ypwl.xiaotouzi.bean.HistoryAssetsByPlatformProtocolBean;

import java.util.Comparator;
/**
 * function :Pinyin匹配
 * Created by llc on 2016/3/30 10:20
 * Email：licailuo@qq.com
 */
public class PinyinComparator implements Comparator<HistoryAssetsByPlatformProtocolBean.ListEntity> {

    public int compare(HistoryAssetsByPlatformProtocolBean.ListEntity o1, HistoryAssetsByPlatformProtocolBean.ListEntity o2) {
        if (o1.getSortLetters().equals("@") || o2.getSortLetters().equals("#")) {
            return -1;
        } else if (o1.getSortLetters().equals("#")
                || o2.getSortLetters().equals("@")) {
            return 1;
        } else {
            return o1.getSortLetters().compareTo(o2.getSortLetters());
        }
    }

}
