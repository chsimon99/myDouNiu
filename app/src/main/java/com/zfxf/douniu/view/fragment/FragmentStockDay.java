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
import com.zfxf.douniu.utils.CommonUtils;
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

public class FragmentStockDay extends BaseFragment {
    private View view;

    @BindView(R.id.kchart_view)
    KChartView mKChartView;
    private KChartAdapter mAdapter;

    private static StockResult stockResult;
    private int totlePage = 0;
    private int currentPage = 1;

    @Override
    public View initView(LayoutInflater inflater) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_stock_day, null);
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
        mKChartView.setOverScrollRange(CommonUtils.dip2px(CommonUtils.getContext(),4));
        visitInternet();

    }

    private void visitInternet() {
        NewsInternetRequest.getStockKLineInformation(getActivity().getIntent().getStringExtra("code"),getActivity().getIntent().getStringExtra("model"), currentPage, "0", new NewsInternetRequest.ForResultStockKLineInfoListener() {
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
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                mAdapter.addFooterData(datas);
//            }
//        });
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