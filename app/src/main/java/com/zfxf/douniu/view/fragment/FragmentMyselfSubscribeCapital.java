package com.zfxf.douniu.view.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.news.ActivityInformation;
import com.zfxf.douniu.adapter.recycleView.MyselfSubscribeCapitalAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.bean.MyContentResult;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.view.RecycleViewDivider;
import com.zfxf.douniu.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FragmentMyselfSubscribeCapital extends BaseFragment {
	private View view;

	@BindView(R.id.tv_myself_subscribe_capital)
	TextView wait;

	@BindView(R.id.rv_myself_subscribe_capital)
	PullLoadMoreRecyclerView mRecyclerView;
	private MyselfSubscribeCapitalAdapter mSubscribeCapitalAdapter;
	private RecycleViewDivider mDivider;

	@Override
	public View initView(LayoutInflater inflater) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_myself_subscribe_capital, null);
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
		NewsInternetRequest.getMySubscribeListInfromation(2, null, null, new NewsInternetRequest.ForResultMyReferenceListener() {
			@Override
			public void onResponseMessage(MyContentResult result) {
				if(result.news_list.size()==0){
					wait.setVisibility(View.VISIBLE);
				}else{
					if(mSubscribeCapitalAdapter == null){
						mSubscribeCapitalAdapter = new MyselfSubscribeCapitalAdapter(getActivity(),result.news_list);
					}

					mRecyclerView.setLinearLayout();
					mRecyclerView.setAdapter(mSubscribeCapitalAdapter);
					if(mDivider == null){
						mDivider = new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL);
						mRecyclerView.addItemDecoration(mDivider);
					}
					mRecyclerView.setPullRefreshEnable(false);
					mRecyclerView.setPushRefreshEnable(false);

					mSubscribeCapitalAdapter.setOnItemClickListener(new MyselfSubscribeCapitalAdapter.MyItemClickListener() {
						@Override
						public void onItemClick(View v, int id) {
							Intent intent = new Intent(CommonUtils.getContext(), ActivityInformation.class);
							intent.putExtra("type","参考详情");
							intent.putExtra("newsinfoId",id);
							startActivity(intent);
							getActivity().overridePendingTransition(0,0);
						}
					});
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