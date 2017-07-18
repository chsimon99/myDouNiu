package com.zfxf.douniu.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.zfxf.douniu.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author IMXU
 * @time   2017/5/3 13:41
 * @des    新闻 详情
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class ActivityStockNewsInformation extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.tv_stock_news_information_title)
    TextView infoTitle;
    @BindView(R.id.tv_stock_news_information_time)
    TextView time;
    @BindView(R.id.wv_stock_news_information)
    WebView mWebView;
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_news_information);
        ButterKnife.bind(this);
        String type_title = getIntent().getStringExtra("type");//传过来的标题
        String type_time = getIntent().getStringExtra("time");
        int type_type = getIntent().getIntExtra("newstype",0);//传过来的类型
        if(type_type == 1){
            title.setText("资讯详情");
        }else if(type_type == 2){
            title.setText("公告详情");
        }else if(type_type == 3){
            title.setText("研报详情");
        }else {
            title.setText("详情");
        }
        mUrl = getIntent().getStringExtra("url");
        time.setText(type_time);
        infoTitle.setText(type_title);
        edit.setVisibility(View.INVISIBLE);
        initData();
        initListener();
    }
    private void initData() {
        mWebView.loadUrl(mUrl);
    }
    private void initListener() {
        back.setOnClickListener(this);
        edit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back:
                finishAll();
                finish();
                break;
        }
    }

    private void finishAll() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
