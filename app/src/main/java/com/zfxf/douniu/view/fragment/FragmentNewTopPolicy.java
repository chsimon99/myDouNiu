package com.zfxf.douniu.view.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.ActivityInformation;
import com.zfxf.douniu.adapter.recycleView.NewTopPolicyAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.view.RecycleViewDivider;
import com.zfxf.douniu.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FragmentNewTopPolicy extends BaseFragment {
    private View view;

    @BindView(R.id.rv_fragment_new_top_policy)
    PullLoadMoreRecyclerView mRecyclerView;
    private NewTopPolicyAdapter mNewTopPolicyAdapter;
    private List<String> datas = new ArrayList<String>();
    private RecycleViewDivider mDivider;

    @Override
    public View initView(LayoutInflater inflater) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_new_top_policy, null);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void initdata() {
        super.initdata();
        if (datas.size() == 0) {
            datas.add("1");
            datas.add("2");
            datas.add("3");
            datas.add("4");
            datas.add("4");
            datas.add("4");
            datas.add("4");
        }
        if (mNewTopPolicyAdapter == null) {
            mNewTopPolicyAdapter = new NewTopPolicyAdapter(getActivity(), datas);
        }

        mRecyclerView.setLinearLayout();
        mRecyclerView.setAdapter(mNewTopPolicyAdapter);
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
                }, 1000);
            }

            @Override
            public void onLoadMore() {
                if (num > 1) {
                    Toast.makeText(CommonUtils.getContext(), "没有数据了", Toast.LENGTH_SHORT).show();
                    mRecyclerView.setPullLoadMoreCompleted();
                    return;
                }
                num++;
                List<String> newdatas = new ArrayList<String>();
                newdatas.add("7");
                mNewTopPolicyAdapter.addDatas("");
                mRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        mNewTopPolicyAdapter.notifyDataSetChanged();
                    }
                });
                mRecyclerView.postDelayed(new Runnable() {//防止滑动过快，loading界面显示太快
                    @Override
                    public void run() {
                        mRecyclerView.setPullLoadMoreCompleted();
                    }
                }, 1000);
            }
        });

        mNewTopPolicyAdapter.setOnItemClickListener(new NewTopPolicyAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View v, int positon) {
                Intent intent = new Intent(CommonUtils.getContext(), ActivityInformation.class);
                startActivity(intent);
                getActivity().overridePendingTransition(0,0);
            }
        });
    }
}