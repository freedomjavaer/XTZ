package com.ypwl.xiaotouzi.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.alibaba.fastjson.JSON;
import com.andview.refreshview.XRefreshView;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.adapter.FilterItemsAdapter;
import com.ypwl.xiaotouzi.adapter.NetPlatformAdapter;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.bean.AllPlatformBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.common.ServerStatus;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.common.UmengEventID;
import com.ypwl.xiaotouzi.interf.IRequestCallback;
import com.ypwl.xiaotouzi.manager.UmengEventHelper;
import com.ypwl.xiaotouzi.manager.net.NetHelper;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.StringUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.UmengUtils;
import com.ypwl.xiaotouzi.utils.ViewUtil;
import com.ypwl.xiaotouzi.view.CheckableSortItem;
import com.ypwl.xiaotouzi.view.CustomXRefreshHeaderView;
import com.ypwl.xiaotouzi.view.rangebar.RangeBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * function :网贷平台页面
 * <p/>
 * Created by tengtao on 2016/3/14.
 */
public class NetPlatformActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout mLayoutTitle;
    private ProgressBar mPbLoading;
    private View mLayoutBack;
    /** 标题栏右边文字按钮 */
    public TextView mTitleRight;
    /** 排序项--评级,投资人数,利率 */
    private CheckableSortItem mSortItemPingji, mSortItemRenshu, mSortItemLilv;
    /** 无数据视图 ,刷新视图,数据内容视图 */
    private View mLayoutNoDataView;
    //    private SwipeRefreshLayout mLayoutContent;
    private XRefreshView mLayoutContent;
    private ListView mListViewContent;

    /** 当前请求类型  默认成交率 */
    private int mCurrentType = Const.PARAM_TYPE_PINGJI;
    /** 当前请求排序方式 初始化 */
    private int mCurrentOrder = Const.PARAM_ORDER_JIANG;
    /** 临时评级排序方式 **/
    private int mTempOrderPingji = Const.PARAM_ORDER_JIANG;
    /** 临时投资人数排序方式 **/
    private int mTempOrderTouZiRenShu = Const.PARAM_ORDER_SHENG;
    /** 临时利率排序方式 **/
    private int mTempOrderLilv = Const.PARAM_ORDER_SHENG;
    /** 当前请求页数 默认第一页 */
    private int mCurrentPage = 1;
    /** 选择的当前期限、利率 */
    private String mCurrentPeriod, mCurrentRate;
    /** 当前筛选条件显示控件 */
    private GridView mChoosedItemsGridView;

    private NetPlatformAdapter mContentAdapter;
    private boolean isLoadMore, isLoading, isFirst, needScroll, isRefresh;
    /** 筛选弹窗 */
    private PopupWindow mFilterWindow;
    /** 筛选弹窗布局 */
    private View mContentViewPopuWindow;
    private FilterItemsAdapter mAdapterFilterItems;
    /** toggle按钮选择状态容器，id--对应--是否选择 */
    private Map<Integer, Boolean> mCheckMap = new HashMap<>();
    private Map<Integer, Boolean> mTempMap = new HashMap<>();
    /** 标期和利率 */
    public int period_down = 0;
    public int period_up = 36;
    public int rate_down = 5;
    public int rate_up = 30;
    private static final int PERIOD_COUNT = 37;
    private static final int RATE_COUNT = 26;
    private static final float PERIOD_HEIGHT = 0;
    private static final float RATE_HEIGHT = 0;
    /** toggle按钮id数组 */
    public static int[] mToggleBtnsArr = new int[]{
            R.id.ipcp_choice_btn_number_1_1, R.id.ipcp_choice_btn_number_1_2, R.id.ipcp_choice_btn_number_1_3, R.id.ipcp_choice_btn_number_2_1,
            R.id.ipcp_choice_btn_number_2_2, R.id.ipcp_choice_btn_number_2_3, R.id.ipcp_choice_btn_number_2_4, R.id.ipcp_choice_btn_number_3_1,
            R.id.ipcp_choice_btn_number_3_2, R.id.ipcp_choice_btn_number_3_3, R.id.ipcp_choice_btn_number_3_4, R.id.ipcp_choice_btn_number_4_1,
            R.id.ipcp_choice_btn_number_4_2, R.id.ipcp_choice_btn_number_4_3, R.id.ipcp_choice_btn_number_4_4, R.id.ipcp_choice_btn_number_4_5,
            R.id.ipcp_choice_btn_number_4_6, R.id.ipcp_choice_btn_number_4_7};

    private ArrayList<Integer> guarantee_url, background_url, btype_url, district_url;
    public static boolean isShaiXuan = false;
    private View mShaiXuanLayout;
    private ImageView mIvShaiXuanArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_net_platforms);
        initView();
        initListener();
        initData();
    }

    private void initView() {
        //title部分
        mLayoutTitle = (LinearLayout) findViewById(R.id.layout_net_platforms_title);
        mTitleRight = (TextView) findViewById(R.id.platform_choice_top_right_tv);
        mShaiXuanLayout = findViewById(R.id.layout_platform_shaixuan);
        mIvShaiXuanArrow = (ImageView) findViewById(R.id.iv_platform_shaixuan_arrow);
        mLayoutBack = findViewById(R.id.layout_net_platform_back);
        //筛选条件显示控件
        mChoosedItemsGridView = (GridView) findViewById(R.id.gv_choose_items);
        mChoosedItemsGridView.setVisibility(View.GONE);
        //排序控件
        mSortItemPingji = (CheckableSortItem) findViewById(R.id.sort_item_pingji);
        mSortItemRenshu = (CheckableSortItem) findViewById(R.id.sort_item_renshu);
        mSortItemLilv = (CheckableSortItem) findViewById(R.id.sort_item_lilv);
        //content
        mListViewContent = (ListView) findViewById(R.id.lv_content_data);
        //刷新显示控件
        mPbLoading = (ProgressBar) findViewById(R.id.layout_hint_pb_loading);
        mLayoutNoDataView = findViewById(R.id.layout_no_data_view);
//        mLayoutContent = (SwipeRefreshLayout) findViewById(R.id.layout_content);
//        mLayoutContent.setOnRefreshListener(this);
        mLayoutContent = findView(R.id.layout_content);
        mLayoutContent.setMoveForHorizontal(true);
        mLayoutContent.setCustomHeaderView(new CustomXRefreshHeaderView(this));
        mLayoutContent.setPullLoadEnable(false);
        mLayoutContent.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh() {
                if (isFirst) {
                    mSortItemPingji.toggle();
                }
                isFirst = false;
                isRefresh = true;
                isLoadMore = false;
                mCurrentPage = 1;
                requestNetData();
            }
        });

        ViewUtil.showContentLayout(Const.LAYOUT_LOADING, mLayoutNoDataView, mLayoutContent);//默认显示加载中视图
    }

    private void initListener() {
        findViewById(R.id.platform_search).setOnClickListener(this);
        mChoosedItemsGridView.setOnItemClickListener(mFilterOnItemClickListener);
        mListViewContent.setOnScrollListener(new MyOnScrollListener());
        mShaiXuanLayout.setOnClickListener(this);
        mSortItemPingji.setOnClickListener(this);
        mSortItemRenshu.setOnClickListener(this);
        mSortItemLilv.setOnClickListener(this);
        mLayoutBack.setOnClickListener(this);
    }

    /** 初始化数据 */
    private void initData() {
        isFirst = true;
        mAdapterFilterItems = new FilterItemsAdapter(mActivity);
        mChoosedItemsGridView.setAdapter(mAdapterFilterItems);
        mContentAdapter = new NetPlatformAdapter(mActivity);
        mListViewContent.setAdapter(mContentAdapter);
        //初始化状态
        mSortItemPingji.clearStatus(true);
        mSortItemRenshu.clearStatus(true);
        mSortItemLilv.clearStatus(true);
        //加载网络数据
        requestNetData();
    }

