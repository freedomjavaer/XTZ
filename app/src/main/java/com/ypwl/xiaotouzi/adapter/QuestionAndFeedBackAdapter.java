package com.ypwl.xiaotouzi.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.bean.QuestionAndFeedBackBean;
import com.ypwl.xiaotouzi.utils.LogUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * function : 常见问题与反馈数据适配器
 * <p/>
 * Created by tengtao on 2016/5/3.
 */
public class QuestionAndFeedBackAdapter extends RecyclerView.Adapter<QuestionAndFeedBackAdapter.ViewHolder> {
    private String TAG = "QuestionAndFeedBackAdapter";
    private Context mContext;
    private List<QuestionAndFeedBackBean.ListBean.QuestionBean> mList = new ArrayList<>();
    private Map<Integer,View> clickView = new HashMap<>();
    private Map<Integer,View> arrowView = new HashMap<>();
    public QuestionAndFeedBackAdapter(Context context){
        this.mContext = context;
        clickView.clear();
        arrowView.clear();
    }

    /** 加载数据 */
    public void loadData(List<QuestionAndFeedBackBean.ListBean> list){
        mList.clear();
        for (int i = 0; i < list.size(); i++) {
            //获取列表对象
            QuestionAndFeedBackBean.ListBean listBean = list.get(i);
            //创建问题数据bean
            QuestionAndFeedBackBean.ListBean.QuestionBean questionBean1 =
                    new QuestionAndFeedBackBean.ListBean.QuestionBean();
            questionBean1.setTitle(listBean.getName());
            questionBean1.setContent("");
            questionBean1.setShowContent(false);
            questionBean1.setTotalTitle(true);
            mList.add(questionBean1);
            //获取对应的问题list
            List<QuestionAndFeedBackBean.ListBean.QuestionBean> questions = listBean.getQuestion();
            for (int j = 0; j < questions.size(); j++) {
                QuestionAndFeedBackBean.ListBean.QuestionBean preBean = questions.get(j);
                QuestionAndFeedBackBean.ListBean.QuestionBean questionBean2 =
                        new QuestionAndFeedBackBean.ListBean.QuestionBean();
                questionBean2.setTitle(preBean.getTitle());
                questionBean2.setContent(preBean.getContent());
                questionBean2.setTid(preBean.getTid());
                questionBean2.setShowContent(false);
                questionBean2.setTotalTitle(false);
                mList.add(questionBean2);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(View.inflate(mContext, R.layout.item_question_and_feedback_layout,null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        QuestionAndFeedBackBean.ListBean.QuestionBean questionBean = mList.get(position);
        /**是否为总标题*/
        boolean totalTitle = questionBean.isTotalTitle();
        /**是否显示内容*/
        boolean showContent = questionBean.isShowContent();
        holder.mTvTitle.setTextSize(totalTitle?14:15);
        holder.mTvTitle.setTextColor(Color.parseColor(totalTitle?"#6E6E70":"#000000"));
        holder.mTvTitle.setPadding(UIUtil.dip2px(totalTitle?12:12),UIUtil.dip2px(totalTitle?12:14),
                UIUtil.dip2px(totalTitle?20:56),UIUtil.dip2px(totalTitle?12:14));
        holder.mTvTitle.setText(questionBean.getTitle());
        if(totalTitle){
            holder.mTitleContainer.setBackgroundColor(mContext.getResources().getColor(R.color.question_feedback_bg));
        }else{
            holder.mTitleContainer.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.selector_color_bg_gray_white));
        }
        String content = questionBean.getContent();
        holder.mTvContent.setText(content);
        holder.mItemLayout.setVisibility(showContent?View.VISIBLE:View.GONE);
        holder.mIvArrow.setVisibility(totalTitle?View.GONE:View.VISIBLE);
        holder.mIvArrow.setImageDrawable(mContext.getResources().getDrawable(showContent?R.mipmap.btn_018_reverse :R.mipmap.btn_018));
        /**设置点击事件*/
        holder.mTitleContainer.setTag(R.id.tv_question_feedback_item_title,questionBean);
        holder.mTitleContainer.setTag(R.id.item_content_layout,holder.mItemLayout);
        holder.mTitleContainer.setTag(R.id.tv_question_feedback_item_content,position);
        holder.mTitleContainer.setTag(R.id.iv_question_feedback_arrow,holder.mIvArrow);
        holder.mTitleContainer.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView mIvArrow;
        TextView mTvTitle,mTvContent;
        View mItemLayout,mTitleContainer;
        public ViewHolder(View itemView) {
            super(itemView);
            mTvTitle = (TextView) itemView.findViewById(R.id.tv_question_feedback_item_title);
            mTvContent = (TextView) itemView.findViewById(R.id.tv_question_feedback_item_content);
            mItemLayout = itemView.findViewById(R.id.item_content_layout);
            mIvArrow = (ImageView) itemView.findViewById(R.id.iv_question_feedback_arrow);
            mTitleContainer = itemView.findViewById(R.id.ll_question_feedback_title);
        }
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ImageView arrow = (ImageView) v.getTag(R.id.iv_question_feedback_arrow);
            int position = (int) v.getTag(R.id.tv_question_feedback_item_content);
            View itemContent = (View) v.getTag(R.id.item_content_layout);
            QuestionAndFeedBackBean.ListBean.QuestionBean questionBean =
                    (QuestionAndFeedBackBean.ListBean.QuestionBean) v.getTag(R.id.tv_question_feedback_item_title);
            /**是否为大标题*/
            boolean totalTitle = questionBean.isTotalTitle();
            /**是否显示内容 */
            boolean showContent = questionBean.isShowContent();
            if(!totalTitle){
                for(Map.Entry<Integer,View> entry : clickView.entrySet()){
                    View view = entry.getValue();
                    Integer key = entry.getKey();
                    if(key != position && mList.get(key).isShowContent()){//点击的不是同一个，先关闭原来打开的
                        LogUtil.e(TAG,"关闭已打开的，位置："+key);
                        close(view);
                        if(arrowView.containsKey(key)) {
                            ImageView iv = (ImageView) arrowView.get(key);
                            iv.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.btn_018));
                        }
                        mList.get(key).setShowContent(false);
                    }
                }
                clickView.clear();
                arrowView.clear();
                if(showContent){//打开--->关闭
                    LogUtil.e(TAG,"关闭点击位置："+position);
                    close(itemContent);
                    clickView.remove(position);
                    arrowView.remove(position);
                }else{//关闭--->打开
                    LogUtil.e(TAG,"打开点击位置："+position);
                    open(itemContent);
                    clickView.put(position,itemContent);
                    arrowView.put(position,arrow);
                }
                questionBean.setShowContent(!showContent);
                arrow.setImageDrawable(mContext.getResources().getDrawable(questionBean.isShowContent()?R.mipmap.btn_018_reverse :R.mipmap.btn_018));
            }
        }
    };

    /** 展开 */
    private void open(final View v) {
        //先测量获取高度
        v.measure(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();
        //设置高度为0，显示出来
        v.getLayoutParams().height = 0;
        v.setVisibility(View.VISIBLE);
        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = (interpolatedTime == 1) ? RelativeLayout.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }
            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        animation.setDuration(200);
        v.startAnimation(animation);
    }

    /** 关闭 */
    private void close(final View v) {
        final int initialHeight = v.getMeasuredHeight();
        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1) {
                    v.setVisibility(View.GONE);//关闭后隐藏
                } else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }
            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        animation.setDuration(200);
        v.startAnimation(animation);
    }
}