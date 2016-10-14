package com.ypwl.xiaotouzi.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.adapter.ContinualTallySelectPlatformAdapter;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.bean.ContinualTallyForFilterSelectPlatformBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.common.ServerStatus;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.event.ContinualTallyForFilterSelectPlatformEvent;
import com.ypwl.xiaotouzi.interf.IRequestCallback;
import com.ypwl.xiaotouzi.manager.EventHelper;
import com.ypwl.xiaotouzi.manager.net.NetHelper;
import com.ypwl.xiaotouzi.utils.CharacterParser;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.ViewUtil;
import com.ypwl.xiaotouzi.view.SideBar;

import java.util.LinkedList;
import java.util.List;


/**
 * 项目名:	XTZ
 * 包名:	com.ypwl.xiaotouzi.ui.activity
 * 类名:	ContinualTallyForFilterSelectPlatformActivity
 * 作者:	罗霄
 * 创建时间:	2016/4/11 17:07
 * <p/>
 * 描述:	流水资产 ==> 筛选 ==> 选择平台
 * <p/>
 * svn版本:	$Revision: 14605 $
 * 更新人:	$Author: luoxiao $
 * 更新时间:	$Date: 2016-05-04 17:36:41 +0800 (周三, 04 五月 2016) $
 * 更新描述:	${TODO}
 */
public class ContinualTallyForFilterSelectPlatformActivity extends BaseActivity implements View.OnClickListener, SideBar.OnTouchingLetterChangedListener, AdapterView.OnItemClickListener {

    private LinearLayout mLlBack;
    private TextView mTvTitleLeft;
    private TextView mTvTitleContent;
    private ListView mLvPlatform;
    private SideBar mSbLetters;
    private TextView mTvShowLetter;

    private View mNoDataView;

