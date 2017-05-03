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


public class FragmentNews extends BaseFragment implements View.OnClickListener{
	private View view;

	@BindView(R.id.tl_fragment_new)
	TabLayout mTabLayout;
	@BindView(R.id.vp_fragment_new)
	ViewPager mViewPager;
	private List<Fragment> list_fragment = new ArrayList<>();
	private FragmentPagerAdapter mAdapter;
	private List<String> list_title = new ArrayList<>();

	private FragmentNewExpress mNewExpress;
	private FragmentNewChoice mNewChoice;
	private FragmentNewNotice mNewNotice;

	@Override
	public View initView(LayoutInflater inflater) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_new, null);
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
		if(mNewExpress == null){
			mNewExpress = new FragmentNewExpress();
		}
		if(mNewChoice == null){
			mNewChoice = new FragmentNewChoice();
		}
		if(mNewNotice == null){
			mNewNotice = new FragmentNewNotice();
		}
		if(list_fragment.size() == 0){
			list_fragment.add(mNewChoice);
			list_fragment.add(mNewExpress);
			list_fragment.add(mNewNotice);
		}

		if(list_title.size() == 0){
			String[] titleStrings = CommonUtils.getResource().getStringArray(R.array.fragment_new_titles);
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
					CommonUtils.setIndicator(mTabLayout, CommonUtils.px2dip(CommonUtils.getContext(),60)
							,CommonUtils.px2dip(CommonUtils.getContext(),60));
				}
			});
		}
	}

	@Override
	public void initListener() {
		super.initListener();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()){

		}
	}
}