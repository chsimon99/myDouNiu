package com.zfxf.douniu.activity;

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
import com.zfxf.douniu.bean.OtherResult;
import com.zfxf.douniu.bean.ProjectListResult;
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

public class ActivityToPay extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;

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

    @BindView(R.id.rl_topay_pay)
    RelativeLayout pay;
    private String mCount;
    private String sx_id;
    private static List<ProjectListResult> mNiubiStyle = new ArrayList<ProjectListResult>();
    private String mInfo;
    private String mOrder;
    private int mChange = 0;
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topay);
        ButterKnife.bind(this);
        title.setText("提交订单");
        edit.setVisibility(View.INVISIBLE);
        String mytype = getIntent().getStringExtra("type");
        mInfo = getIntent().getStringExtra("info");
        mCount = getIntent().getStringExtra("count");
        sx_id = getIntent().getStringExtra("sx_id");
        String from = getIntent().getStringExtra("from");
        mOrder = getIntent().getStringExtra("order");
        mChange = getIntent().getIntExtra("change", 0);
        money.setText("￥"+ mCount);
        type.setText(mytype);
        towho.setText(from);
        api = WXAPIFactory.createWXAPI(this,  Constants.APP_ID);
        api.registerApp(Constants.APP_ID);
        initData();
        initListener();
    }

    private void initData() {
        visitInternet();
        yu_e.setVisibility(View.VISIBLE);
        yu_e_pay.setVisibility(View.VISIBLE);
        v_yu_e.setVisibility(View.GONE);
    }

    private void visitInternet() {
        NewsInternetRequest.getNiuBiShow(new NewsInternetRequest.ForResultNewsInfoListener() {
            @Override
            public void onResponseMessage(OtherResult otherResult) {
                int niubi = Integer.parseInt(otherResult.my_wallet);
                if((niubi-Integer.parseInt(mCount)*10)>=0){//牛币充足
                    yu_e.setVisibility(View.GONE);
                    yu_e_pay.setVisibility(View.GONE);
                    v_yu_e.setVisibility(View.VISIBLE);
                }else {//牛币不足
                    yu_e.setVisibility(View.VISIBLE);
                    yu_e_pay.setVisibility(View.VISIBLE);
                    v_yu_e.setVisibility(View.GONE);
                }
                yu_e_count.setText(niubi+"牛币");
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
        }
        intent = null;
    }

    private void confirmOrder(String pmo_order, String pmo_id) {
        NewsInternetRequest.confirmOrder(payType, pmo_order, pmo_id, new NewsInternetRequest.ForResultNewsInfoListener() {
            @Override
            public void onResponseMessage(OtherResult otherResult) {
                if(payType.equals("alipay")){
                    final String orderInfo = otherResult.response;
                    CommonUtils.logMes("-------orderInfo----"+orderInfo);
                    Runnable payRunnable = new Runnable() {
                        @Override
                        public void run() {
                            CommonUtils.logMes("-------msp--1--");
                            PayTask alipay = new PayTask(ActivityToPay.this);
                            CommonUtils.logMes("-------msp--2--");
                            Map<String, String> result = alipay.payV2(orderInfo, true);
                            CommonUtils.logMes("-------msp--3--"+result);
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
                    }
                }else if(payType.equals("niubi")){
                    //牛币支付
                    SpTools.setBoolean(ActivityToPay.this, Constants.buy, true);
                }
                finish();
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
                        SpTools.setBoolean(ActivityToPay.this, Constants.buy, true);
                        Toast.makeText(ActivityToPay.this, "支付成功", Toast.LENGTH_SHORT).show();
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
}
