package com.ypwl.xiaotouzi.ui.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.SparseArrayCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.bean.KeepAccountsBean;
import com.ypwl.xiaotouzi.bean.LoginBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.common.ServerStatus;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.event.AccountsKeptEvent;
import com.ypwl.xiaotouzi.event.BindAccountEvent;
import com.ypwl.xiaotouzi.event.JiZhangChooseEvent;
import com.ypwl.xiaotouzi.interf.IRequestCallback;
import com.ypwl.xiaotouzi.manager.EventHelper;
import com.ypwl.xiaotouzi.manager.JsonHelper;
import com.ypwl.xiaotouzi.manager.db.JiZhangHistoryDbOpenHelper;
import com.ypwl.xiaotouzi.manager.net.NetHelper;
import com.ypwl.xiaotouzi.utils.DateTimeUtil;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.PopuViewUtil;
import com.ypwl.xiaotouzi.utils.SoftInputUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.Util;
import com.ypwl.xiaotouzi.utils.ViewUtil;
import com.ypwl.xiaotouzi.view.RadioTab;
import com.ypwl.xiaotouzi.view.dialog.lib.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 项目名: XTZ
 * 包名: com.ypwl.xiaotouzi.ui.activity
 * 类名: KeepAccountsActivity
 * 作者: 罗霄
 * 创建时间: 2016/3/24 16:28
 * 描述: 新版记账页面
 * svn版本: $REV$
 * 更新人: $AUTHOR$
 * 更新时间: $2016/3/24$
 * 更新描述:
 */
public class KeepAccountsActivity extends BaseActivity implements View.OnClickListener, RadioTab.OnTabCheckedForGroupListener {

    private LinearLayout mLlBack;
    private LinearLayout mLlBonusPrincipal;
    private LinearLayout mLlObjectName;
    private LinearLayout mLlInvestmentAmount;
    private LinearLayout mLlPeriod;
    private LinearLayout mLlMoneyRate;
    private LinearLayout mLlBidReward;
    private LinearLayout mLlExtraBonus;
    private LinearLayout mLlInterestCosts;
    private LinearLayout mLlEnchashmentCosts;
    private LinearLayout mLlChoosePlatform;
    private LinearLayout mLlChooseValueDate;
    private LinearLayout mLlChooseRepaymentMethod;
    private TextView mTvTitleContent;
    private TextView mTvTitleRight;
    private TextView mTvShowChoosePlatform;
    private TextView mTvShowChooseValueDate;
    private TextView mTvTotalPrincipal;
    private TextView mTvShowChooseRepaymentMethod;
    private TextView mTvNoteCounts;
    private TextView mTvInvestmentAmountUnit;
    private TextView mTvBonusPrincipalUnit;
    private TextView mTvBonusPrincipalReckon;
    private TextView mTvPeriodUnit;
    private TextView mTvMoneyRateUnit;
    private TextView mTvBidRewardUnit;
    private TextView mTvExtraBonusUnit;
    private TextView mTvInterestCostsUnit;
    private TextView mTvShowChooseValueDateRange;
    private TextView mTvEnchashmentCostsUnit;
    private EditText mEtObjectName;
    private EditText mEtInvestmentAmount;
    private EditText mEtBonusPrincipal;
    private EditText mEtPeriod;
    private EditText mEtMoneyRate;
    private EditText mEtNote;
    private EditText mEtBidReward;
    private EditText mEtExtraBonus;
    private EditText mEtInterestCosts;
    private EditText mEtEnchashmentCosts;
    private RadioTab mRtBonusPrincipal;
    private RadioTab mRtPeriod;
    private RadioTab mRtMoneyRate;
    private RadioTab mRtBidReward;
    private RadioTab mRtExtraBonus;
    private Button mBtKeepAgain;
    private Button mBtKeep;
    private ImageView mIvDueAutomaticRepaymen;
    private KProgressHUD mDialogLoading;


    /**
     * 奖励本金的RadioTab显示：% == 0
     * 元 == 1
     */
    private int mRtShowCountBp;
    /**
     * RadioTab显示容器：%、元
     */
    private static final String[] mRtBonusPrincipalArrs = new String[]{UIUtil.getString(R.string.keep_accounts_btn_per_cent), UIUtil.getString(R.string.keep_accounts_btn_yuan)};

    /**
     * 周期的RadioTab显示：月 == 0
     * 天 == 1
     */
    private int mRtShowCountPd;
    /**
     * RadioTab显示容器：月、天
     */
    private static final String[] mRtPeriodArrs = new String[]{UIUtil.getString(R.string.keep_accounts_btn_month), UIUtil.getString(R.string.keep_accounts_btn_day)};

    /**
     * 利率的RadioTab显示：年利率 == 0
     * 日利率 == 1
     */
//    private int mRtShowCountMr;
    /**
     * RadioTab显示容器：年利率、日利率
     */
    private static final String[] mRtMoneyRateArrs = new String[]{UIUtil.getString(R.string.keep_accounts_btn_annual_rate), UIUtil.getString(R.string.keep_accounts_btn_day_rate)};

    /**
     * 投标奖励的RadioTab显示：投标回 == 0
     * 每期回 == 1
     * 到期回 == 2
     */
//    private int mRtShowCountBr;
    /**
     * RadioTab显示容器：投标回、每期回、到期回
     */
    private static final String[] mRtBidRewardArrs = new String[]{UIUtil.getString(R.string.keep_accounts_btn_bide_back), UIUtil.getString(R.string.keep_accounts_btn_terminally_back), UIUtil.getString(R.string.keep_accounts_btn_maturity_back)};

    /**
     * 额外奖励的RadioTab显示：投标回 == 0
     * 到期回 == 1
     */
//    private int mRtShowCountEb;
    /**
     * RadioTab显示容器：投标回、到期回
     */
    private static final String[] mRtExtraBonusArrs = new String[]{UIUtil.getString(R.string.keep_accounts_btn_bide_back), UIUtil.getString(R.string.keep_accounts_btn_maturity_back)};

    private final int charMaxNum = 50;// 可输入的最大字符长度
    //监听edittext输入超出的提示
    private boolean isShowToast;

    /** 日期选择器 */
    private DatePickerDialog mDatePickerDialog;

    /**
     * 还款方式的 key-value 集合
     * key : return_type
     * value : return_name
     */
    private LinkedHashMap<String, String> mPopuKeyValueRepaymentMethod = new LinkedHashMap<>();

    /** 默认还款方式 */
    private final String defReturnType = "3";
    private final String defReturnName = "到期还本息";

    /** json bean */
    private KeepAccountsBean keep_accounts_bean;

    /** 传递过来的记账唯一标识 */
    private String mAid;

    /** 金额限制 -- 百万 */
    private final int RESTRICT_MONEY_MILLION = 10000000;
    /** 金额限制 -- 十万 */
    private final int RESTRICT_MONEY_ONE_HUNDRED_THOUSAND = 1000000;
    /** 百分比数限制 -- 100 */
    private final int RESTRICT_PERCENTAGE = 100;

