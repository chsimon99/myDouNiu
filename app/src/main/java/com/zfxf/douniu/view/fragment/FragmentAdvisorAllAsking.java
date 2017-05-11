package com.zfxf.douniu.view.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.ActivityAnswer;
import com.zfxf.douniu.activity.ActivityAnswerDetail;
import com.zfxf.douniu.activity.ActivityAskAdvisor;
import com.zfxf.douniu.activity.ActivityAskStock;
import com.zfxf.douniu.adapter.recycleView.ZhenguAdvisorAdapter;
import com.zfxf.douniu.adapter.recycleView.ZhenguAnswerAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.view.FullyGridLayoutManager;
import com.zfxf.douniu.view.FullyLinearLayoutManager;
import com.zfxf.douniu.view.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * @author IMXU
 * @time   2017/5/3 13:38
 * @des    首席 微问答
 * 邮箱：butterfly_xu@sina.com
 *
*/

public class FragmentAdvisorAllAsking extends BaseFragment implements View.OnClickListener{
	private View view;

	@BindView(R.id.rl_zhengu_advisor_detail)
	RelativeLayout advisor_detail;
	@BindView(R.id.rl_zhengu_answer_detail)
	RelativeLayout answer_detail;
	@BindView(R.id.iv_zhengu_ask)
	ImageView ask;
	@BindView(R.id.tv_zhengu_text)
	TextView text_advisor;
	@BindView(R.id.tv_zhengu_answer)
	TextView text_answer;

	@BindView(R.id.rv_zhengu_advisor)
	RecyclerView mAdvisorRecyclerView;//在线分析师recycleview
	private LinearLayoutManager mAdvisorManager;
	private ZhenguAdvisorAdapter mAdvisorAdapter;
	private List<String> advisorDatas = new ArrayList<String>();

	@BindView(R.id.rv_zhengu_answer)
	RecyclerView mAnswerRecyclerView;//精彩回答recycleview
	private LinearLayoutManager mAnswerManager;
	private ZhenguAnswerAdapter mAnswerAdapter;
	private List<String> answerDatas = new ArrayList<String>();
	private RecycleViewDivider mDivider;

	@Override
	public View initView(LayoutInflater inflater) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_advisor_all_asking, null);
		}
		ViewGroup parent = (ViewGroup) view.getParent();
		if(parent !=null){
			parent.removeView(view);
		}
		ButterKnife.bind(this,view);
		text_advisor.getPaint().setFakeBoldText(true);//加粗
		text_answer.getPaint().setFakeBoldText(true);//加粗
		return view;
	}

	@Override
	public void init() {
		super.init();
	}
	@Override
	public void initdata() {
		super.initdata();
		/**
		 * 在线首席
		 */
		if (advisorDatas.size() == 0) {
			advisorDatas.add("百变股神");
			advisorDatas.add("孙悟空");
			advisorDatas.add("柯南");
			advisorDatas.add("小魔女");
		}
		if(mAdvisorManager == null){
			mAdvisorManager = new FullyGridLayoutManager(getActivity(),2);
		}
		if(mAdvisorAdapter == null){
			mAdvisorAdapter = new ZhenguAdvisorAdapter(getActivity(), advisorDatas);
		}
		mAdvisorRecyclerView.setLayoutManager(mAdvisorManager);
		mAdvisorRecyclerView.setAdapter(mAdvisorAdapter);

		/**
		 * 精彩回答
		 */
		if (answerDatas.size() == 0) {
			answerDatas.add("1");
			answerDatas.add("2");
			answerDatas.add("3");
			answerDatas.add("4");
			answerDatas.add("5");
		}
		if(mAnswerManager == null){
			mAnswerManager = new FullyLinearLayoutManager(getActivity());
		}
		if(mAnswerAdapter == null){
			mAnswerAdapter = new ZhenguAnswerAdapter(getActivity(), answerDatas);
		}
		mAnswerRecyclerView.setLayoutManager(mAnswerManager);
		mAnswerRecyclerView.setAdapter(mAnswerAdapter);
		if(mDivider == null){
			mDivider = new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL);
			mAnswerRecyclerView.addItemDecoration(mDivider);
		}
	}

	@Override
	public void initListener() {
		super.initListener();
		advisor_detail.setOnClickListener(this);
		answer_detail.setOnClickListener(this);
		ask.setOnClickListener(this);
		mAdvisorAdapter.setOnItemClickListener(new ZhenguAdvisorAdapter.MyItemClickListener() {
			@Override
			public void onItemClick(View v, int positon) {
				Intent intent = new Intent(CommonUtils.getContext(), ActivityAskStock.class);
				intent.putExtra("name",advisorDatas.get(positon).toString());
				startActivity(intent);
				getActivity().overridePendingTransition(0,0);
			}
		});
		mAnswerAdapter.setOnItemClickListener(new ZhenguAnswerAdapter.MyItemClickListener() {
			@Override
			public void onItemClick(View v, int positon) {
				Intent intent = new Intent(CommonUtils.getContext(), ActivityAnswerDetail.class);
				startActivity(intent);
				getActivity().overridePendingTransition(0,0);
			}
		});
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()){
			case R.id.rl_zhengu_advisor_detail:
				intent = new Intent(getActivity(), ActivityAskAdvisor.class);
				startActivity(intent);
				getActivity().overridePendingTransition(0,0);
				break;
			case R.id.rl_zhengu_answer_detail:
				intent = new Intent(getActivity(), ActivityAnswer.class);
				startActivity(intent);
				getActivity().overridePendingTransition(0,0);
				break;
			case R.id.iv_zhengu_ask:
				intent = new Intent(getActivity(), ActivityAskStock.class);
				startActivity(intent);
				getActivity().overridePendingTransition(0,0);
				break;
		}
	}
	private void finishAll() {
		mAdvisorManager = null;
		mAdvisorAdapter = null;
	}
}