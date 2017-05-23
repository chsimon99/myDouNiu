package com.zfxf.douniu.view.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.ActivityMatador;
import com.zfxf.douniu.adapter.recycleView.HomeAdvisorAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.view.FullyLinearLayoutManager;
import com.zfxf.douniu.view.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * @author IMXU
 * @time   2017/5/3 13:27
 * @des    斗牛士评级
 * 邮箱：butterfly_xu@sina.com
 *
*/

public class FragmentBarGrade extends BaseFragment {
	private View view;
	@BindView(R.id.rv_bar_grade_commend)
	RecyclerView mCommendRecyclerView;//斗牛士推荐recycleview
	private LinearLayoutManager mCommendManager;
	private HomeAdvisorAdapter mCommendAdapter;
	private List<String> commendDatas = new ArrayList<String>();

	@BindView(R.id.rv_bar_grade_human)
	RecyclerView mHumanRecyclerView;//人气最高recycleview
	private LinearLayoutManager mHumanManager;
	private HomeAdvisorAdapter mHumanAdapter;
	private List<String> humanDatas = new ArrayList<String>();

	@BindView(R.id.rv_bar_grade_income)
	RecyclerView mIncomeRecyclerView;//收益最好recycleview
	private LinearLayoutManager mIncomeManager;
	private HomeAdvisorAdapter mIncomeAdapter;
	private List<String> incomeDatas = new ArrayList<String>();

	@BindView(R.id.tv_bar_grade_commend)
	TextView commend;
	@BindView(R.id.tv_bar_grade_income)
	TextView income;
	@BindView(R.id.tv_bar_grade_human)
	TextView human;
	private RecycleViewDivider mDivider;

	@Override
	public View initView(LayoutInflater inflater) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_bar_grade, null);
		}
		ViewGroup parent = (ViewGroup) view.getParent();
		if(parent !=null){
			parent.removeView(view);
		}
		ButterKnife.bind(this,view);
		commend.getPaint().setFakeBoldText(true);//加粗
		human.getPaint().setFakeBoldText(true);//加粗
		income.getPaint().setFakeBoldText(true);//加粗
		return view;
	}

	@Override
	public void init() {
		super.init();
	}
	@Override
	public void initdata() {
		/**
		 * 斗牛士推荐
		 */
		if (commendDatas.size() == 0) {
			commendDatas.add("");
			commendDatas.add("");
		}
		if(mCommendManager == null){
			mCommendManager = new FullyLinearLayoutManager(getActivity());
		}
		if(mCommendAdapter == null){
//			mCommendAdapter = new HomeAdvisorAdapter(getActivity(), commendDatas);
		}
		mCommendRecyclerView.setLayoutManager(mCommendManager);
		mCommendRecyclerView.setAdapter(mCommendAdapter);

		/**
		 * 人气最高
		 */
		if (humanDatas.size() == 0) {
			humanDatas.add("");
			humanDatas.add("");
			humanDatas.add("");
		}
		if(mHumanManager == null){
			mHumanManager = new FullyLinearLayoutManager(getActivity());
		}
		if(mHumanAdapter == null){
//			mHumanAdapter = new HomeAdvisorAdapter(getActivity(), humanDatas);
		}
		mHumanRecyclerView.setLayoutManager(mHumanManager);
		mHumanRecyclerView.setAdapter(mHumanAdapter);

		/**
		 * 收益最好
		 */
		if (incomeDatas.size() == 0) {
			incomeDatas.add("");
			incomeDatas.add("");
		}
		if(mIncomeManager == null){
			mIncomeManager = new FullyLinearLayoutManager(getActivity());
		}
		if(mIncomeAdapter == null){
//			mIncomeAdapter = new HomeAdvisorAdapter(getActivity(), incomeDatas);
		}
		mIncomeRecyclerView.setLayoutManager(mIncomeManager);
		mIncomeRecyclerView.setAdapter(mIncomeAdapter);

		if(mDivider ==null){
			mDivider = new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL);
			mCommendRecyclerView.addItemDecoration(mDivider);
			mHumanRecyclerView.addItemDecoration(mDivider);
			mIncomeRecyclerView.addItemDecoration(mDivider);
		}
		super.initdata();
	}

	@Override
	public void initListener() {
		super.initListener();
		mCommendAdapter.setOnItemClickListener(new HomeAdvisorAdapter.MyItemClickListener() {
			@Override
			public void onItemClick(View v, int positon) {
				Intent intent = new Intent(getActivity(), ActivityMatador.class);
				startActivity(intent);
				getActivity().overridePendingTransition(0,0);
			}
		});
		mHumanAdapter.setOnItemClickListener(new HomeAdvisorAdapter.MyItemClickListener() {
			@Override
			public void onItemClick(View v, int positon) {
				Intent intent = new Intent(getActivity(), ActivityMatador.class);
				startActivity(intent);
				getActivity().overridePendingTransition(0,0);
			}
		});
		mIncomeAdapter.setOnItemClickListener(new HomeAdvisorAdapter.MyItemClickListener() {
			@Override
			public void onItemClick(View v, int positon) {
				Intent intent = new Intent(getActivity(), ActivityMatador.class);
				startActivity(intent);
				getActivity().overridePendingTransition(0,0);
			}
		});
	}
}