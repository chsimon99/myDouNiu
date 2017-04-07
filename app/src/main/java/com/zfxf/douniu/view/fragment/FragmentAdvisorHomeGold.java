package com.zfxf.douniu.view.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zfxf.douniu.R;
import com.zfxf.douniu.adapter.recycleView.AdvisorHomeGoldAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.view.RecycleViewDivider;
import com.zfxf.douniu.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FragmentAdvisorHomeGold extends BaseFragment {
    private View view;

    @BindView(R.id.rv_advisor_home_gold)
    PullLoadMoreRecyclerView mRecyclerView;
    private AdvisorHomeGoldAdapter mGoldAdapter;
    private List<String> datas = new ArrayList<String>();

    @Override
    public View initView(LayoutInflater inflater) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_advisor_home_gold, null);
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
        }
        if (mGoldAdapter == null) {
            mGoldAdapter = new AdvisorHomeGoldAdapter(getActivity(), datas);
        }

        mRecyclerView.setLinearLayout();
        mRecyclerView.setAdapter(mGoldAdapter);
        mRecyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL));
        mRecyclerView.setPullRefreshEnable(false);
        mRecyclerView.setPushRefreshEnable(false);
    }

    int num = 0;

    @Override
    public void initListener() {
        super.initListener();
        mGoldAdapter.setOnItemClickListener(new AdvisorHomeGoldAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View v, int positon) {
                Toast.makeText(CommonUtils.getContext(), "点击了" + positon, Toast.LENGTH_SHORT).show();
            }
        });
    }
}