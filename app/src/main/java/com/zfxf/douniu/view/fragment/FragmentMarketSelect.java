package com.zfxf.douniu.view.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.ActivityMarketResearch;
import com.zfxf.douniu.activity.ActivityMySelfStock;
import com.zfxf.douniu.activity.ActivityStockInfo;
import com.zfxf.douniu.activity.login.ActivityLogin;
import com.zfxf.douniu.adapter.recycleView.MarketSelectAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.bean.SimulationInfo;
import com.zfxf.douniu.bean.SimulationResult;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.SpTools;
import com.zfxf.douniu.view.RecycleViewDivider;
import com.zfxf.douniu.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author IMXU
 * @time   2017/5/3 13:42
 * @des    自选股
 * 邮箱：butterfly_xu@sina.com
 *
*/

public class FragmentMarketSelect extends BaseFragment implements View.OnClickListener{
	private View view;

	@BindView(R.id.ll_market_select_edit)
	LinearLayout ll_edit;
	@BindView(R.id.ll_market_select_new)
	LinearLayout ll_new;
	@BindView(R.id.ll_market_select_ratio)
	LinearLayout ll_ratio;
	@BindView(R.id.ll_market_select_none)
	LinearLayout ll_none;

	@BindView(R.id.tv_market_select_edit)
	TextView tv_edit;
	@BindView(R.id.iv_market_select_edit)
	ImageView iv_edit;
	@BindView(R.id.iv_market_select_new)
	ImageView iv_new;
	@BindView(R.id.iv_market_select_ratio)
	ImageView iv_ratio;

	@BindView(R.id.rv_market_select)
	PullLoadMoreRecyclerView mRecyclerView;
	private MarketSelectAdapter mSelectAdapter;
	private RecycleViewDivider mDivider;
	int type = 0;
	private int totlePage = 0;
	private int currentPage = 1;
	@Override
	public View initView(LayoutInflater inflater) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_market_select, null);
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
//		CommonUtils.showProgressDialog(getActivity(),"加载中……");
		NewsInternetRequest.getZiXuanStockInformation(currentPage, type, new NewsInternetRequest.ForResultZiXuanStockListener() {
			@Override
			public void onResponseMessage(SimulationResult result) {
				if(result.result.code.equals("01")){
					ll_none.setVisibility(View.VISIBLE);
					return;
				}
				if(result.mn_zixuan.size() == 0){
					ll_none.setVisibility(View.VISIBLE);
				}
				totlePage = Integer.parseInt(result.total);
				if (totlePage > 0 && currentPage <= totlePage){
					ll_none.setVisibility(View.GONE);
					if(currentPage == 1){
						if(mSelectAdapter == null){
							List<SimulationInfo> mn_zixuan = result.mn_zixuan;
							setList(mn_zixuan);
							mSelectAdapter = new MarketSelectAdapter(getActivity(), result.mn_zixuan);
						}
						mRecyclerView.setLinearLayout();
						mRecyclerView.setAdapter(mSelectAdapter);
						if(mDivider == null){//防止多次加载出现宽度变宽
							mDivider = new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL);
							mRecyclerView.addItemDecoration(mDivider);
						}
						mRecyclerView.setPullRefreshEnable(false);//上拉刷新
						mRecyclerView.setFooterViewText("加载更多……");
						mSelectAdapter.setOnItemClickListener(new MarketSelectAdapter.MyItemClickListener() {
							@Override
							public void onItemClick(View v, String code,String name) {
								Intent intent = new Intent(getActivity(), ActivityStockInfo.class);
								intent.putExtra("code",code);
								intent.putExtra("name",name);
								startActivity(intent);
								getActivity().overridePendingTransition(0,0);
							}
						});
					}else {
						mSelectAdapter.addDatas(result.mn_zixuan);
						mRecyclerView.post(new Runnable() {//避免出现adapter异常
							@Override
							public void run() {
								mSelectAdapter.notifyDataSetChanged();
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
				}else {//没有自选股时候，在编辑界面删除所有后回来刷新界面
					if(mSelectAdapter == null){
						mSelectAdapter = new MarketSelectAdapter(getActivity(), result.mn_zixuan);
					}
					mRecyclerView.setLinearLayout();
					mRecyclerView.setAdapter(mSelectAdapter);
					mSelectAdapter.notifyDataSetChanged();
				}

//				CommonUtils.dismissProgressDialog();
			}

		});
	}
	private static List<SimulationInfo> list;
	public void setList(List<SimulationInfo> infoList){
		list = infoList;
	}
	public static List<SimulationInfo> getList(){
		return list;
	}
	@Override
	public void initListener() {
		super.initListener();
		ll_new.setOnClickListener(this);
		ll_ratio.setOnClickListener(this);
		ll_edit.setOnClickListener(this);
		ll_none.setOnClickListener(this);
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
	boolean isNew = false;
	boolean isRatio = false;
	Intent intent;
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.ll_market_select_new:
				reset();
				isRatio = false;
				iv_ratio.setImageResource(R.drawable.icon_up_down);
				if(isNew){
					isNew = false;
					type = 1;
					iv_new.setImageResource(R.drawable.icon_down_down);
				}else {
					isNew = true;
					type = 2;
					iv_new.setImageResource(R.drawable.icon_up_up);
				}
				visitInternet();
				break;
			case R.id.ll_market_select_ratio:
				reset();
				isNew = false;
				iv_new.setImageResource(R.drawable.icon_up_down);
				if(isRatio){
					isRatio = false;
					type = 3;
					iv_ratio.setImageResource(R.drawable.icon_down_down);
				}else {
					isRatio = true;
					type = 4;
					iv_ratio.setImageResource(R.drawable.icon_up_up);
				}
				visitInternet();
				break;
			case R.id.ll_market_select_edit:
				if(!SpTools.getBoolean(CommonUtils.getContext(), Constants.isLogin,false)){
					Intent intent = new Intent(getActivity(), ActivityLogin.class);
					startActivity(intent);
					getActivity().overridePendingTransition(0,0);
					return;
				}
				intent = new Intent(getActivity(), ActivityMySelfStock.class);
				startActivity(intent);
				getActivity().overridePendingTransition(0,0);
				break;
			case R.id.ll_market_select_none:
				if(!SpTools.getBoolean(CommonUtils.getContext(), Constants.isLogin,false)){
					Intent intent = new Intent(getActivity(), ActivityLogin.class);
					startActivity(intent);
					getActivity().overridePendingTransition(0,0);
					return;
				}
				intent = new Intent(getActivity(), ActivityMarketResearch.class);
				startActivity(intent);
				getActivity().overridePendingTransition(0,0);
				break;
		}
	}

	private void reset() {
		mSelectAdapter = null;
		currentPage =1;
	}

	@Override
	public void onResume() {
		super.onResume();
		if(SpTools.getBoolean(getActivity(), Constants.buy,false)){//删除股票后
			currentPage = 1;
			mSelectAdapter = null;
			visitInternet();
			SpTools.setBoolean(getActivity(), Constants.buy,false);
		}
		if(SpTools.getBoolean(getActivity(), Constants.alreadyLogout,false)){//退出登陆后
			currentPage = 1;
			mSelectAdapter = null;
			visitInternet();
			SpTools.setBoolean(getActivity(), Constants.alreadyLogout,false);
		}
	}
}