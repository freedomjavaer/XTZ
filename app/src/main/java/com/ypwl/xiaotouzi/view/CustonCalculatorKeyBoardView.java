package com.ypwl.xiaotouzi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.ypwl.xiaotouzi.R;

/**
 * function:自定义计算器键盘
 * <p/>
 * Created by tengtao on 2016/1/14.
 */
public class CustonCalculatorKeyBoardView extends LinearLayout implements View.OnClickListener {
    private Context mContext;

    public CustonCalculatorKeyBoardView(Context context) {
        this(context, null);
    }

    public CustonCalculatorKeyBoardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustonCalculatorKeyBoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    private void initView() {
        inflate(mContext, R.layout.layout_calculator_soft_keyboard,this);
        //键盘的点击
        findViewById(R.id.btnNum0).setOnClickListener(this);
        findViewById(R.id.btnNum1).setOnClickListener(this);
        findViewById(R.id.btnNum2).setOnClickListener(this);
        findViewById(R.id.btnNum3).setOnClickListener(this);
        findViewById(R.id.btnNum4).setOnClickListener(this);
        findViewById(R.id.btnNum5).setOnClickListener(this);
        findViewById(R.id.btnNum6).setOnClickListener(this);
        findViewById(R.id.btnNum7).setOnClickListener(this);
        findViewById(R.id.btnNum8).setOnClickListener(this);
        findViewById(R.id.btnNum9).setOnClickListener(this);
        findViewById(R.id.btnNumPoint).setOnClickListener(this);
        findViewById(R.id.btnDelete).setOnClickListener(this);
        findViewById(R.id.btn_next_term).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnNum0:
                mListener.onCustomKeyBoardClick("0");
                break;
            case R.id.btnNum1:
                mListener.onCustomKeyBoardClick("1");
                break;
            case R.id.btnNum2:
                mListener.onCustomKeyBoardClick("2");
                break;
            case R.id.btnNum3:
                mListener.onCustomKeyBoardClick("3");
                break;
            case R.id.btnNum4:
                mListener.onCustomKeyBoardClick("4");
                break;
            case R.id.btnNum5:
                mListener.onCustomKeyBoardClick("5");
                break;
            case R.id.btnNum6:
                mListener.onCustomKeyBoardClick("6");
                break;
            case R.id.btnNum7:
                mListener.onCustomKeyBoardClick("7");
                break;
            case R.id.btnNum8:
                mListener.onCustomKeyBoardClick("8");
                break;
            case R.id.btnNum9:
                mListener.onCustomKeyBoardClick("9");
                break;
            case R.id.btnNumPoint:
                mListener.onCustomKeyBoardClick(".");
                break;
            case R.id.btnDelete://删除
                mListener.onCustomKeyBoardClick("del");
                break;
            case R.id.btn_next_term://下一项
                mListener.onCustomKeyBoardClick("next");
                break;
        }
    }

    /**
     * 自定义键盘的点击事件接口
     */
    public interface OnCustomKeyBoardClickListener{
        /**
         * 点击的处理方法
         * @param key  区别点击的按钮
         *             del：删除
         *             next:下一项
         *             其他的对应输入的符号 0-9以及.
         */
        void onCustomKeyBoardClick(String key);
    }

    private OnCustomKeyBoardClickListener mListener;

    /**
     * 设置监听器
     * @param listener
     */
    public void setOnCustomKeyBoardClickListener(OnCustomKeyBoardClickListener listener){
        this.mListener = listener;
    }
}
