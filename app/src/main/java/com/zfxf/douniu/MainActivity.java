package com.zfxf.douniu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zfxf.douniu.activity.MainActivityTabHost;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, MainActivityTabHost.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        finish();
    }
}
