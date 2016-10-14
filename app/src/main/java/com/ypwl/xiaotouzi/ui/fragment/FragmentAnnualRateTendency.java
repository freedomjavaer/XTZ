package com.ypwl.xiaotouzi.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.github.mikephil.charting.charts.CombinedChart;
import com.squareup.otto.Subscribe;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.adapter.AnnualRateTendencyAdapter;
import com.ypwl.xiaotouzi.base.BaseFragment;
import com.ypwl.xiaotouzi.bean.ProfitTendencyBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.event.ProfitTendencyItemHighlightEvent;
import com.ypwl.xiaotouzi.interf.IRequestCallback;
import com.ypwl.xiaotouzi.manager.net.NetHelper;
import com.ypwl.xiaotouzi.utils.ChartUtils;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.StringUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.ViewUtil;

/**
 * 年化率趋势（在原历史投资的收益趋势的基础上修改）
 * Created by PDK on 2016/1/23.
 */
public class FragmentAnnualRateTendency extends BaseFragment {

    private CombinedChart mCombinedChart;
    private RecyclerView mRecycleView;
    private ProfitTendencyBean mProfitTendencyBean;
    private LinearLayout mDataContainer;
    private View mNoDataView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profit_tendency, container, false);
        mCombinedChart = (CombinedChart) view.findViewById(R.id.profit_tendency_combinedchart);
        mRecycleView = (RecyclerView) view.findViewById(R.id.profit_tendency_recycleview);

        mDataContainer = (LinearLayout) view.findViewById(R.id.profit_tendency_data_container);
        mNoDataView =  view.findViewById(R.id.layout_no_data_view);
        ViewUtil.showContentLayout(Const.LAYOUT_LOADING, mNoDataView, mDataContainer);

        initData();
        return view;
    }

    private void initData() {
        String url = StringUtil.format(URLConstant.PROFIT_TENDENCY, GlobalUtils.token);
        NetHelper.get(url, new IRequestCallback<String>() {

            @Override
            public void onStart() {

            }

            @Override
            public void onFailure(Exception e) {

            }

            @Override
            public void onSuccess(String jsonStr) {
                mProfitTendencyBean = JSON.parseObject(jsonStr, ProfitTendencyBean.class);
                if (mProfitTendencyBean.getStatus() == 0) {
                    if (mProfitTendencyBean.getList().size() == 0) {
                        ViewUtil.showContentLayout(Const.LAYOUT_EMPTY, mNoDataView, mDataContainer);
                        return;
                    }
                    refreshData();
                    ViewUtil.showContentLayout(Const.LAYOUT_DATA, mNoDataView, mDataContainer);
                } else {
                    UIUtil.showToastShort(mProfitTendencyBean.getRet_msg());
                    ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mNoDataView, mDataContainer);
                }
            }

        });

    }

    private void refreshData() {
        //给混合图表设置数据
        ChartUtils.createCombinedChart(getContext(), mCombinedChart, mProfitTendencyBean, mRecycleView);

        //给RecycleView设置数据
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecycleView.setLayoutManager(manager);
        AnnualRateTendencyAdapter mAdapter = new AnnualRateTendencyAdapter(getContext(), mProfitTendencyBean);
        mRecycleView.setAdapter(mAdapter);
    }

    @Subscribe
    public void onProfitTendencyItemHighlightEvent(ProfitTendencyItemHighlightEvent event) {
        if (event != null && !isDetached()) {
            mCombinedChart.highlightValue(event.position, event.highlight);
            mCombinedChart.invalidate();
        }
    }

}
