package com.zfxf.douniu.view.chart.formatter;


import com.zfxf.douniu.view.chart.impl.IDateTimeFormatter;
import com.zfxf.douniu.view.chart.utils.DateUtil;

import java.util.Date;

/**
 * 时间格式化器
 * Created by tifezh on 2016/6/21.
 */

public class TimeFormatter implements IDateTimeFormatter {
    @Override
    public String format(Date date) {
        if (date == null) {
            return "";
        }
        return DateUtil.shortTimeFormat.format(date);
    }
}
