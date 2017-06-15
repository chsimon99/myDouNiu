package com.zfxf.douniu.activity.myself;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.adapter.recycleView.WalletNoticeAdapter;
import com.zfxf.douniu.view.FullyLinearLayoutManager;
import com.zfxf.douniu.view.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author IMXU
 * @time   2017/5/3 13:20
 * @des    联系我们
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class ActivityMyselfWallet extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.tv_myself_wallet)
    TextView count;
    @BindView(R.id.tv_myself_wallet_buy)
    TextView buy;
    @BindView(R.id.rv_myself_wallet)
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    RecycleViewDivider mDivider;
    WalletNoticeAdapter mAdapter;
    List<String> mList = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myself_wallet);
        ButterKnife.bind(this);

        title.setText("我的钱包");
        edit.setVisibility(View.INVISIBLE);

        initdata();
        initListener();
    }

    private void initdata() {
        if(mList.size() == 0){
            mList.add("");
            mList.add("");
            mList.add("");
        }
        if(mLayoutManager == null){
            mLayoutManager = new FullyLinearLayoutManager(this);
        }
        if(mAdapter == null){
            mAdapter = new WalletNoticeAdapter(this,mList);
        }
        if(mDivider == null){//防止多次加载出现宽度变宽
            mDivider = new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL);
            mRecyclerView.addItemDecoration(mDivider);
        }
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
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
            case R.id.ll_myself_disclaimer:
                break;
        }
    }

    private void finishAll() {

    }
}
