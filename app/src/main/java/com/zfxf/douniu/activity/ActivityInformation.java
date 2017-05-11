package com.zfxf.douniu.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zfxf.douniu.R;
import com.zfxf.douniu.bean.NewsInfomationResult;
import com.zfxf.douniu.bean.OtherResult;
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
public class ActivityInformation extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.tv_information_title)
    TextView infoTitle;
    @BindView(R.id.tv_information_from)
    TextView from;
    @BindView(R.id.tv_information_time)
    TextView time;
    @BindView(R.id.tv_information_content)
    TextView content;
    @BindView(R.id.ll_information_text)
    LinearLayout ll_text;
    private int mNewsinfoId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        ButterKnife.bind(this);
        String type_title = getIntent().getStringExtra("type");//传过来的标题
        title.setText(type_title);
        mNewsinfoId = getIntent().getIntExtra("newsinfoId", 0);
        infoTitle.getPaint().setFakeBoldText(true);
        initData();
//        String zp_content = "测试图片信息：<br><img src=\"http://avatar.csdn.net/0/3/8/2_zhang957411207.jpg\" />";
//        Html.ImageGetter imageGetter = new Html.ImageGetter() {
//
//            @Override
//            public Drawable getDrawable(final String source) {
//                CommonUtils.logMes("source="+source);
////                int id = Integer.parseInt(source);
////                Drawable drawable = getResources().getDrawable(id);
//                final Drawable[] drawable = {null};
//                BaseApplication.getThreadPool().submit(new Thread(){
//                    @Override
//                    public void run() {
//                        Bitmap bitmap = null;
//                        try {
//                            bitmap = Glide.with(ActivityInformation.this).load(source).asBitmap().into(500, 500).get();
//                            CommonUtils.logMes("bitmap="+bitmap);
//                            drawable[0] = new BitmapDrawable(bitmap);
//                            drawable[0].setBounds(0, 0, drawable[0].getIntrinsicWidth(), drawable[0].getIntrinsicHeight());
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//                return drawable[0];
//            }
//        };
//
//        Spanned spanned = Html.fromHtml(zp_content, imageGetter, null);
//        content.setText(spanned);
        initListener();
    }
    private void initData() {
        CommonUtils.showProgressDialog(this,"加载中……");
        if(mNewsinfoId != 0){
            NewsInternetRequest.getNewsInformation(mNewsinfoId, new NewsInternetRequest.ForResultNewsInfoListener() {
                @Override
                public void onResponseMessage(OtherResult otherResult) {
                    NewsInfomationResult info = otherResult.news_info;
                    infoTitle.setText(info.cc_title);
                    if (TextUtils.isEmpty(info.cc_from)){
                        from.setVisibility(View.GONE);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2,-2);
                        params.leftMargin = 0;
                        ll_text.removeAllViews();
                        ll_text.addView(time,params);
                    }else {
                        from.setText(info.cc_from);
                    }
                    time.setText(info.cc_datetime);
                    content.setText(Html.fromHtml(info.cc_context));
                    CommonUtils.dismissProgressDialog();
                }
            },getResources().getString(R.string.zixuninfo));
        }
    }
    private void initListener() {
        back.setOnClickListener(this);
        edit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back:
                finishAll();
                finish();
                break;
            case R.id.iv_base_edit:

                break;
        }
    }

    private void finishAll() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CommonUtils.dismissProgressDialog();
    }
}
