package com.zfxf.douniu.view.chart.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.zfxf.douniu.view.chart.EntityImpl.VoLImpl;
import com.zfxf.douniu.view.chart.impl.IKChartView;

/**
 * @author IMXU
 * @time 2017/5/31 17:51
 * @des ${TODO}
 * 邮箱：butterfly_xu@sina.com
 */

public class VolDraw extends BaseDraw<VoLImpl> {

    public VolDraw(Context context) {
        super(context);
    }

    @Override
    public void drawTranslated(@Nullable VoLImpl lastPoint, @NonNull VoLImpl curPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull IKChartView view, int position) {
        drawCandle(view, canvas, curX, curPoint.getOpenPrice(), curPoint.getClosePrice(),curPoint.getVolume());
        view.drawChildLine(canvas, ma5Paint, lastX, lastPoint.getMA5Vol(), curX, curPoint.getMA5Vol());
        view.drawChildLine(canvas, ma10Paint, lastX, lastPoint.getMA10Vol(), curX, curPoint.getMA10Vol());
    }

    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull IKChartView view, int position, float x, float y) {
        String text = "";
        VoLImpl point = (VoLImpl) view.getItem(position);
        text = "VOL:" + view.formatValue(point.getVolume()) + " ";
        canvas.drawText(text, x, y, ma20Paint);
        x += ma5Paint.measureText(text);
        text = "MA5:" + view.formatValue(point.getMA5Vol()) + " ";
        canvas.drawText(text, x, y, ma5Paint);
        x += ma10Paint.measureText(text);
        text = "MA10:" + view.formatValue(point.getMA10Vol()) + " ";
        canvas.drawText(text, x, y, ma10Paint);
    }

    @Override
    public float getMaxValue(VoLImpl point) {
        return Math.max(point.getVolume(), Math.max(point.getMA5Vol(), point.getMA10Vol()));
    }

    @Override
    public float getMinValue(VoLImpl point) {
        return 0;
    }

    @Override
    public float getMaxPrice(VoLImpl point) {
        return 0;
    }

    @Override
    public float getMinPrice(VoLImpl point) {
        return 0;
    }
    /**
     * 画Candle
     *
     * @param canvas
     * @param x      x轴坐标
     * @param open   开盘价
     * @param close  收盘价
     * @param vol  成交量
     */
    private void drawCandle(IKChartView view, Canvas canvas, float x, float open, float close,float vol) {
        open = view.getMainY(open);
        close = view.getMainY(close);
        vol = view.getChildY(vol);
        int r = mCandleWidth / 2;
        if (open > close) {
            //实心
            canvas.drawRect(x - r, vol, x + r, view.getChildY(0), redPaint);
        } else if (open < close) {
            canvas.drawRect(x - r, vol, x + r, view.getChildY(0), greenPaint);
        } else {
            canvas.drawRect(x - r, vol, x + r, view.getChildY(0), redPaint);
        }
    }
}
