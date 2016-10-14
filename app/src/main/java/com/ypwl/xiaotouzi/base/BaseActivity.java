package com.ypwl.xiaotouzi.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.XtzApp;
import com.ypwl.xiaotouzi.common.Configs;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.manager.EventHelper;
import com.ypwl.xiaotouzi.utils.LogUtil;
import com.ypwl.xiaotouzi.utils.StringUtil;
import com.ypwl.xiaotouzi.view.SystemBarTintManager;
import com.ypwl.xiaotouzi.view.backactivity.app.SwipeBackActivity;

import java.lang.ref.WeakReference;


/**
 * android 系统中的四大组件之一Activity基类<br/>
 * Created by lzj on 2015/11/2.
 */
public abstract class BaseActivity extends SwipeBackActivity {
    /*** 整个应用Applicaiton **/
    private XtzApp mApplication = null;
    /** 当前activity的上下文 */
    protected Activity mActivity;
    /** 当前Activity的弱引用，防止内存泄露 **/
    private WeakReference<Activity> activity = null;
    /** 日志输出标志,当前类的类名 **/
    protected final String TAG = this.getClass().getSimpleName();
    /** 系统状态栏管理类 */
    private SystemBarTintManager mSysbarTintManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d(TAG, TAG + "-->onCreate()");
//        this.overridePendingTransition(0, R.anim.activity_gradient_out);//activity淡入效果
        mActivity = this;
        mApplication = (XtzApp) getApplicationContext(); // 获取应用Application
        activity = new WeakReference<Activity>(this); // 将当前Activity压入栈
        mApplication.pushTask(activity);
        PushAgent.getInstance(this).onAppStart();//防止不活跃推送失效
        EventHelper.register(this);//注册事件总线 用于取代EventBus
        setSwipeBackActivityEnable(true);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (this.enableSystemBarManager()) {
            initSystemBarManager();//初始化状态栏管理器 默认设置了bg_primary颜色
        }
    }

    @Override
    protected void onRestart() {
        LogUtil.d(TAG, TAG + "-->onRestart()");
        super.onRestart();
    }

    @Override
    protected void onStart() {
        LogUtil.d(TAG, TAG + "-->onStart()");
        super.onStart();
    }

    @Override
    protected void onResume() {
        LogUtil.d(TAG, TAG + "-->onResume()");
        super.onResume();
        if (!Configs.DEBUG) {
            MobclickAgent.onResume(this);
        }
    }

    @Override
    protected void onPause() {
        LogUtil.d(TAG, TAG + "-->onPause()");
        super.onPause();
        if (!Configs.DEBUG) {
            MobclickAgent.onPause(this);
        }
    }

    @Override
    protected void onStop() {
        LogUtil.d(TAG, TAG + "-->onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        LogUtil.d(TAG, TAG + "-->onDestroy()");
        EventHelper.unregister(this);//反注册当前类的事件总线
        mApplication.removeTask(activity);
        mActivity = null;
        super.onDestroy();
    }

    /** 跳转activity */
    protected void startActivity(Class<?> cls) {
        this.startActivity(cls, null);
    }

    /** 跳转activity */
    protected void startActivity(Class<?> cls, String strParam) {
        Intent intent = new Intent(this, cls);
        if (!StringUtil.isEmptyOrNull(strParam)) {
            intent.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA, strParam);
        }
        startActivity(intent);
    }

    /** 是否需要处理软键盘失去焦点隐藏的事件 */
    protected boolean enableDispatchTouchEventOnSoftKeyboard() {
        return true;
    }

    /** 是否需要使用状态栏管理器,默认使用 (5.0以上系统可不使用) */
    protected boolean enableSystemBarManager() {
        return true;
    }

    /** 设置是否可以侧滑退出当前activity */
    protected void setSwipeBackActivityEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void finish() {
        super.finish();
//        this.overridePendingTransition(0, R.anim.activity_gradient_out);//activity淡出效果
    }

    //处理失去焦点软键盘隐藏事件
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!this.enableDispatchTouchEventOnSoftKeyboard()) {
            return super.dispatchTouchEvent(ev);
        }
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        return getWindow().superDispatchTouchEvent(ev) || onTouchEvent(ev);
    }

    /**
     * 初始化状态栏管理器
     */
    private void initSystemBarManager() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            Window win = this.getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            winParams.flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            win.setAttributes(winParams);
        }
        mSysbarTintManager = new SystemBarTintManager(this);
        mSysbarTintManager.setStatusBarTintColor(getResources().getColor(R.color.style_current_title));
        mSysbarTintManager.setStatusBarTintEnabled(true);
    }

    /** 获取系统状态栏管理器 可用于设置状态栏颜色透明度等 */
    protected SystemBarTintManager getSysbarTintManager() {
        if (mSysbarTintManager == null) {
            initSystemBarManager();
        }
        return mSysbarTintManager;
    }

    /** 点击输入框外需要隐藏键盘 */
    private boolean isShouldHideInput(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            // 获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            // 点击的是输入框区域，保留点击EditText的事件
            return !(ev.getX() > left && ev.getX() < right && ev.getY() > top && ev.getY() < bottom);
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    protected final <T extends View> T findView(int id) {
        return (T) this.findViewById(id);
    }

    @SuppressWarnings("unchecked")
    protected final <T extends View> T findView(View view, int id) {
        return (T) view.findViewById(id);
    }

}
