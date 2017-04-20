package com.zfxf.douniu.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zfxf.douniu.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityDeposit extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.ll_deposit_one)
    LinearLayout one;
    @BindView(R.id.ll_deposit_two)
    LinearLayout two;
    @BindView(R.id.ll_deposit_three)
    LinearLayout three;
    @BindView(R.id.ll_deposit_four)
    LinearLayout four;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);
        ButterKnife.bind(this);
        String mode = getIntent().getStringExtra("mode");
        title.setText(mode+"充值");
        edit.setVisibility(View.INVISIBLE);
        initData();
        initListener();
    }

    private void initData() {


    }
    private void initListener() {
        back.setOnClickListener(this);
        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back:
                finishAll();
                finish();
                break;
            case R.id.ll_deposit_one:
                break;
            case R.id.ll_deposit_two:
                break;
            case R.id.ll_deposit_three:
                break;
            case R.id.ll_deposit_four:
                break;
        }
    }

    private void finishAll() {

    }

}
