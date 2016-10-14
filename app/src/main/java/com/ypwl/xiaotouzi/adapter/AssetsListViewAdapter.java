package com.ypwl.xiaotouzi.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.bean.AssetsAdapterBean;
import com.ypwl.xiaotouzi.bean.InvestAssetsBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.common.UmengEventID;
import com.ypwl.xiaotouzi.event.BidDeletedEvent;
import com.ypwl.xiaotouzi.event.InvestPlatformRefreshEvent;
import com.ypwl.xiaotouzi.interf.IRequestCallback;
import com.ypwl.xiaotouzi.manager.EventHelper;
import com.ypwl.xiaotouzi.manager.UmengEventHelper;
import com.ypwl.xiaotouzi.manager.net.NetHelper;
import com.ypwl.xiaotouzi.ui.activity.LlcBidDetailActivity;
import com.ypwl.xiaotouzi.ui.activity.MyInvestPlatformDetailActivity;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.ImgLoadUtil;
import com.ypwl.xiaotouzi.utils.StringUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.view.dialog.CustomDialog;
import com.ypwl.xiaotouzi.view.stickylistheaders.StickyListHeadersAdapter;
import com.ypwl.xiaotouzi.view.swipelistview.FrontLayout;
import com.ypwl.xiaotouzi.view.swipelistview.SwipeLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * 资产listview适配器
 *
 * Created by PDK on 2016/3/23.
 */
public class AssetsListViewAdapter extends BaseAdapter implements StickyListHeadersAdapter, SectionIndexer {

    Context context;
    private Set<SwipeLayout> mUnClosedLayouts = new HashSet<>();
    private Button btDelete;
    public List<AssetsAdapterBean> listData = new LinkedList<>();
    private List<Integer> mDividerList = new ArrayList<>();
    private InvestAssetsBean bean;
    private FrontLayout layoutFront;
    private CustomDialog mAlertDialog;
    private Activity activity;

    private int[] mSectionIndices;//索引位置
    private String[] mSectionHeaders;//索引值
    /**条目为空的状态*/
    private final int ITEMNULL = 1;
    /**条目不为空的状态*/
    private final int ITEMCONTENT = 2;


    private List<Integer> numList = new ArrayList<>();

