package com.zfxf.douniu.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zfxf.douniu.R;
import com.zfxf.douniu.adapter.recycleView.HomeResearchStockAdapter;
import com.zfxf.douniu.adapter.recycleView.HomeResearchTouguAdapter;
import com.zfxf.douniu.adapter.recycleView.HomeResearchZhiboAdapter;
import com.zfxf.douniu.bean.CourseResult;
import com.zfxf.douniu.bean.ResearchInfo;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.view.RecycleViewLunboDivider;
import com.zfxf.douniu.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityResearch extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.et_research)
    EditText research;
    @BindView(R.id.tv_research_cannel)
    TextView cannel;
    @BindView(R.id.ll_research_original)
    LinearLayout ll_original;
    @BindView(R.id.ll_research_research)
    LinearLayout ll_research;
    @BindView(R.id.ll_research_tougu)
    LinearLayout ll_tougu;
    @BindView(R.id.ll_research_stock)
    LinearLayout ll_stock;
    @BindView(R.id.ll_research_zhibo)
    LinearLayout ll_zhibo;
    @BindView(R.id.ll_research_result)
    LinearLayout ll_result;

    @BindView(R.id.tv_research_name)
    TextView tv_name;//搜索的内容
    @BindView(R.id.tv_research_type)
    TextView tv_type;//搜索的类别

    @BindView(R.id.rv_research_tougu)
    PullLoadMoreRecyclerView mTougu;
    HomeResearchTouguAdapter mTouguAdapter;

    @BindView(R.id.rv_research_stock)
    PullLoadMoreRecyclerView mStock;
    HomeResearchStockAdapter mStockAdapter;

    @BindView(R.id.rv_research_zhibo)
    PullLoadMoreRecyclerView mZhibo;
    HomeResearchZhiboAdapter mZhiboAdapter;

    private RecycleViewLunboDivider mDivider;
    private int totlePage = 0;
    private int currentPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_research);
        ButterKnife.bind(this);
        initData();
        initListener();
    }

    private void initData() {
        if(mDivider == null){
            mDivider = new RecycleViewLunboDivider(this, LinearLayoutManager.HORIZONTAL);
            mTougu.addItemDecoration(mDivider);
            mZhibo.addItemDecoration(mDivider);
            mStock.addItemDecoration(mDivider);
        }
        resetRecycleView();

    }
    private void initListener() {
        cannel.setOnClickListener(this);
        ll_tougu.setOnClickListener(this);
        ll_stock.setOnClickListener(this);
        ll_zhibo.setOnClickListener(this);
        research.setOnClickListener(this);
        research.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch(actionId){
                    case EditorInfo.IME_ACTION_SEARCH:
                        break;
                }
                return false;
            }
        });
        research.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                resetRecycleView();
                if(isResearch){
                    isResearch = false;
                    return;
                }
                if(TextUtils.isEmpty(s)){
                    ll_original.setVisibility(View.VISIBLE);
                    ll_research.setVisibility(View.GONE);
                }else {
                    ll_research.setVisibility(View.VISIBLE);
                    ll_original.setVisibility(View.GONE);
                }
                if(totlePage != 0){//初始化
                    totlePage = 0;
                }
                if(currentPage!=1){//初始化
                    currentPage = 1;
                }
            }
        });
        mStock.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
            }

            @Override
            public void onLoadMore() {
                if(currentPage > totlePage){
                    Toast.makeText(CommonUtils.getContext(),"没有数据了",Toast.LENGTH_SHORT).show();
                    mStock.postDelayed(new Runnable() {//防止滑动过快，loading界面显示太快
                        @Override
                        public void run() {
                            mStock.setPullLoadMoreCompleted();
                        }
                    }, 200);
                    return;
                }
                visitInternet();
            }
        });
    }
    private int type = 0;
    private InputMethodManager imm;
    private boolean isResearch = false;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_research_cannel:
                research.setText("");
                finishAll();
                finish();
                break;
            case R.id.ll_research_tougu:
                type = 1;
                visitInternet();
                imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);//隐藏键盘
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                break;
            case R.id.ll_research_stock:
                type = 0;
                visitInternet();
                imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                break;
            case R.id.ll_research_zhibo:
                type = 2;
                visitInternet();
                imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                break;
            case R.id.et_research:
                ll_result.setVisibility(View.GONE);
                resetRecycleView();
