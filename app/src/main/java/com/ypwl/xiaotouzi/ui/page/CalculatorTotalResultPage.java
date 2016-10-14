package com.ypwl.xiaotouzi.ui.page;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.base.BasePage;
import com.ypwl.xiaotouzi.utils.Util;

/**
 * function:计算器结果展示页面一
 * <p/>
 * Created by tengtao on 2016/1/13.
 */
public class CalculatorTotalResultPage extends BasePage {
    //计算结果显示控件
    private TextView totalGetMoney,myCostMoney,myExtraMoney,realRate;
    //投标奖励和管理费
    private String cal_lixiguanlifei = (String) mContext.getResources().getText(R.string.cal_lixiguanlifei);
    private String cal_toubiaojiangli = (String) mContext.getResources().getText(R.string.cal_toubiaojiangli);

    private int SPANNABLE_TYPE_EXTRA = 1;//奖励
    private int SPANNABLE_TYPE_COST = 2;//管理费

    public CalculatorTotalResultPage(Activity context) {
        super(context);
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = View.inflate(mContext, R.layout.layout_calculator_result_total,null);
        totalGetMoney = (TextView) view.findViewById(R.id.totalGetMoney);
        myCostMoney = (TextView) view.findViewById(R.id.myCostMoney);
        myExtraMoney = (TextView) view.findViewById(R.id.myExtraMoney);
        realRate = (TextView) view.findViewById(R.id.realRate);
        return view;
    }

    //初始化数据
    @Override
    public void initData() {
        totalGetMoney.setText("0.00");
        // 设置利息管理费
        setMyCostOrExtraMoneyByVal(0,SPANNABLE_TYPE_COST);
        // 设置投标奖励
        setMyCostOrExtraMoneyByVal(0, SPANNABLE_TYPE_EXTRA);
        realRate.setText("0.00");
    }

    /**
     * 设置利息管理和投标奖励UI显示
     * @param money 金额
     * @param model  模式：1--投标奖励，2--利息管理费用
     */
    private void setMyCostOrExtraMoneyByVal(double money, int model) {
        String strMoney = String.format(model == 1 ? cal_toubiaojiangli : cal_lixiguanlifei, money);
        String num = String.valueOf(money);
        Util.setSpannable(model == 1 ? myExtraMoney : myCostMoney, strMoney, 6, strMoney.length(), model == 1 ? 0xFFE99534 : 0xFFE93434);
    }

    /**
     * 设置总收益
     * @param money
     */
    public void setTotalGetMoney(String money){
        totalGetMoney.setText(money);
    }

    /**
     * 设置实际利率
     * @param rate
     */
    public void setRealRate(String rate){
        realRate.setText(rate);
    }

    /**
     * 设置利息管理费和额外奖励
     * @param money
     * @param model
     */
    public void setCostOrExtraMoney(double money , int model){
        setMyCostOrExtraMoneyByVal(money, model);
    }
}
