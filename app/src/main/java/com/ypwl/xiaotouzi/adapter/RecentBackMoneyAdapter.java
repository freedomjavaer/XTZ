package com.ypwl.xiaotouzi.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.bean.RecentBackMoneyBean;
import com.ypwl.xiaotouzi.bean.RecentItemBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.ui.activity.SingleBidBackMoneyDetailActivity;
import com.ypwl.xiaotouzi.utils.DateTimeUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.Util;
import com.ypwl.xiaotouzi.utils.ViewHolder;
import com.ypwl.xiaotouzi.view.stickylistheaders.StickyListHeadersAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * function :我的投资--近期回款适配器
 * <p/>
 * Created by tengtao on 2016/3/22.
 */
public class RecentBackMoneyAdapter extends BaseAdapter implements StickyListHeadersAdapter, SectionIndexer {
    private IChangeStatusListener mListener;
    private Activity mActivity;
    private List<RecentItemBean> mList = new ArrayList<>();//数据
    private int[] mSectionIndices;//索引位置
    private List<Integer> mDividerList = new ArrayList<>();
    private String[] mSectionHeaders;//索引值

    public RecentBackMoneyAdapter(Activity activity) {
        this.mActivity = activity;
    }

    public void loadData(List<RecentBackMoneyBean.ListEntity> list){
        handleData(list);
        mSectionIndices = getSectionIndices(mList);
        mSectionHeaders = getSectionHeaders(mList);
        notifyDataSetChanged();
    }

    /**
     * 更改回款状态
     * @param position 更改回款状态的标的位置
     * @param perStatus  前一个回款状态
     * @param status 更改后的状态
     */
    public void changeStatus(int position, String perStatus, String status, String totalMoney){
        //根据状态更改显示
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
        int start = 0;
        int end = 0;
        //改变回款数额
        if(mSectionIndices[mSectionIndices.length-1] <= position){
            start = mSectionIndices[mSectionIndices.length-1];
            end = mList.size()-1;
        }else{
            for(int i=0;i<mSectionIndices.length;i++){
                if(mSectionIndices[i]<=position){
                    start = mSectionIndices[i];
                }
                if(mSectionIndices[i]>position){
                    end = mSectionIndices[i];
                    break;
                }
            }
        }
        DecimalFormat df = new DecimalFormat("###0.00");
        boolean hasBacked = "1".equalsIgnoreCase(status);
        String total = mList.get(position).getTotal();
        Double currTotal = Double.parseDouble(total.replace(",",""));
        for(int j=start;j<mList.size() && j<end;j++){
            RecentItemBean recentItemBean = mList.get(j);
            String money = recentItemBean.getMoney();
            Double perMoney = Double.parseDouble(money.replace(",", ""));
            double result = perMoney;
            if(hasBacked){
                result = perMoney - currTotal;
            }else if("1".equalsIgnoreCase(perStatus)){
                result = perMoney + currTotal;
            }
            recentItemBean.setMoney(Util.markOperatorForMoney(df.format(result)));
        }
        //待收总额
        Double perTotal = Double.parseDouble(totalMoney.replace(",", ""));
        if(hasBacked){
            mListener.onChangeTotalMoney(Util.markOperatorForMoney(df.format(perTotal - currTotal)));
        }else if("1".equalsIgnoreCase(perStatus)){
            mListener.onChangeTotalMoney(Util.markOperatorForMoney(df.format(perTotal + currTotal)));
        }
        notifyDataSetChanged();
    }

    private void handleData(List<RecentBackMoneyBean.ListEntity> list){
        mList.clear();
        for(int i = 0;i<list.size();i++){
            List<RecentBackMoneyBean.ListEntity.DataEntity> data = list.get(i).getData();
            for(int j=0;j<data.size();j++){
                RecentBackMoneyBean.ListEntity.DataEntity dataEntity = data.get(j);
                RecentItemBean bean = new RecentItemBean();
                bean.setDatetime(list.get(i).getDatetime());
                bean.setMoney(list.get(i).getMoney());
                bean.setAid(dataEntity.getAid());
                bean.setCapital(dataEntity.getCapital());
                bean.setP_name(dataEntity.getP_name());
                bean.setPeriod(dataEntity.getPeriod());
                bean.setPeriod_total(dataEntity.getPeriod_total());
                bean.setProfit(dataEntity.getProfit());
                bean.setProject_name(dataEntity.getProject_name());
                bean.setRid(dataEntity.getRid());
                bean.setStatus(dataEntity.getStatus());
                bean.setTotal(dataEntity.getTotal());
                bean.setReturn_time(dataEntity.getReturn_time());
                bean.setIs_auto(dataEntity.getIs_auto());
                mList.add(bean);
            }
        }
    }

