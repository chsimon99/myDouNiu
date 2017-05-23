package com.zfxf.douniu.bean;

import java.util.List;

/**
 * @author Admin
 * @time 2017/4/7 9:44
 * @des ${TODO}
 */

public class XuanguDetail {
    public String zf_id;//id
    public String zf_title;//标题
    public String zf_pjsy;//平均收益
    public String zf_xgcgl;//选股成功率
    public String zf_zsx;//止损线
    public String zf_fdzy;//浮动止盈
    public String zf_cgzq;//持股周期
    public String zf_dys;//订阅数
    public String zf_date;//时间
    public String zf_info;//详情
    public List<XuanguItemList> item_list;
    public String has_dy;//是否订阅

    public String zi_id;//id
    public String zi_title;//标题
    public String zi_tjgps;//推荐股票数
    public List<XuanguGupiaoDetail> gupiao_list;//推荐股票详情

    public List<XuanguHistoryDetail> news_list;//选股历史数据
    public String total;//历史数据页数
}
