package com.zfxf.douniu.bean;

import java.util.List;

/**
 * @author Admin
 * @time 2017/4/7 9:44
 * @des ${TODO}
 */

public class SimulationResult {
    public BasicResult result;//基础结果
    public SimulationDetail user_detail;//个人信息
    public List<SimulationPositionDetail> mn_chigu;//持仓列表
    public List<SimulationEntrust> all_weituo;//委托列表
    public List<SimulationSearchInfo> mg_gupiao;//股票收索列表
    public SimulationBuy mr_gupiao;//买5列表
    public SimulationBuy mc_gupiao;//卖5列表
    public SimulationInfo mn_gupiao;//股票信息

    public List<SimulationInfo> gp_exponent;//行情指数信息
    public List<SimulationInfo> zhang_gupiao;//涨停股票列表
    public List<SimulationInfo> die_gupiao;//跌停股票列表

    public List<SimulationInfo> jinri_jiaoyi;//今日成交查询
    public List<SimulationInfo> lishi_jiaoyi;//历史成交查询


    public String total;//总页数
    public List<SimulationInfo> mn_zixuan;//自选股票列表
    public List<StockChiInfo> dn_jgc;//金股池列表
}
