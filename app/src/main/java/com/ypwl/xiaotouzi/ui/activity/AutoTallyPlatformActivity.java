package com.ypwl.xiaotouzi.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.squareup.otto.Subscribe;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.adapter.AutoTallyPlatformAdapter;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.bean.AutoTallyPlatformBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.common.ServerStatus;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.event.AutoTallyPlatformRefreshEvent;
import com.ypwl.xiaotouzi.event.BindAccountEvent;
import com.ypwl.xiaotouzi.interf.IRequestCallback;
import com.ypwl.xiaotouzi.manager.net.NetHelper;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.ViewUtil;
import com.ypwl.xiaotouzi.view.SideBar;
import com.ypwl.xiaotouzi.view.dialog.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * function:自动记账页面
 * <p/>
 * Created by tengtao on 2015/12/7.
 */
public class AutoTallyPlatformActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, SideBar.OnTouchingLetterChangedListener {
    private View mNoDataView;
    private TextView mTvTopTitle,mTvLetter;
    private ListView mLvAutoTally;
    private SideBar mSideBar;
    private AutoTallyPlatformAdapter mAdapter;
    private List<AutoTallyPlatformBean.Entity> list = new ArrayList<>();
    private List<String> pLetters = new ArrayList<>();

    /** 首字母消失 */
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mTvLetter.setVisibility(View.GONE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_tally);
        initView();
        requestData();
    }

    private void initView() {
        mNoDataView = findViewById(R.id.layout_no_data_view);

        mSideBar = (SideBar) findViewById(R.id.sidebar);
        mSideBar.setOnTouchingLetterChangedListener(this);
        mTvLetter = (TextView) findViewById(R.id.tv_p_letter);
//        mSideBar.setTextView(mTvLetter);

        mTvTopTitle = (TextView) findViewById(R.id.tv_title);
        mTvTopTitle.setText("自动记账");
        findViewById(R.id.layout_title_back).setOnClickListener(this);

        mLvAutoTally = (ListView) findViewById(R.id.lv_activity_auto_tally_platform);
        mLvAutoTally.setDivider(null);
        mAdapter = new AutoTallyPlatformAdapter(mActivity);
        mLvAutoTally.setAdapter(mAdapter);
        mLvAutoTally.setOnItemClickListener(this);
        ViewUtil.showContentLayout(Const.LAYOUT_LOADING, mNoDataView, mLvAutoTally);
    }

    private void requestData(){
        String url = String.format(URLConstant.AUTO_TALLY_PLATFORM_CHOOSE, GlobalUtils.token);
        NetHelper.get(url, new AutoTallyPlatformCallBack());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if("1".equals(list.get(position).getIs_bind())){//已经绑定
            showBindedDialog();
        }else if("0".equals(list.get(position).getIs_bind())){//没有绑定
            Intent intent = new Intent(mActivity, com.ypwl.xiaotouzi.ui.activity.BindPlatformAccountActivity.class);
            intent.putExtra("pid",list.get(position).getPid());
            intent.putExtra("p_name",list.get(position).getP_name());
            startActivity(intent);
        }
    }

    /** 提示已绑定 */
    private void showBindedDialog(){
        final CustomDialog dialog = new CustomDialog.AlertBuilder(mActivity)
                .setContentText("您已经绑定该平台")
                .setContentTextGravity(Gravity.CENTER).create();
        dialog.show();
        UIUtil.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 1000);
    }

    /** 索引条字母改变监听事件 */
    @Override
    public void onTouchingLetterChanged(String s) {
        int index = pLetters.indexOf(s);
        mLvAutoTally.setSelection(index);
        mTvLetter.setText(s);
        if(mTvLetter.getVisibility() == View.VISIBLE){
            UIUtil.removeCallbacksFromMainLooper(mRunnable);
        }
        UIUtil.postDelayed(mRunnable,600);//延迟消失
        mTvLetter.setVisibility(View.VISIBLE);
    }

    private class AutoTallyPlatformCallBack extends IRequestCallback<String> {

        @Override
        public void onStart() {
        }

        @Override
        public void onFailure(Exception e) {
            ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mNoDataView, mLvAutoTally, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestData();
                }
            });
        }

        @Override
        public void onSuccess(String jsonStr) {
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);
                int status = jsonObject.getInt("status");
                if(status == ServerStatus.SERVER_STATUS_OK){//成功访问
                    AutoTallyPlatformBean bean = JSON.parseObject(jsonStr, AutoTallyPlatformBean.class);
                    list.clear();
                    list.addAll(bean.getList());
                    if(list!=null && list.size()>0){
                        pLetters.clear();
                        for(int i=0;i<list.size();i++){
                            String p_letters = list.get(i).getP_letters();
                            boolean isNum = p_letters.matches("[0-9]+");
                            pLetters.add(isNum?"#":p_letters);
                        }
                        mAdapter.updateList(list);
                        ViewUtil.showContentLayout(Const.LAYOUT_DATA,mNoDataView,mLvAutoTally);
                    }else{
                        ViewUtil.showContentLayout(Const.LAYOUT_EMPTY,mNoDataView,mLvAutoTally);
                    }
                }
            } catch (JSONException e) {
                ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mNoDataView, mLvAutoTally, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestData();
                    }
                });
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_title_back://返回我的投资页
                finish();
                break;
        }
    }


    /** 平台更新事件 */
    @Subscribe
    public void onAutoTallyPlatformRefreshEvent(AutoTallyPlatformRefreshEvent event){
        if (event != null && !isFinishing()) {
            requestData();
        }
    }

    /**绑定成功，退出页面*/
    @Subscribe
    public void onBindAccountEvent(BindAccountEvent event){
        if(event!=null && !isFinishing()){
            if(!event.isFinish()) {
                finish();//若绑定页面没有关闭，关闭页面
            }else{
                requestData();//绑定页面已关闭，则刷新页面
            }
        }
    }
}
