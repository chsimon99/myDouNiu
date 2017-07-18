package com.zfxf.douniu.view.fragment;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zfxf.douniu.R;
import com.zfxf.douniu.adapter.recycleView.MyselfConsumeDoneAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.bean.PayListResult;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.SpTools;
import com.zfxf.douniu.view.RecycleViewDivider;
import com.zfxf.douniu.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FragmentMyselfConsumeDone extends BaseFragment {
	private View view;
	@BindView(R.id.tv_myself_consume_done)
	TextView wait;

	@BindView(R.id.rv_myself_consume_done)
	PullLoadMoreRecyclerView mRecyclerView;
	private MyselfConsumeDoneAdapter mConsumeDoneAdapter;
	private RecycleViewDivider mDivider;
	private int totlePage = 0;
	private int currentPage = 1;

	@Override
	public View initView(LayoutInflater inflater) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_myself_consume_done, null);
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
		CommonUtils.showProgressDialog(getActivity(),"加载中……");
		visitInternet();
		mRecyclerView.setPushRefreshEnable(false);
	}

	private void visitInternet() {
		NewsInternetRequest.getPayListInformation(currentPage, 1, null, new NewsInternetRequest.ForResultPayListSuccessInfoListener() {
			@Override
			public void onResponseMessage(PayListResult result) {
				if(result.pay_order.size()==0) {
					wait.setVisibility(View.VISIBLE);
				}else{
					totlePage = Integer.parseInt(result.total);
					if (totlePage > 0 && currentPage <= totlePage){
						if(currentPage == 1){
							if(mConsumeDoneAdapter == null){
								mConsumeDoneAdapter = new MyselfConsumeDoneAdapter(getActivity(),result.pay_order);
							}

							mRecyclerView.setLinearLayout();
							mRecyclerView.setAdapter(mConsumeDoneAdapter);
							if(mDivider == null){
								mDivider = new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL,
										CommonUtils.px2dip(getActivity(), 80), Color.parseColor("#f4f4f4"));
								mRecyclerView.addItemDecoration(mDivider);
							}
							mRecyclerView.setPullRefreshEnable(false);
							mConsumeDoneAdapter.setOnItemClickListener(new MyselfConsumeDoneAdapter.MyItemClickListener() {
								@Override
								public void onItemClick(View v, int positon) {
								}
							});
						}else {
							mConsumeDoneAdapter.addDatas(result.pay_order);
							mRecyclerView.post(new Runnable() {
								@Override
								public void run() {
									mConsumeDoneAdapter.notifyDataSetChanged();
								}
							});
							mRecyclerView.postDelayed(new Runnable() {//防止滑动过快，loading界面显示太快
								@Override
								public void run() {
									mRecyclerView.setPullLoadMoreCompleted();
								}
							},1000);
						}
						currentPage++;
					}
				}
				CommonUtils.dismissProgressDialog();
			}
		});
	}

	@Override
	public void initListener() {
		super.initListener();
		mRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
			@Override
			public void onRefresh() {
			}

			@Override
			public void onLoadMore() {
				if(currentPage > totlePage){
					Toast.makeText(CommonUtils.getContext(),"没有数据了",Toast.LENGTH_SHORT).show();
					mRecyclerView.postDelayed(new Runnable() {//防止滑动过快，loading界面显示太快
						@Override
						public void run() {
							mRecyclerView.setPullLoadMoreCompleted();
						}
					}, 200);
					return;
				}
				visitInternet();
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		if(SpTools.getBoolean(getContext(), Constants.buy,false)){
			SpTools.setBoolean(CommonUtils.getContext(),Constants.buy,false);
			currentPage = 1;
			totlePage = 0;
			visitInternet();
		}
	}
}