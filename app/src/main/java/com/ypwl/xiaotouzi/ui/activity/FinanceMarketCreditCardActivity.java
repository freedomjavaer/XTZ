package com.ypwl.xiaotouzi.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.adapter.FinanceMarketBankAdapter;
import com.ypwl.xiaotouzi.adapter.FinanceMarketCreditCardAdapter;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.bean.FinanceMarketBankBean;
import com.ypwl.xiaotouzi.bean.FinanceMarketCreditCardBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.interf.INetRequestListener;
import com.ypwl.xiaotouzi.netprotocol.FinanceMarketBankProtocol;
import com.ypwl.xiaotouzi.netprotocol.FinanceMarketCreditCardProtocol;
import com.ypwl.xiaotouzi.utils.NetworkUtils;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.ViewUtil;
import com.ypwl.xiaotouzi.view.MeasuredGridView;
import com.ypwl.xiaotouzi.view.MeasuredListView;

import java.util.List;

/**
 * function : 金融超市--信用卡页面
 * <p>
 * Created by tengtao on 2016/4/14.
 */
public class FinanceMarketCreditCardActivity extends BaseActivity implements View.OnClickListener {
    private View mNoDataView;
    private LinearLayout mContentLayout;
    /** 银行 */
    private FinanceMarketBankProtocol mBankProtocol;
    private MeasuredGridView mGridView;
    private FinanceMarketBankAdapter mBankAdapter;
    /** 信用卡*/
    private MeasuredListView mRecyclerView;
    private FinanceMarketCreditCardProtocol mCardProtocol;
    private FinanceMarketCreditCardAdapter mCardAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance_market_credit_card);
        initView();
    }

    private void initView() {
        mNoDataView = findViewById(R.id.layout_no_data_view);
        mContentLayout = (LinearLayout) findViewById(R.id.layout_data_content);
        ViewUtil.showContentLayout(Const.LAYOUT_LOADING,mNoDataView,mContentLayout);
        /**header部分 */
        ((TextView)findViewById(R.id.tv_title)).setText(getResources().getString(R.string.finance_market_credit_card));
        findViewById(R.id.layout_title_back).setOnClickListener(this);
        ((TextView)findViewById(R.id.tv_title_back)).setText("金融超市");
        /** 银行 */
        mGridView = (MeasuredGridView) findViewById(R.id.gv_bank_list);
        mBankAdapter = new FinanceMarketBankAdapter(mActivity);
        mGridView.setAdapter(mBankAdapter);
        /**信用卡*/
        mRecyclerView = (MeasuredListView) findViewById(R.id.rv_credit_card_list);
        mCardAdapter = new FinanceMarketCreditCardAdapter(mActivity);
        mRecyclerView.setAdapter(mCardAdapter);
        requestBankData();
    }

    /** 请求银行数据 */
    private void requestBankData() {
        if(mBankProtocol==null){
            mBankProtocol = new FinanceMarketBankProtocol();
        }
        mBankProtocol.loadData(mBankRequestListener, Const.REQUEST_GET);
    }

    private INetRequestListener mBankRequestListener = new INetRequestListener<FinanceMarketBankBean>() {
        @Override
        public void netRequestCompleted() {
            if(!NetworkUtils.isNetworkConnected(UIUtil.getContext())){
                ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mNoDataView, mContentLayout, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestBankData();
                    }
                });
            }
        }

        @Override
        public void netRequestSuccess(FinanceMarketBankBean bean, boolean isSuccess) {
            if(bean!=null && isSuccess){
                requestCreditCardData();//请求信用卡数据
                List<FinanceMarketBankBean.ListEntity> list = bean.getList();
                if(list!=null && list.size()>0){
                    mBankAdapter.loadData(list);
                }
            }else{
                ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mNoDataView, mContentLayout, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestBankData();
                    }
                });
            }
        }
    };

    /** 请求信用卡数据 */
    private void requestCreditCardData() {
        if(mCardProtocol==null){
            mCardProtocol = new FinanceMarketCreditCardProtocol();
        }
        mCardProtocol.loadData(mCardRequestListener,Const.REQUEST_GET);
    }

    private INetRequestListener mCardRequestListener = new INetRequestListener<FinanceMarketCreditCardBean>() {
        @Override
        public void netRequestCompleted() {
            if(!NetworkUtils.isNetworkConnected(UIUtil.getContext())){
                ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mNoDataView, mContentLayout, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestCreditCardData();
                    }
                });
            }
        }

        @Override
        public void netRequestSuccess(FinanceMarketCreditCardBean bean, boolean isSuccess) {
            if(bean!=null && isSuccess){
                List<FinanceMarketCreditCardBean.ListBean> list = bean.getList();
                if(list!=null && list.size()>0){
                    mCardAdapter.loadData(list);
                    ViewUtil.showContentLayout(Const.LAYOUT_DATA,mNoDataView,mContentLayout);
                }
            }else{
                ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mNoDataView, mContentLayout, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestCreditCardData();
                    }
                });
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_title_back:
                finish();
                break;
        }
    }
}
