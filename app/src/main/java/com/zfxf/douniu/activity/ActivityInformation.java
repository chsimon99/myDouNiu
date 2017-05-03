package com.zfxf.douniu.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zfxf.douniu.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityInformation extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.tv_information_title)
    TextView infoTitle;
    @BindView(R.id.tv_information_from)
    TextView from;
    @BindView(R.id.tv_information_time)
    TextView time;
    @BindView(R.id.tv_information_content)
    TextView content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        ButterKnife.bind(this);
        String type_title = getIntent().getStringExtra("type");//传过来的标题
        title.setText(type_title);
        infoTitle.getPaint().setFakeBoldText(true);
        initData();
        initListener();
    }

    private void initData() {


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

}
