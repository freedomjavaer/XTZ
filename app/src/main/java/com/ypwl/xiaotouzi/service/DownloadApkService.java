package com.ypwl.xiaotouzi.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.base.BaseService;
import com.ypwl.xiaotouzi.bean.AppUpdateBean;
import com.ypwl.xiaotouzi.common.Configs;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.interf.IRequestCallback;
import com.ypwl.xiaotouzi.manager.net.NetHelper;
import com.ypwl.xiaotouzi.ui.activity.UpdateDialogActivity;
import com.ypwl.xiaotouzi.utils.CacheUtils;
import com.ypwl.xiaotouzi.utils.LogUtil;
import com.ypwl.xiaotouzi.utils.NetworkUtils;
import com.ypwl.xiaotouzi.utils.SignUtil;
import com.ypwl.xiaotouzi.utils.StorageUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;

import java.io.File;


/**
 * 下载apk通知栏显示进度服务<br/>
 *
 * Created by lzj on 2016/2/15.
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public class DownloadApkService extends BaseService {

    private String mApkTempPath = StorageUtil.getXtzCacheRootPath(UIUtil.getContext()) + Configs.APKNAMETEMP;
    private String mApkPath = StorageUtil.getXtzCacheRootPath(UIUtil.getContext()) + Configs.APKNAME;
    private File mNewApkFile;
    private AppUpdateBean mAppUpdateBean;

    private int mUpdateNotifId = 900710;//just a number of my birthday
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mNotificationBuilder;
    private Notification mNotification;
    private int progress = 0;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        if (intent == null) {
            stopSelf();
            return;
        }
        mAppUpdateBean = intent.getParcelableExtra(UpdateDialogActivity.KEY_JUMP_UPDATE_DOWNLOAD);
        if (mAppUpdateBean == null || mAppUpdateBean.getApkDownloadUrl() == null) {
            NotifBarFailed("下载数据有误");
            return;
        }

        if (!NetworkUtils.isNetworkConnected(this)) {
            NotifBarFailed("请检查网络");
            return;
        }
        CacheUtils.putBoolean(Const.KEY_UPDATE_FORCING_DOWNLOADING, mAppUpdateBean.getApkUpdateState() == UpdateDialogActivity.SHOW_UPDATE_FORCE);

        NetHelper.downloadSilence(mAppUpdateBean.getApkDownloadUrl(), mApkTempPath, new IRequestCallback<File>() {
            @Override
            public void onStart() {
                UIUtil.post(new Runnable() {
                    @Override
                    public void run() {
                        if (progress == 100) {
                            UIUtil.removeCallbacksFromMainLooper(this);
                        } else {
                            NotifBarProgress(progress);
                            UIUtil.postDelayed(this, 800);
                        }
                    }
                });
            }

            @Override
            public void onProgressUpdate(long writedSize, long totalSize, boolean completed) {
                progress = (int) (writedSize * 100 / totalSize);
//                LogUtil.e("leo", "progress: writedSize=" + writedSize + " ,totalSize=" + totalSize + " progress=" + progress);
            }

            @Override
            public void onFailure(Exception e) {
                CacheUtils.putBoolean(Const.KEY_UPDATE_FORCING_DOWNLOADING, false);
                deleteTempFile();
                NotifBarFailed("请检查网络");
            }

            @Override
            public void onSuccess(File file) {
                CacheUtils.putBoolean(Const.KEY_UPDATE_FORCING_DOWNLOADING, false);
                mNewApkFile = new File(mApkPath);
                if (mNewApkFile.exists()) {
                    mNewApkFile.delete();
                }
                file.renameTo(mNewApkFile);
                PackageInfo packageinfo = getPackageManager().getPackageArchiveInfo(mApkPath, PackageManager.GET_ACTIVITIES);
                if (packageinfo != null) {
                    NotifBarFinish();
                } else {
                    NotifBarFailed("下载资源错误");
                }
            }
        });
    }

    private void deleteTempFile() {
        File file = new File(mApkTempPath);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 下载中
     */
    private void NotifBarProgress(int num) {
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        }
        if (mNotificationBuilder == null) {
            mNotificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setOnlyAlertOnce(true).setOngoing(true)
                    .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(), 0))
                    .setProgress(100, 0, false)
                    .setContentTitle(UIUtil.getString(R.string.app_name) + " 正在下载…" + "0%");
        }
        mNotificationBuilder.setProgress(100, num, false)
                .setContentTitle(UIUtil.getString(R.string.app_name) + " 正在下载…" + num + "%");

        mNotification = mNotificationBuilder.build();
        mNotificationManager.notify(mUpdateNotifId, mNotification);
    }

    /**
     * 下载完成
     */
    private void NotifBarFinish() {
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        }
        mNotificationManager.cancel(mUpdateNotifId);
        if (mNotification != null) {
            mNotification = null;
        }
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(mNewApkFile), "application/vnd.android.package-archive");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        mNotificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOnlyAlertOnce(true).setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setContentTitle(UIUtil.getString(R.string.app_name) + " 下载完成")
                .setContentText("点击安装");
        mNotification = mNotificationBuilder.build();
        mNotificationManager.notify(mUpdateNotifId, mNotification);

        //  下载完成的文件进行md5校验，通过再安装
        String downloadFileMd5 = SignUtil.md5EncodeFile(mNewApkFile);
        if (!downloadFileMd5.equalsIgnoreCase(mAppUpdateBean.getApkMd5())) {
            NotifBarFailed("下载资源被串改");
            if (mNewApkFile.exists()) {
                mNewApkFile.delete();
            }
            return;
        }

        //下载完成自动安装
        Intent intentForInstall = new Intent();
        try {
            intentForInstall.setAction(Intent.ACTION_VIEW);
            intentForInstall.setDataAndType(Uri.fromFile(mNewApkFile), "application/vnd.android.package-archive");
            intentForInstall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intentForInstall);
        } catch (Exception ex) {
            LogUtil.e(TAG, "Exception : " + ex.getMessage());
        }
        stopSelf();
    }

    /**
     * 下载失败
     */
    private void NotifBarFailed(String msg) {
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        }
        mNotificationManager.cancel(mUpdateNotifId);
        if (mNotification != null) {
            mNotification = null;
        }
        mNotificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOnlyAlertOnce(true).setAutoCancel(true)
//                .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(), 0))
                .setContentTitle(UIUtil.getString(R.string.app_name) + " 下载失败")
                .setContentText(msg);
        mNotification = mNotificationBuilder.build();
        mNotificationManager.notify(mUpdateNotifId, mNotification);

        deleteTempFile();
        stopSelf();
    }

}
