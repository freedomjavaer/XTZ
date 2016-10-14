package com.ypwl.xiaotouzi.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.ypwl.xiaotouzi.utils.GlobalUtils;

/**
 * 项目名:	XTZ
 * 包名:	com.ypwl.xiaotouzi.bean
 * 类名:	ContinualTallyForFilterBean
 * 作者:	罗霄
 * 创建时间:	2016/4/12 9:42
 * <p/>
 * 描述:	资产流水的筛选
 * <p/>
 * svn版本:	$Revision: 13978 $
 * 更新人:	$Author: luoxiao $
 * 更新时间:	$Date: 2016-04-14 20:22:48 +0800 (周四, 14 四月 2016) $
 * 更新描述:	${TODO}
 */
public class ContinualTallyForFilterBean implements Parcelable {
//    token	登录token
//    all	全部类型		0：未选， 1：选中
//    invest	投资		0：未选， 1：选中
//    all_return	所以回款		0：未选， 1：选中
//    last_return	最后一起回款		0：未选， 1：选中
//    starttime	开始时间
//    endtime	结束时间
//    pid	平台id		0：所有平台

    private String token = GlobalUtils.token;
    private String all = "1";
    private String invest = "1";
    private String all_return = "1";
    private String last_return = "0";
    private String starttime = "0";
    private String endtime;
    private String pid = "0";
    private String p_name;

    public String getToken() {
        return token;
    }

    public String getAll() {
        return all;
    }

    public void setAll(String all) {
        this.all = all;
    }

    public String getInvest() {
        return invest;
    }

    public void setInvest(String invest) {
        this.invest = invest;
    }

    public String getAll_return() {
        return all_return;
    }

    public void setAll_return(String all_return) {
        this.all_return = all_return;
    }

    public String getLast_return() {
        return last_return;
    }

    public void setLast_return(String last_return) {
        this.last_return = last_return;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.token);
        dest.writeString(this.all);
        dest.writeString(this.invest);
        dest.writeString(this.all_return);
        dest.writeString(this.last_return);
        dest.writeString(this.starttime);
        dest.writeString(this.endtime);
        dest.writeString(this.pid);
        dest.writeString(this.p_name);
    }

    public ContinualTallyForFilterBean() {
    }

    protected ContinualTallyForFilterBean(Parcel in) {
        this.token = in.readString();
        this.all = in.readString();
        this.invest = in.readString();
        this.all_return = in.readString();
        this.last_return = in.readString();
        this.starttime = in.readString();
        this.endtime = in.readString();
        this.pid = in.readString();
        this.p_name = in.readString();
    }

    public static final Creator<ContinualTallyForFilterBean> CREATOR = new Creator<ContinualTallyForFilterBean>() {
        public ContinualTallyForFilterBean createFromParcel(Parcel source) {
            return new ContinualTallyForFilterBean(source);
        }

        public ContinualTallyForFilterBean[] newArray(int size) {
            return new ContinualTallyForFilterBean[size];
        }
    };
}
