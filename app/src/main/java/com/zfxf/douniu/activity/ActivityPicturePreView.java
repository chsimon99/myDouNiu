package com.zfxf.douniu.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zfxf.douniu.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;

/**
 * @author IMXU
 * @time   2017/5/3 13:20
 * @des    微问答免责
 * 邮箱：butterfly_xu@sina.com
 *
*/
public class ActivityPicturePreView extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;
    @BindView(R.id.pv_picture_preview)
    PhotoView mPhotoView;
    private String mUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_preview);
        ButterKnife.bind(this);
        title.setText("1/1");
        edit.setVisibility(View.INVISIBLE);
        mUrl = getIntent().getStringExtra("url");
        initdata();
        initListener();
    }

    private void initdata() {
        if(!TextUtils.isEmpty(mUrl)){
            String picUrl = getResources().getString(R.string.file_host_address)
                    +getResources().getString(R.string.showpic)
                    +mUrl;
            Glide.with(this).load(picUrl).into(mPhotoView);
        }
    }
    private void initListener() {
        back.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back:
                finishAll();
                finish();
                break;
        }
    }

    private void finishAll() {

    }
}
