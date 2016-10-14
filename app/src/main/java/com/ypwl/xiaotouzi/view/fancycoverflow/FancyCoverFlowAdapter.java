package com.ypwl.xiaotouzi.view.fancycoverflow;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


public abstract class FancyCoverFlowAdapter extends BaseAdapter {

    @Override
    public final View getView(int position, View reusableView, ViewGroup viewGroup) {
        FancyCoverFlow coverFlow = (FancyCoverFlow) viewGroup;

        View wrappedView = null;
        FancyCoverFlowItemWrapper coverFlowItem;

        if (reusableView != null) {
            coverFlowItem = (FancyCoverFlowItemWrapper) reusableView;
            wrappedView = coverFlowItem.getChildAt(0);
            coverFlowItem.removeAllViews();
        } else {
            coverFlowItem = new FancyCoverFlowItemWrapper(viewGroup.getContext());
        }

        wrappedView = this.getCoverFlowItem(position, wrappedView, viewGroup);

        if (wrappedView == null) {
            throw new NullPointerException("getCoverFlowItem() was expected to return a view, but null was returned.");
        }

        final boolean isReflectionEnabled = coverFlow.isReflectionEnabled();
        coverFlowItem.setReflectionEnabled(isReflectionEnabled);

        if (isReflectionEnabled) {
            coverFlowItem.setReflectionGap(coverFlow.getReflectionGap());
            coverFlowItem.setReflectionRatio(coverFlow.getReflectionRatio());
        }

        coverFlowItem.setLayoutParams(wrappedView.getLayoutParams());
        coverFlowItem.addView(wrappedView);

        return coverFlowItem;
    }

    public abstract View getCoverFlowItem(int position, View reusableView, ViewGroup parent);
}
