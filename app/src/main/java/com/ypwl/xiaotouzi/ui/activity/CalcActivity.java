package com.ypwl.xiaotouzi.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.adapter.CalResultPagerAdapter;
import com.ypwl.xiaotouzi.base.BaseActivity;
import com.ypwl.xiaotouzi.base.BasePage;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.ui.page.CalculatorHalfListPage;
import com.ypwl.xiaotouzi.ui.page.CalculatorTotalResultPage;
import com.ypwl.xiaotouzi.utils.CacheUtils;
import com.ypwl.xiaotouzi.manager.CalculatorManager;
import com.ypwl.xiaotouzi.utils.UIUtil;
import com.ypwl.xiaotouzi.view.CustonCalculatorKeyBoardView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * function : 计算器功能界面.
 * <p/>
 * Modify by tengtao on 2016/1/14.
 */
public class CalcActivity extends BaseActivity implements View.OnClickListener, CustonCalculatorKeyBoardView.OnCustomKeyBoardClickListener {
    //投标相关参数输入控件
    public EditText calInvMoney,calInvCost,calInvCycle,calInvExtra,calInvRate,curEt;//当前编辑控件和焦点位置
    private int f_index;
    public final int MAX_SAMLL = 3;//部分清单显示的条数
    //清空
    private TextView mTvClear,mTvBack;
    //view切换
    public RelativeLayout calFullList,mRlContent1;
    public Button calFullListBtn;

    private boolean isFirstRes;//是否为第一个页面
    //还款方式数组
    private String[] allReturnWayName = new String[]{
            UIUtil.getString(R.string.cal_return_way_dengebenxi),
            UIUtil.getString(R.string.cal_return_way_dengebenjin),
            UIUtil.getString(R.string.cal_return_way_daoqihuanbenxi),
            UIUtil.getString(R.string.cal_return_way_daoqihuanbenjin)};
    private String[] dayReturnWayName = new String[]{
            UIUtil.getString(R.string.cal_return_way_daoqihuanbenxi)};

    private ArrayList<String[]> calDataList = new ArrayList();

    public final int DENG_E_BEN_XI = 0;
    public final int DENG_E_BEN_JIN = 1;
    public final int DAO_QI_HUAN_BEN_XI = 2;
    public final int DAO_QI_HUAN_BEN = 3;
    public int curReturnWay = DENG_E_BEN_XI;

    private Spinner spinner;
    public ArrayAdapter<String> adapter;//spinner的选项adapter
    //利率和期限切换控件
    public Button btnYearRate,btnDayRate,btnMonth,btnDay;

    public Boolean isYearRate = true;
    public Boolean isMonth = true;

    private ListView calDataLv;
    public SimpleAdapter calLvAdapter;

    public ImageView navImg;
    private ViewPager mCalResultViewpager;
    private ArrayList<BasePage> pagerList;
    private CalculatorTotalResultPage mTotalResultPager;
    private CalculatorHalfListPage mHalfListPager;

    private int SPANNABLE_TYPE_EXTRA = 1;//奖励
    private int SPANNABLE_TYPE_COST = 2;//管理费

