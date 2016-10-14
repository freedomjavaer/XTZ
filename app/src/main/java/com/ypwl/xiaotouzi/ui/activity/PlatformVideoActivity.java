package com.ypwl.xiaotouzi.ui.activity;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.adapter.PlatformChooseGridAdapter;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.bean.VideoInfoBean;
import com.ypwl.xiaotouzi.utils.LogUtil;
import com.ypwl.xiaotouzi.utils.NetworkUtils;
import com.ypwl.xiaotouzi.utils.StringUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 视频播放页面
 */
public class PlatformVideoActivity extends BaseActivity implements View.OnClickListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {
//    IMediaPlayer.OnPreparedListener, IMediaPlayer.OnErrorListener
    private String TAG = this.getClass().getSimpleName();
    private ImageView mIvPlayBtn;
    private TextView mTvTitle, mTvOpenTime;
    private RelativeLayout mLoading;
    private String video_url;
    private List<VideoInfoBean> videoInfos = new ArrayList<>();//视频信息
    private RelativeLayout mVideoTop, mRlScrennController;
    private FrameLayout mVideoShow;
    private LinearLayout mVideoBottom;
    private ImageView mIvFullScreen;
    private GridView mGvVideoChoose;
    private PlatformChooseGridAdapter mAdapter;
    private boolean isFullScreen = false;
    private boolean showController = false;
    private int currentIndex = 0;
//    private IjkVideoView mVideoView;
    private VideoView mVideoView;

    private Runnable dismissController = new Runnable() {
        @Override
        public void run() {
            mRlScrennController.setVisibility(View.GONE);
            showController = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_platform_video);

        List<VideoInfoBean> videoInfosList = getIntent().getParcelableArrayListExtra("video_url");
        if (videoInfosList == null || videoInfosList.size() == 0) {
            UIUtil.showToastShort(getString(R.string.video_no_resource));
            finish();
            return;
        }
        videoInfos.clear();
        videoInfos.addAll(videoInfosList);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        ((TextView)findView(R.id.tv_title_back)).setText("网贷平台");
        mTvOpenTime = (TextView) findViewById(R.id.tv_video_opentime);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
//        mVideoView = (IjkVideoView) findViewById(R.id.video_view2);
//        mVideoView.setRender(IjkVideoView.RENDER_SURFACE_VIEW);
        mVideoView = (VideoView) findViewById(R.id.video_view2);

        mLoading = (RelativeLayout) findViewById(R.id.buffering_progress);
        mLoading.setVisibility(View.VISIBLE);
        mVideoBottom = (LinearLayout) findViewById(R.id.video_bottom2);
        mVideoShow = (FrameLayout) findViewById(R.id.video_show2);
        mVideoTop = (RelativeLayout) findViewById(R.id.video_top2);
        mIvFullScreen = (ImageView) findViewById(R.id.iv_video_fullscreen2);
        mRlScrennController = (RelativeLayout) findViewById(R.id.rl_platformvideo_screen_controller);
        mGvVideoChoose = (GridView) findViewById(R.id.gv_platform_video_choose_item);
        mAdapter = new PlatformChooseGridAdapter(this);
        mGvVideoChoose.setAdapter(mAdapter);
        mIvPlayBtn = (ImageView) findViewById(R.id.iv_platform_video_play_btn);
    }

    private void initData() {
        // 获取屏幕的宽度和高度
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        String title = getIntent().getExtras().getString("p_name");
        mTvTitle.setText(title);
        addVideoChoice();//添加视频选择项

        /** 获取是否在开放时间内,不为空则处于视频关闭时间段 */
        String opentime = videoInfos.get(0).getOpentime();
        if (!StringUtil.isEmptyOrNull(opentime)) {
            mTvOpenTime.setText(opentime);
            mTvOpenTime.setVisibility(View.VISIBLE);
            return;
        }

        video_url = videoInfos.get(0).getUrl();
        if (NetworkUtils.isWifiConnected(getApplicationContext())) {
            playVideo(video_url);
        } else {
            showDialog();
        }
    }


    public void playVideo(String url) {
        mLoading.setVisibility(View.VISIBLE);
        if (url == null) {
            UIUtil.showToastShort("暂无资源");
            return;
        }
        mVideoView.setOnErrorListener(this);
        mVideoView.setOnPreparedListener(this);
        mVideoView.requestFocus();
        mVideoView.setVideoPath(url);
    }

    private void initListener() {
        findViewById(R.id.layout_title_back).setOnClickListener(this);
        mVideoShow.setOnClickListener(this);
        mIvFullScreen.setOnClickListener(this);
        mIvPlayBtn.setOnClickListener(this);
        mGvVideoChoose.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentIndex == position) {
                    return;
                }
                currentIndex = position;
                video_url = videoInfos.get(position).getUrl();
                changeChoose(position);//更改ui

