package com.ypwl.xiaotouzi.base;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.utils.StringUtil;

/**
 * 页面的基类<br/>
 * Created by lzj on 2015/11/2.
 *
 * @deprecated 以后将废弃使用该方式，建议使用fragment进行管理
 */
@SuppressWarnings("unused")
@Deprecated
public abstract class BasePage {
    /** 日志输出标志,当前类的类名 **/
    protected final String TAG = this.getClass().getSimpleName();
    public Activity mContext;
    private View rootView;
    private LayoutInflater inflater;

    public BasePage(Activity context) {
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);
        rootView = initView(inflater);
    }

    /**
     * 初始化视图
     *
     * @return 创建的静态视图
     */
    protected abstract View initView(LayoutInflater inflater);

    /**
     * 获得当前页面布局对象
     *
     * @return 当前布局视图对象
     */
    public View getRootView() {
        return rootView;
    }

    /**
     * 初始化数据
     */
    public abstract void initData();

    /** 跳转activity */
    protected void startActivity(Class<?> cls) {
        this.startActivity(cls, null);
    }

    /** 跳转activity */
    protected void startActivity(Class<?> cls, String strParam) {
        Intent intent = new Intent(mContext, cls);
        if (!StringUtil.isEmptyOrNull(strParam)) {
            intent.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA, strParam);
        }
        mContext.startActivity(intent);
    }


    public void onDestroy() {
        rootView = null;
        mContext = null;
        inflater = null;
        System.gc();
    }
}
