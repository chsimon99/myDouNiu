package com.zfxf.douniu.view.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zfxf.douniu.R;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.bean.StockInfo;
import com.zfxf.douniu.bean.StockResult;
import com.zfxf.douniu.chart.DataHelper;
import com.zfxf.douniu.chart.KChartAdapter;
import com.zfxf.douniu.chart.KChartView;
import com.zfxf.douniu.chart.KLineEntity;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.view.chart.formatter.DateFormatter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author IMXU
 * @time   2017/5/3 13:25
 * @des    日k线
 * 邮箱：butterfly_xu@sina.com
 *
*/

public class FragmentStockMonth extends BaseFragment {
    private View view;

    @BindView(R.id.kchart_month_view)
    KChartView mKChartView;
    private KChartAdapter mAdapter;

    private static StockResult stockResult;
    private int totlePage = 0;
    private int currentPage = 1;

    @Override
    public View initView(LayoutInflater inflater) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_stock_month, null);
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
        if(mAdapter == null){
            mAdapter = new KChartAdapter();
        }
        mKChartView.setAdapter(mAdapter);
        mKChartView.setDateTimeFormatter(new DateFormatter());
        mKChartView.setGridRows(4);
        mKChartView.setGridColumns(4);
        visitInternet();

//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                String fileName = "ibm.json"; //k线图的数据
//                String res = "";
//                try {
//                    InputStream in = getResources().getAssets().open(fileName);
//                    int length = in.available();
//                    byte[] buffer = new byte[length];
//                    in.read(buffer);
//                    res = EncodingUtils.getString(buffer, "UTF-8");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                final List<KLineEntity> data = new Gson().fromJson(res, new TypeToken<List<KLineEntity>>() {
//                }.getType());
//                DataHelper.calculate(data);
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mAdapter.addFooterData(data);
//                        mKChartView.startAnimation();
//
//                        KLineEntity entity = data.get(data.size() - 1);
//                        price.setText(en
//                        tity.getClosePrice() + "");
//                        max.setText("最高：" + entity.getHighPrice() + "");
//                        min.setText("最低：" + entity.getLowPrice() + "");
//                        yesPrice.setText("开盘：" + entity.getOpenPrice() + "");
//                        shoupan.setText("收盘：" + entity.getClosePrice() + "");
//                    }
//                });
//            }
//        }).start();
//        mAdapter = new KChartAdapter();
//        mKChartView.setAdapter(mAdapter);
//        mKChartView.setDateTimeFormatter(new DateFormatter());
//        mKChartView.setGridRows(4);
//        mKChartView.setGridColumns(4);
//        //长按的触发事件
//        mKChartView.setOnSelectedChangedListener(new IKChartView.OnSelectedChangedListener() {
//            @Override
//            public void onSelectedChanged(IKChartView view, Object point, int index) {
//                final KLineEntity data = (KLineEntity) point;
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        price.setText(data.getClosePrice() + "");
//                        max.setText("最高：" + data.getHighPrice() + "");
//                        min.setText("最低：" + data.getLowPrice() + "");
//                        yesPrice.setText("开盘：" + data.getOpenPrice() + "");
//                        shoupan.setText("收盘：" + data.getClosePrice() + "");
//                        percent.setText(data.get + "");
//                    }
//                });
//                Log.i("onSelectedChanged", "index:" + index + " closePrice:" + data.getClosePrice());
//            }
//        });
    }

    private void visitInternet() {
        NewsInternetRequest.getStockKLineInformation(getActivity().getIntent().getStringExtra("code"), currentPage, "2", new NewsInternetRequest.ForResultStockKLineInfoListener() {
            @Override
            public void onResponseMessage(StockResult result) {
                if(result == null){
                    isRfreshing = false;
                    return;
                }
                if(result.mn_gupiao!=null){
                    totlePage = Integer.parseInt(result.total);
                    if (totlePage > 0 && currentPage <= totlePage){
                        if(result.mn_gupiao.size()>0){
                            stockResult = result;
                            drawLine();
                        }
                        currentPage++;
                    }
                    isRfreshing = false;
                }
            }
        });
    }

    private void drawLine() {
        setTrendMin(stockResult.mn_gupiao);
        DataHelper.calculate(datas);
        mAdapter.addFooterData(datas);
        clearData();
    }

    private boolean isRfreshing = false;
    @Override
    public void initListener() {
        super.initListener();
        mKChartView.setStockListener(new KChartView.MyStockListener() {
            @Override
            public void leftSide() {
                if(!isRfreshing){
                    visitInternet();
                    isRfreshing = true;
                }
            }
            @Override
            public void rightSide() {
            }
        });
    }

    private List<KLineEntity> datas = new ArrayList<>();

    private void setTrendMin(List<StockInfo> data) {
        for (int i = 0; i < data.size(); i++) {
            KLineEntity min = new KLineEntity();
            min.Close = Float.parseFloat(data.get(i).close);
            min.Date = data.get(i).date;
            min.High = Float.parseFloat(data.get(i).high);
            min.Low = Float.parseFloat(data.get(i).low);
            min.Open = Float.parseFloat(data.get(i).open);
            min.Volume = Float.parseFloat(data.get(i).volume);
            datas.add(min);
        }
    }
    private void clearData() {
        datas.clear();
        datas = new ArrayList<>();
    }
}