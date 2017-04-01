package com.zfxf.douniu.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.zfxf.douniu.R;

import butterknife.ButterKnife;

public class ActivityAdvisorHome extends FragmentActivity implements View.OnClickListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advisor_home);
        ButterKnife.bind(this);
        initdata();
        initListener();
    }

    private void initdata() {


    }
    private void initListener() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.iv_base_back:
//                finishAll();
//                finish();
//                break;
        }
    }

    private void finishAll() {

    }
}
