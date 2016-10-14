package com.ypwl.xiaotouzi.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.bean.ContinualTallyListBean;
import com.ypwl.xiaotouzi.utils.DateTimeUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * 项目名:	XTZ
 * 包名:	com.ypwl.xiaotouzi.adapter
 * 类名:	ContinualTallyAdapter
 * 作者:	罗霄
 * 创建时间:	2016/4/11 13:31
 * <p/>
 * 描述:	流水资产页面的适配器
 * <p/>
 * svn版本:	$Revision: 14483 $
 * 更新人:	$Author: luoxiao $
 * 更新时间:	$Date: 2016-04-28 16:42:56 +0800 (周四, 28 四月 2016) $
 * 更新描述:	${TODO}
 */
public class ContinualTallyAdapter extends BaseAdapter {

    private final Context mContext;
    private LinkedList<ContinualTallyListBean> mData;
    private final String mStrPeriod = "第%s/%s期";

    public ContinualTallyAdapter(Context context) {
        this.mContext = context;
        mData = new LinkedList<>();
    }

    public void notifyDataSetChanged(List<ContinualTallyListBean> data, boolean isAdd) {
        if (!isAdd) {
            mData.clear();
        }
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public ContinualTallyListBean getPositionBean(int position){
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

            convertView = View.inflate(mContext, R.layout.item_continual_tally, null);

            holder.mTvDay = (TextView) convertView.findViewById(R.id.item_continual_tally_date_day);
            holder.mTvYear = (TextView) convertView.findViewById(R.id.item_continual_tally_date_year);
            holder.mTvPlatformName = (TextView) convertView.findViewById(R.id.item_continual_tally_platform_name);
            holder.mTvContinualType = (TextView) convertView.findViewById(R.id.item_continual_tally_continual_type);
            holder.mTvContinualContent = (TextView) convertView.findViewById(R.id.item_continual_tally_continual_content);
            holder.mTvContinualMoney = (TextView) convertView.findViewById(R.id.item_continual_tally_continual_money);
            holder.mTvContinualPeriods = (TextView) convertView.findViewById(R.id.item_continual_tally_continual_periods);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ContinualTallyListBean continualTallyListBean = mData.get(position);

        if (!TextUtils.isEmpty(continualTallyListBean.getTime())) {
            String[] mTimeSplit = parseTime(continualTallyListBean.getTime()).split("/");
            holder.mTvDay.setText(mTimeSplit[1] + "/" + mTimeSplit[2]);
            holder.mTvYear.setText(mTimeSplit[0]);
        } else {
            holder.mTvDay.setText("");
            holder.mTvYear.setText("");
        }

        holder.mTvPlatformName.setText(TextUtils.isEmpty(continualTallyListBean.getP_name()) ? "" : continualTallyListBean.getP_name());

        holder.mTvContinualContent.setText(TextUtils.isEmpty(continualTallyListBean.getProject_name()) ? "" : continualTallyListBean.getProject_name());

        holder.mTvContinualType.setText(TextUtils.isEmpty(continualTallyListBean.getType()) ? "" : parseType(continualTallyListBean.getType(), holder.mTvContinualMoney));

        holder.mTvContinualMoney.setText(TextUtils.isEmpty(continualTallyListBean.getMoney()) ? "" : continualTallyListBean.getMoney());

        if (!TextUtils.isEmpty(continualTallyListBean.getPeriod()) && !TextUtils.isEmpty(continualTallyListBean.getPeriod_total())) {
            holder.mTvContinualPeriods.setText(String.format(mStrPeriod, continualTallyListBean.getPeriod(), continualTallyListBean.getPeriod_total()));
        } else {
            holder.mTvContinualPeriods.setText("");
        }

        return convertView;
    }

    class ViewHolder {
        TextView mTvDay;
        TextView mTvYear;
        TextView mTvPlatformName;
        TextView mTvContinualType;
        TextView mTvContinualContent;
        TextView mTvContinualMoney;
        TextView mTvContinualPeriods;
    }

    private String parseTime(String mStrTime) {
        return DateTimeUtil.formatDateTime(Long.parseLong(mStrTime + "000"), DateTimeUtil.DF_YYYY_MM_DD).replace("-", "/");
    }

    private String parseType(String mTypeInt, TextView view) {
        String result = "";

        switch (mTypeInt) {
            case "1":
                result = "投资";
                view.setTextColor(UIUtil.getColor(R.color.continual_tally_tag_green));
                break;
            case "2":
                result = "回款";
                view.setTextColor(UIUtil.getColor(R.color.continual_tally_tag_red));
                break;
            case "3":
                result = "回款结束";
//                view.setTextColor(UIUtil.getColor(R.color.continual_tally_tag_gray));
                view.setTextColor(UIUtil.getColor(R.color.continual_tally_tag_red));
                break;
        }

        return result;
    }
}
