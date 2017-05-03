package com.zfxf.douniu.view.fragment;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.ActivitySimulationStock;
import com.zfxf.douniu.adapter.recycleView.MatadorPositonAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.view.FullyLinearLayoutManager;
import com.zfxf.douniu.view.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FragmentSimulationStockBuy extends BaseFragment implements View.OnClickListener{
	private View view;

	@BindView(R.id.rv_fragment_simulation_buy)
	RecyclerView mRecyclerView;
	private LinearLayoutManager mLayoutManager;
	private MatadorPositonAdapter mPositonAdapter;
	private List<String> mStrings = new ArrayList<String>();
	private RecycleViewDivider mDivider;

	@BindView(R.id.rl_simulation_buy_confirm)
	RelativeLayout confirm;
	private AlertDialog mDialog;

	@Override
	public View initView(LayoutInflater inflater) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_simulation_buy, null);
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
		if (mStrings.size() == 0) {
			mStrings.add("");
			mStrings.add("");
			mStrings.add("");
			mStrings.add("");
		}
		if(mLayoutManager == null){
			mLayoutManager = new FullyLinearLayoutManager(getActivity());
		}
		if(mPositonAdapter == null){
			mPositonAdapter = new MatadorPositonAdapter(getActivity(), mStrings);
		}
		if(mDivider == null){//防止多次加载出现宽度变宽
			mDivider = new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL);
			mRecyclerView.addItemDecoration(mDivider);
		}
		mRecyclerView.setLayoutManager(mLayoutManager);
		mRecyclerView.setAdapter(mPositonAdapter);
	}
	@Override
	public void initListener() {
		super.initListener();
		ActivitySimulationStock.setOnRefreshListener(new ActivitySimulationStock.OnRefreshListener() {
			@Override
			public void refreshData() {
				CommonUtils.toastMessage("FragmentSimulationStockBuy  点击了刷新");
			}
		});
		confirm.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.rl_simulation_buy_confirm:
				confirmDialog();
				break;
			case R.id.tv_confirm_dialog_cannel:
				CommonUtils.toastMessage("取消");
				mDialog.dismiss();
				break;
			case R.id.tv_confirm_dialog_confirm:
				CommonUtils.toastMessage("确认购买");
				mDialog.dismiss();
				break;
		}
	}

	private void confirmDialog() {
		AlertDialog.Builder builder=new AlertDialog.Builder(getActivity()); //先得到构造器
		mDialog = builder.create();
		mDialog.show();
		View view = View.inflate(getActivity(), R.layout.activity_confirm_dialog, null);
		mDialog.getWindow().setContentView(view);

		view.findViewById(R.id.tv_confirm_dialog_cannel).setOnClickListener(this);
		view.findViewById(R.id.tv_confirm_dialog_confirm).setOnClickListener(this);
//		TextView textView = (TextView) view.findViewById(R.id.tv_dialog_text);
//		textView.setText("购买超值VIP会员资格，APP内所有视频全部解锁，仅需99元，无需再花一分钱即享永久的增值服务");
//		Button button = (Button) view.findViewById(R.id.bt_dialog_bt1);
//		button.setText("按钮1");
	}
}