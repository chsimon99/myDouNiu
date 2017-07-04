package com.zfxf.douniu.activity.myself;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
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
 * @time   2017/5/3 13:20
 * @des    意见反馈
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class ActivityMyselfQuestion extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.et_myself_question_phone)
    EditText phone;
    @BindView(R.id.et_myself_question_content)
    EditText content;
    @BindView(R.id.rl_myself_question_submit)
    RelativeLayout submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myself_question);
        ButterKnife.bind(this);

        title.setText("意见反馈");
        edit.setVisibility(View.INVISIBLE);

        initdata();
        initListener();
    }

    private void initdata() {

    }
    private void initListener() {
        back.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back:
                finishAll();
                finish();
                break;
            case R.id.rl_myself_question_submit:
                final String str_content = content.getText().toString();
                final String str_phone = phone.getText().toString();
                if(TextUtils.isEmpty(str_content)){
                    CommonUtils.toastMessage("请提出您宝贵的意见");
                    return;
                }
                //提交到服务器
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                AlertDialog dialog = builder.create();
                builder.setTitle("您的意见已经提交，我们会尽快处理")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                NewsInternetRequest.sendQuestion(str_content, str_phone, new NewsInternetRequest.ForResultListener() {
                                    @Override
                                    public void onResponseMessage(String count) {
                                        if(count.equals("成功")){
                                            finishAll();
                                            finish();
                                        }else {
                                            CommonUtils.toastMessage("提交意见失败，请重试");
                                        }
                                    }
                                });
                                dialog.dismiss();
                            }
                        }).show();
                break;
        }
    }

    private void finishAll() {

    }
}
