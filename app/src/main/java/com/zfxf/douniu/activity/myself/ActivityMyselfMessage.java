package com.zfxf.douniu.activity.myself;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zfxf.douniu.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityMyselfMessage extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.ll_myself_message_system)
    LinearLayout ll_system;
    @BindView(R.id.ll_myself_message_advisor)
    LinearLayout ll_advisor;
    @BindView(R.id.ll_myself_message_alarm)
    LinearLayout ll_alarm;
    @BindView(R.id.ll_myself_message_service)
    LinearLayout ll_service;
    @BindView(R.id.ll_myself_message_moni)
    LinearLayout ll_moni;
    @BindView(R.id.ll_myself_message_quan)
    LinearLayout ll_quan;

    @BindView(R.id.tv_myself_message_system)
    TextView tv_system;
    @BindView(R.id.tv_myself_message_system_time)
    TextView tv_system_time;
    @BindView(R.id.tv_myself_message_advisor)
    TextView tv_advisor;
    @BindView(R.id.tv_myself_message_advisor_time)
    TextView tv_advisor_time;
    @BindView(R.id.tv_myself_message_alarm)
    TextView tv_alarm;
    @BindView(R.id.tv_myself_message_alarm_time)
    TextView tv_alarm_time;
    @BindView(R.id.tv_myself_message_service)
    TextView tv_service;
    @BindView(R.id.tv_myself_message_service_time)
    TextView tv_service_time;
    @BindView(R.id.tv_myself_message_moni)
    TextView tv_moni;
    @BindView(R.id.tv_myself_message_moni_time)
    TextView tv_moni_time;
    @BindView(R.id.tv_myself_message_quan)
    TextView tv_quan;
    @BindView(R.id.tv_myself_message_quan_time)
    TextView tv_quan_time;

    @BindView(R.id.v_myself_message_system)
    View v_system;
    @BindView(R.id.v_myself_message_advisor)
    View v_advisor;
    @BindView(R.id.v_myself_message_alarm)
    View v_alarm;
    @BindView(R.id.v_myself_message_service)
    View v_service;
    @BindView(R.id.v_myself_message_moni)
    View v_moni;
    @BindView(R.id.v_myself_message_quan)
    View v_quan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myself_message);
        ButterKnife.bind(this);
        title.setText("消息中心");
        edit.setVisibility(View.INVISIBLE);
        initData();
        initListener();
    }

    private void initData() {
        v_system.setVisibility(View.INVISIBLE);
        v_advisor.setVisibility(View.INVISIBLE);
        v_alarm.setVisibility(View.INVISIBLE);
        v_service.setVisibility(View.INVISIBLE);
        v_moni.setVisibility(View.INVISIBLE);
        v_quan.setVisibility(View.INVISIBLE);
    }

    private void initListener() {
        back.setOnClickListener(this);
        ll_system.setOnClickListener(this);
        ll_advisor.setOnClickListener(this);
        ll_alarm.setOnClickListener(this);
        ll_service.setOnClickListener(this);
        ll_moni.setOnClickListener(this);
        ll_quan.setOnClickListener(this);
    }
    Intent mIntent;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back:
                break;
            case R.id.ll_myself_message_system:
                mIntent = new Intent(this,ActivityMyselfMessageCategorie.class);
                mIntent.putExtra("type","系统消息");
                startActivity(mIntent);
                overridePendingTransition(0,0);
                break;
            case R.id.ll_myself_message_advisor:
                mIntent = new Intent(this,ActivityMyselfMessageCategorie.class);
                mIntent.putExtra("type","首席动态");
                startActivity(mIntent);
                overridePendingTransition(0,0);
                break;
            case R.id.ll_myself_message_alarm:
                mIntent = new Intent(this,ActivityMyselfMessageCategorie.class);
                mIntent.putExtra("type","预警提醒");
                startActivity(mIntent);
                overridePendingTransition(0,0);
                break;
            case R.id.ll_myself_message_service:
                mIntent = new Intent(this,ActivityMyselfMessageCategorie.class);
                mIntent.putExtra("type","服务动态");
                startActivity(mIntent);
                overridePendingTransition(0,0);
                break;
            case R.id.ll_myself_message_moni:
                mIntent = new Intent(this,ActivityMyselfMessageCategorie.class);
                mIntent.putExtra("type","模拟炒股");
                startActivity(mIntent);
                overridePendingTransition(0,0);
                break;
            case R.id.ll_myself_message_quan:
                mIntent = new Intent(this,ActivityMyselfMessageCategorie.class);
                mIntent.putExtra("type","圈子信息");
                startActivity(mIntent);
                overridePendingTransition(0,0);
                break;
        }
        finishAll();
        finish();
    }

    private void finishAll() {
        mIntent = null;
    }

}
