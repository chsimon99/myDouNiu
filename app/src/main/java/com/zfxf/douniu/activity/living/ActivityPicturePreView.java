package com.zfxf.douniu.activity.living;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.github.chrisbanes.photoview.PhotoView;
import com.zfxf.douniu.R;
import com.zfxf.douniu.utils.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author IMXU
 * @time   2017/5/3 13:20
 * @des    直播的图片放大
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
            CommonUtils.logMes("----"+picUrl);
//            Glide.with(this).load(picUrl).into(mPhotoView);
            Glide.with(this).load(picUrl).into(new SimpleTarget<GlideDrawable>() {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    //photoview重写了设置图片的方法 ，所以不能像普通的imageview去对待
                    mPhotoView.setImageDrawable(resource);
                }
            });
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
