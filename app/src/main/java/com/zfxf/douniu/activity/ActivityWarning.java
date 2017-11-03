package com.zfxf.douniu.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zfxf.douniu.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityWarning extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.tv_warning_name)
    TextView tv_name;
    @BindView(R.id.tv_warning_code)
    TextView tv_code;
    @BindView(R.id.tv_warning_price)
    TextView tv_price;
    @BindView(R.id.tv_warning_ratio)
    TextView tv_ratio;

    @BindView(R.id.et_warning_up)
    EditText et_up;
    @BindView(R.id.et_warning_down)
    EditText et_down;
    @BindView(R.id.et_warning_ratio)
    EditText et_ratio;

    @BindView(R.id.iv_warning_up)
    ImageView iv_up;
    @BindView(R.id.iv_warning_down)
    ImageView iv_down;
    @BindView(R.id.iv_warning_ratio)
    ImageView iv_ratio;
    @BindView(R.id.rl_waring_sccess)
    RelativeLayout confirm;

    boolean isUp,isDown,isRatio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning);
        ButterKnife.bind(this);
        title.setText("预警");
        edit.setVisibility(View.INVISIBLE);
        String name = getIntent().getStringExtra("name");
        String code = getIntent().getStringExtra("code");
        String price = getIntent().getStringExtra("price");
        String ratio = getIntent().getStringExtra("ratio");
        if(ratio.contains("+")){
            tv_price.setTextColor(getResources().getColor(R.color.colorRise));
            tv_ratio.setTextColor(getResources().getColor(R.color.colorRise));
        }else {
            tv_price.setTextColor(getResources().getColor(R.color.colorFall));
            tv_ratio.setTextColor(getResources().getColor(R.color.colorFall));
        }
        tv_name.setText(name);
        tv_code.setText(code);
        tv_price.setText(price);
        tv_ratio.setText(ratio);

        initData();
        initListener();
    }

    private void initData() {


    }
    private void initListener() {
        back.setOnClickListener(this);
        confirm.setOnClickListener(this);
        iv_up.setOnClickListener(this);
        iv_down.setOnClickListener(this);
        iv_ratio.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back:
                finishAll();
                finish();
                break;
            case R.id.rl_waring_sccess:
                break;
            case R.id.iv_warning_up:
                if(isUp){
                    isUp = !isUp;
                    iv_up.setImageResource(R.drawable.icon_open);
                }else {
                    isUp = !isUp;
                    iv_up.setImageResource(R.drawable.icon_shut);
                }
                break;
            case R.id.iv_warning_down:
                if(isDown){
                    isDown = !isDown;
                    iv_down.setImageResource(R.drawable.icon_open);
                }else {
                    isDown = !isDown;
                    iv_down.setImageResource(R.drawable.icon_shut);
                }
                break;
            case R.id.iv_warning_ratio:
                if(isRatio){
                    isRatio = !isRatio;
                    iv_ratio.setImageResource(R.drawable.icon_open);
                }else {
                    isRatio = !isRatio;
                    iv_ratio.setImageResource(R.drawable.icon_shut);
                }
                break;
        }
    }

    private void finishAll() {

    }

}
