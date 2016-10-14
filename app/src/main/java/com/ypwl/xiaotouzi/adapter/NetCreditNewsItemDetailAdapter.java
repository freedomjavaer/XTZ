package com.ypwl.xiaotouzi.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.bean.NetCreditNewsDetailBean;
import com.ypwl.xiaotouzi.utils.DateTimeUtil;
import com.ypwl.xiaotouzi.utils.ImgLoadUtil;
import com.ypwl.xiaotouzi.utils.StringUtil;
import com.ypwl.xiaotouzi.utils.ViewHolder;
import com.ypwl.xiaotouzi.view.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * function : 网贷新闻详情评论item适配器
 * <p/>
 * Created by tengtao on 2016/3/15.
 */
public class NetCreditNewsItemDetailAdapter extends BaseAdapter {

    private INewsReplyListener mNewsReplyListener;
    private Activity mContent;
    private List<NetCreditNewsDetailBean.ListBean> mEntityList = new ArrayList<>();

    public NetCreditNewsItemDetailAdapter(Activity content){
        this.mContent = content;
    }

    public void loadData(List<NetCreditNewsDetailBean.ListBean> listEntitiey, boolean isLoadMore){
        if (listEntitiey == null || listEntitiey.size() == 0) {
            return;
        }
        if(!isLoadMore){
            mEntityList.clear();
        }
        mEntityList.addAll(listEntitiey);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mEntityList!=null?mEntityList.size():0;
    }

    @Override
    public Object getItem(int position) {
        return mEntityList!=null?mEntityList.get(position):null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = View.inflate(mContent, R.layout.item_netcredit_news_detail_reply_layout,null);
        }
        CircleImageView icon = ViewHolder.findViewById(convertView, R.id.iv_news_reply_icon);
        TextView replynickName = ViewHolder.findViewById(convertView, R.id.tv_news_reply_nickname);
        TextView replyContent = ViewHolder.findViewById(convertView,R.id.tv_news_reply_content);
        TextView replyDate = ViewHolder.findViewById(convertView,R.id.tv_news_reply_date);
        View mDivider = ViewHolder.findViewById(convertView,R.id.netcredit_divider_line);

        NetCreditNewsDetailBean.ListBean listEntity = mEntityList.get(position);
        //@对象
        String toname = listEntity.getToname();
        String content = listEntity.getContent();
        //回复内容
        replyContent.setText(StringUtil.isEmptyOrNull(toname)?content:"@"+toname+":"+content);
        //昵称
        replynickName.setText(listEntity.getNickname());
        mDivider.setVisibility(position==mEntityList.size()-1?View.GONE:View.VISIBLE);
        //图像
        ImgLoadUtil.loadImgBySplice(listEntity.getHeadimage(),icon, R.mipmap.default_user_icon);
        //回复时间
        long addTime=0;
        try{
            addTime = Long.parseLong(listEntity.getAddtime())*1000;
        }catch (Exception e){e.printStackTrace();}
        replyDate.setText(DateTimeUtil.formatFriendly(addTime));
        replyDate.setVisibility(addTime==0?View.INVISIBLE:View.VISIBLE);

        convertView.setOnClickListener(mItemListener);
        convertView.setTag(R.id.tv_news_reply_nickname, listEntity.getUserid());
        convertView.setTag(R.id.iv_news_reply_icon,listEntity.getNickname());
        return convertView;
    }

    private View.OnClickListener mItemListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String userId = (String) v.getTag(R.id.tv_news_reply_nickname);
            String nickName = (String) v.getTag(R.id.iv_news_reply_icon);
            mNewsReplyListener.newsTargetReply(nickName,userId);
        }
    };

    public interface INewsReplyListener{
        void newsTargetReply(String nickName,String userId);
    }

    public void setINewsReplyListener(INewsReplyListener listener){
        this.mNewsReplyListener = listener;
    }
}
