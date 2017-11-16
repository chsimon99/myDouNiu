package com.zfxf.douniu.activity.pay;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zfxf.douniu.R;
import com.zfxf.douniu.adapter.recycleView.NiuBiDepositAdapter;
import com.zfxf.douniu.bean.OtherResult;
import com.zfxf.douniu.bean.ProjectListResult;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.PayResult;
import com.zfxf.douniu.utils.SpTools;
import com.zfxf.douniu.view.FullyLinearLayoutManager;
import com.zfxf.douniu.view.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * @author IMXU
 * @time   2017/11/10 15:17
 * @des    微信和支付宝充值界面
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class ActivityDeposit extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.rv_deposit)
    RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private RecycleViewDivider mDivider;
    private NiuBiDepositAdapter mAdapter;
    private List<ProjectListResult> niuBiStyle = new ArrayList<ProjectListResult>();
    private String mOrder;
    private String mMode;
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);
        ButterKnife.bind(this);
        mMode = getIntent().getStringExtra("mode");
        title.setText(mMode +"充值");

        edit.setVisibility(View.INVISIBLE);
        mOrder = getIntent().getStringExtra("order");
        api = WXAPIFactory.createWXAPI(this,  Constants.APP_ID);
        api.registerApp(Constants.APP_ID);
        instance = this;
        initData();
        initListener();
    }

    private void initData() {
        niuBiStyle = ActivityToPay.getNiuBiStyle();
        if(niuBiStyle.size()>0){
            showData(niuBiStyle);
        }else {
            NewsInternetRequest.getNiuBiShow(new NewsInternetRequest.ForResultNewsInfoListener() {
                @Override
                public void onResponseMessage(OtherResult otherResult) {
                    showData(otherResult.niubi_style);
                }
            });
        }

    }

    private void showData(List<ProjectListResult> niuBiStyle) {
        if(mLayoutManager == null){
            mLayoutManager = new FullyLinearLayoutManager(this);
        }
        if(mAdapter == null){
            mAdapter = new NiuBiDepositAdapter(this,niuBiStyle);
        }
        if(mDivider == null){//防止多次加载出现宽度变宽
            mDivider = new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL);
            mRecyclerView.addItemDecoration(mDivider);
        }
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new NiuBiDepositAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View v, int id,String moeny) {
                if(TextUtils.isEmpty(mOrder)){
                    mOrder = "";
                }
                String code = "";
                if(mMode.equals("微信")){
                    code = "wechat";
                }else if(mMode.equals("支付宝")){
                    code = "alipay";
                }
                String info = code+ ","+SpTools.getString(CommonUtils.getContext(), Constants.userId, "0")+",";
                final String finalCode = code;
                NewsInternetRequest.sendOrderInformation(mOrder, moeny, 0 + "", 1 + "", code, info,
                        SpTools.getString(CommonUtils.getContext(), Constants.userId, "0"), new NewsInternetRequest.ForResultNewsInfoListener() {
                            @Override
                            public void onResponseMessage(final OtherResult otherResult) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityDeposit.this);
                                builder.setTitle("确认提交订单")
                                        .setPositiveButton("提交", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                confirmOrder(finalCode,otherResult.pay_morder.pmo_order,otherResult.pay_morder.pmo_id);
                                                dialog.dismiss();
                                            }
                                        })
                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).show();
                            }
                        });
            }
        });
    }

    private void initListener() {
        back.setOnClickListener(this);
    }

    private void confirmOrder(final String payType, String pmo_order, String pmo_id) {
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
                            PayTask alipay = new PayTask(ActivityDeposit.this);
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
                        SpTools.setInt(CommonUtils.getContext(),Constants.payweixin,1);//牛币购买的微信支付
                    }
                }
//                SpTools.setBoolean(ActivityDeposit.this, Constants.buy, true);
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back:
                finishAll();
                finish();
                break;
        }
    }

    private void finishAll() {

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
                        SpTools.setBoolean(ActivityDeposit.this, Constants.buy, true);
                        Toast.makeText(ActivityDeposit.this, "支付成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(ActivityDeposit.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };
    private static ActivityDeposit instance;
    public static ActivityDeposit getInstance() {
        return instance;
    }
    public void showResult(int i){
        if(i == 2){
            SpTools.setBoolean(CommonUtils.getContext(), Constants.buy, true);
            finish();
        }
    }
}
