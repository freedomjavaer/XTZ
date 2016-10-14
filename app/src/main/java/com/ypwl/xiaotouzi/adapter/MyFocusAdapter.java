package com.ypwl.xiaotouzi.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.bean.MyFocusBean;
import com.ypwl.xiaotouzi.manager.PlatformCompareManager;
import com.ypwl.xiaotouzi.ui.activity.MyFocusActivity;
import com.ypwl.xiaotouzi.utils.ImgLoadUtil;

import java.util.List;

/**
 * 我的关注数据适配器
 */
public class MyFocusAdapter extends BaseAdapter {

    private Context context;
    private List<MyFocusBean.FocusEntity> datas;
    private OnAbolishFollowPlatformListener mAbolishFollowPlatformListener;

    /**
     * 初始化构造方法
     *
     * @param datas
     * @param context
     */
    public MyFocusAdapter(List<MyFocusBean.FocusEntity> datas, Context context) {
        this.context = context;
        this.datas = datas;
    }

    public void setData(List<MyFocusBean.FocusEntity> datas){
        this.datas = datas;
    }
    @Override
    public int getCount() {
        if (datas != null) {
            return datas.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (datas != null) {
            return datas.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.my_focus_item, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
            holder.mIvChoice = (ImageView) convertView.findViewById(R.id.item_focus_choice);
            holder.mIvIcon = (ImageView) convertView.findViewById(R.id.item_focus_icon);
            holder.mIvStar = (ImageView) convertView.findViewById(R.id.item_focus_star);
            holder.mTvName = (TextView) convertView.findViewById(R.id.item_focus_title);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MyFocusBean.FocusEntity bean = datas.get(position);
        //设置数据
        bean.setSelected(PlatformCompareManager.getList().contains(bean.getPid())?true:false);
        holder.mIvChoice.setImageResource(bean.isSelected() ? R.mipmap.btn_007_select : R.mipmap.btn_015);
        holder.mIvChoice.setVisibility(MyFocusActivity.addCompared ? View.VISIBLE : View.GONE);
        //取消关注
        holder.mIvStar.setImageResource(R.mipmap.focus_blue_star);
        holder.mIvStar.setVisibility(MyFocusActivity.addCompared ? View.GONE : View.VISIBLE);
        //图标
        ImgLoadUtil.loadLogo(bean.getPid(), bean.getP_logo(), holder.mIvIcon, 0);
        //名称
        holder.mTvName.setText(bean.getP_name());

        holder.mIvStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出取消关注对话框
                String pid = datas.get(position).getPid();
                showDialog(pid, position);
            }
        });
        return convertView;
    }

    public class ViewHolder {
        public ImageView mIvChoice;
        public ImageView mIvIcon;
        public TextView mTvName;
        public ImageView mIvStar;
    }

    /**
     * 取消关注对话框
     *
     * @param pid
     * @param position
     */
    private void showDialog(final String pid, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("您确定要取消关注"+datas.get(position).getP_name()+"吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAbolishFollowPlatformListener.onAbolishFollow(pid, position);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 取消关注的监听器
     * @param listener
     */
    public void setOnAbolishFollowPlatformListener(OnAbolishFollowPlatformListener listener) {
        this.mAbolishFollowPlatformListener = listener;
    }

    /**
     * 定义接口
     */
    public interface OnAbolishFollowPlatformListener {
        // 取消关注
        void onAbolishFollow(String pid, int position);
    }
}
