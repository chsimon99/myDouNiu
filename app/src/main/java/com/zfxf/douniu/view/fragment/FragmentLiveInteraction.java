package com.zfxf.douniu.view.fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zfxf.douniu.R;
import com.zfxf.douniu.adapter.recycleView.LiveInteractionAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.bean.LivingContent;
import com.zfxf.douniu.bean.LivingSendMsg;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.SpTools;
import com.zfxf.douniu.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FragmentLiveInteraction extends BaseFragment {
    private View view;
    @BindView(R.id.et_live_interaction)
    EditText et_interaction;
    @BindView(R.id.tv_live_interaction_send)
    TextView tv_send;

    @BindView(R.id.rv_live_interaction)
    PullLoadMoreRecyclerView mRecyclerView;
    @BindView(R.id.ll_live_interaction)
    LinearLayout mLinearLayout;
    private LiveInteractionAdapter mInteractionAdapter;
    private int mId;
    private int type = 0;
    private int lastID = 0;//列表显示的评论中最上面的评论id
    private int firstID = 0;//列表显示的评论中最下面的评论id
    private String earliestID = "0";//是否有历史数据 0有1没有
    private int mStatus;

    @Override
    public View initView(LayoutInflater inflater) {
        if (view == null) {
            view = inflater.inflate(R.layout.activity_live_interaction, null);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        ButterKnife.bind(this, view);
        mId = getActivity().getIntent().getIntExtra("id", 0);
        mStatus = getActivity().getIntent().getIntExtra("status", 1);
        return view;
    }

    @Override
    public void init() {
        super.init();
    }
    @Override
    public void initdata() {
        super.initdata();
        if(mStatus == 0){
            mLinearLayout.setVisibility(View.GONE);
        }
        if(mId !=0){
            CommonUtils.showProgressDialog(getActivity(),"加载中……");
            visitInternet(lastID);
        }
        alwaysRefresh();
    }
    private AutoScrollTask mTask;
    private boolean isRefresh = false;//是否在刷新
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
    int maxLine=0;
    private void visitInternet(int refreshId) {
        isRefresh = true;
        NewsInternetRequest.getLivingInteractInformation(type + "", mId, refreshId,new NewsInternetRequest.ForResultLivingInteractInfoListener() {
            @Override
            public void onResponseMessage(final LivingContent content) {
                if (earliestID.equals("0")){
                    if(type == 0 && lastID == 0){
                        if(mInteractionAdapter == null){
                            mInteractionAdapter = new LiveInteractionAdapter(getActivity(),content.pl_list);
                        }

                        mRecyclerView.setLinearLayout();
//                        mRecyclerView.getLinearLayoutManager().setReverseLayout(true);
                        mRecyclerView.setAdapter(mInteractionAdapter);
                        mRecyclerView.setPushRefreshEnable(false);//禁止上拉加载
                        mRecyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                mRecyclerView.getRecyclerView().smoothScrollToPosition(mInteractionAdapter.getItemCount());//显示到最底部
                            }
                        });
                        type = 1;
                        if(content.pl_list.size()>0){
                            firstID = Integer.parseInt(content.pl_list.get(content.pl_list.size()-1).zp_id);
                        }
                        isRefresh = false;
                    }else{
                        if(type == 0){
                            if(content.pl_list.size() != 0){
                                final int count = mInteractionAdapter.getItemCount();
                                mInteractionAdapter.addALLDatas(content.pl_list);
                                firstID = Integer.parseInt(content.pl_list.get(content.pl_list.size()-1).zp_id);
                                final int lastPosition = mRecyclerView.getLinearLayoutManager().findLastVisibleItemPosition();
                                final int pos = lastPosition - mRecyclerView.getLinearLayoutManager().findFirstVisibleItemPosition();
                                mRecyclerView.post(new Runnable() {
                                    @Override
                                    public void run() {
//                                        mInteractionAdapter.notifyItemRangeChanged(count-1,mInteractionAdapter.getItemCount());//部分更新在用户看历史数据的时候不起作用
                                        mInteractionAdapter.notifyDataSetChanged();
                                        if((mInteractionAdapter.getItemCount()-lastPosition)<pos){//如果观看的页数在最后几条时，更新后直接显示更新内容
                                            mRecyclerView.getRecyclerView().smoothScrollToPosition(mInteractionAdapter.getItemCount());//显示到最底部
                                        }
                                    }
                                });
                            }
                            isRefresh = false;
                            if(mStatus == 0){//在非直播状态，不进行循环刷新
                                isRefresh = true;
                            }
                            return;
                        }else {
                            final int itemPosition = mRecyclerView.getLinearLayoutManager().findLastVisibleItemPosition();
                            maxLine = content.pl_list.size() > maxLine ? content.pl_list.size() : maxLine;
                            mInteractionAdapter.addDatas(content.pl_list);
                            mRecyclerView.post(new Runnable() {
                                @Override
                                public void run() {
//                                    mInteractionAdapter.notifyItemRangeChanged(0,maxLine);
                                    mInteractionAdapter.notifyDataSetChanged();
                                    mRecyclerView.getRecyclerView().scrollToPosition(itemPosition+content.pl_list.size()-1);
                                    mRecyclerView.setPullLoadMoreCompleted();
                                }
                            });
                            isRefresh = false;
                        }
                    }
                    if(content.pl_list.size()>0){
                        lastID = Integer.parseInt(content.pl_list.get(0).zp_id);
                    }
                }
                CommonUtils.dismissProgressDialog();
                if(mStatus == 0){//在非直播状态，不进行循环刷新
                    isRefresh = true;
                }
                if(!TextUtils.isEmpty(content.is_earliest)){
                    earliestID = content.is_earliest;
                }
            }
        });
    }

    @Override
    public void initListener() {
        super.initListener();
        mRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                type = 1;
                if(earliestID.equals("1")){
                    Toast.makeText(CommonUtils.getContext(),"没有数据了",Toast.LENGTH_SHORT).show();
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

            @Override
            public void onLoadMore() {

            }
        });
        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mInteractionAdapter == null){
                    return;
                }
                CommonUtils.showProgressDialog(getActivity(),"发表评论中……");
                String contents = et_interaction.getText().toString();
