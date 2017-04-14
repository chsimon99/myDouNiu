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


public class FragmentAdvisor extends BaseFragment {
	private View view;

	@BindView(R.id.tl_fragment_advisor)
	TabLayout mTabLayout;
	@BindView(R.id.vp_fragment_advisor)
	ViewPager mViewPager;

	private List<Fragment> list_fragment = new ArrayList<>();
	private FragmentPagerAdapter mAdapter;
	private List<String> list_title = new ArrayList<>();
	private Fragment mFragmentAdvisorAllDirect;
	private Fragment mFragmentAdvisorAllPublic;
	private Fragment mFragmentAdvisorAllSecret;
	private Fragment mFragmentAdvisorHomeOne;
	private Fragment mFragmentAdvisorHomeCapital;
	private Fragment mFragmentAdvisorHomeGold;

	@Override
	public View initView(LayoutInflater inflater) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_advisor, null);
		}
		ViewGroup parent = (ViewGroup) view.getParent();
		if(parent !=null){
			parent.removeView(view);
		}
		ButterKnife.bind(this,view);
		return view;
	}

	@Override
	public void init() {
		super.init();
	}
	@Override
	public void initdata() {
		super.initdata();
		if(mFragmentAdvisorAllDirect == null){
			mFragmentAdvisorAllDirect = new FragmentAdvisorAllDirect();
		}
		if(mFragmentAdvisorAllPublic == null){
			mFragmentAdvisorAllPublic = new FragmentAdvisorAllPublic();
		}
		if(mFragmentAdvisorAllSecret == null){
			mFragmentAdvisorAllSecret = new FragmentAdvisorAllSecret();
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
			list_fragment.add(mFragmentAdvisorAllDirect);
			list_fragment.add(mFragmentAdvisorAllPublic);
			list_fragment.add(mFragmentAdvisorAllSecret);
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
		if(mAdapter == null){
			mAdapter = new BarItemAdapter(getActivity().getSupportFragmentManager(),list_fragment,list_title);
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
	}

	@Override
	public void initListener() {
		super.initListener();
	}
}