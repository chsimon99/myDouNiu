package com.zfxf.douniu.view.fragment;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.ActivityMarketResearch;
import com.zfxf.douniu.activity.ActivitySimulationStock;
import com.zfxf.douniu.adapter.recycleView.MatadorPositonAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.bean.SimulationPositionDetail;
import com.zfxf.douniu.bean.SimulationResult;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.SpTools;
import com.zfxf.douniu.view.FullyLinearLayoutManager;
import com.zfxf.douniu.view.MyScrollview;
import com.zfxf.douniu.view.RecycleViewDivider;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author IMXU
 * @time   2017/5/3 13:31
 * @des    模拟炒股卖出
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class FragmentSimulationStockSold extends BaseFragment implements View.OnClickListener{
	private View view;

	@BindView(R.id.rv_fragment_simulation_buy)
	RecyclerView mRecyclerView;
	private LinearLayoutManager mLayoutManager;
	private MatadorPositonAdapter mPositonAdapter;
	private RecycleViewDivider mDivider;

	@BindView(R.id.sv_simulation_sold)
	MyScrollview mScrollview;
	@BindView(R.id.rl_simulation_sold_confirm)
	RelativeLayout confirm;
	@BindView(R.id.ll_simulation_sold_add)
	LinearLayout ll_add;//+0.01
	@BindView(R.id.ll_simulation_sold_minus)
	LinearLayout ll_minus;//-0.01
	@BindView(R.id.ll_simulation_sold_search)
	LinearLayout ll_search;//收索

	@BindView(R.id.tv_simulation_sold_code)
	TextView tv_code;//股票代码
	@BindView(R.id.tv_simulation_sold_name)
	TextView tv_name;//股票名称
	@BindView(R.id.et_simulation_sold_price)
	EditText et_price;//价格
	@BindView(R.id.tv_simulation_sold_price_add)
	TextView tv_price_add;//涨停价格
	@BindView(R.id.tv_simulation_sold_price_minus)
	TextView tv_price_minus;//跌停价格
	@BindView(R.id.et_simulation_sold_count)
	EditText et_count;//购买数量
	@BindView(R.id.tv_simulation_sold_count_all)
	TextView tv_count_a;//全仓
	@BindView(R.id.tv_simulation_sold_count_half)
	TextView tv_count_h;//半仓
	@BindView(R.id.tv_simulation_sold_count_three)
	TextView tv_count_t;//1/3仓
	@BindView(R.id.tv_simulation_sold_count_four)
	TextView tv_count_f;//1/4仓

	@BindView(R.id.tv_simulation_sold_sold1)
	TextView tv_sold1;//卖1
	@BindView(R.id.tv_simulation_sold_sold2)
	TextView tv_sold2;//卖2
	@BindView(R.id.tv_simulation_sold_sold3)
	TextView tv_sold3;//卖3
	@BindView(R.id.tv_simulation_sold_sold4)
	TextView tv_sold4;//卖4
	@BindView(R.id.tv_simulation_sold_sold5)
	TextView tv_sold5;//卖5
	@BindView(R.id.tv_simulation_sold_buy1)
	TextView tv_buy1;//买1
	@BindView(R.id.tv_simulation_sold_buy2)
	TextView tv_buy2;//买2
	@BindView(R.id.tv_simulation_sold_buy3)
	TextView tv_buy3;//买3
	@BindView(R.id.tv_simulation_sold_buy4)
	TextView tv_buy4;//买4
	@BindView(R.id.tv_simulation_sold_buy5)
	TextView tv_buy5;//买5
	private AlertDialog mDialog;
	private final int mRequestCode = 1;
	private String mStockCode;
	private String mYesPrice;
	private String mgKmsl;
	private String mStock_dt;
	private String mStock_zt;

	@Override
	public View initView(LayoutInflater inflater) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_simulation_sold, null);
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
		visitInternet(null);
	}

	private void visitInternet(String code) {
		CommonUtils.showProgressDialog(getActivity(),"加载中……");
		reset();
		NewsInternetRequest.getSimulationSellInformation(code, new NewsInternetRequest.ForResultSimulationIndexListener() {
			@Override
			public void onResponseMessage(SimulationResult result) {
				if(result.mn_chigu !=null){
					if(mLayoutManager == null){
						mLayoutManager = new FullyLinearLayoutManager(getActivity());
					}
					if(mPositonAdapter == null){
						mPositonAdapter = new MatadorPositonAdapter(getActivity(), result.mn_chigu);
					}
					if(mDivider == null){//防止多次加载出现宽度变宽
						mDivider = new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL);
						mRecyclerView.addItemDecoration(mDivider);
					}
				}
				mRecyclerView.setLayoutManager(mLayoutManager);
				mRecyclerView.setAdapter(mPositonAdapter);
				mPositonAdapter.setOnItemClickListener(new MatadorPositonAdapter.MyItemClickListener() {
					@Override
					public void onItemClick(View v, int positon,SimulationPositionDetail detail) {
						tv_name.setText(detail.mg_name);
						mStockCode = detail.mc_code;
						tv_code.setText(mStockCode);
						visitInternet(mStockCode);
						mScrollview.smoothScrollTo(0,0);
					}
				});
				if(result.mn_gupiao !=null){
					mStock_zt = result.mn_gupiao.mg_zt;
					tv_price_add.setText(mStock_zt);
					mStock_dt = result.mn_gupiao.mg_dt;
					tv_price_minus.setText(mStock_dt);
					et_price.setText(result.mn_gupiao.mg_xj);
					mYesPrice = result.mn_gupiao.mg_zs;
					if(!TextUtils.isEmpty(result.mn_gupiao.mg_kmsl)){
						mgKmsl = result.mn_gupiao.mg_kmsl;
					}
				}
				if(result.mr_gupiao!=null){
					if(TextUtils.isEmpty(result.mr_gupiao.mg_b1)){
						tv_buy1.setText("——");
						tv_buy1.setTextColor(getColor());
					}else {
						tv_buy1.setText(result.mr_gupiao.mg_b1);
						tv_buy1.setTextColor(getShowColor(result.mr_gupiao.mg_b1));
					}
					if(TextUtils.isEmpty(result.mr_gupiao.mg_b2)){
						tv_buy2.setText("——");
						tv_buy2.setTextColor(getColor());
					}else {
						tv_buy2.setText(result.mr_gupiao.mg_b2);
						tv_buy2.setTextColor(getShowColor(result.mr_gupiao.mg_b2));
					}
					if(TextUtils.isEmpty(result.mr_gupiao.mg_b3)){
						tv_buy3.setText("——");
						tv_buy3.setTextColor(getColor());
					}else {
						tv_buy3.setText(result.mr_gupiao.mg_b3);
						tv_buy3.setTextColor(getShowColor(result.mr_gupiao.mg_b3));
					}
					if(TextUtils.isEmpty(result.mr_gupiao.mg_b4)){
						tv_buy4.setText("——");
						tv_buy4.setTextColor(getColor());
					}else {
						tv_buy4.setText(result.mr_gupiao.mg_b4);
						tv_buy4.setTextColor(getShowColor(result.mr_gupiao.mg_b4));
					}
					if(TextUtils.isEmpty(result.mr_gupiao.mg_b5)){
						tv_buy5.setText("——");
						tv_buy5.setTextColor(getColor());
					}else {
						tv_buy5.setText(result.mr_gupiao.mg_b5);
						tv_buy5.setTextColor(getShowColor(result.mr_gupiao.mg_b5));
					}
				}
				if(result.mc_gupiao!=null){
					if(TextUtils.isEmpty(result.mc_gupiao.mg_s1)){
						tv_sold1.setText("——");
						tv_sold1.setTextColor(getColor());
					}else {
						tv_sold1.setText(result.mc_gupiao.mg_s1);
						tv_sold1.setTextColor(getShowColor(result.mc_gupiao.mg_s1));
					}
					if(TextUtils.isEmpty(result.mc_gupiao.mg_s2)){
						tv_sold2.setText("——");
						tv_sold2.setTextColor(getColor());
					}else {
						tv_sold2.setText(result.mc_gupiao.mg_s2);
						tv_sold2.setTextColor(getShowColor(result.mc_gupiao.mg_s2));
					}
					if(TextUtils.isEmpty(result.mc_gupiao.mg_s3)){
						tv_sold3.setText("——");
						tv_sold3.setTextColor(getColor());
					}else {
						tv_sold3.setText(result.mc_gupiao.mg_s3);
						tv_sold3.setTextColor(getShowColor(result.mc_gupiao.mg_s3));
					}
					if(TextUtils.isEmpty(result.mc_gupiao.mg_s4)){
						tv_sold4.setText("——");
						tv_sold4.setTextColor(getColor());
					}else {
						tv_sold4.setText(result.mc_gupiao.mg_s4);
						tv_sold4.setTextColor(getShowColor(result.mc_gupiao.mg_s4));
					}
					if(TextUtils.isEmpty(result.mc_gupiao.mg_s5)){
						tv_sold5.setText("——");
						tv_sold5.setTextColor(getColor());
					}else {
						tv_sold5.setText(result.mc_gupiao.mg_s5);
						tv_sold5.setTextColor(getShowColor(result.mc_gupiao.mg_s5));
					}
				}
				CommonUtils.dismissProgressDialog();
			}
		});
	}

	private void reset() {
		tv_buy1.setText("——");
		tv_buy1.setTextColor(getColor());
		tv_buy2.setText("——");
		tv_buy2.setTextColor(getColor());
		tv_buy3.setText("——");
		tv_buy3.setTextColor(getColor());
		tv_buy4.setText("——");
		tv_buy4.setTextColor(getColor());
		tv_buy5.setText("——");
		tv_buy5.setTextColor(getColor());
		tv_sold1.setText("——");
		tv_sold1.setTextColor(getColor());
		tv_sold2.setText("——");
		tv_sold2.setTextColor(getColor());
		tv_sold3.setText("——");
		tv_sold3.setTextColor(getColor());
		tv_sold4.setText("——");
		tv_sold4.setTextColor(getColor());
		tv_sold5.setText("——");
		tv_sold5.setTextColor(getColor());
		tv_price_add.setText("");
		tv_price_minus.setText("");
		et_price.setText("");
	}
	private int getShowColor(String str) {
		float today = Float.parseFloat(str);
		float yes = 0;
		if(!TextUtils.isEmpty(mYesPrice)){
			yes = Float.parseFloat(mYesPrice);
		}
		if(today > yes){
			return getResources().getColor(R.color.colorRise);
		}else if(today < yes){
			return getResources().getColor(R.color.colorFall);
		}else {
			return getResources().getColor(R.color.colorText);
		}
	}

	private int getColor() {
		return getResources().getColor(R.color.colorText);
	}

	@Override
	public void initListener() {
		super.initListener();
		ll_add.setOnClickListener(this);
		ll_minus.setOnClickListener(this);
		ll_search.setOnClickListener(this);
		ActivitySimulationStock.setOnRefreshListener(new ActivitySimulationStock.OnRefreshListener() {
			@Override
			public void refreshData() {
				visitInternet(mStockCode);
			}
		});
		confirm.setOnClickListener(this);
		tv_count_a.setOnClickListener(this);
		tv_count_h.setOnClickListener(this);
		tv_count_t.setOnClickListener(this);
		tv_count_f.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		int count = 0;
		switch (v.getId()){
			case R.id.rl_simulation_sold_confirm:
				if(TextUtils.isEmpty(et_price.getText().toString())){
					return;
				}
				if(Float.parseFloat(et_price.getText().toString())>Float.parseFloat(mStock_zt)){
					CommonUtils.toastMessage("卖出价格不能大于涨停价");
					return;
				}
				if(Float.parseFloat(et_price.getText().toString())<Float.parseFloat(mStock_dt)){
					CommonUtils.toastMessage("卖出价格不能低于跌停价");
					return;
				}
				if(TextUtils.isEmpty(et_count.getText().toString())){
					CommonUtils.toastMessage("卖出数量不能为空");
					return;
				}
				if(Integer.parseInt(et_count.getText().toString().trim())<100){
					CommonUtils.toastMessage("卖出数量不能小于100股");
					return;
				}
				String s_count = et_count.getText().toString().trim();
				et_count.setText(changeCount(Integer.parseInt(s_count))+"");
				confirmDialog();
				break;
			case R.id.tv_confirm_dialog_cannel:
				CommonUtils.toastMessage("取消");
				mDialog.dismiss();
				break;
			case R.id.tv_confirm_dialog_confirm:
				confirmSell();
				mDialog.dismiss();
				break;
			case R.id.ll_simulation_sold_search:
				Intent intent = new Intent(getActivity(), ActivityMarketResearch.class);
				intent.putExtra("simulation",true);
				startActivityForResult(intent, mRequestCode);
				getActivity().overridePendingTransition(0,0);
				break;
			case R.id.ll_simulation_sold_add:
				String s_price = et_price.getText().toString();
				if(TextUtils.isEmpty(s_price)){
					return;
				}
				Float fp = Float.parseFloat(s_price);
				fp = (float)(Math.round((fp+0.01f)*100))/100;
				if(fp > Float.parseFloat(mStock_zt)){
					CommonUtils.toastMessage("卖出价格不能大于涨停价");
					et_price.setText(mStock_zt);
					return;
				}
				et_price.setText(fp+"");
				break;
			case R.id.ll_simulation_sold_minus:
				String m_price = et_price.getText().toString();
				if(TextUtils.isEmpty(m_price)){
					return;
				}
				Float f = Float.parseFloat(m_price);
				f = (float)(Math.round((f-0.01f)*100))/100;
				if(f < Float.parseFloat(mStock_dt)){
					CommonUtils.toastMessage("卖出价格不能低于跌停价");
					et_price.setText(mStock_dt);
					return;
				}
				et_price.setText(f+"");
				break;
			case R.id.tv_simulation_sold_count_all:
				if(TextUtils.isEmpty(mgKmsl)){
					return;
				}
				count= (int) (Float.parseFloat(mgKmsl));
				count= changeCount(count);
				if(count == 0){
					CommonUtils.toastMessage("您的可卖数量不足100股");
					return;
				}
				et_count.setText(count+"");
				break;
			case R.id.tv_simulation_sold_count_half:
				if(TextUtils.isEmpty(mgKmsl)){
					return;
				}
				count= (int) (Float.parseFloat(mgKmsl)/2);
				count= changeCount(count);
				if(count == 0){
					CommonUtils.toastMessage("您的可卖数量不足100股");
					return;
				}
				et_count.setText(count+"");
				break;
			case R.id.tv_simulation_sold_count_three:
				if(TextUtils.isEmpty(mgKmsl)){
					return;
				}
				count= (int) (Float.parseFloat(mgKmsl)/3);
				count= changeCount(count);
				if(count == 0){
					CommonUtils.toastMessage("您的可卖数量不足100股");
					return;
				}
				et_count.setText(count+"");
				break;
			case R.id.tv_simulation_sold_count_four:
				if(TextUtils.isEmpty(mgKmsl)){
					return;
				}
				count= (int) (Float.parseFloat(mgKmsl)/4);
				count= changeCount(count);
				if(count == 0){
					CommonUtils.toastMessage("您的可卖数量不足100股");
					return;
				}
				et_count.setText(count+"");
				break;
		}
	}

	private void confirmSell() {
		if(Float.parseFloat(et_count.getText().toString().trim()) > Float.parseFloat(mgKmsl)){
			CommonUtils.toastMessage("卖出数量超出可卖数量");
			et_count.setText("");
			return;
		}

		NewsInternetRequest.simulationSellStock( tv_code.getText().toString(),tv_name.getText().toString()
				, et_count.getText().toString(),et_price.getText().toString(), new NewsInternetRequest.ForResultListener() {
					@Override
					public void onResponseMessage(String info) {
						if(info.equals("成功")){
							CommonUtils.toastMessage("卖出成功");
							et_count.setText("");
							visitInternet(mStockCode);
							SpTools.setBoolean(CommonUtils.getContext(), Constants.buy,true);//购买股票
						}else {
							CommonUtils.toastMessage(info);
						}
					}
				});
	}

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
		content.setText("确认卖出?");
		count.setText("数量："+et_count.getText().toString());
		price.setText("价格："+et_price.getText().toString());
		name.setText("名称："+tv_name.getText().toString()+"("+tv_code.getText().toString()+")");
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode){
			case 2:
				String stockName =  data.getExtras().getString("name");
				mStockCode = data.getExtras().getString("code");
				tv_name.setText(stockName);
				tv_code.setText(mStockCode);
				visitInternet(mStockCode);
				break;
		}
	}
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			//相当于Fragment的onResume
			if(!TextUtils.isEmpty(mStockCode)){
				visitInternet(mStockCode);
			}
		} else {
			//相当于Fragment的onPause
		}
	}
	public int changeCount(int num){
		if(num < 100){
			return 0;
		}else{
			int i = num / 100;
			DecimalFormat format = new DecimalFormat("0");
			return Integer.parseInt(format.format(i*100));
		}
	}
}