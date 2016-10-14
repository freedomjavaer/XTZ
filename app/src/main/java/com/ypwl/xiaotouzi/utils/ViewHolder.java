package com.ypwl.xiaotouzi.utils;

import android.util.SparseArray;
import android.view.View;

/**
 * findViewById带item复用功能的ViewHolder.
 * <p/>
 * Created by lzj on 2015/11/8.
 */
@SuppressWarnings("unchecked")
public class ViewHolder {

    public static <T extends View> T findViewById(View view, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }
}
