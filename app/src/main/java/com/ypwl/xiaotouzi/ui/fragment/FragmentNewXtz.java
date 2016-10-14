package com.ypwl.xiaotouzi.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.andview.refreshview.XRefreshView;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.adapter.XtzFancyCoverAdapter;
import com.ypwl.xiaotouzi.adapter.XtzNetCreditNewsAdapter;
import com.ypwl.xiaotouzi.base.BaseFragment;
import com.ypwl.xiaotouzi.bean.BannerBean;
import com.ypwl.xiaotouzi.bean.LoginBean;
import com.ypwl.xiaotouzi.bean.XtzNetCreditNewsBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.common.DeviceInfo;
import com.ypwl.xiaotouzi.common.ServerStatus;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.common.UmengEventID;
import com.ypwl.xiaotouzi.event.EnterIntoFinanceMarketEvent;
import com.ypwl.xiaotouzi.event.LoginStateEvent;
import com.ypwl.xiaotouzi.event.MessageBoxHintEvent;
import com.ypwl.xiaotouzi.event.NickNameSuccessEvent;
import com.ypwl.xiaotouzi.interf.INetRequestListener;
import com.ypwl.xiaotouzi.interf.IRequestCallback;
import com.ypwl.xiaotouzi.manager.BannerHelper;
import com.ypwl.xiaotouzi.manager.EventHelper;
import com.ypwl.xiaotouzi.manager.ShareAuthManager;
import com.ypwl.xiaotouzi.manager.UmengEventHelper;
import com.ypwl.xiaotouzi.manager.net.NetHelper;
import com.ypwl.xiaotouzi.netprotocol.XtzHomePagerProtocol;
import com.ypwl.xiaotouzi.ui.activity.AccountBindActivity;
import com.ypwl.xiaotouzi.ui.activity.BindPhoneActivity;
import com.ypwl.xiaotouzi.ui.activity.CalcActivity;
import com.ypwl.xiaotouzi.ui.activity.FinanceSupermarketOfPlatformTargetsActivity;
import com.ypwl.xiaotouzi.ui.activity.FinanceSupermarketOfTargetDetailActivity;
import com.ypwl.xiaotouzi.ui.activity.InvestFriendCircleActivity;
import com.ypwl.xiaotouzi.ui.activity.LoginActivity;
import com.ypwl.xiaotouzi.ui.activity.MessageBoxActivity;
import com.ypwl.xiaotouzi.ui.activity.ModifyNicknameActivity;
import com.ypwl.xiaotouzi.ui.activity.NetPlatformActivity;
import com.ypwl.xiaotouzi.ui.activity.PhoneNumberActivity;
import com.ypwl.xiaotouzi.ui.activity.XtzBannerDetailActivity;
import com.ypwl.xiaotouzi.utils.CacheUtils;
import com.ypwl.xiaotouzi.utils.DateTimeUtil;
import com.ypwl.xiaotouzi.utils.FileUtil;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.ImageUtils;
import com.ypwl.xiaotouzi.utils.ImgLoadUtil;
import com.ypwl.xiaotouzi.utils.LogUtil;
import com.ypwl.xiaotouzi.utils.NetworkUtils;
import com.ypwl.xiaotouzi.utils.PopuViewUtil;
import com.ypwl.xiaotouzi.utils.StorageUtil;
import com.ypwl.xiaotouzi.utils.StringUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.Util;
import com.ypwl.xiaotouzi.utils.ViewUtil;
import com.ypwl.xiaotouzi.view.CircleImageView;
import com.ypwl.xiaotouzi.view.CustomXRefreshHeaderView;
import com.ypwl.xiaotouzi.view.LoadMoreListView;
import com.ypwl.xiaotouzi.view.dialog.CustomDialog;
import com.ypwl.xiaotouzi.view.dialog.lib.KProgressHUD;
import com.ypwl.xiaotouzi.view.fancycoverflow.FancyCoverFlow;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * function :晓投资页面：合并了原来的投友圈和网贷平台
 * <p/>
 * Created by tengtao on 2016/3/14.
 */
