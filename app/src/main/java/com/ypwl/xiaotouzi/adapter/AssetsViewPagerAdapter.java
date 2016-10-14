package com.ypwl.xiaotouzi.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.bean.InvestAssetsBean;
import com.ypwl.xiaotouzi.utils.Util;

/**
 * 资产ViewPager适配器
 *
 * Created by PDK on 2016/3/23.
 */
public class AssetsViewPagerAdapter extends PagerAdapter{

    Context context;
    private InvestAssetsBean bean;

    public AssetsViewPagerAdapter(Context context,InvestAssetsBean bean) {
        this.context = context;
        this.bean = bean;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view;
        if (position == 0) {
            view = View.inflate(context, R.layout.fragment_assets_viewpager_one, null);
            TextView mTotalMoney = (TextView) view.findViewById(R.id.vp_total_money);
            TextView mBackTotalMoney = (TextView) view.findViewById(R.id.vp_wait_back_total_money);
            TextView mExpectProfit = (TextView) view.findViewById(R.id.vp_expect_profit);
            TextView mWeightingRate = (TextView) view.findViewById(R.id.vp_weighting_rate);

            Util.setSpannableSize(mTotalMoney, bean.getStotal(), bean.getStotal().length() - 2, 38);//4.2已改为待回总额
            mBackTotalMoney.setText(bean.getScapital());//4.2已改为待回本金
            mExpectProfit.setText(bean.getSprofit());//4.2已改为在投标收益
            mWeightingRate.setText(bean.getSwannual() + "%");

//            if ("0.00".equals(bean.getStotal()) && "0.00".equals(bean.getScapital()) && "0.00".equals(bean.getSprofit()) && "0.00".equals(bean.getSwannual())){
//                mTotalMoney.setTextColor(context.getResources().getColor(R.color.finance_supermarket_yellow));
//                mBackTotalMoney.setTextColor(context.getResources().getColor(R.color.finance_supermarket_yellow));
//                mExpectProfit.setTextColor(context.getResources().getColor(R.color.finance_supermarket_yellow));
//                mWeightingRate.setTextColor(context.getResources().getColor(R.color.finance_supermarket_yellow));
//            }

        } else if (position == 1) {
            view = View.inflate(context, R.layout.fragment_assets_viewpager_two, null);
            TextView mTwoTotalProfit = (TextView) view.findViewById(R.id.vp_two_total_profit);
            TextView mTwoBidingProfit = (TextView) view.findViewById(R.id.vp_two_biding_profit);
            TextView mTwoEndBidProfit = (TextView) view.findViewById(R.id.vp_two_end_bid_profit);

            Util.setSpannableSize(mTwoTotalProfit, bean.getTprofit(), bean.getTprofit().length() - 2, 38);
            //mTwoTotalProfit.setText(bean.getTprofit());
            mTwoBidingProfit.setText(bean.getSprofit());
            mTwoEndBidProfit.setText(bean.getFprofit());

//            if ("0.00".equals(bean.getStotal()) && "0.00".equals(bean.getScapital()) && "0.00".equals(bean.getSprofit()) && "0.00".equals(bean.getSwannual())){
//                mTwoTotalProfit.setTextColor(context.getResources().getColor(R.color.finance_supermarket_yellow));
//                mTwoBidingProfit.setTextColor(context.getResources().getColor(R.color.finance_supermarket_yellow));
//                mTwoEndBidProfit.setTextColor(context.getResources().getColor(R.color.finance_supermarket_yellow));
//            }

        } else {
            view = View.inflate(context, R.layout.fragment_assets_viewpager_three, null);
            TextView mTwoTotalRate = (TextView) view.findViewById(R.id.vp_two_total_rate);
            TextView mTwoBidingRate = (TextView) view.findViewById(R.id.vp_two_biding_rate);
            TextView mTwoExpectProfit = (TextView) view.findViewById(R.id.vp_two_expect_profit);

            Util.setSpannableSize(mTwoTotalRate, bean.getWannual() + "%", bean.getWannual().length() - 2, 38);
            //mTwoTotalRate.setText(bean.getWannual() + "%");
            mTwoBidingRate.setText(bean.getSwannual() + "%");
            mTwoExpectProfit.setText(bean.getFwannual() + "%");

//            if ("0.00".equals(bean.getStotal()) && "0.00".equals(bean.getScapital()) && "0.00".equals(bean.getSprofit()) && "0.00".equals(bean.getSwannual())){
//                mTwoTotalRate.setTextColor(context.getResources().getColor(R.color.finance_supermarket_yellow));
//                mTwoBidingRate.setTextColor(context.getResources().getColor(R.color.finance_supermarket_yellow));
//                mTwoExpectProfit.setTextColor(context.getResources().getColor(R.color.finance_supermarket_yellow));
//            }

        }
        container.addView(view);
        return view;
    }


}
