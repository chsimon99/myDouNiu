package com.zfxf.douniu.activity.advisor;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.utils.CommonUtils;

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
    TextView count;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advisor_all_public_detail);
        ButterKnife.bind(this);

        title.setText("课程详情");

        initdata();
        initListener();
    }

    private void initdata() {

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
                //关注本公开课的播主
                if (follow.getText().toString().equals("关注")) {
                    follow.setText("已关注");
                    follow.setBackgroundResource(R.drawable.backgroud_button_gary_color);
                } else {
                    follow.setText("关注");
                    follow.setBackgroundResource(R.drawable.backgroud_button_app_color);
                }
                break;
            case R.id.tv_advisor_all_public_detail_subscribe:
                if (subscribe.getText().toString().equals("立即预约")) {
                    subscribe.setText("已预约");
                    subscribe.setBackgroundColor(CommonUtils.getContext().getResources().getColor(R.color.colorGray));
                } else {
                    subscribe.setText("立即预约");
                    subscribe.setBackgroundResource(R.drawable.backgroud_button_app_color);
                    subscribe.setBackgroundColor(CommonUtils.getContext().getResources().getColor(R.color.colorTitle));
                }
                break;
        }
    }

    private void finishAll() {

    }
}
