package com.zfxf.douniu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.login.ActivityLogin;
import com.zfxf.douniu.adapter.viewPager.BarItemAdapter;
import com.zfxf.douniu.bean.IndexResult;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.SpTools;
import com.zfxf.douniu.view.fragment.FragmentAdvisorHomeAsking;
import com.zfxf.douniu.view.fragment.FragmentAdvisorHomeDirect;
import com.zfxf.douniu.view.fragment.FragmentAdvisorHomeGold;
import com.zfxf.douniu.view.fragment.FragmentAdvisorHomePublic;
import com.zfxf.douniu.view.fragment.FragmentAdvisorHomeReference;
import com.zfxf.douniu.view.fragment.FragmentAdvisorHomeSecret;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * @author IMXU
 * @time   2017/5/3 13:11
 * @des    首席个人主页
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class ActivityAdvisorHome extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.tl_advisor_home)
    TabLayout mTabLayout;
    @BindView(R.id.vp_advisor_home)
    ViewPager mViewPager;
    private List<Fragment> list_fragment = new ArrayList<>();
    private FragmentPagerAdapter mAdapter;
    private List<String> list_title = new ArrayList<>();

    @BindView(R.id.ll_advisor_home_guanzhu)
    LinearLayout guanzhu;
    @BindView(R.id.ll_advisor_home_wengu)
    LinearLayout wengu;
    @BindView(R.id.rl_advisor_home_back)
    RelativeLayout back;
    @BindView(R.id.rl_advisor_home_share)
    RelativeLayout share;

    @BindView(R.id.iv_advisor_home_img)
    ImageView mView;//头像
    @BindView(R.id.tv_advisor_home_name)
    TextView name;//昵称
    @BindView(R.id.tv_advisor_home_type)
    TextView type;//类型
    @BindView(R.id.tv_advisor_home_type1)
    TextView type1;//类型
    @BindView(R.id.tv_advisor_home_count)
    TextView mCount;//粉丝数量
    @BindView(R.id.tv_advisor_home_income)
    TextView income;//近期收益
    @BindView(R.id.tv_advisor_home_introduce)
    TextView introduce;//简介
    @BindView(R.id.iv_advisor_home_guanzhu)
    ImageView iv_guanzhu;//简介
    @BindView(R.id.tv_advisor_home_guanzhu)
    TextView tv_guanzhu;//简介
    private Fragment mFragmentAdvisorHomeDirect;
    private Fragment mFragmentAdvisorHomePublic;
    private Fragment mFragmentAdvisorHomeSecret;
    private Fragment mFragmentAdvisorHomeOne;
    private Fragment mFragmentAdvisorHomeCapital;
    private Fragment mFragmentAdvisorHomeGold;
    public int mId;
    private String mNickname;
    private String mFee;
    private String mUb_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advisor_home);
        ButterKnife.bind(this);
        mId = getIntent().getIntExtra("id", 0);
        share.setVisibility(View.INVISIBLE);
        initdata();
        initListener();
    }

    private void initdata() {
        if(mId > 0){
            visitInternet();
        }
        if(mFragmentAdvisorHomeDirect == null){
            mFragmentAdvisorHomeDirect = new FragmentAdvisorHomeDirect();
        }
        if(mFragmentAdvisorHomePublic == null){
            mFragmentAdvisorHomePublic = new FragmentAdvisorHomePublic();
        }
        if(mFragmentAdvisorHomeSecret == null){
            mFragmentAdvisorHomeSecret = new FragmentAdvisorHomeSecret();
        }
        if(mFragmentAdvisorHomeGold == null){
            mFragmentAdvisorHomeGold = new FragmentAdvisorHomeGold();
        }
        if(mFragmentAdvisorHomeOne== null){
            mFragmentAdvisorHomeOne = new FragmentAdvisorHomeAsking();
        }
        if(mFragmentAdvisorHomeCapital == null){
            mFragmentAdvisorHomeCapital = new FragmentAdvisorHomeReference();
        }


        if(list_fragment.size() == 0){
            list_fragment.add(mFragmentAdvisorHomeCapital);
            list_fragment.add(mFragmentAdvisorHomeDirect);
            list_fragment.add(mFragmentAdvisorHomePublic);
            list_fragment.add(mFragmentAdvisorHomeSecret);
            list_fragment.add(mFragmentAdvisorHomeGold);
            list_fragment.add(mFragmentAdvisorHomeOne);
        }
        if(list_title.size() == 0){
            String[] titleStrings = CommonUtils.getResource().getStringArray(R.array.advisor_home_item_titles);
            for(int i = 0; i<titleStrings.length;i++){
                list_title.add(titleStrings[i]);
            }
        }

        mAdapter = new BarItemAdapter(getSupportFragmentManager(),list_fragment,list_title);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.post(new Runnable() {//改变滑动条的长度
            @Override
            public void run() {
                CommonUtils.setIndicator(mTabLayout, CommonUtils.px2dip(CommonUtils.getContext(),10)
                        ,CommonUtils.px2dip(CommonUtils.getContext(),10));
            }
        });

    }

    private void visitInternet() {
        CommonUtils.showProgressDialog(this,"加载中……");
        NewsInternetRequest.getAdvisorDetailInformation(mId, new NewsInternetRequest.ForResultIndexListener() {
            @Override
            public void onResponseMessage(IndexResult indexResult) {
                String picUrl = getResources().getString(R.string.file_host_address)
                        +getResources().getString(R.string.showpic)
                        +indexResult.user_info.zt_fileid;
                Glide.with(ActivityAdvisorHome.this).load(picUrl)
                        .placeholder(R.drawable.home_adviosr_img)
                        .bitmapTransform(new CropCircleTransformation(ActivityAdvisorHome.this)).into(mView);
                mNickname = indexResult.user_info.ud_nickname;
                mFee = indexResult.user_info.df_fee;
                mUb_id = indexResult.user_info.ud_ub_id;
                name.setText(mNickname);
                if(indexResult.user_info.type.equals("1")){
                    type1.setVisibility(View.VISIBLE);
                }else if(indexResult.user_info.type.equals("2")){
                    type.setVisibility(View.VISIBLE);
                }
                mCount.setText(indexResult.user_info.dy_count);
                income.setText(indexResult.user_info.mf_bysy+"%");
                introduce.setText(indexResult.user_info.ud_memo);
                if(indexResult.user_info.has_dy.equals("1")){
                    iv_guanzhu.setImageResource(R.drawable.icon_guanzhu_cannel);
                    tv_guanzhu.setText("取消关注");
                }
                CommonUtils.dismissProgressDialog();
            }
        });
    }

    private void initListener() {
        guanzhu.setOnClickListener(this);
        wengu.setOnClickListener(this);
        back.setOnClickListener(this);
        share.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_advisor_home_guanzhu://关注
                if(!SpTools.getBoolean(CommonUtils.getContext(), Constants.isLogin,false)){
                    Intent intent = new Intent(this, ActivityLogin.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    return;
                }
                if(tv_guanzhu.getText().toString().equals("关注")){
                    subscribeInternet(mId,6,0);
                }else {
                    subscribeInternet(mId,6,1);
                }
                break;
            case R.id.ll_advisor_home_wengu://问股
                if(!SpTools.getBoolean(CommonUtils.getContext(), Constants.isLogin,false)){
                    Intent intent = new Intent(this, ActivityLogin.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    return;
                }
                Intent intent = new Intent(CommonUtils.getContext(), ActivityAskStock.class);
                intent.putExtra("name",mNickname);
                intent.putExtra("fee",mFee);
                intent.putExtra("sx_id",mUb_id);
                startActivity(intent);
                overridePendingTransition(0,0);
                break;
            case R.id.rl_advisor_home_back://返回
                finishAll();
                finish();
                break;
            case R.id.rl_advisor_home_share://分享
                break;
        }
    }

    private void finishAll() {

    }
    private void subscribeInternet(int id, final int typyid, final int type) {
        NewsInternetRequest.subscribeAndCannel(id+"", typyid, type, new NewsInternetRequest.ForResultListener() {
            @Override
            public void onResponseMessage(String count) {
                if (TextUtils.isEmpty(count)) {
                    return;
                }
                if (type == 0) {
                    CommonUtils.toastMessage("关注成功");
                    mCount.setText(count);
                    iv_guanzhu.setImageResource(R.drawable.icon_guanzhu_cannel);
                    tv_guanzhu.setText("取消关注");
                } else {
                    CommonUtils.toastMessage("取消关注成功");
                    mCount.setText(count);
                    iv_guanzhu.setImageResource(R.drawable.icon_guanzhu);
                    tv_guanzhu.setText("关注");
                }
                SpTools.setBoolean(ActivityAdvisorHome.this, Constants.alreadyLogin,true);//存储关注变动
            }
        },getResources().getString(R.string.userdy));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CommonUtils.dismissProgressDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(SpTools.getBoolean(this, Constants.alreadyLogin,false)){
            visitInternet();
            SpTools.setBoolean(this, Constants.alreadyLogin,false);
        }
        if(SpTools.getBoolean(this, Constants.buy,false)){
            SpTools.setBoolean(this, Constants.buy,false);
            final AlertDialog mDialog;
            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityAdvisorHome.this); //先得到构造器
            mDialog = builder.create();
            mDialog.show();
            View view = View.inflate(ActivityAdvisorHome.this, R.layout.activity_pay_ok_dialog, null);
            mDialog.getWindow().setContentView(view);
            view.findViewById(R.id.tv_pay_ok_dialog_confirm).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                }
            });
        }
    }
}
