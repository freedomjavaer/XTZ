package com.ypwl.xiaotouzi.interf;

/**
 * function:UI显示监听，由Activity实现此接口，数据适配器数据处理情况通过该接口回调给Activity
 *
 * <p>Created by lzj on 2016/3/17.</p>
 */
public interface UIAdapterListener {
    /** 正在加载数据 */
    void isLoading();

    /** 总数据个数变化 */
    void dataCountChanged(int count);

    /** 加载数据完毕 */
    void loadFinished(int count);

}
