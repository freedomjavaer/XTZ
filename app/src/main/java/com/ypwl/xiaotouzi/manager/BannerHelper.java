package com.ypwl.xiaotouzi.manager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.bean.BannerBean;
import com.ypwl.xiaotouzi.utils.ImgLoadUtil;
import com.ypwl.xiaotouzi.view.ViewPagerScroller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * function : banner帮助类，运行在Activity中的主线程.
 * <p/>
 * Created by lzj on 2015/12/30.
 */
public class BannerHelper {

    /** 默认轮播间隔时间 */
    private int mIntervalTime = 5000;

    private ViewPagerScroller mScroller;

    private static Activity mActivity;
//    private static volatile BannerHelper instance = new BannerHelper();

    private BannerHelper() {
    }

    public static BannerHelper getInstance(Activity activity) {
        mActivity = activity;
        return new BannerHelper();
    }

    private View mBannerRootLayout;
    private ViewPager mBannerViewpager;
    private LinearLayout mPointersLayout;
    private TextView mBannerDesc;
    private ProgressBar mLoadingPb;
    private List<BannerBean> mBannerList;
    private int previousEnabledPosition = 0;
    private LoopShowTask mLoopShowTask;
    private boolean mIsTouchDown;
    private OnItemClickListener mOnItemClickListener;

    public void init(@NonNull View bannerView) {
        mBannerRootLayout = bannerView;
        mBannerViewpager = (ViewPager) mBannerRootLayout.findViewById(R.id.banner_viewpager);
        mPointersLayout = (LinearLayout) mBannerRootLayout.findViewById(R.id.banner_pointers);
        mBannerDesc = (TextView) mBannerRootLayout.findViewById(R.id.banner_desc);
        mLoadingPb = (ProgressBar) mBannerRootLayout.findViewById(R.id.loading);
        mLoadingPb.setVisibility(View.VISIBLE);

        initViewPagerScroll();
    }

