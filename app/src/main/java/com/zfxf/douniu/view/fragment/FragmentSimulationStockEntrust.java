package com.zfxf.douniu.view.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.ActivitySimulationStock;
import com.zfxf.douniu.adapter.recycleView.SimulationEntrustAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.view.FullyLinearLayoutManager;
import com.zfxf.douniu.view.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FragmentSimulationStockEntrust extends BaseFragment {
	private View view;

	@BindView(R.id.rv_simulation_entrust)
	RecyclerView mRecyclerView;
	private SimulationEntrustAdapter mEntrustAdapter;
	private LinearLayoutManager mManager;
	private List<String> datas = new ArrayList<String>();
	private RecycleViewDivider mDivider;

	@Override
	public View initView(LayoutInflater inflater) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_simulation_entrust, null);
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
		if(datas.size() == 0){
			datas.add("1");
			datas.add("2");
			datas.add("2");
		}
		if(mEntrustAdapter == null){
			mEntrustAdapter = new SimulationEntrustAdapter(getActivity(),datas);
		}
		if(mManager == null){
			mManager = new FullyLinearLayoutManager(getActivity());
		}
		mRecyclerView.setLayoutManager(mManager);
		mRecyclerView.setAdapter(mEntrustAdapter);
		if(mDivider == null){
			mDivider = new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL);
			mRecyclerView.addItemDecoration(mDivider);
		}
	}
	@Override
	public void initListener() {
		super.initListener();
		ActivitySimulationStock.setOnRefreshListener(new ActivitySimulationStock.OnRefreshListener() {
			@Override
			public void refreshData() {
				CommonUtils.toastMessage("FragmentSimulationStockEntrust  点击了刷新");
			}
		});
	}
}