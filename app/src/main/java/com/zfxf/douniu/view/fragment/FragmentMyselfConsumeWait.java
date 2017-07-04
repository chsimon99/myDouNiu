package com.zfxf.douniu.view.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.ActivityToPay;
import com.zfxf.douniu.adapter.recycleView.MyselfConsumeWaitAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.bean.PayListResult;
import com.zfxf.douniu.bean.PayOrderInfo;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.SpTools;
import com.zfxf.douniu.view.RecycleViewDivider;
import com.zfxf.douniu.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FragmentMyselfConsumeWait extends BaseFragment {
	private View view;

	@BindView(R.id.tv_myself_consume_wait)
	TextView wait;

	@BindView(R.id.rv_myself_consume_wait)
	PullLoadMoreRecyclerView mRecyclerView;
	private MyselfConsumeWaitAdapter mConsumeWaitAdapter;
	private RecycleViewDivider mDivider;
	private int totlePage = 0;
	private int currentPage = 1;

	@Override
	public View initView(LayoutInflater inflater) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_myself_consume_wait, null);
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
		NewsInternetRequest.getPayListInformation(currentPage, 0, new NewsInternetRequest.ForResultPayListInfoListener() {
			@Override
			public void onResponseMessage(PayListResult result) {
				if(result.pay_order.size()==0) {
					wait.setVisibility(View.VISIBLE);
				}else {
					totlePage = Integer.parseInt(result.total);
					if (totlePage > 0 && currentPage <= totlePage){
						if(currentPage == 1){
							if(mConsumeWaitAdapter == null){
								mConsumeWaitAdapter = new MyselfConsumeWaitAdapter(getActivity(),result.pay_order);
							}

							mRecyclerView.setLinearLayout();
							mRecyclerView.setAdapter(mConsumeWaitAdapter);
							if(mDivider == null){
								mDivider = new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL,
										CommonUtils.px2dip(getActivity(), 80), Color.parseColor("#f4f4f4"));
								mRecyclerView.addItemDecoration(mDivider);
							}
							mRecyclerView.setPullRefreshEnable(false);
							mConsumeWaitAdapter.setOnItemClickListener(new MyselfConsumeWaitAdapter.MyItemClickListener() {
								@Override
								public void onItemClick(View v, int positon , PayOrderInfo info) {
									Intent intent = new Intent(getActivity(),ActivityToPay.class);
									intent.putExtra("info",info.pmo_sm);
									if(info.type.equals("0")){
										intent.putExtra("type",info.pmo_info);
									}else if(info.type.equals("1")){
										intent.putExtra("type","金股池");
									}else if(info.type.equals("3")){
										intent.putExtra("type","私密课");
									}else if(info.type.equals("5")){
										intent.putExtra("type","大参考");
									}else if(info.type.equals("2")){
										if(info.pmo_info.equals("一元看股")){
											intent.putExtra("type","一元偷偷看");
										}else {
											intent.putExtra("type","微问答");
										}
									}else if(info.type.equals("4")){
										intent.putExtra("type","打赏");
									}
									intent.putExtra("from",info.ud_nickname);
									intent.putExtra("count",info.pmo_fee);
									intent.putExtra("sx_id",info.sx_ub_id);
									intent.putExtra("order",info.pmo_order);
									if(info.pmo_info.contains("充值")){
										intent.putExtra("change",1);
									}
									startActivity(intent);
									getActivity().overridePendingTransition(0,0);

								}
							});
						}else {
							mConsumeWaitAdapter.addDatas(result.pay_order);
							mRecyclerView.post(new Runnable() {
								@Override
								public void run() {
									mConsumeWaitAdapter.notifyDataSetChanged();
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
		},null);
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
		if(SpTools.getBoolean(getContext(), Constants.buy, true)){
			SpTools.setBoolean(getContext(), Constants.buy,false);
			currentPage = 1;
			totlePage = 0;
			visitInternet();
		}
	}
}