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
import com.zfxf.douniu.adapter.recycleView.MatadorPositonAdapter;
import com.zfxf.douniu.view.FullyLinearLayoutManager;
import com.zfxf.douniu.view.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * @author IMXU
 * @time   2017/5/3 13:30
 * @des    首页模拟炒股
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class ActivitySimulation extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.iv_base_question)
    ImageView question;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.tv_matador_name)
    TextView tv_name;
    @BindView(R.id.tv_matador_fans)
    TextView tv_fans;
    @BindView(R.id.tv_matador_type)
    TextView tv_type;
    @BindView(R.id.tv_matador_introduce)
    TextView tv_introduce;
    @BindView(R.id.tv_matador_rank)
    TextView tv_rank;//排名
    @BindView(R.id.tv_matador_position)
    TextView tv_postion;//当前持仓

    @BindView(R.id.iv_matador_img)
    ImageView img;
    @BindView(R.id.iv_matador_type)
    ImageView type;//上升图片
    @BindView(R.id.iv_matador_start_three)
    ImageView start_three;
    @BindView(R.id.iv_matador_start_four)
    ImageView start_four;
    @BindView(R.id.iv_matador_startfive)
    ImageView start_five;

    @BindView(R.id.rl_matador_more)
    RelativeLayout rl_more;
    @BindView(R.id.rl_matador_edit)
    RelativeLayout rl_edit;//编辑

    @BindView(R.id.ll_matador_buy)
    LinearLayout ll_buy;
    @BindView(R.id.ll_matador_sold)
    LinearLayout ll_sold;
    @BindView(R.id.ll_matador_entrust)
    LinearLayout ll_entrust;
    @BindView(R.id.ll_matador_query)
    LinearLayout ll_query;

    @BindView(R.id.tv_matador_mes_asset)
    TextView asset;
    @BindView(R.id.tv_matador_mes_ratio)
    TextView ratio;//仓位
    @BindView(R.id.tv_matador_mes_month_trade)
    TextView trade;//月交易
    @BindView(R.id.tv_matador_mes_total_income)
    TextView total_income;
    @BindView(R.id.tv_matador_mes_month_income)
    TextView month_income;
    @BindView(R.id.tv_matador_mes_week_income)
    TextView week_income;

    @BindView(R.id.rv_matador_detail)
    RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MatadorPositonAdapter mPositonAdapter;
    private List<String> mStrings = new ArrayList<String>();
    private RecycleViewDivider mDivider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulation);
        ButterKnife.bind(this);
        title.setText("模拟炒股");
        edit.setVisibility(View.INVISIBLE);
        question.setVisibility(View.VISIBLE);
        asset.getPaint().setFakeBoldText(true);//加粗
        ratio.getPaint().setFakeBoldText(true);//加粗
        trade.getPaint().setFakeBoldText(true);//加粗
        total_income.getPaint().setFakeBoldText(true);//加粗
        month_income.getPaint().setFakeBoldText(true);//加粗
        week_income.getPaint().setFakeBoldText(true);//加粗
        tv_postion.getPaint().setFakeBoldText(true);//加粗

        initData();
        initListener();
    }

    private void initData() {
        if (mStrings.size() == 0) {
            mStrings.add("");
            mStrings.add("");
            mStrings.add("");
            mStrings.add("");
        }
        if(mLayoutManager == null){
            mLayoutManager = new FullyLinearLayoutManager(this);
        }
        if(mPositonAdapter == null){
            mPositonAdapter = new MatadorPositonAdapter(this, mStrings);
        }
        if(mDivider == null){//防止多次加载出现宽度变宽
            mDivider = new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL);
            mRecyclerView.addItemDecoration(mDivider);
        }
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mPositonAdapter);

    }
    private void initListener() {
        back.setOnClickListener(this);
        question.setOnClickListener(this);
        ll_buy.setOnClickListener(this);
        ll_sold.setOnClickListener(this);
        ll_entrust.setOnClickListener(this);
        ll_query.setOnClickListener(this);
    }
    Intent mIntent;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back:
                finishAll();
                finish();
                break;
            case R.id.ll_matador_buy:
                mIntent = new Intent(this,ActivitySimulationStock.class);
                setIndex(2);
                startActivity(mIntent);
                overridePendingTransition(0,0);
                break;
            case R.id.ll_matador_sold:
                mIntent = new Intent(this,ActivitySimulationStock.class);
                setIndex(3);
                startActivity(mIntent);
                overridePendingTransition(0,0);
                break;
            case R.id.ll_matador_entrust:
                mIntent = new Intent(this,ActivitySimulationStock.class);
                setIndex(4);
                startActivity(mIntent);
                overridePendingTransition(0,0);
                break;
            case R.id.ll_matador_query:
                mIntent = new Intent(this,ActivitySimulationStock.class);
                setIndex(5);
                startActivity(mIntent);
                overridePendingTransition(0,0);
                break;
        }
    }

    private void finishAll() {

    }
    static int index;
    public static void setIndex(int num){
        index = num;
    }
    public static int getIndex(){
        return index;
    }
}
