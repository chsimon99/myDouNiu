package com.zfxf.douniu.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
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

public class ActivityMatador extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.tv_matador_name)
    TextView tv_name;
    @BindView(R.id.tv_matador_fans)
    TextView tv_fans;
    @BindView(R.id.tv_matador_type)
    TextView tv_type;
    @BindView(R.id.tv_matador_follow)
    TextView tv_follow;//关注
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
    RelativeLayout rl_more;//更多

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
        setContentView(R.layout.activity_matador);
        ButterKnife.bind(this);
        title.setText("斗牛士主页");
        edit.setVisibility(View.INVISIBLE);
        tv_follow.getPaint().setFakeBoldText(true);//加粗
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

}
