package com.ypwl.xiaotouzi.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.bean.XtzNetCreditNewsBean;
import com.ypwl.xiaotouzi.common.DeviceInfo;
import com.ypwl.xiaotouzi.utils.ImgLoadUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.ViewHolder;
import com.ypwl.xiaotouzi.view.fancycoverflow.FancyCoverFlow;
import com.ypwl.xiaotouzi.view.fancycoverflow.FancyCoverFlowAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * function : 晓投资页面滚动标的适配器
 * <p>
 * Created by tengtao on 2016/4/14.
 */
public class XtzFancyCoverAdapter extends FancyCoverFlowAdapter {

    private Context mContext;
    private FancyCoverFlow.LayoutParams mParams;
    private LinearLayout.LayoutParams mLlParams;
    private List<XtzNetCreditNewsBean.BlistBean> mList = new ArrayList<>();

    public XtzFancyCoverAdapter(Context context){
        this.mContext = context;
        mParams = new FancyCoverFlow.LayoutParams(FancyCoverFlow.LayoutParams.WRAP_CONTENT, FancyCoverFlow.LayoutParams.MATCH_PARENT);
        mLlParams = new LinearLayout.LayoutParams((int)(DeviceInfo.ScreenWidthPixels*0.85), UIUtil.dip2px(124));
    }

    public void loadData(List<XtzNetCreditNewsBean.BlistBean> list){
        if(list!=null) {
            mList.clear();
            mList.addAll(list);
            notifyDataSetChanged();
        }
    }

    public List<XtzNetCreditNewsBean.BlistBean> getDataList(){
        return mList;
    }

    @Override
    public View getCoverFlowItem(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = View.inflate(mContext, R.layout.item_xtz_fancy_cover_flow_layout,null);
        }
        //TextView mTvP_name = ViewHolder.findViewById(convertView,R.id.tv_fancy_cover_item_p_name);
        TextView mTvBidName = ViewHolder.findViewById(convertView,R.id.tv_fancy_cover_item_bid_name);
        TextView mTvRate = ViewHolder.findViewById(convertView,R.id.tv_fancy_cover_item_rate);
        TextView mTvRateExtra = ViewHolder.findViewById(convertView,R.id.tv_fancy_cover_item_rate_extra);
        //TextView mTvRateExtraInfo = ViewHolder.findViewById(convertView,R.id.tv_fancy_cover_item_rate_extra_info);
        TextView mTvDate = ViewHolder.findViewById(convertView,R.id.tv_fancy_cover_item_time_limit);
        TextView mTvDateType = ViewHolder.findViewById(convertView,R.id.tv_fancy_cover_item_time_type);
        TextView mTvAdd = ViewHolder.findViewById(convertView,R.id.tv_add);
        TextView mExtraPercent = ViewHolder.findViewById(convertView,R.id.tv_fancy_cover_item_rate_extra_percent);
        LinearLayout mLayoutExtra = ViewHolder.findViewById(convertView,R.id.layout_extra_rate);
        ImageView mBidBg = ViewHolder.findViewById(convertView,R.id.iv_accrual_type);

        RelativeLayout mContainer = ViewHolder.findViewById(convertView,R.id.ll_container);
        mContainer.setLayoutParams(mLlParams);
        ImageView mIvPLogo = ViewHolder.findViewById(convertView,R.id.iv_fancy_cover_item_p_logo);

        XtzNetCreditNewsBean.BlistBean blistBean = mList.get(position);
        //mTvP_name.setText(blistBean.getP_name());//平台名称
        mTvBidName.setText(blistBean.getProject_name());//项目名称
        mTvRate.setText(blistBean.getRate());//年化率
        String time_type = blistBean.getTime_type();
        mTvDateType.setText("1".equalsIgnoreCase(time_type)?"个月":"天");
        mTvDate.setText(blistBean.getTime_limit());
        ImgLoadUtil.loadImgBySplice(blistBean.getP_logo(), mIvPLogo, R.mipmap.pic_021);
        String at_type = blistBean.getAt_type();//加息类型
        mTvRateExtra.setText(blistBean.getAdd_interest());//加息
        mTvRateExtra.setVisibility("0".equals(at_type)?View.INVISIBLE:View.VISIBLE);
        if(!"0".equals(at_type)){
            mTvAdd.setVisibility(View.VISIBLE);
            mLayoutExtra.setVisibility(View.VISIBLE);
            Drawable drawable = mContext.getResources().getDrawable("2".equalsIgnoreCase(at_type)?
                    R.mipmap.pic_add_xi:R.mipmap.pic_first_xi);
            mTvRateExtra.setTextColor(mContext.getResources().getColor("2".equalsIgnoreCase(at_type)?
                    R.color.finance_supermarket_choice_text_red: R.color.finance_supermarket_title_select));
            mExtraPercent.setTextColor(mContext.getResources().getColor("2".equalsIgnoreCase(at_type)?
                    R.color.finance_supermarket_choice_text_red: R.color.finance_supermarket_title_select));
            drawable.setBounds(0, 0, UIUtil.dip2px("2".equalsIgnoreCase(at_type)?30:36),UIUtil.dip2px(17));
            mTvBidName.setCompoundDrawables(null, null, drawable, null);
            mTvAdd.setTextColor(mContext.getResources().getColor("1".equals(at_type) ?R.color.finance_supermarket_title_select:R.color.finance_supermarket_choice_text_red));
            mBidBg.setBackgroundResource("1".equals(at_type) ?R.mipmap.first_accrual:R.mipmap.xtz_accrual);


        }else{
            mTvAdd.setVisibility(View.INVISIBLE);
            mLayoutExtra.setVisibility(View.INVISIBLE);
            mTvBidName.setCompoundDrawables(null,null,null,null);
        }
        convertView.setLayoutParams(mParams);
        return convertView;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
