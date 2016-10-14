package com.ypwl.xiaotouzi.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.utils.UIUtil;

/**
 * function : 我的投资---回款状态更改的view
 * <p>
 * Created by tengtao on 2016/3/23.
 */
public class InvestStatusChangeView extends PopupWindow {
    private TextView mTvCancel;
    private View mMenuView;
    private String status = "1";
    private IStatusSelectedListener mIChangeStatusListener;
    private Context mContext;
    private FlowLayout mFlowLayout;
    private String[] choiceItems0 = new String[]{"已回","逾期"};
    private String[] choiceItems1 = new String[]{"未回","逾期"};
    private String[] choiceItems2 = new String[]{"已回","未逾期"};

    public InvestStatusChangeView(Context context) {
        super(context);
        init(context);
    }

    public InvestStatusChangeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public InvestStatusChangeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.layout_invest_recent_back_bottom_window, null);
        mFlowLayout = (FlowLayout) mMenuView.findViewById(R.id.fl_choose_item_container);
        mFlowLayout.setSpace(0,0);
        mTvCancel = (TextView) mMenuView.findViewById(R.id.tv_invest_cancel);
        mTvCancel.setTag(R.id.fl_choose_item_container,"取消");
        mTvCancel.setOnClickListener(mOnClickListener);

        this.setContentView(mMenuView);
        //设置弹出窗体的宽、高
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //设置弹出窗体可点击
        this.setFocusable(true);
        //设置弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimationChangeInvestStatus);
        this.setBackgroundDrawable(new BitmapDrawable());
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y=(int) event.getY();
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(y<height){
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    public void show(String status){
        switch (status){
            case "0":
                addChoiceItem(choiceItems0);
                break;
            case "1":
                addChoiceItem(choiceItems1);
                break;
            case "2":
                addChoiceItem(choiceItems2);
                break;
            case "3":
                addChoiceItem(choiceItems0);
                break;
        }
    }

    private void addChoiceItem(String[] items){
        for(int i=0;i<items.length;i++){
            //添加textview
            TextView tv = new TextView(mContext);
            tv.setText(items[i]);
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(Color.parseColor("#2c7de1"));
            tv.setTag(R.id.fl_choose_item_container,items[i]);
            tv.setOnClickListener(mOnClickListener);
            tv.setPadding(0, UIUtil.dip2px(12),0,UIUtil.dip2px(12));
            tv.setBackgroundDrawable(UIUtil.getDrawable((i == items.length - 1) ?
                    R.drawable.shape_rectangle_invest_back_status_change_bg_selector :
                    R.drawable.selector_color_bg_light_gray_white));
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(params);
            mFlowLayout.addView(tv);
            //添加分割线
            if(i < items.length - 1){
                View line = new View(mContext);
                line.setBackgroundColor(mContext.getResources().getColor(R.color.f));
                ViewGroup.LayoutParams lineParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIUtil.dip2px(1));
                line.setLayoutParams(lineParams);
                mFlowLayout.addView(line);
            }
        }
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String text = (String) v.getTag(R.id.fl_choose_item_container);
            switch (text) {
                case "已回":
                    status = "1";
                    mIChangeStatusListener.onStatusSelected(status);
                    break;
                case "未回":
                    status = "0";
                    mIChangeStatusListener.onStatusSelected(status);
                    break;
                case "逾期":
                    status = "2";
                    mIChangeStatusListener.onStatusSelected(status);
                    break;
                case "未逾期":
                    status = "0";
                    mIChangeStatusListener.onStatusSelected(status);
                    break;
                case "取消":
                    break;
            }
            dismiss();
        }
    };

    public interface IStatusSelectedListener {
        void onStatusSelected(String status);
    }


    public void setStatusSelectedListener(IStatusSelectedListener listener) {
        this.mIChangeStatusListener = listener;
    }
}