    public AssetsListViewAdapter(Context context, Activity activity, InvestAssetsBean mInvestAssetsBean) {
        this.context = context;
        this.bean = mInvestAssetsBean;
        this.activity = activity;
        if (mInvestAssetsBean.getList().size()==0){
            return;
        }
        mSectionIndices = getSectionIndices(mInvestAssetsBean.getList());
        mSectionHeaders = getSectionHeaders(mInvestAssetsBean.getList());
        numList.clear();
        List<InvestAssetsBean.ListEntity> mList = mInvestAssetsBean.getList();
        for (int i = 0; i < mList.size(); i++) {
            List<InvestAssetsBean.ListEntity.DataEntity> dataList = mList.get(i).getData();
            String totalMoney = mList.get(i).getMoney();
            String totalProfit = mList.get(i).getProfit();
            String totalStotal = mList.get(i).getStotal();
            String p_name = mList.get(i).getP_name();
            String is_auto = mList.get(i).getIs_auto();
            String p_logo = mList.get(i).getP_logo();
            String pid = mList.get(i).getPid();
            if(dataList==null || dataList.size()==0){
                AssetsAdapterBean bean = new AssetsAdapterBean();
                bean.setTotal_money(totalMoney);
                bean.setTotal_profit(totalProfit);
                bean.setAid("");
                bean.setPid(pid);
                bean.setMoney("");
                bean.setP_name(p_name);
                bean.setProfit("");
                bean.setProject_name("");
                bean.setRate("");
                bean.setStarttime("");
                bean.setStotal("");
                bean.setIs_auto(is_auto);
                bean.setP_logo(p_logo);
                bean.setPlatformStotal("");
                bean.setNum("0");
                bean.setScapital("");
                bean.setTotalStotal(totalStotal);
                listData.add(bean);
            }else{
                for (int j = 0; j < dataList.size(); j++) {
                    AssetsAdapterBean bean = new AssetsAdapterBean();
                    bean.setTotal_money(totalMoney);
                    bean.setTotal_profit(totalProfit);
                    bean.setAid(dataList.get(j).getAid());
                    bean.setPid(dataList.get(j).getPid());
                    bean.setMoney(dataList.get(j).getMoney());
                    bean.setP_name(dataList.get(j).getP_name());
                    bean.setProfit(dataList.get(j).getProfit());
                    bean.setProject_name(dataList.get(j).getProject_name());
                    bean.setRate(dataList.get(j).getRate());
                    bean.setStarttime(dataList.get(j).getStarttime());
                    bean.setStotal(dataList.get(j).getStotal());
                    bean.setIs_auto(dataList.get(j).getIs_auto());
                    bean.setP_logo(dataList.get(j).getP_logo());
                    bean.setPlatformStotal(dataList.get(j).getStotal());
                    bean.setNum(mList.get(i).getNum());
                    bean.setScapital(dataList.get(j).getScapital());
                    bean.setTotalStotal(totalStotal);
                    listData.add(bean);
                }
            }
        }
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        String aid = listData.get(position).getAid();
        if(StringUtil.isEmptyOrNull(aid)){
            return ITEMNULL;
        }else{
            return ITEMCONTENT;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int itemViewType = getItemViewType(position);
        if (itemViewType == ITEMNULL) {
            if(convertView==null){
                convertView = new FrameLayout(context);
            }
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIUtil.dip2px(0));
            convertView.setLayoutParams(params);
        } else {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(context, R.layout.item_invest_assets_listview, null);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            SwipeLayout mSwipeLayout = (SwipeLayout) convertView.findViewById(R.id.invest_assets_swipelayout);
            layoutFront = (FrontLayout) convertView.findViewById(R.id.layout_front);
            viewHolder.mBidName = (TextView) convertView.findViewById(R.id.invest_assets_bid_name);
            viewHolder.mBidTime = (TextView) convertView.findViewById(R.id.invest_assets_bid_time);
            viewHolder.mBidMoney = (TextView) convertView.findViewById(R.id.invest_assets_bid_money);
            viewHolder.mBidProfit = (TextView) convertView.findViewById(R.id.invest_assets_bid_profit);
            viewHolder.mBidRate = (TextView) convertView.findViewById(R.id.invest_assets_bid_rate);
            btDelete = (Button) convertView.findViewById(R.id.bt_delete);

            if (listData.size() > 0) {
                viewHolder.mBidName.setText(listData.get(position).getProject_name());
                viewHolder.mBidTime.setText(GlobalUtils.dateFormat(listData.get(position).getStarttime()) + "起息");
                viewHolder.mBidMoney.setText(listData.get(position).getMoney());//标投资总额
                viewHolder.mBidProfit.setText(listData.get(position).getStotal());//标待回总额
                viewHolder.mBidRate.setText(listData.get(position).getProfit());//标待回本金

                if ("1".equals(listData.get(position).getIs_auto())) {
                    mSwipeLayout.enableSwipe(false);
                } else {
                    mSwipeLayout.enableSwipe(true);
                }

                mSwipeLayout.close(false, false);//默认全部关闭
                mSwipeLayout.setSwipeListener(mSwipeListener);
                layoutFront.setOnClickListener(onActionClick);
                layoutFront.setTag(position);
                btDelete.setTag(position);
                btDelete.setOnClickListener(onActionClick);
            }
        }
        return convertView;
    }


    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new HeaderViewHolder();
            convertView = View.inflate(context, R.layout.layout_invest_assets_item_header, null);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (HeaderViewHolder) convertView.getTag();
        }
        viewHolder.mPlatformName = (TextView) convertView.findViewById(R.id.invest_assets_bid_name);
        viewHolder.mBidNumber = (TextView) convertView.findViewById(R.id.invest_assets_bid_time);
        viewHolder.mInvestMoney = (TextView) convertView.findViewById(R.id.invest_assets_bid_money);
        viewHolder.mWaitMoney = (TextView) convertView.findViewById(R.id.invest_assets_bid_profit);
        viewHolder.mLogo = (ImageView) convertView.findViewById(R.id.iv_platform_logo);
        viewHolder.mCustomName = (TextView) convertView.findViewById(R.id.tv_customplatform_logo);
        viewHolder.mIvArrowDown = (ImageView) convertView.findViewById(R.id.iv_invest_analysis_arrow_down);
        viewHolder.mIvArrowUp = (ImageView) convertView.findViewById(R.id.iv_invest_analysis_arrow_up);
        viewHolder.mAutoTally = (ImageView) convertView.findViewById(R.id.iv_auto_tally);
        viewHolder.mJumpBackMoney = (LinearLayout) convertView.findViewById(R.id.ll_jump_platform_back_money);
        viewHolder.mJumpBackMoney.setOnClickListener(onActionClick);
        viewHolder.mJumpBackMoney.setTag(position);
        viewHolder.mFLContainer = (FrameLayout) convertView.findViewById(R.id.fl_view_container);

        if (listData.size() > 0){
            viewHolder.mPlatformName.setText(listData.get(position).getP_name());
            viewHolder.mBidNumber.setText(listData.get(position).getNum() + "笔");
            viewHolder.mInvestMoney.setText(listData.get(position).getTotalStotal());
            viewHolder.mWaitMoney.setText(listData.get(position).getTotal_profit());
            //LOGO,区分自定义平台
            boolean b = listData.get(position).getP_logo() != null && listData.get(position).getP_logo().length() > 0;
            viewHolder.mCustomName.setVisibility(b ? View.GONE : View.VISIBLE);
            String name = listData.get(position).getP_name();
            viewHolder.mCustomName.setText(name.length() > 4 ? name.substring(0, 4) : name);
            if (b) {
                ImgLoadUtil.loadLogo(listData.get(position).getPid(), listData.get(position).getP_logo(), viewHolder.mLogo, 0);
            } else {
                viewHolder.mLogo.setImageResource(R.mipmap.custom_platform_logo);
            }

            //判断是否为自动标
            if ("1".equals(listData.get(position).getIs_auto())) {
                viewHolder.mAutoTally.setVisibility(View.VISIBLE);
            } else {
                viewHolder.mAutoTally.setVisibility(View.GONE);
            }

            //箭头显示
            int showStatus = listData.get(position).getShowStatus();
            viewHolder.mIvArrowUp.setVisibility(showStatus == 1 ? View.VISIBLE : View.GONE);
            viewHolder.mIvArrowDown.setVisibility(showStatus == 0 ? View.VISIBLE : View.GONE);
        }

        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        if (mSectionIndices == null) {
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
        return new Object[0];
    }

    @Override
    public int getPositionForSection(int sectionIndex) {//根据header索引获取header在条目位置的索引
        if (mSectionIndices == null) {
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
    public int getSectionForPosition(int position) {//根据header在条目位置的索引获取header索引
        if (mSectionIndices == null) {
            return -1;
        }
        for (int i = 0; i < mSectionIndices.length; i++) {
            if (position < mSectionIndices[i]) {
                return i - 1;
            }
        }
        return mSectionIndices.length - 1;
    }

    /**
     * 获取索引
     *
     * @param list
     */
    private int[] getSectionIndices(List<InvestAssetsBean.ListEntity> list) {
        ArrayList<Integer> sectionIndices = new ArrayList<Integer>();
        int index = 0;
        sectionIndices.add(index);
        for (int i = 0; i < list.size() - 1; i++) {
            List<InvestAssetsBean.ListEntity.DataEntity> data = list.get(i).getData();
            if (data.size() == 0) {
                index += 1;
            } else {
                index += list.get(i).getData().size();
            }
            sectionIndices.add(index);
        }
        mDividerList.clear();
        int[] sections = new int[sectionIndices.size()];
        for (int i = 0; i < sectionIndices.size(); i++) {
            sections[i] = sectionIndices.get(i);
            if (sectionIndices.get(i) >= 1)
                mDividerList.add(sectionIndices.get(i) - 1);
        }
        return sections;
    }

    /**
     * 处理获取header数据
     */
    private String[] getSectionHeaders(List<InvestAssetsBean.ListEntity> list) {
        String[] headers = new String[mSectionIndices.length];
        for (int i = 0; i < mSectionIndices.length; i++) {
            String p_name = list.get(i).getP_name();
            headers[i] = p_name;
        }
        return headers;
    }

    class ViewHolder {
        TextView mBidName;
        TextView mBidTime;
        TextView mBidMoney;
        TextView mBidProfit;
        TextView mBidRate;
    }

    class HeaderViewHolder {
        TextView mPlatformName;
        TextView mBidNumber;
        TextView mInvestMoney;
        TextView mWaitMoney;
        ImageView mIvArrowDown;
        ImageView mIvArrowUp;
        ImageView mLogo;
        TextView mCustomName;
        LinearLayout mJumpBackMoney;
        ImageView mAutoTally;
        FrameLayout mFLContainer;
    }

    /** 改变箭头 */
    public void changeShowStatus(int position, int status) {
        if (mSectionIndices == null) {
            return;
        }
        int endPosition;
        int sectionForPosition = getSectionForPosition(position);
        if (sectionForPosition < mSectionIndices.length - 1) {
            endPosition = getPositionForSection(sectionForPosition + 1);
        } else {
            endPosition = listData.size();
        }
        if (listData != null && listData.size() > 0) {
            for (int i = 0; i < listData.size(); i++) {
                listData.get(i).setShowStatus(status == 0 ? 0 : (i >= position && i < endPosition ? status : 0));
            }
        }
        UIUtil.postDelayed(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        },50);
    }


    /** 操作事件监听 */
    private View.OnClickListener onActionClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            closeAllLayout();
            switch (v.getId()) {
                case R.id.ll_jump_platform_back_money://平台投资详情
                    Intent intent1 = new Intent(context, MyInvestPlatformDetailActivity.class);
                    int position1 = (int) v.getTag();
                    intent1.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA, listData.get(position1).getPid());
                    intent1.putExtra(Const.KEY_INTENT_JUMP_FROM_DATA, "我的投资");
                    intent1.putExtra(MyInvestPlatformDetailActivity.IS_AUTO,Integer.parseInt(listData.get(position1).getIs_auto()));
                    context.startActivity(intent1);
                    break;
                case R.id.bt_delete://删除消息
                    int position2 = (int) v.getTag();
                    String aid = listData.get(position2).getAid();
                    requestDeleteBid(aid, position2);
                    UmengEventHelper.onEvent(UmengEventID.BidDetailDeleteButton);
                    break;
                case R.id.layout_front://点击条目事件
                    int position3 = (int) v.getTag();
                    Intent intent2 = new Intent(context, LlcBidDetailActivity.class);
                    intent2.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA, listData.get(position3).getAid());
                    intent2.putExtra(Const.KEY_INTENT_JUMP_FROM_DATA, "我的投资");
                    context.startActivity(intent2);
                    break;
            }
        }
    };


    /** 请求服务器删除当前标 */
    private void requestDeleteBid(String mAid, final int position) {
        final String url = StringUtil.format(URLConstant.BID_DETAIL_DELETE, GlobalUtils.token, mAid);
        NetHelper.get(url, new IRequestCallback<String>() {

            @Override
            public void onStart() {

            }

            @Override
            public void onFailure(Exception e) {

            }

            @Override
            public void onSuccess(String jsonStr) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    int status = jsonObject.getInt("status");
                    switch (status) {
                        case 0:
                            UIUtil.showToastShort("删除成功");
                            EventHelper.post(new InvestPlatformRefreshEvent());
                            EventHelper.post(new BidDeletedEvent());
                            listData.remove(listData.get(position));
                            notifyDataSetChanged();
                            break;
                        case 1202:
                            UIUtil.showToastShort("token值不匹配");
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
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


}