public class FragmentNewXtz extends BaseFragment implements View.OnClickListener, LoadMoreListView.IListViewRefreshListener, AbsListView.OnScrollListener {
    /** 轮播图 */
    private BannerHelper mBannerHelper;
    private LinearLayout mLlBannerContainer;
    private View mNoDataView;
    //    private CustomSwipeToRefresh mSwipeToRefresh;
    private XRefreshView mSwipeToRefresh;
    private int mCurrPage = 1;
    private boolean isFirst;
    private ImageView mIvPersonIcon, mIvMsgIcon, mIvAdvertismentBanner;
    private CircleImageView mIvCircleIcon;
    private TextView mTvXtzTitle;
    private LoadMoreListView mListView;
    private XtzNetCreditNewsAdapter mNetCreditNewsAdapter;
    private XtzHomePagerProtocol homePagerProtocol;
    private List<XtzNetCreditNewsBean.ListBean> mListEntity = new ArrayList<>();
    @SuppressWarnings("FieldCanBeLocal")
    private String loacalAvatarPath;
    /** 画廊控件 */
    private FancyCoverFlow mFancyCoverFlow;
    /** 画廊适配器 */
    private XtzFancyCoverAdapter mCoverAdapter;
    /** 拍照获取到的照片Uri */
    private Uri mCameraOutputUri = null;
    /** 相册或拍照获取到的照片处理后的文件Uri */
    private Uri mUriFileForCrop;
    /** 选择相册图片请求码 */
    private static final int CODE_REQUEST_IMAGE_PICK = 0x001;
    /** 裁剪相册图片请求码 */
    public static final int CODE_REQUEST_IMAGE_CROP = 0x002;
    /** 相机拍照请求码 */
    private static final int CODE_REQUEST_CAMERA = 0x003;

