package com.ypwl.xiaotouzi.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 平台视频播放信息对象
 */
public class VideoInfoBean implements Parcelable {
    public String name;//视频名称
    public String url;//视频地址
    public String opentime;//开放时间，如果opentime不为空，则当前处于视频关闭时间，前端应给予提示

    public boolean hasChoose;

    public boolean isHasChoose() {
        return hasChoose;
    }

    public void setHasChoose(boolean hasChoose) {
        this.hasChoose = hasChoose;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getOpentime() {
        return opentime;
    }

    public void setOpentime(String opentime) {
        this.opentime = opentime;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.url);
        dest.writeString(this.opentime);
        dest.writeByte(hasChoose ? (byte) 1 : (byte) 0);
    }

    public VideoInfoBean() {
    }

    protected VideoInfoBean(Parcel in) {
        this.name = in.readString();
        this.url = in.readString();
        this.opentime = in.readString();
        this.hasChoose = in.readByte() != 0;
    }

    public static final Parcelable.Creator<VideoInfoBean> CREATOR = new Parcelable.Creator<VideoInfoBean>() {
        public VideoInfoBean createFromParcel(Parcel source) {
            return new VideoInfoBean(source);
        }

        public VideoInfoBean[] newArray(int size) {
            return new VideoInfoBean[size];
        }
    };
}
