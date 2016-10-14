package com.ypwl.xiaotouzi.utils;

import android.app.Activity;

import com.ypwl.xiaotouzi.bean.ShareBean;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.interf.IRequestCallback;
import com.ypwl.xiaotouzi.manager.ShareAuthManager;
import com.ypwl.xiaotouzi.manager.net.NetHelper;
import com.ypwl.xiaotouzi.view.dialog.lib.KProgressHUD;

/**
 * 项目名:	XTZ
 * 包名:	com.ypwl.xiaotouzi.utils
 * 类名:	ShareUtil
 * 作者:	罗霄
 * 创建时间:	2016/5/20 16:44
 * <p/>
 * 描述:	web活动页面分享工具类
 * <p/>
 * svn版本:	$Revision: 15013 $
 * 更新人:	$Author: luoxiao $
 * 更新时间:	$Date: 2016-05-20 17:46:15 +0800 (周五, 20 五月 2016) $
 * 更新描述:	$Message$
 */
public class ShareUtil {

    private static final String url_xlwb = "plname=xlwb";//新浪微博的分享
    private static final String url_wxhy = "plname=wxhy";//微信好友的分享
    private static final String url_qqhy = "plname=qqhy";//QQ好友的分享
    private static final String url_wxpyq = "plname=wxpyq";//微信朋友圈的分享

    /**
     * @param context 上下文，类型：Activity
     * @param url web返回的URL地址
     */
    public static void share(Activity context, String url, KProgressHUD mLoading) {
        mLoading.show();
        launchShare(context, url, mLoading);
    }

    private static void launchShare(Activity context, String url, KProgressHUD mLoading) {
        /** 当前分享平台 */
        int mCurrSharePlatform = -1;
        if (url.contains(url_xlwb)){
            mCurrSharePlatform = ShareAuthManager.PLATFORM_Sina;
        }else if (url.contains(url_wxhy)){
            mCurrSharePlatform = ShareAuthManager.PLATFORM_Weixin;
        }else if (url.contains(url_qqhy)){
            mCurrSharePlatform = ShareAuthManager.PLATFORM_QQ;
        }else if (url.contains(url_wxpyq)){
            mCurrSharePlatform = ShareAuthManager.PLATFORM_Weixin_Circle;
        }
        launchShare(context, mCurrSharePlatform, mLoading);
    }

    /** 分享邀请码 */
    private static void launchShare(final Activity context, final int platformCode, final KProgressHUD mLoading) {

        String url = StringUtil.format(URLConstant.SHARE_GET_APP_SHARE_URL, GlobalUtils.token);
        NetHelper.get(url, new IRequestCallback<ShareBean>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onFailure(Exception e) {
                mLoading.dismiss();
                UIUtil.showToastShort("获取分享信息失败");
            }

            @Override
            public void onSuccess(ShareBean shareBean) {
                mLoading.dismiss();
                if (shareBean.getStatus() == 0) {
                    ShareAuthManager.share(context, platformCode,
                            shareBean.getTitle(), shareBean.getContent(), shareBean.getUrl());
                } else {
                    onFailure(null);
                }
            }
        });
    }

}