    public ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
    private CustonCalculatorKeyBoardView mCustomKeyBoard;
    private Map<String, Object> resultMap = new HashMap<>();//计算结果

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        isFirstRes = true;
        pagerList = new ArrayList<BasePage>();
        initView();
        initListener();
        initData();
    }

    private void initView() {
        //title部分
        findViewById(R.id.layout_title_back).setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.tv_title);
        mTvBack = (TextView) findViewById(R.id.tv_title_back);
        mTvBack.setText("晓投资");
        title.setText("计算器");
        mTvClear = (TextView) findViewById(R.id.tv_title_txt_right);
        mTvClear.setVisibility(View.VISIBLE);
        mTvClear.setOnClickListener(this);
        mTvClear.setText("清空");

        //计算参数控件
        calInvMoney = (EditText) findViewById(R.id.calInvMoney);
        calInvCost = (EditText) findViewById(R.id.calInvCost);
        calInvCycle = (EditText) findViewById(R.id.calInvCycle);
        calInvExtra = (EditText) findViewById(R.id.calInvExtra);
        calInvRate = (EditText) findViewById(R.id.calInvRate);
        curEt = calInvMoney;

        //计算结果页面
        mTotalResultPager = new CalculatorTotalResultPage(mActivity);
        mHalfListPager = new CalculatorHalfListPage(mActivity);
        mHalfListPager.setOnCalculatorHalfListListener(new CalculatorHalfListPage.OnCalculatorHalfListListener() {
            @Override
            public void showCalResultFullList() {
                calFullList.setVisibility(View.VISIBLE);
                mRlContent1.setVisibility(View.INVISIBLE);
                mTvClear.setVisibility(View.GONE);
                renderDataList(calDataList);
            }
        });
        pagerList.add(mTotalResultPager);
        pagerList.add(mHalfListPager);
        mCalResultViewpager = (ViewPager) findViewById(R.id.calculator_result_viewpager);
        mCalResultViewpager.setAdapter(new CalResultPagerAdapter(pagerList));
        pagerList.get(0).initData();

        spinner = (Spinner) findViewById(R.id.calSpinner);
        //全部清单和部分清单切换
        calFullList = (RelativeLayout) findViewById(R.id.calFullList);
        mRlContent1 = (RelativeLayout) findViewById(R.id.rl_cal_content1);

        calFullListBtn = (Button) findViewById(R.id.calFullListBtn);

        //利率和期限
        btnYearRate = (Button) findViewById(R.id.cal_btn_year_rate);
        btnDayRate = (Button) findViewById(R.id.cal_btn_month_rate);
        btnMonth = (Button) findViewById(R.id.cal_btn_month);
        btnDay = (Button) findViewById(R.id.cal_btn_day);


        // 设置全部数据列表
        calDataLv = (ListView) findViewById(R.id.calDataLv);//得到ListView对象的引用 /*为ListView设置Adapter来绑定数据*/
        calLvAdapter = new SimpleAdapter(this, getData(), R.layout.cal_data_item,
                new String[]{"qishu", "yingshoubenjin", "yingshoushouyi", "daishoubenjin", "shishouzonge"},
                new int[]{R.id.calItemQishu, R.id.calItemYingshoubenjin, R.id.calItemYingshoushouyi, R.id.calItemDaishoubenjin, R.id.calItemShishouzonge});
        calDataLv.setAdapter(calLvAdapter);

        //指示点
        navImg = (ImageView) findViewById(R.id.navImg);
        //自定义键盘
        mCustomKeyBoard = (CustonCalculatorKeyBoardView) findViewById(R.id.custom_key_board);
        mCustomKeyBoard.setOnCustomKeyBoardClickListener(this);
    }

    private List<Map<String, String>> getData() {
        return list;
    }

    private void initListener() {
        // 系统自带键盘设定
        View.OnTouchListener touchFunc = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                curEt = (EditText) v;
                changeFocus(curEt);
                int inType = curEt.getInputType(); // backup the input type
                curEt.setInputType(InputType.TYPE_NULL); // disable soft input,不显示软键盘
                curEt.onTouchEvent(event); // call native handler
                curEt.setInputType(inType); // restore input type
                curEt.setSelection(curEt.getText().length());
                return true;
            }
        };

        calFullListBtn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                calFullList.setVisibility(View.INVISIBLE);
                mRlContent1.setVisibility(View.VISIBLE);
                mTvClear.setVisibility(View.VISIBLE);
                curEt.requestFocus();
                return false;
            }
        });

        mCalResultViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                isFirstRes = position == 0 ? true : false;
                navImg.setImageResource(position == 0 ? R.mipmap.pic_058 : R.mipmap.pic_059);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        //添加事件Spinner事件监听
        spinner.setOnItemSelectedListener(new SpinnerSelectedListener());
        spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        selectedExtra();//奖励获取焦点
                        break;
                }
                return false;
            }
        });
        calInvMoney.setOnTouchListener(touchFunc);
        calInvCost.setOnTouchListener(touchFunc);
        calInvCycle.setOnTouchListener(touchFunc);
        calInvExtra.setOnTouchListener(touchFunc);
        calInvRate.setOnTouchListener(touchFunc);
        calInvMoney.setOnFocusChangeListener(new FocusChanged());
        calInvCost.setOnFocusChangeListener(new FocusChanged());
        calInvCycle.setOnFocusChangeListener(new FocusChanged());
        calInvExtra.setOnFocusChangeListener(new FocusChanged());
        calInvRate.setOnFocusChangeListener(new FocusChanged());

        //利率和期限
        btnYearRate.setOnClickListener(this);
        btnDayRate.setOnClickListener(this);
        btnMonth.setOnClickListener(this);
        btnDay.setOnClickListener(this);
    }

    /** 初始化数据 */
    private void initData() {
        curReturnWay = CacheUtils.getInt("curReturnWay", DENG_E_BEN_XI);
        isYearRate = CacheUtils.getBoolean("isYearRate", true);
        isMonth = CacheUtils.getBoolean("isMonth", true);
        isFirstRes = CacheUtils.getBoolean("isFirstRes", true);

        calInvMoney.setText(CacheUtils.getString("calInvMoney", ""));
        calInvCost.setText(CacheUtils.getString("calInvCost", ""));
        calInvCycle.setText(CacheUtils.getString("calInvCycle", ""));
        calInvExtra.setText(CacheUtils.getString("calInvExtra", ""));
        calInvRate.setText(CacheUtils.getString("calInvRate", ""));
        calInvMoney.setSelection(curEt.getText().length());

        _refreshRateBtn();//初始化利率ui
        _refreshCycleBtn();//初始化期限ui
        refreshData();//更新数据

        navImg.setBackgroundResource(isFirstRes ? R.mipmap.pic_058 : R.mipmap.pic_059);
        mCalResultViewpager.setCurrentItem(isFirstRes ? 0 : 1);
    }

    /**设定焦点位置*/
    private void changeFocus(EditText et) {
        switch (et.getId()) {
            case R.id.calInvMoney:
                f_index = 0;
                break;
            case R.id.calInvRate:
                f_index = 1;
                break;
            case R.id.calInvCycle:
                f_index = 2;
                break;
            case R.id.calInvExtra:
                f_index = 4;
                break;
            case R.id.calInvCost:
                f_index = 5;
                break;
        }
    }

    /** 改变还款方式 */
    private class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            curReturnWay = isMonth ? arg2 : 2;
            refreshData();
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    /** 刷新数据 */
    public void refreshData() {
        //投资额，利率，期限缺一则数据重置
        if (calInvMoney.length() <= 0 || calInvRate.length() <= 0 || calInvCycle.length() <= 0) {
            _cleanViewData();
            return;
        }
        //投资额
        double principal = Double.parseDouble(calInvMoney.getText().toString().trim());
        //利率
        double invRate = Double.parseDouble(calInvRate.getText().toString().trim()) / 100;
        double monthRate = isYearRate ? invRate / 12 : invRate * 30;//折换成月利率
        //投资期限
        int moneyCycle = Integer.parseInt(calInvCycle.getText().toString());
        int totalMonth = isMonth ? moneyCycle : (int) Math.ceil(moneyCycle / 30.0);//折换成月

        String extra = calInvExtra.getText().toString().trim();
        String cost = calInvCost.getText().toString().trim();
        double extraRate = extra.length()>0?Double.parseDouble(extra) / 100 : 0;
        double costRate = cost.length()>0?Double.parseDouble(cost) / 100 : 0;

        //根据还款方式计算结果
        resultMap.clear();
        switch (curReturnWay) {
            case DENG_E_BEN_XI:
                //月均
                double yuejun = CalculatorManager.getInstance().calDengebenxiYJHK(principal, monthRate, totalMonth);

                if((yuejun+"").equals("NaN")){
                    mTotalResultPager.setTotalGetMoney("超出范围");
                    mTotalResultPager.setRealRate("0.00");
                    return;
                }
                resultMap = CalculatorManager.getInstance().calDengEBenXi(principal, monthRate, totalMonth, extraRate, costRate);
                break;
            case DENG_E_BEN_JIN:
                resultMap = CalculatorManager.getInstance().calDengebenjin(principal, monthRate, totalMonth, extraRate, costRate);
                break;
            case DAO_QI_HUAN_BEN_XI:
                resultMap = CalculatorManager.getInstance().calDaoqihuanbenxi(isMonth,principal,monthRate,moneyCycle,extraRate,costRate);
                break;
            case DAO_QI_HUAN_BEN:
                resultMap = CalculatorManager.getInstance().calDaoqihuanbenjin(principal,monthRate,totalMonth,extraRate,costRate);
                break;
        }
        showResult();
    }

    private void showResult() {
        if(resultMap.size()<=0){return;}
        // 总利息管理费
        mTotalResultPager.setCostOrExtraMoney((double)resultMap.get(Const.CAL_LXGL), SPANNABLE_TYPE_COST);
        // 总额外奖励
        mTotalResultPager.setCostOrExtraMoney((double) resultMap.get(Const.CAL_TBJL), SPANNABLE_TYPE_EXTRA);
        // 总收益
        mTotalResultPager.setTotalGetMoney(String.valueOf(resultMap.get(Const.CAL_SJSY)));
        //实际利率
        mTotalResultPager.setRealRate(String.valueOf(resultMap.get(Const.CAL_SJLL)));
        //更新数据单
        renderDataList((ArrayList<String[]>) resultMap.get(Const.CAL_JGQD));
    }

    /** 更新数据单 */
    private void renderDataList(ArrayList<String[]> calResultList) {
        calDataList.clear();
        calDataList.addAll(calResultList);
        int len = calDataList.size();
        if (len <= 0) {
            return;
        }
        list.clear();
        for (int i = 0; i < len; ++i) {
            String[] tmp = calDataList.get(i);

            Map<String, String> map;
            map = new HashMap<String, String>();
            map.put("qishu", tmp[0]);
            map.put("yingshoubenjin", tmp[1]);
            map.put("yingshoushouyi", tmp[2]);
            map.put("daishoubenjin", tmp[3]);
            map.put("shishouzonge", tmp[4]);
            list.add(map);
        }

        calDataLv.invalidate();
        calLvAdapter.notifyDataSetInvalidated();
        mHalfListPager.loadHalfList(list.subList(0, (len > MAX_SAMLL ? MAX_SAMLL : len)));
    }

    /** 小数点结尾自动加0 */
    private class FocusChanged implements View.OnFocusChangeListener{
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(v instanceof EditText && !hasFocus){
                String s = ((EditText) v).getText().toString();
                if(s.length()>0 && s.substring(s.length()-1,s.length()).equals(".")){
                    ((EditText) v).setText(s+0);
                }
            }
        }
    }

    /** 投标期限ui和选项 */
    private void _refreshCycleBtn() {
        if (isMonth) {
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, allReturnWayName);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setSelection(curReturnWay, false);
        } else {
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dayReturnWayName);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            curReturnWay = DAO_QI_HUAN_BEN_XI;
            spinner.setSelection(0, false);
        }
        //月
        btnMonth.setBackgroundResource(isMonth?R.drawable.cal_share_account_checked_left:
                R.drawable.cal_share_account_unchedked_left);
        btnMonth.setTextColor(isMonth?Color.WHITE:0xff3487ea);
        //日
        btnDay.setBackgroundResource(isMonth ? R.drawable.cal_share_account_unchecked_right :
                R.drawable.cal_share_account_checked_right);
        btnDay.setTextColor(isMonth?0xff3487ea:Color.WHITE);
    }

    /** 更新利率ui */
    private void _refreshRateBtn() {
        btnYearRate.setBackgroundResource(isYearRate ? R.drawable.cal_share_account_checked_left :
                R.drawable.cal_share_account_unchedked_left);
        btnYearRate.setTextColor(isYearRate ? Color.WHITE : 0xff3487ea);//  ff3db5e6
        btnDayRate.setBackgroundResource(isYearRate ? R.drawable.cal_share_account_unchecked_right :
                R.drawable.cal_share_account_checked_right);
        btnDayRate.setTextColor(isYearRate?0xff3487ea:Color.WHITE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_title_back://返回
                f_index=0;
                curEt = calInvMoney;
                calInvMoney.requestFocus();
                finish();
                break;
            case R.id.cal_btn_year_rate:
                isYearRate = true;
                _refreshRateBtn();
                refreshData();
                break;
            case R.id.cal_btn_month_rate:
                isYearRate = false;
                _refreshRateBtn();
                refreshData();
                break;
            case R.id.cal_btn_month://月
                if(!isMonth){curReturnWay = DENG_E_BEN_XI;}//天-->月，默认等额本息
                isMonth = true;
                _refreshCycleBtn();
                refreshData();
                break;
            case R.id.cal_btn_day://天
                isMonth = false;
                _refreshCycleBtn();
                refreshData();
                break;
            case R.id.tv_title_txt_right://清空
                _handleCleanAllEt();
                calInvMoney.requestFocus();
                f_index = 0;
                curEt = calInvMoney;
                break;
            default:
                break;
        }
    }

    /** 自定义键盘的点击回调监听 */
    @Override
    public void onCustomKeyBoardClick(String key) {
        if("del".equals(key)){//删除
            _handleDeleteEt();
        }else if("next".equals(key)){
            nextTerm();
        }else{
            _handleCalKeyBtn(key);
        }
    }

    /** 下一项 -->下一个编辑框获取焦点 */
    private void nextTerm() {
        //依次顺序calInvMoney,calInvRate,calInvCycle,spinner,calInvExtra,calInvCost
        f_index++;
        EditText[] etData = new EditText[]{calInvMoney, calInvRate, calInvCycle, calInvCycle, calInvExtra, calInvCost};
        if (f_index % 6 != 3) {
            etData[f_index % 6].setFocusable(true);
            etData[f_index % 6].setFocusableInTouchMode(true);
            etData[f_index % 6].requestFocus();
            etData[f_index % 6].findFocus();
            etData[f_index % 6].setSelection(etData[f_index % 6].getText().length());
            curEt = etData[f_index % 6];
        } else {
            spinner.performClick();
            selectedExtra();//奖励获取焦点
        }
    }

    /** 奖励获取焦点 */
    private void selectedExtra(){
        f_index = 4;
        curEt = calInvExtra;
        calInvExtra.requestFocus();
        calInvExtra.setEnabled(true);
        calInvExtra.setSelection(calInvExtra.getText().length());
    }

    /** 删除输入 */
    public void _handleDeleteEt() {
        String str = curEt.getText().toString();
        if (str.length() <= 0) {
            return;
        }
        curEt.setText(str.toCharArray(), 0, str.length() - 1);
        curEt.setSelection(curEt.getText().length());
        refreshData();
    }

    /**
     * 自定义键盘点击事件处理
     * @param n 点击的符号
     */
    public void _handleCalKeyBtn(String n) {
        String oldStr = curEt.getText().toString();
        String newStr = oldStr + n;
        //投资金额
        if(curEt == calInvMoney && !newStr.matches("^(([1-9]\\d*)|0)(\\.\\d{0,2})?$")){
            curEt.setText(oldStr);
        } else if(curEt == calInvCycle && !newStr.matches("^[1-9]\\d*$")){//期限
            curEt.setText(oldStr);
        }else if((curEt == calInvCost || curEt == calInvExtra || curEt == calInvRate) && !newStr.matches("^(([1-9]\\d{0,1})|0)(\\.\\d{0,2})?$")){
            curEt.setText(oldStr);
        } else {
            curEt.setText(newStr);
        }
        curEt.setSelection(curEt.getText().length());
        refreshData();
    }

    /** 重置所有输入框 */
    public void _handleCleanAllEt() {
        String emptyStr = "";
        calInvMoney.setText(emptyStr);
        calInvCost.setText(emptyStr);
        calInvCycle.setText(emptyStr);
        calInvExtra.setText(emptyStr);
        calInvRate.setText(emptyStr);

        isYearRate = true;
        _cleanViewData();
        _refreshRateBtn();
        spinner.setSelection(DENG_E_BEN_XI, true);
    }

    /** 重置结果数据  清空 */
    private void _cleanViewData() {
        calDataList.clear();
        //全部清单清空
        list.clear();
        calDataLv.invalidate();
        calLvAdapter.notifyDataSetInvalidated();
        //清空viewpager结果
        mTotalResultPager.initData();
        mHalfListPager.loadHalfList(list);
    }

    @Override
    public void onPause() {
        //存入数据
        CacheUtils.putString("calInvMoney", calInvMoney.getText().toString());
        CacheUtils.putString("calInvCost", calInvCost.getText().toString());
        CacheUtils.putString("calInvCycle", calInvCycle.getText().toString());
        CacheUtils.putString("calInvExtra", calInvExtra.getText().toString());
        CacheUtils.putString("calInvRate", calInvRate.getText().toString());
        CacheUtils.putInt("curReturnWay", curReturnWay);
        CacheUtils.putBoolean("isYearRate", isYearRate);
        CacheUtils.putBoolean("isMonth", isMonth);
        CacheUtils.putBoolean("isFirstRes", isFirstRes);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mCustomKeyBoard = null;
        super.onDestroy();
    }
}