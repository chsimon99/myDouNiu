package com.zfxf.douniu.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.utils.CommonUtils;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * @author IMXU
 * @time   2017/5/3 13:40
 * @des    问股 界面
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class ActivityAskStock extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_confirm)
    TextView confirm;
    @BindView(R.id.tv_base_title)
    TextView title;


    @BindView(R.id.tv_askstock_fee)
    TextView fee;
    @BindView(R.id.tv_askstock_count)
    TextView count;
    @BindView(R.id.ll_askstock_contact)
    LinearLayout contract;
    @BindView(R.id.et_askstock)
    EditText askstock;
    private String mTv_fee;
    private String mSx_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_askstock);
        ButterKnife.bind(this);
        String toName = getIntent().getStringExtra("name");//向xx提问
        mTv_fee = getIntent().getStringExtra("fee");//问股价钱
        mSx_id = getIntent().getStringExtra("sx_id");//首席id
        if(TextUtils.isEmpty(toName)){
            title.setText("问股");
        }else {
            title.setText("向"+toName+"问股");

        }
        if(!TextUtils.isEmpty(mTv_fee)){
            fee.setVisibility(View.VISIBLE);
            fee.setText(mTv_fee +"元/次");
        }
        edit.setVisibility(View.INVISIBLE);
        confirm.setVisibility(View.VISIBLE);
        initdata();
        initListener();
    }

    private void initdata() {

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(askstock, 0);
            }
        },300);
    }
    private void initListener() {
        back.setOnClickListener(this);
        confirm.setOnClickListener(this);
        contract.setOnClickListener(this);
        //edittext设置多行后， android:imeOptions="actionDone" 这个不起作用，只有单行才起作用
//        askstock.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                switch(actionId){
//                    case EditorInfo.IME_ACTION_DONE:
//                        confirm();
//                        break;
//                }
//                return true;
//            }
//        });
        final int maxLen = 50;
        askstock.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Editable editable = askstock.getText();
                int len = editable.length();
                if(len > maxLen)
                {
                    int selEndIndex = Selection.getSelectionEnd(editable);
                    String str = editable.toString();
                    //截取新字符串
                    String newStr = str.substring(0,maxLen);
                    askstock.setText(newStr);
                    editable = askstock.getText();
                    //新字符串的长度
                    int newLen = editable.length();
                    //旧光标位置超过字符串长度
                    if(selEndIndex > newLen)
                    {
                        selEndIndex = editable.length();
                    }
                    //设置新光标所在的位置
                    Selection.setSelection(editable, selEndIndex);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>50){
                    CommonUtils.toastMessage("最多可以输入50个字");
                }else {
                    count.setText(s.length()+"/50");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back:
                finishAll();
                finish();
                break;
            case R.id.tv_base_confirm:
                //提交服务器
                confirm();
                break;
            case R.id.ll_askstock_contact:
                //细则说明
                break;
        }
    }

    private void finishAll() {

    }
    private void confirm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog = builder.create();
        builder.setTitle("确认提交")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //提交服务器
                        finishAll();
                        finish();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (isHideInput(view, ev)) {
                HideSoftInput(view.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }
    // 判定是否需要隐藏
    private boolean isHideInput(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] l = { 0, 0 };
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            if (ev.getX() > left && ev.getX() < right && ev.getY() > top && ev.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
    // 隐藏软键盘
    private void HideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token,InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
