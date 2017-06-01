package com.zfxf.douniu.view.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.advisor.ActivityAdvisorAllSecretDetail;
import com.zfxf.douniu.adapter.recycleView.MyselfSubscribeSecretAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.bean.MyContentResult;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.view.RecycleViewDivider;
import com.zfxf.douniu.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FragmentMyselfSubscribeSecret extends BaseFragment {
	private View view;

	@BindView(R.id.tv_myself_subscribe_secret)
	TextView wait;

	@BindView(R.id.rv_myself_subscribe_secret)
	PullLoadMoreRecyclerView mRecyclerView;
	private MyselfSubscribeSecretAdapter mSubscribeSecretAdapter;
	private RecycleViewDivider mDivider;

	@Override
	public View initView(LayoutInflater inflater) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_myself_subscribe_secret, null);
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
		NewsInternetRequest.getMySubscribeListInfromation(1, null, new NewsInternetRequest.ForResultMySecretListener() {
			@Override
			public void onResponseMessage(MyContentResult result) {
				if(result.news_list.size()==0){
					wait.setVisibility(View.VISIBLE);
				}else{
					if(mSubscribeSecretAdapter == null){
						mSubscribeSecretAdapter = new MyselfSubscribeSecretAdapter(getActivity(),result.news_list);
					}

					mRecyclerView.setLinearLayout();
					mRecyclerView.setAdapter(mSubscribeSecretAdapter);
					if(mDivider == null){
						mDivider = new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL);
						mRecyclerView.addItemDecoration(mDivider);
					}
					mRecyclerView.setPullRefreshEnable(false);
					mRecyclerView.setPushRefreshEnable(false);

					mSubscribeSecretAdapter.setOnItemClickListener(new MyselfSubscribeSecretAdapter.MyItemClickListener() {
						@Override
						public void onItemClick(View v, int id) {
							Intent intent = new Intent(CommonUtils.getContext(), ActivityAdvisorAllSecretDetail.class);
							intent.putExtra("id",id);
							startActivity(intent);
							getActivity().overridePendingTransition(0,0);
						}
					});
				}
				CommonUtils.dismissProgressDialog();
			}
		},null);
	}

	@Override
	public void initListener() {
		super.initListener();
	}
}