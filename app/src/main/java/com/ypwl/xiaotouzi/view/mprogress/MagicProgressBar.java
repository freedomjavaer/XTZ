package com.ypwl.xiaotouzi.view.mprogress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.ypwl.xiaotouzi.R;

public class MagicProgressBar extends View {
    private int fillColor = Color.GREEN;
    private int backgroundColor = Color.DKGRAY;
    private float percent = 0f;
    private boolean isFlat = false;

    private Paint fillPaint;
    private Paint backgroundPaint;

    public MagicProgressBar(Context context) {
        this(context, null);
    }

    public MagicProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MagicProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(final Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MagicProgressBar);
            percent = typedArray.getFloat(R.styleable.MagicProgressBar_mpb_percent, percent);
            fillColor = typedArray.getColor(R.styleable.MagicProgressBar_mpb_fill_color, fillColor);
            backgroundColor = typedArray.getColor(R.styleable.MagicProgressBar_mpb_background_color, backgroundColor);
            isFlat = typedArray.getBoolean(R.styleable.MagicProgressBar_mpb_flat, isFlat);
            typedArray.recycle();
        }

        fillPaint = new Paint();
        fillPaint.setAntiAlias(true);

        backgroundPaint = new Paint();
        backgroundPaint.setAntiAlias(true);
    }

    public void setFillColor(final int fillColor) {
        this.fillColor = fillColor;
        invalidate();
    }

    public void setBackgroundColor(final int backgroundColor) {
        this.backgroundColor = backgroundColor;
        invalidate();
    }

    /**
     * @param percent FloatRange(from = 0.0, to = 1.0)
     */
    public void setPercent(float percent) {
        percent = Math.min(1, percent);
        percent = Math.max(0, percent);
        this.percent = percent;
        invalidate();
    }

    /**
     * @param flat Whether the right side of progress is round or flat
     */
    public void setFlat(final boolean flat) {
        this.isFlat = flat;
        invalidate();
    }

    private final RectF rectF = new RectF();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        this.fillPaint.setColor(fillColor);
        this.backgroundPaint.setColor(backgroundColor);
        float drawPercent = percent;

        canvas.save();

        final int height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        final int width = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();

        float fillWidth = drawPercent * width;
        final float radius = height / 2.0f;

        rectF.left = 0;
        rectF.top = 0;
        rectF.right = width;
        rectF.bottom = height;

        if (backgroundColor != 0) {
            canvas.drawRoundRect(rectF, radius, radius, backgroundPaint);
        }
        try {
            if (fillColor != 0 && fillWidth > 0) {
                if (fillWidth == width) {
                    rectF.right = fillWidth;
                    canvas.drawRoundRect(rectF, radius, radius, fillPaint);
                    return;
                }
                if (isFlat) {
                    // draw left semicircle
                    canvas.save();
                    rectF.right = fillWidth > radius ? radius : fillWidth;
                    canvas.clipRect(rectF);
                    rectF.right = radius * 2;
                    canvas.drawRoundRect(rectF, radius, radius, fillPaint);
                    canvas.restore();

                    if (fillWidth <= radius) {
                        return;
                    }

                    float leftAreaWidth = width - radius;
                    // draw center
                    float centerX = fillWidth > leftAreaWidth ? leftAreaWidth : fillWidth;
                    rectF.left = radius;
                    rectF.right = centerX;
                    canvas.drawRect(rectF, fillPaint);
                    if (fillWidth <= leftAreaWidth) {
                        return;
                    }

                    // draw right semicircle
                    rectF.left = leftAreaWidth - radius;

                    rectF.right = fillWidth;
                    canvas.clipRect(rectF);

                    rectF.right = width;
                    canvas.drawArc(rectF, -90, 180, true, fillPaint);
                } else {
                    if (fillWidth <= radius * 2) {
                        rectF.right = fillWidth;
                        canvas.clipRect(rectF);
                        rectF.right = radius * 2;
                        canvas.drawRoundRect(rectF, radius, radius, fillPaint);
                    } else {
                        rectF.right = fillWidth;
                        canvas.drawRoundRect(rectF, radius, radius, fillPaint);
                    }
                }
            }
        } finally {
            canvas.restore();
        }
    }
}
