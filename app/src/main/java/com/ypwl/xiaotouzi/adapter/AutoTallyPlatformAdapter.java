package com.ypwl.xiaotouzi.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.bean.AutoTallyPlatformBean;
import com.ypwl.xiaotouzi.utils.ImgLoadUtil;

import java.util.List;

/**
 * function:自动记账平台适配器
 * <p/>
 * Created by tengtao on 2015/12/7.
 */
public class AutoTallyPlatformAdapter extends BaseAdapter {
    private Context mContext;
    private List<AutoTallyPlatformBean.Entity> mDatas;

    public AutoTallyPlatformAdapter(Context context){
        this.mContext = context;
    }

    public void updateList(List<AutoTallyPlatformBean.Entity> datas){
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDatas!=null?mDatas.size():0;
    }

    @Override
    public Object getItem(int position) {
        return mDatas!=null?mDatas.get(position):null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder =null;
        if(convertView==null){
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_auto_tally_platform,null);
            holder.mIvLogo = (ImageView) convertView.findViewById(R.id.iv_item_auto_tally_logo);
            holder.mTvP_name = (TextView) convertView.findViewById(R.id.tv_item_auto_tally_p_name);
            holder.mIvBindTag = (ImageView) convertView.findViewById(R.id.iv_item_auto_tally_bind_tag);
            holder.mTvP_letter = (TextView) convertView.findViewById(R.id.tv_item_auto_tally_p_letter);
            holder.mIvDividerLine = convertView.findViewById(R.id.iv_item_auto_tally_line);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        AutoTallyPlatformBean.Entity entity = mDatas.get(position);
        //加载logo
        ImgLoadUtil.loadLogo(entity.getPid(), entity.getP_logo(), holder.mIvLogo, R.mipmap.pic_021);
        holder.mTvP_name.setText(entity.getP_name());
        holder.mIvBindTag.setImageResource("1".equals(entity.getIs_bind())?R.mipmap.wancheng:R.mipmap.tianjia);
        String p_letters = entity.getP_letters();//当前首字母
        boolean isNum2 = p_letters.matches("[0-9]+");//是否为数字
        holder.mTvP_letter.setText(isNum2?"#":p_letters);
        if(position > 0){
            String pLetters = mDatas.get(position - 1).getP_letters();//前一个首字母
            boolean isNum1 = pLetters.matches("[0-9]+");
            boolean b = p_letters.equalsIgnoreCase(pLetters) || (isNum1 && isNum2);
            holder.mTvP_letter.setVisibility(b?View.GONE:View.VISIBLE);
            holder.mIvDividerLine.setVisibility(b?View.VISIBLE:View.GONE);
        }else{
            holder.mTvP_letter.setVisibility(View.VISIBLE);
            holder.mIvDividerLine.setVisibility(View.GONE);
        }
        return convertView;
    }

    private class ViewHolder{
        private TextView mTvP_name,mTvP_letter;
        private ImageView mIvLogo;
        private ImageView mIvBindTag;
        private View mIvDividerLine;
    }
}
