package com.zfxf.douniu.bean;

/**
 * @author Admin
 * @time 2017/4/7 9:41
 * @des ${TODO}
 */

public class ProjectListResult {

    public String cc_id;
    public String cc_title;//标题
    public String cc_description;//详情
    public String shiyong;//适用人群
    public String feiyong;//费用
    public String cc_datetime;//时间
    public String cc_fielid;//图片地址
    public String biaoshi;// 0预热 1进行


    public String name;// 方式
    public String number;// 数额
    public String money;// 充值数量
    public String niubi;// 牛币数量
    public String jiangli;// 奖励数量

    public String pmo_id;//生成订单的顺序
    public String pmo_order;//订单号
    public String pmo_fee;//订单费用
}
