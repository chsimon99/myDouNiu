package com.zfxf.douniu.bean;

import java.util.List;

/**
 * @author Admin
 * @time 2017/4/7 9:44
 * @des ${TODO}
 */

public class IndexResult {
    public BasicResult result;//基础结果
    public List<IndexAdvisorListInfo> shouxi_list;//主页首席列表
    public List<IndexStockListInfo> xuangu_list;//主页选股王
    public List<IndexLivingListInfo> zhibo_list;//主页热门直播
    public List<LunBoListInfo> lunbo_list;//轮播图
    public String total;//页数
    public advisorDetailInfo user_info;//首席个人主业信息
    public List<AnswerChiefListInfo> online_chief;//微问答主页分析师列表
    public List<AnswerListInfo> bright_answer;//微问答主页精彩回答
    public AnswerListInfo context_info;//微问答回答详情
}
