package com.ypwl.xiaotouzi.ui.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.squareup.otto.Subscribe;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.XtzApp;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.bean.AppUpdateBean;
import com.ypwl.xiaotouzi.bean.HuodongInfosBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.common.ServerStatus;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.event.EnterIntoFinanceMarketEvent;
import com.ypwl.xiaotouzi.event.HuodongHintEvent;
import com.ypwl.xiaotouzi.event.LoginStateEvent;
import com.ypwl.xiaotouzi.im.YMessageCoreService;
import com.ypwl.xiaotouzi.interf.IRequestCallback;
import com.ypwl.xiaotouzi.manager.ConstInstanceHelper;
import com.ypwl.xiaotouzi.manager.EventHelper;
import com.ypwl.xiaotouzi.manager.JsonHelper;
import com.ypwl.xiaotouzi.manager.ShareAuthManager;
import com.ypwl.xiaotouzi.manager.SilenceRequestHelper;
import com.ypwl.xiaotouzi.manager.net.NetHelper;
import com.ypwl.xiaotouzi.ui.fragment.FragmentFinanceSupermarket;
import com.ypwl.xiaotouzi.ui.fragment.FragmentMore;
import com.ypwl.xiaotouzi.ui.fragment.FragmentNewMyInvest;
import com.ypwl.xiaotouzi.ui.fragment.FragmentNewXtz;
import com.ypwl.xiaotouzi.utils.CacheUtils;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.StringUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.Util;

/**
 * function : 主页面入口.
 * <p/>
 * Modify by lzj on 2015/11/5.
 */
