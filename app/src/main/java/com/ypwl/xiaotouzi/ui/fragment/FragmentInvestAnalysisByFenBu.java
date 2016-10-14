package com.ypwl.xiaotouzi.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.base.BaseFragment;
import com.ypwl.xiaotouzi.common.UmengEventID;
import com.ypwl.xiaotouzi.manager.UmengEventHelper;

/**
 * function : 投资分析--->分布
 * <p/>
 * Created by tengtao on 2016/4/9.
 */
public class FragmentInvestAnalysisByFenBu extends BaseFragment {
    /**分布类型选择*/
    public static final String FRAGMENT_TAG_FENBU_ANALYSIS_BY_PLATFORM = "fragment_tag_fenbu_analysis_by_platform";
    public static final String FRAGMENT_TAG_FENBU_ANALYSIS_BY_CYCLE = "fragment_tag_fenbu_analysis_by_cycle";
    public static final String FRAGMENT_TAG_FENBU_ANALYSIS_BY_ANNUALIZE = "fragment_tag_fenbu_nanlysis_by_annualize";
    public static final String FRAGMENT_TAG_FENBU_ANALYSIS_BY_RETURNED_TYPE = "fragment_tag_fenbu_nanlysis_by_returned_type";
    private static final String[] tags = new String[]{FRAGMENT_TAG_FENBU_ANALYSIS_BY_PLATFORM, FRAGMENT_TAG_FENBU_ANALYSIS_BY_CYCLE,
            FRAGMENT_TAG_FENBU_ANALYSIS_BY_ANNUALIZE,FRAGMENT_TAG_FENBU_ANALYSIS_BY_RETURNED_TYPE};
    private FragmentInvestFenBuOption mFragmentInvestFenBuOption;
    private FragmentInvestFenBuOption mFragmentInvestFenBuByCycle;
    private FragmentInvestFenBuOption mFragmentInvestFenBuByAnnualize;
    private FragmentInvestFenBuOption mFragmentInvestFenBuByReturnedType;
    /** 选择按钮 */
    private RadioGroup mRadioGroup;
    private FragmentManager mFragmentManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_invest_analysis_by_fen_bu, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mFragmentManager = getActivity().getSupportFragmentManager();
        mRadioGroup = (RadioGroup) view.findViewById(R.id.radio_tab_invest_analysis_by_fenbu);
        mRadioGroup.setOnCheckedChangeListener(mOnTabCheckedListener);
        mRadioGroup.check(R.id.rb_fenbu_platform);//默认选中平台分布页面
    }

    /** tab切换监听 */
    private RadioGroup.OnCheckedChangeListener mOnTabCheckedListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            hideFragments(mFragmentManager, transaction);
            switch (checkedId) {
                case R.id.rb_fenbu_platform:// 分布-->平台
                    if (mFragmentInvestFenBuOption == null) {
                        mFragmentInvestFenBuOption = FragmentInvestFenBuOption.newInstance(FRAGMENT_TAG_FENBU_ANALYSIS_BY_PLATFORM);
                        transaction.add(R.id.fl_invest_analysis_by_fenbu_container, mFragmentInvestFenBuOption, FRAGMENT_TAG_FENBU_ANALYSIS_BY_PLATFORM);
                    }
                    resetStatus(mFragmentInvestFenBuOption);
                    transaction.show(mFragmentInvestFenBuOption);
                    UmengEventHelper.onEvent(UmengEventID.InvestAnalysisFenbuByPlatform);
                    break;
                case R.id.rb_fenbu_cycle:// 分布-->周期
                    if (mFragmentInvestFenBuByCycle == null) {
                        mFragmentInvestFenBuByCycle = FragmentInvestFenBuOption.newInstance(FRAGMENT_TAG_FENBU_ANALYSIS_BY_CYCLE);
                        transaction.add(R.id.fl_invest_analysis_by_fenbu_container, mFragmentInvestFenBuByCycle, FRAGMENT_TAG_FENBU_ANALYSIS_BY_CYCLE);
                    }
                    resetStatus(mFragmentInvestFenBuByCycle);
                    transaction.show(mFragmentInvestFenBuByCycle);
                    UmengEventHelper.onEvent(UmengEventID.InvestAnalysisFenbuByCycle);
                    break;
                case R.id.rb_fenbu_annualize:// 分布-->年化率
                    if (mFragmentInvestFenBuByAnnualize == null) {
                        mFragmentInvestFenBuByAnnualize = FragmentInvestFenBuOption.newInstance(FRAGMENT_TAG_FENBU_ANALYSIS_BY_ANNUALIZE);
                        transaction.add(R.id.fl_invest_analysis_by_fenbu_container, mFragmentInvestFenBuByAnnualize, FRAGMENT_TAG_FENBU_ANALYSIS_BY_ANNUALIZE);
                    }
                    resetStatus(mFragmentInvestFenBuByAnnualize);
                    transaction.show(mFragmentInvestFenBuByAnnualize);
                    UmengEventHelper.onEvent(UmengEventID.InvestAnalysisFenbuByAnnualize);
                    break;
                case R.id.rb_fenbu_return_type:// 分布-->还款方式
                    if (mFragmentInvestFenBuByReturnedType == null) {
                        mFragmentInvestFenBuByReturnedType = FragmentInvestFenBuOption.newInstance(FRAGMENT_TAG_FENBU_ANALYSIS_BY_RETURNED_TYPE);
                        transaction.add(R.id.fl_invest_analysis_by_fenbu_container, mFragmentInvestFenBuByReturnedType, FRAGMENT_TAG_FENBU_ANALYSIS_BY_RETURNED_TYPE);
                    }
                    resetStatus(mFragmentInvestFenBuByReturnedType);
                    transaction.show(mFragmentInvestFenBuByReturnedType);
                    UmengEventHelper.onEvent(UmengEventID.InvestAnalysisFenbuByReturnedType);
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

    /** 重置状态 */
    private void resetStatus(FragmentInvestFenBuOption fenBuOption){
        fenBuOption.resetStarus();
    }
}
