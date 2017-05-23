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
    public String total;//页数
    public advisorDetailInfo user_info;//首席个人主业信息
}
