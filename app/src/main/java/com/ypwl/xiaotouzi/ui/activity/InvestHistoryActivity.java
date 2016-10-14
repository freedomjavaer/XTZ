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
import com.ypwl.xiaotouzi.ui.fragment.FragmentInvestHistoryByBid;
import com.ypwl.xiaotouzi.ui.fragment.FragmentInvestHistoryByPlatform;

/**
 * function :历史资产
 * Created by llc on 2016/3/23 15:34
 * Email：licailuo@qq.com
 */
public class InvestHistoryActivity extends BaseActivity implements View.OnClickListener {
    /** 历史资产：标的分类 */
    public static final String FRAGMENT_TAG_HISTORY_ASSETS_BID = "fragment_tag_history_assets_bid";
    /** 历史资产：平台分类 */
    public static final String FRAGMENT_TAG_HISTORY_ASSETS_PLATFORM = "fragment_tag_history_assets_platform";
    private static final String[] tags = new String[]{FRAGMENT_TAG_HISTORY_ASSETS_BID, FRAGMENT_TAG_HISTORY_ASSETS_PLATFORM};
    /** 在投标的 */
    private FragmentInvestHistoryByBid mFragmentInvestHistoryByBid;
    private FragmentInvestHistoryByPlatform mFragmentInvestHistoryByPlatform;
    /** fragment管理者 */
    private FragmentManager mFragmentManager;
    private TextView mTvTitle;
    private ImageView mIvChangeIcon;
    private boolean isShowByDate = true;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invest_history);
        mFragmentManager = getSupportFragmentManager();
        //标题
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvTitle.setText(getResources().getString(R.string.history_assets));
        ((TextView)findViewById(R.id.tv_title_back)).setText("我的投资");
        mIvChangeIcon = (ImageView) findViewById(R.id.iv_title_right_image);
        mIvChangeIcon.setVisibility(View.VISIBLE);
        mIvChangeIcon.setImageResource(R.mipmap.pic_all_back_list_1);
        findViewById(R.id.layout_title_back).setOnClickListener(this);
        mIvChangeIcon.setOnClickListener(this);
        changeFragment();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_title_back://返回
                finish();
                break;
            case R.id.iv_title_right_image://切换页面
                changeFragment();
                break;
        }
    }


    private void changeFragment() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        hideFragments(mFragmentManager, transaction);
        if (isShowByDate) {
            if (mFragmentInvestHistoryByBid == null) {
                mFragmentInvestHistoryByBid = new FragmentInvestHistoryByBid();
                transaction.add(R.id.fragment_container, mFragmentInvestHistoryByBid, FRAGMENT_TAG_HISTORY_ASSETS_BID);
            }
            transaction.show(mFragmentInvestHistoryByBid);
            mIvChangeIcon.setImageResource(R.mipmap.pic_all_back_list_1);
        } else {
            if (mFragmentInvestHistoryByPlatform == null) {
                mFragmentInvestHistoryByPlatform = new FragmentInvestHistoryByPlatform();
                transaction.add(R.id.fragment_container, mFragmentInvestHistoryByPlatform, FRAGMENT_TAG_HISTORY_ASSETS_PLATFORM);
            }
            transaction.show(mFragmentInvestHistoryByPlatform);
            mIvChangeIcon.setImageResource(R.mipmap.pic_all_back_list_2);
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
