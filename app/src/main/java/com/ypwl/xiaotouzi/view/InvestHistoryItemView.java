package com.ypwl.xiaotouzi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.bean.HistoryAssetsByPlatformProtocolBean;
import com.ypwl.xiaotouzi.utils.DateTimeUtil;

/**
 * function : 历史资产：按平台分类显示item内容view对象
 * <p/>
 * Created by tengtao on 2016/3/28.
 */
public class InvestHistoryItemView extends LinearLayout {

    private TextView mTvBidName, mTvDate, mTvBenjin, mTvShouyi, mTvLilv;

    public InvestHistoryItemView(Context context) {
        super(context);
        init(context);
    }

    public InvestHistoryItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public InvestHistoryItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View.inflate(context, R.layout.layout_invest_history_by_platform_item_view, this);
        mTvBenjin = (TextView) findViewById(R.id.tv_invest_history_bid_benjin);
        mTvBidName = (TextView) findViewById(R.id.tv_invest_history_bid_name);
        mTvDate = (TextView) findViewById(R.id.tv_invest_history_finish_date);
        mTvShouyi = (TextView) findViewById(R.id.tv_invest_history_bid_shouyi);
        mTvLilv = (TextView) findViewById(R.id.tv_invest_history_bid_lilv);
    }

    public void refreshData(HistoryAssetsByPlatformProtocolBean.ListEntity.DataEntity data) {
        mTvBenjin.setText(data.getMoney());
        mTvLilv.setText(data.getRate() + "%");
        mTvShouyi.setText(data.getProfit());
        mTvBidName.setText(data.getProject_name());
        String[] strings = handleDate(data.getReturn_time());
        mTvDate.setText(strings[0] + "/" + strings[1] + "/" + strings[2] + "结束");
    }

    /**
     * 处理时间戳
     *
     * @param time Unix时间戳
     * @return 年月日的数组
     */
    private String[] handleDate(String time) {
        long millisTime = 0;
        try {
            millisTime = Long.parseLong(time) * 1000;
        } catch (Exception e) {
            e.printStackTrace();
        }
        String formatDateTime = DateTimeUtil.formatDateTime(millisTime, "yyyy-MM-dd");
        return formatDateTime.split("-");
    }
}
