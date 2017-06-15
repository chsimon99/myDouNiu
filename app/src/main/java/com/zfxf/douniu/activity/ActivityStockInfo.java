package com.zfxf.douniu.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.adapter.recycleView.StockNoticeAdapter;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.view.FullyLinearLayoutManager;
import com.zfxf.douniu.view.RecycleViewDivider;
import com.zfxf.douniu.view.fragment.FragmentStockDay;
import com.zfxf.douniu.view.fragment.FragmentStockMinitue;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author IMXU
 * @time   2017/5/3 13:41
 * @des    各股 详情
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class ActivityStockInfo extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.iv_base_share)
    ImageView share;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.rv_stock_info)
    RecyclerView mRecyclerView;
    @BindView(R.id.rl_stock_info_day)
    RelativeLayout rl_day;//日k
    @BindView(R.id.rl_stock_info_week)
    RelativeLayout rl_week;//周k
    @BindView(R.id.rl_stock_info_month)
    RelativeLayout rl_month;//月k
    @BindView(R.id.rl_stock_info_minitue)
    RelativeLayout rl_minitue;//分时
    @BindView(R.id.v_stock_info_minitue)
    View v_minitue;
    @BindView(R.id.v_stock_info_day)
    View v_day;
    @BindView(R.id.v_stock_info_week)
    View v_week;
    @BindView(R.id.v_stock_info_month)
    View v_month;

    @BindView(R.id.tv_stock_info_minitue)
    TextView tv_minitue;
    @BindView(R.id.tv_stock_info_day)
    TextView tv_day;
    @BindView(R.id.tv_stock_info_week)
    TextView tv_week;
    @BindView(R.id.tv_stock_info_month)
    TextView tv_month;

    @BindView(R.id.rl_stock_info_zixun)
    RelativeLayout rl_zixun;//资讯
    @BindView(R.id.tv_stock_info_zixun)
    TextView tv_zixun;
    @BindView(R.id.v_stock_info_zixun)
    View v_zixun;
    @BindView(R.id.rl_stock_info_notice)
    RelativeLayout rl_notice;//公告
    @BindView(R.id.tv_stock_info_notice)
    TextView tv_notice;
    @BindView(R.id.v_stock_info_notice)
    View v_notice;
    @BindView(R.id.rl_stock_info_hudong)
    RelativeLayout rl_hudong;//活动
    @BindView(R.id.tv_stock_info_hudong)
    TextView tv_hudong;
    @BindView(R.id.v_stock_info_hudong)
    View v_hudong;

    @BindView(R.id.ll_stock_info_buy)
    LinearLayout ll_buy;//买入界面
    @BindView(R.id.rl_stock_info_buy_shut)
    RelativeLayout rl_buy_shut;//买入界面关闭
    @BindView(R.id.rl_stock_info_buy_confirm)
    RelativeLayout rl_buy_confirm;//买入确认
    @BindView(R.id.rl_stock_info_buy_cancel)
    RelativeLayout rl_buy_cancel;//买入取消
    @BindView(R.id.tv_stock_info_buy_confirm)
    TextView tv_confirm_buy;
    @BindView(R.id.tv_stock_info_buy_cancel)
    TextView tv_cancel_buy;
    @BindView(R.id.ll_stock_info_sold)
    LinearLayout ll_sold;//卖出界面
    @BindView(R.id.rl_stock_info_sold_shut)
    RelativeLayout rl_sold_shut;//卖出界面关闭
    @BindView(R.id.rl_stock_info_sold_confirm)
    RelativeLayout rl_sold_confirm;//卖出确认
    @BindView(R.id.rl_stock_info_sold_cancel)
    RelativeLayout rl_sold_cancel;//卖出取消
    @BindView(R.id.tv_stock_info_sold_confirm)
    TextView tv_confirm_sold;
    @BindView(R.id.tv_stock_info_sold_cancel)
    TextView tv_cancel_sold;


    @BindView(R.id.ll_stock_info_jiaoyi)
    LinearLayout ll_jiaoyi;//交易
    @BindView(R.id.ll_stock_info_monijiayi)
    LinearLayout ll_moni;//模拟交易
    @BindView(R.id.ll_stock_info_zixuan)
    LinearLayout ll_zixuan;//加自选
    @BindView(R.id.ll_stock_info_yujing)
    LinearLayout ll_yujing;//预警
    @BindView(R.id.tv_stock_info_buy)
    TextView tv_buy;//自选买入
    @BindView(R.id.tv_stock_info_sold)
    TextView tv_sold;//自选卖出

    private StockNoticeAdapter mStockNoticeAdapter;
    private RecycleViewDivider mDivider;
    private LinearLayoutManager mAnswerManager;
    private List<String> datas = new ArrayList<String>();
    private int totlePage = 0;
    private int currentPage = 1;

    private Fragment[] mFragments;
    private int mIndex = 0;
    private FragmentTransaction mFt;
    private FragmentStockDay mFragmentStockDay;
    private FragmentStockMinitue mFragmentStockMinitue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_stock_info);
        ButterKnife.bind(this);
        String type_title = getIntent().getStringExtra("type");//传过来的标题
        title.setTextSize(16);
        title.setText("格林美\n002403");
        edit.setVisibility(View.INVISIBLE);
        share.setVisibility(View.VISIBLE);
        tv_confirm_buy.getPaint().setFakeBoldText(true);//加粗
        tv_confirm_sold.getPaint().setFakeBoldText(true);//加粗
        tv_cancel_buy.getPaint().setFakeBoldText(true);//加粗
        tv_cancel_sold.getPaint().setFakeBoldText(true);//加粗
        initData();
        initListener();
    }
    private void initData() {
        if(mFragmentStockMinitue == null){
            mFragmentStockMinitue = new FragmentStockMinitue();
        }
        if(mFragmentStockDay == null){
            mFragmentStockDay = new FragmentStockDay();
        }
        if(mFragments == null){
            mFragments = new Fragment[]{mFragmentStockMinitue,mFragmentStockDay};
        }
        if(mFt == null){
            mFt = getSupportFragmentManager().beginTransaction();
            mFt.add(R.id.fl_stock_info, mFragmentStockMinitue);
            mFt.commit();
        }
        showFragment(0);

        if(datas.size() == 0){
            datas.add("");
            datas.add("");
            datas.add("");
            datas.add("");
            datas.add("");
            datas.add("");
            datas.add("");
        }

        if (mStockNoticeAdapter == null) {
            mStockNoticeAdapter = new StockNoticeAdapter(ActivityStockInfo.this, datas);
        }
        if(mAnswerManager == null){
            mAnswerManager = new FullyLinearLayoutManager(this);
        }

        mRecyclerView.setLayoutManager(mAnswerManager);
        mRecyclerView.setAdapter(mStockNoticeAdapter);
        if(mDivider == null){//防止多次加载出现宽度变宽
            mDivider = new RecycleViewDivider(ActivityStockInfo.this, LinearLayoutManager.HORIZONTAL);
            mRecyclerView.addItemDecoration(mDivider);
        }

    }
    private void initListener() {
        back.setOnClickListener(this);
        rl_day.setOnClickListener(this);
        rl_week.setOnClickListener(this);
        rl_month.setOnClickListener(this);
        rl_minitue.setOnClickListener(this);
        rl_zixun.setOnClickListener(this);
        rl_notice.setOnClickListener(this);
        rl_hudong.setOnClickListener(this);
        ll_moni.setOnClickListener(this);
        ll_zixuan.setOnClickListener(this);
        ll_yujing.setOnClickListener(this);
        tv_buy.setOnClickListener(this);
        tv_sold.setOnClickListener(this);
        rl_buy_shut.setOnClickListener(this);
        rl_sold_shut.setOnClickListener(this);
        rl_buy_confirm.setOnClickListener(this);
        rl_buy_cancel.setOnClickListener(this);
        rl_sold_confirm.setOnClickListener(this);
        rl_sold_cancel.setOnClickListener(this);
    }

    private void showFragment(int index) {
        if(mIndex == index){
            return;
        }
        mFt = getSupportFragmentManager().beginTransaction();
        mFt.hide(mFragments[mIndex]);//隐藏
        if(!mFragments[index].isAdded()){
            mFt.add(R.id.fl_stock_info,mFragments[index]).show(mFragments[index]);
        }else{
            mFt.show(mFragments[index]);
        }
        mFt.commit();
        mIndex = index;
    }
    private boolean isJiaoYi =false;//交易按钮的显示和隐藏
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back:
                finishAll();
                finish();
                break;
            case R.id.iv_base_edit:
                break;
            case R.id.rl_stock_info_zixun:
                resetNews();
                v_zixun.setVisibility(View.VISIBLE);
                tv_zixun.setTextColor(getResources().getColor(R.color.colorTitle));
                break;
            case R.id.rl_stock_info_notice:
                resetNews();
                v_notice.setVisibility(View.VISIBLE);
                tv_notice.setTextColor(getResources().getColor(R.color.colorTitle));
                break;
            case R.id.rl_stock_info_hudong:
                resetNews();
                v_hudong.setVisibility(View.VISIBLE);
                tv_hudong.setTextColor(getResources().getColor(R.color.colorTitle));
                break;
            case R.id.rl_stock_info_day:
                reset();
                v_day.setVisibility(View.VISIBLE);
                tv_day.setTextColor(getResources().getColor(R.color.colorTitle));
                showFragment(1);
                break;
            case R.id.rl_stock_info_week:
                reset();
                v_week.setVisibility(View.VISIBLE);
                tv_week.setTextColor(getResources().getColor(R.color.colorTitle));
                showFragment(1);
                break;
            case R.id.rl_stock_info_month:
                break;
            case R.id.rl_stock_info_minitue:
                reset();
                v_minitue.setVisibility(View.VISIBLE);
                tv_minitue.setTextColor(getResources().getColor(R.color.colorTitle));
                showFragment(0);
                break;
            case R.id.ll_stock_info_monijiayi:
                if(isJiaoYi){
                    ll_jiaoyi.setVisibility(View.GONE);
                    isJiaoYi = !isJiaoYi;
                }else {
                    ll_jiaoyi.setVisibility(View.VISIBLE);
                    isJiaoYi = !isJiaoYi;
                }
                break;
            case R.id.ll_stock_info_zixuan:

                break;
            case R.id.tv_stock_info_buy:
                isJiaoYi = !isJiaoYi;
                ll_buy.setVisibility(View.VISIBLE);
                ll_jiaoyi.setVisibility(View.GONE);
                break;
            case R.id.tv_stock_info_sold:
                isJiaoYi = !isJiaoYi;
                ll_sold.setVisibility(View.VISIBLE);
                ll_jiaoyi.setVisibility(View.GONE);
                break;
            case R.id.rl_stock_info_buy_shut:
            case R.id.rl_stock_info_buy_cancel:
                ll_buy.setVisibility(View.GONE);
                break;
            case R.id.rl_stock_info_sold_shut:
            case R.id.rl_stock_info_sold_cancel:
                ll_sold.setVisibility(View.GONE);
                break;
        }
    }

    private void resetNews() {
        tv_zixun.setTextColor(getResources().getColor(R.color.titleText));
        tv_notice.setTextColor(getResources().getColor(R.color.titleText));
        tv_hudong.setTextColor(getResources().getColor(R.color.titleText));
        v_zixun.setVisibility(View.GONE);
        v_hudong.setVisibility(View.GONE);
        v_notice.setVisibility(View.GONE);
    }

    private void reset(){
        v_day.setVisibility(View.GONE);
        v_month.setVisibility(View.GONE);
        v_minitue.setVisibility(View.GONE);
        v_week.setVisibility(View.GONE);
        tv_day.setTextColor(getResources().getColor(R.color.titleText));
        tv_month.setTextColor(getResources().getColor(R.color.titleText));
        tv_minitue.setTextColor(getResources().getColor(R.color.titleText));
        tv_week.setTextColor(getResources().getColor(R.color.titleText));
    }

    private void finishAll() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CommonUtils.dismissProgressDialog();
    }
}
