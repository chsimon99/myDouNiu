package com.zfxf.douniu.view.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.ActivityMatador;
import com.zfxf.douniu.activity.ActivityMatadorList;
import com.zfxf.douniu.activity.login.ActivityLogin;
import com.zfxf.douniu.adapter.recycleView.MatadorAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.bean.MatadorResult;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.SpTools;
import com.zfxf.douniu.view.FullyLinearLayoutManager;
import com.zfxf.douniu.view.RecycleViewDivider;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * @author IMXU
 * @time   2017/5/3 13:27
 * @des    斗牛士评级
 * 邮箱：butterfly_xu@sina.com
 *
*/

public class FragmentBarGrade extends BaseFragment implements View.OnClickListener{
	private View view;
	@BindView(R.id.rv_bar_grade_commend)
	RecyclerView mCommendRecyclerView;//斗牛士推荐recycleview
	private LinearLayoutManager mCommendManager;
	private MatadorAdapter mCommendAdapter;

	@BindView(R.id.rv_bar_grade_human)
	RecyclerView mHumanRecyclerView;//周排行recycleview
	private LinearLayoutManager mHumanManager;
	private MatadorAdapter mHumanAdapter;

	@BindView(R.id.rv_bar_grade_income)
	RecyclerView mIncomeRecyclerView;//月排行recycleview
	private LinearLayoutManager mIncomeManager;
	private MatadorAdapter mIncomeAdapter;

	@BindView(R.id.rv_bar_grade_all)
	RecyclerView mALLRecyclerView;//总排行recycleview
	private LinearLayoutManager mALLManager;
	private MatadorAdapter mALLAdapter;

