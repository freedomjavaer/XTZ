package com.ypwl.xiaotouzi.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.base.BaseFragment;
import com.ypwl.xiaotouzi.bean.HuodongInfosBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.common.ServerStatus;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.event.HuodongHintEvent;
import com.ypwl.xiaotouzi.event.LoginStateEvent;
import com.ypwl.xiaotouzi.interf.IRequestCallback;
import com.ypwl.xiaotouzi.manager.JsonHelper;
import com.ypwl.xiaotouzi.manager.ShareAuthManager;
import com.ypwl.xiaotouzi.manager.UmengEventHelper;
import com.ypwl.xiaotouzi.manager.net.NetHelper;
import com.ypwl.xiaotouzi.ui.activity.HuodongActivity;
import com.ypwl.xiaotouzi.ui.activity.LoginActivity;
import com.ypwl.xiaotouzi.ui.activity.MyFocusActivity;
import com.ypwl.xiaotouzi.ui.activity.QuestionAndFeedBackActivity;
import com.ypwl.xiaotouzi.ui.activity.SettingActivity;
import com.ypwl.xiaotouzi.ui.activity.XtzWalletActivity;
import com.ypwl.xiaotouzi.utils.CacheUtils;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.LogUtil;
import com.ypwl.xiaotouzi.utils.StringUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.Util;
import com.ypwl.xiaotouzi.view.dialog.CustomDialog;
import com.ypwl.xiaotouzi.view.dialog.KProgressHUDHelper;
import com.ypwl.xiaotouzi.view.dialog.lib.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * function : 我的.
 * <p>
 * Modify by lzj on 2015/11/6.
 */
