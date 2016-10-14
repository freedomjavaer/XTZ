package com.ypwl.xiaotouzi.blockview;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.adapter.ItemPlatformChooseFollowAdapter;
import com.ypwl.xiaotouzi.base.BaseBlock;
import com.ypwl.xiaotouzi.bean.PlatformChooseAdapterBean;
import com.ypwl.xiaotouzi.bean.PlatformChooseBean;
import com.ypwl.xiaotouzi.ui.activity.MyFocusActivity;
import com.ypwl.xiaotouzi.utils.UIUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * function : 平台选择页面我的关注模块
 * <p/>
 * Created by tengtao on 2016/5/5.
 */
public class PlatformChooseFollwBlock extends BaseBlock<PlatformChooseBean> {
    private TextView mTvFollowMore;
    private ListView mListView;
    private ItemPlatformChooseFollowAdapter mFollowAdapter;
    private List<PlatformChooseAdapterBean> followList = new ArrayList<>();

    /**
     * 初始化试图
     */
    @Override
    protected View initView() {
        View view = View.inflate(UIUtil.getContext(), R.layout.block_layout_platform_choose_follow, null);
        mTvFollowMore = (TextView) view.findViewById(R.id.tv_my_follow_more);
        mListView = (ListView) view.findViewById(R.id.my_follow_container);
        mFollowAdapter = new ItemPlatformChooseFollowAdapter();
        mListView.setAdapter(mFollowAdapter);
        return view;
    }

    /**
     * 加载数据，更新UI
     */
    @Override
    protected void refreshUI(PlatformChooseBean data) {
        mTvFollowMore.setOnClickListener(mOnClickListener);
        List<PlatformChooseBean.Follow> follows = data.getFollow();
        followList.clear();
        for (int i = 0; i < follows.size(); i++) {
            PlatformChooseAdapterBean bean = new PlatformChooseAdapterBean();
            bean.setPid(follows.get(i).getPid());
            bean.setP_name(follows.get(i).getP_name());
            bean.setDelete(false);
            followList.add(bean);
        }
        mTvFollowMore.setVisibility(followList.size() > 6 ? View.VISIBLE : View.GONE);
        if (followList.size() > 6) {
            followList = followList.subList(followList.size() - 6, followList.size());
        }
        mFollowAdapter.updataListView(UIUtil.getContext(), followList);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(MyFocusActivity.class);//进入我的关注页面
        }
    };
}
