package com.zfxf.douniu.view.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.advisor.ActivityAdvisorAllPublicDetail;
import com.zfxf.douniu.adapter.recycleView.MyselfSubscribePublicAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.bean.MyContentResult;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.SpTools;
import com.zfxf.douniu.view.RecycleViewDivider;
import com.zfxf.douniu.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FragmentMyselfSubscribePublic extends BaseFragment {
	private View view;

	@BindView(R.id.tv_myself_subscribe_public)
	TextView wait;

	@BindView(R.id.rv_myself_subscribe_public)
	PullLoadMoreRecyclerView mRecyclerView;
	private MyselfSubscribePublicAdapter mSubscribePublicAdapter;
	private RecycleViewDivider mDivider;

	@Override
	public View initView(LayoutInflater inflater) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_myself_subscribe_public, null);
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
		NewsInternetRequest.getMySubscribeListInfromation(0, new NewsInternetRequest.ForResultMyPublicListener() {
			@Override
			public void onResponseMessage(MyContentResult result) {
				if (result.news_list.size() == 0) {
					wait.setVisibility(View.VISIBLE);
				}
				if (mSubscribePublicAdapter == null) {
					mSubscribePublicAdapter = new MyselfSubscribePublicAdapter(getActivity(), result.news_list);
				}

				mRecyclerView.setLinearLayout();
				mRecyclerView.setAdapter(mSubscribePublicAdapter);
				if (mDivider == null) {
					mDivider = new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL);
					mRecyclerView.addItemDecoration(mDivider);
				}
				mRecyclerView.setPullRefreshEnable(false);
				mRecyclerView.setPushRefreshEnable(false);

				mSubscribePublicAdapter.setOnItemClickListener(new MyselfSubscribePublicAdapter.MyItemClickListener() {
					@Override
					public void onItemClick(View v, int id) {
						Intent intent = new Intent(CommonUtils.getContext(), ActivityAdvisorAllPublicDetail.class);
						intent.putExtra("id", id);
						startActivity(intent);
						getActivity().overridePendingTransition(0, 0);
					}
				});
				CommonUtils.dismissProgressDialog();
			}
		},null,null);
	}

	@Override
	public void initListener() {
		super.initListener();
	}

	@Override
	public void onResume() {
		super.onResume();
		if(SpTools.getBoolean(getActivity(), Constants.publicsubscribe,false)){//如果已经支付成功，重新刷新数据
			mSubscribePublicAdapter = null;
			visitInternet();
			SpTools.setBoolean(getActivity(), Constants.publicsubscribe,false);
		}
	}
}