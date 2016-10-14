package com.ypwl.xiaotouzi.bean;

/**
 * 晓钱包实体
 *
 * Created by PDK on 2016/4/20.
 */
public class WalletBean {


    /**
     * intro : 晓钱包简介
     * money_freeze : 0.00
     * money_usable : 0.00
     * money_wait : 0.00
     * ret_msg :
     * status : 0
     */

    private String intro;
    private String money_freeze;
    private String money_usable;
    private String money_wait;
    private String ret_msg;
    private int status;

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getMoney_freeze() {
        return money_freeze;
    }

    public void setMoney_freeze(String money_freeze) {
        this.money_freeze = money_freeze;
    }

    public String getMoney_usable() {
        return money_usable;
    }

    public void setMoney_usable(String money_usable) {
        this.money_usable = money_usable;
    }

    public String getMoney_wait() {
        return money_wait;
    }

    public void setMoney_wait(String money_wait) {
        this.money_wait = money_wait;
    }

    public String getRet_msg() {
        return ret_msg;
    }

    public void setRet_msg(String ret_msg) {
        this.ret_msg = ret_msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
