package com.zfxf.douniu.view.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.MainActivityTabHost;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.view.MyViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FragmentAdvisor extends BaseFragment implements View.OnClickListener{
	private View view;

	@BindView(R.id.vp_fragment_advisor)
	MyViewPager mViewPager;

	private List<Fragment> list_fragment = new ArrayList<>();
	private myPageAdapter mAdapter;
	private List<String> list_title = new ArrayList<>();
	private Fragment mFragmentAdvisorAllDirect;
	private Fragment mFragmentAdvisorAllPublic;
	private Fragment mFragmentAdvisorAllSecret;
	private Fragment mFragmentAdvisorAllAsking;
	private Fragment mFragmentAdvisorAllReference;
	private Fragment mFragmentAdvisorAllGoldPond;

	@BindView(R.id.tv_fragment_advisor_direct)
	TextView tv_direct;
	@BindView(R.id.tv_fragment_advisor_public)
	TextView tv_public;
	@BindView(R.id.tv_fragment_advisor_secret)
	TextView tv_secret;
	@BindView(R.id.tv_fragment_advisor_gold)
	TextView tv_gold;
	@BindView(R.id.tv_fragment_advisor_ask)
	TextView tv_ask;
	@BindView(R.id.tv_fragment_advisor_reference)
	TextView tv_reference;
	@BindView(R.id.v_fragment_advisor_direct)
	View v_direct;
	@BindView(R.id.v_fragment_advisor_public)
	View v_public;
	@BindView(R.id.v_fragment_advisor_secret)
	View v_secret;
	@BindView(R.id.v_fragment_advisor_gold)
	View v_gold;
	@BindView(R.id.v_fragment_advisor_ask)
	View v_ask;
	@BindView(R.id.v_fragment_advisor_reference)
	View v_reference;
	@BindView(R.id.v_fragment_advisor_view)
	View v_v;
	@BindView(R.id.rl_fragment_advisor_view)
	RelativeLayout rl_view;

	private int lastPositon = 0;
	private int newPositon = 9;
	private int screenWidth;
	private RelativeLayout.LayoutParams mLp;
	private RelativeLayout.LayoutParams mRlp;

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
		initTabLineWidth();
		return view;
	}
	/**
	 * 设置滑动条的宽度为屏幕的1/6(根据Tab的个数而定)
	 */
	private void initTabLineWidth() {
		DisplayMetrics dpMetrics = new DisplayMetrics();
		getActivity().getWindow().getWindowManager().getDefaultDisplay().getMetrics(dpMetrics);
		screenWidth = dpMetrics.widthPixels;
		mLp = (RelativeLayout.LayoutParams) v_v.getLayoutParams();
		mLp.width = screenWidth / 6;
		mRlp = (RelativeLayout.LayoutParams) rl_view.getLayoutParams();
		mRlp.width = screenWidth / 6;
		rl_view.setLayoutParams(mRlp);
		rl_view.setPadding(CommonUtils.px2dip(getContext(),20),0,CommonUtils.px2dip(getContext(),20),0);
		v_v.setLayoutParams(mLp);
	}

	@Override
	public void init() {
		super.init();
		if(list_title.size() == 0){
			String[] titleStrings = CommonUtils.getResource().getStringArray(R.array.advisor_home_item_titles);
			for(int i = 0; i<titleStrings.length;i++){
				list_title.add(titleStrings[i]);
			}
		}
	}
	@Override
	public void initdata() {
		super.initdata();
		tv_direct.setText(list_title.get(0));
		tv_public.setText(list_title.get(1));
		tv_secret.setText(list_title.get(2));
		tv_gold.setText(list_title.get(3));
		tv_ask.setText(list_title.get(4));
		tv_reference.setText(list_title.get(5));

		if(mFragmentAdvisorAllDirect == null){
			mFragmentAdvisorAllDirect = new FragmentAdvisorAllDirect();
		}
		if(mFragmentAdvisorAllPublic == null){
			mFragmentAdvisorAllPublic = new FragmentAdvisorAllPublic();
		}
		if(mFragmentAdvisorAllSecret == null){
			mFragmentAdvisorAllSecret = new FragmentAdvisorAllSecret();
		}
		if(mFragmentAdvisorAllGoldPond == null){
			mFragmentAdvisorAllGoldPond = new FragmentAdvisorAllGoldPond();
		}
		if(mFragmentAdvisorAllAsking == null){
			mFragmentAdvisorAllAsking = new FragmentAdvisorAllAsking();
		}
		if(mFragmentAdvisorAllReference == null){
			mFragmentAdvisorAllReference = new FragmentAdvisorAllReference();
		}
		if(list_fragment.size() == 0){
			list_fragment.add(mFragmentAdvisorAllDirect);
			list_fragment.add(mFragmentAdvisorAllPublic);
			list_fragment.add(mFragmentAdvisorAllSecret);
			list_fragment.add(mFragmentAdvisorAllGoldPond);
			list_fragment.add(mFragmentAdvisorAllAsking);
			list_fragment.add(mFragmentAdvisorAllReference);
		}

		if (mAdapter == null) {
//			mAdapter = new BarItemAdapter(getChildFragmentManager(), list_fragment, list_title);
			mAdapter = new myPageAdapter(getChildFragmentManager(),list_fragment);
			mViewPager.setAdapter(mAdapter);
		}

		int index = MainActivityTabHost.getIndex();
		if(index > 0){
			newPositon = index-2;
			select(newPositon);
			mRlp.width = (newPositon+1) * screenWidth / 6;
			mLp.leftMargin = newPositon * (screenWidth / 6);
			rl_view.setLayoutParams(mRlp);
			v_v.setLayoutParams(mLp);
			MainActivityTabHost.setIndex(0);
			newPositon = 9;
			return;
		}
		select(lastPositon);
	}

	private void select(int i) {
		reset();
		switch (i){
			case 0:
				tv_direct.setTextColor(getActivity().getResources().getColor(R.color.colorWhite));
				break;
			case 1:
				tv_public.setTextColor(getActivity().getResources().getColor(R.color.colorWhite));
				break;
			case 2:
				tv_secret.setTextColor(getActivity().getResources().getColor(R.color.colorWhite));
				break;
			case 3:
				tv_gold.setTextColor(getActivity().getResources().getColor(R.color.colorWhite));
				break;
			case 4:
				tv_ask.setTextColor(getActivity().getResources().getColor(R.color.colorWhite));
				break;
			case 5:
				tv_reference.setTextColor(getActivity().getResources().getColor(R.color.colorWhite));
				break;
		}
		mViewPager.setCurrentItem(i);
	}

	private void reset() {
//		v_direct.setVisibility(View.INVISIBLE);
//		v_public.setVisibility(View.INVISIBLE);
//		v_secret.setVisibility(View.INVISIBLE);
//		v_gold.setVisibility(View.INVISIBLE);
//		v_ask.setVisibility(View.INVISIBLE);
//		v_reference.setVisibility(View.INVISIBLE);
		tv_direct.setTextColor(getActivity().getResources().getColor(R.color.colorGrayWhite));
		tv_public.setTextColor(getActivity().getResources().getColor(R.color.colorGrayWhite));
		tv_secret.setTextColor(getActivity().getResources().getColor(R.color.colorGrayWhite));
		tv_gold.setTextColor(getActivity().getResources().getColor(R.color.colorGrayWhite));
		tv_ask.setTextColor(getActivity().getResources().getColor(R.color.colorGrayWhite));
		tv_reference.setTextColor(getActivity().getResources().getColor(R.color.colorGrayWhite));
	}

	@Override
	public void initListener() {
		super.initListener();
		mViewPager.setOnPageChangeListener(new MyViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				/**
				 * 利用currentIndex(当前所在页面)和position(下一个页面)以及offset来
				 * 设置mTabLineIv的左边距 滑动场景：
				 * 记3个页面,
				 * 从左到右分别为0,1,2
				 * 0->1; 1->2; 2->1; 1->0
				 */
				if (lastPositon == 0 && position == 0){// 0->1
					getLeftMargin(positionOffset,0);
				}else if(lastPositon == 1 && position == 0){ // 1->0
					getLeftMargin(positionOffset,1);
				}else if(lastPositon == 1 && position == 1){ // 1->2
					getLeftMargin(positionOffset,0);
				}else if(lastPositon == 2 && position == 1){ // 2->1
					getLeftMargin(positionOffset,1);
				}else if(lastPositon == 2 && position == 2){
					getLeftMargin(positionOffset,0);
				}else if(lastPositon == 3 && position == 3){
					getLeftMargin(positionOffset,0);
				}else if(lastPositon == 4 && position == 4){
					getLeftMargin(positionOffset,0);
				}else if(lastPositon == 5 && position == 5){
					getLeftMargin(positionOffset,0);
				}else if(lastPositon == 5 && position == 4){
					getLeftMargin(positionOffset,1);
				}else if(lastPositon == 4 && position == 3){
					getLeftMargin(positionOffset,1);
				}else if(lastPositon == 3 && position == 2){
					getLeftMargin(positionOffset,1);
				}
				rl_view.setLayoutParams(mRlp);
				v_v.setLayoutParams(mLp);
			}

			private void getLeftMargin(float positionOffset,int offset) {
				int margin = (int) (-(offset-positionOffset) * (screenWidth * 1.0 / 6) + lastPositon * (screenWidth / 6));
				mLp.leftMargin = margin;
				mRlp.width = margin+screenWidth / 6;
			}

			@Override
			public void onPageSelected(int position) {
				lastPositon = position;
				select(position);
			}
			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
		tv_direct.setOnClickListener(this);
		tv_public.setOnClickListener(this);
		tv_secret.setOnClickListener(this);
		tv_gold.setOnClickListener(this);
		tv_ask.setOnClickListener(this);
		tv_reference.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.tv_fragment_advisor_direct:
				select(0);
				break;
			case R.id.tv_fragment_advisor_public:
				select(1);
				break;
			case R.id.tv_fragment_advisor_secret:
				select(2);
				break;
			case R.id.tv_fragment_advisor_gold:
				select(3);
				break;
			case R.id.tv_fragment_advisor_ask:
				select(4);
				break;
			case R.id.tv_fragment_advisor_reference:
				select(5);
				break;
		}

	}

	class myPageAdapter extends FragmentPagerAdapter {
		private List<Fragment> list_fragment = new ArrayList<Fragment>();

		public myPageAdapter(FragmentManager fm,List<Fragment> list) {
			super(fm);
			list_fragment = list;
		}

		@Override
		public Fragment getItem(int position) {
			return list_fragment.get(position);
		}

		@Override
		public int getCount() {
			return list_fragment.size();
		}

	}
}