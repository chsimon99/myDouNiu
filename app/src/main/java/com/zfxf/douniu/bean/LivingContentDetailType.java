package com.zfxf.douniu.bean;

/**
 * @author Admin
 * @time 2017/4/7 9:44
 * @des ${TODO}
 */

public class LivingContentDetailType {
    public String type;//类型 0文字 1声音
    public String text;//文字内容
    public String url;//声音url
    public String length;//声音长度

    public boolean isShow;

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }
}
