package com.ypwl.xiaotouzi.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.bean.LoginBean;
import com.ypwl.xiaotouzi.bean.OnlineMsgBean;
import com.ypwl.xiaotouzi.event.OnlineAtOneEvent;
import com.ypwl.xiaotouzi.manager.EventHelper;
import com.ypwl.xiaotouzi.utils.ClipboardUtil;
import com.ypwl.xiaotouzi.utils.DateTimeUtil;
import com.ypwl.xiaotouzi.utils.ImgLoadUtil;
import com.ypwl.xiaotouzi.utils.LogUtil;
import com.ypwl.xiaotouzi.utils.SortUtil;
import com.ypwl.xiaotouzi.utils.StringUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.Util;
import com.ypwl.xiaotouzi.view.CircleImageView;
import com.ypwl.xiaotouzi.view.dialog.CustomDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * function : 同城在线消息列表数据适配器
 * <p/>
 * <p>Created by lzj on 2015/1/25.<p/>
 */
public class InvestFriendCircleMessageAdapter extends RecyclerView.Adapter<InvestFriendCircleMessageAdapter.BaseViewHolder> {

    private String TAG = InvestFriendCircleMessageAdapter.class.getSimpleName();
    private Activity mActivity;
    private String mSelfUserId = "";
    private List<OnlineMsgBean.MsgEntity> mDataList = new ArrayList<>();
    private static final int TYPE_MSG_LOG = -1;
    private static final int TYPE_MSG_OTHER = 0;
    private static final int TYPE_MSG_ME = 1;
    private static final String LOG_NAME = "XTZ_LOG_NAME_InvestFriendCircleMessageAdapter_timestamp";


    public InvestFriendCircleMessageAdapter(Activity context) {
        this.mActivity = context;
        LoginBean loginBean = Util.legalLogin();
        if (loginBean != null) {
            this.mSelfUserId = loginBean.getUserid();
        }
    }

    /** 刷新视图数据 */
    public void refreshData(List<OnlineMsgBean.MsgEntity> msgList) {
        List<OnlineMsgBean.MsgEntity> tempList;
        if (msgList != null && msgList.size() > 0) {
            tempList = new ArrayList<>();
            SortUtil.sortAscendingByTime(msgList);
            if (msgList.size() == 1) {//一条消息，接收到服务端下发别人的消息
                OnlineMsgBean.MsgEntity currMsg = msgList.get(0);
                OnlineMsgBean.MsgEntity preMsg = getLastMsgInList();
                if (preMsg != null) {
                    String formatTimeStr = DateTimeUtil.formatDiffTimeForIMMsg(preMsg.getSendtime() * 1000, currMsg.getSendtime() * 1000);
                    if (formatTimeStr != null) {
                        tempList.add(addALogMessage(formatTimeStr));
                    }
                }
                applyMsgFromWhere(currMsg);
                tempList.add(currMsg);
                int beforePosition = mDataList.size();
                mDataList.addAll(tempList);
                notifyItemRangeInserted(beforePosition, tempList.size());
            } else {
                for (int x = 0; x < msgList.size(); x++) {
                    OnlineMsgBean.MsgEntity currMsg = msgList.get(x);
                    applyMsgFromWhere(currMsg);
                    if (x >= 1) {
                        String formatTimeStr;
                        OnlineMsgBean.MsgEntity preMsg = msgList.get(x - 1);
                        formatTimeStr = DateTimeUtil.formatDiffTimeForIMMsg(preMsg.getSendtime() * 1000, currMsg.getSendtime() * 1000);
                        if (formatTimeStr != null) {
                            tempList.add(addALogMessage(formatTimeStr));
                        }
                    }
                    tempList.add(currMsg);
                }
                mDataList.addAll(tempList);
                notifyDataSetChanged();
            }
            LogUtil.e(TAG, "refreshData --> addAll:" + msgList.size());
        }
    }

