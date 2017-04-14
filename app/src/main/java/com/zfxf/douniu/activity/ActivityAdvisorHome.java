package com.zfxf.douniu.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.adapter.viewPager.BarItemAdapter;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.view.fragment.FragmentAdvisorHomeCapital;
import com.zfxf.douniu.view.fragment.FragmentAdvisorHomeDirect;
import com.zfxf.douniu.view.fragment.FragmentAdvisorHomeGold;
import com.zfxf.douniu.view.fragment.FragmentAdvisorHomeOne;
import com.zfxf.douniu.view.fragment.FragmentAdvisorHomePublic;
import com.zfxf.douniu.view.fragment.FragmentAdvisorHomeSecret;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    @BindView(R.id.tv_advisor_home_count)
    TextView count;//粉丝数量
    @BindView(R.id.tv_advisor_home_income)
    TextView income;//近期收益
    @BindView(R.id.tv_advisor_home_super)
    TextView superShort;//超短线
    @BindView(R.id.tv_advisor_home_short)
    TextView Short;//短线
    @BindView(R.id.tv_advisor_home_midder)
    TextView midder;//中线
    @BindView(R.id.tv_advisor_home_introduce)
    TextView introduce;//简介
    private Fragment mFragmentAdvisorHomeDirect;
    private Fragment mFragmentAdvisorHomePublic;
    private Fragment mFragmentAdvisorHomeSecret;
    private Fragment mFragmentAdvisorHomeOne;
    private Fragment mFragmentAdvisorHomeCapital;
    private Fragment mFragmentAdvisorHomeGold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advisor_home);
        ButterKnife.bind(this);
        initdata();
        initListener();
    }

    private void initdata() {
        if(mFragmentAdvisorHomeDirect == null){
            mFragmentAdvisorHomeDirect = new FragmentAdvisorHomeDirect();
        }
        if(mFragmentAdvisorHomePublic == null){
            mFragmentAdvisorHomePublic = new FragmentAdvisorHomePublic();
        }
        if(mFragmentAdvisorHomeSecret == null){
            mFragmentAdvisorHomeSecret = new FragmentAdvisorHomeSecret();
        }
        if(mFragmentAdvisorHomeOne== null){
            mFragmentAdvisorHomeOne = new FragmentAdvisorHomeOne();
        }
        if(mFragmentAdvisorHomeCapital == null){
            mFragmentAdvisorHomeCapital = new FragmentAdvisorHomeCapital();
        }
        if(mFragmentAdvisorHomeGold == null){
            mFragmentAdvisorHomeGold = new FragmentAdvisorHomeGold();
        }

        if(list_fragment.size() == 0){
            list_fragment.add(mFragmentAdvisorHomeDirect);
            list_fragment.add(mFragmentAdvisorHomePublic);
            list_fragment.add(mFragmentAdvisorHomeSecret);
            list_fragment.add(mFragmentAdvisorHomeOne);
            list_fragment.add(mFragmentAdvisorHomeCapital);
            list_fragment.add(mFragmentAdvisorHomeGold);
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
                CommonUtils.setIndicator(mTabLayout, CommonUtils.dip2px(CommonUtils.getContext(),2)
                        ,CommonUtils.dip2px(CommonUtils.getContext(),2));
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
                break;
            case R.id.ll_advisor_home_wengu://问股
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
}
