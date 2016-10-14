package com.ypwl.xiaotouzi.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.base.BaseFragment;
import com.ypwl.xiaotouzi.common.UmengEventID;
import com.ypwl.xiaotouzi.manager.UmengEventHelper;
import com.ypwl.xiaotouzi.utils.UIUtil;

/**
 * 项目名:	XTZ
 * 包名:	com.ypwl.xiaotouzi.ui.fragment
 * 类名:	FragmentFinanceSupermarket
 * 作者:	罗霄
 * 创建时间:	2016/4/15 15:19
 * <p/>
 * 描述:	金融超市页面
 * <p/>
 * svn版本:	$Revision: 16041 $
 * 更新人:	$Author: pengdakai $
 * 更新时间:	$Date: 2016-09-26 17:02:46 +0800 (周一, 26 九月 2016) $
 * 更新描述:	$Message$
 */
public class FragmentFinanceSupermarket extends BaseFragment implements View.OnClickListener {

    private TextView mTvChoice;
    private TextView mTvPlatform;
    private TextView mTvMore;
    private View mBlueLine;

    private int mDefShowPosition = 0;

    private int distance;//粗线条移动的开始位移
    private FragmentManager mFragmentManager;

    private int currentTitleID;
    private int mDefaultDisplayWidth;
    private FragmentFinanceSupermarketOfMore mFragmentMore;
    private FragmentFinanceSupermarketOfChoice mFragmentChoice;
    private FragmentFinanceSupermarketOfPlatform mFragmentPlatform;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_finance_supermarket, container, false);
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {

        mFragmentManager = getActivity().getSupportFragmentManager();

        mDefaultDisplayWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();

        initView(v);
    }

    private void initView(View rootView) {
        ((TextView) findView(rootView, R.id.tv_title)).setText(UIUtil.getString(R.string.finance_supermarket_fragment_name));
        findView(rootView, R.id.layout_title_back).setVisibility(View.GONE);

        //切换区域
        mTvChoice = findView(rootView, R.id.fragment_finance_supermarket_tv_choice);
        mTvChoice.setOnClickListener(this);
        mTvChoice.setTag(FragmentFinanceSupermarketOfChoice.class.getSimpleName());

        mTvPlatform = findView(rootView, R.id.fragment_finance_supermarket_tv_platform);
        mTvPlatform.setOnClickListener(this);
        mTvPlatform.setTag(FragmentFinanceSupermarketOfPlatform.class.getSimpleName());

        mTvMore = findView(rootView, R.id.fragment_finance_supermarket_tv_more);
        mTvMore.setOnClickListener(this);
        mTvMore.setTag(FragmentFinanceSupermarketOfMore.class.getSimpleName());

        //标识线容器
        RelativeLayout mLineContainer = findView(rootView, R.id.fragment_finance_supermarket_rl_line_container);
        mBlueLine = initBlueLine();
        mLineContainer.addView(mBlueLine);
        //默认选择精选
        changeChoose(parseView(mDefShowPosition));
    }

    /** 初始化标记线 */
    private View initBlueLine() {
        RelativeLayout mViewGroup = new RelativeLayout(getActivity());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(mDefaultDisplayWidth / 3, UIUtil.dip2px(3));
        mViewGroup.setLayoutParams(params);

        View mChildView = new View(getActivity());
        RelativeLayout.LayoutParams paramsBlueLine = new RelativeLayout.LayoutParams(mDefaultDisplayWidth / 360 * 74, UIUtil.dip2px(3));
        paramsBlueLine.addRule(RelativeLayout.CENTER_HORIZONTAL);
        mChildView.setLayoutParams(paramsBlueLine);
        mChildView.setBackgroundColor(UIUtil.getColor(R.color.finance_supermarket_title_select));
        mViewGroup.addView(mChildView);

        return mViewGroup;
    }

    @Override
    public void onClick(View v) {
        if (null != v){
            changeChoose((TextView) v);
        }

//        switch (v.getId()) {
//            case R.id.fragment_finance_supermarket_tv_choice://精选
//                changeChoose(mTvChoice);
//                break;
//            case R.id.fragment_finance_supermarket_tv_platform://平台
//                changeChoose(mTvPlatform);
//                break;
//            case R.id.fragment_finance_supermarket_tv_more://更多
//                changeChoose(mTvMore);
//                break;
//        }
    }

    /**
     * 设置默认显示的页面
     * @param position 对应序号
     */
    public void setDefShow(int position){
        mDefShowPosition = position;
        onClick(parseView(position));
    }

    private TextView parseView(int position){
        TextView mView = null;
        switch (position){
            case 0:
                mView = mTvChoice;
                break;
            case 1:
                mView = mTvPlatform;
                break;
            case 2:
                mView = mTvMore;
                break;
        }
        return mView;
    }

    /** 改变分析选择 */
    private void changeChoose(TextView selectView) {
        if (currentTitleID == selectView.getId()) {
            return;
        }
        currentTitleID = selectView.getId();

        FragmentTransaction transaction = resetting();

        selectView.setTextColor(UIUtil.getColor(R.color.finance_supermarket_title_select));

        switch (currentTitleID) {
            case R.id.fragment_finance_supermarket_tv_choice://精选
                lineAnimation(mBlueLine, distance, 0);
                if (mFragmentChoice == null) {
                    mFragmentChoice = new FragmentFinanceSupermarketOfChoice();
                    transaction.add(R.id.fragment_finance_supermarket_fl_fragment_container, mFragmentChoice, (String) selectView.getTag());
                }
                transaction.show(mFragmentChoice);
                UmengEventHelper.onEvent(UmengEventID.FinanceSupermarketChoice);
                break;
            case R.id.fragment_finance_supermarket_tv_platform://平台
                lineAnimation(mBlueLine, distance, mDefaultDisplayWidth / 3);
                if(mFragmentPlatform==null){
                    mFragmentPlatform = new FragmentFinanceSupermarketOfPlatform();
                    transaction.add(R.id.fragment_finance_supermarket_fl_fragment_container, mFragmentPlatform, (String) selectView.getTag());
                }
                transaction.show(mFragmentPlatform);
                UmengEventHelper.onEvent(UmengEventID.FsmPlatform);
                break;
            case R.id.fragment_finance_supermarket_tv_more://更多
                lineAnimation(mBlueLine, distance, mDefaultDisplayWidth * 2 / 3);
                if (mFragmentMore == null) {
                    mFragmentMore = new FragmentFinanceSupermarketOfMore();
                    transaction.add(R.id.fragment_finance_supermarket_fl_fragment_container, mFragmentMore, (String) selectView.getTag());
                }
                transaction.show(mFragmentMore);
                UmengEventHelper.onEvent(UmengEventID.FinanceSupermarketMore);
                break;
        }
        transaction.commit();
    }


    private FragmentTransaction resetting() {
        unSelectTextView(mTvChoice, mTvPlatform, mTvMore);
        return hideFragments(mTvChoice, mTvPlatform, mTvMore);
    }

    private void unSelectTextView(TextView... v) {
        for (TextView tv : v) {
            tv.setTextColor(UIUtil.getColor(R.color.finance_supermarket_title_unselect));
        }
    }

    /** 隐藏所有的fragment */
    private FragmentTransaction hideFragments(View... v) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        for (View view : v) {
            Fragment fragment = mFragmentManager.findFragmentByTag((String) view.getTag());
            if (fragment != null && fragment.isVisible()) {
                fragmentTransaction.hide(fragment);
            }
        }
        return fragmentTransaction;
    }

    /** 线条移动动画 */
    private void lineAnimation(View view, int start, int end) {
        distance += end - start;
        TranslateAnimation animation = new TranslateAnimation(start, end, 0, 0);
        animation.setFillAfter(true);
        animation.setDuration(200);
        view.startAnimation(animation);
    }
}
