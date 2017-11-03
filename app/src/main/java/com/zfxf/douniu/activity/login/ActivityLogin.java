package com.zfxf.douniu.activity.login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zfxf.douniu.R;
import com.zfxf.douniu.internet.LoginInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.SpTools;

import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    @BindView(R.id.iv_login_sina)
    ImageView sina;
    UMShareAPI mShareAPI;
    String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
            ,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE
            ,Manifest.permission.READ_LOGS,Manifest.permission.READ_PHONE_STATE
            , Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.SET_DEBUG_APP
            ,Manifest.permission.SYSTEM_ALERT_WINDOW,Manifest.permission.GET_ACCOUNTS
            ,Manifest.permission.WRITE_APN_SETTINGS};
    private SHARE_MEDIA platform;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        title.setText("登录");
        edit.setVisibility(View.INVISIBLE);

        mShareAPI = UMShareAPI.get(this);//友盟

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
        sina.setOnClickListener(this);
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
                CommonUtils.toastMessage("QQ登录");
                platform = SHARE_MEDIA.QQ;
                LoginPlatformInfo();
                break;
            case R.id.iv_login_sina:
                CommonUtils.toastMessage("微博登录");
                platform = SHARE_MEDIA.SINA;
                LoginPlatformInfo();
                break;
            case R.id.iv_login_wx:
                CommonUtils.toastMessage("微信登录");
                platform = SHARE_MEDIA.WEIXIN;
                LoginPlatformInfo();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
    private void LoginPlatformInfo() {
        if(Build.VERSION.SDK_INT >= 23){
            boolean isprimission= getisPermission();
            if (isprimission) {
                mShareAPI.getPlatformInfo(ActivityLogin.this,platform, authListener);
            } else {
                ActivityCompat.requestPermissions(ActivityLogin.this, mPermissionList, 123);
            }
        }else {
            mShareAPI.getPlatformInfo(ActivityLogin.this,platform, authListener);
        }
    }
    //判断是否获得权限
    private boolean getisPermission() {
        boolean quanxian = true;
        for (int i=0;i<mPermissionList.length;i++){
            if(ContextCompat.checkSelfPermission(ActivityLogin.this, mPermissionList[i]) != PackageManager.PERMISSION_GRANTED){
                quanxian=false;
            }
        }
        return quanxian;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 123) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mShareAPI.getPlatformInfo(ActivityLogin.this, platform, authListener);
            } else {
                CommonUtils.toastMessage("未获得相应权限");
            }
        }
    }

    UMAuthListener authListener = new UMAuthListener() {
        /**
         * @desc 授权开始的回调  @param platform 平台名称
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
        }

        /**
         * @desc 授权成功的回调  @param platform 平台名称 @param action 行为序号，开发者用不上 @param data 用户资料返回
         */
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            Toast.makeText(ActivityLogin.this, "登录成功了", Toast.LENGTH_LONG).show();
            Set<String> keySet = data.keySet();
            //遍历循环，得到里面的key值----用户名，头像....
            for (String string : keySet) {
                CommonUtils.logMes("------data-----"+string+"-----key-----="+data.get(string));
            }
            threeLogin(data);
        }

        /**
         * @desc 授权失败的回调 @param platform 平台名称 @param action 行为序号，开发者用不上 @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {

            Toast.makeText(ActivityLogin.this, "登录失败：" + t.getMessage(),Toast.LENGTH_LONG).show();
        }
        /**
         * @desc 授权取消的回调 @param platform 平台名称 @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(ActivityLogin.this, "登录取消了", Toast.LENGTH_LONG).show();
        }
    };

    private void threeLogin(Map<String, String> data) {
        String uuid = "";
        String name = "";
        String url = "";
        String loginType = "";
        if( platform == SHARE_MEDIA.WEIXIN){
            uuid = data.get("unionid");
            name = data.get("screen_name");
            url = data.get("profile_image_url");
            loginType = "1";
        }else if(platform == SHARE_MEDIA.QQ){
            uuid = data.get("uid");
            name = data.get("name");
            url = data.get("iconurl");
            loginType = "2";
        }else if(platform == SHARE_MEDIA.SINA){
            uuid = data.get("id");
            name = data.get("screen_name");
            url = data.get("profile_image_url");
            loginType = "3";
        }

        final String finalUuid = uuid;
        final String finalName = name;
        final String finalHeader = url;
        final String finalLoginType = loginType;
        CommonUtils.logMes("------uuid--------"+uuid);
        CommonUtils.logMes("------name--------"+name);
        CommonUtils.logMes("------url--------"+url);
        CommonUtils.logMes("------loginType--------"+loginType);
        LoginInternetRequest.thirdRegisterQuery(uuid, name, url, loginType, new LoginInternetRequest.ForResultListener() {
            @Override
            public void onResponseMessage(String code) {
                if (code.equals("成功")) {
                    SpTools.setBoolean(CommonUtils.getContext(), isLogin, true);
                    SpTools.setBoolean(CommonUtils.getContext(), Constants.alreadyLogin, true);
                    finish();
                } else {
                    Intent intent = new Intent(ActivityLogin.this, ActivityThirdRegister.class);
                    intent.putExtra("uuid", finalUuid);
                    intent.putExtra("name", finalName);
                    intent.putExtra("img", finalHeader);
                    intent.putExtra("loginType", finalLoginType);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    finish();
                }
            }
        });
    }
}
