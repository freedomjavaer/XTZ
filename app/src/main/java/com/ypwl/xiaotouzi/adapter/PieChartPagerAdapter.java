package com.ypwl.xiaotouzi.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * 标的详情右侧饼状图的适配器
 *
 * Created by PDK on 2015/12/9.
 */
public class PieChartPagerAdapter extends PagerAdapter {

    private ArrayList<View> mPieChartList;

    public PieChartPagerAdapter(ArrayList<View> mPieChartList ) {
        this.mPieChartList = mPieChartList;
    }

    @Override
    public int getCount() {
        return mPieChartList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mPieChartList.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.getChildAt(position).invalidate();
        container.removeView((View) object);
    }

}
