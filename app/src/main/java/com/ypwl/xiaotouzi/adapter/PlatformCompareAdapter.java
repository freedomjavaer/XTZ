package com.ypwl.xiaotouzi.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ypwl.xiaotouzi.R;
import com.ypwl.xiaotouzi.bean.PlatformCompareBean;
import com.ypwl.xiaotouzi.common.Const;
import com.ypwl.xiaotouzi.manager.PlatformCompareManager;
import com.ypwl.xiaotouzi.ui.activity.PlatformChooseActivity;
import com.ypwl.xiaotouzi.ui.activity.PlatformCompareActivity;
import com.ypwl.xiaotouzi.ui.activity.UserFeedbackActivity;
import com.ypwl.xiaotouzi.utils.ImgLoadUtil;
import com.ypwl.xiaotouzi.utils.UIUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tengtao
 *         平台对比的适配器
 */
public class PlatformCompareAdapter extends BaseAdapter {
    //数据对比项目
    private String[] titles = {"成交量/万","利率/%","投资人数/个","借款人数/个","人均投资/万",
            "人均借款/万","借款标数/个","借贷期限/月"};
    //信息对比项目
    private String[] infos = {"信息对比","评   级","上线时间","所在地","公司类型","注册资本", "债权转让"
            ,"投标保障","资金托管","保障模式","充值费用","提现费用","管理费","融资状况","自动投标"};

    //适配器的item的type
    public final int PLATFORMCOMPARE_ITEM_FIRST = 1;//平台logo
    public final int PLATFORMCOMPARE_ITEM_SECOND = 2;//平台添加
    public final int PLATFORMCOMPARE_ITEM_THIRD = 3;//平台名称
    public final int PLATFORMCOMPARE_ITEM_FOURTH = 4;//周月选择
    public final int PLATFORMCOMPARE_ITEM_FIFTH = 5;//柱状图
    public final int PLATFORMCOMPARE_ITEM_SIXTH = 6;//表头
    public final int PLATFORMCOMPARE_ITEM_SEVENTH = 7;//表内容
    public final int PLATFORMCOMPARE_ITEM_EIGHTH = 8;//信息反馈

    private OnPlatformCompareChangedListener mPlatformCompareChangedListener;
    private List<Boolean> dataVisible = new ArrayList<Boolean>();//条目是否显示
    public static boolean isWeekSelected = true;//是否按周选择对比
    private Context mContext;
    private PlatformCompareBean datas;
    private int mPlatformNumber,ignore;

    public PlatformCompareAdapter(Context context){
        this.mContext = context;
    }

