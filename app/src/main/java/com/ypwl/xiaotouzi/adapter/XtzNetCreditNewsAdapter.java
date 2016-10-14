package com.ypwl.xiaotouzi.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.bean.XtzNetCreditNewsBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.ui.activity.NetCreditNewsDetailActivity;
import com.ypwl.xiaotouzi.utils.DateTimeUtil;
import com.ypwl.xiaotouzi.utils.ImgLoadUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.ViewHolder;

import java.util.List;

/**
 * function : 晓投资主页网贷新闻适配器
 * <p/>
 * Created by tengtao on 2016/3/15.
 */
public class XtzNetCreditNewsAdapter extends BaseAdapter{

    private Activity mContext;
    private List<XtzNetCreditNewsBean.ListBean> mDataList;

    public XtzNetCreditNewsAdapter(Activity context){
        mContext = context;
    }

    public void loadData(List<XtzNetCreditNewsBean.ListBean> dataList){
        if(dataList==null || dataList.size()==0){
            return;
        }
        mDataList= dataList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDataList!=null?mDataList.size():0;
    }

    @Override
    public Object getItem(int position) {
        return mDataList!=null?mDataList.get(position):null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = View.inflate(mContext,R.layout.item_xtz_netcredit_news_layout,null);
        }
        ImageView mIvItemIcon = ViewHolder.findViewById(convertView,R.id.iv_xtz_news_item_icon);
        TextView mTvItemTitle = ViewHolder.findViewById(convertView,R.id.tv_xtz_news_item_title);
        //TextView mTvItemContent = ViewHolder.findViewById(convertView,R.id.tv_xtz_news_item_content);
        TextView mTvItemReadNum = ViewHolder.findViewById(convertView,R.id.tv_xtz_news_item_readnum);
        RelativeLayout mItemView = ViewHolder.findViewById(convertView,R.id.layout_net_credit_news_item);
        TextView mNewsAddtime = ViewHolder.findViewById(convertView, R.id.tv_news_addtime);
        TextView mNewsSource = ViewHolder.findViewById(convertView, R.id.tv_news_source);

        Drawable drawable= mContext.getResources().getDrawable(R.mipmap.news_readed);
        drawable.setBounds(0, 0, UIUtil.dip2px(14), UIUtil.dip2px(10));
        mTvItemReadNum.setCompoundDrawables(drawable, null, null, null);

        XtzNetCreditNewsBean.ListBean ListBean = mDataList.get(position);
        //标题
        mTvItemTitle.setText(ListBean.getTitle());
        //内容
        //mTvItemContent.setText(ListBean.getIntro());
        //来源
        mNewsSource.setText("来源：" + ListBean.getSource());
        //阅读数量
        mTvItemReadNum.setText(ListBean.getClick());
        //加载图片
        ImgLoadUtil.loadImgBySplice(ListBean.getImage(), mIvItemIcon, R.mipmap.pic_021);
        //发表时间
        mNewsAddtime.setText(DateTimeUtil.formatDate(Long.parseLong(ListBean.getAddtime())));

        String news_id = mDataList.get(position).getNews_id();
        mItemView.setTag(R.id.layout_net_credit_news_item,news_id);
        mItemView.setOnClickListener(mOnClickListener);
        return convertView;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String news_id = (String) v.getTag(R.id.layout_net_credit_news_item);
            Intent intent = new Intent(mContext,NetCreditNewsDetailActivity.class);
            intent.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA,news_id);
            mContext.startActivity(intent);
        }
    };
}
