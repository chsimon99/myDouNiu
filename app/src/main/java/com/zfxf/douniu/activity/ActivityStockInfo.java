package com.zfxf.douniu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.login.ActivityLogin;
import com.zfxf.douniu.adapter.recycleView.StockNoticeAdapter;
import com.zfxf.douniu.bean.SimulationResult;
import com.zfxf.douniu.bean.StockInfo;
import com.zfxf.douniu.bean.StockResult;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.SpTools;
import com.zfxf.douniu.view.FullyLinearLayoutManager;
import com.zfxf.douniu.view.RecycleViewDivider;
import com.zfxf.douniu.view.fragment.FragmentStockDay;
import com.zfxf.douniu.view.fragment.FragmentStockMinitue;
import com.zfxf.douniu.view.fragment.FragmentStockMonth;
import com.zfxf.douniu.view.fragment.FragmentStockWeek;
import com.zhy.http.okhttp.OkHttpUtils;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.zfxf.douniu.R.id.tv_stock_info_sold_count_all;
import static com.zfxf.douniu.R.id.tv_stock_info_zd;

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
    RelativeLayout rl_hudong;//研报
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
    @BindView(R.id.tv_stock_info_buy_name)
    TextView tv_buy_name;//买入股票名称
    @BindView(R.id.tv_stock_info_buy_code)
    TextView tv_buy_code;//买入股票代码
    @BindView(R.id.tv_stock_info_buy_price_minus)
    TextView tv_buy_price_die;//买入股票跌停价
    @BindView(R.id.tv_stock_info_buy_price_add)
    TextView tv_buy_price_zhang;//买入股票涨停价
    @BindView(R.id.et_stock_info_buy_price)
    EditText et_buy_price;//买入股票现价
    @BindView(R.id.ll_stock_info_buy_add)
    LinearLayout ll_buy_add;//买入股票买入+0.01
    @BindView(R.id.ll_stock_info_buy_minus)
    LinearLayout ll_buy_minus;//买入股票买入-0.01
    @BindView(R.id.tv_stock_info_buy_count_all)
    TextView tv_buy_count_a;//买入股票买入全仓
    @BindView(R.id.tv_stock_info_buy_count_half)
    TextView tv_buy_count_h;//买入股票买入半仓
    @BindView(R.id.tv_stock_info_buy_count_three)
    TextView tv_buy_count_t;//买入股票买入1/3
    @BindView(R.id.tv_stock_info_buy_count_four)
    TextView tv_buy_count_f;//买入股票买入1/4
    @BindView(R.id.et_stock_info_buy_count)
    EditText et_buy_count;//买入数量


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
    @BindView(R.id.tv_stock_info_sold_name)
    TextView tv_sold_name;//卖出股票名称
    @BindView(R.id.tv_stock_info_sold_code)
    TextView tv_sold_code;//卖出股票代码
    @BindView(R.id.tv_stock_info_sold_price_minus)
    TextView tv_sold_price_die;//卖出股票跌停价
    @BindView(R.id.tv_stock_info_sold_price_add)
    TextView tv_sold_price_zhang;//卖出股票涨停价
    @BindView(R.id.et_stock_info_sold_price)
    EditText et_sold_price;//卖出股票现价
    @BindView(R.id.ll_stock_info_sold_add)
    LinearLayout ll_sold_add;//卖出股票买入+0.01
    @BindView(R.id.ll_stock_info_sold_minus)
    LinearLayout ll_sold_minus;//卖出股票买入-0.01
    @BindView(tv_stock_info_sold_count_all)
    TextView tv_sold_count_a;//卖出股票买入全仓
    @BindView(R.id.tv_stock_info_sold_count_half)
    TextView tv_sold_count_h;//卖出股票买入半仓
    @BindView(R.id.tv_stock_info_sold_count_three)
    TextView tv_sold_count_t;//卖出股票买入1/3
    @BindView(R.id.tv_stock_info_sold_count_four)
    TextView tv_sold_count_f;//卖出股票买入1/4
    @BindView(R.id.et_stock_info_sold_count)
    EditText et_sold_count;//卖出数量


    @BindView(R.id.ll_stock_info_jiaoyi)
    LinearLayout ll_jiaoyi;//交易
    @BindView(R.id.ll_stock_info_monijiayi)
    LinearLayout ll_moni;//模拟交易
    @BindView(R.id.ll_stock_info_zixuan)
    LinearLayout ll_zixuan;//加自选
    @BindView(R.id.iv_stock_info_zixuan)
    ImageView iv_zixuan;
    @BindView(R.id.tv_stock_info_zixuan)
    TextView tv_myzixuan;
    @BindView(R.id.ll_stock_info_yujing)
    LinearLayout ll_yujing;//预警
    @BindView(R.id.tv_stock_info_buy)
    TextView tv_buy;//自选买入
    @BindView(R.id.tv_stock_info_sold)
    TextView tv_sold;//自选卖出

    @BindView(R.id.tv_stock_info_xj)
    TextView tv_xj;//现价
    @BindView(R.id.tv_stock_info_cz)
    TextView tv_cz;//差价
    @BindView(R.id.tv_stock_info_zf)
    TextView tv_zf;//涨幅
    @BindView(R.id.tv_stock_info_jk)
    TextView tv_jk;//今开
    @BindView(R.id.tv_stock_info_zs)
    TextView tv_zs;//昨收
    @BindView(R.id.tv_stock_info_cje)
    TextView tv_cje;//成交额
    @BindView(R.id.tv_stock_info_cjl)
    TextView tv_cjl;//成交量
    @BindView(R.id.tv_stock_info_zg)
    TextView tv_zg;//最高
    @BindView(tv_stock_info_zd)
    TextView tv_zd;//最低

    private StockNoticeAdapter mStockNoticeAdapter;
    private RecycleViewDivider mDivider;
    private LinearLayoutManager mAnswerManager;

    private Fragment[] mFragments;
    private int mIndex = 0;
    private FragmentTransaction mFt;
    private FragmentStockDay mFragmentStockDay;
    private FragmentStockWeek mFragmentStockWeek;
    private FragmentStockMonth mFragmentStockMonth;
    private FragmentStockMinitue mFragmentStockMinitue;
    private String mCode;
    private boolean first = false;
    private String mg_zzc;
    private String nowPrice;
    private String mgKmsl;
    private String mStock_dt;
    private String mStock_zt;
    private Handler fiveHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_stock_info);
        ButterKnife.bind(this);
        //传过来的标题
        mCode = getIntent().getStringExtra("code");
        String name = getIntent().getStringExtra("name");//传过来的标题
        title.setTextSize(16);
        title.setText(name+"\n"+mCode);
        edit.setVisibility(View.INVISIBLE);
        share.setVisibility(View.INVISIBLE);
        tv_confirm_buy.getPaint().setFakeBoldText(true);//加粗
        tv_confirm_sold.getPaint().setFakeBoldText(true);//加粗
        tv_cancel_buy.getPaint().setFakeBoldText(true);//加粗
        tv_cancel_sold.getPaint().setFakeBoldText(true);//加粗
        initData();
        initListener();
    }
    private void initData() {
        if(mHandler == null){
            mHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what){
                        case 1:
                            if(fiveHandler == null){
                                fiveHandler = FragmentStockMinitue.getmFiveHandler();
                            }
                            if(fiveHandler != null) {
                                Message message = fiveHandler.obtainMessage();
                                message.obj = msg.obj;
                                message.what = 1;
                                fiveHandler.sendMessage(message);
                            }

                            if(((StockInfo)msg.obj).mg_cz.contains("+")){
                                tv_xj.setTextColor(getResources().getColor(R.color.colorRise));
                                tv_cz.setTextColor(getResources().getColor(R.color.colorRise));
                                tv_zf.setTextColor(getResources().getColor(R.color.colorRise));
                            }else {
                                tv_xj.setTextColor(getResources().getColor(R.color.colorFall));
                                tv_cz.setTextColor(getResources().getColor(R.color.colorFall));
                                tv_zf.setTextColor(getResources().getColor(R.color.colorFall));
                            }
                            tv_xj.setText(((StockInfo)msg.obj).mg_xj);
                            tv_cz.setText(((StockInfo)msg.obj).mg_cz);
                            tv_zf.setText(((StockInfo)msg.obj).mg_zf);
                            tv_jk.setText(((StockInfo)msg.obj).mg_jk);
                            tv_zs.setText(((StockInfo)msg.obj).mg_zs);
                            tv_cje.setText(((StockInfo)msg.obj).mg_cje);
                            tv_cjl.setText(((StockInfo)msg.obj).mg_cjl);
                            tv_zg.setText(((StockInfo)msg.obj).mg_zg);
                            tv_zd.setText(((StockInfo)msg.obj).mg_zd);
                            if(!first){
                                if(((StockInfo)msg.obj).zx_status.equals("1")){
                                    iv_zixuan.setImageResource(R.drawable.icon_delete_samll);
                                    tv_myzixuan.setText("删自选");
                                }else {
                                    iv_zixuan.setImageResource(R.drawable.icon_add_small);
                                    tv_myzixuan.setText("自选");
                                }
                                first = true;
                            }
                            break;
                    }
                }
            };
        }
        alwaysRefresh();
        if(mFragmentStockMinitue == null){
            mFragmentStockMinitue = new FragmentStockMinitue();
        }
        if(mFragmentStockDay == null){
            mFragmentStockDay = new FragmentStockDay();
        }
        if(mFragmentStockWeek == null){
            mFragmentStockWeek = new FragmentStockWeek();
        }
        if(mFragmentStockMonth == null){
            mFragmentStockMonth = new FragmentStockMonth();
        }
        if(mFragments == null){
            mFragments = new Fragment[]{mFragmentStockMinitue,mFragmentStockDay,mFragmentStockWeek,mFragmentStockMonth};
        }
        if(mFt == null){
            mFt = getSupportFragmentManager().beginTransaction();
            mFt.add(R.id.fl_stock_info, mFragmentStockMinitue);
            mFt.commit();
        }
        showFragment(0);

        newstype = 1;
        visitGetNewsInfo();
    }
    private int newstype = 0;
    private void visitGetNewsInfo() {
        NewsInternetRequest.getStockNewsInformation(mCode, newstype, new NewsInternetRequest.ForResultStockNewsInfoListener() {
            @Override
            public void onResponseMessage(StockResult result) {
                if(result!=null){
                    if (mStockNoticeAdapter == null) {
                        mStockNoticeAdapter = new StockNoticeAdapter(ActivityStockInfo.this, result.all_news);
                    }
                    if(mAnswerManager == null){
                        mAnswerManager = new FullyLinearLayoutManager(ActivityStockInfo.this);
                    }

                    mRecyclerView.setLayoutManager(mAnswerManager);
                    mRecyclerView.setAdapter(mStockNoticeAdapter);
                    if(mDivider == null){//防止多次加载出现宽度变宽
                        mDivider = new RecycleViewDivider(ActivityStockInfo.this, LinearLayoutManager.HORIZONTAL);
                        mRecyclerView.addItemDecoration(mDivider);
                    }
                    mStockNoticeAdapter.setOnItemClickListener(new StockNoticeAdapter.MyItemClickListener() {
                        @Override
                        public void onItemClick(View v, StockInfo bean) {
                            Intent intent = new Intent(ActivityStockInfo.this, ActivityStockNewsInformation.class);
                            intent.putExtra("type",bean.title);
                            intent.putExtra("time",bean.date);
                            intent.putExtra("url",bean.url);
                            intent.putExtra("newstype",newstype);
                            startActivity(intent);
                            overridePendingTransition(0,0);
                        }
                    });
                }
                if (mStockNoticeAdapter == null) {
                    mStockNoticeAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void visitInternet() {
        NewsInternetRequest.getStockDetailInformation(mCode,new NewsInternetRequest.ForResultStockInfoListener() {
            @Override
            public void onResponseMessage(StockResult result) {
                if(result != null){
                    if(result.station.equals("0")){//修市
                        if(mTask !=null){
                            mTask.stop();
                        }
                    }
                    if(result.gupiao_info !=null){
                        if(mHandler !=null){
                            Message message = mHandler.obtainMessage();
                            message.obj = result.gupiao_info;
                            message.what = 1;
                            mHandler.sendMessage(message);
                        }
                    }
                }
            }
        });
    }

    private static Handler mHandler;
    private static AutoScrollTask mTask;
    private void alwaysRefresh() {
        mTask = new AutoScrollTask();
        mTask.start();
    }
    class AutoScrollTask implements Runnable {
        public void start() {/**开始轮播*/
            mHandler.postDelayed(this, 5000l);
        }
        public void stop() { /**结束轮播*/
           mHandler.removeCallbacks(this);
        }
        @Override
        public void run() {
            visitInternet();
            start();
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
        ll_buy_add.setOnClickListener(this);
        ll_buy_minus.setOnClickListener(this);
        tv_buy_count_a.setOnClickListener(this);
        tv_buy_count_h.setOnClickListener(this);
        tv_buy_count_t.setOnClickListener(this);
        tv_buy_count_f.setOnClickListener(this);
        ll_sold_add.setOnClickListener(this);
        ll_sold_minus.setOnClickListener(this);
        tv_sold_count_a.setOnClickListener(this);
        tv_sold_count_h.setOnClickListener(this);
        tv_sold_count_t.setOnClickListener(this);
        tv_sold_count_f.setOnClickListener(this);
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
        int count = 0;
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
                newstype = 1;
                mStockNoticeAdapter = null;
                visitGetNewsInfo();
                break;
            case R.id.rl_stock_info_notice:
                resetNews();
                v_notice.setVisibility(View.VISIBLE);
                tv_notice.setTextColor(getResources().getColor(R.color.colorTitle));
                newstype = 2;
                mStockNoticeAdapter = null;
                visitGetNewsInfo();
                break;
            case R.id.rl_stock_info_hudong:
                resetNews();
                v_hudong.setVisibility(View.VISIBLE);
                tv_hudong.setTextColor(getResources().getColor(R.color.colorTitle));
                newstype = 3;
                mStockNoticeAdapter = null;
                visitGetNewsInfo();
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
                showFragment(2);
                break;
            case R.id.rl_stock_info_month:
                reset();
                v_month.setVisibility(View.VISIBLE);
                tv_month.setTextColor(getResources().getColor(R.color.colorTitle));
                showFragment(3);
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
            case R.id.ll_stock_info_zixuan://添加
                if(!SpTools.getBoolean(CommonUtils.getContext(), Constants.isLogin,false)){
                    Intent intent = new Intent(this, ActivityLogin.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    return;
                }
                if(tv_myzixuan.getText().toString().equals("自选")){
                    NewsInternetRequest.deleteZiXuanStockInformation(mCode, 0, new NewsInternetRequest.ForResultListener() {
                        @Override
                        public void onResponseMessage(String code) {
                            if(code.equals("10")){
                                CommonUtils.toastMessage("添加自选成功");
                                iv_zixuan.setImageResource(R.drawable.icon_delete_samll);
                                tv_myzixuan.setText("删自选");
                                SpTools.setBoolean(ActivityStockInfo.this, Constants.buy,true);
                            }
                        }
                    });
                }else {
                    NewsInternetRequest.deleteZiXuanStockInformation(mCode, 1, new NewsInternetRequest.ForResultListener() {
                        @Override
                        public void onResponseMessage(String code) {
                            if(code.equals("10")){
                                CommonUtils.toastMessage("删除自选成功");
                                iv_zixuan.setImageResource(R.drawable.icon_add_small);
                                tv_myzixuan.setText("自选");
                                SpTools.setBoolean(ActivityStockInfo.this, Constants.buy,true);
                            }
                        }
                    });
                }
                break;
            case R.id.tv_stock_info_buy://模拟交易买入
                if(!SpTools.getBoolean(CommonUtils.getContext(), Constants.isLogin,false)){
                    Intent intent = new Intent(this, ActivityLogin.class);
                    startActivity(intent);
                    ll_jiaoyi.setVisibility(View.GONE);
                    overridePendingTransition(0,0);
                    return;
                }
                isJiaoYi = !isJiaoYi;
                ll_buy.setVisibility(View.VISIBLE);
                ll_jiaoyi.setVisibility(View.GONE);
                getMoniStockInfoBuy();
                break;
            case R.id.tv_stock_info_sold://模拟交易卖出
                if(!SpTools.getBoolean(CommonUtils.getContext(), Constants.isLogin,false)){
                    Intent intent = new Intent(this, ActivityLogin.class);
                    startActivity(intent);
                    ll_jiaoyi.setVisibility(View.GONE);
                    overridePendingTransition(0,0);
                    return;
                }
                isJiaoYi = !isJiaoYi;
                ll_sold.setVisibility(View.VISIBLE);
                ll_jiaoyi.setVisibility(View.GONE);
                getMoniStockInfoSell();
                break;
            case R.id.rl_stock_info_buy_shut:
            case R.id.rl_stock_info_buy_cancel:
                ll_buy.setVisibility(View.GONE);
                break;
            case R.id.rl_stock_info_sold_shut:
            case R.id.rl_stock_info_sold_cancel:
                ll_sold.setVisibility(View.GONE);
                break;
            case R.id.ll_stock_info_buy_add:
                String s_price = et_buy_price.getText().toString();
                if(TextUtils.isEmpty(s_price)){
                    return;
                }
                Float fp = Float.parseFloat(s_price);
                fp = (float)(Math.round((fp+0.01f)*100))/100;
                if(fp > Float.parseFloat(mStock_zt)){
                    CommonUtils.toastMessage("购买的价格不能大于涨停价");
                    et_buy_price.setText(mStock_zt);
                    return;
                }
                et_buy_price.setText(fp+"");
                break;
            case R.id.ll_stock_info_buy_minus:
                String m_price = et_buy_price.getText().toString();
                if(TextUtils.isEmpty(m_price)){
                    return;
                }
                Float f = Float.parseFloat(m_price);
                f = (float)(Math.round((f-0.01f)*100))/100;
                if(f < Float.parseFloat(mStock_dt)){
                    CommonUtils.toastMessage("购买的价格不能低于跌停价");
                    et_buy_price.setText(mStock_dt);
                    return;
                }
                et_buy_price.setText(f+"");
                break;
            case R.id.tv_stock_info_buy_count_all:
                count= (int) (Float.parseFloat(mg_zzc)/Float.parseFloat(nowPrice));
                count= changeCount(count);
                if(count == 0){
                    CommonUtils.toastMessage("您的资产不足购买100股");
                    return;
                }
                et_buy_count.setText(count+"");
                break;
            case R.id.tv_stock_info_buy_count_half:
                count = (int) ((Float.parseFloat(mg_zzc)*0.5)/Float.parseFloat(nowPrice));
                count= changeCount(count);
                if(count == 0){
                    CommonUtils.toastMessage("您的资产不足购买100股");
                    return;
                }
                et_buy_count.setText(count+"");
                break;
            case R.id.tv_stock_info_buy_count_three:
                count = (int) (Float.parseFloat(mg_zzc)/(Float.parseFloat(nowPrice)*3));
                count= changeCount(count);
                if(count == 0){
                    CommonUtils.toastMessage("您的资产不足购买100股");
                    return;
                }
                et_buy_count.setText(count+"");
                break;
            case R.id.tv_stock_info_buy_count_four:
                count = (int) ((Float.parseFloat(mg_zzc)*0.25)/Float.parseFloat(nowPrice));
                count= changeCount(count);
                if(count == 0){
                    CommonUtils.toastMessage("您的资产不足购买100股");
                    return;
                }
                et_buy_count.setText(count+"");
                break;
            case R.id.rl_stock_info_buy_confirm://确认购买
                if(TextUtils.isEmpty(et_buy_price.getText().toString())){
                    return;
                }
                if(Float.parseFloat(et_buy_price.getText().toString())>Float.parseFloat(mStock_zt)){
                    CommonUtils.toastMessage("购买的价格不能大于涨停价");
                    return;
                }
                if(Float.parseFloat(et_buy_price.getText().toString())<Float.parseFloat(mStock_dt)){
                    CommonUtils.toastMessage("购买的价格不能低于跌停价");
                    return;
                }
                if(TextUtils.isEmpty(et_buy_count.getText().toString())){
                    CommonUtils.toastMessage("购买的数量不能为空");
                    return;
                }
                if(Integer.parseInt(et_buy_count.getText().toString().trim())<100){
                    CommonUtils.toastMessage("购买数量不能小于100股");
                    return;
                }
                String b_count = et_buy_count.getText().toString().trim();
                et_buy_count.setText(changeCount(Integer.parseInt(b_count))+"");
                confirmBuyDialog();
                break;
            case R.id.tv_confirm_dialog_cannel:
                CommonUtils.toastMessage("取消");
                mDialog.dismiss();
                break;
            case R.id.tv_confirm_dialog_confirm:
                if(jiaoyiType == 1){
                    confirmBuy();
                }else if(jiaoyiType == 2){
                    confirmSell();
                }
                mDialog.dismiss();
                break;

            case R.id.ll_stock_info_sold_add:
                String sold_price = et_sold_price.getText().toString();
                if(TextUtils.isEmpty(sold_price)){
                    return;
                }
                Float sold_fp = Float.parseFloat(sold_price);
                sold_fp = (float)(Math.round((sold_fp+0.01f)*100))/100;
                if(sold_fp > Float.parseFloat(mStock_zt)){
                    CommonUtils.toastMessage("卖出价格不能大于涨停价");
                    et_buy_price.setText(mStock_zt);
                    return;
                }
                et_buy_price.setText(sold_fp+"");
                break;
            case R.id.ll_stock_info_sold_minus:
                String sold_m_price = et_sold_price.getText().toString();
                if(TextUtils.isEmpty(sold_m_price)){
                    return;
                }
                Float sold_f = Float.parseFloat(sold_m_price);
                sold_f = (float)(Math.round((sold_f-0.01f)*100))/100;
                if(sold_f < Float.parseFloat(mStock_dt)){
                    CommonUtils.toastMessage("卖出价格不能低于跌停价");
                    et_buy_price.setText(mStock_dt);
                    return;
                }
                et_buy_price.setText(sold_f+"");
                break;
            case R.id.tv_simulation_sold_count_all:
                if(TextUtils.isEmpty(mgKmsl)){
                    return;
                }
                count= (int) (Float.parseFloat(mgKmsl));
                count= changeCount(count);
                if(count == 0){
                    CommonUtils.toastMessage("您的可卖数量不足100股");
                    return;
                }
                et_sold_count.setText(count+"");
                break;
            case R.id.tv_simulation_sold_count_half:
                if(TextUtils.isEmpty(mgKmsl)){
                    return;
                }
                count= (int) (Float.parseFloat(mgKmsl)/2);
                count= changeCount(count);
                if(count == 0){
                    CommonUtils.toastMessage("您的可卖数量不足100股");
                    return;
                }
                et_sold_count.setText(count+"");
                break;
            case R.id.tv_simulation_sold_count_three:
                if(TextUtils.isEmpty(mgKmsl)){
                    return;
                }
                count= (int) (Float.parseFloat(mgKmsl)/3);
                count= changeCount(count);
                if(count == 0){
                    CommonUtils.toastMessage("您的可卖数量不足100股");
                    return;
                }
                et_sold_count.setText(count+"");
                break;
            case R.id.tv_simulation_sold_count_four:
                if(TextUtils.isEmpty(mgKmsl)){
                    return;
                }
                count= (int) (Float.parseFloat(mgKmsl)/4);
                count= changeCount(count);
                if(count == 0){
                    CommonUtils.toastMessage("您的可卖数量不足100股");
                    return;
                }
                et_sold_count.setText(count+"");
                break;
            case R.id.rl_simulation_sold_confirm://确认卖出
                if(TextUtils.isEmpty(et_sold_price.getText().toString())){
                    return;
                }
                if(Float.parseFloat(et_sold_price.getText().toString())>Float.parseFloat(mStock_zt)){
                    CommonUtils.toastMessage("卖出价格不能大于涨停价");
                    return;
                }
                if(Float.parseFloat(et_sold_price.getText().toString())<Float.parseFloat(mStock_dt)){
                    CommonUtils.toastMessage("卖出价格不能低于跌停价");
                    return;
                }
                if(TextUtils.isEmpty(et_sold_count.getText().toString())){
                    CommonUtils.toastMessage("卖出数量不能为空");
                    return;
                }
                if(Integer.parseInt(et_sold_count.getText().toString().trim())<100){
                    CommonUtils.toastMessage("卖出数量不能小于100股");
                    return;
                }
                String s_count = et_sold_count.getText().toString().trim();
                et_sold_count.setText(changeCount(Integer.parseInt(s_count))+"");
                confirmSoldDialog();
                break;
        }
    }

    private AlertDialog mDialog;
    private int jiaoyiType = 0;
    private void confirmBuyDialog() {
        jiaoyiType = 1;
        AlertDialog.Builder builder=new AlertDialog.Builder(this); //先得到构造器
        mDialog = builder.create();
        mDialog.show();
        View view = View.inflate(this, R.layout.activity_confirm_dialog, null);
        mDialog.getWindow().setContentView(view);
        TextView name = (TextView) view.findViewById(R.id.tv_confirm_dialog_name);
        TextView count = (TextView) view.findViewById(R.id.tv_confirm_dialog_count);
        TextView price = (TextView) view.findViewById(R.id.tv_confirm_dialog_price);
        TextView content = (TextView) view.findViewById(R.id.tv_confirm_dialog_content);

        view.findViewById(R.id.tv_confirm_dialog_cannel).setOnClickListener(this);
        view.findViewById(R.id.tv_confirm_dialog_confirm).setOnClickListener(this);
        content.setText("确认买入?");
        count.setText("数量："+et_buy_count.getText().toString());
        price.setText("价格："+et_buy_price.getText().toString());
        name.setText("名称："+tv_buy_name.getText().toString()+"("+tv_buy_code.getText().toString()+")");
    }
    private void confirmSoldDialog(){
        jiaoyiType = 2;
        AlertDialog.Builder builder=new AlertDialog.Builder(this); //先得到构造器
        mDialog = builder.create();
        mDialog.show();
        View view = View.inflate(this, R.layout.activity_confirm_dialog, null);
        mDialog.getWindow().setContentView(view);
        TextView name = (TextView) view.findViewById(R.id.tv_confirm_dialog_name);
        TextView count = (TextView) view.findViewById(R.id.tv_confirm_dialog_count);
        TextView price = (TextView) view.findViewById(R.id.tv_confirm_dialog_price);
        TextView content = (TextView) view.findViewById(R.id.tv_confirm_dialog_content);

        view.findViewById(R.id.tv_confirm_dialog_cannel).setOnClickListener(this);
        view.findViewById(R.id.tv_confirm_dialog_confirm).setOnClickListener(this);
        content.setText("确认卖出?");
        count.setText("数量："+et_sold_count.getText().toString());
        price.setText("价格："+et_sold_count.getText().toString());
        name.setText("名称："+tv_sold_name.getText().toString()+"("+tv_sold_code.getText().toString()+")");
    }

    private void confirmBuy() {
        float v = Float.parseFloat(mg_zzc) / Float.parseFloat(nowPrice);
        if(Float.parseFloat(et_buy_count.getText().toString().trim()) > v){
            CommonUtils.toastMessage("购买数量超出可购买额度");
            et_buy_count.setText("");
            return;
        }
        NewsInternetRequest.simulationBuyStock( tv_buy_code.getText().toString(),tv_buy_name.getText().toString()
                , et_buy_count.getText().toString(),et_buy_price.getText().toString(), new NewsInternetRequest.ForResultListener() {
                    @Override
                    public void onResponseMessage(String info) {
                        if(info.equals("成功")){
                            CommonUtils.toastMessage("购买成功");
                            et_buy_count.setText("");
                            ll_buy.setVisibility(View.GONE);
                            SpTools.setBoolean(CommonUtils.getContext(), Constants.buy,true);//购买股票
                        }else {
                            CommonUtils.toastMessage(info);
                        }
                    }
                });
    }
    private void confirmSell() {
        if(Float.parseFloat(et_sold_count.getText().toString().trim()) > Float.parseFloat(mgKmsl)){
            CommonUtils.toastMessage("卖出数量超出可卖数量");
            et_sold_count.setText("");
            return;
        }
        NewsInternetRequest.simulationSellStock( tv_sold_code.getText().toString(),tv_sold_name.getText().toString()
                , et_sold_count.getText().toString(),et_sold_price.getText().toString(), new NewsInternetRequest.ForResultListener() {
                    @Override
                    public void onResponseMessage(String info) {
                        if(info.equals("成功")){
                            CommonUtils.toastMessage("卖出成功");
                            et_sold_count.setText("");
                            ll_sold.setVisibility(View.GONE);
                            SpTools.setBoolean(CommonUtils.getContext(), Constants.buy,true);//卖出股票
                        }else {
                            CommonUtils.toastMessage(info);
                        }
                    }
                });
    }

    private void getMoniStockInfoBuy() {
        NewsInternetRequest.getSimulationBuyInformation(mCode, new NewsInternetRequest.ForResultSimulationIndexListener() {
            @Override
            public void onResponseMessage(SimulationResult result) {
                if(result.mn_gupiao !=null){
                    tv_buy_name.setText(result.mn_gupiao.mg_name);
                    tv_buy_code.setText(result.mn_gupiao.mg_code);
                    mStock_dt = result.mn_gupiao.mg_dt;
                    tv_buy_price_die.setText(mStock_dt);
                    mStock_zt = result.mn_gupiao.mg_zt;
                    tv_buy_price_zhang.setText(mStock_zt);
                    nowPrice = result.mn_gupiao.mg_xj;
                    et_buy_price.setText(nowPrice);
                    mg_zzc = result.mn_gupiao.mg_zzc;
                }
            }
        });
    }
    private void getMoniStockInfoSell() {
        NewsInternetRequest.getSimulationSellInformation(mCode, new NewsInternetRequest.ForResultSimulationIndexListener() {
            @Override
            public void onResponseMessage(SimulationResult result) {
                if(result.mn_gupiao !=null){
                    tv_sold_name.setText(result.mn_gupiao.mg_name);
                    tv_sold_code.setText(result.mn_gupiao.mg_code);
                    mStock_zt = result.mn_gupiao.mg_zt;
                    mStock_dt = result.mn_gupiao.mg_dt;
                    tv_sold_price_zhang.setText(result.mn_gupiao.mg_zt);
                    tv_sold_price_die.setText(result.mn_gupiao.mg_dt);
                    nowPrice = result.mn_gupiao.mg_xj;
                    et_sold_price.setText(nowPrice);
                    mgKmsl = result.mn_gupiao.mg_kmsl;
                    //差一个持仓数量用来进行数量控制
                }
            }
        });
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().getOkHttpClient().dispatcher().cancelAll();//关闭网络请求
        if(mTask !=null){
            mTask.stop();
            mTask = null;
        }
        mHandler = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }
    public int changeCount(int num){
        if(num < 100){
            return 0;
        }else{
            int i = num / 100;
            DecimalFormat format = new DecimalFormat("0");
            return Integer.parseInt(format.format(i*100));
        }
    }
}
