package com.zfxf.douniu.activity.living;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.MainActivityTabHost;
import com.zfxf.douniu.adapter.viewPager.BarItemAdapter;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.SpTools;
import com.zfxf.douniu.view.fragment.FragmentLiveInteraction;
import com.zfxf.douniu.view.fragment.FragmentLiveLiving;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * @author IMXU
 * @time   2017/5/3 13:34
 * @des    直播（直播和互动）
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class ActivityLiving extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.iv_base_share)
    ImageView share;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.tl_living)
    TabLayout mTabLayout;
    @BindView(R.id.vp_living)
    ViewPager mViewPager;
    private List<Fragment> list_fragment = new ArrayList<>();
    private FragmentPagerAdapter mAdapter;
    private List<String> list_title = new ArrayList<>();
    private Fragment mFragmentLiveLiving;
    private Fragment mFragmentLiveInteraction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_living);
        ButterKnife.bind(this);
        title.setText("中方信富首席工作室");
        edit.setVisibility(View.GONE);
        share.setVisibility(View.INVISIBLE);
        initData();
        initListener();
    }

    private void initData() {
        if(mFragmentLiveLiving == null){
            mFragmentLiveLiving = new FragmentLiveLiving();
        }
        if(mFragmentLiveInteraction == null){
            mFragmentLiveInteraction = new FragmentLiveInteraction();
        }
        if(list_fragment.size() == 0){
            list_fragment.add(mFragmentLiveLiving);
            list_fragment.add(mFragmentLiveInteraction);
        }

        if(list_title.size() == 0){
            String[] titleStrings = CommonUtils.getResource().getStringArray(R.array.fragment_living_titles);
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
                CommonUtils.setIndicator(mTabLayout, CommonUtils.px2dip(CommonUtils.getContext(),100)
                        ,CommonUtils.px2dip(CommonUtils.getContext(),100));
            }
        });

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
        if(!SpTools.getBoolean(this, Constants.isOpenApp,false)){
            Intent intent = new Intent(this, MainActivityTabHost.class);
            startActivity(intent);
            overridePendingTransition(0,0);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CommonUtils.dismissProgressDialog();
        finishAll();
        finish();
    }
}
