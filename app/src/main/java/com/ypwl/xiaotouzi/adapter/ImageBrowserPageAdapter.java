package com.ypwl.xiaotouzi.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.utils.ImgLoadUtil;
import com.ypwl.xiaotouzi.utils.LogUtil;

import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * function ： 图片浏览页面数据适配器
 * <p/>
 * Created by lzj on 2015/12/3.
 */
public class ImageBrowserPageAdapter extends PagerAdapter {
    private static final String TAG = ImageBrowserPageAdapter.class.getSimpleName();
    private Activity mActivity;
    private List<String> mUrlList;
    private ViewGroup mContainer;

    public ImageBrowserPageAdapter(Activity context, List<String> urlList) {
        this.mActivity = context;
        this.mUrlList = urlList;
    }

    @Override
    public int getCount() {
        return mUrlList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        mContainer = container;
        String imgUrl = mUrlList.get(position);
        LogUtil.e(TAG, "ImgBrowser load img url : " + imgUrl);
        PhotoView photoView = new PhotoView(mActivity);
        photoView.setOnPhotoTapListener(mOnPhotoTapListener);
        if (imgUrl.startsWith("http") || imgUrl.startsWith("/")) { //本地 或网络全地址
            ImgLoadUtil.loadImgByPath(imgUrl, photoView, R.mipmap.pic_014_1);
        } else {//需要拼接的地址
            ImgLoadUtil.loadImgBySplice(imgUrl, photoView, R.mipmap.pic_014_1);
        }
        container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return photoView;
    }

    private PhotoViewAttacher.OnPhotoTapListener mOnPhotoTapListener = new PhotoViewAttacher.OnPhotoTapListener() {
        @Override
        public void onPhotoTap(View view, float x, float y) {
            mActivity.finish();
        }
    };

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
