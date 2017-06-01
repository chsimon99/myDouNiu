package com.zfxf.douniu.activity.myself;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.internet.LoginInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * @author IMXU
 * @time   2017/5/3 13:18
 * @des    我的 投顾入驻
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class ActivityMyselfAdvisor extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.et_myself_advisor_name)
    EditText name;
    @BindView(R.id.et_myself_advisor_phone)
    EditText phone;
    @BindView(R.id.et_myself_advisor_number)
    EditText number;
    @BindView(R.id.tv_myself_advisor_year)
    TextView year;
    @BindView(R.id.ll_myself_advisor_year)
    LinearLayout ll_year;
    @BindView(R.id.rl_myself_advisor_confirm)
    RelativeLayout confirm;

    TextView year1;
    TextView year2;
    TextView year3;
    TextView year4;
    TextView year5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myself_advisor);
        ButterKnife.bind(this);

        title.setText("投顾申请");
        edit.setVisibility(View.INVISIBLE);

        initdata();
        initListener();
    }

    private void initdata() {

    }
    private void initListener() {
        back.setOnClickListener(this);
        ll_year.setOnClickListener(this);
        confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back:
                finishAll();
                finish();
                break;
            case R.id.rl_myself_advisor_confirm:
                String s_phone = phone.getText().toString();
                String s_name = name.getText().toString();
                String s_number = number.getText().toString();
                String s_year = year.getText().toString();
                if(IsEmpty(s_phone,s_name,s_number)){
                    if(!CommonUtils.isMobilePhone(s_phone)){
                        CommonUtils.toastMessage("您输入的手机号有误");
                        s_phone = "";
                        return ;
                    }
                    int year = 0;
                    if(s_year.equals("0 - 1年")){
                        year = 1;
                    }else if(s_year.equals("1 - 3年")){
                        year = 2;
                    }else if(s_year.equals("3 - 5年")){
                        year = 3;
                    }else if(s_year.equals("5 - 10年")){
                        year = 4;
                    }else if(s_year.equals("10年以上")){
                        year = 5;
                    }
                    LoginInternetRequest.applyTouGu(s_name, s_phone, s_number, year+"", new LoginInternetRequest.ForResultListener() {
                        @Override
                        public void onResponseMessage(String code) {
                            if(code.equals("成功")){
                                CommonUtils.toastMessage("投顾申请已经提交,请等待客服与您联系");
                                finish();
                            }
                        }
                    });
                }else {

                }
                //提交申请
                break;
            case R.id.ll_myself_advisor_year:
                if(mPopWindow !=null && mPopWindow.isShowing()){
                    mPopWindow.dismiss();
                }else {
                    showPopupWindow();
                }
                break;
            case R.id.tv_myself_pop_year1:
                mPopWindow.dismiss();
                year.setText(year1.getText().toString());
                break;
            case R.id.tv_myself_pop_year2:
                mPopWindow.dismiss();
                year.setText(year2.getText().toString());
                break;
            case R.id.tv_myself_pop_year3:
                mPopWindow.dismiss();
                year.setText(year3.getText().toString());
                break;
            case R.id.tv_myself_pop_year4:
                mPopWindow.dismiss();
                year.setText(year4.getText().toString());
                break;
            case R.id.tv_myself_pop_year5:
                mPopWindow.dismiss();
                year.setText(year5.getText().toString());
                break;
        }
    }

    private boolean IsEmpty(String s_phone, String s_name, String s_number) {
        if(TextUtils.isEmpty(s_name)){
            CommonUtils.toastMessage("请输入姓名");
            return false;
        }
        if(TextUtils.isEmpty(s_phone)){
            CommonUtils.toastMessage("请输入手机号");
            return false;
        }
        if(TextUtils.isEmpty(s_number)){
            CommonUtils.toastMessage("请输入资格证号");
            return false;
        }
        return true;
    }

    private PopupWindow mPopWindow;

    private void showPopupWindow() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.activity_myself_pop, null);
        mPopWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopWindow.setContentView(contentView);
        year1 = (TextView) contentView.findViewById(R.id.tv_myself_pop_year1);
        year2 = (TextView) contentView.findViewById(R.id.tv_myself_pop_year2);
        year3 = (TextView) contentView.findViewById(R.id.tv_myself_pop_year3);
        year4 = (TextView) contentView.findViewById(R.id.tv_myself_pop_year4);
        year5 = (TextView) contentView.findViewById(R.id.tv_myself_pop_year5);
        year1.setOnClickListener(this);
        year2.setOnClickListener(this);
        year3.setOnClickListener(this);
        year4.setOnClickListener(this);
        year5.setOnClickListener(this);
        mPopWindow.showAsDropDown(ll_year);
    }

    private void finishAll() {

    }

    @Override
    public void onBackPressed() {
        if(mPopWindow !=null && mPopWindow.isShowing()){
            mPopWindow.dismiss();
        }else {
            super.onBackPressed();
        }
    }
}
