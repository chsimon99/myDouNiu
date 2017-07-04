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
}
