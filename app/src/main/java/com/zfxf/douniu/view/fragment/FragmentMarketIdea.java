package com.zfxf.douniu.view.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zfxf.douniu.R;
import com.zfxf.douniu.adapter.recycleView.MarketPlateAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.view.RecycleViewDivider;
import com.zfxf.douniu.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author IMXU
 * @time   2017/5/3 13:26
 * @des    概念板块
 * 邮箱：butterfly_xu@sina.com
 *
*/

public class FragmentMarketIdea extends BaseFragment implements View.OnClickListener{
	private View view;
	@BindView(R.id.rv_market_trade)
	PullLoadMoreRecyclerView mRecyclerView;
	private MarketPlateAdapter mPlateAdapter;
	private RecycleViewDivider mDivider;

	private int totlePage = 0;
	private int currentPage = 1;
	@Override
	public View initView(LayoutInflater inflater) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_market_trade, null);
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
	}

	private void visitInternet() {
		//		if(mPlateAdapter == null){
		//			mPlateAdapter = new MarketPlateAdapter(getActivity(),lists);
		//		}
		//
		//		mRecyclerView.setLinearLayout();
		//		mRecyclerView.setAdapter(mPlateAdapter);
		//		if(mDivider == null){//防止多次加载出现宽度变宽
		//			mDivider = new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL);
		//			mRecyclerView.addItemDecoration(mDivider);
		//		}
		//		mRecyclerView.setFooterViewText("加载更多……");
		//		mRecyclerView.setPullRefreshEnable(false);//禁止上拉刷新
		//
		//		mPlateAdapter.setOnItemClickListener(new MarketPlateAdapter.MyItemClickListener() {
		//			@Override
		//			public void onItemClick(View v, SimulationInfo bean) {
		//				Intent intent = new Intent(CommonUtils.getContext(), ActivityBarBarDetail.class);
		//				startActivity(intent);
		//				getActivity().overridePendingTransition(0, 0);
		//			}
		//		});
	}

	@Override
	public void initListener() {
		super.initListener();
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
	public void onClick(View v) {

	}
}