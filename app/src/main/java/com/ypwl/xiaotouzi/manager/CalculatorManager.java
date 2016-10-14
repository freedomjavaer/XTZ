package com.ypwl.xiaotouzi.manager;

import com.ypwl.xiaotouzi.common.Const;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * function:计算器计算方式、公式
 * <p/>
 * Created by tengtao on 2016/1/13.
 */
public class CalculatorManager {

    private ArrayList<String[]> calDataList = new ArrayList();
    private Map<String, Object> resultMap = new HashMap<>();

    private static CalculatorManager instance;

    public static CalculatorManager getInstance() {
        if (null == instance) {
            synchronized (CalculatorManager.class) {
                if (null == instance) {
                    instance = new CalculatorManager();
                }
            }
        }
        return instance;
    }

    /**
     * 等额本息
     * @param principal  投资金额
     * @param monthRate  月利率
     * @param totalMonth 投资期限/月
     * @param extraRate  额外奖励百分率
     * @param costRate   利息管理百分率
     * @return   map集合
     */
    public Map<String, Object> calDengEBenXi(double principal, double monthRate, int totalMonth, double extraRate, double costRate) {
        // 设贷款额为a，月利率为i，年利率为I，还款月数为n，每月还款额为b，还款利息总和为Y
        // 月均还款b＝a×i×（1＋i）的n次方÷〔（1 ＋i）的n次方－1〕
        resultMap.clear();
        double yuejun = calDengebenxiYJHK(principal, monthRate, totalMonth);

        // 总利息 Y＝n×b－a
        double totalTakings = totalMonth * yuejun - principal;
        // 总利息管理费
        double dCost = convert(totalTakings * costRate);
        resultMap.put(Const.CAL_LXGL,dCost);
        // 总额外奖励
        double dExtraMoney = convert(principal * extraRate);
        resultMap.put(Const.CAL_TBJL,dExtraMoney);
        // 总收益
        double totalProfit = convert(totalTakings - dCost + dExtraMoney);
        resultMap.put(Const.CAL_SJSY,totalProfit);
        //实际利率
        double rate = getRealRate(totalTakings,dExtraMoney,dCost,principal);
        resultMap.put(Const.CAL_SJLL,rate);

        double dYishoubenjin = 0;
        calDataList.clear();

        for (int i = 0; i < totalMonth; ++i) {
            //期数
            String qishu = String.format("%s/%s", "" + (i + 1), "" + totalMonth);

            // 应收本金
            double dYingshoubenjin = calDengebenxiYSBJ(principal,monthRate,i,totalMonth);
            String strYingshoubenjin = "" + convert(dYingshoubenjin);

            // 应收收益
            double dYingshoushouyi = yuejun - dYingshoubenjin;
            dYingshoushouyi = dYingshoushouyi * (1 - dCost / totalTakings) + ((i == (totalMonth - 1))? dExtraMoney : 0);//最后月加上额外奖励
            String strYingshoushouyi = "" + convert(dYingshoushouyi);

            // 待收本金
            dYishoubenjin += dYingshoubenjin;
            double dDaishoubenjin = principal - dYishoubenjin;
            String strDaishoubenjin = "" + convert(dDaishoubenjin);

            // 实收总额:(本+息)/期限
            yuejun = (principal + totalTakings - dCost + dExtraMoney) / totalMonth;
            String strShishouzonge = "" + convert(yuejun);

            calDataList.add(new String[]{qishu, strYingshoubenjin, strYingshoushouyi, strDaishoubenjin, strShishouzonge});
        }
        resultMap.put(Const.CAL_JGQD,calDataList);

        return resultMap;
    }

