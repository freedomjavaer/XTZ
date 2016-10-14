package com.ypwl.xiaotouzi.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.bean.InvestHistoryBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.ui.activity.LlcBidDetailActivity;
import com.ypwl.xiaotouzi.utils.DateTimeUtil;
import com.ypwl.xiaotouzi.view.swipelistview.FrontLayout;
import com.ypwl.xiaotouzi.view.swipelistview.SwipeLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * function :历史资产适配器（默认界面）
 * Created by llc on 2016/3/24 15:13
 * Email：licailuo@qq.com
 */
public class AssetHistoryAdapter extends BaseAdapter {
    private IDeleteBidListener mDeleteBidListener;
    private Context context;
    private Set<SwipeLayout> mUnClosedLayouts = new HashSet<>();
    private List<InvestHistoryBean.ListEntity> mDataList = new ArrayList<>();

    public AssetHistoryAdapter(Context context) {
        this.context = context;
    }

    /**
     * 刷新适配器数据列表
     *
     * @param list 待添加的数据列表
     */
    public void refreshDataList(List<InvestHistoryBean.ListEntity> list) {
            mDataList.clear();
            mDataList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_invest_history_listview, null);
            convertView.setTag(viewHolder);
            viewHolder.mItemProjectName = (TextView) convertView.findViewById(R.id.item_project_name);
            viewHolder.mItemPName = (TextView) convertView.findViewById(R.id.item_p_name);
            viewHolder.mItemReturnTime = (TextView) convertView.findViewById(R.id.item_return_time);
            viewHolder.mItemMoney = (TextView) convertView.findViewById(R.id.item_money);
            viewHolder.mItemProfit = (TextView) convertView.findViewById(R.id.item_profit);
            viewHolder.mItemRate = (TextView) convertView.findViewById(R.id.item_rate);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        SwipeLayout mSwipeLayout = (SwipeLayout) convertView.findViewById(R.id.invest_assets_history_swipelayout);
        FrontLayout layoutFront = (FrontLayout) convertView.findViewById(R.id.layout_front);
        Button btDelete = (Button) convertView.findViewById(R.id.bt_delete);

        mSwipeLayout.close(false, false);//默认全部关闭
        mSwipeLayout.setSwipeListener(mSwipeListener);
        layoutFront.setTag(R.id.layout_front, position);
        layoutFront.setOnClickListener(onActionClick);
        btDelete.setTag(R.id.layout_front, layoutFront);
        btDelete.setTag(R.id.item_money, position);
        btDelete.setOnClickListener(onActionClick);
        viewHolder.mItemPName.setText(mDataList.get(position).getP_name());
        viewHolder.mItemProjectName.setText(mDataList.get(position).getProject_name());
        viewHolder.mItemReturnTime.setText(DateTimeUtil.formatDateTime(Long.parseLong(mDataList.get(position).getReturn_time()) * 1000, DateTimeUtil.BID_DF_YYYY_MM_DD) + "结束");
        viewHolder.mItemMoney.setText(mDataList.get(position).getMoney());
        viewHolder.mItemProfit.setText(mDataList.get(position).getProfit());
        viewHolder.mItemRate.setText(mDataList.get(position).getRate() + "%");
        if ("1".equals(mDataList.get(position).getIs_auto())) {
            mSwipeLayout.enableSwipe(false);
        } else {
            mSwipeLayout.enableSwipe(true);
        }
        return convertView;
    }


    /** 操作事件监听 */
    private View.OnClickListener onActionClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            closeAllLayout();
            switch (v.getId()) {
                case R.id.bt_delete://删除消息
                    int index = (int) v.getTag(R.id.item_money);
                    mDeleteBidListener.onDeleteBid(mDataList.get(index).getAid(), index);
                    FrontLayout layoutFront = (FrontLayout) v.getTag(R.id.layout_front);
//                    deleteAndRemoveItem(layoutFront);
                    break;
                case R.id.layout_front://点击条目事件
                    int position = (int) v.getTag(R.id.layout_front);
                    String aid = mDataList.get(position).getAid();
                    Intent intent = new Intent(context, LlcBidDetailActivity.class);
                    intent.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA, aid);
                    intent.putExtra(Const.KEY_INTENT_JUMP_FROM_DATA, "历史资产");
                    context.startActivity(intent);
                    break;
            }
        }
    };

    /** 删除消息 */
    private void deleteAndRemoveItem(FrontLayout layoutFront) {
        TranslateAnimation ta = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0);
        ta.setDuration(300);
        layoutFront.startAnimation(ta);
        ta.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                notifyDataSetChanged();
            }
        });
    }

    /** 侧滑事件监听 */
    private SwipeLayout.SwipeListener mSwipeListener = new SwipeLayout.SwipeListener() {
        @Override
        public void onOpen(SwipeLayout swipeLayout) {
            mUnClosedLayouts.add(swipeLayout);
        }

        @Override
        public void onClose(SwipeLayout swipeLayout) {
            mUnClosedLayouts.remove(swipeLayout);
        }

        @Override
        public void onStartClose(SwipeLayout swipeLayout) {
        }

        @Override
        public void onStartOpen(SwipeLayout swipeLayout) {
            closeAllLayout();
        }
    };

    /** 关闭所有侧滑开的条目 */
    public void closeAllLayout() {
        if (mUnClosedLayouts.size() == 0)
            return;
        for (SwipeLayout l : mUnClosedLayouts) {
            l.close(true, false);
        }
        mUnClosedLayouts.clear();
    }


    class ViewHolder {
        private TextView mItemProjectName;
        private TextView mItemPName;
        private TextView mItemReturnTime;
        private TextView mItemMoney;
        private TextView mItemProfit;
        private TextView mItemRate;
    }

    public interface IDeleteBidListener {
        void onDeleteBid(String aid, int position);
    }

    public void setDeleteBidListener(IDeleteBidListener listener) {
        this.mDeleteBidListener = listener;
    }

}
