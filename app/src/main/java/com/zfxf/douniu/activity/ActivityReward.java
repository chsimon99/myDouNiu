package com.zfxf.douniu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.adapter.recycleView.RewardAdapter;
import com.zfxf.douniu.view.FullyGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityReward extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.tv_reward_title)
    TextView tv_title;
    @BindView(R.id.tv_reward_name)
    TextView name;
    @BindView(R.id.tv_reward_count)
    TextView count;
    @BindView(R.id.tv_reward_confirm)
    TextView confirm;
    @BindView(R.id.et_reward_money)
    EditText money;
    @BindView(R.id.rl_reward_confirm)
    RelativeLayout rl_confirm;
    @BindView(R.id.iv_reward_img)
    ImageView img;
    @BindView(R.id.rv_reward)
    RecyclerView mRecyclerView;

    private LinearLayoutManager mAdvisorManager;
    private RewardAdapter mRewardAdapter;
    private List<String> mDatas = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);
        ButterKnife.bind(this);
        title.setText("打赏");
        edit.setVisibility(View.INVISIBLE);
        confirm.getPaint().setFakeBoldText(true);//加粗
        initData();
        initListener();
    }

    private void initData() {
        if (mDatas.size() == 0) {
            mDatas.add("6");
            mDatas.add("8");
            mDatas.add("16");
            mDatas.add("18");
            mDatas.add("66");
            mDatas.add("88");
        }
        if(mAdvisorManager == null){
            mAdvisorManager = new FullyGridLayoutManager(this,3);
        }
        if(mRewardAdapter == null){
            mRewardAdapter = new RewardAdapter(this, mDatas);
        }
        mRecyclerView.setLayoutManager(mAdvisorManager);
        mRecyclerView.setAdapter(mRewardAdapter);
        mRewardAdapter.setOnItemClickListener(new RewardAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View v, int positon) {
                money.setText(mDatas.get(positon));
            }
        });

    }
    private void initListener() {
        back.setOnClickListener(this);
        rl_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back:
                finishAll();
                finish();
                break;
            case R.id.rl_reward_confirm:
                Intent intent = new Intent(this,ActivityToPay.class);
                intent.putExtra("type","观点打赏");
                intent.putExtra("count",money.getText().toString());
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
                break;
        }
    }

    private void finishAll() {

    }

}
