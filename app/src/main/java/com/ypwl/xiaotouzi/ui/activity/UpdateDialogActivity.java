package com.ypwl.xiaotouzi.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.XtzApp;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.bean.AppUpdateBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.service.DownloadApkService;
import com.ypwl.xiaotouzi.utils.CacheUtils;
import com.ypwl.xiaotouzi.utils.DateTimeUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.view.dialog.CustomDialog;

import java.io.File;
import java.util.Date;


/**
 * app升级提示对话框<br/>
 *
 * Created by lzj on 2016/2/15.
 */
public class UpdateDialogActivity extends BaseActivity {

    /** key-跳转到更新提醒界面传递数据 */
    public static final String KEY_JUMP_UPDATE_HINT = "KEY_JUMP_UPDATE_HINT";
    /** key-跳转到下载APK服务传递数据 */
    public static final String KEY_JUMP_UPDATE_DOWNLOAD = "KEY_JUMP_UPDATE_DOWNLOAD";

    private CustomDialog mCustomDialog;

    /** 建议更新 */
    public static final int SHOW_UPDATE_ADVICE = 1;
    /** 强制更新 */
    public static final int SHOW_UPDATE_FORCE = 2;
    /** 马上安装 */
    public static final int SHOW_INSTALL_NOW = 3;

    private AppUpdateBean mAppUpdateBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(0, R.anim.activity_gradient_in);
        getWindow().setDimAmount(0.6f);
        mAppUpdateBean = getIntent().getParcelableExtra(KEY_JUMP_UPDATE_HINT);
        if (mAppUpdateBean == null) {
            UIUtil.showToastShort("暂无更新");
            finish();
            return;
        }
        initView();
    }

    private void initView() {
        int mCurrUpdateType;
        if (mAppUpdateBean.getApkNewpath() != null && new File(mAppUpdateBean.getApkNewpath()).exists()) {
            mCurrUpdateType = SHOW_INSTALL_NOW;
        } else if (mAppUpdateBean.getApkUpdateState() == 1) {//建议更新
            mCurrUpdateType = SHOW_UPDATE_ADVICE;
        } else if (mAppUpdateBean.getApkUpdateState() == 2) {//强制更新
            mCurrUpdateType = SHOW_UPDATE_FORCE;
        } else {
            UIUtil.showToastShort("暂无更新");
            finish();
            return;
        }

        CustomDialog.AlertBuilder builder = new CustomDialog.AlertBuilder(this)
                .setTitleText("更新提示")
                .setContentText(mAppUpdateBean.getApkUpdateTips())
                .setCanceled(false)
                .setCanceledOnTouchOutside(false);

        switch (mCurrUpdateType) {
            case SHOW_UPDATE_FORCE://强制更新
                builder.setPositiveBtn("立即更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        UIUtil.showToastShort(UIUtil.getString(R.string.app_name) + "正在后台下载");
                        startDownloadApk();
                        XtzApp.getApplication().removeAll();
                        finish();
                    }
                });
                break;

            case SHOW_INSTALL_NOW://马上安装
                builder.setNegativeBtn("下次再说", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
                builder.setPositiveBtn("马上安装", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        installApkNow();
                    }
                });
                break;

            case SHOW_UPDATE_ADVICE://建议更新
                String lastIgnoreDate = CacheUtils.getString(Const.KEY_DATE_OF_UPDATE_IGNORE, null);
                String currDate = DateTimeUtil.formatDateTime(new Date(), DateTimeUtil.DF_YYYY_MM_DD);
                if (lastIgnoreDate != null && currDate.equals(lastIgnoreDate)) {
                    finish();
                    return;
                }
                builder.setNegativeBtn("忽略", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String dateTime = DateTimeUtil.formatDateTime(new Date(), DateTimeUtil.DF_YYYY_MM_DD);
                        CacheUtils.putString(Const.KEY_DATE_OF_UPDATE_IGNORE, dateTime);
                        dialog.dismiss();
                        finish();
                    }
                });
                builder.setPositiveBtn("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        UIUtil.showToastShort(UIUtil.getString(R.string.app_name) + "正在后台下载");
                        startDownloadApk();
                        finish();
                    }
                });
                break;
        }
        mCustomDialog = builder.create();
        mCustomDialog.show();
    }

    public void startDownloadApk() {
        Intent intent = new Intent(UIUtil.getContext(), DownloadApkService.class);
        intent.putExtra(KEY_JUMP_UPDATE_DOWNLOAD, mAppUpdateBean);
        UIUtil.getContext().startService(intent);
    }

    public void installApkNow() {
        PackageInfo packageinfo = UIUtil.getContext().getPackageManager().getPackageArchiveInfo(mAppUpdateBean.getApkNewpath(), PackageManager.GET_ACTIVITIES);
        if (packageinfo == null) {
            UIUtil.showToastShort("安装包已损毁，正在重新下载");
            startDownloadApk();
            finish();
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(new File(mAppUpdateBean.getApkNewpath())), "application/vnd.android.package-archive");
        startActivity(intent);
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(0, R.anim.activity_gradient_out);
    }

    @Override
    protected void onDestroy() {
        if (mCustomDialog != null) {
            if (mCustomDialog.isShowing()) {
                mCustomDialog.dismiss();
            }
            mCustomDialog = null;
        }
        super.onDestroy();
    }

}
