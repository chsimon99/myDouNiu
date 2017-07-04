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

import com.bumptech.glide.Glide;
import com.zfxf.douniu.R;
import com.zfxf.douniu.adapter.recycleView.RewardAdapter;
import com.zfxf.douniu.bean.AnswerListInfo;
import com.zfxf.douniu.bean.IndexResult;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.view.FullyGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

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
    private int mId;
    private String nickname;
    private String mSx_id;
    private String dashangType;
    private int rewardType = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);
        ButterKnife.bind(this);
        title.setText("打赏");
        edit.setVisibility(View.INVISIBLE);
        confirm.getPaint().setFakeBoldText(true);//加粗
        //直播的id
        mId = getIntent().getIntExtra("id", 0);
        mSx_id = getIntent().getStringExtra("sx_id");
        dashangType = getIntent().getStringExtra("type");
        if(dashangType.equals("直播")){
            rewardType = 0;
        }else if(dashangType.equals("头条")){
            rewardType = 1;
        }
        initData();
        initListener();
    }

    private void initData() {
        visitInternet();
    }

    private void visitInternet() {
        CommonUtils.showProgressDialog(this,"加载中……");
        NewsInternetRequest.toRewardInformation(mId, rewardType,new NewsInternetRequest.ForResultIndexListener() {
            @Override
            public void onResponseMessage(IndexResult indexResult) {
                if(mAdvisorManager == null){
                    mAdvisorManager = new FullyGridLayoutManager(ActivityReward.this,3);
                }
                List<AnswerListInfo> jiageList = indexResult.jiage_list;
                if (mDatas.size() > 0) {
                    mDatas.clear();
                }
                for(int i = 0;i<jiageList.size();i++){
                    mDatas.add(jiageList.get(i).price+"");
                }
                if(mRewardAdapter == null){
                    mRewardAdapter = new RewardAdapter(ActivityReward.this, mDatas);
                }
                mRecyclerView.setLayoutManager(mAdvisorManager);
                mRecyclerView.setAdapter(mRewardAdapter);
                mRewardAdapter.setOnItemClickListener(new RewardAdapter.MyItemClickListener() {
                    @Override
                    public void onItemClick(View v, int positon) {
                        money.setText(mDatas.get(positon));
                    }
                });

                Glide.with(ActivityReward.this).load(indexResult.context_info.headImg)
                        .placeholder(R.drawable.home_adviosr_img)
                        .bitmapTransform(new CropCircleTransformation(ActivityReward.this))
                        .into(img);
                tv_title.setText(indexResult.context_info.title);
                nickname = indexResult.context_info.nickname;
                name.setText(nickname);
                count.setText(indexResult.context_info.ds_count+"人打赏");
                CommonUtils.dismissProgressDialog();
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
                if(dashangType.equals("直播")){
                    intent.putExtra("info","直播打赏,"+mSx_id+","+mId);
                }else if(dashangType.equals("头条")){
                    intent.putExtra("info","头条打赏,"+mSx_id+","+mId);
                }
                intent.putExtra("type","打赏");
                intent.putExtra("from",nickname);
                intent.putExtra("count",money.getText().toString());
                intent.putExtra("sx_id",mSx_id);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
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
}
