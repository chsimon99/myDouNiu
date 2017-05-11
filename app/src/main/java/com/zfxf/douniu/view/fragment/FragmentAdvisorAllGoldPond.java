package com.zfxf.douniu.view.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zfxf.douniu.R;
import com.zfxf.douniu.adapter.viewPager.BarItemAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * @author IMXU
 * @time   2017/5/3 13:37
 * @des    首席 金股池
 * 邮箱：butterfly_xu@sina.com
 *
*/

public class FragmentAdvisorAllGoldPond extends BaseFragment {
    private View view;

    @BindView(R.id.tl_advisor_home_goldpond)
    TabLayout mTabLayout;
    @BindView(R.id.vp_advisor_home_goldpond)
    ViewPager mViewPager;

    private List<Fragment> list_fragment = new ArrayList<>();
    private FragmentPagerAdapter mAdapter;
    private List<String> list_title = new ArrayList<>();
    private Fragment mFragmentAdvisorAllGoldShort;
    private Fragment mFragmentAdvisorAllGoldMiddle;

    @Override
    public View initView(LayoutInflater inflater) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_advisor_home_goldpond, null);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void initdata() {
        super.initdata();
        if(mFragmentAdvisorAllGoldShort == null){
            mFragmentAdvisorAllGoldShort = new FragmentAdvisorAllGoldShort();
        }
        if(mFragmentAdvisorAllGoldMiddle == null){
            mFragmentAdvisorAllGoldMiddle = new FragmentAdvisorAllGoldMiddle();
        }
        if(list_fragment.size() == 0){
            list_fragment.add(mFragmentAdvisorAllGoldShort);
            list_fragment.add(mFragmentAdvisorAllGoldMiddle);
        }
        if(list_title.size() == 0){
            String[] titleStrings = CommonUtils.getResource().getStringArray(R.array.advisor_home_gold_titles);
            for(int i = 0; i<titleStrings.length;i++){
                list_title.add(titleStrings[i]);
            }
        }
        if(mAdapter == null){
            mAdapter = new BarItemAdapter(getActivity().getSupportFragmentManager(),list_fragment,list_title);
            mViewPager.setAdapter(mAdapter);
            mTabLayout.setupWithViewPager(mViewPager);
            mTabLayout.post(new Runnable() {//改变滑动条的长度
                @Override
                public void run() {
                    CommonUtils.setIndicator(mTabLayout, CommonUtils.px2dip(CommonUtils.getContext(),80)
                            ,CommonUtils.px2dip(CommonUtils.getContext(),80));
                }
            });
        }
    }

    @Override
    public void initListener() {
        super.initListener();
    }
}