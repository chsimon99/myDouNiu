package com.zfxf.douniu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.ImageView;

import com.zfxf.douniu.activity.MainActivityTabHost;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.iv_activity_main)
    ImageView mImageView;
    int i = 0;
    private Timer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mTimer = new Timer();//计时器 3秒后关闭
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(i >= 3){
                    toFinish();
                }
                i++;
            }
        },0,1000);
    }

    private void toFinish() {
        if(mTimer !=null){
            mTimer.cancel();
            mTimer = null;
        }
        Intent intent = new Intent(this, MainActivityTabHost.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        finish();
    }

}