    /**
     * 获取索引
     */
    private int[] getSectionIndices(List<RecentItemBean> list) {
        ArrayList<Integer> sectionIndices = new ArrayList<Integer>();
        String datetime = list.get(0).getDatetime();
        sectionIndices.add(0);
        for (int i = 1; i < list.size(); i++) {
            String time = list.get(i).getDatetime();
            if (!time.equalsIgnoreCase(datetime)) {
                datetime = time;
                sectionIndices.add(i);
            }
        }
        mDividerList.clear();
        int[] sections = new int[sectionIndices.size()];
        for (int i = 0; i < sectionIndices.size(); i++) {
            sections[i] = sectionIndices.get(i);
            if(sectionIndices.get(i) >= 1)
                mDividerList.add(sectionIndices.get(i)-1);
        }
        return sections;
    }

    /**
     * 处理获取header数据
     */
    private String[] getSectionHeaders(List<RecentItemBean> list) {
        String[] headers = new String[mSectionIndices.length];
        for (int i = 0; i < mSectionIndices.length; i++) {
            RecentItemBean listEntity = list.get(mSectionIndices[i]);
            String return_time = listEntity.getReturn_time();
            String header = getFormatDate(return_time);
            headers[i] = header;
        }
        return headers;
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mList != null ? mList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mActivity, R.layout.item_recent_month_back_list, null);
        }
        View mDivider = ViewHolder.findViewById(convertView,R.id.item_divider);
        LinearLayout mItemView = ViewHolder.findViewById(convertView,R.id.layout_item_invest_by_date);
        TextView mTvDate =  ViewHolder.findViewById(convertView,R.id.tv_month_list_date);
        TextView mTvPlatName = ViewHolder.findViewById(convertView, R.id.tv_month_list_plat_name);
        TextView mTvBidName = ViewHolder.findViewById(convertView, R.id.tv_month_list_bid_name);
        TextView mTvPercent = ViewHolder.findViewById(convertView, R.id.tv_month_list_percent);
        TextView mTvFriendTime = ViewHolder.findViewById(convertView, R.id.tv_month_list_friend_time);
        TextView mTvTotal = ViewHolder.findViewById(convertView, R.id.tv_month_list_all_money);
        TextView mTvBenJin = ViewHolder.findViewById(convertView, R.id.tv_month_list_benjin);
        TextView mTvShouYi = ViewHolder.findViewById(convertView, R.id.tv_month_list_shouyi);
        TextView mTvOperation = ViewHolder.findViewById(convertView, R.id.tv_month_list_operation);
        RelativeLayout layoutOperation = ViewHolder.findViewById(convertView,R.id.layout_operation);
        layoutOperation.setOnClickListener(mOnClickListener);
        layoutOperation.setTag(R.id.tv_month_list_operation, position);
        mItemView.setOnClickListener(mOnClickListener);
        mItemView.setTag(R.id.layout_item_invest_by_date, position);
        //分界线的显示逻辑
        mDivider.setVisibility(mDividerList.contains(position) ? View.VISIBLE : View.GONE);
        RecentItemBean data = mList.get(position);
        String return_time = data.getReturn_time();
        String[] dates = handleDate(return_time);
        //设置数据
        mTvDate.setText(dates[1] + "月" + dates[2] + "日");
        //平台名称
        String p_name = data.getP_name();
        mTvPlatName.setText(p_name);
        //标的名称
        String project_name = data.getProject_name();
        mTvBidName.setText(project_name);
        mTvPercent.setText(data.getPeriod() + "/" + data.getPeriod_total() + "期");
        long time=0;
        try {
            time = Long.parseLong(return_time);
        }catch (Exception e){
            e.printStackTrace();
        }
        mTvFriendTime.setText(getNearDate(time));
        mTvTotal.setText(data.getTotal());//回款
        mTvBenJin.setText("本金"+data.getCapital());
        mTvShouYi.setText("收益"+data.getProfit());
        String status =data.getStatus();
        String is_auto = data.getIs_auto();//0：手动记账，1：自动记账
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

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = View.inflate(mActivity, R.layout.layout_item_recent_back_money_header, null);
            holder.mTvDate = (TextView) convertView.findViewById(R.id.tv_item_recent_back_money_date);
            holder.mTvTotalMoney = (TextView) convertView.findViewById(R.id.tv_item_recent_back_month_money);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        String return_time = mList.get(position).getReturn_time();
        String money = mList.get(position).getMoney();
        holder.mTvDate.setText(getFormatDate(return_time));
        holder.mTvTotalMoney.setText("待回"+money);
        return convertView;
    }

    @Override
    public Object[] getSections() {
        return mSectionHeaders;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        if (mSectionIndices.length == 0) {
            return 0;
        }
        if (sectionIndex >= mSectionIndices.length) {
            sectionIndex = mSectionIndices.length - 1;
        } else if (sectionIndex < 0) {
            sectionIndex = 0;
        }
        return mSectionIndices[sectionIndex];
    }

    @Override
    public int getSectionForPosition(int position) {
        for (int i = 0; i < mSectionIndices.length; i++) {
            if (position < mSectionIndices[i]) {
                return i - 1;
            }
        }
        return mSectionIndices.length - 1;
    }

    @Override
    public long getHeaderId(int position) {
        for (int i = 0; i < mSectionIndices.length; i++) {
            if (position < mSectionIndices[i]) {
                return i - 1;
            }
        }
        return mSectionIndices.length - 1;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.layout_operation://改变状态按钮
                    int position = (int) v.getTag(R.id.tv_month_list_operation);
                    RecentItemBean recentItemBean = mList.get(position);
                    String status = recentItemBean.getStatus();
                    String is_auto = recentItemBean.getIs_auto();
                    if("0".equalsIgnoreCase(is_auto)) {//非自动记账平台
                        mListener.onChangeStatus(mList.get(position).getRid(), status, position);
                    }else{
                        UIUtil.showToastShort("自动记账平台，不能更改状态");
                    }
                    break;
                case R.id.layout_item_invest_by_date:
                    int index = (int) v.getTag(R.id.layout_item_invest_by_date);
                    String aid = mList.get(index).getAid();
                    Intent intent = new Intent(mActivity, SingleBidBackMoneyDetailActivity.class);
                    intent.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA,aid);
                    intent.putExtra(Const.KEY_INTENT_JUMP_FROM_DATA,"我的投资");
                    mActivity.startActivity(intent);
                    Const.JUMP_TO_SINGLE_BID_FROM_TYPE = 0;//标记从回款跳转到单个标的回款详情
                    break;
            }
        }
    };

    class HeaderViewHolder {
        TextView mTvDate, mTvTotalMoney;
    }

    /**
     * 处理时间戳
     * @param date Unix时间戳
     * @return 年月日的数组
     */
    private String getFormatDate(String date) {
        long millisTime = 0;
        try {
            millisTime = Long.parseLong(date)*1000;
        } catch (Exception e) {
            e.printStackTrace();
        }
        String formatDateTime = DateTimeUtil.formatDateTime(millisTime, "yyyy-MM-dd");
        String[] split = formatDateTime.split("-");
        String header = split[0].substring(2,4) + "年" + split[1] + "月";
        return header;
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

    /**
     * 返回前天、昨天、今天、明天、后台;
     * 之外几天前或几天后形式
     * @param time unix时间戳
     * @return
     */
    private String getNearDate(long time){
        String[] strings = handleDate(time+"");
        String date = strings[0]+"-"+strings[1]+"-"+strings[2];
        Date targetDate = DateTimeUtil.parseDate(date, DateTimeUtil.DF_YYYY_MM_DD);
        Date currentDate = DateTimeUtil.parseDate(DateTimeUtil.formatDateTime(DateTimeUtil.getCurrentDate(), DateTimeUtil.DF_YYYY_MM_DD), DateTimeUtil.DF_YYYY_MM_DD);
        //差值
        long mDvalue = targetDate.getTime() - currentDate.getTime();

        if (Math.abs(mDvalue) < DateTimeUtil.day) {
            return "今天";
        } else if (Math.abs(mDvalue) < 2 * DateTimeUtil.day) {
            return mDvalue>0?"明天":"昨天";
        } else if (Math.abs(mDvalue) < 3 * DateTimeUtil.day) {
            return mDvalue>0?"后天":"前天";
        }else{
            return DateTimeUtil.getIntervalTimeDay(new Date(System.currentTimeMillis()), new Date(time*1000));
        }
    }

    public interface IChangeStatusListener{
        void onChangeStatus(String rids,String status,int position);
        void onChangeTotalMoney(String total);
    }

    public void setIChangeStatusListener(IChangeStatusListener listener){
        this.mListener = listener;
    }
}

