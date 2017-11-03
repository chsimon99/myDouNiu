package com.zfxf.douniu.view.chart.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.zfxf.douniu.R;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.view.chart.EntityImpl.MINImpl;
import com.zfxf.douniu.view.chart.impl.IKChartView;


/**
 * 分时图的实现类
 * Created by lianming on 2017/3/7.
 */

public class MINDraw extends BaseDraw<MINImpl> {

    private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mSelectorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**
     * 构造方法
     *
     * @param context
     */
    public MINDraw(Context context) {
        super(context);
        mTextPaint.setColor(context.getResources().getColor(R.color.chart_text));
        mTextPaint.setTextSize(context.getResources().getDimension(R.dimen.chart_selector_text_size));
        mSelectorPaint.setColor(context.getResources().getColor(R.color.chart_selector));
        mSelectorPaint.setAlpha(200);
        ma5Paint.setColor(context.getResources().getColor(R.color.order_tab_select));
        ma10Paint.setColor(context.getResources().getColor(R.color.price_av));
    }

    @Override
    public void drawTranslated(@Nullable MINImpl lastPoint, @NonNull MINImpl curPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull IKChartView view, int position) {

        //画实价
        if (lastPoint.getClosePrice() != 0) {
//            drawCandle(view, canvas,lastX, curX,curPoint);//画阴影，不过画的顺序会覆盖底部的格子
            view.drawMainLine(canvas, ma5Paint, lastX, lastPoint.getClosePrice(), curX, curPoint.getClosePrice());
        }
        //画均价
        if (lastPoint.getAVPrice() != 0) {
            view.drawMainLine(canvas, ma10Paint, lastX, lastPoint.getAVPrice(), curX, curPoint.getAVPrice());
        }
    }

    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull IKChartView view, int position, float x, float y) {
//        MINImpl point = (MINImpl) view.getItem(position);
//        String text = "MA5:" + view.formatValue(point.getClosePrice()) + " ";
//        canvas.drawText(text, x, y, ma5Paint);
//        x += ma5Paint.measureText(text);
//        text = "MA10:" + view.formatValue(point.getAVPrice()) + " ";
//        canvas.drawText(text, x, y, ma10Paint);
//        x += ma10Paint.measureText(text);
    }
    private void drawCandle(IKChartView view, Canvas canvas, float lastX, float cutX, @NonNull MINImpl curPoint){
        float eX = cutX - lastX;
        float r = eX / 2;
        float pointX = cutX - r;
        redPaint.setColor(mContext.getResources().getColor(R.color.bg_color));
        canvas.drawRect(pointX - r, view.getMainPoint(curPoint.getClosePrice())
                + CommonUtils.px2dip(CommonUtils.getContext(),5), pointX + r, view.getMainBottom(), redPaint);
    }


    @Override
    public float getMaxValue(MINImpl point) {
        return point.getLastClosePrice() + Math.max(Math.abs(point.getHighPrice() - point.getLastClosePrice()), Math.abs(point.getLowPrice() - point.getLastClosePrice()));
    }

    @Override
    public float getMinValue(MINImpl point) {
        return point.getLastClosePrice() - Math.max(Math.abs(point.getHighPrice() - point.getLastClosePrice()), Math.abs(point.getLowPrice() - point.getLastClosePrice()));
    }

    @Override
    public float getMaxPrice(MINImpl point) {
        return 0;
    }

    @Override
    public float getMinPrice(MINImpl point) {
        return 0;
    }

}
