package com.zfxf.douniu.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zfxf.douniu.R;
import com.zfxf.douniu.adapter.recycleView.MatadorPositonAdapter;
import com.zfxf.douniu.bean.SimulationDetail;
import com.zfxf.douniu.bean.SimulationResult;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.SpTools;
import com.zfxf.douniu.view.FullyLinearLayoutManager;
import com.zfxf.douniu.view.RecycleViewDivider;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * @author IMXU
 * @time   2017/5/3 13:28
 * @des    斗牛士主页
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class ActivityMatador extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.tv_matador_name)
    TextView tv_name;
    @BindView(R.id.tv_matador_fans)
    TextView tv_fans;
    @BindView(R.id.tv_matador_type)
    TextView tv_type;
    @BindView(R.id.tv_matador_follow)
    TextView tv_follow;//关注
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

    @BindView(R.id.rl_matador_more)
    RelativeLayout rl_more;//更多

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
    private String mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matador);
        ButterKnife.bind(this);
        title.setText("斗牛士主页");
        edit.setVisibility(View.INVISIBLE);
        tv_follow.getPaint().setFakeBoldText(true);//加粗
        asset.getPaint().setFakeBoldText(true);//加粗
        ratio.getPaint().setFakeBoldText(true);//加粗
        trade.getPaint().setFakeBoldText(true);//加粗
        total_income.getPaint().setFakeBoldText(true);//加粗
        month_income.getPaint().setFakeBoldText(true);//加粗
        week_income.getPaint().setFakeBoldText(true);//加粗
        tv_postion.getPaint().setFakeBoldText(true);//加粗
        mId = getIntent().getStringExtra("id");
        initData();
        initListener();
    }

    private void initData() {

        CommonUtils.showProgressDialog(this,"加载中……");
        NewsInternetRequest.getSimulationIndexInformation(mId,new NewsInternetRequest.ForResultSimulationIndexListener() {
            @Override
            public void onResponseMessage(SimulationResult result) {
                SimulationDetail detail = result.user_detail;
                if(detail.url.contains("http")){
                    Glide.with(ActivityMatador.this).load(detail.url)
                            .placeholder(R.drawable.home_adviosr_img)
                            .bitmapTransform(new CropCircleTransformation(ActivityMatador.this)).into(img);
                }else {
                    String picUrl = getResources().getString(R.string.file_host_address)
                            +getResources().getString(R.string.showpic)
                            +detail.url;
                    Glide.with(ActivityMatador.this).load(picUrl)
                            .placeholder(R.drawable.home_adviosr_img)
                            .bitmapTransform(new CropCircleTransformation(ActivityMatador.this)).into(img);
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
                if(detail.is_attention.equals("1")){
                    tv_follow.setText("已关注");
                }else {
                    tv_follow.setText("关注");
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
                    mLayoutManager = new FullyLinearLayoutManager(ActivityMatador.this);
                }
                if(mPositonAdapter == null){
                    mPositonAdapter = new MatadorPositonAdapter(ActivityMatador.this, result.mn_chigu);
                }
                if(mDivider == null){//防止多次加载出现宽度变宽
                    mDivider = new RecycleViewDivider(ActivityMatador.this, LinearLayoutManager.HORIZONTAL);
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
        tv_follow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back:
                finishAll();
                finish();
                break;
            case R.id.tv_matador_follow:
                if(tv_follow.getText().toString().equals("关注")){
                    subscribeInternet(Integer.parseInt(mId),6,0);
                }else {
                    subscribeInternet(Integer.parseInt(mId),6,1);
                }
                break;
        }
    }
    private void subscribeInternet(int id, final int typyid, final int type) {
        NewsInternetRequest.subscribeAndCannel(id+"", typyid, type, new NewsInternetRequest.ForResultListener() {
            @Override
            public void onResponseMessage(String count) {
                if (TextUtils.isEmpty(count)) {
                    return;
                }
                if (type == 0) {
                    CommonUtils.toastMessage("关注成功");
                    tv_follow.setText("已关注");
                } else {
                    CommonUtils.toastMessage("取消关注成功");
                    tv_follow.setText("关注");
                }
                tv_fans.setText(count);
                SpTools.setBoolean(CommonUtils.getContext(), Constants.alreadyGuanzhu,true);//存储关注变动
            }
        },getResources().getString(R.string.userdy));
    }
    private void finishAll() {

    }

}
