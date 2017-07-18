package com.zfxf.douniu.chart;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.zfxf.douniu.view.chart.BaseKChart;
import com.zfxf.douniu.view.chart.draw.BOLLDraw;
import com.zfxf.douniu.view.chart.draw.KDJDraw;
import com.zfxf.douniu.view.chart.draw.MACDDraw;
import com.zfxf.douniu.view.chart.draw.MainDraw;
import com.zfxf.douniu.view.chart.draw.RSIDraw;
import com.zfxf.douniu.view.chart.draw.VolDraw;


/**
 * k线图
 * Created by tian on 2016/5/20.
 */
public class KChartView extends BaseKChart {
    private Context context;
    public KChartView(Context context) {
        this(context, null);
    }

    public KChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        addChildDraw("VOL", new VolDraw(getContext()).withMainChart(true));
        addChildDraw("MACD", new MACDDraw(getContext()));
        addChildDraw("KDJ", new KDJDraw(getContext()));
        addChildDraw("RSI", new RSIDraw(getContext()));
        addChildDraw("BOLL", new BOLLDraw(getContext()));
        setMainDraw(new MainDraw(getContext()));
    }

    @Override
    public void onLeftSide() {
//        Log.d("--LeftSide---","最新的数据没有了");
        //滑到了最左边
        mListener.leftSide();
    }

    @Override
    public void onRightSide() {
//        Log.d("--RightSide---","最新的数据没有了");
        //滑到了最右边
        mListener.rightSide();
    }

    private float	mDownX;
    private float	mDownY;
    private float	mMoveX;
    private float	mMoveY;
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
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
        return super.onTouchEvent(ev);
    }
    private static MyStockListener mListener;
    public interface MyStockListener{
        void leftSide();
        void rightSide();
    }
    public static void setStockListener(MyStockListener listener){
        mListener = listener;
    }

}
