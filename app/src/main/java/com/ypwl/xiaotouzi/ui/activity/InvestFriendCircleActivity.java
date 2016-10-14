package com.ypwl.xiaotouzi.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.amap.api.location.AMapLocation;
import com.squareup.otto.Subscribe;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.adapter.InvestFriendCircleFiterAdapter;
import com.ypwl.xiaotouzi.adapter.InvestFriendCircleMessageAdapter;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.bean.LoginBean;
import com.ypwl.xiaotouzi.bean.OnlineMsgBean;
import com.ypwl.xiaotouzi.bean.OnlineSendMsgBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.event.OnlineAtOneEvent;
import com.ypwl.xiaotouzi.im.message.YMessageAgent;
import com.ypwl.xiaotouzi.manager.ConstInstanceHelper;
import com.ypwl.xiaotouzi.manager.LocationManager;
import com.ypwl.xiaotouzi.utils.CacheUtils;
import com.ypwl.xiaotouzi.utils.DateTimeUtil;
import com.ypwl.xiaotouzi.utils.LogUtil;
import com.ypwl.xiaotouzi.utils.SoftInputUtil;
import com.ypwl.xiaotouzi.utils.StringUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.Util;
import com.ypwl.xiaotouzi.view.DropMenu;
import com.ypwl.xiaotouzi.view.dialog.CustomDialog;
import com.ypwl.xiaotouzi.view.dialog.KProgressHUDHelper;
import com.ypwl.xiaotouzi.view.dialog.lib.KProgressHUD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * function:投友圈
 *
 * <p>Created by lzj on 2016/3/17.</p>
 */
@SuppressWarnings("FieldCanBeLocal")
public class InvestFriendCircleActivity extends BaseActivity implements View.OnClickListener {

    private List<View> mPopupViews = new ArrayList<>();
    private String mFilterTabs[] = {"定位中"};
    private String mFilterItems[] = {"深圳", UIUtil.getString(R.string.samecity_online_title_myword), UIUtil.getString(R.string.samecity_online_title_ame)};
    /** 当前筛选项条目的位置 默认第一条 */
    private int mCurrentFilterPosition = 0;

    private View mContentView;
    private DropMenu mDropMenu;
    private View mContentMaskView;
    private ProgressBar mLoadingMore;
    private RecyclerView mMsgRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private ImageView mFab2Bottom;
    private EditText mInputTextUI;
    private KProgressHUD mLoading;
    private YMessageAgent mYMessageAgent;
    private InvestFriendCircleMessageAdapter mAdapter;
    private LoginBean mLoginBean;
    private String mNsp;
    private boolean mIsLoading = false;
    /** 消息列表第一条消息的时间戳 */
    private long mFirstMsgTime = -1;

    private static Map<String, String> mCitiesMap = new LinkedHashMap<>();

    static {
        mCitiesMap.put("/beijing", "北京");
        mCitiesMap.put("/shanghai", "上海");
        mCitiesMap.put("/guangzhou", "广州");
        mCitiesMap.put("/shenzhen", "深圳");
    }

