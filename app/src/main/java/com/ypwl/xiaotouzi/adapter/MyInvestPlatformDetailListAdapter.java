package com.ypwl.xiaotouzi.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.bean.CommonBean;
import com.ypwl.xiaotouzi.bean.MyInvestPlatformDetailBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.event.BidDeletedEvent;
import com.ypwl.xiaotouzi.event.MyInvestPlatformDetailRefreshEvent;
import com.ypwl.xiaotouzi.interf.IRequestCallback;
import com.ypwl.xiaotouzi.interf.UIAdapterListener;
import com.ypwl.xiaotouzi.manager.EventHelper;
import com.ypwl.xiaotouzi.manager.net.NetHelper;
import com.ypwl.xiaotouzi.ui.activity.LlcBidDetailActivity;
import com.ypwl.xiaotouzi.ui.activity.MyInvestPlatformDetailActivity;
import com.ypwl.xiaotouzi.utils.DateTimeUtil;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.StringUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.ViewHolder;
import com.ypwl.xiaotouzi.view.dialog.CustomDialog;
import com.ypwl.xiaotouzi.view.dialog.KProgressHUDHelper;
import com.ypwl.xiaotouzi.view.dialog.lib.KProgressHUD;
import com.ypwl.xiaotouzi.view.swipelistview.SwipeLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * function : 我的投资-资产-平台详情 列表数据适配器
 * <p/>
 * Created by lzj on 2016/3/24.
 */
public class MyInvestPlatformDetailListAdapter extends BaseAdapter {
    private Activity mActivity;
    private UIAdapterListener mUIAdapterListener;
    private String mFragmentType;
    private String mCurrentPid;
    private int mIsAuto;
    private List<MyInvestPlatformDetailBean.ListEntity> mDataList = new ArrayList<>();
    private int mCurrentPage = 1;
    private MyInvestPlatformDetailBean mMyInvestPlatformDetailBean;
    private Set<SwipeLayout> mUnClosedLayouts = new HashSet<>();

    public MyInvestPlatformDetailListAdapter(Activity activity, UIAdapterListener uiAdapterListener,
                                             String fragmentType, String currentPid, int isAuto) {
        mActivity = activity;
        mUIAdapterListener = uiAdapterListener;
        mFragmentType = fragmentType;
        mCurrentPid = currentPid;
        mIsAuto = isAuto;
    }

    public void initPage(int pageNumber) {
        mCurrentPage = pageNumber;
    }

    /** 异步加载数据 */
    public void asyncLoadData() {
        int option;
        if (MyInvestPlatformDetailActivity.FRAGMENT_TAG_ING.equals(mFragmentType)) {//在投标的
            option = 0;
        } else if (MyInvestPlatformDetailActivity.FRAGMENT_TAG_END.equals(mFragmentType)) {//结束投标的
            option = 1;
        } else {
            throw new RuntimeException("unknow fragment tag!!!");
        }
        String url = StringUtil.format(URLConstant.MYINVEST_PLATFORM_DETAIL, GlobalUtils.token, mCurrentPid, option, mCurrentPage, mIsAuto);
        NetHelper.get(url, new IRequestCallback<MyInvestPlatformDetailBean>() {
            @Override
            public void onStart() {
                mUIAdapterListener.isLoading();
            }

            @Override
            public void onFailure(Exception e) {
                mUIAdapterListener.dataCountChanged(Const.LOADED_ERROR);
            }

            @Override
            public void onSuccess(MyInvestPlatformDetailBean bean) {
                if (bean.getStatus() != 0) {
                    onFailure(null);
                    return;
                }
                EventHelper.post(new MyInvestPlatformDetailRefreshEvent(bean));
                mMyInvestPlatformDetailBean = bean;
                if (mCurrentPage == 1) {//第一次加载
                    mDataList.clear();
                    if (bean.getList() != null && bean.getList().size() > 0) {
                        mDataList.addAll(bean.getList());
                    }
                    mUIAdapterListener.loadFinished(mDataList.size());
                    notifyDataSetChanged();
                } else {//加载更多
                    if (bean.getList() != null && bean.getList().size() > 0) {
                        mDataList.addAll(bean.getList());
                        notifyDataSetChanged();
                        mUIAdapterListener.loadFinished(mDataList.size());
                    } else {
                        mUIAdapterListener.loadFinished(mDataList.size());
                        mUIAdapterListener.dataCountChanged(Const.LOADED_NO_MORE);
                    }
                }
            }
        });
    }

    public MyInvestPlatformDetailBean getDataBean() {
        return this.mMyInvestPlatformDetailBean;
    }

    /** 加载更多 */
    public void loadMore() {
        ++mCurrentPage;
        asyncLoadData();
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public MyInvestPlatformDetailBean.ListEntity getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = View.inflate(parent.getContext(), R.layout.item_fragment_myinvest_platformdetail, null);
        }
        SwipeLayout swipeLayout = ViewHolder.findViewById(convertView, R.id.layout_swipe);

