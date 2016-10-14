package com.ypwl.xiaotouzi.view.scrollview;

/**
 * 自定义ScrollView滚动监听
 * Created by PDK on 2015/12/11.
 */
public interface ScrollViewListener {
    void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy);
}
