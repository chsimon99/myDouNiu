package com.zfxf.douniu.activity.goodproject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author IMXU
 * @time   2017/7/26 13:20
 * @des    好项目申请
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class ActivityXiangMuResearch extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.rl_xiangmu_research)
    RelativeLayout confirm;

    @BindView(R.id.et_xiangmu_research_name)
    EditText et_name;
    @BindView(R.id.et_xiangmu_research_phone)
    EditText et_phone;
    @BindView(R.id.et_xiangmu_research_count)
    EditText et_count;
    @BindView(R.id.et_xiangmu_research_demo)
    EditText et_demo;

    @BindView(R.id.ll_xiangmu_research_time)
    LinearLayout ll_time;
    private int mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiangmu_research);
        ButterKnife.bind(this);

        title.setText("项目调研");
        edit.setVisibility(View.INVISIBLE);
        mId = getIntent().getIntExtra("id", 0);
        initdata();
        initListener();
    }

    private void initdata() {

    }
    private void initListener() {
        back.setOnClickListener(this);
        confirm.setOnClickListener(this);
        ll_time.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back:
                finishAll();
                finish();
                break;
            case R.id.rl_xiangmu_research:
                if(TextUtils.isEmpty(et_name.getText().toString())){
                    CommonUtils.toastMessage("姓名不能为空");
                    return;
                }
                if(TextUtils.isEmpty(et_phone.getText().toString())){
                    CommonUtils.toastMessage("手机号不能为空");
                    return;
                }
                if(TextUtils.isEmpty(et_count.getText().toString())){
                    CommonUtils.toastMessage("参加人数不能为空");
                    return;
                }
                if(TextUtils.isEmpty(et_demo.getText().toString())){
                    CommonUtils.toastMessage("调研需求不能为空");
                    return;
                }

                NewsInternetRequest.projectResearch(mId+"",et_name.getText().toString(), et_phone.getText().toString(),
                        et_count.getText().toString(), et_demo.getText().toString(),new NewsInternetRequest.ForResultListener() {
                            @Override
                            public void onResponseMessage(String result) {
                                if(!TextUtils.isEmpty(result) && result.equals("成功")){
                                    CommonUtils.toastMessage("项目调研提交成功，请等待客服联系");
                                    finish();
                                }else {
                                    CommonUtils.toastMessage("请重新提交调研申请");
                                }
                            }
                        });
                break;
        }
    }

    private void finishAll() {

    }
}
