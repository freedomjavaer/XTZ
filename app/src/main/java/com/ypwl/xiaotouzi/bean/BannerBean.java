package com.ypwl.xiaotouzi.bean;


/**
 * function : banner实体数据对象.
 * <p/>
 * Created by lzj on 2015/12/30.
 */
public class BannerBean {
    private int testImgResId;
    private String imgurl;
    private String desc;
    private String detailurl;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;

    public BannerBean(int testImgResId, String imgurl, String desc, String detailurl, String type) {
        this.testImgResId = testImgResId;
        this.imgurl = imgurl;
        this.desc = desc;
        this.detailurl = detailurl;
        this.type = type;
    }

    public int getTestImgResId() {
        return testImgResId;
    }

    public void setTestImgResId(int testImgResId) {
        this.testImgResId = testImgResId;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDetailurl() {
        return detailurl;
    }

    public void setDetailurl(String detailurl) {
        this.detailurl = detailurl;
    }
}
