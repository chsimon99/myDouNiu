package com.zfxf.douniu.activity.login;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.internet.LoginInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.SpTools;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.zfxf.douniu.utils.Constants.isLogin;

public class ActivityThirdRegister extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.et_third_register_phone)
    EditText phone;
    @BindView(R.id.et_third_register_passport)
    EditText passport;
    @BindView(R.id.et_third_register_next_passport)
    EditText nextpassport;
    @BindView(R.id.rl_third_register_login)
    RelativeLayout success;
    @BindView(R.id.ll_third_register_xieyi)
    LinearLayout xieyi;
    @BindView(R.id.iv_third_register_select)
    ImageView select;
    @BindView(R.id.iv_third_register_img)
    ImageView iv_img;
    @BindView(R.id.tv_third_register_text)
    TextView tv_text;

    private String phoneNumber;
    private String mCode;
    private String mPassword;
    private String nextPassword;
    private String mLoginType;
    private String mUuid;
    private String mName;
    private String mImgurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_register);
        ButterKnife.bind(this);
        mUuid = getIntent().getStringExtra("uuid");
        mName = getIntent().getStringExtra("name");
        mImgurl = getIntent().getStringExtra("img");
        mLoginType = getIntent().getStringExtra("loginType");
        if(mLoginType.equals("1")){
            title.setText("微信注册");
            iv_img.setImageResource(R.drawable.icon_wx);
            tv_text.setText("微信登陆");
        }else if(mLoginType.equals("2")){
            title.setText("QQ注册");
            iv_img.setImageResource(R.drawable.icon_qq);
            tv_text.setText("QQ登陆");
        }else if(mLoginType.equals("3")){
            title.setText("微博注册");
            iv_img.setImageResource(R.drawable.icon_sina);
            tv_text.setText("微博登陆");
        }
        edit.setVisibility(View.INVISIBLE);
        initdata();
        initListener();
    }

    private void initdata() {

    }
    private void initListener() {
        back.setOnClickListener(this);
        success.setOnClickListener(this);
        xieyi.setOnClickListener(this);
    }

//    private boolean isAgree = false;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back:
                finishAll();
                finish();
                break;
            case R.id.rl_third_register_login://完成注册
                successLogin();
                break;
            case R.id.ll_third_register_xieyi://同意协议
                break;

        }
    }

    private void successLogin() {
        mPassword = passport.getText().toString();
        nextPassword = nextpassport.getText().toString();
        phoneNumber = phone.getText().toString();
        LoginInternetRequest.thirdRegister(phoneNumber,mPassword,nextPassword,mUuid,mName
                ,mImgurl,mLoginType,passport,nextpassport,new LoginInternetRequest.ForResultListener() {
            @Override
            public void onResponseMessage(String code) {
                if(code.equals("成功")){
                    CommonUtils.toastMessage("注册成功");
                    SpTools.setBoolean(CommonUtils.getContext(), isLogin,true);
                    SpTools.setBoolean(CommonUtils.getContext(), Constants.alreadyLogin,true);
                    finish();
                }
            }
        });
    }

    private void finishAll() {
    }
}
