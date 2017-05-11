package com.zfxf.douniu.view.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zfxf.douniu.R;
import com.zfxf.douniu.adapter.recycleView.AdvisorHomeDirectAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.bean.TestBean;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.view.RecycleViewDivider;
import com.zfxf.douniu.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author IMXU
 * @time   2017/5/3 13:11
 * @des    首席个人主页直播课
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class FragmentAdvisorHomeDirect extends BaseFragment {
	private View view;

	@BindView(R.id.rv_advisor_home_direct)
	PullLoadMoreRecyclerView mRecyclerView;
	private AdvisorHomeDirectAdapter mHomeDirectAdapter;
	private List<TestBean> datas = new ArrayList<TestBean>();
	private RecycleViewDivider mDivider;

	@Override
	public View initView(LayoutInflater inflater) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_advisor_home_direct, null);
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
		if(datas.size() == 0){
			for (int i =1;i<11;i++){
				TestBean testBean = new TestBean();
				testBean.setStr(i+"");
				if(i == 1){
					testBean.setFlag(true);
				}else{
					testBean.setFlag(false);
				}
				datas.add(testBean);
			}
		}
		if(mHomeDirectAdapter == null){
			mHomeDirectAdapter = new AdvisorHomeDirectAdapter(getActivity(),datas);
		}

		mRecyclerView.setLinearLayout();
		mRecyclerView.setAdapter(mHomeDirectAdapter);
		if(mDivider == null){
			mDivider = new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL);
			mRecyclerView.addItemDecoration(mDivider);
		}
		mRecyclerView.setFooterViewText("加载更多……");
	}
	int num = 0;
	@Override
	public void initListener() {
		super.initListener();
		mRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
			@Override
			public void onRefresh() {
				mRecyclerView.postDelayed(new Runnable() {//防止滑动过快，loading界面显示太快
					@Override
					public void run() {
						mRecyclerView.setPullLoadMoreCompleted();
					}
				},1000);
			}

			@Override
			public void onLoadMore() {
				if(num > 1){
					Toast.makeText(CommonUtils.getContext(),"没有数据了",Toast.LENGTH_SHORT).show();
					mRecyclerView.setPullLoadMoreCompleted();
					return;
				}
				num++;
				TestBean testBean = new TestBean();
				testBean.setStr("11");
				mHomeDirectAdapter.addDatas(testBean);
				mRecyclerView.post(new Runnable() {
					@Override
					public void run() {
						mHomeDirectAdapter.notifyDataSetChanged();
					}
				});
				mRecyclerView.postDelayed(new Runnable() {//防止滑动过快，loading界面显示太快
					@Override
					public void run() {
						mRecyclerView.setPullLoadMoreCompleted();
					}
				},1000);
			}
		});

		mHomeDirectAdapter.setOnItemClickListener(new AdvisorHomeDirectAdapter.MyItemClickListener() {
			@Override
			public void onItemClick(View v, int positon) {
				Toast.makeText(CommonUtils.getContext(),"点击了"+positon,Toast.LENGTH_SHORT).show();
			}
		});
	}
}