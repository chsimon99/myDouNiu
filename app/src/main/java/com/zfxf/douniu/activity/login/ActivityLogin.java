package com.zfxf.douniu.activity.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bslee.threelogin.api.OauthListener;
import com.bslee.threelogin.api.OauthLoginListener;
import com.bslee.threelogin.api.ThirdQQLoginApi;
import com.bslee.threelogin.api.ThirdWeiXinLoginApi;
import com.bslee.threelogin.db.LoginPlatForm;
import com.bslee.threelogin.model.AuthToken;
import com.bslee.threelogin.model.AuthUser;
import com.bslee.threelogin.model.QQToken;
import com.bslee.threelogin.model.QQUserInfo;
import com.bslee.threelogin.model.WeiBoToken;
import com.bslee.threelogin.model.WeiBoUserInfo;
import com.bslee.threelogin.model.WeiXinUserInfo;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.zfxf.douniu.R;
import com.zfxf.douniu.internet.LoginInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.SpTools;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.zfxf.douniu.utils.CommonUtils.handler;
import static com.zfxf.douniu.utils.Constants.isLogin;

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

    MyReceiver mReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mReceiver = new MyReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,new IntentFilter("ACTION_WX_LOGIN_SUCEESS"));

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
                break;
            case R.id.tv_login_forget:
                intent = new Intent(this,ActivityForget.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                break;
            case R.id.iv_login_qq:
                ThirdQQLoginApi.getTencent(getApplicationContext());
                ThirdQQLoginApi.login(this, oauth, oauthlogin);
                break;
            case R.id.iv_login_wx:
                CommonUtils.toastMessage("微信登录");
                ThirdWeiXinLoginApi.getWXAPI(getApplicationContext());
                ThirdWeiXinLoginApi.login(getApplicationContext());
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
                    SpTools.setBoolean(CommonUtils.getContext(), isLogin,true);
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
    /**
     * 微信授权广播回调
     *
     * @author user
     *
     */
    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("ACTION_WX_LOGIN_SUCEESS".equals(intent.getAction())) {
                //拿着code获取个人信息
                final String code = intent.getStringExtra("code");
                if (!TextUtils.isEmpty(code)) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ThirdWeiXinLoginApi.getOauthAcces(code, oauthlogin);
                        }
                    }).start();
                }else {
                    int errorCode = intent.getIntExtra("erro",0);
                    if( errorCode != BaseResp.ErrCode.ERR_OK ){
                        //微信
                        String resid = errorCode == BaseResp.ErrCode.ERR_USER_CANCEL ? "授权取消" : "授权失败";
                        Toast.makeText(CommonUtils.getContext(), resid, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
    /**
     * QQ，WeiBo，WeiXin登录成功回调
     */
    private OauthLoginListener oauthlogin = new OauthLoginListener() {
        @Override
        public void OauthLoginSuccess(final AuthToken token, final AuthUser user) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    String uuid = "";//三方用户唯一ID
                    String name = "";
                    String header = "";
                    String gender = "";
                    String loginType = "";
                    int type = token.authtype;
                    switch (type) {
                        case LoginPlatForm.QQZONE_PLATPORM:
                            uuid = ((QQToken) token).getOpenid();
                            name = ((QQUserInfo) user).getNickname();
                            header = ((QQUserInfo) user).getFigureurl_qq_1();
                            gender = ((QQUserInfo) user).getGender();
                            loginType = "2";
                            break;
                        case LoginPlatForm.WECHAT_PLATPORM:
                            uuid = ((WeiXinUserInfo) user).getOpenid();
                            name = ((WeiXinUserInfo) user).getNickname();
                            header = ((WeiXinUserInfo) user).getHeadimgurl();
                            gender = ((WeiXinUserInfo) user).getSex();
                            if ("1".equals(gender)){
                                gender = "男";
                            }else {
                                gender = "女";
                            }
                            loginType = "1";
                            break;
                        case LoginPlatForm.WEIBO_PLATPORM:
                            uuid = ((WeiBoToken) token).getUid();
                            name = ((WeiBoUserInfo) user).getName();
                            header = ((WeiBoUserInfo) user).getProfile_image_url();
                            gender = ((WeiBoUserInfo) user).getGender();
                            loginType = "3";
                            break;
                    }
                    final String finalUuid = uuid;
                    final String finalName = name;
                    final String finalHeader = header;
                    final String finalLoginType = loginType;
                    LoginInternetRequest.thirdRegisterQuery(uuid,name,header,loginType,new LoginInternetRequest.ForResultListener() {
                                @Override
                                public void onResponseMessage(String code) {
                                    if(code.equals("成功")){
                                        SpTools.setBoolean(CommonUtils.getContext(), isLogin,true);
                                        SpTools.setBoolean(CommonUtils.getContext(), Constants.alreadyLogin,true);
                                        finish();
                                    }else{
                                        Intent intent = new Intent(ActivityLogin.this,ActivityThirdRegister.class);
                                        intent.putExtra("uuid", finalUuid);
                                        intent.putExtra("name", finalName);
                                        intent.putExtra("img", finalHeader);
                                        intent.putExtra("loginType", finalLoginType);
                                        startActivity(intent);
                                        overridePendingTransition(0,0);
                                        finish();
                                    }
                                }
                            });
//                    MuTiLogin(uuid,name,header,gender,loginType);
                }
            });

        }
        @Override
        public void OauthLoginFail() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    CommonUtils.toastMessage("授权失败，请重试！");
                    finish();
                }
            });

        }
    };
    /**
     * QQ，微博授权回调
     */
    private OauthListener oauth = new OauthListener() {

        @Override
        public void OauthSuccess(Object obj) {
//            mProressbar.setText("正在为你登录");
//            mProressbar.setVisibility(View.VISIBLE);
        }
        @Override
        public void OauthFail(Object type) {
            Toast.makeText(getApplicationContext(), "授权失败", Toast.LENGTH_SHORT).show();
        }
        @Override
        public void OauthCancel(Object type) {
            Toast.makeText(getApplicationContext(), "取消授权", Toast.LENGTH_SHORT).show();
        }
    };
}
