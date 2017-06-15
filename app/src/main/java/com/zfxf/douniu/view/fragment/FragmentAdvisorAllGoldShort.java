package com.zfxf.douniu.view.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.ActivityGoldPond;
import com.zfxf.douniu.adapter.recycleView.AdvisorHomeGoldAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.bean.SimulationResult;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.view.RecycleViewDivider;
import com.zfxf.douniu.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author IMXU
 * @time   2017/5/3 13:37
 * @des    金股池 短线
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class FragmentAdvisorAllGoldShort extends BaseFragment {
    private View view;

    @BindView(R.id.rv_advisor_home_gold)
    PullLoadMoreRecyclerView mRecyclerView;
    private AdvisorHomeGoldAdapter mGoldAdapter;
    private RecycleViewDivider mDivider;
    private boolean isShow = false;

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
        if(!isShow){
            isShow = true;
            CommonUtils.showProgressDialog(getActivity(),"加载中……");
            visitInternet();
        }
    }

    private void visitInternet() {
        NewsInternetRequest.getGoldPondListInformation("", 0, new NewsInternetRequest.ForResultGoldPoneShortStockListener() {
            @Override
            public void onResponseMessage(SimulationResult result) {
                if(result.dn_jgc.size()>0){
                    if (mGoldAdapter == null) {
                        mGoldAdapter = new AdvisorHomeGoldAdapter(getActivity(), result.dn_jgc);
                    }

                    mRecyclerView.setLinearLayout();
                    mRecyclerView.setAdapter(mGoldAdapter);
                    if(mDivider == null){
                        mDivider = new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL);
                        mRecyclerView.addItemDecoration(mDivider);
                    }
//                    mRecyclerView.setPullRefreshEnable(false);//禁止下拉刷新
                    mRecyclerView.setPushRefreshEnable(false);//禁止上拉加载
                    mGoldAdapter.setOnItemClickListener(new AdvisorHomeGoldAdapter.MyItemClickListener() {
                        @Override
                        public void onItemClick(View v, int positon) {
                            Intent intent = new Intent(CommonUtils.getContext(), ActivityGoldPond.class);
                            startActivity(intent);
                            getActivity().overridePendingTransition(0,0);
                        }
                    });
                }
                CommonUtils.dismissProgressDialog();
            }
        },null);
    }

    @Override
    public void initListener() {
        super.initListener();
        mRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                mGoldAdapter = null;
                CommonUtils.showProgressDialog(getActivity(),"加载中……");
                visitInternet();
                mRecyclerView.postDelayed(new Runnable() {//防止滑动过快，loading界面显示太快
                    @Override
                    public void run() {
                        mRecyclerView.setPullLoadMoreCompleted();
                    }
                },1000);
            }

            @Override
            public void onLoadMore() {

            }
        });
    }
}