package com.zfxf.douniu.activity.myself;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.activity.login.ActivityLogin;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.DataCleanManager;
import com.zfxf.douniu.utils.SpTools;
import com.zfxf.douniu.view.fragment.FragmentMyself;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * @author IMXU
 * @time   2017/5/3 13:20
 * @des    我的 设置
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class ActivityMyselfShezhi extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.tv_myself_shezhi_cache)
    TextView tv_cache;

    @BindView(R.id.ll_myself_she_contact)
    LinearLayout contact;//联系我们
    @BindView(R.id.ll_myself_she_question)
    LinearLayout question;//意见反馈
    @BindView(R.id.ll_myself_she_code)
    LinearLayout code;//密码修改
    @BindView(R.id.ll_myself_she_cache)
    LinearLayout cache;//清除缓存
    @BindView(R.id.ll_myself_she_update)
    LinearLayout update;//检查更新
    @BindView(R.id.ll_myself_she_quit)
    LinearLayout quit;//退出
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myself_shezhi);
        ButterKnife.bind(this);

        title.setText("设置");
        edit.setVisibility(View.INVISIBLE);

        String cacheSize = DataCleanManager.getCacheSize(this);
        if(cacheSize.contains("KB")){
            tv_cache.setText("0MB");
        }else if(cacheSize.contains("Byte")){
            tv_cache.setText("0MB");
        }else {
            tv_cache.setText(cacheSize);
        }

        initdata();
        initListener();
    }

    private void initdata() {

    }
    private void initListener() {
        back.setOnClickListener(this);
        contact.setOnClickListener(this);
        question.setOnClickListener(this);
        code.setOnClickListener(this);
        cache.setOnClickListener(this);
        update.setOnClickListener(this);
        quit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back:
                finishAll();
                finish();
                break;
            case R.id.ll_myself_she_contact:
                mIntent = new Intent(CommonUtils.getContext(), ActivityMyselfContact.class);
                startActivity(mIntent);
                overridePendingTransition(0,0);
                break;
            case R.id.ll_myself_she_question:
                mIntent = new Intent(CommonUtils.getContext(), ActivityMyselfQuestion.class);
                startActivity(mIntent);
                overridePendingTransition(0,0);
                break;
            case R.id.ll_myself_she_code:
                if(!SpTools.getBoolean(CommonUtils.getContext(), Constants.isLogin,false)){
                    mIntent = new Intent(this, ActivityLogin.class);
                    startActivity(mIntent);
                    overridePendingTransition(0,0);
                    return;
                }
                mIntent = new Intent(CommonUtils.getContext(), ActivityMyselfReviseCode.class);
                startActivity(mIntent);
                overridePendingTransition(0, 0);
                break;
            case R.id.ll_myself_she_cache:
                AlertDialog.Builder builder_dialog = new AlertDialog.Builder(this);
                builder_dialog.setTitle("清除缓存")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DataCleanManager.deleteCache(CommonUtils.getContext());
                                tv_cache.setText("0MB");
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
            case R.id.ll_myself_she_update:
                break;
            case R.id.ll_myself_she_quit:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("是否退出登录")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SpTools.setBoolean(CommonUtils.getContext(), Constants.isLogin,false);
                                SpTools.setString(CommonUtils.getContext(),Constants.userId,"");
                                CommonUtils.deleteBitmap("myicon.jpg");
                                FragmentMyself.exit();
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
                break;
        }
    }

    private void finishAll() {
        mIntent = null;
    }
}
