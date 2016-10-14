package com.ypwl.xiaotouzi.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.adapter.PlatformCompareAdapter;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.bean.CommonBean;
import com.ypwl.xiaotouzi.bean.MyFocusBean;
import com.ypwl.xiaotouzi.bean.PlatformCompareBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.event.IsCollectEvent;
import com.ypwl.xiaotouzi.event.PlatformCompareRefreshEvent;
import com.ypwl.xiaotouzi.interf.INetRequestListener;
import com.ypwl.xiaotouzi.manager.EventHelper;
import com.ypwl.xiaotouzi.manager.PlatformCompareManager;
import com.ypwl.xiaotouzi.manager.db.CollectDbOpenHelper;
import com.ypwl.xiaotouzi.netprotocol.MyHasFollowedProtocol;
import com.ypwl.xiaotouzi.netprotocol.PlatformCancelFollowProtocol;
import com.ypwl.xiaotouzi.netprotocol.PlatformCompareProtocol;
import com.ypwl.xiaotouzi.netprotocol.PlatformFollowProtocol;
import com.ypwl.xiaotouzi.utils.NetworkUtils;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.ViewUtil;
import com.ypwl.xiaotouzi.view.dialog.KProgressHUDHelper;
import com.ypwl.xiaotouzi.view.dialog.lib.KProgressHUD;

import java.util.ArrayList;
import java.util.List;


/**
 * 平台对比页面
 * tengtao
 */
