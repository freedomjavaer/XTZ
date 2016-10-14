package com.ypwl.xiaotouzi.bean;

/**
 * 项目名:	XTZ
 * 包名:	com.ypwl.xiaotouzi.bean
 * 类名:	ContinualTallyListBean
 * 作者:	罗霄
 * 创建时间:	2016/4/12 10:38
 * <p/>
 * 描述:	流水资产 ==> 筛选 ==> 选择平台
 * <p/>
 * svn版本:	$Revision: 13887 $
 * 更新人:	$Author: luoxiao $
 * 更新时间:	$Date: 2016-04-13 13:06:45 +0800 (周三, 13 四月 2016) $
 * 更新描述:	${TODO}
 */
public class ContinualTallyForFilterSelectPlatformBean {
//        pid	平台ID
//        p_name	平台名称

        private String pid;
        private String p_name;
        private String initial; //首字母

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

        public String getInitial() {
                return initial;
        }

        public void setInitial(String initial) {
                this.initial = initial;
        }
}
