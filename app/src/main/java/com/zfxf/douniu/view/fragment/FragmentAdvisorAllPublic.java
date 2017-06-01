package com.zfxf.douniu.view.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.advisor.ActivityAdvisorAllPublicDetail;
import com.zfxf.douniu.adapter.recycleView.AdvisorAllPublicAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.bean.LunBoListInfo;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.MyLunBo;
import com.zfxf.douniu.utils.SpTools;
import com.zfxf.douniu.view.RecycleViewLunboDivider;
import com.zfxf.douniu.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author IMXU
 * @time   2017/5/3 13:35
 * @des    首席 公开课
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class FragmentAdvisorAllPublic extends BaseFragment{
	private View view;
	private MyLunBo mMyLunBO;

	@BindView(R.id.rv_advisor_all_public)
	PullLoadMoreRecyclerView mRecyclerView;
	private AdvisorAllPublicAdapter mAllPublicAdapter;
	private RecycleViewLunboDivider mDivider;
	private int totlePage = 0;
	private int currentPage = 1;

	@Override
	public View initView(LayoutInflater inflater) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_advisor_all_public, null);
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
		mAllPublicAdapter = null;
		CommonUtils.showProgressDialog(getActivity(),"加载中……");
		visitInternet();

	}

	private void visitInternet() {
		NewsInternetRequest.getListInformation(currentPage+"", null, new NewsInternetRequest.ForResultPolicyInfoListener() {
			@Override
			public void onResponseMessage(List<Map<String, String>> lists, String totalpage, List<LunBoListInfo> lunbo_list) {
				totlePage = Integer.parseInt(totalpage);
				if (totlePage > 0 && currentPage <= totlePage){
					if(currentPage == 1){
						if(mAllPublicAdapter == null){
							mAllPublicAdapter = new AdvisorAllPublicAdapter(getActivity(),lists,lunbo_list);
							View view = View.inflate(getActivity(),R.layout.item_lunbo_with_gray,null);
							mAllPublicAdapter.setHeaderView(view);
						}

						mRecyclerView.setLinearLayout();
						mRecyclerView.setAdapter(mAllPublicAdapter);
						if(mDivider == null){
							mDivider = new RecycleViewLunboDivider(getActivity(), LinearLayoutManager.HORIZONTAL);
							mRecyclerView.addItemDecoration(mDivider);
						}
						mRecyclerView.setFooterViewText("加载更多……");
						mMyLunBO = mAllPublicAdapter.getLunBo();

						mAllPublicAdapter.setOnItemClickListener(new AdvisorAllPublicAdapter.MyItemClickListener() {
							@Override
							public void onItemClick(View v,int id,int isYd) {
								Intent intent = new Intent(CommonUtils.getContext(), ActivityAdvisorAllPublicDetail.class);
								intent.putExtra("id",id);
								startActivity(intent);
								getActivity().overridePendingTransition(0,0);
							}
						});
						mAllPublicAdapter.setOnSubscribeClickListener(new AdvisorAllPublicAdapter.MySubscribeClickListener() {
							@Override
							public void onItemClick(View v, int id,String type) {
								if(type.equals("已预约")){
									CommonUtils.toastMessage("预约成功");
								}else if(type.equals("预约")){
									CommonUtils.toastMessage("取消预约成功");
								}
							}
						});
					}else{
						mAllPublicAdapter.addDatas(lists);
						mRecyclerView.post(new Runnable() {
							@Override
							public void run() {
								mAllPublicAdapter.notifyDataSetChanged();
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
				currentPage = 1;
				mAllPublicAdapter = null;
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
		if(SpTools.getBoolean(getActivity(), Constants.subscribe,false)){//如果已经支付成功，重新刷新数据
			currentPage = 1;
			mAllPublicAdapter = null;
			CommonUtils.showProgressDialog(getActivity(),"加载中……");
			SpTools.setBoolean(getActivity(), Constants.subscribe,false);
			visitInternet();
		}
		super.onResume();
	}
}