package com.zfxf.douniu.activity;

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
import com.zfxf.douniu.adapter.viewPager.BarItemAdapter;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.view.fragment.FragmentBarGrade;
import com.zfxf.douniu.view.fragment.FragmentMarketTrade;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author IMXU
 * @time   2017/5/3 13:26
 * @des    板块
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class ActivityMarketPlate extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.tl_market_plate)
    TabLayout mTabLayout;
    @BindView(R.id.vp_market_plate)
    ViewPager mViewPager;

    private List<Fragment> list_fragment = new ArrayList<>();
    private FragmentPagerAdapter mAdapter;
    private List<String> list_title = new ArrayList<>();
    private Fragment mFragmentMarketTrade;
    private Fragment mFragmentMarketIdea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_plate);
        ButterKnife.bind(this);
        title.setText("板块");
        edit.setVisibility(View.INVISIBLE);
        initdata();
        initListener();
    }

    private void initdata() {
        if (mFragmentMarketTrade == null) {
            mFragmentMarketTrade = new FragmentMarketTrade();
        }
        if (mFragmentMarketIdea == null) {
            mFragmentMarketIdea = new FragmentBarGrade();
        }
        if(list_fragment.size() == 0){
            list_fragment.add(mFragmentMarketTrade);
            list_fragment.add(mFragmentMarketIdea);
        }
        if(list_title.size() == 0){
            String[] titleStrings = CommonUtils.getResource().getStringArray(R.array.market_plate_titles);
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
                CommonUtils.setIndicator(mTabLayout, CommonUtils.px2dip(CommonUtils.getContext(),40)
                        ,CommonUtils.px2dip(CommonUtils.getContext(),40));
            }
        });
    }
    private void initListener() {
        back.setOnClickListener(this);
        edit.setOnClickListener(this);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CommonUtils.dismissProgressDialog();
    }
}
