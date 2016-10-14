package com.ypwl.xiaotouzi.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.bean.ChoiceBean;
import com.ypwl.xiaotouzi.bean.PlatformTargetsBean;
import com.ypwl.xiaotouzi.utils.ImgLoadUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * 项目名:	XTZ
 * 包名:	com.ypwl.xiaotouzi.adapter
 * 类名:	PlatformTargetsAdapter
 * 作者:	罗霄
 * 创建时间:	2016/4/19 17:54
 * <p/>
 * 描述:	金融超市 -- 平台标的列表适配器
 * <p/>
 * svn版本:	$Revision: 15184 $
 * 更新人:	$Author: luoxiao $
 * 更新时间:	$Date: 2016-05-27 16:14:28 +0800 (周五, 27 五月 2016) $
 * 更新描述:	$Message$
 */
public class PlatformTargetsAdapter extends BaseAdapter {

    private final Context mContext;
    private LinkedList<PlatformTargetsBean.ListEntity> mData;

    public PlatformTargetsAdapter(Context context) {
        this.mContext = context;
        mData = new LinkedList<>();
    }

    public void notifyDataSetChanged(List<PlatformTargetsBean.ListEntity> data, boolean isAdd) {

        if (!isAdd) {
            mData.clear();
        }
        mData.addAll(data);
        notifyDataSetChanged();
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

            convertView = View.inflate(mContext, R.layout.item_finance_supermarket_of_platform_target, null);

            holder.mTvName = (TextView) convertView.findViewById(R.id.item_finance_supermarket_of_platform_target_tv_name);
//            holder.mTvInfo = (TextView) convertView.findViewById(R.id.item_finance_supermarket_of_platform_target_tv_info);
            holder.mTvYield = (TextView) convertView.findViewById(R.id.item_finance_supermarket_of_platform_target_tv_yield);
            holder.mRlJiaxi = (RelativeLayout) convertView.findViewById(R.id.item_finance_supermarket_of_platform_target_rl_jiaxi);
            holder.mTvJiaxi = (TextView) convertView.findViewById(R.id.item_finance_supermarket_of_platform_target_tv_jiaxi);
            holder.mTvJiaxiUnit = (TextView) convertView.findViewById(R.id.item_finance_supermarket_of_platform_target_tv_jiaxi_unit);
//            holder.mTvJiaxiType = (TextView) convertView.findViewById(R.id.item_finance_supermarket_of_platform_target_tv_jiaxi_type);
            holder.mIvJiaxiType = (ImageView) convertView.findViewById(R.id.item_finance_supermarket_of_platform_target_iv_jiaxitype);
            holder.mTvPeriod = (TextView) convertView.findViewById(R.id.item_finance_supermarket_of_platform_target_tv_period);
            holder.mTvPeriodUnit = (TextView) convertView.findViewById(R.id.item_finance_supermarket_of_platform_target_tv_period_unit);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final PlatformTargetsBean.ListEntity listEntity = mData.get(position);

        holder.mTvJiaxi.setText(TextUtils.isEmpty(listEntity.getAdd_interest()) ? "0" : listEntity.getAdd_interest());

        switchForJiaXi(holder.mRlJiaxi, listEntity.getAt_type(), holder.mTvJiaxi, holder.mTvJiaxiUnit, holder.mIvJiaxiType);
//        switchForJiaXi(holder.mRlJiaxi, "1", holder.mIvJiaxiType);

        holder.mTvName.setText(TextUtils.isEmpty(listEntity.getProject_name()) ? "" : listEntity.getProject_name());
//        holder.mTvInfo.setText(TextUtils.isEmpty(listEntity.getProject_name()) ? "" : listEntity.getProject_name());
        holder.mTvYield.setText(TextUtils.isEmpty(listEntity.getRate()) ? "0.00" : listEntity.getRate());
        holder.mTvPeriod.setText(TextUtils.isEmpty(listEntity.getTime_limit()) ? "0" : listEntity.getTime_limit());
        holder.mTvPeriodUnit.setText(getPeriodUnit(listEntity.getTime_type()));

        return convertView;
    }

