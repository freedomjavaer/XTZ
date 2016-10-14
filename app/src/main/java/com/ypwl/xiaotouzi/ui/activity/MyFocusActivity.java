package com.ypwl.xiaotouzi.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.XtzApp;
import com.ypwl.xiaotouzi.adapter.MyFocusAdapter;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.bean.CommonBean;
import com.ypwl.xiaotouzi.bean.MyFocusBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.event.IsCollectEvent;
import com.ypwl.xiaotouzi.event.JiZhangChooseEvent;
import com.ypwl.xiaotouzi.event.PlatformCompareRefreshEvent;
import com.ypwl.xiaotouzi.interf.INetRequestListener;
import com.ypwl.xiaotouzi.manager.EventHelper;
import com.ypwl.xiaotouzi.manager.PlatformCompareManager;
import com.ypwl.xiaotouzi.manager.db.CollectDbOpenHelper;
import com.ypwl.xiaotouzi.netprotocol.MyHasFollowedProtocol;
import com.ypwl.xiaotouzi.netprotocol.PlatformCancelFollowProtocol;
import com.ypwl.xiaotouzi.utils.NetworkUtils;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.ViewUtil;
import com.ypwl.xiaotouzi.view.dialog.KProgressHUDHelper;
import com.ypwl.xiaotouzi.view.dialog.lib.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的关注页面
 */
public class MyFocusActivity extends BaseActivity implements View.OnClickListener, MyFocusAdapter.OnAbolishFollowPlatformListener{

