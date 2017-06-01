package com.zfxf.douniu.view.fragment;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.TextUtils;
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
    private int type = 0;
    private int lastID = 0;//列表显示的直播中最下面的直播id
    private int firstID = 0;//列表显示的评论中最上面的直播id
    private String earliestID = "0";//是否有历史数据 0有1没有
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
            visitInternet(lastID);
        }
    }
    private MediaPlayer mPlayer;
    private void visitInternet(int refreshId) {
        NewsInternetRequest.getLivingInformation(type + "", mId, refreshId, new NewsInternetRequest.ForResultLivingInfoListener() {
            @Override
            public void onResponseMessage(LivingContent content) {
                title.setText(content.zt_name);
                if (earliestID.equals("0")){
                    if(type == 0 && lastID == 0){
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
                        type = 1;
                        firstID = Integer.parseInt(content.context_list.get(0).zc_id);
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
                    lastID = Integer.parseInt(content.context_list.get(content.context_list.size()-1).zc_id);
                    CommonUtils.dismissProgressDialog();
                }else {
                    CommonUtils.dismissProgressDialog();
                    return;
                }
                if(!TextUtils.isEmpty(content.is_earliest)){
                    earliestID = content.is_earliest;
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
                visitInternet(firstID);
                mRecyclerView.postDelayed(new Runnable() {//防止滑动过快，loading界面显示太快
                    @Override
                    public void run() {
                        mRecyclerView.setPullLoadMoreCompleted();
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore() {
                if (earliestID.equals("1")) {
                    Toast.makeText(CommonUtils.getContext(), "没有数据了", Toast.LENGTH_SHORT).show();
                    mRecyclerView.postDelayed(new Runnable() {//防止滑动过快，loading界面显示太快
                        @Override
                        public void run() {
                            mRecyclerView.setPullLoadMoreCompleted();
                        }
                    }, 200);
                    return;
                }
                visitInternet(lastID);
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