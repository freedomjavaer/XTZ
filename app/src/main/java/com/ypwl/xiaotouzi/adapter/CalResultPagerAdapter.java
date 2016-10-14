package com.ypwl.xiaotouzi.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.ypwl.xiaotouzi.base.BasePage;

import java.util.ArrayList;

/**
 * function: 计算器结果清单适配器
 * <p/>
 * Created by tengtao on 2016/1/13.
 */
public class CalResultPagerAdapter extends PagerAdapter {

    private ArrayList<BasePage> mPagerList;

    public CalResultPagerAdapter(ArrayList<BasePage> pagers) {
        this.mPagerList = pagers;
    }

    @Override
    public int getCount() {
        return mPagerList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mPagerList.get(position).getRootView();
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.getChildAt(position).invalidate();
        container.removeView((View) object);
    }
}
