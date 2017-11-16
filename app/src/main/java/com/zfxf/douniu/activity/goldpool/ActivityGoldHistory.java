package com.zfxf.douniu.activity.goldpool;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
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
 * @des    金股池历史战绩
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class ActivityGoldHistory extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;
    @BindView(R.id.wv_gold_history)
    WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gold_history);
        ButterKnife.bind(this);
        title.setText(getIntent().getStringExtra("name"));
        edit.setVisibility(View.INVISIBLE);
        initData();
        initListener();
    }
    private void initData() {
        if(!TextUtils.isEmpty(getIntent().getStringExtra("url"))){
            mWebView.loadUrl(getIntent().getStringExtra("url"));
        }
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
        }
    }

    private void finishAll() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
