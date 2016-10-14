package com.ypwl.xiaotouzi.view.backactivity.app;


import com.ypwl.xiaotouzi.view.backactivity.SwipeBackLayout;

@SuppressWarnings("unused")
public interface SwipeBackActivityBase {
    /**
     * @return the SwipeBackLayout associated with this activity.
     */
    SwipeBackLayout getSwipeBackLayout();

    /**
     * Scroll out contentView and finish the activity
     */
    void scrollToFinishActivity();

}
