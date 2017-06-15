package com.zfxf.douniu.view.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.ActivityAnswerDetail;
import com.zfxf.douniu.activity.ActivityToPay;
import com.zfxf.douniu.activity.login.ActivityLogin;
import com.zfxf.douniu.adapter.recycleView.AdvisorHomeAskingAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.bean.AnswerListInfo;
import com.zfxf.douniu.bean.IndexResult;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.SpTools;
import com.zfxf.douniu.view.RecycleViewDivider;
import com.zfxf.douniu.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * @author IMXU
 * @time   2017/5/3 13:13
 * @des    首席个人主页微问答
 * 邮箱：butterfly_xu@sina.com
 *
*/

public class FragmentAdvisorHomeAsking extends BaseFragment {
	private View view;

	@BindView(R.id.rv_advisor_home_asking)
	PullLoadMoreRecyclerView mRecyclerView;
	private AdvisorHomeAskingAdapter mAskingAdapter;
	private RecycleViewDivider mDivider;
	private int totlePage = 0;
	private int currentPage = 1;
	private int mId;
	private boolean isShow = false;
	@Override
	public View initView(LayoutInflater inflater) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_advisor_home_asking, null);
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
		if (!isShow){
			isShow = true;
			CommonUtils.showProgressDialog(getActivity(),"加载中……");
			visitInternet();
		}
	}

	private void visitInternet() {
		NewsInternetRequest.getAnswerLIstInformation(currentPage, mId+"", new NewsInternetRequest.ForResultAnswerIndexListener() {
			@Override
			public void onResponseMessage(IndexResult result) {
				totlePage = Integer.parseInt(result.total);
				if (totlePage > 0 && currentPage <= totlePage){
					if(currentPage == 1){
						if(mAskingAdapter == null){
							mAskingAdapter = new AdvisorHomeAskingAdapter(getActivity(),result.bright_answer);
						}

						mRecyclerView.setLinearLayout();
						mRecyclerView.setAdapter(mAskingAdapter);
						if(mDivider == null){
							mDivider = new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL);
							mRecyclerView.addItemDecoration(mDivider);
						}
						mRecyclerView.setPullRefreshEnable(false);
						//		mRecyclerView.setPushRefreshEnable(false);
						mAskingAdapter.setOnItemClickListener(new AdvisorHomeAskingAdapter.MyItemClickListener() {
							@Override
							public void onItemClick(View v, int positon,AnswerListInfo bean) {
								if(!SpTools.getBoolean(CommonUtils.getContext(), Constants.isLogin,false)){
									Intent intent = new Intent(getActivity(), ActivityLogin.class);
									getActivity().startActivity(intent);
									getActivity().overridePendingTransition(0,0);
									return;
								}
								if (bean.zc_sfjf.equals("0")){
									Intent intent = new Intent(CommonUtils.getContext(), ActivityToPay.class);
									intent.putExtra("type","问股");
									intent.putExtra("count",bean.zc_fee);
									intent.putExtra("from",bean.ud_nickname);
									startActivity(intent);
									getActivity().overridePendingTransition(0,0);
								}else{
									Intent intent = new Intent(CommonUtils.getContext(), ActivityAnswerDetail.class);
									intent.putExtra("id",bean.zc_id);
									startActivity(intent);
									getActivity().overridePendingTransition(0,0);
								}
							}
						});
					}else {
						mAskingAdapter.addDatas(result.bright_answer);
						mRecyclerView.post(new Runnable() {
							@Override
							public void run() {
								mAskingAdapter.notifyDataSetChanged();
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
				}
				CommonUtils.dismissProgressDialog();

			}
		});
	}

	@Override
	public void initListener() {
		super.initListener();
		mRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
			@Override
			public void onRefresh() {

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
		super.onResume();
		if(SpTools.getBoolean(getActivity(), Constants.buy,false)){
			currentPage = 1;
			mAskingAdapter = null;
			CommonUtils.showProgressDialog(getActivity(),"加载中……");
			visitInternet();
			SpTools.setBoolean(getActivity(), Constants.buy,false);
		}
		if(SpTools.getBoolean(getActivity(), Constants.read,false)){
			currentPage = 1;
			mAskingAdapter = null;
			CommonUtils.showProgressDialog(getActivity(),"加载中……");
			visitInternet();
			SpTools.setBoolean(getActivity(), Constants.read,false);
		}
		if(SpTools.getBoolean(getActivity(), Constants.alreadyLogin,false)){
			currentPage = 1;
			mAskingAdapter = null;
			CommonUtils.showProgressDialog(getActivity(),"加载中……");
			visitInternet();
			SpTools.setBoolean(getActivity(), Constants.alreadyLogin,false);
		}
	}
}