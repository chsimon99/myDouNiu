package com.zfxf.douniu.view.fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zfxf.douniu.R;
import com.zfxf.douniu.adapter.recycleView.LiveInteractionAdapter;
import com.zfxf.douniu.base.BaseFragment;
import com.zfxf.douniu.bean.LivingContent;
import com.zfxf.douniu.bean.LivingInteract;
import com.zfxf.douniu.bean.LivingSendMsg;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

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
    private LiveInteractionAdapter mInteractionAdapter;
    private int mId;
    private int type = 0;
    private int lastID = 0;//列表显示的评论中最上面的评论id
    private int firstID = 0;//列表显示的评论中最下面的评论id
    private String earliestID = "0";//是否有历史数据 0有1没有
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
    int maxLine=0;
    private void visitInternet(int refreshId) {
        NewsInternetRequest.getLivingInteractInformation(type + "", mId, refreshId,new NewsInternetRequest.ForResultLivingInteractInfoListener() {
            @Override
            public void onResponseMessage(final LivingContent content) {
                if (earliestID.equals("0")){
                    if(type == 0 && lastID == 0){
                        if(mInteractionAdapter == null){
                            mInteractionAdapter = new LiveInteractionAdapter(getActivity(),content.pl_list);
                        }

                        mRecyclerView.setLinearLayout();
                        mRecyclerView.setAdapter(mInteractionAdapter);
                        mRecyclerView.setPushRefreshEnable(false);//禁止上拉加载
                        mRecyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                mRecyclerView.getRecyclerView().smoothScrollToPosition(mInteractionAdapter.getItemCount());//显示到最底部
                            }
                        });
                        type = 1;
                        firstID = Integer.parseInt(content.pl_list.get(content.pl_list.size()-1).zp_id);
                    }else{
                        final int itemPosition = mRecyclerView.getLinearLayoutManager().findLastVisibleItemPosition();
                        maxLine = content.pl_list.size() > maxLine ? content.pl_list.size() : maxLine;
                        mInteractionAdapter.addDatas(content.pl_list);
                        mRecyclerView.post(new Runnable() {
                            @Override
                            public void run() {
//                                mInteractionAdapter.notifyItemRangeChanged(0,maxLine);
                                mInteractionAdapter.notifyDataSetChanged();
                                mRecyclerView.getRecyclerView().scrollToPosition(itemPosition+content.pl_list.size());
                                mRecyclerView.setPullLoadMoreCompleted();
                            }
                        });
                    }
                    lastID = Integer.parseInt(content.pl_list.get(0).zp_id);
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

    @Override
    public void initListener() {
        super.initListener();
        mRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
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
                            LivingInteract interact = new LivingInteract();
                            interact.setUd_nickname(sendMsg.zp_ud_nickname);
                            interact.setZp_date(sendMsg.zp_date);
                            interact.setHeadImg(sendMsg.headImg);
                            interact.setZp_pl(sendMsg.zp_pl);
                            interact.setRole("1");
                            int count = mInteractionAdapter.getItemCount();
                            mInteractionAdapter.addNewDatas(interact);
                            mInteractionAdapter.notifyItemInserted(count);
                            mRecyclerView.getRecyclerView().smoothScrollToPosition(mInteractionAdapter.getItemCount());//显示到最底部
                        }
                        CommonUtils.dismissProgressDialog();
                    }
                });
            }
        });
    }
}