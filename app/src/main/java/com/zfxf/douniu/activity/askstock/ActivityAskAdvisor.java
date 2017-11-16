package com.zfxf.douniu.activity.askstock;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.login.ActivityLogin;
import com.zfxf.douniu.adapter.recycleView.AdvisorAdapter;
import com.zfxf.douniu.bean.AnswerChiefListInfo;
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
 * @time   2017/5/3 13:41
 * @des    微问答 在线分析师
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class ActivityAskAdvisor extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.rv_advisor)
    PullLoadMoreRecyclerView mRecyclerView;
    private AdvisorAdapter mAdapter;

    private int totlePage = 0;
    private int currentPage = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_advisor);
        ButterKnife.bind(this);
        title.setText("精英首席");
        edit.setVisibility(View.INVISIBLE);
        initdata();
        initListener();
    }

    private void initdata() {
        currentPage = 1;
        mAdapter = null;
        CommonUtils.showProgressDialog(this,"加载中……");
        visitInternet();
    }

    private void visitInternet() {
        NewsInternetRequest.getAnswerAdvisorListInformation(currentPage, new NewsInternetRequest.ForResultAnswerIndexListener() {
            @Override
            public void onResponseMessage(IndexResult result) {
                if(!TextUtils.isEmpty(result.total)){
                    totlePage = Integer.parseInt(result.total);
                }
                if (totlePage > 0 && currentPage <= totlePage){
                    if(currentPage == 1){
                        if(mAdapter == null){
                            mAdapter = new AdvisorAdapter(ActivityAskAdvisor.this,result.online_chief);
                        }
                        mRecyclerView.setLinearLayout();
                        mRecyclerView.setAdapter(mAdapter);
                        mRecyclerView.setPullRefreshEnable(false);//下拉刷新
                        mRecyclerView.addItemDecoration(new RecycleViewDivider(ActivityAskAdvisor.this, LinearLayoutManager.HORIZONTAL));
                        mRecyclerView.setFooterViewText("加载更多……");

                        mAdapter.setOnItemClickListener(new AdvisorAdapter.MyItemClickListener() {
                            @Override
                            public void onItemClick(View v, int positon,AnswerChiefListInfo bean) {
                                if(!SpTools.getBoolean(CommonUtils.getContext(), Constants.isLogin,false)){
                                    Intent intent = new Intent(ActivityAskAdvisor.this, ActivityLogin.class);
                                    startActivity(intent);
                                    overridePendingTransition(0,0);
                                    return;
                                }
                                Intent intent = new Intent(CommonUtils.getContext(), ActivityAskStock.class);
                                intent.putExtra("name",bean.ud_nickname);
                                intent.putExtra("fee",bean.df_fee);
                                intent.putExtra("sx_id",bean.sx_ub_id);
                                startActivity(intent);
                                overridePendingTransition(0,0);
                            }
                        });

                    }else {
                        mAdapter.addDatas(result.online_chief);
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
            SpTools.setBoolean(this, Constants.buy,false);

            final AlertDialog mDialog;
            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityAskAdvisor.this); //先得到构造器
            mDialog = builder.create();
            mDialog.show();
            View view = View.inflate(ActivityAskAdvisor.this, R.layout.activity_pay_ok_dialog, null);
            mDialog.getWindow().setContentView(view);
            view.findViewById(R.id.tv_pay_ok_dialog_confirm).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                }
            });
        }
    }
}
