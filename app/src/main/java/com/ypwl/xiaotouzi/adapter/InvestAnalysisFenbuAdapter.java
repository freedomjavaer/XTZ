package com.ypwl.xiaotouzi.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.bean.InvestAnalysisByFenbuBean;
import com.ypwl.xiaotouzi.bean.InvestFenBuPlatformItemBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.ui.activity.LlcBidDetailActivity;
import com.ypwl.xiaotouzi.ui.activity.MyInvestPlatformDetailActivity;
import com.ypwl.xiaotouzi.utils.ChartUtils;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.view.CustomRoundImageView;
import com.ypwl.xiaotouzi.view.stickylistheaders.StickyListHeadersAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * function : 我的投资：投资分布适配器
 * <p/>
 * Created by tengtao on 2016/4/11.
 */
public class InvestAnalysisFenbuAdapter extends BaseAdapter implements StickyListHeadersAdapter, SectionIndexer {
    //年化率
    private String[] annualRates = new String[]{"0＜年化率≤5%","5%＜年化率≤10%","10%＜年化率≤15%",
            "15%＜年化率≤20%","20%＜年化率≤25%","25%＜年化率"};
    //周期
    private String[] cycles = new String[]{"0天＜周期≤27天","27天＜周期≤93天","93天＜周期≤186天",
            "186天＜周期≤372天","372天＜周期"};
    private Activity mActivity;
    private int mCurrentFragmenType;
    private int[] mSectionIndices;//索引位置
    private String[] mSectionHeaders;//索引值
    private List<Integer> mDividerList = new ArrayList<>();
    private List<InvestFenBuPlatformItemBean> mList = new ArrayList<>();
    private int selectPostion = -1;
    private int ITEM_TYPE_EMPTY = 1;
    private int ITEM_TYPE_REAL = 2;

    public InvestAnalysisFenbuAdapter(Activity activity,int currentFragmenType){
        this.mActivity = activity;
        this.mCurrentFragmenType = currentFragmenType;
    }

    public void loadData(List<InvestAnalysisByFenbuBean.ListBean> list){
        if(list!=null && list.size()>0){
            mSectionIndices = getSectionIndices(list);
            mSectionHeaders = getSectionHeaders(list);
            mList.clear();
            handleData(list);
            notifyDataSetChanged();
        }
    }

    private void handleData(List<InvestAnalysisByFenbuBean.ListBean> list){
        for(int i=0;i<list.size();i++){
            InvestAnalysisByFenbuBean.ListBean listBean = list.get(i);
            if(listBean.getNumber().equalsIgnoreCase("0")){//笔数为0
                InvestFenBuPlatformItemBean itemBean = new InvestFenBuPlatformItemBean();
                itemBean.setPid(listBean.getPid());
                itemBean.setMoney(listBean.getMoney());
                itemBean.setP_name(listBean.getP_name());
                itemBean.setPercent(listBean.getPercent());
                itemBean.setNumber(listBean.getNumber());
                itemBean.setType(listBean.getType());
                itemBean.setReturn_name(listBean.getReturn_name());

                itemBean.setAid("");
                itemBean.setBid_money("");
                itemBean.setProject_name("");
                itemBean.setData_p_name("");
                itemBean.setRate("");
                itemBean.setTime_limit("");
                itemBean.setTime_type("");
                itemBean.setShowStatus(0);
                mList.add(itemBean);
            }else{
                List<InvestAnalysisByFenbuBean.ListBean.DataBean> data = listBean.getData();
                for(int j=0;j < data.size();j++){
                    InvestAnalysisByFenbuBean.ListBean.DataBean dataBean = data.get(j);
                    InvestFenBuPlatformItemBean itemBean = new InvestFenBuPlatformItemBean();
                    itemBean.setPid(listBean.getPid());
                    itemBean.setMoney(listBean.getMoney());
                    itemBean.setP_name(listBean.getP_name());
                    itemBean.setPercent(listBean.getPercent());
                    itemBean.setNumber(listBean.getNumber());
                    itemBean.setType(listBean.getType());
                    itemBean.setReturn_name(listBean.getReturn_name());

                    itemBean.setAid(dataBean.getAid());
                    itemBean.setBid_money(dataBean.getMoney());
                    itemBean.setProject_name(dataBean.getProject_name());
                    itemBean.setData_p_name(dataBean.getP_name());
                    itemBean.setRate(dataBean.getRate());
                    itemBean.setTime_limit(dataBean.getTime_limit());
                    itemBean.setTime_type(dataBean.getTime_type());
                    itemBean.setShowStatus(0);
                    mList.add(itemBean);
                }
            }
        }
    }

