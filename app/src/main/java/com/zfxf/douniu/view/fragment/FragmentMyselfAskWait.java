package com.zfxf.douniu.view.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.adapter.recycleView.MyselfAskWaitAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.bean.NewsResult;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.view.RecycleViewDivider;
import com.zfxf.douniu.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FragmentMyselfAskWait extends BaseFragment {
	private View view;

	@BindView(R.id.tv_myself_ask_wait)
	TextView wait;

	@BindView(R.id.rv_myself_ask_wait)
	PullLoadMoreRecyclerView mRecyclerView;
	private MyselfAskWaitAdapter mAskWaitAdapter;
	private RecycleViewDivider mDivider;
	private int totlePage = 0;
	private int currentPage = 1;

	@Override
	public View initView(LayoutInflater inflater) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_myself_ask, null);
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
		visitInternet();
	}

	private void visitInternet() {
		CommonUtils.showProgressDialog(getActivity(),"加载中……");
		NewsInternetRequest.getMyAskDoneInformation(currentPage, 1, null, new NewsInternetRequest.ForResultMyAskWaitListener() {
			@Override
			public void onResponseMessage(NewsResult result) {
				if(result.no_answer.size() == 0 || result.total.equals("0")){
					wait.setVisibility(View.VISIBLE);
				}else {
					totlePage = Integer.parseInt(result.total);
					if (totlePage > 0 && currentPage <= totlePage){
						if (currentPage == 1){
							if(mAskWaitAdapter == null){
								mAskWaitAdapter = new MyselfAskWaitAdapter(getActivity(),result.no_answer);
							}
							mRecyclerView.setLinearLayout();
							mRecyclerView.setAdapter(mAskWaitAdapter);
							if(mDivider == null){
								mDivider = new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL);
								mRecyclerView.addItemDecoration(mDivider);
							}
							mRecyclerView.setPullRefreshEnable(false);

							mAskWaitAdapter.setOnItemClickListener(new MyselfAskWaitAdapter.MyItemClickListener() {
								@Override
								public void onItemClick(View v, int positon) {
//									Toast.makeText(CommonUtils.getContext(),"点击了"+positon,Toast.LENGTH_SHORT).show();
								}
							});

						}else {
							mAskWaitAdapter.addDatas(result.no_answer);
							mRecyclerView.post(new Runnable() {
								@Override
								public void run() {
									mAskWaitAdapter.notifyDataSetChanged();
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
					}else {

					}
				}
				CommonUtils.dismissProgressDialog();
			}
		});
	}

	@Override
	public void initListener() {
		super.initListener();
	}
}