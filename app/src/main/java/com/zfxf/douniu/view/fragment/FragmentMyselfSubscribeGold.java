package com.zfxf.douniu.view.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.goldpool.ActivityGoldPond;
import com.zfxf.douniu.adapter.recycleView.MyselfSubscribeGoldAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.bean.SimulationResult;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.view.RecycleViewDivider;
import com.zfxf.douniu.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FragmentMyselfSubscribeGold extends BaseFragment {
	private View view;

	@BindView(R.id.tv_myself_subscribe_gold)
	TextView wait;

	@BindView(R.id.rv_myself_subscribe_gold)
	PullLoadMoreRecyclerView mRecyclerView;
	private MyselfSubscribeGoldAdapter mSubscribeGoldAdapter;
	private RecycleViewDivider mDivider;

	@Override
	public View initView(LayoutInflater inflater) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_myself_subscribe_gold, null);
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
		visitInternet();
	}

	private void visitInternet() {
		CommonUtils.showProgressDialog(getActivity(),"加载中……");
		NewsInternetRequest.getGoldPondListInformation("", 2, new NewsInternetRequest.ForResultGoldPoneShortStockListener() {
			@Override
			public void onResponseMessage(SimulationResult result) {
				if(result.dn_jgc.size()==0){
					wait.setVisibility(View.VISIBLE);
				}else{
					if(mSubscribeGoldAdapter == null){
						mSubscribeGoldAdapter = new MyselfSubscribeGoldAdapter(getActivity(),result.dn_jgc);
					}

					mRecyclerView.setLinearLayout();
					mRecyclerView.setAdapter(mSubscribeGoldAdapter);
					if(mDivider == null){
						mDivider = new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL);
						mRecyclerView.addItemDecoration(mDivider);
					}
					mRecyclerView.setPullRefreshEnable(false);
					mRecyclerView.setPushRefreshEnable(false);
					mSubscribeGoldAdapter.setOnItemClickListener(new MyselfSubscribeGoldAdapter.MyItemClickListener() {
						@Override
						public void onItemClick(View v, int id,int jgc) {
							Intent intent = new Intent(CommonUtils.getContext(), ActivityGoldPond.class);
							intent.putExtra("id",id);
							intent.putExtra("jgcId",jgc);
							startActivity(intent);
							getActivity().overridePendingTransition(0,0);
						}
					});
				}
				CommonUtils.dismissProgressDialog();
			}
		},null);
	}

	@Override
	public void initListener() {
		super.initListener();
	}
}