package com.ypwl.xiaotouzi.view.swipelistview;


/**
 * function : 滑动条目接口
 * </p>
 * Created by lzj on 2015/11/23.
 */
public interface SwipeLayoutInterface {

    SwipeLayout.Status getCurrentStatus();

    void close();

    void open();
}
