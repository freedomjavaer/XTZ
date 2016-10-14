package com.ypwl.xiaotouzi.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.andview.refreshview.XRefreshView;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.adapter.PlatformOfFinanceAdapter;
import com.ypwl.xiaotouzi.base.BaseFragment;
import com.ypwl.xiaotouzi.bean.PlatformOfFinanceBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.common.ServerStatus;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.common.UmengEventID;
import com.ypwl.xiaotouzi.interf.IRequestCallback;
import com.ypwl.xiaotouzi.manager.UmengEventHelper;
import com.ypwl.xiaotouzi.manager.net.NetHelper;
import com.ypwl.xiaotouzi.ui.activity.FinanceSupermarketOfPlatformTargetsActivity;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.ViewUtil;
import com.ypwl.xiaotouzi.view.CustomXRefreshHeaderView;
import com.ypwl.xiaotouzi.view.LoadMoreListView;

import java.util.List;

/**
 * 项目名:	XTZ
 * 包名:	com.ypwl.xiaotouzi.ui.fragment
 * 类名:	FragmentFinanceSupermarketOfPlatform
 * 作者:	罗霄
 * 创建时间:	2016/4/19 11:17
 * <p/>
 * 描述:	金融超市 -- 平台页面
 * <p/>
 * svn版本:	$Revision: 16041 $
 * 更新人:	$Author: pengdakai $
 * 更新时间:	$Date: 2016-09-26 17:02:46 +0800 (周一, 26 九月 2016) $
 * 更新描述:	$Message$
 */
public class FragmentFinanceSupermarketOfPlatform extends BaseFragment implements LoadMoreListView.IListViewRefreshListener, AdapterView.OnItemClickListener {
    private LoadMoreListView mLvLoadMore;
    private View mNoDataView;
    private PlatformOfFinanceAdapter mAdapter;

    private int mPage = 1;
    //    private CustomSwipeToRefresh mSwipeToRefresh;
    private XRefreshView xRefreshView;
    private String url;

    //一页20条数据
    private final int mDefItemCount = 20;
    private View mIvNoData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_finance_supermarket_of_platform, container, false);
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {

        initView(v);

        initListener();

        initData();
    }

    private void initData() {
        mAdapter = new PlatformOfFinanceAdapter(getActivity());
        mLvLoadMore.setAdapter(mAdapter);

        ViewUtil.showContentLayout(Const.LAYOUT_LOADING, mNoDataView, mLvLoadMore);
        getData(mPage, false);
    }

    private void initListener() {
        mLvLoadMore.setOnRefreshListener(this);
        mLvLoadMore.setOnItemClickListener(this);
//        mSwipeToRefresh.setOnRefreshListener(this);
    }

    /**
     * 填充数据
     */
    private void fillData(List<PlatformOfFinanceBean> mData, final int page, final boolean isAdd) {
        if (null != mData && mData.size() != 0) {
            mAdapter.notifyDataSetChanged(mData, isAdd);
            mLvLoadMore.setLoadMoreEnable(mData.size() >= mDefItemCount);
            ViewUtil.showContentLayout(Const.LAYOUT_DATA, mNoDataView, mLvLoadMore);
        } else {
            if (1 == page) {
                ViewUtil.showContentLayout(Const.LAYOUT_EMPTY, mNoDataView, mLvLoadMore, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getData(page, isAdd);
                    }
                });
                mNoDataView.setVisibility(View.GONE);
                mIvNoData.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 获取数据
     */
    private void getData(final int page, final boolean isAdd) {

        url = String.format(URLConstant.FINANCE_PLATFORM_LIST, page, GlobalUtils.token);

        NetHelper.get(url, new IRequestCallback<String>() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onFailure(Exception e) {
                        mLvLoadMore.stopLoadMore();
                        xRefreshView.stopRefresh();
//                        mSwipeToRefresh.setRefreshing(false);
                        ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mNoDataView, mLvLoadMore, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getData(page, isAdd);
                            }
                        });
                    }

                    @Override
                    public void onSuccess(String jsonStr) {
                        mLvLoadMore.stopLoadMore();
//                        mSwipeToRefresh.setRefreshing(false);
                        xRefreshView.stopRefresh();
                        ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mNoDataView, mLvLoadMore, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getData(page, isAdd);
                            }
                        });
                        try {
                            JSONObject jsonObject = JSON.parseObject(jsonStr);
                            int status = jsonObject.getIntValue(Const.JSON_KEY_status);
                            String ret_msg = jsonObject.getString("ret_msg");

                            switch (status) {
                                case ServerStatus.SERVER_STATUS_OK:
                                    List<PlatformOfFinanceBean> list = JSON.parseArray(jsonObject.getString("list"), PlatformOfFinanceBean.class);
                                    fillData(list, page, isAdd);

                                    if (null != list && list.size() == 20) {
                                        mPage++;
                                    } else {
                                        mLvLoadMore.setDefaultText(getString(R.string.loadmore_status_nomore));
                                    }

                                    break;
                                default:
                                    UIUtil.showToastShort("" + ret_msg);
                                    break;
                            }

                        } catch (Exception e) {
                            this.onFailure(e);
                        }
                    }
                }

        );

    }

    private void initView(View rootView) {
        xRefreshView = findView(rootView, R.id.fragment_finance_supermarket_of_platform_swipe_refresh);
        mLvLoadMore = findView(rootView, R.id.fragment_finance_supermarket_of_platform_lv_loadmorelistview);
        mNoDataView = findView(rootView, R.id.layout_no_data_view);

        mIvNoData = findView(rootView, R.id.fragment_finance_supermarket_of_platform_iv_no_data);

        xRefreshView.setMoveForHorizontal(true);
        xRefreshView.setCustomHeaderView(createHeaderView());
        xRefreshView.setPullLoadEnable(false);
        xRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh() {
                mPage = 1;
                getData(mPage, false);
                UIUtil.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        xRefreshView.stopRefresh();
                    }
                }, 10 * 1000);
            }

        });
    }

    @Override
    public void onRefreshLoadMore() {
        getData(mPage, true);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra(Const.KEY_INTENT_JUMP_FROM_DATA, UIUtil.getString(R.string.finance_supermarket_fragment_name));
        intent.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA, ((PlatformOfFinanceBean) mAdapter.getItem(position - 1)).getPid( ));
        intent.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA + "name", ((PlatformOfFinanceBean) mAdapter.getItem(position - 1)).getP_name());
        intent.setClass(getActivity(), FinanceSupermarketOfPlatformTargetsActivity.class);
        startActivity(intent);
        UmengEventHelper.onEvent(UmengEventID.FsmPlatformBidList);
    }

//    @Override
//    public void onRefresh() {
//        mPage = 1;
//        mSwipeToRefresh.setRefreshing(true);
//        getData(mPage, false);
//        UIUtil.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mSwipeToRefresh.setRefreshing(false);
//            }
//        }, 10 * 1000);
//    }

    private View createHeaderView() {
        return new CustomXRefreshHeaderView(getContext());
    }
}
