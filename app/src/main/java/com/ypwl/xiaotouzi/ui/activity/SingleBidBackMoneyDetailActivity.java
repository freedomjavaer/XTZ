package com.ypwl.xiaotouzi.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.XtzApp;
import com.ypwl.xiaotouzi.adapter.SingleBidDetailAdapter;
import com.ypwl.xiaotouzi.adapter.StackChartRecycleAdapter;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.bean.CommonBean;
import com.ypwl.xiaotouzi.bean.SingleBidBackMoneyBean;
import com.ypwl.xiaotouzi.bean.StackBarChartBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.common.ServerStatus;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.event.AccountsKeptEvent;
import com.ypwl.xiaotouzi.event.BidStatusChangeEvent;
import com.ypwl.xiaotouzi.event.InvestPlatformRefreshEvent;
import com.ypwl.xiaotouzi.interf.INetRequestListener;
import com.ypwl.xiaotouzi.interf.IRequestCallback;
import com.ypwl.xiaotouzi.manager.EventHelper;
import com.ypwl.xiaotouzi.manager.InvestStatusChangeHelper;
import com.ypwl.xiaotouzi.manager.net.NetHelper;
import com.ypwl.xiaotouzi.netprotocol.ChangeReturnedStatusProtocol;
import com.ypwl.xiaotouzi.netprotocol.SingleBidBackMoneyProtocol;
import com.ypwl.xiaotouzi.utils.DateTimeUtil;
import com.ypwl.xiaotouzi.utils.FileUtil;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.NetworkUtils;
import com.ypwl.xiaotouzi.utils.PopuViewUtil;
import com.ypwl.xiaotouzi.utils.StringUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.Util;
import com.ypwl.xiaotouzi.utils.ViewHolder;
import com.ypwl.xiaotouzi.utils.ViewUtil;
import com.ypwl.xiaotouzi.view.InvestBackStackChartView;
import com.ypwl.xiaotouzi.view.dialog.CustomDialog;
import com.ypwl.xiaotouzi.view.dialog.lib.KProgressHUD;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * function : 单个标的回款详情
 * <p/>
 * Created by tengtao on 2016/3/28.
 */
