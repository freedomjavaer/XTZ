package com.ypwl.xiaotouzi.ui.page;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.base.BasePage;
import com.ypwl.xiaotouzi.utils.UIUtil;

import java.util.List;
import java.util.Map;

/**
 * function:计算器结果展示页面二
 * <p/>
 * Created by tengtao on 2016/1/13.
 */
public class CalculatorHalfListPage extends BasePage {
    private LinearLayout mDataContainer;
    private Button mCalHalfListBtn;

    public CalculatorHalfListPage(Activity context) {
        super(context);
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = View.inflate(mContext, R.layout.layout_calculator_result_halflist,null);
        mDataContainer = (LinearLayout) view.findViewById(R.id.ll_calculator_half_data_container);
        mCalHalfListBtn = (Button) view.findViewById(R.id.calHalfListBtn);

        mCalHalfListBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mListener.showCalResultFullList();
                return false;
            }
        });
        return view;
    }

    @Override
    public void initData() {

    }

    /**
     * 更新数据
     * @param list
     */
    public void loadHalfList(List<Map<String, String>> list){
        mDataContainer.removeAllViews();
        for(int i=0; i< list.size();i++){
            //添加条目
            View v = View.inflate(mContext,R.layout.cal_data_item,null);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIUtil.dip2px(40));
            v.setLayoutParams(params);
            TextView qishu = (TextView) v.findViewById(R.id.calItemQishu);
            TextView ysbj = (TextView) v.findViewById(R.id.calItemYingshoubenjin);
            TextView yssy = (TextView) v.findViewById(R.id.calItemYingshoushouyi);
            TextView dsbj = (TextView) v.findViewById(R.id.calItemDaishoubenjin);
            TextView ssze = (TextView) v.findViewById(R.id.calItemShishouzonge);
            qishu.setText(list.get(i).get("qishu"));
            ysbj.setText(list.get(i).get("yingshoubenjin"));
            yssy.setText(list.get(i).get("yingshoushouyi"));
            dsbj.setText(list.get(i).get("daishoubenjin"));
            ssze.setText(list.get(i).get("shishouzonge"));
            mDataContainer.addView(v);
            //添加线
            View line = new View(mContext);
            line.setBackgroundColor(Color.parseColor("#cccccc"));
            ViewGroup.LayoutParams p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
            line.setLayoutParams(p);
            mDataContainer.addView(line);
        }
    }

    private OnCalculatorHalfListListener mListener;
    public void setOnCalculatorHalfListListener(OnCalculatorHalfListListener listener){
        this.mListener = listener;
    }
    public interface OnCalculatorHalfListListener{
        void showCalResultFullList();
    }
}
