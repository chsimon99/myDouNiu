package com.zfxf.douniu.activity.simulation;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zfxf.douniu.R;
import com.zfxf.douniu.adapter.recycleView.SimulationQueryTodayAdapter;
import com.zfxf.douniu.bean.SimulationResult;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.view.RecycleViewDivider;
import com.zfxf.douniu.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * @author IMXU
 * @time   2017/5/3 13:32
 * @des    模拟炒股 历史成交
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class ActivitysimulationQueryHistory extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.iv_base_refresh)
    ImageView refresh;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.rv_simulation_query_today)
    PullLoadMoreRecyclerView mRecyclerView;
    private SimulationQueryTodayAdapter mQueryTodayAdapter;
    private RecycleViewDivider mDivider;
    private int totlePage = 0;
    private int currentPage = 1;

    @BindView(R.id.ll_simulation_query_history_start)
    LinearLayout time_start;
    @BindView(R.id.ll_simulation_query_history_end)
    LinearLayout time_end;
    @BindView(R.id.tv_simulation_query_history_start)
    TextView tv_start;
    @BindView(R.id.tv_simulation_query_history_end)
    TextView tv_end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulation_query_history);
        ButterKnife.bind(this);
        title.setText("历史成交");
        edit.setVisibility(View.INVISIBLE);
        refresh.setVisibility(View.VISIBLE);
        initData();
        initListener();
    }

    private void initData() {
        CommonUtils.showProgressDialog(this,"加载中……");
        visitInternet();
    }

    private void visitInternet() {
        NewsInternetRequest.getSimulationQuereInformation("1", currentPage, new NewsInternetRequest.ForResultSimulationIndexListener() {
            @Override
            public void onResponseMessage(SimulationResult result) {
                if(result !=null){
                    totlePage = Integer.parseInt(result.total);
                    if (totlePage > 0 && currentPage <= totlePage){
                        if(currentPage == 1){
                            if(mQueryTodayAdapter == null){
                                mQueryTodayAdapter = new SimulationQueryTodayAdapter(ActivitysimulationQueryHistory.this,result.lishi_jiaoyi);
                            }
                            mRecyclerView.setLinearLayout();
                            mRecyclerView.setAdapter(mQueryTodayAdapter);
                            if(mDivider == null){
                                mDivider = new RecycleViewDivider(ActivitysimulationQueryHistory.this, LinearLayoutManager.HORIZONTAL);
                                mRecyclerView.addItemDecoration(mDivider);
                            }
                            mRecyclerView.setPullRefreshEnable(false);//下拉刷新
                            mRecyclerView.setFooterViewText("加载更多……");
                        }else {
                            mQueryTodayAdapter.addDatas(result.lishi_jiaoyi);
                            mRecyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    mQueryTodayAdapter.notifyDataSetChanged();
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
                    mRecyclerView.setPullRefreshEnable(false);//下拉刷新
                    CommonUtils.dismissProgressDialog();
                }
            }
        });
    }

    private void initListener() {
        back.setOnClickListener(this);
        refresh.setOnClickListener(this);
        time_start.setOnClickListener(this);
        time_end.setOnClickListener(this);
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
            case R.id.iv_base_refresh:
                currentPage = 1;
                mQueryTodayAdapter = null;
                visitInternet();
                break;
            case R.id.ll_simulation_query_history_start:
//                getTime(tv_start);
                break;
            case R.id.ll_simulation_query_history_end:
//                getTime(tv_end);
                break;
        }
    }

    private void finishAll() {

    }
    private void getTime(final TextView tv){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        DatePickerDialog pickerDialog =new DatePickerDialog(ActivitysimulationQueryHistory.this
                , R.style.MydateStyle, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                tv.setText(year+"年"+(month+1)+"月"+dayOfMonth+"日");
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        pickerDialog.show();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CommonUtils.dismissProgressDialog();
    }
}
