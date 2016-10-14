package com.ypwl.xiaotouzi.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.utils.ImgLoadUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * function : 平台详情公司信息公司图片列表数据适配器
 * <p/>
 * Created by lzj on 2015/11/8.
 */
public class PlatformDetailCompanyImgAdapter extends BaseAdapter {
    private Activity mActivity;
    private String mPid;
    private List<String> mDataList = new ArrayList<>();

    public PlatformDetailCompanyImgAdapter(Activity activity, String platformPid) {
        mActivity = activity;
        mPid = platformPid;
    }

    public void loadData(List<String> dataList) {
        if (dataList != null && dataList.size() > 0) {
            mDataList.clear();
            mDataList.addAll(dataList);
        }
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
        View view;
        if (null == convertView) {
            view = View.inflate(mActivity, R.layout.item_platform_detail_company_img, null);
        }else {
            view = convertView;
        }
        ImageView huodongImg = (ImageView) view.findViewById(R.id.img_company);
        String imgFileName = mDataList.get(position);
        ImgLoadUtil.loadLogo(mPid, imgFileName, huodongImg, R.drawable.pic_021);
        return view;
    }
}
