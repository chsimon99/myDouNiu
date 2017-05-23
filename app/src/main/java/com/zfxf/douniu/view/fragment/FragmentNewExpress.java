package com.zfxf.douniu.view.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zfxf.douniu.R;
import com.zfxf.douniu.adapter.recycleView.NewExpressAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * @author IMXU
 * @time   2017/5/3 13:22
 * @des    资讯 快讯
 * 邮箱：butterfly_xu@sina.com
 *
*/

public class FragmentNewExpress extends BaseFragment {
	private View view;

	@BindView(R.id.rv_fragment_new_express)
	PullLoadMoreRecyclerView mRecyclerView;
	private NewExpressAdapter mExpressAdapter;
	private int totlePage = 0;
	private int currentPage = 1;
	private String mLastId = "0";

	@Override
	public View initView(LayoutInflater inflater) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_new_express, null);
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
				mExpressAdapter.deleteAll();
				mExpressAdapter = null;
				currentPage = 1;
				mLastId = "0";
				visitInternet();
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
		NewsInternetRequest.getNewsListInformation(1, currentPage + "", mLastId ,null,new NewsInternetRequest.ForResultEventInfoListener() {
			@Override
			public void onResponseMessage(List<Map<String, String>> lists, String totalpage) {
				totlePage = Integer.parseInt(totalpage);
				if (totlePage > 0 && currentPage <= totlePage){
					if(currentPage == 1){
						if (mExpressAdapter == null) {
							mExpressAdapter = new NewExpressAdapter(getActivity(), lists);
						}

						mRecyclerView.setLinearLayout();
						mRecyclerView.setAdapter(mExpressAdapter);
						mRecyclerView.setFooterViewText("加载更多……");

						mLastId = mExpressAdapter.getLastId();//获取最后一条的新闻Id
					}else {
						mExpressAdapter.addDatas(lists);
						mRecyclerView.post(new Runnable() {
							@Override
							public void run() {
								mExpressAdapter.notifyDataSetChanged();
							}
						});
						mRecyclerView.postDelayed(new Runnable() {//防止滑动过快，loading界面显示太快
							@Override
							public void run() {
								mRecyclerView.setPullLoadMoreCompleted();
							}
						}, 1000);
						mLastId = mExpressAdapter.getLastId();
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
		},null);
	}
}