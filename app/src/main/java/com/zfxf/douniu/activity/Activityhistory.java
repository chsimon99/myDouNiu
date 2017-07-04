package com.zfxf.douniu.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zfxf.douniu.R;
import com.zfxf.douniu.adapter.recycleView.HistoryAdapter;
import com.zfxf.douniu.bean.XuanguDetail;
import com.zfxf.douniu.bean.XuanguResult;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
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
public class Activityhistory extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.rv_history)
    PullLoadMoreRecyclerView mRecyclerView;
    private HistoryAdapter mHistoryAdapter;
    private int mId;
    private int totlePage = 0;
    private int currentPage = 1;
    private String mUb_id;
    private String mType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);
        title.setText("历史数据");
        edit.setVisibility(View.INVISIBLE);
        mId = getIntent().getIntExtra("id", 0);
        mUb_id = getIntent().getStringExtra("sx_id");
        mType = getIntent().getStringExtra("type");
        initData();
        initListener();
    }

    private void initData() {
        mHistoryAdapter = null;
        CommonUtils.showProgressDialog(this,"加载中……");
        visitInternet();
    }

    private void visitInternet() {
        if(mId>0){
            NewsInternetRequest.getXuanGuHistoryInformation(mId, currentPage + "", new NewsInternetRequest.ForResultXuanGuDetailListener() {
                @Override
                public void onResponseMessage(XuanguDetail result) {
                    totlePage = Integer.parseInt(result.total);
                    if (totlePage > 0 && currentPage <= totlePage){
                        if(currentPage == 1){
                            if(mHistoryAdapter == null){
                                mHistoryAdapter = new HistoryAdapter(Activityhistory.this, result.news_list);
                            }
                            mRecyclerView.setLinearLayout();
                            mRecyclerView.setAdapter(mHistoryAdapter);
                            mRecyclerView.setPullRefreshEnable(false);//下拉刷新
                            mRecyclerView.setFooterViewText("加载更多……");
                        }else {
                            mHistoryAdapter.addDatas(result.news_list);
                            mRecyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    mHistoryAdapter.notifyDataSetChanged();
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
        if(!TextUtils.isEmpty(mUb_id) && !TextUtils.isEmpty(mType)){
            NewsInternetRequest.getGoldPondHistoryInformation(mUb_id, mType, new NewsInternetRequest.ForResultGoldPoneHistoryStockListener() {
                @Override
                public void onResponseMessage(XuanguResult result) {
                    if(mHistoryAdapter == null){
                        mHistoryAdapter = new HistoryAdapter(Activityhistory.this, result.ls_result);
                    }
                    mRecyclerView.setLinearLayout();
                    mRecyclerView.setAdapter(mHistoryAdapter);
                    mRecyclerView.setPullRefreshEnable(false);//下拉刷新
                    mRecyclerView.setPushRefreshEnable(false);//上拉加载

                    CommonUtils.dismissProgressDialog();
                }
            });
        }
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

}
