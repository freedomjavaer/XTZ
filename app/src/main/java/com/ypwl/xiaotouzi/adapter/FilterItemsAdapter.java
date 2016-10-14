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
 * function : 筛选条件列表数据适配器
 * <p/>
 * Created by lzj on 2015/11/11.
 */
public class FilterItemsAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mDataList = new ArrayList<>();

    public FilterItemsAdapter(Context context) {
        this.mContext = context;
    }

    /**
     * 刷新视图数据
     */
    public void refresh(List<String> dataList) {
        if (dataList == null) {
            return;
        }
        mDataList.clear();
        mDataList.addAll(dataList);
        notifyDataSetChanged();

    }


    public List<String> getDataList() {
        return mDataList;
    }

    /**
     * 移除条目
     */
    public void removeItem(int position) {
        if (position < mDataList.size()) {
            mDataList.remove(position);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mDataList.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_rv_flatform_choice_show_on_top, null);
        }
        TextView filterItemName = ViewHolder.findViewById(convertView, R.id.platform_item_choice_btn_tv);
        filterItemName.setText(position < mDataList.size() ? mDataList.get(position) : "");
        filterItemName.setBackgroundResource(position < mDataList.size() ? R.mipmap.x : R.mipmap.add_filter);
        return convertView;
    }
}