    /**
     * 等额本金
     * @param principal  投资金额
     * @param monthRate  月利率
     * @param totalMonth 投资期限/月
     * @param extraRate  额外奖励百分率
     * @param costRate   利息管理百分率
     * @return   map集合
     */
    public Map<String, Object> calDengebenjin(double principal, double monthRate, int totalMonth, double extraRate, double costRate) {
        // 每月应还利息 = 剩余本金×月利率 = (贷款本金-已归还本金累计额) × 月利率
        // 总利息=〔(总贷款额÷还款月数+总贷款额×月利率)+总贷款额÷还款月数×(1+月利率)〕÷2×还款月数-总贷款额
        //每月还款金额 = （贷款本金 / 还款月数）+（本金 — 已归还本金累计额）×每月利率
        resultMap.clear();
        //月本金
        double perMonthBenjin = principal / totalMonth;
        //利息
        double totalTakings = calDengebenjinLiXi(principal, monthRate, totalMonth);
        //利息管理费
        double dCost = convert(totalTakings * costRate);
        resultMap.put(Const.CAL_LXGL,dCost);
        //额外奖励
        double dExtraMoney = convert(principal * extraRate);
        resultMap.put(Const.CAL_TBJL, dExtraMoney);
        //实际收益
        double totalProfit = convert (totalTakings - dCost + dExtraMoney);
        resultMap.put(Const.CAL_SJSY, totalProfit);
        //实际利率
        double rate = getRealRate(totalTakings, dExtraMoney, dCost, principal);
        resultMap.put(Const.CAL_SJLL, rate);

        //数据清单更新
        calDataList.clear();
        double dYishoubenjin = 0;

        for (int i = 0; i < totalMonth; ++i) {
            //期数
            String qishu = String.format("%s/%s", "" + (i + 1), "" + totalMonth);
            // 应收本金
            String strYingshoubenjin = "" + convert(perMonthBenjin);
            // 应收收益
            double oneMonthTalkings = (principal - perMonthBenjin * i) * monthRate;
            double oneMonthRealTalkings = oneMonthTalkings * (1 - dCost / totalTakings) + (i == (totalMonth - 1)?dExtraMoney:0);
            String strYingshoushouyi = "" + convert(oneMonthRealTalkings);

            // 待收本金
            dYishoubenjin += perMonthBenjin;
            String strDaishoubenjin = "" + convert(principal - dYishoubenjin);

            // 实收总额
            String strShishouzonge = "" + convert(perMonthBenjin + oneMonthTalkings + (i == (totalMonth - 1)?dExtraMoney:0));//最后一次加奖励

            calDataList.add(new String[]{qishu, strYingshoubenjin, strYingshoushouyi, strDaishoubenjin, strShishouzonge});
        }
        resultMap.put(Const.CAL_JGQD, calDataList);

        return resultMap;
    }

    /**
     * 到期还本息
     * @param isMonth    true：月 ，false：天
     * @param principal  投资金额
     * @param monthRate  月利率
     * @param totalMonth 投资期限/月
     * @param extraRate  额外奖励百分率
     * @param costRate   利息管理百分率
     * @return   map集合
     */
    public Map<String, Object> calDaoqihuanbenxi(boolean isMonth, double principal, double monthRate, int totalMonth, double extraRate, double costRate) {
        resultMap.clear();
        //利息
        double totalTakings = principal * monthRate * totalMonth / (isMonth ? 1 : 30);
        //利息管理费
        double dCost = convert(totalTakings * costRate);
        resultMap.put(Const.CAL_LXGL,dCost);
        //额外奖励
        double dExtraMoney = convert(principal * extraRate);
        resultMap.put(Const.CAL_TBJL, dExtraMoney);
        //总收入
        double totalProfit = convert (totalTakings - dCost + dExtraMoney);
        resultMap.put(Const.CAL_SJSY, totalProfit);
        //实际利率
        double rate = getRealRate(totalTakings, dExtraMoney, dCost, principal);
        resultMap.put(Const.CAL_SJLL, rate);

        //更新数据清单
        calDataList.clear();
        // 期数
        String qishu = "1/1";
        // 应收本金
        String strYingshoubenjin = "" + convert(principal);
        // 应收收益:利率收益+奖励收益-利息管理费
        String strYingshoushouyi = "" + convert(totalTakings - dCost + dExtraMoney);
        // 待收本金
        String strDaishoubenjin = "0";
        // 实收总额
        String strShishouzonge = "" + convert(principal + totalTakings - dCost + dExtraMoney);

        calDataList.add(new String[]{qishu, strYingshoubenjin, strYingshoushouyi, strDaishoubenjin, strShishouzonge});
        resultMap.put(Const.CAL_JGQD, calDataList);

        return resultMap;
    }

