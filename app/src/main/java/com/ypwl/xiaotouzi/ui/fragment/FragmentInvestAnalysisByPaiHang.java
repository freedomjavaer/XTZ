package com.ypwl.xiaotouzi.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.base.BaseFragment;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.view.RadioTab;

import java.util.Arrays;

/**
 * function : 投资分析--->排行
 * <p/>
 * Created by tengtao on 2016/4/9.
 */
public class FragmentInvestAnalysisByPaiHang extends BaseFragment {

    private final String[] mTabTitleArr = new String[]{
            UIUtil.getString(R.string.radio_btn_profit),
            UIUtil.getString(R.string.radio_btn_rate)};
    private RadioTab mRadioButton;
    private FragmentManager fragmentManager;

    /** Fragment TAG :  收益 */
    public static final String FRAGMENT_TAG_PROFIT = "fragment_tag_profit";
    /** Fragment TAG :  年化率 */
    public static final String FRAGMENT_TAG_RATE = "fragment_tag_rate";
    /** Fragment TAG数组 */
    private static final String[] mFragmentTags = new String[]{FRAGMENT_TAG_PROFIT, FRAGMENT_TAG_RATE};


    private FragmentMyProfit mFragmentMyProfit;
    private FragmentMyRate mFragmentMyRate;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getActivity().getSupportFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_range_profit, container, false);

        mRadioButton = (RadioTab) view.findViewById(R.id.rt_button);
        mRadioButton.setTabTitleTextColorBackgroundResource(R.color.base_color_radiotab_text_selector)
                .setTabLeftBackgroundResource(R.drawable.base_radiotab_bg_left_selector)
                .setTabMiddleBackgroundResource(R.drawable.base_radiotab_bg_middle_selector)
                .setTabRightBackgroundResource(R.drawable.base_radiotab_bg_right_selector)
                .setOnTabCheckedListener(mOnTabCheckedListener).addTabs(Arrays.asList(mTabTitleArr));

        return view;
    }



    /** tab切换监听 */
    private RadioTab.OnTabCheckedListener mOnTabCheckedListener = new RadioTab.OnTabCheckedListener() {
        @Override
        public void onChecked(View view, CharSequence checkedTitle, int checkedIndex) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            hideFragments(fragmentManager, transaction);
            switch (checkedIndex) {
                case 0:// 收益
                    if (mFragmentMyProfit == null) {
                        mFragmentMyProfit = new FragmentMyProfit();
                        transaction.add(R.id.fl_fragment_container,mFragmentMyProfit, FRAGMENT_TAG_PROFIT);
                    }
                    transaction.show(mFragmentMyProfit);
                    break;
                case 1:// 年化率
                    if (mFragmentMyRate == null) {
                        mFragmentMyRate = new FragmentMyRate();
                        transaction.add(R.id.fl_fragment_container, mFragmentMyRate, FRAGMENT_TAG_RATE);
                    }
                    transaction.show(mFragmentMyRate);
                    break;
            }
            transaction.commit();
        }
    };

    /** 隐藏所有的fragment */
    private void hideFragments(FragmentManager fragmentManager, FragmentTransaction transaction) {
        for (String fragmentTag : mFragmentTags) {
            Fragment fragment = fragmentManager.findFragmentByTag(fragmentTag);
            if (fragment != null && fragment.isVisible()) {
                transaction.hide(fragment);
            }
        }
    }


}
