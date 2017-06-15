package com.zfxf.douniu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.login.ActivityLogin;
import com.zfxf.douniu.bean.OtherResult;
import com.zfxf.douniu.bean.ProjectListResult;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.SpTools;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author IMXU
 * @time   2017/5/3 13:32
 * @des    好项目 详情
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class ActivityXiangMuDetail extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.iv_xiangmu_detail_img)
    ImageView img;//项目图片
    @BindView(R.id.iv_xiangmu_detail_type)
    ImageView type;//项目进度

    @BindView(R.id.tv_xiangmu_detail_name)
    TextView name;//项目名称
    @BindView(R.id.tv_xiangmu_detail_time)
    TextView time;//项目截至时间
    @BindView(R.id.tv_xiangmu_detail_content)
    TextView content;//项目内容
    @BindView(R.id.tv_xiangmu_detail_human)
    TextView human;//项目适合人群
    @BindView(R.id.tv_xiangmu_detail_qualify)
    TextView qualify;//项目资格
    private int mNewsinfoId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiangmu_detail);
        ButterKnife.bind(this);

        title.setText("好项目详情");
        edit.setVisibility(View.INVISIBLE);
        mNewsinfoId = getIntent().getIntExtra("newsinfoId", 0);
        initdata();
        initListener();
    }

    private void initdata() {
        if(mNewsinfoId == 0){
            CommonUtils.toastMessage("获取详情信息失败，请重试");
            return;
        }
        CommonUtils.showProgressDialog(this,"加载中……");
        visitInternet();
    }
    private void initListener() {
        back.setOnClickListener(this);
        qualify.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back:
                finishAll();
                finish();
                break;
            case R.id.tv_xiangmu_detail_qualify:
                if(!SpTools.getBoolean(CommonUtils.getContext(), Constants.isLogin,false)){
                    Intent intent = new Intent(this, ActivityLogin.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                }
                break;
        }
    }

    private void finishAll() {

    }
    private void visitInternet() {
        NewsInternetRequest.getNewsInformation(mNewsinfoId, new NewsInternetRequest.ForResultNewsInfoListener() {
            @Override
            public void onResponseMessage(OtherResult otherResult) {
                ProjectListResult projectInfo = otherResult.project_info;
                Glide.with(ActivityXiangMuDetail.this).load(projectInfo.cc_fielid)
                        .placeholder(R.drawable.xiangmu_img)
                        .into(img);
                name.setText(projectInfo.cc_title);
                time.setText(projectInfo.cc_datetime);
                if(Integer.parseInt(projectInfo.biaoshi) == 0){
                    type.setImageResource(R.drawable.xiangmu_yure);
                }else{
                    type.setImageResource(R.drawable.xiangmu_ing);
                }
                content.setText(projectInfo.cc_description);
                human.setText(projectInfo.shiyong);
                qualify.setText("认购项目("+ projectInfo.feiyong+"元起)");
                CommonUtils.dismissProgressDialog();
            }
        },getResources().getString(R.string.projectinfo));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CommonUtils.dismissProgressDialog();
    }
}
