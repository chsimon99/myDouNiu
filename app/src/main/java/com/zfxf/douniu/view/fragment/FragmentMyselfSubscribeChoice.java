package com.zfxf.douniu.view.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.ActivityIntelligenceChoose;
import com.zfxf.douniu.adapter.recycleView.MyselfSubscribeChoiceAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.bean.XuanguResult;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.view.RecycleViewDivider;
import com.zfxf.douniu.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FragmentMyselfSubscribeChoice extends BaseFragment {
	private View view;

	@BindView(R.id.tv_myself_subscribe_choice)
	TextView wait;

	@BindView(R.id.rv_myself_subscribe_choice)
	PullLoadMoreRecyclerView mRecyclerView;
	private MyselfSubscribeChoiceAdapter mSubscribeChoiceAdapter;
	private RecycleViewDivider mDivider;

	@Override
	public View initView(LayoutInflater inflater) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_myself_subscribe_choice, null);
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
		NewsInternetRequest.getMyXuanGuListInformation(1, new NewsInternetRequest.ForResultXuanGuListener() {
			@Override
			public void onResponseMessage(XuanguResult result) {
				if(result.news_list.size()==0){
					wait.setVisibility(View.VISIBLE);
				}else {
					if(mSubscribeChoiceAdapter == null){
						mSubscribeChoiceAdapter = new MyselfSubscribeChoiceAdapter(getActivity(),result.news_list);
					}

					mRecyclerView.setLinearLayout();
					mRecyclerView.setAdapter(mSubscribeChoiceAdapter);
					if(mDivider == null){
						mDivider = new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL,
								CommonUtils.px2dip(getActivity(), 20), Color.parseColor("#f4f4f4"));
						mRecyclerView.addItemDecoration(mDivider);
					}
					mRecyclerView.setPullRefreshEnable(false);
					mRecyclerView.setPushRefreshEnable(false);

					mSubscribeChoiceAdapter.setOnItemClickListener(new MyselfSubscribeChoiceAdapter.MyItemClickListener() {
						@Override
						public void onItemClick(View v, int id) {
							Intent intent = new Intent(getActivity(), ActivityIntelligenceChoose.class);
							intent.putExtra("id",id);
							getActivity().startActivity(intent);
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