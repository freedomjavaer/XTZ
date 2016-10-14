package com.ypwl.xiaotouzi.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.event.GetMoneyEvent;
import com.ypwl.xiaotouzi.interf.IRequestCallback;
import com.ypwl.xiaotouzi.manager.EventHelper;
import com.ypwl.xiaotouzi.manager.net.NetHelper;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.StringUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.view.dialog.KProgressHUDHelper;
import com.ypwl.xiaotouzi.view.dialog.lib.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 提现页面
 *
 * Created by PDK on 2016/4/20.
 */
public class GetMoneyActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTitle;
    private TextView mTitleBackTv;
    private EditText mBankName;
    private EditText mBackCardNumber;
    private EditText mUserName;
    private EditText mBackMoney;
    private TextView mAvailableMoney;
    private String mUsableMoney;
    private TextView mSubmit;
    private String backName;
    private String cardNumber;
    private String userName;
    private String backMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_money);
        mUsableMoney = getIntent().getStringExtra(Const.KEY_INTENT_JUMP_BASE_DATA);

        initView();
        initData();

    }

    private void initData() {
        //title
        mTitle.setText("提现");
        mTitleBackTv.setText("晓钱包");

        //content
        mAvailableMoney.setText("可提现金额：" + mUsableMoney);

    }

    private void initView() {
        mTitle = findView(R.id.tv_title);
        mTitleBackTv = findView(R.id.tv_title_back);

        mBankName = findView(R.id.et_get_money_bank_name);
        mBackCardNumber = findView(R.id.et_get_money_bank_card_number);
        mUserName = findView(R.id.et_get_money_bank_real_name);
        mBackMoney = findView(R.id.et_get_money_back_money);
        mAvailableMoney = findView(R.id.tv_get_back_money_available_money);
        mSubmit = findView(R.id.tv_submit);

        mSubmit.setOnClickListener(this);
        findView(R.id.layout_title_back).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.layout_title_back:
                finish();
                break;
            case R.id.tv_submit:
                backName = mBankName.getText().toString().trim();
                cardNumber = mBackCardNumber.getText().toString().trim();
                userName = mUserName.getText().toString().trim();
                backMoney = mBackMoney.getText().toString().trim();

                if (TextUtils.isEmpty(backName)||TextUtils.isEmpty(cardNumber)||TextUtils.isEmpty(userName)||TextUtils.isEmpty(backMoney)){
                    UIUtil.createMyToast("银行信息不能为空或者提现金额不能为空").show();
                }else {
                    int userMoney = (int) Double.parseDouble(mUsableMoney);
                    int money = (int) Double.parseDouble(backMoney);
                    if (userMoney < 100){
                        UIUtil.createMyToast("可用余额不足100元").show();
                    }else {
                        if (money < 100){
                            UIUtil.createMyToast("提现金额100起").show();
                        }else {
                            submitData();
                        }
                    }
                }
                break;
        }

    }

    /**提交用户转账银行信息*/
    private void submitData() {
        final KProgressHUD mLoading = KProgressHUDHelper.createLoading(this);
        String url = StringUtil.format(URLConstant.MY_INFO_WALLET_GET_MONEY, GlobalUtils.token,userName,backName,cardNumber,backMoney);
        NetHelper.get(url, new IRequestCallback<String>() {
            @Override
            public void onStart() {
                mLoading.show();
            }

            @Override
            public void onFailure(Exception e) {
                mLoading.dismiss();
            }

            @Override
            public void onSuccess(String jsonStr) {
                mLoading.dismiss();
                try {

                    JSONObject jsonObject = new JSONObject(jsonStr);
                    int status = jsonObject.optInt("status");
                    if (status == 0){
                        UIUtil.createMyToast("提交提现申请成功，后台会尽快做转账处理").show();
                        EventHelper.post(new GetMoneyEvent());
                        finish();
                    }else if (status == 120){
                        UIUtil.createMyToast("未绑定手机号").show();
                    }else if (status == 5001){
                        UIUtil.createMyToast("银行卡账号为16-19位纯数字").show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

}
