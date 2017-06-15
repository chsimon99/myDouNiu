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
    public String djf_id;//对应方案的id
    public List<SimulationInfo> gu_piao;//股票信息

    public String dy_fee;//订阅费用
    public String djf_syrq;//试用人群
    public String djf_fxts;//风险提示
    public String dj_type;//0短线 1中线
}