    public void updateList(PlatformCompareBean datas){
        this.datas = datas;
        ignore = (datas.getPid().size() == 5 ? 1 : 0);
        if (datas != null) {
            mPlatformNumber = datas.getPid().size();//对比的平台数
            dataVisible.clear();
            for(int i=0;i<5;i++){
                dataVisible.add(i < datas.getPid().size() ? true : false);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (datas == null) {return 0;}
        return datas.getPid().size() + (datas.getPid().size() == 5 ? 26 : 27);
    }

    @Override
    public Object getItem(int position) {
        return datas != null ? datas : null;
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount() + 8;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mPlatformNumber) {
            return PLATFORMCOMPARE_ITEM_FIRST;
        } else if (position == mPlatformNumber && datas.getPid().size()!=5) {
            return PLATFORMCOMPARE_ITEM_SECOND;//添加
        } else if (position == mPlatformNumber + 1 - ignore) {
            return PLATFORMCOMPARE_ITEM_THIRD;//平台名称
        } else if (position == mPlatformNumber + 2 - ignore) {
            return PLATFORMCOMPARE_ITEM_FOURTH;//周月选择
        } else if (position > mPlatformNumber + 2 - ignore && position < mPlatformNumber + 11 - ignore) {
            return PLATFORMCOMPARE_ITEM_FIFTH;//柱状图
        } else if (position == mPlatformNumber + 11 - ignore) {
            return PLATFORMCOMPARE_ITEM_SIXTH;//表头
        } else if (position > mPlatformNumber + 11 - ignore && position < mPlatformNumber + 26 - ignore) {
            return PLATFORMCOMPARE_ITEM_SEVENTH;//表格内容
        } else {
            return PLATFORMCOMPARE_ITEM_EIGHTH;//信息反馈
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        if (type == PLATFORMCOMPARE_ITEM_FIRST) {
            //平台信息
            LogoNameViewHolder logoNameHolder = null;
            if (convertView == null) {
                logoNameHolder = new LogoNameViewHolder();
                convertView = View.inflate(mContext, R.layout.item_platform_compare_logo_name,
                        null);
                logoNameHolder.mRlItem = (RelativeLayout) convertView.findViewById(R.id.item_platform_compare_logo_name);
                logoNameHolder.mIvStar = (ImageView) convertView.findViewById(R.id.iv_star_follow);
                logoNameHolder.mIvLogo = (ImageView) convertView.findViewById(R.id
                        .iv_platform_logo);
                logoNameHolder.mTvName = (TextView) convertView.findViewById(R.id.tv_platform_name);
                logoNameHolder.mIvDelete = (ImageView) convertView.findViewById(R.id
                        .iv_delete_compare);
                convertView.setTag(logoNameHolder);
            } else {
                logoNameHolder = (LogoNameViewHolder) convertView.getTag();
            }
            initTypeFirst(position, logoNameHolder);
        } else if (type == PLATFORMCOMPARE_ITEM_SECOND) {
            //添加平台
            convertViewHolder addHolder = null;
            if (convertView == null) {
                addHolder = new convertViewHolder();
                convertView = View.inflate(mContext, R.layout.item_platform_compare_platform_add,
                        null);
                addHolder.mTvAddPlatform = (TextView) convertView.findViewById(R.id
                        .tv_add_platform);
                convertView.setTag(addHolder);
            } else {
                addHolder = (convertViewHolder) convertView.getTag();
            }
            initItemSecond(addHolder);
        } else if (type == PLATFORMCOMPARE_ITEM_THIRD) {
            //数据对比头部信息
            PlatformNameViewHolder nameHolder = null;
            if (convertView == null) {
                nameHolder = new PlatformNameViewHolder();
                convertView = View.inflate(mContext, R.layout.item_platform_compare_data_compare,
                        null);
                nameHolder.mTvCompareTitle1 = (TextView) convertView.findViewById(R.id.tv_compare_content1);
                nameHolder.mTvCompareTitle2 = (TextView) convertView.findViewById(R.id.tv_compare_content2);
                nameHolder.mTvCompareTitle3 = (TextView) convertView.findViewById(R.id.tv_compare_content3);
                nameHolder.mTvCompareTitle4 = (TextView) convertView.findViewById(R.id.tv_compare_content4);
                nameHolder.mTvCompareTitle5 = (TextView) convertView.findViewById(R.id.tv_compare_content5);
                convertView.setTag(nameHolder);
            } else {
                nameHolder = (PlatformNameViewHolder) convertView.getTag();
            }
            initItemThird(nameHolder);
        } else if (type == PLATFORMCOMPARE_ITEM_FOURTH) {
            //周和月的选择
            WeekMonthViewHolder weekMonthHolder = null;
            if (convertView == null) {
                weekMonthHolder = new WeekMonthViewHolder();
                convertView = View.inflate(mContext, R.layout.item_platform_compare_week_month, null);
                weekMonthHolder.mTvWeek = (TextView) convertView.findViewById(R.id.view_tv_week);
                weekMonthHolder.mTvMonth = (TextView) convertView.findViewById(R.id.view_tv_month);
                convertView.setTag(weekMonthHolder);
            } else {
                weekMonthHolder = (WeekMonthViewHolder) convertView.getTag();
            }
            weekMonthHolder.mTvWeek.setSelected(isWeekSelected);
            weekMonthHolder.mTvMonth.setSelected(!isWeekSelected);
            weekMonthHolder.mTvWeek.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isWeekSelected) {
                        return;
                    } else {
                        isWeekSelected = true;
                        mPlatformCompareChangedListener.onSelected(true);
                    }
                }
            });
            weekMonthHolder.mTvMonth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isWeekSelected) {
                        isWeekSelected = false;
                        mPlatformCompareChangedListener.onSelected(false);
                    } else {
                        return;
                    }
                }
            });
        } else if (type == PLATFORMCOMPARE_ITEM_FIFTH) {
            //柱状图的显示
            BarChartViewHolder barChartHolder = null;
            if (convertView == null) {
                barChartHolder = new BarChartViewHolder();
                convertView = View.inflate(mContext, R.layout.item_platform_compare_barchart, null);
                barChartHolder.mTvValueName = (TextView) convertView.findViewById(R.id
                        .tv_barchart_value_name);
                barChartHolder.mTvBarChartValue1 = (TextView) convertView.findViewById(R.id
                        .tv_barchart_value1);
                barChartHolder.mTvBarChartValue2 = (TextView) convertView.findViewById(R.id
                        .tv_barchart_value2);
                barChartHolder.mTvBarChartValue3 = (TextView) convertView.findViewById(R.id
                        .tv_barchart_value3);
                barChartHolder.mTvBarChartValue4 = (TextView) convertView.findViewById(R.id
                        .tv_barchart_value4);
                barChartHolder.mTvBarChartValue5 = (TextView) convertView.findViewById(R.id
                        .tv_barchart_value5);
                barChartHolder.mBarChart1 = (ProgressBar) convertView.findViewById(R.id
                        .item_barchart1);
                barChartHolder.mBarChart2 = (ProgressBar) convertView.findViewById(R.id
                        .item_barchart2);
                barChartHolder.mBarChart3 = (ProgressBar) convertView.findViewById(R.id
                        .item_barchart3);
                barChartHolder.mBarChart4 = (ProgressBar) convertView.findViewById(R.id
                        .item_barchart4);
                barChartHolder.mBarChart5 = (ProgressBar) convertView.findViewById(R.id
                        .item_barchart5);
                convertView.setTag(barChartHolder);
            } else {
                barChartHolder = (BarChartViewHolder) convertView.getTag();
            }
            initItemFifth(position, barChartHolder);
        } else if (type == PLATFORMCOMPARE_ITEM_SIXTH) {
            //表格头--信息对比
            TableHeaderViewHolder tableHeaderHolder = null;
            if (convertView == null) {
                tableHeaderHolder = new TableHeaderViewHolder();
                convertView = View.inflate(mContext, R.layout.item_platform_compare_grid_header,
                        null);
                tableHeaderHolder.mTvInfoCompare = (TextView) convertView.findViewById(R.id
                        .tv_info_compare);
                tableHeaderHolder.mTvTitle1 = (TextView) convertView.findViewById(R.id.tv_title_content1);
                tableHeaderHolder.mTvTitle2 = (TextView) convertView.findViewById(R.id.tv_title_content2);
                tableHeaderHolder.mTvTitle3 = (TextView) convertView.findViewById(R.id.tv_title_content3);
                tableHeaderHolder.mTvTitle4 = (TextView) convertView.findViewById(R.id.tv_title_content4);
                tableHeaderHolder.mTvTitle5 = (TextView) convertView.findViewById(R.id.tv_title_content5);
                tableHeaderHolder.mLlTitle1 = (LinearLayout) convertView.findViewById(R.id.ll_title_content1);
                tableHeaderHolder.mLlTitle2 = (LinearLayout) convertView.findViewById(R.id.ll_title_content2);
                tableHeaderHolder.mLlTitle3 = (LinearLayout) convertView.findViewById(R.id.ll_title_content3);
                tableHeaderHolder.mLlTitle4 = (LinearLayout) convertView.findViewById(R.id.ll_title_content4);
                tableHeaderHolder.mLlTitle5 = (LinearLayout) convertView.findViewById(R.id.ll_title_content5);
                convertView.setTag(tableHeaderHolder);
            } else {
                tableHeaderHolder = (TableHeaderViewHolder) convertView.getTag();
            }
            initItemSixth(tableHeaderHolder);
        } else if (type == PLATFORMCOMPARE_ITEM_SEVENTH) {//表格
            TableContentViewHolder tableContentHolder = null;
            if (convertView == null) {
                tableContentHolder = new TableContentViewHolder();
                convertView = View.inflate(mContext, R.layout.item_platform_compare_grid_content,
                        null);
                tableContentHolder.mTvHeader = (TextView) convertView.findViewById(R.id
                        .tv_info_header);
                tableContentHolder.mTvContent1 = (TextView) convertView.findViewById(R.id
                        .tv_info_content1);
                tableContentHolder.mTvContent2 = (TextView) convertView.findViewById(R.id
                        .tv_info_content2);
                tableContentHolder.mTvContent3 = (TextView) convertView.findViewById(R.id
                        .tv_info_content3);
                tableContentHolder.mTvContent4 = (TextView) convertView.findViewById(R.id
                        .tv_info_content4);
                tableContentHolder.mTvContent5 = (TextView) convertView.findViewById(R.id
                        .tv_info_content5);
                tableContentHolder.mLlContent1 = (LinearLayout) convertView.findViewById(R.id.ll_info_content1);
                tableContentHolder.mLlContent2 = (LinearLayout) convertView.findViewById(R.id.ll_info_content2);
                tableContentHolder.mLlContent3 = (LinearLayout) convertView.findViewById(R.id.ll_info_content3);
                tableContentHolder.mLlContent4 = (LinearLayout) convertView.findViewById(R.id.ll_info_content4);
                tableContentHolder.mLlContent5 = (LinearLayout) convertView.findViewById(R.id.ll_info_content5);
                convertView.setTag(tableContentHolder);
            } else {
                tableContentHolder = (TableContentViewHolder) convertView.getTag();
            }
            initItemSeventh(position, tableContentHolder);
        } else if (type == PLATFORMCOMPARE_ITEM_EIGHTH) {
            //信息反馈
            InfoErrorViewHolder errorHolder = null;
            if (convertView == null) {
                errorHolder = new InfoErrorViewHolder();
                convertView = View.inflate(mContext, R.layout
                        .item_platform_compare_info_error, null);
                errorHolder.mTvInfoError = (TextView) convertView.findViewById(R.id.tv_info_error);
                convertView.setTag(errorHolder);
            } else {
                errorHolder = (InfoErrorViewHolder) convertView.getTag();
            }
            initItemEighth(errorHolder);
        }
        return convertView;
    }


    private class LogoNameViewHolder {//平台信息
        RelativeLayout mRlItem;//条目
        ImageView mIvStar,mIvDelete,mIvLogo;
        TextView mTvName;
    }

    private class convertViewHolder {//平台添加
        TextView mTvAddPlatform;
    }

    private class PlatformNameViewHolder {//数据对比
        TextView mTvCompareTitle1,mTvCompareTitle2,mTvCompareTitle3,mTvCompareTitle4,mTvCompareTitle5;
    }

    private class WeekMonthViewHolder {//周/月选择
        TextView mTvWeek,mTvMonth;
    }

    private class BarChartViewHolder {//数据对比
        TextView mTvValueName,mTvBarChartValue1,mTvBarChartValue2,mTvBarChartValue3,mTvBarChartValue4,mTvBarChartValue5;
        ProgressBar mBarChart1,mBarChart2,mBarChart3,mBarChart4,mBarChart5;
    }

    private class TableHeaderViewHolder {//平台信息对比
        TextView mTvInfoCompare,mTvTitle1,mTvTitle2,mTvTitle3,mTvTitle4,mTvTitle5;
        LinearLayout mLlTitle1,mLlTitle2,mLlTitle3,mLlTitle4,mLlTitle5;
    }

    private class TableContentViewHolder {//平台内容
        TextView mTvHeader,mTvContent1,mTvContent2,mTvContent3,mTvContent4,mTvContent5;
        LinearLayout mLlContent1,mLlContent2,mLlContent3,mLlContent4,mLlContent5;
    }

    private class InfoErrorViewHolder {//信息反馈
        TextView mTvInfoError;
    }

    private void initTypeFirst(final int position, final LogoNameViewHolder holder) {
        holder.mRlItem.setBackgroundResource((position%2==0)?R.color.h:R.color.g);
        //设置数据
        final boolean followed = PlatformCompareActivity.followPids.contains(datas.getPid().get(position));
        holder.mIvStar.setImageResource(followed ? R.mipmap.focus_blue_star : R.mipmap.focus_empty_star);
        ImgLoadUtil.loadLogo(datas.getPid().get(position), datas.getP_logo().get(position), holder.mIvLogo, 0);
        holder.mTvName.setText(datas.getP_name().get(position));
        //关注平台
        holder.mIvStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (followed) {
                    mPlatformCompareChangedListener.onAbolishFollow(datas, position);//取消关注这个平台
                } else {
                    mPlatformCompareChangedListener.onFollowed(datas, position);//去关注这个平台
                }
            }
        });
        holder.mIvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlatformCompareManager.getList().remove(datas.getPid().get(position));//删除数据
                mPlatformCompareChangedListener.onDeleted(position);
            }
        });
    }

    private void initItemSecond(convertViewHolder holder) {
        holder.mTvAddPlatform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = PlatformCompareManager.getList().size();
                if (number > 4) {
                    UIUtil.showToastShort("最多只能对比5个平台");
                    return;
                }
                //进入平台选择页面
                Const.PLATFORM_CHOOSE_REQUEST_FROM = 1;//确定平台选择请求来于平台对比
                Intent intent = new Intent(mContext, PlatformChooseActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    private void initItemThird(PlatformNameViewHolder holder) {
        if (mPlatformNumber > 0) {
            holder.mTvCompareTitle1.setText(datas.getP_name().get(0));
            if (mPlatformNumber > 1) {
                holder.mTvCompareTitle2.setText(datas.getP_name().get(1));
                if (mPlatformNumber > 2) {
                    holder.mTvCompareTitle3.setText(datas.getP_name().get(2));
                    if (mPlatformNumber > 3) {
                        holder.mTvCompareTitle4.setText(datas.getP_name().get(3));
                        if (mPlatformNumber > 4) {
                            holder.mTvCompareTitle5.setText(datas.getP_name().get(4));
                        }
                    }
                }
            }
        }
        holder.mTvCompareTitle1.setVisibility(dataVisible.get(0) ? View.VISIBLE : View.GONE);
        holder.mTvCompareTitle2.setVisibility(dataVisible.get(1) ? View.VISIBLE : View.GONE);
        holder.mTvCompareTitle3.setVisibility(dataVisible.get(2) ? View.VISIBLE : View.GONE);
        holder.mTvCompareTitle4.setVisibility(dataVisible.get(3) ? View.VISIBLE : View.GONE);
        holder.mTvCompareTitle5.setVisibility(dataVisible.get(4) ? View.VISIBLE : View.GONE);
    }

    private void initItemFifth(int position, BarChartViewHolder holder) {
        switch (position - mPlatformNumber - (3 - ignore)) {
            case 0://成交量
                initBarChartData(0, holder, datas.getData_cjl());
                break;
            case 1://利率
                initBarChartData(1, holder, datas.getData_ll());
                break;
            case 2://投资人数
                initBarChartData(2, holder, datas.getData_tzrs());
                break;
            case 3://借款人数
                initBarChartData(3, holder, datas.getData_jkrs());
                break;
            case 4://人均投资金额
                initBarChartData(4, holder, datas.getData_rjtzje());
                break;
            case 5://人均借款金额
                initBarChartData(5, holder, datas.getData_rjjkje());
                break;
            case 6://借款标数
                initBarChartData(6, holder, datas.getData_jkbs());
                break;
            case 7://借款期限
                initBarChartData(7, holder, datas.getData_pjjkqx());
                break;
            default:
                break;
        }
    }

    private void initItemSixth(TableHeaderViewHolder holder) {
        //表头数据
        if (mPlatformNumber > 0) {
            holder.mTvTitle1.setText(datas.getP_name().get(0));
            if (mPlatformNumber > 1) {
                holder.mTvTitle2.setText(datas.getP_name().get(1));
                if (mPlatformNumber > 2) {
                    holder.mTvTitle3.setText(datas.getP_name().get(2));
                    if (mPlatformNumber > 3) {
                        holder.mTvTitle4.setText(datas.getP_name().get(3));
                        if (mPlatformNumber > 4) {
                            holder.mTvTitle5.setText(datas.getP_name().get(4));
                        }
                    }
                }
            }
        }
        holder.mLlTitle1.setVisibility(dataVisible.get(0) ? View.VISIBLE : View.GONE);
        holder.mLlTitle2.setVisibility(dataVisible.get(1) ? View.VISIBLE : View.GONE);
        holder.mLlTitle3.setVisibility(dataVisible.get(2) ? View.VISIBLE : View.GONE);
        holder.mLlTitle4.setVisibility(dataVisible.get(3) ? View.VISIBLE : View.GONE);
        holder.mLlTitle5.setVisibility(dataVisible.get(4) ? View.VISIBLE : View.GONE);
    }

    private void initItemSeventh(int position, TableContentViewHolder holder) {
        switch (position - mPlatformNumber - (11-ignore)) {
            case 1://评级
                initInfoData(1, holder, datas.getLevel());
                break;
            case 2://上线时间
                initInfoData(2, holder, datas.getLaunch_time());
                break;
            case 3://所在地
                initInfoData(3, holder, datas.getCity());
                break;
            case 4://公司类型
                initInfoData(4, holder, datas.getC_type());
                break;
            case 5://注册资本
                initInfoData(5, holder, datas.getC_capital());
                break;
            case 6://债权转让
                initInfoData(6, holder, datas.getAoc());
                break;
            case 7://投标保障
                initInfoData(7, holder, datas.getGoi());
                break;
            case 8://资金托管
                initInfoData(8, holder, datas.getTrust_funds());
                break;
            case 9://保障模式
                initInfoData(9, holder, datas.getG_mode());
                break;
            case 10://充值费用
                initInfoData(10, holder, datas.getRecharge_rule());
                break;
            case 11://提现费用
                initInfoData(11, holder, datas.getWithdraw_rule());
                break;
            case 12://管理费
                initInfoData(12, holder, datas.getM_cost());
                break;
            case 13://融资状况
                initInfoData(13, holder, datas.getF_situation());
                break;
            case 14://自动投标
                initInfoData(14, holder, datas.getAuto_invest());
                break;
            default:
                break;
        }
    }

    private void initItemEighth(InfoErrorViewHolder holder) {
        //信息反馈
        holder.mTvInfoError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent feedBack = new Intent(mContext, UserFeedbackActivity.class);
                feedBack.putExtra(Const.KEY_INTENT_JUMP_FROM_DATA,"平台对比");
                mContext.startActivity(feedBack);
            }
        });
    }

    private void initInfoData(int index, TableContentViewHolder holder, List<String> data) {
        //表格数据
        holder.mTvHeader.setText(infos[index]);
        holder.mTvHeader.setTextSize(12);
        holder.mTvHeader.setBackgroundResource(index % 2 == 0 ? R.color.table_bg_01 : R.color.h);
        if (mPlatformNumber > 0) {
            holder.mTvContent1.setText("".equals(data.get(0))?"- -":data.get(0));
            if (mPlatformNumber > 1) {
                holder.mTvContent2.setText("".equals(data.get(1))?"- -":data.get(1));
                if (mPlatformNumber > 2) {
                    holder.mTvContent3.setText("".equals(data.get(2))?"- -":data.get(2));
                    if (mPlatformNumber > 3) {
                        holder.mTvContent4.setText("".equals(data.get(3))?"- -":data.get(3));
                        if (mPlatformNumber > 4) {
                            holder.mTvContent5.setText("".equals(data.get(4))?"- -":data.get(4));
                        }
                    }
                }
            }
        }
        holder.mTvContent1.setPadding(2, 4, 2, 4);
        holder.mTvContent2.setPadding(2, 4, 2, 4);
        holder.mTvContent3.setPadding(2, 4, 2, 4);
        holder.mTvContent4.setPadding(2, 4, 2, 4);
        holder.mTvContent5.setPadding(2, 4, 2, 4);
        holder.mTvContent1.setBackgroundResource(index % 2 == 0 ? R.color.table_bg_01 : R.color.h);
        holder.mTvContent2.setBackgroundResource(index % 2 == 0 ? R.color.table_bg_01 : R.color.h);
        holder.mTvContent3.setBackgroundResource(index % 2 == 0 ? R.color.table_bg_01 : R.color.h);
        holder.mTvContent4.setBackgroundResource(index % 2 == 0 ? R.color.table_bg_01 : R.color.h);
        holder.mTvContent5.setBackgroundResource(index % 2 == 0 ? R.color.table_bg_01 : R.color.h);
        holder.mLlContent1.setVisibility(dataVisible.get(0) ? View.VISIBLE : View.GONE);
        holder.mLlContent2.setVisibility(dataVisible.get(1) ? View.VISIBLE : View.GONE);
        holder.mLlContent3.setVisibility(dataVisible.get(2) ? View.VISIBLE : View.GONE);
        holder.mLlContent4.setVisibility(dataVisible.get(3) ? View.VISIBLE : View.GONE);
        holder.mLlContent5.setVisibility(dataVisible.get(4) ? View.VISIBLE : View.GONE);
    }

    private void initBarChartData(int index, BarChartViewHolder holder, List<String> data) {
        //获取最大值
        float target = 0.0f;
        for (int i = 0; i < mPlatformNumber; i++) {
            float value = Float.parseFloat(data.get(i));
            target = (value > target) ? value : target;
        }
        //动态设定最大值
        int max = randomMax(target,index);
        holder.mTvValueName.setText(titles[index]);
        holder.mTvValueName.setVisibility(View.VISIBLE);
        holder.mBarChart1.setMax(max);
        holder.mBarChart2.setMax(max);
        holder.mBarChart3.setMax(max);
        holder.mBarChart4.setMax(max);
        holder.mBarChart5.setMax(max);
        holder.mBarChart1.setVisibility(dataVisible.get(0) ? View.VISIBLE : View.GONE);
        holder.mBarChart2.setVisibility(dataVisible.get(1) ? View.VISIBLE : View.GONE);
        holder.mBarChart3.setVisibility(dataVisible.get(2) ? View.VISIBLE : View.GONE);
        holder.mBarChart4.setVisibility(dataVisible.get(3) ? View.VISIBLE : View.GONE);
        holder.mBarChart5.setVisibility(dataVisible.get(4) ? View.VISIBLE : View.GONE);
        holder.mTvBarChartValue1.setVisibility(dataVisible.get(0) ? View.VISIBLE : View.GONE);
        holder.mTvBarChartValue2.setVisibility(dataVisible.get(1) ? View.VISIBLE : View.GONE);
        holder.mTvBarChartValue3.setVisibility(dataVisible.get(2) ? View.VISIBLE : View.GONE);
        holder.mTvBarChartValue4.setVisibility(dataVisible.get(3) ? View.VISIBLE : View.GONE);
        holder.mTvBarChartValue5.setVisibility(dataVisible.get(4) ? View.VISIBLE : View.GONE);
        if (mPlatformNumber > 0) {
            holder.mTvBarChartValue1.setText(data.get(0));
            holder.mBarChart1.setProgress((int) ((Float.parseFloat(data.get(0))) * 100));
            if (mPlatformNumber > 1) {
                holder.mTvBarChartValue2.setText(data.get(1));
                holder.mBarChart2.setProgress((int) ((Float.parseFloat(data.get(1))) * 100));
                if (mPlatformNumber > 2) {
                    holder.mTvBarChartValue3.setText(data.get(2));
                    holder.mBarChart3.setProgress((int) ((Float.parseFloat(data.get(2))) * 100));
                    if (mPlatformNumber > 3) {
                        holder.mTvBarChartValue4.setText(data.get(3));
                        holder.mBarChart4.setProgress((int) ((Float.parseFloat(data.get(3))) * 100));
                        if (mPlatformNumber > 4) {
                            holder.mTvBarChartValue5.setText(data.get(4));
                            holder.mBarChart5.setProgress((int) ((Float.parseFloat(data.get(4))) * 100));
                        }
                    }
                }
            }
        }
    }

    private int[] random = new int[]{18,10,16,8,12,20,14,17,10,16};

    private int randomMax(float max, int index) {
        int value = (int) (max * 100);
//        return value + value * (10 + new Random().nextInt(20)) / 100;
        return value + value * (10 + random[index]) / 100;
    }

    //设置关注监听器
    public void setOnPlatformCompareChangedListener(OnPlatformCompareChangedListener listener) {
        this.mPlatformCompareChangedListener = listener;
    }

    public interface OnPlatformCompareChangedListener {
        // 关注和取消关注
        void onFollowed(PlatformCompareBean datas, int position);
        void onAbolishFollow(PlatformCompareBean datas, int position);
        //周或月比较数据
        void onSelected(boolean isWeekSelected);
        //删除平台
        void onDeleted(int position);
    }
}
