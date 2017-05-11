package com.zfxf.douniu.view.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.adapter.recycleView.MarketSelectAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.view.FullyLinearLayoutManager;
import com.zfxf.douniu.view.RecycleViewDivider;

import java.util.ArrayList;
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

	@BindView(R.id.rv_market_select)
	RecyclerView mRecyclerView;
	private LinearLayoutManager mLayoutManager;
	private MarketSelectAdapter mSelectAdapter;
	private List<String> mStrings = new ArrayList<String>();
	private RecycleViewDivider mDivider;
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
		if(mStrings.size() == 0){
			mStrings.add("");
			mStrings.add("");
			mStrings.add("");
			mStrings.add("");
			mStrings.add("");
			mStrings.add("");
			mStrings.add("");
			mStrings.add("");
			mStrings.add("");
			mStrings.add("");
			mStrings.add("");
			mStrings.add("");
			mStrings.add("");
			mStrings.add("");
			mStrings.add("");
			mStrings.add("");
			mStrings.add("");
			mStrings.add("");
			mStrings.add("");
			mStrings.add("");
		}
		if(mLayoutManager == null){
			mLayoutManager = new FullyLinearLayoutManager(getActivity());
		}
		if(mSelectAdapter == null){
			mSelectAdapter = new MarketSelectAdapter(getActivity(), mStrings);
		}
		mRecyclerView.setLayoutManager(mLayoutManager);
		mRecyclerView.setAdapter(mSelectAdapter);
		if(mDivider == null){//防止多次加载出现宽度变宽
			mDivider = new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL);
			mRecyclerView.addItemDecoration(mDivider);
		}
		if(mStrings.size() >0){
			ll_none.setVisibility(View.GONE);
		}
	}

	@Override
	public void initListener() {
		super.initListener();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()){

		}
	}
}