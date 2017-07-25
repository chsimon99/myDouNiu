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
import com.zfxf.douniu.adapter.recycleView.AdvisorAllReferenceAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.bean.LunBoListInfo;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.MyLunBo;
import com.zfxf.douniu.utils.SpTools;
import com.zfxf.douniu.view.RecycleViewLunboDivider;
import com.zfxf.douniu.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author IMXU
 * @time   2017/5/3 13:41
 * @des    首席 大参考
 * 邮箱：butterfly_xu@sina.com
 *
*/

public class FragmentAdvisorAllReference extends BaseFragment{
	private View view;

	private MyLunBo mMyLunBO;

	@BindView(R.id.rv_advisor_all_reference)
	PullLoadMoreRecyclerView mRecyclerView;
	private AdvisorAllReferenceAdapter mReferenceAdapter;
	private RecycleViewLunboDivider mDivider;
	private int totlePage = 0;
	private int currentPage = 1;
	private boolean isShow = false;

	@Override
	public View initView(LayoutInflater inflater) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_advisor_all_reference, null);
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
		if(!isShow){
			isShow = true;
			CommonUtils.showProgressDialog(getActivity(),"加载中……");
			visitInternet();
		}
	}

	private void visitInternet() {
		NewsInternetRequest.getListInformation(currentPage+"", null, null,new NewsInternetRequest.ForResultReferenceInfoListener() {
			@Override
			public void onResponseMessage(List<Map<String, String>> lists, String totalpage, List<LunBoListInfo> lunbo_list) {
				totlePage = Integer.parseInt(totalpage);
				if (totlePage > 0 && currentPage <= totlePage){
					if (currentPage == 1){
						if(mReferenceAdapter == null){
							mReferenceAdapter = new AdvisorAllReferenceAdapter(getActivity(),lists,lunbo_list);
							View view = View.inflate(getActivity(),R.layout.item_lunbo_with_gray,null);
							mReferenceAdapter.setHeaderView(view);
						}

						mRecyclerView.setLinearLayout();
						mRecyclerView.setAdapter(mReferenceAdapter);
						if(mDivider == null){
							mDivider = new RecycleViewLunboDivider(getActivity(), LinearLayoutManager.HORIZONTAL);
							mRecyclerView.addItemDecoration(mDivider);
						}
						mRecyclerView.setFooterViewText("加载更多……");
						mMyLunBO = mReferenceAdapter.getLunBo();

						mReferenceAdapter.setOnItemClickListener(new AdvisorAllReferenceAdapter.MyItemClickListener() {
							@Override
							public void onItemClick(View v, int id, Map<String, String> map) {
								if(map.get("has_buy").equals("0")){//是否购买返回来判断 0为未购买
									Intent intent = new Intent(CommonUtils.getContext(), ActivityToPay.class);
									intent.putExtra("info","大参考,"+map.get("cc_ub_id")+","+map.get("cc_id"));
									intent.putExtra("sx_id",map.get("cc_ub_id"));
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
					}else{
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
				}
				CommonUtils.dismissProgressDialog();
			}
		},getActivity().getResources().getString(R.string.cankaolist));
	}

	@Override
	public void initListener() {
		super.initListener();
		mRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
			@Override
			public void onRefresh() {
				currentPage = 1;
				mReferenceAdapter = null;
				CommonUtils.showProgressDialog(getActivity(),"加载中……");
				visitInternet();
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
		if(SpTools.getBoolean(getActivity(), Constants.buy,false)){//如果已经支付成功，重新刷新数据
			currentPage = 1;
			mReferenceAdapter = null;
			CommonUtils.showProgressDialog(getActivity(),"加载中……");
			SpTools.setBoolean(getActivity(), Constants.buy,false);
			visitInternet();
		}
		super.onResume();
	}
}