package com.zfxf.douniu.activity.myself;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.utils.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityMyselfShezhi extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.ll_myself_she_contact)
    LinearLayout contact;//联系我们
    @BindView(R.id.ll_myself_she_question)
    LinearLayout question;//意见反馈
    @BindView(R.id.ll_myself_she_code)
    LinearLayout code;//密码修改
    @BindView(R.id.ll_myself_she_cache)
    LinearLayout cache;//清除缓存
    @BindView(R.id.ll_myself_she_update)
    LinearLayout update;//检查更新
    @BindView(R.id.ll_myself_she_quit)
    LinearLayout quit;//退出
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myself_shezhi);
        ButterKnife.bind(this);

        title.setText("设置");
        edit.setVisibility(View.INVISIBLE);

        initdata();
        initListener();
    }

    private void initdata() {

    }
    private void initListener() {
        back.setOnClickListener(this);
        contact.setOnClickListener(this);
        question.setOnClickListener(this);
        code.setOnClickListener(this);
        cache.setOnClickListener(this);
        update.setOnClickListener(this);
        quit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back:
                finishAll();
                finish();
                break;
            case R.id.ll_myself_she_contact:
                mIntent = new Intent(CommonUtils.getContext(), ActivityMyselfContact.class);
                startActivity(mIntent);
                overridePendingTransition(0,0);
                break;
            case R.id.ll_myself_she_question:
                mIntent = new Intent(CommonUtils.getContext(), ActivityMyselfQuestion.class);
                startActivity(mIntent);
                overridePendingTransition(0,0);
                break;
            case R.id.ll_myself_she_code:
                mIntent = new Intent(CommonUtils.getContext(), ActivityMyselfReviseCode.class);
                startActivity(mIntent);
                overridePendingTransition(0, 0);
                break;
            case R.id.ll_myself_she_cache:
                break;
            case R.id.ll_myself_she_update:
                break;
            case R.id.ll_myself_she_quit:
                break;
        }
    }

    private void finishAll() {
        mIntent = null;
    }
}
