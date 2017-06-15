package com.zfxf.douniu.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.login.ActivityLogin;
import com.zfxf.douniu.adapter.recycleView.HeadlineDetailAdapter;
import com.zfxf.douniu.bean.CommentInformationResult;
import com.zfxf.douniu.bean.CommentResult;
import com.zfxf.douniu.bean.DaShangInformationResult;
import com.zfxf.douniu.bean.NewsInfomationResult;
import com.zfxf.douniu.bean.OtherResult;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.SpTools;
import com.zfxf.douniu.view.FullyLinearLayoutManager;
import com.zfxf.douniu.view.RecycleViewDivider;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * @author IMXU
 * @time   2017/5/3 17:12
 * @des    头条详情
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class ActivityHeadLineDetail extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.iv_base_share)
    ImageView share;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.iv_headline_detail_img)
    ImageView img;
    @BindView(R.id.rv_headline_detail)
    RecyclerView mRecyclerView;
    @BindView(R.id.ll_headline_detail_shang)
    LinearLayout ll_shang;
    @BindView(R.id.ll_headline_detail_shafa)
    LinearLayout ll_sofa;//评论为0的时候显示沙发
    @BindView(R.id.ll_headline_detail_zan)
    LinearLayout ll_zan;//点赞
    @BindView(R.id.tv_headline_detail_name)
    TextView name;//新闻发布者
    @BindView(R.id.tv_headline_detail_title)
    TextView detail_title;//新闻标题
    @BindView(R.id.tv_headline_detail_detail)
    TextView detail_detail;//新闻详情
    @BindView(R.id.tv_headline_detail_time)
    TextView time;//新闻发布时间
    @BindView(R.id.tv_headline_detail_count)
    TextView count;//观看次数
    @BindView(R.id.tv_headline_detail_count_zan)
    TextView count_zan;//点赞数量
    @BindView(R.id.tv_headline_detail_count_shang)
    TextView count_shang;//打赏数量
    @BindView(R.id.tv_headline_detail_profile)
    TextView profile;//简介
    @BindView(R.id.tv_headline_detail_comment)
    TextView comment;
    @BindView(R.id.tv_headline_detail_commentcount)
    TextView comment_count;//评论数量
    @BindView(R.id.tv_headline_detail_jianjie)
    TextView jianjie;

    @BindView(R.id.et_headline_detail)
    EditText et_comment;

    private LinearLayoutManager mAdvisorManager;
    private HeadlineDetailAdapter mDetailAdapter;
    private RecycleViewDivider mDivider;
    private int mNewsinfoId;
    private String mIsDz;
    private int mPlTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_headline_detail);
        ButterKnife.bind(this);
        title.setText("头条详情");

        edit.setVisibility(View.INVISIBLE);
        share.setVisibility(View.VISIBLE);
        mNewsinfoId = getIntent().getIntExtra("newsinfoId", 0);
        detail_title.getPaint().setFakeBoldText(true);//加粗
        jianjie.getPaint().setFakeBoldText(true);//加粗
        comment.getPaint().setFakeBoldText(true);//加粗
        initData();
        initListener();
    }

    private void initData() {
        if(mNewsinfoId == 0){
            CommonUtils.toastMessage("获取头条详情失败，请重试");
            return;
        }
        CommonUtils.showProgressDialog(this,"加载中……");
        visitInternet();
    }

    private void visitInternet() {
        NewsInternetRequest.getNewsInformation(mNewsinfoId, new NewsInternetRequest.ForResultNewsInfoListener() {
            @Override
            public void onResponseMessage(OtherResult otherResult) {
                NewsInfomationResult news_info = otherResult.news_info;//新闻详情
                if(news_info == null){
                    CommonUtils.toastMessage("获取头条详情失败，请重试");
                    return;
                }
                Glide.with(ActivityHeadLineDetail.this).load(news_info.ud_photo_fileid)
                        .placeholder(R.drawable.home_adviosr_img)
                        .bitmapTransform(new CropCircleTransformation(ActivityHeadLineDetail.this))
                        .into(img);
                name.setText(news_info.ud_nickname);
                detail_title.setText(news_info.cc_title);
                detail_detail.setText(Html.fromHtml(news_info.cc_context));
                profile.setText(news_info.cc_description);
                time.setText(news_info.cc_datetime);
                count.setText(news_info.cc_count);
                count_zan.setText("赞"+news_info.cc_agr);

                DaShangInformationResult ds_infos = otherResult.ds_info;//打赏
                count_shang.setText("已有"+ds_infos.ds_count+"人打赏");

                List<CommentInformationResult> pl_info = otherResult.pl_info;//评论
                //评论数
                mPlTotal = Integer.parseInt(otherResult.pl_total);
                comment_count.setText("("+ mPlTotal +")");
                if(pl_info.size() == 0){
                    ll_sofa.setVisibility(View.VISIBLE);
                }else{
                    ll_sofa.setVisibility(View.GONE);
                }
                if(mAdvisorManager == null){
                    mAdvisorManager = new FullyLinearLayoutManager(ActivityHeadLineDetail.this);
                }
                if(mDetailAdapter == null){
                    mDetailAdapter = new HeadlineDetailAdapter(ActivityHeadLineDetail.this, pl_info);
                }
                mRecyclerView.setLayoutManager(mAdvisorManager);
                mRecyclerView.setAdapter(mDetailAdapter);
                if(mDivider == null){//防止多次加载出现宽度变宽
                    mDivider = new RecycleViewDivider(ActivityHeadLineDetail.this, LinearLayoutManager.HORIZONTAL);
                    mRecyclerView.addItemDecoration(mDivider);
                }

                mIsDz = otherResult.is_dz; //是否点赞
                CommonUtils.dismissProgressDialog();
            }
        },getResources().getString(R.string.headinfo));
    }

    private void initListener() {
        back.setOnClickListener(this);
        share.setOnClickListener(this);
        ll_shang.setOnClickListener(this);
        ll_zan.setOnClickListener(this);
        et_comment.setOnClickListener(this);
        et_comment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch(actionId){
                    case EditorInfo.IME_ACTION_DONE:
                        if(!SpTools.getBoolean(CommonUtils.getContext(), Constants.isLogin,false)){
                            intent = new Intent(ActivityHeadLineDetail.this, ActivityLogin.class);
                            startActivity(intent);
                            overridePendingTransition(0,0);
                            return false;
                        }
                        String str = et_comment.getText().toString();
                        if(TextUtils.isEmpty(str)){
                            CommonUtils.toastMessage("评论的内容不能为空");
                            break;
                        }
                        NewsInternetRequest.publishComment(mNewsinfoId, str, new NewsInternetRequest.ForResultNewsInfoListener() {
                            @Override
                            public void onResponseMessage(OtherResult result) {
                                CommentResult commentResult = result.cms_contextpl;
                                CommentInformationResult informationResult = new CommentInformationResult();
                                informationResult.setUd_nickname(commentResult.ccp_ud_nickname);
                                informationResult.setUd_photo_fileid(commentResult.ud_photo_fileid);
                                informationResult.setCcp_info(commentResult.ccp_info);
                                informationResult.setCcp_time(commentResult.ccp_time);
                                mDetailAdapter.addDatas(informationResult);
                                mRecyclerView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mDetailAdapter.notifyDataSetChanged();
                                    }
                                });
                                CommonUtils.toastMessage("发表评论成功");
                                mPlTotal++;
                                ll_sofa.setVisibility(View.GONE);
                                comment_count.setText("("+ mPlTotal +")");
                            }
                        });
                        break;
                }
                et_comment.setText("");
                et_comment.setFocusable(false);
                InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                mRecyclerView.smoothScrollToPosition(0);//滑动到第一条，不起作用
                return false;
            }
        });
    }
    Intent intent;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back:
                finishAll();
                finish();
                break;
            case R.id.iv_base_share:

                break;
            case R.id.ll_headline_detail_zan:
                if(!SpTools.getBoolean(CommonUtils.getContext(), Constants.isLogin,false)){
                    intent = new Intent(ActivityHeadLineDetail.this, ActivityLogin.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    return;
                }
                if(mIsDz.equals("0")){//点赞并提交服务器
                    NewsInternetRequest.dianZan(mNewsinfoId, new NewsInternetRequest.ForResultListener() {
                        @Override
                        public void onResponseMessage(String count) {
                            if(!TextUtils.isEmpty(count)){
                                count_zan.setText("赞"+count);
                                mIsDz = "1";
                            }
                        }
                    });
                }else{
                    CommonUtils.toastMessage("您已经点过赞了");
                }
                break;
            case R.id.ll_headline_detail_shang://打赏
                if(!SpTools.getBoolean(CommonUtils.getContext(), Constants.isLogin,false)){
                    intent = new Intent(ActivityHeadLineDetail.this, ActivityLogin.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    return;
                }
                intent = new Intent(this,ActivityReward.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                break;
            case R.id.et_headline_detail://发表评论
                if(et_comment.isFocusable()){

                }else{
                    et_comment.setFocusable(true);
                    et_comment.setFocusableInTouchMode(true);
                    et_comment.requestFocus();
                    et_comment.findFocus();
                    InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0,InputMethodManager.SHOW_FORCED);
                }
                break;
        }
    }

    private void finishAll() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CommonUtils.dismissProgressDialog();
    }
    /**
     * RecyclerView 移动到当前位置，
     * @param manager   设置RecyclerView对应的manager
     * @param mRecyclerView  当前的RecyclerView
     * @param n  要跳转的位置
     */
    public static void MoveToPosition(LinearLayoutManager manager, RecyclerView mRecyclerView, int n) {
        int firstItem = manager.findFirstVisibleItemPosition();
        int lastItem = manager.findLastVisibleItemPosition();
        if (n <= firstItem) {
            mRecyclerView.scrollToPosition(n);
        } else if (n <= lastItem) {
            int top = mRecyclerView.getChildAt(n - firstItem).getTop();
            mRecyclerView.scrollBy(0, top);
        } else {
            mRecyclerView.scrollToPosition(n);
        }
    }
}
