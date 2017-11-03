package com.zfxf.douniu.bean;

/**
 * @author Admin
 * @time 2017/4/7 9:44
 * @des ${TODO}
 */

public class SimulationSearchInfo {
    public String mb_name;//类型
    public String mg_id;//id
    public String mg_code;//委托时间
    public String mg_name;//委托价
    public String name;// 1深A  2指数  3沪A
    public String zixuan_status;//是否已加过自选 0否  1是
    public boolean isAddSuccess;//是否添加自选成功

    public boolean isAddSuccess() {
        return isAddSuccess;
    }

    public void setAddSuccess(boolean addSuccess) {
        isAddSuccess = addSuccess;
    }
}
