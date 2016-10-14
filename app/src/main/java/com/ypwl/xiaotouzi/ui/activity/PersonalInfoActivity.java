package com.ypwl.xiaotouzi.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.bean.CommonBean;
import com.ypwl.xiaotouzi.bean.LoginBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.common.ServerStatus;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.common.UmengEventID;
import com.ypwl.xiaotouzi.event.ExitOutEvent;
import com.ypwl.xiaotouzi.event.LoginStateEvent;
import com.ypwl.xiaotouzi.event.UserInfoChangeEvent;
import com.ypwl.xiaotouzi.interf.INetRequestListener;
import com.ypwl.xiaotouzi.interf.IRequestCallback;
import com.ypwl.xiaotouzi.manager.EventHelper;
import com.ypwl.xiaotouzi.manager.ShareAuthManager;
import com.ypwl.xiaotouzi.manager.UmengEventHelper;
import com.ypwl.xiaotouzi.manager.net.NetHelper;
import com.ypwl.xiaotouzi.netprotocol.SaveNickNameProtocol;
import com.ypwl.xiaotouzi.utils.CacheUtils;
import com.ypwl.xiaotouzi.utils.DateTimeUtil;
import com.ypwl.xiaotouzi.utils.FileUtil;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.ImageUtils;
import com.ypwl.xiaotouzi.utils.ImgLoadUtil;
import com.ypwl.xiaotouzi.utils.LogUtil;
import com.ypwl.xiaotouzi.utils.PopuViewUtil;
import com.ypwl.xiaotouzi.utils.StorageUtil;
import com.ypwl.xiaotouzi.utils.StringUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.Util;
import com.ypwl.xiaotouzi.view.CircleImageView;
import com.ypwl.xiaotouzi.view.ContainsEmojiEditText;
import com.ypwl.xiaotouzi.view.dialog.CustomDialog;
import com.ypwl.xiaotouzi.view.dialog.lib.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * function :主页---我（个人信息页面）
 * <p/>
 * Created by tengtao on 2016/3/16.
 */
public class PersonalInfoActivity extends BaseActivity implements View.OnClickListener {

