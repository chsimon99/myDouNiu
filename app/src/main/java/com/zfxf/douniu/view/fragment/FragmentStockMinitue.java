package com.zfxf.douniu.view.fragment;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.bean.StockInfo;
import com.zfxf.douniu.bean.StockResult;
import com.zfxf.douniu.chart.DataHelper;
import com.zfxf.douniu.chart.KLineEntity;
import com.zfxf.douniu.chart.MChartAdapter;
import com.zfxf.douniu.chart.MTrendView;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.view.chart.formatter.TimeFormatter;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author IMXU
 * @time   2017/5/3 13:25
 * @des    分时线
 * 邮箱：butterfly_xu@sina.com
 *
*/

public class FragmentStockMinitue extends BaseFragment {
    private View view;

    @BindView(R.id.mt_view)
    MTrendView minView;
//    @BindView(R.id.mt_order_view)
//    OrderView orderView;

    @BindView(R.id.tv_stock_minitue_buy5_price)
    TextView tv_buy5_price;
    @BindView(R.id.tv_stock_minitue_buy5_count)
    TextView tv_buy5_count;
    @BindView(R.id.tv_stock_minitue_buy4_price)
    TextView tv_buy4_price;
    @BindView(R.id.tv_stock_minitue_buy4_count)
    TextView tv_buy4_count;
    @BindView(R.id.tv_stock_minitue_buy3_price)
    TextView tv_buy3_price;
    @BindView(R.id.tv_stock_minitue_buy3_count)
    TextView tv_buy3_count;
    @BindView(R.id.tv_stock_minitue_buy2_price)
    TextView tv_buy2_price;
    @BindView(R.id.tv_stock_minitue_buy2_count)
    TextView tv_buy2_count;
    @BindView(R.id.tv_stock_minitue_buy1_price)
    TextView tv_buy1_price;
    @BindView(R.id.tv_stock_minitue_buy1_count)
    TextView tv_buy1_count;

    @BindView(R.id.tv_stock_minitue_sell5_price)
    TextView tv_sell5_price;
    @BindView(R.id.tv_stock_minitue_sell5_count)
    TextView tv_sell5_count;
    @BindView(R.id.tv_stock_minitue_sell4_price)
    TextView tv_sell4_price;
    @BindView(R.id.tv_stock_minitue_sell4_count)
    TextView tv_sell4_count;
    @BindView(R.id.tv_stock_minitue_sell3_price)
    TextView tv_sell3_price;
    @BindView(R.id.tv_stock_minitue_sell3_count)
    TextView tv_sell3_count;
    @BindView(R.id.tv_stock_minitue_sell2_price)
    TextView tv_sell2_price;
    @BindView(R.id.tv_stock_minitue_sell2_count)
    TextView tv_sell2_count;
    @BindView(R.id.tv_stock_minitue_sell1_price)
    TextView tv_sell1_price;
    @BindView(R.id.tv_stock_minitue_sell1_count)
    TextView tv_sell1_count;

    private static MChartAdapter mAdapter;

