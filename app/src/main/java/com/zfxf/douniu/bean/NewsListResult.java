package com.zfxf.douniu.bean;

/**
 * @author Admin
 * @time 2017/4/7 9:41
 * @des ${TODO}
 */

public class NewsListResult {

    public String cc_id;
    public String cc_title;
    public String cc_datetime;
    public String cc_count;
    public String headImg;
    public String ud_nickname;
    public String cc_description;
    public String cc_auth; //作者
    public String cc_from; //信息来源
    public String cc_fee; //价格
    public String cc_fielid; //图片地址
    public String buy_count; //购买数量
    public String dy_count; //订阅数量
    public String has_buy; //是否购买 0未购买 1已购买
    public String has_dy; //是否订阅 0未订阅 1已订阅
    public String cc_ub_id; //首席id

    public String zt_id; //直播ID
    public String zt_name; //直播主题
    public String zt_clicks; //直播数量
    public String ud_ub_id; //播主id
    public String zt_start; //开始时间
    public String status; //直播状态 1直播 0结束
}