//                research.setText("");
                if(research.isFocusable()){

                }else{//打开键盘
                    research.setFocusable(true);
                    research.setFocusableInTouchMode(true);
                    research.requestFocus();
                    research.findFocus();
                    InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0,InputMethodManager.SHOW_FORCED);
                }
                break;
        }
    }

    private void visitInternet() {
        isResearch = true;
        NewsInternetRequest.getResearchInformation(research.getText().toString(), currentPage + "", type + "", new NewsInternetRequest.ForResultResearchInfoListener() {
            @Override
            public void onResponseMessage(CourseResult result) {
                String name = research.getText().toString();
                ll_research.setVisibility(View.GONE);
                resetRecycleView();
                if(type == 1){
                    if(result.news_list.size() == 0){
                        ll_result.setVisibility(View.VISIBLE);
                        tv_name.setText(name);
                        tv_type.setText("相关投顾");
                        research.setText("");
                        return;
                    }
                    mTougu.setVisibility(View.VISIBLE);
                    totlePage = Integer.parseInt(result.total);
                    if (totlePage > 0 && currentPage <= totlePage){
                        if(currentPage == 1){
                            mTouguAdapter = new HomeResearchTouguAdapter(ActivityResearch.this,result.news_list);
                            mTougu.setLinearLayout();
                            mTougu.setAdapter(mTouguAdapter);
                            mTougu.setPullRefreshEnable(false);//下拉刷新
                            mTouguAdapter.setOnItemClickListener(new HomeResearchTouguAdapter.MyItemClickListener() {
                                @Override
                                public void onItemClick(View v, int id) {
                                    Intent intent = new Intent(ActivityResearch.this, ActivityAdvisorHome.class);
                                    intent.putExtra("id",id);
                                    startActivity(intent);
                                    overridePendingTransition(0,0);
                                    finish();
                                }
                            });
                        }else {
                            mTouguAdapter.addDatas(result.news_list);
                            mTougu.post(new Runnable() {
                                @Override
                                public void run() {
                                    mTouguAdapter.notifyDataSetChanged();
                                }
                            });
                            mTougu.postDelayed(new Runnable() {//防止滑动过快，loading界面显示太快
                                @Override
                                public void run() {
                                    mTougu.setPullLoadMoreCompleted();
                                }
                            },1000);
                        }
                        currentPage++;
                    }

                }else if(type ==0){
                    if(result.news_list.size() == 0){
                        ll_result.setVisibility(View.VISIBLE);
                        tv_name.setText(name);
                        tv_type.setText("相关股票");
                        research.setText("");
                        return;
                    }
                    mStock.setVisibility(View.VISIBLE);
                    totlePage = Integer.parseInt(result.total);
                    if (totlePage > 0 && currentPage <= totlePage){
                        if(currentPage == 1){
                            mStockAdapter = new HomeResearchStockAdapter(ActivityResearch.this,result.news_list);
                            mStock.setLinearLayout();
                            mStock.setAdapter(mStockAdapter);
                            mStock.setPullRefreshEnable(false);//下拉刷新
                            mStockAdapter.setOnItemClickListener(new HomeResearchStockAdapter.MyItemClickListener() {
                                @Override
                                public void onItemClick(View v, ResearchInfo info) {
                                    Intent intent = new Intent(ActivityResearch.this, ActivityStockInfo.class);
                                    intent.putExtra("code",info.mg_code);
                                    intent.putExtra("name",info.mg_name);
                                    startActivity(intent);
                                    finish();
                                    overridePendingTransition(0,0);
                                }
                            });
                        }else {
                            mStockAdapter.addDatas(result.news_list);
                            mStock.post(new Runnable() {
                                @Override
                                public void run() {
                                    mStockAdapter.notifyDataSetChanged();
                                }
                            });
                            mStock.postDelayed(new Runnable() {//防止滑动过快，loading界面显示太快
                                @Override
                                public void run() {
                                    mStock.setPullLoadMoreCompleted();
                                }
                            },1000);
                        }
                        currentPage++;
                    }
                }else if(type ==2){
                    if(result.news_list.size() == 0){
                        ll_result.setVisibility(View.VISIBLE);
                        tv_name.setText(name);
                        tv_type.setText("相关直播");
                        research.setText("");
                        return;
                    }
                    mZhibo.setVisibility(View.VISIBLE);
                    totlePage = Integer.parseInt(result.total);
                    if (totlePage > 0 && currentPage <= totlePage){
                        if(currentPage == 1){
                            mZhiboAdapter = new HomeResearchZhiboAdapter(ActivityResearch.this,result.news_list);
                            mZhibo.setLinearLayout();
                            mZhibo.setAdapter(mZhiboAdapter);
                            mZhibo.setPullRefreshEnable(false);//下拉刷新
                            mZhiboAdapter.setOnItemClickListener(new HomeResearchZhiboAdapter.MyItemClickListener() {
                                @Override
                                public void onItemClick(View v, int id,int sx_id) {
                                    Intent intent = new Intent(ActivityResearch.this, ActivityLiving.class);
                                    intent.putExtra("id",id);
                                    intent.putExtra("sx_id",sx_id+"");
                                    startActivity(intent);
                                    overridePendingTransition(0,0);
                                    finish();
                                }
                            });
                        }else {
                            mZhiboAdapter.addDatas(result.news_list);
                            mZhibo.post(new Runnable() {
                                @Override
                                public void run() {
                                    mZhiboAdapter.notifyDataSetChanged();
                                }
                            });
                            mZhibo.postDelayed(new Runnable() {//防止滑动过快，loading界面显示太快
                                @Override
                                public void run() {
                                    mZhibo.setPullLoadMoreCompleted();
                                }
                            },1000);
                        }
                        currentPage++;
                    }
                }
                research.setText("");
            }
        });
    }

    private void resetRecycleView() {
        mTougu.setVisibility(View.GONE);
        mStock.setVisibility(View.GONE);
        mZhibo.setVisibility(View.GONE);
    }

    private void finishAll() {

    }

}
