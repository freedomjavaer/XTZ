package com.ypwl.xiaotouzi.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.base.BaseFragment;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.common.UmengEventID;
import com.ypwl.xiaotouzi.manager.UmengEventHelper;
import com.ypwl.xiaotouzi.ui.activity.ContinualTallyActivity;
import com.ypwl.xiaotouzi.ui.activity.InvestHistoryActivity;
import com.ypwl.xiaotouzi.ui.activity.KeepAccountsActivity;
import com.ypwl.xiaotouzi.ui.activity.LoginActivity;
import com.ypwl.xiaotouzi.ui.activity.MyInvestAnalysisActivity;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.Util;
import com.ypwl.xiaotouzi.view.RadioTab;

import java.util.Arrays;

/**
 * function : 我的投资
 * <p/>
 * Created by tengtao on 2016/3/22.
 */
public class FragmentNewMyInvest extends BaseFragment implements View.OnClickListener {

    /** 回款和资产选择 */
    private static final String[] mTabTitleArr = new String[]{"资产", "回款"};
    public static final String FRAGMENT_TAG_RETURNED_MONEY = "fragment_tag_returned_money";
    public static final String FRAGMENT_TAG_ASSETS = "fragment_tag_assets";
    private static final String[] tags = new String[]{FRAGMENT_TAG_RETURNED_MONEY, FRAGMENT_TAG_ASSETS};
    /** 回款页面 */
    private FragmentRecentReturned mFragmentRecentReturned;
    /** 资产页面 */
    private FragmentInvestAssets mFragmentInvestAssets;
    /** fragment管理者 */
    private FragmentManager fragmentManager;
    private RadioTab mRadioTab;
    private ImageView mIvThreePoint;//资产部分选项按钮
    private PopupWindow popupWindow;
    private Intent intent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_invest, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        fragmentManager = getActivity().getSupportFragmentManager();
        mIvThreePoint = (ImageView) view.findViewById(R.id.iv_my_invest_three_point);
        mIvThreePoint.setOnClickListener(this);
        view.findViewById(R.id.iv_my_invest_tally).setOnClickListener(this);//记账

        /**初始化回款和资产切换 */
        mRadioTab = (RadioTab) view.findViewById(R.id.rt_returned_assets);
//        mRadioTab.setTabTitleTextColorBackgroundResource(R.color.selector_color_my_invest_returned_assets_txt)
        mRadioTab.setTabTitleTextColorBackgroundResource(R.color.selector_color_my_invest_returned_assets_txt)
                .setTabLeftBackgroundResource(R.drawable.my_invest_returned_bg_selector)
                .setTabRightBackgroundResource(R.drawable.my_invest_assets_bg_selector);
        mRadioTab.addTabs(Arrays.asList(mTabTitleArr));
        mRadioTab.setOnTabCheckedListener(mOnTabCheckedListener);
        mOnTabCheckedListener.onChecked(null, null, 0);//默认选中回款页面

    }

    /** tab切换监听 */
    private RadioTab.OnTabCheckedListener mOnTabCheckedListener = new RadioTab.OnTabCheckedListener() {
        @Override
        public void onChecked(View view, CharSequence checkedTitle, int checkedIndex) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            hideFragments(fragmentManager, transaction);
            switch (checkedIndex) {
                case 0:// 资产
                    if (mFragmentInvestAssets == null) {
                        mFragmentInvestAssets = new FragmentInvestAssets();
                        transaction.add(R.id.fl_returned_assets_container, mFragmentInvestAssets, FRAGMENT_TAG_ASSETS);
                    }
                    transaction.show(mFragmentInvestAssets);
                    UmengEventHelper.onEvent(UmengEventID.MyInvestAssetsButton);
                    break;
                case 1:// 回款

                    if (mFragmentRecentReturned == null) {
                        mFragmentRecentReturned = new FragmentRecentReturned();
                        transaction.add(R.id.fl_returned_assets_container, mFragmentRecentReturned, FRAGMENT_TAG_RETURNED_MONEY);
                    }
                    transaction.show(mFragmentRecentReturned);
                    UmengEventHelper.onEvent(UmengEventID.MyInvestRecentBackMoneyButton);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_my_invest_three_point://投资流水、历史资产
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                } else {
                    initmPopupWindowView();
                    popupWindow.showAsDropDown(v, 0, UIUtil.dip2px(-25));
                }
                break;
            case R.id.iv_my_invest_tally://记账
                intent = new Intent(getContext(), KeepAccountsActivity.class);
                intent.putExtra(Const.KEY_INTENT_JUMP_FROM_DATA, "我的投资");
                startActivity(intent);
                UmengEventHelper.onEvent(UmengEventID.MyInvestTallyButton);
                break;
        }
    }

    /**初始化弹窗*/
    public void initmPopupWindowView() {
        View customView = View.inflate(getActivity(),R.layout.layout_my_invest_assets_three_point_choice, null);
        popupWindow = new PopupWindow(customView, UIUtil.dip2px(125), UIUtil.dip2px(140));// 宽度和高度
        popupWindow.setAnimationStyle(R.style.AnimationLeftFade);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//点击外面消失
        customView.findViewById(R.id.ll_my_invest_analysis).setOnClickListener(mOnClickListener);
        customView.findViewById(R.id.ll_my_invest_history_assets).setOnClickListener(mOnClickListener);
        customView.findViewById(R.id.ll_my_invest_assets_stream).setOnClickListener(mOnClickListener);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_my_invest_analysis://统计分析
                    startActivity(Util.legalLogin() != null ? MyInvestAnalysisActivity.class : LoginActivity.class);
                    UmengEventHelper.onEvent(UmengEventID.MyInvestAssetsAnalysisButton);
                    break;
                case R.id.ll_my_invest_history_assets://历史资产
                    startActivity(InvestHistoryActivity.class);
                    UmengEventHelper.onEvent(UmengEventID.MyInvestAssetsHistoryButton);
                    break;
                case R.id.ll_my_invest_assets_stream://资产流水
                    intent = new Intent(getContext(),ContinualTallyActivity.class);
                    intent.putExtra(Const.KEY_INTENT_JUMP_FROM_DATA, "我的投资");
                    startActivity(intent);
                    UmengEventHelper.onEvent(UmengEventID.MyInvestAssetsContinualButton);
                    break;
            }
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
                popupWindow = null;
            }
        }
    };
}
