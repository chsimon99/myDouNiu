package com.zfxf.douniu.view.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.bar.ActivityMatador;
import com.zfxf.douniu.adapter.recycleView.MyselfAttentionMatadorAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.bean.MatadorResult;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.SpTools;
import com.zfxf.douniu.view.RecycleViewDivider;
import com.zfxf.douniu.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FragmentMyselfAttentionMatador extends BaseFragment {
	private View view;

	@BindView(R.id.tv_myself_attention_matador)
	TextView matador;

	@BindView(R.id.rv_myself_attention_matador)
	PullLoadMoreRecyclerView mRecyclerView;
	private MyselfAttentionMatadorAdapter mAttentionAdvisorAdapter;
	private RecycleViewDivider mDivider;
	private int totlePage = 0;
	private int currentPage = 1;

	@Override
	public View initView(LayoutInflater inflater) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_myself_attention_matador, null);
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
		NewsInternetRequest.getMySubscribeMadatorListInformation(currentPage,new NewsInternetRequest.ForResultMatadorIndexListener() {
			@Override
			public void onResponseMessage(MatadorResult result) {
				if(result.user_list.size()==0){
					matador.setVisibility(View.VISIBLE);
				}
					totlePage = Integer.parseInt(result.total);
					if (totlePage > 0 && currentPage <= totlePage){
						if(currentPage == 1){
							if(mAttentionAdvisorAdapter == null){
								mAttentionAdvisorAdapter = new MyselfAttentionMatadorAdapter(getActivity(),result.user_list);
							}

							mRecyclerView.setLinearLayout();
							mRecyclerView.setAdapter(mAttentionAdvisorAdapter);
							if(mDivider == null){
								mDivider = new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL);
								mRecyclerView.addItemDecoration(mDivider);
							}
							mRecyclerView.setPullRefreshEnable(false);
							mAttentionAdvisorAdapter.setOnItemClickListener(new MyselfAttentionMatadorAdapter.MyItemClickListener() {
								@Override
								public void onItemClick(View v, int id) {
									Intent intent = new Intent(getActivity(), ActivityMatador.class);
									intent.putExtra("id",id+"");
									startActivity(intent);
									getActivity().overridePendingTransition(0,0);
								}
							});

						}else {
							mAttentionAdvisorAdapter.addDatas(result.user_list);
							mRecyclerView.post(new Runnable() {
								@Override
								public void run() {
									mAttentionAdvisorAdapter.notifyDataSetChanged();
								}
							});
							mRecyclerView.postDelayed(new Runnable() {//防止滑动过快，loading界面显示太快
								@Override
								public void run() {
									mRecyclerView.setPullLoadMoreCompleted();
								}
							},1000);
						}
					}else {//关注取消后size为0时刷新界面

						if(mAttentionAdvisorAdapter == null){
							mAttentionAdvisorAdapter = new MyselfAttentionMatadorAdapter(getActivity(),result.user_list);
						}
						mRecyclerView.setLinearLayout();
						mRecyclerView.setAdapter(mAttentionAdvisorAdapter);
						mAttentionAdvisorAdapter.notifyDataSetChanged();
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
					},200);
					return;
				}
				visitInternet();
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		if(SpTools.getBoolean(getActivity(), Constants.alreadyGuanzhu,false)){
			currentPage = 1;
			mAttentionAdvisorAdapter = null;
			visitInternet();
			SpTools.setBoolean(getActivity(), Constants.alreadyGuanzhu,false);
		}
	}
}