package com.ypwl.xiaotouzi.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.bean.LoginBean;
import com.ypwl.xiaotouzi.utils.Util;

/**
 * 手机号码显示
 *
 * Created by PDK on 2016/5/25.
 */
public class PhoneNumberActivity extends BaseActivity implements View.OnClickListener {

    private LoginBean loginBean;
    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);
        loginBean = Util.legalLogin();

        initView();
    }

    private void initView() {
        mTitle = (TextView) findViewById(R.id.tv_title);
        TextView mPhoneNum = (TextView) findViewById(R.id.tv_user_phone_number);
        mPhoneNum.setText(loginBean.getPhone());
        mTitle.setText("手机号码");

        findViewById(R.id.layout_title_back).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_title_back:
                finish();
                break;

        }
    }

}
