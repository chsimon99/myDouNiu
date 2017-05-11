package com.zfxf.douniu.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author IMXU
 * @time   2017/5/3 13:41
 * @des    新闻 详情
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class ActivityPostBar extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;
    @BindView(R.id.tv_base_confirm)
    TextView confirm;

    @BindView(R.id.et_post_bar_title)
    EditText et_title;
    @BindView(R.id.et_post_bar_content)
    EditText et_content;
    @BindView(R.id.ll_post_bar_stock)
    LinearLayout ll_stock;
    @BindView(R.id.ll_post_bar_other)
    LinearLayout ll_other;
    @BindView(R.id.iv_post_bar_stock_noselect)
    ImageView iv_stock_no;
    @BindView(R.id.iv_post_bar_stock_select)
    ImageView iv_stock;
    @BindView(R.id.iv_post_bar_other_noselect)
    ImageView iv_other_no;
    @BindView(R.id.iv_post_bar_other_select)
    ImageView iv_other;
    private String mTitle;
    private String mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_bar);
        ButterKnife.bind(this);
        title.setText("发观点");
        confirm.setVisibility(View.VISIBLE);
        edit.setVisibility(View.INVISIBLE);
        initData();
        initListener();
    }

    private void initData() {

    }
    private void initListener() {
        back.setOnClickListener(this);
        confirm.setOnClickListener(this);
        ll_stock.setOnClickListener(this);
        ll_other.setOnClickListener(this);
    }
    private int flag = -1;
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.iv_base_back:
                finishAll();
                finish();
                break;
            case R.id.tv_base_confirm:
                if(flag < 0){
                    CommonUtils.toastMessage("请选择观点类型");
                    return;
                }
                mTitle = et_title.getText().toString();
                mContent = et_content.getText().toString();
                if(TextUtils.isEmpty(mTitle)){
                    CommonUtils.toastMessage("请输入标题");
                }
                if(TextUtils.isEmpty(mContent)){
                    CommonUtils.toastMessage("请输入内容");
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                AlertDialog dialog = builder.create();
                builder.setTitle("确认提交观点")
                        .setPositiveButton("提交", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                visitInternet();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                break;
            case R.id.ll_post_bar_stock:
                reset();
                iv_stock.setVisibility(View.VISIBLE);
                iv_stock_no.setVisibility(View.INVISIBLE);
                flag = 0;
                break;
            case R.id.ll_post_bar_other:
                reset();
                iv_other.setVisibility(View.VISIBLE);
                iv_other_no.setVisibility(View.INVISIBLE);
                flag = 1;
                break;
        }
    }

    private void visitInternet() {
        NewsInternetRequest.postBar(mTitle, mContent, flag, new NewsInternetRequest.ForResultListener() {
            @Override
            public void onResponseMessage(String count) {
                if(count.equals("成功")){
                    CommonUtils.toastMessage("您发表的观点已成功提交审核");
                    finishAll();
                    finish();
                }else {
                    CommonUtils.toastMessage("发表观点失败，请重新提交");
                }

            }
        });
    }

    private void reset() {
        iv_other_no.setVisibility(View.VISIBLE);
        iv_stock.setVisibility(View.INVISIBLE);
        iv_stock_no.setVisibility(View.VISIBLE);
        iv_other.setVisibility(View.INVISIBLE);
    }

    private void finishAll() {

    }
}
