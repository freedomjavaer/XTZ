package com.ypwl.xiaotouzi.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.squareup.otto.Subscribe;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.adapter.AssetHistoryAdapter;
import com.ypwl.xiaotouzi.base.BaseFragment;
import com.ypwl.xiaotouzi.bean.CommonBean;
import com.ypwl.xiaotouzi.bean.InvestHistoryBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.event.InvestPlatformRefreshEvent;
import com.ypwl.xiaotouzi.interf.INetRequestListener;
import com.ypwl.xiaotouzi.netprotocol.DeleteBidProtocol;
import com.ypwl.xiaotouzi.netprotocol.HistoryAssetsByBidProtocol;
import com.ypwl.xiaotouzi.utils.NetworkUtils;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.ViewUtil;
import com.ypwl.xiaotouzi.view.CustomSwipeToRefresh;
import com.ypwl.xiaotouzi.view.CustomXRefreshHeaderView;
import com.ypwl.xiaotouzi.view.dialog.lib.KProgressHUD;

/**
 * function : 历史资产：按标的分类
 * <p/>
 * Created by tengtao on 2016/3/28.
 */
public class FragmentInvestHistoryByBid extends BaseFragment implements AssetHistoryAdapter.IDeleteBidListener {
    private View mNoDataView;
    //    private CustomSwipeToRefresh mSwipeRefreshLayout;
    private XRefreshView mSwipeRefreshLayout;
    private ListView mListView;
    private AssetHistoryAdapter assetHistoryAdapter;
    private HistoryAssetsByBidProtocol mAssetsProtocol;
    private DeleteBidProtocol mDeleteBidProtocol;//删除标的
    private KProgressHUD mDialogLoading;
    private TextView mTvHistoryHeaderEarnings, mTvHistoryHeaderRatio;//累计收益和加权年化率

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_invest_history_by_bid, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mDialogLoading = KProgressHUD.create(getActivity());
        mNoDataView = view.findViewById(R.id.layout_no_data_view);
//        mSwipeRefreshLayout = (CustomSwipeToRefresh) view.findViewById(R.id.sf_invest_history_content_by_bid);
//        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout = findView(view, R.id.sf_invest_history_content_by_bid);
        mSwipeRefreshLayout.setMoveForHorizontal(true);
        mSwipeRefreshLayout.setCustomHeaderView(new CustomXRefreshHeaderView(getContext()));
        mSwipeRefreshLayout.setPullLoadEnable(false);
        mSwipeRefreshLayout.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh() {
                mAssetsProtocol.loadData(mRequestListener, Const.REQUEST_GET);
                UIUtil.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.stopRefresh();
                    }
                }, 10 * 1000);
            }
        });

        //listview
        mListView = (ListView) view.findViewById(R.id.invest_history_content_by_bid);
        LinearLayout headerView = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.header_invest_history, null);
        mTvHistoryHeaderEarnings = (TextView) headerView.findViewById(R.id.tv_history_header_earnings);
        mTvHistoryHeaderRatio = (TextView) headerView.findViewById(R.id.tv_history_header_ratio);
        mListView.addHeaderView(headerView);

        assetHistoryAdapter = new AssetHistoryAdapter(getActivity());
        mListView.setAdapter(assetHistoryAdapter);
        assetHistoryAdapter.setDeleteBidListener(this);

        ViewUtil.showContentLayout(Const.LAYOUT_LOADING, mNoDataView, mSwipeRefreshLayout);
        initData();
    }

    private void initData() {
        mAssetsProtocol = new HistoryAssetsByBidProtocol();
        mAssetsProtocol.loadData(mRequestListener, Const.REQUEST_GET);
    }

    private INetRequestListener mRequestListener = new INetRequestListener<InvestHistoryBean>() {
        @Override
        public void netRequestCompleted() {
            mSwipeRefreshLayout.stopRefresh();
            if (!NetworkUtils.isNetworkConnected(UIUtil.getContext())) {
                ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mNoDataView, mSwipeRefreshLayout, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAssetsProtocol.loadData(mRequestListener, Const.REQUEST_GET);
                    }
                });
            }
        }

        @Override
        public void netRequestSuccess(InvestHistoryBean bean, boolean isSuccess) {
            if (bean != null && isSuccess) {
                if (bean.getList().size() == 0) {
                    ViewUtil.showContentLayout(Const.LAYOUT_EMPTY, mNoDataView, mSwipeRefreshLayout, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mAssetsProtocol.loadData(mRequestListener, Const.REQUEST_GET);
                        }
                    });
                    return;
                }
                try {
                    String[] tprofit = bean.getTprofit().split("\\.");
                    String[] wannual = bean.getWannual().split("\\.");
                    mTvHistoryHeaderEarnings.setText(Html.fromHtml("<big>" + tprofit[0] + "</big>." + tprofit[1]));
                    mTvHistoryHeaderRatio.setText(Html.fromHtml("<big>" + wannual[0] + "</big>." + wannual[1] + "%"));
                    assetHistoryAdapter.refreshDataList(bean.getList());
                    ViewUtil.showContentLayout(Const.LAYOUT_DATA, mNoDataView, mSwipeRefreshLayout);
                } catch (Exception e) {
                    ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mNoDataView, mSwipeRefreshLayout, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mAssetsProtocol.loadData(mRequestListener, Const.REQUEST_GET);
                        }
                    });
                }

            } else {
                ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mNoDataView, mSwipeRefreshLayout, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAssetsProtocol.loadData(mRequestListener, Const.REQUEST_GET);
                    }
                });
            }
        }
    };

//    @Override
//    public void onRefresh() {
//        mSwipeRefreshLayout.setRefreshing(true);
//        mAssetsProtocol.loadData(mRequestListener, Const.REQUEST_GET);
//        UIUtil.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mSwipeRefreshLayout.setRefreshing(false);
//            }
//        }, 10 * 1000);
//    }

    /** 删除标的 */
    @Override
    public void onDeleteBid(String aid, final int position) {
        if (mDeleteBidProtocol == null) {
            mDeleteBidProtocol = new DeleteBidProtocol();
        }
        mDialogLoading.show();
        mDeleteBidProtocol.loadData(aid, new INetRequestListener<CommonBean>() {
            @Override
            public void netRequestCompleted() {
                mDialogLoading.dismiss();
            }

            @Override
            public void netRequestSuccess(CommonBean bean, boolean isSuccess) {
                if (bean != null && isSuccess) {
                    UIUtil.showToastShort("删除成功");
                    mAssetsProtocol.loadData(mRequestListener, Const.REQUEST_GET);
                }
            }
        });
    }

    @Subscribe
    public void onInvestPlatformRefreshByBidEvent(InvestPlatformRefreshEvent event) {
        if (event != null && !isDetached()) {
            mAssetsProtocol.loadData(mRequestListener, Const.REQUEST_GET);
        }
    }
}
