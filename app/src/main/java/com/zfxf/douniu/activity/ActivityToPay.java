package com.zfxf.douniu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.SpTools;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topay);
        ButterKnife.bind(this);
        title.setText("提交订单");
        edit.setVisibility(View.INVISIBLE);
        String pay_type = getIntent().getStringExtra("type");
        String count = getIntent().getStringExtra("count");
        String from = getIntent().getStringExtra("from");
        money.setText("￥"+count);
        type.setText(pay_type);
        towho.setText(from);
        initData();
        initListener();
    }

    private void initData() {
        yu_e.setVisibility(View.VISIBLE);
        yu_e_pay.setVisibility(View.VISIBLE);
        v_yu_e.setVisibility(View.GONE);
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
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back:
                finishAll();
                finish();
                break;
            case R.id.rl_topay_pay://暂时是直接支付成功
                SpTools.setBoolean(this, Constants.buy,true);
                finish();
                break;
            case R.id.ll_topay_niubi:
                reset();
                niu_select.setVisibility(View.VISIBLE);
                niu_noselect.setVisibility(View.INVISIBLE);
                break;
            case R.id.ll_topay_wx:
                reset();
                wx_select.setVisibility(View.VISIBLE);
                wx_noselect.setVisibility(View.INVISIBLE);
                break;
            case R.id.ll_topay_zfb:
                reset();
                zfb_select.setVisibility(View.VISIBLE);
                zfb_noselect.setVisibility(View.INVISIBLE);
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
}