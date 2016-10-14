package com.ypwl.xiaotouzi.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.bean.ContinualTallyForFilterSelectPlatformBean;
import com.ypwl.xiaotouzi.utils.UIUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * 项目名:	XTZ
 * 包名:	com.ypwl.xiaotouzi.adapter
 * 类名:	ContinualTallySelectPlatformAdapter
 * 作者:	罗霄
 * 创建时间:	2016/4/11 18:02
 * <p/>
 * 描述:	流水资产 ==> 筛选 ==> 选择平台页面的适配器
 * <p/>
 * svn版本:	$Revision: 13887 $
 * 更新人:	$Author: luoxiao $
 * 更新时间:	$Date: 2016-04-13 13:06:45 +0800 (周三, 13 四月 2016) $
 * 更新描述:	${TODO}
 */
public class ContinualTallySelectPlatformAdapter extends BaseAdapter {

    private String mPidSelect = "0";

    private final Context mContext;
    private LinkedList<ContinualTallyForFilterSelectPlatformBean> mData;

    public ContinualTallySelectPlatformAdapter(Context context) {
        this.mContext = context;
        mData = new LinkedList<>();
    }

    public void notifyDataSetChanged(List<ContinualTallyForFilterSelectPlatformBean> data, String defPidSelected) {
        if (null != data && data.size() != 0) {
            mData.clear();

            ContinualTallyForFilterSelectPlatformBean continualTallyForFilterSelectPlatformBean = new ContinualTallyForFilterSelectPlatformBean();
            continualTallyForFilterSelectPlatformBean.setPid("0");
            continualTallyForFilterSelectPlatformBean.setP_name(UIUtil.getString(R.string.continual_tally_for_filter_all_platform));
            data.add(0, continualTallyForFilterSelectPlatformBean);

            mData.addAll(data);
        }
        notifyDataSetChanged(defPidSelected);
    }

    public void notifyDataSetChanged(String defPidSelected) {
        this.mPidSelect = defPidSelected;
        notifyDataSetChanged();
    }

    public int getPositionForSelected(String mStrLetter) {
        int result = -1;
        if (!TextUtils.isEmpty(mStrLetter)){
            for (int i = 0; i < mData.size(); i++){
                if (mStrLetter.equalsIgnoreCase(mData.get(i).getInitial())){
                    result = i;
                    break;
                }
            }
        }
        return result;
    }

    public ContinualTallyForFilterSelectPlatformBean getBeanForPosition(int position){
        return mData.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();

            convertView = View.inflate(mContext, R.layout.item_continual_tally_for_filter_select_platform, null);

            holder.mTvLetter = (TextView) convertView.findViewById(R.id.item_continual_tally_for_filter_select_platform_tv_letter);
            holder.mTvName = (TextView) convertView.findViewById(R.id.item_continual_tally_for_filter_select_platform_tv_name);
            holder.mIvSelected = (ImageView) convertView.findViewById(R.id.item_continual_tally_for_filter_select_platform_iv_selected);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ContinualTallyForFilterSelectPlatformBean continualTallyForFilterSelectPlatformBean = mData.get(position);

        holder.mIvSelected.setVisibility(mPidSelect.equalsIgnoreCase(continualTallyForFilterSelectPlatformBean.getPid()) ? View.VISIBLE : View.INVISIBLE);

        String p_name = continualTallyForFilterSelectPlatformBean.getP_name();
        holder.mTvName.setText(TextUtils.isEmpty(p_name) ? "" : p_name);

        holder.mTvLetter.setText(TextUtils.isEmpty(continualTallyForFilterSelectPlatformBean.getInitial()) ? "" : continualTallyForFilterSelectPlatformBean.getInitial());
        if (0 != position) {
            holder.mTvLetter.setVisibility(continualTallyForFilterSelectPlatformBean.getInitial().equalsIgnoreCase(mData.get(position - 1).getInitial()) ? View.GONE : View.VISIBLE);
        }

        return convertView;
    }

    class ViewHolder {
        TextView mTvLetter;
        TextView mTvName;
        ImageView mIvSelected;
    }
}
