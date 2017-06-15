package com.zfxf.douniu.bean;

/**
 * @author Admin
 * @time 2017/4/7 9:44
 * @des ${TODO}
 */

public class SimulationEntrust {
    public String mj_type;//类型 0卖出  1买入
    public String mg_name;//股票名
    public String mc_id;//股票id
    public String mj_wtsj;//委托时间
    public String mj_wtj;//委托价
    public String mj_cjj;//成交价
    public String mj_wtl;//委托量
    public String mj_cjl;//成交量
    public String mj_result;//状态  0正在等待 1交易成功 2部分交易 3撤销

}
