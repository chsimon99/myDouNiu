package com.zfxf.douniu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.utils.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityAnswerDetail extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.iv_base_share)
    ImageView share;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.tv_answer_content)
    TextView content;//提问内容
    @BindView(R.id.tv_answer_time)
    TextView askTime;//提问时间
    @BindView(R.id.tv_answer_name)
    TextView name;//回答者姓名
    @BindView(R.id.tv_answer_answer_time)
    TextView answerTime;//回答时间
    @BindView(R.id.tv_answer_zan)
    TextView zan;//点赞数量
    @BindView(R.id.tv_answer_detail_answer_content)
    TextView answer_content;//回答内容
    @BindView(R.id.tv_answer_detail_homepage)
    TextView homepage;//主页
    @BindView(R.id.tv_answer_detail_ask)
    TextView ask;//问股

    @BindView(R.id.iv_answer_detail_img)
    ImageView mImageView;//回答者头像


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_detail);
        ButterKnife.bind(this);

        title.setText("回答详情");
        edit.setVisibility(View.INVISIBLE);
        share.setVisibility(View.VISIBLE);

        initdata();
        initListener();
    }

    private void initdata() {


    }
    private void initListener() {
        back.setOnClickListener(this);
        homepage.setOnClickListener(this);
        ask.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back:
                finishAll();
                finish();
                break;
            case R.id.tv_answer_detail_homepage:
                break;
            case R.id.tv_answer_detail_ask:
                Intent intent = new Intent(CommonUtils.getContext(), ActivityAskStock.class);
                intent.putExtra("name",name.getText().toString());
                startActivity(intent);
                overridePendingTransition(0,0);
                break;
        }
    }

    private void finishAll() {

    }
}
