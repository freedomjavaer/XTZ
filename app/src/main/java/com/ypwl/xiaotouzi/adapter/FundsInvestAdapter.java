package com.ypwl.xiaotouzi.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.bean.FundsInvestBean;

/**
 * 基金投资适配器
 * Created by PDK on 2016/4/15.
 */
public class FundsInvestAdapter extends BaseAdapter{

    private Context context;
    private FundsInvestBean bean;

    public FundsInvestAdapter(Context applicationContext, FundsInvestBean fundsInvestBean) {
        this.context = applicationContext;
        this.bean = fundsInvestBean;
    }

    @Override
    public int getCount() {
        return bean.getData().size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_funds_invest_adapter, null);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.mFundsName = (TextView) convertView.findViewById(R.id.tv_funds_name);
        viewHolder.mRate = (TextView) convertView.findViewById(R.id.tv_max_rate);
        viewHolder.mFundsFeature = (TextView) convertView.findViewById(R.id.tv_funds_feature);
        viewHolder.mStartInvestMoney = (TextView) convertView.findViewById(R.id.tv_start_invest_money);
        viewHolder.mPercent = (TextView) convertView.findViewById(R.id.tv_percent);
        viewHolder.mCollectComplete = (ImageView) convertView.findViewById(R.id.iv_collect_complete);
        viewHolder.mExtraWord = (TextView) convertView.findViewById(R.id.tv_extra_word);

        viewHolder.mFundsName.setText(bean.getData().get(position).getProduct_name());
        if (Character.isDigit(bean.getData().get(position).getMax_profit().charAt(0))){
            viewHolder.mRate.setText(bean.getData().get(position).getMax_profit());
        }else {
            viewHolder.mRate.setText(bean.getData().get(position).getMax_profit());
            viewHolder.mPercent.setVisibility(View.GONE);
        }
        viewHolder.mFundsFeature.setText(bean.getData().get(position).getFeature());
        viewHolder.mStartInvestMoney.setText(bean.getData().get(position).getThreshold());

        if ("0".equals(bean.getData().get(position).getPro_status())){
            viewHolder.mCollectComplete.setVisibility(View.VISIBLE);
            viewHolder.mFundsName.setTextColor(context.getResources().getColor(R.color.textcolor));
            viewHolder.mRate.setTextColor(context.getResources().getColor(R.color.textcolor));
            viewHolder.mStartInvestMoney.setTextColor(context.getResources().getColor(R.color.textcolor));
            viewHolder.mPercent.setTextColor(context.getResources().getColor(R.color.textcolor));
            viewHolder.mExtraWord.setTextColor(context.getResources().getColor(R.color.textcolor));
        }else {
            viewHolder.mCollectComplete.setVisibility(View.GONE);
            viewHolder.mFundsName.setTextColor(context.getResources().getColor(R.color.black));
            viewHolder.mRate.setTextColor(context.getResources().getColor(R.color.red));
            viewHolder.mStartInvestMoney.setTextColor(context.getResources().getColor(R.color.black));
            viewHolder.mPercent.setTextColor(context.getResources().getColor(R.color.red));
            viewHolder.mExtraWord.setTextColor(context.getResources().getColor(R.color.black));
        }

        return convertView;
    }

    class ViewHolder{
        TextView mFundsName;
        TextView mRate;
        TextView mFundsFeature;
        TextView mStartInvestMoney;
        TextView mPercent;
        ImageView mCollectComplete;
        TextView mExtraWord;
    }

}
