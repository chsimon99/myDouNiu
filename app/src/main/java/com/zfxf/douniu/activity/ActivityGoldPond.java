package com.zfxf.douniu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.adapter.recycleView.AdvisorAllGoldPondHistoryAdapter;
import com.zfxf.douniu.bean.XuanguResult;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.SpTools;
import com.zfxf.douniu.view.FullyLinearLayoutManager;
import com.zfxf.douniu.view.RecycleViewDivider;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * @author IMXU
 * @time   2017/5/3 13:37
 * @des    金股池 详情
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class ActivityGoldPond extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.iv_base_share)
    ImageView share;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.tv_gold_pond_detail_name)
    TextView tv_name;//姓名
    @BindView(R.id.tv_gold_pond_detail_count)
    TextView tv_count;//股池股票数量
    @BindView(R.id.tv_gold_pond_detail_money)
    TextView tv_money;//费用
    @BindView(R.id.tv_gold_pond_detail_people)
    TextView tv_people;//适用人群
    @BindView(R.id.tv_gold_pond_detail_tese)
    TextView tv_tese;//金股池特色
    @BindView(R.id.tv_gold_pond_detail_subscribe)
    TextView tv_subscribe;//订阅人数
    @BindView(R.id.tv_gold_pond_detail_time)
    TextView tv_time;//发布周期
    @BindView(R.id.tv_gold_pond_detail_brief)
    TextView tv_brief;//风险提示
    @BindView(R.id.tv_gold_pond_detail_confirm)
    TextView tv_confirm;//确认支付

    @BindView(R.id.rl_gold_pond_detail_history)
    RelativeLayout rl_history;//历史战绩
    @BindView(R.id.rl_gold_pond_detail_stock)
    RelativeLayout rl_stock;//入选个股
    @BindView(R.id.ll_gold_pond_detail)
    LinearLayout pay_info;//购买提示信息(与分割线互斥)

    @BindView(R.id.tv_gold_pond_detail_deletime)
    TextView tv_deletime;//到期时间
    @BindView(R.id.iv_gold_pond_detail_jiantou)
    ImageView iv_jiantou;
    @BindView(R.id.v_gold_pond_detail)
    View pay_info_view;//购买提示信息分割线
    @BindView(R.id.v_gold_pond_detail_view)
    View pay_info_view2;//最下方的空白处

    @BindView(R.id.rv_gold_pond_detail_history)
    RecyclerView rv_history;//历史战绩
    private LinearLayoutManager mHistoryManager;
    private AdvisorAllGoldPondHistoryAdapter mGoldPondHistoryAdapter;
    @BindView(R.id.rv_gold_pond_detail_stock)
    RecyclerView rv_stock;//股池
    private LinearLayoutManager mChooseManager;
    private AdvisorAllGoldPondHistoryAdapter mGoldPondStockAdapter;

    private RecycleViewDivider mHistoryDivider;
    private RecycleViewDivider mStockDivider;
    private int mSx_id;
    private int mJgcId;
    private String mStatus;
    private String dj_type;
    private String nickname;
    private String mFee;
    private String mDys;
    private String history_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gold_pond);
        ButterKnife.bind(this);
        title.setText("金股池详情");
        share.setVisibility(View.INVISIBLE);
        edit.setVisibility(View.INVISIBLE);
        tv_name.getPaint().setFakeBoldText(true);//加粗
        tv_money.getPaint().setFakeBoldText(true);//加粗
        tv_confirm.getPaint().setFakeBoldText(true);//加粗

        mSx_id = getIntent().getIntExtra("id", 0);
        mJgcId = getIntent().getIntExtra("jgcId", 0);
        rv_stock.setVisibility(View.GONE);
        pay_info_view.setVisibility(View.GONE);

        initData();
        initListener();
    }

    private void initData() {
        CommonUtils.showProgressDialog(this,"加载中……");
        visitInternet();
    }

    private void visitInternet() {
        NewsInternetRequest.getGoldPondDetailInformation(mSx_id, mJgcId, new NewsInternetRequest.ForResultXuanGuListener() {
            @Override
            public void onResponseMessage(XuanguResult result) {
                //历史战绩
                if(mHistoryManager == null){
                    mHistoryManager = new FullyLinearLayoutManager(ActivityGoldPond.this);
                }
                if(mGoldPondHistoryAdapter == null){
                    mGoldPondHistoryAdapter = new AdvisorAllGoldPondHistoryAdapter(ActivityGoldPond.this, result.ls_jgc);
                }
                rv_history.setLayoutManager(mHistoryManager);
                rv_history.setAdapter(mGoldPondHistoryAdapter);
                if(mHistoryDivider == null){//防止多次加载出现宽度变宽
                    mHistoryDivider = new RecycleViewDivider(ActivityGoldPond.this, LinearLayoutManager.HORIZONTAL);
                    rv_history.addItemDecoration(mHistoryDivider);
                }

                //当前股票
                if(mChooseManager == null){
                    mChooseManager = new FullyLinearLayoutManager(ActivityGoldPond.this);
                }
                if(mGoldPondStockAdapter == null){
                    mGoldPondStockAdapter = new AdvisorAllGoldPondHistoryAdapter(ActivityGoldPond.this, result.rx_jgc);
                }
                rv_stock.setLayoutManager(mChooseManager);
                rv_stock.setAdapter(mGoldPondStockAdapter);
                if(mStockDivider == null){//防止多次加载出现宽度变宽
                    mStockDivider = new RecycleViewDivider(ActivityGoldPond.this, LinearLayoutManager.HORIZONTAL);
                    rv_stock.addItemDecoration(mStockDivider);
                }
                dj_type = result.dn_jgc.dj_type;
                nickname = result.dn_jgc.ud_nickname;
                tv_name.setText(nickname +"的金股池");
                mDys = result.dn_jgc.dy_count;
                tv_subscribe.setText(mDys);
                mFee = result.dn_jgc.dy_fee;
                history_url = result.dn_jgc.history_url;
                //                tv_money.setText("￥"+ mFee);
//                tv_time.setText("发布周期:"+result.dn_jgc.jgcfa_date);
                tv_tese.setText(result.dn_jgc.djf_gcts);
                tv_people.setText(result.dn_jgc.djf_syrq);
                tv_brief.setText(result.dn_jgc.djf_fxts);
                tv_count.setText(result.jgc_count+"股");
                mStatus = result.status;
                if(mStatus.equals("1")){
                    pay_info.setVisibility(View.INVISIBLE);
                    iv_jiantou.setVisibility(View.INVISIBLE);
                    tv_confirm.setVisibility(View.GONE);
                    pay_info_view2.setVisibility(View.GONE);
                    rv_stock.setVisibility(View.VISIBLE);
                    pay_info_view.setVisibility(View.VISIBLE);
                    tv_deletime.setVisibility(View.VISIBLE);
                    tv_deletime.setText("到期时间："+result.dn_jgc.djf_edate);
                }else{
                    pay_info.setVisibility(View.VISIBLE);
                    iv_jiantou.setVisibility(View.VISIBLE);
                    tv_confirm.setVisibility(View.VISIBLE);
                    rv_stock.setVisibility(View.GONE);
                    pay_info_view.setVisibility(View.GONE);
                    tv_deletime.setVisibility(View.GONE);

                }
                CommonUtils.dismissProgressDialog();
            }
        });
    }

    private void initListener() {
        back.setOnClickListener(this);
        share.setOnClickListener(this);
        rl_history.setOnClickListener(this);
        rl_stock.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);
    }
