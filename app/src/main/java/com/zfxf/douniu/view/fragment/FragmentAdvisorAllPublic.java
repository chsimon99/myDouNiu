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
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.MyLunBo;
import com.zfxf.douniu.view.RecycleViewLunboDivider;
import com.zfxf.douniu.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FragmentAdvisorAllPublic extends BaseFragment{
	private View view;

	private List<Integer> mDatas = new ArrayList<Integer>();
	private MyLunBo mMyLunBO;

	@BindView(R.id.rv_advisor_all_public)
	PullLoadMoreRecyclerView mRecyclerView;
	private AdvisorAllPublicAdapter mAllPublicAdapter;
	private List<String> datas = new ArrayList<String>();
	private RecycleViewLunboDivider mDivider;

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

		if (mDatas.size() == 0) {
			mDatas.add(R.drawable.home_banner);
			mDatas.add(R.drawable.home_banner);
			mDatas.add(R.drawable.home_banner);
			mDatas.add(R.drawable.home_banner);
		}

		if(datas.size() == 0){
			datas.add("");
			datas.add("");
			datas.add("");
			datas.add("");
			datas.add("");
			datas.add("");
		}
		if(mAllPublicAdapter == null){
			mAllPublicAdapter = new AdvisorAllPublicAdapter(getActivity(),datas,mDatas);
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
	}
	int num = 0;
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
				if(num > 1){
					Toast.makeText(CommonUtils.getContext(),"没有数据了",Toast.LENGTH_SHORT).show();
					mRecyclerView.setPullLoadMoreCompleted();
					return;
				}
				num++;
				mAllPublicAdapter.addDatas("2");
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
		});

		mAllPublicAdapter.setOnItemClickListener(new AdvisorAllPublicAdapter.MyItemClickListener() {
			@Override
			public void onItemClick(View v, int positon) {
				Intent intent = new Intent(CommonUtils.getContext(), ActivityAdvisorAllPublicDetail.class);
				startActivity(intent);
				getActivity().overridePendingTransition(0,0);
			}
		});
		mAllPublicAdapter.setOnSubscribeClickListener(new AdvisorAllPublicAdapter.MySubscribeClickListener() {
			@Override
			public void onItemClick(View v, int positon,String type) {
				if(type.equals("已预约")){
					CommonUtils.toastMessage("提交服务器");
				}else if(type.equals("预约")){
					CommonUtils.toastMessage("取消");
				}
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
		super.onResume();
	}
}