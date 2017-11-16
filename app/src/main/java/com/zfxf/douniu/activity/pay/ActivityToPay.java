package com.zfxf.douniu.activity.pay;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.askstock.ActivityAnswerDetail;
import com.zfxf.douniu.activity.myself.wallet.ActivityBuyNiu;
import com.zfxf.douniu.bean.OtherResult;
import com.zfxf.douniu.bean.PayStyle;
import com.zfxf.douniu.bean.ProjectListResult;
import com.zfxf.douniu.bean.XuanguResult;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.PayResult;
import com.zfxf.douniu.utils.SpTools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * @author IMXU
 * @time   2017/11/10 15:48
 * @des    支付界面
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class ActivityToPay extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.tv_topay_type_name)
    TextView tv_name;//购买的类型 智能选股王和金股池
    @BindView(R.id.tv_topay_type_count)
    TextView tv_count;//订阅数量
    @BindView(R.id.rl_topay_money1)
    RelativeLayout rl_money1;//选择支付的类型
    @BindView(R.id.rl_topay_money2)
    RelativeLayout rl_money2;
    @BindView(R.id.rl_topay_money3)
    RelativeLayout rl_money3;
    @BindView(R.id.tv_topay_type_name1)
    TextView tv_name1;//购买类型
    @BindView(R.id.tv_topay_price1)
    TextView tv_price1;//购买类型价格
    @BindView(R.id.tv_topay_type_name2)
    TextView tv_name2;//购买类型
    @BindView(R.id.tv_topay_price2)
    TextView tv_price2;//购买类型价格
    @BindView(R.id.tv_topay_type_name3)
    TextView tv_name3;//购买类型
    @BindView(R.id.tv_topay_price3)
    TextView tv_price3;//购买类型价格
    @BindView(R.id.tv_topay_type_yuan1)
    TextView tv_yuan1;
    @BindView(R.id.tv_topay_type_yuan2)
    TextView tv_yuan2;
    @BindView(R.id.tv_topay_type_yuan3)
    TextView tv_yuan3;
    @BindView(R.id.iv_topay_select1)
    ImageView iv_select1;
    @BindView(R.id.iv_topay_select2)
    ImageView iv_select2;
    @BindView(R.id.iv_topay_select3)
    ImageView iv_select3;

    @BindView(R.id.tv_topay_towho)
    TextView towho;
    @BindView(R.id.tv_topay_money)
    TextView money;
    @BindView(R.id.tv_topay_type)
    TextView type;
    @BindView(R.id.tv_topay_yu_e)
    TextView yu_e;
    @BindView(R.id.tv_topay_yu_e_count)
    TextView yu_e_count;
    @BindView(R.id.tv_topay_yue_pay)
    TextView yu_e_pay;
    @BindView(R.id.view_topay_yu_e)
    View v_yu_e;

    @BindView(R.id.ll_topay_niubi)
    LinearLayout niubi;
    @BindView(R.id.ll_topay_wx)
    LinearLayout wx;
    @BindView(R.id.ll_topay_zfb)
    LinearLayout zfb;
    @BindView(R.id.ll_topay_orignal)
    LinearLayout ll_original;//其他购买界面
    @BindView(R.id.ll_topay_type)
    LinearLayout ll_type;//智能选股王和金股池购买界面

    @BindView(R.id.iv_topay_niu_select)
    ImageView niu_select;
    @BindView(R.id.iv_topay_wx_select)
    ImageView wx_select;
    @BindView(R.id.iv_topay_zfb_select)
    ImageView zfb_select;
    @BindView(R.id.iv_topay_niu_noselect)
    ImageView niu_noselect;
    @BindView(R.id.iv_topay_wx_noselect)
    ImageView wx_noselect;
    @BindView(R.id.iv_topay_zfb_noselect)
    ImageView zfb_noselect;

    @BindView(R.id.rl_topay_wx)
    RelativeLayout rl_wx;//微信的按钮
    @BindView(R.id.rl_topay_zfb)
    RelativeLayout rl_zfb;//支付宝的按钮

    @BindView(R.id.rl_topay_pay)
    RelativeLayout pay;
    private String mCount;
    private String mType;//购买的类型周、月、季等
    private String sx_id;
    private static List<ProjectListResult> mNiubiStyle = new ArrayList<ProjectListResult>();
    private String mInfo;
    private String mOrder;
    private int mChange = 0;
    private IWXAPI api;
    private String mFrom;
    private int mPlanId;
    private List<PayStyle> mFeeList;
    private String mDycount;
    private String mBuytype = "";//付款类型
    private int mNiubi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topay);
        ButterKnife.bind(this);
        title.setText("提交订单");
        edit.setVisibility(View.INVISIBLE);
        mBuytype = getIntent().getStringExtra("type");
        mInfo = getIntent().getStringExtra("info");
        mCount = getIntent().getStringExtra("count");
        sx_id = getIntent().getStringExtra("sx_id");
        mFrom = getIntent().getStringExtra("from");
        mOrder = getIntent().getStringExtra("order");
        mDycount = getIntent().getStringExtra("dycount");

        mChange = getIntent().getIntExtra("change", 0);
        mPlanId = getIntent().getIntExtra("planId", 0);//方案id
        money.setText("￥"+ mCount);
        type.setText(mBuytype);
        towho.setText(mFrom);

        tv_yuan1.getPaint().setFakeBoldText(true);//加粗
        tv_yuan2.getPaint().setFakeBoldText(true);//加粗
        tv_yuan3.getPaint().setFakeBoldText(true);//加粗
        tv_price1.getPaint().setFakeBoldText(true);//加粗
        tv_price2.getPaint().setFakeBoldText(true);//加粗
        tv_price3.getPaint().setFakeBoldText(true);//加粗
        iv_select1.setVisibility(View.VISIBLE);

        api = WXAPIFactory.createWXAPI(this,  Constants.APP_ID);
        api.registerApp(Constants.APP_ID);
        instance = this;
        initData();
        initListener();
    }

    private void initData() {
        visitInternet();
        if("选股王".equals(mFrom)){
            tv_name.setText("智能选股王");
            getBuyListInfo();
        }else if("金股池".equals(mFrom)){
            tv_name.setText("金股池");
            getBuyListInfo();
        }else {
            ll_original.setVisibility(View.VISIBLE);
        }
//        yu_e.setVisibility(View.VISIBLE);
//        yu_e_pay.setVisibility(View.VISIBLE);
//        v_yu_e.setVisibility(View.GONE);
        if(mCount.equals("0")){
            rl_wx.setVisibility(View.GONE);
            rl_zfb.setVisibility(View.GONE);
            wx.setVisibility(View.GONE);
            zfb.setVisibility(View.GONE);
        }
    }

    private void getBuyListInfo() {
        NewsInternetRequest.BuyListInformation(mPlanId + "", sx_id,new NewsInternetRequest.ForResultXuanGuListener() {
            @Override
            public void onResponseMessage(XuanguResult result) {
                if(result.fee_list.size()>0){
                    mFeeList = result.fee_list;
                    tv_name1.setText(mFeeList.get(0).m_type+"卡");
                    tv_price1.setText(mFeeList.get(0).m_fee);
                    tv_name2.setText(mFeeList.get(1).m_type+"卡");
                    tv_price2.setText(mFeeList.get(1).m_fee);
                    tv_name3.setText(mFeeList.get(2).m_type+"卡");
                    tv_price3.setText(mFeeList.get(2).m_fee);
                    ll_type.setVisibility(View.VISIBLE);
                    mCount = mFeeList.get(0).m_fee;
                    mType = mFeeList.get(0).m_type;
                    if(mNiubi>0){
                        if((mNiubi -Integer.parseInt(mCount)*10)>=0){//牛币充足
                            yu_e.setVisibility(View.GONE);
                            yu_e_pay.setVisibility(View.GONE);
                            v_yu_e.setVisibility(View.VISIBLE);
                        }else {//牛币不足
                            yu_e.setVisibility(View.VISIBLE);
                            yu_e_pay.setVisibility(View.VISIBLE);
                            v_yu_e.setVisibility(View.GONE);
                        }
                    }
                    tv_count.setText("已订阅"+mDycount+"人次");
                }
            }
        });
    }

    private void visitInternet() {
        NewsInternetRequest.getNiuBiShow(new NewsInternetRequest.ForResultNewsInfoListener() {
            @Override
            public void onResponseMessage(OtherResult otherResult) {
                mNiubi = Integer.parseInt(otherResult.my_wallet);
                if(Integer.parseInt(mCount) > 0){
                    if((mNiubi -Integer.parseInt(mCount)*10)>=0){//牛币充足
                        yu_e.setVisibility(View.GONE);
                        yu_e_pay.setVisibility(View.GONE);
                        v_yu_e.setVisibility(View.VISIBLE);
                    }else {//牛币不足
                        yu_e.setVisibility(View.VISIBLE);
                        yu_e_pay.setVisibility(View.VISIBLE);
                        v_yu_e.setVisibility(View.GONE);
                    }
                }
                yu_e_count.setText(mNiubi +"牛币");
                yu_e_pay.setText(otherResult.niubi_style_info+" 去充值》");
                mNiubiStyle = otherResult.niubi_style;
            }
        });
    }

    public static List<ProjectListResult> getNiuBiStyle(){
        return mNiubiStyle;
    }
    private void initListener() {
        back.setOnClickListener(this);
        niubi.setOnClickListener(this);
        wx.setOnClickListener(this);
        zfb.setOnClickListener(this);
        pay.setOnClickListener(this);
        yu_e_pay.setOnClickListener(this);
        rl_money1.setOnClickListener(this);
        rl_money2.setOnClickListener(this);
        rl_money3.setOnClickListener(this);
    }
    Intent intent;
    int i = 0;
    private String payType = "";
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back:
                finishAll();
                finish();
                break;
            case R.id.rl_topay_pay://暂时是直接支付成功
                if(i == 0){
                    CommonUtils.toastMessage("请选择支付方式");
                    return;
                }
                switch (i){
                    case 1:
                        payType = "niubi";
                        break;
                    case 2:
                        payType = "wechat";
                        break;
                    case 3:
                        payType = "alipay";
                        break;
                }
                if(TextUtils.isEmpty(mOrder)){
                    mOrder = "";
                }
                if("选股王".equals(mFrom)){
                    if(TextUtils.isEmpty(mType)){
                        CommonUtils.toastMessage("请选择购买类型");
                        return;
                    }
                    if(TextUtils.isEmpty(mOrder)){
                        mInfo = mInfo+","+mType;
                    }else {
                        mInfo = mInfo.substring(0,mInfo.length()-1)+mType;
                    }
                }else if("金股池".equals(mFrom)){
                    if(TextUtils.isEmpty(mType)){
                        CommonUtils.toastMessage("请选择购买类型");
                        return;
                    }
                    if(TextUtils.isEmpty(mOrder)){
                        mInfo = mInfo+","+mType;
                    }else {
                        mInfo = mInfo.substring(0,mInfo.length()-1)+mType;
                    }
                }
                NewsInternetRequest.sendOrderInformation(mOrder,mCount, 0+"", mChange+"", payType, mInfo, sx_id, new NewsInternetRequest.ForResultNewsInfoListener() {
                    @Override
                    public void onResponseMessage(final OtherResult otherResult) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityToPay.this);
                            builder.setTitle("确认提交订单")
                                    .setPositiveButton("提交", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            confirmOrder(otherResult.pay_morder.pmo_order,otherResult.pay_morder.pmo_id);
                                            dialog.dismiss();
                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mOrder = otherResult.pay_morder.pmo_order;
                                            dialog.dismiss();
                                        }
                                    }).show();
