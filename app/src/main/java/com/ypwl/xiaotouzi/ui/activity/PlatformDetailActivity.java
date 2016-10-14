package com.ypwl.xiaotouzi.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.squareup.otto.Subscribe;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.XtzApp;
import com.ypwl.xiaotouzi.adapter.PieChartPagerAdapter;
import com.ypwl.xiaotouzi.adapter.PlatformDetailCompanyImgAdapter;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.bean.LoginBean;
import com.ypwl.xiaotouzi.bean.PlatformDetailBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.common.UmengEventID;
import com.ypwl.xiaotouzi.event.IsCollectEvent;
import com.ypwl.xiaotouzi.interf.IRequestCallback;
import com.ypwl.xiaotouzi.manager.PlatformCompareManager;
import com.ypwl.xiaotouzi.manager.UmengEventHelper;
import com.ypwl.xiaotouzi.manager.db.CollectDbOpenHelper;
import com.ypwl.xiaotouzi.manager.net.NetHelper;
import com.ypwl.xiaotouzi.utils.ChartUtils;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.ImgLoadUtil;
import com.ypwl.xiaotouzi.utils.LogUtil;
import com.ypwl.xiaotouzi.utils.StringUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.Util;
import com.ypwl.xiaotouzi.utils.ViewUtil;
import com.ypwl.xiaotouzi.view.MeasuredListView;
import com.ypwl.xiaotouzi.view.PlatformDetailBidLimitPieChart;
import com.ypwl.xiaotouzi.view.PlatformDetailTotalMoneyPieChart;
import com.ypwl.xiaotouzi.view.scrollview.ObservableScrollView;
import com.ypwl.xiaotouzi.view.scrollview.ScrollViewListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * function : 平台详情页面.
 * <p/>
 */
public class PlatformDetailActivity extends BaseActivity implements View.OnClickListener, ScrollViewListener {
    private TextView mPlatformBackgroud;//平台背景
    private ImageView mPlatformLogo;
    private TextView mPlatformFundMoney;//注册资金
    private TextView mPlatformFinancingState;//融资状况
    private TextView mPlatformOnlineDate;//上线时间
    private TextView mPlatformUrl;//平台网址
    private TextView mPlatformLocation;//公司地址
    private TextView mBidProfit;//平均收益
    private TextView mBidAutoInvest;//自动投标
    private TextView mBidAoc;//债权转让
    private TextView mBidTrustFunds;//资金托管
    private TextView mBidGoi;//投标保障
    private TextView mBidMode;//保障模式
    private TextView mBidGuarantor;//担保机构
    private TextView mCompanyInfoName;//公司名称
    private TextView mCompanyInfoLegalPerson;//企业法人
    private TextView mCompanyInfoICP;//ICP备案号
    private TextView mCompanyInfoTEL;//公司电话
    private TextView mCompanyInfoFAX;//公司传真
    private TextView mCompanyInfo400;//400热线
    private TextView mCompanyInfoEmail;//服务邮箱
    private TextView mCompanyInfoBusinessType;//业务类型
    private TextView mCompanyInfoAddress;//公司地址
    private RelativeLayout mLayoutPlatformVideoLogo;//视频
    private MeasuredListView mCompanyInfoImgListView;//显示图片的容器
    private PlatformDetailCompanyImgAdapter mCompanyImgAdapter;
    private PlatformDetailBean mPlatformBean;//平台详情网络请求回来的数据
    private String mPlatformPid;//平台详情唯一标识
    private String mPlatformName;//平台名字
    private TextView mBidPieChatTitle;//饼状图标题
    private ArrayList<View> mPieChartList;
    private PieChartPagerAdapter mPieChartPagerAdapter;
    private ViewPager mBidPieChatViewPager;//容器
    private CombinedChart mPlatformDataChartChengJiaoLiang;//混合图表
    private BarChart mPlatformDataChartTouziRenshu;//投资人柱形图
    private BarChart mPlatformDataChartJieKuanRenshu;//借款人柱形图

    WeakReference<CombinedChart> combinedWeak = null;
    WeakReference<BarChart> barWeak1 = null;
    WeakReference<BarChart> barWeak2 = null;
    WeakReference<List> weakList = null;
    WeakReference<PieChartPagerAdapter> weakAdapter = null;
    WeakReference<ViewPager> weakViewPager = null;

