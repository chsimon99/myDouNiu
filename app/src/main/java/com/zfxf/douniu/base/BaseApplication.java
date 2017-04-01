package com.zfxf.douniu.base;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

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

    @Override
    public void onCreate() {
        mContext = getApplicationContext();// 上下文
        mMainThread = Thread.currentThread(); // 主线程
        mMainTreadId = android.os.Process.myTid();// 主线程Id
        mMainLooper = getMainLooper(); // 主线程Looper对象
        mHandler = new Handler();// 定义一个handler
        super.onCreate();
    }
}