    private static Float stock_zs;
    private List<StockInfo> oldStockInfos = new ArrayList<StockInfo>();
    private List<StockInfo> newStockInfos = new ArrayList<StockInfo>();
    private static FragmentActivity activity;
    private Timer mTimer;
    private static StockResult stockResult;
    @Override
    public View initView(LayoutInflater inflater) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_stock_minitue, null);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void initdata() {
        super.initdata();
        mHandler = new Handler();
        mFiveHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        showFive(((StockInfo)msg.obj),Float.parseFloat(((StockInfo)msg.obj).mg_zs));
                        break;
                }
            }
        };
        drawKLine();
        activity = getActivity();
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                visitInternet();
            }
        },0,5000l);
        if(mAdapter == null){
            mAdapter = new MChartAdapter();
        }
        minView.setAdapter(mAdapter);
        minView.setDateTimeFormatter(new TimeFormatter());
        minView.setGridRows(4);
        minView.setGridColumns(4);

    }

    private void visitInternet() {
        NewsInternetRequest.getStockInformation(activity.getIntent().getStringExtra("code"), activity, null, new NewsInternetRequest.ForResultStockFensiInfoListener() {
            @Override
            public void onResponseMessage(StockResult result) {
                if(result !=null){
                    stockResult = result;
                    if(result.station.equals("0")){//修市
                        if(mTimer !=null){
                            mTimer.cancel();
                        }
                    }
                }
            }
        });
    }

    private void clearData() {
        datas.clear();
        datas = new ArrayList<>();
    }

    private void drawKLine() {
        alwaysRefresh();
    }
    private static Handler mHandler;
    private AutoScrollTask mTask;
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
            drawStock();
            start();
        }
    }

    private void drawStock() {
        if(stockResult !=null){
            if(stockResult.mg_gupiao.size()<=0){
                return;
            }
            stock_zs = Float.parseFloat(stockResult.gupiao_info.mg_zs);
//            showFive(stockResult.gupiao_info,stock_zs);
            if(oldStockInfos.size() == (stockResult.mg_gupiao.size())){//数据相同
                return;
            }else if(oldStockInfos.size() > (stockResult.mg_gupiao.size())){
                return;
            }
            if(oldStockInfos.size() == 0){
                newStockInfos = stockResult.mg_gupiao;
                oldStockInfos = stockResult.mg_gupiao;
            }
            if(oldStockInfos.size() < stockResult.mg_gupiao.size()){//数据有刷新
                newStockInfos = stockResult.mg_gupiao.subList(oldStockInfos.size(),stockResult.mg_gupiao.size());
                oldStockInfos = stockResult.mg_gupiao;
            }
            setTrendMin(newStockInfos);
            DataHelper.calculate(datas);
            mAdapter.addFooterData(datas);
            clearData();
//            minView.startAnimation();
        }
    }

    private List<KLineEntity> datas = new ArrayList<>();

    private void setTrendMin(List<StockInfo> data) {
        for (int i = 0; i < data.size(); i++) {
            KLineEntity min = new KLineEntity();
            min.isMinDraw = true;
            min.Close = Float.parseFloat(data.get(i).trade_price);
            min.avPrice = Float.parseFloat(data.get(i).avg);
            min.lastPrice = i >= 0 ? Float.parseFloat(data.get(i).trade_price) : stock_zs;
            min.lastClosePrice = stock_zs;
            min.Volume = Float.parseFloat(data.get(i).trade_number) * 100;
            min.Date = data.get(i).datetime;
            float high = Float.parseFloat(data.get(i).high_price) - stock_zs;
            float low = stock_zs - Float.parseFloat(data.get(i).low_price);

            min.High = high > low ? stock_zs+high: stock_zs+low;
            min.Low = high >low ? stock_zs-high:stock_zs-low;
            datas.add(min);
        }
    }
    @Override
    public void initListener() {
        super.initListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().getOkHttpClient().dispatcher().cancelAll();
        if(mTask!=null){
            mTask.stop();
        }
        if(mTimer !=null){
            mTimer.cancel();
        }
        mAdapter = null;
        mTask = null;
    }

    private void showFive(StockInfo bean, Float zs) {
        setColorAndValue(Float.parseFloat(bean.PricePrice1),zs,tv_sell1_price,tv_sell1_count,bean.SellVolume1);
        setColorAndValue(Float.parseFloat(bean.PricePrice2),zs,tv_sell2_price,tv_sell2_count,bean.SellVolume2);
        setColorAndValue(Float.parseFloat(bean.PricePrice3),zs,tv_sell3_price,tv_sell3_count,bean.SellVolume3);
        setColorAndValue(Float.parseFloat(bean.PricePrice4),zs,tv_sell4_price,tv_sell4_count,bean.SellVolume4);
        setColorAndValue(Float.parseFloat(bean.PricePrice5),zs,tv_sell5_price,tv_sell5_count,bean.SellVolume5);

        setColorAndValue(Float.parseFloat(bean.BuyPrice1),zs,tv_buy1_price,tv_buy1_count,bean.BuyVolume1);
        setColorAndValue(Float.parseFloat(bean.BuyPrice2),zs,tv_buy2_price,tv_buy2_count,bean.BuyVolume2);

        setColorAndValue(Float.parseFloat(bean.BuyPrice3),zs,tv_buy3_price,tv_buy3_count,bean.BuyVolume3);
        setColorAndValue(Float.parseFloat(bean.BuyPrice4),zs,tv_buy4_price,tv_buy4_count,bean.BuyVolume4);
        setColorAndValue(Float.parseFloat(bean.BuyPrice5),zs,tv_buy5_price,tv_buy5_count,bean.BuyVolume5);
    }

    private void setColorAndValue(Float price, Float zs, TextView tv_price, TextView tv_count, String Volume) {
        if(activity != null){
            if(price == 0){
                tv_price.setText("--");
                tv_count.setText("--");
                return;
            }
            if(price > zs){
                tv_price.setTextColor(activity.getResources().getColor(R.color.colorRise));
                tv_count.setTextColor(activity.getResources().getColor(R.color.colorRise));
            }else if(price == zs){
                tv_price.setTextColor(activity.getResources().getColor(R.color.titleText));
                tv_count.setTextColor(activity.getResources().getColor(R.color.titleText));
            }else {
                tv_price.setTextColor(activity.getResources().getColor(R.color.colorFall));
                tv_count.setTextColor(activity.getResources().getColor(R.color.colorFall));
            }
            tv_price.setText(price+"");
            tv_count.setText(Volume);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
            //onPause
            if(mTask !=null){
                mTask.stop();
            }
            if(mTimer !=null){
                mTimer.cancel();
            }
        } else {
            //onResume
            if(mTask !=null){
                mTask.start();
            }
            if(mTimer !=null){
                mTimer = new Timer();
                mTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        visitInternet();
                    }
                },0,5000l);
            }
        }
    }

    public static Handler mFiveHandler;
    public static Handler getmFiveHandler(){
        return mFiveHandler;
    }
}