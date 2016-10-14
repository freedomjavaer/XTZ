package com.ypwl.xiaotouzi.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.adapter.FundsInvestAdapter;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.bean.FundsInvestBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.interf.IRequestCallback;
import com.ypwl.xiaotouzi.manager.net.NetHelper;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.StringUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.ViewUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 基金页面
 *
 * Created by PDK on 2016/4/15.
 */
public class FundsInevstActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private TextView mTitle;
    private LinearLayout mTitleBackLayout;
    private TextView mTitleBackTv;
    private ListView mFundsListView;
    private FundsInvestBean fundsInvestBean;
    private View mNoDataView;
    private LinearLayout mDataContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_funds_invest_activity);

        initView();
        initData();
    }

    private void initData() {
        //title
        mTitle.setText("私人银行");
        mTitleBackTv.setText("金融超市");

        //content
        String url = StringUtil.format(URLConstant.FINANCE_FUNDS_INVEST, GlobalUtils.token);
        NetHelper.get(url,new MyHttpCallBack());

    }

    /**listview条目点击*/
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String url = StringUtil.format(URLConstant.FINANCE_PLATFORM_FUNDS_DETAIL,fundsInvestBean.getData().get(position).getProduct_id());
        Intent intent = new Intent(getApplicationContext(),FundsDetailActivity.class);
        intent.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA,url);
        intent.putExtra(Const.KEY_INTENT_JUMP_FROM_DATA,fundsInvestBean.getData().get(position).getProduct_name());
        startActivity(intent);
    }

    private class MyHttpCallBack extends IRequestCallback<String> {

        @Override
        public void onStart() {

        }

        @Override
        public void onFailure(Exception e) {
            ViewUtil.showContentLayout(Const.LAYOUT_ERROR,mNoDataView,mDataContainer);
        }

        @Override
        public void onSuccess(String jsonStr) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(jsonStr);
                int code = jsonObject.optInt("code");
                if (code == 20){
                    fundsInvestBean = JSON.parseObject(jsonStr, FundsInvestBean.class);
                    if (fundsInvestBean.getData().size() != 0){
                        FundsInvestAdapter mAdapter = new FundsInvestAdapter(getApplicationContext(),fundsInvestBean);
                        mFundsListView.setAdapter(mAdapter);

                        ViewUtil.showContentLayout(Const.LAYOUT_DATA,mNoDataView,mDataContainer);
                    }else {
                        ViewUtil.showContentLayout(Const.LAYOUT_EMPTY,mNoDataView,mDataContainer);
                    }

                }else {
                    UIUtil.showToastLong("网络异常");
                    ViewUtil.showContentLayout(Const.LAYOUT_ERROR,mNoDataView,mDataContainer);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    private void initView() {
        mTitle = findView(R.id.tv_title);
        mTitleBackLayout = findView(R.id.layout_title_back);
        mTitleBackTv = findView(R.id.tv_title_back);
        mFundsListView = findView(R.id.lv_funds_invest);

        mNoDataView = findView(R.id.layout_no_data_view);
        mDataContainer = findView(R.id.ll_content_container);
        ViewUtil.showContentLayout(Const.LAYOUT_LOADING,mNoDataView,mDataContainer);

        mFundsListView.setOnItemClickListener(this);
        mTitleBackLayout.setOnClickListener(this);

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
