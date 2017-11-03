package com.zfxf.douniu.bean;

import java.util.List;

/**
 * @author Admin
 * @time 2017/4/7 9:44
 * @des ${TODO}
 */

public class OtherResult {
    public BasicResult result;//基础结果
    public String total;//总页数
    public List<NewsListResult> news_list;//新闻列表数据
    public List<ProjectListResult> project_list;//好项目列表数据
    public ProjectListResult project_info;//好项目详情数据
    public NewsInfomationResult news_info;//新闻头条详情
    public String pl_total;//评论数
    public String is_dz;//是否点赞 0:没有  1:有
    public DaShangInformationResult ds_info;//打赏信息
    public List<CommentInformationResult> pl_info;//评论列表数据
    public CommentResult cms_contextpl;//发表评论成功信息
    public List<LunBoListInfo> lunbo_list;//轮播图

    public String my_wallet;//钱包余额
    public List<ProjectListResult> hq_nb;//钱包中获取牛币方式
    public String niubi_style_info;//牛币支付信息
    public List<ProjectListResult> niubi_style;//钱包中获取牛币方式

    public ProjectListResult pay_morder;//提交订单返回信息

    public String response;//支付宝返回的信息
    public WeiXinInfo wx_params;//微信返回信息

    public String url;
    public String zc_id;//问股返回
    public String cc_id;//项目申请返回
    public String pd_id;//项目调研返回

}