	@BindView(R.id.tv_bar_grade_commend)
	TextView commend;
	@BindView(R.id.tv_bar_grade_income)
	TextView income;
	@BindView(R.id.tv_bar_grade_human)
	TextView human;
	@BindView(R.id.tv_bar_grade_all)
	TextView all;
	@BindView(R.id.ll_bar_grade_human)
	LinearLayout iv_zhou;
	@BindView(R.id.ll_bar_grade_income)
	LinearLayout iv_yue;
	@BindView(R.id.ll_bar_grade_all)
	LinearLayout iv_all;
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
		all.getPaint().setFakeBoldText(true);//加粗
		return view;
	}

	@Override
	public void init() {
		super.init();
	}
	@Override
	public void initdata() {
		visitInternet();
		super.initdata();
	}

	private void visitInternet() {
		CommonUtils.showProgressDialog(getActivity(),"加载中……");
		NewsInternetRequest.getMatadorIndexInformation(new NewsInternetRequest.ForResultMatadorIndexListener() {
			@Override
			public void onResponseMessage(MatadorResult result) {
				/**
				 * 斗牛士推荐
				 */
				if(mCommendManager == null){
					mCommendManager = new FullyLinearLayoutManager(getActivity());
				}
				if (mCommendAdapter == null) {
					mCommendAdapter = new MatadorAdapter(getActivity(), result.t_list);
				}
				mCommendRecyclerView.setLayoutManager(mCommendManager);
				mCommendRecyclerView.setAdapter(mCommendAdapter);
				/**
				 * 周排行
				 */
				if(mHumanManager == null){
					mHumanManager = new FullyLinearLayoutManager(getActivity());
				}
				if(mHumanAdapter == null){
					mHumanAdapter = new MatadorAdapter(getActivity(), result.w_list);
				}
				mHumanRecyclerView.setLayoutManager(mHumanManager);
				mHumanRecyclerView.setAdapter(mHumanAdapter);
				/**
				 * 月排行
				 */
				if(mIncomeManager == null){
					mIncomeManager = new FullyLinearLayoutManager(getActivity());
				}
				if(mIncomeAdapter == null){
					mIncomeAdapter = new MatadorAdapter(getActivity(), result.m_list);
				}
				mIncomeRecyclerView.setLayoutManager(mIncomeManager);
				mIncomeRecyclerView.setAdapter(mIncomeAdapter);
				/**
				 * 总排行
				 */
				if(mALLManager == null){
					mALLManager = new FullyLinearLayoutManager(getActivity());
				}
				if(mALLAdapter == null){
					mALLAdapter = new MatadorAdapter(getActivity(), result.z_list);
				}
				mALLRecyclerView.setLayoutManager(mALLManager);
				mALLRecyclerView.setAdapter(mALLAdapter);

				if(mDivider ==null){
					mDivider = new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL);
					mCommendRecyclerView.addItemDecoration(mDivider);
					mHumanRecyclerView.addItemDecoration(mDivider);
					mIncomeRecyclerView.addItemDecoration(mDivider);
					mALLRecyclerView.addItemDecoration(mDivider);
				}

				mCommendAdapter.setOnItemClickListener(new MatadorAdapter.MyItemClickListener() {
					@Override
					public void onItemClick(View v, int id) {
						if(!SpTools.getBoolean(CommonUtils.getContext(), Constants.isLogin,false)){
							Intent intent = new Intent(getActivity(), ActivityLogin.class);
							startActivity(intent);
							getActivity().overridePendingTransition(0,0);
							return;
						}
						Intent intent = new Intent(getActivity(), ActivityMatador.class);
						intent.putExtra("id",id+"");
						startActivity(intent);
						getActivity().overridePendingTransition(0,0);
					}
				});
				mHumanAdapter.setOnItemClickListener(new MatadorAdapter.MyItemClickListener() {
					@Override
					public void onItemClick(View v, int id) {
						if(!SpTools.getBoolean(CommonUtils.getContext(), Constants.isLogin,false)){
							Intent intent = new Intent(getActivity(), ActivityLogin.class);
							startActivity(intent);
							getActivity().overridePendingTransition(0,0);
							return;
						}
						Intent intent = new Intent(getActivity(), ActivityMatador.class);
						intent.putExtra("id",id+"");
						startActivity(intent);
						getActivity().overridePendingTransition(0,0);
					}
				});
				mIncomeAdapter.setOnItemClickListener(new MatadorAdapter.MyItemClickListener() {
					@Override
					public void onItemClick(View v, int id) {
						if(!SpTools.getBoolean(CommonUtils.getContext(), Constants.isLogin,false)){
							Intent intent = new Intent(getActivity(), ActivityLogin.class);
							startActivity(intent);
							getActivity().overridePendingTransition(0,0);
							return;
						}
						Intent intent = new Intent(getActivity(), ActivityMatador.class);
						intent.putExtra("id",id+"");
						startActivity(intent);
						getActivity().overridePendingTransition(0,0);
					}
				});
				mALLAdapter.setOnItemClickListener(new MatadorAdapter.MyItemClickListener() {
					@Override
					public void onItemClick(View v, int id) {
						if(!SpTools.getBoolean(CommonUtils.getContext(), Constants.isLogin,false)){
							Intent intent = new Intent(getActivity(), ActivityLogin.class);
							startActivity(intent);
							getActivity().overridePendingTransition(0,0);
							return;
						}
						Intent intent = new Intent(getActivity(), ActivityMatador.class);
						intent.putExtra("id",id+"");
						startActivity(intent);
						getActivity().overridePendingTransition(0,0);
					}
				});
				CommonUtils.dismissProgressDialog();
			}
		});
	}

	@Override
	public void initListener() {
		super.initListener();
		iv_zhou.setOnClickListener(this);
		iv_yue.setOnClickListener(this);
		iv_all.setOnClickListener(this);
	}
	Intent intent;
	@Override
	public void onClick(View v) {
		intent = new Intent(getActivity(), ActivityMatadorList.class);
		switch (v.getId()){
			case R.id.ll_bar_grade_human:
				intent.putExtra("id",1);
				startActivity(intent);
				getActivity().overridePendingTransition(0,0);
				break;
			case R.id.ll_bar_grade_income:
				intent.putExtra("id",2);
				startActivity(intent);
				getActivity().overridePendingTransition(0,0);
				break;
			case R.id.ll_bar_grade_all:
				intent.putExtra("id",3);
				startActivity(intent);
				getActivity().overridePendingTransition(0,0);
				break;
		}
		intent = null;
	}
}