package com.zfxf.douniu.activity.myself;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.internet.LoginInternetRequest;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityMyselfReviseCode extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.et_myself_revise_password)
    EditText password;
    @BindView(R.id.et_myself_revise_newpassword)
    EditText newpassword;
    @BindView(R.id.et_myself_revise_newpassword_confirm)
    EditText password_confirm;
    @BindView(R.id.rl_myself_revise_confirm)
    RelativeLayout confirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myself_revise_code);
        ButterKnife.bind(this);

        title.setText("修改密码");
        edit.setVisibility(View.INVISIBLE);

        initdata();
        initListener();
    }

    private void initdata() {

    }
    private void initListener() {
        back.setOnClickListener(this);
        confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back:
                finishAll();
                finish();
                break;
            case R.id.rl_myself_revise_confirm:
                LoginInternetRequest.reviseCode(password.getText().toString(),
                        newpassword.getText().toString(), password_confirm.getText().toString(),
                        password, newpassword, password_confirm, new LoginInternetRequest.ForResultListener() {
                            @Override
                            public void onResponseMessage(String code) {

                            }
                        });
                break;
        }
    }

    private void finishAll() {

    }
}
