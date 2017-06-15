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
import com.zfxf.douniu.activity.ActivityToPay;
import com.zfxf.douniu.activity.login.ActivityLogin;
import com.zfxf.douniu.adapter.recycleView.ZhenguAdvisorAdapter;
import com.zfxf.douniu.adapter.recycleView.ZhenguAnswerAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.bean.AnswerListInfo;
import com.zfxf.douniu.bean.IndexResult;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.SpTools;
import com.zfxf.douniu.view.FullyGridLayoutManager;
import com.zfxf.douniu.view.FullyLinearLayoutManager;
import com.zfxf.douniu.view.RecycleViewDivider;

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

	@BindView(R.id.rv_zhengu_answer)
	RecyclerView mAnswerRecyclerView;//精彩回答recycleview
	private LinearLayoutManager mAnswerManager;
	private ZhenguAnswerAdapter mAnswerAdapter;
	private RecycleViewDivider mDivider;
	private boolean isShow = false;

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
		if(!isShow){
			isShow = true;
			visitInternet();
		}
	}

	private void visitInternet() {
		CommonUtils.showProgressDialog(getActivity(),"加载中……");
		NewsInternetRequest.getAnswerIndexInformation(new NewsInternetRequest.ForResultAnswerIndexListener() {
			@Override
			public void onResponseMessage(final IndexResult result) {
				/**
				 * 在线首席
				 */
				if(mAdvisorManager == null){
					mAdvisorManager = new FullyGridLayoutManager(getActivity(),2);
				}
				if(mAdvisorAdapter == null){
					mAdvisorAdapter = new ZhenguAdvisorAdapter(getActivity(), result.online_chief);
				}
				mAdvisorRecyclerView.setLayoutManager(mAdvisorManager);
				mAdvisorRecyclerView.setAdapter(mAdvisorAdapter);
				mAdvisorAdapter.setOnItemClickListener(new ZhenguAdvisorAdapter.MyItemClickListener() {
					@Override
					public void onItemClick(View v, int positon) {
						if(!SpTools.getBoolean(CommonUtils.getContext(), Constants.isLogin,false)){
							Intent intent = new Intent(getActivity(), ActivityLogin.class);
							getActivity().startActivity(intent);
							getActivity().overridePendingTransition(0,0);
							return;
						}
						Intent intent = new Intent(CommonUtils.getContext(), ActivityAskStock.class);
						intent.putExtra("name",result.online_chief.get(positon).ud_nickname);
						intent.putExtra("fee",result.online_chief.get(positon).df_fee);
						intent.putExtra("sx_id",result.online_chief.get(positon).sx_ub_id);
						startActivity(intent);
						getActivity().overridePendingTransition(0,0);
					}
				});

				/**
				 * 精彩回答
				 */
				if(mAnswerManager == null){
					mAnswerManager = new FullyLinearLayoutManager(getActivity());
				}
				if(mAnswerAdapter == null){
					mAnswerAdapter = new ZhenguAnswerAdapter(getActivity(), result.bright_answer);
				}
				mAnswerRecyclerView.setLayoutManager(mAnswerManager);
				mAnswerRecyclerView.setAdapter(mAnswerAdapter);
				if(mDivider == null){
					mDivider = new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL);
					mAnswerRecyclerView.addItemDecoration(mDivider);
				}
				mAnswerAdapter.setOnItemClickListener(new ZhenguAnswerAdapter.MyItemClickListener() {
					@Override
					public void onItemClick(View v, int positon,AnswerListInfo bean) {
						if(!SpTools.getBoolean(CommonUtils.getContext(), Constants.isLogin,false)){
							Intent intent = new Intent(getActivity(), ActivityLogin.class);
							getActivity().startActivity(intent);
							getActivity().overridePendingTransition(0,0);
							return;
						}
						if (bean.zc_sfjf.equals("0")){
							Intent intent = new Intent(CommonUtils.getContext(), ActivityToPay.class);
							intent.putExtra("type","问股");
							intent.putExtra("count",bean.zc_fee);
							intent.putExtra("from",bean.ud_nickname);
							startActivity(intent);
							getActivity().overridePendingTransition(0,0);
						}else{
							Intent intent = new Intent(CommonUtils.getContext(), ActivityAnswerDetail.class);
							intent.putExtra("id",bean.zc_id);
							startActivity(intent);
							getActivity().overridePendingTransition(0,0);
						}
					}
				});
				CommonUtils.dismissProgressDialog();
			}
		});
	}

	@Override
	public void initListener() {
		super.initListener();
		advisor_detail.setOnClickListener(this);
		answer_detail.setOnClickListener(this);
		ask.setOnClickListener(this);
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
				if(!SpTools.getBoolean(CommonUtils.getContext(), Constants.isLogin,false)){
					intent = new Intent(getActivity(), ActivityLogin.class);
					getActivity().startActivity(intent);
					getActivity().overridePendingTransition(0,0);
					return;
				}
				intent = new Intent(getActivity(), ActivityAnswer.class);
				startActivity(intent);
				getActivity().overridePendingTransition(0,0);
				break;
			case R.id.iv_zhengu_ask:
				if(!SpTools.getBoolean(CommonUtils.getContext(), Constants.isLogin,false)){
					intent = new Intent(getActivity(), ActivityLogin.class);
					getActivity().startActivity(intent);
					getActivity().overridePendingTransition(0,0);
					return;
				}
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

	@Override
	public void onResume() {
		super.onResume();
		if(SpTools.getBoolean(getActivity(), Constants.buy,false)){
			mAnswerAdapter = null;
			visitInternet();
			SpTools.setBoolean(getActivity(), Constants.buy,false);
		}
		if(SpTools.getBoolean(getActivity(), Constants.read,false)){
			mAnswerAdapter = null;
			visitInternet();
			SpTools.setBoolean(getActivity(), Constants.read,false);
		}
		if(SpTools.getBoolean(getActivity(), Constants.alreadyLogin,false)){
			mAnswerAdapter = null;
			visitInternet();
			SpTools.setBoolean(getActivity(), Constants.alreadyLogin,false);
		}
	}
}