package com.zfxf.douniu.activity.bar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.login.ActivityLogin;
import com.zfxf.douniu.bean.NewsInfomationResult;
import com.zfxf.douniu.bean.OtherResult;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.SpTools;

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
public class ActivityBarBarDetail extends FragmentActivity implements View.OnClickListener{

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

    private int mNewsinfoId;
    private String mIsDz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_bar_detail);
        ButterKnife.bind(this);
        title.setText("详情");
        edit.setVisibility(View.INVISIBLE);
        share.setVisibility(View.INVISIBLE);
        mNewsinfoId = getIntent().getIntExtra("newsinfoId", 0);
        detail_title.getPaint().setFakeBoldText(true);//加粗
        initData();
        initListener();
    }

    private void initData() {
        if(mNewsinfoId == 0){
            CommonUtils.toastMessage("获取详情失败，请重试");
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
                if(news_info.ud_photo_fileid.contains("http")){
                    Glide.with(ActivityBarBarDetail.this).load(news_info.ud_photo_fileid)
                            .placeholder(R.drawable.home_adviosr_img)
                            .bitmapTransform(new CropCircleTransformation(ActivityBarBarDetail.this))
                            .into(img);
                }else {
                    String picUrl = getResources().getString(R.string.file_host_address)
                            +getResources().getString(R.string.showpic)
                            +news_info.ud_photo_fileid;
                    Glide.with(ActivityBarBarDetail.this).load(picUrl)
                            .placeholder(R.drawable.home_adviosr_img)
                            .bitmapTransform(new CropCircleTransformation(ActivityBarBarDetail.this))
                            .into(img);
                }
                name.setText(news_info.ud_nickname);
                detail_title.setText(news_info.cc_title);
                detail_detail.setText(Html.fromHtml(news_info.cc_context));
                time.setText(news_info.cc_datetime);
                count.setText(news_info.cc_count);
                count_zan.setText("赞"+news_info.cc_agr);

                mIsDz = otherResult.is_dz; //是否点赞
                CommonUtils.dismissProgressDialog();
            }
        },getResources().getString(R.string.douniubarinfo));
    }

    private void initListener() {
        back.setOnClickListener(this);
        share.setOnClickListener(this);
        ll_zan.setOnClickListener(this);
    }

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
                    Intent intent = new Intent(this, ActivityLogin.class);
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
        }
    }

    private void finishAll() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CommonUtils.dismissProgressDialog();
    }
}
