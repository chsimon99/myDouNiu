package com.zfxf.douniu.view.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.login.ActivityLogin;
import com.zfxf.douniu.activity.myself.ActivityMyselfAdvisor;
import com.zfxf.douniu.activity.myself.ActivityMyselfAsk;
import com.zfxf.douniu.activity.myself.ActivityMyselfAttention;
import com.zfxf.douniu.activity.myself.ActivityMyselfConsume;
import com.zfxf.douniu.activity.myself.ActivityMyselfEleContract;
import com.zfxf.douniu.activity.myself.ActivityMyselfInformation;
import com.zfxf.douniu.activity.myself.evaluate.ActivityMyselfRvaluateOne;
import com.zfxf.douniu.activity.myself.evaluate.ActivityMyselfRvaluateResult;
import com.zfxf.douniu.activity.myself.install.ActivityMyselfShezhi;
import com.zfxf.douniu.activity.myself.ActivityMyselfSubscribe;
import com.zfxf.douniu.activity.myself.wallet.ActivityMyselfWallet;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.internet.LoginInternetRequest;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.SpTools;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * @author IMXU
 * @time   2017/5/3 13:16
 * @des    我的界面
 * 邮箱：butterfly_xu@sina.com
 *
*/

public class FragmentMyself extends BaseFragment implements View.OnClickListener{
	private View view;

