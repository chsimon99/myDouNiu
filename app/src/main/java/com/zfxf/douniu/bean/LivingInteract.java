package com.zfxf.douniu.bean;

/**
 * @author Admin
 * @time 2017/4/7 9:44
 * @des ${TODO}
 */

public class LivingInteract {
    public String ud_nickname;//昵称
    public String zp_ub_id;//直播人的id
    public String role;//角色 0播主 1观众
    public String zp_pl;//内容
    public String zp_date;//时间
    public String headImg;//图片地址

    public void setUd_nickname(String ud_nickname) {
        this.ud_nickname = ud_nickname;
    }

    public void setZp_ub_id(String zp_ub_id) {
        this.zp_ub_id = zp_ub_id;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setZp_pl(String zp_pl) {
        this.zp_pl = zp_pl;
    }

    public void setZp_date(String zp_date) {
        this.zp_date = zp_date;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }
}
