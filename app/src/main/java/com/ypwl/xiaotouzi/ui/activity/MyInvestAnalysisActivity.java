package com.ypwl.xiaotouzi.ui.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.common.UmengEventID;
import com.ypwl.xiaotouzi.event.ProfitRangeStatusChangeEvent;
import com.ypwl.xiaotouzi.manager.EventHelper;
import com.ypwl.xiaotouzi.manager.UmengEventHelper;
import com.ypwl.xiaotouzi.ui.fragment.FragmentAnnualRateTendency;
import com.ypwl.xiaotouzi.ui.fragment.FragmentInvestAnalysisByFenBu;
import com.ypwl.xiaotouzi.ui.fragment.FragmentInvestAnalysisByPaiHang;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.UIUtil;


/**
 * @author tengtao
 * @time ${DATA} 18:11
 * @des 我的投资：投资分析页面
 */
public class MyInvestAnalysisActivity extends BaseActivity implements View.OnClickListener {
    private TextView mTvBack, mTvTitle,mTvFenBu,mTvQuShi,mTvPaiHang;
    private RelativeLayout mLineContainer;
    private View mBlueLine;
    private FragmentManager mFragmentManager;
    private ImageView mTitleOperate;
    private PopupWindow mPopupWindow;
    /** 我的投资分析--分布 */
    private FragmentInvestAnalysisByFenBu mAnalysisFenBu;
    public static final String FRAGMENT_TAG_ANALYSIS_BY_FENBU = "fragment_tag_analysis_by_fenbu";
    /** 我的投资分析--排行 */
    private FragmentInvestAnalysisByPaiHang mAnalysisPaiHang;
    public static final String FRAGMENT_TAG_ANALYSIS_BY_PAIHANG = "fragment_tag_analysis_by_paihang";
    /** 我的投资分析--趋势 */
    private FragmentAnnualRateTendency mAnalysisQuShi;
    public static final String FRAGMENT_TAG_ANALYSIS_BY_QUSHI = "fragment_tag_analysis_by_qushi";