//                SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
//                String nowTime = df.format(new Date());
//                LivingInteract interact = new LivingInteract();
//                interact.setUd_nickname(SpTools.getString(getContext(), Constants.nickname,"请您去设置昵称"));
//                interact.setZp_date(nowTime);
//                interact.setHeadImg(SpTools.getString(getContext(), Constants.imgurl,""));
//                interact.setZp_pl(contents);
//                interact.setRole("1");
//                int count = mInteractionAdapter.getItemCount();
//                mInteractionAdapter.addNewDatas(interact);
//                mInteractionAdapter.notifyItemInserted(count);
//                mRecyclerView.getRecyclerView().smoothScrollToPosition(mInteractionAdapter.getItemCount());//显示到最底部
//                et_interaction.setText("");

                NewsInternetRequest.sendInteractInformation(contents
                        , mId, new NewsInternetRequest.ForResultSendInteractInfoListener() {
                    @Override
                    public void onResponseMessage(LivingSendMsg sendMsg) {
                        if(sendMsg !=null){
                            et_interaction.setText("");
//                            LivingInteract interact = new LivingInteract();
//                            interact.setUd_nickname(sendMsg.zp_ud_nickname);
//                            interact.setZp_date(sendMsg.zp_date);
//                            interact.setHeadImg(sendMsg.headImg);
//                            interact.setZp_pl(sendMsg.zp_pl);
//                            interact.setRole("1");
//                            int count = mInteractionAdapter.getItemCount();
//                            mInteractionAdapter.addNewDatas(interact);
//                            mRecyclerView.getRecyclerView().smoothScrollToPosition(mInteractionAdapter.getItemCount());//显示到最底部
                            type = 0;
                            lastID = 0;
                            earliestID = "0";
                            mInteractionAdapter = null;
                            visitInternet(lastID);
                        }
                        CommonUtils.dismissProgressDialog();
                    }
                });
            }
        });
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //相当于Fragment的onResume
            if(mTask!=null){
                mTask.start();
            }
            CommonUtils.logMes("-------论坛---onResume---");
        } else {
            //相当于Fragment的onPause
            if(mTask!=null){
                mTask.stop();
            }
            CommonUtils.logMes("-------论坛---onpausee---");
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
    }
}