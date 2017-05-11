package com.zfxf.douniu.view.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.ActivitysimulationQueryHistory;
import com.zfxf.douniu.activity.ActivitysimulationQueryToday;
import com.zfxf.douniu.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author IMXU
 * @time   2017/5/3 13:31
 * @des    模拟炒股查询
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class FragmentSimulationStockQuery extends BaseFragment implements View.OnClickListener{
	private View view;

	@BindView(R.id.rl_simulation_query_history)
	RelativeLayout history;
	@BindView(R.id.rl_simulation_query_today)
	RelativeLayout today;
	@Override
	public View initView(LayoutInflater inflater) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_simulation_query, null);
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
		today.setOnClickListener(this);
		history.setOnClickListener(this);
	}
	Intent intent;
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.rl_simulation_query_today:
				intent = new Intent(getActivity(), ActivitysimulationQueryToday.class);
				startActivity(intent);
				getActivity().overridePendingTransition(0,0);
				break;
			case R.id.rl_simulation_query_history:
				intent = new Intent(getActivity(), ActivitysimulationQueryHistory.class);
				startActivity(intent);
				getActivity().overridePendingTransition(0,0);
				break;
		}
		intent = null;
	}
}