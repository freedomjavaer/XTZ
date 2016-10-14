package com.ypwl.xiaotouzi.view.getsturepassword;

import android.annotation.SuppressLint;
import android.content.Context;

import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.ypwl.xiaotouzi.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 手势密码容器类
 * Created by PDK on 2016/3/15.
 */
@SuppressLint("ViewConstructor")
public class GestureContentView extends ViewGroup {
    private int mBasePix = 5;
    /** 屏幕宽高 */
    private int[] mScreenDispaly;
    /** 每个点区域的宽度 */
    private int mBlockWidth;
    /** 声明一个集合用来封装坐标集合 */
    private List<GesturePoint> mPointList;
    private Context mContext;
    private GestureDrawline mGestureDrawline;

    /**
     * 包含9个ImageView的容器，初始化
     *
     * @param isVerify 是否为校验手势密码
     * @param passWord 用户传入密码
     * @param callBack 手势绘制完毕的回调
     */
    public GestureContentView(Context context, boolean isVerify, String passWord, GestureDrawline.GestureCallBack callBack) {
        super(context);
        this.mContext = context;
        mScreenDispaly = getScreenDispaly(context);
        mBlockWidth = mScreenDispaly[0] / 3;
        this.mPointList = new ArrayList<>();
        addChild(); // 添加9个图标
        // 初始化一个可以画线的view
        mGestureDrawline = new GestureDrawline(context, mPointList, isVerify, passWord, callBack);
    }

    private void addChild() {
        for (int i = 0; i < 9; i++) {
            ImageView image = new ImageView(mContext);
            image.setBackgroundResource(R.mipmap.gp_gesture_node_normal);
            this.addView(image);
            invalidate();
            int row = i / 3;// 第几行
            int col = i % 3;// 第几列
            // 定义点的每个属性
            int leftX = col * mBlockWidth + mBlockWidth / mBasePix;
            int topY = row * mBlockWidth + mBlockWidth / mBasePix;
            int rightX = col * mBlockWidth + mBlockWidth - mBlockWidth / mBasePix;
            int bottomY = row * mBlockWidth + mBlockWidth - mBlockWidth / mBasePix;
            GesturePoint p = new GesturePoint(leftX, rightX, topY, bottomY, image, i + 1);
            this.mPointList.add(p);
        }
    }

    public void setParentView(ViewGroup parent) {
        // 得到屏幕的宽度
        int width = mScreenDispaly[0];
        int height = mScreenDispaly[0];
        LayoutParams layoutParams = new LayoutParams(width, height);
        this.setLayoutParams(layoutParams);
        mGestureDrawline.setLayoutParams(layoutParams);
        parent.addView(mGestureDrawline);
        parent.addView(this);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            int row = i / 3;  //行
            int col = i % 3;//列
            View v = getChildAt(i);
            v.layout(col * mBlockWidth + mBlockWidth / mBasePix, row * mBlockWidth + mBlockWidth / mBasePix,
                    col * mBlockWidth + mBlockWidth - mBlockWidth / mBasePix, row * mBlockWidth + mBlockWidth - mBlockWidth / mBasePix);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 遍历设置每个子view的大小
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            v.measure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    /** 获取屏幕分辨率 */
    @SuppressWarnings("deprecation")
    public static int[] getScreenDispaly(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();// 手机屏幕的宽度
        int height = windowManager.getDefaultDisplay().getHeight();// 手机屏幕的高度
        return new int[]{width, height};
    }

    /**
     * 保留路径delayTime时间长
     */
    public void clearDrawlineState(long delayTime) {
        mGestureDrawline.clearDrawlineState(delayTime);
    }

}
