package com.ypwl.xiaotouzi.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.base.BaseFragment;
import com.ypwl.xiaotouzi.bean.LoginBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.ui.activity.AutoTallyPlatformActivity;
import com.ypwl.xiaotouzi.ui.activity.CommonWebPageActivity;
import com.ypwl.xiaotouzi.ui.activity.FinanceMarketCreditCardActivity;
import com.ypwl.xiaotouzi.ui.activity.FinanceMarketPawnActivity;
import com.ypwl.xiaotouzi.ui.activity.FundsInevstActivity;
import com.ypwl.xiaotouzi.ui.activity.InsuranceActivity;
import com.ypwl.xiaotouzi.ui.activity.LoginActivity;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.LogUtil;
import com.ypwl.xiaotouzi.utils.StringUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.Util;

/**
 * 项目名:	XTZ
 * 包名:	com.ypwl.xiaotouzi.ui.fragment
 * 类名:	FragmentFinanceSupermarketOfMore
 * 作者:	罗霄
 * 创建时间:	2016/4/15 16:21
 * <p/>
 * 描述:	金融超市 -- 更多页面
 * <p/>
 * svn版本:	$Revision: 15121 $
 * 更新人:	$Author: luoxiao $
 * 更新时间:	$Date: 2016-05-26 15:04:23 +0800 (周四, 26 五月 2016) $
 * 更新描述:	$Message$
 */
public class FragmentFinanceSupermarketOfMore extends BaseFragment implements View.OnClickListener {

    private LinearLayout mItemContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_finance_supermarket_of_more, container, false);
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        initView(v);
    }

    private void initView(View rootView) {
        mItemContainer = findView(rootView, R.id.fragment_finance_supermarket_of_more_item_container);
        addView(mItemContainer, R.string.finance_supermarket_more_invest_name,
                R.string.finance_supermarket_more_insurance_name,
                R.string.finance_supermarket_more_credit_name,
//                R.string.finance_supermarket_more_financing_name,  //快速融资这一栏目前不需要显示
                R.string.finance_supermarket_more_impawn_name);
    }

    private void addView(ViewGroup containerView, int... mIdTag) {
        for (int idTag : mIdTag) {
            containerView.addView(getItem(idTag));
        }
    }

    private View getItem(int mIdTag) {
        View mItem = View.inflate(getActivity(), R.layout.base_item_finance_supermarket_of_more, null);

        TextView mTvName = findView(mItem, R.id.base_item_finance_supermarket_of_more_name);
        TextView mTvInfoLeft = findView(mItem, R.id.base_item_finance_supermarket_of_more_info_left);
        TextView mTvInfoRight = findView(mItem, R.id.base_item_finance_supermarket_of_more_info_right);
        ImageView mIvLogo = findView(mItem, R.id.base_item_finance_supermarket_of_more_iv_logo);

        switch (mIdTag) {
            case R.string.finance_supermarket_more_invest_name:
                mTvName.setText(UIUtil.getString(R.string.finance_supermarket_more_invest_name));
                mTvInfoLeft.setText(UIUtil.getString(R.string.finance_supermarket_more_invest_info_left));
                mTvInfoRight.setText(UIUtil.getString(R.string.finance_supermarket_more_invest_info_right));
                mIvLogo.setImageDrawable(UIUtil.getDrawable(R.mipmap.pic_finance_supermarket_more_invest));
                break;
            case R.string.finance_supermarket_more_insurance_name:
                mTvName.setText(UIUtil.getString(R.string.finance_supermarket_more_insurance_name));
                mTvInfoLeft.setText(UIUtil.getString(R.string.finance_supermarket_more_insurance_info_left));
                mTvInfoRight.setText(UIUtil.getString(R.string.finance_supermarket_more_insurance_info_right));
                mIvLogo.setImageDrawable(UIUtil.getDrawable(R.mipmap.pic_finance_supermarket_more_insurance));
                break;
            case R.string.finance_supermarket_more_credit_name:
                mTvName.setText(UIUtil.getString(R.string.finance_supermarket_more_credit_name));
                mTvInfoLeft.setText(UIUtil.getString(R.string.finance_supermarket_more_credit_info_left));
                mTvInfoRight.setText(UIUtil.getString(R.string.finance_supermarket_more_credit_info_right));
                mIvLogo.setImageDrawable(UIUtil.getDrawable(R.mipmap.pic_finance_supermarket_more_credit));
                break;
            case R.string.finance_supermarket_more_financing_name:
                mTvName.setText(UIUtil.getString(R.string.finance_supermarket_more_financing_name));
                mTvInfoLeft.setText(UIUtil.getString(R.string.finance_supermarket_more_financing_info_left));
                mTvInfoRight.setText(UIUtil.getString(R.string.finance_supermarket_more_financing_info_right));
                break;
            case R.string.finance_supermarket_more_impawn_name:
                mTvName.setText(UIUtil.getString(R.string.finance_supermarket_more_impawn_name));
                mTvInfoLeft.setText(UIUtil.getString(R.string.finance_supermarket_more_impawn_info_left));
                mTvInfoRight.setText("");
                mIvLogo.setImageDrawable(UIUtil.getDrawable(R.mipmap.pic_finance_supermarket_more_impawn));
                break;
        }

        mItem.setTag(mIdTag);
        mItem.setOnClickListener(this);

        return mItem;
    }

    private boolean toLogin() {
        LoginBean loginBean = Util.legalLogin();
        if (loginBean == null) {
            UIUtil.showToastShort("请先登录");
            startActivity(LoginActivity.class);
        }

        return loginBean == null;
    }

    @Override
    public void onClick(View v) {
        switch ((int) v.getTag()) {
            case R.string.finance_supermarket_more_invest_name://高端投资
                if (!toLogin()) {
                    startActivity(FundsInevstActivity.class);
                }
                break;
            case R.string.finance_supermarket_more_insurance_name://香港保险
                if (!toLogin()) {
                    startActivity(InsuranceActivity.class);
                }
                break;
            case R.string.finance_supermarket_more_credit_name://信用卡申请
                if (!toLogin()) {
                    startActivity(FinanceMarketCreditCardActivity.class);
                }
                break;
            case R.string.finance_supermarket_more_financing_name://快速融资
                if (!toLogin()) {
                    Intent intent = new Intent(getContext(), CommonWebPageActivity.class);
                    String url = StringUtil.format(URLConstant.FINANCE_PLATFORM_RONGZI, GlobalUtils.token);
                    LogUtil.e(TAG, url);
                    intent.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA, url);
                    intent.putExtra(Const.KEY_INTENT_JUMP_FROM_DATA, "");
                    startActivity(intent);
                }
                break;
            case R.string.finance_supermarket_more_impawn_name://典当
                startActivity(FinanceMarketPawnActivity.class);
                break;
        }
    }
}
