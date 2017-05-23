package com.zfxf.douniu.view.fragment;

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
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.SpTools;
import com.zfxf.douniu.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;

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
    private int totlePage = 0;
    private int currentPage = 1;

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
            visitInternet();
        }
    }
    int maxLine=0;
    private void visitInternet() {
        NewsInternetRequest.getLivingInteractInformation(currentPage + "", mId, new NewsInternetRequest.ForResultLivingInteractInfoListener() {
            @Override
            public void onResponseMessage(final LivingContent content) {
                totlePage = Integer.parseInt(content.total);
                if (totlePage > 0 && currentPage <= totlePage){
                    if(currentPage == 1){
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
                    currentPage++;
                    CommonUtils.dismissProgressDialog();
                }else {
                    CommonUtils.dismissProgressDialog();
                    return;
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
                if(currentPage > totlePage){
                    Toast.makeText(CommonUtils.getContext(),"没有数据了",Toast.LENGTH_SHORT).show();
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
                SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
                String nowTime = df.format(new Date());
                LivingInteract interact = new LivingInteract();
                interact.setUd_nickname(SpTools.getString(getContext(), Constants.nickname,"请您去设置昵称"));
                interact.setZp_date(nowTime);
                interact.setHeadImg(SpTools.getString(getContext(), Constants.imgurl,""));
                String contents = et_interaction.getText().toString();
                interact.setZp_pl(contents);
                interact.setRole("1");
                int count = mInteractionAdapter.getItemCount();
                mInteractionAdapter.addNewDatas(interact);
                mInteractionAdapter.notifyItemInserted(count);
                mRecyclerView.getRecyclerView().smoothScrollToPosition(mInteractionAdapter.getItemCount());//显示到最底部
                et_interaction.setText("");

                NewsInternetRequest.sendInteractInformation(contents
                        , mId, new NewsInternetRequest.ForResultSendInteractInfoListener() {
                    @Override
                    public void onResponseMessage(LivingSendMsg sendMsg) {
//                        if(sendMsg !=null){
//                            et_interaction.setText("");
//                            LivingInteract interact = new LivingInteract();
//                            interact.setUd_nickname(sendMsg.zp_ud_nickname);
//                            interact.setZp_date(sendMsg.zp_date);
//                            interact.setHeadImg(sendMsg.headImg);
//                            interact.setZp_pl(sendMsg.zp_pl);
//                            interact.setRole("1");
//                            int count = mInteractionAdapter.getItemCount();
//                            mInteractionAdapter.addNewDatas(interact);
//                            mInteractionAdapter.notifyItemInserted(count);
//                            mRecyclerView.getRecyclerView().smoothScrollToPosition(mInteractionAdapter.getItemCount());//显示到最底部
//                        }
                    }
                });
            }
        });
    }
}