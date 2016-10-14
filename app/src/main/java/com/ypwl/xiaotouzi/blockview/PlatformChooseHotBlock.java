package com.ypwl.xiaotouzi.blockview;

import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.base.BaseBlock;
import com.ypwl.xiaotouzi.bean.PlatformChooseBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.event.PlatformChooseEvent;
import com.ypwl.xiaotouzi.event.PlatformCompareRefreshEvent;
import com.ypwl.xiaotouzi.manager.EventHelper;
import com.ypwl.xiaotouzi.manager.PlatformCompareManager;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.view.HotPlatformLayout;

import java.util.List;

/**
 * function : 平台选择热门推荐模块
 * <p/>
 * Created by tengtao on 2016/5/6.
 */
public class PlatformChooseHotBlock extends BaseBlock<PlatformChooseBean> {

    private HotPlatformLayout mPlatformLayout;
    @Override
    protected View initView() {
        View view = View.inflate(UIUtil.getContext(), R.layout.block_layout_platform_choose_hot,null);
        mPlatformLayout = (HotPlatformLayout) view.findViewById(R.id.ll_hot_container);
        return view;
    }

    @Override
    protected void refreshUI(PlatformChooseBean data) {
        final List<PlatformChooseBean.Hot> hots = data.getHot();
        mPlatformLayout.removeAllViews();
        mPlatformLayout.setSpace(UIUtil.dip2px(20),UIUtil.dip2px(6));
        for (int x = 0; x < hots.size(); x++) {
            TextView view = new TextView(UIUtil.getContext());
            view.setText(hots.get(x).getP_name());
            view.setTextSize(13);
            view.setGravity(Gravity.CENTER);
            view.setTextColor(UIUtil.getContext().getResources().getColorStateList(R.color.selector_platform_choose_hot_text_color));
            view.setBackgroundResource(R.drawable.hot_platform_choose_btn_selector);
            view.setPadding(UIUtil.dip2px(18), UIUtil.dip2px(2), UIUtil.dip2px(18), UIUtil.dip2px(2));
            mPlatformLayout.addView(view);
            final int finalX = x;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    skip2From(hots.get(finalX).getPid(), hots.get(finalX).getP_name());
                }
            });
        }
    }

    /** 根据来源回跳,并保存数据*/
    private void skip2From(String pid, String p_name){
        if(Const.PLATFORM_CHOOSE_REQUEST_FROM == 1){//对比平台
            if("".equals(pid)){return;}
            if (PlatformCompareManager.getList().contains(pid)) {
                UIUtil.showToastShort(p_name + "已存在");
            } else {
                if (PlatformCompareManager.getList().size() > 5) {
                    UIUtil.showToastShort("最多选择5个对比平台");
                    return;
                }
                PlatformCompareManager.getList().add(pid);
                EventHelper.post(new PlatformCompareRefreshEvent(true, 1));//更新全部数据
                EventHelper.post(new PlatformChooseEvent(p_name,pid,false,false));
            }
        }else if(Const.PLATFORM_CHOOSE_REQUEST_FROM == 0){//记账
            EventHelper.post(new PlatformChooseEvent(p_name,pid,false,true));
        }
    }
}
