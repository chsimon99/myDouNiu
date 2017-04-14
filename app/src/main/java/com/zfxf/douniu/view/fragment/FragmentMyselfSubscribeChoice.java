package com.zfxf.douniu.view.fragment;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zfxf.douniu.R;
import com.zfxf.douniu.adapter.recycleView.MyselfSubscribeChoiceAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.view.RecycleViewDivider;
import com.zfxf.douniu.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FragmentMyselfSubscribeChoice extends BaseFragment {
	private View view;

	@BindView(R.id.tv_myself_subscribe_choice)
	TextView wait;

	@BindView(R.id.rv_myself_subscribe_choice)
	PullLoadMoreRecyclerView mRecyclerView;
	private MyselfSubscribeChoiceAdapter mSubscribeChoiceAdapter;
	private List<String> datas = new ArrayList<String>();
	private RecycleViewDivider mDivider;

	@Override
	public View initView(LayoutInflater inflater) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_myself_subscribe_choice, null);
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
//			wait.setVisibility(View.VISIBLE);
		}
		if(mSubscribeChoiceAdapter == null){
			mSubscribeChoiceAdapter = new MyselfSubscribeChoiceAdapter(getActivity(),datas);
		}

		mRecyclerView.setLinearLayout();
		mRecyclerView.setAdapter(mSubscribeChoiceAdapter);
		if(mDivider == null){
			mDivider = new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL,
					CommonUtils.dip2px(getActivity(), 5), Color.parseColor("#f4f4f4"));
			mRecyclerView.addItemDecoration(mDivider);
		}
		mRecyclerView.setPullRefreshEnable(false);
		mRecyclerView.setPushRefreshEnable(false);
	}

	@Override
	public void initListener() {
		super.initListener();

		mSubscribeChoiceAdapter.setOnItemClickListener(new MyselfSubscribeChoiceAdapter.MyItemClickListener() {
			@Override
			public void onItemClick(View v, int positon) {
				Toast.makeText(CommonUtils.getContext(),"点击了"+positon,Toast.LENGTH_SHORT).show();
			}
		});
	}
}