package com.zfxf.douniu.bean;

import java.util.List;

/**
 * @author Admin
 * @time 2017/4/7 9:41
 * @des ${TODO}
 */

public class StockResult {
    public BasicResult result;//基础结果

    public StockInfo gupiao_info;//股票基础信息
    public List<StockInfo> mg_gupiao;
    public List<StockInfo> mn_gupiao;
    public String station;
    public String total;
    public List<StockInfo> all_news;//股票news信息
}
