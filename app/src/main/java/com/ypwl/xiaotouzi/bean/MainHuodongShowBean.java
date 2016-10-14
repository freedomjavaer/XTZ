package com.ypwl.xiaotouzi.bean;

/**
 * function : 主页活动展示模式对象.
 * <p/>
 * Created by lzj on 2015/11/4.
 */
public class MainHuodongShowBean {

    /**
     * status : 0
     * id : data
     * title : 流量大放送
     * url : https://p2p.kinimi.com/index.php?c=activity&a=dataPage
     */

    private int status;
    private String id;
    private String title;
    private String url;

    public void setStatus(int status) {
        this.status = status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStatus() {
        return status;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }
}
