package com.zfxf.douniu.view.chart.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.zfxf.douniu.R;
import com.zfxf.douniu.view.chart.EntityImpl.VoLImpl;
import com.zfxf.douniu.view.chart.impl.IKChartView;

/**
 * @author IMXU
 * @time 2017/5/31 17:51
 * @des ${TODO}
 * 邮箱：butterfly_xu@sina.com
 */

public class VolDraw extends BaseDraw<VoLImpl> {
    private boolean isMainChart;
    public VolDraw(Context context) {
        super(context);
    }

    @Override
    public void drawTranslated(@Nullable VoLImpl lastPoint, @NonNull VoLImpl curPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull IKChartView view, int position) {
        drawCandle(view, canvas,lastX, curX,curPoint);
        if (isMainChart) {
            if (position > 4) {
                view.drawChildLine(canvas, ma5Paint, lastX, lastPoint.getMA5Vol(), curX, curPoint.getMA5Vol());
            }
            if (position > 9) {
                view.drawChildLine(canvas, ma10Paint, lastX, lastPoint.getMA10Vol(), curX, curPoint.getMA10Vol());
            }
        }
    }

    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull IKChartView view, int position, float x, float y) {
        String text = "";
        VoLImpl point = (VoLImpl) view.getItem(position);

        if (point.getVolume() > 1000000) {
            text = "VOL:" + view.formatValue(point.getVolume() / 1000000f) + "万手 ";
        } else {
            text = "VOL:" + view.formatValue(point.getVolume() / 100f) + "手 ";
        }
        canvas.drawText(text, x, y, ma20Paint);
        if (isMainChart) {
            x += ma5Paint.measureText(text);
            text = "MA5:" + view.formatValue(point.getMA5Vol() / 1000000f) + "万手 ";
            canvas.drawText(text, x, y, ma5Paint);
            x += ma10Paint.measureText(text);
            text = "MA10:" + view.formatValue(point.getMA10Vol() / 1000000f) + "万手 ";
            canvas.drawText(text, x, y, ma10Paint);
        }
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


    private void drawCandle(IKChartView view, Canvas canvas, float lastX, float cutX, VoLImpl curPoint) {
        float closeDif = curPoint.getCloseDif();
        float vol = view.getChildY(curPoint.getVolume());
        float eX = cutX - lastX;
        float r = eX / 2;
        if (isMainChart) {
            r = mCandleWidth / 2;
            if (closeDif < 0) {
                canvas.drawRect(cutX - r, vol, cutX + r, view.getChildY(0), greenPaint);
            } else if (closeDif == 0) {
                if (curPoint.getRate()) {
                    canvas.drawRect(cutX - r, vol, cutX + r, view.getChildY(0), redPaint);
                } else {
                    canvas.drawRect(cutX - r, vol, cutX + r, view.getChildY(0), greenPaint);
                }
            } else {
                canvas.drawRect(cutX - r, vol, cutX + r, view.getChildY(0), redPaint);
            }
        } else {
            if (closeDif < 0) {
                canvas.drawRect(cutX - r, vol, cutX + r, view.getChildY(0), greenPaint);
            } else if (closeDif == 0) {
                redPaint.setColor(Color.LTGRAY);
                canvas.drawRect(cutX - r, vol, cutX + r, view.getChildY(0), redPaint);
            } else {
                redPaint.setColor(mContext.getResources().getColor(R.color.chart_red));
                canvas.drawRect(cutX - r, vol, cutX + r, view.getChildY(0), redPaint);
            }
        }
    }

    public VolDraw withMainChart(boolean isMainChart) {
        this.isMainChart = isMainChart;
        return this;
    }
}