    @Override
    protected boolean enableDispatchTouchEventOnSoftKeyboard() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoginBean = Util.legalLogin();
        //要想来聊天，userid是必备条件
        if (mLoginBean == null || StringUtil.isEmptyOrNull(mLoginBean.getUserid())) {
            startActivity(LoginActivity.class);
            finish();
            return;
        }
        setContentView(R.layout.activity_samecity_online_header);
        mContentView = View.inflate(this, R.layout.activity_samecity_online_content, null);
        mLoading = KProgressHUDHelper.createLoading(this).setDimAmount(0f).show();
        initView();
        initData();
    }

    private void initData() {
        //TODO 保留功能：复原上次输入的内容
//        if (!StringUtil.isEmptyOrNull(ConstInstanceHelper.tempChatMsg)) {
//            mInputTextUI.setText(ConstInstanceHelper.tempChatMsg);
//        }

        //定位进入聊天室,定位一次
        String localCacheNsp = CacheUtils.getString(Const.KEY_NSP_CHAT, null);
        if (localCacheNsp != null && localCacheNsp.length() > 0) {
            String cityName = mCitiesMap.get(localCacheNsp);
            initDataBeforeIntoChatRoom(localCacheNsp, cityName);
            return;
        }
        LocationManager.startLocation(new LocationManager.ILocationCallback() {

            @Override
            public void onStart() {
                mLoading.show();
            }

            @Override
            public void onError() {

            }

            @Override
            public void onComplete(AMapLocation amapLocation) {
                String city = amapLocation.getCity();
                LogUtil.e(TAG, "location : city=" + city);
                if (isFinishing()) {
                    return;
                }
                String nsp = null;
                String cityName = null;
                for (Map.Entry<String, String> entry : mCitiesMap.entrySet()) {
                    String value = entry.getValue();
                    if (city.contains(value)) {
                        nsp = entry.getKey();
                        cityName = entry.getValue();
                        break;
                    }
                }
                if (nsp != null && cityName != null) {
                    initDataBeforeIntoChatRoom(nsp, cityName);
                } else {
                    chooseCityForChat();
                }
            }
        });
    }

    /** 弹出对话框选择城市 */
    private void chooseCityForChat() {
        if (isFinishing()) {
            return;
        }
        mLoading.dismiss();
        final View contentView = UIUtil.inflate(R.layout.dialog_content_samecity_online_choosecity);
        final CustomDialog dialog = new CustomDialog.AlertBuilder(this)
                .setTitleLayoutVisibility(View.GONE).setCustomContentView(contentView)
                .setCanceled(false).setCanceledOnTouchOutside(false).create();
        dialog.show();
        final RadioGroup rgCities = findView(contentView, R.id.rg_cities);
        findView(contentView, R.id.btn_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton checkedRadio = findView(contentView, rgCities.getCheckedRadioButtonId());
                if (rgCities.getCheckedRadioButtonId() == -1) {
                    findView(contentView, R.id.choose_city_hint).startAnimation(AnimationUtils.loadAnimation(v.getContext(), R.anim.shake));
                    return;
                }
                String cityName = checkedRadio.getText().toString().trim();
                String nsp = null;
                for (Map.Entry<String, String> entry : mCitiesMap.entrySet()) {
                    if (entry.getValue().equals(cityName)) {
                        nsp = entry.getKey();
                        break;
                    }
                }
                dialog.dismiss();
                mLoading.show();
                initDataBeforeIntoChatRoom(nsp, cityName);
            }
        });
    }

    private void initDataBeforeIntoChatRoom(String nsp, String cityName) {
        LogUtil.e(TAG, "initDataBeforeIntoChatRoom : nsp=" + nsp + " ,cityName=" + cityName);
        if (isFinishing()) {
            return;
        }
        mNsp = nsp;
        mFilterItems[0] = cityName;
        CacheUtils.putString(Const.KEY_NSP_CHAT, nsp);
        //title
        ListView cityView = new ListView(this);
        cityView.setDivider(null);
        cityView.setDividerHeight(0);
        //TODO:BUG
        cityView.setBackgroundDrawable(UIUtil.getDrawable(R.drawable.bg_dropmenu));
        cityView.setAdapter(new InvestFriendCircleFiterAdapter(mActivity, Arrays.asList(mFilterItems)));
        cityView.setOnItemClickListener(mFilterItemClickListener);
        mPopupViews.add(cityView);
        mFilterTabs[0] = mFilterItems[0];
        mDropMenu.setDropMenu(Arrays.asList(mFilterTabs), mPopupViews, mContentView);
        mDropMenu.setCancleOutside(true);

        //content
        mYMessageAgent = YMessageAgent.getInstance();
        mYMessageAgent.setMessageCallBack(mIMessageCallBack);
        mYMessageAgent.setNspAndEnable(mNsp);
    }

    private void initView() {
        //title
        findView(R.id.layout_title_back_chat).setOnClickListener(this);
        mDropMenu = findView(R.id.dm);
        //contetn
        mContentMaskView = mContentView.findViewById(R.id.layout_content_mask);
        mLoadingMore = (ProgressBar) mContentView.findViewById(R.id.pb_more_loading);
        mMsgRecyclerView = (RecyclerView) mContentView.findViewById(R.id.rv_messages);
        mAdapter = new InvestFriendCircleMessageAdapter(this);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mMsgRecyclerView.setLayoutManager(mLinearLayoutManager);
        mMsgRecyclerView.setAdapter(mAdapter);
        mMsgRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                SoftInputUtil.hideSoftInput(v);
                return false;
            }
        });
        mMsgRecyclerView.addOnScrollListener(mOnScrollListener);
        mFab2Bottom = (ImageView) mContentView.findViewById(R.id.fab);
        mFab2Bottom.setOnClickListener(this);
        mInputTextUI = (EditText) mContentView.findViewById(R.id.layout_input_text);
        mInputTextUI.addTextChangedListener(mInputMsgTextWatcher);
        mContentView.findViewById(R.id.layout_input_sendbtn).setOnClickListener(this);
    }

    /** 列表滚动监听 */
    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (mAdapter.getItemCount() - 1 == mLinearLayoutManager.findLastVisibleItemPosition()) {
                mFab2Bottom.setVisibility(View.GONE);
                return;
            } else {
                mFab2Bottom.setVisibility(View.VISIBLE);
            }
            if (mIsLoading) {
                return;
            }
            if (mLinearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                requestLoadMore();
            }
        }
    };

    /** 请求加载更多数据 */
    private void requestLoadMore() {
        LogUtil.e(TAG, "requestLoadMore");
        mContentMaskView.setVisibility(View.VISIBLE);
        mLoadingMore.setVisibility(View.VISIBLE);
        UIUtil.postDelayed(new Runnable() {
            @Override
            public void run() {
                requestCategoryMsg();
            }
        }, 200);
    }

    /** 临时存储消息容器 */
    private List<OnlineMsgBean.MsgEntity> mTempMsgList = new ArrayList<>();
    /** 消息类型--接收服务端下发消息 */
    private static final int MSG_TYPE_onReceiveMsg = 0;
    /** 消息类型--获取所有消息 */
    private static final int MSG_TYPE_onGetAllMsg = 1;
    /** 消息类型--获取我的发言 */
    private static final int MSG_TYPE_onGetMyMsg = 2;
    /** 消息类型--获取@我的消息 */
    private static final int MSG_TYPE_onGetAmeMsg = 3;

    /** 消息回调 */
    private YMessageAgent.IMessageCallBack mIMessageCallBack = new YMessageAgent.IMessageCallBack() {

        @Override
        public void connectError() {
//            UIUtil.showToastShort("连接出错！");
        }

        @Override
        public void loginChatRoomFailed() {
//            UIUtil.showToastShort("登录聊天室失败");
        }

        @Override
        public void loginChatroomSuccess() {
            mLoading.dismiss();
            mFirstMsgTime = DateTimeUtil.getCurrentDateTimeSeconds();
            requestCategoryMsg();//进入聊天室拉取所有数据
        }

        @Override
        public void onReceiveMsg(OnlineMsgBean.MsgEntity msgEntity) {
            if (mCurrentFilterPosition == 1 || mCurrentFilterPosition == 2) {//当前页面不在定位城市聊天
                return;
            }
            if (msgEntity != null) {
                mTempMsgList.clear();
                mTempMsgList.add(msgEntity);
                loadDataIntoAdapter(mTempMsgList, MSG_TYPE_onReceiveMsg);
                //TODO 隐藏该功能：收到消息会有声音和震动提示
//                if (msgEntity.getFromme() == 0) {
//                    MsgHintHelper.hintMsgCome(mActivity);
//                }
            }
        }

        @Override
        public void onGetAllMsg(List<OnlineMsgBean.MsgEntity> msgEntityList) {
            LogUtil.e(TAG, "onGetAllMsg : " + (msgEntityList == null ? "null" : msgEntityList.size()));
            handleDataForAdapter(msgEntityList, MSG_TYPE_onGetAllMsg);
        }

        @Override
        public void onGetMyMsg(List<OnlineMsgBean.MsgEntity> msgEntityList) {
            LogUtil.e(TAG, "onGetMyMsg : " + (msgEntityList == null ? "null" : msgEntityList.size()));
            handleDataForAdapter(msgEntityList, MSG_TYPE_onGetMyMsg);
        }

        @Override
        public void onGetAmeMsg(List<OnlineMsgBean.MsgEntity> msgEntityList) {
            LogUtil.e(TAG, "onGetAmeMsg : " + (msgEntityList == null ? "null" : msgEntityList.size()));
            handleDataForAdapter(msgEntityList, MSG_TYPE_onGetAmeMsg);
        }
    };

    /** 处理首次加载还是分页查询的数据 */
    private void handleDataForAdapter(List<OnlineMsgBean.MsgEntity> msgEntityList, int msgType) {
        if (mLoadingMore.getVisibility() == View.VISIBLE) {//分页加载更多
            loadMoreDataIntoAdapter(msgEntityList);
        } else {//第一次加载
            loadDataIntoAdapter(msgEntityList, msgType);
        }
        OnlineMsgBean.MsgEntity firstMsgInList = mAdapter.getFirstMsgInList();
        if (firstMsgInList != null) {
            mFirstMsgTime = firstMsgInList.getSendtime();
        }
        mIsLoading = false;
        mLoadingMore.setVisibility(View.GONE);
        mContentMaskView.setVisibility(View.GONE);
    }

    /** 加载分页数据进入适配器 */
    private void loadMoreDataIntoAdapter(List<OnlineMsgBean.MsgEntity> msgEntityList) {
        if (msgEntityList != null && msgEntityList.size() > 0) {
            int loadAgainNum = mAdapter.loadMoreData(msgEntityList);
            mMsgRecyclerView.smoothScrollToPosition(loadAgainNum - 1);
        }
    }

    /** 加载数据进入适配器 */
    private void loadDataIntoAdapter(List<OnlineMsgBean.MsgEntity> msgEntityList, int msgType) {
        if (msgType == MSG_TYPE_onReceiveMsg) {
            mAdapter.refreshData(msgEntityList);
            if (mAdapter.getItemCount() > 0) {
                mMsgRecyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);
            }
        } else {
            mAdapter.clearDataList();
            if (msgEntityList != null && msgEntityList.size() > 0) {
                mAdapter.refreshData(msgEntityList);
                if (mAdapter.getItemCount() > 0) {
                    mMsgRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
                }
            }
        }
    }

    /** 筛选条目点击事件 */
    private AdapterView.OnItemClickListener mFilterItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            SoftInputUtil.hideSoftInput(view);
            if (mIsLoading) {
                return;
            }
            mDropMenu.setTabText(mFilterItems[position]);
            mDropMenu.closeMenu();
            if (mCurrentFilterPosition == position) {
                return;
            }
            mCurrentFilterPosition = position;
            mFirstMsgTime = DateTimeUtil.getCurrentDateTimeSeconds();
            requestCategoryMsg();
        }

    };

    /** 请求分类消息 */
    private void requestCategoryMsg() {
        if (mIsLoading || mFirstMsgTime == -1) {
            return;
        }
        mIsLoading = true;
        mYMessageAgent.requestCategoryMsg(mCurrentFilterPosition + 1, mFirstMsgTime);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_title_back_chat://back
                SoftInputUtil.hideSoftInput(v);
                this.onBackPressed();
                break;
            case R.id.fab:// scroll to bottom
                if (mAdapter.getItemCount() > 0) {
                    mMsgRecyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);
                }
                break;
            case R.id.layout_input_sendbtn://  send chat message
                sendMyWord();
                break;
        }
    }

    /** 提交我的发言 */
    private void sendMyWord() {
        String content = mInputTextUI.getText().toString().trim();
        if (StringUtil.isEmptyOrNull(content)) {
            mInputTextUI.startAnimation(AnimationUtils.loadAnimation(UIUtil.getContext(), R.anim.shake));
            return;
        }
        long currentDateTimeSecond = DateTimeUtil.getCurrentDateTimeSeconds();
        OnlineSendMsgBean bean = new OnlineSendMsgBean();
        bean.setMessage(content);
        bean.setSendtime(currentDateTimeSecond);
        if (atMap.size() > 0) {
            List<OnlineSendMsgBean.ToidEntity> toidList = new ArrayList<>();
            for (Map.Entry<String, String> entry : atMap.entrySet()) {
                OnlineSendMsgBean.ToidEntity toidEntity = new OnlineSendMsgBean.ToidEntity();
                toidEntity.setUserid(entry.getKey());
                toidEntity.setUsername(entry.getValue());
                toidList.add(toidEntity);
            }
            bean.setToid(toidList);
        }
        mYMessageAgent.sendMsg(bean);
        mInputTextUI.setText("");

        //add in adapter
        OnlineMsgBean.MsgEntity selfMsg = new OnlineMsgBean.MsgEntity();
        if (atMap.size() > 0) {
            List<OnlineMsgBean.MsgEntity.ToidEntity> toidList = new ArrayList<>();
            for (Map.Entry<String, String> entry : atMap.entrySet()) {
                OnlineMsgBean.MsgEntity.ToidEntity toidEntity = new OnlineMsgBean.MsgEntity.ToidEntity();
                toidEntity.setUserid(entry.getKey());
                toidEntity.setUsername(entry.getValue());
                toidList.add(toidEntity);
            }
            selfMsg.setToid(toidList);
        }
        atMap.clear();
        selfMsg.setFromme(1);
        selfMsg.setHeadimage(mLoginBean.getAvatarUrl());
        selfMsg.setUsername(mLoginBean.getNickname());
        selfMsg.setUserid(mLoginBean.getUserid());
        selfMsg.setMessage(content);
        selfMsg.setSendtime(currentDateTimeSecond);
        mAdapter.appendOneMsgAtLast(selfMsg);
        mMsgRecyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);
    }

    /** 消息输入监听 */
    private TextWatcher mInputMsgTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            LogUtil.e("leo", "beforeTextChanged : " + s.toString());
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            LogUtil.e("leo", "onTextChanged : " + s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {
//            LogUtil.e("leo", "afterTextChanged : " + s.toString());
            removeAtMap(s.toString());
        }
    };

    private void removeAtMap(String inputStr) {
        if (inputStr == null || inputStr.length() == 0) {
            return;
        }
        for (Map.Entry<String, String> entry : atMap.entrySet()) {
            if (!inputStr.contains(entry.getValue())) {
                atMap.remove(entry.getKey());
                break;
            }
        }
    }


    private Map<String, String> atMap = new LinkedHashMap<>();

    @Subscribe
    public void onEventWhenLongPressUserAvatar(OnlineAtOneEvent event) {
        if (atMap.size() == 3) {
            UIUtil.showToastShort("您最多只能@3个人");
            return;
        }
        if (atMap.get(event.userid) != null) {
            return;
        }
        atMap.put(event.userid, event.userName);
        mInputTextUI.getText().insert(mInputTextUI.getSelectionStart(), "@" + event.userName + " ");
    }

    @Override
    public void onBackPressed() {
        if (mDropMenu != null && mDropMenu.isShowing()) {
            mDropMenu.closeMenu();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        mPopupViews.clear();
        if (mLoading != null && mLoading.isShowing()) {
            mLoading.dismiss();
            mLoading = null;
        }
        if (mYMessageAgent != null) {
            mYMessageAgent.removeMessageCallBack();
        }
        mMsgRecyclerView.removeOnScrollListener(mOnScrollListener);
        ConstInstanceHelper.setTempChatMsg(mInputTextUI.getText().toString().trim());
        super.onDestroy();
    }

}
