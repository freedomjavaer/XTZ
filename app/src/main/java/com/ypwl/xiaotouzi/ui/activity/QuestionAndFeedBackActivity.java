package com.ypwl.xiaotouzi.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.adapter.QuestionAndFeedBackAdapter;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.bean.QuestionAndFeedBackBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.interf.INetRequestListener;
import com.ypwl.xiaotouzi.netprotocol.QuestionAndFeedBackProtocol;
import com.ypwl.xiaotouzi.view.dialog.KProgressHUDHelper;
import com.ypwl.xiaotouzi.view.dialog.lib.KProgressHUD;

import java.util.List;

/**
 * function : 常见问题和反馈页面
 * <p/>
 * Created by tengtao on 2016/5/3.
 */
public class QuestionAndFeedBackActivity extends BaseActivity implements View.OnClickListener {

    private QuestionAndFeedBackProtocol mQuestionAndFeedBackProtocol;
    private KProgressHUD mLoading;
    private RecyclerView mRecyclerView;
    private QuestionAndFeedBackAdapter mQuestionAndFeedBackAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_and_feedback);
        initView();
        loadData();
    }

    /**初始化view*/
    private void initView() {
        mLoading = KProgressHUDHelper.createLoading(this);
        //titile
        ((TextView)findViewById(R.id.tv_title)).setText(getResources().getString(R.string.question_and_feedback));
        ((TextView)findViewById(R.id.tv_title_back)).setText("更多");
        //content
        mRecyclerView = (RecyclerView) findViewById(R.id.question_feedback_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mQuestionAndFeedBackAdapter = new QuestionAndFeedBackAdapter(this);
        mRecyclerView.setAdapter(mQuestionAndFeedBackAdapter);

        findViewById(R.id.layout_title_back).setOnClickListener(this);
        findViewById(R.id.tv_submit_yout_opinion).setOnClickListener(this);
    }

    /**加载数据*/
    private void loadData(){
        if(mQuestionAndFeedBackProtocol==null)
            mQuestionAndFeedBackProtocol = new QuestionAndFeedBackProtocol();
        mLoading.show();
        mQuestionAndFeedBackProtocol.loadData(mQuestionAndFeedBackListener, Const.REQUEST_GET);
    }

    /** 数据请求回调 */
    private INetRequestListener mQuestionAndFeedBackListener = new INetRequestListener<QuestionAndFeedBackBean>() {
        @Override
        public void netRequestCompleted() {
            mLoading.dismiss();
        }

        @Override
        public void netRequestSuccess(QuestionAndFeedBackBean bean, boolean isSuccess) {
            if(bean!=null && isSuccess){
                List<QuestionAndFeedBackBean.ListBean> list = bean.getList();
                if(list!=null && list.size()>0)
                    mQuestionAndFeedBackAdapter.loadData(list);
            }else{
                //解析出错或者网络请求失败
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_title_back://返回
                finish();
                break;
            case R.id.tv_submit_yout_opinion://提交意见
                Intent intent = new Intent(this,UserFeedbackActivity.class);
                intent.putExtra(Const.KEY_INTENT_JUMP_FROM_DATA,getResources().getString(R.string.question_and_feedback));
                startActivity(intent);
                break;
        }
    }
}
