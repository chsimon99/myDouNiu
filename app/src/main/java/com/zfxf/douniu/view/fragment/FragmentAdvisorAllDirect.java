package com.zfxf.douniu.view.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zfxf.douniu.R;
import com.zfxf.douniu.adapter.recycleView.AdvisorAllDirectAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.view.RecycleViewDivider;
import com.zfxf.douniu.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FragmentAdvisorAllDirect extends BaseFragment implements View.OnClickListener{
	private View view;

	@BindView(R.id.rv_advisor_all_direct)
	PullLoadMoreRecyclerView mRecyclerView;
	private AdvisorAllDirectAdapter mAllDirectAdapter;
	private List<String> datas = new ArrayList<String>();
	private RecycleViewDivider mDivider;

	@BindView(R.id.ll_advisor_all_direct_hudong)
	LinearLayout hudong;
	@BindView(R.id.ll_advisor_all_direct_renqi)
	LinearLayout renqi;
	@BindView(R.id.ll_advisor_all_direct_guandian)
	LinearLayout guandian;

	@Override
	public View initView(LayoutInflater inflater) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_advisor_all_direct, null);
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
			datas.add("");
			datas.add("");
			datas.add("");
			datas.add("");
			datas.add("");
			datas.add("");
		}
		if(mAllDirectAdapter == null){
			mAllDirectAdapter = new AdvisorAllDirectAdapter(getActivity(),datas);
		}

		mRecyclerView.setLinearLayout();
		mRecyclerView.setAdapter(mAllDirectAdapter);
		if(mDivider == null){
			mDivider = new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL);
			mRecyclerView.addItemDecoration(mDivider);
		}
		mRecyclerView.setFooterViewText("加载更多……");
	}
	int num = 0;
	@Override
	public void initListener() {
		super.initListener();
		hudong.setOnClickListener(this);
		renqi.setOnClickListener(this);
		guandian.setOnClickListener(this);
		mRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
			@Override
			public void onRefresh() {
				mRecyclerView.postDelayed(new Runnable() {//防止滑动过快，loading界面显示太快
					@Override
					public void run() {
						mRecyclerView.setPullLoadMoreCompleted();
					}
				},1000);
			}

			@Override
			public void onLoadMore() {
				if(num > 1){
					Toast.makeText(CommonUtils.getContext(),"没有数据了",Toast.LENGTH_SHORT).show();
					mRecyclerView.setPullLoadMoreCompleted();
					return;
				}
				num++;
				mAllDirectAdapter.addDatas("2");
				mRecyclerView.post(new Runnable() {
					@Override
					public void run() {
						mAllDirectAdapter.notifyDataSetChanged();
					}
				});
				mRecyclerView.postDelayed(new Runnable() {//防止滑动过快，loading界面显示太快
					@Override
					public void run() {
						mRecyclerView.setPullLoadMoreCompleted();
					}
				},1000);
			}
		});

		mAllDirectAdapter.setOnItemClickListener(new AdvisorAllDirectAdapter.MyItemClickListener() {
			@Override
			public void onItemClick(View v, int positon) {
				Toast.makeText(CommonUtils.getContext(),"点击了"+positon,Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.ll_advisor_all_direct_hudong:
				CommonUtils.toastMessage("互动点击");
				break;
			case R.id.ll_advisor_all_direct_renqi:
				break;
			case R.id.ll_advisor_all_direct_guandian:
				break;
		}
	}
}