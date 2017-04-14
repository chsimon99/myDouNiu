package com.zfxf.douniu.activity.advisor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.ActivityToPay;
import com.zfxf.douniu.utils.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityAdvisorAllSecretDetail extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.iv_advisor_all_secret_detail_img)
    ImageView img;
    @BindView(R.id.tv_advisor_all_secret_detail_title)
    TextView tv_title;
    @BindView(R.id.tv_advisor_all_secret_detail_from)
    TextView from;
    @BindView(R.id.tv_advisor_all_secret_detail_time)
    TextView time;
    @BindView(R.id.tv_advisor_all_secret_detail_count)
    TextView count;
    @BindView(R.id.tv_advisor_all_secret_detail_pay)
    TextView pay;
    @BindView(R.id.tv_advisor_all_secret_detail_name)
    TextView name;
    @BindView(R.id.tv_advisor_all_secret_detail_detail)
    TextView detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advisor_all_secret_detail);
        ButterKnife.bind(this);

        title.setText("课程详情");

        initdata();
        initListener();
    }

    private void initdata() {

    }
    private void initListener() {
        back.setOnClickListener(this);
        pay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back:
                finishAll();
                finish();
                break;
            case R.id.tv_advisor_all_secret_detail_pay:
                Intent intent = new Intent(CommonUtils.getContext(), ActivityToPay.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                break;
        }
    }

    private void finishAll() {

    }
}
