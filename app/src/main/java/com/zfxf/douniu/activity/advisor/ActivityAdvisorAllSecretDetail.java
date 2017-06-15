package com.zfxf.douniu.activity.advisor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.ActivityToPay;
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
 * @des    私密课 详情
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class ActivityAdvisorAllSecretDetail extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.iv_advisor_all_secret_detail_img)
    ImageView img;
    @BindView(R.id.tv_advisor_all_secret_detail_title)
    TextView tv_title;
    @BindView(R.id.tv_advisor_all_secret_detail_from)
    TextView from;
    @BindView(R.id.tv_advisor_all_secret_detail_time)
    TextView time;
    @BindView(R.id.tv_advisor_all_secret_detail_count)
    TextView mCount;
    @BindView(R.id.tv_advisor_all_secret_detail_money)
    TextView money;
    @BindView(R.id.tv_advisor_all_secret_detail_pay)
    TextView pay;
    @BindView(R.id.tv_advisor_all_secret_detail_name)
    TextView name;
    @BindView(R.id.tv_advisor_all_secret_detail_detail)
    TextView detail;
    private int mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advisor_all_secret_detail);
        ButterKnife.bind(this);
        mId = getIntent().getIntExtra("id",0);
        title.setText("课程详情");

        initdata();
        initListener();
    }
    private String fee;
    private void initdata() {
        if(mId != 0){
            visitInternet();
        }
    }

    private void visitInternet() {
        CommonUtils.showProgressDialog(this,"加载中……");
        NewsInternetRequest.getCourseInformation(mId, new NewsInternetRequest.ForResultCourseInfoListener() {
            @Override
            public void onResponseMessage(CourseResult courseResult) {
                Glide.with(ActivityAdvisorAllSecretDetail.this).load(courseResult.news_info.img)
                        .placeholder(R.drawable.advisor_detail).into(img);
                tv_title.setText(courseResult.news_info.cc_title);
                from.setText(courseResult.news_info.ud_nickname);
                time.setText(courseResult.news_info.cc_datetime);
                mCount.setText(courseResult.news_info.buy_count);
                detail.setText(courseResult.news_info.cc_description);
                fee = courseResult.news_info.cc_fee;
                money.setText("￥"+fee+"元");

                if(courseResult.news_info.has_buy.equals("0")){
                    buyCannel();
                }else{
                    buySuccess();
                }
                CommonUtils.dismissProgressDialog();
            }
        },getResources().getString(R.string.sikeinfo));
    }

    private void buyCannel() {
        pay.setText("立即支付");
        pay.setBackgroundResource(R.drawable.backgroud_button_app_color);
        pay.setBackgroundColor(CommonUtils.getContext().getResources().getColor(R.color.colorTitle));
    }

    private void buySuccess() {
        pay.setText("已付款");
        pay.setBackgroundResource(R.drawable.backgroud_button_gary_color);
        pay.setBackgroundColor(CommonUtils.getContext().getResources().getColor(R.color.colorGray));
    }

    private void initListener() {
        back.setOnClickListener(this);
        pay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back:
                finishAll();
                finish();
                break;
            case R.id.tv_advisor_all_secret_detail_pay:
                if(!SpTools.getBoolean(CommonUtils.getContext(), Constants.isLogin,false)){
                    Intent intent = new Intent(this, ActivityLogin.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    return;
                }
                if(pay.getText().toString().equals("已付款")){
                    return;
                }else {
                    Intent intent = new Intent(CommonUtils.getContext(), ActivityToPay.class);
                    intent.putExtra("type","私密课");
                    intent.putExtra("count",fee);
                    intent.putExtra("from",from.getText().toString());
                    startActivity(intent);
                    overridePendingTransition(0,0);
                }
                break;
        }
    }

    private void finishAll() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(SpTools.getBoolean(this, Constants.buy,false)){
            buySuccess();
            SpTools.setBoolean(this, Constants.buy,false);
        }
        if(SpTools.getBoolean(this, Constants.alreadyLogin,false)){
            visitInternet();
            SpTools.setBoolean(this, Constants.alreadyLogin,false);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CommonUtils.dismissProgressDialog();
    }
}