    public void startBanner(List<BannerBean> bannerList, OnItemClickListener onItemClickListener) {
        if (mBannerRootLayout == null || bannerList == null || bannerList.size() == 0) {
            return;
        }
        mBannerList = bannerList;
        mOnItemClickListener = onItemClickListener;

        mBannerViewpager.setAdapter(new BannerPicturePagerAdapter());
        mBannerViewpager.removeOnPageChangeListener(mBannerPageChangeListener);
        mBannerViewpager.addOnPageChangeListener(mBannerPageChangeListener);
        mBannerViewpager.setOnTouchListener(mBannerOnTouchListener);

        // 初始化图片的点
        mPointersLayout.removeAllViews();
        for (int x = 0; x < bannerList.size(); x++) {
            View v = new View(mActivity);
            v.setBackgroundResource(R.drawable.selector_pointers);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
            if (x != 0) {
                params.leftMargin = 10;
            }
            v.setLayoutParams(params);
            v.setEnabled(false);
            mPointersLayout.addView(v);
        }
        //开启轮播
        if (mPointersLayout.getChildCount() > 0)
            mLoadingPb.setVisibility(View.GONE);
        if (mPointersLayout.getChildCount() > 0) {
            previousEnabledPosition = 0;
            mPointersLayout.getChildAt(previousEnabledPosition).setEnabled(true);
            String pageTitle = mBannerViewpager.getAdapter().getPageTitle(0).toString();
            if (!TextUtils.isEmpty(pageTitle)) {
                mBannerDesc.setText(pageTitle);
            }
            if(mPointersLayout.getChildCount() > 1){
                if (mLoopShowTask == null) {
                    mLoopShowTask = new LoopShowTask();
                    mLoopShowTask.start();
                } else if (!mIsTouchDown) {
                    mLoopShowTask.stop();
                    mLoopShowTask.start();
                }
            }
        }

        mViews.clear();
        for(int i=0;i<mBannerList.size();i++){
            ImageView iv = new ImageView(mActivity);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);//拉伸填充宽高
            //            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            BannerBean item = mBannerList.get(i);
            ImgLoadUtil.loadImgBySplice(item.getImgurl(), iv, item.getTestImgResId());//加载网络图片
            mViews.add(iv);
        }
    }
    private List<View> mViews = new ArrayList<>();

    /**
     * call in current activity  onResume()
     */
    public void onResume() {
        if (mPointersLayout.getChildCount() > 1) {
            if (mLoopShowTask == null) {
                mLoopShowTask = new LoopShowTask();
            }
            mLoopShowTask.start();
        }
    }

    /**
     * call in current activity  onPause()
     */
    public void onPause() {
        if (mPointersLayout.getChildCount() > 0) {
            if (mLoopShowTask != null) {
                mLoopShowTask.stop();
            }
        }
    }

    /**
     * call in current activity  onDestroy()
     */
    public void onDestroy() {
        if(mLoopShowTask!=null){
            mLoopShowTask.stop();
            mLoopShowTask.removeCallbacksAndMessages(null);
        }
        mPointersLayout.removeAllViews();
        mBannerViewpager.removeOnPageChangeListener(mBannerPageChangeListener);
        mBannerViewpager.removeAllViews();
        mOnItemClickListener = null;
    }


    private ViewPager.OnPageChangeListener mBannerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            int newPosition = position % mPointersLayout.getChildCount();
            mPointersLayout.getChildAt(previousEnabledPosition).setEnabled(false);// 取消前一个高亮
            mPointersLayout.getChildAt(newPosition).setEnabled(true);// 显示当前的高亮
            String pageTitle = mBannerViewpager.getAdapter().getPageTitle(newPosition).toString();
            if (!TextUtils.isEmpty(pageTitle)) {
                mBannerDesc.setText(pageTitle);
            }
            previousEnabledPosition = newPosition;
        }
    };

    /** 手势滑动 */
    private View.OnTouchListener mBannerOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (mPointersLayout.getChildCount() <= 1) {
                return false;
            }
            float x = 0;
            float y = 0;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = event.getX();
                    y = event.getY();
                    mIsTouchDown = true;
                    mLoopShowTask.stop();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float x1 = event.getX();
                    float y1 = event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    mIsTouchDown = false;
                    mLoopShowTask.start();
                    break;
            }
            return false;
        }
    };


    /** 循环播放banner图片任务 */
    @SuppressLint("HandlerLeak")
    private class LoopShowTask extends Handler implements Runnable {
        @Override
        public void run() {
            int item = mBannerViewpager.getCurrentItem();
            mBannerViewpager.setCurrentItem((++item) % mBannerViewpager.getAdapter().getCount(), true);
            postDelayed(mLoopShowTask, mIntervalTime);
        }

        public void start() {
            stop();
            mLoopShowTask.postDelayed(mLoopShowTask, mIntervalTime);
        }

        public void stop() {
            mLoopShowTask.removeCallbacks(mLoopShowTask);
        }
    }

    /** banner图片数据列表适配器 */
    class BannerPicturePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mBannerList.get(position % mBannerList.size()).getDesc();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView iv = new ImageView(mActivity);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);//拉伸填充宽高
            iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            BannerBean item = mBannerList.get(position % mBannerList.size());
            ImgLoadUtil.loadImgBySplice(item.getImgurl(), iv, item.getTestImgResId());//加载网络图片
            iv.setTag(R.id.banner_rootlayout, item);
            iv.setOnClickListener(mOnClickListener);

            container.addView(iv);
            return iv;
        }

        /** 条目点击事件 */
        private View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BannerBean bean = (BannerBean) v.getTag(R.id.banner_rootlayout);
                if (bean != null && mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(bean);
                }
            }
        };

    }

    /**
     * 条目点击回调监听
     */
    public interface OnItemClickListener {
        void onItemClick(BannerBean bean);
    }

    /**
     * 设置ViewPager的滑动速度
     * */
    private void initViewPagerScroll() {
        try {
            Field mScroller = null;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            this.mScroller = new ViewPagerScroller(
                    mBannerViewpager.getContext());
            mScroller.set(mBannerViewpager, this.mScroller);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /** 获取轮播间隔时间 */
    public int getIntervalTime(){
        return mIntervalTime;
    }

    /** 设置间隔时间，并返回设置成功状态：当设置时间按大于0时，才会设置成功， */
    public boolean setIntervalTime(int time){
        boolean change = false;
        if (time > 0){
            mIntervalTime = time;
            change = true;
        }
        return change;
    }
}
