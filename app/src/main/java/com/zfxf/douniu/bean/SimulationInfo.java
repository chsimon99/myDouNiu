package com.zfxf.douniu.bean;

/**
 * @author Admin
 * @time 2017/4/7 9:44
 * @des ${TODO}
 */

public class SimulationInfo {
    public String mg_id;//id
    public String mg_xj;//现价
    public String mg_dt;//跌停
    public String mg_zt;//涨停
    public String mg_zs;//昨收盘价
    public String mg_code;//代码
    public String mg_name;//名字
    public String mg_zfz;//涨幅值
    public String mg_cj;//增加值
    public String Trade_price;//现价
    public String mg_zzc;//总资产
    public String mg_ccsl;//购买数量
    public String mg_cbj;//购买成本价
    public String mg_kmsl;//可买数量

    public boolean isSelect;//是否选中

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String djf_id;//id
    public String mg_rxjg;//入选价
    public String mg_zgj;//最高价
    public String mg_tjly;//推荐理由

    public void setMg_code(String mg_code) {
        this.mg_code = mg_code;
    }

    public void setMg_name(String mg_name) {
        this.mg_name = mg_name;
    }

    public void setMg_ccsl(String mg_ccsl) {
        this.mg_ccsl = mg_ccsl;
    }

    public void setMg_cbj(String mg_cbj) {
        this.mg_cbj = mg_cbj;
    }

    public String mj_id;//
    public String mj_type;// 0卖出 1买入
    public String mj_cjsj;//成交时间
    public String mj_cjj;//成交价
    public String mj_cjl;//成交量
    public String mj_result;//结果

}
