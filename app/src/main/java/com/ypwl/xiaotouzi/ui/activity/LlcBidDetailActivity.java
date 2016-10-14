package com.ypwl.xiaotouzi.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.squareup.otto.Subscribe;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.XtzApp;
import com.ypwl.xiaotouzi.adapter.BidDetailReceivableAdapter;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.bean.CommonBean;
import com.ypwl.xiaotouzi.bean.LlcBidDetailBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.common.ServerStatus;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.common.UmengEventID;
import com.ypwl.xiaotouzi.event.AccountsKeptEvent;
import com.ypwl.xiaotouzi.event.AutoTallyPlatformRefreshEvent;
import com.ypwl.xiaotouzi.event.BidDeletedEvent;
import com.ypwl.xiaotouzi.event.BidStatusChangeEvent;
import com.ypwl.xiaotouzi.event.InvestPlatformRefreshEvent;
import com.ypwl.xiaotouzi.interf.INetRequestListener;
import com.ypwl.xiaotouzi.interf.IRequestCallback;
import com.ypwl.xiaotouzi.manager.EventHelper;
import com.ypwl.xiaotouzi.manager.InvestStatusChangeHelper;
import com.ypwl.xiaotouzi.manager.UmengEventHelper;
import com.ypwl.xiaotouzi.manager.net.NetHelper;
import com.ypwl.xiaotouzi.netprotocol.ChangeReturnedStatusProtocol;
import com.ypwl.xiaotouzi.utils.DateTimeUtil;
import com.ypwl.xiaotouzi.utils.FileUtil;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.LogUtil;
import com.ypwl.xiaotouzi.utils.NetworkUtils;
import com.ypwl.xiaotouzi.utils.PopuViewUtil;
import com.ypwl.xiaotouzi.utils.StringUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.ViewHolder;
import com.ypwl.xiaotouzi.utils.ViewUtil;
import com.ypwl.xiaotouzi.view.dialog.CustomDialog;
import com.ypwl.xiaotouzi.view.dialog.lib.KProgressHUD;
import com.ypwl.xiaotouzi.view.fancycoverflow.FancyCoverFlow;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * function :标的详情（llc注明表示已有此类，重复写）
 * Created by llc on 2016/3/25 10:20
 * Email：licailuo@qq.com
 */

