package com.ypwl.xiaotouzi.event;

import com.ypwl.xiaotouzi.bean.ContinualTallyForFilterBean;
import com.ypwl.xiaotouzi.bean.ContinualTallyForFilterSelectPlatformBean;

/**
 * 项目名:	XTZ
 * 包名:	com.ypwl.xiaotouzi.event
 * 类名:	ContinualTallyForFilterEvent
 * 作者:	罗霄
 * 创建时间:	2016/4/12 11:52
 * <p/>
 * 描述:	流水资产 ==> 筛选 ==> 选择平台的事件
 * <p/>
 * svn版本:	$Revision: 13887 $
 * 更新人:	$Author: luoxiao $
 * 更新时间:	$Date: 2016-04-13 13:06:45 +0800 (周三, 13 四月 2016) $
 * 更新描述:	${TODO}
 */
public class ContinualTallyForFilterSelectPlatformEvent {

    public final ContinualTallyForFilterSelectPlatformBean mBean;

    public ContinualTallyForFilterSelectPlatformEvent(ContinualTallyForFilterSelectPlatformBean bean){
        this.mBean = bean;
    }

}
