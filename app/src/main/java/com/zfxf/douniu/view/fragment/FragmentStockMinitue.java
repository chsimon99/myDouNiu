package com.zfxf.douniu.view.fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zfxf.douniu.R;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.chart.DataHelper;
import com.zfxf.douniu.chart.KLineEntity;
import com.zfxf.douniu.chart.MChartAdapter;
import com.zfxf.douniu.chart.MTrendView;
import com.zfxf.douniu.chart.MinLineEntity;
import com.zfxf.douniu.view.chart.OrderView;
import com.zfxf.douniu.view.chart.formatter.TimeFormatter;
import com.zfxf.douniu.view.chart.impl.IKChartView;

import org.apache.http.util.EncodingUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    @BindView(R.id.mt_order_view)
    OrderView orderView;
    private MChartAdapter mAdapter;
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
        orderView.setTopPadding(MTrendView.PADDING_VALUE);
        //定义顶部栏
//        orderView.setBtnListData(new String[]{"五档", "明细"});
        orderView.setBtnListData(new String[]{"五档"});
        mAdapter = new MChartAdapter();
        minView.setAdapter(mAdapter);
        minView.setDateTimeFormatter(new TimeFormatter());
        minView.setGridRows(4);
        minView.setGridColumns(4);
        minView.setOnSelectedChangedListener(new IKChartView.OnSelectedChangedListener() {
            @Override
            public void onSelectedChanged(IKChartView view, Object point, int index) {
                KLineEntity data = (KLineEntity) point;
                Log.i("onSelectedChanged", "index:" + index + " closePrice:" + data.getClosePrice());
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                String fileName = "min.json"; //分时图数据
                String res = "";
                try {
                    InputStream in = getResources().getAssets().open(fileName);
                    int length = in.available();
                    byte[] buffer = new byte[length];
                    in.read(buffer);
                    res = EncodingUtils.getString(buffer, "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                final List<MinLineEntity> data = new Gson().fromJson(res, new TypeToken<List<MinLineEntity>>() {
                }.getType());
                setTrendMin(data);

                final List<String[]> buys = new ArrayList<String[]>();
                final List<String[]> sells = new ArrayList<String[]>();
                for (int i = 0; i < 5; i++) {
                    buys.add(new String[]{"10.55", "1000"});
                }
                int cachePrice = 352;
                Random random = new Random();
                for (int i = 0; i < 10; i++) {
                    String[] ss = {"10.55", "1000"};
                    ss[0] = String.format("%.2f", cachePrice / 100f);
                    int oV = 1 | random.nextInt(100000);
                    if (oV >= 10000) {
                        float value1 = oV / 10000f;
                        ss[1] = String.format("%.2f", value1) + "万";
                    } else {
                        ss[1] = String.valueOf(oV);
                    }
                    if (i < 5) {
                        if (i == 4) {
                            ss[1] = "1.53万";
                        }
                        if (i == 3) {
                            ss[1] = "61.37万";
                        }
                        buys.set(4 - i, ss);
                    } else {
                        if (i == 5) {
                            ss[1] = "60万";
                        }
                        sells.add(ss);
                    }
                    cachePrice++;
                }
                DataHelper.calculate(datas);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        orderView.setClose(3.54f);
                        orderView.setOrder(buys, sells);
                        //                        mAdapter.addHeaderData(datas);
                        mAdapter.addFooterData(datas);
                        minView.startAnimation();
                    }
                });
            }
        }).start();
    }
    private List<KLineEntity> datas = new ArrayList<>();

    private void setTrendMin(List<MinLineEntity> data) {
        for (int i = 0; i < data.size(); i++) {
            KLineEntity min = new KLineEntity();
            min.isMinDraw = true;
            min.Close = (float) data.get(i).price;
            min.avPrice = (float) data.get(i).avg;
            min.lastPrice = (float) (i > 0 ? data.get(i - 1).price : 3.54f);
            min.lastClosePrice = 3.54f;
            min.Volume = data.get(i).vol * 100;
            min.Date = data.get(i).time;
            min.High = 3.57f;
            min.Low = 3.52f;
            datas.add(min);
        }
    }

    @Override
    public void initListener() {
        super.initListener();
    }
}