package com.zfxf.douniu.activity.stock;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.adapter.recycleView.MarketMarketAdapter;
import com.zfxf.douniu.view.RecycleViewDivider;
import com.zfxf.douniu.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * @author IMXU
 * @time   2017/11/10 15:46
 * @des    股票板块详情
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class ActivityStockTradeDetail extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.tv_stock_trade_detail_name)
    TextView tv_name;
    @BindView(R.id.tv_stock_trade_detail_income)
    TextView tv_income;

    @BindView(R.id.rv_stockr_trade_detail)
    PullLoadMoreRecyclerView mRecyclerView;
    private MarketMarketAdapter mAdapter;
    private RecycleViewDivider mDivider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_trade_detail);
        ButterKnife.bind(this);
        String name = getIntent().getStringExtra("name");
        title.setText(name);
        edit.setVisibility(View.INVISIBLE);
        initData();
        initListener();
    }

    private void initData() {


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
