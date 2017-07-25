package com.zfxf.douniu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zfxf.douniu.R;
import com.zfxf.douniu.adapter.recycleView.MatadorPositonAdapter;
import com.zfxf.douniu.bean.SimulationDetail;
import com.zfxf.douniu.bean.SimulationPositionDetail;
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
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * @author IMXU
 * @time   2017/5/3 13:30
 * @des    首页模拟炒股
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class ActivitySimulation extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.iv_base_question)
    ImageView question;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.tv_matador_name)
    TextView tv_name;
    @BindView(R.id.tv_matador_fans)
    TextView tv_fans;
    @BindView(R.id.tv_matador_type)
    TextView tv_type;
    @BindView(R.id.tv_matador_introduce)
    TextView tv_introduce;
    @BindView(R.id.tv_matador_rank)
    TextView tv_rank;//排名
    @BindView(R.id.tv_matador_position)
    TextView tv_postion;//当前持仓

    @BindView(R.id.iv_matador_img)
    ImageView img;
    @BindView(R.id.iv_matador_type)
    ImageView type;//上升图片
    @BindView(R.id.iv_matador_start_three)
    ImageView start_three;
    @BindView(R.id.iv_matador_start_four)
    ImageView start_four;
    @BindView(R.id.iv_matador_startfive)
    ImageView start_five;

