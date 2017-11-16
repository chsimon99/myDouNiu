package com.zfxf.douniu.activity.askstock;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.ActivityAdvisorHome;
import com.zfxf.douniu.bean.IndexResult;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.SpTools;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * @author IMXU
 * @time   2017/5/3 13:39
 * @des    微问答 回答详情
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class ActivityAnswerDetail extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.iv_base_share)
    ImageView share;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.tv_answer_content)
    TextView content;//提问内容
    @BindView(R.id.tv_answer_time)
    TextView askTime;//提问时间
    @BindView(R.id.tv_answer_name)
    TextView name;//回答者姓名
    @BindView(R.id.tv_answer_answer_time)
    TextView answerTime;//回答时间
    @BindView(R.id.tv_answer_zan)
    TextView zan;//点赞数量
    @BindView(R.id.tv_answer_detail_answer_content)
    TextView answer_content;//回答内容
    @BindView(R.id.tv_answer_detail_homepage)
    TextView homepage;//主页
    @BindView(R.id.tv_answer_detail_ask)
    TextView ask;//问股
    @BindView(R.id.ll_answer_zan)
    LinearLayout ll_zan;//问股

    @BindView(R.id.iv_answer_detail_img)
    ImageView mImageView;//回答者头像
    private String mId;
    private String sx_id;
    private String sx_fee;
    private String mIsZan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_detail);
        ButterKnife.bind(this);

        title.setText("回答详情");
        edit.setVisibility(View.INVISIBLE);
        share.setVisibility(View.INVISIBLE);
        mId = getIntent().getStringExtra("id");
        initdata();
        initListener();
    }

    private void initdata() {
        visitInternet();

    }

    private void visitInternet() {
        CommonUtils.showProgressDialog(this,"加载中……");
        NewsInternetRequest.getAnswerDetailInformation(mId, new NewsInternetRequest.ForResultAnswerIndexListener() {
            @Override
            public void onResponseMessage(IndexResult result) {
                content.setText(result.context_info.zc_context);
                askTime.setText(result.context_info.zc_response_date);
                Glide.with(ActivityAnswerDetail.this).load(result.context_info.sx_url)
                        .bitmapTransform(new CropCircleTransformation(ActivityAnswerDetail.this))
                        .placeholder(R.drawable.home_adviosr_img).into(mImageView);
                name.setText(result.context_info.sx_ud_nickname);
                answerTime.setText(result.context_info.sx_answer_date);
                zan.setText("赞("+result.context_info.sx_count+")");
                answer_content.setText(result.context_info.sx_pl);
                sx_id = result.context_info.sx_ub_id;
                sx_fee = result.context_info.sx_fee;
                mIsZan = result.context_info.is_zan;
                SpTools.setBoolean(ActivityAnswerDetail.this, Constants.read,true);
                CommonUtils.dismissProgressDialog();
            }
        });
    }

    private void initListener() {
        back.setOnClickListener(this);
        homepage.setOnClickListener(this);
        ask.setOnClickListener(this);
        ll_zan.setOnClickListener(this);
    }
    Intent intent;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back:
                finishAll();
                finish();
                break;
            case R.id.tv_answer_detail_homepage:
                intent = new Intent(CommonUtils.getContext(), ActivityAdvisorHome.class);
                intent.putExtra("id",Integer.parseInt(sx_id));
                startActivity(intent);
                overridePendingTransition(0,0);
                break;
            case R.id.ll_answer_zan:
                if(mIsZan.equals("0")){//点赞并提交服务器
                    NewsInternetRequest.dianZanAnswer(Integer.parseInt(mId),new NewsInternetRequest.ForResultListener() {
                        @Override
                        public void onResponseMessage(String count) {
                            if(!TextUtils.isEmpty(count)){
                                zan.setText("赞("+count+")");
                                mIsZan = "1";
                            }
                        }
                    });
                }else{
                    CommonUtils.toastMessage("您已经点过赞了");
                }
                break;
            case R.id.tv_answer_detail_ask:
                intent = new Intent(CommonUtils.getContext(), ActivityAskStock.class);
                intent.putExtra("name",name.getText().toString());
                intent.putExtra("fee",sx_fee);
                intent.putExtra("sx_id",sx_id);
                startActivity(intent);
                overridePendingTransition(0,0);
                break;
        }
        intent = null;
    }

    private void finishAll() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CommonUtils.dismissProgressDialog();
    }
}
