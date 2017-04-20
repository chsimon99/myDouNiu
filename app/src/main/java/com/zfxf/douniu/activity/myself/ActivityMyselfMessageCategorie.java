package com.zfxf.douniu.activity.myself;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.ActivityInformation;
import com.zfxf.douniu.adapter.recycleView.NewNoticeAdapter;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.view.RecycleViewDivider;
import com.zfxf.douniu.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityMyselfMessageCategorie extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.rv_myself_message_categorie)
    PullLoadMoreRecyclerView mRecyclerView;
    private NewNoticeAdapter mNoticeAdapter;
    private List<String> datas = new ArrayList<String>();
    private RecycleViewDivider mDivider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myself_message_categorie);
        ButterKnife.bind(this);
        String type = getIntent().getStringExtra("type");
        title.setText(type);
        edit.setVisibility(View.INVISIBLE);
        initData();
        initListener();
    }

    private void initData() {
        if (datas.size() == 0) {
            datas.add("1");
            datas.add("2");
            datas.add("3");
            datas.add("4");
            datas.add("4");
            datas.add("4");
            datas.add("4");
        }
        if (mNoticeAdapter == null) {
            mNoticeAdapter = new NewNoticeAdapter(this, datas);
        }

        mRecyclerView.setLinearLayout();
        mRecyclerView.setAdapter(mNoticeAdapter);
        if(mDivider == null){//防止多次加载出现宽度变宽
            mDivider = new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL);
            mRecyclerView.addItemDecoration(mDivider);
        }
        mRecyclerView.setFooterViewText("加载更多……");
    }
    int num = 0;
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
                }, 1000);
            }

            @Override
            public void onLoadMore() {
                if (num > 1) {
                    Toast.makeText(CommonUtils.getContext(), "没有数据了", Toast.LENGTH_SHORT).show();
                    mRecyclerView.setPullLoadMoreCompleted();
                    return;
                }
                num++;
                List<String> newdatas = new ArrayList<String>();
                newdatas.add("7");
                mNoticeAdapter.addDatas("");
                mRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        mNoticeAdapter.notifyDataSetChanged();
                    }
                });
                mRecyclerView.postDelayed(new Runnable() {//防止滑动过快，loading界面显示太快
                    @Override
                    public void run() {
                        mRecyclerView.setPullLoadMoreCompleted();
                    }
                }, 1000);
            }
        });

        mNoticeAdapter.setOnItemClickListener(new NewNoticeAdapter.MyItemClickListener() {
            Intent mIntent;
            @Override
            public void onItemClick(View v, int positon) {
                mIntent = new Intent(ActivityMyselfMessageCategorie.this, ActivityInformation.class);
                startActivity(mIntent);
                overridePendingTransition(0,0);
                mIntent = null;
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
