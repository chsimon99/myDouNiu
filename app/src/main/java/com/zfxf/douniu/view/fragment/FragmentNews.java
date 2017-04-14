package com.zfxf.douniu.view.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FragmentNews extends BaseFragment implements View.OnClickListener{
	private View view;

	@BindView(R.id.tv_fragment_new_tt)
	TextView tt;
	@BindView(R.id.tv_fragment_new_kx)
	TextView kx;
	@BindView(R.id.tv_fragment_new_jx)
	TextView jx;
	@BindView(R.id.tv_fragment_new_gg)
	TextView gg;
	@BindView(R.id.v_fragment_new_tt)
	View v_tt;
	@BindView(R.id.v_fragment_new_kx)
	View v_kx;
	@BindView(R.id.v_fragment_new_jx)
	View v_jx;
	@BindView(R.id.v_fragment_new_gg)
	View v_gg;
	@BindView(R.id.rl_fragment_new_tt)
	RelativeLayout rl_tt;
	@BindView(R.id.rl_fragment_new_kx)
	RelativeLayout rl_kx;

	private FragmentTransaction mFt;
	private FragmentNewTop mNewTop;
	private FragmentNewExpress mNewExpress;
	private FragmentNewChoice mNewChoice;
	private FragmentNewNotice mNewNotice;
	private Fragment[] mFragments;
	private int mIndex = 0;

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

		if(mNewTop == null){
			mNewTop = new FragmentNewTop();
		}
		if(mNewExpress == null){
			mNewExpress = new FragmentNewExpress();
		}
		if(mNewChoice == null){
			mNewChoice = new FragmentNewChoice();
		}
		if(mNewNotice == null){
			mNewNotice = new FragmentNewNotice();
		}
		mFragments = new Fragment[]{mNewTop,mNewExpress,mNewChoice,mNewNotice};
		if(mFt == null){
			mFt = getActivity().getSupportFragmentManager().beginTransaction();
			mFt.add(R.id.fl_fragment_new_content, mNewTop);
			mFt.commit();
		}
		select(0);
	}

	@Override
	public void initListener() {
		super.initListener();
		tt.setOnClickListener(this);
		kx.setOnClickListener(this);
		jx.setOnClickListener(this);
		gg.setOnClickListener(this);

	}

	private void select(int index) {
		if(mIndex == index){
			return;
		}
		mFt = getActivity().getSupportFragmentManager().beginTransaction();
		mFt.hide(mFragments[mIndex]);//隐藏
		if(!mFragments[index].isAdded()){
			mFt.add(R.id.fl_fragment_new_content,mFragments[index]).show(mFragments[index]);
		}else{
			mFt.show(mFragments[index]);
		}
		mFt.commit();
		mIndex = index;
	}

	@Override
	public void onClick(View v) {
		reset();
		switch (v.getId()){
			case R.id.tv_fragment_new_tt:
				v_tt.setVisibility(View.VISIBLE);
				select(0);
				break;
			case R.id.tv_fragment_new_kx:
				v_kx.setVisibility(View.VISIBLE);
				select(1);
				break;
			case R.id.tv_fragment_new_jx:
				v_jx.setVisibility(View.VISIBLE);
				select(2);
				break;
			case R.id.tv_fragment_new_gg:
				v_gg.setVisibility(View.VISIBLE);
				select(3);
				break;
		}
	}

	private void reset() {
		v_tt.setVisibility(View.INVISIBLE);
		v_kx.setVisibility(View.INVISIBLE);
		v_jx.setVisibility(View.INVISIBLE);
		v_gg.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onResume() {
		super.onResume();
		reset();
		v_tt.setVisibility(View.VISIBLE);
		select(0);
	}
}