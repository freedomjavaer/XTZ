package com.ypwl.xiaotouzi.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.common.Const;

/**
 * 信息详情
 *
 * Created by PDK on 2016/3/28.
 * <p/>
 * Modify by lzj on 2016/4/5.
 */
public class MessageDetailActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        initView();
    }

    private void initView() {
        findView(R.id.layout_title_back).setOnClickListener(this);
        TextView mTitle = (TextView) findViewById(R.id.tv_title);
        mTitle.setText(R.string.message_detail);
        TextView backHint = findView(R.id.tv_title_back);
        backHint.setText("收件箱");
        TextView mMsgDetail = findView(R.id.tv_msg_detail);
        mMsgDetail.setText(getIntent().getStringExtra(Const.KEY_INTENT_JUMP_BASE_DATA));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_title_back:
                finish();
                break;
        }
    }
}