public class SingleBidBackMoneyDetailActivity extends BaseActivity implements View.OnClickListener, StackChartRecycleAdapter.OnStackBarSelectedListener, SingleBidDetailAdapter.IChangeStatusListener, AbsListView.OnScrollListener {
    private View mNoDataView, mContentLayout;
    private TextView mTvTitle, mTvP_name, mTvBidName, mTvReturnedMoney, mTvDueMoney,mTvSyncTime,mTvSyncButton,mTvBack;
    private ListView mListView;
    private InvestBackStackChartView mStackChartView;
    private SingleBidBackMoneyProtocol mBackMoneyProtocol;
    private KProgressHUD mDialogLoading;
    private String aid,currRid, perStatus,pid;
    private String rMoney,sMoney;//已收总额，待收总额
    private List<SingleBidBackMoneyBean.ListEntity> mList;
    private int index;
    private SingleBidDetailAdapter mSingleBidDetailAdapter;
    private TextView mTvReturnedType;
    private List<StackBarChartBean> datas;
    private LinearLayout mSyncingLayout;
    private boolean isStatusChange = true;
    private String backName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        aid = intent.getStringExtra(Const.KEY_INTENT_JUMP_BASE_DATA);
        backName = intent.getStringExtra(Const.KEY_INTENT_JUMP_FROM_DATA);
        if (StringUtil.isEmptyOrNull(aid)) {
            UIUtil.showToastShort("投资ID不能为空");
            return;
        }
        setContentView(R.layout.activity_bid_returned_money_detail);
        mDialogLoading = KProgressHUD.create(mActivity);
        mBackMoneyProtocol = new SingleBidBackMoneyProtocol();
        mList = new ArrayList<>();
        initView();
        initData();
    }

    private void initView() {
        //视图部分
        mNoDataView = findViewById(R.id.layout_no_data_view);
        mContentLayout = findViewById(R.id.layout_content);
        //标题部分
        findViewById(R.id.layout_title_back).setOnClickListener(this);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvTitle.setText("回款详情");
        mTvBack = (TextView) findViewById(R.id.tv_title_back);
        mTvBack.setText(StringUtil.isEmptyOrNull(backName)?"返回":backName);
        mTvSyncButton = (TextView) findViewById(R.id.tv_title_txt_right);
        mTvSyncButton.setText("同步数据");
        mTvSyncButton.setOnClickListener(this);
        mTvSyncTime = (TextView) findViewById(R.id.tv_single_bid_detail_tb_date);
        mSyncingLayout = (LinearLayout) findViewById(R.id.ll_bid_back_detail_sync_ing);
        findViewById(R.id.iv_bid_returned_detail_right_arrow)
                .setVisibility(Const.JUMP_TO_SINGLE_BID_FROM_TYPE == 0 ? View.VISIBLE : View.GONE);
        findViewById(R.id.layout_bid_name_and_project_name).setOnClickListener(this);
        //平台名称、标的名称
        mTvP_name = (TextView) findViewById(R.id.tv_bid_returned_detail_p_name);
        mTvBidName = (TextView) findViewById(R.id.tv_bid_returned_detail_project_name);
        //回款清单列表
        mListView = (ListView) findViewById(R.id.single_bid_list_view);
        mSingleBidDetailAdapter = new SingleBidDetailAdapter(mActivity);
        mSingleBidDetailAdapter.setIChangeStatusListener(this);//改变状态
        //堆积图
        mStackChartView = (InvestBackStackChartView) findViewById(R.id.bid_returned_detail_bar_chart);
        mStackChartView.setOnStackBarSelectedListener(this);
        mStackChartView.setScrollToDivider(false);
        //还款方式和已收款、待收款
        LinearLayout headerView = (LinearLayout) View.inflate(mActivity,R.layout.layout_single_bid_returned_detail_list_header_view,null);
        mTvReturnedType = (TextView) headerView.findViewById(R.id.single_bid_header_view_returned_type);
        mTvReturnedMoney = (TextView) headerView.findViewById(R.id.tv_bid_returned_detail_returned_money);
        mTvDueMoney = (TextView) headerView.findViewById(R.id.tv_bid_returned_detail_due_money);
        mListView.addHeaderView(headerView);

        mListView.setOnScrollListener(this);
        mListView.setAdapter(mSingleBidDetailAdapter);

        ViewUtil.showContentLayout(Const.LAYOUT_LOADING, mNoDataView, mContentLayout);
    }

    private void initData() {
        mBackMoneyProtocol.loadData(aid, mRequestListener);//请求网络数据
    }

    private INetRequestListener mRequestListener = new INetRequestListener<SingleBidBackMoneyBean>() {
        @Override
        public void netRequestCompleted() {
            if(!NetworkUtils.isNetworkConnected(UIUtil.getContext())){
                ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mNoDataView, mContentLayout, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBackMoneyProtocol.loadData(aid, mRequestListener);
                    }
                });
            }
        }

        @Override
        public void netRequestSuccess(SingleBidBackMoneyBean bean, boolean isSuccess) {
            if (bean != null && isSuccess) {
                mSyncingLayout.setVisibility(View.INVISIBLE);
                mTvSyncTime.setVisibility(View.VISIBLE);
                refreshData(bean);
                ViewUtil.showContentLayout(bean.getList().size()==0?Const.LAYOUT_EMPTY:Const.LAYOUT_DATA, mNoDataView, mContentLayout);
            } else {
                ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mNoDataView, mContentLayout, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBackMoneyProtocol.loadData(aid, mRequestListener);
                    }
                });
            }
        }
    };

    private void refreshData(SingleBidBackMoneyBean bean) {
        rMoney = bean.getRmoney();
        sMoney = bean.getSmoney();
        mList.clear();
        mList.addAll(bean.getList());
        String is_auto = bean.getIs_auto();
        pid = bean.getPid();
        mTvSyncButton.setVisibility("0".equalsIgnoreCase(is_auto)?View.GONE:View.VISIBLE);
        mTvSyncTime.setVisibility("0".equalsIgnoreCase(is_auto) ? View.GONE : View.VISIBLE);
        findViewById(R.id.iv_single_bid_detail_auto_tally_icon).setVisibility("0".equalsIgnoreCase(is_auto) ? View.GONE : View.VISIBLE);
        long update = Long.parseLong(bean.getUpdate_time());
        if ("1".equalsIgnoreCase(is_auto)) {
            mTvSyncTime.setText(DateTimeUtil.getIntervalTimeDayMin(System.currentTimeMillis(),  update* 1000) + "同步");
        }
        mTvReturnedType.setText(bean.getReturn_name());
        mTvP_name.setText(bean.getP_name());
        mTvBidName.setText(bean.getProject_name());
        mTvReturnedMoney.setText(rMoney);
        mTvDueMoney.setText(sMoney);
        handleData(mList);//处理数据给stackbarchart
        mSingleBidDetailAdapter.loadData(bean);
    }

    /***
     * 处理数据-->堆积图
     *
     * @param list
     */
    private void handleData(List<SingleBidBackMoneyBean.ListEntity> list) {
        datas = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            SingleBidBackMoneyBean.ListEntity listEntity = list.get(i);
            String status = listEntity.getStatus();
            StackBarChartBean bean = new StackBarChartBean();
            bean.setSelected(false);
            bean.setOverdue("2".equals(status) ? listEntity.getTotal() : "0");
            bean.setType("1".equals(status) ? "1" : "2");
            bean.setDatetime(listEntity.getReturn_time());
            bean.setMoney(listEntity.getTotal());
            datas.add(bean);
        }
        mStackChartView.updateView(datas);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_title_back:
                finish();
                break;
            case R.id.layout_bid_name_and_project_name://跳转到标的详情
                //从回款页进入可跳转
                if (Const.JUMP_TO_SINGLE_BID_FROM_TYPE == 0) {
                    Intent intent = new Intent(mActivity, LlcBidDetailActivity.class);
                    intent.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA, aid);
                    intent.putExtra(Const.KEY_INTENT_JUMP_FROM_DATA, "回款详情");
                    mActivity.startActivity(intent);
                }
                break;
            case R.id.tv_title_txt_right://同步
                requestSync(String.format(URLConstant.SYNC_AUTO_TALLY, GlobalUtils.token, pid, ""));
                break;
        }
    }

    private void requestSync(String url) {
        NetHelper.get(url, new IRequestCallback<String>() {
            @Override
            public void onStart() {
                mSyncingLayout.setVisibility(View.VISIBLE);
                mTvSyncTime.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Exception e) {
                UIUtil.showToastShort("网络请求异常");
                mSyncingLayout.setVisibility(View.INVISIBLE);
                mTvSyncTime.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(String jsonStr) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    int status = jsonObject.getInt(Const.JSON_KEY_status);
                    if (status == ServerStatus.SERVER_STATUS_OK) {
                        syncAutoTallyData(getString(R.string.auto_tally_sync_data));//数据同步中

                    } else if (status == ServerStatus.SERVER_STATUS_AUTO_TALLY_AUTH_CODE) { //输入验证码
                        String imgdata = jsonObject.getString(Const.JSON_KEY_AUTH_CODE_imgdata);
                        if (!StringUtil.isEmptyOrNull(imgdata) && imgdata.contains("base64")) {
                            int index = imgdata.indexOf("base64");
                            showAuthCode(imgdata.substring(index + 6));
                        }
                    } else {
                        mSyncingLayout.setVisibility(View.INVISIBLE);
                        mTvSyncTime.setVisibility(View.VISIBLE);
                        String errorMsg = "";
                        try {
                            errorMsg = jsonObject.getString(Const.JSON_KEY_ret_msg);
                        } catch (Exception e) {
                        }
                        syncAutoTallyData(errorMsg.length() > 0 ? errorMsg : getString(R.string.auto_tally_sync_service_busy));//服务器繁忙
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 对话框提示
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
                String url = String.format(URLConstant.SYNC_AUTO_TALLY, GlobalUtils.token, pid, -1);
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
                        } catch (Exception e) {
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
                requestSync(String.format(URLConstant.SYNC_AUTO_TALLY, GlobalUtils.token, pid, code));
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

    /** 选中堆积图条目 */
    @Override
    public void onStackBarSelected(int position) {
        mListView.setSelection(position + 1);
    }

    @Override
    public void onChangeStatus(String rids, String status, final int position) {
        currRid = rids;
        perStatus = status;
        this.index = position;
        PopuViewUtil dialog = InvestStatusChangeHelper.getInstance().show(this, status, new InvestStatusChangeHelper.IStatusSelectedListener() {
            @Override
            public void onStatusSelected(final String status) {
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
                            mSingleBidDetailAdapter.changeStatus(index, status);
                            isStatusChange = false;
                            EventHelper.post(new BidStatusChangeEvent());
                            changeChartView(index, status);
                            changeTotalMoney(index, status);
                        } else {
                            UIUtil.showToastShort("更改失败");
                        }
                    }
                });
            }
        });
        dialog.show();
    }

    /**
     * 更改已收回款和待收回款
     * @param position 更改状态的条目位置
     * @param status 更改后的状态
     */
    private void changeTotalMoney(int position,String status){
        DecimalFormat df = new DecimalFormat("###0.00");
        boolean hasBacked = "1".equalsIgnoreCase(status);
        String total = mList.get(position).getTotal();
        Double currTotal = Double.parseDouble(total.replace(",", ""));
        Double currRMoney = Double.parseDouble(rMoney.replace(",", ""));
        Double currSMoney = Double.parseDouble(sMoney.replace(",", ""));
        if(hasBacked){//已回
            rMoney = Util.markOperatorForMoney(df.format(currRMoney + currTotal));
            sMoney = Util.markOperatorForMoney(df.format(currSMoney - currTotal));
        }else if("1".equalsIgnoreCase(perStatus)){
            rMoney = Util.markOperatorForMoney(df.format(currRMoney - currTotal));
            sMoney = Util.markOperatorForMoney(df.format(currSMoney + currTotal));
        }
        mTvReturnedMoney.setText(rMoney);
        mTvDueMoney.setText(sMoney);
    }

    /** 更改堆积图 */
    private void changeChartView(int position,String status){
        boolean hasBacked = "1".equalsIgnoreCase(status);//改为已回
        StackBarChartBean bean = datas.get(position);
        String money = bean.getMoney();
        if(hasBacked){
            bean.setMoney(money);
            bean.setOverdue("0");
            bean.setType("1");
        }else if("2".equalsIgnoreCase(status)){//改为逾期
            bean.setMoney(money);
            bean.setOverdue(money);
            bean.setType("2");
        }else{
            bean.setMoney(money);
            bean.setOverdue("0");
            bean.setType("2");
        }
        mStackChartView.setScrollToDivider(true);
        mStackChartView.setToPosition(position);
        mStackChartView.updateView(datas);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //listview显示到顶部是下拉可以刷新
        if (firstVisibleItem == 0) {
            mContentLayout.setEnabled(true);
        } else {
            mContentLayout.setEnabled(false);
        }
    }

    @Subscribe
    public void onInvestPlatformRefreshEvent(InvestPlatformRefreshEvent event){
        if(event!=null && !isFinishing()){
            initData();
        }
    }

    @Subscribe
    public void onBidStatusChangeEvent(BidStatusChangeEvent event){
        if(event!=null && !isFinishing() && isStatusChange){
            initData();
        }
        isStatusChange = true;
    }

    /** 编辑保存标的 */
    @Subscribe
    public void onAccountsKeptEvent(AccountsKeptEvent event){
        if(event!=null && !isFinishing()){
            initData();
        }
    }
}
