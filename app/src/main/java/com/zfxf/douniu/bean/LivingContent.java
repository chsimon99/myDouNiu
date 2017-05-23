package com.zfxf.douniu.bean;

import java.util.List;

/**
 * @author Admin
 * @time 2017/4/7 9:44
 * @des ${TODO}
 */

public class LivingContent {
    public String zt_id;//直播ID
    public String zt_name;//直播主题
    public String total;//总页数
    public List<LivingContentDetail> context_list;//直播内容
    public List<LivingInteract> pl_list;//互动内容
}
