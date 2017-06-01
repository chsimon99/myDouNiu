package com.zfxf.douniu.view.chart.EntityImpl;

/**
 * Created by tifezh on 2016/6/10.
 */

public interface VoLImpl {

    public float getMA5Vol();

    public float getMA10Vol();

    public float getMA20Vol();

    public float getVolume();

    /**
     * 开盘价
     * @return
     */
    public float getOpenPrice();

    /**
     * 收盘价
     * @return
     */
    public float getClosePrice();
}
