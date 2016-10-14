package com.ypwl.xiaotouzi.im;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * function : 消息模型数据对象.
 *
 * Created by lzj on 2016/1/31.
 */
public class YMessage implements Parcelable {

    /** 消息类型--推送消息 */
    public static final int TYPE_PUSH_MESSAGE = 0;
    /** 消息类型-- 异地登录 */
    public static final int TYPE_REMOTE_LOGIN = 1;

    private int mType;
    private String mMessage;

    public int getType() {
        return mType;
    }

    public void setType(int mType) {
        this.mType = mType;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mType);
        dest.writeString(this.mMessage);
    }

    public YMessage() {
    }

    protected YMessage(Parcel in) {
        this.mType = in.readInt();
        this.mMessage = in.readString();
    }

    public static final Creator<YMessage> CREATOR = new Creator<YMessage>() {
        public YMessage createFromParcel(Parcel source) {
            return new YMessage(source);
        }

        public YMessage[] newArray(int size) {
            return new YMessage[size];
        }
    };

    @Override
    public String toString() {
        return "YMessage{" +
                "mType=" + mType +
                ", mMessage='" + mMessage + '\'' +
                '}';
    }
}