public class LlcBidDetailActivity extends BaseActivity implements View.OnClickListener,BidDetailReceivableAdapter.IChangeStatusListener {
    /** 标题右上角 */
    private ImageView mRightImg;
    /** 标题右上角点击弹出的PopupWindow */
    private PopupWindow pw;
    /** 标的id,不能为空 */
    private String mAid;
    private View mLayoutNoDataView;
    private LinearLayout mLayoutDataContainer;
    private TextView mBidDetailAid, mBidDetailProjectName, mBidDetailBidStatus, mBidDetailStarttime, mBidDetailMoney, mBidDetailAwardCapital;
    private TextView mBidDetailRate, mBidDetailYear, mBidDetailTimeLimit, mBidDetailMonth, mBidDetailReturnType, mBidDetailReturnTime, mBidDetailProfit, mBidDetailWannual;
    private LinearLayout mLlBidDetailReward;
    private LinearLayout mLlBidDetailAward;
    private TextView mBidDetailTenderAward, mBidDetailExtraAward;
    private LinearLayout mLlBidDetailCost;
    private TextView mBidDetailCost, mBidDetailCashCost, mBidDetailRemark, mBidDetailRatio, mBidDetailPeriod, mBidDetailMyMoney;
    private TextView mBidDetailRtotal, mBidDetailRcapital, mBidDetailRprofit, mBidDetailSprofit, mBidDetailStotal, mBidDetailScapital;
    private ProgressBar PbBidDetailRtotal;
    private int status;
    private String currRid;//当前回款id
    private String currStatus;//当前状态
    private RelativeLayout mBidDetailRlNextReturn;
    private String mPid;
    private LlcBidDetailBean intentDetailBean;
    private KProgressHUD mDialogLoading;
    private String mFromWhere;
    private String rId;
    private String is_auto;
    private TextView mTvSyncTime, mRightTv;
    private LinearLayout mSyncingLayout;
    private ListView mLvReceivable;
    private ScrollView scrollView;
    private BidDetailReceivableAdapter bidDetailReceivableAdapter;
    private int position;
    private RelativeLayout mLlBidDetailPrincipal;//本金移动父视图
    private int principalWidth = 0;//本金可以显示的宽度
    private int allWidth = 0;//全部长度
    private boolean refreshAll = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.llc_activity_bid_detail);

        mAid = getIntent().getStringExtra(Const.KEY_INTENT_JUMP_BASE_DATA);
        mFromWhere = getIntent().getStringExtra(Const.KEY_INTENT_JUMP_FROM_DATA);
        if (StringUtil.isEmptyOrNull(mAid)) {
            UIUtil.showToastShort("未找到相关标");
            finish();
            return;
        }
        initView();
        initData();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        refreshAll = true;
        initData();
    }

    private void initData() {
        String url = String.format(URLConstant.MYINVEST_BID_DETAIL, GlobalUtils.token, mAid, 4.2 + "");
        LogUtil.e("----------------url--" + url);
        NetHelper.get(url, new RecentReturnBidDetailCallBack());
    }


    @Override
    public void onChangeStatus(String rids, String status, final int position) {
        this.position = position;
        currRid = rids;
        currStatus = status;
        PopuViewUtil dialog = InvestStatusChangeHelper.getInstance().show(this, status, new InvestStatusChangeHelper.IStatusSelectedListener() {
            @Override
            public void onStatusSelected(final String status) {
                if (currStatus.equals(status)) {
                    return;
                }
                ChangeReturnedStatusProtocol mStatusProtocol = new ChangeReturnedStatusProtocol();
                mDialogLoading.show();
                mStatusProtocol.loadData(currRid, status, new INetRequestListener<CommonBean>() {
                    @Override
                    public void netRequestCompleted() {
                        mDialogLoading.dismiss();
                    }

                    @Override
                    public void netRequestSuccess(CommonBean bean, boolean isSuccess) {
                        if (bean != null && isSuccess) {
                            EventHelper.post(new BidStatusChangeEvent());
                            EventHelper.post(new InvestPlatformRefreshEvent());


                            if (null != bidDetailReceivableAdapter) {
                                bidDetailReceivableAdapter.changeStatus(position, currStatus, status);
                            }
                            refreshAll = false;
                            initData();

                        } else {
                            UIUtil.showToastShort("更改失败");
                        }
                    }
                });
            }
        });
        dialog.show();
    }


    /** 标的详情回调接口 */
    private class RecentReturnBidDetailCallBack extends IRequestCallback<String> {

        @Override
        public void onStart() {
        }

        @Override
        public void onFailure(Exception e) {
            UIUtil.showToastShort("获取数据失败");
            ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mLayoutNoDataView, mLayoutDataContainer, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewUtil.showContentLayout(Const.LAYOUT_LOADING, mLayoutNoDataView, mLayoutDataContainer);
                    refreshAll = true;
                    initData();
                }
            });
        }

        @Override
        public void onSuccess(String jsonStr) {
            mDialogLoading.dismiss();
            if (!NetworkUtils.isNetworkConnected(UIUtil.getContext())) {
                ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mLayoutNoDataView, mLayoutDataContainer, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refreshAll = true;
                        initData();
                    }
                });
            }
            LogUtil.e("-------------jsonStr-" + jsonStr);
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);
                int status = jsonObject.optInt(Const.JSON_KEY_status, -1);
                LlcBidDetailBean bean = JSON.parseObject(jsonStr, LlcBidDetailBean.class);
                switch (status) {
                    case ServerStatus.SERVER_STATUS_OK:
                        intentDetailBean = bean;
                        loadDataToView(bean);
                        break;
                    default:
                        this.onFailure(null);
                        break;
                }
            } catch (Exception e) {
                this.onFailure(e);
            }
        }


    }

    //加载数据到视图
    private void loadDataToView(LlcBidDetailBean bean) {
        try {
            DecimalFormat df = new DecimalFormat("######0.00");
            Double rtotal = Double.parseDouble(bean.getRtotal().replaceAll(",", "").trim());
            Double stotal = Double.parseDouble(bean.getStotal().replaceAll(",", "").trim());
            Double total = rtotal + stotal;
            if (!refreshAll) {
                mBidDetailRatio.setText("已回" + df.format(rtotal / total * 100) + "%");
                mBidDetailPeriod.setText(bean.getPeriod() + "/" + bean.getPeriod_total() + "期");
                //比例
                PbBidDetailRtotal.setProgress((int) (rtotal / total * 100));
                mBidDetailRtotal.setText("已回总额" + bean.getRtotal());
                mBidDetailStotal.setText("待回总额" + bean.getStotal());
                mBidDetailRcapital.setText("本金" + bean.getRcapital());
                mBidDetailRprofit.setText("收益" + bean.getRprofit());
                mBidDetailScapital.setText("本金" + bean.getScapital());
                mBidDetailSprofit.setText("收益" + bean.getSprofit());
                int lineWidth = UIUtil.dip2px(1);
                principalWidth = principalWidth - lineWidth;//本金可以显示的宽度
                double principalNum = Double.parseDouble(bean.getMoney().replaceAll(",", "").trim());
                int percentage = (int) (principalNum / total * 100);
                int maginLeft = 0;
                if (percentage > 40) {
                    maginLeft = principalWidth / 60 * (percentage - 40);
                }
                mLlBidDetailPrincipal.removeAllViews();
                View lineView = new View(mActivity);
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                        UIUtil.dip2px(1), FancyCoverFlow.LayoutParams.MATCH_PARENT);
                LogUtil.e("----------------", "----------------lineWidth------:" + lineWidth);
                LogUtil.e("----------------", "----------------principalWidth------:" + principalWidth);
                LogUtil.e("----------------", "----------------percentage------:" + percentage);
                LogUtil.e("----------------", "----------------maginLeft------:" + maginLeft);
                lp.setMargins(maginLeft > principalWidth ? principalWidth : maginLeft, 0, 0, 0);

                lineView.setBackgroundColor(UIUtil.getColor(R.color.e));
                lineView.setLayoutParams(lp);//设置布局参数
                mLlBidDetailPrincipal.addView(lineView);
                return;
            }
            mPid = bean.getPid();
            mBidDetailAid.setText(bean.getP_name());
            mBidDetailProjectName.setText(bean.getProject_name());
            is_auto = bean.getIs_auto();
            LogUtil.e("---------------is_auto--" + is_auto);
            findViewById(R.id.iv_single_bid_detail_auto_tally_icon).setVisibility("0".equalsIgnoreCase(is_auto) ?
                    View.GONE : View.VISIBLE);
            mRightTv.setVisibility("0".equalsIgnoreCase(is_auto) ?
                    View.GONE : View.VISIBLE);
            mRightImg.setVisibility("1".equalsIgnoreCase(is_auto) ?
                    View.GONE : View.VISIBLE);
            long update = Long.parseLong(bean.getUpdate_time());
            if ("1".equalsIgnoreCase(is_auto)) {
                mSyncingLayout.setVisibility(View.INVISIBLE);
                mTvSyncTime.setVisibility(View.VISIBLE);
                if (1 != update) {
                    mTvSyncTime.setText(DateTimeUtil.getIntervalTimeDayMin(System.currentTimeMillis(), update * 1000) + "同步");
                }
            }
            String bidStatus;
            if (Integer.parseInt(bean.getBid_status()) == 0) {
                bidStatus = "还款中";
            } else if (Integer.parseInt(bean.getBid_status()) == 1) {
                bidStatus = "已结束";
            } else {
                bidStatus = "未知";
            }
            mBidDetailBidStatus.setText(bidStatus);
            String startTime = DateTimeUtil.formatDateTime(Long.parseLong(bean.getStarttime()) * 1000, DateTimeUtil.DF_YYYYMMDD);
            mBidDetailStarttime.setText(startTime);
            mBidDetailMoney.setText("投标" + bean.getMoney());
//

            if (bean.getAward_capital() == null || Double.parseDouble(bean.getAward_capital().replaceAll(",", "").trim()) == 0.00) {
                mBidDetailAwardCapital.setVisibility(View.GONE);
            } else {
                double money = Double.parseDouble(bean.getMoney().replaceAll(",", "").trim());
                double awrdCapital = Double.parseDouble(bean.getAward_capital().replaceAll(",", "").trim());
                double whole = money + awrdCapital;
                mBidDetailAwardCapital.setVisibility(View.VISIBLE);
                mBidDetailAwardCapital.setText("奖励本金" + awrdCapital + "             合计" + df.format(whole)
                );
            }
            //TODO 记账设置
            mBidDetailRate.setText("标的年化率" + bean.getRate() + "%");
            String timeType = "天";
            if (bean.getTime_type().equals(1 + "")) {
                timeType = "个月";
            }
            mBidDetailTimeLimit.setText("周期" + bean.getTime_limit() + timeType);
            mBidDetailReturnType.setText(bean.getReturn_name());
            String resultTime = DateTimeUtil.formatDateTime(Long.parseLong(bean.getReturn_time()) * 1000, DateTimeUtil.DF_YYYYMMDD);
            mBidDetailReturnTime.setText(resultTime);
            mBidDetailProfit.setText("预期收益" + bean.getProfit());
            if (StringUtil.isEmptyOrNull(bean.getWannual())) {
                mBidDetailWannual.setText("综合年化率" + 0 + "%");
            } else {
                mBidDetailWannual.setText("综合年化率" + bean.getWannual() + "%");
            }

            List<String> s = new ArrayList<>();
            if (null != bean.getTender_award() && !bean.getTender_award().equals("0.00")) {
                s.add("投标奖励" + bean.getTender_award());
            }
            if (null != bean.getExtra_award() && !bean.getExtra_award().equals("0.00")) {
                s.add("额外奖励" + bean.getExtra_award());
            }
            if (null != bean.getCost() && !bean.getCost().equals("0.00")) {
                s.add("利息管理费" + bean.getCost());
            }
            if (null != bean.getCash_cost() && !bean.getCash_cost().equals("0.00")) {
                s.add("取现手续费" + bean.getCash_cost());
            }
            if (s.size() == 0) {
                mLlBidDetailReward.setVisibility(View.GONE);
            } else {
                mLlBidDetailReward.setVisibility(View.VISIBLE);
                if (s.size() == 1) {
                    mLlBidDetailCost.setVisibility(View.GONE);
                    mBidDetailExtraAward.setVisibility(View.GONE);
                    mBidDetailTenderAward.setText(s.get(0));
                } else if (s.size() == 2) {
                    mLlBidDetailCost.setVisibility(View.GONE);
                    mBidDetailTenderAward.setText(s.get(0));
                    mBidDetailExtraAward.setText(s.get(1));
                } else if (s.size() == 3) {
                    mLlBidDetailCost.setVisibility(View.VISIBLE);
                    mBidDetailTenderAward.setText(s.get(0));
                    mBidDetailExtraAward.setText(s.get(1));
                    mBidDetailCost.setText(s.get(2));
                    mBidDetailCashCost.setVisibility(View.GONE);
                } else {
                    mLlBidDetailCost.setVisibility(View.VISIBLE);
                    mBidDetailCashCost.setVisibility(View.VISIBLE);
                    mBidDetailTenderAward.setText(s.get(0));
                    mBidDetailExtraAward.setText(s.get(1));
                    mBidDetailCost.setText(s.get(2));
                    mBidDetailCashCost.setText(s.get(3));
                }

            }
            if (StringUtil.isEmptyOrNull(bean.getRemark())) {
                mBidDetailRemark.setVisibility(View.GONE);
            } else {
                mBidDetailRemark.setVisibility(View.VISIBLE);
                mBidDetailRemark.setText(bean.getRemark());
            }


            mBidDetailRatio.setText("已回" + df.format(rtotal / total * 100) + "%");
            mBidDetailPeriod.setText(bean.getPeriod() + "/" + bean.getPeriod_total() + "期");
            mBidDetailMyMoney.setText("本金" + bean.getMoney());
            if (principalWidth == 0 || allWidth == 0) {
                ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mLayoutNoDataView, mLayoutDataContainer, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewUtil.showContentLayout(Const.LAYOUT_LOADING, mLayoutNoDataView, mLayoutDataContainer);
                        refreshAll = true;
                        initData();
                    }
                });
                return;

            }
            int lineWidth = UIUtil.dip2px(1);
            principalWidth = principalWidth - lineWidth;//本金可以显示的宽度
            double principalNum = Double.parseDouble(bean.getMoney().replaceAll(",", "").trim());
            int percentage = (int) (principalNum / total * 100);
            int maginLeft = 0;
            if (percentage > 40) {
                maginLeft = principalWidth / 60 * (percentage - 40);
            }
            mLlBidDetailPrincipal.removeAllViews();
            View lineView = new View(mActivity);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    UIUtil.dip2px(1), FancyCoverFlow.LayoutParams.MATCH_PARENT);
            LogUtil.e("----------------", "----------------maginLeft------:" + maginLeft);
            lp.setMargins(maginLeft > principalWidth ? principalWidth : maginLeft, 0, 0, 0);

            lineView.setBackgroundColor(UIUtil.getColor(R.color.e));
            lineView.setLayoutParams(lp);//设置布局参数
            mLlBidDetailPrincipal.addView(lineView);


            //比例
            PbBidDetailRtotal.setProgress((int) (rtotal / total * 100));
            mBidDetailRtotal.setText("已回总额" + bean.getRtotal());
            mBidDetailStotal.setText("待回总额" + bean.getStotal());
            mBidDetailRcapital.setText("本金" + bean.getRcapital());
            mBidDetailRprofit.setText("收益" + bean.getRprofit());
            mBidDetailScapital.setText("本金" + bean.getScapital());
            mBidDetailSprofit.setText("收益" + bean.getSprofit());
            if (bean.getList().size() == 0) {
                mBidDetailRlNextReturn.setVisibility(View.GONE);
            } else {
                mBidDetailRlNextReturn.setVisibility(View.VISIBLE);
                bidDetailReceivableAdapter = new BidDetailReceivableAdapter(mActivity, bean);
                bidDetailReceivableAdapter.setIChangeStatusListener(this);
                mLvReceivable.setAdapter(bidDetailReceivableAdapter);
                setListViewHeightBasedOnChildren(mLvReceivable);
                scrollView.post(new Runnable() {
                    //让scrollview跳转到顶部，必须放在runnable()方法中
                    @Override
                    public void run() {
                        scrollView.scrollTo(0, 0);
                    }
                });
            }
            ViewUtil.showContentLayout(Const.LAYOUT_DATA, mLayoutNoDataView, mLayoutDataContainer);
        } catch (Exception e) {
            e.printStackTrace();
            ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mLayoutNoDataView, mLayoutDataContainer, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewUtil.showContentLayout(Const.LAYOUT_LOADING, mLayoutNoDataView, mLayoutDataContainer);
                    refreshAll = true;
                    initData();
                }
            });
        }
    }

    private void initView() {
        //title
        TextView mTitle = (TextView) findViewById(R.id.tv_title);
        mTitle.setText(UIUtil.getString(R.string.platform_detail_bid));
        TextView mTvTitleBack = (TextView) findViewById(R.id.tv_title_back);
        mTvTitleBack.setText(mFromWhere);
        mRightImg = (ImageView) findViewById(R.id.iv_title_right_image);
        mRightTv = (TextView) findViewById(R.id.tv_title_txt_right);
        mRightTv.setText("同步数据");
        mRightImg.setVisibility(View.VISIBLE);
        mRightTv.setVisibility(View.GONE);
        mRightImg.setImageResource(R.mipmap.three_blue_posint);
        findViewById(R.id.layout_title_back).setOnClickListener(this);
        mRightImg.setOnClickListener(this);
        mRightTv.setOnClickListener(this);

        //body
        mLayoutNoDataView = findViewById(R.id.layout_no_data_view);
        mLayoutDataContainer = (LinearLayout) findViewById(R.id.layout_data_container);
        scrollView = (ScrollView) findViewById(R.id.scrollview);

        mBidDetailAid = (TextView) findViewById(R.id.bid_detail_aid);
        mBidDetailProjectName = (TextView) findViewById(R.id.bid_detail_project_name);
        mBidDetailBidStatus = (TextView) findViewById(R.id.bid_detail_bid_status);
        mBidDetailStarttime = (TextView) findViewById(R.id.bid_detail_starttime);
        mBidDetailMoney = (TextView) findViewById(R.id.bid_detail_money);
        mBidDetailAwardCapital = (TextView) findViewById(R.id.bid_detail_award_capital);
        mBidDetailRate = (TextView) findViewById(R.id.bid_detail_rate);
        mBidDetailTimeLimit = (TextView) findViewById(R.id.bid_detail_time_limit);
        mBidDetailReturnType = (TextView) findViewById(R.id.bid_detail_return_type);
        mBidDetailReturnTime = (TextView) findViewById(R.id.bid_detail_return_time);
        mBidDetailProfit = (TextView) findViewById(R.id.bid_detail_profit);
        mBidDetailWannual = (TextView) findViewById(R.id.bid_detail_wannual);
        mLlBidDetailReward = (LinearLayout) findViewById(R.id.ll_bid_detail_reward);
        mLlBidDetailAward = (LinearLayout) findViewById(R.id.ll_bid_detail_award);
        mBidDetailTenderAward = (TextView) findViewById(R.id.bid_detail_tender_award);
        mBidDetailExtraAward = (TextView) findViewById(R.id.bid_detail_extra_award);
        mLlBidDetailCost = (LinearLayout) findViewById(R.id.ll_bid_detail_cost);
        mBidDetailCost = (TextView) findViewById(R.id.bid_detail_cost);
        mBidDetailCashCost = (TextView) findViewById(R.id.bid_detail_cash_cost);
        mBidDetailRemark = (TextView) findViewById(R.id.bid_detail_remark);
        mBidDetailRatio = (TextView) findViewById(R.id.bid_detail_ratio);
        PbBidDetailRtotal = (ProgressBar) findViewById(R.id.pb_bid_detail_rtotal);
        mLlBidDetailPrincipal = (RelativeLayout) findViewById(R.id.rl_bid_detail_principal);
        mBidDetailPeriod = (TextView) findViewById(R.id.bid_detail_period);
        mBidDetailMyMoney = (TextView) findViewById(R.id.bid_detail_my_money);
        mBidDetailRprofit = (TextView) findViewById(R.id.bid_detail_rprofit);
        mBidDetailSprofit = (TextView) findViewById(R.id.bid_detail_sprofit);
        mBidDetailRtotal = (TextView) findViewById(R.id.bid_detail_rtotal);
        mBidDetailRlNextReturn = (RelativeLayout) findViewById(R.id.rl_next_return);
        mBidDetailRcapital = (TextView) findViewById(R.id.bid_detail_rcapital);
        mBidDetailStotal = (TextView) findViewById(R.id.bid_detail_stotal);
        mBidDetailScapital = (TextView) findViewById(R.id.bid_detail_scapital);

        mTvSyncTime = (TextView) findViewById(R.id.tv_single_bid_detail_tb_date);
        mSyncingLayout = (LinearLayout) findViewById(R.id.ll_bid_back_detail_sync_ing);
        mLvReceivable = (ListView) findViewById(R.id.lv_receivable);

        mDialogLoading = KProgressHUD.create(mActivity);
        mBidDetailAid.setOnClickListener(this);

        ViewTreeObserver vto = mLlBidDetailPrincipal.getViewTreeObserver();

        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mLlBidDetailPrincipal.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int width = mLlBidDetailPrincipal.getMeasuredWidth();
                principalWidth = width;
                LogUtil.e("-----------------", "----------principalWidth----:" + principalWidth);
            }

        });
        ViewTreeObserver pVto = PbBidDetailRtotal.getViewTreeObserver();

        pVto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                PbBidDetailRtotal.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int width = PbBidDetailRtotal.getMeasuredWidth();
                allWidth = width;
                LogUtil.e("-----------------", "----------allWidth----:" + allWidth);
            }

        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /** 点击返回图标 */
            case R.id.layout_title_back:
                finish();
                break;
            /** 点击右上角菜单弹出选择器 */
            case R.id.iv_title_right_image:
                if (is_auto != null) {
                    showPopupWindow("2");
                }

                break;
            /** 点击右上角同步按钮 */
            case R.id.tv_title_txt_right:
                if (is_auto != null && mPid != null) {
                    changeSysnData(true, false);
                    requestSync(String.format(URLConstant.SYNC_AUTO_TALLY, GlobalUtils.token, mPid, ""), Const.NET_TAG_REQUEST_SYNC_AUTO_TALLY_DATA);
                }

                break;

            /** 点击平台详情 */
            case R.id.bid_detail_aid:
                if (mPid != null && is_auto != null) {
                    Intent intent1 = new Intent(this, MyInvestPlatformDetailActivity.class);
                    intent1.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA, mPid);
                    intent1.putExtra(MyInvestPlatformDetailActivity.IS_AUTO, Integer.parseInt(is_auto));
                    intent1.putExtra(Const.KEY_INTENT_JUMP_FROM_DATA, "标的详情");
                    Log.e("------------------", "--------------mPid---" + mPid);
                    Log.e("------------------", "--------------is_auto---" + is_auto);
                    Log.e("------------------", "--------------标的详情---" + "标的详情");
                    startActivity(intent1);
                }
                break;

        }

    }


    /**
     * 显示popupwindow
     */
    private void showPopupWindow(String autoValue) {
        final View contentView;
        LinearLayout mSync = null;
        LinearLayout mUnBund = null;
        LinearLayout mEditor = null;
        LinearLayout mDelete = null;
        if ("1".equals(autoValue)) {
            contentView = View.inflate(getApplicationContext(), R.layout.item_biddetail_popupwindow_sync, null);
            mSync = (LinearLayout) contentView.findViewById(R.id.ll_popupwindow_sync);
            mUnBund = (LinearLayout) contentView.findViewById(R.id.ll_popupwindow_unbund);
            mSync.setOnClickListener(new PopupWindowListener());
            mUnBund.setOnClickListener(new PopupWindowListener());
            pw = new PopupWindow(contentView, UIUtil.dip2px(140), UIUtil.dip2px(100));
        } else {
            contentView = View.inflate(getApplicationContext(), R.layout.item_biddetail_popupwindow, null);
            mEditor = (LinearLayout) contentView.findViewById(R.id.ll_popupwindow_editor);
            mDelete = (LinearLayout) contentView.findViewById(R.id.ll_popupwindow_delete);
            mEditor.setOnClickListener(new PopupWindowListener());
            mDelete.setOnClickListener(new PopupWindowListener());
            pw = new PopupWindow(contentView, UIUtil.dip2px(100), UIUtil.dip2px(78));
        }
        pw.setAnimationStyle(R.style.AnimationFade);
        pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 获取焦点 可以对pw里的组件操作
        pw.setFocusable(true);
        pw.setOutsideTouchable(true);
        //显示时背景变暗
        final WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().setAttributes(lp);
        //当消失时，恢复原状
        pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
        pw.showAsDropDown(mRightImg, 0, UIUtil.dip2px(-20));
    }


    private class PopupWindowListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {


            switch (v.getId()) {
                case R.id.ll_popupwindow_editor://投标编辑
                    pw.dismiss();
                    if (intentDetailBean != null) {
                        startActivity(KeepAccountsActivity.class, mAid);
                    }
                    break;
                case R.id.ll_popupwindow_delete://删除标
                    pw.dismiss();
                    if (is_auto != null) {
                        if ("0".equalsIgnoreCase(is_auto)) {
                            showAlertDialog();
                        } else {
                            UIUtil.showToastShort("此标类型为自动记账");
                        }
                    } else {
                        UIUtil.showToastShort("请重新加载此界面");
                    }
                    break;

            }
        }
    }


    private void changeSysnData(boolean b1, boolean b2) {
        mSyncingLayout.setVisibility(b1 ? View.VISIBLE : View.GONE);
        mTvSyncTime.setVisibility(b2 ? View.VISIBLE : View.GONE);
    }

    private void requestSync(String url, final String tag) {
        NetHelper.get(url, new IRequestCallback<String>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onFailure(Exception e) {
                mSyncingLayout.setVisibility(View.GONE);
                mTvSyncTime.setVisibility(View.VISIBLE);
                mTvSyncTime.setText("同步失败，请重试");
                UIUtil.showToastShort("网络请求异常");
            }

            @Override
            public void onSuccess(String jsonStr) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    int status = jsonObject.getInt(Const.JSON_KEY_status);
                    if (status == ServerStatus.SERVER_STATUS_OK) {
                        if (tag.equals(Const.NET_TAG_REQUEST_AUTO_TALLY_REMOVE_BINDING)) {
                            EventHelper.post(new InvestPlatformRefreshEvent());
                            EventHelper.post(new AutoTallyPlatformRefreshEvent());
                            finish();
                            UIUtil.showToastLong("解除绑定成功");
                        } else {
                            syncAutoTallyData(getString(R.string.auto_tally_sync_data));//数据同步中
                            mSyncingLayout.setVisibility(View.GONE);
                            mTvSyncTime.setVisibility(View.VISIBLE);
                            mTvSyncTime.setText("同步通知成功");


                        }
                    } else if (status == ServerStatus.SERVER_STATUS_AUTO_TALLY_AUTH_CODE) { //输入验证码
                        changeSysnData(false, true);
                        String imgdata = jsonObject.getString(Const.JSON_KEY_AUTH_CODE_imgdata);
                        if (!StringUtil.isEmptyOrNull(imgdata) && imgdata.contains("base64")) {
                            int index = imgdata.indexOf("base64");
                            showAuthCode(imgdata.substring(index + 6));
                        }
                    } else {
                        changeSysnData(false, true);
                        String errorMsg = "";
                        try {
                            errorMsg = jsonObject.getString(Const.JSON_KEY_ret_msg);
                        } catch (Exception e) {
                        }
                        syncAutoTallyData(errorMsg.length() > 0 ? errorMsg : getString(R.string.auto_tally_sync_service_busy));//服务器繁忙
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /** 显示输入验证码 */
    private void showAuthCode(String imgdata) {
        if (isFinishing()) {
            return;
        }
        View contentView = View.inflate(XtzApp.getApplication().getTopActivity(), R.layout.layout_dialog_auto_tally_auth_ocde, null);
        final EditText inputNumber = (EditText) contentView.findViewById(R.id.auth_code);
        final ImageView authPic = (ImageView) contentView.findViewById(R.id.pic_code);
        authPic.setImageBitmap(FileUtil.getImgFromBase64Stream(imgdata));
        Button submit = ViewHolder.findViewById(contentView, R.id.btn_submit);

        final CustomDialog dialog = new CustomDialog.AlertBuilder(mActivity)
                .setTitleText("请输入验证码")
                .setCustomContentView(contentView)
                .create();
        dialog.show();

        /** 点击刷新验证码图片 */
        authPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = String.format(URLConstant.SYNC_AUTO_TALLY, GlobalUtils.token, mPid, -1);
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
                            int status = jsonObject.getInt(Const.JSON_KEY_status);
                            if (status == ServerStatus.SERVER_STATUS_AUTO_TALLY_AUTH_CODE) {
                                String imgdata = jsonObject.getString(Const.JSON_KEY_AUTH_CODE_imgdata);
                                if (!StringUtil.isEmptyOrNull(imgdata) && imgdata.contains("base64")) {
                                    int index = imgdata.indexOf("base64");
                                    authPic.setImageBitmap(FileUtil.getImgFromBase64Stream(imgdata.substring(index + 6)));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        /** 提交验证码 */
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = inputNumber.getText().toString().trim();
                if (StringUtil.isEmptyOrNull(code)) {
                    UIUtil.showToastShort("验证码不能为空");
                    return;
                }
                requestSync(String.format(URLConstant.SYNC_AUTO_TALLY, GlobalUtils.token, mPid, code), Const.NET_TAG_REQUEST_SYNC_AUTO_TALLY_DATA);
                dialog.dismiss();
            }
        });
        /**调用系统输入法，延时保证dialog加载完成*/
        UIUtil.postDelayed(new Runnable() {
            @Override
            public void run() {
                inputNumber.setFocusable(true);
                inputNumber.setFocusableInTouchMode(true);
                inputNumber.requestFocus();
                InputMethodManager inputManager = (InputMethodManager) inputNumber.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(inputNumber, 0);
            }
        }, 200);
    }


    /**
     * 对话框提示
     *
     * @param s 提示信息
     */
    private void syncAutoTallyData(String s) {
        if (isFinishing()) {
            return;
        }
        final CustomDialog dialog = new CustomDialog.AlertBuilder(mActivity)
                .setTitleText("提示")
                .setContentText(s).setContentTextGravity(Gravity.CENTER)
                .setCanceledOnTouchOutside(false)
                .create();
        dialog.show();
        UIUtil.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 2000);
    }

    private CustomDialog mAlertDialog;

    /** 显示提示框，是否删除标 */
    private void showAlertDialog() {
        CustomDialog.AlertBuilder builder = new CustomDialog.AlertBuilder(this);
        builder.setContentText("是否删除该标")
                .setContentTextGravity(Gravity.CENTER)
                .setPositiveBtn("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDialogLoading.show();
                        requestDeleteBid();
                        UmengEventHelper.onEvent(UmengEventID.BidDetailDeleteButton);
                    }
                })
                .setNegativeBtn("取消", null);
        mAlertDialog = builder.create();
        mAlertDialog.show();
    }


    /** 请求服务器删除当前标 */
    private void requestDeleteBid() {
        final String url = StringUtil.format(URLConstant.BID_DETAIL_DELETE, GlobalUtils.token, mAid);
        NetHelper.get(url, new IRequestCallback<String>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onFailure(Exception e) {
                mDialogLoading.dismiss();
                UIUtil.showToastShort("删除失败");
            }

            @Override
            public void onSuccess(String jsonStr) {
                mDialogLoading.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    int status = jsonObject.getInt("status");
                    switch (status) {
                        case 0:
                            UIUtil.showToastShort("删除成功");
                            EventHelper.post(new InvestPlatformRefreshEvent());//删除标的事件，历史记录刷新
                            EventHelper.post(new BidDeletedEvent());//删除标的事件，资产页刷新
                            EventHelper.post(new AccountsKeptEvent());//删除标的事件，流水资产刷新
                            //关闭单个标的回款详情
                            XtzApp.getApplication().finishActivity(SingleBidBackMoneyDetailActivity.class);
                            finish();
                            break;
                        case 1202:
                            UIUtil.showToastShort("token值不匹配");
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    onFailure(e);
                }

            }
        });
    }


    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度

            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }


    /** 平台更新事件 */
    @Subscribe
    public void onAutoTallyPlatformRefreshEvent(AutoTallyPlatformRefreshEvent event){
        if (event != null && !isFinishing()) {
            finish();
        }
    }

}
