package com.zfxf.douniu.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.zfxf.douniu.R;
import com.zfxf.douniu.bean.LoginResult;
import com.zfxf.douniu.internet.NewsInternetRequest;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.HorizontalProgressBarWithNumber;
import com.zfxf.douniu.utils.SpTools;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

import static com.zfxf.douniu.utils.Constants.userId;

public class ActivityVideoUpload extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.iv_video_upload_upload)
    ImageView iv_upload;
    @BindView(R.id.iv_video_upload_img)
    ImageView iv_img;
    @BindView(R.id.iv_video_upload_delete)
    ImageView iv_delete;
    @BindView(R.id.et_video_upload_title)
    EditText et_title;
    @BindView(R.id.tv_video_upload_upload)
    TextView tv_upload;

    @BindView(R.id.rl_video_upload_img)
    RelativeLayout rl_img;
    @BindView(R.id.rl_video_upload_more)
    RelativeLayout rl_more;

    @BindView(R.id.pb_video_upload)
    HorizontalProgressBarWithNumber mNumber;
    private Bitmap mBitmap;
    private String mPath;
    private boolean isUplode = false;
    private String MYICON = "myshipinPic.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_upload);
        ButterKnife.bind(this);
        title.setText("上传视频");
        edit.setVisibility(View.INVISIBLE);
        initdata();
        initListener();
    }

    private void initdata() {

    }
    private void initListener() {
        back.setOnClickListener(this);
        iv_upload.setOnClickListener(this);
        iv_delete.setOnClickListener(this);
        tv_upload.setOnClickListener(this);
        rl_more.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back:
                finishAll();
                break;
            case R.id.tv_video_upload_upload:
                if(isUplode){
                    return;
                }
                long length = new File(mPath).length();
                if(length > 1024*1024*100l){
                    CommonUtils.toastMessage("上传的视频大于100M，请重新选择");
                    return;
                }
                final String title = et_title.getText().toString();
                if(TextUtils.isEmpty(title)){
                    CommonUtils.toastMessage("请输入视频标题");
                    return;
                }
                String url = getResources().getString(R.string.file_host_address)
                        .concat(getResources().getString(R.string.upload));
                OkHttpUtils.post()
                        .addFile("image[]", "视频.mp4", new File(mPath))
                        .addFile("image[]", MYICON, new File(CommonUtils.getContext().getFilesDir(),MYICON))
                        .url(url)
                        .addParams("sid", "")
                        .addParams("index", "1")
                        .addParams("ub_id", SpTools.getString(CommonUtils.getContext(), userId, "0"))
                        .addParams("uo_long", "")
                        .addParams("uo_lat", "")
                        .addParams("uo_high", "")
                        .build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        CommonUtils.logMes("uplodePicture  e  =" + e);
                        isUplode = false;
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        CommonUtils.logMes("uplodePicture=" + response);
                        Gson mGson = new Gson();
                        LoginResult result = mGson.fromJson(response, LoginResult.class);
                        NewsInternetRequest.UplodeShipin(title, result.file_ids.get(0), result.file_ids.get(1)
                                , new NewsInternetRequest.ForResultListener() {
                                    @Override
                                    public void onResponseMessage(String count) {
                                        if(count.equals("成功")){
                                            mNumber.setVisibility(View.GONE);
                                            isUplode = false;
                                            CommonUtils.toastMessage("上传视频成功");
                                            finishAll();
                                        }
                                    }
                                });
                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        super.inProgress(progress, total, id);
                        if (progress > 0f) {
                            mNumber.setProgress((int) (progress * 100));
                            isUplode = true;
                        }
                    }
                });
                mNumber.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_video_upload_delete:
                rl_img.setVisibility(View.GONE);
                iv_upload.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_video_upload_upload:
                PictureSelector.create(this).openGallery(PictureMimeType.ofVideo())
                        .selectionMode(PictureConfig.SINGLE)//多选 or 单选
                        .previewVideo(true)// 是否可预览视频
                        .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
                break;
            case R.id.rl_video_upload_more:
                Intent intent = new Intent(this,ActivityVideoUploadInfo.class);
                startActivity(intent);
                overridePendingTransition(0,0);
        }
    }

    private void finishAll() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    LocalMedia media = PictureSelector.obtainMultipleResult(data).get(0);
                    mPath = media.getPath();

                    MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                    mmr.setDataSource(media.getPath());
                    //获取第一帧图片
                    mBitmap = mmr.getFrameAtTime();
                    rl_img.setVisibility(View.VISIBLE);
                    iv_upload.setVisibility(View.GONE);
                    iv_img.setImageBitmap(mBitmap);
                    CommonUtils.saveBitmapFile(mBitmap, MYICON);//先保存文件到本地

                    mmr.release();//释放资源
                    break;
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
