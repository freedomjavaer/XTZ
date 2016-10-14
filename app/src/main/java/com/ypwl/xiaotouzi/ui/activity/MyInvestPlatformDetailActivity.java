package com.ypwl.xiaotouzi.ui.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.common.ServerStatus;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.event.AutoTallyPlatformRefreshEvent;
import com.ypwl.xiaotouzi.event.InvestPlatformRefreshEvent;
import com.ypwl.xiaotouzi.event.MyInvestPlatformDetailRefreshEvent;
import com.ypwl.xiaotouzi.interf.IRequestCallback;
import com.ypwl.xiaotouzi.manager.EventHelper;
import com.ypwl.xiaotouzi.manager.net.NetHelper;
import com.ypwl.xiaotouzi.ui.fragment.FragmentMyInvestPlatformDetailOption;
import com.ypwl.xiaotouzi.utils.DateTimeUtil;
import com.ypwl.xiaotouzi.utils.FileUtil;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.LogUtil;
import com.ypwl.xiaotouzi.utils.SoftInputUtil;
import com.ypwl.xiaotouzi.utils.StringUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.ViewHolder;
import com.ypwl.xiaotouzi.view.HideTopLinearLayout;
import com.ypwl.xiaotouzi.view.RadioTab;
import com.ypwl.xiaotouzi.view.dialog.CustomDialog;
import com.ypwl.xiaotouzi.view.dialog.lib.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * function: 我的投资-资产-平台详情.
 *
 * <p>Created by lzj on 2016/3/24.</p>
 */
@SuppressLint("SetTextI18n")
public class MyInvestPlatformDetailActivity extends BaseActivity implements View.OnClickListener, HideTopLinearLayout.OnGiveUpTouchEventListener {

    private TextView mTitle;
    private ProgressBar mTitleLoadingBar;
    private TextView mTitleRight;
    private ImageView mTitleBtnOperate;
    private HideTopLinearLayout mLayoutContainer;
    private LinearLayout mLayoutTop;
    private TextView mTotalWaitInt, mTotalWaitDec, mTotalProfitInt, mTotalProfitDec, mTotalRate, mLastSyncTimeUI;
    private RadioTab mRadioTab;
    private final String[] mTabTitleArr = new String[]{
            UIUtil.getString(R.string.myinvest_platform_detailt_bid_ing),
            UIUtil.getString(R.string.myinvest_platform_detailt_bid_end)};
    private FragmentManager fragmentManager;
    /** Fragment TAG :  在投标的 */
    public static final String FRAGMENT_TAG_ING = "MPDA_fragment_tag_ing";
    /** Fragment TAG :  结束标的 */
    public static final String FRAGMENT_TAG_END = "MPDA_fragment_tag_end";
    /** Fragment TAG数组 */
    private static final String[] mFragmentTags = new String[]{FRAGMENT_TAG_ING, FRAGMENT_TAG_END};
    /** Fragment :  在投标的 */
    private FragmentMyInvestPlatformDetailOption mFragmentIng = null;
    /** Fragment :  结束标的 */
    private FragmentMyInvestPlatformDetailOption mFragmentEnd = null;
    /** 平台id */
    private String mPid;
    private PopupWindow mPopupWindow;
    /** 上次同步时间 */
    private long mLastSyncTime;
    private String mFromText;

    /** 是否为自动记账平台 */
    public static final String IS_AUTO = "IS_AUTO";

