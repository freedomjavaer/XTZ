package com.ypwl.xiaotouzi.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.bean.ProfitTendencyBean;
import com.ypwl.xiaotouzi.event.ProfitTendencyItemHighlightEvent;
import com.ypwl.xiaotouzi.manager.EventHelper;
import com.ypwl.xiaotouzi.utils.ChartUtils;
import com.ypwl.xiaotouzi.utils.DateTimeUtil;
import com.ypwl.xiaotouzi.utils.GlobalUtils;

/**
 * 年化率趋势适配器
 * Created by PDK on 2016/1/29.
 */
public class AnnualRateTendencyAdapter extends RecyclerView.Adapter<AnnualRateTendencyAdapter.ProfitTendencyViewHolder> {

    private Context context;
    private ProfitTendencyBean bean;
    private int selectPostion = -1;

    public AnnualRateTendencyAdapter(Context context, ProfitTendencyBean bean) {
        this.context = context;
        this.bean = bean;
    }

    @Override
    public ProfitTendencyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ProfitTendencyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_profit_tendency_recycleview, parent, false));
    }

    @Override
    public void onBindViewHolder(ProfitTendencyViewHolder holder, int position) {
        holder.mBackMoneyState.setTextColor(Color.parseColor("0".equals(bean.getList().get(position).getType()) ? "#FE5E5E" : "#47D047"));
        if (selectPostion == position){
            holder.tvProfitTandencyDate.setTextColor(context.getResources().getColor(R.color.black));
            holder.mTendencyDataType.setTextColor(context.getResources().getColor(R.color.choosedcolor));
            holder.tvInvestAnalyzePlatformName.setTextColor(context.getResources().getColor(R.color.black));
            holder.tvProfitTandencyBidName.setTextColor(context.getResources().getColor(R.color.choosedcolor));
            holder.tvProfitTandencyMoney.setTextColor(context.getResources().getColor(R.color.choosedcolor));
            holder.tvProfitTandencyAnnualRate.setTextColor(context.getResources().getColor(R.color.choosedcolor));
            //holder.mBackMoneyState.setTextColor(context.getResources().getColor(R.color.choosedcolor));

        }else {
            holder.tvProfitTandencyDate.setTextColor(context.getResources().getColor(R.color.unchoosedcolor));
            holder.mTendencyDataType.setTextColor(context.getResources().getColor(R.color.unchoosedcolor));
            holder.tvInvestAnalyzePlatformName.setTextColor(context.getResources().getColor(R.color.unchoosedcolor));
            holder.tvProfitTandencyBidName.setTextColor(context.getResources().getColor(R.color.unchoosedcolor));
            holder.tvProfitTandencyMoney.setTextColor(context.getResources().getColor(R.color.unchoosedcolor));
            holder.tvProfitTandencyAnnualRate.setTextColor(context.getResources().getColor(R.color.unchoosedcolor));
            //holder.mBackMoneyState.setTextColor(context.getResources().getColor(R.color.unchoosedcolor));

        }

        String date = GlobalUtils.dateFormat(bean.getList().get(position).getTime());
        String dateString = DateTimeUtil.processDate(date);
        holder.tvProfitTandencyDate.setText(dateString);
        holder.mTendencyDataType.setText(DateTimeUtil.dateCapture(date));
        holder.tvInvestAnalyzePlatformName.setText(bean.getList().get(position).getP_name());
        holder.tvProfitTandencyBidName.setText(bean.getList().get(position).getProject_name());
        holder.tvProfitTandencyMoney.setText("投资" + bean.getList().get(position).getMoney());
        holder.tvProfitTandencyAnnualRate.setText("年化率" + bean.getList().get(position).getB_rate() + "%");

        holder.mBackMoneyState.setText("0".equals(bean.getList().get(position).getType()) ? "回款结束" : "投资");

        holder.item.setTag(R.id.tv_profit_tandency_date, position);
        holder.item.setTag(R.id.tv_profit_tendency_data_type, holder);
        holder.item.setOnClickListener(mOnClickListener);

    }

    @Override
    public int getItemCount() {
        return bean.getList().size();
    }

    public class ProfitTendencyViewHolder extends RecyclerView.ViewHolder{

        private TextView tvProfitTandencyDate;
        private TextView tvInvestAnalyzePlatformName;
        private TextView tvProfitTandencyBidName;
        private TextView tvProfitTandencyMoney;
        private TextView tvProfitTandencyAnnualRate;
        private TextView mTendencyDataType;
        private View item;
        private final TextView mBackMoneyState;

        public ProfitTendencyViewHolder(View itemView) {
            super(itemView);
            item = itemView;
            tvProfitTandencyDate = (TextView) itemView.findViewById(R.id.tv_profit_tandency_date);
            mTendencyDataType = (TextView) itemView.findViewById(R.id.tv_profit_tendency_data_type);
            tvInvestAnalyzePlatformName = (TextView) itemView.findViewById(R.id.tv_profit_tandency_platform_name);
            tvProfitTandencyBidName = (TextView) itemView.findViewById(R.id.tv_profit_tandency_bid_name);
            tvProfitTandencyMoney = (TextView) itemView.findViewById(R.id.tv_profit_tandency_money);
            tvProfitTandencyAnnualRate = (TextView) itemView.findViewById(R.id.tv_profit_tandency_annual_rate);
            mBackMoneyState = (TextView) itemView.findViewById(R.id.tv_back_money_state);

        }
    }

    public void selectItem(int position){
        selectPostion = position;
        notifyDataSetChanged();
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag(R.id.tv_profit_tandency_date);
            boolean b = !(selectPostion==position);
            if (b){
                EventHelper.post(new ProfitTendencyItemHighlightEvent(position, 0));
                ChartUtils.profitTendencyBarSet.setHighLightAlpha(120);//使柱状图能高亮
            }else {
                EventHelper.post(new ProfitTendencyItemHighlightEvent(position, -1));
                ChartUtils.profitTendencyBarSet.setHighLightAlpha(0);//使柱状图不能高亮
            }

            selectPostion = b?position:-1;

            ProfitTendencyViewHolder holder = (ProfitTendencyViewHolder) v.getTag(R.id.tv_profit_tendency_data_type);

            if (b ){
                holder.tvProfitTandencyDate.setTextColor(context.getResources().getColor(R.color.choosedcolor));
                holder.mTendencyDataType.setTextColor(context.getResources().getColor(R.color.choosedcolor));
                holder.tvInvestAnalyzePlatformName.setTextColor(context.getResources().getColor(R.color.choosedcolor));
                holder.tvProfitTandencyBidName.setTextColor(context.getResources().getColor(R.color.choosedcolor));
                holder.tvProfitTandencyMoney.setTextColor(context.getResources().getColor(R.color.choosedcolor));
                holder.tvProfitTandencyAnnualRate.setTextColor(context.getResources().getColor(R.color.choosedcolor));
                notifyDataSetChanged();
            }else {
                holder.tvProfitTandencyDate.setTextColor(context.getResources().getColor(R.color.unchoosedcolor));
                holder.mTendencyDataType.setTextColor(context.getResources().getColor(R.color.unchoosedcolor));
                holder.tvInvestAnalyzePlatformName.setTextColor(context.getResources().getColor(R.color.unchoosedcolor));
                holder.tvProfitTandencyBidName.setTextColor(context.getResources().getColor(R.color.unchoosedcolor));
                holder.tvProfitTandencyMoney.setTextColor(context.getResources().getColor(R.color.unchoosedcolor));
                holder.tvProfitTandencyAnnualRate.setTextColor(context.getResources().getColor(R.color.unchoosedcolor));
                notifyDataSetChanged();
            }

        }
    };


}
