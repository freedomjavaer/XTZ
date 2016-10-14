package com.ypwl.xiaotouzi.utils;

import com.ypwl.xiaotouzi.bean.OnlineMsgBean;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortUtil {

    /**
     * 按时间降序排序---同城在线消息列表
     */
    public static void sortDescendingByTime(List<OnlineMsgBean.MsgEntity> msgList) {
        if (msgList == null || msgList.size() < 2) {
            return;
        }
        Collections.sort(msgList, new TimeComparatorDescending());
    }

    /** 时间降序比较器 ---同城在线消息列表 */
    private static class TimeComparatorDescending implements Comparator<OnlineMsgBean.MsgEntity> {
        @Override
        public int compare(OnlineMsgBean.MsgEntity lhs, OnlineMsgBean.MsgEntity rhs) {
            if (lhs.getSendtime() < rhs.getSendtime()) {
                return 1;
            } else if (lhs.getSendtime() == rhs.getSendtime()) {
                return 0;
            }
            return -1;
        }
    }

    /**
     * 按时间升序排序---同城在线消息列表
     */
    public static void sortAscendingByTime(List<OnlineMsgBean.MsgEntity> msgList) {
        if (msgList == null || msgList.size() < 2) {
            return;
        }
        Collections.sort(msgList, new TimeComparatorAscending());
    }


    /** 按时间升序排序---同城在线消息列表 */
    private static class TimeComparatorAscending implements Comparator<OnlineMsgBean.MsgEntity> {

        @Override
        public int compare(OnlineMsgBean.MsgEntity lhs, OnlineMsgBean.MsgEntity rhs) {
            if (lhs.getSendtime() > rhs.getSendtime()) {
                return 1;
            } else if (lhs.getSendtime() == rhs.getSendtime()) {
                return 0;
            }
            return -1;
        }
    }

}