        View layoutRoot = ViewHolder.findViewById(convertView, R.id.layout_root);
        TextView projectName = ViewHolder.findViewById(convertView, R.id.invest_project_name);
        TextView investTime = ViewHolder.findViewById(convertView, R.id.invest_time);
        TextView moneyIn = ViewHolder.findViewById(convertView, R.id.invest_money_in);
        TextView moneyInTitle = ViewHolder.findViewById(convertView, R.id.invest_money_in_title);
        TextView moneyProfit = ViewHolder.findViewById(convertView, R.id.invest_money_profit);
        TextView moneyProfitTitle = ViewHolder.findViewById(convertView, R.id.invest_money_profit_title);
        TextView moneyRate = ViewHolder.findViewById(convertView, R.id.invest_money_rate);
        TextView moneyRateTitle = ViewHolder.findViewById(convertView, R.id.invest_money_rate_title);
        View btnDelete = ViewHolder.findViewById(convertView, R.id.btn_delete);

        swipeLayout.close(false, false);//默认全部关闭
        swipeLayout.setSwipeListener(mSwipeListener);

        MyInvestPlatformDetailBean.ListEntity item = getItem(position);

        swipeLayout.enableSwipe(item.getIs_auto() != 1);//自动记账平台 禁用侧滑删除功能

        //项目名称
        projectName.setText(item.getProject_name());

        //投标或结束日期
        String formatDateTime = DateTimeUtil.formatDateTime(item.getStarttime() * 1000, DateTimeUtil.DF_YYYY_MM_DD_SPRIT);
        if (MyInvestPlatformDetailActivity.FRAGMENT_TAG_ING.equals(mFragmentType)) {//在投标的
            investTime.setText(StringUtil.format(R.string.myinvest_platform_detailt_item_time_bid, formatDateTime));

            moneyIn.setText(item.getMoney());
            moneyInTitle.setText("投资金额");

            moneyProfit.setTextColor(UIUtil.getColor(R.color.pd_focus_red));
            moneyProfit.setText(item.getStotal());
            moneyProfitTitle.setText("待回总额");

            moneyRate.setText(item.getProfit());
            moneyRateTitle.setText("预期收益");

        } else {//结束投标的
            investTime.setText(StringUtil.format(R.string.myinvest_platform_detailt_item_time_end, formatDateTime));

            moneyIn.setText(item.getMoney());
            moneyInTitle.setText("投资");

            moneyProfit.setTextColor(UIUtil.getColor(R.color.black));
            moneyProfit.setText(item.getProfit());
            moneyProfitTitle.setText("收益");

            moneyRate.setText(item.getRate() + "%");
            moneyRateTitle.setText("年化率");
        }

        layoutRoot.setTag(R.id.invest_project_name, position);
        layoutRoot.setOnClickListener(mOnClickListener);
        btnDelete.setTag(R.id.invest_project_name, position);
        btnDelete.setOnClickListener(onActionClick);

        return convertView;
    }

    /** 操作事件监听 */
    private View.OnClickListener onActionClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            closeAllLayout();
            int position = (int) v.getTag(R.id.invest_project_name);
            if (v.getId() == R.id.btn_delete) {// 删除记录
                deleteOneItem(position);
            }
        }
    };

    private KProgressHUD mLoading;

    /** 删除指定位置的条目 */
    private void deleteOneItem(final int position) {
        final MyInvestPlatformDetailBean.ListEntity item = getItem(position);
        if (null == item) {
            UIUtil.showToastShort("删除失败");
            return;
        }
        if (mLoading == null) {
            mLoading = KProgressHUDHelper.createLoading(mActivity);
        }
        new CustomDialog.AlertBuilder(mActivity).setTitleText("提示")
                .setContentText("确定删除?").setContentTextGravity(Gravity.CENTER)
                .setPositiveBtn("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mLoading.show();
                        String url = StringUtil.format(URLConstant.INVEST_DELETE_IN_BATCH, GlobalUtils.token, item.getAid());
                        NetHelper.get(url, new IRequestCallback<CommonBean>() {
                            @Override
                            public void onStart() {
                            }

                            @Override
                            public void onFailure(Exception e) {
                                mLoading.dismiss();
                                UIUtil.showToastShort("删除失败");
                            }

                            @Override
                            public void onSuccess(CommonBean bean) {
                                if (bean.getStatus() == 0) {
                                    mLoading.dismiss();
                                    mDataList.remove(position);
                                    notifyDataSetChanged();
                                    EventHelper.post(new BidDeletedEvent());
                                } else {
                                    onFailure(null);
                                }
                            }
                        });
                    }
                }).setNegativeBtn("取消", null)
                .create().show();
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

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag(R.id.invest_project_name);
            MyInvestPlatformDetailBean.ListEntity item = getItem(position);
            if (item == null || StringUtil.isEmptyOrNull(item.getAid())) {
                return;
            }
            Intent intent = new Intent(mActivity, LlcBidDetailActivity.class);
            intent.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA, item.getAid());
            intent.putExtra(Const.KEY_INTENT_JUMP_FROM_DATA, "平台详情");
            mActivity.startActivity(intent);
        }
    };


}
