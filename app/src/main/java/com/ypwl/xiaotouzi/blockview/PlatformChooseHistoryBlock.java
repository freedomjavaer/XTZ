package com.ypwl.xiaotouzi.blockview;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.adapter.ItemPlatformChooseHistoryAdapter;
import com.ypwl.xiaotouzi.base.BaseBlock;
import com.ypwl.xiaotouzi.bean.PlatformChooseAdapterBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.event.PlatformChooseClearHistoryEvent;
import com.ypwl.xiaotouzi.manager.EventHelper;
import com.ypwl.xiaotouzi.manager.db.ContrastHistoryDbOpenHelper;
import com.ypwl.xiaotouzi.manager.db.JiZhangHistoryDbOpenHelper;
import com.ypwl.xiaotouzi.utils.UIUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * function : 平台选择页面，历史记录模块
 * <p/>
 * Created by tengtao on 2016/5/5.
 */
public class PlatformChooseHistoryBlock extends BaseBlock<List<PlatformChooseAdapterBean>> {

    private TextView mTvHistoryClear;//清理历史记录
    private ListView mListView;//历史记录列表
    private ItemPlatformChooseHistoryAdapter mHistoryAdapter;
    private List<PlatformChooseAdapterBean> mHistoryDbList = new ArrayList<>();
    private ContrastHistoryDbOpenHelper.HistoryDbHelper mContrastHistoryDbHelper;//对比历史数据
    private JiZhangHistoryDbOpenHelper.HistoryDbHelper mJiZhangHistoryDbHelper;//记账历史数据

    @Override
    protected View initView() {
        View view = View.inflate(UIUtil.getContext(), R.layout.block_layout_platform_choose_history,null);
        mTvHistoryClear = (TextView) view.findViewById(R.id.tv_history_clear);
        mListView = (ListView) view.findViewById(R.id.history_container);
        mHistoryAdapter = new ItemPlatformChooseHistoryAdapter();
        mListView.setAdapter(mHistoryAdapter);

        return view;
    }

    @Override
    protected void refreshUI(List<PlatformChooseAdapterBean> data) {
        mTvHistoryClear.setOnClickListener(mOnClickListener);
        mHistoryAdapter.updataListView(UIUtil.getContext(),data);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            clearDb();//清空历史记录
            EventHelper.post(new PlatformChooseClearHistoryEvent());
        }
    };

    /**
     * 从数据库中获取历史记录，并设置数据
     * @return 返回是否有历史记录
     */
    public boolean setDataFromDb(){
        mHistoryDbList.clear();
        if(mContrastHistoryDbHelper==null)
            mContrastHistoryDbHelper = ContrastHistoryDbOpenHelper.HistoryDbHelper.getInstance(UIUtil.getContext());
        if(mJiZhangHistoryDbHelper==null)
            mJiZhangHistoryDbHelper = JiZhangHistoryDbOpenHelper.HistoryDbHelper.getInstance(UIUtil.getContext());
        List<PlatformChooseAdapterBean> historyDatas;
        if(Const.PLATFORM_CHOOSE_REQUEST_FROM == 1){
            historyDatas = mContrastHistoryDbHelper.queryAll();
        }else{
            historyDatas = mJiZhangHistoryDbHelper.queryAll();
        }
        mHistoryDbList.addAll(historyDatas.size()>6?historyDatas.subList(0,6):historyDatas);
        setData(mHistoryDbList);
        return mHistoryDbList!=null && mHistoryDbList.size()>0;
    }

    /** 清理数据库 */
    public void clearDb(){
        if(Const.PLATFORM_CHOOSE_REQUEST_FROM == 1){
            mContrastHistoryDbHelper.clearTable(ContrastHistoryDbOpenHelper.TABLE_HISTORY_INFO);
        }else{
            mJiZhangHistoryDbHelper.clearTable(JiZhangHistoryDbOpenHelper.TABLE_HISTORY_INFO);
        }
        mHistoryDbList.clear();
    }

    /**
     * 保存平台数据
     * @param pid 平台id
     * @param p_name 平台名称
     */
    public void savePlatform(String pid, String p_name){
        if(Const.PLATFORM_CHOOSE_REQUEST_FROM == 1) {//对比
            saveCompareHistoryData(pid,p_name);
        }else{
            saveTallyHistoryData(pid,p_name);
        }
    }

    /**存入平台对比选择的历史记录*/
    private void saveCompareHistoryData(String pid, String p_name) {
        if(mContrastHistoryDbHelper.queryByPid(pid)){//包含就删除
            mContrastHistoryDbHelper.deleteByPid(pid);
        }
        if(mHistoryDbList.size() >= 6){//总量保持6条
            for(int i=5;i < mHistoryDbList.size();i++)
            mContrastHistoryDbHelper.deleteByPid(mHistoryDbList.get(5).getPid());//删除时间最远的一条
        }
        mContrastHistoryDbHelper.insert(pid, p_name, System.currentTimeMillis() + "");
    }

    /** 保存记账搜索历史记录 */
    private void saveTallyHistoryData(String pid, String p_name){
        if(pid.equals("0")){return;}//自定义平台在保存时存入数据库
        if(mJiZhangHistoryDbHelper.queryByPid(pid)){//包含就删除
            mJiZhangHistoryDbHelper.deleteByPid(pid);
        }
        if(mHistoryDbList.size() >= 6){//总量保持6条
            for(int i=5;i < mHistoryDbList.size();i++)
                mJiZhangHistoryDbHelper.deleteByPid(mHistoryDbList.get(i).getPid());//删除时间最远的一条
        }
        mJiZhangHistoryDbHelper.insert(pid, p_name, System.currentTimeMillis() + "");
    }
}
