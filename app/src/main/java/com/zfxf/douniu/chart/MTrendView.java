package com.zfxf.douniu.chart;

import android.content.Context;
import android.util.AttributeSet;

import com.zfxf.douniu.view.chart.BaseKChart;
import com.zfxf.douniu.view.chart.draw.MINDraw;
import com.zfxf.douniu.view.chart.draw.VolDraw;

import static com.zfxf.douniu.view.chart.utils.ViewUtil.Dp2Px;

/**
 * 分时
 * Created by silladus on 2017/3/7.
 */

public class MTrendView extends BaseKChart {
    public static final int PADDING_VALUE = 5;
    public MTrendView(Context context) {
        this(context, null);
    }

    public MTrendView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MTrendView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTopPadding = Dp2Px(context, PADDING_VALUE);
        addChildDraw("VOL", new VolDraw(getContext()));
        setMainDraw(new MINDraw(getContext()));
    }

    @Override
    public void onLeftSide() {

    }

    @Override
    public void onRightSide() {

    }

//    @Override
//    public boolean onSingleTapUp(MotionEvent e) {
//        Activity activity = (Activity) getContext();
//        boolean isVertical = (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
//        if (isVertical) {
//            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        } else {
//            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
//        }
//        return true;
//    }
}
