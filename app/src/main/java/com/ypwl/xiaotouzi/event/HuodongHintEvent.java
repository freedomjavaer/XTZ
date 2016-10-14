package com.ypwl.xiaotouzi.event;

/**
 * function : 优惠活动提示事件
 * </p>
 * Created by lzj on 2015/11/6.
 */
public class HuodongHintEvent {
    public int huodongNumber;

    public HuodongHintEvent(int huodongNumber) {
        this.huodongNumber = huodongNumber;
    }

    @Override
    public String toString() {
        return "HuodongHintEvent{" +
                "huodongNumber=" + huodongNumber +
                '}';
    }
}
