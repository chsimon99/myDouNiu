package com.zfxf.douniu.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.adapter.viewPager.BarItemAdapter;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.view.fragment.FragmentBarBar;
import com.zfxf.douniu.view.fragment.FragmentBarGrade;
import com.zfxf.douniu.view.fragment.FragmentBarZhibo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityBar extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;

    @BindView(R.id.tl_activity_bar)
    TabLayout mTabLayout;
    @BindView(R.id.vp_activity_bar)
    ViewPager mViewPager;

    private List<Fragment> list_fragment = new ArrayList<>();
    private FragmentPagerAdapter mAdapter;
    private List<String> list_title = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar);
        ButterKnife.bind(this);
        initdata();
        initListener();
    }

    private void initdata() {
        Fragment fragmentBarBar = new FragmentBarBar();
        Fragment fragmentBarZhibo = new FragmentBarZhibo();
        Fragment fragmentBarGrade = new FragmentBarGrade();
        if(list_fragment.size() == 0){
            list_fragment.add(fragmentBarBar);
            list_fragment.add(fragmentBarZhibo);
            list_fragment.add(fragmentBarGrade);
        }
        if(list_title.size() == 0){
            String[] titleStrings = CommonUtils.getResource().getStringArray(R.array.bar_item_titles);
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
                CommonUtils.setIndicator(mTabLayout, CommonUtils.dip2px(CommonUtils.getContext(),10)
                        ,CommonUtils.dip2px(CommonUtils.getContext(),9));
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
        list_fragment = null;
        list_title = null;
        mAdapter = null;
    }
}