public class FragmentMore extends BaseFragment implements View.OnClickListener {
    /** 活动提醒布局 */
    private View mLayoutHuodongHint;
    /** 活动数量 */
    private TextView mHuodongNumber;
    //    private LoginBean mLoginBean;
    private KProgressHUD mDialogLoading;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_myinfo, container, false);
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        findView(v, R.id.layout_title_back).setVisibility(View.GONE);
        TextView title = findView(v, R.id.tv_title);
        title.setText("更多");
        findView(v, R.id.layout_item_setting).setOnClickListener(this);
        findView(v, R.id.layout_item_myfocus).setOnClickListener(this);
        findView(v, R.id.layout_item_share_invitecode).setOnClickListener(this);
        findView(v, R.id.layout_item_huodong).setOnClickListener(this);
        findView(v, R.id.layout_item_advice).setOnClickListener(this);
        findView(v, R.id.layout_item_wallet).setOnClickListener(this);

        mLayoutHuodongHint = findView(v, R.id.layout_item_huodong_layout_hint);
        mHuodongNumber = findView(v, R.id.layout_item_huodong_layout_hint_number);
        refreshViewData();
    }

    /** 刷新视图数据 */
    private void refreshViewData() {
        if (Util.legalLogin() == null) {//未登录
            mLayoutHuodongHint.setVisibility(View.GONE);
        } else {//已登录
            HuodongInfosBean bean = JsonHelper.parseObject(CacheUtils.getString(Const.HUODONG_INFO, null), HuodongInfosBean.class);
            if (bean != null && bean.getNum() > 0) {
                mLayoutHuodongHint.setVisibility(View.VISIBLE);
                mHuodongNumber.setText(String.valueOf(bean.getNum()));
            } else {
                mLayoutHuodongHint.setVisibility(View.GONE);
            }
        }
    }

    /** 优惠活动事件接收 */
    @Subscribe
    public void onHuodongHintEvent(HuodongHintEvent event) {
        if (mLayoutHuodongHint == null || mHuodongNumber == null) {
            return;
        }
        if (event.huodongNumber > 0) {
            mLayoutHuodongHint.setVisibility(View.VISIBLE);
            mHuodongNumber.setText(String.valueOf(event.huodongNumber));
        } else {
            mLayoutHuodongHint.setVisibility(View.GONE);
        }
    }

    @Subscribe
    public void onEventWhenLoginStateChange(LoginStateEvent event) {
        if (event != null && event.hasLogin) {
            refreshViewData();
        } else {
            mLayoutHuodongHint.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_item_setting://进入设置页面
                startActivity(Util.legalLogin() == null ? LoginActivity.class : SettingActivity.class);
                break;
            case R.id.layout_item_myfocus://进入我的关注页面
                Const.PLATFORM_CHOOSE_REQUEST_FROM = 2;
                Intent intent = new Intent();
                if (null == Util.legalLogin()) {
                    intent.setClass(getContext(), LoginActivity.class);
                } else {
                    intent.putExtra(Const.KEY_INTENT_JUMP_FROM_DATA, UIUtil.getString(R.string.main_tab_text_myinfo));
                    intent.setClass(getContext(), MyFocusActivity.class);
                }
                startActivity(intent);
//                startActivity(null == mLoginBean ? LoginAndRegisterActivity.class : MyFocusActivity.class);
                break;
            case R.id.layout_item_share_invitecode://分享邀请码
                shareInviteCode();
                break;
            case R.id.layout_item_huodong://进入优惠活动页面
                startActivity(null == Util.legalLogin() ? LoginActivity.class : HuodongActivity.class);
                mLayoutHuodongHint.setVisibility(View.GONE);
                break;
            case R.id.layout_item_advice://进入常见问题与反馈
                startActivity(null == Util.legalLogin() ? LoginActivity.class : QuestionAndFeedBackActivity.class);
                break;
            case R.id.layout_item_wallet://晓钱包
                startActivity(null == Util.legalLogin() ? LoginActivity.class : XtzWalletActivity.class);
                break;
        }
    }


    /** 分享App的邀请码 */
    private void shareInviteCode() {
        if (null == Util.legalLogin()) {
            startActivity(LoginActivity.class);
            return;
        }
        if (mDialogLoading == null) {
            mDialogLoading = KProgressHUDHelper.createLoading(getActivity());
        }
        String url = StringUtil.format(URLConstant.SHARE_GET_APP_SHARE_URL, GlobalUtils.token);
        NetHelper.get(url, new IRequestCallback<String>() {
            @Override
            public void onStart() {
                mDialogLoading.show();
            }

            @Override
            public void onFailure(Exception e) {
                mDialogLoading.dismiss();
                UIUtil.showToastShort(getString(R.string.fragment_myinfo_share_failed));
            }

            @Override
            public void onSuccess(String jsonStr) {
                mDialogLoading.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    int status = jsonObject.optInt(Const.JSON_KEY_status, -1);
                    if (ServerStatus.SERVER_STATUS_OK == status) {
                        String shareTitle = jsonObject.getString(Const.JSON_KEY_title);
                        String shareContent = jsonObject.getString(Const.JSON_KEY_content);
                        String shareUrl = jsonObject.getString(Const.JSON_KEY_url);
                        showShare(shareTitle, shareContent, shareUrl);
                    } else {
                        this.onFailure(null);
                    }
                } catch (JSONException e) {
                    this.onFailure(null);
                }
            }
        });
    }


    /** 装载分享信息，启动分享GUI */
    private void showShare(final String shareTitle, final String shareContent, final String shareUrl) {
        UmengEventHelper.onEvent("ShareInvitationCode");
        LogUtil.e(TAG, "share: shareTitle=" + shareTitle + " ,shareContent=" + shareContent + " ,shareUrl=" + shareUrl);

        View contentView = UIUtil.inflate(R.layout.layout_dialog_content_share);
        final CustomDialog dialog = new CustomDialog.TipsBuilder(getActivity())
                .setDialogBgColor(UIUtil.getColor(R.color.transparent))
                .setCustomContentView(contentView)
                .setDialogWidthMatch(true)
                .create();
        dialog.show();

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                switch (v.getId()) {
                    case R.id.share_item_weixin_circle:
                        ShareAuthManager.share(getActivity(), ShareAuthManager.PLATFORM_Weixin_Circle, shareTitle, shareContent, shareUrl);
                        break;
                    case R.id.share_item_weixin:
                        ShareAuthManager.share(getActivity(), ShareAuthManager.PLATFORM_Weixin, shareTitle, shareContent, shareUrl);
                        break;
                    case R.id.share_item_qq:
                        ShareAuthManager.share(getActivity(), ShareAuthManager.PLATFORM_QQ, shareTitle, shareContent, shareUrl);
                        break;
                    case R.id.share_item_sina:
                        ShareAuthManager.share(getActivity(), ShareAuthManager.PLATFORM_Sina, shareTitle, shareContent, shareUrl);
                        break;
                    case R.id.share_item_sms:
                        ShareAuthManager.share(getActivity(), ShareAuthManager.PLATFORM_SMS, shareTitle, shareContent, shareUrl);
                        break;
                }
            }
        };
        findView(contentView, R.id.share_item_weixin_circle).setOnClickListener(onClickListener);
        findView(contentView, R.id.share_item_weixin).setOnClickListener(onClickListener);
        findView(contentView, R.id.share_item_qq).setOnClickListener(onClickListener);
        findView(contentView, R.id.share_item_sina).setOnClickListener(onClickListener);
        findView(contentView, R.id.share_item_sms).setOnClickListener(onClickListener);
    }

    @Override
    public void onDestroy() {
        mDialogLoading = null;
        super.onDestroy();
    }
}


