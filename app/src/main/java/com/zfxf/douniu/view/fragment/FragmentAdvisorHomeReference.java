package com.zfxf.douniu.view.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zfxf.douniu.R;
import com.zfxf.douniu.adapter.recycleView.AdvisorHomeReferenceAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.view.RecycleViewDivider;
import com.zfxf.douniu.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FragmentAdvisorHomeReference extends BaseFragment {
	private View view;

	@BindView(R.id.rv_advisor_home_capital)
	PullLoadMoreRecyclerView mRecyclerView;
	private AdvisorHomeReferenceAdapter mReferenceAdapter;
	private List<String> datas = new ArrayList<String>();
	private RecycleViewDivider mDivider;

	@Override
	public View initView(LayoutInflater inflater) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_advisor_home_capital, null);
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
			datas.add("3");
		}
		if(mReferenceAdapter == null){
			mReferenceAdapter = new AdvisorHomeReferenceAdapter(getActivity(),datas);
		}

		mRecyclerView.setLinearLayout();
		mRecyclerView.setAdapter(mReferenceAdapter);
		if(mDivider == null){
			mDivider = new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL);
			mRecyclerView.addItemDecoration(mDivider);
		}
		mRecyclerView.setPullRefreshEnable(false);
		mRecyclerView.setPushRefreshEnable(false);
	}
	int num = 0;
	@Override
	public void initListener() {
		super.initListener();

		mReferenceAdapter.setOnItemClickListener(new AdvisorHomeReferenceAdapter.MyItemClickListener() {
			@Override
			public void onItemClick(View v, int positon) {
//				Toast.makeText(CommonUtils.getContext(),"点击了"+positon,Toast.LENGTH_SHORT).show();
			}
		});
	}
}