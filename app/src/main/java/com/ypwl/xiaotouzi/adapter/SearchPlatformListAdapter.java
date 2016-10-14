package com.ypwl.xiaotouzi.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.bean.SearchPlatformResultBean;
import com.ypwl.xiaotouzi.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * function : 主页网贷平台搜索数据列表数据适配器
 * <p/>
 * Created by lzj on 2015/11/10.
 */
public class SearchPlatformListAdapter extends BaseAdapter {

    private Context mContext;
    private List<SearchPlatformResultBean.DataEntity> mDataList = new ArrayList<>();

    public SearchPlatformListAdapter(Context context) {
        this.mContext = context;
    }

    /** 刷新视图数据 */
    public void loadData(List<SearchPlatformResultBean.DataEntity> dataList) {
        if (dataList == null) {
            return;
        }
        mDataList.clear();
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDataList.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.adapter_search_platform_item, null);
        }
        TextView name = ViewHolder.findViewById(convertView, R.id.search_platform_item_tv);
        SearchPlatformResultBean.DataEntity bean = mDataList.get(position);
        name.setText(bean.getP_name());
        name.setTextColor(Color.parseColor(bean.getDate()!=null && bean.getDate().length() > 5?"#7c7c7c":"#333333"));
        return convertView;
    }
}