    /** 改变箭头  */
    public void changeShowStatus(int position,int status){
        if(mSectionIndices==null){return;}
        int endPosition;
        int sectionForPosition = getSectionForPosition(position);
        if(sectionForPosition < mSectionIndices.length - 1) {
            endPosition = getPositionForSection(sectionForPosition + 1);
        }else{
            endPosition = mList.size();
        }
        if(mList!=null&& mList.size()>0){
            for(int i=0;i<mList.size();i++){
                mList.get(i).setShowStatus(status==0?0:(i>=position && i < endPosition ?status:0));
            }
        }
        UIUtil.postDelayed(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        },50);
    }

    /**
     * 获取索引
     */
    private int[] getSectionIndices(List<InvestAnalysisByFenbuBean.ListBean> list) {
        ArrayList<Integer> sectionIndices = new ArrayList<Integer>();
        int index = 0;
        sectionIndices.add(index);
        for(int i= 0;i<list.size()-1;i++){
            List<InvestAnalysisByFenbuBean.ListBean.DataBean> data = list.get(i).getData();
            if(data.size()==0){
                index += 1;
            }else{
                index += list.get(i).getData().size();
            }
            sectionIndices.add(index);
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
    private String[] getSectionHeaders(List<InvestAnalysisByFenbuBean.ListBean> list) {
        String[] headers = new String[mSectionIndices.length];
        for (int i = 0; i < mSectionIndices.length; i++) {
            String p_name = list.get(i).getP_name();
            headers[i] = p_name;
        }
        return headers;
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
    public int getViewTypeCount() {
        return super.getViewTypeCount()+2;
    }

    @Override
    public int getItemViewType(int position) {
        if("0".equalsIgnoreCase(mList.get(position).getNumber())){
            return ITEM_TYPE_EMPTY;
        }
        return ITEM_TYPE_REAL;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(getItemViewType(position)==ITEM_TYPE_REAL){
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mActivity, R.layout.layout_invest_analysis_fen_bu_platform_item_content, null);
                holder.item = (LinearLayout) convertView.findViewById(R.id.invest_fenbu_item_content_item);
                holder.mTvType = (TextView) convertView.findViewById(R.id.tv_invest_analysis_bid_type);
                holder.mTvBidName = (TextView) convertView.findViewById(R.id.tv_invest_analysis_bid_name);
                holder.mTvP_Name = (TextView) convertView.findViewById(R.id.tv_invest_analysis_p_name);
                holder.mTvBidMoney = (TextView) convertView.findViewById(R.id.tv_invest_analysis_bid_money);
                holder.mTvBidMoney2 = (TextView) convertView.findViewById(R.id.tv_invest_analysis_bid_money2);
                holder.mTvBidName2 = (TextView) convertView.findViewById(R.id.tv_invest_analysis_bid_name2);
                holder.mDivider = convertView.findViewById(R.id.divider_line);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            InvestFenBuPlatformItemBean itemBean = mList.get(position);
            holder.mTvBidName.setVisibility(mCurrentFragmenType == 0 ? View.VISIBLE : View.GONE);
            holder.mTvBidMoney.setVisibility(mCurrentFragmenType == 0 ? View.VISIBLE : View.GONE);
            switch (mCurrentFragmenType) {
                case 0://平台
                    holder.mTvType.setVisibility(View.GONE);
                    holder.mTvBidName2.setVisibility(View.GONE);
                    holder.mTvP_Name.setVisibility(View.GONE);
                    holder.mTvBidMoney2.setVisibility(View.GONE);
                    holder.mTvBidName.setText(itemBean.getProject_name());
                    holder.mTvBidMoney.setText("投资" + itemBean.getBid_money());
                    break;
                case 1://周期
                    holder.mTvType.setVisibility(View.VISIBLE);
                    holder.mTvBidName2.setVisibility(View.VISIBLE);
                    holder.mTvType.setText(itemBean.getTime_limit() + (itemBean.getTime_type().equalsIgnoreCase("1") ? "个月" : "天"));
                    holder.mTvBidName2.setText(itemBean.getProject_name());
                    holder.mTvP_Name.setText(itemBean.getData_p_name());
                    holder.mTvBidMoney2.setText(itemBean.getBid_money());
                    break;
                case 2://年化率
                    holder.mTvType.setVisibility(View.VISIBLE);
                    holder.mTvBidName2.setVisibility(View.VISIBLE);
                    holder.mTvType.setText(itemBean.getRate() + "%");
                    holder.mTvBidName2.setText(itemBean.getProject_name());
                    holder.mTvP_Name.setText(itemBean.getData_p_name());
                    holder.mTvBidMoney2.setText(itemBean.getBid_money());
                    break;
                case 3://还款方式
                    holder.mTvType.setVisibility(View.INVISIBLE);
                    holder.mTvBidName2.setVisibility(View.VISIBLE);
                    holder.mTvBidName2.setText(itemBean.getProject_name());
                    holder.mTvP_Name.setText(itemBean.getData_p_name());
                    holder.mTvBidMoney2.setText(itemBean.getBid_money());
                    break;
            }

            holder.mDivider.setVisibility(mDividerList.contains(position) ? View.GONE : View.VISIBLE);

            holder.item.setTag(R.id.invest_fenbu_item_content_item, itemBean.getAid());
            holder.item.setOnClickListener(mOnClickListener);
        }else{
            if(convertView==null){
                convertView = new TextView(mActivity);
            }
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIUtil.dip2px(0));
            convertView.setLayoutParams(params);
        }
        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if(convertView==null){
            holder = new HeaderViewHolder();
            convertView = View.inflate(mActivity, R.layout.layout_invest_analysis_fen_bu_platform_item_header,null);
            holder.header = (LinearLayout) convertView.findViewById(R.id.layout_item_invest_analysis_fenbu_header);
            holder.mPlatformColor = (CustomRoundImageView) convertView.findViewById(R.id.iv_platform_color);
            holder.mTvP_name = (TextView) convertView.findViewById(R.id.tv_invest_p_name);
            holder.mTvPercent = (TextView) convertView.findViewById(R.id.tv_invest_percent);
            holder.mTvTotalMoney = (TextView) convertView.findViewById(R.id.tv_invest_total_money);
            holder.mTvNumber = (TextView) convertView.findViewById(R.id.tv_invest_number);
            holder.mIvArrowDown = (ImageView) convertView.findViewById(R.id.iv_invest_analysis_arrow_down);
            holder.mIvArrowUp = (ImageView) convertView.findViewById(R.id.iv_invest_analysis_arrow_up);
            convertView.setTag(holder);
        }else{
            holder = (HeaderViewHolder) convertView.getTag();
        }
        if(position==selectPostion){
            holder.header.setBackgroundColor(mActivity.getResources().getColor(R.color.f));
            UIUtil.postDelayed(mRunnable,1000);
        }else{
            holder.header.setBackground(mActivity.getResources().getDrawable(R.drawable.selector_color_bg_gray_white));
        }

        //数据设置
        InvestFenBuPlatformItemBean itemBean = mList.get(position);
        int sectionForPosition = getSectionForPosition(position);
        holder.mPlatformColor.setRectAdius(UIUtil.dip2px(7));
        holder.mPlatformColor.setBackgroundColor(Color.parseColor(ChartUtils.pieColors[sectionForPosition]));
        holder.mTvPercent.setText(itemBean.getPercent()+"%");
        holder.mTvTotalMoney.setText("投资"+itemBean.getMoney());
        holder.mTvNumber.setText(itemBean.getNumber()+"笔");
        switch (mCurrentFragmenType){
            case 0://平台
                holder.mTvP_name.setText(itemBean.getP_name());
                holder.mTvP_name.setTextColor(Color.parseColor("#0078FF"));
                break;
            case 1://周期
                String type = itemBean.getType();
                int i = 0;
                try{
                    i = Integer.parseInt(type);
                }catch (Exception e){e.printStackTrace();}
                holder.mTvP_name.setText(cycles[i-1]);
                holder.mTvP_name.setTextColor(Color.BLACK);
                break;
            case 2://年化率
                String type1 = itemBean.getType();
                int j = 0;
                try{
                    j = Integer.parseInt(type1);
                }catch (Exception e){e.printStackTrace();}
                holder.mTvP_name.setText(annualRates[j-1]);
                holder.mTvP_name.setTextColor(Color.BLACK);
                break;
            case 3://还款方式
                holder.mTvP_name.setText(itemBean.getReturn_name());
                holder.mTvP_name.setTextColor(Color.BLACK);
                break;
        }

        if(mCurrentFragmenType==0){
            holder.mTvP_name.setTag(R.id.tv_invest_p_name,itemBean.getPid());
            holder.mTvP_name.setOnClickListener(mOnClickListener);
        }

        //箭头显示
        int showStatus = itemBean.getShowStatus();
        holder.mIvArrowUp.setVisibility(showStatus==1?View.VISIBLE:View.GONE);
        holder.mIvArrowDown.setVisibility(showStatus==0?View.VISIBLE:View.GONE);
        return convertView;
    }

    /** 选中条目 */
    public void selectItem(int position){
        selectPostion = position;
        notifyDataSetChanged();
    }

    /** 取消选择状态，底色回复 */
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            selectPostion = -1;
            notifyDataSetChanged();
        }
    };


    @Override
    public Object[] getSections() {
        return mSectionHeaders;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        if(mSectionIndices==null){
            return -1;
        }
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
        if(mSectionIndices==null){
            return -1;
        }
        for (int i = 0; i < mSectionIndices.length; i++) {
            if (position < mSectionIndices[i]) {
                return i - 1;
            }
        }
        return mSectionIndices.length - 1;
    }

    @Override
    public long getHeaderId(int position) {
        if(mSectionIndices==null){
            return -1;
        }
        for (int i = 0; i < mSectionIndices.length; i++) {
            if (position < mSectionIndices[i]) {
                return i - 1;
            }
        }
        return mSectionIndices.length - 1;
    }

    private class HeaderViewHolder {
        LinearLayout header;
        CustomRoundImageView mPlatformColor;
        TextView mTvP_name;
        TextView mTvPercent;
        TextView mTvTotalMoney;
        TextView mTvNumber;
        ImageView mIvArrowDown,mIvArrowUp;
    }

    private class ViewHolder {
        LinearLayout item;
        TextView mTvType;
        TextView mTvBidName;
        TextView mTvP_Name;
        TextView mTvBidMoney;
        TextView mTvBidMoney2;
        TextView mTvBidName2;
        View mDivider;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.invest_fenbu_item_content_item://跳转到标的详情
                    String aid = (String) v.getTag(R.id.invest_fenbu_item_content_item);
                    Intent intent = new Intent(mActivity, LlcBidDetailActivity.class);
                    intent.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA,aid);
                    intent.putExtra(Const.KEY_INTENT_JUMP_FROM_DATA,"我的分析");
                    mActivity.startActivity(intent);
                    break;
                case R.id.tv_invest_p_name://平台详情
                    String pid = (String) v.getTag(R.id.tv_invest_p_name);
                    Intent intent1 = new Intent(mActivity, MyInvestPlatformDetailActivity.class);
                    intent1.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA,pid);
                    intent1.putExtra(Const.KEY_INTENT_JUMP_FROM_DATA,"我的分析");
                    intent1.putExtra(MyInvestPlatformDetailActivity.IS_AUTO,2);//从投资分析进入的，全部为不能同步
                    mActivity.startActivity(intent1);
                    break;
            }
        }
    };
}
