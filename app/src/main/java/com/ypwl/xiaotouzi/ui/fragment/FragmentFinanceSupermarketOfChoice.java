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
import com.ypwl.xiaotouzi.adapter.ChoiceAdapter;
import com.ypwl.xiaotouzi.base.BaseFragment;
import com.ypwl.xiaotouzi.bean.ChoiceBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.common.ServerStatus;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.interf.IRequestCallback;
import com.ypwl.xiaotouzi.manager.net.NetHelper;
import com.ypwl.xiaotouzi.ui.activity.FinanceSupermarketOfTargetDetailActivity;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.ViewUtil;
import com.ypwl.xiaotouzi.view.CustomXRefreshHeaderView;
import com.ypwl.xiaotouzi.view.LoadMoreListView;

import java.util.List;

/**
 * 项目名:	XTZ
 * 包名:	com.ypwl.xiaotouzi.ui.fragment
 * 类名:	FragmentFinanceSupermarketOfChoice
 * 作者:	罗霄
 * 创建时间:	2016/4/18 14:27
 * <p/>
 * 描述:	金融超市--精选页面
 * <p/>
 * svn版本:	$Revision: 15856 $
 * 更新人:	$Author: pengdakai $
 * 更新时间:	$Date: 2016-07-27 16:56:33 +0800 (周三, 27 七月 2016) $
 * 更新描述:	$Message$
 */
public class FragmentFinanceSupermarketOfChoice extends BaseFragment implements AdapterView.OnItemClickListener, LoadMoreListView.IListViewRefreshListener {

    private LoadMoreListView mLvLoadMore;
    private View mNoDataView;
    private ChoiceAdapter choiceAdapter;

    private int mPage = 1;
    private XRefreshView xRefreshView;
    private String url;

    //一页20条数据
    private final int mDefItemCount = 20;
    private Intent intent;
    private View mIvNoData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_finance_supermarket_of_choice, container, false);
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {

        initView(v);

        initListener();

        initData();
    }

    private void initData() {
        choiceAdapter = new ChoiceAdapter(getActivity());
        mLvLoadMore.setAdapter(choiceAdapter);

        ViewUtil.showContentLayout(Const.LAYOUT_LOADING, mNoDataView, mLvLoadMore);
        getData(mPage, false);
    }

    private void initListener() {
        mLvLoadMore.setOnRefreshListener(this);
        mLvLoadMore.setOnItemClickListener(this);
//        mSwipeToRefresh.setOnRefreshListener(this);
    }

    /** 填充数据 */
    private void fillData(List<ChoiceBean> mData, final int page, final boolean isAdd) {
        if (null != mData && mData.size() != 0) {
            choiceAdapter.notifyDataSetChanged(mData, isAdd);
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

    /** 获取数据 */
    private void getData(final int page, final boolean isAdd) {

        url = String.format(URLConstant.FINANCE_CHOICE_LIST, page, GlobalUtils.token);

        NetHelper.get(url, new IRequestCallback<String>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onFailure(Exception e) {
                mLvLoadMore.stopLoadMore();
                xRefreshView.stopRefresh();
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
                            List<ChoiceBean> list = JSON.parseArray(jsonObject.getString("list"), ChoiceBean.class);
                            fillData(list, page, isAdd);

                            if (null != list && list.size() == mDefItemCount) {
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
        });
    }

    private void initView(View rootView) {
        xRefreshView = findView(rootView, R.id.fragment_finance_supermarket_of_choice_swipe_refresh);
        mLvLoadMore = findView(rootView, R.id.fragment_finance_supermarket_of_choice_lv_loadmorelistview);
        mNoDataView = findView(rootView, R.id.layout_no_data_view);

        mIvNoData = findView(rootView, R.id.fragment_finance_supermarket_of_choice_iv_no_data);


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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ChoiceBean item = (ChoiceBean) choiceAdapter.getItem(position - 1);

        intent = new Intent();
        intent.putExtra(Const.KEY_INTENT_JUMP_FROM_DATA, UIUtil.getString(R.string.finance_supermarket_fragment_name));
        intent.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA + "p_name", item.getP_name());
        intent.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA + "pid", item.getPid());
        intent.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA, item.getProject_id());
        intent.setClass(getActivity(), FinanceSupermarketOfTargetDetailActivity.class);
        startActivity(intent);
    }

//    @Override
//    public void onRefresh() {
//        mPage = 1;
//        mSwipeToRefresh.setRefreshing(true);
//        getData(mPage,false);
//        UIUtil.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mSwipeToRefresh.setRefreshing(false);
//            }
//        },10*1000);
//    }

    private View createHeaderView() {
        return new CustomXRefreshHeaderView(getContext());
    }

//    private View createFootView() {
//        return new CustomXRefreshFootView(getContext());
//    }

    @Override
    public void onRefreshLoadMore() {
        //        getData(mPage, true);
        UIUtil.postDelayed(new Runnable() {
            @Override
            public void run() {
                mLvLoadMore.stopLoadMore();
            }
        }, 5 * 1000);
    }
}
