package com.zfxf.douniu.activity.myself;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.SpTools;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityMyselfRvaluateFourth extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.ll_myself_rvaluate_first)
    LinearLayout first;
    @BindView(R.id.ll_myself_rvaluate_second)
    LinearLayout second;
    @BindView(R.id.ll_myself_rvaluate_third)
    LinearLayout third;

    @BindView(R.id.iv_myself_rvaluate_first_select)
    ImageView first_select;
    @BindView(R.id.iv_myself_rvaluate_second_select)
    ImageView second_select;
    @BindView(R.id.iv_myself_rvaluate_third_select)
    ImageView third_select;
    @BindView(R.id.iv_myself_rvaluate_first_noselect)
    ImageView first_noselect;
    @BindView(R.id.iv_myself_rvaluate_second_noselect)
    ImageView second_noselect;
    @BindView(R.id.iv_myself_rvaluate_third_noselect)
    ImageView third_noselect;


    @BindView(R.id.rl_myself_rvaluate_confirm)
    RelativeLayout confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myself_rvaluate_fourth);
        ButterKnife.bind(this);

        title.setText("风险评估");
        edit.setVisibility(View.INVISIBLE);

        initdata();
        initListener();
    }

    private void initdata() {

    }
    private void initListener() {
        back.setOnClickListener(this);
        first.setOnClickListener(this);
        second.setOnClickListener(this);
        third.setOnClickListener(this);
        confirm.setOnClickListener(this);
    }
    int num = 1;
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.iv_base_back:
                finishAll();
                finish();
                break;
            case R.id.ll_myself_rvaluate_first:
                reset();
                first_select.setVisibility(View.VISIBLE);
                first_noselect.setVisibility(View.INVISIBLE);
                num = 1;
                break;
            case R.id.ll_myself_rvaluate_second:
                reset();
                second_select.setVisibility(View.VISIBLE);
                second_noselect.setVisibility(View.INVISIBLE);
                num = 3;
                break;
            case R.id.ll_myself_rvaluate_third:
                reset();
                third_select.setVisibility(View.VISIBLE);
                third_noselect.setVisibility(View.INVISIBLE);
                num = 7;
                break;
            case R.id.rl_myself_rvaluate_confirm:
                int result = SpTools.getInt(this, Constants.rvaluateResult, 0);
                result = result+num;
                Intent intent = new Intent(this,ActivityMyselfRvaluateFifth.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                SpTools.setInt(this, Constants.rvaluateResult,result);
                finishAll();
                finish();
                break;
        }
    }

    private void reset() {
        first_select.setVisibility(View.INVISIBLE);
        second_select.setVisibility(View.INVISIBLE);
        third_select.setVisibility(View.INVISIBLE);
        first_noselect.setVisibility(View.VISIBLE);
        second_noselect.setVisibility(View.VISIBLE);
        third_noselect.setVisibility(View.VISIBLE);
    }

    private void finishAll() {

    }
}