	@BindView(R.id.iv_myself_img)
	ImageView img;//头像
	@BindView(R.id.tv_myself_name)
	TextView name;//设置或昵称
//	@BindView(R.id.rl_myself_message)
//	RelativeLayout message;//消息
	@BindView(R.id.rl_myself_wallet)
	RelativeLayout wallet;//钱包
	@BindView(R.id.rl_myself_stock)
	RelativeLayout stock;//问股
	@BindView(R.id.rl_myself_account)
	RelativeLayout account;//我的订阅
	@BindView(R.id.rl_myself_follow)
	RelativeLayout follow;//关注
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
	private static final int LOGIN	= 101;
	private static final int EDITINFOR	= 102;

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
		//访问网络获取个人信息
		if(!SpTools.getBoolean(getActivity(),Constants.isLogin,false)){
			img.setImageResource(R.drawable.home_adviosr_img);
			name.setText("注册/登陆");
			return;
		}
		showMessage();
	}

	private void showMessage() {
		String nickname = SpTools.getString(getActivity(), Constants.nickname, "");
		if(!TextUtils.isEmpty(nickname)){
			name.setText(nickname);
			Bitmap cacheBitmap = CommonUtils.getCacheFile("myicon.jpg");
			if (cacheBitmap != null) {
				byte[] bytes = CommonUtils.getBitMapByteArray(cacheBitmap);
				Glide.with(getActivity()).load(bytes)
						.placeholder(R.drawable.home_adviosr_img)
						.bitmapTransform(new CropCircleTransformation(getActivity()))
						.into(img);
				return;
			}
		}
		getUserInformation();
	}

	@Override
	public void initListener() {
		super.initListener();
//		message.setOnClickListener(this);
		wallet.setOnClickListener(this);
		stock.setOnClickListener(this);
		account.setOnClickListener(this);
		follow.setOnClickListener(this);
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
//			case R.id.rl_myself_message:
//				intent = new Intent(CommonUtils.getContext(), ActivityMyselfMessage.class);
//				startActivity(intent);
//				getActivity().overridePendingTransition(0,0);
//				break;
			case R.id.rl_myself_wallet:
				if(!SpTools.getBoolean(CommonUtils.getContext(), Constants.isLogin,false)){
					toLogin();
				}else {
					intent = new Intent(CommonUtils.getContext(), ActivityMyselfWallet.class);
					startActivity(intent);
					getActivity().overridePendingTransition(0,0);
				}
				break;
			case R.id.rl_myself_stock:
				if(!SpTools.getBoolean(CommonUtils.getContext(), Constants.isLogin,false)){
					toLogin();
				}else {
					intent = new Intent(CommonUtils.getContext(), ActivityMyselfAsk.class);
					startActivity(intent);
					getActivity().overridePendingTransition(0,0);
				}
				break;
			case R.id.rl_myself_account:
				if(!SpTools.getBoolean(CommonUtils.getContext(), Constants.isLogin,false)){
					toLogin();
				}else {
					intent = new Intent(CommonUtils.getContext(), ActivityMyselfSubscribe.class);
					startActivity(intent);
					getActivity().overridePendingTransition(0,0);
				}
				break;
			case R.id.rl_myself_follow:
				if(!SpTools.getBoolean(CommonUtils.getContext(), Constants.isLogin,false)){
					toLogin();
				}else {
					intent = new Intent(CommonUtils.getContext(), ActivityMyselfAttention.class);
					startActivity(intent);
					getActivity().overridePendingTransition(0,0);
				}
				break;
			case R.id.ll_myself_consume:
				if(!SpTools.getBoolean(CommonUtils.getContext(), Constants.isLogin,false)){
					toLogin();
				}else {
					intent = new Intent(CommonUtils.getContext(), ActivityMyselfConsume.class);
					startActivity(intent);
					getActivity().overridePendingTransition(0,0);
				}
				break;
			case R.id.ll_myself_advisor:
				intent = new Intent(CommonUtils.getContext(), ActivityMyselfAdvisor.class);
				startActivity(intent);
				getActivity().overridePendingTransition(0,0);
				break;
			case R.id.ll_myself_evaluate:
				if(!SpTools.getBoolean(CommonUtils.getContext(), Constants.isLogin,false)){
					toLogin();
				}else {
					int resultNum = SpTools.getInt(getActivity(), Constants.rvaluateResult, 0);
					if(resultNum == 0){
						NewsInternetRequest.getMyRiskGrade(new NewsInternetRequest.ForResultListener() {
							@Override
							public void onResponseMessage(String count) {
								if(!TextUtils.isEmpty(count)){
									if("0".equals(count)){
										intent = new Intent(CommonUtils.getContext(), ActivityMyselfRvaluateOne.class);
										startActivity(intent);
										getActivity().overridePendingTransition(0,0);
									}else {
										SpTools.setInt(getActivity(),Constants.rvaluateResult,Integer.parseInt(count));
										intent = new Intent(CommonUtils.getContext(), ActivityMyselfRvaluateResult.class);
										startActivity(intent);
										getActivity().overridePendingTransition(0,0);
									}
								}else {
									CommonUtils.toastMessage("网络不好请重试");
								}
							}
						});
					}else {
						intent = new Intent(CommonUtils.getContext(), ActivityMyselfRvaluateResult.class);
						startActivity(intent);
						getActivity().overridePendingTransition(0,0);
					}
//					boolean isSuccess = SpTools.getBoolean(CommonUtils.getContext(), Constants.rvaluateSuccess, false);
//					if(isSuccess){
//						intent = new Intent(CommonUtils.getContext(), ActivityMyselfRvaluateResult.class);
//						startActivity(intent);
//						getActivity().overridePendingTransition(0,0);
//					}else{
//						intent = new Intent(CommonUtils.getContext(), ActivityMyselfRvaluateOne.class);
//						startActivity(intent);
//						getActivity().overridePendingTransition(0,0);
//					}
				}
				break;
			case R.id.ll_myself_contract:
				intent = new Intent(CommonUtils.getContext(), ActivityMyselfEleContract.class);
				startActivity(intent);
				getActivity().overridePendingTransition(0,0);
				break;
			case R.id.ll_myself_shezhi:
				intent = new Intent(CommonUtils.getContext(), ActivityMyselfShezhi.class);
				startActivity(intent);
				getActivity().overridePendingTransition(0,0);
				break;
			case R.id.rl_myself_img:
			case R.id.tv_myself_name:
				boolean isLogin = SpTools.getBoolean(CommonUtils.getContext(), Constants.isLogin, false);
//				Intent intent = new Intent(getActivity(), ActivityMyselfInformation.class);
//				startActivityForResult(intent,EDITINFOR);
//				getActivity().overridePendingTransition(0,0);
				if(isLogin){
					Intent intent = new Intent(getActivity(), ActivityMyselfInformation.class);
					startActivityForResult(intent,EDITINFOR);
					getActivity().overridePendingTransition(0,0);
				}else{
					Intent intent = new Intent(getActivity(), ActivityLogin.class);
					startActivityForResult(intent,LOGIN);
					getActivity().overridePendingTransition(0,0);
				}
				break;
		}
		intent = null;
	}

	private void toLogin() {
		intent = new Intent(getActivity(), ActivityLogin.class);
		getActivity().startActivity(intent);
		getActivity().overridePendingTransition(0,0);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode){
			case Constants.resultCodeLogin:
			case Constants.resultCodeEditInfor:
				getUserInformation();
				break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	private static boolean isExit = false;
	public static void exit(){//退出登陆后修改此设置
		isExit = true;
	}

	@Override
	public void onResume() {
		super.onResume();
		if(isExit){
			isExit = false;
			if(!SpTools.getBoolean(getActivity(),Constants.isLogin,false)){
				img.setImageResource(R.drawable.home_adviosr_img);
				name.setText("注册/登陆");
			}
		}
		if(SpTools.getBoolean(getActivity(), Constants.alreadyLogin,false)){
			SpTools.setBoolean(getActivity(), Constants.alreadyLogin,false);
			showMessage();
		}
	}

	private void getUserInformation() {
		CommonUtils.showProgressDialog(getActivity(),"加载中……");
		LoginInternetRequest.getUserInformation(new LoginInternetRequest.ForResultInfoListener() {
			@Override
			public void onResponseMessage(Map<String, String> map) {
				if (map.isEmpty()) {
					CommonUtils.dismissProgressDialog();
					return;
				} else {
					String nickname = map.get("ud_nickname");
					String address = map.get("ud_addr");
					String borth = map.get("ud_borth");
					final String photoUrl = getResources().getString(R.string.file_host_address)
							+getResources().getString(R.string.showpic)
							+map.get("ud_photo_fileid");
					if (TextUtils.isEmpty(nickname)) {
						name.setText("请您去设置昵称");
					} else {
						name.setText(nickname);
						SpTools.setString(getActivity(),Constants.nickname,nickname);
					}
					if (TextUtils.isEmpty(photoUrl)) {
						img.setImageResource(R.drawable.home_adviosr_img);
					} else {
						SpTools.setString(getActivity(),Constants.imgurl,photoUrl);
						Bitmap cacheBitmap = CommonUtils.getCacheFile("myicon.jpg");
						if (cacheBitmap == null) {
							Glide.with(getActivity()).load(photoUrl)
									.placeholder(R.drawable.home_adviosr_img)
									.bitmapTransform(new CropCircleTransformation(getActivity()))
									.into(img);

								new Thread(new Runnable() {
									@Override
									public void run() {
										Bitmap bitmap = null;//保存图片到本地
										try {
											bitmap = Glide.with(getActivity()).load(photoUrl).asBitmap().into(500, 500).get();
											CommonUtils.saveBitmapFile(bitmap,"myicon.jpg");
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								}).start();

						} else {
							byte[] bytes = CommonUtils.getBitMapByteArray(cacheBitmap);
							Glide.with(getActivity()).load(bytes)
									.placeholder(R.drawable.home_adviosr_img)
									.bitmapTransform(new CropCircleTransformation(getActivity()))
									.into(img);
						}
					}
					CommonUtils.dismissProgressDialog();
				}
			}
		});
	}

}