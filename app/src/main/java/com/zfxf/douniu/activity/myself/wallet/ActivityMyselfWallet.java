package com.zfxf.douniu.activity.myself.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.adapter.recycleView.WalletNoticeAdapter;
import com.zfxf.douniu.bean.OtherResult;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.SpTools;
import com.zfxf.douniu.view.FullyLinearLayoutManager;
import com.zfxf.douniu.view.RecycleViewDivider;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author IMXU
 * @time   2017/5/3 13:20
 * @des    我的钱包
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class ActivityMyselfWallet extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.tv_myself_wallet)
    TextView count;
    @BindView(R.id.tv_myself_wallet_buy)
    TextView buy;
    @BindView(R.id.rv_myself_wallet)
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    RecycleViewDivider mDivider;
    WalletNoticeAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myself_wallet);
        ButterKnife.bind(this);

        title.setText("我的钱包");
        edit.setVisibility(View.INVISIBLE);

        initdata();
        initListener();
    }

    private void initdata() {
        visitInternet();
    }

    private void visitInternet() {
        CommonUtils.showProgressDialog(this,"加载中……");
        NewsInternetRequest.getWalletInformation(new NewsInternetRequest.ForResultNewsInfoListener() {
            @Override
            public void onResponseMessage(OtherResult otherResult) {
                if(mLayoutManager == null){
                    mLayoutManager = new FullyLinearLayoutManager(ActivityMyselfWallet.this);
                }
                if(mAdapter == null){
                    mAdapter = new WalletNoticeAdapter(ActivityMyselfWallet.this,otherResult.hq_nb);
                }
                if(mDivider == null){//防止多次加载出现宽度变宽
                    mDivider = new RecycleViewDivider(ActivityMyselfWallet.this, LinearLayoutManager.HORIZONTAL);
                    mRecyclerView.addItemDecoration(mDivider);
                }
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAdapter);
                String wallet = otherResult.my_wallet;
                if(TextUtils.isEmpty(wallet)){
                    count.setText("0牛币");
                }else {
                    count.setText(wallet +"牛币");
                }
                CommonUtils.dismissProgressDialog();
            }
        });
    }

    private void initListener() {
        back.setOnClickListener(this);
        buy.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back:
                finishAll();
                finish();
                break;
            case R.id.ll_myself_disclaimer:
                break;
            case R.id.tv_myself_wallet_buy:
                Intent intent = new Intent(this,ActivityBuyNiu.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                break;
        }
    }

    private void finishAll() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CommonUtils.dismissProgressDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(SpTools.getBoolean(this, Constants.buy,false)){//如果已经支付成功，重新刷新数据
            SpTools.setBoolean(this, Constants.buy,false);
            visitInternet();
        }
    }
}
