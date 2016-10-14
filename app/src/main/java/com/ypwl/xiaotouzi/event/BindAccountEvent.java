package com.ypwl.xiaotouzi.event;

/**
 * @author tengtao
 * @time ${DATA} 15:02
 * @des ${记账绑定账号事件}
 */
public class BindAccountEvent {

    private boolean isFinish;//绑定帐号页面是否关闭

    public BindAccountEvent(boolean isFinish){
        this.isFinish = isFinish;
    }

    public boolean isFinish(){
        return isFinish;
    }
}