    /** 首字母消失 */
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mTvShowLetter.setVisibility(View.GONE);
        }
    };
    private ContinualTallySelectPlatformAdapter continualTallySelectPlatformAdapter;
    private String mDefPid;
    private CharacterParser characterParser;

    /** 首字母容器 */
    private LinkedList<String> mLetterList;

    /** 自定义字母表 */
    private final String[] mArraysLetter = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};
    private FrameLayout mFlContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_continual_tally_for_filter_select_platform);

        mDefPid = getIntent().getStringExtra(Const.KEY_INTENT_JUMP_BASE_DATA);
        if (TextUtils.isEmpty(mDefPid)) {
            mDefPid = "0";
        }

        initView();

        initListener();

        initData();
    }

    private void initData() {

        characterParser = CharacterParser.getInstance();

        mLetterList = new LinkedList<>();

        mTvTitleLeft.setText(UIUtil.getString(R.string.continual_tally_for_filter_title_content));
        mTvTitleContent.setText(UIUtil.getString(R.string.continual_tally_for_filter_select_platform));

        continualTallySelectPlatformAdapter = new ContinualTallySelectPlatformAdapter(mActivity);
        mLvPlatform.setAdapter(continualTallySelectPlatformAdapter);

        getData();
    }

    private void getData() {
        String url = String.format(URLConstant.CONTINUAL_TALLY_PLATFORM_LIST, GlobalUtils.token);

        NetHelper.get(url, new IRequestCallback<String>() {
            @Override
            public void onStart() {
                ViewUtil.showContentLayout(Const.LAYOUT_LOADING, mNoDataView, mFlContainer);
            }

            @Override
            public void onFailure(Exception e) {
                ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mNoDataView, mFlContainer, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getData();
                    }
                });
            }

            @Override
            public void onSuccess(String jsonStr) {
                ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mNoDataView, mFlContainer, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getData();
                    }
                });
                try {
                    JSONObject jsonObject = JSON.parseObject(jsonStr);
                    int status = jsonObject.getIntValue(Const.JSON_KEY_status);
                    String ret_msg = jsonObject.getString("ret_msg");

                    switch (status) {
                        case ServerStatus.SERVER_STATUS_OK:

                            fillData(JSON.parseArray(jsonObject.getString("list"), ContinualTallyForFilterSelectPlatformBean.class));

                            break;
                        default:
                            UIUtil.showToastShort("" + ret_msg);
                            break;
                    }

                } catch (JSONException e) {
                    this.onFailure(e);
                }
            }
        });
    }

    private void fillData(List<ContinualTallyForFilterSelectPlatformBean> mData) {
        if (null != mData && mData.size() != 0) {

            mData = sortAndInitLetterList(mData);

            continualTallySelectPlatformAdapter.notifyDataSetChanged(mData, mDefPid);
            mLvPlatform.setVisibility(View.VISIBLE);
            mSbLetters.setVisibility(View.VISIBLE);

            ViewUtil.showContentLayout(Const.LAYOUT_DATA, mNoDataView, mFlContainer);
        }else {
            ViewUtil.showContentLayout(Const.LAYOUT_EMPTY, mNoDataView, mFlContainer, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getData();
                }
            });
        }
    }

    /** 按首字母排序 并 初始化字母列表 */
    private List<ContinualTallyForFilterSelectPlatformBean> sortAndInitLetterList(List<ContinualTallyForFilterSelectPlatformBean> mData) {

        for (int i = 0; i < mData.size(); i++) {
            try {
                ContinualTallyForFilterSelectPlatformBean continualTallyForFilterSelectPlatformBean = mData.get(i);
                String pinyin = characterParser.getSelling(continualTallyForFilterSelectPlatformBean.getP_name());
                String sortString = pinyin.substring(0, 1).toUpperCase();
                if (sortString.matches("[A-Z]")) {
                    continualTallyForFilterSelectPlatformBean.setInitial(sortString.toUpperCase());
                } else {
                    continualTallyForFilterSelectPlatformBean.setInitial("#");
                }

                if (!mLetterList.contains(continualTallyForFilterSelectPlatformBean.getInitial())) {
                    equalsPositionForLetter(mLetterList, continualTallyForFilterSelectPlatformBean.getInitial(), mLetterList.size() - 1);
                }
            } catch (Exception e) {
                continue;
            }
        }

        //item 排序
        mData = sortItem(mData);

        //填充字母索引
        mSbLetters.setText(mLetterList.toArray(new String[mLetterList.size()]));
        return mData;
    }

    /** 比较字母索引顺序 并 添加到容器 */
    private void equalsPositionForLetter(LinkedList<String> container, String positionLetter, int position) {
        if (position < 0) {
            container.add(0, positionLetter);
        } else if (getSortPosition(positionLetter) >= getSortPosition(container.get(position))) {
            container.add(position + 1, positionLetter);
        } else {
            equalsPositionForLetter(container, positionLetter, position - 1);
        }
    }

    private List<ContinualTallyForFilterSelectPlatformBean> sortItem(List<ContinualTallyForFilterSelectPlatformBean> mData) {
        LinkedList<ContinualTallyForFilterSelectPlatformBean> container = new LinkedList<>();
        for (int i = 0; i < mData.size(); i++) {
            ContinualTallyForFilterSelectPlatformBean continualTallyForFilterSelectPlatformBean = mData.get(i);
            if (i == 0) {
                container.add(continualTallyForFilterSelectPlatformBean);
            } else {
                equalsPositionForLetter(container, continualTallyForFilterSelectPlatformBean, container.size() - 1);
            }
        }
        return container;
    }

    private void equalsPositionForLetter(LinkedList<ContinualTallyForFilterSelectPlatformBean> container, ContinualTallyForFilterSelectPlatformBean mBean, int position) {
        if (position < 0) {
            container.add(0, mBean);
        } else if (getSortPosition(mBean.getInitial()) >= getSortPosition(container.get(position).getInitial())) {
            container.add(position + 1, mBean);
        } else {
            equalsPositionForLetter(container, mBean, position - 1);
        }
    }

    /** 获取字符串在自定义字母表中的位置 */
    private int getSortPosition(String letter) {
        int result = -1;
        if (!TextUtils.isEmpty(letter)) {
            for (int i = 0; i < mArraysLetter.length; i++) {
                if (letter.equals(mArraysLetter[i])) {
                    result = i + 1;
                    break;
                }
            }
        }
        return result;
    }

    private void initListener() {
        mLlBack.setOnClickListener(this);
        mSbLetters.setOnTouchingLetterChangedListener(this);
    }

    private void initView() {
        mLlBack = findView(R.id.layout_title_back);
        mTvTitleLeft = findView(R.id.tv_title_back);
        mTvTitleContent = findView(R.id.tv_title);

        mLvPlatform = findView(R.id.activity_continual_tally_for_filter_select_platform_lv_list);
        mSbLetters = findView(R.id.activity_continual_tally_for_filter_select_platform_sb_letters);
        mTvShowLetter = findView(R.id.activity_continual_tally_for_filter_select_platform_tv_show_letter);

        mLvPlatform.setOnItemClickListener(this);
        mLvPlatform.setVisibility(View.INVISIBLE);
        mSbLetters.setVisibility(View.INVISIBLE);

        mSbLetters.setColor(UIUtil.getColor(R.color.continual_tally_filter_select_platform_letter));

        mFlContainer = findView(R.id.activity_continual_tally_for_filter_select_platform_fl_container);
        mNoDataView = findView(R.id.layout_no_data_view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_title_back:
                finish();
                break;
        }
    }

    @Override
    public void onTouchingLetterChanged(String s) {
        int position = continualTallySelectPlatformAdapter.getPositionForSelected(s.substring(0, 1));
        if (position != -1) {
            mLvPlatform.setSelection(position);
        }
        mTvShowLetter.setText(s);
        if (mTvShowLetter.getVisibility() == View.VISIBLE) {
            UIUtil.removeCallbacksFromMainLooper(mRunnable);
        }
        UIUtil.postDelayed(mRunnable, 600);//延迟消失
        mTvShowLetter.setVisibility(View.VISIBLE);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        EventHelper.post(new ContinualTallyForFilterSelectPlatformEvent(continualTallySelectPlatformAdapter.getBeanForPosition(position)));
        finish();
    }
}
