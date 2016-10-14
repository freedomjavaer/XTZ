package com.ypwl.xiaotouzi.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.ui.activity.FinancePawnRouteDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * function : 典当列表适配器
 * <p/>
 * Created by tengtao on 2016/4/18.
 */
public class FinanceMarketPawnAdapter extends RecyclerView.Adapter<FinanceMarketPawnAdapter.ViewHolder> {

    private Context mContext;
    private List<PoiItem> mList = new ArrayList<>();
    private LatLonPoint mStartPoint;
    private String city;


    public FinanceMarketPawnAdapter(Context context){
        this.mContext = context;
    }

    public void loadData(List<PoiItem> list,LatLonPoint startPoint, String city){
        this.city = city;
        this.mStartPoint = startPoint;
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(View.inflate(mContext, R.layout.item_finance_market_pawn_list_layout,null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PoiItem poiItem = mList.get(position);
        holder.mTvName.setText((position+1)+". "+poiItem.getTitle());//名称
        holder.mTvAddress.setText(poiItem.getSnippet());//地址
        int distance = poiItem.getDistance();//距离--米
        holder.mTvDistance.setText(distance+"米");
        //电话
        String tel = poiItem.getTel();
        if(tel.contains(";")){
            String[] split = tel.split(";");
            String phone = split[0];
            for(int i=1;i<split.length;i++){
                phone = phone+"\n"+split[i];
            }
            holder.mTvTel.setText(phone);
        }else{
            holder.mTvTel.setText(tel);
        }
        holder.mDivider.setVisibility(position==mList.size()-1?View.GONE:View.VISIBLE);

        holder.mItem.setTag(R.id.tv_pawn_address,position);
        holder.mItem.setOnClickListener(mOnClickListener);

        holder.mTvTarget.setTag(R.id.tv_begin_to_target, poiItem.getLatLonPoint());
        holder.mTvTarget.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mTvName,mTvDistance,mTvAddress,mTvTel,mTvTarget;
        private View mDivider,mItem;
        public ViewHolder(View itemView) {
            super(itemView);
            mItem = itemView.findViewById(R.id.item_pawn_list);
            mTvAddress = (TextView) itemView.findViewById(R.id.tv_pawn_address);
            mTvDistance = (TextView) itemView.findViewById(R.id.tv_pawn_distance);
            mTvName = (TextView) itemView.findViewById(R.id.tv_pawn_name);
            mDivider = itemView.findViewById(R.id.pawn_list_divider);
            mTvTel = (TextView) itemView.findViewById(R.id.tv_pawn_tel);
            mTvTarget = (TextView) itemView.findViewById(R.id.tv_begin_to_target);
        }
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_begin_to_target://路线
                    LatLonPoint point = (LatLonPoint) v.getTag(R.id.tv_begin_to_target);
                    Intent intent = new Intent(mContext,FinancePawnRouteDetailActivity.class);
                    intent.putExtra("city",city);
                    intent.putExtra("start",mStartPoint);
                    intent.putExtra("end",point);
                    mContext.startActivity(intent);
                    break;
                case R.id.item_pawn_list:
                    int position = (int) v.getTag(R.id.tv_pawn_address);
                    mOnPawnListItemClickListener.onPawnItemClick(position);
                    break;
            }
        }
    };

    private OnPawnListItemClickListener mOnPawnListItemClickListener;

    public void setOnPawnListItemClickListener(OnPawnListItemClickListener listener){
        mOnPawnListItemClickListener = listener;
    }
    public interface OnPawnListItemClickListener{
        void onPawnItemClick(int position);
    }
}
