package com.zfxf.douniu.view.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.myself.ActivityMyselfAdvisor;
import com.zfxf.douniu.activity.myself.ActivityMyselfAsk;
import com.zfxf.douniu.activity.myself.ActivityMyselfAttention;
import com.zfxf.douniu.activity.myself.ActivityMyselfConsume;
import com.zfxf.douniu.activity.myself.ActivityMyselfInformation;
import com.zfxf.douniu.activity.myself.ActivityMyselfRvaluateOne;
import com.zfxf.douniu.activity.myself.ActivityMyselfRvaluateResult;
import com.zfxf.douniu.activity.myself.ActivityMyselfShezhi;
import com.zfxf.douniu.activity.myself.ActivityMyselfSubscribe;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.SpTools;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FragmentMyself extends BaseFragment implements View.OnClickListener{
	private View view;

	@BindView(R.id.iv_myself_img)
	ImageView img;//头像
	@BindView(R.id.tv_myself_name)
	TextView name;//设置或昵称
	@BindView(R.id.rl_myself_message)
	RelativeLayout message;//消息
	@BindView(R.id.rl_myself_wallet)
	RelativeLayout wallet;//钱包
	@BindView(R.id.rl_myself_stock)
	RelativeLayout stock;//问股
	@BindView(R.id.rl_myself_account)
	RelativeLayout account;//开户
	@BindView(R.id.rl_myself_follow)
	RelativeLayout follow;//关注
	@BindView(R.id.ll_myself_subscribe)
	LinearLayout subscribe;//订阅
	@BindView(R.id.ll_myself_consume)
	LinearLayout consume;//消费记录
	@BindView(R.id.ll_myself_advisor)
	LinearLayout advisor;//投顾入驻
	@BindView(R.id.ll_myself_evaluate)
	LinearLayout evaluate;//风险评估
	@BindView(R.id.ll_myself_contract)
	LinearLayout contract;//电子合同
	@BindView(R.id.ll_myself_shezhi)
	LinearLayout shezhi;//设置
	@BindView(R.id.rl_myself_img)
	RelativeLayout login;//登录


	@Override
	public View initView(LayoutInflater inflater) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_myself, null);
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
	}

	@Override
	public void initListener() {
		super.initListener();
		message.setOnClickListener(this);
		wallet.setOnClickListener(this);
		stock.setOnClickListener(this);
		account.setOnClickListener(this);
		follow.setOnClickListener(this);
		subscribe.setOnClickListener(this);
		consume.setOnClickListener(this);
		advisor.setOnClickListener(this);
		evaluate.setOnClickListener(this);
		contract.setOnClickListener(this);
		shezhi.setOnClickListener(this);
		login.setOnClickListener(this);
		name.setOnClickListener(this);
	}
	Intent intent;
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.rl_myself_message:
				break;
			case R.id.rl_myself_wallet:
				break;
			case R.id.rl_myself_stock:
				intent = new Intent(CommonUtils.getContext(), ActivityMyselfAsk.class);
				startActivity(intent);
				getActivity().overridePendingTransition(0,0);
				break;
			case R.id.rl_myself_account:
				CommonUtils.toastMessage("此功能暂缓开通");
				break;
			case R.id.rl_myself_follow:
				intent = new Intent(CommonUtils.getContext(), ActivityMyselfAttention.class);
				startActivity(intent);
				getActivity().overridePendingTransition(0,0);
				break;
			case R.id.ll_myself_subscribe:
				intent = new Intent(CommonUtils.getContext(), ActivityMyselfSubscribe.class);
				startActivity(intent);
				getActivity().overridePendingTransition(0,0);
				break;
			case R.id.ll_myself_consume:
				intent = new Intent(CommonUtils.getContext(), ActivityMyselfConsume.class);
				startActivity(intent);
				getActivity().overridePendingTransition(0,0);
				break;
			case R.id.ll_myself_advisor:
				intent = new Intent(CommonUtils.getContext(), ActivityMyselfAdvisor.class);
				startActivity(intent);
				getActivity().overridePendingTransition(0,0);
				break;
			case R.id.ll_myself_evaluate:
				boolean isSuccess = SpTools.getBoolean(CommonUtils.getContext(), Constants.rvaluateSuccess, false);
				if(isSuccess){
					intent = new Intent(CommonUtils.getContext(), ActivityMyselfRvaluateResult.class);
					startActivity(intent);
					getActivity().overridePendingTransition(0,0);
				}else{
					intent = new Intent(CommonUtils.getContext(), ActivityMyselfRvaluateOne.class);
					startActivity(intent);
					getActivity().overridePendingTransition(0,0);
				}
				break;
			case R.id.ll_myself_contract:
				break;
			case R.id.ll_myself_shezhi:
				intent = new Intent(CommonUtils.getContext(), ActivityMyselfShezhi.class);
				startActivity(intent);
				getActivity().overridePendingTransition(0,0);
				break;
			case R.id.rl_myself_img:
			case R.id.tv_myself_name:
				boolean isLogin = SpTools.getBoolean(CommonUtils.getContext(), Constants.isLogin, false);
				Intent intent = new Intent(getActivity(), ActivityMyselfInformation.class);
				startActivity(intent);
				getActivity().overridePendingTransition(0,0);
//				if(isLogin){
//					Intent intent = new Intent(getActivity(), ActivityMyselfInformation.class);
//					startActivity(intent);
//					getActivity().overridePendingTransition(0,0);
//				}else{
//					Intent intent = new Intent(getActivity(), ActivityLogin.class);
//					startActivity(intent);
//					getActivity().overridePendingTransition(0,0);
//				}
				break;
		}
		intent = null;
	}
}