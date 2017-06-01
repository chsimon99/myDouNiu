package com.zfxf.douniu.view.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.ActivityInformation;
import com.zfxf.douniu.activity.ActivityToPay;
import com.zfxf.douniu.adapter.recycleView.AdvisorHomeReferenceAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.bean.LunBoListInfo;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.SpTools;
import com.zfxf.douniu.view.RecycleViewDivider;
import com.zfxf.douniu.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * @author IMXU
 * @time   2017/5/3 13:13
 * @des    首席个人主页大参考
 * 邮箱：butterfly_xu@sina.com
 *
*/

public class FragmentAdvisorHomeReference extends BaseFragment {
	private View view;

	@BindView(R.id.rv_advisor_home_capital)
	PullLoadMoreRecyclerView mRecyclerView;
	private AdvisorHomeReferenceAdapter mReferenceAdapter;
	private RecycleViewDivider mDivider;

	private int totlePage = 0;
	private int currentPage = 1;
	private int mId;

	@Override
	public View initView(LayoutInflater inflater) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_advisor_home_capital, null);
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
		currentPage = 1;
		mReferenceAdapter = null;
		CommonUtils.showProgressDialog(getActivity(),"加载中……");
		visitInternet();

//		mRecyclerView.setPushRefreshEnable(false);
	}

	private void visitInternet() {
		NewsInternetRequest.getListInformation(currentPage + "", mId + "", new NewsInternetRequest.ForResultPolicyInfoListener() {
			@Override
			public void onResponseMessage(List<Map<String, String>> lists, String totalpage, List<LunBoListInfo> lunbo_list) {
				totlePage = Integer.parseInt(totalpage);
				if (totlePage > 0 && currentPage <= totlePage){
					if(currentPage == 1){
						if(mReferenceAdapter == null){
							mReferenceAdapter = new AdvisorHomeReferenceAdapter(getActivity(),lists);
						}

						mRecyclerView.setLinearLayout();
						mRecyclerView.setAdapter(mReferenceAdapter);
						if(mDivider == null){
							mDivider = new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL);
							mRecyclerView.addItemDecoration(mDivider);
						}
						mRecyclerView.setPullRefreshEnable(false);

						mReferenceAdapter.setOnItemClickListener(new AdvisorHomeReferenceAdapter.MyItemClickListener() {
							@Override
							public void onItemClick(View v, int id,Map<String, String> map) {
								if(map.get("has_buy").equals("0")){//是否购买返回来判断 0为未购买
									Intent intent = new Intent(CommonUtils.getContext(), ActivityToPay.class);
									intent.putExtra("type","大参考");
									intent.putExtra("count",map.get("cc_fee"));
									intent.putExtra("from",map.get("cc_from"));
									startActivity(intent);
									getActivity().overridePendingTransition(0,0);
								}else{
									Intent intent = new Intent(CommonUtils.getContext(), ActivityInformation.class);
									intent.putExtra("type","参考详情");
									intent.putExtra("newsinfoId",id);
									startActivity(intent);
									getActivity().overridePendingTransition(0,0);
								}
							}
						});
					}else {
						mReferenceAdapter.addDatas(lists);
						mRecyclerView.post(new Runnable() {
							@Override
							public void run() {
								mReferenceAdapter.notifyDataSetChanged();
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
		},getActivity().getResources().getString(R.string.cankaolist));
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

	@Override
	public void onResume() {
		super.onResume();
		if(SpTools.getBoolean(getActivity(), Constants.buy,false)){//如果已经支付成功，重新刷新数据
			currentPage = 1;
			mReferenceAdapter = null;
			CommonUtils.showProgressDialog(getActivity(),"加载中……");
			SpTools.setBoolean(getActivity(), Constants.buy,false);
			visitInternet();
		}
	}
}