package com.zfxf.douniu.view.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.login.ActivityLogin;
import com.zfxf.douniu.base.BaseFragment;

import butterknife.ButterKnife;


public class FragmentMyself extends BaseFragment {
	private View view;
	@Override
	public View initView(LayoutInflater inflater) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_myself, null);
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
		Intent intent = new Intent(getActivity(), ActivityLogin.class);
		getActivity().startActivity(intent);
	}

	@Override
	public void initListener() {
		super.initListener();
	}
}