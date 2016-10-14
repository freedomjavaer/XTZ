package com.ypwl.xiaotouzi.view.getsturepassword;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.ypwl.xiaotouzi.R;

/**
 * 手势密码图案提示
 *
 * Created by PDK on 2016/3/15.
 */
public class GesturePatternHintView extends View {

    private int mRowNumber = 3;    // 行
    private int mColumNumber = 3; // 列
    /** 提示图案的宽 */
    private int mPatternWidth = 60;
    /** 提示图案的高 */
    private int mPatternHeight = 60;
    private int f = 5;
    private int g = 5;
    private Drawable mPatternNoraml = null;
    private Drawable mPatternPressed = null;
    private String mLockPswStr; // 手势密码

    public GesturePatternHintView(Context paramContext) {
        super(paramContext);
    }

    @SuppressWarnings("deprecation")
    public GesturePatternHintView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet, 0);
        mPatternNoraml = getResources().getDrawable(R.mipmap.gp_hint_pattern_node_normal);
        mPatternPressed = getResources().getDrawable(R.mipmap.gp_hint_pattern_node_pressed);
        if (mPatternPressed != null) {
            mPatternWidth = mPatternPressed.getIntrinsicWidth();
            mPatternHeight = mPatternPressed.getIntrinsicHeight();
            this.f = (mPatternWidth / 4);
            this.g = (mPatternHeight / 4);
            mPatternPressed.setBounds(0, 0, mPatternWidth, mPatternHeight);
            mPatternNoraml.setBounds(0, 0, mPatternWidth, mPatternHeight);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if ((mPatternPressed == null) || (mPatternNoraml == null)) {
            return;
        }
        // 绘制3*3的图标
        for (int rowIndex = 0; rowIndex < mRowNumber; rowIndex++) {
            for (int columIndex = 0; columIndex < mColumNumber; columIndex++) {
                int i1 = columIndex * mPatternHeight + columIndex * this.g;
                int i2 = rowIndex * mPatternWidth + rowIndex * this.f;
                canvas.save();
                canvas.translate(i1, i2);
                String curNum = String.valueOf(mColumNumber * rowIndex + (columIndex + 1));
                if (!TextUtils.isEmpty(mLockPswStr)) {
                    if (!mLockPswStr.contains(curNum)) { // 未选中
                        mPatternNoraml.draw(canvas);
                    } else { // 被选中
                        mPatternPressed.draw(canvas);
                    }
                } else {// 重置状态
                    mPatternNoraml.draw(canvas);
                }
                canvas.restore();
            }
        }
    }

    @Override
    protected void onMeasure(int paramInt1, int paramInt2) {
        if (mPatternPressed != null) {
            setMeasuredDimension(mColumNumber * mPatternHeight + this.g * (-1 + mColumNumber), mRowNumber * mPatternWidth + this.f * (-1 + mRowNumber));
        }
    }

    /**
     * 请求重新绘制
     *
     * @param paramString 手势密码字符序列
     */
    public void setPath(String paramString) {
        mLockPswStr = paramString;
        invalidate();
    }
}
