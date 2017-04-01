package com.zfxf.douniu.utils;

import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.zfxf.douniu.R;
import com.zfxf.douniu.view.InnerView;

import java.util.ArrayList;
import java.util.List;

public class MyLunBo {
    private AutoScrollTask mTask;
    private LinearLayout mLayout;
    private InnerView mInnerView;
    private List<Integer> mLists = new ArrayList<Integer>();

    public MyLunBo(LinearLayout layout, InnerView innerView, List<Integer> lists) {
        mLayout = layout;
        mInnerView = innerView;
        mLists = lists;
    }

    public void startLunBO(){
        reFreshData();
        reFreshHolder();
    }
    public void stopLunBO(){
        if (mTask != null) {
            mTask.stop();
        }
    }
    public void restartLunBO(){
        if (mTask != null) {
            mTask.start();
        }
    }

    private void reFreshData() {
        mLayout.removeAllViews();
        if (mTask != null) {
            mTask.stop();
        }
    }
    private void reFreshHolder() {

        // 添加小点
        for (int i = 0; i < mLists.size(); i++) {
            View indicatorView = new View(CommonUtils.getContext());
            indicatorView.setBackgroundResource(R.drawable.point_gray);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(CommonUtils.dip2px(CommonUtils.getContext(),10)
                    ,CommonUtils.dip2px(CommonUtils.getContext(),10));// dp-->px
            // 左边距
            params.leftMargin = CommonUtils.dip2px(CommonUtils.getContext(),5);
            // 下边距
//            params.bottomMargin = CommonUtils.dip2px(CommonUtils.getContext(),5);
            mLayout.addView(indicatorView, params);
            // 默认选中效果
            if (i == 0) {
                indicatorView.setBackgroundResource(R.drawable.point_white);
            }
        }
        mInnerView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                position = position % mLists.size();
                for (int i = 0; i < mLists.size(); i++) {
                    View indicatorView = mLayout.getChildAt(i);
                    // 还原背景
                    indicatorView.setBackgroundResource(R.drawable.point_gray);
                    if (i == position) {
                        indicatorView.setBackgroundResource(R.drawable.point_white);
                    }
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // 设置curItem为count/2
        int diff = Integer.MAX_VALUE / 2 % mLists.size();
        int index = Integer.MAX_VALUE / 2;
        mInnerView.setCurrentItem(index - diff);

        // 自动轮播
        mTask = new AutoScrollTask();
        mTask.start();
        // 用户触摸的时候移除自动轮播的任务
        mInnerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        mTask.stop();
                        break;
                    case MotionEvent.ACTION_UP:
                        mTask.start();
                        break;
                }
                return false;
            }
        });
    }
    class AutoScrollTask implements Runnable {
        public void start() {/**开始轮播*/
            CommonUtils.postTaskDelay(this, 3000);
        }
        public void stop() { /**结束轮播*/
            CommonUtils.removeTask(this);
        }
        @Override
        public void run() {
//            isRun = !isRun;
            int item = mInnerView.getCurrentItem();
            item++;
            mInnerView.setCurrentItem(item);
            // 结束-->再次开始
            start();
        }
    }

}
