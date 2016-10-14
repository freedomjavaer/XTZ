package com.ypwl.xiaotouzi.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.adapter.AllBackByPlatformAdapter;
import com.ypwl.xiaotouzi.base.BaseFragment;
import com.ypwl.xiaotouzi.bean.InvestByPlatformBidBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.event.AccountsKeptEvent;
import com.ypwl.xiaotouzi.event.BidDeletedEvent;
import com.ypwl.xiaotouzi.event.InvestPlatformRefreshEvent;
import com.ypwl.xiaotouzi.interf.INetRequestListener;
import com.ypwl.xiaotouzi.netprotocol.InvestByPlatformBidProtocol;
import com.ypwl.xiaotouzi.utils.NetworkUtils;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.ViewUtil;
import com.ypwl.xiaotouzi.view.stickylistheaders.ExpandableStickyListHeadersListView;
import com.ypwl.xiaotouzi.view.stickylistheaders.StickyListHeadersListView;

import java.util.ArrayList;
import java.util.List;

/**
 * function :全部回款---按平台显示：在投标的和结束标的
 * <p>
 * Created by tengtao on 2016/3/25.
 */
public class FragmentBidOption extends BaseFragment {
    private View mNoDataView;
    private static final String FRAGMENT_TAG = "fragment_tag";
    private String mCurrentFragmenType;//当前fragment类型
    private InvestByPlatformBidProtocol mBidProtocol;
    private List<InvestByPlatformBidBean.ListEntity> mList = new ArrayList<>();
    private boolean isBiding = true;
    private ExpandableStickyListHeadersListView mHeadersListView;
    private AllBackByPlatformAdapter mAdapter;
    private int currHightPosition;

//    private ListView mListView;
//    private AllBackByPlatformRecyclerAdapter mListViewAdapter;

    public static FragmentBidOption newInstance(@NonNull String fragmentType) {
        FragmentBidOption fragment = new FragmentBidOption();
        Bundle args = new Bundle();
        args.putString(FRAGMENT_TAG, fragmentType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCurrentFragmenType = getArguments().getString(FRAGMENT_TAG);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_back_by_platform_layout,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //粘性header listview
        mHeadersListView = (ExpandableStickyListHeadersListView) view.findViewById(R.id.invest_by_platform_expandable_sticky_list);
        mAdapter = new AllBackByPlatformAdapter(getActivity());
        mHeadersListView.setAdapter(mAdapter);
        mHeadersListView.setBeginHideAll(true);
        mHeadersListView.setOnHeaderClickListener(mHeaderClickListener);
        // listviw实现
//        mListView = (ListView) view.findViewById(R.id.invest_by_platform_recyclerview);
//        mListViewAdapter = new AllBackByPlatformRecyclerAdapter(getContext());
//        mListView.setAdapter(mListViewAdapter);

        mNoDataView = view.findViewById(R.id.layout_no_data_view);
        isBiding = mCurrentFragmenType.equals(FragmentAllBackByPlatform.FRAGMENT_TAG_BID_ING) ?true:false;
        mBidProtocol = new InvestByPlatformBidProtocol();
        mBidProtocol.loadData(isBiding? "0" : "1", mRequestListener);
        ViewUtil.showContentLayout(Const.LAYOUT_LOADING, mNoDataView, mHeadersListView);
    }

    private INetRequestListener mRequestListener = new INetRequestListener<InvestByPlatformBidBean>() {
        @Override
        public void netRequestCompleted() {
            if(!NetworkUtils.isNetworkConnected(UIUtil.getContext())){
                ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mNoDataView, mHeadersListView, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBidProtocol.loadData(isBiding? "0" : "1", mRequestListener);
                    }
                });
            }
        }

        @Override
        public void netRequestSuccess(InvestByPlatformBidBean bean, boolean isSuccess) {
            if(bean!=null && isSuccess){
                mList.clear();
                List<InvestByPlatformBidBean.ListEntity> list = bean.getList();
                if(list!=null && list.size()>0){
                    mList.addAll(list);
                    mAdapter.loadData(mList,isBiding ? "0" : "1");
//                    mListViewAdapter.loadData(mList,isBiding ? "0" : "1");
                    ViewUtil.showContentLayout(Const.LAYOUT_DATA, mNoDataView, mHeadersListView);
                }else{
                    ViewUtil.showContentLayout(Const.LAYOUT_EMPTY, mNoDataView, mHeadersListView);
                }
            }else{
                ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mNoDataView, mHeadersListView, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBidProtocol.loadData(isBiding? "0" : "1", mRequestListener);
                    }
                });
            }
        }
    };

    /**头部点击事件 */
    private StickyListHeadersListView.OnHeaderClickListener mHeaderClickListener = new StickyListHeadersListView.OnHeaderClickListener() {
        @Override
        public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {
            //获取第position个在listview中的位置
            int positionForSection = mAdapter.getPositionForSection((int)headerId);
            if(currHightPosition != headerId){//新点击的header--->先关闭原来的
                mHeadersListView.collapse(currHightPosition);
                mHeadersListView.expand(headerId);
                mAdapter.changeShowStatus(positionForSection,true);
            }else{
                if(mHeadersListView.isHeaderCollapsed(headerId)){
                    mHeadersListView.expand(headerId);
                    mAdapter.changeShowStatus(positionForSection,true);
                }else {
                    mHeadersListView.collapse(headerId);
                    mAdapter.changeShowStatus(positionForSection,false);
                }
            }
            currHightPosition = (int) headerId;
        }
    };

    /** 保存记账 */
    @Subscribe
    public void onAccountsKeptEvent(AccountsKeptEvent event){
        if(event!=null && !isDetached()){
            mBidProtocol.loadData(isBiding? "0" : "1", mRequestListener);
        }
    }

    /** 删除标的 */
    @Subscribe
    public void onBidDeletedEvent(BidDeletedEvent event){
        if(event!=null && !isDetached()){
            mBidProtocol.loadData(isBiding? "0" : "1", mRequestListener);
        }
    }

    /** 同步数据成功后刷新 */
    @Subscribe
    public void onInvestPlatformRefreshEvent(InvestPlatformRefreshEvent event){
        if(event!=null && !isDetached()){
            mBidProtocol.loadData(isBiding? "0" : "1", mRequestListener);
        }
    }
}