    private View mLayoutNoDataView;
//    private SwipeRefreshLayout mLayoutContent;
    private LinearLayout mIvBackArrow;//返回
    private TextView mTvTitle,mTvAddCompare,mTvNumber;//标题，添加对比，对比数
    private Button mBtnCancle,mBtnBegin;//取消对比,开始对比
    private FrameLayout mLayout;//底部按钮
    private ListView mLv;
    private LinearLayout mRlNoinfo;//无关注
    private MyFocusAdapter mAdapter;
    private int number;//选中对比平台的数量
    private int index;
    private List<String> pids;//记录初始已选择的对比数据
    private List<MyFocusBean.FocusEntity> datas;
    public static boolean addCompared = false;//是否点击加入对比
    private CollectDbOpenHelper.CollectDbHelper mCollectDbHelper;//关注数据库帮助类
    private String mLastPageName;
    private MyHasFollowedProtocol mHasFollowedProtocol;
    private PlatformCancelFollowProtocol mCancelFollowProtocol;
    private KProgressHUD mPbLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_focus);
        mHasFollowedProtocol = new MyHasFollowedProtocol();
        mLastPageName = getIntent().getStringExtra(Const.KEY_INTENT_JUMP_FROM_DATA);
        mPbLoading = KProgressHUDHelper.createLoading(this);
        //实例化数据库帮助类
        mCollectDbHelper = CollectDbOpenHelper.CollectDbHelper.getInstance(mActivity);
        //初始化
        initView();
        initData();
        initListener();
    }

    private void initView() {
        mLayoutNoDataView = findViewById(R.id.layout_no_data_view);
//        mLayoutContent = (SwipeRefreshLayout) findViewById(R.id.layout_content);
//        mLayoutContent.setOnRefreshListener(this);
        mIvBackArrow = (LinearLayout) findViewById(R.id.layout_title_back);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvAddCompare = (TextView) findViewById(R.id.tv_title_txt_right);
        mLv = (ListView) findViewById(R.id.sv_my_focus);
        mLv.setDivider(getResources().getDrawable(R.mipmap.line_void));
        mBtnBegin = (Button) findViewById(R.id.btn_begin);
        mBtnCancle = (Button) findViewById(R.id.btn_cancle);
        mLayout = (FrameLayout) findViewById(R.id.layout_focus);
        mRlNoinfo = (LinearLayout) findViewById(R.id.rl_focus_no_info);
        mTvNumber = (TextView) findViewById(R.id.tv_number);
        ViewUtil.showContentLayout(Const.LAYOUT_LOADING, mLayoutNoDataView, mLv);//默认显示加载中视图
        mTvTitle.setText(getString(R.string.my_follow));
        mTvAddCompare.setText(getString(R.string.add_to_compare));

        if (!TextUtils.isEmpty(mLastPageName)){
            ((TextView)findView(R.id.tv_title_back)).setText(mLastPageName);
        }
    }

    private void initListener() {
        mIvBackArrow.setOnClickListener(this);
        mTvAddCompare.setOnClickListener(this);
        mBtnCancle.setOnClickListener(this);
        mBtnBegin.setOnClickListener(this);
        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (addCompared) {//点击了加入对比,响应是否勾选
                    //根据选择更新对比平台id集合
                    if (datas.get(position).isSelected()) {//取消
                        number--;
                        PlatformCompareManager.getList().remove(datas.get(position).getPid());
                        mTvNumber.setText(number + "");
                    } else {
                        if (number > 4) {
                            UIUtil.showToastShort("最多选择5个平台");
                            return;
                        }
                        number++;
                        PlatformCompareManager.getList().add(datas.get(position).getPid());
                        mTvNumber.setText(number + "");
                    }
                    datas.get(position).setSelected(!datas.get(position).isSelected());
                    mAdapter.notifyDataSetChanged();//更新ui
                    mTvNumber.setVisibility(number > 0 ? View.VISIBLE : View.GONE);
                }else if(Const.PLATFORM_CHOOSE_REQUEST_FROM == 0){//来源于记账
                    MyFocusBean.FocusEntity entity = datas.get(position);
                    EventHelper.post(new JiZhangChooseEvent(true, entity.getP_name(), entity.getPid(), "0"));
                    finish();
                }else if(Const.PLATFORM_CHOOSE_REQUEST_FROM == 1){//来源平台对比
                    if (number > 4) {
                        UIUtil.showToastShort("最多选择5个平台");
                        return;
                    }
                    if(PlatformCompareManager.getList().contains(datas.get(position).getPid())){
                        UIUtil.showToastShort(datas.get(position).getP_name()+"已存在");
                        return;
                    }
                    PlatformCompareManager.getList().add(datas.get(position).getPid());//加入进对比集合
                    EventHelper.post(new PlatformCompareRefreshEvent(true, 1));
                    XtzApp.getApplication().finishActivity(PlatformChooseActivity.class);
                    finish();
                }else{//没记账和平台对比进入，点击进入平台详情
                    Intent intent = new Intent(MyFocusActivity.this,PlatformDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("p_name",datas.get(position).getP_name());
                    bundle.putString("pid",datas.get(position).getPid());
                    intent.putExtras(bundle);
                    mActivity.startActivity(intent);
                }
            }
        });
    }

    /** 获取已关注平台数据 */
    private void initData() {
        pids = new ArrayList<String>();
        pids.addAll(PlatformCompareManager.getList());
        number = pids.size();
        //获取数据
        if(datas==null){
            datas = new ArrayList<MyFocusBean.FocusEntity>();
        }
        mHasFollowedProtocol.loadData(mHasFollowedListener,Const.REQUEST_GET);
    }

    private INetRequestListener mHasFollowedListener = new INetRequestListener<MyFocusBean>() {
        @Override
        public void netRequestCompleted() {
            if(!NetworkUtils.isNetworkConnected(UIUtil.getContext())){
                ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mLayoutNoDataView, mLv, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        initData();
                    }
                });
            }
        }

        @Override
        public void netRequestSuccess(MyFocusBean bean, boolean isSuccess) {
            if(bean!=null && isSuccess){
                datas = bean.getList();
                showData();
            }else{
                ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mLayoutNoDataView, mLv, "获取数据失败\n点击重试", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        initData();
                    }
                });
            }
        }
    };

    /** 取消关注一个平台 */
    @Override
    public void onAbolishFollow(String pid, int position) {
        index = position;
        if(mCancelFollowProtocol==null){
            mCancelFollowProtocol = new PlatformCancelFollowProtocol();
        }
        mPbLoading.show();
        mCancelFollowProtocol.loadData(pid, mCancelFollowListener);
    }

    private INetRequestListener mCancelFollowListener = new INetRequestListener<CommonBean>() {
        @Override
        public void netRequestCompleted() {
            mPbLoading.dismiss();
        }

        @Override
        public void netRequestSuccess(CommonBean bean, boolean isSuccess) {
            if(bean!=null && isSuccess){
                //取消关注成功
                mCollectDbHelper.deleteByPid(datas.get(index).getPid());
                datas.remove(index);
                mAdapter.notifyDataSetChanged();
                if(datas.size()==0){
                    mRlNoinfo.setVisibility(datas.size() == 0 ? View.VISIBLE : View.GONE);
                    mLv.setVisibility(datas.size() == 0 ? View.GONE : View.VISIBLE);
                }
                EventHelper.post(new PlatformCompareRefreshEvent(true, 0));
                EventHelper.post(new IsCollectEvent());
            }else{
                UIUtil.showToastShort("操作失败");
            }
        }
    };

    /**
     * 展示数据
     */
    private void showData() {
        ViewUtil.showContentLayout(Const.LAYOUT_DATA, mLayoutNoDataView, mLv);
        mTvNumber.setText(number + "");
        mTvNumber.setVisibility(number > 0 ? View.VISIBLE : View.GONE);
        mRlNoinfo.setVisibility(datas.size() == 0 ? View.VISIBLE : View.GONE);
        mLv.setVisibility(datas.size() == 0 ? View.GONE : View.VISIBLE);
        //通过记账和平台对比选择平台进入的关注页面不显示加入对比
        if(Const.PLATFORM_CHOOSE_REQUEST_FROM == 0 || Const.PLATFORM_CHOOSE_REQUEST_FROM==1) {
            mTvAddCompare.setVisibility(View.GONE);
        }else{
            mTvAddCompare.setVisibility(datas.size() >0?View.VISIBLE:View.GONE);
        }
        //初始化是否已添加到对比集合状态
        if(mAdapter==null){
            mAdapter = new MyFocusAdapter(datas, MyFocusActivity.this);
            mLv.setAdapter(mAdapter);
        }else{
            mAdapter.setData(datas);
            mAdapter.notifyDataSetChanged();
        }
        mAdapter.setOnAbolishFollowPlatformListener(this);//设置取消关注监听器
    }

    //点击事件处理
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_title_back://返回
                addCompared = false;
                finish();
                break;
            case R.id.tv_title_txt_right://加入对比
                if (addCompared || datas.size() == 0) {
                    return;
                }
                addCompared = true;
                mTvAddCompare.setVisibility(View.GONE);
                mTvNumber.setVisibility(number>0?View.VISIBLE:View.GONE);
                mLayout.setVisibility(View.VISIBLE);//显示底部按钮
                for (int i = 0; i < datas.size(); i++) {
                    datas.get(i).setSelected(PlatformCompareManager.getList().contains(datas.get(i).getPid()));
                }
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_begin://进入对比页面
                if (number == 0) {
                    UIUtil.showToastShort(getString(R.string.please_choose_platform));
                    return;
                } else if (number > 5) {
                    return;
                }
                for (int i = 0; i < datas.size(); i++) {
                    if (datas.get(i).isSelected()) {
                        if (!PlatformCompareManager.getList().contains(datas.get(i).getPid()))
                            PlatformCompareManager.getList().add(datas.get(i).getPid());
                    }
                }
                pids.clear();
                Intent intent = new Intent(this,PlatformCompareActivity.class);
                intent.putExtra(Const.KEY_INTENT_JUMP_FROM_DATA,"更多");
                startActivity(intent);
                finish();
                addCompared = false;
                break;
            case R.id.btn_cancle://取消
                //恢复初始状态
                PlatformCompareManager.getList().clear();
                PlatformCompareManager.getList().addAll(pids);
                //选择集合和选择数回复初始状态
                number = PlatformCompareManager.getList().size();
                mTvNumber.setText(number + "");
                addCompared = false;
                mTvAddCompare.setVisibility(View.VISIBLE);
                //更新ui
                mLayout.setVisibility(View.GONE);
                mAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        addCompared = false;
    }
}
