package com.ypwl.xiaotouzi.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.squareup.otto.Subscribe;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.adapter.CalendarBackMoneyAdapter;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.bean.CalendarBackMoneyBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.common.ServerStatus;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.event.AccountsKeptEvent;
import com.ypwl.xiaotouzi.event.BidDeletedEvent;
import com.ypwl.xiaotouzi.event.CalendarBackMoneyBidStatusChangeEvent;
import com.ypwl.xiaotouzi.interf.IRequestCallback;
import com.ypwl.xiaotouzi.manager.net.NetHelper;
import com.ypwl.xiaotouzi.utils.DateTimeUtil;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.LogUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.ViewUtil;
import com.ypwl.xiaotouzi.view.customcalendar.CustomCalendar;
import com.ypwl.xiaotouzi.view.customcalendar.CustomCalendarListener;
import com.ypwl.xiaotouzi.view.dialog.lib.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * function : 近期回款--日历.
 * <p/>
 * Modify by lzj on 2015/11/9.
 */
public class CalendarBackMoneyActivity extends BaseActivity implements View.OnClickListener, CustomCalendarListener {
    private View mNoDataView;
    private KProgressHUD mLoading;
    public static List<CalendarBackMoneyBean.ListEntity> mDataForCaldroid = new ArrayList<>();//当ListView显示具体某天的数据时,日历容器不改变
    private boolean isMonth;//标记是否为月,为月时,日历容器数据不变
    private CalendarBackMoneyAdapter mAdapter;
    private TextView mTvBackText;
    private ListView mListView;
    private String tempDate;//当前请求数据日期
    private int cMonth,cYear;
    private HashMap<String,Integer> day_aid = new HashMap<>();
    private CustomCalendar mCustomCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_back_money);
        mLoading = KProgressHUD.create(this);
        mLoading.show();
        initView();
    }

    private void initView() {
        mNoDataView = findViewById(R.id.layout_no_data_view);
        //title
        findViewById(R.id.layout_title_back).setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText("还款日历");
        mTvBackText = (TextView) findViewById(R.id.tv_title_back);
        mTvBackText.setText("全部回款");
        //content
        mListView = (ListView) findViewById(R.id.lv_content_data);
        mAdapter = new CalendarBackMoneyAdapter(mActivity);
        mListView.setAdapter(mAdapter);
        mCustomCalendar = (CustomCalendar) findViewById(R.id.back_money_by_calendar);
        mCustomCalendar.setPreviousMonthCount(48);
        mCustomCalendar.setCalendarType(CustomCalendar.TYPE_VIEWPAGER);//日历显示类型
        mCustomCalendar.setDynamicCalendarListener(this);

        ViewUtil.showContentLayout(Const.LAYOUT_LOADING,mNoDataView,mListView);
    }

    /** 通过date日期请求网络数据 */
    private void requestDataByDate(String date) {
        getIsMonthStatus(date);
        String url = String.format(URLConstant.RECENT_RETURN_MONEY, GlobalUtils.token, date);
        NetHelper.get(url, new RecentReturnMoneyCallBack());
    }

    /** 近期回款回调接口 */
    private class RecentReturnMoneyCallBack extends IRequestCallback<String> {

        @Override
        public void onStart() {
            if(!mLoading.isShowing())
                mLoading.show();
        }

        @Override
        public void onFailure(Exception e) {
            mLoading.dismiss();
            UIUtil.showToastShort("获取数据失败");
            ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mNoDataView, mListView, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestDataByDate(tempDate);
                }
            });
            mListView.setVisibility(View.GONE);
        }

        @Override
        public void onSuccess(String jsonStr) {
            mLoading.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);
                int status = jsonObject.optInt(Const.JSON_KEY_status, -1);
                CalendarBackMoneyBean bean = JSON.parseObject(jsonStr, CalendarBackMoneyBean.class);
                switch (status) {
                    case ServerStatus.SERVER_STATUS_OK:
                        if (bean == null || bean.getList() == null || bean.getList().size() == 0) {
                            mListView.setVisibility(View.GONE);
                            ViewUtil.showContentLayout(Const.LAYOUT_EMPTY,mNoDataView,mListView);
                            return;
                        }
                        if (isMonth) {
                            mDataForCaldroid.clear();
                            mDataForCaldroid.addAll(bean.getList());
                            handleData(bean.getList());
                            handleCustomCalendarData(bean.getList());
                        }
                        mListView.setVisibility(View.VISIBLE);
                        mListView.smoothScrollToPosition(0);
                        mAdapter.loadData(bean.getList());
                        ViewUtil.showContentLayout(Const.LAYOUT_DATA,mNoDataView,mListView);
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

    /** 处理日历标记数据 */
    private void handleCustomCalendarData(List<CalendarBackMoneyBean.ListEntity> list){
        if(list.size()==0){return;}
        Map<String,Integer> backInfos = new HashMap<>();
        int count = 0;
        String mCurrentDate="";
        for (int i = 0; i < list.size(); i++) {
            CalendarBackMoneyBean.ListEntity bean = list.get(i);
            long return_time = 0;
            try {
                return_time = Long.parseLong(bean.getReturn_time());
            }catch (Exception e){
                e.printStackTrace();
            }
            String formatDateTime = DateTimeUtil.formatDateTime(return_time * 1000, "yyyy-MM-dd");
            if(formatDateTime.equalsIgnoreCase(mCurrentDate)){
                Integer num = backInfos.get(mCurrentDate);
                num ++;
                backInfos.put(mCurrentDate,num);
            }else{
                count=1;
                mCurrentDate = formatDateTime;
                backInfos.put(mCurrentDate,count);
            }
        }
        mCustomCalendar.setSelectedData(backInfos);
    }

    private void handleData(List<CalendarBackMoneyBean.ListEntity> list){
        String currTime = "";
        for(int i=0;i<list.size();i++){
            String return_time = list.get(i).getReturn_time();
            long l=0;
            try{
                l = Long.parseLong(return_time);
            }catch (Exception e){
                e.printStackTrace();
            }
            String time = DateTimeUtil.formatDateTime(l * 1000, "yyyy-MM-dd");
            if(!currTime.equals(time)){
                day_aid.put(time,i);
                currTime = time;
            }
        }
    }

    /** 近期回款状态改变事件 */
    @Subscribe
    public void onRecentBackMoneyRefreshEvent(CalendarBackMoneyBidStatusChangeEvent event){
        if(event!=null && !isFinishing()){
            onMonthChanged(cYear,cMonth);
        }
    }

    /**标的删除事件*/
    @Subscribe
    public void onBidDeletedEvent(BidDeletedEvent event){
        if(event!=null && !isFinishing()){
            onMonthChanged(cYear,cMonth);
        }
    }

    /**标的编辑事件*/
    @Subscribe
    public void onAccountsKeptEvent(AccountsKeptEvent event){
        if(event!=null && !isFinishing()){
            onMonthChanged(cYear,cMonth);
        }
    }

    /** 获得isMonth的状态 */
    private void getIsMonthStatus(String date) {
        isMonth = (date.length() <= 8);
    }

    /** 8----->08 */
    private String getUsefulMonth(int month) {
        String s = month + "";
        if (1 == s.length()) {
            s = "0" + s;
            return s;
        } else {
            return month + "";
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_title_back://返回
                finish();
                break;
        }
    }

    @Override
    public void onDayOfMonthSelected(int year, int month, int day) {
        //月默认：0-11
        LogUtil.e(TAG, year + ":" + (month+1) + ":" + day);
        String m = String.valueOf(month + 1);
        String d = String.valueOf(day);
        //设置点击,不重新加载数据，滚动显示到目标日期
        String formatDate = year+"-"+(m.length()==1?("0"+m):m)+"-"+(d.length()==1?("0"+d):d);
        if (!day_aid.containsKey(formatDate)) {
            return;
        }
        int index = day_aid.get(formatDate);
        mListView.setSelection(index);
    }

    @Override
    public void onMonthChanged(int year, int month) {
        cMonth = month;
        cYear = year;
        mDataForCaldroid.clear();
        day_aid.clear();//清空当月天和aid对应的数据
        //加载此时数据,刷新日历,延迟加载，改善ui卡顿
        tempDate = year + "-" + getUsefulMonth(month);
        requestDataByDate(tempDate);
    }
}
