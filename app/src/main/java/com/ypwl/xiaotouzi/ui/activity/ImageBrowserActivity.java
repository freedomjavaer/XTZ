package com.ypwl.xiaotouzi.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.adapter.ImageBrowserPageAdapter;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.event.ImageRemoveEvent;
import com.ypwl.xiaotouzi.manager.EventHelper;
import com.ypwl.xiaotouzi.utils.LogUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.view.ImageBrowserViewPager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * function : 图片浏览页面
 * </p>
 * Created by lzj on 2015/12/3.
 */
public class ImageBrowserActivity extends BaseActivity implements View.OnClickListener {
    private static List<String> mUrlList;
    private static int mCurrentPositon;
    private static boolean mIsEditeMode = false;
    private ImageBrowserViewPager mVp;
    private TextView mIndexText;
    private ImageView mBtnAction;
    private ImageBrowserPageAdapter mAdapter;


    /**
     * 展示图片浏览页面
     *
     * @param urlList  图片链接数据列表
     * @param position 点击的位置 从0开始
     */
    public static void show(Activity ctx, List<String> urlList, int position, boolean isEditMode) {
        if (urlList == null || urlList.size() == 0) {
            UIUtil.showToastShort("无图片可展示");
            return;
        }
        if (position < 0 || position >= urlList.size()) {
            UIUtil.showToastShort("数据出错，请联系客服");
            return;
        }
        mUrlList = new ArrayList<>();
        mUrlList.addAll(urlList);
        mCurrentPositon = position;
        mIsEditeMode = isEditMode;
        ctx.startActivity(new Intent(ctx, ImageBrowserActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getSysbarTintManager().setStatusBarTintColor(R.color.transparent);
        setContentView(R.layout.activity_image_browser);
        initView();
    }

    private void initView() {
        mVp = (ImageBrowserViewPager) findViewById(R.id.vp);
        mIndexText = (TextView) findViewById(R.id.index);
        mIndexText.setVisibility(View.VISIBLE);
        mBtnAction = (ImageView) findViewById(R.id.action);
        mBtnAction.setOnClickListener(this);
        mBtnAction.setVisibility(mIsEditeMode ? View.VISIBLE : View.GONE);
        initData();
    }

    private void initData() {
        mAdapter = new ImageBrowserPageAdapter(mActivity, mUrlList);
        mVp.setAdapter(mAdapter);
        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPositon = position;
                mIndexText.setText(position + 1 + "/" + mUrlList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mVp.setCurrentItem(mCurrentPositon);
        mIndexText.setText(mCurrentPositon + 1 + "/" + mUrlList.size());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action://动作处理--删除
                deleteCurrentImg();
                break;
        }
    }

    /** 删除图片 */
    private void deleteCurrentImg() {
        int totolPosition = mUrlList.size() - 1;
        String currentImgPath = null;
        try {
            currentImgPath = mUrlList.get(mCurrentPositon);
            File file = new File(currentImgPath);
            if (file.exists() && file.isFile()) {
                boolean delete = file.delete();
                if (!delete) {
                    UIUtil.showToastShort("删除失败");
                    return;
                }
            }
            mUrlList.remove(mCurrentPositon);
        } catch (Exception e) {
            LogUtil.e(TAG, "Exception : " + e.getMessage());
        }
        if (mCurrentPositon == 0) {
            ++mCurrentPositon;
        } else if (mCurrentPositon == totolPosition) {
            --mCurrentPositon;
        } else {
            ++mCurrentPositon;
        }
        EventHelper.post(new ImageRemoveEvent(currentImgPath));
        if (mUrlList.size() > 0) {
            initData();
        } else {
            finish();
        }

    }

    @Override
    protected void onDestroy() {
        mUrlList = null;
        mAdapter = null;
        super.onDestroy();
    }
}
