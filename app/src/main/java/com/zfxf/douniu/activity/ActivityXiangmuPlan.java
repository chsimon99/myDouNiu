package com.zfxf.douniu.activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.zfxf.douniu.R;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.HorizontalProgressBarWithNumber;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * @author IMXU
 * @time   2017/5/3 13:20
 * @des    好项目商业计划书
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class ActivityXiangmuPlan extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;
    @BindView(R.id.pdf_gold_pond)
    PDFView mPDFView;

    @BindView(R.id.pb_goldpond_history)
    HorizontalProgressBarWithNumber mNumber;

    private String mUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiangmu_plan);
        ButterKnife.bind(this);
        title.setText("商业计划书");
        edit.setVisibility(View.INVISIBLE);
//        mUrl = getResources().getString(R.string.file_host_address)
//                +getResources().getString(R.string.showpic)
//                +getIntent().getStringExtra("url");
        initdata();
        initListener();
    }

    private void initdata() {
        CommonUtils.logMes("------mUrl-------"+mUrl);
        OkHttpUtils.get().url(getIntent().getStringExtra("url")).build().execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), "history.pdf") {
            @Override
            public void onError(Call call, Exception e, int id) {
                mNumber.setVisibility(View.GONE);
            }

            @Override
            public void onResponse(File response, int id) {
                mNumber.setVisibility(View.GONE);
                mPDFView.fromFile(response)
                        .defaultPage(0)
                        .enableAnnotationRendering(false)
                        .spacing(0) // in dp
                        .load();
            }

            @Override
            public void inProgress(float progress, long total, int id) {
                if (progress > 0f) {
                    mNumber.setProgress((int) (progress * 100));
                }
            }
        });
        mNumber.setVisibility(View.VISIBLE);

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
}
