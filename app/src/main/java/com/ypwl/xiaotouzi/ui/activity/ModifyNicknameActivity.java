package com.ypwl.xiaotouzi.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.bean.CommonBean;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.event.NickNameSuccessEvent;
import com.ypwl.xiaotouzi.interf.IRequestCallback;
import com.ypwl.xiaotouzi.manager.EventHelper;
import com.ypwl.xiaotouzi.manager.net.NetHelper;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.StringUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.view.dialog.lib.KProgressHUD;

/**
 * 修改昵称
 *
 * Created by PDK on 2016/5/20.
 */
public class ModifyNicknameActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTitle;
    private TextView mRightTitle;
    private EditText mNickName;
    private String nickName;
    private KProgressHUD kProgressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_nickname);
        kProgressHUD = KProgressHUD.create(this);
        initView();

    }

    private void initView() {
        mTitle = (TextView) findViewById(R.id.tv_title);
        mRightTitle = (TextView) findViewById(R.id.tv_title_txt_right);
        mRightTitle.setVisibility(View.VISIBLE);
        mNickName = (EditText) findViewById(R.id.et_nickname);


        mTitle.setText("修改昵称");
        mRightTitle.setText("提交");

        findViewById(R.id.layout_title_back).setOnClickListener(this);
        mRightTitle.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_title_back:
                finish();
                break;
            case R.id.tv_title_txt_right:
                commitData();
                break;

        }
    }

    /**向服务器提交数据*/
    private void commitData() {
        nickName = mNickName.getText().toString().trim();

        if (!TextUtils.isEmpty(nickName)){
            String url = StringUtil.format(URLConstant.USER_INFO_UPDATE, GlobalUtils.token, GlobalUtils.toURLEncoded(nickName));
            NetHelper.get(url,DataCallBack);
        }else {
            UIUtil.showToastShort("昵称不能为空");
        }

    }

    IRequestCallback DataCallBack = new IRequestCallback<CommonBean>() {

        @Override
        public void onStart() {
            kProgressHUD.show();
        }

        @Override
        public void onFailure(Exception e) {
            UIUtil.showToastShort("昵称修改失败，请稍后再试");
            kProgressHUD.dismiss();
        }

        @Override
        public void onSuccess(CommonBean commonBean) {
            kProgressHUD.dismiss();
            if (commonBean.getStatus() == 0){
                UIUtil.showToastShort("昵称修改成功");
                EventHelper.post(new NickNameSuccessEvent(nickName));
                finish();
            }
        }
    };


}
