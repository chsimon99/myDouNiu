package com.zfxf.douniu.view.chart.formatter;


import com.zfxf.douniu.view.chart.impl.IValueFormatter;

/**
 * Value格式化类
 * Created by tifezh on 2016/6/21.
 */

public class ValueFormatter implements IValueFormatter {
    @Override
    public String format(float value) {
        if(value < 10000){
            return String.format("%.2f", value);
        }else {

            return String.format("%.2f"+"万", value/10000);
        }
    }
}
