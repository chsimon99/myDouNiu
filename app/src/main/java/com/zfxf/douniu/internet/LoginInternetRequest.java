package com.zfxf.douniu.internet;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.igexin.sdk.PushManager;
import com.zfxf.douniu.R;
import com.zfxf.douniu.bean.EditUserInformationBean;
import com.zfxf.douniu.bean.LoginResult;
import com.zfxf.douniu.bean.TouGuDetail;
import com.zfxf.douniu.bean.TouGuInformationBean;
import com.zfxf.douniu.bean.UserDetail;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.SpTools;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;

import static com.zfxf.douniu.utils.Constants.userId;

/**
 * @author Admin
 * @time 2017/4/7 9:56
 * @des ${TODO}
 */

public class LoginInternetRequest {

    private static int index = 0;
    private static Context context;
    private static Gson mGson;
    private static ForResultListener mListener;
    private static ForResultInfoListener mInfoListener;
    private static TimeCount timeCount;
    private static TextView mTextView;
    private static boolean isRun = false;
    static {
        mGson = new Gson();
        context = CommonUtils.getContext();
        timeCount = new TimeCount(60000, 1000);
    }

    /**
     * 登陆
     * @param phonenumber 电话号码
     * @param password 密码
     * @param listener listener
     */
    public static void login(String phonenumber, String password, ForResultListener listener){
        mListener = listener;
        if(!CommonUtils.isNetworkAvailable(CommonUtils.getContext())){
            CommonUtils.toastMessage("您当前无网络，请联网再试");
            mListener.onResponseMessage("");
            return;
        }
        if(TextUtils.isEmpty(phonenumber)){
            CommonUtils.toastMessage("您输入的手机号为空");
            mListener.onResponseMessage("");
            return ;
        }else if(TextUtils.isEmpty(password)){
            CommonUtils.toastMessage("您输入的密码为空");
            return;
        }

        if(!TextUtils.isEmpty(phonenumber)){
            if(!CommonUtils.isMobilePhone(phonenumber)){
                CommonUtils.toastMessage("您输入的手机号有误");
                mListener.onResponseMessage("");
                return ;
            }
        }
        String url = context.getResources().getString(R.string.service_host_address)
                .concat(context.getResources().getString(R.string.getLogin));
        OkHttpUtils.post().url(url)
                .addParams("sid","")
                .addParams("ub_phone",phonenumber)
                .addParams("index",(index++)+"")
                .addParams("ud_pwd",password)
                .addParams("uo_long","")
                .addParams("uo_lat","")
                .addParams("uo_high","")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes(e+"");
                CommonUtils.toastMessage("您网络信号不稳定，请稍后再试");
            }

            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("login="+response);
                LoginResult result = mGson.fromJson(response, LoginResult.class);
                String code = result.result.code;
                String info = result.result.info;
                if (code.equals("10")) {
                    SpTools.setString(context, userId, result.ub_id);//存储用户的ub_id
                    PushManager.getInstance().bindAlias(CommonUtils.getContext(),result.ub_id);
                    mListener.onResponseMessage("成功");
                } else if (code.equals("20")) {
                    if (info.equals("手机长度不正确")) {
                        CommonUtils.toastMessage("您输入的手机号有误,请重新输入");
                    } else if (info.endsWith("密码不正确")) {
                        CommonUtils.toastMessage("您输入的密码有误,请重新输入");
                    }else if (info.endsWith("手机号不存在")) {
                        CommonUtils.toastMessage("该手机号还没有注册");
                    }
                }
            }
        });
    }
    /**
     *  获取验证码
     * @param phoneNumber 电话号码
     * @param view 验证码的view
     * @param listener listener
     */
    public static void verificationCode(String phoneNumber, TextView view, ForResultListener listener){
        mListener = listener;
        mTextView = view;
        if(!CommonUtils.isNetworkAvailable(CommonUtils.getContext())){
            CommonUtils.toastMessage("您当前无网络，请联网再试");
            mListener.onResponseMessage("");
            return;
        }
        if(TextUtils.isEmpty(phoneNumber)){
            CommonUtils.toastMessage("您输入的手机号为空");
            mListener.onResponseMessage("");
            return ;
        }
        if(!TextUtils.isEmpty(phoneNumber)){
            if(!CommonUtils.isMobilePhone(phoneNumber)){
                CommonUtils.toastMessage("您输入的手机号有误");
                mListener.onResponseMessage("");
                return ;
            }
        }
        if(isRun){
            return ;
        }
        timeCount.start();
        String url = context.getResources().getString(R.string.service_host_address)
                .concat(context.getResources().getString(R.string.sendCode));
        OkHttpUtils.post().url(url)
                .addParams("sid","")
                .addParams("ub_phone",phoneNumber)
                .addParams("index",(index++)+"")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.toastMessage("您网络信号不稳定，请稍后再试");
                mListener.onResponseMessage("");
            }

            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("verificationCode="+response);
                LoginResult result = mGson.fromJson(response,LoginResult.class);
                String code = result.result.code;
                String info = result.result.info;
                if(code.equals("10")){
                    String mVc_code = result.vc_code;
                    mListener.onResponseMessage(mVc_code);
                }else if(code.equals("20")){
                    if(info.equals("ub_phone error!")){
                        CommonUtils.toastMessage("您输入的手机号错误");
                        mListener.onResponseMessage("");
                    }
                }
            }
        });
    }

    /**
     * 注册新用户
     * @param phoneNumber 电话号码
     * @param vc_code 获取的验证码
     * @param password 密码
     * @param code 用户输入的验证码
     * @param view 验证码的View
     * @param listener listener
     */
    public static void register(String phoneNumber, String vc_code, String password, String code, TextView view, ForResultListener listener){
        mListener = listener;
        mTextView = view;
        if(!CommonUtils.isNetworkAvailable(CommonUtils.getContext())){
            CommonUtils.toastMessage("您当前无网络，请联网再试");
            return;
        }
        if(TextUtils.isEmpty(phoneNumber)){
            CommonUtils.toastMessage("您输入的手机号为空");
            return;
        }else if(TextUtils.isEmpty(code)){
            CommonUtils.toastMessage("您输入的验证码为空");
            return;
        }else if(TextUtils.isEmpty(password)){
            CommonUtils.toastMessage("您输入的密码为空");
            return;
        }
        if(!TextUtils.isEmpty(phoneNumber)){
            if(!CommonUtils.isMobilePhone(phoneNumber)){
                CommonUtils.toastMessage("您输入的手机号有误");
                mListener.onResponseMessage("");
                return ;
            }
        }
        if(!vc_code.equals(code)){
            CommonUtils.toastMessage("您输入的验证码错误");
            return;
        }
        String url = context.getResources().getString(R.string.service_host_address)
                .concat(context.getResources().getString(R.string.reg));
        OkHttpUtils.post().url(url)
                .addParams("sid","")
                .addParams("ub_phone",phoneNumber)
                .addParams("index",(index++)+"")
                .addParams("ud_pwd",password)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.toastMessage("您网络信号不稳定，请稍后再试");
            }
            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("register="+response);
                LoginResult result = mGson.fromJson(response, LoginResult.class);
                String code = result.result.code;
                String info = result.result.info;
                if(code.equals("10")){
                    reset();
                    mListener.onResponseMessage("成功");
                }else if(code.equals("20")){
                    if(info.equals("手机长度不正确")){
                        CommonUtils.toastMessage("您输入的手机号错误");
                    }else if(info.equals("手机号已存在！")){
                        CommonUtils.toastMessage("您输入的手机号已注册");
                    }else if(info.equals("密码长度不正确！")){
                        CommonUtils.toastMessage("您输入的密码位数不足6位");
                    }
                }
            }
        });

    }

    /**
     * 第三方登陆注册
     * @param phoneNumber 电话号码
     * @param password 密码
     * @param nextPassword 确认密码
     * @param openid 第三方获取的id
     * @param username 第三方获取的昵称
     * @param imgUrl 第三方获取的头像url等其他信息，用| 隔开
     * @param type 第三方类型 1微信 1qq 3新浪微博 4支付宝
     * @param passport 输入密码的Editext
     * @param nextPassport 再次输入密码的Editext
     * @param listener
     */
    public static void thirdRegister(String phoneNumber, String password, String nextPassword
            ,String openid,String username,String imgUrl,String type
            , final EditText passport, final EditText nextPassport, ForResultListener listener){
        mListener = listener;
        if(!CommonUtils.isNetworkAvailable(CommonUtils.getContext())){
            CommonUtils.toastMessage("您当前无网络，请联网再试");
            return;
        }
        if(TextUtils.isEmpty(phoneNumber)){
            CommonUtils.toastMessage("您输入的手机号为空");
            return;
        }else if(TextUtils.isEmpty(password) | TextUtils.isEmpty(nextPassword)){
            CommonUtils.toastMessage("您输入的密码为空");
            passport.setText("");
            nextPassport.setText("");
            return;
        }else if(password.length()<6 |nextPassword.length()<6){
            CommonUtils.toastMessage("输入的密码长度不要小于6位");
            passport.setText("");
            nextPassport.setText("");
            return;
        }
        if(!TextUtils.isEmpty(phoneNumber)){
            if(!CommonUtils.isMobilePhone(phoneNumber)){
                CommonUtils.toastMessage("您输入的手机号有误");
                mListener.onResponseMessage("");
                return ;
            }
        }
        if(!password.equals(nextPassword)){
            CommonUtils.toastMessage("二次输入的密码不一致");
            passport.setText("");
            nextPassport.setText("");
            return;
        }
        String url = context.getResources().getString(R.string.service_host_address)
                .concat(context.getResources().getString(R.string.tparty));
        OkHttpUtils.post().url(url)
                .addParams("sid","")
                .addParams("index",(index++)+"")
                .addParams("tp_openid",openid)
                .addParams("tp_username",username)
                .addParams("tp_tparty_info",imgUrl)
                .addParams("tp_tparty_type",type)
                .addParams("type",1+"")
                .addParams("ub_phone",phoneNumber)
                .addParams("ud_pwd",password)
                .addParams("uo_long","")
                .addParams("uo_lat","")
                .addParams("uo_high","")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("thirdRegister="+e);
            }

            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("thirdRegister="+response);
                LoginResult result = mGson.fromJson(response, LoginResult.class);
                String code = result.result.code;
                String info = result.result.info;
                if (code.equals("10")) {
                    SpTools.setString(context, userId, result.ub_id);//存储用户的ub_id
                    mListener.onResponseMessage("成功");
                }
            }
        });
    }
    public static void thirdRegisterQuery(String openid,String username,String imgUrl,String type
            , ForResultListener listener){
        mListener = listener;
        String url = context.getResources().getString(R.string.service_host_address)
                .concat(context.getResources().getString(R.string.tparty));
        OkHttpUtils.post().url(url)
                .addParams("sid","")
                .addParams("index",(index++)+"")
                .addParams("tp_openid",openid)
                .addParams("tp_username",username)
                .addParams("tp_tparty_info",imgUrl)
                .addParams("tp_tparty_type",type)
                .addParams("type",1+"")
                .addParams("uo_long","")
                .addParams("uo_lat","")
                .addParams("uo_high","")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("thirdRegisterQuery="+e);
            }

            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("thirdRegisterQuery="+response);
                LoginResult result = mGson.fromJson(response, LoginResult.class);
                String code = result.result.code;
                String info = result.result.info;
                if (code.equals("10")) {
                    SpTools.setString(context, userId, result.ub_id);//存储用户的ub_id
                    mListener.onResponseMessage("成功");
                }else if(code.equals("20")){
                    mListener.onResponseMessage("");
                }
            }
        });
    }
    /**
     * 忘记密码
     * @param phoneNumber 电话号码
     * @param vc_code 获得到验证码
     * @param code 输入的验证码
     * @param password 输入的密码
     * @param nextPassword 再次输入的密码
     * @param view 验证码的View
     * @param passport 输入密码的Editext
     * @param nextPassport 再次输入密码的Editext
     * @param listener listener
     */
    public static void forgetPassword(String phoneNumber, String vc_code, String code, String password, String nextPassword, TextView view, final EditText passport, final EditText nextPassport, ForResultListener listener){
        mListener = listener;
        mTextView = view;
        if(!CommonUtils.isNetworkAvailable(CommonUtils.getContext())){
            CommonUtils.toastMessage("您当前无网络，请联网再试");
            return;
        }
        if(TextUtils.isEmpty(phoneNumber)){
            CommonUtils.toastMessage("您输入的手机号为空");
            return;
        }else if(TextUtils.isEmpty(code)){
            CommonUtils.toastMessage("您输入的验证码为空");
            return;
        }else if(TextUtils.isEmpty(password) | TextUtils.isEmpty(nextPassword)){
            CommonUtils.toastMessage("您输入的密码为空");
            passport.setText("");
            nextPassport.setText("");
            return;
        }else if(password.length()<6 |nextPassword.length()<6){
            CommonUtils.toastMessage("输入的密码长度不要小于6位");
            passport.setText("");
            nextPassport.setText("");
            return;
        }
        if(!TextUtils.isEmpty(phoneNumber)){
            if(!CommonUtils.isMobilePhone(phoneNumber)){
                CommonUtils.toastMessage("您输入的手机号有误");
                mListener.onResponseMessage("");
                return ;
            }
        }
        if(!password.equals(nextPassword)){
            CommonUtils.toastMessage("二次输入的密码不一致");
            passport.setText("");
            nextPassport.setText("");
            return;
        }
        if(!vc_code.equals(code)){
            CommonUtils.toastMessage("您输入的验证码错误");
            return;
        }

        String url = context.getResources().getString(R.string.service_host_address)
                .concat(context.getResources().getString(R.string.reset));
        OkHttpUtils.post().url(url)
                .addParams("sid","")
                .addParams("ub_phone",phoneNumber)
                .addParams("index",(index++)+"")
                .addParams("ud_pwd",password)
                .addParams("vc_code",code)
                .addParams("uo_long","")
                .addParams("uo_lat","")
                .addParams("uo_high","")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.toastMessage("您网络信号不稳定，请稍后再试");
            }
            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("forgetPassword="+response);
                LoginResult result = mGson.fromJson(response, LoginResult.class);
                String code = result.result.code;
                String info = result.result.info;
                if(code.equals("10")){
                    reset();
                    passport.setText("");
                    nextPassport.setText("");
                    mListener.onResponseMessage("成功");

                }else if(code.equals("20")){
                    if (info.endsWith("手机号不存在")) {
                        CommonUtils.toastMessage("该手机号还没有注册");
                    }else{
                        CommonUtils.toastMessage("修改密码失败，请重新输入");
                    }
                }
            }
        });

    }

    /**
     *  重置密码
     * @param password 原密码
     * @param newpassword 新密码
     * @param confirmpassword 新密码确认
     * @param editpass 原密码的Editext
     * @param editnewpass 新密码的Editext
     * @param editconfirm 确认密码的Editext
     * @param listener listener
     */
    public static void reviseCode(String password, String newpassword, String confirmpassword, final EditText editpass, final EditText editnewpass, final EditText editconfirm, ForResultListener listener) {
        if(!CommonUtils.isNetworkAvailable(CommonUtils.getContext())){
            CommonUtils.toastMessage("您当前无网络，请联网再试");
            return;
        }
        mListener = listener;
        if(TextUtils.isEmpty(password) | TextUtils.isEmpty(newpassword) | TextUtils.isEmpty(confirmpassword)){
            CommonUtils.toastMessage("您输入的密码为空");
            editpass.setText("");
            editnewpass.setText("");
            editconfirm.setText("");
            return;
        }else if(password.length()<6 |confirmpassword.length()<6){
            CommonUtils.toastMessage("输入的新密码长度不要小于6位");
            editpass.setText("");
            editnewpass.setText("");
            editconfirm.setText("");
            return;
        }
        if(!newpassword.equals(confirmpassword)){
            CommonUtils.toastMessage("二次输入的新密码不一致");
            editnewpass.setText("");
            editconfirm.setText("");
            return;
        }
        String url = context.getResources().getString(R.string.service_host_address)
                .concat(context.getResources().getString(R.string.chpwd));
        OkHttpUtils.post().url(url)
                .addParams("sid","")
                .addParams("index",(index++)+"")
                .addParams("ub_id",SpTools.getString(context, userId,"0"))
                .addParams("uo_long","")
                .addParams("uo_lat","")
                .addParams("uo_high","")
                .addParams("ud_pwd",password)
                .addParams("nw_pwd",newpassword)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.toastMessage("您网络信号不稳定，请稍后再试");
            }
            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("reviseCode="+response);
                LoginResult result = mGson.fromJson(response, LoginResult.class);
                String code = result.result.code;
                String info = result.result.info;
                if(code.equals("10")){
                    mListener.onResponseMessage("成功");
                }else if(code.equals("20")){
                    if(info.equals("原密码不正确")){
                        CommonUtils.toastMessage("您输入的原密码错误");
                        editpass.setText("");
                        editnewpass.setText("");
                        editconfirm.setText("");
                    }else if(info.equals("密码长度不够")){
                        CommonUtils.toastMessage("您输入的密码位数不足6位");
                    }
                }
            }
        });

    }

    /**
     * 获取个人信息
     * @param listener
     */
    public static void getUserInformation(ForResultInfoListener listener) {
        String url = context.getResources().getString(R.string.service_host_address)
                .concat(context.getResources().getString(R.string.mycount));
        mInfoListener = listener;
        final Map<String,String> map = new HashMap<>();
        OkHttpUtils.post().url(url)
                .addParams("sid","")
                .addParams("index",(index++)+"")
                .addParams("ub_id",SpTools.getString(context, userId,"0"))
                .addParams("uo_long","")
                .addParams("uo_lat","")
                .addParams("uo_high","")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("getUserInformation="+e);
            }

            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("getUserInformation="+response);
                LoginResult result = mGson.fromJson(response, LoginResult.class);
                String ud_addr = result.user_detail.getUd_addr();
                String ud_borth = result.user_detail.getUd_borth();
                String ud_nickname = result.user_detail.getUd_nickname();
                String ud_photo_fileid = result.user_detail.getUd_photo_fileid();
                String ud_memo = result.user_detail.getUd_memo();
                map.put("ud_addr",ud_addr);
                map.put("ud_borth",ud_borth);
                map.put("ud_nickname",ud_nickname);
                map.put("ud_photo_fileid",ud_photo_fileid);
                map.put("ud_memo",ud_memo);
                mInfoListener.onResponseMessage(map);
            }
        });
    }

    /**
     * 上传头像
     * @param picName  头像存储的名字
     * @param listener
     */
    public static void uplodePicture(String picName, ForResultListener listener){
        String url = context.getResources().getString(R.string.file_host_address)
                .concat(context.getResources().getString(R.string.upload));
        mListener = listener;
        OkHttpUtils.post()
                .addFile("image[]",picName,new File(context.getFilesDir(),picName))
                .url(url)
                .addParams("sid", "")
                .addParams("index", (index++) + "")
                .addParams("ub_id", SpTools.getString(CommonUtils.getContext(), userId ,"0"))
                .addParams("uo_long","")
                .addParams("uo_lat","")
                .addParams("uo_high","")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("uplodePicture   e  ="+e);
                mListener.onResponseMessage("");
            }
            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("uplodePicture="+response);
                LoginResult result = mGson.fromJson(response, LoginResult.class);
                String picId = result.file_ids.get(0);
                if(result.result.code.equals("10")){
                    mListener.onResponseMessage(picId);
                    CommonUtils.toastMessage("上传头像成功");
                }
            }
        });
    }

    /**
     * 编辑个人信息
     * @param name 昵称
     * @param date 时间
     * @param address 地址
     * @param id 上传图片成功后传递的图片id
     * @param listener
     */
    public static void editUserInformation(String name, String date, String address,String info, String id,ForResultListener listener) {
        String url = context.getResources().getString(R.string.service_host_address)
                .concat(context.getResources().getString(R.string.mycountEdit));
        mListener = listener;
        EditUserInformationBean informationBean = new EditUserInformationBean();
        UserDetail detail = new UserDetail();
        detail.setUd_addr(address);
        detail.setUd_borth(date);
        detail.setUd_nickname(name);
        detail.setUd_memo(info);
        if(!TextUtils.isEmpty(id)){
            detail.setUd_photo_fileid(id);
        }

        informationBean.setSid("");
        informationBean.setIndex((index++)+"");
        informationBean.setUb_id(Integer.parseInt(SpTools.getString(context, userId,"0")));
        informationBean.setUo_high("");
        informationBean.setUo_lat("");
        informationBean.setUo_long("");
        informationBean.setUser_detail(detail);

        String json = mGson.toJson(informationBean);
        CommonUtils.logMes("response"+json);
        OkHttpUtils.postString().url(url)
                .content(json)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("editUserInformation="+e);
                CommonUtils.toastMessage("提交个人信息失败，请重新再试");
            }

            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("editUserInformation="+response);
                LoginResult result = mGson.fromJson(response, LoginResult.class);
                String code = result.result.code;
                if(code.equals("10")){
                    mListener.onResponseMessage("成功");
                }
            }
        });
    }

    public static void applyTouGu(String s_name, String s_phone, String s_number, String s_year,ForResultListener listener){
        String url = context.getResources().getString(R.string.service_host_address)
                .concat(context.getResources().getString(R.string.tougu));
        mListener = listener;
        TouGuInformationBean informationBean = new TouGuInformationBean();
        TouGuDetail touGuDetail = new TouGuDetail();
        touGuDetail.setDt_xm(s_name);
        touGuDetail.setDt_phone(s_phone);
        touGuDetail.setDt_zgzs(s_number);
        touGuDetail.setDt_cynx(s_year);

        informationBean.setSid("");
        informationBean.setIndex((index++)+"");
        informationBean.setUb_id(Integer.parseInt(SpTools.getString(context, userId,"0")));
        informationBean.setUo_high("");
        informationBean.setUo_lat("");
        informationBean.setUo_long("");
        informationBean.setDn_tougu(touGuDetail);

        String json = mGson.toJson(informationBean);
        CommonUtils.logMes("response"+json);
        OkHttpUtils.postString().url(url)
                .content(json)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("applyTouGu="+e);
                CommonUtils.toastMessage("提交个人信息失败，请重新再试");
            }

            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("applyTouGu="+response);
                LoginResult result = mGson.fromJson(response, LoginResult.class);
                String code = result.result.code;
                if(code.equals("10")){
                    mListener.onResponseMessage("成功");
                }else if(code.equals("02")){
                    if(result.result.info.equals("申请已通过")){
                        CommonUtils.toastMessage("您的申请已通过");
                    }else{
                        CommonUtils.toastMessage("您已经申请过，请耐心等待");
                    }
                }
            }
        });

    }

    public interface ForResultListener{
        void onResponseMessage(String code);
    }
    public interface ForResultInfoListener{
        void onResponseMessage(Map<String,String> map);
    }
    /**
     * 验证码倒计时timecount
     */
    static class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }
        @Override
        public void onFinish() {// 计时完毕时触发
            mTextView.setText("获取验证码");
            mTextView.setClickable(true);
            isRun = false;
        }
        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            mTextView.setClickable(false);
            isRun = true;
            mTextView.setText("获取验证码("+millisUntilFinished / 1000 + "秒)");
        }
    }

    private static void reset() {
        if(timeCount != null){
            timeCount.onFinish();
        }
    }
}
