package com.zfxf.douniu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.adapter.recycleView.ZhenguAdvisorAdapter;
import com.zfxf.douniu.adapter.recycleView.ZhenguAnswerAdapter;
import com.zfxf.douniu.bean.AnswerListInfo;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.view.FullyGridLayoutManager;
import com.zfxf.douniu.view.FullyLinearLayoutManager;
import com.zfxf.douniu.view.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityZhengu extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;


    @BindView(R.id.rl_zhengu_advisor_detail)
    RelativeLayout advisor_detail;
    @BindView(R.id.rl_zhengu_answer_detail)
    RelativeLayout answer_detail;
    @BindView(R.id.iv_zhengu_ask)
    ImageView ask;
    @BindView(R.id.tv_zhengu_text)
    TextView text_advisor;
    @BindView(R.id.tv_zhengu_answer)
    TextView text_answer;

    @BindView(R.id.rv_zhengu_advisor)
    RecyclerView mAdvisorRecyclerView;//在线首席recycleview
    private LinearLayoutManager mAdvisorManager;
    private ZhenguAdvisorAdapter mAdvisorAdapter;
    private List<String> advisorDatas = new ArrayList<String>();

    @BindView(R.id.rv_zhengu_answer)
    RecyclerView mAnswerRecyclerView;//精彩回答recycleview
    private LinearLayoutManager mAnswerManager;
    private ZhenguAnswerAdapter mAnswerAdapter;
    private List<String> answerDatas = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhengu);
        ButterKnife.bind(this);
        text_advisor.getPaint().setFakeBoldText(true);//加粗
        text_answer.getPaint().setFakeBoldText(true);//加粗
        title.setText("微问答");
        edit.setVisibility(View.INVISIBLE);
        initdata();
        initListener();
    }

    private void initdata() {
        /**
         * 在线首席
         */
        if (advisorDatas.size() == 0) {
            advisorDatas.add("百变股神");
            advisorDatas.add("孙悟空");
            advisorDatas.add("柯南");
            advisorDatas.add("小魔女");
        }
        if(mAdvisorManager == null){
            mAdvisorManager = new FullyGridLayoutManager(this,2);
        }
        if(mAdvisorAdapter == null){
//            mAdvisorAdapter = new ZhenguAdvisorAdapter(this, advisorDatas);
        }
        mAdvisorRecyclerView.setLayoutManager(mAdvisorManager);
        mAdvisorRecyclerView.setAdapter(mAdvisorAdapter);

        /**
         * 精彩回答
         */
        if (answerDatas.size() == 0) {
            answerDatas.add("1");
            answerDatas.add("2");
            answerDatas.add("3");
            answerDatas.add("4");
            answerDatas.add("5");
        }
        if(mAnswerManager == null){
            mAnswerManager = new FullyLinearLayoutManager(this);
        }
        if(mAnswerAdapter == null){
//            mAnswerAdapter = new ZhenguAnswerAdapter(this, answerDatas);
        }
        mAnswerRecyclerView.setLayoutManager(mAnswerManager);
        mAnswerRecyclerView.setAdapter(mAnswerAdapter);
        mAnswerRecyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL));

    }
    private void initListener() {
        back.setOnClickListener(this);
        advisor_detail.setOnClickListener(this);
        answer_detail.setOnClickListener(this);
        ask.setOnClickListener(this);
        mAdvisorAdapter.setOnItemClickListener(new ZhenguAdvisorAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View v, int positon) {
                Intent intent = new Intent(CommonUtils.getContext(), ActivityAskStock.class);
                intent.putExtra("name",advisorDatas.get(positon).toString());
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });
        mAnswerAdapter.setOnItemClickListener(new ZhenguAnswerAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View v, int positon,AnswerListInfo bean) {
                Intent intent = new Intent(CommonUtils.getContext(), ActivityAnswerDetail.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.iv_base_back:
                finishAll();
                finish();
                break;
            case R.id.rl_zhengu_advisor_detail:
                intent = new Intent(this, ActivityAskAdvisor.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                break;
            case R.id.rl_zhengu_answer_detail:
                intent = new Intent(this, ActivityAnswer.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                break;
            case R.id.iv_zhengu_ask:
                intent = new Intent(this, ActivityAskStock.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                break;
        }
    }

    private void finishAll() {
        mAdvisorManager = null;
        mAdvisorAdapter = null;
    }
}
