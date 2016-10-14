package com.ypwl.xiaotouzi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 已测量高度,避免高度展现不全<br/>
 * <br/>
 * Created by lzj on 2015/11/12.
 */
public class MeasuredGridView extends GridView {

    public MeasuredGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MeasuredGridView(Context context) {
        super(context);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
