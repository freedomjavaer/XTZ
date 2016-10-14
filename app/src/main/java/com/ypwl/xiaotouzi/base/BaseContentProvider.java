package com.ypwl.xiaotouzi.base;

import android.content.ContentProvider;

/**
 * android 系统中的四大组件之一ContentProvider基类<br/>
 * Created by lzj on 2015/11/2.
 */
public abstract class BaseContentProvider extends ContentProvider {

	/** 日志输出标志 **/
	protected final String TAG = this.getClass().getSimpleName();
}
