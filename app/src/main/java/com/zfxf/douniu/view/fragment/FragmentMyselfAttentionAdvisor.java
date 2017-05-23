package com.zfxf.douniu.view.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.ActivityAdvisorHome;
import com.zfxf.douniu.adapter.recycleView.HomeAdvisorAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.bean.IndexResult;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.view.RecycleViewDivider;
import com.zfxf.douniu.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author IMXU
 * @time   2017/5/3 13:29
 * @des    首席分析师列表
 * 邮箱：butterfly_xu@sina.com
 *
*/
@SuppressLint("ValidFragment")
public class FragmentMyselfAttentionAdvisor extends BaseFragment {
	private View view;

	@BindView(R.id.tv_myself_attention_advisor)
	TextView advisor;

	@BindView(R.id.rv_myself_attention_advisor)
	PullLoadMoreRecyclerView mRecyclerView;
	private HomeAdvisorAdapter mAttentionAdvisorAdapter;
	private RecycleViewDivider mDivider;

	private int type;
	private int totlePage = 0;
	private int currentPage = 1;

	public FragmentMyselfAttentionAdvisor(int type) {
		this.type = type;
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
		CommonUtils.showProgressDialog(getActivity(),"加载中……");
		visitInternet();
	}

	private void visitInternet() {
		NewsInternetRequest.getAdvisorListInformation(type, currentPage, null, new NewsInternetRequest.ForResultAdvisorJPListener() {
			@Override
			public void onResponseMessage(IndexResult indexResult) {
				totlePage = Integer.parseInt(indexResult.total);
				if (totlePage > 0 && currentPage <= totlePage){
					if(currentPage == 1){
						if(mAttentionAdvisorAdapter == null){
							mAttentionAdvisorAdapter = new HomeAdvisorAdapter(getActivity(),indexResult.shouxi_list);
						}
						mRecyclerView.setLinearLayout();
						mRecyclerView.setAdapter(mAttentionAdvisorAdapter);
						if(mDivider == null){
							mDivider = new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL);
							mRecyclerView.addItemDecoration(mDivider);
						}
						mRecyclerView.setPullRefreshEnable(false);//下拉刷新

						mAttentionAdvisorAdapter.setOnItemClickListener(new HomeAdvisorAdapter.MyItemClickListener() {
							@Override
							public void onItemClick(View v, int id) {
								Intent intent = new Intent(getActivity(), ActivityAdvisorHome.class);
								intent.putExtra("id",id);
								getActivity().startActivity(intent);
								getActivity().overridePendingTransition(0,0);
							}
						});
					}else {
						mAttentionAdvisorAdapter.addDatas(indexResult.shouxi_list);
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
					currentPage++;
					CommonUtils.dismissProgressDialog();
				}else {
					CommonUtils.dismissProgressDialog();
					return;
				}
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
}