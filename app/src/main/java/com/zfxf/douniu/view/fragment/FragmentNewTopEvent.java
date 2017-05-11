package com.zfxf.douniu.view.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.ActivityHeadLineDetail;
import com.zfxf.douniu.adapter.recycleView.NewTopPolicyAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.view.RecycleViewDivider;
import com.zfxf.douniu.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * @author IMXU
 * @time   2017/5/3 13:25
 * @des    头条 事件追踪
 * 邮箱：butterfly_xu@sina.com
 *
*/

public class FragmentNewTopEvent extends BaseFragment {
	private View view;

	@BindView(R.id.rv_fragment_new_top_event)
	PullLoadMoreRecyclerView mRecyclerView;
	private NewTopPolicyAdapter mNewTopPolicyAdapter;
	private RecycleViewDivider mDivider;

	private int totlePage = 0;
	private int currentPage = 1;

	@Override
	public View initView(LayoutInflater inflater) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_new_top_event, null);
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
		currentPage = 1;
		mNewTopPolicyAdapter = null;
		CommonUtils.showProgressDialog(getActivity(),"加载中……");
		visitInternet();
	}

	private void visitInternet() {
		NewsInternetRequest.getHeadListInformation(1, currentPage + "", null, new NewsInternetRequest.ForResultEventInfoListener() {
			@Override
			public void onResponseMessage(List<Map<String, String>> lists, String totalpage) {

				totlePage = Integer.parseInt(totalpage);
				if (totlePage > 0 && currentPage <= totlePage){
					if (currentPage == 1) {
						if (mNewTopPolicyAdapter == null) {
							mNewTopPolicyAdapter = new NewTopPolicyAdapter(getActivity(), lists);
						}
						mRecyclerView.setLinearLayout();
						mRecyclerView.setAdapter(mNewTopPolicyAdapter);
						if (mDivider == null) {
							mDivider = new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL);
							mRecyclerView.addItemDecoration(mDivider);
						}
						mRecyclerView.setFooterViewText("加载更多……");
						mRecyclerView.setPullRefreshEnable(false);//禁止上啦刷新

						mNewTopPolicyAdapter.setOnItemClickListener(new NewTopPolicyAdapter.MyItemClickListener() {
							@Override
							public void onItemClick(View v, int infoId) {
								Intent intent = new Intent(CommonUtils.getContext(), ActivityHeadLineDetail.class);
								intent.putExtra("newsinfoId",infoId);
								startActivity(intent);
								getActivity().overridePendingTransition(0, 0);
							}
						});
					} else {
						mNewTopPolicyAdapter.addDatas(lists);
						mRecyclerView.post(new Runnable() {
							@Override
							public void run() {
								mNewTopPolicyAdapter.notifyDataSetChanged();
							}
						});
						mRecyclerView.postDelayed(new Runnable() {//防止滑动过快，loading界面显示太快
							@Override
							public void run() {
								mRecyclerView.setPullLoadMoreCompleted();
							}
						}, 1000);
					}
					currentPage++;
					CommonUtils.dismissProgressDialog();
				}else{
					mRecyclerView.postDelayed(new Runnable() {//防止滑动过快，loading界面显示太快
						@Override
						public void run() {
							mRecyclerView.setPullLoadMoreCompleted();
						}
					}, 1000);
					CommonUtils.dismissProgressDialog();
					return;
				}
			}
		}, null);
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
				}, 1000);
			}

			@Override
			public void onLoadMore() {
				if (currentPage > totlePage) {
					Toast.makeText(CommonUtils.getContext(), "没有数据了", Toast.LENGTH_SHORT).show();
					mRecyclerView.setPullLoadMoreCompleted();
					return;
				}
				visitInternet();
			}
		});
	}
}