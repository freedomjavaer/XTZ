package com.ypwl.xiaotouzi.bean;

/**
 * function :堆积图数据对象
 * <p/>
 * Created by tengtao on 2016/3/25.
 */
public class StackBarChartBean {
    private String datetime;
    private String money;
    private String overdue;
    private String type;//1：已收；2：待收
    private boolean selected;//是否选中

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getOverdue() {
        return overdue;
    }

    public void setOverdue(String overdue) {
        this.overdue = overdue;
    }
}