    private String mFixBannerUrl;
    private String mFixBannerTitle;
    private String mBannerTye, mFixBannerType;
    private CircleImageView mUserLogo;
    private TextView mNickName;
    private LinearLayout mBindOtherAccount;
    private File mCameraOutputUriFile;
    private TextView mPhoneNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_xtz, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        view.findViewById(R.id.tv_title_back).setVisibility(View.GONE);
        mNoDataView = view.findViewById(R.id.layout_no_data_view);
//        mSwipeToRefresh = (CustomSwipeToRefresh) view.findViewById(R.id.xtz_swipe_refresh);
//        mSwipeToRefresh.setOnRefreshListener(this);
        mSwipeToRefresh = findView(view, R.id.xtz_swipe_refresh);
        mSwipeToRefresh.setMoveForHorizontal(true);
        mSwipeToRefresh.setCustomHeaderView(new CustomXRefreshHeaderView(getContext()));
        mSwipeToRefresh.setPullLoadEnable(false);
        mSwipeToRefresh.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh() {

                mCurrPage = 1;
                isFirst = true;
                homePagerProtocol.loadDataByPage(mCurrPage, homePageRequestListener);
                UIUtil.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeToRefresh.stopRefresh();
                    }
                }, 10 * 1000);

            }

        });

        mTvXtzTitle = (TextView) view.findViewById(R.id.tv_title);
        findView(view, R.id.layout_title_back).setOnClickListener(this);//点击进入个人信息页面
        mIvPersonIcon = (ImageView) view.findViewById(R.id.iv_title_back);
        mIvCircleIcon = (CircleImageView) view.findViewById(R.id.iv_circle_icon);
        mIvCircleIcon.setOnClickListener(this);
        mIvMsgIcon = (ImageView) view.findViewById(R.id.iv_title_right_image);
        mIvMsgIcon.setOnClickListener(this);//收件箱页面
        //新闻列表
        mListView = (LoadMoreListView) view.findViewById(R.id.xtz_main_page_loadmorelistview);
        mListView.setOnRefreshListener(this);
        mListView.setOnScrollListener(this);
        //listview的头view
        View headerView = View.inflate(getActivity(), R.layout.layout_main_page_load_more_list_header_view, null);
        mIvAdvertismentBanner = (ImageView) headerView.findViewById(R.id.advertisement_banner);
        mIvAdvertismentBanner.setOnClickListener(this);
        mFancyCoverFlow = (FancyCoverFlow) headerView.findViewById(R.id.fancyCoverFlow);
        mFancyCoverFlow.setOnItemClickListener(mOnItemClickListener);
        mFancyCoverFlow.setUnselectedAlpha(1.0f);
        mFancyCoverFlow.setUnselectedSaturation(0.0f);//未选中饱和度
        mFancyCoverFlow.setUnselectedScale(0.8f);//未选中规模
        mFancyCoverFlow.setSpacing(-(int) (DeviceInfo.ScreenWidthPixels * 0.19));
        mFancyCoverFlow.setMaxRotation(0);//进入旋转角度
        mFancyCoverFlow.setScaleDownGravity(0.5f);//下重力
        mFancyCoverFlow.setActionDistance(FancyCoverFlow.ACTION_DISTANCE_AUTO);

        mCoverAdapter = new XtzFancyCoverAdapter(getActivity());
        mFancyCoverFlow.setAdapter(mCoverAdapter);
        /** 初始化轮播图 */
        mBannerHelper = BannerHelper.getInstance(getActivity());
        mLlBannerContainer = (LinearLayout) headerView.findViewById(R.id.banner_rootlayout);
        mBannerHelper.init(mLlBannerContainer);
        headerView.findViewById(R.id.xtz_rb_wdpt).setOnClickListener(this);
        headerView.findViewById(R.id.xtz_rb_tyq).setOnClickListener(this);
        headerView.findViewById(R.id.xtz_rb_money_manager).setOnClickListener(this);
        headerView.findViewById(R.id.xtz_rb_jsq).setOnClickListener(this);
        mListView.addCustomView(headerView);
        mListView.requestDisallowInterceptTouchEvent(true);

        ViewUtil.showContentLayout(Const.LAYOUT_LOADING, mNoDataView, mSwipeToRefresh);
        initData();
        //请求数据
        refreshView();
    }

    /** 请求数据 */
    private void refreshView() {
        mTvXtzTitle.setText(getString(R.string.main_tab_text_xtz));
        LoginBean mLoginBean = Util.legalLogin();
        if (mLoginBean != null) {
            ImgLoadUtil.loadImgByPath(mLoginBean.getAvatarUrl(), mIvCircleIcon, R.mipmap.default_user_icon);
            mIvCircleIcon.setVisibility(View.VISIBLE);
            mIvPersonIcon.setVisibility(View.GONE);
        } else {
            mIvPersonIcon.setImageResource(R.mipmap.personal_info_icon);
            mIvPersonIcon.setVisibility(View.VISIBLE);
            mIvCircleIcon.setVisibility(View.GONE);
        }
        mIvMsgIcon.setImageResource(R.mipmap.xtz_not_message_icon);
        mIvMsgIcon.setVisibility(View.VISIBLE);

        mNetCreditNewsAdapter = new XtzNetCreditNewsAdapter(getActivity());
        mListView.setAdapter(mNetCreditNewsAdapter);
        isFirst = true;
        homePagerProtocol = new XtzHomePagerProtocol();
        homePagerProtocol.loadDataByPage(mCurrPage, homePageRequestListener);
    }

    private INetRequestListener homePageRequestListener = new INetRequestListener<XtzNetCreditNewsBean>() {
        @Override
        public void netRequestCompleted() {
            mListView.stopLoadMore();
//            mSwipeToRefresh.setRefreshing(false);
            mSwipeToRefresh.stopRefresh();
            if ((!NetworkUtils.isNetworkConnected(UIUtil.getContext())) && isFirst) {
                ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mNoDataView, mSwipeToRefresh, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        homePagerProtocol.loadDataByPage(mCurrPage, homePageRequestListener);
                    }
                });
            }
        }

        @Override
        public void netRequestSuccess(XtzNetCreditNewsBean bean, boolean isSuccess) {
            if (bean != null && isSuccess) {
                isFirst = false;
                List<XtzNetCreditNewsBean.ListBean> list = bean.getList();
                if (mCurrPage == 1) {
                    mListEntity.clear();
                    mListEntity.addAll(list);
                    initAdvertisementAndFancyCoverFlow(bean);
                    mNetCreditNewsAdapter.loadData(mListEntity);
                    onInitBanner(bean.getBanner());
                } else {
                    mListEntity.addAll(bean.getList());
                    if (list.size() > 0) {
                        mNetCreditNewsAdapter.loadData(mListEntity);
                    } else {
                        mListView.setDefaultText(getString(R.string.loadmore_status_nomore));
                        mCurrPage--;
                    }
                }

                ViewUtil.showContentLayout(Const.LAYOUT_DATA, mNoDataView, mSwipeToRefresh);
            } else {
                if (isFirst)
                    ViewUtil.showContentLayout(Const.LAYOUT_ERROR, mNoDataView, mSwipeToRefresh, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            homePagerProtocol.loadDataByPage(mCurrPage, homePageRequestListener);
                        }
                    });
            }
        }

    };

    private LoginBean mLoginBean;
    private KProgressHUD mDialogLoading;

    private void initData() {
        mLoginBean = Util.legalLogin();
        mDialogLoading = KProgressHUD.create(getActivity());
    }

    /** 初始化固定banner和滑动标的 */
    private void initAdvertisementAndFancyCoverFlow(XtzNetCreditNewsBean bean) {
        List<XtzNetCreditNewsBean.FixBannerBean> fix_banner = bean.getFix_banner();
        if (fix_banner != null && fix_banner.size() > 0) {
            mIvAdvertismentBanner.setVisibility(View.VISIBLE);
            String image = fix_banner.get(0).getImage();
            mFixBannerTitle = fix_banner.get(0).getTitle();
            mFixBannerUrl = fix_banner.get(0).getUrl();
            mFixBannerType = fix_banner.get(0).getType();
            ImgLoadUtil.loadAvatarBySplice(image, mIvAdvertismentBanner, R.mipmap.pic_021);
        } else {
            mIvAdvertismentBanner.setVisibility(View.GONE);
        }
        List<XtzNetCreditNewsBean.BlistBean> blist = bean.getBlist();
        if (blist != null && blist.size() > 0) {
            mFancyCoverFlow.setVisibility(View.VISIBLE);
            mCoverAdapter.loadData(blist);
            if (blist.size() > 1) {
                mFancyCoverFlow.setSelection(1, false);
            }
        } else {//没有推荐标的，隐藏推荐标的画廊
            mFancyCoverFlow.setVisibility(View.GONE);
        }
    }

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (mFancyCoverFlow.getVisibility() == View.GONE) {
                return;
            }
            int selectedItemPosition = mFancyCoverFlow.getSelectedItemPosition();
            if (position < selectedItemPosition) {
                mFancyCoverFlow.setSelection(position + 1, true);
                return;
            }
            if (position > selectedItemPosition) {
                mFancyCoverFlow.setSelection(position - 1, true);
                return;
            }
            XtzNetCreditNewsBean.BlistBean entity = mCoverAdapter.getDataList().get(position);
            if (entity != null) {
                Intent intent = new Intent(getActivity(), FinanceSupermarketOfTargetDetailActivity.class);
                intent.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA + "p_name", entity.getP_name());
                intent.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA + "pid", entity.getPid());
                intent.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA, entity.getProject_id());
                intent.putExtra(Const.KEY_INTENT_JUMP_FROM_DATA, "晓投资");
                getActivity().startActivity(intent);
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.xtz_rb_jsq://计算器
                startActivity(CalcActivity.class);
                UmengEventHelper.onEvent(UmengEventID.XtzNetLendingCalculator);
                break;
            case R.id.xtz_rb_tyq://同城在线
                startActivity(Util.legalLogin() == null ? LoginActivity.class : InvestFriendCircleActivity.class);
                UmengEventHelper.onEvent(UmengEventID.XtzSameCityOnlineButton);
                break;
            case R.id.xtz_rb_money_manager://理财-->金融超市
                EventHelper.post(new EnterIntoFinanceMarketEvent());
                UmengEventHelper.onEvent(UmengEventID.XtzFinanceMarket);
                break;
            case R.id.xtz_rb_wdpt://网贷平台
                startActivity(NetPlatformActivity.class);
                UmengEventHelper.onEvent(UmengEventID.XtzNetplatformButton);
                break;
            case R.id.layout_title_back:
            case R.id.iv_circle_icon://个人信息
                //startActivity(Util.legalLogin() == null ? LoginActivity.class : PersonalInfoActivity.class);
                if (mLoginBean == null) {
                    startActivity(LoginActivity.class);
                } else {
                    showDialog();
                }
                break;
            case R.id.iv_title_right_image://收件箱
                if (Util.legalLogin() == null) {
                    startActivity(LoginActivity.class);
                    return;
                }
                mIvMsgIcon.setImageResource(R.mipmap.xtz_not_message_icon);
                CacheUtils.putBoolean(Const.KEY_NOTE_REPLY_NEW, false);
                startActivity(MessageBoxActivity.class);
                break;
            case R.id.advertisement_banner://固定banner
                if (StringUtil.isEmptyOrNull(mFixBannerUrl)) {
                    return;
                }
                if ("3".equalsIgnoreCase(mFixBannerType)) {
                    if ("0".equalsIgnoreCase(mFixBannerUrl)) {//跳转到金融超市 - 更多模块
                        if (mOnBannerClickListener != null)
                            mOnBannerClickListener.skipToFinanceMore();
                    } else {//跳转到 金融超市-平台详情列表, url=pid
                        Intent intent = new Intent(getActivity(), FinanceSupermarketOfPlatformTargetsActivity.class);
                        intent.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA + "name", "");
                        intent.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA, mFixBannerUrl);
                        intent.putExtra(Const.KEY_INTENT_JUMP_FROM_DATA, "晓投资");
                        getActivity().startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(getActivity(), XtzBannerDetailActivity.class);
                    intent.putExtra("title", mFixBannerTitle);
                    intent.putExtra("url", mFixBannerUrl);
                    getActivity().startActivity(intent);
                }
                break;
            case R.id.tv_personal_info_nickname://修改昵称
                startActivity(ModifyNicknameActivity.class);
                break;
            case R.id.iv_personal_info_icon://修改头像
                chooseAvatarFrom();
                break;
            case R.id.layout_item_bind_other_account://绑定社交账号
                startActivity(AccountBindActivity.class);
                break;
            case R.id.tv_login_or_logout://退出登录状态
                showDialogForHintExitLogin();
                break;
            case R.id.layout_personal_info_bind_phone://手机号
                //设置手机号绑定信息
                String phoneNumber = mLoginBean.getPhone();
                if (!StringUtil.isEmptyOrNull(phoneNumber)) {
                    startActivity(PhoneNumberActivity.class);
                } else {
                    startActivity(BindPhoneActivity.class);
                }
                break;

        }
    }

    private CustomDialog mDialog;//从屏幕中央弹出的对话框

    /** 弹出对话框提示是否退出登录 */
    private void showDialogForHintExitLogin() {
        if (mDialog == null) {
            mDialog = new CustomDialog.AlertBuilder(getActivity())
                    .setDialogLayout(R.layout.cd_dialog_exit)
                    .setContentText(getString(R.string.fragment_myinfo_exit_login_hint))
                    .setContentTextGravity(Gravity.CENTER)
                    .setPositiveBtn("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mDialog.dismiss();
                        }
                    })
                    .setNegativeBtn("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mDialog.dismiss();
                            tipDialog.dismiss();
                            UmengEventHelper.onEvent(UmengEventID.MoreLogout);
                            //EventHelper.post(new ExitOutEvent());
                            Util.clearLoginInfo();
                            startActivity(LoginActivity.class);//退到注册登录页面
                            ShareAuthManager.authDelete();
                        }
                    })
                    .create();
        }
        mDialog.show();
    }

    private CustomDialog tipDialog;//从屏幕左侧弹出的对话框

    /** 用户资料 */
    private void showDialog() {
        View contentView = UIUtil.inflate(R.layout.activity_user_info);
        tipDialog = new CustomDialog.TipsBuilder(getActivity())
                .setDialogGravity(Gravity.LEFT)
                .setCustomContentView(contentView)
                .setWindowTitleTran(true)
                .setDialogInOutAnimResId(R.style.CD_Dialog_Anim_Left)
                .create();

        mUserLogo = (CircleImageView) contentView.findViewById(R.id.iv_personal_info_icon);
        mNickName = (TextView) contentView.findViewById(R.id.tv_personal_info_nickname);
        mBindOtherAccount = (LinearLayout) contentView.findViewById(R.id.layout_item_bind_other_account);
        mPhoneNumber = (TextView) contentView.findViewById(R.id.info_layout_item_bind_phone_content);

        mUserLogo.setOnClickListener(this);
        mNickName.setOnClickListener(this);
        mBindOtherAccount.setOnClickListener(this);
        contentView.findViewById(R.id.tv_login_or_logout).setOnClickListener(this);
        if (!TextUtils.isEmpty(mLoginBean.getPhone())){
            //contentView.findViewById(R.id.layout_personal_info_bind_phone).setOnClickListener(this);
            contentView.findViewById(R.id.info_layout_item_bind_phone_arrow).setVisibility(View.GONE);
            mPhoneNumber.setVisibility(View.VISIBLE);
            mPhoneNumber.setText(mLoginBean.getPhone());
        }else {
            contentView.findViewById(R.id.layout_personal_info_bind_phone).setOnClickListener(this);
            contentView.findViewById(R.id.info_layout_item_bind_phone_arrow).setVisibility(View.VISIBLE);
            mPhoneNumber.setVisibility(View.GONE);
        }

        //更新用户资料
        refreshUserData();
        tipDialog.show();

    }

    @Subscribe
    public void modifyNicknameEvent(NickNameSuccessEvent event) {
        if (null != event) {
            mLoginBean.setNickname(event.nickName);
            CacheUtils.putString(Const.KEY_LOGIN_USER, JSON.toJSONString(mLoginBean));//改变后的LoginBean转换成jsonString,存入本地
            mNickName.setText(event.nickName);
        }
    }

    /** 更新用户资料 */
    private void refreshUserData() {
        if (null == mLoginBean) {
            UIUtil.showToastLong("登录信息有误");
            return;
        }
        //设置头像
        if (!StringUtil.isEmptyOrNull(mLoginBean.getAvatarUrl())) {
            ImgLoadUtil.loadImgByPath(mLoginBean.getAvatarUrl(), mUserLogo, R.mipmap.default_user_icon);
        } else {
            ImgLoadUtil.loadImgByPath(null, mUserLogo, R.mipmap.default_user_icon);
        }
        //设置用户名
        mNickName.setText(StringUtil.isEmptyOrNull(mLoginBean.getNickname()) ? "" : mLoginBean.getNickname());

    }


    /** 弹出对话框选择修改头像的来源 */
    private void chooseAvatarFrom() {

        LinkedList<String> photoChoice = new LinkedList<>();
        photoChoice.add(getString(R.string.user_info_change_avatar_from_camera));
        photoChoice.add(getString(R.string.user_info_change_avatar_from_album));

        PopuViewUtil dialog = new PopuViewUtil(getActivity(), photoChoice, new PopuViewUtil.OnClickCountsListener() {
            @Override
            public void onClick(int position, String str) {
                Intent intent;
                switch (position) {
                    case 1: // 点击 >> 相册
                        intent = new Intent(Intent.ACTION_PICK, null);
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(intent, CODE_REQUEST_IMAGE_PICK);
                        break;
                    case 0: // 点击 >> 拍照
                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        mCameraOutputUriFile = new File(StorageUtil.getTempPath(getActivity()), DateTimeUtil.formatDateTime(new Date(), "yyyyMMddHHmmss") + ".jpg");
                        mCameraOutputUri = Uri.fromFile(mCameraOutputUriFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraOutputUri);
                        startActivityForResult(intent, CODE_REQUEST_CAMERA);
                        break;
                }
            }
        });
        dialog.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ShareAuthManager.getInstance().onActivityResult(getActivity(), requestCode, resultCode, data);
        switch (requestCode) {
            case CODE_REQUEST_IMAGE_PICK:// 裁剪相册图片
                if (data != null && null != data.getData()) {
                    mUriFileForCrop = ImageUtils.startImageCrop(getActivity(), data.getData(), 1, 1, UIUtil.dip2px(200), UIUtil.dip2px(200), CODE_REQUEST_IMAGE_CROP);
                }
                break;
            case CODE_REQUEST_CAMERA:// 裁剪相机拍照
                if (mCameraOutputUri != null && mCameraOutputUriFile.exists()) {
                    mUriFileForCrop = ImageUtils.startImageCrop(getActivity(), mCameraOutputUri, 1, 1, UIUtil.dip2px(60), UIUtil.dip2px(60), CODE_REQUEST_IMAGE_CROP);
                }
                break;
            case CODE_REQUEST_IMAGE_CROP:// 从裁剪获取到图片返回
                if (data != null) {
                    loacalAvatarPath = ImageUtils.saveImageFromUri(getActivity(), mUriFileForCrop, StorageUtil.getPictureDirPath(getActivity()), true);
                    if (StringUtil.isEmptyOrNull(loacalAvatarPath)) {
                        if (StringUtil.isEmptyOrNull(mLoginBean.getAvatarUrl())) {
                            Picasso.with(getActivity()).load(R.mipmap.default_user_icon).into(mUserLogo);
                        } else {
                            UIUtil.showToastLong(getString(R.string.user_info_change_avatar_from_hint_failed));
                        }
                        ImgLoadUtil.loadImgByPath(mLoginBean.getAvatarUrl(), mUserLogo, R.mipmap.default_user_icon);
                    } else {
                        //Picasso.with(getActivity()).load(loacalAvatarPath).placeholder(R.mipmap.pic_027).into(mUserLogo);
                        uploadAvatar2Server(loacalAvatarPath);//上传头像图片
                    }
                } else {
                    UIUtil.showToastLong(getString(R.string.user_info_change_avatar_from_hint_failed));
                }
                break;
        }
    }

    /** 上传头像图片 */
    private void uploadAvatar2Server(String loacalAvatarPath) {
        File avatarImgFile = new File(loacalAvatarPath);
        LogUtil.e(TAG, " avatar img path 123 = " + avatarImgFile.getPath());
        LogUtil.e(TAG, " avatar img path = " + avatarImgFile.getAbsolutePath());
        LogUtil.e(TAG, " avatar img compress path = " + FileUtil.compressFile(avatarImgFile).getAbsolutePath());
        String url = StringUtil.format(URLConstant.UPLOAD_USER_AVATAR, GlobalUtils.token);
        Map<String, File> fileMap = new HashMap<>();
        fileMap.put("file", avatarImgFile);
        NetHelper.upload(url, null, fileMap, new IRequestCallback<String>() {
            @Override
            public void onStart() {
                mDialogLoading.show();
            }

            @Override
            public void onFailure(Exception e) {
                mDialogLoading.dismiss();
                UIUtil.showToastShort(getString(R.string.user_info_change_avatar_upload_failed));
            }

            @Override
            public void onSuccess(String jsonStr) {
                mDialogLoading.dismiss();
                LogUtil.e(TAG, "upload avatar : jsonStr = " + (jsonStr == null ? "null" : jsonStr));
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    int status = jsonObject.optInt(Const.JSON_KEY_status, -1);
                    switch (status) {
                        case ServerStatus.SERVER_STATUS_OK:
                            UIUtil.showToastLong(getString(R.string.user_info_change_avatar_upload_success));
                            String avatarUrl = jsonObject.getString(Const.JSON_KEY_avatarUrl);//上传后的图片的url
                            mLoginBean.setAvatarUrl(avatarUrl);//设置内存中的LoginBean
                            CacheUtils.putString(Const.KEY_LOGIN_USER, JSON.toJSONString(mLoginBean));//改变后的LoginBean转换成jsonString,存入本地
                            if (!StringUtil.isEmptyOrNull(avatarUrl)) {
                                ImgLoadUtil.loadImgByPath(avatarUrl, mUserLogo, R.mipmap.default_user_icon);
                            }
                            EventHelper.post(new LoginStateEvent(true));//发送事件要求刷新FragmentMyinfo界面视图(更改了头像)
                            break;
                        case ServerStatus.SERVER_STATUS_AVATAR_TOO_BIG: // 上传图片大小过大
                            UIUtil.showToastShort(getString(R.string.user_info_change_avatar_upload_failed_hint_too_big));
                            break;
                        case ServerStatus.SERVER_STATUS_INVALID_IMG_TYPE: // 上传图片类型不合法，必须是jpg，gif，bmp，png
                            UIUtil.showToastShort(getString(R.string.user_info_change_avatar_upload_failed_hint_illegal_type));
                            break;
                        case ServerStatus.SERVER_STATUS_UPLOAD_IMG_ERR: // 服务器接收文件失败
                            UIUtil.showToastShort(getString(R.string.user_info_change_avatar_upload_failed_hint_server_receiv_failed));
                            break;
                        case ServerStatus.SERVER_STATUS_UPLOAD_IMG_EMPTY: // 上传图片为空
                            UIUtil.showToastShort(getString(R.string.user_info_change_avatar_upload_failed_hint_img_null));
                            break;
                        default:
                            this.onFailure(null);
                            break;
                    }
                } catch (JSONException e) {
                    this.onFailure(e);
                }
            }
        });
    }

    /**
     * 初始化banner
     */
    public void onInitBanner(List<XtzNetCreditNewsBean.BannerBean> bannerList) {
        if (bannerList == null) {
            mLlBannerContainer.setVisibility(View.GONE);
            return;
        }
        mLlBannerContainer.setVisibility(View.VISIBLE);
        List<BannerBean> dataList = new ArrayList<>();
        for (int i = 0; i < bannerList.size(); i++) {
            dataList.add(new BannerBean(R.mipmap.pic_021, bannerList.get(i).getImage(), bannerList.get(i).getTitle(), bannerList.get(i).getUrl(), bannerList.get(i).getType()));
        }
        mBannerHelper.startBanner(dataList, new BannerHelper.OnItemClickListener() {
            @Override
            public void onItemClick(BannerBean bean) {
//                if(Util.legalLogin()==null){
//                    startActivity(LoginActivity.class);
//                    return;
//                }
                String detailurl = bean.getDetailurl();
                if (StringUtil.isEmptyOrNull(detailurl)) {//url为空，不跳转
                    return;
                }
                String type = bean.getType();
                if ("3".equalsIgnoreCase(type)) {
                    if ("0".equalsIgnoreCase(detailurl)) {//跳转到金融超市 - 更多模块
                        if (mOnBannerClickListener != null)
                            mOnBannerClickListener.skipToFinanceMore();
                    } else {//跳转到 金融超市-平台详情列表, url=pid
                        Intent intent = new Intent(getActivity(), FinanceSupermarketOfPlatformTargetsActivity.class);
                        intent.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA + "name", "");
                        intent.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA, detailurl);
                        intent.putExtra(Const.KEY_INTENT_JUMP_FROM_DATA, "晓投资");
                        getActivity().startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(getActivity(), XtzBannerDetailActivity.class);
                    intent.putExtra("title", bean.getDesc());
                    intent.putExtra("url", detailurl);
                    getActivity().startActivity(intent);
                }
            }
        });
    }

    /** 加载更多数据 */
    @Override
    public void onRefreshLoadMore() {
        isFirst = false;
        ++mCurrPage;
        homePagerProtocol.loadDataByPage(mCurrPage, homePageRequestListener);
    }

    /** 收件箱有消息提示事件 */
    @Subscribe
    public void onEventMessageBoxHint(MessageBoxHintEvent event) {
        mIvMsgIcon.setImageResource(R.mipmap.xtz_has_message_icon);
    }

    @Subscribe
    public void onLoginStateEvent(LoginStateEvent event) {
        if (event != null && !isDetached()) {
            mLoginBean = Util.legalLogin();
            if (event.hasLogin) {
                mCurrPage = 1;
                homePagerProtocol.loadDataByPage(mCurrPage, homePageRequestListener);
            }
            if (mLoginBean != null) {
                ImgLoadUtil.loadImgByPath(mLoginBean.getAvatarUrl(), mIvCircleIcon, R.mipmap.default_user_icon);
                mIvCircleIcon.setVisibility(View.VISIBLE);
                mIvPersonIcon.setVisibility(View.GONE);
            } else {
                mIvPersonIcon.setImageResource(R.mipmap.personal_info_icon);
                mIvPersonIcon.setVisibility(View.VISIBLE);
                mIvCircleIcon.setVisibility(View.GONE);
                mIvMsgIcon.setImageResource(R.mipmap.xtz_not_message_icon);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mBannerHelper != null)
            mBannerHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mBannerHelper != null)
            mBannerHelper.onPause();
    }

    @Override
    public void onDestroy() {
        homePagerProtocol = null;
        if (mBannerHelper != null)
            mBannerHelper.onDestroy();
        super.onDestroy();
    }

//    @Override
//    public void onRefresh() {
//        mCurrPage = 1;
//        isFirst = true;
//        mSwipeToRefresh.setRefreshing(true);
//        homePagerProtocol.loadDataByPage(mCurrPage, homePageRequestListener);
//        UIUtil.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mSwipeToRefresh.setRefreshing(false);
//            }
//        }, 10 * 1000);
//
//    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem == 0 && view.getChildAt(firstVisibleItem).getTop() == 0) {
            mSwipeToRefresh.setEnabled(true);
        } else {
            mSwipeToRefresh.setEnabled(false);
        }
    }

    /** banner点击进入金融超市--更多接口 */
    public interface OnBannerClickListener {
        void skipToFinanceMore();
    }

    private OnBannerClickListener mOnBannerClickListener;

    /** 设置监听回调 */
    public void setOnBannerClickListener(OnBannerClickListener listener) {
        this.mOnBannerClickListener = listener;
    }

}

