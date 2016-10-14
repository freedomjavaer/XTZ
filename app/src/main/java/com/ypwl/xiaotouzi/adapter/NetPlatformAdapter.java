package com.ypwl.xiaotouzi.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.bean.AllPlatformBean;
import com.ypwl.xiaotouzi.bean.LoginBean;
import com.ypwl.xiaotouzi.bean.VideoInfoBean;
import com.ypwl.xiaotouzi.ui.activity.LoginActivity;
import com.ypwl.xiaotouzi.ui.activity.NetPlatformActivity;
import com.ypwl.xiaotouzi.ui.activity.PlatformDetailActivity;
import com.ypwl.xiaotouzi.ui.activity.PlatformVideoActivity;
import com.ypwl.xiaotouzi.utils.ImgLoadUtil;
import com.ypwl.xiaotouzi.utils.Util;
import com.ypwl.xiaotouzi.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * function : 主页网贷平台数据列表数据适配器
 * <p/>
 * Created by lzj on 2015/11/10.
 */
public class NetPlatformAdapter extends BaseAdapter {

    private boolean isLoading;
    private Context mContext;
    private List<AllPlatformBean.DataEntity> mDataList = new ArrayList<>();

    public NetPlatformAdapter(Context context) {
        this.mContext = context;
    }

    public void setLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    /** 刷新视图数据 */
    public void laodData(List<AllPlatformBean.DataEntity> dataList, boolean isAddData) {
        if (dataList == null) {
            return;
        }
        if (!isAddData) {
            mDataList.clear();
        }
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public List<AllPlatformBean.DataEntity> getDataList() {
        return mDataList;
    }


    public void clearDataList() {
        mDataList.clear();
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_platform_recyclerview_content, null);
        }
        LinearLayout layoutItem = ViewHolder.findViewById(convertView, R.id.item_flatform_ll);
        ImageView logo = ViewHolder.findViewById(convertView, R.id.item_flatform_iv_logo);
        TextView name = ViewHolder.findViewById(convertView, R.id.item_flatform_tv_pname);
        TextView level = ViewHolder.findViewById(convertView, R.id.item_flatform_tv_level);
        TextView bidderNum = ViewHolder.findViewById(convertView, R.id.item_flatform_tv_bidder_num);
        TextView averageRate = ViewHolder.findViewById(convertView, R.id.item_flatform_tv_average_rate);
        ImageView plogo_play = ViewHolder.findViewById(convertView, R.id.item_flatfrom_iv_icon_video);
        FrameLayout layoutLogo = ViewHolder.findViewById(convertView, R.id.item_flatform_fl_logo);

        AllPlatformBean.DataEntity bean = mDataList.get(position);
//        logo.setTag(bean.getP_logo());
//        logo.setImageResource(R.mipmap.pic_021);
//        if(logo.getTag()!=null && logo.getTag().equals(bean.getP_logo())) {
//            Util.loadLogo(bean.getPid(), bean.getP_logo(), logo, 0);
//        }
        ImgLoadUtil.loadLogo(bean.getPid(), bean.getP_logo(), logo, R.mipmap.pic_021);
        name.setText(bean.getP_name());
        level.setText(bean.getLevel());
        bidderNum.setText(bean.getBidder_num());
        averageRate.setText(bean.getAverage_rate() + "%");
        plogo_play.setVisibility((bean.getVideoInfo()).size() > 0 ? View.VISIBLE : View.GONE);

        layoutItem.setTag(R.id.item_flatform_tv_pname, bean);
        layoutItem.setOnClickListener(mOnClickListener);
        layoutLogo.setTag(R.id.item_flatform_tv_pname, bean);
        layoutLogo.setOnClickListener(mOnClickListener);

        return convertView;
    }


    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isLoading || NetPlatformActivity.isShaiXuan) {
                return;
            }
            AllPlatformBean.DataEntity bean = (AllPlatformBean.DataEntity) v.getTag(R.id.item_flatform_tv_pname);
            if (bean == null) {
                return;
            }
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            switch (v.getId()) {
                case R.id.item_flatform_ll://点击条目
                    intent.setClass(mContext, PlatformDetailActivity.class);
                    bundle.putString("pid", bean.getPid());
                    bundle.putString("p_name", bean.getP_name());
                    break;
                case R.id.item_flatform_fl_logo://点击logo
                    List<VideoInfoBean> videoInfos = bean.getVideoInfo();
                    if (videoInfos.size() == 0) {
                        return;
                    }
                    if (!checkLogin()) {//没有登录
                        intent.setClass(mContext, LoginActivity.class);
                        mContext.startActivity(intent);
                        return;
                    }
                    intent.setClass(mContext, PlatformVideoActivity.class);
                    bundle.putString("p_name", bean.getP_name());
                    bundle.putParcelableArrayList("video_url", (ArrayList<? extends Parcelable>) videoInfos);
//                    bundle.putSerializable("video_url", (Serializable) videoInfos);
                    break;
            }
            intent.putExtras(bundle);
            mContext.startActivity(intent);
        }
    };

    /** 检测是否登录 */
    private boolean checkLogin() {
        LoginBean loginBean = Util.legalLogin();
        return (loginBean != null);
    }
}
