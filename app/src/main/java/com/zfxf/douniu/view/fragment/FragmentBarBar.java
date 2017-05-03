package com.zfxf.douniu.view.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zfxf.douniu.R;
import com.zfxf.douniu.adapter.recycleView.BarBarAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.view.RecycleViewDivider;
import com.zfxf.douniu.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FragmentBarBar extends BaseFragment {
	private View view;
	@BindView(R.id.rv_bar_bar)
	PullLoadMoreRecyclerView mRecyclerView;
	private BarBarAdapter mBarAdapter;
	private List<String> datas = new ArrayList<String>();
	private RecycleViewDivider mDivider;

	@Override
	public View initView(LayoutInflater inflater) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_bar_bar, null);
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
		if(datas.size() == 0){
			datas.add("");
			datas.add("");
			datas.add("");
			datas.add("");
		}
		if(mBarAdapter == null){
			mBarAdapter = new BarBarAdapter(getActivity(),datas);
		}

		mRecyclerView.setLinearLayout();
		mRecyclerView.setAdapter(mBarAdapter);
		if(mDivider == null){//防止多次加载出现宽度变宽
			mDivider = new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL);
			mRecyclerView.addItemDecoration(mDivider);
		}
//		mRecyclerView.addItemDecoration(
//				new RecycleViewDivider(getActivity(),LinearLayoutManager.HORIZONTAL,
//						CommonUtils.px2dip(getActivity(),20), Color.parseColor("#f4f4f4")));
		mRecyclerView.setFooterViewText("加载更多……");


		super.initdata();

	}
	int num = 0;
	@Override
	public void initListener() {
		mBarAdapter.setOnItemClickListener(new BarBarAdapter.MyItemClickListener() {
			@Override
			public void onItemClick(View v, int positon) {
				Toast.makeText(CommonUtils.getContext(),"点击了"+positon,Toast.LENGTH_SHORT).show();
			}
		});

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
				List<String> newdatas = new ArrayList<String>();
				newdatas.add("1");
				mBarAdapter.addDatas(newdatas);
				mRecyclerView.post(new Runnable() {
					@Override
					public void run() {
						mBarAdapter.notifyDataSetChanged();
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
		super.initListener();
	}
}