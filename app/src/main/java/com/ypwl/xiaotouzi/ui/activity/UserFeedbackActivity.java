package com.ypwl.xiaotouzi.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ta.utdid2.android.utils.StringUtils;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.interf.IRequestCallback;
import com.ypwl.xiaotouzi.manager.net.NetHelper;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.view.dialog.CustomDialog;
import com.ypwl.xiaotouzi.view.dialog.lib.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户反馈
 * <p/>
 * Created by PDK on 2016/3/17.
 */
public class UserFeedbackActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTitle;
    private TextView mWordCount;
    private EditText mSuggestionEdit;
    private EditText mContactInfo;
    private String userMessage;
    private String userInfo;
    private KProgressHUD mDialogLoading;
    private TextView mTitleBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feedback);

        initView();
        initData();
    }

    private void initData() {
        //标题
        mTitle.setText(R.string.suggestion_feedback);
        String backText = getIntent().getStringExtra(Const.KEY_INTENT_JUMP_FROM_DATA);
        mTitleBack.setText(backText);

        //监听信息编辑框输入情况
        listenEditText();

    }

    private void listenEditText() {
        mSuggestionEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = mSuggestionEdit.getText().length();
                mWordCount.setText(length + "");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initView() {
        mTitle = (TextView) findViewById(R.id.tv_title);
        mWordCount = (TextView) findViewById(R.id.word_count);
        mSuggestionEdit = (EditText) findViewById(R.id.suggestion_message);
        mContactInfo = (EditText) findViewById(R.id.useruser_contact_info);
        mTitleBack = (TextView) findViewById(R.id.tv_title_back);

        findViewById(R.id.commit_useruser_contact_info).setOnClickListener(this);
        findViewById(R.id.layout_title_back).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_title_back:
                finish();
                break;
            case R.id.commit_useruser_contact_info:
                userMessage = mSuggestionEdit.getText().toString().trim();
                userInfo = mContactInfo.getText().toString().trim();
                if (StringUtils.isEmpty(userMessage)) {
                    UIUtil.showToastShort("问题反馈不能为空");
                    return;
                }
                commitUserFeedBack();
                break;
            case R.id.iv_feedback_success_certain:
                finish();
                break;
        }
    }

    private void commitUserFeedBack() {
        mDialogLoading = KProgressHUD.create(this);

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("token", GlobalUtils.token);
        paramMap.put("content", userMessage);
        paramMap.put("contact", userInfo);
        paramMap.put("type", "0");

        NetHelper.post(URLConstant.QUESTTION_FEEDBACK_MESSAGE, paramMap, new IRequestCallback<String>() {

            private String ret_msg;

            @Override
            public void onStart() {
                mDialogLoading.show();
            }

            @Override
            public void onFailure(Exception e) {
                mDialogLoading.dismiss();
            }

            @Override
            public void onSuccess(String jsonStr) {
                mDialogLoading.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    int status = jsonObject.optInt(Const.JSON_KEY_status, -1);
                    ret_msg = jsonObject.optString("ret_msg");
                    if (status == 0) {
                        showHintMessage();
                    } else {
                        this.onFailure(null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    /** 反馈成功提示 */
    private void showHintMessage() {
        View view = View.inflate(this, R.layout.activity_feedback_success, null);
        ImageView mCertain = (ImageView) view.findViewById(R.id.iv_feedback_success_certain);
        mCertain.setOnClickListener(this);
        new CustomDialog.AlertBuilder(mActivity)
                .setCanceledOnTouchOutside(false)
                .setCustomContentView(view)
                .setDialogRectangle(true)
                .setTitleLayoutVisibility(View.GONE)
                .create()
                .show();
    }

}
