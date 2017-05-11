package com.zfxf.douniu.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.adapter.recycleView.HistoryAdapter;
import com.zfxf.douniu.view.FullyLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * @author IMXU
 * @time   2017/5/3 13:38
 * @des    历史战绩
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class Activityhistory extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.rv_history)
    RecyclerView mRecyclerView;
    private LinearLayoutManager mAdvisorManager;
    private HistoryAdapter mHistoryAdapter;
    private List<String> mDatas = new ArrayList<String>();
//    private RecycleViewDivider mDivider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);
        title.setText("历史数据");
        edit.setVisibility(View.INVISIBLE);
        initData();
        initListener();
    }

    private void initData() {
        if (mDatas.size() == 0) {
            mDatas.add("");
            mDatas.add("");
            mDatas.add("");
        }
        if(mAdvisorManager == null){
            mAdvisorManager = new FullyLinearLayoutManager(this);
        }
        if(mHistoryAdapter == null){
            mHistoryAdapter = new HistoryAdapter(this, mDatas);
        }
        mRecyclerView.setLayoutManager(mAdvisorManager);
        mRecyclerView.setAdapter(mHistoryAdapter);
    }
    private void initListener() {
        back.setOnClickListener(this);
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