public class PlatformCompareActivity extends BaseActivity implements
        PlatformCompareAdapter.OnPlatformCompareChangedListener, AbsListView.OnScrollListener {
    private KProgressHUD mPbLoading;
    private View mLayoutNoDataView;
    private LinearLayout mIvTopBack;//返回
    private TextView mTvTitle,mTvContentTitle,mTvContent1,mTvContent2,mTvContent3,mTvContent4,mTvContent5;
    private ListView mLvPlatformCompare;
    private LinearLayout mCompareHeader1;

    private String pids;//参与对比的平台id
    private PlatformCompareBean mCompareBean;//获取的对比数据
    private int index;

    private List<MyFocusBean.FocusEntity> followInfos;//关注的平台
    public static List<String> followPids = new ArrayList<String>();//关注平台的id集合
    private PlatformCompareAdapter mAdapter;
    private CollectDbOpenHelper.CollectDbHelper mCollectDbHelper;//关注数据库帮助类
    private boolean isWeek = true;
    private String mLastPageName;
    private boolean isFirst;

    private PlatformFollowProtocol mFollowProtocol;//关注平台请求
    private PlatformCancelFollowProtocol mCancelFollowProtocol;//取消关注请求
    private PlatformCompareProtocol mCompareProtocol;//平台对比数据请求
    private MyHasFollowedProtocol mHasFollowedProtocol;//我已关注的平台数据请求

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_platform_compare);
        mPbLoading = KProgressHUDHelper.createLoading(this);
        isFirst = true;
        //实例化数据库帮助类
        mCollectDbHelper = CollectDbOpenHelper.CollectDbHelper.getInstance(mActivity);
        mLastPageName = getIntent().getStringExtra(Const.KEY_INTENT_JUMP_FROM_DATA);
        mCompareProtocol = new PlatformCompareProtocol();
        initView();
        initData();
    }

    private void initView() {
        mLayoutNoDataView = findViewById(R.id.layout_no_data_view);
        mCompareHeader1 = (LinearLayout) findViewById(R.id.compare_header1);
        mIvTopBack = (LinearLayout) findViewById(R.id.layout_title_back);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mLvPlatformCompare = (ListView) findViewById(R.id.listview_platform_compare);
        mAdapter = new PlatformCompareAdapter(mActivity);
        mLvPlatformCompare.setAdapter(mAdapter);
        ViewUtil.showContentLayout(Const.LAYOUT_LOADING, mLayoutNoDataView, mLvPlatformCompare);//默认显示加载中视图
        mIvTopBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlatformCompareManager.getList().clear();
                finish();
            }
        });
        initHeader();//初始化浮条
        if (!TextUtils.isEmpty(mLastPageName)){
            ((TextView)findView(R.id.tv_title_back)).setText(mLastPageName);
        }
    }

    private void initHeader() {
        mTvContentTitle = (TextView) findViewById(R.id.tv_platform_compare_above_header);
        mTvContent1 = (TextView) findViewById(R.id.tv_compare_content1);
        mTvContent2 = (TextView) findViewById(R.id.tv_compare_content2);
        mTvContent3 = (TextView) findViewById(R.id.tv_compare_content3);
        mTvContent4 = (TextView) findViewById(R.id.tv_compare_content4);
        mTvContent5 = (TextView) findViewById(R.id.tv_compare_content5);
    }

    private void initData() {
        mTvTitle.setText(getString(R.string.platform_compare));
        getFollowData();//获取关注的平台信息
        onSelected(isWeek);//默认获取按周对比平台信息
    }

    /** 获取已关注平台数据 */
    private void getFollowData() {
        if(mHasFollowedProtocol==null){
            mHasFollowedProtocol = new MyHasFollowedProtocol();
        }
        mHasFollowedProtocol.loadData(mHasFollowedListener,Const.REQUEST_GET);
    }

    private INetRequestListener mHasFollowedListener = new INetRequestListener<MyFocusBean>() {
        @Override
        public void netRequestCompleted() {

        }

        @Override
        public void netRequestSuccess(MyFocusBean bean, boolean isSuccess) {
            if(bean!=null && isSuccess){
                followInfos = bean.getList();
                if (followInfos != null && followInfos.size() > 0) {
                    followPids.clear();
                    for (int i = 0; i < followInfos.size(); i++) {
                        followPids.add(followInfos.get(i).getPid());
                    }
                    if(mCompareBean !=null && mCompareBean.getPid().size()>0)
                        mAdapter.updateList(mCompareBean);
                }
            }else{
                UIUtil.showToastShort("获取关注信息异常");
            }
        }
    };

    /**
     * 获取平台对比数据
     * @param isWeekSelected true:按周,false:按月
     */
    @Override
    public void onSelected(boolean isWeekSelected) {
        isWeek = isWeekSelected;
        //获取对比平台的pid
        if (PlatformCompareManager.getList().size() > 0) {
            pids = pidsToString(PlatformCompareManager.getList());
        } else {
            UIUtil.showToastShort(getString(R.string.no_choose_compare_platform));
            return;
        }
        if(!isFirst) {
            isFirst = false;
            mPbLoading.show();
        }
        mCompareProtocol.loadData(pids, isWeekSelected?"1":"2", mCompareListener);//1：周；2：月
    }

    private INetRequestListener mCompareListener = new INetRequestListener<PlatformCompareBean>() {
        @Override
        public void netRequestCompleted() {
            mPbLoading.dismiss();
            if(!NetworkUtils.isNetworkConnected(UIUtil.getContext())){
                ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mLayoutNoDataView, mLvPlatformCompare, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        initData();
                    }
                });
            }
        }

        @Override
        public void netRequestSuccess(PlatformCompareBean bean, boolean isSuccess) {
            if(bean!=null && isSuccess){
                mCompareBean = bean;
                if(bean.getPid().size()>0){
                    showData();
                }
            }else{
                ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mLayoutNoDataView, mLvPlatformCompare, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        initData();
                    }
                });
            }
        }
    };

    /** 展示数据 */
    private void showData() {
        if(followInfos==null){
            followInfos = new ArrayList<MyFocusBean.FocusEntity>();
        }
        ViewUtil.showContentLayout(Const.LAYOUT_DATA, mLayoutNoDataView, mLvPlatformCompare);
        mLvPlatformCompare.setDividerHeight(0);//不设分割线
        mAdapter.updateList(mCompareBean);//更新所有数据
        mAdapter.setOnPlatformCompareChangedListener(this);
        mLvPlatformCompare.setOnScrollListener(this);
        refreshHeader1(true);
    }

    //头部数据
    private void refreshHeader1(boolean isFirst) {
        int num = PlatformCompareManager.getList().size();
        if (num > 0) {
            mTvContent1.setText(mCompareBean.getP_name().get(0));
            if (num > 1) {
                mTvContent2.setText(mCompareBean.getP_name().get(1));
                if (num > 2) {
                    mTvContent3.setText(mCompareBean.getP_name().get(2));
                    if (num > 3) {
                        mTvContent4.setText(mCompareBean.getP_name().get(3));
                        if (num > 4) {
                            mTvContent5.setText(mCompareBean.getP_name().get(4));
                        }
                    }
                }
            }
        }
        mTvContentTitle.setText(isFirst ? "数据对比" : "信息对比");
        mTvContent1.setVisibility(num > 0 ? View.VISIBLE : View.GONE);
        mTvContent2.setVisibility(num > 1 ? View.VISIBLE : View.GONE);
        mTvContent3.setVisibility(num > 2 ? View.VISIBLE : View.GONE);
        mTvContent4.setVisibility(num > 3 ? View.VISIBLE : View.GONE);
        mTvContent5.setVisibility(num > 4 ? View.VISIBLE : View.GONE);
    }


    /** 关注一个平台 */
    @Override
    public void onFollowed(PlatformCompareBean datas, int position) {
        //关注这个平台
        index = position;
        mPbLoading.show();
        if(mFollowProtocol==null){
            mFollowProtocol = new PlatformFollowProtocol();
        }
        mFollowProtocol.loadData(datas.getPid().get(position),mFollowListener);
    }

    private INetRequestListener mFollowListener = new INetRequestListener<CommonBean>() {
        @Override
        public void netRequestCompleted() {
            mPbLoading.dismiss();
        }

        @Override
        public void netRequestSuccess(CommonBean bean, boolean isSuccess) {
            if(bean!=null && isSuccess){
                UIUtil.showToastShort(getString(R.string.platform_compare_follow_success));
                //更改ui和数据属性
                followPids.add(mCompareBean.getPid().get(index));
                mCollectDbHelper.insert(mCompareBean.getPid().get(index));//插入数据库
                mAdapter.updateList(mCompareBean);//更新关注数据
                EventHelper.post(new IsCollectEvent());
            }else{
                UIUtil.showToastShort(getString(R.string.platform_compare_follow_fault));
            }
        }
    };

    /** 取消一个平台关注 */
    @Override
    public void onAbolishFollow(PlatformCompareBean datas, int position) {
        index = position;
        mPbLoading.show();
        if(mCancelFollowProtocol==null){
            mCancelFollowProtocol = new PlatformCancelFollowProtocol();
        }
        mCancelFollowProtocol.loadData(datas.getPid().get(position),mCancelFollowListener);
    }

    private INetRequestListener mCancelFollowListener = new INetRequestListener<CommonBean>() {
        @Override
        public void netRequestCompleted() {
            mPbLoading.dismiss();
        }

        @Override
        public void netRequestSuccess(CommonBean bean, boolean isSuccess) {
            if(bean!=null && isSuccess){
                //取消关注成功
                mCollectDbHelper.deleteByPid(mCompareBean.getPid().get(index));
                followPids.remove(mCompareBean.getPid().get(index));
                mAdapter.updateList(mCompareBean);
                UIUtil.showToastShort("已取消关注");
                EventHelper.post(new IsCollectEvent());
            }else{
                UIUtil.showToastShort("操作失败");
            }
        }
    };

    @Override
    public void onDeleted(int position) {//删除对比平台
        if(mCompareBean.getPid().size()<2){
            finish();
            return;
        }
        deleteData(mCompareBean.getPid(),position);
        deleteData(mCompareBean.getP_logo(),position);
        deleteData(mCompareBean.getP_name(),position);
        deleteData(mCompareBean.getLaunch_time(),position);
        deleteData(mCompareBean.getCity(),position);
        deleteData(mCompareBean.getC_type(),position);
        deleteData(mCompareBean.getC_capital(),position);
        deleteData(mCompareBean.getAoc(),position);
        deleteData(mCompareBean.getGoi(), position);
        deleteData(mCompareBean.getTrust_funds(), position);
        deleteData(mCompareBean.getG_mode(),position);
        deleteData(mCompareBean.getRecharge_rule(),position);
        deleteData(mCompareBean.getWithdraw_rule(),position);
        deleteData(mCompareBean.getM_cost(),position);
        deleteData(mCompareBean.getF_situation(),position);
        deleteData(mCompareBean.getAuto_invest(),position);
        deleteData(mCompareBean.getData_cjl(),position);
        deleteData(mCompareBean.getData_ll(),position);
        deleteData(mCompareBean.getData_tzrs(),position);
        deleteData(mCompareBean.getData_jkrs(),position);
        deleteData(mCompareBean.getData_rjtzje(),position);
        deleteData(mCompareBean.getData_rjjkje(),position);
        deleteData(mCompareBean.getData_jkbs(),position);
        deleteData(mCompareBean.getData_pjjkqx(),position);
        deleteData(mCompareBean.getLevel(), position);
        refreshHeader1(true);
        mAdapter.updateList(mCompareBean);//更新所有数据
    }

    /** 接收刷新数据事件 */
    @Subscribe
    public void onPlatformCompareRefreshEvent(PlatformCompareRefreshEvent event) {
        if(event!=null && event.isExecuteRefresh == true){
            if(event.scope==1){
                ViewUtil.showContentLayout(Const.LAYOUT_LOADING, mLayoutNoDataView, mLvPlatformCompare);
                onSelected(PlatformCompareAdapter.isWeekSelected);
            }else if(event.scope==0){
                getFollowData();//更新关注数据
            }
        }
    }

    /** listview滚动监听事件 */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // 滑动的过程中
        if (mCompareBean == null)
            return;
        int off = (PlatformCompareManager.getList().size() == 5) ? 1 : 0;
        refreshHeader1(firstVisibleItem >= (mCompareBean.getPid().size() + 1 - off) + 10 ? false : true);
        mCompareHeader1.setVisibility((firstVisibleItem >= (mCompareBean.getPid().size() + 1 - off)) ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onDestroy() {
        PlatformCompareAdapter.isWeekSelected = true;
        PlatformCompareManager.getList().clear();//销毁对比清空数据
        PlatformCompareManager.hasChooseCompare = null;//置空
        super.onDestroy();
    }

    private String pidsToString(List<String> list) {
        String s = "";
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (i == list.size() - 1) {
                    s = s + list.get(i);
                } else {
                    s = s + list.get(i) + ",";
                }
            }
            return s;
        }
        return null;
    }

    private void deleteData(List<String> list, int position){
        list.remove(position);
    }
}