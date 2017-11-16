package com.zfxf.douniu.activity.askstock;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.pay.ActivityToPay;
import com.zfxf.douniu.adapter.recycleView.ZhenguAnswerAdapter;
import com.zfxf.douniu.bean.AnswerListInfo;
import com.zfxf.douniu.bean.IndexResult;
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
 * @time   2017/5/3 13:40
 * @des    微问答 更多精彩回答
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class ActivityAnswer extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.rv_advisor)
    PullLoadMoreRecyclerView mRecyclerView;
    private ZhenguAnswerAdapter mAdapter;

    private int totlePage = 0;
    private int currentPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_advisor);
        ButterKnife.bind(this);
        title.setText("回答");
        edit.setVisibility(View.INVISIBLE);
        initdata();
        initListener();
    }

    private void initdata() {
        visitInternet();
    }

    private void visitInternet() {
        CommonUtils.showProgressDialog(this,"加载中……");
        NewsInternetRequest.getAnswerLIstInformation(currentPage, null, new NewsInternetRequest.ForResultAnswerIndexListener() {
            @Override
            public void onResponseMessage(IndexResult result) {
                totlePage = Integer.parseInt(result.total);
                if (totlePage > 0 && currentPage <= totlePage){
                    if(currentPage == 1){

                        if(mAdapter == null){
                            mAdapter = new ZhenguAnswerAdapter(ActivityAnswer.this,result.bright_answer);
                        }
                        mRecyclerView.setLinearLayout();
                        mRecyclerView.setAdapter(mAdapter);
                        mRecyclerView.addItemDecoration(new RecycleViewDivider(ActivityAnswer.this, LinearLayoutManager.HORIZONTAL));
                        mRecyclerView.setFooterViewText("加载更多……");

                        mAdapter.setOnItemClickListener(new ZhenguAnswerAdapter.MyItemClickListener() {
                            @Override
                            public void onItemClick(View v, int positon,AnswerListInfo bean) {
                                if (bean.zc_sfjf.equals("0")){
                                    Intent intent = new Intent(CommonUtils.getContext(), ActivityToPay.class);

                                    intent.putExtra("info","微问答,"+bean.sx_ub_id+","+bean.zc_id);
                                    intent.putExtra("type","一元偷偷看");
                                    intent.putExtra("count",bean.zc_fee);
                                    intent.putExtra("sx_id",bean.sx_ub_id);
                                    intent.putExtra("from",bean.ud_nickname);
                                    intent.putExtra("planId",Integer.parseInt(bean.zc_id));

                                    startActivity(intent);
                                    overridePendingTransition(0,0);
                                }else{
                                    Intent intent = new Intent(CommonUtils.getContext(), ActivityAnswerDetail.class);
                                    intent.putExtra("id",bean.zc_id);
                                    startActivity(intent);
                                    overridePendingTransition(0,0);
                                }
                            }
                        });
                    }else {
                        mAdapter.addDatas(result.bright_answer);
                        mRecyclerView.post(new Runnable() {//避免出现adapter异常
                            @Override
                            public void run() {
                                mAdapter.notifyDataSetChanged();
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
                currentPage = 1;
                mAdapter = null;
                visitInternet();
                mRecyclerView.postDelayed(new Runnable() {//防止滑动过快，loading界面显示太快
                    @Override
                    public void run() {
                        mRecyclerView.setPullLoadMoreCompleted();
                    }
                },1000);
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
                    }, 200);
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
        mAdapter = null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CommonUtils.dismissProgressDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(SpTools.getBoolean(this, Constants.buy,false)){
            currentPage = 1;
            mAdapter = null;
            visitInternet();
            SpTools.setBoolean(this, Constants.buy,false);
        }
        if(SpTools.getBoolean(this, Constants.yiyuanbuy,false)){
            currentPage = 1;
            mAdapter = null;
            visitInternet();
            SpTools.setBoolean(this, Constants.yiyuanbuy, false);
        }
    }
}