//    @Override
//    public void onRefresh() {
//        if (isFirst) {
//            mSortItemPingji.toggle();
//        }
//        isFirst = false;
//        isRefresh = true;
//        isLoadMore = false;
//        mCurrentPage = 1;
//        mLayoutContent.setRefreshing(true);
//        requestNetData();
//    }

    /** 滑动加载更多事件监听 */
    private class MyOnScrollListener implements AbsListView.OnScrollListener {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (mListViewContent.getLastVisiblePosition() == view.getAdapter().getCount() - 1 && !isLoading) {
                isLoadMore = true;
                requestNetData();
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        }
    }

    /** 请求数据 */
    private void requestNetData() {
        String url = StringUtil.format(URLConstant.ALL_PLATFORM_DEFAULT, mCurrentType, mCurrentOrder, mCurrentPage);
        url = handleFilterData(url) + "&first=" + (isFirst ? 1 : 0);
        NetHelper.get(url, new LoadPlatformDataCallBack());
    }

    /** 网贷平台加载数据回调接口 */
    private class LoadPlatformDataCallBack extends IRequestCallback<String> {
        @Override
        public void onStart() {
            isLoading = true;
            mContentAdapter.setLoading(true);
            if (isLoadMore) {
                mPbLoading.setVisibility(View.VISIBLE);
            } else {
                mPbLoading.setVisibility(isFirst || isRefresh ? View.GONE : View.VISIBLE);
            }
        }

        @Override
        public void onFailure(Exception e) {
            isLoading = false;
            isRefresh = false;
            mLayoutContent.stopRefresh();
            mContentAdapter.setLoading(false);
            mPbLoading.setVisibility(View.GONE);
            UIUtil.showToastShort("获取数据失败");
            ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mLayoutNoDataView, mLayoutContent, "出错了\n点击重试", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mAdapterFilterItems.getDataList().size() == 0) {
                        requestNetData();
                        return;
                    }
                    ViewUtil.showContentLayout(Const.LAYOUT_EMPTY, mLayoutNoDataView, mLayoutContent);
                }
            });
        }

        @Override
        public void onSuccess(String jsonStr) {
            isLoading = false;
            isRefresh = false;
            mLayoutContent.stopRefresh();
            mContentAdapter.setLoading(false);
            mPbLoading.setVisibility(View.GONE);
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);
                int status = jsonObject.optInt(Const.JSON_KEY_status, -1);
                switch (status) {
                    case ServerStatus.SERVER_STATUS_OK:
                        try {
                            AllPlatformBean bean = JSON.parseObject(jsonStr, AllPlatformBean.class);
                            if (bean == null || bean.getData() == null) {
                                this.onFailure(null);
                                return;
                            }
                            //无数据，空视图
                            if (bean.getData().size() == 0 && !isLoadMore) {
                                ViewUtil.showContentLayout(Const.LAYOUT_EMPTY, mLayoutNoDataView, mLayoutContent, "暂无数据");
                                return;
                            }
                            //没有更多数据
                            if (bean.getData().size() == 0 && mContentAdapter.getDataList().size() != 0 && isLoadMore) {
                                UIUtil.showToastShort("没有更多数据");
                                return;
                            }
                            ViewUtil.showContentLayout(Const.LAYOUT_DATA, mLayoutNoDataView, mLayoutContent);
                            mContentAdapter.laodData(bean.getData(), isLoadMore);
                            ++mCurrentPage;//还有数据
                            if (needScroll) {
                                mListViewContent.smoothScrollToPosition(0);
                                needScroll = false;
                            }
                        } catch (Exception e) {
                            this.onFailure(null);
                        }
                        break;
                    default:
                        this.onFailure(null);
                        break;
                }
            } catch (JSONException e) {
                this.onFailure(e);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.platform_search://点击搜索框
                hidePopuwindow();
                startActivity(NetPlatformSearchActivity.class);
                break;
            case R.id.layout_platform_shaixuan://点击右上角
                if ("取消".equals(mTitleRight.getText().toString())) {
                    UmengEventHelper.onEvent(UmengEventID.NetPlatformFilterCancleButton);
                    isShaiXuan = false;
                    restoreView();
                } else {
                    UmengEventHelper.onEvent(UmengEventID.NetPlatformFilterButton);
                    isShaiXuan = true;
                    mTitleRight.setText("取消");
                    mIvShaiXuanArrow.setImageResource(R.mipmap.btn_im_reverse);
                    mLayoutBack.setVisibility(View.INVISIBLE);
                    showPopuwindow();
                }
                break;
            case R.id.sort_item_pingji:// 点击评级排序项
                if (isLoading) {
                    return;
                }
                mTempOrderPingji = mTempOrderPingji == Const.PARAM_ORDER_JIANG && !isFirst ? Const.PARAM_ORDER_SHENG : Const.PARAM_ORDER_JIANG;
                changeOrderStatus(true, false, false, mTempOrderPingji);
                UmengEventHelper.onEvent(UmengEventID.NetPlatformListRateButton);
                break;
            case R.id.sort_item_renshu:// 点击投资人数排序项
                if (isLoading) {
                    return;
                }
                mTempOrderTouZiRenShu = mTempOrderTouZiRenShu == Const.PARAM_ORDER_JIANG ? Const.PARAM_ORDER_SHENG : Const.PARAM_ORDER_JIANG;
                changeOrderStatus(false, true, false, mTempOrderTouZiRenShu);
                UmengEventHelper.onEvent(UmengEventID.NetPlatformListInvestorButton);
                break;
            case R.id.sort_item_lilv:// 点击利率排序项
                if (isLoading) {
                    return;
                }
                mTempOrderLilv = mTempOrderLilv == Const.PARAM_ORDER_JIANG ? Const.PARAM_ORDER_SHENG : Const.PARAM_ORDER_JIANG;
                changeOrderStatus(false, false, true, mTempOrderLilv);
                UmengEventHelper.onEvent(UmengEventID.NetPlatformListAverageInterestRateButton);
                break;
            case R.id.layout_net_platform_back://返回
                if (!isShaiXuan)
                    finish();
                break;
            case R.id.view_footer://收回弹窗
                hidePopuwindow();
                break;
        }
    }

    /***
     * 根据选择重置排序条件
     *
     * @param isPingji     是否选择了评级
     * @param isRenjhu     是否选择了投资人数
     * @param isLilv       是否选择了利率
     * @param currentOrder 当前的排序模式
     */
    private void changeOrderStatus(boolean isPingji, boolean isRenjhu, boolean isLilv, int currentOrder) {
        isFirst = false;
        needScroll = true;
        isLoadMore = false;
        mCurrentPage = 1;
        mSortItemPingji.clearStatus(!isPingji);
        mSortItemPingji.resetCheck(!isPingji);
        mSortItemRenshu.clearStatus(!isRenjhu);
        mSortItemRenshu.resetCheck(!isRenjhu);
        mSortItemLilv.clearStatus(!isLilv);
        mSortItemLilv.resetCheck(!isLilv);
        mCurrentOrder = currentOrder;

        mTempOrderPingji = isPingji ? mTempOrderPingji : Const.PARAM_ORDER_SHENG;
        mTempOrderTouZiRenShu = isRenjhu ? mTempOrderTouZiRenShu : Const.PARAM_ORDER_SHENG;
        mTempOrderLilv = isLilv ? mTempOrderLilv : Const.PARAM_ORDER_SHENG;
        if (isPingji) {
            mSortItemPingji.toggle();
            mCurrentType = Const.PARAM_TYPE_PINGJI;
        } else if (isRenjhu) {
            mSortItemRenshu.toggle();
            mCurrentType = Const.PARAM_TYPE_TOUZIRENSHU;
        } else if (isLilv) {
            mSortItemLilv.toggle();
            mCurrentType = Const.PARAM_TYPE_LILV;
        }
        requestNetData();
    }

    /** 显示筛选弹窗 */
    private void showPopuwindow() {
        if (mFilterWindow == null) {
            mContentViewPopuWindow = View.inflate(mActivity, R.layout.item_platform_choice_popupwindow, null);
            mFilterWindow = new PopupWindow(mContentViewPopuWindow, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mContentViewPopuWindow.findViewById(R.id.view_footer).setOnClickListener(this);
            mFilterWindow.setOutsideTouchable(true);
            mFilterWindow.setBackgroundDrawable(new BitmapDrawable());
            mFilterWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    mLayoutBack.setVisibility(View.VISIBLE);
                    UIUtil.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isShaiXuan = false;
                            mTitleRight.setText("筛选");
                            mTempMap.clear();
                            mIvShaiXuanArrow.setImageResource(R.mipmap.btn_im);
                        }
                    }, 300);
                }
            });
            //            int height = mFilterWindow.getHeight();
            mFilterWindow.setAnimationStyle(R.style.platform_popwin_anim_style);
            for (int toggleBtnId : mToggleBtnsArr) {
                ToggleButton tBtn = (ToggleButton) mContentViewPopuWindow.findViewById(toggleBtnId);
                tBtn.setOnCheckedChangeListener(mOnCheckedChangeListener);
                mCheckMap.put(toggleBtnId, tBtn.isChecked());
            }
        }
        initWindowData(); //初始化数据
        mFilterWindow.showAsDropDown(mLayoutTitle);
    }

    /** 初始化筛选窗体内容数据 */
    @SuppressLint("SetTextI18n")
    @SuppressWarnings("StatementWithEmptyBody")
    private void initWindowData() {
        // 初始化选择状态
        for (Map.Entry<Integer, Boolean> entry : mCheckMap.entrySet()) {
            ((ToggleButton) mContentViewPopuWindow.findViewById(entry.getKey())).setChecked(entry.getValue());
        }

        //标期:0-36(37)
        RangeBar rangeBarBidPeriod = (RangeBar) mContentViewPopuWindow.findViewById(R.id.ipcp_choice_rb_period);
        final TextView rangePeriodDown = (TextView) mContentViewPopuWindow.findViewById(R.id.ipcp_choice_rb_period_down);
        final TextView rangPeriodUp = (TextView) mContentViewPopuWindow.findViewById(R.id.ipcp_choice_rb_period_up);
        //利率:5-30(26)
        RangeBar rangeBarBidLilv = (RangeBar) mContentViewPopuWindow.findViewById(R.id.ipcp_choice_rb_rate);
        final TextView rangeLilvDown = (TextView) mContentViewPopuWindow.findViewById(R.id.ipcp_choice_rb_rate_down);
        final TextView rangLilvUp = (TextView) mContentViewPopuWindow.findViewById(R.id.ipcp_choice_rb_rate_up);
        mContentViewPopuWindow.findViewById(R.id.ipcp_choice_btn_sure).setOnClickListener(mSureFilterOnClickListener);

        rangeBarBidPeriod.setTickCount(PERIOD_COUNT);
        rangeBarBidPeriod.setTickHeight(PERIOD_HEIGHT);
        if (StringUtil.isEmptyOrNull(mCurrentPeriod)) {
            rangeBarBidPeriod.setLeft(0);
            rangeBarBidPeriod.setRight(36);
            rangeBarBidPeriod.setThumbIndices(0, 36);
            rangePeriodDown.setText(0 + "个月");//初始化rangebar旁边的textview
            rangPeriodUp.setText(36 + "个月");
        } else {
            rangeBarBidPeriod.setLeft(period_down);
            rangeBarBidPeriod.setRight(period_up);
            rangeBarBidPeriod.setThumbIndices(period_down, period_up);
            rangePeriodDown.setText(period_down + "个月");//初始化rangebar旁边的textview
            rangPeriodUp.setText(period_up + "个月");
        }
        rangeBarBidPeriod.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onIndexChangeListener(RangeBar rangeBar, int leftThumbIndex, int rightThumbIndex) {
                if (0 == leftThumbIndex && 36 == rightThumbIndex) {
                } else {
                    if (leftThumbIndex < 0) { //滑动超出边界
                        leftThumbIndex = 0;
                        rangeBar.setThumbIndices(0, rightThumbIndex);
                    }
                    if (rightThumbIndex > 36) {
                        rightThumbIndex = 36;
                        rangeBar.setThumbIndices(leftThumbIndex, 36);
                    }
                    mCurrentPeriod = leftThumbIndex + "~" + rightThumbIndex + "个月";//实时数据
                }
                period_down = leftThumbIndex;
                period_up = rightThumbIndex;
                rangePeriodDown.setText(leftThumbIndex + "个月");
                rangPeriodUp.setText(rightThumbIndex + "个月");
            }
        });


        rangeBarBidLilv.setTickCount(RATE_COUNT);
        rangeBarBidLilv.setTickHeight(RATE_HEIGHT);
        if (StringUtil.isEmptyOrNull(mCurrentRate)) {
            rangeBarBidLilv.setLeft(0);
            rangeBarBidLilv.setRight(25);
            rangeBarBidLilv.setThumbIndices(0, 25);
            rangeLilvDown.setText(5 + "%");
            rangLilvUp.setText(30 + "%");
        } else {
            rangeBarBidLilv.setLeft(rate_down - 5);
            rangeBarBidLilv.setRight(rate_up - 5);
            rangeBarBidLilv.setThumbIndices(rate_down - 5, rate_up - 5);
            rangeLilvDown.setText(rate_down + "%");
            rangLilvUp.setText(rate_up + "%");
        }
        rangeBarBidLilv.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onIndexChangeListener(RangeBar rangeBar, int leftThumbIndex,
                                              int rightThumbIndex) {
                if (leftThumbIndex < 0) {//滑动超出边界
                    leftThumbIndex = 0;
                    rangeBar.setThumbIndices(0, rightThumbIndex);
                }
                if (rightThumbIndex > 25) {
                    rightThumbIndex = 25;
                    rangeBar.setThumbIndices(leftThumbIndex, 25);
                }
                rate_down = leftThumbIndex + 5;
                rate_up = rightThumbIndex + 5;
                if (0 == leftThumbIndex && 25 == rightThumbIndex) {
                    //do nothing
                } else {
                    mCurrentRate = rate_down + "~" + rate_up + "%";
                }
                rangeLilvDown.setText(rate_down + "%");
                rangLilvUp.setText(rate_up + "%");
            }
        });
    }

    /** toggle按钮切换事件 */
    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (mTempMap != null) {
//                mCheckMap.put(buttonView.getId(), isChecked);
                mTempMap.put(buttonView.getId(), isChecked);
            }
        }
    };

    /** 确定筛选按钮点击事件 */
    private View.OnClickListener mSureFilterOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isFirst) {
                mSortItemPingji.toggle();
            }
            isFirst = false;
            isLoadMore = false;
            mCurrentPage = 1;
            mTitleRight.setText("筛选");
            mIvShaiXuanArrow.setImageResource(R.mipmap.btn_im);
            mCheckMap.putAll(mTempMap);
            mTempMap.clear();
            hidePopuwindow();
            requestNetData();
            UmengUtils.uMengTopChoice(mCheckMap, mCurrentPeriod, mCurrentRate);//提交筛选统计
            UmengEventHelper.onEvent(UmengEventID.NetPlatformFilterConfirmButton);
        }
    };

    /** 处理筛选条件 -->请求url */
    private String handleFilterData(String url) {
        List<String> filterItemsList = new ArrayList<>();
        if (guarantee_url == null) {
            guarantee_url = new ArrayList<>();
        } else {
            guarantee_url.clear();
        }
        if (background_url == null) {
            background_url = new ArrayList<>();
        } else {
            background_url.clear();
        }
        if (btype_url == null) {
            btype_url = new ArrayList<>();
        } else {
            btype_url.clear();
        }
        if (district_url == null) {
            district_url = new ArrayList<>();
        } else {
            district_url.clear();
        }

        for (Map.Entry<Integer, Boolean> entry : mCheckMap.entrySet()) {
            if (!entry.getValue()) {
                continue;
            }//没有选中就继续
            int id = entry.getKey();
            switch (id) {
                case R.id.ipcp_choice_btn_number_1_1:
                    //                    url += "&guarantee=" + Const.PARAM_GUARANTEE_TUOGUAN;
                    guarantee_url.add(Const.PARAM_GUARANTEE_TUOGUAN);
                    break;
                case R.id.ipcp_choice_btn_number_1_2:
                    //                    url += "&guarantee=" + Const.PARAM_GUARANTEE_DANBAO;
                    guarantee_url.add(Const.PARAM_GUARANTEE_DANBAO);
                    break;
                case R.id.ipcp_choice_btn_number_1_3://视频认证
                    url += "&video_atc=" + Const.PARAM_VIDEO_ATC;
                    break;
                case R.id.ipcp_choice_btn_number_2_1:
                    //                    url += "&background=" + Const.PARAM_BACKGROUND_YINGHANG;
                    background_url.add(Const.PARAM_BACKGROUND_YINGHANG);
                    break;
                case R.id.ipcp_choice_btn_number_2_2:
                    //                    url += "&background=" + Const.PARAM_BACKGROUND_FENGTOU;
                    background_url.add(Const.PARAM_BACKGROUND_FENGTOU);
                    break;
                case R.id.ipcp_choice_btn_number_2_3:
                    //                    url += "&background=" + Const.PARAM_BACKGROUND_GUOZI;
                    background_url.add(Const.PARAM_BACKGROUND_GUOZI);
                    break;
                case R.id.ipcp_choice_btn_number_2_4:
                    //                    url += "&background=" + Const.PARAM_BACKGROUND_SHANGSHI;
                    background_url.add(Const.PARAM_BACKGROUND_SHANGSHI);
                    break;
                case R.id.ipcp_choice_btn_number_3_1:
                    //                    url += "&btype=" + Const.PARAM_BTYPE_DIYA;
                    btype_url.add(Const.PARAM_BTYPE_DIYA);
                    break;
                case R.id.ipcp_choice_btn_number_3_2:
                    //                    url += "&btype=" + Const.PARAM_BTYPE_XINYONG;
                    btype_url.add(Const.PARAM_BTYPE_XINYONG);
                    break;
                case R.id.ipcp_choice_btn_number_3_3:
                    //                    url += "&btype=" + Const.PARAM_BTYPE_PIAOJU;
                    btype_url.add(Const.PARAM_BTYPE_PIAOJU);
                    break;
                case R.id.ipcp_choice_btn_number_3_4:
                    //                    url += "&btype=" + Const.PARAM_BTYPE_ZAIQUAN;
                    btype_url.add(Const.PARAM_BTYPE_ZAIQUAN);
                    break;
                case R.id.ipcp_choice_btn_number_4_1:
                    //                    url += "&district=" + Const.PARAM_DISTRICT_BEIJING;
                    district_url.add(Const.PARAM_DISTRICT_BEIJING);
                    break;
                case R.id.ipcp_choice_btn_number_4_2:
                    //                    url += "&district=" + Const.PARAM_DISTRICT_SHANGHAI;
                    district_url.add(Const.PARAM_DISTRICT_SHANGHAI);
                    break;
                case R.id.ipcp_choice_btn_number_4_3:
                    //                    url += "&district=" + Const.PARAM_DISTRICT_GUANGDONG;
                    district_url.add(Const.PARAM_DISTRICT_GUANGDONG);
                    break;
                case R.id.ipcp_choice_btn_number_4_4:
                    //                    url += "&district=" + Const.PARAM_DISTRICT_ZEJIANG;
                    district_url.add(Const.PARAM_DISTRICT_ZEJIANG);
                    break;
                case R.id.ipcp_choice_btn_number_4_5:
                    //                    url += "&district=" + Const.PARAM_DISTRICT_JIANGSU;
                    district_url.add(Const.PARAM_DISTRICT_JIANGSU);
                    break;
                case R.id.ipcp_choice_btn_number_4_6:
                    //                    url += "&district=" + Const.PARAM_DISTRICT_SHANDONG;
                    district_url.add(Const.PARAM_DISTRICT_SHANDONG);
                    break;
                case R.id.ipcp_choice_btn_number_4_7:
                    //                    url += "&district=" + Const.PARAM_DISTRICT_ELSE;
                    district_url.add(Const.PARAM_DISTRICT_ELSE);
                    break;
            }
            //添加进筛选条件集合
            filterItemsList.add(((ToggleButton) mContentViewPopuWindow.findViewById(id)).getText().toString());
        }

        /*******拼接网络请求url地址*******/
        if (guarantee_url.size() > 0) {
            url += "&guarantee=";
            for (int x = 0; x < guarantee_url.size(); x++) {
                url += guarantee_url.get(x) + ((x == guarantee_url.size() - 1) ? "" : ",");
            }
        }

        if (background_url.size() > 0) {
            url += "&background=";
            for (int x = 0; x < background_url.size(); x++) {
                url += background_url.get(x) + ((x == background_url.size() - 1) ? "" : ",");
            }
        }

        if (btype_url.size() > 0) {
            url += "&btype=";
            for (int x = 0; x < btype_url.size(); x++) {
                url += btype_url.get(x) + ((x == btype_url.size() - 1) ? "" : ",");
            }
        }

        if (district_url.size() > 0) {
            url += "&district=";
            for (int x = 0; x < district_url.size(); x++) {
                url += district_url.get(x) + ((x == district_url.size() - 1) ? "" : ",");
            }
        }
        /*******************/


        if (!StringUtil.isEmptyOrNull(mCurrentPeriod)) {
            filterItemsList.add(mCurrentPeriod);
            url += "&period=" + GlobalUtils.getIntByDouString(mCurrentPeriod, 0) + "," + GlobalUtils.getIntByDouString(mCurrentPeriod, 1);
        }
        if (!StringUtil.isEmptyOrNull(mCurrentRate)) {
            filterItemsList.add(mCurrentRate);
            url += "&rate=" + GlobalUtils.getIntByDouString(mCurrentRate, 0) + "," + GlobalUtils.getIntByDouString(mCurrentRate, 1);
        }
        //根据数量决定是否显示条件框
        mChoosedItemsGridView.setVisibility(filterItemsList.size() == 0 ? View.GONE : View.VISIBLE);
        mAdapterFilterItems.refresh(filterItemsList);//刷新筛选条目适配器数据列表显示
        return url;
    }

    /** 点击条目删除筛选项 */
    private AdapterView.OnItemClickListener mFilterOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position == mAdapterFilterItems.getDataList().size()) {
                mTitleRight.setText("取消");
                mIvShaiXuanArrow.setImageResource(R.mipmap.btn_im_reverse);
                showPopuwindow();
                return;
            }
            String itemName = mAdapterFilterItems.getDataList().get(position);
            for (Map.Entry<Integer, Boolean> entry : mCheckMap.entrySet()) {
                if (itemName.equals(((ToggleButton) mContentViewPopuWindow.findViewById(entry.getKey())).getText().toString()) && entry.getValue()) {
                    mCheckMap.remove(entry.getKey());
                    mCheckMap.put(entry.getKey(), false);
                    break;
                }
            }
            if (itemName.equals(mCurrentPeriod)) {
                mCurrentPeriod = null;//期限值为空
            }
            if (itemName.equals(mCurrentRate)) {
                mCurrentRate = null;//利率值为空
            }
            mAdapterFilterItems.removeItem(position);
            isLoadMore = false;
            mCurrentPage = 1;
            requestNetData();
        }
    };


    /** 隐藏筛选窗体 */
    private void hidePopuwindow() {
        if (mFilterWindow != null && mFilterWindow.isShowing()) {
            mFilterWindow.dismiss();
        }
    }

    /** 是否需要恢复初始视图 */
    public boolean needRestoreView() {
        return "取消".equals(mTitleRight.getText().toString());
    }

    /** 恢复初始视图 */
    private void restoreView() {
        mTitleRight.setText("筛选");
        mIvShaiXuanArrow.setImageResource(R.mipmap.btn_im);
        hidePopuwindow();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && needRestoreView()) {
            restoreView();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
