package com.ypwl.xiaotouzi.utils;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.manager.UmengEventHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * function:友盟统计工具类
 * <p/>
 * Created by tengtao on 2015/12/23.
 */
public class UmengUtils {
    /**
     * umeng统计平台搜索筛选条件关键字
     *
     * @param checkMap
     * @param currentPeriod
     * @param currentRate
     */
    public static void uMengTopChoice(final Map<Integer, Boolean> checkMap, final String currentPeriod, final String currentRate) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final HashMap<String, String> uMap = new HashMap<>();
                for (Map.Entry<Integer, Boolean> entry : checkMap.entrySet()) {
                    if (!entry.getValue()) {
                        continue;
                    }//没有选择就继续
                    int id = entry.getKey();
                    switch (id) {
                        case R.id.ipcp_choice_btn_number_1_1:
                            uMap.put("key0", "第三方托管");
                            break;
                        case R.id.ipcp_choice_btn_number_1_2:
                            uMap.put("key1", "第三方担保");
                            break;
                        case R.id.ipcp_choice_btn_number_1_3:
                            uMap.put("key19", "视频认证");
                            break;
                        case R.id.ipcp_choice_btn_number_2_1:
                            uMap.put("key2", "银行");
                            break;
                        case R.id.ipcp_choice_btn_number_2_2:
                            uMap.put("key3", "风投");
                            break;
                        case R.id.ipcp_choice_btn_number_2_3:
                            uMap.put("key4", "国资");
                            break;
                        case R.id.ipcp_choice_btn_number_2_4:
                            uMap.put("key5", "上市");
                            break;
                        case R.id.ipcp_choice_btn_number_3_1:
                            uMap.put("key6", "抵押");
                            break;
                        case R.id.ipcp_choice_btn_number_3_2:
                            uMap.put("key7", "信用");
                            break;
                        case R.id.ipcp_choice_btn_number_3_3:
                            uMap.put("key8", "票据");
                            break;
                        case R.id.ipcp_choice_btn_number_3_4:
                            uMap.put("key9", "债券转让");
                            break;
                        case R.id.ipcp_choice_btn_number_4_1:
                            uMap.put("key10", "北京");
                            break;
                        case R.id.ipcp_choice_btn_number_4_2:
                            uMap.put("key11", "上海");
                            break;
                        case R.id.ipcp_choice_btn_number_4_3:
                            uMap.put("key12", "广东");
                            break;
                        case R.id.ipcp_choice_btn_number_4_4:
                            uMap.put("key13", "浙江");
                            break;
                        case R.id.ipcp_choice_btn_number_4_5:
                            uMap.put("key14", "江苏");
                            break;
                        case R.id.ipcp_choice_btn_number_4_6:
                            uMap.put("key15", "山东");
                            break;
                        case R.id.ipcp_choice_btn_number_4_7:
                            uMap.put("key16", "其他");
                            break;
                    }
                    if (!StringUtil.isEmptyOrNull(currentPeriod)) {
                        uMap.put("key17", "标期长短:" + currentPeriod);
                    }
                    if (!StringUtil.isEmptyOrNull(currentRate)) {
                        uMap.put("key18", "平均利益:" + currentRate);
                    }
                }
                UIUtil.post(new Runnable() {
                    @Override
                    public void run() {
                        UmengEventHelper.onEventMap("UserFiltrate", uMap);
                    }
                });
            }
        }).start();
    }
}
