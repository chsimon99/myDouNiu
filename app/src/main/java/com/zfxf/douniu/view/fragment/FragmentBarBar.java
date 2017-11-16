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
import com.zfxf.douniu.activity.bar.ActivityBarBarDetail;
import com.zfxf.douniu.adapter.recycleView.BarBarAdapter;
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
 * @time   2017/5/3 13:26
 * @des    斗牛吧
 * 邮箱：butterfly_xu@sina.com
 *
*/

public class FragmentBarBar extends BaseFragment implements View.OnClickListener{
	private View view;
	@BindView(R.id.rv_bar_bar)
	PullLoadMoreRecyclerView mRecyclerView;
	private BarBarAdapter mBarAdapter;
	private RecycleViewDivider mDivider;
	@BindView(R.id.ll_bar_bar_hot)
	LinearLayout ll_hot;
	@BindView(R.id.ll_bar_bar_stock)
	LinearLayout ll_stock;
	@BindView(R.id.ll_bar_bar_other)
	LinearLayout ll_other;

	@BindView(R.id.tv_bar_bar_hot)
	TextView tv_hot;
	@BindView(R.id.tv_bar_bar_stock)
	TextView tv_stock;
	@BindView(R.id.tv_bar_bar_other)
	TextView tv_other;

	private int headType = 0;
	private int totlePage = 0;
	private int currentPage = 1;
	@Override
	public View initView(LayoutInflater inflater) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_bar_bar, null);
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

	private void visitInternet() {
		NewsInternetRequest.getBarListInformation(headType, currentPage+"", new NewsInternetRequest.ForResultPolicyInfoListener() {
			@Override
			public void onResponseMessage(List<Map<String, String>> lists, String totalpage, List<LunBoListInfo> lunbo_list) {
				totlePage = Integer.parseInt(totalpage);
				if(totlePage > 0 && currentPage <= totlePage){
					if(currentPage == 1){
						if(mBarAdapter == null){
							mBarAdapter = new BarBarAdapter(getActivity(),lists);
						}

						mRecyclerView.setLinearLayout();
						mRecyclerView.setAdapter(mBarAdapter);
						if(mDivider == null){//防止多次加载出现宽度变宽
							mDivider = new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL);
							mRecyclerView.addItemDecoration(mDivider);
						}
						mRecyclerView.setFooterViewText("加载更多……");
						mRecyclerView.setPullRefreshEnable(false);//禁止上拉刷新

						mBarAdapter.setOnItemClickListener(new BarBarAdapter.MyItemClickListener() {
							@Override
							public void onItemClick(View v, int id) {
								Intent intent = new Intent(CommonUtils.getContext(), ActivityBarBarDetail.class);
								intent.putExtra("newsinfoId",id);
								startActivity(intent);
								getActivity().overridePendingTransition(0, 0);
							}
						});
					}else {
						mBarAdapter.addDatas(lists);
						mRecyclerView.post(new Runnable() {
							@Override
							public void run() {
								mBarAdapter.notifyDataSetChanged();
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
		});
	}

	@Override
	public void initListener() {
		super.initListener();
		ll_hot.setOnClickListener(this);
		ll_stock.setOnClickListener(this);
		ll_other.setOnClickListener(this);
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
	public void onClick(View v) {
		reset();
		switch (v.getId()){
			case R.id.ll_bar_bar_hot:
				headType = 0;
				tv_hot.setTextColor(getResources().getColor(R.color.colorTitle));
				break;
			case R.id.ll_bar_bar_stock:
				headType = 1;
				tv_stock.setTextColor(getResources().getColor(R.color.colorPhone));
				break;
			case R.id.ll_bar_bar_other:
				headType = 2;
				tv_other.setTextColor(getResources().getColor(R.color.advisorType));
				break;
		}
		currentPage = 1;
		mBarAdapter = null;
		visitInternet();
	}
	private void reset() {
		tv_hot.setTextColor(getResources().getColor(R.color.colorText));
		tv_stock.setTextColor(getResources().getColor(R.color.colorText));
		tv_other.setTextColor(getResources().getColor(R.color.colorText));
	}
}