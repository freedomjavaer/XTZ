package com.ypwl.xiaotouzi.bean;

import java.util.List;

/**
 * 平台详情页面Bean实体
 *
 */
public class PlatformDetailBean {

    /**
     *o-JSON
     v-aoc : "不可转让"
     v-auto_invest : "支持"
     v-avg_profit : "22.67%"
     v-business_type : "信用(50%)、车贷(10%)、房贷(20%)、供应链(20%)"
     v-c_address : "深圳市福田区福华三路国际商会中心24楼08-12"
     v-c_capital : "10000万元"
     a-c_companyimg
     v-c_email : "serina@chenghuitongl.net"
     v-c_fax : "0755-88301569"
     v-c_name : "诚汇通"
     v-c_tel : "0755-88303356"
     v-c_tel400 : "400-018-0107"
     v-f_situation : "未发现公开融资"
     v-follow : "0"
     v-g_mode : "平台垫付、风险准备金(1350万元)"
     v-goi : "本息全保障"
     v-guarantor : ""
     v-launch_time : "2013-07-10"
     v-legal_person : "刘建"
     v-location : "广东省 | 深圳市"
     v-p_icp : "粤ICP备13044593号"
     v-p_logo : "logo_55ee583fc3efd.jpg"
     v-p_name : "诚汇通"
     v-p_site : "http://www.chenghuitong.net"
     a-pie_money
     a-pie_time
     n-status : 0
     v-trust_funds : "有托管"
     a-video_url
     a-x_date
     a-y_cjl
     a-y_jkrs
     a-y_ll
     a-y_tzrs
     */

    public String aoc;//债权转让
    public String auto_invest;//自动投标
    public String avg_profit;//平均收益
    public String business_type;//业务类型
    public String background;//平台背景
    public String c_address;//公司地址
    public String c_capital;//注册资本
    public String c_email;//服务邮箱
    public String c_fax;//公司传真
    public String c_name;//公司名称
    public String c_tel;//公司电话
    public String c_tel400;//400热线
    public String f_situation;//融资状况
    public String follow;//是否已关注
    public String g_mode;//保障模式
    public String goi;//投标保障
    public String guarantor;//担保机构
    public String launch_time;//上线时间
    public String legal_person;//企业法人
    public String location;//公司地址
    public String p_icp;//ICP备案号
    public String p_logo;//平台logo
    public String p_name;//平台名称
    public String p_site;//平台网址
    public String trust_funds;//资金托管
    public int status;//请求状态
    public String level;

    public List<String> c_companyimg;//公司图片
    public List<Money_Data> pie_money;//近三月标的金额饼图数据
    public List<Time_Data> pie_time;//近三月标的期限饼图数据
    public List<VideoInfoBean> video_url;//监控视频地址
    public List<String> x_date;//x轴日期
    public List<String> y_cjl;//成交量y轴数据
    public List<String> y_jkrs;//借款人数y轴数据
    public List<String> y_ll;//利率y轴数据
    public List<String> y_tzrs;//投资人数y轴数据

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }


    public static class Money_Data{
        public String key;
        public String value;
    }

    public static class Time_Data{
        public String key;
        public String value;
    }
    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public List<Money_Data> getPie_money(){
        return pie_money;
    }

    public List<Time_Data> getPie_time(){
        return pie_time;
    }


}

