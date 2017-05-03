package com.zfxf.douniu.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.adapter.recycleView.SimulationQueryTodayAdapter;
import com.zfxf.douniu.view.FullyLinearLayoutManager;
import com.zfxf.douniu.view.RecycleViewDivider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    RecyclerView mRecyclerView;
    private SimulationQueryTodayAdapter mQueryTodayAdapter;
    private LinearLayoutManager mManager;
    private List<String> datas = new ArrayList<String>();
    private RecycleViewDivider mDivider;

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
        initData();
        initListener();
    }

    private void initData() {
        if(datas.size() == 0){
            datas.add("1");
            datas.add("2");
            datas.add("2");
        }
        if(mQueryTodayAdapter == null){
            mQueryTodayAdapter = new SimulationQueryTodayAdapter(this,datas);
        }
        if(mManager == null){
            mManager = new FullyLinearLayoutManager(this);
        }
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setAdapter(mQueryTodayAdapter);
        if(mDivider == null){
            mDivider = new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL);
            mRecyclerView.addItemDecoration(mDivider);
        }

    }
    private void initListener() {
        back.setOnClickListener(this);
        refresh.setOnClickListener(this);
        time_start.setOnClickListener(this);
        time_end.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back:
                finishAll();
                finish();
                break;
            case R.id.iv_base_refresh:
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
}
