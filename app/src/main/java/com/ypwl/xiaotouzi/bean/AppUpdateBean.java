package com.ypwl.xiaotouzi.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * app更新数据查询模式对象.
 *
 * Created by lzj on 2016/2/15.
 */
@SuppressWarnings("unused")
public class AppUpdateBean implements Parcelable {
    private int status;
    /** 1.建议更新，2.强制更新 */
    private int apkUpdateState;
    private int apkNewVersionCode;
    private String apkMd5;
    private String apkDownloadUrl;
    private String apkUpdateTips;

    private String apkNewpath = null;

    public AppUpdateBean() {
    }

    public int getApkNewVersionCode() {
        return apkNewVersionCode;
    }

    public void setApkNewVersionCode(int apkNewVersionCode) {
        this.apkNewVersionCode = apkNewVersionCode;
    }

    public String getApkMd5() {
        return apkMd5;
    }

    public void setApkMd5(String apkMd5) {
        this.apkMd5 = apkMd5;
    }

    public String getApkDownloadUrl() {
        return apkDownloadUrl;
    }

    public void setApkDownloadUrl(String apkDownloadUrl) {
        this.apkDownloadUrl = apkDownloadUrl;
    }

    public String getApkUpdateTips() {
        return apkUpdateTips;
    }

    public void setApkUpdateTips(String apkUpdateTips) {
        this.apkUpdateTips = apkUpdateTips;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getApkUpdateState() {
        return apkUpdateState;
    }

    public void setApkUpdateState(int apkUpdateState) {
        this.apkUpdateState = apkUpdateState;
    }

    public String getApkNewpath() {
        return apkNewpath;
    }

    public void setApkNewpath(String apkNewpath) {
        this.apkNewpath = apkNewpath;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.status);
        dest.writeInt(this.apkUpdateState);
        dest.writeInt(this.apkNewVersionCode);
        dest.writeString(this.apkMd5);
        dest.writeString(this.apkDownloadUrl);
        dest.writeString(this.apkUpdateTips);
        dest.writeString(this.apkNewpath);
    }

    protected AppUpdateBean(Parcel in) {
        this.status = in.readInt();
        this.apkUpdateState = in.readInt();
        this.apkNewVersionCode = in.readInt();
        this.apkMd5 = in.readString();
        this.apkDownloadUrl = in.readString();
        this.apkUpdateTips = in.readString();
        this.apkNewpath = in.readString();
    }

    public static final Parcelable.Creator<AppUpdateBean> CREATOR = new Parcelable.Creator<AppUpdateBean>() {
        @Override
        public AppUpdateBean createFromParcel(Parcel source) {
            return new AppUpdateBean(source);
        }

        @Override
        public AppUpdateBean[] newArray(int size) {
            return new AppUpdateBean[size];
        }
    };


    @Override
    public String toString() {
        return "AppUpdateBean{" +
                "status=" + status +
                ", apkUpdateState=" + apkUpdateState +
                ", apkNewVersionCode=" + apkNewVersionCode +
                ", apkMd5='" + apkMd5 + '\'' +
                ", apkDownloadUrl='" + apkDownloadUrl + '\'' +
                ", apkUpdateTips='" + apkUpdateTips + '\'' +
                ", apkNewpath='" + apkNewpath + '\'' +
                '}';
    }
}
