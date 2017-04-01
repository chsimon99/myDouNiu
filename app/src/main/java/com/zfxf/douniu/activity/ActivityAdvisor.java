package com.zfxf.douniu.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zfxf.douniu.R;
import com.zfxf.douniu.adapter.recycleView.AdvisorAdapter;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.view.RecycleViewDivider;
import com.zfxf.douniu.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityAdvisor extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.rv_advisor)
    PullLoadMoreRecyclerView mRecyclerView;
    private AdvisorAdapter mAdapter;
    private List<String> datas = new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advisor);
        ButterKnife.bind(this);
        title.setText("精英首席");
        edit.setVisibility(View.INVISIBLE);
        initdata();
        initListener();
    }

    private void initdata() {
        if(datas.size() == 0){
            datas.add("");
            datas.add("");
            datas.add("");
            datas.add("");
            datas.add("");
            datas.add("");
            datas.add("");
            datas.add("");
        }
        if(mAdapter == null){
            mAdapter = new AdvisorAdapter(this,datas);
        }
        mRecyclerView.setLinearLayout();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL));
        mRecyclerView.setFooterViewText("加载更多……");

    }
    int num = 0;
    private void initListener() {
        back.setOnClickListener(this);
        mAdapter.setOnItemClickListener(new AdvisorAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View v, int positon) {
                CommonUtils.toastMessage("点击了"+positon);
            }
        });

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
                if(num > 1){
                    Toast.makeText(CommonUtils.getContext(),"没有数据了",Toast.LENGTH_SHORT).show();
                    mRecyclerView.setPullLoadMoreCompleted();
                    return;
                }
                num++;
                List<String> newdatas = new ArrayList<String>();
                newdatas.add("1");
                mAdapter.addDatas(newdatas);
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
}
