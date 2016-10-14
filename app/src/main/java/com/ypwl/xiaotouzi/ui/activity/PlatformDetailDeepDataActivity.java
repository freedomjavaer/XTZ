package com.ypwl.xiaotouzi.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.github.mikephil.charting.charts.CombinedChart;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.bean.PlatformDeepDataBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.interf.IRequestCallback;
import com.ypwl.xiaotouzi.manager.net.NetHelper;
import com.ypwl.xiaotouzi.utils.ChartUtils;
import com.ypwl.xiaotouzi.utils.FileUtil;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.ViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 平台详情深度数据页面
 * Created by PDK on 2015/11/9.
 */
public class PlatformDetailDeepDataActivity extends BaseActivity implements View.OnClickListener {

    private TextView mDeepDataDay;//日
    private TextView mDeepDataWeek;//周
    private TextView mDeepDataMonth;//月
    private CombinedChart mDeepDataCombinedchart;//混合你图表对象
    private TextView mPlatformDeepDataName;//平台名
    private ImageView mDeepDataBack;//返回键
    private ListView mPlatformDeepData;//条件选择条目
    private String pid;//平台id
    private PlatformDeepDataBean platformDeepDataBean;//深度数据实体
    private DeepDataAdapter myAdapter;//适配器
    private TextView mDeepDataLeft_y;//图表左边Y轴顶部描述
    private TextView mDeepDataRight_y;//图表右边Y轴顶部描述
    private ImageView mUpArrow;
    private ImageView mDownArrow;
    private View mNoDataView;
    private LinearLayout mDeepDataContainer;
    private TextView mItemTv;

    /**判断日是否被点击------默认为false*/
    private boolean ISDAY = false;
    /**判断周是否被点击------默认为false*/
    private boolean ISWEEK = false;
    /**判断月是否被点击------默认为false*/
    private boolean ISMONTH = false;
    /**初始化标记---默认为true*/
    private boolean ISINIT = true;

    //存储标记（条目位置、数据类型、日期类型）
    List<Integer> positionList = new ArrayList<>();
    List<Integer> typeList = new ArrayList<>();
    List<Integer> dateList = new ArrayList<>();
    List<Boolean> stateList = new ArrayList<>();
    int num = 0;

