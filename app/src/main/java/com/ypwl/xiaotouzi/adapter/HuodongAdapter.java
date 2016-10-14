package com.ypwl.xiaotouzi.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.bean.HuodongInfosBean;
import com.ypwl.xiaotouzi.common.URLConstant;
import com.ypwl.xiaotouzi.interf.IRequestCallback;
import com.ypwl.xiaotouzi.interf.UIAdapterListener;
import com.ypwl.xiaotouzi.manager.net.NetHelper;
import com.ypwl.xiaotouzi.ui.activity.HuodongItemDetailActivity;
import com.ypwl.xiaotouzi.utils.GlobalUtils;
import com.ypwl.xiaotouzi.utils.ImgLoadUtil;
import com.ypwl.xiaotouzi.utils.StringUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * function : 优惠活动列表数据适配器
 * <p/>
 * Created by lzj on 2015/11/8.
 */
public class HuodongAdapter extends BaseAdapter {
    private Activity mActivity;
    private UIAdapterListener mUIAdapterListener;
    private List<HuodongInfosBean.HuodongInfo> mDataList = new ArrayList<>();

    public HuodongAdapter(Activity activity, UIAdapterListener uiAdapterListener) {
        mActivity = activity;
        mUIAdapterListener = uiAdapterListener;
    }


    /**
     * 异步加载数据并可作为刷新页面数据使用
     */
    public void asyncLoadData() {
        String url = StringUtil.format(URLConstant.USER_HUODONG_LIEBIAO, GlobalUtils.token);
        NetHelper.get(url, new IRequestCallback<HuodongInfosBean>() {
            @Override
            public void onStart() {
                mUIAdapterListener.isLoading();
            }


            @Override
            public void onFailure(Exception e) {
                mUIAdapterListener.dataCountChanged(-1);
            }

            @Override
            public void onSuccess(HuodongInfosBean huodongInfosBean) {
                List<HuodongInfosBean.HuodongInfo> huodongList = huodongInfosBean.getList();
                if (huodongList == null || huodongList.size() == 0) {
                    mUIAdapterListener.dataCountChanged(mDataList.size());
                    return;
                }
                mDataList.clear();
                mDataList.addAll(huodongList);
                notifyDataSetChanged();
                mUIAdapterListener.dataCountChanged(mDataList.size());
            }
        });
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

    @SuppressWarnings("deprecation")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = View.inflate(mActivity, R.layout.item_huodong, null);
        }
        ImageView huodongImg = ViewHolder.findViewById(convertView, R.id.huodong_item_img);
        //        View layoutHuodongMsg = ViewHolder.findViewById(convertView, R.id.layout_huodong_item_info_msg);
        //        TextView huodongTitle = ViewHolder.findViewById(convertView, R.id.huodong_item_title);
        //        TextView huodongEndTime = ViewHolder.findViewById(convertView, R.id.huodong_item_endtime);
        //        layoutHuodongMsg.setBackgroundDrawable(ViewUtil.getAlphaBlackBgShape());

        HuodongInfosBean.HuodongInfo huodongInfo = mDataList.get(position);
        //        huodongTitle.setText(huodongInfo.getTitle());
        //        String endTime = StringUtil.format(R.string.huodong_end_time, DateTimeUtil.formatDateTime(huodongInfo.getEndtime() * 1000, DateTimeUtil.DF_YYYY_MM_DD));
        //        huodongEndTime.setText(endTime);
        ImgLoadUtil.loadImgByPath(huodongInfo.getImg(), huodongImg, R.drawable.bg_bottom_bar);

        huodongImg.setTag(R.id.huodong_item_img, huodongInfo);
        huodongImg.setOnClickListener(mOnClickListener);

        return convertView;
    }

    /** 点击条目查看活动详情跳转 */
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            HuodongInfosBean.HuodongInfo huodongInfo = (HuodongInfosBean.HuodongInfo) v.getTag(R.id.huodong_item_img);
            if (huodongInfo == null) {
                UIUtil.showToastShort("活动信息有误,请查看其他活动");
                return;
            }
            Intent intent = new Intent(mActivity, HuodongItemDetailActivity.class);
            intent.putExtra("title", huodongInfo.getTitle());
            intent.putExtra("url", huodongInfo.getUrl());
            mActivity.startActivity(intent);
        }
    };

}
