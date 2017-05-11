package com.zfxf.douniu.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zfxf.douniu.R;
import com.zfxf.douniu.adapter.recycleView.XiangMuAdapter;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.view.RecycleViewDivider;
import com.zfxf.douniu.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * @author IMXU
 * @time   2017/5/3 13:32
 * @des    主页 好项目
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class ActivityXiangMu extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.rv_xiangmu)
    PullLoadMoreRecyclerView mRecyclerView;
    private XiangMuAdapter mAdapter;
    private int totlePage = 0;
    private int currentPage = 1;
    private List<String> datas = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiangmu);
        ButterKnife.bind(this);

        title.setText("好项目");
        edit.setVisibility(View.INVISIBLE);

        initdata();
        initListener();
    }

    private void initdata() {
        CommonUtils.showProgressDialog(this,"加载中……");
        visitInternet();
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
                if( currentPage > totlePage ){
                    Toast.makeText(CommonUtils.getContext(),"没有数据了",Toast.LENGTH_SHORT).show();
                    mRecyclerView.setPullLoadMoreCompleted();
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

    private void visitInternet(){
        NewsInternetRequest.getProjectListInformation(currentPage + "", new NewsInternetRequest.ForResultPolicyInfoListener() {
            @Override
            public void onResponseMessage(List<Map<String, String>> lists, String totalpage) {
                totlePage = Integer.parseInt(totalpage);
                if (totlePage > 0 && currentPage <= totlePage){
                    if(currentPage == 1){
                        if(mAdapter == null){
                            mAdapter = new XiangMuAdapter(ActivityXiangMu.this,lists);
                        }
                        mRecyclerView.setLinearLayout();
                        mRecyclerView.setAdapter(mAdapter);
                        mRecyclerView.addItemDecoration(new RecycleViewDivider(ActivityXiangMu.this, LinearLayoutManager.HORIZONTAL
                                ,CommonUtils.px2dip(ActivityXiangMu.this,40), Color.parseColor("#f4f4f4")));
                        mRecyclerView.setFooterViewText("加载更多……");
                        mRecyclerView.setPullRefreshEnable(false);//禁止上啦刷新

                        mAdapter.setOnItemClickListener(new XiangMuAdapter.MyItemClickListener() {
                            @Override
                            public void onItemClick(View v, int id) {
                                Intent intent = new Intent(CommonUtils.getContext(), ActivityXiangMuDetail.class);
                                intent.putExtra("newsinfoId",id);
                                startActivity(intent);
                                overridePendingTransition(0,0);
                            }
                        });
                    }else {
                        mAdapter.addDatas(lists);
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
                }else{
                    mRecyclerView.postDelayed(new Runnable() {//防止滑动过快，loading界面显示太快
                        @Override
                        public void run() {
                            mRecyclerView.setPullLoadMoreCompleted();
                        }
                    }, 1000);
                    CommonUtils.dismissProgressDialog();
                    return;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CommonUtils.dismissProgressDialog();
    }
}
