package com.ypwl.xiaotouzi.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.bean.InvestByPlatformBidBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.ui.activity.SingleBidBackMoneyDetailActivity;
import com.ypwl.xiaotouzi.utils.DateTimeUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.ViewHolder;
import com.ypwl.xiaotouzi.view.stickylistheaders.StickyListHeadersAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * function : 所有回款，按平台分类数据适配器
 * <p/>
 * Created by tengtao on 2016/5/4.
 */
public class AllBackByPlatformAdapter extends BaseAdapter implements StickyListHeadersAdapter, SectionIndexer {

    private Context mContext;
    private String mType;
    private List<InvestByPlatformBidBean.ListEntity.DataEntity> mList = new ArrayList<>();
    private int[] mSectionIndices;//索引位置
    private String[] mSectionHeaders;//索引值

    public AllBackByPlatformAdapter(Context context){
       this.mContext = context;
    }

    /**加载数据*/
    public void loadData(List<InvestByPlatformBidBean.ListEntity> list, String type){
        if (list == null || list.size() == 0) {
            return;
        }
        this.mType = type;
        mList.clear();
        handleData(list);
        notifyDataSetChanged();
    }

    /** 改变箭头  */
    public void changeShowStatus(int position,boolean status){
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
                mList.get(i).setShowContent(false?false:(i>=position && i < endPosition ?status:false));
            }
        }
        UIUtil.postDelayed(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        },50);
    }

    /**处理数据*/
    private void handleData(List<InvestByPlatformBidBean.ListEntity> list){
        for (int i = 0; i < list.size(); i++) {
            InvestByPlatformBidBean.ListEntity listEntity = list.get(i);
            List<InvestByPlatformBidBean.ListEntity.DataEntity> dataEntities = listEntity.getData();
            for (int j = 0; j < dataEntities.size(); j++) {
                InvestByPlatformBidBean.ListEntity.DataEntity dataEntity = dataEntities.get(j);
                dataEntity.setP_money(listEntity.getMoney());
                dataEntity.setP_num(listEntity.getNum());
                dataEntity.setShowContent(false);
                mList.add(dataEntity);
            }
        }
        mSectionIndices = getSectionIndices(mList);
        mSectionHeaders = getSectionHeaders(mList);
    }

    /**
     * 获取索引
     */
    private int[] getSectionIndices(List<InvestByPlatformBidBean.ListEntity.DataEntity> list) {
        ArrayList<Integer> sectionIndices = new ArrayList<Integer>();
        int index = 0;
        sectionIndices.add(index);
        String name = list.get(0).getP_name();
        for(int i= 0;i<list.size();i++){
            String p_name = list.get(i).getP_name();
            if(!name.equalsIgnoreCase(p_name)){
                name = p_name;
                sectionIndices.add(i);
            }
        }
        int[] sections = new int[sectionIndices.size()];
        for (int i = 0; i < sectionIndices.size(); i++) {
            sections[i] = sectionIndices.get(i);
        }
        return sections;
    }

    /**
     * 处理获取header数据
     */
    private String[] getSectionHeaders(List<InvestByPlatformBidBean.ListEntity.DataEntity> list) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = View.inflate(mContext, R.layout.item_all_back_by_platform_content_layout,null);
        }
        TextView mTvBidName = ViewHolder.findViewById(convertView,R.id.tv_invest_by_platform_bid_name);
        TextView mTvBidMoney = ViewHolder.findViewById(convertView,R.id.tv_invest_by_platform_item_bid_money);
        TextView mTvEndDate = ViewHolder.findViewById(convertView,R.id.tv_invest_by_platform_bid_end_date);
        LinearLayout mItemView = ViewHolder.findViewById(convertView,R.id.item_all_back_by_platform_content);
        //设置数据
        InvestByPlatformBidBean.ListEntity.DataEntity dataEntity = mList.get(position);
        mTvBidName.setText(dataEntity.getProject_name());
        mTvBidMoney.setText(("0".equals(mType) ? "待回" : "已回") + dataEntity.getMoney());
        long millisTime = 0;
        try {
            millisTime = Long.parseLong(dataEntity.getReturn_time()) * 1000;
        } catch (Exception e) {e.printStackTrace();}
        String formatDateTime = DateTimeUtil.formatDateTime(millisTime, "yyyy-MM-dd");
        String[] strings = formatDateTime.split("-");
        mTvEndDate.setText(strings[0] + "/" + strings[1] + "/" + strings[2] + "结束");
        //点击事件
        mItemView.setTag(R.id.tv_invest_by_platform_number,dataEntity.getAid());
        mItemView.setOnClickListener(mClickListener);
        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = View.inflate(mContext,R.layout.item_all_back_by_platform_header_layout,null);
        }
        TextView mTvP_name = ViewHolder.findViewById(convertView,R.id.tv_invest_by_platform_pname);
        TextView mTvTotalMoney = ViewHolder.findViewById(convertView,R.id.tv_invest_by_platform_total_money);
        TextView mTvBidNum = ViewHolder.findViewById(convertView, R.id.tv_invest_by_platform_number);
        ImageView mIvArrowDown = ViewHolder.findViewById(convertView,R.id.iv_arrow_down);
        ImageView mIvArrowUp = ViewHolder.findViewById(convertView,R.id.iv_arrow_up);

        InvestByPlatformBidBean.ListEntity.DataEntity dataEntity = mList.get(position);
        mTvP_name.setText(dataEntity.getP_name());
        mTvTotalMoney.setText(("0".equals(mType) ? "待回" : "已回") + dataEntity.getP_money());
        mTvBidNum.setText(dataEntity.getP_num() + "笔");
        //箭头的显示
        boolean showContent = dataEntity.isShowContent();
        mIvArrowDown.setVisibility(showContent?View.GONE:View.VISIBLE);
        mIvArrowUp.setVisibility(showContent?View.VISIBLE:View.GONE);
        return convertView;
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

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String aid = (String) v.getTag(R.id.tv_invest_by_platform_number);
            Intent intent = new Intent(mContext, SingleBidBackMoneyDetailActivity.class);
            intent.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA,aid);
            intent.putExtra(Const.KEY_INTENT_JUMP_FROM_DATA,"全部回款");
            mContext.startActivity(intent);
            Const.JUMP_TO_SINGLE_BID_FROM_TYPE = 0;//标记从回款跳转到单个标的回款详情
        }
    };
}
