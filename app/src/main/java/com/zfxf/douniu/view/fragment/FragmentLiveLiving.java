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
import com.zfxf.douniu.activity.login.ActivityLogin;
import com.zfxf.douniu.adapter.recycleView.LiveLivingAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.bean.LivingContent;
import com.zfxf.douniu.bean.LivingContentDetailType;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.SpTools;
import com.zfxf.douniu.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;

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
        alwaysRefresh();
    }

    private AutoScrollTask mTask;
    private void alwaysRefresh() {
        mTask = new AutoScrollTask();
        mTask.start();
    }
    class AutoScrollTask implements Runnable {
        public void start() {/**开始轮播*/
            CommonUtils.postTaskDelay(this, 5000);
        }
        public void stop() { /**结束轮播*/
            CommonUtils.removeTask(this);
        }
        @Override
        public void run() {
            if(!isRefresh){
                type = 0;
                earliestID = "0";
                visitInternet(firstID);
                if(SpTools.getBoolean(CommonUtils.getContext(), Constants.error,false)){//网络错误重连
                    SpTools.setBoolean(CommonUtils.getContext(),Constants.error,false);
                    type = 0;
                    visitInternet(firstID);
                }
            }
            start();
        }
    }

    private MediaPlayer mPlayer;
    private void visitInternet(int refreshId) {
        isRefresh = true;
        NewsInternetRequest.getLivingInformation(type + "", mId, refreshId, new NewsInternetRequest.ForResultLivingInfoListener() {
            @Override
            public void onResponseMessage(final LivingContent content) {
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
                            public void onItemClick(View v, int positon, final LivingContentDetailType type, final ImageView view) {
                                if(type.type.equals("1")){
                                    if(currentPos == positon){
//                                        stopAnimation(view);
                                        if(mPlayer!=null){
                                            mPlayer.stop();
                                            mPlayer.release();
                                        }
                                        currentPos = -1;
                                        mPlayer = null;
                                        type.setShow(false);
                                    }else{
                                        if(mPlayer != null){
//                                            stopAnimation(currentView);
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
                                                type.setShow(false);
                                                stopAnimation(currentView);
                                                mPlayer.release();
                                                mPlayer = null;
                                            }
                                        });
                                        type.setShow(true);
                                        mLivingAdapter.notifyDataSetChanged();
//                                        startAnimation(view);
                                        mPlayer.start();
                                    }
                                }
                            }
                        });
                        type = 1;
                        if(content.context_list.size() > 0){
                            firstID = Integer.parseInt(content.context_list.get(0).zc_id);
                        }
                        isRefresh = false;
                    }else {
                        if(type == 0){
                            if(content.context_list.size() != 0){
                                final int size = content.context_list.size();
                                mLivingAdapter.refreshDatas(content);
                                firstID = Integer.parseInt(content.context_list.get(0).zc_id);
                                mRecyclerView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mLivingAdapter.notifyItemRangeChanged(0,size);
                                    }
                                });
                            }
                            isRefresh = false;
                            if(content.status.equals("0")){//在非直播状态，不进行循环刷新
                                isRefresh = true;
                            }
                            return;
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
                            isRefresh = false;
                        }
                    }
                    if(content.context_list.size() > 0){
                        lastID = Integer.parseInt(content.context_list.get(content.context_list.size()-1).zc_id);
                    }
                }
                CommonUtils.dismissProgressDialog();
                if(content.status.equals("0")){//在非直播状态，不进行循环刷新
                    isRefresh = true;
                }
                if(!TextUtils.isEmpty(content.is_earliest)){
                    earliestID = content.is_earliest;
                }
            }
        });
    }
    private AnimationDrawable voiceAnimation = null;
    public void stopAnimation(ImageView view){
        if(voiceAnimation!=null){
            voiceAnimation.stop();
        }
        view.setImageResource(R.drawable.icon_sound);
    }
    public void startAnimation(ImageView view){
        view.setImageResource(R.drawable.voiceanimation);
        voiceAnimation = (AnimationDrawable) view.getDrawable();
        voiceAnimation.start();
    }
    private boolean isRefresh = false;//是否在刷新
    @Override
    public void initListener() {
        super.initListener();
        iv_shang.setOnClickListener(this);
        mRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                type = 0;
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
                type = 1;
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
                if(!SpTools.getBoolean(CommonUtils.getContext(), Constants.isLogin,false)){
                    Intent intent = new Intent(getActivity(), ActivityLogin.class);
                    getActivity().startActivity(intent);
                    getActivity().overridePendingTransition(0,0);
                    return;
                }
                Intent intent = new Intent(getActivity(),ActivityReward.class);
                intent.putExtra("id",mId);
                intent.putExtra("sx_id",getActivity().getIntent().getStringExtra("sx_id"));
                intent.putExtra("type","直播");
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().getOkHttpClient().dispatcher().cancelAll();//关闭网络请求
        if(mTask!=null){
            mTask.stop();
        }
        mTask = null;
        if(mPlayer !=null){
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //相当于Fragment的onResume
            if(mTask!=null){
                mTask.start();
            }
            CommonUtils.logMes("-------直播---onResume---");
        } else {
            //相当于Fragment的onPause
            if(mTask!=null){
                mTask.stop();
            }
            CommonUtils.logMes("-------直播---onPause---");
        }
    }
}