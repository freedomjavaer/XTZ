package com.ypwl.xiaotouzi.event;

/**
 * function : 投资分布--选择事件
 * <p/>
 * Created by tengtao on 2016/4/12.
 */
public class InvestAnalysisFenBuSelectedEvent {
    private int position;
    public InvestAnalysisFenBuSelectedEvent(int position){
        this.position = position;
    }

    public int getPosition(){
        return position;
    }
}
