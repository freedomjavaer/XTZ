package com.ypwl.xiaotouzi.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.bean.FinanceMarketCreditCardBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.ui.activity.CommonWebPageActivity;
import com.ypwl.xiaotouzi.utils.ImgLoadUtil;
import com.ypwl.xiaotouzi.utils.StringUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.view.CustomRoundImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * function : 金融超市--信用卡数据适配器
 * <p>
 * Created by tengtao on 2016/4/14.
 */
public class FinanceMarketCreditCardAdapter extends BaseAdapter {

    private Context mContext;
    private List<FinanceMarketCreditCardBean.ListBean> mList = new ArrayList<>();

    public FinanceMarketCreditCardAdapter(Context context){
        this.mContext = context;
    }

    public void loadData(List<FinanceMarketCreditCardBean.ListBean> list){
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
        if(convertView==null){
            convertView = View.inflate(mContext,R.layout.item_finance_market_credit_card_layout,null);
        }
        CustomRoundImageView mIvCardIcon = (CustomRoundImageView) convertView.findViewById(R.id.iv_finance_credit_card_icon);
        TextView mTvCardName = (TextView) convertView.findViewById(R.id.tv_finance_credit_card_name);
        TextView mTvCardIntro1 = (TextView) convertView.findViewById(R.id.tv_finance_credit_card_intro1);
        TextView mTvCardIntro2 = (TextView) convertView.findViewById(R.id.tv_finance_credit_card_intro2);
        LinearLayout item = (LinearLayout) convertView.findViewById(R.id.finance_credit_card_item);

        FinanceMarketCreditCardBean.ListBean listEntity = mList.get(position);
        mIvCardIcon.setRectAdius(UIUtil.dip2px(3));
        ImgLoadUtil.loadImgBySplice(listEntity.getImage(),mIvCardIcon,R.mipmap.pic_021);
        mTvCardName.setText(listEntity.getName());
        String description = listEntity.getDescription();
        mTvCardIntro1.setText(description);
        String bank = listEntity.getBank();
        mTvCardIntro2.setText(bank);

        item.setTag(R.id.iv_finance_credit_card_icon,listEntity.getUrl());
        item.setTag(R.id.tv_finance_credit_card_name,listEntity.getName());
        item.setOnClickListener(mClickListener);
        return convertView;
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String url = (String) v.getTag(R.id.iv_finance_credit_card_icon);
            String name = (String) v.getTag(R.id.tv_finance_credit_card_name);
            if(StringUtil.isEmptyOrNull(url)){return;}
            Intent intent = new Intent(mContext, CommonWebPageActivity.class);
            intent.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA,url);
            intent.putExtra(Const.KEY_INTENT_JUMP_BASE_DATA_1,name);
            intent.putExtra(Const.KEY_INTENT_JUMP_FROM_DATA,"信用卡");
            mContext.startActivity(intent);
        }
    };
}
