package com.zfxf.douniu.view.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zfxf.douniu.R;
import com.zfxf.douniu.adapter.recycleView.HomeAdvisorAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.view.RecycleViewDivider;
import com.zfxf.douniu.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FragmentMyselfAttentionAdvisor extends BaseFragment {
	private View view;

	@BindView(R.id.tv_myself_attention_advisor)
	TextView advisor;

	@BindView(R.id.rv_myself_attention_advisor)
	PullLoadMoreRecyclerView mRecyclerView;
	private HomeAdvisorAdapter mAttentionAdvisorAdapter;
	private List<String> datas = new ArrayList<String>();
	private RecycleViewDivider mDivider;

	public FragmentMyselfAttentionAdvisor(List<String> data) {
		datas = data;
	}

	@Override
	public View initView(LayoutInflater inflater) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_myself_attention_advisor, null);
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

		if(mAttentionAdvisorAdapter == null){
			mAttentionAdvisorAdapter = new HomeAdvisorAdapter(getActivity(),datas);
		}

		mRecyclerView.setLinearLayout();
		mRecyclerView.setAdapter(mAttentionAdvisorAdapter);
		if(mDivider == null){
			mDivider = new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL);
			mRecyclerView.addItemDecoration(mDivider);
		}
		mRecyclerView.setPullRefreshEnable(false);
		mRecyclerView.setPushRefreshEnable(false);
	}

	@Override
	public void initListener() {
		super.initListener();

		mAttentionAdvisorAdapter.setOnItemClickListener(new HomeAdvisorAdapter.MyItemClickListener() {
			@Override
			public void onItemClick(View v, int positon) {
				Toast.makeText(CommonUtils.getContext(),"点击了"+positon,Toast.LENGTH_SHORT).show();
			}
		});
	}
}