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
import com.ypwl.xiaotouzi.bean.AllBackItemBean;
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
 * function : 所有回款--日期分组list适配器
 * <p/>
 * Created by tengtao on 2016/3/24.
 */
public class AllBackByDateAdapter extends BaseAdapter implements StickyListHeadersAdapter, SectionIndexer {
    private IChangeStatusListener mListener;
    private Activity mActivity;
    private List<AllBackItemBean> mList = new ArrayList<>();
    private int divider;
    private int[] mSectionIndices;//索引位置
    private List<Integer> mDividerList = new ArrayList<>();
    private String[] mSectionHeaders;//索引值
    private final int NORMAL_TYPE = 1;
    private final int EMPTY_TYPE = 2;
    private String stotal ="";

    public AllBackByDateAdapter(Activity activity){
        this.mActivity = activity;
    }

    public void loadDate(List<AllBackItemBean> data, int divider, String stotal){
        this.stotal = stotal;
        this.divider = divider;
        mList.clear();
        mList.addAll(data);
        mSectionIndices = getSectionIndices(mList);
        mSectionHeaders = getSectionHeaders(mList);
        notifyDataSetChanged();
    }

    /**
     * 根据状态改变金额
     * @param position  改变回款状态的位置
     * @param perStatus 改变前的状态
     * @param status 改变后的状态
     */
    public void changeStatus(int position, String perStatus, String status){
        //根据状态更改显示
        if("0".equals(status)){
            String[] strings = handleDate(mList.get(position).getReturn_time());
            String date = strings[0]+"-"+strings[1]+"-"+strings[2];
            Date targetDate = DateTimeUtil.parseDate(date, DateTimeUtil.DF_YYYY_MM_DD);
            Date currentDate = DateTimeUtil.parseDate(DateTimeUtil.formatDateTime(DateTimeUtil.getCurrentDate(), DateTimeUtil.DF_YYYY_MM_DD), DateTimeUtil.DF_YYYY_MM_DD);
            //差值
            long mDvalue = targetDate.getTime() - currentDate.getTime();

            if (Math.abs(mDvalue) < DateTimeUtil.day || mDvalue <=0) {
                mList.get(position).setStatus("3");//今天以前设置我待确认
            }else{
                mList.get(position).setStatus(status);
            }
        }else{
            mList.get(position).setStatus(status);
        }
        //改变回款数额
        int start = 0;
        int end = 0;
        if (mSectionIndices[mSectionIndices.length - 1] <= position) {
            start = mSectionIndices[mSectionIndices.length - 1];
            end = mList.size();
        } else {
            for (int i = 0; i < mSectionIndices.length; i++) {
                if (mSectionIndices[i] <= position) {
                    start = mSectionIndices[i];
                }
                if (mSectionIndices[i] > position) {
                    end = mSectionIndices[i];
                    break;
                }
            }
        }
        DecimalFormat df = new DecimalFormat("###0.00");
        boolean hasBacked = "1".equalsIgnoreCase(status);//是否改为已回
        String total = mList.get(position).getTotal();//当期回款总额
        Double currTotal = Double.parseDouble(total.replace(",",""));
        for (int j = start; j < mList.size() && j < end; j++) {
            AllBackItemBean itemBean;
            itemBean = mList.get(j);
            String money = itemBean.getMoney();
            Double perMoney = Double.parseDouble(money.replace(",", ""));//当月额
            double result = perMoney;
            if(hasBacked){
                if(itemBean.getType().equalsIgnoreCase("1")){//已回款项
                    result = perMoney + currTotal;
                }else{
                    result = perMoney - currTotal;
                }
            }else if("1".equalsIgnoreCase(perStatus)){//从已回改其他状态
                if(itemBean.getType().equalsIgnoreCase("1")) {//已回款项
                    result = perMoney - currTotal;
                }else{
                    result = perMoney + currTotal;
                }
            }
            itemBean.setMoney(Util.markOperatorForMoney(df.format(result)));
        }
        Double perTotal = Double.parseDouble(stotal.replace(",", ""));//待收总额
        if(hasBacked) {
            stotal = Util.markOperatorForMoney(df.format(perTotal - currTotal));
        }else{
            stotal = Util.markOperatorForMoney(df.format(perTotal + currTotal));
        }
        notifyDataSetChanged();
    }

