package com.zfxf.douniu.activity.bar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zfxf.douniu.R;
import com.zfxf.douniu.adapter.recycleView.MatadorAdapter;
import com.zfxf.douniu.bean.IndexAdvisorListInfo;
import com.zfxf.douniu.bean.MatadorResult;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.view.RecycleViewDivider;
import com.zfxf.douniu.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * @author IMXU
 * @time   2017/5/3 13:26
 * @des    斗牛士排行榜
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class ActivityMatadorRank extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.iv_matador_first_img)
    ImageView iv_first;
    @BindView(R.id.tv_matador_first_name)
    TextView tv_first_name;
    @BindView(R.id.tv_matador_first_income)
    TextView tv_first_income;
    @BindView(R.id.iv_matador_second_img)
    ImageView iv_second;
    @BindView(R.id.tv_matador_second_name)
    TextView tv_second_name;
    @BindView(R.id.tv_matador_second_income)
    TextView tv_second_income;
    @BindView(R.id.iv_matador_third_img)
    ImageView iv_third;
    @BindView(R.id.tv_matador_third_name)
    TextView tv_third_name;
    @BindView(R.id.tv_matador_third_income)
    TextView tv_third_income;

    @BindView(R.id.rl_matador_second)
    RelativeLayout rl_second;
    @BindView(R.id.rl_matador_first)
    RelativeLayout rl_first;
    @BindView(R.id.rl_matador_third)
    RelativeLayout rl_third;

    @BindView(R.id.rv_matador_rank)
    PullLoadMoreRecyclerView mRecyclerView;
    private MatadorAdapter mMatadorAdapter;
    private RecycleViewDivider mDivider;

    private int totlePage = 0;
    private int currentPage = 1;
    private Context mContext;
    private String first_id;
    private String second_id;
    private String third_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matador_rank);
        ButterKnife.bind(this);
        title.setText("斗牛士排行");
        edit.setVisibility(View.INVISIBLE);
        mContext = this;
        initdata();
        initListener();
    }

    private void initdata() {
        CommonUtils.showProgressDialog(this,"加载中……");
        visitInternet();
    }
    private void initListener() {
        back.setOnClickListener(this);
        edit.setOnClickListener(this);
        rl_first.setOnClickListener(this);
        rl_second.setOnClickListener(this);
        rl_third.setOnClickListener(this);
        mRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                if(currentPage > totlePage){
                    Toast.makeText(CommonUtils.getContext(),"没有数据了",Toast.LENGTH_SHORT).show();
                    mRecyclerView.postDelayed(new Runnable() {//防止滑动过快，loading界面显示太快
                        @Override
                        public void run() {
                            mRecyclerView.setPullLoadMoreCompleted();
                        }
                    },200);
                    return;
                }
                visitInternet();
            }
        });
    }

    private void visitInternet() {
        NewsInternetRequest.getMatadorListInformation(4, currentPage,new NewsInternetRequest.ForResultMatadorIndexListener() {
            @Override
            public void onResponseMessage(MatadorResult indexResult) {
                totlePage = Integer.parseInt(indexResult.total);
                if (totlePage > 0 && currentPage <= totlePage){
                    if(currentPage == 1){
                        List<IndexAdvisorListInfo> user_list = indexResult.user_list;

                        String picUrl1 = getResources().getString(R.string.file_host_address)
                                +getResources().getString(R.string.showpic)
                                +user_list.get(0).headImg;
                        Glide.with(mContext).load(picUrl1)
                                .placeholder(R.drawable.home_adviosr_img)
                                .bitmapTransform(new CropCircleTransformation(mContext)).into(iv_first);
                        tv_first_name.setText(user_list.get(0).ud_nickname);
                        tv_first_income.setText("收益:"+user_list.get(0).mf_bysy+"%");
                        first_id = user_list.get(0).ud_ub_id;

                        String picUrl2 = getResources().getString(R.string.file_host_address)
                                +getResources().getString(R.string.showpic)
                                +user_list.get(1).headImg;
                        Glide.with(mContext).load(picUrl2)
                                .placeholder(R.drawable.home_adviosr_img)
                                .bitmapTransform(new CropCircleTransformation(mContext)).into(iv_second);
                        tv_second_name.setText(user_list.get(1).ud_nickname);
                        tv_second_income.setText("收益:"+user_list.get(0).mf_bysy+"%");
                        second_id = user_list.get(1).ud_ub_id;

                        String picUrl3 = getResources().getString(R.string.file_host_address)
                                +getResources().getString(R.string.showpic)
                                +user_list.get(2).headImg;
                        Glide.with(mContext).load(picUrl3)
                                .placeholder(R.drawable.home_adviosr_img)
                                .bitmapTransform(new CropCircleTransformation(mContext)).into(iv_third);
                        tv_third_name.setText(user_list.get(2).ud_nickname);
                        tv_third_income.setText("收益:"+user_list.get(0).mf_bysy+"%");
                        third_id = user_list.get(2).ud_ub_id;

                        if(mMatadorAdapter == null){
                            mMatadorAdapter = new MatadorAdapter(ActivityMatadorRank.this,user_list.subList(3,user_list.size()));
                        }
                        mRecyclerView.setLinearLayout();
                        mRecyclerView.setAdapter(mMatadorAdapter);
                        if(mDivider == null){
                            mDivider = new RecycleViewDivider(ActivityMatadorRank.this, LinearLayoutManager.HORIZONTAL);
                            mRecyclerView.addItemDecoration(mDivider);
                        }
                        mRecyclerView.setPullRefreshEnable(false);//下拉刷新

                        mMatadorAdapter.setOnItemClickListener(new MatadorAdapter.MyItemClickListener() {
                            @Override
                            public void onItemClick(View v, int id) {
                                Intent intent = new Intent(ActivityMatadorRank.this, ActivityMatador.class);
                                intent.putExtra("id",id+"");
                                startActivity(intent);
                                overridePendingTransition(0,0);
                            }
                        });
                    }else {
                        mMatadorAdapter.addDatas(indexResult.user_list);

                        mRecyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                mMatadorAdapter.notifyDataSetChanged();
                            }
                        });
                        mRecyclerView.postDelayed(new Runnable() {//防止滑动过快，loading界面显示太快
                            @Override
                            public void run() {
                                mRecyclerView.setPullLoadMoreCompleted();
                            }
                        },1000);
                    }
                    currentPage++;
                    CommonUtils.dismissProgressDialog();
                }else {
                    CommonUtils.dismissProgressDialog();
                    return;
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back:
                finishAll();
                finish();
                break;
            case R.id.iv_base_edit:

                break;
            case R.id.rl_matador_first:
                if(!TextUtils.isEmpty(first_id)){
                    Intent intent = new Intent(ActivityMatadorRank.this, ActivityMatador.class);
                    intent.putExtra("id",first_id);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                }
                break;
            case R.id.rl_matador_second:
                if(!TextUtils.isEmpty(second_id)){
                    Intent intent = new Intent(ActivityMatadorRank.this, ActivityMatador.class);
                    intent.putExtra("id",second_id);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                }
                break;
            case R.id.rl_matador_third:
                if(!TextUtils.isEmpty(third_id)){
                    Intent intent = new Intent(ActivityMatadorRank.this, ActivityMatador.class);
                    intent.putExtra("id",third_id);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                }
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
