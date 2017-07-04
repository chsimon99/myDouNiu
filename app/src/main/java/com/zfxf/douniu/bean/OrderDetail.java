package com.zfxf.douniu.bean;

/**
 * @author Admin
 * @time 2017/4/7 9:41
 * @des ${TODO}
 */

public class OrderDetail {

    public String pmo_fee;//支付费用
    public String pmo_type;//订单类型 0 购买 1 充值
    public String pmo_info;//订单描述
    public String pmo_status;//订单状态 默认传 0 待支付

    public String getPmo_fee() {
        return pmo_fee;
    }

    public void setPmo_fee(String pmo_fee) {
        this.pmo_fee = pmo_fee;
    }

    public String getPmo_type() {
        return pmo_type;
    }

    public void setPmo_type(String pmo_type) {
        this.pmo_type = pmo_type;
    }

    public String getPmo_info() {
        return pmo_info;
    }

    public void setPmo_info(String pmo_info) {
        this.pmo_info = pmo_info;
    }

    public String getPmo_status() {
        return pmo_status;
    }

    public void setPmo_status(String pmo_status) {
        this.pmo_status = pmo_status;
    }
}
