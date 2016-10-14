package com.ypwl.xiaotouzi.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.handler.SinaSsoHandler;
import com.ypwl.xiaotouzi.base.BaseActivity;

/**
 * function: 新浪微博分享预留类
 *
 * <br/>
 * Created by lzj on 2016/5/3.
 */
public class WBShareActivity extends BaseActivity implements IWeiboHandler.Response {
    protected SinaSsoHandler sinaSsoHandler = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UMShareAPI api = UMShareAPI.get(this.getApplicationContext());
        this.sinaSsoHandler = (SinaSsoHandler) api.getHandler(SHARE_MEDIA.SINA);
        this.sinaSsoHandler.onCreate(this, PlatformConfig.getPlatform(SHARE_MEDIA.SINA));
        if (this.getIntent() != null) {
            this.sinaSsoHandler.getmWeiboShareAPI().handleWeiboResponse(this.getIntent(), this);
        }
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.setIntent(intent);
        UMShareAPI api = UMShareAPI.get(this.getApplicationContext());
        this.sinaSsoHandler = (SinaSsoHandler) api.getHandler(SHARE_MEDIA.SINA);
        this.sinaSsoHandler.onCreate(this, PlatformConfig.getPlatform(SHARE_MEDIA.SINA));
        this.sinaSsoHandler.getmWeiboShareAPI().handleWeiboResponse(intent, this);
    }

    public void onResponse(BaseResponse baseResponse) {
        if (this.sinaSsoHandler != null) {
            this.sinaSsoHandler.onResponse(baseResponse);
        }

        this.finish();
    }
}
