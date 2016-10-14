package com.ypwl.xiaotouzi.event;

/**
 * 手势密码状态变化事件
 *
 * Created by lzj on 2016/4/29.
 */
public class GpswChangeEvent {
    public boolean gpswState;

    public GpswChangeEvent(boolean gpswState) {
        this.gpswState = gpswState;
    }
}
