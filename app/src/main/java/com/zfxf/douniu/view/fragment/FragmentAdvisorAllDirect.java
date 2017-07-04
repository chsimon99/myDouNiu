package com.zfxf.douniu.view.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.ActivityLiving;
import com.zfxf.douniu.adapter.recycleView.AdvisorAllDirectAdapter;
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
 * @time   2017/5/3 13:34
 * @des    首席 直播课
 * 邮箱：butterfly_xu@sina.com
 *
*/

public class FragmentAdvisorAllDirect extends BaseFragment implements View.OnClickListener{
	private View view;

	@BindView(R.id.rv_advisor_all_direct)
	PullLoadMoreRecyclerView mRecyclerView;
	private AdvisorAllDirectAdapter mAllDirectAdapter;
	private RecycleViewDivider mDivider;

	@BindView(R.id.ll_advisor_all_direct_hudong)
	LinearLayout hudong;
	@BindView(R.id.ll_advisor_all_direct_renqi)
	LinearLayout renqi;
	@BindView(R.id.ll_advisor_all_direct_guandian)
	LinearLayout guandian;
	@BindView(R.id.tv_advisor_all_direct_hudong)
	TextView tv_hudong;
	@BindView(R.id.tv_advisor_all_direct_renqi)
	TextView tv_renqi;
	@BindView(R.id.tv_advisor_all_direct_guandian)
	TextView tv_guandian;

	private int headType = 0;
	private int totlePage = 0;
	private int currentPage = 1;
	private boolean isShow = false;

	@Override
	public View initView(LayoutInflater inflater) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_advisor_all_direct, null);
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
	private void visitInternet(){
		NewsInternetRequest.getLivingListInformation(headType, currentPage+"", null, new NewsInternetRequest.ForResultEventInfoListener() {
			@Override
			public void onResponseMessage(List<Map<String, String>> lists, String totalpage, List<LunBoListInfo> lunbo_list) {
				totlePage = Integer.parseInt(totalpage);
				if (totlePage > 0 && currentPage <= totlePage){
					if(currentPage == 1){
						if(mAllDirectAdapter == null){
							mAllDirectAdapter = new AdvisorAllDirectAdapter(getActivity(),lists);
						}

						mRecyclerView.setLinearLayout();
						mRecyclerView.setAdapter(mAllDirectAdapter);
						if(mDivider == null){
							mDivider = new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL);
							mRecyclerView.addItemDecoration(mDivider);
						}
						mRecyclerView.setFooterViewText("加载更多……");

						mAllDirectAdapter.setOnItemClickListener(new AdvisorAllDirectAdapter.MyItemClickListener() {
							@Override
							public void onItemClick(View v, int id,String sx_id) {
								Intent intent = new Intent(CommonUtils.getContext(), ActivityLiving.class);
								intent.putExtra("id",id);
								intent.putExtra("sx_id",sx_id);
								startActivity(intent);
								getActivity().overridePendingTransition(0,0);
							}
						});
					}else {
						if(lists !=null){
							mAllDirectAdapter.addDatas(lists);
						}
						mRecyclerView.post(new Runnable() {
							@Override
							public void run() {
								mAllDirectAdapter.notifyDataSetChanged();
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
		},getActivity().getResources().getString(R.string.zhibolist));

	}
	@Override
	public void initListener() {
		super.initListener();
		hudong.setOnClickListener(this);
		renqi.setOnClickListener(this);
		guandian.setOnClickListener(this);
		mRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
			@Override
			public void onRefresh() {
				currentPage = 1;
				mAllDirectAdapter = null;
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

	@Override
	public void onClick(View v) {
		reset();
		switch (v.getId()){
			case R.id.ll_advisor_all_direct_hudong:
				headType = 0;
				tv_hudong.setTextColor(getResources().getColor(R.color.colorTitle));
				break;
			case R.id.ll_advisor_all_direct_renqi:
				headType = 1;
				tv_renqi.setTextColor(getResources().getColor(R.color.colorPhone));
				break;
			case R.id.ll_advisor_all_direct_guandian:
				headType = 2;
				tv_guandian.setTextColor(getResources().getColor(R.color.advisorType));
				break;
		}
		currentPage = 1;
		mAllDirectAdapter = null;
		visitInternet();
	}

	private void reset() {
		tv_hudong.setTextColor(getResources().getColor(R.color.colorText));
		tv_renqi.setTextColor(getResources().getColor(R.color.colorText));
		tv_guandian.setTextColor(getResources().getColor(R.color.colorText));
	}
}