package com.zfxf.douniu.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.adapter.recycleView.MarketResearchAdapter;
import com.zfxf.douniu.bean.SimulationResult;
import com.zfxf.douniu.bean.SimulationSearchInfo;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.SpTools;
import com.zfxf.douniu.view.FullyLinearLayoutManager;
import com.zfxf.douniu.view.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author IMXU
 * @time   2017/5/3 13:43
 * @des    行情 搜索界面
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class ActivityMarketResearch extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.et_research)
    EditText research;

    @BindView(R.id.tv_research_cannel)
    TextView cannel;

    @BindView(R.id.rv_market_research)
    RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MarketResearchAdapter mResearchAdapter;
    private List<SimulationSearchInfo> mStrings = new ArrayList<SimulationSearchInfo>();
    private RecycleViewDivider mDivider;
    private boolean mSimulation;
    private int mType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_research);
        ButterKnife.bind(this);
        mSimulation = getIntent().getBooleanExtra("simulation", false);
        mType = getIntent().getIntExtra("type", 0);//搜索的类型 0 所有 1模拟盘的搜索
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(research, 0);
            }
        },300);
        initData();
        initListener();
    }

    private void initData() {
        if(mLayoutManager == null){
            mLayoutManager = new FullyLinearLayoutManager(this);
        }
        if(mResearchAdapter == null){
            mResearchAdapter = new MarketResearchAdapter(this, mStrings,mSimulation);
        }
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mResearchAdapter);
        if(mDivider == null){//防止多次加载出现宽度变宽
            mDivider = new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL);
            mRecyclerView.addItemDecoration(mDivider);
        }

    }
    private void initListener() {
        cannel.setOnClickListener(this);
        research.setOnClickListener(this);
        research.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(final TextView v, int actionId, KeyEvent event) {
                switch(actionId){
                    case EditorInfo.IME_ACTION_SEARCH:
                        CommonUtils.toastMessage("开始搜索");
                        InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        NewsInternetRequest.getSearchStockInformation(research.getText().toString(),mType,new NewsInternetRequest.ForResultSimulationIndexListener() {
                            @Override
                            public void onResponseMessage(SimulationResult result) {
                                if(result.mg_gupiao.size() == 0){
                                    CommonUtils.toastMessage("没有搜索到数据");
                                }else if(result.mg_gupiao.size()>0){
                                    mResearchAdapter.deleteDatas();
                                    mResearchAdapter.addDatas(result.mg_gupiao);
                                    mResearchAdapter.notifyDataSetChanged();
                                }
                                research.setFocusable(false);
                                research.setText("");
                            }
                        });
                        break;
                }
                return false;
            }
        });
        mResearchAdapter.setOnItemClickListener(new MarketResearchAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View v, int position,SimulationSearchInfo info) {
                if(mSimulation){
                    fanhui(info);
                    finish();
                }else {
                    //跳转股票详情信息
                    Intent intent = new Intent(ActivityMarketResearch.this, ActivityStockInfo.class);
                    intent.putExtra("code",info.mg_code);
                    intent.putExtra("name",info.mg_name);
                    intent.putExtra("model",info.name);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                }
            }
        });
        mResearchAdapter.setOnAddStockClickListener(new MarketResearchAdapter.MyAddStockClickListener() {
            @Override
            public void onItemClick(View v, int position, SimulationSearchInfo info) {
                if(info.isAddSuccess() || info.zixuan_status.equals("1")){
                    return;
                }
                addZiXuanStock(info.mg_code,position);
            }
        });
    }

    private void addZiXuanStock(String mg_code, final int position) {
        NewsInternetRequest.deleteZiXuanStockInformation(mg_code, 0, new NewsInternetRequest.ForResultListener() {
            @Override
            public void onResponseMessage(String code) {
                if(code.equals("10")){
                    CommonUtils.toastMessage("添加自选成功");
                    SpTools.setBoolean(ActivityMarketResearch.this, Constants.buy,true);
                }else {
                    mResearchAdapter.changeItemImage(position);
                    mResearchAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void fanhui(SimulationSearchInfo info) {
        Intent intent = new Intent();
        intent.putExtra("name",info.mg_name);
        intent.putExtra("code",info.mg_code);
        intent.putExtra("model",info.name);
        setResult(2,intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_research_cannel:
                finishAll();
                finish();
                break;
            case R.id.et_research://从新获取焦点
                if(research.isFocusable()){

                }else{
                    research.setFocusable(true);
                    research.setFocusableInTouchMode(true);
                    research.requestFocus();
                    research.findFocus();
                    InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0,InputMethodManager.SHOW_FORCED);
                    mResearchAdapter.deleteDatas();
                    mResearchAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    private void finishAll() {

    }

}
