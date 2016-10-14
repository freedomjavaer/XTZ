package com.ypwl.xiaotouzi.bean;

import java.util.List;

/**
 * function : 网贷新闻详情对象
 * <p/>
 * Created by tengtao on 2016/3/15.
 */
public class NetCreditNewsDetailBean {

    /**
     * content : [{"addtime":"1459408420","content":"移动支付市场松动 支付宝占比份额缩水","source":"1","title":"移动支付市场松动 支付宝占比份额缩水"}]
     * list : [{"content":"什么？","headimage":"up_files/avatar/615785710ada6b8343.jpg","nickname":"我在京东买了两个坏手机","toname":"晓咪_1452830665","userid":"61578"}]
     * status : 0
     */

    private int status;
    /**
     * addtime : 1459408420
     * content : 移动支付市场松动 支付宝占比份额缩水
     * source : 1
     * title : 移动支付市场松动 支付宝占比份额缩水
     */

    private List<ContentBean> content;
    /**
     * content : 什么？
     * headimage : up_files/avatar/615785710ada6b8343.jpg
     * nickname : 我在京东买了两个坏手机
     * toname : 晓咪_1452830665
     * userid : 61578
     */

    private List<ListBean> list;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<ContentBean> getContent() {
        return content;
    }

    public void setContent(List<ContentBean> content) {
        this.content = content;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ContentBean {
        private String addtime;
        private String content;
        private String source;
        private String title;

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public static class ListBean {
        private String content;
        private String headimage;
        private String nickname;
        private String toname;
        private String userid;
        private String addtime;

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getHeadimage() {
            return headimage;
        }

        public void setHeadimage(String headimage) {
            this.headimage = headimage;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getToname() {
            return toname;
        }

        public void setToname(String toname) {
            this.toname = toname;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }
    }
}
