package com.ypwl.xiaotouzi.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.bean.PlatformOfFinanceBean;
import com.ypwl.xiaotouzi.common.DeviceInfo;
import com.ypwl.xiaotouzi.utils.ImgLoadUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * 项目名:	XTZ
 * 包名:	com.ypwl.xiaotouzi.adapter
 * 类名:	PlatformOfFinanceAdapter
 * 作者:	罗霄
 * 创建时间:	2016/4/19 11:30
 * <p/>
 * 描述:	金融超市 -- 平台适配器
 * <p/>
 * svn版本:	$Revision: 14267 $
 * 更新人:	$Author: luoxiao $
 * 更新时间:	$Date: 2016-04-22 17:38:47 +0800 (周五, 22 四月 2016) $
 * 更新描述:	$Message$
 */
public class PlatformOfFinanceAdapter extends BaseAdapter {
    private final Context mContext;
    private LinkedList<PlatformOfFinanceBean> mData;

    private int mIvLayoutParamsWidth;
    //简介图片宽高比
    private final int mIvRatio = 940 / 260;

    public PlatformOfFinanceAdapter(Context context) {
        this.mContext = context;
        mData = new LinkedList<>();
    }

    public void notifyDataSetChanged(List<PlatformOfFinanceBean> data, boolean isAdd) {
        if (!isAdd) {
            mData.clear();
        }
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();

            convertView = View.inflate(mContext, R.layout.item_finance_supermarket_of_platform, null);

            holder.mIvLogo = (ImageView) convertView.findViewById(R.id.item_finance_supermarket_of_platform_iv_logo);
            holder.mTvName = (TextView) convertView.findViewById(R.id.item_finance_supermarket_of_platform_tv_name);
            holder.mTvInfo = (TextView) convertView.findViewById(R.id.item_finance_supermarket_of_platform_tv_info);
            holder.mIvRenZheng = (ImageView) convertView.findViewById(R.id.item_finance_supermarket_of_platform_iv_renzheng);
            holder.mIvInfo = (ImageView) convertView.findViewById(R.id.item_finance_supermarket_of_platform_iv_info);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final PlatformOfFinanceBean platformOfFinanceBean = mData.get(position);

        holder.mIvRenZheng.setVisibility((!TextUtils.isEmpty(platformOfFinanceBean.getVideo_atc()) && "1".equalsIgnoreCase(platformOfFinanceBean.getVideo_atc())) ? View.VISIBLE : View.INVISIBLE);

        ImgLoadUtil.loadImgBySplice(platformOfFinanceBean.getP_logo(), holder.mIvLogo, R.mipmap.custom_platform_logo);

        holder.mTvName.setText(TextUtils.isEmpty(platformOfFinanceBean.getP_name()) ? "" : platformOfFinanceBean.getP_name());
        holder.mTvInfo.setText(TextUtils.isEmpty(platformOfFinanceBean.getIntro()) ? "" : platformOfFinanceBean.getIntro());

        ImgLoadUtil.loadImgBySplice(platformOfFinanceBean.getImage(), holder.mIvInfo, R.mipmap.custom_platform_logo);
        setImageLayoutParams(holder.mIvInfo);

        return convertView;
    }

    class ViewHolder {
        TextView mTvName;
        TextView mTvInfo;
        ImageView mIvLogo;
        ImageView mIvInfo;
        ImageView mIvRenZheng;
    }

    private void setImageLayoutParams(final View imageView) {
        if (0 >= mIvLayoutParamsWidth) {
            imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onGlobalLayout() {
                    mIvLayoutParamsWidth = imageView.getWidth();

                    ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                    layoutParams.height = mIvLayoutParamsWidth / mIvRatio;
                    imageView.setLayoutParams(layoutParams);

                    imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        } else {
            ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
            layoutParams.height = mIvLayoutParamsWidth / mIvRatio;
            imageView.setLayoutParams(layoutParams);
        }
    }


}
