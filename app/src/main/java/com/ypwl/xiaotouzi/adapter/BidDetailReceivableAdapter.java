package com.ypwl.xiaotouzi.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.bean.LlcBidDetailBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.ui.activity.SingleBidBackMoneyDetailActivity;
import com.ypwl.xiaotouzi.utils.DateTimeUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.ViewHolder;

import java.util.Date;
import java.util.List;

/**
 * function :标的详情 回款列表list
 * Created by llc on 2016/4/25 16:38
 * Email：licailuo@qq.com
 */
public class BidDetailReceivableAdapter extends BaseAdapter {


    private Activity mContext;
    private List<LlcBidDetailBean.ListEntity> mDataList;
    private String isAuto;
    private String pName;
    private String p_time;
    private String aId;

    public BidDetailReceivableAdapter(Activity context, LlcBidDetailBean bean) {
        this.mContext = context;
        this.mDataList = bean.getList();
        this.isAuto = bean.getIs_auto();
        this.pName = bean.getP_name();
        this.p_time = bean.getProject_name();
        this.aId = bean.getAid();
    }


    @Override
    public int getCount() {
        return mDataList != null ? mDataList.size() : 0;

    }

    @Override
    public LlcBidDetailBean.ListEntity getItem(int position) {
        return mDataList != null ? mDataList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_single_bid_detail_layout, null);
        }
        TextView mTvMonthDay = ViewHolder.findViewById(convertView, R.id.tv_bid_returned_detail_month_day);
        TextView mTvAllMoney = ViewHolder.findViewById(convertView, R.id.tv_bid_returned_detail_all_money);
        TextView mTvPercent = ViewHolder.findViewById(convertView, R.id.tv_bid_returned_detail_percent);
        TextView mTvYear = ViewHolder.findViewById(convertView, R.id.tv_bid_returned_detail_year);
        TextView mTvBenjin = ViewHolder.findViewById(convertView, R.id.tv_bid_returned_detail_benjin);
        TextView mTvShouyi = ViewHolder.findViewById(convertView, R.id.tv_bid_returned_detail_shouyi);
        TextView mTvOperation = ViewHolder.findViewById(convertView, R.id.tv_bid_returned_detail_operation);
        RelativeLayout mLayoutOperation = ViewHolder.findViewById(convertView, R.id.layout_single_bid_operation);
        LinearLayout mLayoutItem = ViewHolder.findViewById(convertView, R.id.layout_item_invest_by_date);
        mLayoutOperation.setTag(R.id.layout_single_bid_operation, position);
        mLayoutOperation.setOnClickListener(mOnClickListener);
        mLayoutItem.setTag(R.id.layout_item_invest_by_date, position);
        mLayoutItem.setOnClickListener(mOnClickListener);

        LlcBidDetailBean.ListEntity data = mDataList.get(position);
        String return_time = data.getReturn_time();
        String[] dates = handleDate(return_time);
        //日期  月 日
        mTvMonthDay.setText(dates[1] + "/" + dates[2]);
        //日期  年
        mTvYear.setText(dates[0]);
        //回款金额
        mTvAllMoney.setText("回款" + data.getTotal());
        //时间
        long time = 0;
        try {
            time = Long.parseLong(return_time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //期数
        mTvPercent.setText(data.getPeriod() + "/" + data.getPeriod_total() + "期");
        mTvBenjin.setText("本金" + data.getCapital());
        mTvShouyi.setText("收益" + data.getProfit());
        String status = data.getStatus();
        switch (status) {
            case "0":
                mTvOperation.setText("待回");
                mTvOperation.setTextColor(Color.parseColor(("0".equalsIgnoreCase(isAuto)) ? "#ffffff" : "#9E9D9D"));
                if ("0".equalsIgnoreCase(isAuto)) {
                    mTvOperation.setBackgroundResource(R.drawable.shape_back_money_status_change_deep_bg);
                }
                break;
            case "1":
                mTvOperation.setText("已回");
                mTvOperation.setTextColor(Color.parseColor(("0".equalsIgnoreCase(isAuto)) ? "#ffffff" : "#D5D5D5"));
                if ("0".equalsIgnoreCase(isAuto)) {
                    mTvOperation.setBackgroundResource(R.drawable.shape_back_money_status_change_bg);
                }
                break;
            case "2":
                mTvOperation.setText("逾期");
                mTvOperation.setTextColor(Color.parseColor(("0".equalsIgnoreCase(isAuto)) ? "#ffffff" : "#fed61c"));

                if ("0".equalsIgnoreCase(isAuto)) {
                    mTvOperation.setBackgroundResource(R.drawable.shape_back_money_status_change_yellow_bg);
                }
                break;
            case "3":
                mTvOperation.setText("待确认");
                mTvOperation.setTextColor(Color.parseColor(("0".equalsIgnoreCase(isAuto)) ? "#ffffff" : "#2b7de1"));

                if ("0".equalsIgnoreCase(isAuto)) {
                    mTvOperation.setBackgroundResource(R.drawable.shape_back_money_status_change_blue_bg);
                }
                break;
            default:
                mTvOperation.setText("待回");
                mTvOperation.setTextColor(Color.parseColor(("0".equalsIgnoreCase(isAuto)) ? "#ffffff" : "#9E9D9D"));
                if ("0".equalsIgnoreCase(isAuto)) {
                    mTvOperation.setBackgroundResource(R.drawable.shape_back_money_status_change_deep_bg);
                }
                break;
        }
        if ("1".equalsIgnoreCase(isAuto)) {//自动记账平台
            mTvOperation.setBackgroundDrawable(null);
        }


        return convertView;
    }

    /**
     * 返回前天、昨天、今天、明天、后台;
     * 之外几天前或几天后形式
     *
     * @param time unix时间戳
     * @return
     */
    private String getNearDate(long time) {
        String[] strings = handleDate(time + "");
        String date = strings[0] + "-" + strings[1] + "-" + strings[2];
        Date targetDate = DateTimeUtil.parseDate(date, DateTimeUtil.DF_YYYY_MM_DD);
        Date currentDate = DateTimeUtil.parseDate(DateTimeUtil.formatDateTime(DateTimeUtil.getCurrentDate(), DateTimeUtil.DF_YYYY_MM_DD), DateTimeUtil.DF_YYYY_MM_DD);
        //差值
        long mDvalue = targetDate.getTime() - currentDate.getTime();

        if (Math.abs(mDvalue) < DateTimeUtil.day) {
            return "今天";
        } else if (Math.abs(mDvalue) < 2 * DateTimeUtil.day) {
            return mDvalue > 0 ? "明天" : "昨天";
        } else if (Math.abs(mDvalue) < 3 * DateTimeUtil.day) {
            return mDvalue > 0 ? "后天" : "前天";
        } else {
            return DateTimeUtil.getIntervalTimeDay(new Date(System.currentTimeMillis()), new Date(time * 1000));
        }
    }

    /**
     * 更改回款状态
     *
     * @param position  更改回款状态的标的位置
     * @param perStatus 前一个回款状态
     * @param status    更改后的状态
     */
    public void changeStatus(int position, String perStatus, String status) {
        //根据状态更改显示
        if ("0".equals(status)) {
            String[] strings = handleDate(mDataList.get(position).getReturn_time());
            String date = strings[0] + "-" + strings[1] + "-" + strings[2];
            Date targetDate = DateTimeUtil.parseDate(date, DateTimeUtil.DF_YYYY_MM_DD);
            Date currentDate = DateTimeUtil.parseDate(DateTimeUtil.formatDateTime(DateTimeUtil.getCurrentDate(), DateTimeUtil.DF_YYYY_MM_DD), DateTimeUtil.DF_YYYY_MM_DD);
            //差值
            long mDvalue = targetDate.getTime() - currentDate.getTime();

            if (Math.abs(mDvalue) < DateTimeUtil.day || mDvalue <= 0) {
                mDataList.get(position).setStatus("3");//今天以前设置为待确认
            } else {
                mDataList.get(position).setStatus(status);
            }
        } else {
            mDataList.get(position).setStatus(status);
        }
        notifyDataSetChanged();
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.layout_single_bid_operation://改变状态按钮
                    int position = (int) v.getTag(R.id.layout_single_bid_operation);
                    LlcBidDetailBean.ListEntity recentItemBean = mDataList.get(position);
                    String status = recentItemBean.getStatus();
                    if ("0".equalsIgnoreCase(isAuto)) {//非自动记账平台
                        mListener.onChangeStatus(mDataList.get(position).getRid(), status, position);
                    } else {
                        UIUtil.showToastShort("自动记账平台，不能更改状态");
                    }
                    break;
                case R.id.layout_item_invest_by_date:
                    Intent intent = new Intent(mContext, SingleBidBackMoneyDetailActivity.class);
                    intent.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA, aId);
                    intent.putExtra(Const.KEY_INTENT_JUMP_FROM_DATA, "近期回款");
                    mContext.startActivity(intent);
                    Const.JUMP_TO_SINGLE_BID_FROM_TYPE = 0;//标记从回款跳转到单个标的回款详情
                    break;
            }
        }
    };


    public interface IChangeStatusListener {
        void onChangeStatus(String rids, String status, int position);
    }

    private IChangeStatusListener mListener;

    public void setIChangeStatusListener(IChangeStatusListener listener) {
        this.mListener = listener;
    }

    /**
     * 处理时间戳
     *
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
