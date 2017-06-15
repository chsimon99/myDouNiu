package com.zfxf.douniu.view.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.ActivityStockInfo;
import com.zfxf.douniu.activity.ActivityStockList;
import com.zfxf.douniu.adapter.recycleView.MarketMarketAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.bean.SimulationResult;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.view.FullyLinearLayoutManager;
import com.zfxf.douniu.view.RecycleViewDivider;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * @author IMXU
 * @time   2017/5/3 13:42
 * @des    行情
 * 邮箱：butterfly_xu@sina.com
 *
*/

public class FragmentMarketMarket extends BaseFragment implements View.OnClickListener{
	private View view;

	@BindView(R.id.tv_market_market_sz_index)
	TextView sz_index;
	@BindView(R.id.tv_market_market_shz_index)
	TextView shz_index;
	@BindView(R.id.tv_market_market_cy_index)
	TextView cy_index;

	@BindView(R.id.tv_market_market_sz_price)
	TextView sz_price;
	@BindView(R.id.tv_market_market_sz_ratio)
	TextView sz_ratio;
	@BindView(R.id.tv_market_market_shz_price)
	TextView shz_price;
	@BindView(R.id.tv_market_market_shz_ratio)
	TextView shz_ratio;
	@BindView(R.id.tv_market_market_cy_price)
	TextView cy_price;
	@BindView(R.id.tv_market_market_cy_ratio)
	TextView cy_ratio;

	@BindView(R.id.rl_market_market_rise_more)
	RelativeLayout rl_rise;
	@BindView(R.id.rl_market_market_fall_more)
	RelativeLayout rl_fall;

	@BindView(R.id.rv_market_market_rise)
	RecyclerView mRiseRecyclerView;
	private LinearLayoutManager mRiseManager;
	private MarketMarketAdapter mRiseAdapter;

	@BindView(R.id.rv_market_market_fall)
	RecyclerView mFallRecyclerView;
	private LinearLayoutManager mFallManager;
	private MarketMarketAdapter mFallAdapter;

	private RecycleViewDivider mRiseDivider;
	private RecycleViewDivider mFallDivider;

	@Override
	public View initView(LayoutInflater inflater) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_market_market, null);
		}
		ViewGroup parent = (ViewGroup) view.getParent();
		if(parent !=null){
			parent.removeView(view);
		}
		ButterKnife.bind(this,view);
		sz_index.getPaint().setFakeBoldText(true);//加粗
		shz_index.getPaint().setFakeBoldText(true);//加粗
		cy_index.getPaint().setFakeBoldText(true);//加粗
		return view;
	}

	@Override
	public void init() {
		super.init();
	}
	@Override
	public void initdata() {
		super.initdata();
		mRiseAdapter = null;
		mFallAdapter = null;
		visitInternet();
	}

	private void visitInternet() {
		CommonUtils.showProgressDialog(getActivity(),"加载中……");
		NewsInternetRequest.getQuotationIndexInformation(new NewsInternetRequest.ForResultSimulationIndexListener() {
			@Override
			public void onResponseMessage(SimulationResult result) {
				if(result!=null){
					if(mRiseManager == null){
						mRiseManager = new FullyLinearLayoutManager(getActivity());
					}
					if(mRiseAdapter == null){
						mRiseAdapter = new MarketMarketAdapter(getActivity(), result.zhang_gupiao);
					}
					if(mFallManager == null){
						mFallManager = new FullyLinearLayoutManager(getActivity());
					}
					if(mFallAdapter == null){
						mFallAdapter = new MarketMarketAdapter(getActivity(), result.die_gupiao);
					}

					mRiseRecyclerView.setLayoutManager(mRiseManager);
					mRiseRecyclerView.setAdapter(mRiseAdapter);
					if(mRiseDivider == null){//防止多次加载出现宽度变宽
						mRiseDivider = new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL);
						mRiseRecyclerView.addItemDecoration(mRiseDivider);
					}

					mFallRecyclerView.setLayoutManager(mFallManager);
					mFallRecyclerView.setAdapter(mFallAdapter);
					if(mFallDivider == null){//防止多次加载出现宽度变宽
						mFallDivider = new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL);
						mFallRecyclerView.addItemDecoration(mFallDivider);
					}
					mRiseAdapter.setOnItemClickListener(new MarketMarketAdapter.MyItemClickListener() {
						@Override
						public void onItemClick(View v, int positon) {
							Intent intent = new Intent(getActivity(), ActivityStockInfo.class);
							startActivity(intent);
							getActivity().overridePendingTransition(0,0);
						}
					});
					mFallAdapter.setOnItemClickListener(new MarketMarketAdapter.MyItemClickListener() {
						@Override
						public void onItemClick(View v, int positon) {

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
		rl_fall.setOnClickListener(this);
		rl_rise.setOnClickListener(this);
	}
	Intent mIntent;
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.rl_market_market_rise_more:
				mIntent = new Intent(getActivity(), ActivityStockList.class);
				mIntent.putExtra("status",1);
				startActivity(mIntent);
				getActivity().overridePendingTransition(0,0);
				break;
			case R.id.rl_market_market_fall_more:
				mIntent = new Intent(getActivity(), ActivityStockList.class);
				mIntent.putExtra("status",0);
				startActivity(mIntent);
				getActivity().overridePendingTransition(0,0);
				break;
		}
	}
}