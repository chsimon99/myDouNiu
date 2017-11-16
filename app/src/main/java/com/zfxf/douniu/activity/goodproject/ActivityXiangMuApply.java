package com.zfxf.douniu.activity.goodproject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
public class ActivityXiangMuApply extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.rl_xiangmu_confirm)
    RelativeLayout confirm;

    @BindView(R.id.et_xiangmu_apply_name)
    EditText et_name;
    @BindView(R.id.et_xiangmu_apply_lingyu)
    EditText et_lingyu;
    @BindView(R.id.et_xiangmu_apply_money)
    EditText et_money;
    @BindView(R.id.et_xiangmu_apply_address)
    EditText et_address;
    @BindView(R.id.et_xiangmu_apply_phone)
    EditText et_phone;
    @BindView(R.id.et_xiangmu_apply_introduce)
    EditText et_introduce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiangmu_apply);
        ButterKnife.bind(this);

        title.setText("项目申请");
        edit.setVisibility(View.INVISIBLE);

        initdata();
        initListener();
    }

    private void initdata() {

    }
    private void initListener() {
        back.setOnClickListener(this);
        confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back:
                finishAll();
                finish();
                break;
            case R.id.rl_xiangmu_confirm:
                if(TextUtils.isEmpty(et_name.getText().toString())){
                    CommonUtils.toastMessage("项目名称不能为空");
                    return;
                }
                if(TextUtils.isEmpty(et_lingyu.getText().toString())){
                    CommonUtils.toastMessage("所属领域不能为空");
                    return;
                }
                if(TextUtils.isEmpty(et_money.getText().toString())){
                    CommonUtils.toastMessage("融资金额不能为空");
                    return;
                }
                if(TextUtils.isEmpty(et_address.getText().toString())){
                    CommonUtils.toastMessage("公司地址不能为空");
                    return;
                }
                if(TextUtils.isEmpty(et_phone.getText().toString())){
                    CommonUtils.toastMessage("联系电话不能为空");
                    return;
                }
                if(TextUtils.isEmpty(et_introduce.getText().toString())){
                    CommonUtils.toastMessage("项目介绍不能为空");
                    return;
                }

                NewsInternetRequest.projectApplyInformation(et_name.getText().toString(), et_lingyu.getText().toString(),
                        et_money.getText().toString(), et_address.getText().toString(), et_phone.getText().toString(),
                        et_introduce.getText().toString(), new NewsInternetRequest.ForResultListener() {
                            @Override
                            public void onResponseMessage(String result) {
                                if(!TextUtils.isEmpty(result) && result.equals("成功")){
                                    CommonUtils.toastMessage("项目申请提交成功，请等待客服联系");
                                    finish();
                                }else {
                                    CommonUtils.toastMessage("请重新提交申请");
                                }
                            }
                        });
                break;
        }
    }

    private void finishAll() {

    }
}
