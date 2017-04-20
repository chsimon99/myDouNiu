package com.zfxf.douniu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.adapter.recycleView.AdvisorAllGoldPondHistoryAdapter;
import com.zfxf.douniu.view.FullyLinearLayoutManager;
import com.zfxf.douniu.view.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityGoldPond extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.iv_base_share)
    ImageView share;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.tv_gold_pond_detail_name)
    TextView tv_name;//姓名
    @BindView(R.id.tv_gold_pond_detail_count)
    TextView tv_count;//股池股票数量
    @BindView(R.id.tv_gold_pond_detail_money)
    TextView tv_money;//费用
    @BindView(R.id.tv_gold_pond_detail_people)
    TextView tv_people;//适用人群
    @BindView(R.id.tv_gold_pond_detail_subscribe)
    TextView tv_subscribe;//订阅人数
    @BindView(R.id.tv_gold_pond_detail_time)
    TextView tv_time;//发布周期
    @BindView(R.id.tv_gold_pond_detail_brief)
    TextView tv_brief;//风险提示
    @BindView(R.id.tv_gold_pond_detail_confirm)
    TextView tv_confirm;//确认支付

    @BindView(R.id.rl_gold_pond_detail_history)
    RelativeLayout rl_history;//历史战绩
    @BindView(R.id.rl_gold_pond_detail_stock)
    RelativeLayout rl_stock;//入选个股
    @BindView(R.id.ll_gold_pond_detail)
    LinearLayout pay_info;//购买提示信息(与分割线互斥)
    @BindView(R.id.v_gold_pond_detail)
    View pay_info_view;//购买提示信息分割线

    @BindView(R.id.rv_gold_pond_detail_history)
    RecyclerView rv_history;//历史战绩
    private LinearLayoutManager mHistoryManager;
    private AdvisorAllGoldPondHistoryAdapter mGoldPondHistoryAdapter;
    @BindView(R.id.rv_gold_pond_detail_stock)
    RecyclerView rv_stock;//股池
    private LinearLayoutManager mChooseManager;
    private AdvisorAllGoldPondHistoryAdapter mGoldPondStockAdapter;

    private List<String> advisorDatas = new ArrayList<String>();
    private List<String> chooseDatas = new ArrayList<String>();
    private RecycleViewDivider mHistoryDivider;
    private RecycleViewDivider mStockDivider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gold_pond);
        ButterKnife.bind(this);
        title.setText("金股池详情");
        share.setVisibility(View.VISIBLE);
        edit.setVisibility(View.INVISIBLE);
        tv_name.getPaint().setFakeBoldText(true);//加粗
        tv_money.getPaint().setFakeBoldText(true);//加粗
        tv_confirm.getPaint().setFakeBoldText(true);//加粗

        rv_stock.setVisibility(View.GONE);
        pay_info_view.setVisibility(View.GONE);

        initData();
        initListener();
    }

    private void initData() {
        if (advisorDatas.size() == 0) {
            advisorDatas.add("");
            advisorDatas.add("");
        }
        if(mHistoryManager == null){
            mHistoryManager = new FullyLinearLayoutManager(this);
        }
        if(mGoldPondHistoryAdapter == null){
            mGoldPondHistoryAdapter = new AdvisorAllGoldPondHistoryAdapter(this, advisorDatas);
        }

        if(chooseDatas.size() == 0){
            chooseDatas.add("");
            chooseDatas.add("");
            chooseDatas.add("");
        }
        if(mChooseManager == null){
            mChooseManager = new FullyLinearLayoutManager(this);
        }
        if(mGoldPondStockAdapter == null){
            mGoldPondStockAdapter = new AdvisorAllGoldPondHistoryAdapter(this, chooseDatas);
        }
        rv_history.setLayoutManager(mHistoryManager);
        rv_history.setAdapter(mGoldPondHistoryAdapter);
        if(mHistoryDivider == null){//防止多次加载出现宽度变宽
            mHistoryDivider = new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL);
            rv_history.addItemDecoration(mHistoryDivider);
        }
        rv_stock.setLayoutManager(mChooseManager);
        rv_stock.setAdapter(mGoldPondStockAdapter);
        if(mStockDivider == null){//防止多次加载出现宽度变宽
            mStockDivider = new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL);
            rv_stock.addItemDecoration(mStockDivider);
        }
    }
    private void initListener() {
        back.setOnClickListener(this);
        share.setOnClickListener(this);
        rl_history.setOnClickListener(this);
        rl_stock.setOnClickListener(this);
    }
    boolean isBuy = false;//后期放在服务器上获取是否购买信息
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back:
                finishAll();
                finish();
                break;
            case R.id.iv_base_share:

                break;
            case R.id.rl_gold_pond_detail_history:
                Intent intent = new Intent(this,Activityhistory.class);
                startActivity(intent);
                overridePendingTransition(0,0);

                break;
            case R.id.rl_gold_pond_detail_stock:
                if(!isBuy){
                    isBuy = !isBuy;
                    pay_info.setVisibility(View.INVISIBLE);
                    rv_stock.setVisibility(View.VISIBLE);
                    pay_info_view.setVisibility(View.VISIBLE);

                }else{
                    isBuy = !isBuy;
                    pay_info.setVisibility(View.VISIBLE);
                    rv_stock.setVisibility(View.GONE);
                    pay_info_view.setVisibility(View.GONE);

                }
                break;
        }
    }

    private void finishAll() {

    }

}
