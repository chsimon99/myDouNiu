package com.zfxf.douniu.activity.advisor;

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
import com.zfxf.douniu.activity.login.ActivityLogin;
import com.zfxf.douniu.bean.CourseResult;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.SpTools;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author IMXU
 * @time   2017/5/3 13:36
 * @des    公开课 详情
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class ActivityAdvisorAllPublicDetail extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.iv_advisor_all_public_detail_img)
    ImageView img;
    @BindView(R.id.tv_advisor_all_public_detail_title)
    TextView tv_title;
    @BindView(R.id.tv_advisor_all_public_detail_from)
    TextView from;
    @BindView(R.id.tv_advisor_all_public_detail_time)
    TextView time;
    @BindView(R.id.tv_advisor_all_public_detail_count)
    TextView mCount;
    @BindView(R.id.tv_advisor_all_public_detail_subscribe)
    TextView subscribe;
    @BindView(R.id.tv_advisor_all_public_detail_follow)
    TextView follow;
    @BindView(R.id.tv_advisor_all_public_detail_name)
    TextView name;
    @BindView(R.id.tv_advisor_all_public_detail_detail)
    TextView detail;

    @BindView(R.id.ll_advisor_all_public_detail)
    LinearLayout ll_subscribe;
    private int mId;
    private String mAuthUbId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advisor_all_public_detail);
        ButterKnife.bind(this);
        mId = getIntent().getIntExtra("id",0);
        title.setText("课程详情");

        initdata();
        initListener();
    }

    private void initdata() {
        if(mId != 0){
            CommonUtils.showProgressDialog(this,"加载中……");
            NewsInternetRequest.getCourseInformation(mId, new NewsInternetRequest.ForResultCourseInfoListener() {
                @Override
                public void onResponseMessage(CourseResult courseResult) {
                    Glide.with(ActivityAdvisorAllPublicDetail.this).load(courseResult.news_info.img)
                            .placeholder(R.drawable.advisor_detail).into(img);
                    tv_title.setText(courseResult.news_info.cc_title);
                    from.setText(courseResult.news_info.ud_nickname);
                    time.setText(courseResult.news_info.cc_datetime);
                    mCount.setText(courseResult.news_info.dy_count);
                    mAuthUbId = courseResult.news_info.auth_ub_id;
                    if(courseResult.news_info.has_gz.equals("0")){
                        followCannel();
                    }else{
                        followSuccess();
                    }

                    if(courseResult.news_info.has_dy.equals("0")){
                        subscribeCannel();
                    }else{
                        subscribeSuccess();
                    }
                    detail.setText(courseResult.news_info.cc_description);
                    CommonUtils.dismissProgressDialog();
                }
            },getResources().getString(R.string.gongkeinfo));
        }
    }
    private void initListener() {
        back.setOnClickListener(this);
        ll_subscribe.setOnClickListener(this);
        subscribe.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back:
                finishAll();
                finish();
                break;
            case R.id.ll_advisor_all_public_detail:
                if(!SpTools.getBoolean(CommonUtils.getContext(), Constants.isLogin,false)){
                    Intent intent = new Intent(this, ActivityLogin.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    return;
                }
                //关注本公开课的播主
                if (follow.getText().toString().equals("关注")) {
                    subscribeInternet(Integer.parseInt(mAuthUbId),6,0);
                    followSuccess();
                } else {
                    subscribeInternet(Integer.parseInt(mAuthUbId),6,1);
                    followCannel();
                }
                break;
            case R.id.tv_advisor_all_public_detail_subscribe:
                if(!SpTools.getBoolean(CommonUtils.getContext(), Constants.isLogin,false)){
                    Intent intent = new Intent(this, ActivityLogin.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    return;
                }
                if (subscribe.getText().toString().equals("立即预约")) {
                    subscribeInternet(mId,0,0);
                } else {
                    subscribeInternet(mId,0,1);
                }
                break;
        }
    }

    private void followCannel() {
        follow.setText("关注");
        follow.setBackgroundResource(R.drawable.backgroud_button_app_color);
    }

    private void followSuccess() {
        follow.setText("已关注");
        follow.setBackgroundResource(R.drawable.backgroud_button_gary_color);
    }

    private void subscribeCannel() {
        subscribe.setText("立即预约");
        subscribe.setBackgroundResource(R.drawable.backgroud_button_app_color);
        subscribe.setBackgroundColor(CommonUtils.getContext().getResources().getColor(R.color.colorTitle));
    }

    private void subscribeSuccess() {
        subscribe.setText("已预约");
        subscribe.setBackgroundResource(R.drawable.backgroud_button_gary_color);
        subscribe.setBackgroundColor(CommonUtils.getContext().getResources().getColor(R.color.colorGray));
    }

    /**
     * @param id 订阅/关注的id
     * @param typyid 0订阅 6关注
     * @param type 0订阅/关注 1取消
     */
    private void subscribeInternet(int id, final int typyid, final int type) {
        NewsInternetRequest.subscribeAndCannel(id+"", typyid, type, new NewsInternetRequest.ForResultListener() {
                    @Override
                    public void onResponseMessage(String count) {
                        if(TextUtils.isEmpty(count)){
                            return;
                        }
                        if(typyid == 0){
                            mCount.setText(count);
                            if(type == 0){
                                subscribeSuccess();
                                CommonUtils.toastMessage("预约成功");
                            }else{
                                subscribeCannel();
                                CommonUtils.toastMessage("取消预约成功");
                            }
                            SpTools.setBoolean(ActivityAdvisorAllPublicDetail.this, Constants.subscribe,true);
                        }else{
                            if(type == 0){
                                CommonUtils.toastMessage("关注成功");
                                followSuccess();
                            } else {
                                CommonUtils.toastMessage("取消关注成功");
                                followCannel();
                            }
                        }
                    }
                },getResources().getString(R.string.userdy));
    }
    private void finishAll() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CommonUtils.dismissProgressDialog();
    }
}