    /** 加载分页的数据 */
    public int loadMoreData(List<OnlineMsgBean.MsgEntity> msgList) {
        List<OnlineMsgBean.MsgEntity> tempList;
        if (msgList != null && msgList.size() > 0) {
            tempList = new ArrayList<>();
            SortUtil.sortAscendingByTime(msgList);
            if (msgList.size() == 1) {
                OnlineMsgBean.MsgEntity currMsg = msgList.get(0);
                applyMsgFromWhere(currMsg);
                String formatTimeStr = DateTimeUtil.formatDateTime(currMsg.getSendtime() * 1000, DateTimeUtil.DF_MM_DD_HH_MM);
                tempList.add(addALogMessage(formatTimeStr));
                tempList.add(currMsg);
            } else {
                for (int x = 0; x < msgList.size(); x++) {
                    OnlineMsgBean.MsgEntity currMsg = msgList.get(x);
                    applyMsgFromWhere(currMsg);
                    if (x >= 1) {
                        String formatTimeStr;
                        OnlineMsgBean.MsgEntity preMsg = msgList.get(x - 1);
                        formatTimeStr = DateTimeUtil.formatDiffTimeForIMMsg(preMsg.getSendtime() * 1000, currMsg.getSendtime() * 1000);
                        if (formatTimeStr != null) {
                            tempList.add(addALogMessage(formatTimeStr));
                        }
                    }
                    tempList.add(currMsg);
                }
            }
            mDataList.addAll(0, tempList);
            LogUtil.e(TAG, "loadMoreData --> addAll:" + msgList.size());
            notifyItemRangeInserted(0, tempList.size());
            return tempList.size();
        }
        return 0;
    }

    private void applyMsgFromWhere(OnlineMsgBean.MsgEntity currMsg) {
        if (LOG_NAME.equals(currMsg.getUsername())) {
            currMsg.setFromme(TYPE_MSG_LOG);
        } else if (mSelfUserId.equals(currMsg.getUserid())) {
            currMsg.setFromme(TYPE_MSG_ME);
        } else {
            currMsg.setFromme(TYPE_MSG_OTHER);
        }
    }

    /** 在最尾追加一条消息 */
    public void appendOneMsgAtLast(OnlineMsgBean.MsgEntity msgEntity) {
        if (msgEntity == null) {
            return;
        }
        List<OnlineMsgBean.MsgEntity> tempList = new ArrayList<>();
        tempList.add(msgEntity);
        LogUtil.e(TAG, "appendOneMsgAtLast --> msg:" + msgEntity.getMessage());
        refreshData(tempList);
    }

    public void clearDataList() {
        mDataList.clear();
        notifyDataSetChanged();
    }

    /** 获取列表第一条消息 */
    public OnlineMsgBean.MsgEntity getFirstMsgInList() {
        if (mDataList.size() > 0) {
            return mDataList.get(0);
        }
        return null;
    }

    /** 获取列表最后一条消息 */
    public OnlineMsgBean.MsgEntity getLastMsgInList() {
        if (mDataList.size() > 0) {
            return mDataList.get(mDataList.size() - 1);
        }
        return null;
    }

