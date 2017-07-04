package com.zfxf.douniu.bean;

/**
 * @author Admin
 * @time 2017/4/7 9:44
 * @des ${TODO}
 */

public class OrderInformationBean {
    public int ub_id;
    public String sid;
    public String index;
    public String uo_long;
    public String uo_lat;
    public String uo_high;
    public String sx_ub_id;//首席id
    public String pmo_order;//订单号
    public String pt_code;//支付方式 wechat  微信 alipay  支付宝 niubi  牛币
    public OrderDetail pay_order;//订单信息

    public void setUb_id(int ub_id) {
        this.ub_id = ub_id;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public void setUo_long(String uo_long) {
        this.uo_long = uo_long;
    }

    public void setUo_lat(String uo_lat) {
        this.uo_lat = uo_lat;
    }

    public void setUo_high(String uo_high) {
        this.uo_high = uo_high;
    }

    public void setPt_code(String pt_code) {
        this.pt_code = pt_code;
    }

    public void setPay_order(OrderDetail pay_order) {
        this.pay_order = pay_order;
    }

    public void setSx_ub_id(String sx_ub_id) {
        this.sx_ub_id = sx_ub_id;
    }

    public void setPmo_order(String pmo_order) {
        this.pmo_order = pmo_order;
    }
}
