package com.ypwl.xiaotouzi.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.bean.InvestByPlatformBidBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.ui.activity.SingleBidBackMoneyDetailActivity;
import com.ypwl.xiaotouzi.utils.DateTimeUtil;
import com.ypwl.xiaotouzi.utils.LogUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * function : 所有回款按平台分类数据适配器,listview实现方式
 * <p/>
 * Created by tengtao on 2016/5/4.
 */
public class AllBackByPlatformRecyclerAdapter extends BaseAdapter{
    private Context mContext;
    private String mType;
    private List<InvestByPlatformBidBean.ListEntity.DataEntity> mList = new ArrayList<>();
    private Map<Integer,View> clickMaps = new HashMap<>();//已打开view
    private int[] mSectionIndices;//显示平台信息的索引位置
    private List<Integer> preClick = new ArrayList<>();
    public AllBackByPlatformRecyclerAdapter(Context context){
        this.mContext = context;
    }

    /**加载数据*/
    public void loadData(List<InvestByPlatformBidBean.ListEntity> list, String type){
        if (list == null || list.size() == 0) {return;}
        mList.clear();
        clickMaps.clear();
        preClick.clear();
        this.mType = type;
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
        mSectionIndices = getHeaderIndices(mList);
        notifyDataSetChanged();
    }

    /**获取显示平台信息的位置*/
    private int[] getHeaderIndices(List<InvestByPlatformBidBean.ListEntity.DataEntity> list){
        ArrayList<Integer> sectionIndices = new ArrayList<Integer>();
        String p_name = list.get(0).getP_name();
        sectionIndices.add(0);
        for(int i=1;i<list.size();i++){
            String name = list.get(i).getP_name();
            if (!p_name.equalsIgnoreCase(name)) {
                p_name = name;
                sectionIndices.add(i);
            }
        }
        int[] sections = new int[sectionIndices.size()];
        for (int i = 0; i < sectionIndices.size(); i++) {
            sections[i] = sectionIndices.get(i);
        }
        return sections;
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
            convertView = View.inflate(mContext,R.layout.item_all_back_by_platform_layout,null);
        }
        LinearLayout mItemHeader = ViewHolder.findViewById(convertView,R.id.layout_item_invest_bid_header);
        LinearLayout mItemContent = ViewHolder.findViewById(convertView,R.id.item_all_back_by_platform_content);
        ImageView mIvArrowDown = ViewHolder.findViewById(convertView,R.id.iv_arrow_down);
        ImageView mIvArrowUp = ViewHolder.findViewById(convertView,R.id.iv_arrow_up);
        TextView mTvP_name = ViewHolder.findViewById(convertView,R.id.tv_invest_by_platform_pname);
        TextView mTvTotalMoney = ViewHolder.findViewById(convertView,R.id.tv_invest_by_platform_total_money);
        TextView mTvNumber = ViewHolder.findViewById(convertView,R.id.tv_invest_by_platform_number);
        TextView mTvBidName = ViewHolder.findViewById(convertView,R.id.tv_invest_by_platform_bid_name);
        TextView mTvItemMoney = ViewHolder.findViewById(convertView,R.id.tv_invest_by_platform_item_bid_money);
        TextView mTvFinishDate = ViewHolder.findViewById(convertView,R.id.tv_invest_by_platform_bid_end_date);

        InvestByPlatformBidBean.ListEntity.DataEntity bean = mList.get(position);
        /**设置平台数据*/
        String p_name = bean.getP_name();
        mTvP_name.setText(p_name);
        mTvTotalMoney.setText(("0".equals(mType) ? "待回" : "已回") + bean.getP_money());
        mTvNumber.setText(bean.getP_num() + "笔");
        /**具体每一个标数据*/
        mTvBidName.setText(bean.getProject_name());
        mTvItemMoney.setText(("0".equals(mType) ? "待回" : "已回") + bean.getMoney());
        long millisTime = 0;
        try {
            millisTime = Long.parseLong(bean.getReturn_time()) * 1000;
        } catch (Exception e) {e.printStackTrace();}
        String formatDateTime = DateTimeUtil.formatDateTime(millisTime, "yyyy-MM-dd");
        String[] strings = formatDateTime.split("-");
        mTvFinishDate.setText(strings[0] + "/" + strings[1] + "/" + strings[2] + "结束");
        /**平台部分相同不显示*/
        if(position>0){
            InvestByPlatformBidBean.ListEntity.DataEntity dataEntity = mList.get(position - 1);
            String preP_name = dataEntity.getP_name();//前一个平台名称
            mItemHeader.setVisibility(p_name.equalsIgnoreCase(preP_name)?View.GONE:View.VISIBLE);
        }
        if(!clickMaps.containsKey(position)){
            clickMaps.put(position,mItemContent);
        }
        mItemContent.setVisibility(bean.isShowContent()?View.VISIBLE:View.GONE);
        mItemContent.setTag(R.id.tv_invest_by_platform_number,bean.getAid());
        mItemContent.setOnClickListener(mOnClickListener);
        mIvArrowDown.setVisibility(bean.isShowContent()?View.GONE:View.VISIBLE);
        mIvArrowUp.setVisibility(bean.isShowContent()?View.VISIBLE:View.GONE);

