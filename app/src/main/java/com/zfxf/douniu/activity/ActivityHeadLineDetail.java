package com.zfxf.douniu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.adapter.recycleView.HeadlineDetailAdapter;
import com.zfxf.douniu.view.FullyLinearLayoutManager;
import com.zfxf.douniu.view.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityHeadLineDetail extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.iv_base_share)
    ImageView share;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.iv_headline_detail_img)
    ImageView img;
    @BindView(R.id.rv_headline_detail)
    RecyclerView mRecyclerView;
    @BindView(R.id.ll_headline_detail_shang)
    LinearLayout ll_shang;
    @BindView(R.id.ll_headline_detail_shafa)
    LinearLayout ll_sofa;
    @BindView(R.id.tv_headline_detail_name)
    TextView name;
    @BindView(R.id.tv_headline_detail_title)
    TextView detail_title;
    @BindView(R.id.tv_headline_detail_time)
    TextView time;
    @BindView(R.id.tv_headline_detail_count)
    TextView count;
    @BindView(R.id.tv_headline_detail_count_zan)
    TextView count_zan;
    @BindView(R.id.tv_headline_detail_count_shang)
    TextView count_shang;
    @BindView(R.id.tv_headline_detail_profile)
    TextView profile;
    @BindView(R.id.tv_headline_detail_comment)
    TextView comment;
    @BindView(R.id.tv_headline_detail_commentcount)
    TextView comment_count;
    @BindView(R.id.tv_headline_detail_jianjie)
    TextView jianjie;

    private LinearLayoutManager mAdvisorManager;
    private HeadlineDetailAdapter mDetailAdapter;
    private List<String> advisorDatas = new ArrayList<String>();
    private RecycleViewDivider mDivider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_headline_detail);
        ButterKnife.bind(this);
        title.setText("头条详情");
        edit.setVisibility(View.INVISIBLE);
        share.setVisibility(View.VISIBLE);

        detail_title.getPaint().setFakeBoldText(true);//加粗
        jianjie.getPaint().setFakeBoldText(true);//加粗
        comment.getPaint().setFakeBoldText(true);//加粗
        initData();
        initListener();
    }

    private void initData() {
        ll_sofa.setVisibility(View.GONE);
        if (advisorDatas.size() == 0) {
            advisorDatas.add("");
            advisorDatas.add("");
            advisorDatas.add("");
            advisorDatas.add("");
            advisorDatas.add("");
            advisorDatas.add("");
            advisorDatas.add("");
        }
        if(mAdvisorManager == null){
            mAdvisorManager = new FullyLinearLayoutManager(this);
        }
        if(mDetailAdapter == null){
            mDetailAdapter = new HeadlineDetailAdapter(this, advisorDatas);
        }
        mRecyclerView.setLayoutManager(mAdvisorManager);
        mRecyclerView.setAdapter(mDetailAdapter);
        if(mDivider == null){//防止多次加载出现宽度变宽
            mDivider = new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL);
            mRecyclerView.addItemDecoration(mDivider);
        }
    }
    private void initListener() {
        back.setOnClickListener(this);
        share.setOnClickListener(this);
        ll_shang.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back:
                finishAll();
                finish();
                break;
            case R.id.iv_base_share:

                break;
            case R.id.ll_headline_detail_shang:
                Intent intent = new Intent(this,ActivityReward.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                break;
        }
    }

    private void finishAll() {

    }

}
