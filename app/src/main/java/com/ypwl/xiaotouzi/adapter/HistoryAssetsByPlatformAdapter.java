package com.ypwl.xiaotouzi.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nineoldandroids.animation.ObjectAnimator;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.bean.HistoryAssetsByPlatformProtocolBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.ui.activity.LlcBidDetailActivity;
import com.ypwl.xiaotouzi.ui.activity.MyInvestPlatformDetailActivity;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.ViewHolder;
import com.ypwl.xiaotouzi.view.FlowLayout;
import com.ypwl.xiaotouzi.view.InvestHistoryItemView;
import com.ypwl.xiaotouzi.view.expandablelayout.ExpandableLayoutItem;

import java.util.ArrayList;
import java.util.List;

/**
 * function :资产适配器 （例外一种形式显示）
 * Created by llc on 2016/3/24 16:44
 * Email：licailuo@qq.com
 */
public class HistoryAssetsByPlatformAdapter extends BaseAdapter {
    private Activity mActivity;
    private List<HistoryAssetsByPlatformProtocolBean.ListEntity> mList = new ArrayList<>();
    private List<Integer> numList = new ArrayList<>();


    public HistoryAssetsByPlatformAdapter(Activity activity) {

        this.mActivity = activity;
        numList.clear();
    }

    public void LoadData(List<HistoryAssetsByPlatformProtocolBean.ListEntity> list) {
        mList.clear();
        mList.addAll(list);
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


    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = mList.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mActivity, R.layout.item_invest_history_by_platform, null);
        }
        ExpandableLayoutItem layoutItem = ViewHolder.findViewById(convertView, R.id.invest_history_by_platform_item);
        //header部分
        TextView mTvP_letter = ViewHolder.findViewById(convertView, R.id.tv_item_invest_history_p_letter);
        TextView mTvP_name = ViewHolder.findViewById(convertView, R.id.tv_invest_history_p_name);
        TextView mTvShouyi = ViewHolder.findViewById(convertView, R.id.tv_invest_history_shouyi);
        TextView mTvLilv = ViewHolder.findViewById(convertView, R.id.tv_invest_history_lilv);
        ImageView mIvArrow = ViewHolder.findViewById(convertView, R.id.iv_invest_history_arrow);
        //内容部分
        FlowLayout mContentLayout = ViewHolder.findViewById(convertView, R.id.fl_invest_history_item_content);
        mContentLayout.removeAllViews();
        mContentLayout.setSpace(0, UIUtil.dip2px(1));
        //加载header
        HistoryAssetsByPlatformProtocolBean.ListEntity listEntity = mList.get(position);
        String p_name = listEntity.getP_name();
        String pf_name = listEntity.getProfit();
        String letter = listEntity.getSortLetters();
        mTvP_letter.setText(letter);
        if (position > 0) {
            String perLetter = mList.get(position - 1).getSortLetters();
            mTvP_letter.setVisibility(letter.equalsIgnoreCase(perLetter) ? View.GONE : View.VISIBLE);
        }
        mTvP_letter.setOnClickListener(null);
        mTvP_name.setText(p_name);
        mTvP_name.setTag(R.id.tv_invest_history_p_name, listEntity.getData().get(0).getPid());
        mTvP_name.setTag(R.id.tv_invest_history_shouyi, listEntity.getData().get(0).getIs_auto());
        mTvP_name.setOnClickListener(mTextOnClickListener);
        mTvShouyi.setText("收益" + pf_name);
        mTvLilv.setText("年化率" + listEntity.getRate() + "%");
        //加载内容
        List<HistoryAssetsByPlatformProtocolBean.ListEntity.DataEntity> datas = listEntity.getData();
        for (int i = 0; i < datas.size(); i++) {
            InvestHistoryItemView view = new InvestHistoryItemView(mActivity);
            view.refreshData(datas.get(i));
            view.setTag(R.id.tv_invest_history_lilv, datas.get(i).getAid());
            view.setOnClickListener(mOnClickListener);
            mContentLayout.addView(view);
        }
        //箭头动画
        layoutItem.setTag(R.id.iv_arrow_down, mIvArrow);
        layoutItem.setTag(R.id.tv_invest_by_platform_number, position);
        layoutItem.setOnToggleItemClickListener(mToggleListener);
        return convertView;
    }

    private View.OnClickListener mTextOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            String pid = (String) v.getTag(R.id.tv_invest_history_p_name);
            int is_auto = (int) v.getTag(R.id.tv_invest_history_shouyi);
            Intent intent = new Intent(mActivity, MyInvestPlatformDetailActivity.class);
            intent.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA, pid);
            intent.putExtra(MyInvestPlatformDetailActivity.IS_AUTO, is_auto);
            intent.putExtra(Const.KEY_INTENT_JUMP_FROM_DATA, "历史资产");
            mActivity.startActivity(intent);
        }
    };


    private View.OnClickListener mOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            String aid = (String) v.getTag(R.id.tv_invest_history_lilv);
            Intent intent = new Intent(mActivity, LlcBidDetailActivity.class);
            intent.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA, aid);
            intent.putExtra(Const.KEY_INTENT_JUMP_FROM_DATA, "历史资产");
            mActivity.startActivity(intent);
        }
    };

    private ExpandableLayoutItem.OnToggleItemClickListener mToggleListener = new ExpandableLayoutItem.OnToggleItemClickListener() {

        @Override
        public void onToggleItem(ExpandableLayoutItem itemView, int position, boolean isShow, boolean isAll) {
            ImageView mIvArrow = (ImageView) itemView.getTag(R.id.iv_arrow_down);
            int index = (int) itemView.getTag(R.id.tv_invest_by_platform_number) + 1;
            if (isShow) {
                numList.add(position);
                if (index == position)
                    ObjectAnimator.ofFloat(mIvArrow, "rotation", 0, 180).start();//打开
            } else {
                if (isAll) {
                    if (numList.size() > 0 && numList.get(0) == index) {
                        ObjectAnimator.ofFloat(mIvArrow, "rotation", -180, 0).start();//关闭
                        numList.clear();
                    }
                } else {
                    ObjectAnimator.ofFloat(mIvArrow, "rotation", -180, 0).start();//关闭
                    numList.clear();
                }
            }
        }
    };


}
