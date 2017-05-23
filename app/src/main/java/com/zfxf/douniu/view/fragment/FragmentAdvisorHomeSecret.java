package com.zfxf.douniu.view.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.advisor.ActivityAdvisorAllSecretDetail;
import com.zfxf.douniu.adapter.recycleView.AdvisorHomeSecretAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.SpTools;
import com.zfxf.douniu.view.RecycleViewDivider;
import com.zfxf.douniu.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author IMXU
 * @time   2017/5/3 13:12
 * @des    首席个人主页私密课
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class FragmentAdvisorHomeSecret extends BaseFragment {
	private View view;

	@BindView(R.id.rv_advisor_home_secret)
	PullLoadMoreRecyclerView mRecyclerView;
	private AdvisorHomeSecretAdapter mSecretAdapter;
	private RecycleViewDivider mDivider;
	private int totlePage = 0;
	private int currentPage = 1;
	private int mId;

	@Override
	public View initView(LayoutInflater inflater) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_advisor_home_secret, null);
		}
		ViewGroup parent = (ViewGroup) view.getParent();
		if(parent !=null){
			parent.removeView(view);
		}
		ButterKnife.bind(this,view);
		mId = getActivity().getIntent().getIntExtra("id", 0);
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
		mSecretAdapter = null;
		CommonUtils.showProgressDialog(getActivity(),"加载中……");
		visitInternet();
	}

	private void visitInternet() {
		NewsInternetRequest.getSimikeListInformation(currentPage + "", mId + "", new NewsInternetRequest.ForResultPointInfoListener() {
			@Override
			public void onResponseMessage(List<Map<String, String>> lists, String totalpage) {
				totlePage = Integer.parseInt(totalpage);
				if (totlePage > 0 && currentPage <= totlePage){
					if(currentPage == 1){
						if(mSecretAdapter == null){
							mSecretAdapter = new AdvisorHomeSecretAdapter(getActivity(),lists);
						}

						mRecyclerView.setLinearLayout();
						mRecyclerView.setAdapter(mSecretAdapter);
						if(mDivider == null){
							mDivider = new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL);
							mRecyclerView.addItemDecoration(mDivider);
						}
						mRecyclerView.setPullRefreshEnable(false);//下拉刷新
//						mRecyclerView.setPushRefreshEnable(false);

						mSecretAdapter.setOnItemClickListener(new AdvisorHomeSecretAdapter.MyItemClickListener() {
							@Override
							public void onItemClick(View v, int id) {
								Intent intent = new Intent(CommonUtils.getContext(), ActivityAdvisorAllSecretDetail.class);
								intent.putExtra("id",id);
								startActivity(intent);
								getActivity().overridePendingTransition(0,0);
							}
						});
					}else {
						mSecretAdapter.addDatas(lists);
						mRecyclerView.post(new Runnable() {
							@Override
							public void run() {
								mSecretAdapter.notifyDataSetChanged();
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
		},getActivity().getResources().getString(R.string.sikelist));
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
	public void onResume() {
		if(SpTools.getBoolean(getActivity(), Constants.buy,false)){//如果已经支付成功，重新刷新数据
			currentPage = 1;
			mSecretAdapter = null;
			CommonUtils.showProgressDialog(getActivity(),"加载中……");
			SpTools.setBoolean(getActivity(), Constants.buy,false);
			visitInternet();
		}
		super.onResume();
	}
}