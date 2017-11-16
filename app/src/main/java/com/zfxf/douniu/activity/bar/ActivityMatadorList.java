package com.zfxf.douniu.activity.bar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.login.ActivityLogin;
import com.zfxf.douniu.adapter.recycleView.MatadorAdapter;
import com.zfxf.douniu.bean.MatadorResult;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.SpTools;
import com.zfxf.douniu.view.RecycleViewDivider;
import com.zfxf.douniu.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author IMXU
 * @time   2017/5/3 13:38
 * @des    斗牛士列表
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class ActivityMatadorList extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.rv_matador_list)
    PullLoadMoreRecyclerView mRecyclerView;
    private MatadorAdapter mAttentionAdvisorAdapter;
    private RecycleViewDivider mDivider;

    private int mId;
    private int totlePage = 0;
    private int currentPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matador_list);
        ButterKnife.bind(this);
        title.setText("斗牛士列表");
        edit.setVisibility(View.INVISIBLE);
        mId = getIntent().getIntExtra("id", 0);
        initData();
        initListener();
    }

    private void initData() {
        CommonUtils.showProgressDialog(this,"加载中……");
        visitInternet();
    }

    private void visitInternet() {
        NewsInternetRequest.getMatadorListInformation(mId, currentPage,new NewsInternetRequest.ForResultMatadorIndexListener() {
            @Override
            public void onResponseMessage(MatadorResult indexResult) {
                totlePage = Integer.parseInt(indexResult.total);
                if (totlePage > 0 && currentPage <= totlePage){
                    if(currentPage == 1){
                        if(mAttentionAdvisorAdapter == null){
                            mAttentionAdvisorAdapter = new MatadorAdapter(ActivityMatadorList.this,indexResult.user_list);
                        }
                        mRecyclerView.setLinearLayout();
                        mRecyclerView.setAdapter(mAttentionAdvisorAdapter);
                        if(mDivider == null){
                            mDivider = new RecycleViewDivider(ActivityMatadorList.this, LinearLayoutManager.HORIZONTAL);
                            mRecyclerView.addItemDecoration(mDivider);
                        }
                        mRecyclerView.setPullRefreshEnable(false);//下拉刷新

                        mAttentionAdvisorAdapter.setOnItemClickListener(new MatadorAdapter.MyItemClickListener() {
                            @Override
                            public void onItemClick(View v, int id) {
                                if(!SpTools.getBoolean(CommonUtils.getContext(), Constants.isLogin,false)){
                                    Intent intent = new Intent(ActivityMatadorList.this, ActivityLogin.class);
                                    startActivity(intent);
                                    overridePendingTransition(0,0);
                                    return;
                                }
                                Intent intent = new Intent(ActivityMatadorList.this, ActivityMatador.class);
                                intent.putExtra("id",id+"");
                                startActivity(intent);
                                overridePendingTransition(0,0);
                            }
                        });
                    }else {
                        mAttentionAdvisorAdapter.addDatas(indexResult.user_list);

                        mRecyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                mAttentionAdvisorAdapter.notifyDataSetChanged();
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

    private void initListener() {
        back.setOnClickListener(this);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CommonUtils.dismissProgressDialog();
    }
}