    private void switchForJiaXi(RelativeLayout container, String type, TextView jiaXiView, TextView jiaXiUnitView, ImageView jiaXiTyptView){
//        0：没有加息
//        1：首投加息
//        2：加息
        container.setVisibility(View.VISIBLE);
        switch (type){
            case "0":
                container.setVisibility(View.INVISIBLE);
                break;
            case "1":
                jiaXiTyptView.setImageDrawable(UIUtil.getDrawable(R.mipmap.first_accrual));
                jiaXiView.setTextColor(UIUtil.getColor(R.color.style_current_title));
                jiaXiUnitView.setTextColor(UIUtil.getColor(R.color.style_current_title));
                break;
            case "2":
                jiaXiTyptView.setImageDrawable(UIUtil.getDrawable(R.mipmap.xtz_accrual));
                jiaXiView.setTextColor(UIUtil.getColor(R.color.finance_supermarket_choice_text_red));
                jiaXiUnitView.setTextColor(UIUtil.getColor(R.color.finance_supermarket_choice_text_red));
                break;
        }
    }

    private void switchForJiaXi(RelativeLayout container, TextView drawableRightView, String type, TextView jiaXiView, TextView jiaXiUnitView, TextView jiaXiTyptView){
//        0：没有加息
//        1：首投加息
//        2：加息
        container.setVisibility(View.VISIBLE);
        switch (type){
            case "0":
                container.setVisibility(View.INVISIBLE);
                setDrawable(drawableRightView, null, 32, 19);
                break;
            case "1":

                jiaXiTyptView.setText(UIUtil.getString(R.string.finance_supermarket_target_detail_jiaxi_type_first));
                jiaXiTyptView.setBackgroundDrawable(UIUtil.getDrawable(R.drawable.shape_finance_choice_info_bg_blue));
                jiaXiTyptView.setTextColor(UIUtil.getColor(R.color.style_current_title));
                jiaXiView.setTextColor(UIUtil.getColor(R.color.style_current_title));
                jiaXiUnitView.setTextColor(UIUtil.getColor(R.color.style_current_title));
                setDrawable(drawableRightView, UIUtil.getDrawable(R.mipmap.pic_first_xi), 41, 17);
                break;
            case "2":
                jiaXiTyptView.setText(UIUtil.getString(R.string.finance_supermarket_target_detail_jiaxi_type_not_first));
                jiaXiTyptView.setBackgroundDrawable(UIUtil.getDrawable(R.drawable.shape_finance_choice_info_bg_red));
                jiaXiTyptView.setTextColor(UIUtil.getColor(R.color.finance_supermarket_choice_text_red));
                jiaXiView.setTextColor(UIUtil.getColor(R.color.finance_supermarket_choice_text_red));
                jiaXiUnitView.setTextColor(UIUtil.getColor(R.color.finance_supermarket_choice_text_red));
                setDrawable(drawableRightView, UIUtil.getDrawable(R.mipmap.pic_add_xi), 41, 17);
                break;
        }
    }

    /**
     * 设置drawable上图片
     *
     * @param tv TextView
     */
    public void setDrawable(TextView tv, Drawable drawable, int widthDip, int heightDip) {
        if (null != drawable) {
            drawable.setBounds(0, 0, UIUtil.dip2px(widthDip), UIUtil.dip2px(heightDip));
        }
        tv.setCompoundDrawables(null, null, drawable, null);
    }

    private String getPeriodUnit(String str) {
        switch (str) {
            case "2":
                str = "天";
                break;
            default:
                str = "个月";
                break;
        }
        return str;
    }

    class ViewHolder {
        TextView mTvName;
//        TextView mTvInfo;
        TextView mTvYield;
        RelativeLayout mRlJiaxi;
        TextView mTvJiaxi;
        TextView mTvJiaxiUnit;
//        TextView mTvJiaxiType;
        ImageView mIvJiaxiType;
        TextView mTvPeriod;
        TextView mTvPeriodUnit;
    }
}
