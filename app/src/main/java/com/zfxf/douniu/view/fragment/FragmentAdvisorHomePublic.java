package com.zfxf.douniu.view.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.advisor.ActivityAdvisorAllPublicDetail;
import com.zfxf.douniu.adapter.recycleView.AdvisorHomePublicAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.bean.LunBoListInfo;
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
 * @des    首席个人主页公开课
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class FragmentAdvisorHomePublic extends BaseFragment {
	private View view;

	@BindView(R.id.rv_advisor_home_public)
	PullLoadMoreRecyclerView mRecyclerView;
	private AdvisorHomePublicAdapter mPublicAdapter;
	private RecycleViewDivider mDivider;

	private int totlePage = 0;
	private int currentPage = 1;
	private int mId;

	@Override
	public View initView(LayoutInflater inflater) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_advisor_home_public, null);
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
		mPublicAdapter = null;
		CommonUtils.showProgressDialog(getActivity(),"加载中……");
		visitInternet();
	}

	private void visitInternet() {
		NewsInternetRequest.getListInformation(currentPage + "", mId + "", new NewsInternetRequest.ForResultPolicyInfoListener() {
			@Override
			public void onResponseMessage(List<Map<String, String>> lists, String totalpage, List<LunBoListInfo> lunbo_list) {
				totlePage = Integer.parseInt(totalpage);
				if (totlePage > 0 && currentPage <= totlePage){
					if(currentPage == 1){
						if(mPublicAdapter == null){
							mPublicAdapter = new AdvisorHomePublicAdapter(getActivity(),lists);
						}

						mRecyclerView.setLinearLayout();
						mRecyclerView.setAdapter(mPublicAdapter);
						if(mDivider == null){
							mDivider = new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL);
							mRecyclerView.addItemDecoration(mDivider);
						}
						mRecyclerView.setPullRefreshEnable(false);//下拉刷新
						mRecyclerView.setFooterViewText("加载更多……");

						mPublicAdapter.setOnItemClickListener(new AdvisorHomePublicAdapter.MyItemClickListener() {
							@Override
							public void onItemClick(View v, int id,int isYd) {
								Intent intent = new Intent(CommonUtils.getContext(), ActivityAdvisorAllPublicDetail.class);
								intent.putExtra("id",id);
								startActivity(intent);
								getActivity().overridePendingTransition(0,0);
							}
						});
						mPublicAdapter.setOnSubscribeClickListener(new AdvisorHomePublicAdapter.MySubscribeClickListener() {
							@Override
							public void onItemClick(View v, int id, String type) {
								if(type.equals("已预约")){
									CommonUtils.toastMessage("预约成功");
								}else if(type.equals("预约")){
									CommonUtils.toastMessage("取消预约成功");
								}
							}
						});

					}else {
						mPublicAdapter.addDatas(lists);
						mRecyclerView.post(new Runnable() {
							@Override
							public void run() {
								mPublicAdapter.notifyDataSetChanged();
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
		},getActivity().getResources().getString(R.string.gongkelist));
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
		if(SpTools.getBoolean(getActivity(), Constants.subscribe,false)){//如果已经支付成功，重新刷新数据
			currentPage = 1;
			mPublicAdapter = null;
			CommonUtils.showProgressDialog(getActivity(),"加载中……");
			SpTools.setBoolean(getActivity(), Constants.subscribe,false);
			visitInternet();
		}
		super.onResume();
	}
}