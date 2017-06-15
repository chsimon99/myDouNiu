package com.zfxf.douniu.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.internet.LoginInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.SpTools;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * @author IMXU
 * @time   2017/5/3 13:20
 * @des    登录主页
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class ActivityLogin extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.et_login_phone)//登录手机号
    EditText phone;
    @BindView(R.id.et_login_passport)//登录密码
    EditText passport;
    @BindView(R.id.rl_login_eye)//密码明文
    RelativeLayout eye;
    @BindView(R.id.rl_login_login)//登录
    RelativeLayout login;
    @BindView(R.id.tv_login_register)//新用户注册
    TextView register;
    @BindView(R.id.tv_login_forget)//忘记密码
    TextView forget;
    @BindView(R.id.iv_login_qq)
    ImageView qq;
    @BindView(R.id.iv_login_wx)
    ImageView wx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        title.setText("登录");
        edit.setVisibility(View.INVISIBLE);
        initdata();
        initListener();
    }

    private void initdata() {


    }
    int num = 0;
    private void initListener() {
        back.setOnClickListener(this);
        eye.setOnClickListener(this);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        forget.setOnClickListener(this);
        qq.setOnClickListener(this);
        wx.setOnClickListener(this);
    }

    private boolean isEye = false;
    Intent intent;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back:
                finishAll();
                finish();
                break;
            case R.id.rl_login_eye:
                if(!isEye){
                    isEye = !isEye;
                    passport.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    isEye = !isEye;
                    passport.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                passport.setSelection(passport.getText().length());
                break;
            case R.id.rl_login_login:
                logIn();
                break;
            case R.id.tv_login_register:
                intent = new Intent(this,ActivityRegister.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                finishAll();
                finish();
                break;
            case R.id.tv_login_forget:
                intent = new Intent(this,ActivityForget.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                finishAll();
                finish();
                break;
            case R.id.iv_login_qq:
                break;
            case R.id.iv_login_wx:
                break;
        }
        intent = null;
    }

    private void logIn() {
        String str_phone = phone.getText().toString();
        String str_passport = passport.getText().toString();

        if(TextUtils.isEmpty(str_phone)){
            CommonUtils.toastMessage("用户名不能为空");
            return;
        }else if (TextUtils.isEmpty(str_passport)){
            CommonUtils.toastMessage("密码不能为空");
            return;
        }else if(!TextUtils.isEmpty(str_phone)) {
            if (!CommonUtils.isMobilePhone(str_phone)) {
                CommonUtils.toastMessage("您输入的手机号有误");
                return;
            }
        }
        LoginInternetRequest.login(str_phone, str_passport, new LoginInternetRequest.ForResultListener() {
            @Override
            public void onResponseMessage(String code) {
                if(code.equals("成功")){
                    CommonUtils.toastMessage("登录成功");
                    SpTools.setBoolean(CommonUtils.getContext(), Constants.isLogin,true);
                    SpTools.setBoolean(CommonUtils.getContext(), Constants.alreadyLogin,true);
                    setResult(Constants.resultCodeLogin,null);
                    finishAll();
                    finish();
                }
            }
        });
    }

    private void finishAll() {
    }
}
