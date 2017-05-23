package com.zfxf.douniu.view.fragment;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.ActivityReward;
import com.zfxf.douniu.adapter.recycleView.LiveLivingAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.bean.LivingContent;
import com.zfxf.douniu.bean.LivingContentDetailType;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * @author IMXU
 * @time   2017/5/3 13:34
 * @des    直播
 * 邮箱：butterfly_xu@sina.com
 *
*/

public class FragmentLiveLiving extends BaseFragment implements View.OnClickListener{
    private View view;

    @BindView(R.id.tv_live_living_title)
    TextView title;
    @BindView(R.id.iv_live_living_shang)
    ImageView iv_shang;
    @BindView(R.id.rv_live_living)
    PullLoadMoreRecyclerView mRecyclerView;
    private LiveLivingAdapter mLivingAdapter;
    private int mId;
    private int totlePage = 0;
    private int currentPage = 1;
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
        mId = getActivity().getIntent().getIntExtra("id", 0);
        return view;
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void initdata() {
        super.initdata();
        if(mId !=0){
            CommonUtils.showProgressDialog(getActivity(),"加载中……");
            visitInternet();
        }
    }
    private MediaPlayer mPlayer;
    private void visitInternet() {
        NewsInternetRequest.getLivingInformation(currentPage + "", 0, mId, new NewsInternetRequest.ForResultLivingInfoListener() {
            @Override
            public void onResponseMessage(LivingContent content) {
                totlePage = Integer.parseInt(content.total);
                title.setText(content.zt_name);
                if (totlePage > 0 && currentPage <= totlePage){
                    if(currentPage == 1){
                        if (mLivingAdapter == null) {
                            mLivingAdapter = new LiveLivingAdapter(getActivity(), content);
                        }

                        mRecyclerView.setLinearLayout();
                        mRecyclerView.setAdapter(mLivingAdapter);
                        mRecyclerView.setFooterViewText("加载更多……");

                        mLivingAdapter.setOnItemClickListener(new LiveLivingAdapter.MyItemClickListener() {
                            int currentPos = -1;
                            ImageView currentView;
                            @Override
                            public void onItemClick(View v, int positon, LivingContentDetailType type, final ImageView view) {
                                if(type.type.equals("1")){
                                    if(currentPos == positon){
                                        stopAnimation(view);
                                        mPlayer.stop();
                                        mPlayer.release();
                                        currentPos = -1;
                                        mPlayer = null;
                                    }else{
                                        if(mPlayer != null){
                                            stopAnimation(currentView);
                                            mPlayer.stop();
                                            mPlayer.release();
                                            mPlayer = null;
                                        }
                                        currentPos = positon;
                                        currentView = view;

                                        mPlayer = MediaPlayer.create(CommonUtils.getContext(), Uri.parse(type.url));

                                        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                            @Override
                                            public void onCompletion(MediaPlayer mp) {
                                                stopAnimation(currentView);
                                                mPlayer.release();
                                                mPlayer = null;
                                            }
                                        });
                                        startAnimation(view);
                                        mPlayer.start();
                                    }
                                }
                            }
                        });
                    }else {
                        mLivingAdapter.addDatas(content);
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
                    currentPage++;
                    CommonUtils.dismissProgressDialog();
                }else {
                    CommonUtils.dismissProgressDialog();
                    return;
                }
            }
        });
    }
    private AnimationDrawable voiceAnimation = null;
    public void stopAnimation(ImageView view){
        voiceAnimation.stop();
        view.setImageResource(R.drawable.icon_sound);
    }
    public void startAnimation(ImageView view){
        view.setImageResource(R.drawable.voiceanimation);
        voiceAnimation = (AnimationDrawable) view.getDrawable();
        voiceAnimation.start();
    }
    @Override
    public void initListener() {
        super.initListener();
        iv_shang.setOnClickListener(this);
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
                if (currentPage > totlePage) {
                    Toast.makeText(CommonUtils.getContext(), "没有数据了", Toast.LENGTH_SHORT).show();
                    mRecyclerView.postDelayed(new Runnable() {//防止滑动过快，loading界面显示太快
                        @Override
                        public void run() {
                            mRecyclerView.setPullLoadMoreCompleted();
                        }
                    }, 200);
                    return;
                }
                visitInternet();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_live_living_shang:
                Intent intent = new Intent(getActivity(),ActivityReward.class);
                startActivity(intent);
                getActivity().overridePendingTransition(0,0);
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mPlayer !=null){
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }
}