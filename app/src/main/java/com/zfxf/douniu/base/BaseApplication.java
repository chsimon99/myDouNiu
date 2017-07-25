package com.zfxf.douniu.base;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * @author Admin
 * @time 2017/2/10 12:40
 * @des ${TODO}
 */

public class BaseApplication extends Application {
    private static Context mContext;
    private static Thread	mMainThread;
    private static long		mMainTreadId;
    private static Looper	mMainLooper;
    private static Handler mHandler;
    private static ExecutorService threadPool;
    private static final int DEFAULT_TIMEOUT = 10;

    public static Handler getHandler() {
        return mHandler;
    }

    public static Context getContext() {
        return mContext;
    }

    public static Thread getMainThread() {
        return mMainThread;
    }

    public static long getMainTreadId() {
        return mMainTreadId;
    }

    public static Looper getMainThreadLooper() {
        return mMainLooper;
    }

    public static ExecutorService getThreadPool() {
        return threadPool;
    }

    @Override
    public void onCreate() {
        mContext = getApplicationContext();// 上下文
        mMainThread = Thread.currentThread(); // 主线程
        mMainTreadId = android.os.Process.myTid();// 主线程Id
        mMainLooper = getMainLooper(); // 主线程Looper对象
        mHandler = new Handler();// 定义一个handler
        threadPool = Executors.newCachedThreadPool();//定义一个线程池
        PlatformConfig.setWeixin("wx086c758d5e01f463","0ffe0ba97f1480d02ae91f9c1fbdea7a");
//        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
        PlatformConfig.setSinaWeibo("1211225805", "0a1b01274b5c4d78e177a6d1ee3717c6","http://sns.whalecloud.com/sina2/callback");
        Config.DEBUG = true;

        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        UMShareAPI.get(this).setShareConfig(config);

        String path = mContext.getCacheDir().getAbsolutePath();
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT,TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .cache(new Cache(new File(path,"cache"),cacheSize))
                .build();
        OkHttpUtils.initClient(client);
        super.onCreate();
    }
}