    /** 添加log消息 */
    private OnlineMsgBean.MsgEntity addALogMessage(String msg) {
        OnlineMsgBean.MsgEntity entity = new OnlineMsgBean.MsgEntity();
        entity.setFromme(TYPE_MSG_LOG);
        entity.setMessage(msg);
        entity.setUsername(LOG_NAME);
        return entity;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout;
        switch (viewType) {
            case TYPE_MSG_OTHER:
                layout = R.layout.item_online_message_other;
                return new MessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(layout, parent, false));
            case TYPE_MSG_ME:
                layout = R.layout.item_online_message_me;
                return new MessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(layout, parent, false));
            case TYPE_MSG_LOG:
                layout = R.layout.item_online_message_log;
                return new LogViewHolder(LayoutInflater.from(parent.getContext()).inflate(layout, parent, false));
            default:
                throw new IllegalArgumentException("viewType illegal !");
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder viewHolder, int position) {
        OnlineMsgBean.MsgEntity entity = mDataList.get(position);
        if (entity.getFromme() == TYPE_MSG_LOG) {//log
            LogViewHolder logViewHolder = (LogViewHolder) viewHolder;
            logViewHolder.setMessage(entity.getMessage());
        } else {//message
            MessageViewHolder messageViewHolder = (MessageViewHolder) viewHolder;
            messageViewHolder.setUserAvatar(entity.getHeadimage());
            messageViewHolder.setUsername(entity.getUsername());
            messageViewHolder.setUserid(entity.getUserid());

            messageViewHolder.setMessageBg(entity.getFromme() == TYPE_MSG_ME ? R.drawable.chat_msg_bg_me : R.drawable.chat_msg_bg_other);
            List<OnlineMsgBean.MsgEntity.ToidEntity> toid = entity.getToid();
            if (toid != null) {
                for (OnlineMsgBean.MsgEntity.ToidEntity toidEntity : toid) {
                    if (mSelfUserId.equals(toidEntity.getUserid())) {
                        messageViewHolder.setMessageBg(R.drawable.chat_msg_bg_atme);
                        break;
                    }
                }
            }
            messageViewHolder.setMessage(entity.getMessage());
//            messageViewHolder.setMessage(entity);
        }
    }

    //让@的字符串变色
    private SpannableStringBuilder colorForAtOneStr(OnlineMsgBean.MsgEntity entity) {
        SpannableStringBuilder style = new SpannableStringBuilder(entity.getMessage());
        List<OnlineMsgBean.MsgEntity.ToidEntity> toid = entity.getToid();
        if (null != toid){
            for (OnlineMsgBean.MsgEntity.ToidEntity toidEntity : toid) {
                String[] msgArr = entity.getMessage().split("@");
                for (String msgBlock : msgArr) {
                    if (msgBlock.contains(toidEntity.getUsername())) {
                        String fcontent = "@" + toidEntity.getUsername();
                        int fstart = entity.getMessage().indexOf(fcontent);
                        int fend = fstart + fcontent.length();
                        style.setSpan(new ForegroundColorSpan(Color.RED), fstart, fend, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    }
                }
            }
        }
        if (TextUtils.isEmpty(style.toString())) {
            style.append(entity.getMessage());
        }
        return style;
    }

    //让@的字符串变色
//    private String colorForAtOneStr(OnlineMsgBean.MsgEntity entity) {
//        StringBuilder sb = new StringBuilder();
//        List<OnlineMsgBean.MsgEntity.ToidEntity> toid = entity.getToid();
//        for (OnlineMsgBean.MsgEntity.ToidEntity toidEntity : toid) {
//            String[] msgArr = entity.getMessage().split("@");
//            for (String msgBlock : msgArr) {
//                if (msgBlock.contains(toidEntity.getUsername())) {
//                    String excludeAtStrMsg = msgBlock.substring(toidEntity.getUsername().length());
//                    sb.append(Html.fromHtml("<font color='#5795E6'>@" + toidEntity.getUsername() + "</font>"));
//                    sb.append(excludeAtStrMsg);
//                }
//            }
//        }
//        return sb.toString();
//    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mDataList.get(position).getFromme();
    }

    /** Message 的ViewHolder */
    public class MessageViewHolder extends BaseViewHolder {
        private CircleImageView mUserAvatar;
        private TextView mUsername;
        private TextView mMessage;
        private String userid;

        public MessageViewHolder(View itemView) {
            super(itemView);
            mUserAvatar = (CircleImageView) itemView.findViewById(R.id.user_avatar);
            mUsername = (TextView) itemView.findViewById(R.id.user_name);
            mMessage = (TextView) itemView.findViewById(R.id.message);
        }

        public void setUserAvatar(String userAvatarPath) {
            if (null == mUserAvatar) return;
            ImgLoadUtil.loadAvatarByPath(userAvatarPath, mUserAvatar, R.mipmap.default_user_icon);
            mUserAvatar.setTag(R.id.user_avatar, this);
            mUserAvatar.setOnLongClickListener(mOnLongClickAvatarListener);
        }

        public void setUsername(String username) {
            if (null == mUsername) return;
            mUsername.setText(username == null ? "unknow" : username);
        }

        public String getUsername() {
            if (null == mUsername) return null;
            return mUsername.getText().toString();
        }

        public void setMessage(String message) {
            if (null == mMessage) return;
            mMessage.setText(message == null ? "unknow" : message);
            mMessage.setTag(R.id.message, this);
//            mMessage.setOnLongClickListener(mOnLongClickMessageListener); // TODO 埋点： 预留功能-长按消息可以进行复制消息
        }

        public void setMessage(OnlineMsgBean.MsgEntity entity) {
            if (null == entity) return;
            mMessage.setText(TextUtils.isEmpty(entity.getMessage()) ? "unknow" : colorForAtOneStr(entity));
            mMessage.setTag(R.id.message, this);
//            mMessage.setOnLongClickListener(mOnLongClickMessageListener); // TODO 埋点： 预留功能-长按消息可以进行复制消息
        }

        public String getMessage() {
            if (null == mMessage) return null;
            return mMessage.getText().toString();
        }

        public void setMessageBg(int resId) {
            if (null == mMessage) return;
            mMessage.setBackgroundResource(resId);
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }
    }

    /** 长按头像@ta */
    private View.OnLongClickListener mOnLongClickAvatarListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            MessageViewHolder messageViewHolder = (MessageViewHolder) v.getTag(R.id.user_avatar);
            if (messageViewHolder == null || messageViewHolder.getUserid() == null) {
                return false;
            }
            if (mSelfUserId.equals(messageViewHolder.getUserid())) {
                return true;
            }
            EventHelper.post(new OnlineAtOneEvent(messageViewHolder.getUsername(), messageViewHolder.getUserid()));
            return true;
        }
    };

    /** 长按消息 */
    @SuppressWarnings("unused")
    private View.OnLongClickListener mOnLongClickMessageListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            MessageViewHolder messageViewHolder = (MessageViewHolder) v.getTag(R.id.message);
            if (messageViewHolder == null
                    || messageViewHolder.getMessage() == null
                    || "timestamp".equals(messageViewHolder.getUsername())) {
                return false;
            }
            final String message = messageViewHolder.getMessage();
            String[] items = new String[]{"复制"};
            ListView lv = new ListView(v.getContext());
            lv.setAdapter(new ArrayAdapter<>(v.getContext(), R.layout.dialog_listview_item_text, Arrays.asList(items)));
            final CustomDialog dialog = new CustomDialog.AlertBuilder(mActivity)
                    .setTitleLayoutVisibility(View.GONE).setCustomContentView(lv).create();
            dialog.show();

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    dialog.dismiss();
                    switch (position) {
                        case 0://复制
                            UIUtil.showToastShort("已复制到剪切板");
                            ClipboardUtil.copyString(message);
                            break;
                    }
                }
            });
            return true;
        }
    };


    /** Message Log的ViewHolder */
    public class LogViewHolder extends BaseViewHolder {
        private TextView mMessage;

        public LogViewHolder(View itemView) {
            super(itemView);
            mMessage = (TextView) itemView.findViewById(R.id.message);
        }

        public void setMessage(String message) {
            if (null == mMessage) return;
            mMessage.setText(StringUtil.isEmptyOrNull(message) ? "unknow" : message);
        }
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {
        public BaseViewHolder(View itemView) {
            super(itemView);
        }
    }
}