        mItemHeader.setTag(R.id.layout_item_invest_bid_header,position);
        mItemHeader.setOnClickListener(mOnClickListener);
        return convertView;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.layout_item_invest_bid_header://点击头部
                    clickHeader(v);
                    break;
                case R.id.item_all_back_by_platform_content://具体条目
                    clickContent(v);
                    break;
            }
        }
    };

    /**点击头部，展开或者关闭*/
    private void clickHeader(View v){
        int position = (int) v.getTag(R.id.layout_item_invest_bid_header);
        LogUtil.e("AllBackByPlatformRecyclerAdapter","点击："+position);
        int end = getClickRangeEnd(position);
        LogUtil.e("AllBackByPlatformRecyclerAdapter","结束位置："+end);
        if(!preClick.contains(position) && preClick.size()>0){//点击的不是原来打开的--->关闭原来打开的
            int start1 = preClick.get(0);
            int end1 = getClickRangeEnd(start1);
            //关闭原来的
            for(Map.Entry<Integer,View> entry:clickMaps.entrySet()){
                Integer key = entry.getKey();
                View view = entry.getValue();
                InvestByPlatformBidBean.ListEntity.DataEntity dataEntity = mList.get(key);
                if(key>=start1 && key<=end1) {
                    close(view);
                    dataEntity.setShowContent(false);
                    LogUtil.e("AllBackByPlatformRecyclerAdapter","关闭原来的："+key);
                }
            }
            preClick.clear();
            //打开新点的
            for(Map.Entry<Integer,View> entry:clickMaps.entrySet()){
                Integer key = entry.getKey();
                View view = entry.getValue();
                if(key>=position && key<=end){
                    InvestByPlatformBidBean.ListEntity.DataEntity dataEntity = mList.get(key);
                    open(view);
                    LogUtil.e("AllBackByPlatformRecyclerAdapter","打开新点击的："+key);
                    dataEntity.setShowContent(true);
                }
            }
            preClick.add(position);
        }else{//点击的是原来的打开的
            for(Map.Entry<Integer,View> entry:clickMaps.entrySet()){
                Integer key = entry.getKey();
                View view = entry.getValue();
                if(key>=position && key<=end){
                    InvestByPlatformBidBean.ListEntity.DataEntity dataEntity = mList.get(key);
                    boolean showContent = dataEntity.isShowContent();
                    if(showContent){//打开--->关闭
                        close(view);
                        LogUtil.e("AllBackByPlatformRecyclerAdapter","关闭原点击的："+key);
                    }else{//关闭-->打开
                        open(view);
                        LogUtil.e("AllBackByPlatformRecyclerAdapter","打开原点击的："+key);
                    }
                    dataEntity.setShowContent(!showContent);
                }
            }
            if(preClick.size()>0){
                preClick.clear();
            }else{
                preClick.add(position);
            }
        }
        UIUtil.postDelayed(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        },250);
    }

    /**点击跳转*/
    private void clickContent(View v){
        String aid = (String) v.getTag(R.id.tv_invest_by_platform_number);
        Intent intent = new Intent(mContext, SingleBidBackMoneyDetailActivity.class);
        intent.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA,aid);
        intent.putExtra(Const.KEY_INTENT_JUMP_FROM_DATA,"全部回款");
        mContext.startActivity(intent);
        Const.JUMP_TO_SINGLE_BID_FROM_TYPE = 0;//标记从回款跳转到单个标的回款详情
    }

    /** 展开 */
    private void open(final View v) {
        //先测量获取高度
        v.measure(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();
        //设置高度为0，显示出来
        v.getLayoutParams().height = 0;
        v.setVisibility(View.VISIBLE);
        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = (interpolatedTime == 1) ? RelativeLayout.LayoutParams.WRAP_CONTENT : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }
            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        animation.setDuration(200);
        v.startAnimation(animation);
    }

    /** 关闭 */
    private void close(final View v) {
        final int initialHeight = v.getMeasuredHeight();
        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1) {
                    v.setVisibility(View.GONE);//关闭后隐藏
                } else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }
            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        animation.setDuration(200);
        v.startAnimation(animation);
    }

    /**获取点击的区域结尾位置*/
    private int getClickRangeEnd(int position){
        if(position==mSectionIndices[mSectionIndices.length-1]){
            return mList.size()-1;
        }else{
            for (int i = 0; i < mSectionIndices.length; i++) {
                if (position < mSectionIndices[i]) {
                   return mSectionIndices[i] - 1;
                }
            }
        }
        return position;
    }
}
