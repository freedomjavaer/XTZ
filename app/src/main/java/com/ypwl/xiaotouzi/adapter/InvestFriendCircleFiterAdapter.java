package com.ypwl.xiaotouzi.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * function: 同城在线标题栏筛选适配器
 *
 * <p>Created by lzj on 2016/3/18.</p>
 */
public class InvestFriendCircleFiterAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mDataList = new ArrayList<>();

    public InvestFriendCircleFiterAdapter(Context context, List<String> dataList) {
        mContext = context;
        mDataList.clear();
        mDataList.addAll(dataList);
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public String getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_samecity_online_filter, null);
        }

        TextView itemName = ViewHolder.findViewById(convertView, R.id.filter_text);
        itemName.setText(getItem(position));

        if (position == getCount() - 1){
            ViewHolder.findViewById(convertView, R.id.filter_line).setVisibility(View.INVISIBLE);
        }else {
            ViewHolder.findViewById(convertView, R.id.filter_line).setVisibility(View.VISIBLE);
        }

        return convertView;
    }
}
