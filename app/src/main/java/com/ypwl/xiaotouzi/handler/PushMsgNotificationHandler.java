package com.ypwl.xiaotouzi.handler;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.XtzApp;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.event.InvestPlatformRefreshEvent;
import com.ypwl.xiaotouzi.event.PlatformSyncFinishedNotificationEvent;
import com.ypwl.xiaotouzi.manager.EventHelper;
import com.ypwl.xiaotouzi.ui.activity.MyInvestPlatformDetailActivity;
import com.ypwl.xiaotouzi.utils.CacheUtils;
import com.ypwl.xiaotouzi.utils.Util;
import com.ypwl.xiaotouzi.view.dialog.CustomDialog;

import java.util.Map;

/**
 * function : 推送消息处理器--Notification设置
 * <p/>
 * Created by lzj on 2015/11/13.
 */
public class PushMsgNotificationHandler extends UmengMessageHandler {

    private String p_name;
    private String pid;
    private Intent intent;
    private Context mContext;
    /** 自动记账同步完毕提醒 */
    private static final String PUSH_MSG_TYPE_AUTO_WRITEACCOUNT_HINT = "5";
    /** 回款提醒 */
    private static final String PUSH_MSG_TYPE_BACKMONEY_HINT = "3";

    @Override
    public Notification getNotification(Context context, UMessage msg) {
//        switch (msg.builder_id) {
//            case 1:
//                NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
//                RemoteViews myNotificationView = new RemoteViews(context.getPackageName(), R.layout.notification_view);
//                myNotificationView.setTextViewText(R.id.notification_title, msg.title);
//                myNotificationView.setTextViewText(R.id.notification_text, msg.text);
//                myNotificationView.setImageViewBitmap(R.id.notification_large_icon, getLargeIcon(context, msg));
//                myNotificationView.setImageViewResource(R.id.notification_small_icon, getSmallIconId(context, msg));
//                builder.setContent(myNotificationView);
//                builder.setContentTitle(msg.title)
//                        .setContentText(msg.text)
//                        .setTicker(msg.ticker)
//                        .setAutoCancel(true);
//                Notification mNotification = builder.build();
//                //由于Android v4包的bug，在2.3及以下系统，Builder创建出来的Notification，并没有设置RemoteView，故需要添加此代码
//                mNotification.contentView = myNotificationView;
//
//                return mNotification;
//            default:
//                //默认为0，若填写的builder_id并不存在，也使用默认。
//                return super.getNotification(context, msg);
//        }

        Map<String, String> dataMap = msg.extra;
        try {
            if (dataMap.containsKey("type") && dataMap.containsKey("p_name") && dataMap.containsKey("pid")){
                p_name = dataMap.get("p_name");
                pid = dataMap.get("pid");
                mContext = XtzApp.getApplication().getTopActivity();
                for (Map.Entry<String, String> entry : dataMap.entrySet()) {
                    if (PUSH_MSG_TYPE_AUTO_WRITEACCOUNT_HINT.equals(entry.getValue())) {
                        EventHelper.post(new InvestPlatformRefreshEvent());
                        CacheUtils.putBoolean(p_name + pid + "", true);
                        showDialog();
                    }
                }
            }
            return super.getNotification(context, msg);
        } catch (Exception e) {
            e.printStackTrace();
            return super.getNotification(context, msg);
        }

    }


    /** 自定义处理消息 */
//    @Override
//    public void dealWithNotificationMessage(final Context context, UMessage uMessage) {
//
//        //String custom = uMessage.custom;
//        //LogUtil.e("dealWithNotificationMessage",custom);
//
//
//        Map<String, String> dataMap = uMessage.extra;
//        p_name = dataMap.get("p_name");
//        pid = dataMap.get("pid");
//        //LogUtil.e("haha",p_name + pid + "-----------");
//        mContext = XtzApp.getApplication().getTopActivity();
//        if (dataMap == null) {
//            return;
//        }
//        for (Map.Entry<String, String> entry : dataMap.entrySet()) {
//            if (!"type".equals(entry.getKey())) {
//                continue;
//            }
//            if (PUSH_MSG_TYPE_BACKMONEY_HINT.equals(entry.getValue())) {
//                super.dealWithNotificationMessage(context, uMessage);
//            }
//            if (PUSH_MSG_TYPE_AUTO_WRITEACCOUNT_HINT.equals(entry.getValue())) {
//                EventHelper.post(new InvestPlatformRefreshEvent());
//                CacheUtils.putBoolean(p_name + pid + "", true);
//                showDialog();
//            }
//        }
//    }

    private void showDialog() {
        if(Util.legalLogin()==null){return;}
        new CustomDialog.AlertBuilder((Activity) mContext)
                .setTitleText(R.string.app_name)
                .setContentText(p_name + "平台数据同步完成")
                .setPositiveBtn("去看看", new ChoosePositiveClickListener())
                .setNegativeBtn("知道了", new ChooseCancelClickListener())
                .setCanceledOnTouchOutside(false)
                .create().show();
    }

    private class ChoosePositiveClickListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            if(XtzApp.getApplication().getTopActivity().getClass() == MyInvestPlatformDetailActivity.class){
                EventHelper.post(new PlatformSyncFinishedNotificationEvent());
                return;
            }
            intent = new Intent(mContext, MyInvestPlatformDetailActivity.class);
            intent.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA,pid);
            intent.putExtra(MyInvestPlatformDetailActivity.IS_AUTO,1);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
    }

    private class ChooseCancelClickListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            EventHelper.post(new PlatformSyncFinishedNotificationEvent());
        }
    }
}