    private LinearLayout mLayoutBtnAddInCompare;//加入对比的布局
    private TextView mAddInCompareTxt;//加入对比
    private LinearLayout mLayoutBtnStartCompare;//开始对比的布局
    private TextView mCompareNumber;//开始对比

    private final int INVESTNUMBER = 0;//投资人数标识
    private final int BORROWNUMBER = 1;//借款人数标识

    private boolean ISINCREASECOMPARE = false;//是否添加了对比，默认没有添加
    private ImageView mPlatformCollectBtn, mIvPlatformLevelTag;//关注按钮,平台评级
    private CollectDbOpenHelper.CollectDbHelper mCollectDbHelper;//关注数据库帮助类
    /** 标的详情右侧饼状图标题数组 */
    private String[] mPieChartTittleArr = new String[]{"近三月标的期限", "近三月标的金额"};
    private View mNoDataView;
    private ObservableScrollView mPlatformDetailContainer;
    private TextView mBidDetailItem;

    private TextView mItemTitle;
    private TextView mPlatformDataItem;
    private TextView mCompanyInfo;
    private ImageView mDetailChartNoData;
    private ImageView mDetailNoDataBarChartOne;
    private ImageView mDetailNoDataBarChartTwo;
    private LinearLayout mPointContainer;
    private TextView mTitleBack;
    private String mLastPageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_platform_detail);

        Bundle bundle = getIntent().getExtras();
        mLastPageName = bundle.getString(Const.KEY_INTENT_JUMP_FROM_DATA);
        mPlatformName = bundle.getString("p_name");
        mPlatformPid = bundle.getString("pid");
        if (StringUtil.isEmptyOrNull(mPlatformName) || StringUtil.isEmptyOrNull(mPlatformPid)) {
            UIUtil.showToastShort("数据获取有误");
            finish();
            return;
        }

        //实例化数据库帮助类
        mCollectDbHelper = CollectDbOpenHelper.CollectDbHelper.getInstance(mActivity);

        initView();
        initData();
    }

    private void initView() {
        mIvPlatformLevelTag = (ImageView) findViewById(R.id.iv_platform_detail_logo_level);
        mPlatformBackgroud = (TextView) findViewById(R.id.activity_detail_platform_background);
        //title
        findViewById(R.id.layout_title_back).setOnClickListener(this);
        mTitleBack = (TextView) findViewById(R.id.tv_title_back);
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText(mPlatformName);
//        TextView titleRight = (TextView) findViewById(R.id.tv_title_txt_right);
//        titleRight.setText("记一笔");
//        titleRight.setVisibility(View.VISIBLE);
//        titleRight.setOnClickListener(this);

        mNoDataView = findViewById(R.id.layout_no_data_view);
        mPlatformDetailContainer = (ObservableScrollView) findViewById(R.id.sv_platformdetail);
        ViewUtil.showContentLayout(Const.LAYOUT_LOADING, mNoDataView, mPlatformDetailContainer);

        //content
        mLayoutPlatformVideoLogo = (RelativeLayout) findViewById(R.id.rl_detail_video_icon);
        findViewById(R.id.fl_detail_icon).setOnClickListener(this);
        mPlatformCollectBtn = (ImageView) findViewById(R.id.detail_collect);
        mPlatformUrl = (TextView) findViewById(R.id.activity_account_company_website_address);
        mPlatformLogo = (ImageView) findViewById(R.id.activity_account_company_image);
        mPlatformFundMoney = (TextView) findViewById(R.id.activity_account_registered_fund_money);
        mPlatformFinancingState = (TextView) findViewById(R.id.activity_account_financing_status);
        mPlatformOnlineDate = (TextView) findViewById(R.id.activity_account_online_time);
        mPlatformLocation = (TextView) findViewById(R.id.activity_account_company_address);

        mBidProfit = (TextView) findViewById(R.id.activity_account_avg_profit);
        mBidAutoInvest = (TextView) findViewById(R.id.activity_account_auto_invest);
        mBidAoc = (TextView) findViewById(R.id.activity_account_aoc);
        mBidTrustFunds = (TextView) findViewById(R.id.activity_account_trust_funds);
        mBidGoi = (TextView) findViewById(R.id.activity_account_goi);
        mBidMode = (TextView) findViewById(R.id.activity_account_g_mode);
        mBidGuarantor = (TextView) findViewById(R.id.activity_account_guarantor);
        mBidPieChatTitle = (TextView) findViewById(R.id.detail_right_title);
        mBidPieChatViewPager = (ViewPager) findViewById(R.id.detail_page_pie_chart);
        mPointContainer = (LinearLayout) findViewById(R.id.fl_point_container);
        mPlatformDataChartChengJiaoLiang = (CombinedChart) findViewById(R.id.activity_account_combinedchart);
        mPlatformDataChartTouziRenshu = (BarChart) findViewById(R.id.activity_account_barchart1);
        mPlatformDataChartJieKuanRenshu = (BarChart) findViewById(R.id.activity_account_barchart2);

        combinedWeak = new WeakReference<CombinedChart>(mPlatformDataChartChengJiaoLiang);
        barWeak1 = new WeakReference<BarChart>(mPlatformDataChartJieKuanRenshu);
        barWeak2 = new WeakReference<BarChart>(mPlatformDataChartTouziRenshu);
        weakViewPager = new WeakReference<ViewPager>(mBidPieChatViewPager);

        mDetailChartNoData = (ImageView) findViewById(R.id.platform_detail_no_data_combinedchart);
        mDetailNoDataBarChartOne = (ImageView) findViewById(R.id.platform_detail_no_data_barchart_one);
        mDetailNoDataBarChartTwo = (ImageView) findViewById(R.id.platform_detail_no_data_barchart_two);

        mCompanyInfoName = (TextView) findViewById(R.id.activity_account_c_name);
        mCompanyInfoLegalPerson = (TextView) findViewById(R.id.activity_account_legal_person);
        mCompanyInfoICP = (TextView) findViewById(R.id.activity_account_p_icp);
        mCompanyInfoTEL = (TextView) findViewById(R.id.activity_account_c_tel);
        mCompanyInfoFAX = (TextView) findViewById(R.id.activity_account_c_fax);
        mCompanyInfo400 = (TextView) findViewById(R.id.activity_account_c_tel400);
        mCompanyInfoEmail = (TextView) findViewById(R.id.activity_account_c_email);
        mCompanyInfoBusinessType = (TextView) findViewById(R.id.activity_account_business_type);
        mCompanyInfoAddress = (TextView) findViewById(R.id.activity_account_c_address);
        mCompanyInfoImgListView = (MeasuredListView) findViewById(R.id.lv_c_img_container);
        mCompanyImgAdapter = new PlatformDetailCompanyImgAdapter(mActivity, mPlatformPid);
        mCompanyInfoImgListView.setAdapter(mCompanyImgAdapter);

        mLayoutBtnAddInCompare = (LinearLayout) findViewById(R.id.ll_increase_compare);
        mAddInCompareTxt = (TextView) findViewById(R.id.tv_increase_compare);
        mLayoutBtnStartCompare = (LinearLayout) findViewById(R.id.ll_start_compare);
        mCompareNumber = (TextView) findViewById(R.id.tv_compare_xiaoyuanquan);

        //条目置顶
        mBidDetailItem = (TextView) findViewById(R.id.rl_bid_detail_item);
        mItemTitle = (TextView) findViewById(R.id.tv_item_title);
        mPlatformDataItem = (TextView) findViewById(R.id.tv_platform_data_item);
        mCompanyInfo = (TextView) findViewById(R.id.tv_company_info);

        mPlatformDetailContainer.setScrollViewListener(this);

        mPlatformUrl.setOnClickListener(this);
        mPlatformCollectBtn.setOnClickListener(this);
        mLayoutBtnAddInCompare.setOnClickListener(this);
        mLayoutBtnStartCompare.setOnClickListener(this);

    }


    int[] mBidDetailLocation = new int[2];
    int[] mPlatformDataLocation = new int[2];
    int[] mCompanyInfoLocation = new int[2];

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {

        //获取指定控件的位置
        mBidDetailItem.getLocationOnScreen(mBidDetailLocation);//标详情的位置
        mPlatformDataItem.getLocationOnScreen(mPlatformDataLocation);//平台数据的位置
        mCompanyInfo.getLocationOnScreen(mCompanyInfoLocation);//公司信息的位置

        if (y > mBidDetailLocation[1] + UIUtil.dip2px(30)) {
            mItemTitle.setVisibility(View.VISIBLE);
            mItemTitle.setText("标的详情");

        } else {
            mItemTitle.setVisibility(View.GONE);
        }

        if (y > mPlatformDataLocation[1] + UIUtil.dip2px(250)) {
            mItemTitle.setVisibility(View.VISIBLE);
            mItemTitle.setText("平台数据");
        }

        if (y > mCompanyInfoLocation[1] + UIUtil.dip2px(1020)) {
            mItemTitle.setVisibility(View.VISIBLE);
            mItemTitle.setText("公司信息");
        }

    }

    private void initData() {
        //title
        mTitleBack.setText(TextUtils.isEmpty(mLastPageName) ? "网贷平台" : mLastPageName);
        //判断该详情页面是否已经加入对比
        if (PlatformCompareManager.getList().size() > 0) {
            mCompareNumber.setVisibility(View.VISIBLE);
            mCompareNumber.setText(String.valueOf(PlatformCompareManager.getList().size()));
            if (PlatformCompareManager.getList().contains(mPlatformPid)) {
                mAddInCompareTxt.setText("移除对比");
                ISINCREASECOMPARE = true;
            }
        }
        //初始设置是否关注，取本地数据库数据
        boolean collected = mCollectDbHelper.queryByPid(mPlatformPid);
        mPlatformCollectBtn.setImageResource(collected ? R.drawable.focus_blue_star : R.drawable.focus_empty_star);
        //根据pid请求详情数据
        String url = StringUtil.format(URLConstant.PLATFORM_DETAIL, mPlatformPid);
        NetHelper.get(url, new IRequestCallback<String>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onFailure(Exception e) {
                ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mNoDataView, mPlatformDetailContainer);
                UIUtil.showToastShort("获取数据失败");
            }

            @Override
            public void onSuccess(final String jsonStr) {
                if (GlobalUtils.isTokenMatach(jsonStr)) {
                    mPlatformBean = JSON.parseObject(jsonStr, PlatformDetailBean.class);
                    if (mPlatformBean == null) {
                        ViewUtil.showContentLayout(Const.LAYOUT_EMPTY, mNoDataView, mPlatformDetailContainer);
                        return;
                    }
                    ViewUtil.showContentLayout(Const.LAYOUT_DATA, mNoDataView, mPlatformDetailContainer);
                    refreshViewData();
                }
            }
        });
    }

    /** 显示数据 此时数据必然存在 即mPlatformBean不为空 */
    private void refreshViewData() {
        if (this.isFinishing()) {
            return;
        }
        //是否显示有视频可看
        if (mPlatformBean.video_url != null) {
            mLayoutPlatformVideoLogo.setVisibility(mPlatformBean.video_url.size() > 0 ? View.VISIBLE : View.GONE);
        }

        //设置等级tag
        setPlatformLevel(mIvPlatformLevelTag);
        mBidPieChatTitle.setText("< 近三月标的期限 >");
        //公司LOGO
        ImgLoadUtil.loadLogo(mPlatformPid, mPlatformBean.p_logo, mPlatformLogo, 0);
        //公司注册资金
        mPlatformFundMoney.setText(mPlatformBean.c_capital);
        //公司融资情况
        String finaceState = StringUtil.format(R.string.platform_detail_company_info_financing_state, StringUtil.isEmptyOrNull(mPlatformBean.avg_profit) ? "--" : mPlatformBean.f_situation);
        mPlatformFinancingState.setText(finaceState);
        //上线时间
        mPlatformOnlineDate.setText(mPlatformBean.launch_time);
        /** 平台背景 */
        String background = mPlatformBean.getBackground();
        mPlatformBackgroud.setText(!StringUtil.isEmptyOrNull(background) ? background : "--");
        //公司网址
        mPlatformUrl.setText(mPlatformBean.p_site);
        mPlatformUrl.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        mPlatformUrl.getPaint().setAntiAlias(true);//抗锯齿
        //公司地址
        mPlatformLocation.setText(mPlatformBean.location);
        //平均收益
        String avg_profit = StringUtil.format(R.string.platform_detail_bid_average_revenue, StringUtil.isEmptyOrNull(mPlatformBean.avg_profit) ? "--" : mPlatformBean.avg_profit);
        Util.setSpannable(mBidProfit, avg_profit, 5, Color.BLACK);
        //自动投标
        String auto_invest = StringUtil.format(R.string.platform_detail_bid_auto_bid, StringUtil.isEmptyOrNull(mPlatformBean.auto_invest) ? "--" : mPlatformBean.auto_invest);
        Util.setSpannable(mBidAutoInvest, auto_invest, 5, Color.BLACK);
        //债权转让
        String aoc = StringUtil.format(R.string.platform_detail_bid_debenture_transfer, StringUtil.isEmptyOrNull(mPlatformBean.aoc) ? "--" : mPlatformBean.aoc);
        Util.setSpannable(mBidAoc, aoc, 5, Color.BLACK);
        //资金托管
        String trust_funds = StringUtil.format(R.string.platform_detail_bid_funds_trusteeship, StringUtil.isEmptyOrNull(mPlatformBean.trust_funds) ? "--" : mPlatformBean.trust_funds);
        Util.setSpannable(mBidTrustFunds, trust_funds, 5, Color.BLACK);
        //投标保障
        String goi = StringUtil.format(R.string.platform_detail_bid_guarantee, StringUtil.isEmptyOrNull(mPlatformBean.goi) ? "--" : mPlatformBean.goi);
        Util.setSpannable(mBidGoi, goi, 5, Color.BLACK);
        //保障模式
        String g_mode = StringUtil.format(R.string.platform_detail_bid_security_mode, StringUtil.isEmptyOrNull(mPlatformBean.g_mode) ? "--" : mPlatformBean.g_mode);
        Util.setSpannable(mBidMode, g_mode, 5, Color.BLACK);
        //担保机构
        String guarantor = StringUtil.format(R.string.platform_detail_bid_guarantor, StringUtil.isEmptyOrNull(mPlatformBean.guarantor) ? "--" : mPlatformBean.guarantor);
        Util.setSpannable(mBidGuarantor, guarantor, 5, Color.BLACK);
        //公司名称
        String c_name = StringUtil.format(R.string.platform_detail_company_info_name, StringUtil.isEmptyOrNull(mPlatformBean.c_name) ? "--" : mPlatformBean.c_name);
        mCompanyInfoName.setText(c_name);
        //企业法人
        String legal_person = StringUtil.format(R.string.platform_detail_company_info_legal_person, StringUtil.isEmptyOrNull(mPlatformBean.legal_person) ? "--" : mPlatformBean.legal_person);
        mCompanyInfoLegalPerson.setText(legal_person);
        //ICP备案号
        String p_icp = StringUtil.format(R.string.platform_detail_company_info_icp, StringUtil.isEmptyOrNull(mPlatformBean.p_icp) ? "--" : mPlatformBean.p_icp);
        mCompanyInfoICP.setText(p_icp);
        //公司电话
        String c_tel = StringUtil.format(R.string.platform_detail_company_info_phone, StringUtil.isEmptyOrNull(mPlatformBean.c_tel) ? "--" : mPlatformBean.c_tel);
        mCompanyInfoTEL.setText(c_tel);
        //公司传真
        String c_fax = StringUtil.format(R.string.platform_detail_company_info_fax, StringUtil.isEmptyOrNull(mPlatformBean.c_fax) ? "--" : mPlatformBean.c_fax);
        mCompanyInfoFAX.setText(c_fax);
        //400热线
        String c_tel400 = StringUtil.format(R.string.platform_detail_company_info_400, StringUtil.isEmptyOrNull(mPlatformBean.c_tel400) ? "--" : mPlatformBean.c_tel400);
        mCompanyInfo400.setText(c_tel400);
        //服务邮箱
        String c_email = StringUtil.format(R.string.platform_detail_company_info_email, StringUtil.isEmptyOrNull(mPlatformBean.c_email) ? "--" : mPlatformBean.c_email);
        mCompanyInfoEmail.setText(c_email);
        //业务类型
        String business_type = StringUtil.format(R.string.platform_detail_company_info_bussiness_type, StringUtil.isEmptyOrNull(mPlatformBean.business_type) ? "--" : mPlatformBean.business_type);
        mCompanyInfoBusinessType.setText(business_type);
        //公司地址
        String c_address = StringUtil.format(R.string.platform_detail_company_info_address, StringUtil.isEmptyOrNull(mPlatformBean.c_address) ? "--" : mPlatformBean.c_address);
        mCompanyInfoAddress.setText(c_address);
        //公司图片
        ((TextView) findViewById(R.id.company_img)).setText(StringUtil.format(R.string.platform_detail_company_info_images, "无图片"));
        if (mPlatformBean.c_companyimg != null && mPlatformBean.c_companyimg.size() > 0) {
            List<String> imgDataList = new ArrayList<>();
            for (int i = 0; i < mPlatformBean.c_companyimg.size(); i++) {
                String imgUrl = mPlatformBean.c_companyimg.get(i);
                if (!StringUtil.isEmptyOrNull(imgUrl)) {
                    imgDataList.add(imgUrl);
                }
            }
            if (imgDataList.size() > 0) {
                ((TextView) findViewById(R.id.company_img)).setText(StringUtil.format(R.string.platform_detail_company_info_images, ""));
                mCompanyImgAdapter.loadData(imgDataList);
            }
        }

        //生成饼状图
        mPieChartList = new ArrayList<>();
        weakList = new WeakReference<List>(mPieChartList);
        mPieChartList.add(new PlatformDetailBidLimitPieChart(mActivity));
        mPieChartList.add(new PlatformDetailTotalMoneyPieChart(mActivity));

        if (mPieChartPagerAdapter == null) {
            mPieChartPagerAdapter = new PieChartPagerAdapter(mPieChartList);
            weakAdapter = new WeakReference<PieChartPagerAdapter>(mPieChartPagerAdapter);
        }
        mBidPieChatViewPager.setAdapter(mPieChartPagerAdapter);
        mBidPieChatViewPager.addOnPageChangeListener(new MyChangeListener());
        ((PlatformDetailBidLimitPieChart) (mPieChartList.get(0))).loadData(mPlatformBean);

        //动态添加ViewPager里的点
        for (int i = 0; i < 2; i++) {
            View v_point = new View(mActivity);
            LogUtil.e("点的数量为：" + mPieChartList.size());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(UIUtil.dip2px(5), UIUtil.dip2px(5));
            if (i == 0) {
                v_point.setBackgroundResource(R.drawable.blue_point_piechart);
            } else {
                lp.leftMargin = UIUtil.dip2px(5);//设置点之间的间距
                v_point.setBackgroundResource(R.drawable.gray_point);
            }
            v_point.setLayoutParams(lp);
            mPointContainer.addView(v_point);
        }

        //成交量与利率混合图表
        if (combinedWeak.get() != null)
            ChartUtils.createCombinedBarChart(PlatformDetailActivity.this, mPlatformName, mPlatformPid, mPlatformDataChartChengJiaoLiang, mPlatformBean);
        if (mPlatformBean.y_cjl.size() == 0) {
            mDetailChartNoData.setVisibility(View.VISIBLE);
        }
        //投资人以及借款人柱状图
        if (barWeak2.get() != null)
            ChartUtils.createBarChart(PlatformDetailActivity.this, mPlatformName, mPlatformPid, mPlatformDataChartTouziRenshu, mPlatformBean, INVESTNUMBER);
        if (mPlatformBean.y_tzrs.size() == 0) {
            mDetailNoDataBarChartOne.setVisibility(View.VISIBLE);
        }
        if (barWeak1.get() != null)
            ChartUtils.createBarChart(PlatformDetailActivity.this, mPlatformName, mPlatformPid, mPlatformDataChartJieKuanRenshu, mPlatformBean, BORROWNUMBER);
        if (mPlatformBean.y_jkrs.size() == 0) {
            mDetailNoDataBarChartTwo.setVisibility(View.VISIBLE);
        }

    }


    /** 平台设置评级 */
    private void setPlatformLevel(ImageView iv) {
        if (mPlatformBean == null) {
            return;
        }
        mIvPlatformLevelTag.setVisibility(View.VISIBLE);
        String level = mPlatformBean.getLevel();
        if ("A+".equals(level)) {
            iv.setImageResource(R.mipmap.a_up_level);
        } else if ("A".equals(level)) {
            iv.setImageResource(R.mipmap.a_level);
        } else if ("B+".equals(level)) {
            iv.setImageResource(R.mipmap.b_up_level);
        } else if ("B".equals(level)) {
            iv.setImageResource(R.mipmap.b_level);
        } else if ("C+".equals(level)) {
            iv.setImageResource(R.mipmap.c_up_level);
        } else if ("C".equals(level)) {
            iv.setImageResource(R.mipmap.c_level);
        } else if ("D".equals(level)) {
            iv.setImageResource(R.mipmap.d_level);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_title_back://返回
                finish();
                break;
            case R.id.activity_account_company_website_address://点击网址，跳转到该公司主页
                jump2ShowCompanyInfoByUrl();
                break;
            case R.id.fl_detail_icon://点击图标跳转值视频播放页面
                jump2ShowVideo();
                break;
            case R.id.detail_collect://点击关注平台
                changeCollectState();
                break;
            case R.id.ll_increase_compare://点击加入对比按钮
                addIn2Compare();
                UmengEventHelper.onEvent(UmengEventID.NetPlatformDetailAddInCompareButton);
                break;
            case R.id.ll_start_compare://点击开始对比按钮，跳转到平台对比页面
                startCompare();
                UmengEventHelper.onEvent(UmengEventID.NetPlatformDetailStartCompareButton);
                break;
        }
    }


    /** 通过url查看公司详情 */
    private void jump2ShowCompanyInfoByUrl() {
        if (mPlatformBean == null || StringUtil.isEmptyOrNull(mPlatformBean.p_site)) {
            return;
        }
        Intent intent = new Intent(mActivity, CommonWebPageActivity.class);

        intent.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA, mPlatformBean.p_site);
        intent.putExtra(Const.KEY_INTENT_JUMP_FROM_DATA, mPlatformBean.p_name);
        startActivity(intent);
    }

    /** 查看视频 */
    private void jump2ShowVideo() {
        if (mPlatformBean.video_url == null || mPlatformBean.video_url.size() == 0) {
            return;
        }
        if (Util.legalLogin() == null) {
            startActivity(LoginActivity.class);
            return;
        }
        Intent intent = new Intent(mActivity, PlatformVideoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("p_name", mPlatformName);
        bundle.putParcelableArrayList("video_url", (ArrayList<? extends Parcelable>) mPlatformBean.video_url);
//        bundle.putSerializable("video_url", (Serializable) mPlatformBean.video_url);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /** 切换收藏 */
    private void changeCollectState() {
        if (Util.legalLogin() == null) {
            startActivity(LoginActivity.class);
            return;
        }
        final boolean collected = mCollectDbHelper.queryByPid(mPlatformPid);
        String urlType = collected ? URLConstant.PLATFORM_CANCEL_CELLECT : URLConstant.PLATFORM_CELLECT;
        String requestTag = collected ? Const.NET_TAG_REQUEST_GET_DETAIL_CANCEL_COLLECT : Const.NET_TAG_REQUEST_GET_DETAIL_COLLECT;
        String url = StringUtil.format(urlType, GlobalUtils.token, mPlatformPid);
        NetHelper.get(url, new IRequestCallback<String>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onFailure(Exception e) {
                UIUtil.showToastShort("操作失败");
            }

            @Override
            public void onSuccess(String jsonStr) {
                if (GlobalUtils.isTokenMatach(jsonStr)) {
                    if (collected) {
                        mPlatformCollectBtn.setImageResource(R.drawable.focus_empty_star);
                        UIUtil.showToastShort("取消关注成功");
                        mCollectDbHelper.deleteByPid(mPlatformPid);
                    } else {
                        mPlatformCollectBtn.setImageResource(R.drawable.focus_blue_star);
                        UIUtil.showToastShort("关注成功");
                        mCollectDbHelper.insert(mPlatformPid);
                    }
                } else {
                    this.onFailure(null);
                }
            }
        });
    }

    /** 更新收藏状态 */
    @Subscribe
    public void onIsCollectEvent(IsCollectEvent event) {
        if (null != event) {
            boolean collected = mCollectDbHelper.queryByPid(mPlatformPid);
            if (!collected) {
                mPlatformCollectBtn.setImageResource(R.drawable.focus_empty_star);
            } else {
                mPlatformCollectBtn.setImageResource(R.drawable.focus_blue_star);
            }
        }
    }

    /** 加入比较 */
    private void addIn2Compare() {
        if (!ISINCREASECOMPARE) {
            //没有加入对比
            if (PlatformCompareManager.getList().size() < 5) {

                LoginBean loginBean = Util.legalLogin();
                if (loginBean == null) {//未登录
                    startActivity(LoginActivity.class);
                    return;
                }

                PlatformCompareManager.getList().add(mPlatformPid);
                LogUtil.e(TAG, PlatformCompareManager.getList().size());
                mAddInCompareTxt.setText("移除对比");
                mCompareNumber.setVisibility(View.VISIBLE);
                int size = PlatformCompareManager.getList().size();
                mCompareNumber.setText(String.valueOf(size));
                ISINCREASECOMPARE = true;
            } else {
                UIUtil.showToastShort("对比的平台数量不能超过5个");
            }
        } else {
            //已经加入对比
            PlatformCompareManager.getList().remove(mPlatformPid);
            mAddInCompareTxt.setText("加入对比");
            ISINCREASECOMPARE = false;
            if (PlatformCompareManager.getList().size() == 0) {
                mCompareNumber.setVisibility(View.INVISIBLE);
                return;
            }
            mCompareNumber.setText(String.valueOf(PlatformCompareManager.getList().size()));
        }
    }

    /** 该是比较 */
    private void startCompare() {
        LoginBean loginBean = Util.legalLogin();
        if (loginBean == null) {//未登录
            startActivity(LoginActivity.class);
        } else {
            if (PlatformCompareManager.getList().size() == 0) {
                UIUtil.showToastShort("还没有选择对比平台");
                return;
            }
            Intent intent = new Intent(this,PlatformCompareActivity.class);
            intent.putExtra(Const.KEY_INTENT_JUMP_FROM_DATA, mPlatformName);
            startActivity(intent);
            mAddInCompareTxt.setText("加入对比");
            mCompareNumber.setVisibility(View.INVISIBLE);
            ISINCREASECOMPARE = false;
        }
    }

    private class MyChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position == 0) {
                ((PlatformDetailBidLimitPieChart) (mPieChartList.get(0))).loadData(mPlatformBean);
                mBidPieChatTitle.setText("< 近三月标的期限 >");
            } else {
                ((PlatformDetailTotalMoneyPieChart) (mPieChartList.get(position))).loadData(mPlatformBean);
                mBidPieChatTitle.setText("< 近三月标的金额 >");
            }

            //动态改变点的颜色
            for (int i = 0; i < mPointContainer.getChildCount(); i++) {
                if (i == position % 4) {
                    //当前位置的点蓝色
                    mPointContainer.getChildAt(i).setBackgroundResource(R.drawable.blue_point_piechart);
                } else {
                    //灰色
                    mPointContainer.getChildAt(i).setBackgroundResource(R.drawable.gray_point);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompanyInfoImgListView = null;
        mCompanyImgAdapter = null;
        mPlatformDataChartJieKuanRenshu = null;
        mPlatformDataChartTouziRenshu = null;
        mPlatformDataChartChengJiaoLiang = null;
        if (mPieChartList != null) {
            for (View view : mPieChartList) {
                view.invalidate();
                view = null;
            }
            mPieChartList.clear();
        }
        mBidPieChatViewPager = null;
        mPieChartPagerAdapter = null;
        combinedWeak.clear();
        barWeak1.clear();
        barWeak2.clear();
        XtzApp.getApplication().finishActivity(PlatformDetailActivity.class);
        System.gc();
    }
}
