package com.zfxf.douniu.bean;

/**
 * @author Admin
 * @time 2017/4/7 9:41
 * @des ${TODO}
 */

public class CourseInfo {

    public String cc_id;//资讯id
    public String img;//图片
    public String cc_from;//来源
    public String cc_title;//标题
    public String cc_datetime;//时间
    public String cc_description;//课程介绍
    public String ud_nickname;//作者
    public String url;//直播地址
    public String status;//直播状态 0已结束 1正在直播 2未开始

    public String auth_ub_id;//作者id，用于关注
    public String has_dy;//是否订阅
    public String dy_count;//订阅数量
    public String has_gz;//是否关注
    public String has_buy;//是否购买
    public String buy_count;//购买数量
    public String cc_fee;//价格

}
