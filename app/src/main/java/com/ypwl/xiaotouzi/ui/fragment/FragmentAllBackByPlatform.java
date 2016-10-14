package com.ypwl.xiaotouzi.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.base.BaseFragment;
import com.ypwl.xiaotouzi.common.UmengEventID;
import com.ypwl.xiaotouzi.manager.UmengEventHelper;
import com.ypwl.xiaotouzi.view.RadioTab;

import java.util.Arrays;

/**
 * function : 全部回款--平台分组：在投标的，结束标的
 * <p/>
 * Created by tengtao on 2016/3/24.
 */
public class FragmentAllBackByPlatform extends BaseFragment {
    private static final String[] mTabTitleArr = new String[]{"在投标的", "结束标的"};
    public static final String FRAGMENT_TAG_BID_ING = "fragment_tag_bid_ing";
    public static final String FRAGMENT_TAG_BID_FINISH = "fragment_tag_bid_finish";
    private static final String[] tags = new String[]{FRAGMENT_TAG_BID_ING, FRAGMENT_TAG_BID_FINISH};
    /** 在投标的 */
    private FragmentBidOption mFragmentBiding;
    private FragmentBidOption mFragmentBidFinished;
    /** fragment管理者 */
    private FragmentManager fragmentManager;
    private RadioTab mRadioTab;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_invest_back_by_platform,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        fragmentManager = getActivity().getSupportFragmentManager();
        /**初始化在投标的和结束标的切换 */
        mRadioTab = (RadioTab) view.findViewById(R.id.rt_invest_back_money_by_platform);
//        mRadioTab.setTabTitleTextColorBackgroundResource(R.color.selector_color_my_invest_returned_assets_txt)
//                .setTabLeftBackgroundResource(R.drawable.my_invest_returned_bg_selector)
//                .setTabRightBackgroundResource(R.drawable.my_invest_assets_bg_selector);

        mRadioTab.addTabs(Arrays.asList(mTabTitleArr));
        mRadioTab.setOnTabCheckedListener(mOnTabCheckedListener);
        mOnTabCheckedListener.onChecked(null, null, 0);//默认在投标的页面
    }

    /** tab切换监听 */
    private RadioTab.OnTabCheckedListener mOnTabCheckedListener = new RadioTab.OnTabCheckedListener() {
        @Override
        public void onChecked(View view, CharSequence checkedTitle, int checkedIndex) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            hideFragments(fragmentManager, transaction);
            switch (checkedIndex) {
                case 0:// 在投标的
                    if (mFragmentBiding == null) {
                        mFragmentBiding = FragmentBidOption.newInstance(FRAGMENT_TAG_BID_ING);
                        transaction.add(R.id.invest_by_platform_fragment_container, mFragmentBiding, FRAGMENT_TAG_BID_ING);
                    }
                    transaction.show(mFragmentBiding);
                    UmengEventHelper.onEvent(UmengEventID.AllBackMoneyBidingButton);
                    break;
                case 1:// 结束标的
                    if (mFragmentBidFinished == null) {
                        mFragmentBidFinished = FragmentBidOption.newInstance(FRAGMENT_TAG_BID_FINISH);
                        transaction.add(R.id.invest_by_platform_fragment_container, mFragmentBidFinished, FRAGMENT_TAG_BID_FINISH);
                    }
                    transaction.show(mFragmentBidFinished);
                    UmengEventHelper.onEvent(UmengEventID.AllBackMoneyBidFinishedButton);
                    break;
            }
            transaction.commit();
        }
    };

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
