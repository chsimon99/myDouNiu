package com.zfxf.douniu.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.bean.OtherResult;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author IMXU
 * @time   2017/5/3 13:41
 * @des    新闻 详情
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class ActivityMessageInformation extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;
    @BindView(R.id.wv_message_information)
    WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_information);
        ButterKnife.bind(this);
        title.setText("消息详情");
        edit.setVisibility(View.INVISIBLE);
        String id = getIntent().getStringExtra("id");//传过来的标题
        initData(id);
        initListener();
    }
    private void initData(String id) {
        if(!TextUtils.isEmpty(id)){
            CommonUtils.showProgressDialog(this,"加载中……");
            NewsInternetRequest.getMessageDetailInformation(id, new NewsInternetRequest.ForResultNewsInfoListener() {
                @Override
                public void onResponseMessage(OtherResult otherResult) {
                    if(otherResult !=null){
                        mWebView.loadUrl(otherResult.url);
                    }
                    CommonUtils.dismissProgressDialog();
                }
            });
        }
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
