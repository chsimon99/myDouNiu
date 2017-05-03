package com.zfxf.douniu.view.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.ActivitySimulationStock;
import com.zfxf.douniu.adapter.recycleView.MatadorPositonAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.view.FullyLinearLayoutManager;
import com.zfxf.douniu.view.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FragmentSimulationStockSold extends BaseFragment {
	private View view;

	@BindView(R.id.rv_fragment_simulation_buy)
	RecyclerView mRecyclerView;
	private LinearLayoutManager mLayoutManager;
	private MatadorPositonAdapter mPositonAdapter;
	private List<String> mStrings = new ArrayList<String>();
	private RecycleViewDivider mDivider;
	@Override
	public View initView(LayoutInflater inflater) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_simulation_sold, null);
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
		if (mStrings.size() == 0) {
			mStrings.add("");
			mStrings.add("");
			mStrings.add("");
			mStrings.add("");
		}
		if(mLayoutManager == null){
			mLayoutManager = new FullyLinearLayoutManager(getActivity());
		}
		if(mPositonAdapter == null){
			mPositonAdapter = new MatadorPositonAdapter(getActivity(), mStrings);
		}
		if(mDivider == null){//防止多次加载出现宽度变宽
			mDivider = new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL);
			mRecyclerView.addItemDecoration(mDivider);
		}
		mRecyclerView.setLayoutManager(mLayoutManager);
		mRecyclerView.setAdapter(mPositonAdapter);
	}
	@Override
	public void initListener() {
		super.initListener();
		ActivitySimulationStock.setOnRefreshListener(new ActivitySimulationStock.OnRefreshListener() {
			@Override
			public void refreshData() {
				CommonUtils.toastMessage("FragmentSimulationStockSold  点击了刷新");
			}
		});
	}
}