    public int[] getSectionIndicesResult(){
        return mSectionIndices;
    }

    /**
     * 获取索引
     */
    private int[] getSectionIndices(List<AllBackItemBean> list) {
        ArrayList<Integer> sectionIndices = new ArrayList<Integer>();
        String datetime = list.get(0).getDatetime();
        sectionIndices.add(0);
        for(int i=1;i<divider;i++){
            String time = list.get(i).getDatetime();
            if (!datetime.equalsIgnoreCase(time)) {
                datetime = time;
                sectionIndices.add(i);
            }
        }
        if(divider>0)
            sectionIndices.add(divider);//分界线
        //待回款项位置
        if(list.size() > divider+1){
            String sDateTime = list.get(divider+1).getDatetime();
            sectionIndices.add(divider+1);//待收第一个
            for(int j = divider+1;j < mList.size();j++){
                String time = list.get(j).getDatetime();
                if(!sDateTime.equalsIgnoreCase(time)){
                    sDateTime = time;
                    sectionIndices.add(j);
                }
            }
        }
        mDividerList.clear();
        int[] sections = new int[sectionIndices.size()];
        for (int i = 0; i < sectionIndices.size(); i++) {
            sections[i] = sectionIndices.get(i);
            if(sectionIndices.get(i)>=1)
                mDividerList.add(sectionIndices.get(i)-1);
        }
        return sections;
    }

    /**
     * 处理获取header数据
     */
    private String[] getSectionHeaders(List<AllBackItemBean> list) {
        String[] headers = new String[mSectionIndices.length];
        int index = 0;
        for (int i = 0; i < mSectionIndices.length; i++) {
            if(mSectionIndices[i] == divider){//分界线
                String header = getFormatDate(System.currentTimeMillis()/1000+"");
                headers[i] = header;
            }else{
                index = mSectionIndices[i];
                AllBackItemBean listEntity = list.get(index);
                String datetime = listEntity.getDatetime();
                String header = getFormatDate(datetime);
                headers[i] = header;
            }
        }
        return headers;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount()+2;
    }

    @Override
    public int getItemViewType(int position) {
        boolean isEmptyMonth = mList.get(position).getIsEmptyMonth();
        if(isEmptyMonth){
            return EMPTY_TYPE;
        } else{
            return NORMAL_TYPE;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(getItemViewType(position)== EMPTY_TYPE){
            if(convertView==null){
                convertView = new TextView(mActivity);
            }
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIUtil.dip2px(position==divider?1:8));
            convertView.setBackgroundColor(Color.parseColor("#E9E9E9"));
            convertView.setLayoutParams(params);
        }else{
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.item_recent_month_back_list, null);
            }
            View mDivider = ViewHolder.findViewById(convertView,R.id.item_divider);
            LinearLayout mItemView = ViewHolder.findViewById(convertView,R.id.layout_item_invest_by_date);
            TextView mTvDate = ViewHolder.findViewById(convertView,R.id.tv_month_list_date);
            TextView mTvPlatName = ViewHolder.findViewById(convertView, R.id.tv_month_list_plat_name);
            TextView mTvBidName = ViewHolder.findViewById(convertView, R.id.tv_month_list_bid_name);
            TextView mTvPercent = ViewHolder.findViewById(convertView, R.id.tv_month_list_percent);
            TextView mTvFriendTime = ViewHolder.findViewById(convertView, R.id.tv_month_list_friend_time);
            TextView mTvTotal = ViewHolder.findViewById(convertView, R.id.tv_month_list_all_money);
            TextView mTvOperation = ViewHolder.findViewById(convertView, R.id.tv_month_list_operation);
            RelativeLayout layoutOperation = ViewHolder.findViewById(convertView,R.id.layout_operation);