    private static final String[] tags = new String[]{FRAGMENT_TAG_ANALYSIS_BY_FENBU, FRAGMENT_TAG_ANALYSIS_BY_PAIHANG,FRAGMENT_TAG_ANALYSIS_BY_QUSHI};
    /** 分布 */
    private final int TYPE_FEN_BU = 1;
    /** 趋势 */
    private final int TYPE_QU_SHI = 2;
    /** 排行 */
    private final int TYPE_PAI_HANG = 3;
    private int currType;//当前选择类型
    private int width;
    private int distance = 0;//粗线条移动的位移

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_invest_analysis);
        width = getWindowManager().getDefaultDisplay().getWidth();
        mFragmentManager = getSupportFragmentManager();
        initView();
    }

    private void initView() {
        //标题部分
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTitleOperate = (ImageView) findViewById(R.id.iv_title_right_image);
        mTitleOperate.setOnClickListener(this);
        mTvTitle.setText(getResources().getString(R.string.statistics_analysis));
        mTvBack = (TextView) findViewById(R.id.tv_title_back);
        mTvBack.setText("我的投资");
        findViewById(R.id.layout_title_back).setOnClickListener(this);
        //切换区域
        mTvFenBu = (TextView) findViewById(R.id.tv_my_invest_analysis_fenbu);
        mTvFenBu.setOnClickListener(this);
        mTvPaiHang = (TextView) findViewById(R.id.tv_my_invest_analysis_paihang);
        mTvPaiHang.setOnClickListener(this);
        mTvQuShi = (TextView) findViewById(R.id.tv_my_invest_analysis_qushi);
        mTvQuShi.setOnClickListener(this);
        //标识线
        mLineContainer = (RelativeLayout) findViewById(R.id.layout_line_container);
        mBlueLine = new View(mActivity);
        mBlueLine.setBackgroundColor(Color.parseColor("#0078FF"));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width/3, UIUtil.dip2px(4));
        mBlueLine.setLayoutParams(params);
        mLineContainer.addView(mBlueLine);
        //默认选择投资分布
        changeChoose(TYPE_FEN_BU);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_title_back://返回
                finish();
                break;
            case R.id.tv_my_invest_analysis_fenbu://分布
                changeChoose(TYPE_FEN_BU);
                break;
            case R.id.tv_my_invest_analysis_paihang://排行
                changeChoose(TYPE_PAI_HANG);
                break;
            case R.id.tv_my_invest_analysis_qushi://趋势
                changeChoose(TYPE_QU_SHI);
                break;
            case R.id.iv_title_right_image:
                initmPopupWindowView();
                mPopupWindow.showAsDropDown(v, 0, UIUtil.dip2px(-20));
                break;
        }
    }

    /**状态类型---0为在投；1为结束；2为全部*/
    private int status = 2;
    /**项目类型---0为标；1为平台*/
    private int type = 1;

    public void initmPopupWindowView() {
        View customView = UIUtil.inflate(R.layout.layout_profit_range_pop_up_choose);
        mPopupWindow = new PopupWindow(customView, UIUtil.dip2px(210), UIUtil.dip2px(190));// 宽度和高度
        mPopupWindow.setAnimationStyle(R.style.AnimationFade);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//点击外面消失
        //背景变换
        final WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.8f;
        getWindow().setAttributes(lp);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });

        LinearLayout totalData = (LinearLayout) customView.findViewById(R.id.ll_total_data);
        LinearLayout bidData = (LinearLayout) customView.findViewById(R.id.ll_bid_data);
        LinearLayout endData = (LinearLayout) customView.findViewById(R.id.ll_end_bid);
        LinearLayout platformChoose = (LinearLayout) customView.findViewById(R.id.ll_platform_choose);
        LinearLayout bidChoose = (LinearLayout) customView.findViewById(R.id.ll_bid_choose);

        final ImageView mChooseTotal = (ImageView) customView.findViewById(R.id.iv_choose_total);
        final ImageView mChooseBiding = (ImageView) customView.findViewById(R.id.iv_choose_biding);
        final ImageView mChooseEndBid = (ImageView) customView.findViewById(R.id.iv_choose_end_bid);
        final ImageView mChoosePlatform = (ImageView) customView.findViewById(R.id.iv_choose_platform);
        final ImageView mChooseBid = (ImageView) customView.findViewById(R.id.iv_choose_bid);

        if (status == 0){
            mChooseTotal.setVisibility(View.INVISIBLE);
            mChooseBiding.setVisibility(View.VISIBLE);
            mChooseEndBid.setVisibility(View.INVISIBLE);
        }else if (status == 1){
            mChooseTotal.setVisibility(View.INVISIBLE);
            mChooseBiding.setVisibility(View.INVISIBLE);
            mChooseEndBid.setVisibility(View.VISIBLE);
        }else if (status == 2){
            mChooseTotal.setVisibility(View.VISIBLE);
            mChooseBiding.setVisibility(View.INVISIBLE);
            mChooseEndBid.setVisibility(View.INVISIBLE);
        }

        if (type == 0){
            mChoosePlatform.setVisibility(View.INVISIBLE);
            mChooseBid.setVisibility(View.VISIBLE);
        }else {
            mChoosePlatform.setVisibility(View.VISIBLE);
            mChooseBid.setVisibility(View.INVISIBLE);
        }

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.ll_total_data://显示全部数据
                        mChooseTotal.setVisibility(View.VISIBLE);
                        mChooseBiding.setVisibility(View.INVISIBLE);
                        mChooseEndBid.setVisibility(View.INVISIBLE);
                        if (status == 2){
                            return;
                        }else {
                            status = 2;
                            GlobalUtils.STATUS = 2;
                            EventHelper.post(new ProfitRangeStatusChangeEvent(status, type));
                        }

                        break;
                    case R.id.ll_bid_data://显示在投标数据
                        mChooseTotal.setVisibility(View.INVISIBLE);
                        mChooseBiding.setVisibility(View.VISIBLE);
                        mChooseEndBid.setVisibility(View.INVISIBLE);
                        if (status == 0){
                            return;
                        }else {
                            status = 0;
                            GlobalUtils.STATUS = 0;
                            EventHelper.post(new ProfitRangeStatusChangeEvent(status, type));
                        }
                        break;
                    case R.id.ll_end_bid://显示结束标数据
                        mChooseTotal.setVisibility(View.INVISIBLE);
                        mChooseBiding.setVisibility(View.INVISIBLE);
                        mChooseEndBid.setVisibility(View.VISIBLE);
                        if (status == 1){
                            return;
                        }else {
                            status = 1;
                            GlobalUtils.STATUS = 1;
                            EventHelper.post(new ProfitRangeStatusChangeEvent(status, type));
                        }
                        break;
                    case R.id.ll_platform_choose://显示平台数据
                        mChoosePlatform.setVisibility(View.VISIBLE);
                        mChooseBid.setVisibility(View.INVISIBLE);
                        if (type == 1){
                            return;
                        }else {
                            type = 1;
                            GlobalUtils.B_TYPE = 1;
                            EventHelper.post(new ProfitRangeStatusChangeEvent(status, type));
                        }

                        break;
                    case R.id.ll_bid_choose://显示标数据
                        mChoosePlatform.setVisibility(View.INVISIBLE);
                        mChooseBid.setVisibility(View.VISIBLE);
                        if (type == 0){
                            return;
                        }else {
                            type = 0;
                            GlobalUtils.B_TYPE = 0;
                            EventHelper.post(new ProfitRangeStatusChangeEvent(status, type));
                        }
                        break;
                }
            }
        };

        totalData.setOnClickListener(onClickListener);
        bidData.setOnClickListener(onClickListener);
        endData.setOnClickListener(onClickListener);
        platformChoose.setOnClickListener(onClickListener);
        bidChoose.setOnClickListener(onClickListener);
    }


    /** 改变分析选择 */
    private void changeChoose(int type){
        if(currType == type){return;}
        currType = type;
        mTvFenBu.setTextColor(Color.parseColor("#8E8E93"));
        mTvPaiHang.setTextColor(Color.parseColor("#8E8E93"));
        mTvQuShi.setTextColor(Color.parseColor("#8E8E93"));
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        hideFragments(mFragmentManager, transaction);
        switch (type){
            case TYPE_FEN_BU://分布
                mTvFenBu.setTextColor(getResources().getColor(R.color.a));
                mTitleOperate.setVisibility(View.GONE);
                lineAnimation(mBlueLine, distance, 0);
                if(mAnalysisFenBu==null){
                    mAnalysisFenBu = new FragmentInvestAnalysisByFenBu();
                    transaction.add(R.id.fl_my_invest_analysis_container, mAnalysisFenBu, FRAGMENT_TAG_ANALYSIS_BY_FENBU);
                }
                transaction.show(mAnalysisFenBu);
                UmengEventHelper.onEvent(UmengEventID.MyInvestAnalysisByFenBu);
                break;
            case TYPE_QU_SHI://趋势
                mTvQuShi.setTextColor(getResources().getColor(R.color.a));
                mTitleOperate.setVisibility(View.GONE);
                lineAnimation(mBlueLine, distance, width * 2/ 3);
                if(mAnalysisQuShi==null){
                    mAnalysisQuShi = new FragmentAnnualRateTendency();
                    transaction.add(R.id.fl_my_invest_analysis_container, mAnalysisQuShi, FRAGMENT_TAG_ANALYSIS_BY_QUSHI);
                }
                transaction.show(mAnalysisQuShi);
                UmengEventHelper.onEvent(UmengEventID.MyInvestAnalysisByQuShi);
                break;
            case TYPE_PAI_HANG://排行
                mTvPaiHang.setTextColor(getResources().getColor(R.color.a));
                mTitleOperate.setVisibility(View.VISIBLE);
                mTitleOperate.setImageResource(R.mipmap.three_blue_posint);
                lineAnimation(mBlueLine, distance, width / 3);
                if(mAnalysisPaiHang==null){
                    mAnalysisPaiHang = new FragmentInvestAnalysisByPaiHang();
                    transaction.add(R.id.fl_my_invest_analysis_container, mAnalysisPaiHang, FRAGMENT_TAG_ANALYSIS_BY_PAIHANG);
                }
                transaction.show(mAnalysisPaiHang);
                UmengEventHelper.onEvent(UmengEventID.MyInvestAnalysisByPaiHang);
                break;
        }
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

    /** 线条移动动画 */
    private void lineAnimation(View view,int start,int end){
        distance += end-start;
        TranslateAnimation animation = new TranslateAnimation(start,end,0,0);
        animation.setFillAfter(true);
        animation.setDuration(200);
        view.startAnimation(animation);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        GlobalUtils.STATUS = 2;
        GlobalUtils.B_TYPE = 1;
    }

}
