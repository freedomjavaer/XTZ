package com.ypwl.xiaotouzi.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;


/**
 * @author chenwentao on 15/5/24.
 */
public class CheckableSortItem extends RelativeLayout implements Checkable {


//    private String TAG = "CheckableSortItem";


    public boolean isCheck = true;

    private TextView textView;
    private ImageView upImage;
    private ImageView downImage;

    private String title = "null";

    public CheckableSortItem(Context context) {
        this(context, null);
    }

    public CheckableSortItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CheckableSortItem, 0, 0);
        title = array.getString(R.styleable.CheckableSortItem_item_text);
        array.recycle();

        init();
    }

    private void init() {
        inflate(getContext(), R.layout.custom_checkable_relative, this);
        this.textView = (TextView) findViewById(R.id.custom_checkable_relative_text);
        this.upImage = (ImageView) findViewById(R.id.custom_checkable_relative_image_up);
        this.downImage = (ImageView) findViewById(R.id.custom_checkable_relative_image_down);

        textView.setText(title);
        textView.setTextSize(12);
        textView.setTextColor(getResources().getColor(R.color.color_001));

    }

    @Override
    public void setChecked(boolean checked) {
        isCheck = checked;
        changeImage(isCheck);
        refreshDrawableState();
    }

    public void resetCheck(boolean isCheck){
        if(!isCheck){return;}
        this.isCheck = true;
    }

    @Override
    public boolean isChecked() {
        return isCheck;
    }

    @Override
    public void toggle() {
        setChecked(!isCheck);
    }

    public void clearStatus(boolean isClear) {
        if(!isClear){return;}
        upImage.setImageResource(R.drawable.btn_016);
        downImage.setImageResource(R.drawable.btn_017);
        textView.setTextColor(getResources().getColor(R.color.color_004));
    }

    public void changeImage(boolean isCheck) {

        textView.setTextColor(getResources().getColor(R.color.color_001));

        if (isCheck) {
            upImage.setImageResource(R.drawable.btn_016);
            downImage.setImageResource(R.drawable.btn_017_select);
        } else {
            upImage.setImageResource(R.drawable.btn_016_select);
            downImage.setImageResource(R.drawable.btn_017);
        }
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CheckedStateSet);
        }
        return drawableState;
    }


    private static final int[] CheckedStateSet = {
            android.R.attr.state_checked
    };


}