            layoutOperation.setTag(R.id.tv_month_list_operation, position);
            layoutOperation.setOnClickListener(mOnClickListener);

            mDivider.setVisibility(mDividerList.contains(position) && position != divider ? View.VISIBLE:View.GONE);
            AllBackItemBean data = mList.get(position);
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
            String status = data.getStatus();
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
            mItemView.setOnClickListener(mOnClickListener);
            mItemView.setTag(R.id.layout_item_invest_by_date,position);
        }
        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mActivity, R.layout.layout_item_recent_back_money_header, null);
        }
        LinearLayout dividerView = ViewHolder.findViewById(convertView, R.id.layout_divider_view);
        RelativeLayout headerView = ViewHolder.findViewById(convertView, R.id.layout_header_view);
        headerView.setVisibility(position == divider?View.GONE:View.VISIBLE);
        dividerView.setVisibility(position == divider?View.VISIBLE:View.GONE);
        if (position == divider) {
            TextView mTodayDate = ViewHolder.findViewById(convertView, R.id.tv_all_back_today_date);
            TextView mDSMoney = ViewHolder.findViewById(convertView, R.id.tv_all_back_collection);
            mDSMoney.setText(stotal);
            String[] strings = handleDate(System.currentTimeMillis() / 1000 + "");
            mTodayDate.setText("今天" + strings[0] + "年" + strings[1] + "月" + strings[2] + "日");
        } else if(position < mList.size()) {
            TextView mTvDate = ViewHolder.findViewById(convertView, R.id.tv_item_recent_back_money_date);
            TextView mTvTotalMoney = ViewHolder.findViewById(convertView, R.id.tv_item_recent_back_month_money);
            String datetime = mList.get(position).getDatetime();
            String money = mList.get(position).getMoney();
            mTvDate.setText(getFormatDate(datetime));
            String type = mList.get(position).getType();
            mTvTotalMoney.setText(("1".equals(type) ? "已回" : "待回") + money);
        }
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
                case R.id.layout_operation://操作按钮
                    int position = (int) v.getTag(R.id.tv_month_list_operation);
                    AllBackItemBean allBackItemBean = mList.get(position);
                    String status = allBackItemBean.getStatus();
                    String is_auto = allBackItemBean.getIs_auto();
                    if("0".equalsIgnoreCase(is_auto)){
                        mListener.onChangeStatus(mList.get(position).getRid(),status,position);
                    }else{
                        UIUtil.showToastShort("自动记账平台，不能更改状态");
                    }
                    break;
                case R.id.layout_item_invest_by_date:
                    int index = (int) v.getTag(R.id.layout_item_invest_by_date);
                    String aid = mList.get(index).getAid();
                    Intent intent = new Intent(mActivity, SingleBidBackMoneyDetailActivity.class);
                    intent.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA,aid);
                    intent.putExtra(Const.KEY_INTENT_JUMP_FROM_DATA,"全部回款");
                    Const.JUMP_TO_SINGLE_BID_FROM_TYPE = 0;//标记从回款跳转到单个标的回款详情
                    mActivity.startActivity(intent);
                    break;
            }
        }
    };

    public interface IChangeStatusListener{
        void onChangeStatus(String rids,String status,int position);
    }

    public void setIChangeStatusListener(IChangeStatusListener listener){
        this.mListener = listener;
    }

    private String getFormatDate(String date) {
        String[] split = handleDate(date);
        String substring = split[0].substring(2, 4);
        String header = substring + "年" + split[1] + "月";
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
            return (Math.abs(mDvalue) / DateTimeUtil.day) + (mDvalue>0?"天后":"天前");
        }
    }
}