    /** RadioTab 对应 position */
    private final int RADIOTAB_TAG_FIRST = 0;
    private final int RADIOTAB_TAG_SECOND = 1;
//    private final int RADIOTAB_TAG_THIRD = 2;

    private EditText mHasFocusView;
    private PopuViewUtil popuViewUtil;
    private BindOnFocusChangeListener mBindOnFocusChangeListener;
    private String mLastPageName;
    private TextView mTvTitleLeft;
    private View mNoDataView;
    private LinearLayout mLlContainer;
    private ImageView mIvYearDayNum;
    private View mLineYearDayNum;
    private RelativeLayout mRlYearDayNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_keep_accounts);

        mAid = getIntent().getStringExtra(Const.KEY_INTENT_JUMP_BASE_DATA);
        mLastPageName = getIntent().getStringExtra(Const.KEY_INTENT_JUMP_FROM_DATA);

        initView();

        initData();
    }

    /**
     * 显示还款方式文字内容
     */
    private void fillDataForRepaymentMethod() {
        if (mPopuKeyValueRepaymentMethod.size() != 0 && null != keep_accounts_bean && !TextUtils.isEmpty(keep_accounts_bean.getReturn_type())) {
            for (Map.Entry<String, String> entry : mPopuKeyValueRepaymentMethod.entrySet()) {
                if (entry.getKey().equalsIgnoreCase(keep_accounts_bean.getReturn_type())) {
                    mTvShowChooseRepaymentMethod.setText(entry.getValue());
                    ViewUtil.showContentLayout(Const.LAYOUT_DATA, mNoDataView, mLlContainer);
                    break;
                }
            }
        }
    }

    /**
     * 获取还款方式列表
     */
    private void getRepaymentMethod() {
        String url = String.format(URLConstant.KEEP_ACCOUNTS_REPAYMENT_METHOD, GlobalUtils.token);

        NetHelper.get(url, new IRequestCallback<String>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onFailure(Exception e) {
                UIUtil.showToastShort("获取还款方式失败");
            }

            @Override
            public void onSuccess(String jsonStr) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    int status = jsonObject.optInt(Const.JSON_KEY_status, -1);
                    String ret_msg = jsonObject.optString(Const.JSON_KEY_ret_msg);
                    JSONArray list = jsonObject.optJSONArray("list");

                    if (null != list) {
                        switch (status) {
                            case ServerStatus.SERVER_STATUS_OK:

                                mPopuKeyValueRepaymentMethod.clear();

                                for (int i = 0; i < list.length(); i++) {
                                    // return_type	还款方式类型
                                    // return_name	还款方式的名称
                                    JSONObject elementJo = list.optJSONObject(i);
                                    String return_type = elementJo.optString("return_type", "");
                                    String return_name = elementJo.optString("return_name", "");
                                    mPopuKeyValueRepaymentMethod.put(return_type, return_name);
                                }

                                fillDataForRepaymentMethod();

                                break;
                            default:
                                UIUtil.showToastShort("" + ret_msg);
                                break;
                        }
                    }

                } catch (JSONException e) {
                    this.onFailure(e);
                }
            }
        });
    }

    /**
     * 根据传递过来的aid获取信息
     */
    private void getDataFromAid() {
        String url = String.format(URLConstant.KEEP_ACCOUNTS_GET_DATA_FROM_AID, GlobalUtils.token, mAid);

        NetHelper.get(url, new IRequestCallback<String>() {
            @Override
            public void onStart() {
                ViewUtil.showContentLayout(Const.LAYOUT_LOADING, mNoDataView, mLlContainer);
            }

            @Override
            public void onFailure(Exception e) {
                UIUtil.showToastShort("编辑信息获取失败");
                ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mNoDataView, mLlContainer, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getDataFromAid();
                        getRepaymentMethod();
                    }
                });
            }

            @Override
            public void onSuccess(String jsonStr) {
                try {
                    keep_accounts_bean = JsonHelper.parseObject(jsonStr, KeepAccountsBean.class);
                    keep_accounts_bean.setCustom_pname("");
                    keep_accounts_bean.setAid(mAid);
                    if (ServerStatus.SERVER_STATUS_OK == keep_accounts_bean.getStatus()) {
                        if (!TextUtils.isEmpty(mTvShowChooseRepaymentMethod.getText().toString().trim())) {
                            ViewUtil.showContentLayout(Const.LAYOUT_DATA, mNoDataView, mLlContainer);
                        } else {
                            ViewUtil.showContentLayout(Const.LAYOUT_LOADING, mNoDataView, mLlContainer);
                        }
                        fillData();
                    } else {
                        UIUtil.showToastShort("编辑信息获取失败");
                    }
                } catch (NumberFormatException e) {
                    UIUtil.showToastShort("编辑信息异常");
                }
            }
        });
    }

    /**
     * 根据获取的bean，填充信息
     */
    private void fillData() {

        mTvShowChoosePlatform.setText(keep_accounts_bean.getP_name());
        mEtInvestmentAmount.setText(keep_accounts_bean.getMoney());
        /** 当有自定义objName时，优先设置 */
        mEtObjectName.setText(keep_accounts_bean.getProject_name());
        mTvShowChooseValueDate.setText(DateTimeUtil.formatDateTime(Long.parseLong(keep_accounts_bean.getStarttime() + "000"), DateTimeUtil.DF_YYYY_MM_DD).replace("-", "/"));

        fillDataForRepaymentMethod();

        //                    onChecked(mRtBonusPrincipal, null, null, 0);
        if (!TextUtils.isEmpty(keep_accounts_bean.getAward_capital())) {
            mEtBonusPrincipal.setText(keep_accounts_bean.getAward_capital());
            mRtBonusPrincipal.setSelect(Integer.parseInt(keep_accounts_bean.getAward_capital_type()) - 1);
        }

        //                    onChecked(mRtPeriod, null, null, 0);
        if (!TextUtils.isEmpty(keep_accounts_bean.getTime_limit())) {
            mRtPeriod.setSelect(Integer.parseInt(keep_accounts_bean.getTime_type()) - 1);
            mEtPeriod.setText(keep_accounts_bean.getTime_limit());
        }
        //                    onChecked(mRtMoneyRate, null, null, 0);
        if (!TextUtils.isEmpty(keep_accounts_bean.getRate())) {
            mEtMoneyRate.setText(keep_accounts_bean.getRate());
            mRtMoneyRate.setSelect(Integer.parseInt(keep_accounts_bean.getRate_type()) - 1);
        }

        if (!TextUtils.isEmpty(keep_accounts_bean.getStandard_year())) {
            setValueYearDayNum(null);
        }

        //                    onChecked(mRtBidReward, null, null, 0);
        if (!TextUtils.isEmpty(keep_accounts_bean.getTender_award())) {
            mEtBidReward.setText(keep_accounts_bean.getTender_award());
            mRtBidReward.setSelect(Integer.parseInt(keep_accounts_bean.getTender_award_type()) - 1);
        }
        //                    onChecked(mRtExtraBonus, null, null, 0);
        if (!TextUtils.isEmpty(keep_accounts_bean.getExtra_award())) {
            mEtExtraBonus.setText(keep_accounts_bean.getExtra_award());
            mRtExtraBonus.setSelect(Integer.parseInt(keep_accounts_bean.getExtra_award_type()) - 1);
        }

        mEtInterestCosts.setText(TextUtils.isEmpty(keep_accounts_bean.getCost()) ? "" : keep_accounts_bean.getCost());
        mEtEnchashmentCosts.setText(TextUtils.isEmpty(keep_accounts_bean.getCash_cost()) ? "" : keep_accounts_bean.getCash_cost());

        if (!TextUtils.isEmpty(keep_accounts_bean.getAuto_payment())) {
            mIvDueAutomaticRepaymen.setImageResource(keep_accounts_bean.getAuto_payment().equals("1") ? R.mipmap.btn_046_select : R.mipmap.btn_046);
        }

        mEtNote.setText(TextUtils.isEmpty(keep_accounts_bean.getRemark()) ? "" : keep_accounts_bean.getRemark());
    }

    private void initRadioTab(RadioTab view, String[] arrs, int titleTextSize, int titleMinWidth, boolean notPaddingTopAndBottom,
                              int paddingLeftAndRight) {
        view.setTabTitleTextColorBackgroundResource(R.color.base_color_radiotab_text_selector)
                .setTabLeftBackgroundResource(R.drawable.base_radiotab_bg_left_selector)
                .setTabMiddleBackgroundResource(R.drawable.base_radiotab_bg_middle_selector)
                .setTabRightBackgroundResource(R.drawable.base_radiotab_bg_right_selector);
        view.setTabTitleTextSize(titleTextSize);
        view.setTabTitleMinWidth(titleMinWidth);
        view.addTabs(Arrays.asList(arrs), notPaddingTopAndBottom, paddingLeftAndRight);
        view.setOnTabCheckedForGroupListener(this);
    }

    private void initData() {

        if (!TextUtils.isEmpty(mLastPageName)) {
            mTvTitleLeft.setText(mLastPageName);
        }


        initRadioTab(mRtBonusPrincipal, mRtBonusPrincipalArrs, 12, UIUtil.dip2px(29), true, UIUtil.dip2px(8));

        initRadioTab(mRtPeriod, mRtPeriodArrs, 12, UIUtil.dip2px(29), true, UIUtil.dip2px(8));

        initRadioTab(mRtMoneyRate, mRtMoneyRateArrs, 12, UIUtil.dip2px(49), true, UIUtil.dip2px(8));

        initRadioTab(mRtBidReward, mRtBidRewardArrs, 12, UIUtil.dip2px(49), true, UIUtil.dip2px(5));

        initRadioTab(mRtExtraBonus, mRtExtraBonusArrs, 12, UIUtil.dip2px(49), true, UIUtil.dip2px(8));

        mTvTitleContent.setText(UIUtil.getString(R.string.keep_accounts_title_content));
        mTvTitleRight.setText(UIUtil.getString(R.string.keep_accounts_title_right));
        mTvTitleRight.setVisibility(View.VISIBLE);

        getRepaymentMethod();

        if (!TextUtils.isEmpty(mAid)) {

            getDataFromAid();

        } else {

            ViewUtil.showContentLayout(Const.LAYOUT_DATA, mNoDataView, mLlContainer);

            keep_accounts_bean = new KeepAccountsBean();
            keep_accounts_bean.setAid("0");
            keep_accounts_bean.setIs_thirty("1");
            keep_accounts_bean.setIs_year("1");

            keep_accounts_bean.setAuto_payment("0");
            keep_accounts_bean.setStandard_year("0");

            String replace = DateTimeUtil.formatDateTime(System.currentTimeMillis() + DateTimeUtil.day, DateTimeUtil.DF_YYYY_MM_DD).replace("-", "/");
            mTvShowChooseValueDate.setText(replace);

            onChecked(mRtBonusPrincipal, null, null, 0);
            onChecked(mRtBidReward, null, null, 0);
            onChecked(mRtExtraBonus, null, null, 0);
            onChecked(mRtMoneyRate, null, null, 0);
            onChecked(mRtPeriod, null, null, 0);

            mTvShowChooseRepaymentMethod.setText(defReturnName);
            keep_accounts_bean.setReturn_type(defReturnType);

        }
    }


    private void initView() {

        mNoDataView = findView(R.id.layout_no_data_view);
        mLlContainer = findView(R.id.activity_keep_accounts_ll_container);

        mBindOnFocusChangeListener = new BindOnFocusChangeListener();

        mDialogLoading = KProgressHUD.create(mActivity);

        mLlBack = findView(R.id.layout_title_back);
        mTvTitleLeft = findView(R.id.tv_title_back);
        mTvTitleContent = findView(R.id.tv_title);
        mTvTitleRight = findView(R.id.tv_title_txt_right);

        /** 平台选择 */
        mLlChoosePlatform = findView(R.id.activity_keep_accounts_ll_choose_platform);
        mTvShowChoosePlatform = findView(R.id.activity_keep_accounts_tv_show_choose_platform);

        /** 起息时间 */
        mLlChooseValueDate = findView(R.id.activity_keep_accounts_ll_choose_value_date);
        mTvShowChooseValueDate = findView(R.id.activity_keep_accounts_tv_show_choose_value_date);
        mTvShowChooseValueDateRange = findView(R.id.activity_keep_accounts_tv_show_choose_value_date_range);

        /** 标的名称 */
        mLlObjectName = findView(R.id.activity_keep_accounts_ll_object_name);
        mEtObjectName = findView(R.id.activity_keep_accounts_et_object_name);

        /** 投资金额 */
        mLlInvestmentAmount = findView(R.id.activity_keep_accounts_ll_investment_amount);
        mEtInvestmentAmount = findView(R.id.activity_keep_accounts_et_investment_amount);
        mTvInvestmentAmountUnit = findView(R.id.activity_keep_accounts_tv_investment_amount_unit);

        /** 奖励本金 */
        mLlBonusPrincipal = findView(R.id.activity_keep_accounts_ll_bonus_principal);
        mEtBonusPrincipal = findView(R.id.activity_keep_accounts_et_bonus_principal);
        mTvBonusPrincipalUnit = findView(R.id.activity_keep_accounts_tv_bonus_principal_unit);
        mTvBonusPrincipalReckon = findView(R.id.activity_keep_accounts_tv_bonus_principal_reckon);
        mRtBonusPrincipal = findView(R.id.activity_keep_accounts_rt_bonus_principal);

        /** 总本金*/
        mTvTotalPrincipal = findView(R.id.activity_keep_accounts_tv_total_principal);

        /** 还款方式 */
        mLlChooseRepaymentMethod = findView(R.id.activity_keep_accounts_ll_choose_repayment_method);
        mTvShowChooseRepaymentMethod = findView(R.id.activity_keep_accounts_tv_show_choose_repayment_method);

        /** 周期 */
        mLlPeriod = findView(R.id.activity_keep_accounts_ll_period);
        mEtPeriod = findView(R.id.activity_keep_accounts_et_period);
        mTvPeriodUnit = findView(R.id.activity_keep_accounts_tv_period_unit);
        mRtPeriod = findView(R.id.activity_keep_accounts_rt_period);

        /** 1年按365天算 */
        mLineYearDayNum = findView(R.id.activity_keep_accounts_line_year_day_num);
        mRlYearDayNum = findView(R.id.activity_keep_accounts_rl_year_day_num);
        mIvYearDayNum = findView(R.id.activity_keep_accounts_iv_year_day_num);

        /** 利率 */
        mLlMoneyRate = findView(R.id.activity_keep_accounts_ll_money_rate);
        mEtMoneyRate = findView(R.id.activity_keep_accounts_et_money_rate);
        mTvMoneyRateUnit = findView(R.id.activity_keep_accounts_tv_money_rate_unit);
        mRtMoneyRate = findView(R.id.activity_keep_accounts_rt_money_rate);

        /** 投标奖励 */
        mLlBidReward = findView(R.id.activity_keep_accounts_ll_bid_reward);
        mEtBidReward = findView(R.id.activity_keep_accounts_et_bid_reward);
        mTvBidRewardUnit = findView(R.id.activity_keep_accounts_tv_bid_reward_unit);
        mRtBidReward = findView(R.id.activity_keep_accounts_rt_bid_reward);

        /** 额外奖励 */
        mLlExtraBonus = findView(R.id.activity_keep_accounts_ll_extra_bonus);
        mEtExtraBonus = findView(R.id.activity_keep_accounts_et_extra_bonus);
        mTvExtraBonusUnit = findView(R.id.activity_keep_accounts_tv_extra_bonus_unit);
        mRtExtraBonus = findView(R.id.activity_keep_accounts_rt_extra_bonus);

        /** 利息管理费 */
        mLlInterestCosts = findView(R.id.activity_keep_accounts_ll_interest_costs);
        mEtInterestCosts = findView(R.id.activity_keep_accounts_et_interest_costs);
        mTvInterestCostsUnit = findView(R.id.activity_keep_accounts_tv_interest_costs_unit);

        /** 取现手续费 */
        mLlEnchashmentCosts = findView(R.id.activity_keep_accounts_ll_enchashment_costs);
        mEtEnchashmentCosts = findView(R.id.activity_keep_accounts_et_enchashment_costs);
        mTvEnchashmentCostsUnit = findView(R.id.activity_keep_accounts_tv_enchashment_costs_unit);

        /** 到期自动还款 */
        mIvDueAutomaticRepaymen = findView(R.id.activity_keep_accounts_iv_due_automatic_repayment);

        /** 备注*/
        mEtNote = findView(R.id.activity_keep_accounts_et_note);
        mTvNoteCounts = findView(R.id.activity_keep_accounts_tv_note_counts);

        /** 保存再记 && 保存 */
        mBtKeepAgain = findView(R.id.activity_keep_accounts_bt_keep_again);
        mBtKeep = findView(R.id.activity_keep_accounts_bt_keep);

        adaptation();

        initListener();
    }

    /**
     * 适配魅族的 EditText 问题
     */
    private void adaptation() {
        if (!"Meizu".equals(android.os.Build.MANUFACTURER)) {
            mEtInvestmentAmount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            mEtBonusPrincipal.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            mEtPeriod.setInputType(InputType.TYPE_CLASS_NUMBER);
            mEtMoneyRate.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            mEtBidReward.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            mEtExtraBonus.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            mEtInterestCosts.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            mEtEnchashmentCosts.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }
    }

    /**
     * 回滚到上一次字符输入
     *
     * @param mEtView 输入控件
     * @param s       字符串类容
     * @param start   上一次输入的光标起始位置
     */
    private void revertAndCursorSelect(EditText mEtView, CharSequence s, int start) {
        String mShowString = s.subSequence(0, start).toString() + s.subSequence(start + 1, s.length()).toString();
        mEtView.setText(mShowString);
        mEtView.setSelection(start);
    }

    /**
     * 浮点数是否超出小数范围
     * @param dValue 浮点数
     * @param length 需要判断的小数部分位数
     * @return 是否超出
     */
