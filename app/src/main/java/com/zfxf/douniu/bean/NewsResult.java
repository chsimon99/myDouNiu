package com.zfxf.douniu.bean;

import java.util.List;

/**
 * @author Admin
 * @time 2017/4/7 9:44
 * @des ${TODO}
 */

public class NewsResult {
    public BasicResult result;//基础结果
    public String total;//总页数
    public List<NewsNewsResult> news_list;//资讯列表数据
    public List<AskDone> have_answer;//我的问答已回答详情
    public List<AskWait> no_answer;//我的问答未回答详情
}
