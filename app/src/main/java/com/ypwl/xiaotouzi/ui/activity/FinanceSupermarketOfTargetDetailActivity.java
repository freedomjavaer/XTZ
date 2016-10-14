package com.ypwl.xiaotouzi.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.bean.LoginBean;
import com.ypwl.xiaotouzi.bean.TargetDetailBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.common.ServerStatus;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.common.UmengEventID;
import com.ypwl.xiaotouzi.interf.IRequestCallback;
import com.ypwl.xiaotouzi.manager.JsonHelper;
import com.ypwl.xiaotouzi.manager.UmengEventHelper;
import com.ypwl.xiaotouzi.manager.net.NetHelper;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.PopuViewUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.Util;
import com.ypwl.xiaotouzi.utils.ViewUtil;
import com.ypwl.xiaotouzi.view.dialog.lib.KProgressHUD;

import java.util.LinkedList;

/**
 * 项目名:	XTZ
 * 包名:	com.ypwl.xiaotouzi.ui.activity
 * 类名:	FinanceSupermarketOfTargetDetailActivity
 * 作者:	罗霄
 * 创建时间:	2016/4/19 15:05
 * <p/>
 * 描述:	金融超市 -- 标的详情页面
 * <p/>
 * svn版本:	$Revision: 16041 $
 * 更新人:	$Author: pengdakai $
 * 更新时间:	$Date: 2016-09-26 17:02:46 +0800 (周一, 26 九月 2016) $
 * 更新描述:	$Message$
 */
public class FinanceSupermarketOfTargetDetailActivity extends BaseActivity implements View.OnClickListener {

