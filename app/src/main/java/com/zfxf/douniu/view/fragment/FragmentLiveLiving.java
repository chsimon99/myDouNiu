package com.zfxf.douniu.view.fragment;

import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zfxf.douniu.R;
import com.zfxf.douniu.adapter.recycleView.LiveLivingAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FragmentLiveLiving extends BaseFragment {
    private View view;

    @BindView(R.id.tv_live_living_title)
    TextView title;
    @BindView(R.id.rv_live_living)
    PullLoadMoreRecyclerView mRecyclerView;
    private LiveLivingAdapter mLivingAdapter;
    private List<String> datas = new ArrayList<String>();

    @Override
    public View initView(LayoutInflater inflater) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_live_living, null);
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
        if (mLivingAdapter == null) {
            mLivingAdapter = new LiveLivingAdapter(getActivity(), datas);
        }

        mRecyclerView.setLinearLayout();
        mRecyclerView.setAdapter(mLivingAdapter);
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
                mLivingAdapter.addDatas("");
                mRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        mLivingAdapter.notifyDataSetChanged();
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

        mLivingAdapter.setOnItemClickListener(new LiveLivingAdapter.MyItemClickListener() {
            MediaPlayer mPlayer = MediaPlayer.create(CommonUtils.getContext(), R.raw.by2);
            @Override
            public void onItemClick(View v, int positon) {
                if(positon == 1){
                    mPlayer.start();
                }else{
                    mPlayer.stop();
                    try {
                        mPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}