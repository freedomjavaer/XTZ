package com.ypwl.xiaotouzi.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.bean.FinanceMarketBankBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.ui.activity.CommonWebPageActivity;
import com.ypwl.xiaotouzi.utils.ImgLoadUtil;
import com.ypwl.xiaotouzi.utils.StringUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.utils.ViewHolder;
import com.ypwl.xiaotouzi.view.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * function : 金融超市--银行数据适配器
 * <p>
 * Created by tengtao on 2016/4/14.
 */
public class FinanceMarketBankAdapter extends BaseAdapter {

    private Context mContext;
    private List<FinanceMarketBankBean.ListEntity> mList = new ArrayList<>();

    public FinanceMarketBankAdapter(Context context){
        this.mContext = context;
    }

    public void loadData(List<FinanceMarketBankBean.ListEntity> list){
        mList.clear();
        mList.addAll(list);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_finance_market_bank_layout,null);
        }
        LinearLayout itemView = ViewHolder.findViewById(convertView,R.id.item_finance_bank);
        CircleImageView mIvBankIcon = ViewHolder.findViewById(convertView, R.id.iv_finance_bank_icon);
        TextView mTvBankName = ViewHolder.findViewById(convertView,R.id.tv_finance_bank_name);

        FinanceMarketBankBean.ListEntity listEntity = mList.get(position);
        ImgLoadUtil.loadImgBySplice(listEntity.getImage(),mIvBankIcon,R.mipmap.pic_021);
        mTvBankName.setText(listEntity.getName());

        itemView.setTag(R.id.item_finance_bank,listEntity.getUrl());
        itemView.setTag(R.id.iv_finance_bank_icon,listEntity.getName());
        itemView.setOnClickListener(mOnClickListener);

        return convertView;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String url = (String) v.getTag(R.id.item_finance_bank);
//            String name = (String) v.getTag(R.id.iv_finance_bank_icon);
            String name = UIUtil.getString(R.string.finance_market_credit_card);
            if(StringUtil.isEmptyOrNull(url)){return;}
            Intent intent = new Intent(mContext, CommonWebPageActivity.class);
            intent.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA,url);
            intent.putExtra(Const.KEY_INTENT_JUMP_FROM_DATA,name);
            mContext.startActivity(intent);
        }
    };
}
