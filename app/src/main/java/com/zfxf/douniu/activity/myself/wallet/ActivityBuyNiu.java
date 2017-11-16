package com.zfxf.douniu.activity.myself.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.pay.ActivityDeposit;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityBuyNiu extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.ll_buy_niu_zfb)
    LinearLayout zfb;
    @BindView(R.id.ll_buy_niu_wx)
    LinearLayout wx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_niu);
        ButterKnife.bind(this);
        title.setText("充值");
        edit.setVisibility(View.INVISIBLE);
        initData();
        initListener();
    }

    private void initData() {


    }
    private void initListener() {
        back.setOnClickListener(this);
        zfb.setOnClickListener(this);
        wx.setOnClickListener(this);
    }
    Intent intent;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back:
                break;
            case R.id.ll_buy_niu_wx:
                intent = new Intent(this,ActivityDeposit.class);
                intent.putExtra("mode","微信");
                startActivity(intent);
                overridePendingTransition(0,0);
                break;
            case R.id.ll_buy_niu_zfb:
                intent = new Intent(this,ActivityDeposit.class);
                intent.putExtra("mode","支付宝");
                startActivity(intent);
                overridePendingTransition(0,0);
                break;
        }
        intent = null;
        finishAll();
        finish();
    }

    private void finishAll() {

    }

}
