package com.ypwl.xiaotouzi.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.umeng.analytics.MobclickAgent;
import com.ypwl.xiaotouzi.common.Configs;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.manager.EventHelper;
import com.ypwl.xiaotouzi.utils.LogUtil;
import com.ypwl.xiaotouzi.utils.StringUtil;

/**
 * Fragment基类(兼容低版本)<br/>
 * Created by lzj on 2015/11/2.
 */
@SuppressWarnings("deprecation")
public abstract class BaseFragment extends Fragment {
    /** 日志输出标志 **/
    protected final String TAG = this.getClass().getSimpleName();

    @Override
    public void onAttach(Activity activity) {
        LogUtil.d(TAG, TAG + "-->onAttach()");
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        LogUtil.d(TAG, TAG + "-->onCreate()");
        super.onCreate(savedInstanceState);
        EventHelper.register(this);
    }

    @Override
    public void onResume() {
        LogUtil.d(TAG, TAG + "-->onResume()");
        super.onResume();
        if (!Configs.DEBUG)
            MobclickAgent.onResume(getActivity());
    }

    @Override
    public void onStop() {
        LogUtil.d(TAG, TAG + "-->onStop()");
        super.onStop();
    }

    @Override
    public void onPause() {
        LogUtil.d(TAG, TAG + "-->onPause()");
        super.onPause();
        if (!Configs.DEBUG)
            MobclickAgent.onPause(getActivity());
    }

    @Override
    public void onDestroyView() {
        LogUtil.d(TAG, TAG + "-->onDestroyView()");
        EventHelper.unregister(this);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        LogUtil.d(TAG, TAG + "-->onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        LogUtil.d(TAG, TAG + "-->onDetach()");
        super.onDetach();
    }

    /** 跳转activity */
    protected void startActivity(Class<?> cls) {
        this.startActivity(cls, null);
    }

    /** 跳转activity */
    protected void startActivity(Class<?> cls, String strParam) {
        Intent intent = new Intent(this.getActivity(), cls);
        if (!StringUtil.isEmptyOrNull(strParam)) {
            intent.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA, strParam);
        }
        startActivity(intent);
    }

    @SuppressWarnings("unchecked")
    protected final <T extends View> T findView(View view, int id) {
        return (T) view.findViewById(id);
    }
}
