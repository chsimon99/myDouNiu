package com.zfxf.douniu.bean;

/**
 * @author Admin
 * @time 2017/4/7 9:41
 * @des ${TODO}
 */

public class AnswerListInfo {

    public String zc_context;//标题
    public String url;//图片
    public String zc_fee;//问股价格
    public String zc_date;//时间
    public String zc_count;//访问数量
    public String zc_id;//问股id
    public String sx_ub_id;//首席id
    public String zc_pl;//详情
    public String ud_nickname;//昵称
    public String zc_sfjf;//是否购买

    // 回答详情信息
    public String zc_response_date;//提问时间
    public String sx_url;//首席图片
    public String sx_ud_nickname;//首席昵称
    public String sx_count;//点赞数量
    public String sx_answer_date;//回答时间
    public String sx_pl;//回答详情
    public String sx_fee;//问问题费用
    public String is_zan;//是否点赞 0没有 1有

    //打赏金额
    public String price;

    //打赏
    public String id;// 新闻或直播id
    public String title;// 标题
    public String auth_id;// 作者id
    public String nickname;// 昵称
    public String headImg;// 图片
    public String ds_count;// 打赏数量
}
