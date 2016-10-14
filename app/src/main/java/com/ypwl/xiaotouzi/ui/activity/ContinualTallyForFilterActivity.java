package com.ypwl.xiaotouzi.ui.activity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.bean.ContinualTallyForFilterBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.event.ContinualTallyForFilterEvent;
import com.ypwl.xiaotouzi.event.ContinualTallyForFilterSelectPlatformEvent;
import com.ypwl.xiaotouzi.manager.EventHelper;
import com.ypwl.xiaotouzi.utils.DateTimeUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;

import java.util.Calendar;
import java.util.Date;

/**
 * 项目名:	XTZ
 * 包名:	com.ypwl.xiaotouzi.ui.activity
 * 类名:	ContinualTallyForFilter
 * 作者:	罗霄
 * 创建时间:	2016/4/11 14:26
 * <p/>
 * 描述:	流水资产 ==> 筛选页面
 * <p/>
 * svn版本:	$Revision: 15161 $
 * 更新人:	$Author: luoxiao $
 * 更新时间:	$Date: 2016-05-26 18:26:48 +0800 (周四, 26 五月 2016) $
 * 更新描述:	${TODO}
 */
public class ContinualTallyForFilterActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout mLlBack;
    private ImageView mIvBack;
    private TextView mTvTitleLeft;
    private TextView mTvTitleContent;
    private TextView mTvTitleRight;
    private TextView mTvClearConditions;
    private RelativeLayout mRlAllType;
    private ImageView mIvAllType;
    private RelativeLayout mRlTypeInvest;
    private ImageView mIvTypeInvest;
    private RelativeLayout mRlTypeBack;
    private ImageView mIvTypeBack;
    private RelativeLayout mRlShowLastBack;
    private ImageView mIvShowLastBack;
    private TextView mTvShowLastBackContent;
    private RelativeLayout mRlAllTime;
    private ImageView mIvAllTime;
    private RelativeLayout mRlTimeFrom;
    private TextView mTvTimeFrom;
    private RelativeLayout mRlTimeEnd;
    private TextView mTvTimeEnd;
    private RelativeLayout mRlSelectPlatform;
    private TextView mTvSelectPlatform;
    private ContinualTallyForFilterBean mBean;

    private DatePickerDialog mStartTimePickerDialog;
    private DatePickerDialog mEndTimePickerDialog;
    private long mCurrentLong;

    private final String state_selected = "1";
    private final String state_unselect = "0";
    private final String state_all_time = state_unselect;
    private final String state_not_all_time = "-1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_continual_tally_for_filter);

        this.overridePendingTransition(R.anim.login_register_bottom_in, R.anim.login_register_bottom_in_out);

        mBean = getIntent().getParcelableExtra(Const.KEY_INTENT_JUMP_BASE_DATA);

        initView();

        initListener();

        initData();
    }

    private void notifyDataSetChanged() {
        if (null != mBean) {

            if (!TextUtils.isEmpty(mBean.getEndtime()) && state_all_time.equalsIgnoreCase(mBean.getStarttime())){
                mBean.setStarttime(state_not_all_time);
            }

            if ((state_unselect.equalsIgnoreCase(mBean.getAll()) && state_unselect.equalsIgnoreCase(mBean.getInvest()) && state_unselect.equalsIgnoreCase(mBean.getAll_return()))
                    || (state_not_all_time.equalsIgnoreCase(mBean.getStarttime()) && TextUtils.isEmpty(mBean.getEndtime()))){
//                    || (!state_all_time.equalsIgnoreCase(mBean.getStarttime()) && TextUtils.isEmpty(mBean.getEndtime()))){
                mTvTitleRight.setEnabled(false);
                mTvTitleRight.setTextColor(Color.GRAY);
            }else {
                mTvTitleRight.setEnabled(true);
                mTvTitleRight.setTextColor(UIUtil.getColor(R.color.white));
            }

            mBean.setAll(state_selected.equalsIgnoreCase(mBean.getInvest()) && state_selected.equalsIgnoreCase(mBean.getAll_return()) && state_unselect.equalsIgnoreCase(mBean.getLast_return()) ? state_selected : state_unselect);

            mIvAllType.setVisibility(state_selected.equalsIgnoreCase(mBean.getAll()) ? View.VISIBLE : View.INVISIBLE);
            mIvTypeInvest.setVisibility(state_selected.equalsIgnoreCase(mBean.getInvest()) ? View.VISIBLE : View.INVISIBLE);
            mIvTypeBack.setVisibility(state_selected.equalsIgnoreCase(mBean.getAll_return()) ? View.VISIBLE : View.INVISIBLE);
            mIvShowLastBack.setImageResource(state_selected.equalsIgnoreCase(mBean.getLast_return()) ? R.mipmap.btn_046_select : R.mipmap.btn_046);
            mTvSelectPlatform.setText(state_unselect.equalsIgnoreCase(mBean.getPid()) ? UIUtil.getString(R.string.continual_tally_for_filter_all_platform) : (TextUtils.isEmpty(mBean.getP_name()) ? "" : mBean.getP_name()));

            if (state_all_time.equalsIgnoreCase(mBean.getStarttime())) {
                mIvAllTime.setVisibility(View.VISIBLE);
                mBean.setEndtime("");
                mTvTimeFrom.setText("");
                mTvTimeEnd.setText("");
            } else {
                mIvAllTime.setVisibility(View.INVISIBLE);

                if (!state_not_all_time.equalsIgnoreCase(mBean.getStarttime())){
                    mTvTimeFrom.setText(DateTimeUtil.formatDateTime(Long.parseLong(mBean.getStarttime() + "000"), DateTimeUtil.DF_YYYY_MM_DD));
                }
                if (!TextUtils.isEmpty(mBean.getEndtime())){
                    mTvTimeEnd.setText(DateTimeUtil.formatDateTime(Long.parseLong(mBean.getEndtime() + "000"), DateTimeUtil.DF_YYYY_MM_DD));
                }
            }

            mRlShowLastBack.setVisibility(state_selected.equalsIgnoreCase(mBean.getAll_return()) ? View.VISIBLE : View.GONE);
            mTvShowLastBackContent.setVisibility(state_selected.equalsIgnoreCase(mBean.getAll_return()) ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(0, R.anim.login_register_bottom_out);//activity下滑出去
    }

    private void initListener() {
        mLlBack.setOnClickListener(this);
        mTvTitleRight.setOnClickListener(this);
        mTvClearConditions.setOnClickListener(this);
        mRlAllType.setOnClickListener(this);
        mRlTypeInvest.setOnClickListener(this);
        mRlTypeBack.setOnClickListener(this);
        mRlShowLastBack.setOnClickListener(this);
        mRlAllTime.setOnClickListener(this);
        mRlTimeFrom.setOnClickListener(this);
        mRlTimeEnd.setOnClickListener(this);
        mRlSelectPlatform.setOnClickListener(this);
    }

    private void initData() {
        mTvTitleLeft.setText(UIUtil.getString(R.string.continual_tally_for_filter_title_left));
        mTvTitleContent.setText(UIUtil.getString(R.string.continual_tally_for_filter_title_content));
        mTvTitleRight.setText(UIUtil.getString(R.string.continual_tally_for_filter_title_right));

        if (null == mBean){
            mBean = new ContinualTallyForFilterBean();
        }else {
            notifyDataSetChanged();
        }

        //当天的时间戳
        mCurrentLong = DateTimeUtil.parseDate(DateTimeUtil.formatDateTime(DateTimeUtil.getCurrentDate(), DateTimeUtil.DF_YYYY_MM_DD), DateTimeUtil.DF_YYYY_MM_DD).getTime();
    }

    private void initView() {
        mLlBack = findView(R.id.layout_title_back);
        mIvBack = findView(R.id.iv_title_back);
//        mIvBack.setVisibility(View.GONE);
        mTvTitleLeft = findView(R.id.tv_title_back);
        mTvTitleContent = findView(R.id.tv_title);
        mTvTitleRight = findView(R.id.tv_title_txt_right);
        mTvTitleRight.setVisibility(View.VISIBLE);

        mTvClearConditions = findView(R.id.activity_continual_tally_for_filter_tv_clear_conditions);

        mRlAllType = findView(R.id.activity_continual_tally_for_filter_rl_all_type);
        mIvAllType = findView(R.id.activity_continual_tally_for_filter_iv_all_type);

        mRlTypeInvest = findView(R.id.activity_continual_tally_for_filter_rl_type_invest);
        mIvTypeInvest = findView(R.id.activity_continual_tally_for_filter_iv_type_invest);

        mRlTypeBack = findView(R.id.activity_continual_tally_for_filter_rl_type_back);
        mIvTypeBack = findView(R.id.activity_continual_tally_for_filter_iv_type_back);

        mRlShowLastBack = findView(R.id.activity_continual_tally_for_filter_rl_last_back);
        mIvShowLastBack = findView(R.id.activity_continual_tally_for_filter_iv_last_back);
        mTvShowLastBackContent = findView(R.id.activity_continual_tally_for_filter_tv_last_back_content);

        mRlAllTime = findView(R.id.activity_continual_tally_for_filter_rl_all_time);
        mIvAllTime = findView(R.id.activity_continual_tally_for_filter_iv_all_time);

        mRlTimeFrom = findView(R.id.activity_continual_tally_for_filter_rl_time_from);
        mTvTimeFrom = findView(R.id.activity_continual_tally_for_filter_tv_time_from);

        mRlTimeEnd = findView(R.id.activity_continual_tally_for_filter_rl_time_end);
        mTvTimeEnd = findView(R.id.activity_continual_tally_for_filter_tv_time_end);

        mRlSelectPlatform = findView(R.id.activity_continual_tally_for_filter_rl_select_platform);
        mTvSelectPlatform = findView(R.id.activity_continual_tally_for_filter_tv_select_platform);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_title_back:
                finish();
                break;
            case R.id.tv_title_txt_right:
                //发送筛选完成的事件，通知其他有有关联的页面更新数据
                EventHelper.post(new ContinualTallyForFilterEvent(mBean));
                finish();
                break;
            case R.id.activity_continual_tally_for_filter_tv_clear_conditions:
                mBean = new ContinualTallyForFilterBean();
                notifyDataSetChanged();
                break;
            case R.id.activity_continual_tally_for_filter_rl_all_type:
                if (state_selected.equalsIgnoreCase(mBean.getAll())) {
                    mBean.setAll(state_unselect);
                    mBean.setInvest(state_unselect);
                    mBean.setAll_return(state_unselect);
                } else {
                    mBean.setAll(state_selected);
                    mBean.setInvest(state_selected);
                    mBean.setAll_return(state_selected);
                }
                mBean.setLast_return(state_unselect);
                notifyDataSetChanged();
                break;
            case R.id.activity_continual_tally_for_filter_rl_type_invest:
                mBean.setInvest(state_selected.equalsIgnoreCase(mBean.getInvest()) ? state_unselect : state_selected);
                notifyDataSetChanged();
                break;
            case R.id.activity_continual_tally_for_filter_rl_type_back:
                mBean.setAll_return(state_selected.equalsIgnoreCase(mBean.getAll_return()) ? state_unselect : state_selected);
                mBean.setLast_return(state_unselect);
                notifyDataSetChanged();
                break;
            case R.id.activity_continual_tally_for_filter_rl_last_back:
                mBean.setLast_return(state_selected.equalsIgnoreCase(mBean.getLast_return()) ? state_unselect : state_selected);
                notifyDataSetChanged();
                break;
            case R.id.activity_continual_tally_for_filter_rl_all_time:
                if (!state_all_time.equalsIgnoreCase(mBean.getStarttime())){
                    mBean.setStarttime(state_all_time);
                    mBean.setEndtime("");
                }else {
                    mBean.setStarttime(state_not_all_time);
                }
                notifyDataSetChanged();
                break;
            case R.id.activity_continual_tally_for_filter_rl_time_from:
                if (null == mStartTimePickerDialog) {
                    Calendar mCalendar = Calendar.getInstance();
                    mCalendar.setTimeInMillis(System.currentTimeMillis());
                    mStartTimePickerDialog = new DatePickerDialog(mActivity, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                            long mSelectLong = DateTimeUtil.parseDate(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth, DateTimeUtil.DF_YYYY_MM_DD).getTime();

                            if (mSelectLong < mCurrentLong + DateTimeUtil.day){

                                if (!TextUtils.isEmpty(mBean.getEndtime()) && Long.parseLong(mBean.getEndtime()) < mSelectLong / 1000){
                                    UIUtil.showToastShort("开始日期不能大于截止日期");
                                    return;
                                }

                                mBean.setStarttime(Long.toString(mSelectLong / 1000));
                                notifyDataSetChanged();
                            }
                        }
                    }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));

                    mStartTimePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                }
                mStartTimePickerDialog.show();
                break;
            case R.id.activity_continual_tally_for_filter_rl_time_end:
                if (null == mEndTimePickerDialog) {
                    Calendar mCalendar = Calendar.getInstance();
                    mCalendar.setTimeInMillis(System.currentTimeMillis());
                    mEndTimePickerDialog = new DatePickerDialog(mActivity, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                            long mSelectLong = DateTimeUtil.parseDate(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth, DateTimeUtil.DF_YYYY_MM_DD).getTime();

                            if (mSelectLong < mCurrentLong + DateTimeUtil.day){

                                if (!state_unselect.equalsIgnoreCase(mBean.getStarttime()) && Long.parseLong(mBean.getStarttime()) > mSelectLong / 1000){
                                    UIUtil.showToastShort("开始日期不能大于截止日期");
                                    return;
                                }

                                mBean.setEndtime(Long.toString(mSelectLong / 1000));
                                notifyDataSetChanged();
                            }
                        }
                    }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));

                    mEndTimePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                }
                mEndTimePickerDialog.show();
                break;
            case R.id.activity_continual_tally_for_filter_rl_select_platform:
                startActivity(ContinualTallyForFilterSelectPlatformActivity.class, mBean.getPid());
                break;
        }
    }

    @Subscribe
    public void onSelectPlatform(ContinualTallyForFilterSelectPlatformEvent event) {
        if (!isFinishing() && null != event && null != event.mBean) {
            mBean.setPid(event.mBean.getPid());
            mBean.setP_name(event.mBean.getP_name());
            notifyDataSetChanged();
        }
    }
}
