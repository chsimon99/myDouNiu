package com.zfxf.douniu.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author Admin
 * @time 2017/2/13 15:06
 * @des ${TODO}
 */

public class InnerView extends ViewPager {

    private float	mDownX;
    private float	mDownY;
    private float	mMoveX;
    private float	mMoveY;

    public InnerView(Context context) {
        super(context);
    }

    public InnerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
//        // 左右滑动-->自己处理   上下滑动-->父亲处理
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                mDownX = ev.getRawX();
//                mDownY = ev.getRawY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                mMoveX = ev.getRawX();
//                mMoveY = ev.getRawY();
//
//                int diffX = (int) (mMoveX - mDownX);
//                int diffY = (int) (mMoveY - mDownY);
//                // 左右滚动的绝对值 > 上下滚动的绝对值
//                if (Math.abs(diffX) > Math.abs(diffY)) {// 左右
//                    getParent().requestDisallowInterceptTouchEvent(true);
//                } else {// 上下
//                    getParent().requestDisallowInterceptTouchEvent(false);
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//
//                break;
//            case MotionEvent.ACTION_CANCEL:
//
//                break;
//
//            default:
//                break;
//        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // 左右滑动-->自己处理   上下滑动-->父亲处理
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getRawX();
                mDownY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                mMoveX = ev.getRawX();
                mMoveY = ev.getRawY();

                int diffX = (int) (mMoveX - mDownX);
                int diffY = (int) (mMoveY - mDownY);
                // 左右滚动的绝对值 > 上下滚动的绝对值
                if (Math.abs(diffX) > Math.abs(diffY)) {// 左右
                    getParent().requestDisallowInterceptTouchEvent(true);
                } else {// 上下
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:

                break;
            case MotionEvent.ACTION_CANCEL:

                break;

            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
