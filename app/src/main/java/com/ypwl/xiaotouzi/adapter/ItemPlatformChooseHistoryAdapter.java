package com.ypwl.xiaotouzi.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.bean.PlatformChooseAdapterBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.event.PlatformChooseEvent;
import com.ypwl.xiaotouzi.event.PlatformCompareRefreshEvent;
import com.ypwl.xiaotouzi.manager.EventHelper;
import com.ypwl.xiaotouzi.manager.PlatformCompareManager;
import com.ypwl.xiaotouzi.utils.UIUtil;

import java.util.List;

/**
 * function:平台选择-->历史记录的适配器
 * <p/>
 * Created by tengtao on 2015/11/28.
 */
public class ItemPlatformChooseHistoryAdapter extends BaseAdapter {
    private List<PlatformChooseAdapterBean> list;
    private Context context;

    public void updataListView(Context context,List<PlatformChooseAdapterBean> list){
        this.list = list;
        this.context = context;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list!=null?list.size():0;
    }

    @Override
    public Object getItem(int position) {
        return list!=null?list.get(position):null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            holder = new ViewHolder();
            convertView = View.inflate(context,R.layout.item_platform_choose_follow,null);
            holder.mTvName = (TextView) convertView.findViewById(R.id.tv_platform_choose_follow_name);
            holder.mItemView = convertView.findViewById(R.id.item_platform_choose_follow_layout);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        String p_name = list.get(position).getP_name();
        holder.mTvName.setText(p_name);
        holder.mItemView.setTag(R.id.tv_platform_choose_follow_name,p_name);
        holder.mItemView.setTag(R.id.item_platform_choose_follow_layout,list.get(position).getPid());
        holder.mItemView.setOnClickListener(mOnClickListener);
        return convertView;
    }

    private class ViewHolder{
        TextView mTvName;
        View mItemView;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String p_name = (String) v.getTag(R.id.tv_platform_choose_follow_name);
            String pid = (String) v.getTag(R.id.item_platform_choose_follow_layout);
            if (Const.PLATFORM_CHOOSE_REQUEST_FROM == 1) {//对比平台
                if ("".equals(pid)) {
                    return;
                }
                if (PlatformCompareManager.getList().contains(pid)) {
                    UIUtil.showToastShort(p_name + "已存在");
                } else {
                    if (PlatformCompareManager.getList().size() > 5) {
                        UIUtil.showToastShort("最多选择5个对比平台");
                        return;
                    }
                    PlatformCompareManager.getList().add(pid);
                    EventHelper.post(new PlatformCompareRefreshEvent(true, 1));//更新全部数据
                    EventHelper.post(new PlatformChooseEvent(p_name, pid, false, false));
                }
            } else if (Const.PLATFORM_CHOOSE_REQUEST_FROM == 0) {//记账
                EventHelper.post(new PlatformChooseEvent(p_name, pid, false, true));
            }
        }
    };
}
