package com.ypwl.xiaotouzi.handler;

import android.content.Context;
import android.content.Intent;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.ypwl.xiaotouzi.XtzApp;
import com.ypwl.xiaotouzi.ui.activity.CalendarBackMoneyActivity;
import com.ypwl.xiaotouzi.ui.activity.SplashActivity;
import com.ypwl.xiaotouzi.utils.LogUtil;
import com.ypwl.xiaotouzi.utils.Util;

import java.util.HashMap;
import java.util.Map;

/**
 * function : 推送消息处理器--点击跳转事件处理
 * <p/>
 * Created by lzj on 2015/11/13.
 */
public class PushMsgNotificationClickHandler extends UmengNotificationClickHandler {

    private static final String TAG = PushMsgNotificationClickHandler.class.getSimpleName();
    /** 回款提醒 */
    private static final String PUSH_MSG_TYPE_BACKMONEY_HINT = "3";
    private String p_name;
    private String pid;

    private Context mContext;

    private UMessage mTempUMengMsg;

    //    Message - text : 您有一笔来自轩辕财富官网的回款，2天后到期。
    //    Message - title : 晓投资
    //    Message - extra : {pid=2448, p_name=轩辕财富官网, aid=10395, type=3, rid=54044}

    @Override
    public void handleMessage(Context context, UMessage uMessage) {
        LogUtil.e(TAG, "handleMessage - text : " + uMessage.text);
        LogUtil.e(TAG, "handleMessage - title : " + uMessage.title);
        LogUtil.e(TAG, "handleMessage - extra : " + uMessage.extra);
        mContext = context;
        mTempUMengMsg = uMessage;
        super.handleMessage(context, uMessage);
    }

    private void handleClickEvent() {
        Intent intent = null;
        Map<String, String> dataMap = mTempUMengMsg.extra;
        p_name = dataMap.get("p_name");
        pid = dataMap.get("pid");
        if (dataMap == null) {
            return;
        }
        for (Map.Entry<String, String> entry : dataMap.entrySet()) {
            if (!"type".equals(entry.getKey())) {
                continue;
            }
            if (PUSH_MSG_TYPE_BACKMONEY_HINT.equals(entry.getValue())) {
                intent = new Intent(mContext, CalendarBackMoneyActivity.class);
            }
        }
        if (intent != null && Util.legalLogin() != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        } else {
            openMyApp(mContext);
        }
    }

    private void openMyApp(Context context) {
        XtzApp.getApplication().removeAll();
        Intent intentForLauncher = new Intent(context, SplashActivity.class);
        intentForLauncher.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intentForLauncher);
    }

    @Override
    public void dismissNotification(Context context, UMessage msg) {
        super.dismissNotification(context, msg);
        MobclickAgent.onEvent(context, "dismiss_notification");
    }

    @Override
    public void launchApp(Context context, UMessage msg) {
        super.launchApp(context, msg);
        Map<String, String> map = new HashMap<>();
        map.put("action", "launch_app");
        MobclickAgent.onEvent(context, "click_notification", map);
        //handleClickEvent();
    }

    @Override
    public void openActivity(Context context, UMessage msg) {
        super.openActivity(context, msg);
        Map<String, String> map = new HashMap<>();
        map.put("action", "open_activity");
        MobclickAgent.onEvent(context, "click_notification", map);
        handleClickEvent();
    }

    @Override
    public void openUrl(Context context, UMessage msg) {
        super.openUrl(context, msg);
        Map<String, String> map = new HashMap<>();
        map.put("action", "open_url");
        MobclickAgent.onEvent(context, "click_notification", map);
    }

    @Override
    public void dealWithCustomAction(Context context, UMessage msg) {
        super.dealWithCustomAction(context, msg);
        Map<String, String> map = new HashMap<>();
        map.put("action", "custom_action");
        MobclickAgent.onEvent(context, "click_notification", map);
    }

    @Override
    public void autoUpdate(Context context, UMessage msg) {
        super.autoUpdate(context, msg);
        Map<String, String> map = new HashMap<>();
        map.put("action", "auto_update");
        MobclickAgent.onEvent(context, "click_notification", map);
    }
}
