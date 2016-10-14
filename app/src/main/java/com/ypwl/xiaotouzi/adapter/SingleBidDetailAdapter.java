package com.ypwl.xiaotouzi.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.bean.SingleBidBackMoneyBean;
import com.ypwl.xiaotouzi.utils.DateTimeUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.ViewHolder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * function : 单个标的回款详情数据适配器
 * <p/>
 * Created by tengtao on 2016/3/28.
 */
public class SingleBidDetailAdapter extends BaseAdapter {
    private IChangeStatusListener mListener;
    private Activity mActivity;
    private List<SingleBidBackMoneyBean.ListEntity> mList = new ArrayList<>();
    private String is_auto;

    public SingleBidDetailAdapter(Activity activity){
        this.mActivity = activity;
    }

    public void loadData(SingleBidBackMoneyBean bean){
        is_auto = bean.getIs_auto();
        mList.clear();
        List<SingleBidBackMoneyBean.ListEntity> list = bean.getList();
        if(list!=null && list.size()>0){
            mList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void changeStatus(int position, String status){
        if("0".equals(status)){
            String[] strings = handleDate(mList.get(position).getReturn_time());
            String date = strings[0]+"-"+strings[1]+"-"+strings[2];
            Date targetDate = DateTimeUtil.parseDate(date, DateTimeUtil.DF_YYYY_MM_DD);
            Date currentDate = DateTimeUtil.parseDate(DateTimeUtil.formatDateTime(DateTimeUtil.getCurrentDate(), DateTimeUtil.DF_YYYY_MM_DD), DateTimeUtil.DF_YYYY_MM_DD);
            //差值
            long mDvalue = targetDate.getTime() - currentDate.getTime();

            if (Math.abs(mDvalue) < DateTimeUtil.day || mDvalue <=0) {
                mList.get(position).setStatus("3");//今天以前设置为待确认
            }else{
                mList.get(position).setStatus(status);
            }
        }else{
            mList.get(position).setStatus(status);
        }
        notifyDataSetChanged();
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mActivity, R.layout.item_single_bid_detail_layout, null);
        }
        TextView mTvMonthDay = ViewHolder.findViewById(convertView, R.id.tv_bid_returned_detail_month_day);
        TextView mTvAllMoney = ViewHolder.findViewById(convertView, R.id.tv_bid_returned_detail_all_money);
        TextView mTvPercent = ViewHolder.findViewById(convertView, R.id.tv_bid_returned_detail_percent);
        TextView mTvYear = ViewHolder.findViewById(convertView, R.id.tv_bid_returned_detail_year);
        TextView mTvBenjin = ViewHolder.findViewById(convertView, R.id.tv_bid_returned_detail_benjin);
        TextView mTvShouyi = ViewHolder.findViewById(convertView, R.id.tv_bid_returned_detail_shouyi);
        TextView mTvOperation = ViewHolder.findViewById(convertView, R.id.tv_bid_returned_detail_operation);
        RelativeLayout mLayoutOperation = ViewHolder.findViewById(convertView,R.id.layout_single_bid_operation);

        SingleBidBackMoneyBean.ListEntity listEntity = mList.get(position);
        String return_time = listEntity.getReturn_time();
        String[] strings = handleDate(return_time);
        mTvMonthDay.setText(strings[1] + "/" + strings[2]);
        mTvYear.setText(strings[0]);
        mTvAllMoney.setText("回款" + listEntity.getTotal());
        mTvPercent.setText(listEntity.getPeriod() + "/" + listEntity.getPeriod_total() + "期");
        mTvBenjin.setText("本金" + listEntity.getCapital());
        mTvShouyi.setText("收益" + listEntity.getProfit());

        mLayoutOperation.setTag(R.id.tv_bid_returned_detail_operation, position);
        mLayoutOperation.setOnClickListener(mOnClickListener);
        String status = listEntity.getStatus();
        switch (status){
            case "0":
                mTvOperation.setText("待回");
                mTvOperation.setTextColor(Color.parseColor(("0".equalsIgnoreCase(is_auto)) ? "#ffffff" : "#9E9D9D"));
                if("0".equalsIgnoreCase(is_auto)){
                    mTvOperation.setBackgroundResource(R.drawable.shape_back_money_status_change_deep_bg);
                }
                break;
            case "1":
                mTvOperation.setText("已回");
                mTvOperation.setTextColor(Color.parseColor(("0".equalsIgnoreCase(is_auto)) ? "#ffffff" : "#D5D5D5"));
                if("0".equalsIgnoreCase(is_auto)){
                    mTvOperation.setBackgroundResource(R.drawable.shape_back_money_status_change_bg);
                }
                break;
            case "2":
                mTvOperation.setText("逾期");
                mTvOperation.setTextColor(Color.parseColor(("0".equalsIgnoreCase(is_auto)) ? "#ffffff" : "#fed61c"));

                if("0".equalsIgnoreCase(is_auto)){
                    mTvOperation.setBackgroundResource(R.drawable.shape_back_money_status_change_yellow_bg);
                }
                break;
            case "3":
                mTvOperation.setText("待确认");
                mTvOperation.setTextColor(Color.parseColor(("0".equalsIgnoreCase(is_auto)) ? "#ffffff" : "#2b7de1"));

                if("0".equalsIgnoreCase(is_auto)){
                    mTvOperation.setBackgroundResource(R.drawable.shape_back_money_status_change_blue_bg);
                }
                break;
            default:
                mTvOperation.setText("待回");
                mTvOperation.setTextColor(Color.parseColor(("0".equalsIgnoreCase(is_auto)) ? "#ffffff" : "#9E9D9D"));
                if("0".equalsIgnoreCase(is_auto)){
                    mTvOperation.setBackgroundResource(R.drawable.shape_back_money_status_change_deep_bg);
                }
                break;
        }
        if("1".equalsIgnoreCase(is_auto)){//自动记账平台
            mTvOperation.setBackgroundDrawable(null);
        }
        return convertView;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag(R.id.tv_bid_returned_detail_operation);
            String status = mList.get(position).getStatus();
            if("0".equalsIgnoreCase(is_auto)){
                mListener.onChangeStatus(mList.get(position).getRid(), status, position);
            }else{
                UIUtil.showToastShort("自动记账平台，不能更改状态");
            }
        }
    };

    public interface IChangeStatusListener{
        void onChangeStatus(String rids,String status,int position);
    }

    public void setIChangeStatusListener(IChangeStatusListener listener){
        this.mListener = listener;
    }

    /**
     * 处理时间戳
     * @param time Unix时间戳
     * @return 年月日的数组
     */
    private String[] handleDate(String time) {
        long millisTime = 0;
        try {
            millisTime = Long.parseLong(time) * 1000;
        } catch (Exception e) {
            e.printStackTrace();
        }
        String formatDateTime = DateTimeUtil.formatDateTime(millisTime, "yyyy-MM-dd");
        return formatDateTime.split("-");
    }
}
