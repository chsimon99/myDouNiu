package com.zfxf.douniu.bean.newsexpress;

import java.util.List;

/**
 * @author Admin
 * @time 2017/4/7 9:41
 * @des ${TODO}
 */

public class NewsExpressResult {

    public int error;//错误id
    public String msg;//信息
    public String c;//来源
    public String d;//时间
    public List<NewsExpressItem> item;//新闻信息
}