//    boolean isBuy = false;//后期放在服务器上获取是否购买信息
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back:
                finishAll();
                finish();
                break;
            case R.id.iv_base_share:

                break;
            case R.id.tv_gold_pond_detail_confirm:
                toPay();
                break;
            case R.id.rl_gold_pond_detail_history:
//                Intent intent = new Intent(this,Activityhistory.class);
//                intent.putExtra("sx_id",mSx_id+"");
//                intent.putExtra("type",dj_type);
                Intent intent = new Intent(this,ActivityGoldHistory.class);
                intent.putExtra("url",history_url);
                intent.putExtra("name","历史战绩");
                startActivity(intent);
                overridePendingTransition(0,0);
                break;
            case R.id.rl_gold_pond_detail_stock:
                if(mStatus.equals("1")){
                    pay_info.setVisibility(View.INVISIBLE);
                    iv_jiantou.setVisibility(View.INVISIBLE);
                    rv_stock.setVisibility(View.VISIBLE);
                    tv_confirm.setVisibility(View.GONE);
                    pay_info_view2.setVisibility(View.GONE);
                    pay_info_view.setVisibility(View.VISIBLE);

                }else{
                    toPay();
                    pay_info.setVisibility(View.VISIBLE);
                    iv_jiantou.setVisibility(View.VISIBLE);
                    rv_stock.setVisibility(View.GONE);
                    pay_info_view.setVisibility(View.GONE);

                }
                break;
        }
    }

    private void toPay() {
        Intent intent = new Intent(this,ActivityToPay.class);
        intent.putExtra("info","金股池,"+mSx_id+","+mJgcId);
        intent.putExtra("type","金股池");
        intent.putExtra("from","金股池");
        intent.putExtra("count",mFee);
        intent.putExtra("planId",mJgcId);
        intent.putExtra("sx_id",mSx_id+"");
        intent.putExtra("dycount",mDys);
        startActivity(intent);
        overridePendingTransition(0,0);
    }

    private void finishAll() {
        if(!SpTools.getBoolean(this, Constants.isOpenApp,false)){
            Intent intent = new Intent(this, MainActivityTabHost.class);
            startActivity(intent);
            overridePendingTransition(0,0);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CommonUtils.dismissProgressDialog();
        finishAll();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(SpTools.getBoolean(this, Constants.buy,false)){//如果已经支付成功，重新刷新数据
            CommonUtils.showProgressDialog(this,"加载中……");
            mGoldPondStockAdapter = null;
            SpTools.setBoolean(this, Constants.buy,false);
            visitInternet();
        }
    }
}
