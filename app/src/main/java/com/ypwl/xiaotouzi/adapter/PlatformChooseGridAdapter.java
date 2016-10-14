package com.ypwl.xiaotouzi.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.bean.VideoInfoBean;

import java.util.List;

/**
 * function:视频选项的适配器
 * <p/>
 * Created by tengtao on 2015/12/16.
 */
public class PlatformChooseGridAdapter extends BaseAdapter {
    private Context mContext;
    private List<VideoInfoBean> videoInfos;

    public PlatformChooseGridAdapter(Context context){
        this.mContext = context;
    }

    public void loadData(List<VideoInfoBean> videoInfos){
        if(videoInfos.size()>0){
            this.videoInfos = videoInfos;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return videoInfos!=null?videoInfos.size():0;
    }

    @Override
    public Object getItem(int position) {
        return videoInfos!=null?videoInfos.get(position):null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.layout_platform_video_choose_item,null);
            holder.mTvChooseItem = (TextView) convertView.findViewById(R.id.tv_platform_video_choose_item);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mTvChooseItem.setText(videoInfos.get(position).getName());
        holder.mTvChooseItem.setBackgroundResource(videoInfos.get(position).isHasChoose()?
                R.drawable.shape_platform_video_choose_bg_selected:R.drawable.shape_platform_video_choose_bg_normal);
        holder.mTvChooseItem.setTextColor(mContext.getResources().getColor(videoInfos.get(position).isHasChoose()?
                R.color.b:R.color.d));
        return convertView;
    }

    private class ViewHolder{
        TextView mTvChooseItem;
    }
}
