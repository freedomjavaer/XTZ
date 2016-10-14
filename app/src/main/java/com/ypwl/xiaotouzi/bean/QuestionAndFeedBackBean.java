package com.ypwl.xiaotouzi.bean;

import java.util.List;

/**
 * function : 常见问题和反馈数据对象
 * <p/>
 * Created by tengtao on 2016/5/3.
 */
public class QuestionAndFeedBackBean {

    /**
     * status : 0
     * list : [{"name":"软件常见问题","question":[{"tid":"3","title":"关于网贷评级和数据如何判定与获取？","content":"网贷平台的评级都是通过第三方平台获取，仅供投资人参考。已通过晓投资视频认证的平台，会有相应的调整。"},{"tid":"3","title":"记账后如何修改数据记录？","content":"在\u201c我的投资\u201d-\u201c资产\u201d- 进入\u201c标的详情\u201d，点击右上角\u201c...\u201d按钮，点击\u201c编辑\u201d然后修改数据保存即可。"},{"tid":"3","title":"记账后如何删除平台记录数据？","content":"有两种删除方式：\r\n1.在\u201c我的投资\u201d-\u201c资产\u201d-对需要删除的标的左划点击\u201c删除\u201d 按钮。\r\n2.在\u201c我的投资\u201d-\u201c资产\u201d-进入\u201c标的详情\u201d，点击右上角\u201c...\u201d 按钮，点击\u201c删除\u201d然后点击确定即可。"},{"tid":"3","title":"自动记账需要输入平台的账号密码安全吗？","content":"自动记账只需填写要记账平台的登录账号及密码即可完成数据同步，不关联交易密码，所以不存在任何安全问题。"},{"tid":"3","title":"晓投资数据同步到哪里？换了手机数据会保存吗？","content":"数据在联网状态下会自动同步到云服务器，即使更换了手机，仍可登录账号查看记账数据。"}]}]
     * ret_msg :
     */

    private int status;
    private String ret_msg;
    /**
     * name : 软件常见问题
     * question : [{"tid":"3","title":"关于网贷评级和数据如何判定与获取？","content":"网贷平台的评级都是通过第三方平台获取，仅供投资人参考。已通过晓投资视频认证的平台，会有相应的调整。"},{"tid":"3","title":"记账后如何修改数据记录？","content":"在\u201c我的投资\u201d-\u201c资产\u201d- 进入\u201c标的详情\u201d，点击右上角\u201c...\u201d按钮，点击\u201c编辑\u201d然后修改数据保存即可。"},{"tid":"3","title":"记账后如何删除平台记录数据？","content":"有两种删除方式：\r\n1.在\u201c我的投资\u201d-\u201c资产\u201d-对需要删除的标的左划点击\u201c删除\u201d 按钮。\r\n2.在\u201c我的投资\u201d-\u201c资产\u201d-进入\u201c标的详情\u201d，点击右上角\u201c...\u201d 按钮，点击\u201c删除\u201d然后点击确定即可。"},{"tid":"3","title":"自动记账需要输入平台的账号密码安全吗？","content":"自动记账只需填写要记账平台的登录账号及密码即可完成数据同步，不关联交易密码，所以不存在任何安全问题。"},{"tid":"3","title":"晓投资数据同步到哪里？换了手机数据会保存吗？","content":"数据在联网状态下会自动同步到云服务器，即使更换了手机，仍可登录账号查看记账数据。"}]
     */

    private List<ListBean> list;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRet_msg() {
        return ret_msg;
    }

    public void setRet_msg(String ret_msg) {
        this.ret_msg = ret_msg;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private String name;
        /**
         * tid : 3
         * title : 关于网贷评级和数据如何判定与获取？
         * content : 网贷平台的评级都是通过第三方平台获取，仅供投资人参考。已通过晓投资视频认证的平台，会有相应的调整。
         */

        private List<QuestionBean> question;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<QuestionBean> getQuestion() {
            return question;
        }

        public void setQuestion(List<QuestionBean> question) {
            this.question = question;
        }

        public static class QuestionBean {
            private String tid;
            private String title;
            private String content;

            public String getTid() {
                return tid;
            }

            public void setTid(String tid) {
                this.tid = tid;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }


            private boolean showContent;//是否显示内容
            private boolean isTotalTitle;//是否是总标题

            public boolean isTotalTitle() {
                return isTotalTitle;
            }

            public void setTotalTitle(boolean totalTitle) {
                isTotalTitle = totalTitle;
            }

            public boolean isShowContent() {
                return showContent;
            }

            public void setShowContent(boolean showContent) {
                this.showContent = showContent;
            }

        }
    }
}