    private CircleImageView mInfoIcon;
    private TextView mTvPhoneNumber;
    private TextView mTvLoginOrLogout;
    private TextView mTvNickName, mTvBack;
    private ImageView mBindPhoneArrow;
    private KProgressHUD mDialogLoading;
    private CustomDialog mDialog;
    private LoginBean mLoginBean;
    @SuppressWarnings("FieldCanBeLocal")
    private String loacalAvatarPath;
    /** 拍照获取到的照片Uri */
    private Uri mCameraOutputUri = null;
    /** 相册或拍照获取到的照片处理后的文件Uri */
    private Uri mUriFileForCrop;
    /** 选择相册图片请求码 */
    private static final int CODE_REQUEST_IMAGE_PICK = 0x001;
    /** 裁剪相册图片请求码 */
    private static final int CODE_REQUEST_IMAGE_CROP = 0x002;
    /** 相机拍照请求码 */
    private static final int CODE_REQUEST_CAMERA = 0x003;
    private SaveNickNameProtocol mSaveNickNameProtocol;
    private String newUserName;
    private CustomDialog mNickNameDialog;
    private ContainsEmojiEditText mEmojiEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        //dialog
        mDialogLoading = KProgressHUD.create(this);
        initView();
        initData();
    }

    private void initView() {
        TextView mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvTitle.setText("个人资料");
        mTvBack = (TextView) findViewById(R.id.tv_title_back);
        mTvBack.setText("晓投资");
        mInfoIcon = (CircleImageView) findViewById(R.id.iv_personal_info_icon);
        mTvPhoneNumber = (TextView) findViewById(R.id.info_layout_item_bind_phone_content);
        mTvNickName = (TextView) findViewById(R.id.tv_personal_info_nickname);
        mBindPhoneArrow = (ImageView) findViewById(R.id.info_layout_item_bind_phone_arrow);
        mTvLoginOrLogout = (TextView) findViewById(R.id.tv_login_or_logout);

        findViewById(R.id.layout_item_bind_other_account).setOnClickListener(this);
        findViewById(R.id.layout_title_back).setOnClickListener(this);
        findViewById(R.id.iv_personal_info_change_icon).setOnClickListener(this);
        mInfoIcon.setOnClickListener(this);
        mTvLoginOrLogout.setOnClickListener(this);
    }

    private void initData() {
        mLoginBean = Util.legalLogin();
        if (null == mLoginBean) {
            UIUtil.showToastLong("登录信息有误");
            this.finish();
            return;
        }
        //设置头像
        if (!StringUtil.isEmptyOrNull(mLoginBean.getAvatarUrl())) {
            ImgLoadUtil.loadImgByPath(mLoginBean.getAvatarUrl(), mInfoIcon, R.mipmap.pic_027);
        } else {
            ImgLoadUtil.loadImgByPath(null, mInfoIcon, R.mipmap.pic_027);
        }
        //设置用户名
        mTvNickName.setText(StringUtil.isEmptyOrNull(mLoginBean.getNickname()) ? "" : mLoginBean.getNickname());
//        mTvNickName.setSelection(mTvNickName.getText().toString().length());

        findViewById(R.id.layout_personal_info_bind_phone).setOnClickListener(this);
        findViewById(R.id.layout_personal_info_bind_phone).setClickable(true);
        //设置手机号绑定信息
        String phoneNumber = mLoginBean.getPhone();
        if (!StringUtil.isEmptyOrNull(phoneNumber)) {
            findViewById(R.id.layout_personal_info_bind_phone).setOnClickListener(null);
            findViewById(R.id.layout_personal_info_bind_phone).setClickable(false);
            mTvPhoneNumber.setText(phoneNumber);
            mBindPhoneArrow.setVisibility(View.GONE);
        }
        mTvLoginOrLogout.setText(mLoginBean == null ? "登录" : "退出登录");
    }

    /** 手机号绑定后信息改变的事件接收 */
    @Subscribe
    public void onUserInfoChangeEvent(UserInfoChangeEvent event) {
        if (event != null && event.infoChange) {
            initData();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_title_back://返回
                finish();
                break;
            case R.id.layout_personal_info_bind_phone://绑定手机号
                startActivity(BindPhoneActivity.class);
                break;
            case R.id.layout_item_bind_other_account://绑定社交账号
                startActivity(AccountBindActivity.class);
                break;
            case R.id.iv_personal_info_icon://更换头像
                chooseAvatarFrom();
                break;
            case R.id.iv_personal_info_change_icon://更改昵称
                showNickNameDialog();
                break;
            case R.id.tv_login_or_logout://退出、登录
                String loginStateStr = ((TextView) v).getText().toString();
                if (getString(R.string.fragment_myinfo_login).equals(loginStateStr)) {
                    startActivity(LoginActivity.class);
                } else {
                    showDialogForHintExitLogin();
                }
                break;
        }
    }

    /** 修改昵称 */
    private void showNickNameDialog() {
        View customView = View.inflate(mActivity, R.layout.layout_change_nickname_view, null);
        mEmojiEditText = (ContainsEmojiEditText) customView.findViewById(R.id.tv_nickname);
        mNickNameDialog = new CustomDialog.AlertBuilder(mActivity).setCustomContentView(customView)
                .setTitleLayoutVisibility(View.GONE)
                .setPositiveBtn("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newUserName = mEmojiEditText.getText().toString().trim();
                        if (newUserName.length() == 0) {
                            mEmojiEditText.startAnimation(AnimationUtils.loadAnimation(UIUtil.getContext(), R.anim.shake));
                            return;
                        }
                        mDialogLoading.show();
                        mSaveNickNameProtocol = new SaveNickNameProtocol();
                        mSaveNickNameProtocol.loadData(newUserName, mNetRequestListener);
                    }
                }).create();
        mNickNameDialog.show();
        /**调用系统输入法，延时保证dialog加载完成*/
        UIUtil.postDelayed(new Runnable() {
            @Override
            public void run() {
                mEmojiEditText.setFocusable(true);
                mEmojiEditText.requestFocus();
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(mEmojiEditText, 0);
            }
        }, 200);
    }

    /** 保存昵称 */
    private INetRequestListener<CommonBean> mNetRequestListener = new INetRequestListener<CommonBean>() {
        @Override
        public void netRequestCompleted() {
            mDialogLoading.dismiss();
        }

        @Override
        public void netRequestSuccess(CommonBean bean, boolean isSuccess) {
            if (bean != null && isSuccess) {
                UIUtil.showToastShort(getString(R.string.user_info_change_save_hint_success));
                mLoginBean.setNickname(newUserName);//设置内存中的LoginBean
                CacheUtils.putString(Const.KEY_LOGIN_USER, JSON.toJSONString(mLoginBean));//改变后的LoginBean转换成jsonString,存入本地
                mTvNickName.setText(newUserName);
                mNickNameDialog.dismiss();
            }
        }
    };

    /** 弹出对话框选择修改头像的来源 */
    private void chooseAvatarFrom() {
//        String[] photoChoice = new String[]{getString(R.string.user_info_change_avatar_from_album), getString(R.string.user_info_change_avatar_from_camera)};
//        ListView lv = new ListView(this);
//        lv.setAdapter(new ArrayAdapter<>(this, R.layout.dialog_listview_item_text, Arrays.asList(photoChoice)));
//        final CustomDialog dialog = new CustomDialog.AlertBuilder(this).setTitleText("更换头像").setTitleTextColor(R.color.black).setCustomContentView(lv).create();
//        dialog.show();
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                dialog.dismiss();
//                Intent intent;
//                switch (position) {
//                    case 0: // 点击 >> 相册
//                        intent = new Intent(Intent.ACTION_PICK, null);
//                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//                        startActivityForResult(intent, CODE_REQUEST_IMAGE_PICK);
//                        break;
//                    case 1: // 点击 >>  拍照
//                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        File file = new File(StorageUtil.getTempPath(mActivity), DateTimeUtil.formatDateTime(new Date(), "yyyyMMddHHmmss") + ".jpg");
//                        mCameraOutputUri = Uri.fromFile(file);
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraOutputUri);
//                        startActivityForResult(intent, CODE_REQUEST_CAMERA);
//                        break;
//                }
//            }
//        });

        LinkedList<String> photoChoice = new LinkedList<>();
        photoChoice.add(getString(R.string.user_info_change_avatar_from_album));
        photoChoice.add(getString(R.string.user_info_change_avatar_from_camera));

        PopuViewUtil dialog = new PopuViewUtil(mActivity, photoChoice, new PopuViewUtil.OnClickCountsListener() {
            @Override
            public void onClick(int position, String str) {
                Intent intent;
                switch (position) {
                    case 0: // 点击 >> 相册
                        intent = new Intent(Intent.ACTION_PICK, null);
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(intent, CODE_REQUEST_IMAGE_PICK);
                        break;
                    case 1: // 点击 >>  拍照
                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File file = new File(StorageUtil.getTempPath(mActivity), DateTimeUtil.formatDateTime(new Date(), "yyyyMMddHHmmss") + ".jpg");
                        mCameraOutputUri = Uri.fromFile(file);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraOutputUri);
                        startActivityForResult(intent, CODE_REQUEST_CAMERA);
                        break;
                }
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ShareAuthManager.getInstance().onActivityResult(this, requestCode, resultCode, data);
        switch (requestCode) {
            case CODE_REQUEST_IMAGE_PICK:// 裁剪相册图片
                if (data != null && null != data.getData()) {
                    mUriFileForCrop = ImageUtils.startImageCrop(mActivity, data.getData(), 1, 1, UIUtil.dip2px(200), UIUtil.dip2px(200), CODE_REQUEST_IMAGE_CROP);
                }
                break;
            case CODE_REQUEST_CAMERA:// 裁剪相机拍照
                if (mCameraOutputUri != null) {
                    mUriFileForCrop = ImageUtils.startImageCrop(mActivity, mCameraOutputUri, 1, 1, UIUtil.dip2px(60), UIUtil.dip2px(60), CODE_REQUEST_IMAGE_CROP);
                }
                break;
            case CODE_REQUEST_IMAGE_CROP:// 从裁剪获取到图片返回
                if (data != null) {
                    loacalAvatarPath = ImageUtils.saveImageFromUri(mActivity, mUriFileForCrop, StorageUtil.getPictureDirPath(mActivity), true);
                    if (StringUtil.isEmptyOrNull(loacalAvatarPath)) {
                        if (StringUtil.isEmptyOrNull(mLoginBean.getAvatarUrl())) {
                            Picasso.with(mActivity).load(R.mipmap.pic_027).into(mInfoIcon);
                        } else {
                            UIUtil.showToastLong(getString(R.string.user_info_change_avatar_from_hint_failed));
                        }
                        ImgLoadUtil.loadImgByPath(mLoginBean.getAvatarUrl(), mInfoIcon, R.mipmap.pic_027);
                    } else {
                        Picasso.with(mActivity).load(loacalAvatarPath).placeholder(R.mipmap.pic_027).into(mInfoIcon);
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
                                ImgLoadUtil.loadImgByPath(avatarUrl, mInfoIcon, R.mipmap.pic_027);
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

    /** 弹出对话框提示是否退出登录 */
    private void showDialogForHintExitLogin() {
        if (mDialog == null) {
            mDialog = new CustomDialog.AlertBuilder(this)
                    .setTitleText(getString(R.string.app_name))
                    .setContentText(getString(R.string.fragment_myinfo_exit_login_hint))
                    .setContentTextGravity(Gravity.CENTER)
                    .setPositiveBtn(getString(R.string.btn_text_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mDialog.dismiss();
                            UmengEventHelper.onEvent(UmengEventID.MoreLogout);
                            EventHelper.post(new ExitOutEvent());
                            Util.clearLoginInfo();
                            startActivity(LoginActivity.class);//退到注册登录页面
                            finish();
                            ShareAuthManager.authDelete();

                        }
                    })
                    .setNegativeBtn(getString(R.string.btn_text_cancle), null)
                    .create();
        }
        mDialog.show();
    }

    @Override
    protected void onDestroy() {
        mDialogLoading = null;
        mDialog = null;
        mSaveNickNameProtocol = null;
        super.onDestroy();
    }
}
