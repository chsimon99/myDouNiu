package com.zfxf.douniu.activity.goodproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * @author IMXU
 * @time   2017/5/3 13:32
 * @des    好项目 详情
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class ActivityXiangMuDetail extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;
    @BindView(R.id.iv_base_question)
    ImageView question;

//    @BindView(R.id.iv_xiangmu_detail_img)
//    ImageView img;//项目图片
    @BindView(R.id.iv_xiangmu_detail_type)
    ImageView type;//项目进度

    @BindView(R.id.tv_xiangmu_detail_name)
    TextView name;//项目名称
    @BindView(R.id.tv_xiangmu_detail_lingyu)
    TextView lingyu;//所属领域
    @BindView(R.id.tv_xiangmu_detail_money)
    TextView money;//融资金额
    @BindView(R.id.tv_xiangmu_detail_address)
    TextView address;//公司地址
    @BindView(R.id.tv_xiangmu_detail_phone)
    TextView phone;//联系电话
    @BindView(R.id.tv_xiangmu_detail_content)
    TextView content;//项目内容
    @BindView(R.id.tv_xiangmu_detail_qualify)
    TextView qualify;//项目调研

    @BindView(R.id.videoplayer)
    JCVideoPlayerStandard videoplayer;

    private int mNewsinfoId;
    private String mPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiangmu_detail);
        ButterKnife.bind(this);

        title.setText("好项目详情");
        edit.setVisibility(View.INVISIBLE);
        question.setVisibility(View.VISIBLE);
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
        question.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back:
                finishAll();
                finish();
                break;
            case R.id.iv_base_question://项目说明
                Intent in = new Intent(this, ActivityXiangMuExplain.class);
                startActivity(in);
                overridePendingTransition(0,0);

                break;
            case R.id.tv_xiangmu_detail_qualify://项目调研跳转
                if(!SpTools.getBoolean(CommonUtils.getContext(), Constants.isLogin,false)){
                    Intent intent = new Intent(this, ActivityLogin.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                }else {
//                    showDailog();
                    Intent intent = new Intent(this, ActivityXiangMuResearch.class);
                    intent.putExtra("id",mNewsinfoId);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                }
                break;
            case R.id.tv_contract_dialog_cannel:
                mDialog.dismiss();
                break;
            case R.id.tv_contract_dialog_confirm:
                callPhone();
                mDialog.dismiss();
                break;
        }
    }

    private void callPhone() {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+mPhone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private AlertDialog mDialog;
    private void showDailog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this); //先得到构造器
        mDialog = builder.create();
        mDialog.show();
        View view = View.inflate(this, R.layout.activity_contract_dialog, null);
        mDialog.getWindow().setContentView(view);
        TextView phone = (TextView) view.findViewById(R.id.tv_contract_dialog_phone);
        mPhone = phone.getText().toString();
        view.findViewById(R.id.tv_contract_dialog_cannel).setOnClickListener(this);
        view.findViewById(R.id.tv_contract_dialog_confirm).setOnClickListener(this);

    }

    private void finishAll() {

    }
    private void visitInternet() {
        NewsInternetRequest.getNewsInformation(mNewsinfoId, new NewsInternetRequest.ForResultNewsInfoListener() {
            @Override
            public void onResponseMessage(OtherResult otherResult) {
                ProjectListResult projectInfo = otherResult.project_info;

                String picUrl = getResources().getString(R.string.file_host_address)
                        +getResources().getString(R.string.showpic)
                        +projectInfo.video_pic;
//                String shipinUrl = getResources().getString(R.string.file_host_address)
//                        +getResources().getString(R.string.showpic)
//                        +projectInfo.video;
                videoplayer.setUp(projectInfo.video ,JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "","","");
                Glide.with(ActivityXiangMuDetail.this).load(picUrl).into(videoplayer.thumbImageView);

//                Glide.with(ActivityXiangMuDetail.this).load(picUrl)
//                        .placeholder(R.drawable.xiangmu_img)
//                        .into(img);
                name.setText(projectInfo.cc_title);
                if(Integer.parseInt(projectInfo.biaoshi) == 0){
                    type.setImageResource(R.drawable.xiangmu_yure);
                }else{
                    type.setImageResource(R.drawable.xiangmu_ing);
                }
                lingyu.setText(projectInfo.shiyong);
                money.setText(projectInfo.feiyong);
                address.setText(projectInfo.addr);
                phone.setText(projectInfo.phone);
                content.setText(projectInfo.cc_description);
                CommonUtils.dismissProgressDialog();
            }
        },getResources().getString(R.string.projectinfo));
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
        CommonUtils.dismissProgressDialog();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }
}
