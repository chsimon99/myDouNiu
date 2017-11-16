package com.zfxf.douniu.view.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.living.ActivityLiving;
import com.zfxf.douniu.adapter.recycleView.AdvisorHomeDirectAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.bean.LunBoListInfo;
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
 * @time   2017/5/3 13:11
 * @des    首席个人主页直播课
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class FragmentAdvisorHomeDirect extends BaseFragment {
	private View view;

	@BindView(R.id.rv_advisor_home_direct)
	PullLoadMoreRecyclerView mRecyclerView;
	private AdvisorHomeDirectAdapter mHomeDirectAdapter;
	private RecycleViewDivider mDivider;

	private int totlePage = 0;
	private int currentPage = 1;
	private int mId;
	private boolean isShow = false;

	@Override
	public View initView(LayoutInflater inflater) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_advisor_home_direct, null);
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
		if(!isShow){
			isShow = true;
			CommonUtils.showProgressDialog(getActivity(),"加载中……");
			visitInternet();
		}
	}
	private void visitInternet(){
		NewsInternetRequest.getLivingListInformation(0, currentPage+"", mId+"", new NewsInternetRequest.ForResultEventInfoListener() {
			@Override
			public void onResponseMessage(List<Map<String, String>> lists, String totalpage, List<LunBoListInfo> lunbo_list) {
				totlePage = Integer.parseInt(totalpage);
				if (totlePage > 0 && currentPage <= totlePage){
					if(currentPage == 1){
						if(mHomeDirectAdapter == null){
							mHomeDirectAdapter = new AdvisorHomeDirectAdapter(getActivity(),lists);
						}

						mRecyclerView.setLinearLayout();
						mRecyclerView.setAdapter(mHomeDirectAdapter);
						if(mDivider == null){
							mDivider = new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL);
							mRecyclerView.addItemDecoration(mDivider);
						}
						mRecyclerView.setPullRefreshEnable(false);//下拉刷新
						mRecyclerView.setFooterViewText("加载更多……");

						mHomeDirectAdapter.setOnItemClickListener(new AdvisorHomeDirectAdapter.MyItemClickListener() {
							@Override
							public void onItemClick(View v, int id,int status) {
								Intent intent = new Intent(CommonUtils.getContext(), ActivityLiving.class);
								intent.putExtra("id",id);
								intent.putExtra("status",status);
								startActivity(intent);
								getActivity().overridePendingTransition(0,0);
							}
						});
					}else {
						mHomeDirectAdapter.addDatas(lists);
						mRecyclerView.post(new Runnable() {
							@Override
							public void run() {
								mHomeDirectAdapter.notifyDataSetChanged();
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
				CommonUtils.dismissProgressDialog();
			}
		},getActivity().getResources().getString(R.string.zhibolist));

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
}