package com.zfxf.douniu.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zfxf.douniu.R;
import com.zfxf.douniu.adapter.recycleView.StockNoticeAdapter;
import com.zfxf.douniu.chart.DataHelper;
import com.zfxf.douniu.chart.KChartAdapter;
import com.zfxf.douniu.chart.KChartView;
import com.zfxf.douniu.chart.KLineEntity;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.view.FullyLinearLayoutManager;
import com.zfxf.douniu.view.RecycleViewDivider;
import com.zfxf.douniu.view.chart.formatter.DateFormatter;
import com.zfxf.douniu.view.chart.impl.IKChartView;

import org.apache.http.util.EncodingUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author IMXU
 * @time   2017/5/3 13:41
 * @des    新闻 详情
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

    private StockNoticeAdapter mStockNoticeAdapter;
    private RecycleViewDivider mDivider;
    private LinearLayoutManager mAnswerManager;
    private List<String> datas = new ArrayList<String>();
    private int totlePage = 0;
    private int currentPage = 1;

    @BindView(R.id.kchart_view)
    KChartView mKChartView;
    private KChartAdapter mAdapter;
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
        initData();
        initListener();
    }
    private void initData() {

        new Thread(new Runnable() {

            @Override
            public void run() {
                String fileName = "ibm2.json"; //k线图的数据
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
                CommonUtils.logMes("---res---="+res);
                final List<KLineEntity> data = new Gson().fromJson(res, new TypeToken<List<KLineEntity>>() {
                }.getType());
                DataHelper.calculate(data);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.addFooterData(data);
                        mKChartView.startAnimation();

                        KLineEntity entity = data.get(data.size() - 1);
//                        price.setText(entity.getClosePrice()+"");
//                        max.setText("最高："+entity.getHighPrice()+"");
//                        min.setText("最低："+entity.getLowPrice()+"");
//                        yesPrice.setText("开盘："+entity.getOpenPrice()+"");
//                        shoupan.setText("收盘："+entity.getClosePrice()+"");
                    }
                });
            }
        }).start();

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

        mAdapter = new KChartAdapter();
        mKChartView.setAdapter(mAdapter);
        mKChartView.setDateTimeFormatter(new DateFormatter());
        mKChartView.setGridRows(4);
        mKChartView.setGridColumns(4);
        //长按的触发事件
        mKChartView.setOnSelectedChangedListener(new IKChartView.OnSelectedChangedListener() {
            @Override
            public void onSelectedChanged(IKChartView view, Object point, int index) {
                final KLineEntity data = (KLineEntity) point;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        price.setText(data.getClosePrice()+"");
//                        max.setText("最高："+data.getHighPrice()+"");
//                        min.setText("最低："+data.getLowPrice()+"");
//                        yesPrice.setText("开盘："+data.getOpenPrice()+"");
//                        shoupan.setText("收盘："+data.getClosePrice()+"");
//                        percent.setText(data.get+"");
                    }
                });
                Log.i("onSelectedChanged", "index:" + index + " closePrice:" + data.getClosePrice());
            }
        });

    }
    private void initListener() {
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back:
                finishAll();
                finish();
                break;
            case R.id.iv_base_edit:

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
