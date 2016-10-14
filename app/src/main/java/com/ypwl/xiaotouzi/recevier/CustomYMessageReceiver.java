package com.ypwl.xiaotouzi.recevier;

import android.content.Context;
import android.content.Intent;

import com.ypwl.xiaotouzi.XtzApp;
import com.ypwl.xiaotouzi.base.BaseBroadcastReceiver;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.event.MessageBoxHintEvent;
import com.ypwl.xiaotouzi.im.YConst;
import com.ypwl.xiaotouzi.im.YMessage;
import com.ypwl.xiaotouzi.im.push.YPushAgent;
import com.ypwl.xiaotouzi.manager.EventHelper;
import com.ypwl.xiaotouzi.utils.CacheUtils;
import com.ypwl.xiaotouzi.utils.LogUtil;
import com.ypwl.xiaotouzi.utils.Util;
import com.ypwl.xiaotouzi.utils.ViewUtil;

/**
 * function : 新回复消息接收.
 *
 * Created by lzj on 2016/1/31.
 */
public class CustomYMessageReceiver extends BaseBroadcastReceiver {

    /** 消息类型--收件箱新消息提示 */
    private static final String TYPE = "2";

    @Override
    public void onReceive(Context context, Intent intent) {
        YMessage ymessage = intent.getParcelableExtra(YConst.KEY_PUSH_MSG);
        LogUtil.e(TAG, "message : " + ymessage.toString());

        if (!YPushAgent.getInstance().connected()) {
            return;
        }
        if (ymessage.getType() == YMessage.TYPE_PUSH_MESSAGE
                && TYPE.equals(ymessage.getMessage())) {
            EventHelper.post(new MessageBoxHintEvent());//Fragment已经打开时能够收到这个事件
            CacheUtils.putBoolean(Const.KEY_NOTE_REPLY_NEW, true);//缓存本地，收件箱按钮初始化显示状态
        } else if (ymessage.getType() == YMessage.TYPE_REMOTE_LOGIN) {//帐号异地登录
            if (Util.legalLogin() != null)
                ViewUtil.againLoginAndRegister(XtzApp.getApplication().getTopActivity());
        }
    }

}