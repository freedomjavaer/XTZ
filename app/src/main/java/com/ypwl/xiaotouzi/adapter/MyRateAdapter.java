package com.ypwl.xiaotouzi.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.bean.ProfitRangeBean;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.view.mprogress.MagicProgressBar;

/**
 * 收益排行------年化率
 * Created by PDK on 2016/4/12.
 */
public class MyRateAdapter extends BaseAdapter {

    private ProfitRangeBean bean;
    private Context context;
    private int type;

    public MyRateAdapter(ProfitRangeBean bean, int type, Context context) {
        this.bean = bean;
        this.context = context;
        this.type = type;
    }

    @Override
    public int getCount() {
        return bean.getList().size();
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

        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            if (type == 1){
                convertView = View.inflate(context, R.layout.item_rate_range_one, null);
            }else {
                convertView = View.inflate(context, R.layout.item_rate_range_two, null);
            }
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (type == 1) {//平台
            viewHolder.mSerialNumber = (TextView) convertView.findViewById(R.id.tv_serial_number);
            viewHolder.mRLContent = (RelativeLayout) convertView.findViewById(R.id.rl_profit_item_content);
            viewHolder.mPlatformName = (TextView) convertView.findViewById(R.id.tv_profit_range_platform_name);
            viewHolder.mNumberOne = (TextView) convertView.findViewById(R.id.tv_profit_range_number_one);
            viewHolder.mNumberTwo = (TextView) convertView.findViewById(R.id.tv_profit_range_number_two);

            viewHolder.mBarChart = (MagicProgressBar) convertView.findViewById(R.id.pb_chart_view);
            viewHolder.mBarChart.setBackgroundColor(position == 0 ? COLOR_PROGRESS : COLOR_TRANS);
            viewHolder.mBarChart.setFillColor(COLOR_PROGRESS);
            float percent = (float) (Double.parseDouble(bean.getList().get(position).getRate().replaceAll(",", ""))/Double.parseDouble(bean.getList().get(0).getRate().replaceAll(",", "")));
            if (percent < 0.06f){
                percent = 0.06f;
            }
            viewHolder.mBarChart.setPercent(percent);

            viewHolder.mSerialNumber.setText(position + 1 + "");
            viewHolder.mPlatformName.setText(bean.getList().get(position).getP_name());
            viewHolder.mNumberOne.setText(bean.getList().get(position).getRate() + "%");
            viewHolder.mNumberTwo.setText("收益" + bean.getList().get(position).getProfit());

        } else {//标
            viewHolder.mSerialNumber = (TextView) convertView.findViewById(R.id.tv_serial_number);
            viewHolder.mRLContent = (RelativeLayout) convertView.findViewById(R.id.rl_profit_item_content);
            viewHolder.mPlatformName = (TextView) convertView.findViewById(R.id.tv_profit_range_platform_name);
            viewHolder.mBidName = (TextView) convertView.findViewById(R.id.tv_profit_range_bid_name);
            viewHolder.mNumberOne = (TextView) convertView.findViewById(R.id.tv_profit_range_number_profit);
            viewHolder.mNumberTwo = (TextView) convertView.findViewById(R.id.tv_profit_range_rate);

            viewHolder.mBarChart = (MagicProgressBar) convertView.findViewById(R.id.pb_chart_view);
            viewHolder.mBarChart.setBackgroundColor(position == 0 ? COLOR_PROGRESS : COLOR_TRANS);
            viewHolder.mBarChart.setFillColor(COLOR_PROGRESS);
            float percent = (float) (Double.parseDouble(bean.getList().get(position).getRate().replaceAll(",", ""))/Double.parseDouble(bean.getList().get(0).getRate().replaceAll(",", "")));
            if (percent < 0.06f){
                percent = 0.06f;
            }
            viewHolder.mBarChart.setPercent(percent);

            viewHolder.mSerialNumber.setText(position + 1 + "");
            viewHolder.mPlatformName.setText(bean.getList().get(position).getP_name());
            viewHolder.mBidName.setText(bean.getList().get(position).getProject_name());
            viewHolder.mNumberOne.setText(bean.getList().get(position).getRate() + "%");
            viewHolder.mNumberTwo.setText("收益" + bean.getList().get(position).getProfit());
        }

        return convertView;
    }

    private static final int COLOR_TRANS = UIUtil.getColor(R.color.transparent);
    private static final int COLOR_PROGRESS = UIUtil.getColor(R.color.color_001);

    class ViewHolder {
        TextView mSerialNumber;
        TextView mPlatformName;
        TextView mBidName;
        TextView mNumberOne;
        TextView mNumberTwo;
        RelativeLayout mRLContent;
        MagicProgressBar mBarChart;
    }

}
