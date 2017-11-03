package com.zfxf.douniu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMWeb;
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
    @BindView(R.id.iv_base_share)
    ImageView share;

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
    @BindView(R.id.wv_information)
    WebView mWebView;
    private int mNewsinfoId;
    private String mWeburl;//H5的url
    private String datetime;//时间
    private String mFrom;//新闻来源
    private String mTitle;//新闻标题

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        ButterKnife.bind(this);
        String type_title = getIntent().getStringExtra("type");//传过来的标题
        title.setText(type_title);
        mNewsinfoId = getIntent().getIntExtra("newsinfoId", 0);
        infoTitle.getPaint().setFakeBoldText(true);
        edit.setVisibility(View.INVISIBLE);
        share.setVisibility(View.VISIBLE);
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
        if(mNewsinfoId != 0){
            CommonUtils.showProgressDialog(this,"加载中……");
            NewsInternetRequest.getNewsInformation(mNewsinfoId, new NewsInternetRequest.ForResultNewsInfoListener() {
                @Override
                public void onResponseMessage(OtherResult otherResult) {
                    NewsInfomationResult info = otherResult.news_info;
                    mTitle = info.cc_title;
                    infoTitle.setText(mTitle);
                    if (TextUtils.isEmpty(info.cc_from)){
                        from.setVisibility(View.GONE);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2,-2);
                        params.leftMargin = 0;
                        ll_text.removeAllViews();
                        ll_text.addView(time,params);
                    }else {
                        mFrom = info.cc_from;
                        from.setText(mFrom);
                    }
                    datetime = info.cc_datetime;
                    time.setText(datetime);
//                    content.setText(Html.fromHtml(info.cc_context));
                    mWeburl = info.context_url;
                    mWebView.loadUrl(mWeburl);
                    CommonUtils.dismissProgressDialog();
                }
            },getResources().getString(R.string.zixuninfo));
        }
    }
    private void initListener() {
        back.setOnClickListener(this);
        edit.setOnClickListener(this);
        share.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back:
                finishAll();
                finish();
                break;
            case R.id.iv_base_share:
                UMWeb web = new UMWeb(mWeburl);
                web.setTitle(mTitle);//标题
                web.setDescription(mFrom+" "+datetime);//描述
                new ShareAction(ActivityInformation.this).withText("斗牛财经")
                        .withMedia(web)
                        .setDisplayList(SHARE_MEDIA.SINA,SHARE_MEDIA.QQ,SHARE_MEDIA.QZONE,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE)
                        .setCallback(shareListener).open();
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
    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调 @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
        }
        /**
         * @descrption 分享成功的回调 @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(CommonUtils.getContext(),"分享成功了",Toast.LENGTH_LONG).show();
        }
        /**
         * @descrption 分享失败的回调 @param platform 平台类型 @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(CommonUtils.getContext(),"分享失败"+t.getMessage(),Toast.LENGTH_LONG).show();
        }
        /**
         * @descrption 分享取消的回调 @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
//            Toast.makeText(CommonUtils.getContext(),"分享取消了",Toast.LENGTH_LONG).show();
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data);
    }
}
