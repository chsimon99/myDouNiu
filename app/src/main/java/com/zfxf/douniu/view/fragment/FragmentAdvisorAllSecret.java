package com.zfxf.douniu.view.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.advisor.ActivityAdvisorAllSecretDetail;
import com.zfxf.douniu.adapter.recycleView.AdvisorAllSecretAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.MyLunBo;
import com.zfxf.douniu.utils.SpTools;
import com.zfxf.douniu.view.RecycleViewDivider;
import com.zfxf.douniu.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * @author IMXU
 * @time   2017/5/3 13:36
 * @des    首席 私密课
 * 邮箱：butterfly_xu@sina.com
 *
*/

public class FragmentAdvisorAllSecret extends BaseFragment{
	private View view;

	private List<Integer> mDatas = new ArrayList<Integer>();
	private MyLunBo mMyLunBO;

	@BindView(R.id.rv_advisor_all_secret)
	PullLoadMoreRecyclerView mRecyclerView;
	private AdvisorAllSecretAdapter mAllSecretAdapter;
	private RecycleViewDivider mDivider;
	private int totlePage = 0;
	private int currentPage = 1;

	@Override
	public View initView(LayoutInflater inflater) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_advisor_all_secret, null);
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
		if (mDatas.size() == 0) {
			mDatas.add(R.drawable.home_banner);
			mDatas.add(R.drawable.home_banner);
			mDatas.add(R.drawable.home_banner);
			mDatas.add(R.drawable.home_banner);
		}

		currentPage = 1;
		mAllSecretAdapter = null;
		CommonUtils.showProgressDialog(getActivity(),"加载中……");
		visitInternet();

	}

	private void visitInternet(){
		NewsInternetRequest.getSimikeListInformation(currentPage + "", null, new NewsInternetRequest.ForResultPointInfoListener() {
			@Override
			public void onResponseMessage(List<Map<String, String>> lists, String totalpage) {
				totlePage = Integer.parseInt(totalpage);
				if (totlePage > 0 && currentPage <= totlePage){
					if(currentPage == 1){
						if(mAllSecretAdapter == null){
							mAllSecretAdapter = new AdvisorAllSecretAdapter(getActivity(),lists,mDatas);
							View view = View.inflate(getActivity(),R.layout.item_lunbo_with_gray,null);
							mAllSecretAdapter.setHeaderView(view);
						}

						mRecyclerView.setLinearLayout();
						mRecyclerView.setAdapter(mAllSecretAdapter);
						if(mDivider == null){
							mDivider = new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL);
							mRecyclerView.addItemDecoration(mDivider);
						}
						mRecyclerView.setFooterViewText("加载更多……");
						mMyLunBO = mAllSecretAdapter.getLunBo();

						mAllSecretAdapter.setOnItemClickListener(new AdvisorAllSecretAdapter.MyItemClickListener() {
							@Override
							public void onItemClick(View v, int id) {
								Intent intent = new Intent(CommonUtils.getContext(), ActivityAdvisorAllSecretDetail.class);
								intent.putExtra("id",id);
								startActivity(intent);
								getActivity().overridePendingTransition(0,0);
							}
						});
					}else {
						mAllSecretAdapter.addDatas(lists);
						mRecyclerView.post(new Runnable() {
							@Override
							public void run() {
								mAllSecretAdapter.notifyDataSetChanged();
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
				currentPage = 1;
				mAllSecretAdapter = null;
				CommonUtils.showProgressDialog(getActivity(),"加载中……");
				visitInternet();
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

	private boolean isOnPause = false;
	@Override
	public void onPause() {
		isOnPause = true;
		if (mMyLunBO != null)
			mMyLunBO.stopLunBO();
		super.onPause();
	}
	@Override
	public void onResume() {
		if (isOnPause) {//防止轮播图暂定不动
			if (mMyLunBO != null)
				mMyLunBO.restartLunBO();//不用restart是为了防止突然轮播的速度快
			isOnPause = false;
		}
		if(SpTools.getBoolean(getActivity(), Constants.buy,false)){//如果已经支付成功，重新刷新数据
			currentPage = 1;
			mAllSecretAdapter = null;
			CommonUtils.showProgressDialog(getActivity(),"加载中……");
			SpTools.setBoolean(getActivity(), Constants.buy,false);
			visitInternet();
		}
		super.onResume();
	}
}