//    private boolean isOverrunFromDouble(double dValue, int length){
//        boolean isOverrun = false;
//        String mValueString = String.valueOf(dValue);
//        if (isOverrunFromDouble(mValueString, length)){
//            isOverrun = true;
//        }
//        return isOverrun;
//    }

    /**
     * 浮点数是否超出小数范围
     *
     * @param dValueString 浮点数对应的字符串
     * @param length       需要判断的小数部分位数
     * @return 是否超出
     */
    private boolean isOverrunFromDouble(String dValueString, int length) {
        boolean isOverrun = false;
        if (dValueString.contains(".") && dValueString.split("\\.").length == 2 && dValueString.split("\\.")[1].length() > length) {
            isOverrun = true;
        }
        return isOverrun;
    }

    /**
     * 无意义的开头 : 以零开头的非小数（整数），
     *
     * @param s 内容
     * @return 是否无意义
     */
    private boolean isNonsensicalStart(CharSequence s) {
        boolean isOverrun = false;
        String mValueString = s.toString().trim();
        if (mValueString.startsWith("00") || mValueString.startsWith(".")) {
            isOverrun = true;
        }
        return isOverrun;
    }

    /** 关联标的名称 */
    private void setObjNameWithChooseValueDate(String str){
        String match = "^[0-9]{4}[-][0-9]{2}[-][0-9]{2}$";
        String objName = mEtObjectName.getText().toString().trim();
        if (TextUtils.isEmpty(objName) || objName.matches(match)){
            mEtObjectName.setText(str);
            mEtObjectName.setSelection(str.length());
        }
    }

    private void initListener() {

        mLlBack.setOnClickListener(this);
        mLlChoosePlatform.setOnClickListener(this);
        mLlChooseValueDate.setOnClickListener(this);
        mLlChooseRepaymentMethod.setOnClickListener(this);
        mLlBonusPrincipal.setOnClickListener(this);
        mLlObjectName.setOnClickListener(this);
        mLlInvestmentAmount.setOnClickListener(this);
        mLlPeriod.setOnClickListener(this);
        mLlMoneyRate.setOnClickListener(this);
        mLlBidReward.setOnClickListener(this);
        mLlExtraBonus.setOnClickListener(this);
        mLlInterestCosts.setOnClickListener(this);
        mLlEnchashmentCosts.setOnClickListener(this);
        mTvTitleRight.setOnClickListener(this);
        mIvDueAutomaticRepaymen.setOnClickListener(this);
        mIvYearDayNum.setOnClickListener(this);
        mBtKeepAgain.setOnClickListener(this);
        mBtKeep.setOnClickListener(this);

        mEtObjectName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                keep_accounts_bean.setProject_name(s.toString().trim());
            }
        });

        mTvShowChooseValueDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mTvShowChooseValueDateRange.setText("");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!TextUtils.isEmpty(s)) {
                    String replace = s.toString().replace("/", "-");

                    setObjNameWithChooseValueDate(replace);

                    Date date = DateTimeUtil.parseDate(replace, DateTimeUtil.DF_YYYY_MM_DD);

                    Date currentDate = DateTimeUtil.parseDate(DateTimeUtil.formatDateTime(DateTimeUtil.getCurrentDate(), DateTimeUtil.DF_YYYY_MM_DD), DateTimeUtil.DF_YYYY_MM_DD);

                    long mDvalue = date.getTime() - currentDate.getTime();

                    if (mDvalue >= 0) {
                        if (mDvalue < DateTimeUtil.day) {
                            mTvShowChooseValueDateRange.setText("今天");
                        } else if (mDvalue < 2 * DateTimeUtil.day) {
                            mTvShowChooseValueDateRange.setText("明天");
                        } else if (mDvalue < 3 * DateTimeUtil.day) {
                            mTvShowChooseValueDateRange.setText("后天");
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                keep_accounts_bean.setStarttime(Long.toString(DateTimeUtil.parseDate(s.toString().trim().replace("/", "-"), DateTimeUtil.DF_YYYY_MM_DD).getTime() / 1000));
            }
        });

        mBindOnFocusChangeListener.OnFocusAndBindUnitView(mEtInvestmentAmount, mTvInvestmentAmountUnit);
        mEtInvestmentAmount.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    try {
                        double mValueDouble = Double.parseDouble(s.toString().trim());
                        if (mValueDouble >= RESTRICT_MONEY_MILLION || isOverrunFromDouble(s.toString().trim(), 2) || isNonsensicalStart(s)) {
                            revertAndCursorSelect(mEtInvestmentAmount, s, start);
                        }else {
                            keep_accounts_bean.setMoney(s.toString().trim());
                        }
                    } catch (NumberFormatException e) {
                        revertAndCursorSelect(mEtInvestmentAmount, s, start);
                    }
                }
                reckonBp();
            }

            @Override
            public void afterTextChanged(Editable s) {
                mBindOnFocusChangeListener.onFocusChange(mEtInvestmentAmount, !TextUtils.isEmpty(s.toString().trim()));
            }
        });

        mBindOnFocusChangeListener.OnFocusAndBindUnitView(mEtBonusPrincipal, mTvBonusPrincipalUnit);
        mEtBonusPrincipal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!TextUtils.isEmpty(s)) {
                    try {
                        double mValueDouble = Double.parseDouble(s.toString().trim());

                        if ((mRtShowCountBp == RADIOTAB_TAG_FIRST && mValueDouble > RESTRICT_PERCENTAGE) || (mRtShowCountBp == RADIOTAB_TAG_SECOND && mValueDouble >= RESTRICT_MONEY_MILLION) || isOverrunFromDouble(s.toString().trim(), 2) || isNonsensicalStart(s)) {
                            revertAndCursorSelect(mEtBonusPrincipal, s, start);
                        }else {
                            keep_accounts_bean.setAward_capital(s.toString().trim());
                        }

                    } catch (NumberFormatException e) {
                        revertAndCursorSelect(mEtBonusPrincipal, s, start);
                    }
                }
                reckonBp();
            }

            @Override
            public void afterTextChanged(Editable s) {
                mBindOnFocusChangeListener.onFocusChange(mEtBonusPrincipal, !TextUtils.isEmpty(s.toString().trim()));
            }
        });

        mBindOnFocusChangeListener.OnFocusAndBindUnitView(mEtPeriod, mTvPeriodUnit);
        mEtPeriod.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isNonsensicalStart(s)) {
                    revertAndCursorSelect(mEtPeriod, s, start);
                }else {
                    keep_accounts_bean.setTime_limit(s.toString().trim());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                mBindOnFocusChangeListener.onFocusChange(mEtPeriod, !TextUtils.isEmpty(s.toString().trim()));
            }
        });

        mBindOnFocusChangeListener.OnFocusAndBindUnitView(mEtMoneyRate, mTvMoneyRateUnit);
        mEtMoneyRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    try {

                        double mValueDouble = Double.parseDouble(s.toString().trim());

                        if (mValueDouble > RESTRICT_PERCENTAGE || isOverrunFromDouble(s.toString().trim(), 2) || isNonsensicalStart(s)) {
                            revertAndCursorSelect(mEtMoneyRate, s, start);
                        }else {
                            keep_accounts_bean.setRate(s.toString().trim());
                        }

                    } catch (NumberFormatException e) {
                        revertAndCursorSelect(mEtMoneyRate, s, start);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                mBindOnFocusChangeListener.onFocusChange(mEtMoneyRate, !TextUtils.isEmpty(s.toString().trim()));
            }
        });

        mBindOnFocusChangeListener.OnFocusAndBindUnitView(mEtBidReward, mTvBidRewardUnit);
        mEtBidReward.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    try {
                        double mValueDouble = Double.parseDouble(s.toString().trim());

                        if (mValueDouble > RESTRICT_PERCENTAGE || isOverrunFromDouble(s.toString().trim(), 2) || isNonsensicalStart(s)) {
                            revertAndCursorSelect(mEtBidReward, s, start);
                        }else {
                            keep_accounts_bean.setTender_award(s.toString().trim());
                        }
                    } catch (NumberFormatException e) {
                        revertAndCursorSelect(mEtBidReward, s, start);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                mBindOnFocusChangeListener.onFocusChange(mEtBidReward, !TextUtils.isEmpty(s.toString().trim()));
            }
        });

        mBindOnFocusChangeListener.OnFocusAndBindUnitView(mEtExtraBonus, mTvExtraBonusUnit);
        mEtExtraBonus.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    try {
                        double mValueDouble = Double.parseDouble(s.toString().trim());

                        if (mValueDouble >= RESTRICT_MONEY_ONE_HUNDRED_THOUSAND || isOverrunFromDouble(s.toString().trim(), 2) || isNonsensicalStart(s)) {
                            revertAndCursorSelect(mEtExtraBonus, s, start);
                        }else {
                            keep_accounts_bean.setExtra_award(s.toString().trim());
                        }
                    } catch (NumberFormatException e) {
                        revertAndCursorSelect(mEtExtraBonus, s, start);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                mBindOnFocusChangeListener.onFocusChange(mEtExtraBonus, !TextUtils.isEmpty(s.toString().trim()));
            }
        });

        mBindOnFocusChangeListener.OnFocusAndBindUnitView(mEtInterestCosts, mTvInterestCostsUnit);
        mEtInterestCosts.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    try {
                        double mValueDouble = Double.parseDouble(s.toString().trim());

                        if (mValueDouble > RESTRICT_PERCENTAGE || isOverrunFromDouble(s.toString().trim(), 2) || isNonsensicalStart(s)) {
                            revertAndCursorSelect(mEtInterestCosts, s, start);
                        }else {
                            keep_accounts_bean.setCost(s.toString().trim());
                        }
                    } catch (NumberFormatException e) {
                        revertAndCursorSelect(mEtInterestCosts, s, start);
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                mBindOnFocusChangeListener.onFocusChange(mEtInterestCosts, !TextUtils.isEmpty(s.toString().trim()));
            }
        });

        mBindOnFocusChangeListener.OnFocusAndBindUnitView(mEtEnchashmentCosts, mTvEnchashmentCostsUnit);
        mEtEnchashmentCosts.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    try {
                        double mValueDouble = Double.parseDouble(s.toString().trim());

                        if (mValueDouble >= RESTRICT_MONEY_ONE_HUNDRED_THOUSAND || isOverrunFromDouble(s.toString().trim(), 2) || isNonsensicalStart(s)) {
                            revertAndCursorSelect(mEtEnchashmentCosts, s, start);
                        }else {
                            keep_accounts_bean.setCash_cost(s.toString().trim());
                        }
                    } catch (NumberFormatException e) {
                        revertAndCursorSelect(mEtEnchashmentCosts, s, start);
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                mBindOnFocusChangeListener.onFocusChange(mEtEnchashmentCosts, !TextUtils.isEmpty(s.toString().trim()));
            }
        });
        mEtNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > charMaxNum) {
                    if (!isShowToast) {
                        isShowToast = true;

                        UIUtil.showToastShort("你输入的字数已经超过了限制！");

                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {

                            public void run() {
                                isShowToast = false;
                            }

                        }, 2000);
                    }
                    revertAndCursorSelect(mEtNote, s, start);
                } else {
                    String mValueString = s.length() + "/50";
                    mTvNoteCounts.setText(mValueString);
                    keep_accounts_bean.setRemark(s.toString().trim());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * 显示周期的单位
     */
    private void togglePeriodDateUnit() {
        if (mRtShowCountPd == RADIOTAB_TAG_FIRST) {
            mTvPeriodUnit.setText("个月");
        } else {
            mTvPeriodUnit.setText("天");
        }
    }

    /** 给总本金添加单位 */
    private void addUnitWithTotalPrincipa() {
        String mValueTotalPrincipal = mTvTotalPrincipal.getText().toString().trim();
        if (!TextUtils.isEmpty(mValueTotalPrincipal) && !mValueTotalPrincipal.endsWith(UIUtil.getString(R.string.keep_accounts_btn_yuan))) {
            mValueTotalPrincipal += UIUtil.getString(R.string.keep_accounts_btn_yuan);
            mTvTotalPrincipal.setText(mValueTotalPrincipal);
        }
    }

    /**
     * 计算奖励本金和总本金
     */
    private void reckonBp() {

        mTvBonusPrincipalUnit.setText(mRtBonusPrincipalArrs[mRtShowCountBp]);

        String mTextIa = mEtInvestmentAmount.getText().toString().trim();
        String mTextBp = mEtBonusPrincipal.getText().toString().trim();

        if (mRtShowCountBp == 0) {

            if (!TextUtils.isEmpty(mTextIa) && !TextUtils.isEmpty(mTextBp)) {
                double money = Double.parseDouble(mTextIa) / 100.0 * Double.parseDouble(mTextBp);
                money = Util.interceptDouble(money);
                String mValueString = "合" + money + "元";
                mTvBonusPrincipalReckon.setText(mValueString);
                mTvTotalPrincipal.setText(String.valueOf(Double.parseDouble(mTextIa) + money));
            } else {
                mTvBonusPrincipalReckon.setText("");
                mTvTotalPrincipal.setText("");
                if (!TextUtils.isEmpty(mTextIa)) {
                    mTvTotalPrincipal.setText(String.valueOf(Double.parseDouble(mTextIa)));
                    addUnitWithTotalPrincipa();
                }
            }
        } else {
            mTvBonusPrincipalReckon.setText("");
            double total = 0.0;
            mTvTotalPrincipal.setText("");
            if (!TextUtils.isEmpty(mTextIa)) {
                total += Double.parseDouble(mTextIa);
                mTvTotalPrincipal.setText(String.valueOf(total));
            }
            if (!TextUtils.isEmpty(mTextBp)) {
                total += Double.parseDouble(mTextBp);
                mTvTotalPrincipal.setText(String.valueOf(total));
            }
        }

        addUnitWithTotalPrincipa();
    }

    @Override
    public void onClick(View v) {

        clearFocusFromEtViewAndHideInput();

        switch (v.getId()) {
            case R.id.layout_title_back:
                finish();
                break;
            case R.id.tv_title_txt_right:
                LoginBean loginBean = Util.legalLogin();
                if (loginBean == null) {
                    UIUtil.showToastShort("请先登录");
                }
                startActivity(loginBean != null ? AutoTallyPlatformActivity.class : LoginActivity.class);
                break;
            case R.id.activity_keep_accounts_ll_choose_platform:
                //设置为零，以免和对比页面的相关操作冲突
                Const.PLATFORM_CHOOSE_REQUEST_FROM = 0;
                startActivityForResult(new Intent(this, PlatformChooseActivity.class), 1);
                break;
            case R.id.activity_keep_accounts_ll_choose_value_date:
                if (null == mDatePickerDialog) {
                    Calendar mCalendar = Calendar.getInstance();
                    // 默认显示选择第二天
                    Long lStarttime = System.currentTimeMillis();
                    mCalendar.setTimeInMillis(lStarttime + DateTimeUtil.day);
                    mDatePickerDialog = new DatePickerDialog(mActivity, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                            String mValueString = DateTimeUtil.formatDateTime(DateTimeUtil.parseDate(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth, DateTimeUtil.DF_YYYY_MM_DD), DateTimeUtil.DF_YYYY_MM_DD_SPRIT);

                            mTvShowChooseValueDate.setText(mValueString);

                        }
                    }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
                }
                mDatePickerDialog.show();
                break;
            case R.id.activity_keep_accounts_ll_choose_repayment_method:

                if (null == mPopuKeyValueRepaymentMethod || mPopuKeyValueRepaymentMethod.size() == 0) {
                    UIUtil.showToastShort("获取还款方式失败,重试中……");
                    getRepaymentMethod();
                    break;
                }

                if (null == popuViewUtil) {

                    LinkedList<String> mDataPopu = new LinkedList<>();
                    mDataPopu.addAll(mPopuKeyValueRepaymentMethod.values());

                    popuViewUtil = new PopuViewUtil(mActivity, mDataPopu, new PopuViewUtil.OnClickCountsListener() {
                        @Override
                        public void onClick(int position, String str) {

                            mTvShowChooseRepaymentMethod.setText(str);

                            for (Map.Entry<String, String> entry : mPopuKeyValueRepaymentMethod.entrySet()) {
                                if (entry.getValue().equalsIgnoreCase(str)) {
                                    keep_accounts_bean.setReturn_type(entry.getKey());
                                    break;
                                }
                            }
                            if (isConflictFromPeriod()) {
                                UIUtil.showToastShort("当前还款方式，还款周期需为3个月的倍数");
                            }
                        }

                    });
                }
                popuViewUtil.show();
                break;
            case R.id.activity_keep_accounts_iv_due_automatic_repayment:
                keep_accounts_bean.setAuto_payment(keep_accounts_bean.getAuto_payment().equals("1") ? "0" : "1");
                mIvDueAutomaticRepaymen.setImageResource(keep_accounts_bean.getAuto_payment().equals("1") ? R.mipmap.btn_046_select : R.mipmap.btn_046);
                break;
            case R.id.activity_keep_accounts_iv_year_day_num:
                setValueYearDayNum(keep_accounts_bean.getStandard_year().equals("1") ? "0" : "1");
                break;
            case R.id.activity_keep_accounts_bt_keep_again:

                setClickable(false);
                keepAccounts(true);

                break;
            case R.id.activity_keep_accounts_bt_keep:

                setClickable(false);
                keepAccounts(false);

                break;
            case R.id.activity_keep_accounts_ll_object_name:

                requestFocusAndShowInput(mEtObjectName);

                break;
            case R.id.activity_keep_accounts_ll_investment_amount:

                requestFocusAndShowInput(mEtInvestmentAmount);

                break;
            case R.id.activity_keep_accounts_ll_bonus_principal:

                requestFocusAndShowInput(mEtBonusPrincipal);

                break;
            case R.id.activity_keep_accounts_ll_period:

                requestFocusAndShowInput(mEtPeriod);

                break;
            case R.id.activity_keep_accounts_ll_money_rate:

                requestFocusAndShowInput(mEtMoneyRate);

                break;
            case R.id.activity_keep_accounts_ll_bid_reward:

                requestFocusAndShowInput(mEtBidReward);

                break;
            case R.id.activity_keep_accounts_ll_extra_bonus:

                requestFocusAndShowInput(mEtExtraBonus);

                break;
            case R.id.activity_keep_accounts_ll_interest_costs:

                requestFocusAndShowInput(mEtInterestCosts);

                break;
            case R.id.activity_keep_accounts_ll_enchashment_costs:

                requestFocusAndShowInput(mEtEnchashmentCosts);

                break;

        }
    }

    /**
     * 操作 非Edittext 控件时，清除 mHasFocusView 记录的焦点，并收起软键盘
     */
    private void clearFocusFromEtViewAndHideInput() {
        if (null != mHasFocusView) {
            mHasFocusView.clearFocus();
        }
        SoftInputUtil.hideSoftInput(mActivity);
    }

    /**
     * 获取焦点并弹出软键盘
     *
     * @param mEtView 目标edittext
     */
    private void requestFocusAndShowInput(EditText mEtView) {
        mEtView.requestFocus();
        String mValueString = mEtView.getText().toString().trim();
        if (!TextUtils.isEmpty(mValueString)) {
            mEtView.setSelection(mValueString.length());
        }
        SoftInputUtil.showSoftInput(mActivity);
    }

    /**
     * 设置提交按钮的可点状态
     *
     * @param enabled 是否可点击
     */
    private void setClickable(boolean enabled) {
        mBtKeep.setClickable(enabled);
        mBtKeepAgain.setClickable(enabled);
    }

    /**
     * 提交记账
     */
    private void keepAccounts(final boolean isAgain) {
        if (null == keep_accounts_bean) {
            //获取数据失败
            UIUtil.showToastShort("编辑信息获取失败");
            setClickable(true);
        } else if (TextUtils.isEmpty(keep_accounts_bean.getPid()) || ("0".equals(keep_accounts_bean.getPid()) && TextUtils.isEmpty(keep_accounts_bean.getCustom_pname()))) {
            UIUtil.showToastShort("请选择平台");
            setClickable(true);
        } else if (TextUtils.isEmpty(keep_accounts_bean.getStarttime())) {
            UIUtil.showToastShort("请选择起息时间");
            setClickable(true);
        } else if (TextUtils.isEmpty(keep_accounts_bean.getProject_name())) {
            UIUtil.showToastShort("请填写标的名称");
            setClickable(true);
        } else if (TextUtils.isEmpty(keep_accounts_bean.getMoney())) {
            UIUtil.showToastShort("请填写投资金额");
            setClickable(true);
        } else if (TextUtils.isEmpty(keep_accounts_bean.getReturn_type())) {
            UIUtil.showToastShort("请选择还款方式");
            setClickable(true);
        } else if (TextUtils.isEmpty(keep_accounts_bean.getTime_limit())) {
            UIUtil.showToastShort("请填写周期");
            setClickable(true);
        } else if (TextUtils.isEmpty(keep_accounts_bean.getRate())) {
            UIUtil.showToastShort("请填写利率");
            setClickable(true);
        } else if (isConflictFromPeriod()) {
            UIUtil.showToastShort("当前还款方式，还款周期需为3个月的倍数");
            setClickable(true);
        } else {
            String url = String.format(URLConstant.KEEP_ACCOUNTS, GlobalUtils.token, keep_accounts_bean.getAid(),
                    keep_accounts_bean.getPid(), keep_accounts_bean.getProject_name(), keep_accounts_bean.getStarttime(), keep_accounts_bean.getMoney(),
                    keep_accounts_bean.getReturn_type(), keep_accounts_bean.getTime_limit(), keep_accounts_bean.getTime_type(), keep_accounts_bean.getRate(),
                    keep_accounts_bean.getRate_type(), null == keep_accounts_bean.getTender_award() ? "" : keep_accounts_bean.getTender_award(), keep_accounts_bean.getTender_award_type(), null == keep_accounts_bean.getExtra_award() ? "" : keep_accounts_bean.getExtra_award(),
                    keep_accounts_bean.getExtra_award_type(), null == keep_accounts_bean.getCost() ? "" : keep_accounts_bean.getCost(), null == keep_accounts_bean.getRemark() ? "" : keep_accounts_bean.getRemark(), keep_accounts_bean.getCustom_pname(),
                    keep_accounts_bean.getIs_year(), keep_accounts_bean.getIs_thirty(), null == keep_accounts_bean.getAward_capital() ? "" : keep_accounts_bean.getAward_capital(), keep_accounts_bean.getAward_capital_type(),
                    null == keep_accounts_bean.getCash_cost() ? "" : keep_accounts_bean.getCash_cost(), keep_accounts_bean.getAuto_payment(), keep_accounts_bean.getStandard_year());

            NetHelper.get(url, new IRequestCallback<String>() {
                @Override
                public void onStart() {
                    mDialogLoading.show();
                }

                @Override
                public void onFailure(Exception e) {
                    mDialogLoading.dismiss();
                    setClickable(true);
                    UIUtil.showToastShort("保存失败！");
                }

                @Override
                public void onSuccess(String jsonStr) {
                    mDialogLoading.dismiss();
                    setClickable(true);
                    try {
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        int status = jsonObject.optInt(Const.JSON_KEY_status, -1);
//                        String aid = jsonObject.optString("aid");
                        String ret_msg = jsonObject.optString("ret_msg");
                        String custom_pid = jsonObject.optString("custom_pid");

                        switch (status) {
                            case ServerStatus.SERVER_STATUS_OK:
                                //发送保存成功的事件，通知其他有有关联的页面更新数据
                                EventHelper.post(new AccountsKeptEvent());
                                if (!TextUtils.isEmpty(custom_pid)){
                                    JiZhangHistoryDbOpenHelper.HistoryDbHelper instance = JiZhangHistoryDbOpenHelper.HistoryDbHelper.getInstance(UIUtil.getContext());
                                    instance.insert(custom_pid, keep_accounts_bean.getCustom_pname(), System.currentTimeMillis() + "");
                                }

                                Intent intent = new Intent();
                                intent.putExtra(Const.KEY_INTENT_JUMP_FROM_DATA, TextUtils.isEmpty(mLastPageName) ? "" : mLastPageName);
                                if (isAgain) {
                                    intent.setClass(mActivity, KeepAccountsActivity.class);
                                    startActivity(intent);
                                } else if (TextUtils.isEmpty(mAid)) {
                                    intent.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA, jsonObject.optString("aid"));
                                    intent.setClass(mActivity, LlcBidDetailActivity.class);
                                    startActivity(intent);
                                }
                                finish();

                                break;
                            default:
                                UIUtil.showToastShort("" + ret_msg);
                                break;
                        }
                    } catch (JSONException e) {
                        this.onFailure(e);
                    }
                }
            });
        }

    }

    /**
     * 根据选择的还款方式，判断其是否与周期存在冲突：有关季度的还款方式;
     * <p/>
     * false : 没冲突
     * true ： 有冲突
     */
    private boolean isConflictFromPeriod() {
        boolean defBl = false;

        String mRepaymentMethod = mTvShowChooseRepaymentMethod.getText().toString().trim();
        String mPeriod = mEtPeriod.getText().toString().trim();

        if (!TextUtils.isEmpty(mPeriod) && !TextUtils.isEmpty(mRepaymentMethod) && mRepaymentMethod.contains("季")) {
            if (mRtShowCountPd == 1 || Integer.parseInt(mPeriod) % 3 != 0) {
                defBl = true;
            }

        }
        return defBl;
    }

    @Override
    public void onChecked(View viewgroup, View view, CharSequence checkedTitle, int checkedIndex) {
        switch (viewgroup.getId()) {
            case R.id.activity_keep_accounts_rt_bonus_principal:
                mRtShowCountBp = checkedIndex;
                keep_accounts_bean.setAward_capital_type(mRtShowCountBp + 1 + "");

                reckonBp();

                break;
            case R.id.activity_keep_accounts_rt_period:
                mRtShowCountPd = checkedIndex;
                keep_accounts_bean.setTime_type(mRtShowCountPd + 1 + "");

                //月份模式下，月数最大十级，即2位数；天模式下，天数最大千级，即4位数
                mEtPeriod.setFilters(new InputFilter[]{new InputFilter.LengthFilter(0 == checkedIndex ? 2 : 4)});

                togglePeriodDateUnit();

                checkShouldYearDayNumVisibility();

                break;
            case R.id.activity_keep_accounts_rt_money_rate:
//                mRtShowCountMr = checkedIndex;
                keep_accounts_bean.setRate_type(checkedIndex + 1 + "");

                checkShouldYearDayNumVisibility();

                break;
            case R.id.activity_keep_accounts_rt_bid_reward:
//                mRtShowCountBr = checkedIndex;
                keep_accounts_bean.setTender_award_type(checkedIndex + 1 + "");
                break;
            case R.id.activity_keep_accounts_rt_extra_bonus:
//                mRtShowCountEb = checkedIndex;
                keep_accounts_bean.setExtra_award_type(checkedIndex + 1 + "");
                break;
        }
    }

    /** 检查是否显示360折算栏 */
    private void checkShouldYearDayNumVisibility(){
        setYearDayNumVisibility("1".equalsIgnoreCase(keep_accounts_bean.getRate_type()) && "2".equalsIgnoreCase(keep_accounts_bean.getTime_type()) ? View.VISIBLE : View.GONE);
    }

    /** 当选择年利率时，显示此栏；否则隐藏 */
    private void setYearDayNumVisibility(int visibility) {
        mLineYearDayNum.setVisibility(visibility);
        mRlYearDayNum.setVisibility(visibility);

        setValueYearDayNum(View.GONE == visibility ? "0" : null);
    }

    /**
     * 设置360天按钮的选择状态，并改变 bean 的数据
     *
     * @param value 当 value 值 isEmpty 时，不改变数据值，只做状态的按钮显示
     */
    private void setValueYearDayNum(String value) {
        if (!TextUtils.isEmpty(value) && ("1".equalsIgnoreCase(value) || "0".equalsIgnoreCase(value))) {
            keep_accounts_bean.setStandard_year(value);
        }
        mIvYearDayNum.setImageResource(keep_accounts_bean.getStandard_year().equalsIgnoreCase("1") ? R.mipmap.btn_046_select : R.mipmap.btn_046);
    }

    /**
     * 平台选择页面选择平台
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {//如果直接按返回键，则data中没有数据，所以为空
            Bundle bundle = data.getExtras();
            String invest_platform = bundle.getString("company_name");
            String invest_pid = bundle.getString("company_pid");
//            is_auto = bundle.getString("is_auto");
            mTvShowChoosePlatform.setText(invest_platform);

            keep_accounts_bean.setPid(invest_pid);
            keep_accounts_bean.setCustom_pname("0".equals(invest_pid) ? invest_platform : "");
        }
    }

    /**
     * 自定义edittext焦点监听，关联相应的单位显示判断
     */
    private class BindOnFocusChangeListener implements View.OnFocusChangeListener {

        /** 把单位view与各自的EditText关联存储 */
        private SparseArrayCompat<View> mArray = new SparseArrayCompat<>();

        public void OnFocusAndBindUnitView(EditText mEtView, View mUnitView) {
            mEtView.setTag(mEtView.getHint());
            mArray.put(mEtView.getId(), mUnitView);
            mEtView.setOnFocusChangeListener(this);
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (v instanceof EditText) {
                if (hasFocus) {
                    mHasFocusView = (EditText) v;
                    ((EditText) v).setHint(" ");

                    /** 添加自定义OnFocusChangeListener后，需要主动弹出软键盘 */
                    SoftInputUtil.showSoftInput(v);

                    mArray.get(v.getId()).setVisibility(View.VISIBLE);
                } else {
                    mHasFocusView = null;

                    String mStr = ((EditText) v).getText().toString().trim();
                    if ("0".equalsIgnoreCase(mStr) || "0.".equalsIgnoreCase(mStr)) {
                        ((EditText) v).setText("");
                        mStr = "";
                    }

                    if (TextUtils.isEmpty(mStr)) {
                        ((EditText) v).setHint((CharSequence) v.getTag());
                        mArray.get(v.getId()).setVisibility(View.INVISIBLE);
                    }
                }
            }
        }
    }

    /** 我的关注页面选择平台 */
    @Subscribe
    public void onJiZhangChooseEvent(JiZhangChooseEvent event) {
        if (event != null && !isFinishing() && event.hasChoose) {
            String invest_platform = event.company_name;
            String invest_pid = event.company_pid;
//                        is_auto = event.is_auto;
            mTvShowChoosePlatform.setText(invest_platform);

            keep_accounts_bean.setPid(invest_pid);
            keep_accounts_bean.setCustom_pname("0".equals(invest_pid) ? invest_platform : "");
        }
    }

    @Override
    protected boolean enableDispatchTouchEventOnSoftKeyboard() {
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();

        clearFocusFromEtViewAndHideInput();
    }

    /**绑定页面没有退出，自动记账绑定成功后退出*/
    @Subscribe
    public void onBindAccountEvent(BindAccountEvent event){
        if(event!=null && !isFinishing() && !event.isFinish()){
            finish();//若绑定页面没有关闭，关闭页面
        }
    }
}