public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, FragmentNewXtz.OnBannerClickListener {
    private FragmentManager fragmentManager;
    private RadioGroup mRadioGroup;
    private RadioButton mRadioBtnXtz, mRadioBtnFinanceMarket, mRadioBtnMyInvest, mRadioBtnMore;

    /** Fragment TAG :  晓投资 */
    public static final String FRAGMENT_TAG_XTZ = "fragment_tag_xtz";
    /** Fragment TAG :  我的投资 */
    public static final String FRAGMENT_TAG_MYINVEST = "fragment_tag_myinvest";
    /** Fragment TAG :  更多 */
    public static final String FRAGMENT_TAG_MORE = "fragment_tag_more";
    /** 金融超市 */
    public static final String FRAGMENT_TAG_FINANCE_MARKET = "fragment_tag_finance_market";
    /** Fragment TAG数组 */
    private static final String[] mFragmentTags = new String[]{FRAGMENT_TAG_MYINVEST, FRAGMENT_TAG_FINANCE_MARKET, FRAGMENT_TAG_MORE, FRAGMENT_TAG_XTZ};
    /** Fragment : 晓投资 */
    private FragmentNewXtz mFragmentXtz;
    /** Fragment : 金融超市 */
    private FragmentFinanceSupermarket mFinanceMarket;
    /** Fragment :  我的投资 */
    private FragmentNewMyInvest mFragmentMyInvest;
    /** Fragment :  更多 */
    private FragmentMore mFragmentMore;
    /** 上次选择的radioId */
    private int mLastCheckedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_main);
        overridePendingTransition(R.anim.activity_gradient_in, R.anim.activity_gradient_out);
        setSwipeBackActivityEnable(false);

        startService(new Intent(this, YMessageCoreService.class));//保留服务，提高app进程的长久性

        //更新逻辑--start
        boolean isForceUpdating = CacheUtils.getBoolean(Const.KEY_UPDATE_FORCING_DOWNLOADING, false);
        if (isForceUpdating) {
            UIUtil.showToastShort("正在下载更新，请稍后再试");
            finish();
            return;
        }
        SilenceRequestHelper.getInstance().checkAppIsNeedUpdate(new SilenceRequestHelper.AppUpdateCheckCallBack() {
            @Override
            public void onCompleted(boolean needUpdate, AppUpdateBean appUpdateBean) {
                if (needUpdate) {
                    Intent intent = new Intent(UIUtil.getContext(), UpdateDialogActivity.class);
                    intent.putExtra(UpdateDialogActivity.KEY_JUMP_UPDATE_HINT, appUpdateBean);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    UIUtil.getContext().startActivity(intent);
                }
            }
        });
        //更新逻辑--end

        fragmentManager = getSupportFragmentManager();
        initView();
    }

    private void initView() {
        mRadioGroup = findView(R.id.rg_tabs);
        mRadioBtnXtz = findView(R.id.tab_xtz);
        mRadioBtnFinanceMarket = findView(R.id.tab_finance_market);
        mRadioBtnMyInvest = findView(R.id.tab_myinvest);
        mRadioBtnMore = findView(R.id.tab_more);
        mRadioGroup.setOnCheckedChangeListener(this);
        initDefaultDisplayFragment();
    }


    /** 初始化默认显示的fragment */
    private void initDefaultDisplayFragment() {
        String tag = getIntent().getStringExtra(Const.KEY_INTENT_JUMP_BASE_DATA);
        int id;
        if (FRAGMENT_TAG_MYINVEST.equals(tag)) {
            id = R.id.tab_myinvest;
        } else if (FRAGMENT_TAG_MORE.equals(tag)) {
            id = R.id.tab_more;
        } else if (FRAGMENT_TAG_FINANCE_MARKET.equals(tag)) {
            id = R.id.tab_finance_market;
        } else {
            id = R.id.tab_xtz;
        }
        mRadioGroup.check(id);
        asyncCheckHuodongHintData();//异步获取活动列表
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(fragmentManager, transaction);
        switch (checkedId) {
            case R.id.tab_xtz:// 切换到晓投资
                mLastCheckedId = checkedId;
                mRadioBtnXtz.setChecked(true);
                if (mFragmentXtz == null) {
                    mFragmentXtz = new FragmentNewXtz();
                    mFragmentXtz.setOnBannerClickListener(this);
                    transaction.add(R.id.layout_fragment_content, mFragmentXtz, FRAGMENT_TAG_XTZ);
                }
                transaction.show(mFragmentXtz);
                break;
            case R.id.tab_finance_market://金融超市
                mLastCheckedId = checkedId;
                mRadioBtnFinanceMarket.setChecked(true);
                if (mFinanceMarket == null) {
                    mFinanceMarket = new FragmentFinanceSupermarket();
                    transaction.add(R.id.layout_fragment_content, mFinanceMarket, FRAGMENT_TAG_FINANCE_MARKET);
                }
                transaction.show(mFinanceMarket);
                break;
            case R.id.tab_myinvest:// 切换到我的投资
                if (Util.legalLogin() == null) {
                    startActivity(LoginActivity.class);
                    onCheckedChanged(group, mLastCheckedId);
                    return;
                }
                mLastCheckedId = checkedId;
                mRadioBtnMyInvest.setChecked(true);
                if (mFragmentMyInvest == null) {
                    mFragmentMyInvest = new FragmentNewMyInvest();
                    transaction.add(R.id.layout_fragment_content, mFragmentMyInvest, FRAGMENT_TAG_MYINVEST);
                }
                transaction.show(mFragmentMyInvest);
                break;
            case R.id.tab_more:// 切换到更多
                mLastCheckedId = checkedId;
                mRadioBtnMore.setChecked(true);
                if (mFragmentMore == null) {
                    mFragmentMore = new FragmentMore();
                    transaction.add(R.id.layout_fragment_content, mFragmentMore, FRAGMENT_TAG_MORE);
                }
                transaction.show(mFragmentMore);
                displayMyinfoHint(0);
                break;
        }
        transaction.commit();
    }

    /** 隐藏所有的fragment */
    private void hideFragments(FragmentManager fragmentManager, FragmentTransaction transaction) {
        for (String fragmentTag : mFragmentTags) {
            Fragment fragment = fragmentManager.findFragmentByTag(fragmentTag);
            if (fragment != null && fragment.isVisible()) {
                transaction.hide(fragment);
            }
        }
        mRadioBtnXtz.setChecked(false);
        mRadioBtnMyInvest.setChecked(false);
        mRadioBtnMore.setChecked(false);
        mRadioBtnFinanceMarket.setChecked(false);
    }

    /** 获取优惠活动的个数,并显示出来 */
    private void asyncCheckHuodongHintData() {
        if (Util.legalLogin() == null) {
            return;
        }
        String url = StringUtil.format(URLConstant.USER_HUODONG_LIEBIAO, GlobalUtils.token);
        NetHelper.get(url, new IRequestCallback<String>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onFailure(Exception e) {

            }

            @Override
            public void onSuccess(String jsonStr) {
                HuodongInfosBean bean = JsonHelper.parseObject(jsonStr, HuodongInfosBean.class);
                if (bean == null || bean.getStatus() != ServerStatus.SERVER_STATUS_OK) {
                    return;
                }
                CacheUtils.putString(Const.HUODONG_INFO, jsonStr);
                displayMyinfoHint(bean.getNum());
                EventHelper.post(new HuodongHintEvent(bean.getNum()));//向更多页面发送活动事件提示
            }
        });
    }

    /** 是否显示我的上的红点提示 */
    private void displayMyinfoHint(int num) {
        if (num > 0) {
            Drawable drawable = UIUtil.getDrawable(R.drawable.main_radiobutton_me_selector_hongdian);
            mRadioBtnMore.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
        } else {
            Drawable drawable = UIUtil.getDrawable(R.drawable.main_radiobutton_me_selector);
            mRadioBtnMore.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
        }
    }

    @Subscribe
    public void onEventLoginStateChange(LoginStateEvent event) {
        if (event.hasLogin) {
            asyncCheckHuodongHintData();//重新请求优惠活动数
        } else {
            displayMyinfoHint(0);
        }
    }

    private long touchTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            long currentTime = System.currentTimeMillis();
            long waitTime = 2000;
            if ((currentTime - touchTime) >= waitTime) {
                UIUtil.showToastShort(getString(R.string.main_hint_exit_app));
                touchTime = currentTime;
            } else {
                XtzApp.getApplication().removeAll();
            }
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (null != mFragmentXtz && FragmentNewXtz.CODE_REQUEST_IMAGE_CROP == requestCode){
            mFragmentXtz.onActivityResult(requestCode, resultCode, data);
//        }

        ShareAuthManager.getInstance().onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        //APP退出做内存释放(主要针对单例实例的内存数据需要清理)
        ConstInstanceHelper.getInstance().release();

        super.onDestroy();
    }

    /** 晓投资页面点击金融超市精选 */
    @Subscribe
    public void onEnterIntoFinanceMarketEvent(EnterIntoFinanceMarketEvent event) {
        if (event != null && !isFinishing()) {
//            mRadioGroup.check(R.id.tab_finance_market);
            onCheckedChanged(mRadioGroup, R.id.tab_finance_market);
            mFinanceMarket.setDefShow(0);
        }
    }

    /**跳转到金融超市更多*/
    @Override
    public void skipToFinanceMore() {
        if (!isFinishing()) {
//            mRadioGroup.check(R.id.tab_finance_market);
            onCheckedChanged(mRadioGroup, R.id.tab_finance_market);
            mFinanceMarket.setDefShow(2);
        }
    }


}
