package com.zfxf.douniu.view.fragment;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.ActivitySimulationStock;
import com.zfxf.douniu.adapter.recycleView.SimulationEntrustAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.bean.SimulationEntrust;
import com.zfxf.douniu.bean.SimulationResult;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.view.FullyLinearLayoutManager;
import com.zfxf.douniu.view.RecycleViewDivider;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * @author IMXU
 * @time   2017/5/3 13:31
 * @des    模拟炒股 委托
 * 邮箱：butterfly_xu@sina.com
 *
*/

public class FragmentSimulationStockEntrust extends BaseFragment implements View.OnClickListener{
	private View view;

	@BindView(R.id.rv_simulation_entrust)
	RecyclerView mRecyclerView;
	private SimulationEntrustAdapter mEntrustAdapter;
	private LinearLayoutManager mManager;
	private RecycleViewDivider mDivider;

	@Override
	public View initView(LayoutInflater inflater) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_simulation_entrust, null);
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
		CommonUtils.showProgressDialog(getActivity(),"加载中……");
		NewsInternetRequest.getSimulationEntrustInformation(new NewsInternetRequest.ForResultSimulationIndexListener() {
			@Override
			public void onResponseMessage(SimulationResult result) {
				if(mEntrustAdapter == null){
					mEntrustAdapter = new SimulationEntrustAdapter(getActivity(),result.all_weituo);
				}
				if(mManager == null){
					mManager = new FullyLinearLayoutManager(getActivity());
				}
				mRecyclerView.setLayoutManager(mManager);
				mRecyclerView.setAdapter(mEntrustAdapter);
				if(mDivider == null){
					mDivider = new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL);
					mRecyclerView.addItemDecoration(mDivider);
				}
				mEntrustAdapter.setOnItemClickListener(new SimulationEntrustAdapter.MyItemClickListener() {
					@Override
					public void onItemClick(View v, int position, SimulationEntrust entrust) {

						mLastTime=mCurTime;
						mCurTime= System.currentTimeMillis();
						if(mCurTime-mLastTime<300){//双击事件
							mCurTime =0;
							mLastTime = 0;
                            mEntrust = entrust;
//                            handler.removeMessages(1);
                            handler.sendEmptyMessage(2);
						}else{//单击事件
//							handler.sendEmptyMessageDelayed(1, 310);
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
		ActivitySimulationStock.setOnRefreshListener(new ActivitySimulationStock.OnRefreshListener() {
			@Override
			public void refreshData() {
				CommonUtils.toastMessage("FragmentSimulationStockEntrust  点击了刷新");
			}
		});
	}
	long mLastTime=0;
	long mCurTime=0;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case 1:
//					Toast.makeText(getActivity(),"这是单击事件",Toast.LENGTH_LONG).show();
					break;
				case 2:
//					Toast.makeText(getActivity(),"这是双击事件",Toast.LENGTH_LONG).show();
					confirmDialog();
					break;
			}
		}
	};
	private AlertDialog mDialog;
    private SimulationEntrust mEntrust;
	private void confirmDialog() {
		AlertDialog.Builder builder=new AlertDialog.Builder(getActivity()); //先得到构造器
		mDialog = builder.create();
		mDialog.show();
		View view = View.inflate(getActivity(), R.layout.activity_confirm_dialog, null);
		mDialog.getWindow().setContentView(view);
		TextView name = (TextView) view.findViewById(R.id.tv_confirm_dialog_name);
		TextView count = (TextView) view.findViewById(R.id.tv_confirm_dialog_count);
		TextView price = (TextView) view.findViewById(R.id.tv_confirm_dialog_price);
		TextView content = (TextView) view.findViewById(R.id.tv_confirm_dialog_content);

		view.findViewById(R.id.tv_confirm_dialog_cannel).setOnClickListener(this);
		view.findViewById(R.id.tv_confirm_dialog_confirm).setOnClickListener(this);
		content.setText("确认取消此委托?");
		count.setText("数量："+mEntrust.mj_wtl);
		price.setText("价格："+mEntrust.mj_wtj);
		name.setText("名称："+mEntrust.mg_name);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.tv_confirm_dialog_cannel:
				mDialog.dismiss();
				break;
			case R.id.tv_confirm_dialog_confirm:
				NewsInternetRequest.getSimulationEntrustDelete(mEntrust.mc_id, new NewsInternetRequest.ForResultSimulationIndexListener() {
					@Override
					public void onResponseMessage(SimulationResult result) {
						if(result != null){
							CommonUtils.toastMessage("委托取消成功");
							mEntrustAdapter = null;
							visitInternet();
						}else {
							CommonUtils.toastMessage("取消失败，请重试");
						}
					}
				});
				mDialog.dismiss();
				break;
		}
	}
}