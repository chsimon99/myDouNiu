package com.zfxf.douniu.view.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.ActivityMarketResearch;
import com.zfxf.douniu.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * @author IMXU
 * @time   2017/5/3 13:42
 * @des    主页 行情
 * 邮箱：butterfly_xu@sina.com
 *
*/

public class FragmentMarket extends BaseFragment implements View.OnClickListener{
	private View view;

	@BindView(R.id.tv_fragment_market_market)
	TextView market;
	@BindView(R.id.tv_fragment_market_select)
	TextView select;

	@BindView(R.id.rl_fragment_market_search)
	RelativeLayout search;

	private FragmentTransaction mFt;
	private FragmentMarketMarket mMarketMarket;
	private FragmentMarketSelect mMarketSelect;
	private Fragment[] mFragments;
	private int mIndex = 0;

	@Override
	public View initView(LayoutInflater inflater) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_market, null);
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
		if(mMarketMarket == null){
			mMarketMarket = new FragmentMarketMarket();
		}
		if(mMarketSelect == null){
			mMarketSelect = new FragmentMarketSelect();
		}
		if(mFragments == null){
			mFragments = new Fragment[]{mMarketMarket, mMarketSelect};
		}
		if(mFt == null){
			mFt = getActivity().getSupportFragmentManager().beginTransaction();
			mFt.add(R.id.fl_market, mMarketMarket);
			mFt.commit();
		}
		select(0);
	}

	@Override
	public void initListener() {
		super.initListener();
		market.setOnClickListener(this);
		select.setOnClickListener(this);
		search.setOnClickListener(this);
	}
	private void select(int index) {
		if(mIndex == index){
			return;
		}
		mFt = getActivity().getSupportFragmentManager().beginTransaction();
		mFt.hide(mFragments[mIndex]);//隐藏
		if(!mFragments[index].isAdded()){
			mFt.add(R.id.fl_market,mFragments[index]).show(mFragments[index]);
		}else{
			mFt.show(mFragments[index]);
		}
		mFt.commit();
		mIndex = index;
	}

	@Override
	public void onStart() {
		switch (mIndex){
			case 0:
				select(0);
				selectMarket();
				break;
			case 1:
				select(1);
				selectSelect();
				break;
		}
		super.onStart();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.tv_fragment_market_market:
				selectMarket();
				select(0);
				break;
			case R.id.tv_fragment_market_select:
				selectSelect();
				select(1);
				break;
			case R.id.rl_fragment_market_search:
				Intent intent = new Intent(getActivity(), ActivityMarketResearch.class);
				startActivity(intent);
				getActivity().overridePendingTransition(0,0);
				break;
		}
	}

	private void selectSelect() {
		select.setTextColor(Color.parseColor("#FB6733"));
		select.setBackgroundResource(R.drawable.backgroud_bg_white_right);
		market.setTextColor(Color.parseColor("#FFFFFF"));
		market.setBackgroundResource(R.drawable.backgroud_bg_null);
	}

	private void selectMarket() {
		market.setTextColor(Color.parseColor("#FB6733"));
		market.setBackgroundResource(R.drawable.backgroud_bg_white_left);
		select.setTextColor(Color.parseColor("#FFFFFF"));
		select.setBackgroundResource(R.drawable.backgroud_bg_null);
	}
}