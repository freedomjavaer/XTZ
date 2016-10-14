package com.ypwl.xiaotouzi.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.squareup.otto.Subscribe;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.bean.WalletBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.event.GetMoneyEvent;
import com.ypwl.xiaotouzi.interf.IRequestCallback;
import com.ypwl.xiaotouzi.manager.net.NetHelper;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.StringUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.Util;
import com.ypwl.xiaotouzi.utils.ViewUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 晓钱包界面
 *
 * Created by PDK on 2016/4/19.
 */
public class XtzWalletActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTitle;
    private TextView mTitleBackTV;
    private LinearLayout mTitleBack;
    private TextView mAvailableMoney;
    private TextView mWaitBackMoney;
    private TextView mFreezeMoney;
    private WalletBean mWalletBean;
    private LinearLayout mDataView;
    private View mNoDataView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xtz_wallet);

        initView();
        initData();
    }

    private void initData() {
        //title
        mTitle.setText("晓钱包");
        mTitleBackTV.setText("更多");

        //content
        String url = StringUtil.format(URLConstant.MY_INFO_WALLET, GlobalUtils.token);
        NetHelper.get(url,new XtzHttpCallBack());

    }

    private class XtzHttpCallBack extends IRequestCallback<String> {

        @Override
        public void onStart() {

        }

        @Override
        public void onFailure(Exception e) {
            ViewUtil.showContentLayout(Const.LAYOUT_ERROR,mNoDataView,mDataView);
        }

        @Override
        public void onSuccess(String jsonStr) {
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);
                int status = jsonObject.optInt("status");
                if (status == 0){
                    mWalletBean = JSON.parseObject(jsonStr, WalletBean.class);
                    refreshData();
                    ViewUtil.showContentLayout(Const.LAYOUT_DATA,mNoDataView,mDataView);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                ViewUtil.showContentLayout(Const.LAYOUT_ERROR,mNoDataView,mDataView);
            }
        }

    }

    private void refreshData() {

        Util.setSpannableSize(mAvailableMoney, mWalletBean.getMoney_usable(), mWalletBean.getMoney_usable().length() - 2, 38);
        Util.setSpannableSize(mWaitBackMoney, mWalletBean.getMoney_wait(), mWalletBean.getMoney_wait().length() - 2, 28);
        Util.setSpannableSize(mFreezeMoney, mWalletBean.getMoney_freeze(), mWalletBean.getMoney_freeze().length() - 2, 28);

    }


    private void initView() {
        mTitle = findView(R.id.tv_title);
        mTitleBackTV = findView(R.id.tv_title_back);
        mTitleBack = findView(R.id.layout_title_back);

        mAvailableMoney = findView(R.id.tv_available_money);
        mWaitBackMoney = findView(R.id.tv_wait_back_total_money);
        mFreezeMoney = findView(R.id.tv_freeze_money);

        mDataView = findView(R.id.ll_data_container);
        mNoDataView = findView(R.id.layout_no_data_view);
        ViewUtil.showContentLayout(Const.LAYOUT_LOADING,mNoDataView,mDataView);

        findView(R.id.tv_get_back_money).setOnClickListener(this);
        mTitleBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.layout_title_back:
                finish();
                break;
            case R.id.tv_get_back_money:
                int money = (int) Double.parseDouble(mWalletBean.getMoney_usable());
                if (money < 100){
                    UIUtil.createMyToast("可用余额需满100元才能提现").show();
                }else {
                    startActivity(GetMoneyActivity.class,mWalletBean.getMoney_usable());
                }
                break;
        }
    }

    @Subscribe
    public void onGetMoneyEvent(GetMoneyEvent event){
        if (null != event && !isFinishing()){
            initData();
        }
    }

}
