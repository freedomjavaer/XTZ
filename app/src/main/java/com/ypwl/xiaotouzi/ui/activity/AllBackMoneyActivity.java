package com.ypwl.xiaotouzi.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.common.UmengEventID;
import com.ypwl.xiaotouzi.manager.UmengEventHelper;
import com.ypwl.xiaotouzi.ui.fragment.FragmentAllBackByDate;
import com.ypwl.xiaotouzi.ui.fragment.FragmentAllBackByPlatform;

/**
 * function : 我的投资--全部回款项
 * <p/>
 * Created by tengtao on 2016/3/22.
 */
public class AllBackMoneyActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mIvHideChart,mIvChange;
    private TextView mTvTitle,mTvBack;
    private FragmentManager mFragmentManager;
    private FragmentAllBackByDate mAllBackByDate;//按日期分组
    private FragmentAllBackByPlatform mAllBackByPlatform;//按平台分组
    private boolean isShowByDate = false;
    private boolean isShowBarChart = true;
    //全部回款--日期分组
    public static final String FRAGMENT_TAG_ALL_BACK_DATE = "fragment_tag_all_back_date";
    //全部回款--平台分组
    public static final String FRAGMENT_TAG_ALL_BACK_PLATFORM = "fragment_tag_all_back_platform";
    private static final String[] tags = new String[]{FRAGMENT_TAG_ALL_BACK_DATE, FRAGMENT_TAG_ALL_BACK_PLATFORM};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_back_money);
        mFragmentManager = getSupportFragmentManager();
        initView();
    }

    private void initView() {
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvTitle.setText("全部回款");
        mTvBack = (TextView) findViewById(R.id.tv_title_back);
        mTvBack.setText("我的投资");
        mIvChange = (ImageView) findViewById(R.id.iv_title_right_image);
        mIvChange.setImageResource(R.mipmap.pic_all_back_list_1);
        mIvChange.setVisibility(View.VISIBLE);
        mIvChange.setOnClickListener(this);//切换
        mIvHideChart = (ImageView) findViewById(R.id.iv_title_right_image_2);
        mIvHideChart.setImageResource(R.mipmap.show_stack_bar_chart);
        mIvHideChart.setVisibility(View.VISIBLE);
        mIvHideChart.setOnClickListener(this);

        findViewById(R.id.layout_title_back).setOnClickListener(this);//近期回款
        changeFragment();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_title_back://近期回款
                finish();
                break;
            case R.id.iv_title_right_image_2://  隐藏/显示柱状图
                mIvHideChart.setImageResource(isShowBarChart?R.mipmap.hide_stack_bar_chart:R.mipmap.show_stack_bar_chart);
                isShowBarChart = !isShowBarChart;
                mAllBackByDate.toggleHideStackBarChart();
                break;
            case R.id.iv_title_right_image://切换
                changeFragment();
                break;
        }
    }

    /** 切换 */
    private void changeFragment(){
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        hideFragments(mFragmentManager, transaction);
        if(isShowByDate){
            if (mAllBackByPlatform == null) {
                mAllBackByPlatform = new FragmentAllBackByPlatform();
                transaction.add(R.id.all_back_bar_chart_container, mAllBackByPlatform, FRAGMENT_TAG_ALL_BACK_PLATFORM);
            }
            transaction.show(mAllBackByPlatform);
            mIvHideChart.setVisibility(View.GONE);
            mIvChange.setImageResource(R.mipmap.pic_all_back_list_2);
            UmengEventHelper.onEvent(UmengEventID.AllBackMoneyByPlatformButton);
        }else{
            if (mAllBackByDate == null) {
                mAllBackByDate = new FragmentAllBackByDate();
                transaction.add(R.id.all_back_bar_chart_container, mAllBackByDate, FRAGMENT_TAG_ALL_BACK_DATE);
            }
            transaction.show(mAllBackByDate);
            mIvHideChart.setVisibility(View.VISIBLE);
            mIvChange.setImageResource(R.mipmap.pic_all_back_list_1);
            UmengEventHelper.onEvent(UmengEventID.AllBackMoneyByDateButton);
        }
        isShowByDate = !isShowByDate;
        transaction.commit();
    }

    /** 隐藏所有的fragment */
    private void hideFragments(FragmentManager fragmentManager, FragmentTransaction transaction) {
        for (String fragmentTag : tags) {
            Fragment fragment = fragmentManager.findFragmentByTag(fragmentTag);
            if (fragment != null && fragment.isVisible()) {
                transaction.hide(fragment);
            }
        }
    }
}
