package com.zfxf.douniu.bean;

import java.util.List;

/**
 * @author Admin
 * @time 2017/4/7 9:41
 * @des ${TODO}
 */

public class StockChiInfo {

    public String ud_nickname;//昵称
    public String ud_ub_id;//首席id
    public String jgcfa_date;//发布日期
    public String dy_count;//订阅数
    public String jgc_count;//股池数量
    public String is_up;// 0跌  1涨
    public String status;//最大涨幅
    public List<SimulationInfo> gu_piao;//股票信息

}