//                            SpTools.setBoolean(CommonUtils.getContext(),Constants.sendOrder,true);
                        }
                });
                break;
            case R.id.ll_topay_niubi:
                reset();
                niu_select.setVisibility(View.VISIBLE);
                niu_noselect.setVisibility(View.INVISIBLE);
                i = 1;
                break;
            case R.id.ll_topay_wx:
                reset();
                wx_select.setVisibility(View.VISIBLE);
                wx_noselect.setVisibility(View.INVISIBLE);
                i = 2;
                break;
            case R.id.ll_topay_zfb:
                reset();
                zfb_select.setVisibility(View.VISIBLE);
                zfb_noselect.setVisibility(View.INVISIBLE);
                i = 3;
                break;
            case R.id.tv_topay_yue_pay:
                CommonUtils.toastMessage("余额支付");
                intent = new Intent(this,ActivityBuyNiu.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                break;
            case R.id.rl_topay_money1:
                resetSelect();
                iv_select1.setVisibility(View.VISIBLE);
                if(mFeeList.size()>0){
                    mCount = mFeeList.get(0).m_fee;
                    mType = mFeeList.get(0).m_type;
                }
                break;
            case R.id.rl_topay_money2:
                resetSelect();
                iv_select2.setVisibility(View.VISIBLE);
                if(mFeeList.size()>0){
                    mCount = mFeeList.get(1).m_fee;
                    mType = mFeeList.get(1).m_type;
                }
                break;
            case R.id.rl_topay_money3:
                resetSelect();
                iv_select3.setVisibility(View.VISIBLE);
                if(mFeeList.size()>0){
                    mCount = mFeeList.get(2).m_fee;
                    mType = mFeeList.get(2).m_type;
                }
                break;
        }
        intent = null;
        if(mNiubi>0){
            if((mNiubi -Integer.parseInt(mCount)*10)>=0){//牛币充足
                yu_e.setVisibility(View.GONE);
                yu_e_pay.setVisibility(View.GONE);
                v_yu_e.setVisibility(View.VISIBLE);
            }else {//牛币不足
                yu_e.setVisibility(View.VISIBLE);
                yu_e_pay.setVisibility(View.VISIBLE);
                v_yu_e.setVisibility(View.GONE);
            }
        }
    }

    private void resetSelect() {
        iv_select1.setVisibility(View.GONE);
        iv_select2.setVisibility(View.GONE);
        iv_select3.setVisibility(View.GONE);
    }

    private void confirmOrder(String pmo_order, String pmo_id) {
        NewsInternetRequest.confirmOrder(payType, pmo_order, pmo_id, new NewsInternetRequest.ForResultNewsInfoListener() {
            @Override
            public void onResponseMessage(OtherResult otherResult) {
                if(payType.equals("alipay")){
                    final String orderInfo = otherResult.response;
                    Runnable payRunnable = new Runnable() {
                        @Override
                        public void run() {
                            PayTask alipay = new PayTask(ActivityToPay.this);
                            Map<String, String> result = alipay.payV2(orderInfo, true);
                            Message msg = new Message();
                            msg.what = SDK_PAY_FLAG;
                            msg.obj = result;
                            mHandler.sendMessage(msg);
                        }
                    };
                    Thread payThread = new Thread(payRunnable);
                    payThread.start();
                }else if(payType.equals("wechat")){
                    if (api != null) {
                        PayReq req = new PayReq();
                        req.appId = otherResult.wx_params.appid;
                        req.partnerId = otherResult.wx_params.partnerid;
                        req.prepayId = otherResult.wx_params.prepayid;
                        req.packageValue = "Sign=WXPay";
                        req.nonceStr = otherResult.wx_params.noncestr;
                        req.timeStamp = otherResult.wx_params.timestamp;
                        req.sign = otherResult.wx_params.sign;
                        api.sendReq(req);
                        SpTools.setInt(CommonUtils.getContext(),Constants.payweixin,2);//非牛币购买的微信支付
                    }
                }else if(payType.equals("niubi")){
                    //牛币支付
                    if("一元偷偷看".equals(mBuytype)){
                        Intent intent = new Intent(CommonUtils.getContext(), ActivityAnswerDetail.class);
                        intent.putExtra("id",mPlanId+"");
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        SpTools.setBoolean(ActivityToPay.this, Constants.yiyuanbuy, true);
                        CommonUtils.toastMessage("支付成功");
                    }else {
                        SpTools.setBoolean(ActivityToPay.this, Constants.buy, true);
                        CommonUtils.toastMessage("支付成功");
                    }
                    finish();
                }
            }
        });
    }

    private void finishAll() {

    }
    private void reset() {
        niu_select.setVisibility(View.INVISIBLE);
        niu_noselect.setVisibility(View.VISIBLE);
        wx_select.setVisibility(View.INVISIBLE);
        wx_noselect.setVisibility(View.VISIBLE);

        zfb_select.setVisibility(View.INVISIBLE);
        zfb_noselect.setVisibility(View.VISIBLE);
    }
    private static final int SDK_PAY_FLAG = 1;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        if("一元偷偷看".equals(mBuytype)){
                            Intent intent = new Intent(CommonUtils.getContext(), ActivityAnswerDetail.class);
                            intent.putExtra("id",mPlanId+"");
                            startActivity(intent);
                            overridePendingTransition(0,0);
                            SpTools.setBoolean(ActivityToPay.this, Constants.yiyuanbuy, true);
                            CommonUtils.toastMessage("支付成功");
                        }else {
                            SpTools.setBoolean(ActivityToPay.this, Constants.buy, true);
                        }
                        Toast.makeText(ActivityToPay.this, "支付成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(ActivityToPay.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    private static ActivityToPay instance;
    public static ActivityToPay getInstance() {
        return instance;
    }
    public void showResult(int i){
        if(i == 2){
            if("一元偷偷看".equals(mBuytype)){
                Intent intent = new Intent(CommonUtils.getContext(), ActivityAnswerDetail.class);
                intent.putExtra("id",mPlanId+"");
                startActivity(intent);
                overridePendingTransition(0,0);
                SpTools.setBoolean(ActivityToPay.this, Constants.yiyuanbuy, true);
                CommonUtils.toastMessage("支付成功");
            }else {
                SpTools.setBoolean(ActivityToPay.this, Constants.buy, true);
                CommonUtils.toastMessage("支付成功");
            }
            finish();
        }
    }
}