    private String project_id;//项目id
    private String mLastPageName;
    private LinearLayout mLlBack;
    private TextView mTvTitleLeft;
    private TextView mTvTitleContent;
    private TextView mTvTitleRight;
    private String p_name;
    private View mNoDataView;
    private TextView mTvProName;
    private TextView mTvYield;
    private RelativeLayout mRlJiaxi;
    private TextView mTvJiaxi;
    private TextView mTvJiaxiType;
    private TextView mTvPeriod;
//    private TextView mTvPeriodUnit;
    private TextView mTvRepMethod;
    private TextView mTvButton;
    private Intent intent;
    private String pid;
    private LinearLayout mLlContainer;
    private String url;
    //    private TextView mTvInfoContent;
    private PopuViewUtil popuViewUtil;
    private KProgressHUD mDialogLoading;
    private LinearLayout mLlContainerInfoHeader;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance_supermarket_of_target_detail);

        project_id = getIntent().getStringExtra(Const.KEY_INTENT_JUMP_BASE_DATA);
        p_name = getIntent().getStringExtra(Const.KEY_INTENT_JUMP_BASE_DATA + "p_name");
        pid = getIntent().getStringExtra(Const.KEY_INTENT_JUMP_BASE_DATA + "pid");
        mLastPageName = getIntent().getStringExtra(Const.KEY_INTENT_JUMP_FROM_DATA);

        initView();

        initListener();

        initData();
    }

    private void initListener() {
        mTvTitleRight.setOnClickListener(this);
        mLlBack.setOnClickListener(this);
        mTvButton.setOnClickListener(this);
    }

    private void initData() {
        mTvTitleLeft.setText(TextUtils.isEmpty(mLastPageName) ? "" : mLastPageName);
        mTvTitleContent.setText(TextUtils.isEmpty(p_name) ? "" : p_name);
        mTvTitleRight.setText(UIUtil.getString(R.string.finance_supermarket_target_list_title_right));

        ViewUtil.showContentLayout(Const.LAYOUT_LOADING, mNoDataView, mLlContainer);

        getData();
    }

    private void getData() {
        url = String.format(URLConstant.FINANCE_PLATFORM_TARGET_DEATAIL, pid, project_id,GlobalUtils.token);

        NetHelper.get(url, new IRequestCallback<String>() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onFailure(Exception e) {
                        ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mNoDataView, mLlContainer, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getData();
                            }
                        });
                    }

                    @Override
                    public void onSuccess(String jsonStr) {
                        ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mNoDataView, mLlContainer, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getData();
                            }
                        });
                        try {
                            TargetDetailBean targetDetailBean = JsonHelper.parseObject(jsonStr, TargetDetailBean.class);

                            switch (targetDetailBean.getStatus()) {
                                case ServerStatus.SERVER_STATUS_OK:

                                    fillData(targetDetailBean);

                                    break;
                                default:
                                    UIUtil.showToastShort("" + targetDetailBean.getRet_msg());
                                    break;
                            }

                        } catch (Exception e) {
                            this.onFailure(e);
                        }
                    }
                }

        );
    }

    private void fillData(TargetDetailBean bean) {
        ViewUtil.showContentLayout(Const.LAYOUT_DATA, mNoDataView, mLlContainer);

        mTvProName.setText(TextUtils.isEmpty(bean.getProject_name()) ? "" : bean.getProject_name());
        mTvYield.setText(TextUtils.isEmpty(bean.getRate()) ? "0" : bean.getRate());
        mTvJiaxi.setText(TextUtils.isEmpty(bean.getAdd_interest()) ? "0" : bean.getAdd_interest());

        switchForJiaXi(mRlJiaxi, bean.getAt_type(), mTvJiaxiType);

        mTvPeriod.setText(TextUtils.isEmpty(bean.getTime_limit()) || TextUtils.isEmpty(bean.getTime_type()) ? "" : UIUtil.getString(R.string.finance_supermarket_target_list_period_info) + "：" + bean.getTime_limit() + getPeriodUnit(bean.getTime_type()));
//        mTvPeriodUnit.setText(getPeriodUnit(bean.getTime_type()));
        mTvRepMethod.setText(TextUtils.isEmpty(bean.getReturn_name()) ? "" : String.format(UIUtil.getString(R.string.finance_supermarket_target_detail_rep_method), bean.getReturn_name()));
        //        mTvInfoContent.setText(TextUtils.isEmpty(bean.getDescription()) ? "" : Html.fromHtml(bean.getDescription()));
        /**加载webview*/
        mWebView.loadData(bean.getDescription(), "text/html; charset=UTF-8", null);
    }

    private void switchForJiaXi(RelativeLayout container, String type, TextView jiaXiTyptView) {
        //        0：没有加息
        //        1：首投加息
        //        2：加息
        container.setVisibility(View.VISIBLE);
        jiaXiTyptView.setTextColor(UIUtil.getColor(R.color.finance_supermarket_of_target_detail_bg_blue));
        switch (type) {
            case "0":
                container.setVisibility(View.INVISIBLE);
                break;
            case "1":
                jiaXiTyptView.setText(UIUtil.getString(R.string.finance_supermarket_target_detail_jiaxi_type_first));
                jiaXiTyptView.setBackgroundDrawable(UIUtil.getDrawable(R.drawable.shape_finance_target_detail_jiaxi_info_bg));
//                jiaXiTyptView.setTextColor(UIUtil.getColor(R.color.finance_supermarket_of_target_detail_bg_blue));
                break;
            case "2":
                jiaXiTyptView.setText(UIUtil.getString(R.string.finance_supermarket_target_detail_jiaxi_type_not_first));
                jiaXiTyptView.setBackgroundDrawable(UIUtil.getDrawable(R.drawable.shape_finance_target_detail_jiaxi_t_info_bg));
//                jiaXiTyptView.setTextColor(UIUtil.getColor(R.color.finance_supermarket_yellow));
                break;
        }
    }

    private String getPeriodUnit(String str) {
        switch (str) {
            case "2":
                str = "天";
                break;
            default:
                str = "个月";
                break;
        }
        return str;
    }

    private void initView() {
        mDialogLoading = KProgressHUD.create(mActivity);

        mLlBack = findView(R.id.layout_title_back);
        mTvTitleLeft = findView(R.id.tv_title_back);
        mTvTitleContent = findView(R.id.tv_title);
        mTvTitleRight = findView(R.id.tv_title_txt_right);
        mTvTitleRight.setVisibility(View.VISIBLE);

        mNoDataView = findView(R.id.layout_no_data_view);

        mTvProName = findView(R.id.activity_finance_supermarket_of_target_detail_tv_pro_name);
        mTvYield = findView(R.id.activity_finance_supermarket_of_target_detail_tv_yield);
        mRlJiaxi = findView(R.id.activity_finance_supermarket_of_target_detail_rl_jiaxi);
        mTvJiaxi = findView(R.id.activity_finance_supermarket_of_target_detail_tv_jiaxi);
        mTvJiaxiType = findView(R.id.activity_finance_supermarket_of_target_detail_tv_jiaxi_type);
        mTvPeriod = findView(R.id.activity_finance_supermarket_of_target_detail_tv_period);
//        mTvPeriodUnit = findView(R.id.activity_finance_supermarket_of_target_detail_tv_period_unit);
        mTvRepMethod = findView(R.id.activity_finance_supermarket_of_target_detail_tv_rep_method);
        //        mTvInfoContent = findView(R.id.activity_finance_supermarket_of_target_detail_info_content);
        mTvButton = findView(R.id.activity_finance_supermarket_of_target_detail_button);

        mLlContainerInfoHeader = findView(R.id.activity_finance_supermarket_of_target_detail_ll_container_info_header);

        mLlContainer = findView(R.id.activity_finance_supermarket_of_target_detail_ll_container);
        mWebView = findView(R.id.activity_finance_supermarket_of_target_detail_info_webview);
    }

    private boolean toLogin() {
        LoginBean loginBean = Util.legalLogin();
        if (loginBean == null) {
            UIUtil.showToastShort("请先登录");
            startActivity(LoginActivity.class);
            mTvButton.setEnabled(true);
        }

        return loginBean == null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_title_back:
                finish();
                break;
            case R.id.tv_title_txt_right:
                intent = new Intent();
                intent.putExtra(Const.KEY_INTENT_JUMP_FROM_DATA, p_name);
                intent.putExtra("p_name", p_name);
                intent.putExtra("pid", pid);
                intent.setClass(mActivity, PlatformDetailActivity.class);
                startActivity(intent);
                break;
            case R.id.activity_finance_supermarket_of_target_detail_button:
                mTvButton.setEnabled(false);
                if (!toLogin()) {
                    buttonToGetData();
                }
                break;
        }
    }

    private void buttonToGetData() {
        url = String.format(URLConstant.FINANCE_PLATFORM_TARGET_DEATAIL_BUTTON_ISBIND, GlobalUtils.token, pid);

        NetHelper.get(url, new IRequestCallback<String>() {
                    @Override
                    public void onStart() {
                        mDialogLoading.show();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        mDialogLoading.dismiss();
                        mTvButton.setEnabled(true);
                        mDialogLoading.dismiss();
                        mTvButton.setEnabled(true);
                    }

                    @Override
                    public void onSuccess(String jsonStr) {
                        mDialogLoading.dismiss();
                        mTvButton.setEnabled(true);
                        try {
                            JSONObject jsonObject = JSON.parseObject(jsonStr);
                            int status = jsonObject.getIntValue(Const.JSON_KEY_status);
                            String ret_msg = jsonObject.getString("ret_msg");

                            switch (status) {
                                case ServerStatus.SERVER_STATUS_OK:

                                    toWebView(String.format(URLConstant.FINANCE_PLATFORM_TARGET_DEATAIL_BUTTON, GlobalUtils.token, pid, project_id));
                                    UmengEventHelper.onEvent(UmengEventID.FsmPlatformInvest);
                                    break;
                                case 5002:
                                    //     5002（未绑定该平台）
                                    showDialog();

                                    break;
                                default:
                                    UIUtil.showToastShort("" + ret_msg);
                                    break;
                            }

                        } catch (Exception e) {
                            this.onFailure(e);
                        }
                    }
                }
        );
    }

    private void toWebView(String url) {
        intent = new Intent(getApplicationContext(),CommonWebPageActivity.class);
        //intent.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA + "ISSHOW", true);
        //        intent.putExtra(Const.KEY_INTENT_JUMP_FROM_DATA, UIUtil.getString(R.string.finance_supermarket_fragment_name));
        intent.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA, url);
        //intent.setClass(mActivity, CommonWebPageActivity.class);
        startActivity(intent);
    }

    private void showDialog() {
        if (null == popuViewUtil) {

            LinkedList<String> mDataPopu = new LinkedList<>();
            //            0：注册，1登录
            mDataPopu.add(UIUtil.getString(R.string.finance_supermarket_target_detail_button_popu_create));
            mDataPopu.add(UIUtil.getString(R.string.finance_supermarket_target_detail_button_popu_binding));

            popuViewUtil = new PopuViewUtil(mActivity, mDataPopu, new PopuViewUtil.OnClickCountsListener() {
                @Override
                public void onClick(int position, String str) {
                    url = String.format(URLConstant.FINANCE_PLATFORM_TARGET_DEATAIL_BUTTON_POPU, GlobalUtils.token, project_id, pid, "" + position);
                    toWebView(url);
                    if (position == 0){
                        UmengEventHelper.onEvent(UmengEventID.FsmPlatformReg);
                    }else {
                        UmengEventHelper.onEvent(UmengEventID.FsmPlatformLogin);
                    }
                }

            });
        }
        popuViewUtil.setTitleContent(String.format(UIUtil.getString(R.string.finance_supermarket_target_detail_button_popu_title), p_name));
        popuViewUtil.setCancelButtonVisibility(View.GONE);
        popuViewUtil.setGravity(Gravity.CENTER);
        popuViewUtil.setWindowAnimations(R.style.AnimationInvestStatus);
        popuViewUtil.show();
    }
}
