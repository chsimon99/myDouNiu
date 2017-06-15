package com.zfxf.douniu.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zfxf.douniu.R;
import com.zfxf.douniu.adapter.recycleView.MarketMarketAdapter;
import com.zfxf.douniu.bean.SimulationResult;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.view.RecycleViewDivider;
import com.zfxf.douniu.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author IMXU
 * @time   2017/5/3 13:38
 * @des    历史战绩
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class ActivityStockList extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.rv_stockr_list)
    PullLoadMoreRecyclerView mRecyclerView;
    private MarketMarketAdapter mAttentionAdvisorAdapter;
    private RecycleViewDivider mDivider;

    private int totlePage = 0;
    private int currentPage = 1;
    private int mStatus;
    private int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_list);
        ButterKnife.bind(this);
        title.setText("涨幅榜");
        edit.setVisibility(View.INVISIBLE);
        mStatus = getIntent().getIntExtra("status", -1);
        initData();
        initListener();
    }

    private void initData() {
        CommonUtils.showProgressDialog(this,"加载中……");
        visitInternet();
    }

    private void visitInternet() {
        NewsInternetRequest.getStockListInformation(currentPage,type,mStatus,new NewsInternetRequest.ForResultZiXuanStockListener() {
            @Override
            public void onResponseMessage(SimulationResult indexResult) {
                totlePage = Integer.parseInt(indexResult.total);
                if (totlePage > 0 && currentPage <= totlePage){
                    if(currentPage == 1){
                        if(mAttentionAdvisorAdapter == null){
                            mAttentionAdvisorAdapter = new MarketMarketAdapter(ActivityStockList.this,indexResult.zhang_gupiao);
                        }
                        mRecyclerView.setLinearLayout();
                        mRecyclerView.setAdapter(mAttentionAdvisorAdapter);
                        if(mDivider == null){
                            mDivider = new RecycleViewDivider(ActivityStockList.this, LinearLayoutManager.HORIZONTAL);
                            mRecyclerView.addItemDecoration(mDivider);
                        }

                        mAttentionAdvisorAdapter.setOnItemClickListener(new MarketMarketAdapter.MyItemClickListener() {
                            @Override
                            public void onItemClick(View v, int id) {

                            }
                        });
                    }else {
                        mAttentionAdvisorAdapter.addDatas(indexResult.zhang_gupiao);

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
                }
                CommonUtils.dismissProgressDialog();
            }
        });
    }

    private void initListener() {
        back.setOnClickListener(this);
        mRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                mRecyclerView.postDelayed(new Runnable() {//防止滑动过快，loading界面显示太快
                    @Override
                    public void run() {
                        mRecyclerView.setPullLoadMoreCompleted();
                    }
                },1000);
                currentPage = 1;
                mAttentionAdvisorAdapter = null;
                visitInternet();
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
