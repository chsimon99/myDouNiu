package com.zfxf.douniu.activity.stock;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.adapter.recycleView.MarketZiXuanAdapter;
import com.zfxf.douniu.bean.SimulationInfo;
import com.zfxf.douniu.bean.SimulationResult;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.SpTools;
import com.zfxf.douniu.view.FullyLinearLayoutManager;
import com.zfxf.douniu.view.RecycleViewDivider;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author IMXU
 * @time   2017/5/3 13:41
 * @des    我的自选股
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class ActivityMySelfStock extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_backto)
    ImageView backto;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;
    @BindView(R.id.tv_base_done)
    TextView done;

    @BindView(R.id.iv_myself_stock_all)
    ImageView select_all;
    @BindView(R.id.tv_myself_stock_count)
    TextView tv_all;
    @BindView(R.id.rl_myself_stock_delete)
    RelativeLayout rl_delete;
    @BindView(R.id.rl_myself_stock_all)
    RelativeLayout rl_all;

    @BindView(R.id.rv_myself_stock)
    RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MarketZiXuanAdapter mXuanAdapter;
    private RecycleViewDivider mRecycleViewDivider;
    private List<SimulationInfo> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myself_stock);
        ButterKnife.bind(this);
        title.setText("我的自选股");
        edit.setVisibility(View.GONE);
        backto.setVisibility(View.GONE);
        done.setVisibility(View.VISIBLE);
        initData();
        initListener();
    }
    private void initData() {
        visitInternet();
    }
    private String deleteCode;
    private void visitInternet() {
        NewsInternetRequest.getMyZiXuanStockInformation(new NewsInternetRequest.ForResultZiXuanStockListener() {
            @Override
            public void onResponseMessage(SimulationResult result) {
                if(SpTools.getBoolean(ActivityMySelfStock.this, Constants.buy,false)){
                    CommonUtils.toastMessage("删除成功");
                    select_all.setImageResource(R.drawable.my_noselect);
                    tv_all.setText("共选中0个");
                }
                if(result.mn_zixuan.size()>0){
                    if(mLayoutManager == null){
                        mLayoutManager = new FullyLinearLayoutManager(ActivityMySelfStock.this);
                    }
                    if(mXuanAdapter == null){
                        mList = result.mn_zixuan;
                        mXuanAdapter = new MarketZiXuanAdapter(ActivityMySelfStock.this, mList);
                    }
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(mXuanAdapter);
                    if(mRecycleViewDivider == null){//防止多次加载出现宽度变宽
                        mRecycleViewDivider = new RecycleViewDivider(ActivityMySelfStock.this, LinearLayoutManager.HORIZONTAL);
                        mRecyclerView.addItemDecoration(mRecycleViewDivider);
                    }
                    mXuanAdapter.setOnItemClickListener(new MarketZiXuanAdapter.MyItemClickListener() {
                        @Override
                        public void onItemClick(View v, int count) {
                            if(isDeleteing){
                                return;//删除的时候不允许点股票
                            }
                            if(count == mList.size()){
                                select_all.setImageResource(R.drawable.my_select);
                            }else {
                                select_all.setImageResource(R.drawable.my_noselect);
                            }
                            tv_all.setText("共选中"+count+"个");
                            selectType = false;
                        }
                    });
                }else{
                    if(mXuanAdapter == null){
                        mList = result.mn_zixuan;
                        mXuanAdapter = new MarketZiXuanAdapter(ActivityMySelfStock.this, mList);
                    }
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(mXuanAdapter);
                }
            }
        });
    }

    private void initListener() {
        edit.setOnClickListener(this);
        back.setOnClickListener(this);
        rl_all.setOnClickListener(this);
        rl_delete.setOnClickListener(this);
    }
    private boolean selectType = false;
    private boolean isDeleteing = false;//是否删除中
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back:
                finishAll();
                finish();
                break;
            case R.id.iv_base_edit:

                break;
            case R.id.rl_myself_stock_all://删除全选
                selectType = !selectType;
                if(mList.size() == 0){
                    return;
                }
                mXuanAdapter.selectAll(selectType);
                if(selectType){
                    select_all.setImageResource(R.drawable.my_select);
                    tv_all.setText("共选中"+mList.size()+"个");
                }else {
                    select_all.setImageResource(R.drawable.my_noselect);
                    tv_all.setText("共选中0个");
                }
                break;
            case R.id.rl_myself_stock_delete://删除自选
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                AlertDialog dialog = builder.create();
                builder.setTitle("确认删除")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                deleteCode = mXuanAdapter.getDeleteCode();
                                if(TextUtils.isEmpty(deleteCode)){
                                    return;
                                }
                                deleteCode = deleteCode.substring(0, deleteCode.length() - 1);
                                isDeleteing = true;
                                mXuanAdapter.setState(isDeleteing);
                                NewsInternetRequest.deleteZiXuanStockInformation(deleteCode, 1, new NewsInternetRequest.ForResultListener() {
                                    @Override
                                    public void onResponseMessage(String code) {
                                        isDeleteing = false;
                                        mXuanAdapter.setState(isDeleteing);
                                        if(code.equals("10")){
                                            SpTools.setBoolean(ActivityMySelfStock.this, Constants.buy,true);
                                            mXuanAdapter = null;
                                            visitInternet();
                                        }else {

                                        }
                                    }
                                });
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
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