    private int mCurrIsAuto = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinvest_platform_detail);
        fragmentManager = getSupportFragmentManager();
        Intent fromIntent = getIntent();
        mPid = fromIntent.getStringExtra(Const.KEY_INTENT_JUMP_BASE_DATA);
        mFromText = fromIntent.getStringExtra(Const.KEY_INTENT_JUMP_FROM_DATA);
        mCurrIsAuto = fromIntent.getIntExtra(IS_AUTO, mCurrIsAuto);
        if (StringUtil.isEmptyOrNull(mPid)) {
            UIUtil.showToastShort("未找到相关平台 pid=" + mPid);
            finish();
            return;
        }
        initView();
    }

    private void initView() {
        //title
        findView(R.id.layout_title_back).setOnClickListener(this);
        if (!StringUtil.isEmptyOrNull(mFromText)) {
            ((TextView) findViewById(R.id.tv_title_back)).setText(mFromText);
        }
        mTitle = findView(R.id.tv_title);
        mTitleLoadingBar = findView(R.id.pb_title_right_loading);
        mTitleRight = findView(R.id.tv_title_txt_right);
        mTitleBtnOperate = findView(R.id.iv_title_right_image);
        mTitleBtnOperate.setImageResource(R.mipmap.three_blue_posint);
        mTitleBtnOperate.setOnClickListener(this);
        mLastSyncTimeUI = findView(R.id.tv_update_time);
        onSyncStateChange();//显示同步状态视图

        //content
        mRadioTab = findView(R.id.radiotab_myinvest_platform_detail);
        mRadioTab.setOnTabCheckedListener(mOnTabCheckedListener).addTabs(Arrays.asList(mTabTitleArr));

        mLayoutContainer = findView(R.id.layout_container);
        mLayoutContainer.setOnGiveUpTouchEventListener(this);
        mLayoutContainer.requestDisallowInterceptTouchEventOnHeader(false);
        mLayoutTop = findView(R.id.layout_top);

        mTotalWaitInt = findView(R.id.myinvest_platformdetail_total_wait_int);
        mTotalWaitDec = findView(R.id.myinvest_platformdetail_total_wait_dec);
        mTotalProfitInt = findView(R.id.myinvest_platformdetail_total_profit_int);
        mTotalProfitDec = findView(R.id.myinvest_platformdetail_total_profit_dec);
        mTotalRate = findView(R.id.myinvest_platformdetail_total_rate);
        showDefault();
    }

    /** 暂时初始默认视图 */
    private void showDefault() {
        mTotalWaitInt.setText("0.");
        mTotalWaitDec.setText("00");
        mTotalProfitInt.setText("0.");
        mTotalProfitDec.setText("00");
        mTotalRate.setText("0%");
        String tag = getIntent().getStringExtra(Const.KEY_INTENT_JUMP_BASE_DATA_1);
        mRadioTab.setSelect(FRAGMENT_TAG_END.equals(tag) ? 1 : 0);
    }

    /** 适配器里面请求倒是数据后回来刷新UI */
    @Subscribe
    public void onEventWhenRequestOver(MyInvestPlatformDetailRefreshEvent event) {
        LogUtil.e(TAG, "onEventWhenRequestOver...");
        if (!isFinishing() && event.bean != null) {
            mTitle.setText(event.bean.getP_name());
            String[] totalWait = splitMoney(event.bean.getStotal());
            mTotalWaitInt.setText(totalWait[0]);
            mTotalWaitDec.setText(totalWait[1]);
            String[] totalProfit = splitMoney(event.bean.getProfit());
            mTotalProfitInt.setText(totalProfit[0]);
            mTotalProfitDec.setText(totalProfit[1]);
            mTotalRate.setText(event.bean.getWannual() + "%");
            if (mCurrIsAuto == 1) {//自动同步平台
                mTitleBtnOperate.setVisibility(View.VISIBLE);
                mCurrentSyncState = SYNC_STATE_INIT;
                onSyncStateChange();
                long update_time = event.bean.getUpdate_time();
                if (update_time == 1) {//正在同步
                    mCurrentSyncState = SYNC_STATE_ING;
                    onSyncStateChange();
                    mLastSyncTimeUI.setVisibility(View.GONE);
                } else if (update_time > 0) {//上次同步时间
                    mLastSyncTimeUI.setVisibility(View.VISIBLE);
                    mLastSyncTime = event.bean.getUpdate_time();
                    mCurrentSyncState = SYNC_STATE_END;
                    onSyncStateChange();
                } else {
                    mLastSyncTimeUI.setVisibility(View.GONE);
                }
            } else {//非自动同步平台
                mTitleBtnOperate.setVisibility(View.GONE);
                mLastSyncTimeUI.setVisibility(View.GONE);
            }
        }
    }

    /** 切割金额数量，[0]整数部分,[1]小数部分 */
    private String[] splitMoney(String money) {
        String[] result = new String[2];
        if (StringUtil.isEmptyOrNull(money)) {
            result[0] = "0.";
            result[1] = "00";
        } else {
            String[] split = money.split("\\.");
            if (split.length != 2) {
                result[0] = money + ".";
                result[1] = "00";
            } else {
                result[0] = split[0] + ".";
                result[1] = split[1];
            }
        }
        LogUtil.e(TAG, "after splitMoney : [0]=" + result[0] + " , [1]=" + result[1]);
        return result;
    }

    /** tab切换监听 */
    private RadioTab.OnTabCheckedListener mOnTabCheckedListener = new RadioTab.OnTabCheckedListener() {
        @Override
        public void onChecked(View view, CharSequence checkedTitle, int checkedIndex) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            hideFragments(fragmentManager, transaction);
            switch (checkedIndex) {
                case 0:// 切换到ing
                    if (mFragmentIng == null) {
                        mFragmentIng = FragmentMyInvestPlatformDetailOption.newInstance(FRAGMENT_TAG_ING, mPid, mCurrIsAuto);
                        transaction.add(R.id.layout_content_fragment, mFragmentIng, FRAGMENT_TAG_ING);
                    }
                    transaction.show(mFragmentIng);
                    break;
                case 1:// 切换到end
                    if (mFragmentEnd == null) {
                        mFragmentEnd = FragmentMyInvestPlatformDetailOption.newInstance(FRAGMENT_TAG_END, mPid, mCurrIsAuto);
                        transaction.add(R.id.layout_content_fragment, mFragmentEnd, FRAGMENT_TAG_END);
                    }
                    transaction.show(mFragmentEnd);
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

    @Override
    public boolean giveUpTouchEvent(MotionEvent event) {
        int height = mLayoutTop.getMeasuredHeight();
        int currentSelectedTabposition = mRadioTab.getCurrentSelectedTabposition();
        boolean isListViewTop;
        if (currentSelectedTabposition == 0) {
            isListViewTop = mFragmentIng.isListViewTop();
        } else {
            isListViewTop = mFragmentEnd.isListViewTop();
        }
        mLayoutContainer.setSticky(isListViewTop);
        return height == 0 && isListViewTop;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_title_back://back
                finish();
                break;
            case R.id.iv_title_right_image://operate for auto platform
                operateForAutoAccountPlatform(v);
                break;
        }
    }

    /** 操作自动记账平台 */
    private void operateForAutoAccountPlatform(View v) {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        } else {
            initmPopupWindowView();
            mPopupWindow.showAsDropDown(v, 0, UIUtil.dip2px(-20));
        }
    }

    /** 同步状态--未同步 */
    private static final int SYNC_STATE_INIT = 0;
    /** 同步状态--同步中 */
    private static final int SYNC_STATE_ING = 1;
    /** 同步状态--同步完成 */
    private static final int SYNC_STATE_END = 2;
    /** 当前同步状态 */
    private int mCurrentSyncState = SYNC_STATE_INIT;

    public void initmPopupWindowView() {
        View customView = UIUtil.inflate(R.layout.layout_platform_invest_detail_sync_choose);
        mPopupWindow = new PopupWindow(customView, UIUtil.dip2px(140), UIUtil.dip2px(100));// 宽度和高度
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
        LinearLayout syncData = (LinearLayout) customView.findViewById(R.id.ll_sync_data);
        LinearLayout removeBind = (LinearLayout) customView.findViewById(R.id.ll_remove_bind);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopupWindow != null && mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                    mPopupWindow = null;
                }
                switch (v.getId()) {
                    case R.id.ll_sync_data://同步数据
                        mCurrentSyncState = SYNC_STATE_ING;
                        onSyncStateChange();
                        requestSyncData("");
                        break;
                    case R.id.ll_remove_bind://解除绑定
                        showComfrimDialogForAsk();
                        break;
                }

            }
        };
        syncData.setOnClickListener(onClickListener);
        removeBind.setOnClickListener(onClickListener);
    }

    private KProgressHUD mLoading;

    /** 解除绑定 */
    private void showComfrimDialogForAsk() {
        new CustomDialog.AlertBuilder(this).setTitleText("提示")
                .setContentText("您确认要解除绑定吗？").setContentTextGravity(Gravity.CENTER)
                .setPositiveBtn("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (mLoading == null) {
                            mLoading = KProgressHUD.create(mActivity);
                        }
                        mLoading.show();
                        String url = StringUtil.format(URLConstant.REMOVE_BIND_AUTO_TALLY, GlobalUtils.token, mPid);
                        NetHelper.get(url, new IRequestCallback<String>() {
                            @Override
                            public void onStart() {
                            }

                            @Override
                            public void onFailure(Exception e) {
                                UIUtil.showToastShort("请稍后再试");
                            }

                            @Override
                            public void onSuccess(String jsonStr) {
                                if (GlobalUtils.responseOK(jsonStr)) {
                                    EventHelper.post(new InvestPlatformRefreshEvent());
                                    EventHelper.post(new AutoTallyPlatformRefreshEvent());
                                    UIUtil.showToastLong("解除绑定成功");
                                    finish();
                                } else {
                                    onFailure(null);
                                }
                            }
                        });
                    }
                })
                .setNegativeBtn("取消", null)
                .create()
                .show();
    }

    /** 请求同步数据 */
    private void requestSyncData(String checkCode) {
        String url = StringUtil.format(URLConstant.SYNC_AUTO_TALLY, GlobalUtils.token, mPid, checkCode);
        NetHelper.get(url, new IRequestCallback<String>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onFailure(Exception e) {
                UIUtil.showToastShort("请稍后再试");
                mCurrentSyncState = SYNC_STATE_INIT;
                onSyncStateChange();
            }

            @Override
            public void onSuccess(String jsonStr) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    int status = jsonObject.getInt(Const.JSON_KEY_status);
                    if (status == ServerStatus.SERVER_STATUS_OK) {//请求同步成功,服务端正在同步中
                        //对话框提示服务端在同步
                        autoCloseHintDailogForMsg(getString(R.string.auto_tally_sync_data));
                    } else if (status == ServerStatus.SERVER_STATUS_AUTO_TALLY_AUTH_CODE) { //需要输入验证码
                        String imgdata = jsonObject.getString(Const.JSON_KEY_AUTH_CODE_imgdata);
                        if (!StringUtil.isEmptyOrNull(imgdata) && imgdata.contains("base64")
                                && FileUtil.getImgFromBase64Stream(imgdata.substring(imgdata.indexOf("base64") + 6)) != null) {
                            showAuthCheckCode(imgdata.substring(imgdata.indexOf("base64") + 6));
                        } else {
                            onFailure(null);
                        }
                    } else {
                        mCurrentSyncState = SYNC_STATE_INIT;
                        onSyncStateChange();
                        String errorMsg = jsonObject.getString(Const.JSON_KEY_ret_msg);
                        if (!StringUtil.isEmptyOrNull(errorMsg)) {
                            autoCloseHintDailogForMsg(errorMsg);
                        } else {
                            onFailure(null);
                        }
                    }
                } catch (JSONException e) {
                    onFailure(e);
                }
            }
        });
    }

    /** 显示输入验证码 */
    private void showAuthCheckCode(String imgdata) {
        if (isFinishing()) {
            return;
        }
        View contentView = UIUtil.inflate(R.layout.layout_dialog_auto_tally_auth_ocde);
        final EditText inputNumber = (EditText) contentView.findViewById(R.id.auth_code);
        final ImageView authPic = (ImageView) contentView.findViewById(R.id.pic_code);
        authPic.setImageBitmap(FileUtil.getImgFromBase64Stream(imgdata));
        Button submit = ViewHolder.findViewById(contentView, R.id.btn_submit);

        final CustomDialog dialog = new CustomDialog.AlertBuilder(mActivity)
                .setTitleText("请输入验证码")
                .setCustomContentView(contentView)
                .create();
        dialog.show();

        /** 点击刷新验证码图片 */
        authPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = StringUtil.format(URLConstant.SYNC_AUTO_TALLY, GlobalUtils.token, mPid, -1);
                NetHelper.get(url, new IRequestCallback<String>() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }

                    @Override
                    public void onSuccess(String jsonStr) {
                        try {
                            JSONObject jsonObject = new JSONObject(jsonStr);
                            int status = jsonObject.getInt(Const.JSON_KEY_status);
                            if (status == ServerStatus.SERVER_STATUS_AUTO_TALLY_AUTH_CODE) {
                                String imgdata = jsonObject.getString("imgdata");
                                if (!StringUtil.isEmptyOrNull(imgdata) && imgdata.contains("base64")) {
                                    int index = imgdata.indexOf("base64");
                                    Bitmap imgFromBase64Stream = FileUtil.getImgFromBase64Stream(imgdata.substring(index + 6));
                                    if (imgFromBase64Stream != null) {
                                        authPic.setImageBitmap(imgFromBase64Stream);
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        /** 提交验证码 */
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = inputNumber.getText().toString().trim();
                if (StringUtil.isEmptyOrNull(code)) {
                    UIUtil.showToastShort("验证码不能为空");
                    return;
                }
                dialog.dismiss();
                mCurrentSyncState = SYNC_STATE_ING;
                onSyncStateChange();
                requestSyncData(code);
            }
        });

        /**调用系统输入法，延时保证dialog加载完成*/
        UIUtil.postDelayed(new Runnable() {
            @Override
            public void run() {
                inputNumber.setFocusable(true);
                inputNumber.setFocusableInTouchMode(true);
                inputNumber.requestFocus();
                SoftInputUtil.showSoftInput(inputNumber);
            }
        }, 200);
    }

    /** 对话框提示 */
    private void autoCloseHintDailogForMsg(String msg) {
        if (isFinishing()) {
            return;
        }
        final CustomDialog dialog = new CustomDialog.AlertBuilder(mActivity)
                .setTitleText("提示")
                .setContentText(msg).setContentTextGravity(Gravity.CENTER)
                .setCanceledOnTouchOutside(false)
                .setCanceled(false)
                .create();
        dialog.show();
        UIUtil.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 2000);
    }

    /** 设置当前同步状态视图 */
    private void onSyncStateChange() {
        switch (mCurrentSyncState) {
            case SYNC_STATE_INIT://初始化默认
                mTitleBtnOperate.setVisibility(View.GONE);
                mTitleLoadingBar.setVisibility(View.GONE);
                mTitleRight.setVisibility(View.GONE);
                mLastSyncTimeUI.setVisibility(View.GONE);
                break;
            case SYNC_STATE_ING://同步中
                mTitleBtnOperate.setVisibility(View.GONE);
                mTitleLoadingBar.setVisibility(View.VISIBLE);
                mTitleRight.setVisibility(View.VISIBLE);
                mTitleRight.setText("同步中...");
                if (mLastSyncTime > 0) {
                    mLastSyncTimeUI.setVisibility(View.VISIBLE);
                    String msg = DateTimeUtil.getIntervalTimeDayMin(System.currentTimeMillis(), mLastSyncTime * 1000) + "同步";
                    mLastSyncTimeUI.setText(msg);
                } else {
                    mLastSyncTimeUI.setVisibility(View.GONE);
                }
                break;
            case SYNC_STATE_END://同步完成
                mTitleBtnOperate.setVisibility(View.VISIBLE);
                mTitleLoadingBar.setVisibility(View.GONE);
                mTitleRight.setVisibility(View.GONE);
                mLastSyncTimeUI.setVisibility(View.VISIBLE);
                String msg = DateTimeUtil.getIntervalTimeDayMin(System.currentTimeMillis(), mLastSyncTime * 1000) + "同步";
                mLastSyncTimeUI.setText(msg);
                break;
        }

    }

}
