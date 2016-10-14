package com.ypwl.xiaotouzi.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.adapter.HistoryAssetsByPlatformAdapter;
import com.ypwl.xiaotouzi.base.BaseFragment;
import com.ypwl.xiaotouzi.bean.HistoryAssetsByPlatformProtocolBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.event.InvestPlatformRefreshEvent;
import com.ypwl.xiaotouzi.interf.INetRequestListener;
import com.ypwl.xiaotouzi.netprotocol.HistoryAssetesByPlatformProtocol;
import com.ypwl.xiaotouzi.utils.CharacterParser;
import com.ypwl.xiaotouzi.utils.LogUtil;
import com.ypwl.xiaotouzi.utils.NetworkUtils;
import com.ypwl.xiaotouzi.utils.PinyinComparator;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.ViewUtil;
import com.ypwl.xiaotouzi.view.SideBar;
import com.ypwl.xiaotouzi.view.expandablelayout.ExpandableLayoutListView;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * function : 历史资产：按平台分类
 * <p/>
 * Created by tengtao on 2016/3/28.
 */
public class FragmentInvestHistoryByPlatform extends BaseFragment implements SideBar.OnTouchingLetterChangedListener {
    private View mNoDataView;
    private RelativeLayout mSwipeRefreshLayout;
    private HistoryAssetsByPlatformAdapter mAssetsByPlatformAdapter;
    private ExpandableLayoutListView mListView;
    private TextView mTvLetter;
    private SideBar mSideBar;
    private boolean hasAdd = false;
    private HistoryAssetesByPlatformProtocol mPlatformProtocol;
    private CharacterParser characterParser;
    private PinyinComparator pinyinComparator;
    /** 首字母消失 */
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mTvLetter.setVisibility(View.GONE);
        }
    };
    private TextView mTvHistoryHeaderEarnings, mTvHistoryHeaderRatio;//累计收益和加权年化率

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_invest_history_by_platform, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        mPlatformProtocol = new HistoryAssetesByPlatformProtocol();
        mNoDataView = view.findViewById(R.id.layout_no_data_view);

        mSwipeRefreshLayout = (RelativeLayout) view.findViewById(R.id.layout_content_data);
        mTvLetter = (TextView) view.findViewById(R.id.tv_p_letter);
        mSideBar = (SideBar) view.findViewById(R.id.sidebar);
        mSideBar.setOnTouchingLetterChangedListener(this);
        mListView = (ExpandableLayoutListView) view.findViewById(R.id.lv_invest_history_by_platform);
        if (!hasAdd) {
            View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.header_invest_history, null);
            mTvHistoryHeaderEarnings = (TextView) headerView.findViewById(R.id.tv_history_header_earnings);
            mTvHistoryHeaderRatio = (TextView) headerView.findViewById(R.id.tv_history_header_ratio);
            mListView.addHeaderView(headerView);
            headerView.setOnClickListener(null);

            hasAdd = true;
        }
        mAssetsByPlatformAdapter = new HistoryAssetsByPlatformAdapter(getActivity());
        mListView.setAdapter(mAssetsByPlatformAdapter);
        initData();
    }

    private void initData() {
        mPlatformProtocol.loadData(mRequestListener, Const.REQUEST_GET);
    }

    private INetRequestListener mRequestListener = new INetRequestListener<HistoryAssetsByPlatformProtocolBean>() {
        @Override
        public void netRequestCompleted() {
            if(!NetworkUtils.isNetworkConnected(UIUtil.getContext())){
                ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mNoDataView, mSwipeRefreshLayout, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPlatformProtocol.loadData(mRequestListener, Const.REQUEST_GET);
                    }
                });
            }
        }

        @Override
        public void netRequestSuccess(HistoryAssetsByPlatformProtocolBean bean, boolean isSuccess) {
            if (bean != null && isSuccess) {
                List<HistoryAssetsByPlatformProtocolBean.ListEntity> list = bean.getList();
                if (list.size() == 0) {
                    ViewUtil.showContentLayout(Const.LAYOUT_EMPTY, mNoDataView, mSwipeRefreshLayout, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mPlatformProtocol.loadData(mRequestListener, Const.REQUEST_GET);
                        }
                    });
                    return;
                }

                String[] tprofit = bean.getTprofit().split("\\.");
                String[] wannual = bean.getWannual().split("\\.");
                mTvHistoryHeaderEarnings.setText(Html.fromHtml("<big>" + tprofit[0] + "</big>." + tprofit[1]));
                mTvHistoryHeaderRatio.setText(Html.fromHtml("<big>" + wannual[0] + "</big>." + wannual[1] + "%"));
                List<HistoryAssetsByPlatformProtocolBean.ListEntity> lists = filledData(list);
                Collections.sort(lists, pinyinComparator);
                String[] rightLetter = new String[lists.size()];
                //初始化字母索引
                for (int i = 0; i < lists.size(); i++) {
                    rightLetter[i] = lists.get(i).getSortLetters();
                }
                List<String> resultList = new LinkedList<String>();
                for (int i = 0; i < rightLetter.length; i++) {
                    if (!resultList.contains(rightLetter[i])) {
                        resultList.add(rightLetter[i]);
                    }
                }
                String[] c = resultList.toArray(new String[resultList.size()]);
                LogUtil.e("------------c--" + c.length);
                mSideBar.setText(c);

                mAssetsByPlatformAdapter.LoadData(lists);
                ViewUtil.showContentLayout(Const.LAYOUT_DATA, mNoDataView, mSwipeRefreshLayout);
            } else {
                ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mNoDataView, mSwipeRefreshLayout, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPlatformProtocol.loadData(mRequestListener, Const.REQUEST_GET);
                    }
                });
            }

        }
    };


    /**
     * 填充数据
     *
     * @param date
     * @return
     */
    private List<HistoryAssetsByPlatformProtocolBean.ListEntity> filledData(List<HistoryAssetsByPlatformProtocolBean.ListEntity> date) {
        List<HistoryAssetsByPlatformProtocolBean.ListEntity> mSortList = date;

        for (int i = 0; i < date.size(); i++) {
            String pinyin = characterParser.getSelling(date.get(i).getP_name());
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {
                mSortList.get(i).setSortLetters(sortString.toUpperCase());
            } else {
                mSortList.get(i).setSortLetters("#");
            }
        }
        return mSortList;

    }

    @Override
    public void onTouchingLetterChanged(String s) {
        mSwipeRefreshLayout.setEnabled(false);
        int position = mAssetsByPlatformAdapter.getPositionForSection(s.charAt(0));
        if (position != -1) {
            mListView.setSelection(position);
        }
        mTvLetter.setText(s);
        if (mTvLetter.getVisibility() == View.VISIBLE) {
            UIUtil.removeCallbacksFromMainLooper(mRunnable);
        }
        UIUtil.postDelayed(mRunnable, 600);//延迟消失
        mTvLetter.setVisibility(View.VISIBLE);
    }

    @Subscribe
    public void onInvestPlatformRefreshByPlatformEvent(InvestPlatformRefreshEvent event){
        if(event!=null && !isDetached()){
            initData();
        }
    }
}
