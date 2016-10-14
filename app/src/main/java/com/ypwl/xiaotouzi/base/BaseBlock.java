package com.ypwl.xiaotouzi.base;

import android.content.Intent;
import android.view.View;

import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.utils.StringUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;

/**
 * function : 自定义小模块view基础类
 * <p/>
 * Created by tengtao on 2016/5/5.
 */
public abstract class BaseBlock<T> {

    private View mBlockView;
    private T mData;

    public BaseBlock() {
        mBlockView = initView();
    }

    /**
     * 初始化试图
     */
    protected abstract View initView();

    /**
     * 更新数据，刷新UI试图，监听回调在此添加
     */
    protected abstract void refreshUI(T data);

    /**
     * 设置数据
     */
    public void setData(T data) {
        this.mData = data;
        refreshUI(data);
    }

    /**
     * 获取模块试图
     */
    public View getBlockView() {
        return mBlockView;
    }

    /**
     * 获取模块数据
     */
    public T getBlockData() {
        return mData;
    }

    /** 跳转activity */
    protected void startActivity(Class<?> cls) {
        this.startActivity(cls, null);
    }

    /** 跳转activity */
    protected void startActivity(Class<?> cls, String strParam) {
        Intent intent = new Intent(UIUtil.getContext(), cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (!StringUtil.isEmptyOrNull(strParam)) {
            intent.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA, strParam);
        }
        UIUtil.getContext().startActivity(intent);
    }
}
