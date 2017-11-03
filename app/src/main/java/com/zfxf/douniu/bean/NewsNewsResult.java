package com.zfxf.douniu.bean;

/**
 * @author Admin
 * @time 2017/4/7 9:41
 * @des ${TODO}
 */

public class NewsNewsResult {

    public String cc_id;//资讯id
    public String cc_fielid;//图片
    public String cc_from;//来源
    public String cc_title;//标题
    public String cc_datetime;//时间
    public String this_date;//当天时间
    public String cc_context;//快讯内容

    public String ov_id;//视频id
    public String ov_ub_id;//发布视频者id
    public String ov_title;//视频标题
    public String ov_file;//视频文件地址
    public String ov_pic;//视频图片地址
    public String headImg;//头像地址
    public String ud_nickname;//昵称
    public String has_dz;//是否点赞
    public String dz_count;//观看数量

    public void setHas_dz(String has_dz) {
        this.has_dz = has_dz;
    }

}
