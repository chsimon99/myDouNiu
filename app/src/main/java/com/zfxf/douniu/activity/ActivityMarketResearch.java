package com.zfxf.douniu.activity;

import android.content.Context;
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
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.view.FullyLinearLayoutManager;
import com.zfxf.douniu.view.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityMarketResearch extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.et_research)
    EditText research;

    @BindView(R.id.tv_research_cannel)
    TextView cannel;

    @BindView(R.id.rv_market_research)
    RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MarketResearchAdapter mResearchAdapter;
    private List<String> mStrings = new ArrayList<String>();
    private RecycleViewDivider mDivider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_research);
        ButterKnife.bind(this);
        initData();
        initListener();
    }

    private void initData() {
        if(mLayoutManager == null){
            mLayoutManager = new FullyLinearLayoutManager(this);
        }
        if(mResearchAdapter == null){
            mResearchAdapter = new MarketResearchAdapter(this, mStrings);
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
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch(actionId){
                    case EditorInfo.IME_ACTION_SEARCH:
                        CommonUtils.toastMessage("开始搜索");
                        mStrings = new ArrayList<String>();
                        mStrings.add("");
                        mStrings.add("");
                        mResearchAdapter.addDatas(mStrings);
                        mResearchAdapter.notifyDataSetChanged();
                        InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        research.setFocusable(false);
                        research.setText("");
                        break;
                }
                return false;
            }
        });
        mResearchAdapter.setOnItemClickListener(new MarketResearchAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View v, int positon) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_research_cannel:
                finishAll();
                finish();
                break;
            case R.id.et_research://从新获取焦点
                research.setFocusable(true);
                research.setFocusableInTouchMode(true);
                research.requestFocus();
                research.findFocus();
                InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0,InputMethodManager.SHOW_FORCED);
                mResearchAdapter.deleteDatas();
                mResearchAdapter.notifyDataSetChanged();
                break;
        }
    }

    private void finishAll() {

    }

}
