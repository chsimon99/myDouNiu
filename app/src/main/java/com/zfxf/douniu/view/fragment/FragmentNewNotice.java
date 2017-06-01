package com.zfxf.douniu.view.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.ActivityInformation;
import com.zfxf.douniu.adapter.recycleView.NewNoticeAdapter;
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
 * @time   2017/5/3 13:24
 * @des    资讯 公告
 * 邮箱：butterfly_xu@sina.com
 *
*/

public class FragmentNewNotice extends BaseFragment {
	private View view;

	@BindView(R.id.rv_fragment_new_notice)
	PullLoadMoreRecyclerView mRecyclerView;
	private NewNoticeAdapter mNoticeAdapter;
	private RecycleViewDivider mDivider;
	private int totlePage = 0;
	private int currentPage = 1;
	@Override
	public View initView(LayoutInflater inflater) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_new_notice, null);
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

	private void visitInternet() {
		NewsInternetRequest.getNewsListInformation(2, currentPage + "", 0+"",null,null,new NewsInternetRequest.ForResultPointInfoListener() {
			@Override
			public void onResponseMessage(List<Map<String, String>> lists, String totalpage, List<LunBoListInfo> lunbo_list) {
				totlePage = Integer.parseInt(totalpage);
				if (totlePage > 0 && currentPage <= totlePage){
					if(currentPage == 1){
						if (mNoticeAdapter == null) {
							mNoticeAdapter = new NewNoticeAdapter(getActivity(), lists);
						}

						mRecyclerView.setLinearLayout();
						mRecyclerView.setAdapter(mNoticeAdapter);
						if(mDivider == null){//防止多次加载出现宽度变宽
							mDivider = new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL);
							mRecyclerView.addItemDecoration(mDivider);
						}
						mRecyclerView.setFooterViewText("加载更多……");
						mRecyclerView.setPullRefreshEnable(false);//禁止上拉刷新
						mNoticeAdapter.setOnItemClickListener(new NewNoticeAdapter.MyItemClickListener() {
							@Override
							public void onItemClick(View v, int id) {
								Intent intent = new Intent(getActivity(), ActivityInformation.class);
								intent.putExtra("type","公告详情");
								intent.putExtra("newsinfoId",id);
								startActivity(intent);
								getActivity().overridePendingTransition(0,0);
							}
						});
					}else {
						mNoticeAdapter.addDatas(lists);
						mRecyclerView.post(new Runnable() {
							@Override
							public void run() {
								mNoticeAdapter.notifyDataSetChanged();
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
		});
	}
}