    /**
     * 到期换本金
     * @param principal  投资金额
     * @param monthRate  月利率
     * @param totalMonth 投资期限/月
     * @param extraRate  额外奖励百分率
     * @param costRate   利息管理百分率
     * @return   map集合
     */
    public Map<String, Object> calDaoqihuanbenjin(double principal, double monthRate, int totalMonth, double extraRate, double costRate) {
        //利息
        double totalTakings = principal * monthRate * totalMonth;
        //管理费
        double dCost = convert(totalTakings * costRate);
        resultMap.put(Const.CAL_LXGL,dCost);
        double perMonthCost = dCost / totalMonth;
        //额外奖励
        double dExtraMoney = convert(principal * extraRate);
        resultMap.put(Const.CAL_TBJL, dExtraMoney);
        //总收入--实际收益
        double totalProfit = convert (totalTakings - dCost + dExtraMoney);
        resultMap.put(Const.CAL_SJSY, totalProfit);
        //实际利率
        double rate = getRealRate(totalTakings, dExtraMoney, dCost, principal);
        resultMap.put(Const.CAL_SJLL, rate);

        calDataList.clear();
        double dYishoubenjin = 0;

        for (int i = 0; i < totalMonth; ++i) {
            //期数
            String qishu = String.format("%s/%s", "" + (i + 1), "" + totalMonth);
            // 应收本金
            double dYingshoubenjin = (i == (totalMonth - 1) ? principal : 0);
            String strYingshoubenjin = "" + convert(dYingshoubenjin);

            // 应收收益
            double dYingshoushouyi = principal * monthRate - perMonthCost + (i == (totalMonth - 1)?dExtraMoney:0);
            String strYingshoushouyi = "" + convert(dYingshoushouyi);

            // 待收本金
            dYishoubenjin += dYingshoubenjin;
            String strDaishoubenjin = "" + convert(principal - dYishoubenjin);

            // 实收总额
            String strShishouzonge = "" + convert(dYingshoubenjin + dYingshoushouyi);

            calDataList.add(new String[]{qishu, strYingshoubenjin, strYingshoushouyi, strDaishoubenjin, strShishouzonge});
        }
        resultMap.put(Const.CAL_JGQD, calDataList);

        return resultMap;
    }

    /** 四舍五入的实现 */
    private double convert(double value) {
        long i = Math.round(value * 100);
        return (i / 100.0);
    }


    /**
     * 等额本息计算
     * 公式：设贷款额为a，月利率为i，年利率为I，还款月数为n，每月还款额为b，还款利息总和为Y
     * 月均还款 b＝ a×i×（1＋i）的n次方 ÷〔（1 ＋i）的n次方 － 1〕
     *
     * @param principal  投资金额
     * @param monthRate  月利率
     * @param totalMonth 投资期限（月）
     * @return 月均还款
     */
    public double calDengebenxiYJHK(double principal, double monthRate, int totalMonth) {
        return principal * monthRate * Math.pow(1 + monthRate, totalMonth) / (Math.pow(1 + monthRate, totalMonth) - 1);
    }

    /**
     * 等额本息---月应收本金
     * @param principal 投资金额
     * @param monthRate  月利率
     * @param curMonth   当前月
     * @param totalMonth  总投资期限（月）
     * @return 应收本金
     */
    public double calDengebenxiYSBJ(double principal, double monthRate, int curMonth, int totalMonth) {
        return principal * monthRate * Math.pow(1 + monthRate, curMonth) / (Math.pow(1 + monthRate, totalMonth) - 1);
    }

    /**
     * 等额本金
     * 公式：每月应还利息 = 剩余本金×月利率 = (贷款本金-已归还本金累计额)×月利率
     * 总利息=〔(总贷款额÷还款月数+总贷款额×月利率)+总贷款额÷还款月数×(1+月利率)〕÷2×还款月数-总贷款额
     *
     * @param principal  投资金额
     * @param monthRate  月利率
     * @param totalMonth  投资期限（月）
     * @return 总利息
     */
    public double calDengebenjinLiXi(double principal, double monthRate, int totalMonth) {
        return ((principal / totalMonth + principal * monthRate) + principal / totalMonth * (1 + monthRate)) / 2 * totalMonth - principal;
    }

    /**
     * 计算实际利率
     * @param totalTakings  总收益
     * @param dExtraMoney   额外奖励
     * @param dCost         利息关联费
     * @param principal     本金
     * @return              折算后的实际利率
     */
    public double getRealRate(double totalTakings, double dExtraMoney, double dCost, double principal) {
        double value = (totalTakings + dExtraMoney - dCost) / principal * 100;
        long i = Math.round(value * 100);
        double dRealRate = i / 100.0;
        return dRealRate;
    }
}
