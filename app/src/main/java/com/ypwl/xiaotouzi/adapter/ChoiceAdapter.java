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
import com.ypwl.xiaotouzi.utils.ImgLoadUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * 项目名:	XTZ
 * 包名:	com.ypwl.xiaotouzi.adapter
 * 类名:	ChoiceAdapter
 * 作者:	罗霄
 * 创建时间:	2016/4/18 14:35
 * <p/>
 * 描述:	金融超市--精选页面适配器
 * <p/>
 * svn版本:	$Revision: 15362 $
 * 更新人:	$Author: pengdakai $
 * 更新时间:	$Date: 2016-06-07 15:50:28 +0800 (周二, 07 六月 2016) $
 * 更新描述:	$Message$
 */
public class ChoiceAdapter extends BaseAdapter {

    private final Context mContext;
    private LinkedList<ChoiceBean> mData;

    public ChoiceAdapter(Context context) {
        this.mContext = context;
        mData = new LinkedList<>();
    }

    public void notifyDataSetChanged(List<ChoiceBean> data, boolean isAdd) {
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

            convertView = View.inflate(mContext, R.layout.item_finance_supermarket_of_choice, null);

//            holder.mTvName = (TextView) convertView.findViewById(R.id.item_finance_supermarket_of_choice_tv_name);
            holder.mIvLogo = (ImageView) convertView.findViewById(R.id.item_finance_supermarket_of_choice_iv_logo);
            holder.mTvInfo = (TextView) convertView.findViewById(R.id.item_finance_supermarket_of_choice_tv_info);
            holder.mTvYield = (TextView) convertView.findViewById(R.id.item_finance_supermarket_of_choice_tv_yield);
            holder.mRlJiaxi = (RelativeLayout) convertView.findViewById(R.id.item_finance_supermarket_of_choice_rl_jiaxi);
            holder.mTvJiaxi = (TextView) convertView.findViewById(R.id.item_finance_supermarket_of_choice_tv_jiaxi);
            holder.mTvJiaxiUnit = (TextView) convertView.findViewById(R.id.item_finance_supermarket_of_choice_tv_jiaxi_unit);
            holder.mIvJiaxiType = (ImageView) convertView.findViewById(R.id.item_finance_supermarket_of_choice_iv_jiaxi_type);
            holder.mTvPeriod = (TextView) convertView.findViewById(R.id.item_finance_supermarket_of_choice_tv_period);
            holder.mTvPeriodUnit = (TextView) convertView.findViewById(R.id.item_finance_supermarket_of_choice_tv_period_unit);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ChoiceBean choiceBean = mData.get(position);

        holder.mTvJiaxi.setText(TextUtils.isEmpty(choiceBean.getAdd_interest()) ? "0" : choiceBean.getAdd_interest());

//        switchForJiaXi(holder.mRlJiaxi, holder.mTvInfo, choiceBean.getAt_type(), holder.mTvJiaxi, holder.mTvJiaxiUnit, holder.mTvJiaxiType);
        switchForJiaXi(holder.mRlJiaxi, holder.mTvInfo, choiceBean.getAt_type(), holder.mTvJiaxi, holder.mTvJiaxiUnit, holder.mIvJiaxiType);

//        holder.mTvName.setText(TextUtils.isEmpty(choiceBean.getP_name()) ? "" : choiceBean.getP_name());
        holder.mTvInfo.setText(TextUtils.isEmpty(choiceBean.getProject_name()) ? "" : choiceBean.getProject_name());
        holder.mTvYield.setText(TextUtils.isEmpty(choiceBean.getRate()) ? "0.00" : choiceBean.getRate());
        holder.mTvPeriod.setText(TextUtils.isEmpty(choiceBean.getTime_limit()) ? "0" : choiceBean.getTime_limit());
        holder.mTvPeriodUnit.setText(getPeriodUnit(choiceBean.getTime_type()));

        ImgLoadUtil.loadLogo(choiceBean.getPid(),choiceBean.getP_logo(), holder.mIvLogo, R.mipmap.custom_platform_logo);

        return convertView;
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

//    private void switchForJiaXi(RelativeLayout container, TextView drawableRightView, String type, TextView jiaXiView, TextView jiaXiUnitView, TextView jiaXiTyptView) {
////        0：没有加息
////        1：首投加息
////        2：加息
//        container.setVisibility(View.VISIBLE);
//        switch (type) {
//            case "0":
//                container.setVisibility(View.INVISIBLE);
//                setDrawable(drawableRightView, null, 30, 17);
//                break;
//            case "1":
//                jiaXiTyptView.setText(UIUtil.getString(R.string.finance_supermarket_target_detail_jiaxi_type_first));
//                jiaXiTyptView.setBackgroundDrawable(UIUtil.getDrawable(R.drawable.shape_finance_choice_info_bg_blue));
//                jiaXiTyptView.setTextColor(UIUtil.getColor(R.color.finance_supermarket_title_select));
//                jiaXiView.setTextColor(UIUtil.getColor(R.color.finance_supermarket_title_select));
//                jiaXiUnitView.setTextColor(UIUtil.getColor(R.color.finance_supermarket_title_select));
//                setDrawable(drawableRightView, UIUtil.getDrawable(R.mipmap.pic_first_xi), 41, 17);
//                break;
//            case "2":
//                jiaXiTyptView.setText(UIUtil.getString(R.string.finance_supermarket_target_detail_jiaxi_type_not_first));
//                jiaXiTyptView.setBackgroundDrawable(UIUtil.getDrawable(R.drawable.shape_finance_choice_info_bg_red));
//                jiaXiTyptView.setTextColor(UIUtil.getColor(R.color.finance_supermarket_choice_text_red));
//                jiaXiView.setTextColor(UIUtil.getColor(R.color.finance_supermarket_choice_text_red));
//                jiaXiUnitView.setTextColor(UIUtil.getColor(R.color.finance_supermarket_choice_text_red));
//                setDrawable(drawableRightView, UIUtil.getDrawable(R.mipmap.pic_add_xi), 30, 17);
//                break;
//        }
//    }

    private void switchForJiaXi(RelativeLayout container, TextView drawableRightView, String type,  TextView jiaXiView, TextView jiaXiUnitView, ImageView jiaXiTyptView) {
//        0：没有加息
//        1：首投加息
//        2：加息
        container.setVisibility(View.VISIBLE);
        switch (type) {
            case "0":
                container.setVisibility(View.INVISIBLE);
                setDrawable(drawableRightView, null, 30, 17);
                break;
            case "1":
                jiaXiView.setTextColor(UIUtil.getColor(R.color.finance_supermarket_choice_text_red));
                jiaXiUnitView.setTextColor(UIUtil.getColor(R.color.finance_supermarket_choice_text_red));
                jiaXiTyptView.setImageDrawable(UIUtil.getDrawable(R.mipmap.pic_first_xi_type));
                setDrawable(drawableRightView, UIUtil.getDrawable(R.mipmap.pic_first_xi), 41, 17);
                break;
            case "2":
                jiaXiView.setTextColor(UIUtil.getColor(R.color.finance_supermarket_choice_text_red));
                jiaXiUnitView.setTextColor(UIUtil.getColor(R.color.finance_supermarket_choice_text_red));
                jiaXiTyptView.setImageDrawable(UIUtil.getDrawable(R.mipmap.pic_add_xi_type));
                setDrawable(drawableRightView, UIUtil.getDrawable(R.mipmap.pic_add_xi), 41, 17);
                break;
        }
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
        //        TextView mTvName;
        ImageView mIvLogo;
        TextView mTvInfo;
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