//    @BindView(R.id.rl_matador_more)
//    RelativeLayout rl_more;

    @BindView(R.id.ll_matador_buy)
    LinearLayout ll_buy;
    @BindView(R.id.ll_matador_sold)
    LinearLayout ll_sold;
    @BindView(R.id.ll_matador_entrust)
    LinearLayout ll_entrust;
    @BindView(R.id.ll_matador_query)
    LinearLayout ll_query;

    @BindView(R.id.tv_matador_mes_asset)
    TextView asset;
    @BindView(R.id.tv_matador_mes_ratio)
    TextView ratio;//仓位
    @BindView(R.id.tv_matador_mes_month_trade)
    TextView trade;//月交易
    @BindView(R.id.tv_matador_mes_total_income)
    TextView total_income;
    @BindView(R.id.tv_matador_mes_month_income)
    TextView month_income;
    @BindView(R.id.tv_matador_mes_week_income)
    TextView week_income;

    @BindView(R.id.rv_matador_detail)
    RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MatadorPositonAdapter mPositonAdapter;
    private RecycleViewDivider mDivider;
    private static List<SimulationPositionDetail> mChigu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulation);
        ButterKnife.bind(this);
        title.setText("模拟炒股");
        edit.setVisibility(View.INVISIBLE);
        question.setVisibility(View.VISIBLE);
        asset.getPaint().setFakeBoldText(true);//加粗
        ratio.getPaint().setFakeBoldText(true);//加粗
        trade.getPaint().setFakeBoldText(true);//加粗
        total_income.getPaint().setFakeBoldText(true);//加粗
        month_income.getPaint().setFakeBoldText(true);//加粗
        week_income.getPaint().setFakeBoldText(true);//加粗
        tv_postion.getPaint().setFakeBoldText(true);//加粗

        initData();
        initListener();
    }

    private void initData() {
        CommonUtils.showProgressDialog(this,"加载中……");
        visitInternet();

    }

    private void visitInternet() {
        NewsInternetRequest.getSimulationIndexInformation(null,new NewsInternetRequest.ForResultSimulationIndexListener() {
            @Override
            public void onResponseMessage(SimulationResult result) {
                SimulationDetail detail = result.user_detail;
                mChigu = result.mn_chigu;
                if(detail.url.contains("http")){
                    Glide.with(ActivitySimulation.this).load(detail.url)
                            .placeholder(R.drawable.home_adviosr_img)
                            .bitmapTransform(new CropCircleTransformation(ActivitySimulation.this)).into(img);
                }else {
                    String picUrl = getResources().getString(R.string.file_host_address)
                            +getResources().getString(R.string.showpic)
                            +detail.url;
                    Glide.with(ActivitySimulation.this).load(picUrl)
                            .placeholder(R.drawable.home_adviosr_img)
                            .bitmapTransform(new CropCircleTransformation(ActivitySimulation.this)).into(img);
                }
                tv_name.setText(detail.ud_nickname);
                tv_fans.setText(detail.ud_fensi);
                tv_type.setText(detail.ud_ul_name);
                if(detail.ud_ul_level.equals("4")){
                    start_three.setVisibility(View.GONE);
                    start_four.setVisibility(View.GONE);
                    start_five.setVisibility(View.GONE);
                }else if(detail.ud_ul_level.equals("3")){
                    start_four.setVisibility(View.GONE);
                    start_five.setVisibility(View.GONE);
                }else if(detail.ud_ul_level.equals("2")){
                    start_five.setVisibility(View.GONE);
                }else {

                }
                if(detail.mf_status.equals("1")){
                    type.setImageResource(R.drawable.myself_up);
                }else {
                    type.setImageResource(R.drawable.myself_up_down);
                }
                tv_introduce.setText("简介："+detail.ud_memo);
                tv_rank.setText(detail.mf_zpm);
                asset.setText(detail.mf_zzc);
                ratio.setText(detail.mf_cw);
                trade.setText(detail.mf_yjy);
                total_income.setText(detail.mf_zsy);
                month_income.setText(detail.mf_bysy);
                week_income.setText(detail.mf_bzsy);

                if(mLayoutManager == null){
                    mLayoutManager = new FullyLinearLayoutManager(ActivitySimulation.this);
                }
                if(mPositonAdapter == null){
                    mPositonAdapter = new MatadorPositonAdapter(ActivitySimulation.this, result.mn_chigu);
                }
                if(mDivider == null){//防止多次加载出现宽度变宽
                    mDivider = new RecycleViewDivider(ActivitySimulation.this, LinearLayoutManager.HORIZONTAL);
                    mRecyclerView.addItemDecoration(mDivider);
                }
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mPositonAdapter);

                CommonUtils.dismissProgressDialog();
            }
        });
    }

    private void initListener() {
        back.setOnClickListener(this);
        question.setOnClickListener(this);
        ll_buy.setOnClickListener(this);
        ll_sold.setOnClickListener(this);
        ll_entrust.setOnClickListener(this);
        ll_query.setOnClickListener(this);
    }
    public static List<SimulationPositionDetail> getList(){
        return mChigu;
    }
    Intent mIntent;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back:
                finishAll();
                finish();
                break;
            case R.id.ll_matador_buy:
                mIntent = new Intent(this,ActivitySimulationStock.class);
                setIndex(2);
                startActivity(mIntent);
                overridePendingTransition(0,0);
                break;
            case R.id.ll_matador_sold:
                mIntent = new Intent(this,ActivitySimulationStock.class);
                setIndex(3);
                startActivity(mIntent);
                overridePendingTransition(0,0);
                break;
            case R.id.ll_matador_entrust:
                mIntent = new Intent(this,ActivitySimulationStock.class);
                setIndex(4);
                startActivity(mIntent);
                overridePendingTransition(0,0);
                break;
            case R.id.ll_matador_query:
                mIntent = new Intent(this,ActivitySimulationStock.class);
                setIndex(5);
                startActivity(mIntent);
                overridePendingTransition(0,0);
                break;
        }
    }

    private void finishAll() {

    }
    static int index;
    public static void setIndex(int num){
        index = num;
    }
    public static int getIndex(){
        return index;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CommonUtils.dismissProgressDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(SpTools.getBoolean(CommonUtils.getContext(), Constants.buy,false)){//模拟买入后刷新数据
            SpTools.setBoolean(CommonUtils.getContext(), Constants.buy,false);
            visitInternet();
        }
    }
}
