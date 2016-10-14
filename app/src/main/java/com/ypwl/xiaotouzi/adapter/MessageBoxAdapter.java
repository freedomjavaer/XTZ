package com.ypwl.xiaotouzi.adapter;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.bean.MessageBoxBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.interf.IRequestCallback;
import com.ypwl.xiaotouzi.interf.UIAdapterListener;
import com.ypwl.xiaotouzi.manager.db.MessageBoxDbOpenHelper;
import com.ypwl.xiaotouzi.manager.net.NetHelper;
import com.ypwl.xiaotouzi.utils.DateTimeUtil;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.StringUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 收件箱的适配器
 *
 * Created by PDK on 2016/3/18.
 * <p/>
 * Modify by lzj on 2016/4/5.
 */
public class MessageBoxAdapter extends BaseAdapter {

    private List<MessageBoxBean.ListEntity> mDataList = new ArrayList<>();
    private UIAdapterListener mUiListener;
    private int mCurrentPage = 1;

    public MessageBoxAdapter(UIAdapterListener uiAdapterListener) {
        mUiListener = uiAdapterListener;
    }

    /** 异步获取数据 */
    public void asyncRequestData() {
        String url = StringUtil.format(URLConstant.MESSAGE_BOX, GlobalUtils.token, mCurrentPage);
        NetHelper.get(url, new IRequestCallback<MessageBoxBean>() {

            @Override
            public void onStart() {
            }

            @Override
            public void onFailure(Exception e) {
                mUiListener.dataCountChanged(Const.LOADED_ERROR);
            }

            @Override
            public void onSuccess(MessageBoxBean messageBoxBean) {
                if (mCurrentPage == 1) {//刷新或是第一次加载
                    mDataList.clear();
                    if (messageBoxBean != null && messageBoxBean.getList() != null && messageBoxBean.getList().size() > 0) {
                        addData(messageBoxBean.getList());
                    }
                    mUiListener.loadFinished(mDataList.size());
                    notifyDataSetChanged();
                } else {//加载更多
                    if (messageBoxBean != null && messageBoxBean.getList() != null && messageBoxBean.getList().size() > 0) {
                        addData(messageBoxBean.getList());
                        notifyDataSetChanged();
                        mUiListener.loadFinished(mDataList.size());
                    } else {
                        mUiListener.loadFinished(mDataList.size());
                        mUiListener.dataCountChanged(Const.LOADED_NO_MORE);
                    }
                }
            }

        });
    }

    /** 过滤数据 */
    private void addData(List<MessageBoxBean.ListEntity> listEntity) {
        // 系统消息已读的 不显示未读给用户
        for (MessageBoxBean.ListEntity entity : listEntity) {
            if (MessageBoxDbOpenHelper.MessageBoxDbHelper.getInstance().queryByStandId(entity.getStand_id())) {
                entity.setStatus(1);
            }
        }
        mDataList.addAll(listEntity);
    }

    /** 加载更多 */
    public void loadMore() {
        ++mCurrentPage;
        asyncRequestData();
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public MessageBoxBean.ListEntity getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = UIUtil.inflate(R.layout.item_task_message_box);
        }
        ImageView icon = ViewHolder.findViewById(convertView, R.id.iv_message_box);
        TextView message = ViewHolder.findViewById(convertView, R.id.tv_message);
        TextView date = ViewHolder.findViewById(convertView, R.id.tv_message_date);
        TextView title = ViewHolder.findViewById(convertView, R.id.tv_message_title);

        MessageBoxBean.ListEntity item = getItem(position);

        message.setText(item.getContent());
        title.setText(item.getName());
        date.setText(DateTimeUtil.formatMsgBoxDate(item.getAddtime() * 1000));
        if (1 == item.getStatus()) {//已读
            icon.setImageResource(R.mipmap.message_box_read);
            message.setTextColor(Color.parseColor("#838383"));
        } else {//未读
            icon.setImageResource(R.mipmap.message_box_unread);
            message.setTextColor(Color.parseColor("#000000"));
        }
        return convertView;
    }

}
