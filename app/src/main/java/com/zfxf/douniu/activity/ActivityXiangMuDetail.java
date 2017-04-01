package com.zfxf.douniu.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zfxf.douniu.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityXiangMuDetail extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.iv_xiangmu_detail_img)
    ImageView img;//项目图片
    @BindView(R.id.iv_xiangmu_detail_type)
    ImageView type;//项目进度

    @BindView(R.id.tv_xiangmu_detail_name)
    TextView name;//项目名称
    @BindView(R.id.tv_xiangmu_detail_time)
    TextView time;//项目截至时间
    @BindView(R.id.tv_xiangmu_detail_content)
    TextView content;//项目内容
    @BindView(R.id.tv_xiangmu_detail_human)
    TextView human;//项目适合人群
    @BindView(R.id.tv_xiangmu_detail_qualify)
    TextView qualify;//项目资格
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiangmu_detail);
        ButterKnife.bind(this);

        title.setText("好项目");
        edit.setVisibility(View.INVISIBLE);

        initdata();
        initListener();
    }

    private void initdata() {

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
