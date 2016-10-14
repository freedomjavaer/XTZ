package com.ypwl.xiaotouzi.bean;

/**
 * 项目名:	XTZ
 * 包名:	com.ypwl.xiaotouzi.bean
 * 类名:	PlatformOfFinanceBean
 * 作者:	罗霄
 * 创建时间:	2016/4/19 11:34
 * <p/>
 * 描述:	金融超市 -- 平台列表数据bean
 * <p/>
 * svn版本:	$Revision: 14164 $
 * 更新人:	$Author: luoxiao $
 * 更新时间:	$Date: 2016-04-20 17:58:38 +0800 (周三, 20 四月 2016) $
 * 更新描述:	$Message$
 */
public class PlatformOfFinanceBean {

    /**
     * pid : 3489
     * p_name : 龙腾理财
     * p_logo : up_files/platform_image/3489/logo_56ad9d448cfa3.jpg
     * intro : 2222
     * video_atc : 0  //0：否，1：已认证
     * image : market_57147aef37b44.png
     */

    private String pid;
    private String p_name;
    private String p_logo;
    private String intro;
    private String video_atc;
    private String image;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public String getP_logo() {
        return p_logo;
    }

    public void setP_logo(String p_logo) {
        this.p_logo = p_logo;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getVideo_atc() {
        return video_atc;
    }

    public void setVideo_atc(String video_atc) {
        this.video_atc = video_atc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
