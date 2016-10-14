package com.ypwl.xiaotouzi.manager;

import android.app.Activity;

import com.ypwl.xiaotouzi.utils.PopuViewUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * function : 更改回款状态帮助类
 * <p>
 * Created by tengtao on 2016/5/13.
 */
public class InvestStatusChangeHelper {
    private String status = "1";
    private IStatusSelectedListener mIChangeStatusListener;
    private static InvestStatusChangeHelper instance;
    private Activity mActivity;
    private String[] choiceItems0 = new String[]{"已回","逾期"};
    private String[] choiceItems1 = new String[]{"未回","逾期"};
    private String[] choiceItems2 = new String[]{"已回","未逾期"};

    public static InvestStatusChangeHelper getInstance() {
        if (null == instance) {
            synchronized (InvestStatusChangeHelper.class) {
                if (null == instance) {
                    instance = new InvestStatusChangeHelper();
                }
            }
        }
        return instance;
    }

    /**
     * 显示更改状态弹窗
     */
    public PopuViewUtil show(Activity activity, String status,IStatusSelectedListener listener){
        this.mActivity = activity;
        this.mIChangeStatusListener = listener;
        PopuViewUtil dialog;
        switch (status){
            case "0":
                dialog = addChoiceItem(choiceItems0);
                break;
            case "1":
                dialog = addChoiceItem(choiceItems1);
                break;
            case "2":
                dialog = addChoiceItem(choiceItems2);
                break;
            default:
                dialog = addChoiceItem(choiceItems0);
                break;
        }
        return dialog;
    }

    /** 创建弹窗 */
    private PopuViewUtil addChoiceItem(String[] items){
        List list = new ArrayList();
        for(String item:items){
            list.add(item);
        }
        PopuViewUtil dialog = new PopuViewUtil(mActivity, list, new PopuViewUtil.OnClickCountsListener() {
            @Override
            public void onClick(int position, String str) {
                switch (str) {
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
            }
        });
        dialog.setTitleSize(15);
        dialog.setTitlePadding(UIUtil.dip2px(14));
        dialog.setTitleContent("改变状态");
        return dialog;
    }

    public interface IStatusSelectedListener {
        void onStatusSelected(String status);
    }
}