    //定义数据类型名称
    private String[] dataType = new String[]{
            "成交量", "平均利率", "历史待还", "资金净流入", "投资人数", "借款人数", "人均投资金额",
            "人均借款金额", "借款标数", "平均借款期限", "待收投资人数", "待还借款人数"
    };
    private LinearLayout mPieChartRightTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_platform_detail_deep_data);
        //实例化网络请求对象

        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        mDeepDataDay.setOnClickListener(this);
        mDeepDataWeek.setOnClickListener(this);
        mDeepDataMonth.setOnClickListener(this);
        mUpArrow.setOnClickListener(this);
        mDownArrow.setOnClickListener(this);

        //给返回图设置点击事件
        mDeepDataBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //ListView条目点击事件
        mPlatformDeepData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //计算条目被选中总数
                int num = FileUtil.judgeStateNum(stateList);
                if (num == 1){
                    if (positionList.get(0) == position){
                        //点击已选中条目
                        positionList.add(0);
                        ISINIT = true;
                    }else {
                        //点击未选中条目
                        positionList.add(position);
                        stateList.set(position,true);
                        typeList.remove(1);
                        typeList.add(position + 1);
                    }

                }else if (num == 2){
                    if (positionList.get(0) == position){
                        //点击已选中条目
                        stateList.set(position, false);
                        positionList.remove(0);
                        positionList.add(position);
                        typeList.remove(0);
                        typeList.add(0);
                    }else if (positionList.get(1) == position){
                        //点击已选中条目
                        stateList.set(position, false);
                        positionList.remove(1);
                        positionList.add(position);
                        typeList.remove(1);
                        typeList.add(0);
                    }else {
                        stateList.set(positionList.get(0),false);
                        positionList.remove(0);
                        stateList.set(position,true);
                        positionList.add(position);
                        typeList.remove(0);
                        typeList.add(position + 1);
                    }
                }

                //更新图表Y轴顶部描述
                updateChartDesc();
                myAdapter.notifyDataSetChanged();
                //执行网络请求
                excuteRequest();

            }
        });

        //监听ListView滚动事件
        mPlatformDeepData.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    num = mPlatformDeepData.getFirstVisiblePosition();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

    }

    /**更新图表Y轴顶部描述*/
    private void updateChartDesc() {

        if (positionList.get(0) == 0) {
            mDeepDataLeft_y.setText(dataType[0] + "/万元");
        } else if (positionList.get(0) == 1) {
            mDeepDataLeft_y.setText(dataType[1] + "/%");
        } else if (positionList.get(0) == 2) {
            mDeepDataLeft_y.setText(dataType[2] + "/万元");
        } else if (positionList.get(0) == 3) {
            mDeepDataLeft_y.setText(dataType[3] + "/万元");
        } else if (positionList.get(0) == 4) {
            mDeepDataLeft_y.setText(dataType[4] + "/人");
        } else if (positionList.get(0) == 5) {
            mDeepDataLeft_y.setText(dataType[5] + "/人");
        } else if (positionList.get(0) == 6) {
            mDeepDataLeft_y.setText(dataType[6] + "/万元");
        } else if (positionList.get(0) == 7) {
            mDeepDataLeft_y.setText(dataType[7] + "/万元");
        } else if (positionList.get(0) == 8) {
            mDeepDataLeft_y.setText(dataType[8] + "/个");
        } else if (positionList.get(0) == 9) {
            mDeepDataLeft_y.setText(dataType[9] + "/月");
        } else if (positionList.get(0) == 10) {
            mDeepDataLeft_y.setText(dataType[10] + "/人");
        } else if (positionList.get(0) == 11) {
            mDeepDataLeft_y.setText(dataType[11] + "/人");
        } else if (positionList.get(0) == 12) {
            mDeepDataLeft_y.setText(dataType[12]);
        } else if (positionList.get(0) == 13) {
            mDeepDataLeft_y.setText(dataType[13]);
        }

        if (positionList.get(1) == 0) {
            mDeepDataRight_y.setText(dataType[0] + "/万元");
        } else if (positionList.get(1) == 1) {
            mDeepDataRight_y.setText(dataType[1] + "/%");
        } else if (positionList.get(1) == 2) {
            mDeepDataRight_y.setText(dataType[2] + "/万元");
        } else if (positionList.get(1) == 3) {
            mDeepDataRight_y.setText(dataType[3] + "/万元");
        } else if (positionList.get(1) == 4) {
            mDeepDataRight_y.setText(dataType[4] + "/人");
        } else if (positionList.get(1) == 5) {
            mDeepDataRight_y.setText(dataType[5] + "/人");
        } else if (positionList.get(1) == 6) {
            mDeepDataRight_y.setText(dataType[6] + "/万元");
        } else if (positionList.get(1) == 7) {
            mDeepDataRight_y.setText(dataType[7] + "/万元");
        } else if (positionList.get(1) == 8) {
            mDeepDataRight_y.setText(dataType[8] + "/个");
        } else if (positionList.get(1) == 9) {
            mDeepDataRight_y.setText(dataType[9] + "/月");
        } else if (positionList.get(1) == 10) {
            mDeepDataRight_y.setText(dataType[10] + "/人");
        } else if (positionList.get(1) == 11) {
            mDeepDataRight_y.setText(dataType[11] + "/人");
        } else if (positionList.get(1) == 12) {
            mDeepDataRight_y.setText(dataType[12]);
        } else if (positionList.get(1) == 13) {
            mDeepDataRight_y.setText(dataType[13]);
        }

    }

    private void initData() {
        //初始化选中按日的数据
        mDeepDataDay.setSelected(true);
        ISDAY = true;
        dateList.add(0);
        //存储条目状态标记
        saveStateMark();

        //设置平台名字
        Intent intent = getIntent();
        final String platformName = intent.getStringExtra("platformname");
        pid = intent.getStringExtra("pid");
        String positon1 = intent.getStringExtra("position1");
        String positon2 = intent.getStringExtra("position2");
        mPlatformDeepDataName.setText(platformName);

        //初始化数据类型的标记
        if ("0".equals(positon2)){
            typeList.add(Integer.parseInt(positon1) + 1);
            typeList.add(0);
            if ("4".equals(positon1)){
                stateList.set(Integer.parseInt(positon1),true);
            }else{
                stateList.set(Integer.parseInt(positon1), true);
            }
        }else {
            typeList.add(Integer.parseInt(positon1) + 1);
            typeList.add(Integer.parseInt(positon2) + 1);
            stateList.set(Integer.parseInt(positon1), true);
            stateList.set(Integer.parseInt(positon2), true);
        }

        //初始化条目位置的标记
        positionList.add(Integer.parseInt(positon1));
        positionList.add(Integer.parseInt(positon2));

        //给ListView数据选择框填充条目
        //取消ScrollBar
        mPlatformDeepData.setVerticalScrollBarEnabled(false);
        if (myAdapter == null) {
            myAdapter = new DeepDataAdapter();
        }
        mPlatformDeepData.setAdapter(myAdapter);

        //网络请求数据
        excuteRequest();

    }

    /**存储条目以及其状态标记-----默认都为false*/
    private void saveStateMark() {
        stateList.add(false);
        stateList.add(false);
        stateList.add(false);
        stateList.add(false);
        stateList.add(false);
        stateList.add(false);
        stateList.add(false);
        stateList.add(false);
        stateList.add(false);
        stateList.add(false);
        stateList.add(false);
        stateList.add(false);
    }

    private void initView() {
        mDeepDataDay = (TextView) findViewById(R.id.tv_platform_detail_deep_data_day);
        mDeepDataWeek = (TextView) findViewById(R.id.tv_platform_detail_deep_data_week);
        mDeepDataMonth = (TextView) findViewById(R.id.tv_platform_detail_deep_data_month);
        mDeepDataCombinedchart = (CombinedChart) findViewById(R.id.platform_deep_data_combinedchart);
        mPlatformDeepDataName = (TextView) findViewById(R.id.tv_platform_deep_data_name);
        mDeepDataBack = (ImageView) findViewById(R.id.iv_platform_deep_data_back);
        mPlatformDeepData = (ListView) findViewById(R.id.lv_platform_deep_data);
        mDeepDataLeft_y = (TextView) findViewById(R.id.tv_platform_deep_data_left_y);
        mDeepDataRight_y = (TextView) findViewById(R.id.tv_platform_deep_data_right_y);
        mUpArrow = (ImageView) findViewById(R.id.iv_deep_data_up_arrow);
        mDownArrow = (ImageView) findViewById(R.id.iv_deep_data_down_arrow);
        mPieChartRightTitle = (LinearLayout) findViewById(R.id.ll_piechart_right_title);

        mNoDataView = findViewById(R.id.layout_no_data_view);
        mDeepDataContainer = (LinearLayout) findViewById(R.id.ll_deep_data_container);
        ViewUtil.showContentLayout(Const.LAYOUT_LOADING, mNoDataView, mDeepDataContainer);
    }


    /**
     * 日、周、月的点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_platform_detail_deep_data_day:
                if (ISDAY == false) {
                    dateList.remove(0);
                    dateList.add(0);
                    excuteRequest();
                    mDeepDataDay.setSelected(true);
                    mDeepDataWeek.setSelected(false);
                    mDeepDataMonth.setSelected(false);
                    ISDAY = true;
                    ISWEEK = false;
                    ISMONTH = false;
                }
                break;

            case R.id.tv_platform_detail_deep_data_week:
                if (ISWEEK == false) {
                    dateList.remove(0);
                    dateList.add(1);
                    excuteRequest();
                    mDeepDataDay.setSelected(false);
                    mDeepDataWeek.setSelected(true);
                    mDeepDataMonth.setSelected(false);
                    ISWEEK = true;
                    ISDAY = false;
                    ISMONTH = false;
                }
                break;
            case R.id.tv_platform_detail_deep_data_month:
                if (ISMONTH == false) {
                    dateList.remove(0);
                    dateList.add(2);
                    excuteRequest();
                    mDeepDataDay.setSelected(false);
                    mDeepDataWeek.setSelected(false);
                    mDeepDataMonth.setSelected(true);
                    ISMONTH = true;
                    ISDAY = false;
                    ISWEEK = false;
                }
                break;
            case R.id.iv_deep_data_up_arrow:
                if (num < 6) {
                    mPlatformDeepData.setSelection(++num);
                }
                break;
            case R.id.iv_deep_data_down_arrow:
                if (num > 0) {
                    mPlatformDeepData.setSelection(--num);
                }
                break;
            default:
                break;
        }

    }

    /**执行网络请求的方法*/
    private void excuteRequest() {

        String url = String.format(URLConstant.PLATFORM_DEEP_DATA, pid, typeList.get(0), typeList.get(1), dateList.get(0));
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
                if (GlobalUtils.isTokenMatach(jsonStr)) {
                    platformDeepDataBean = null;
                    platformDeepDataBean = JSON.parseObject(jsonStr, PlatformDeepDataBean.class);
                    if (platformDeepDataBean != null) {
                        //初始化成交量、利率图表
                        ChartUtils.createCombinedBarChart(mActivity, mDeepDataCombinedchart, platformDeepDataBean, typeList, dateList, mPieChartRightTitle);
                        if (positionList.size() > 1) {
                            updateChartDesc();
                        }
                        ViewUtil.showContentLayout(Const.LAYOUT_DATA, mNoDataView, mDeepDataContainer);
                    } else {
                        ViewUtil.showContentLayout(Const.LAYOUT_EMPTY, mNoDataView, mDeepDataContainer);
                    }

                }
            }
        });
    }


    private class DeepDataAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return dataType.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, android.view.View convertView, ViewGroup parent) {
            View view;
            if (convertView == null) {
                view = View.inflate(PlatformDetailDeepDataActivity.this, R.layout.item_lv_deep_data, null);
            } else {
                view = convertView;
            }
            mItemTv = (TextView) view.findViewById(R.id.tv_platform_deep_data_item);
            ImageView mDeepDataImg = (ImageView) view.findViewById(R.id.tv_platform_deep_data_img);
            mItemTv.setText(dataType[position]);
            mItemTv.setSelected(false);
            mDeepDataImg.setVisibility(View.GONE);
            //判断是否为初始化进入
            if (ISINIT) {
                if (positionList.get(1) == 0 && positionList.get(0) == position) {
                    mItemTv.setSelected(true);
                    mDeepDataImg.setVisibility(View.VISIBLE);
                }else if (positionList.get(1) != 0){
                    if (positionList.contains(position)) {
                        mItemTv.setSelected(true);
                        mDeepDataImg.setVisibility(View.VISIBLE);
                    }
                }

                if (position == 6) {
                    ISINIT = false;
                    if (positionList.get(1) == 0){
                        positionList.remove(1);
                    }
                }

            } else {

                if (typeList.get(1) == 0){
                    if (positionList.get(0) == position){
                        if (positionList.size()>1){
                            positionList.remove(1);
                        }
                        mItemTv.setSelected(true);
                        mDeepDataImg.setVisibility(View.VISIBLE);
                    }
                }else {
                    if (positionList.contains(position)) {
                        mItemTv.setSelected(true);
                        mDeepDataImg.setVisibility(View.VISIBLE);
                    } else {
                        mItemTv.setSelected(false);
                        mDeepDataImg.setVisibility(View.GONE);
                    }
                }

            }

            return view;
        }
    }

}