                /** 获取是否在开放时间内,不为空则处于视频关闭时间段 */
                String opentime = videoInfos.get(position).getOpentime();
                if (!StringUtil.isEmptyOrNull(opentime)) {
                    mTvOpenTime.setText(opentime);
                    mTvOpenTime.setVisibility(View.VISIBLE);
                    return;
                }
                mTvOpenTime.setVisibility(View.GONE);

                mVideoView.pause();
                playVideo(video_url);
                mRlScrennController.setVisibility(View.GONE);
                showController = false;
                mLoading.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * 更改视频选项ui
     */
    private void changeChoose(int position) {
        for (int i = 0; i < videoInfos.size(); i++) {
            videoInfos.get(i).setHasChoose(i == position ? true : false);//更改选中颜色
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 是否全屏显示
     *
     * @param fullScreen true为全屏，false为正常
     */
    private void videoViewDisplay(boolean fullScreen) {
//        mVideoView.toggleAspectRatio();//IjkVideoView
        mIvFullScreen.setImageResource(fullScreen ? R.mipmap.video_normalscreen : R.mipmap.video_fullscreen);
        if (fullScreen) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//正常
        }
        setRequestedOrientation(fullScreen ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE :
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//横竖屏切换
        mVideoTop.setVisibility(fullScreen ? View.GONE : View.VISIBLE);
        mVideoBottom.setVisibility(fullScreen ? View.GONE : View.VISIBLE);
        ViewGroup.LayoutParams lp = mVideoShow.getLayoutParams();//设置布局
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        int h = UIUtil.dip2px(250);
        lp.height = fullScreen ? FrameLayout.LayoutParams.MATCH_PARENT : h;
        mVideoShow.setLayoutParams(lp);
        isFullScreen = !isFullScreen;//更改状态
    }

    /**
     * 创建视频选择项
     */
    private void addVideoChoice() {
        videoInfos.get(0).setHasChoose(true);
        mGvVideoChoose.setHorizontalSpacing(UIUtil.dip2px(6));//设置间隔
        mGvVideoChoose.setVerticalSpacing(UIUtil.dip2px(8));
        mAdapter.loadData(videoInfos);
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PlatformVideoActivity.this);
        builder.setMessage("您目前没有连接WIFI是否继续播放？");
        builder.setPositiveButton("继续播放", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                playVideo(video_url);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mIvPlayBtn.setVisibility(View.VISIBLE);
                mLoading.setVisibility(View.INVISIBLE);
            }
        });
        builder.create().show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_title_back://返回
                if (mVideoView != null) {
                    mVideoView.pause();
                    mVideoView.stopPlayback();
                }
                finish();
                break;
            case R.id.video_show2://显示操作条
                if (!mVideoView.isPlaying()) {
                    return;
                }
                mRlScrennController.setVisibility(showController ? View.GONE : View.VISIBLE);
                if (showController) {
                    UIUtil.removeCallbacksFromMainLooper(dismissController);
                } else {
                    UIUtil.postDelayed(dismissController, 3000);//3秒后消失
                }
                showController = !showController;
                break;
            case R.id.iv_video_fullscreen2://横竖屏切换
                videoViewDisplay(isFullScreen ? false : true);
                break;
            case R.id.iv_platform_video_play_btn://重播
                if (NetworkUtils.isWifiConnected(getApplicationContext())) {
                    playVideo(video_url);
                } else {
                    showDialog();
                }
                mIvPlayBtn.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (mVideoView != null)
            mVideoView = null;
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
        if (isFullScreen) {
            videoViewDisplay(false);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mVideoView.start();//准备完成后播放
        //延迟放开加载，防止切换时出现画面跳帧
        UIUtil.postDelayed(new Runnable() {
            @Override
            public void run() {
                mLoading.setVisibility(View.GONE);
            }
        }, 1000);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case -1004:
                LogUtil.e(TAG, "media_error_io");
                break;
            case -1007:
                LogUtil.e(TAG, "media_error_malformed");
                break;
            case 200:
                LogUtil.e(TAG, "media_error_not_valid_for_progressive_playback");
                break;
            case 100:
                LogUtil.e(TAG, "media_error_server_died");
                break;
            case -110:
                LogUtil.e(TAG, "media_error_timed_out");
                break;
            case 1:
                LogUtil.e(TAG, "media_error_unknown");
                break;
            case -1010:
                LogUtil.e(TAG, "media_error_unsupported");
                break;
        }
        switch (extra) {
            case 800:
                LogUtil.e(TAG, "media_info_bad_interleaving");
                break;
            case 702:
                LogUtil.e(TAG, "media_info_buffering_end");
                break;
            case 701:
                LogUtil.e(TAG, "media_info_metadata_update");
                break;
            case 802:
                LogUtil.e(TAG, "media_info_metadata_update");
                break;
            case 801:
                LogUtil.e(TAG, "media_info_not_seekable");
                break;
            case 1:
                LogUtil.e(TAG, "media_info_unknown");
                break;
            case 3:
                LogUtil.e(TAG, "media_info_video_rendering_start");
                break;
            case 700:
                LogUtil.e(TAG, "media_info_video_track_lagging");
                break;
        }
        return false;
    }
}
