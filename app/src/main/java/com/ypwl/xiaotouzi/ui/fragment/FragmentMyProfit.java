package com.ypwl.xiaotouzi.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.squareup.otto.Subscribe;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.adapter.MyProfitAdapter;
import com.ypwl.xiaotouzi.base.BaseFragment;
import com.ypwl.xiaotouzi.bean.ProfitRangeBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.event.AccountsKeptEvent;
import com.ypwl.xiaotouzi.event.BidDeletedEvent;
import com.ypwl.xiaotouzi.event.ProfitRangeStatusChangeEvent;
import com.ypwl.xiaotouzi.interf.IRequestCallback;
import com.ypwl.xiaotouzi.manager.net.NetHelper;
import com.ypwl.xiaotouzi.ui.activity.MyInvestPlatformDetailActivity;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.StringUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.ViewUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 收益排行---收益界面
 *
 * Created by PDK on 2016/4/12.
 */
public class FragmentMyProfit extends BaseFragment{

    private ProfitRangeBean mProfitRangeBean;
    private ListView mListView;
    private LinearLayout mDataContainer;
    private View mNoDataView;

    /**状态类型---0为在投；1为结束；2为全部*/
    private int status = 2;
    /**项目类型---0为标；1为平台*/
    private int b_type = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_profit,container,false);

        mListView = (ListView) view.findViewById(R.id.lv_range_profit);

        mDataContainer = (LinearLayout) view.findViewById(R.id.ll_profit_data_container);
        mNoDataView = view.findViewById(R.id.layout_no_data_view);
        ViewUtil.showContentLayout(Const.LAYOUT_LOADING, mNoDataView, mDataContainer);

        initData();
        initEvent();
        return view;
    }

    private void initEvent() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (GlobalUtils.STATUS == 1){
                    Intent intent = new Intent(getContext(),MyInvestPlatformDetailActivity.class);
                    intent.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA,mProfitRangeBean.getList().get(position).getPid());
                    intent.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA_1,MyInvestPlatformDetailActivity.FRAGMENT_TAG_END);
                    intent.putExtra(MyInvestPlatformDetailActivity.IS_AUTO,2);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getContext(),MyInvestPlatformDetailActivity.class);
                    intent.putExtra(MyInvestPlatformDetailActivity.IS_AUTO,2);
                    intent.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA,mProfitRangeBean.getList().get(position).getPid());
                    startActivity(intent);
                }
            }
        });
    }

    private void initData() {
        String url = StringUtil.format(URLConstant.INVEST_ANALYSIS_RANGE_PROFIT, GlobalUtils.token, status, b_type, 0);
        NetHelper.get(url, new IRequestCallback<String>() {

            @Override
            public void onStart() {

            }

            @Override
            public void onFailure(Exception e) {
                UIUtil.showToastShort("网络异常");
            }

            @Override
            public void onSuccess(String jsonStr) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    int status = jsonObject.optInt("status");
                    if (status == 0) {
                        mProfitRangeBean = JSON.parseObject(jsonStr, ProfitRangeBean.class);
                        if (mProfitRangeBean.getList().size() == 0) {
                            ViewUtil.showContentLayout(Const.LAYOUT_EMPTY, mNoDataView, mDataContainer);
                            return;
                        }
                        MyProfitAdapter mAdapter = new MyProfitAdapter(mProfitRangeBean,b_type,getContext());
                        mListView.setAdapter(mAdapter);

                        ViewUtil.showContentLayout(Const.LAYOUT_DATA, mNoDataView, mDataContainer);
                    } else {
                        UIUtil.showToastShort(mProfitRangeBean.getRet_msg());
                        ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mNoDataView, mDataContainer);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });

    }

    @Subscribe
    public void onRangeStatusChangeEvent(ProfitRangeStatusChangeEvent event){
        if (null != event && !getActivity().isFinishing()){
            status = event.getStatus();
            b_type = event.getType();
            initData();
        }
    }

    @Subscribe
    public void onBidDeleteEvent(BidDeletedEvent event){
        if (null != event && !getActivity().isFinishing()){
            initData();
        }
    }

    @Subscribe
    public void onKeepAccountEvent(AccountsKeptEvent event){
        if (null != event && !getActivity().isFinishing()){
            initData();
        }
    }

}
