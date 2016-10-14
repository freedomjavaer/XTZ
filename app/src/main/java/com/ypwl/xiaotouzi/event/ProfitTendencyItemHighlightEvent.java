package com.ypwl.xiaotouzi.event;

/**
 * function : 收益趋势item高亮事件
 * <p/>
 * Created by PDK on 2016/2/19.
 */
public class ProfitTendencyItemHighlightEvent {

    public int position;
    public int highlight;//是否高亮

    public ProfitTendencyItemHighlightEvent(int position, int highlight){
        this.position = position;
        this.highlight = highlight;
    }

}
