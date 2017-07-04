package com.zfxf.douniu.bean;

import java.util.List;

/**
 * @author Admin
 * @time 2017/4/7 9:44
 * @des ${TODO}
 */

public class XuanguResult {
    public BasicResult result;//基础结果
    public XuanguDetail news_info;//详情信息
    public XuanguItemDetail item_info;//选股详情
    public List<XuanguDetail> news_list;//我的选股王列表


    public List<SimulationInfo> ls_jgc;//历史金股池
    public List<SimulationInfo> rx_jgc;//入选金股池
    public String status;//状态 0未购买  1已购买
    public StockChiInfo dn_jgc;//金股池详情信息
    public String jgc_count;//入选股数量

    public List<XuanguHistoryDetail> ls_result;//金股池历史数据
}
