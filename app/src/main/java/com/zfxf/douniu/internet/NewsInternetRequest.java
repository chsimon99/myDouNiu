package com.zfxf.douniu.internet;

import android.content.Context;

import com.google.gson.Gson;
import com.zfxf.douniu.R;
import com.zfxf.douniu.bean.NewsListResult;
import com.zfxf.douniu.bean.NewsNewsResult;
import com.zfxf.douniu.bean.NewsResult;
import com.zfxf.douniu.bean.OtherResult;
import com.zfxf.douniu.bean.ProjectListResult;
import com.zfxf.douniu.bean.ZanResult;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.SpTools;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * @author Admin
 * @time 2017/4/7 9:56
 * @des ${TODO}
 */

public class NewsInternetRequest {

    private static int index = 0;
    private static Context context;
    private static Gson mGson;
    private static ForResultPolicyInfoListener policyInfoListener;
    private static ForResultEventInfoListener eventInfoListener;
    private static ForResultPointInfoListener pointInfoListener;
    private static ForResultNewsInfoListener newsInfoListener;
    private static ForResultListener resultListener;
    static {
        mGson = new Gson();
        context = CommonUtils.getContext();
    }

    /**
     * 获取好项目列表
     * @param page 访问页数
     * @param policylistener
     */
    public static void getProjectListInformation( String page, ForResultPolicyInfoListener policylistener) {
        String url = context.getResources().getString(R.string.service_host_address)
                .concat(context.getResources().getString(R.string.projectlist));
        policyInfoListener = policylistener;
        final List<Map<String,String>> result_lists = new ArrayList<>();
        OkHttpUtils.post().url(url)
                .addParams("sid","")
                .addParams("index",(index++)+"")
                .addParams("ub_id",SpTools.getString(context, Constants.userId,"0"))
                .addParams("uo_long","")
                .addParams("uo_lat","")
                .addParams("uo_high","")
                .addParams("page",page)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("getProjectListInformation="+e);
            }

            @Override

            public void onResponse(String response, int id) {
                CommonUtils.logMes("getProjectListInformation="+response);
                OtherResult result = mGson.fromJson(response, OtherResult.class);
                List<ProjectListResult> lists = result.project_list;
                String total = result.total;
                for (ProjectListResult list :lists) {
                    Map<String,String> map = new HashMap<>();
                    map.put("cc_id",list.cc_id);
                    map.put("cc_title",list.cc_title);
                    map.put("cc_datetime",list.cc_datetime);
                    map.put("cc_fielid",list.cc_fielid);
                    map.put("biaoshi",list.biaoshi);
                    result_lists.add(map);
                }
                policyInfoListener.onResponseMessage(result_lists,total);
            }
        });
    }

    /**
     *   获取头条列表
     * @param headType  列表类型 0政策 1事件 2观点
     * @param page  访问页数
     * @param policylistener 政策listner
     * @param eventlistener  事件listner
     * @param pointlistener  观点listner
     */
    public static void getHeadListInformation(final int headType, String page, ForResultPolicyInfoListener policylistener
            , ForResultEventInfoListener eventlistener, ForResultPointInfoListener pointlistener) {
        String url = context.getResources().getString(R.string.service_host_address)
                .concat(context.getResources().getString(R.string.headlist));
        if(policylistener !=null){
            policyInfoListener = policylistener;
        }
        if(eventlistener !=null){
            eventInfoListener = eventlistener;
        }
        if(pointlistener !=null){
            pointInfoListener = pointlistener;
        }
        final List<Map<String,String>> result_lists = new ArrayList<>();
        OkHttpUtils.post().url(url)
                .addParams("sid","")
                .addParams("index",(index++)+"")
                .addParams("ub_id",SpTools.getString(context, Constants.userId,"0"))
                .addParams("uo_long","")
                .addParams("uo_lat","")
                .addParams("uo_high","")
                .addParams("type",headType+"")
                .addParams("page",page)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("getHeadListInformation="+e);
                if(headType == 0){
                    policyInfoListener.onResponseMessage(null,0+"");
                }else if(headType == 1){
                    eventInfoListener.onResponseMessage(null,0+"");
                }else if(headType == 2){
                    pointInfoListener.onResponseMessage(null,0+"");
                }
            }

            @Override

            public void onResponse(String response, int id) {
                CommonUtils.logMes("getHeadListInformation="+response);
                OtherResult result = mGson.fromJson(response, OtherResult.class);
                List<NewsListResult> lists = result.news_list;
                String total = result.total;
                for (NewsListResult list :lists) {
                    Map<String,String> map = new HashMap<>();
                    map.put("cc_id",list.cc_id);
                    map.put("cc_title",list.cc_title);
                    map.put("cc_datetime",list.cc_datetime);
                    map.put("cc_count",list.cc_count);
                    map.put("headImg",list.headImg);
                    map.put("ud_nickname",list.ud_nickname);
                    map.put("cc_description",list.cc_description);
                    result_lists.add(map);
                }
                if(headType == 0){
                    policyInfoListener.onResponseMessage(result_lists,total);
                }else if(headType == 1){
                    eventInfoListener.onResponseMessage(result_lists,total);
                }else if(headType == 2){
                    pointInfoListener.onResponseMessage(result_lists,total);
                }
            }
        });
    }

    /**
     *  获取新闻详情
     * @param newsinfoId 新闻ID
     * @param listener listener
     */
    public static void getNewsInformation(int newsinfoId,ForResultNewsInfoListener listener,String html) {
        String url = context.getResources().getString(R.string.service_host_address)
                .concat(html);
        newsInfoListener = listener;
        OkHttpUtils.post().url(url)
                .addParams("sid","")
                .addParams("index",(index++)+"")
                .addParams("ub_id",SpTools.getString(context, Constants.userId,"0"))
                .addParams("uo_long","")
                .addParams("uo_lat","")
                .addParams("uo_high","")
                .addParams("cc_id",newsinfoId+"")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("getNewsInformation="+e);
            }

            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("getNewsInformation="+response);
                OtherResult result = mGson.fromJson(response, OtherResult.class);
                newsInfoListener.onResponseMessage(result);
            }
        });
    }

    /**
     * 发表评论
     * @param newsinfoId 评论的新闻ID
     * @param str 评论的内容
     * @param listener
     */
    public static void publishComment(int newsinfoId, String str,ForResultNewsInfoListener listener) {
        String url = context.getResources().getString(R.string.service_host_address)
                .concat(context.getResources().getString(R.string.newsfbpl));
        newsInfoListener = listener;
        OkHttpUtils.post().url(url)
                .addParams("sid","")
                .addParams("index",(index++)+"")
                .addParams("ub_id",SpTools.getString(context, Constants.userId,"0"))
                .addParams("uo_long","")
                .addParams("uo_lat","")
                .addParams("uo_high","")
                .addParams("ccp_cc_id",newsinfoId+"")
                .addParams("ccp_cs_id",0+"")
                .addParams("ccp_info",str)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("publishComment="+e);
                CommonUtils.toastMessage("发表评论失败，请重试");
            }

            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("publishComment="+response);
                OtherResult result = mGson.fromJson(response, OtherResult.class);
                if(result.result.code.equals("02")){
                    CommonUtils.toastMessage("发表评论失败，请重试");
                }else{
                    newsInfoListener.onResponseMessage(result);
                }
            }
        });
    }

    /**
     * 点赞
     * @param newsinfoId 新闻ID
     * @param listener
     */
    public static void dianZan(int newsinfoId,ForResultListener listener) {
        String url = context.getResources().getString(R.string.service_host_address)
                .concat(context.getResources().getString(R.string.newsdz));
        resultListener = listener;
        OkHttpUtils.post().url(url)
                .addParams("sid","")
                .addParams("index",(index++)+"")
                .addParams("ub_id",SpTools.getString(context, Constants.userId,"0"))
                .addParams("uo_long","")
                .addParams("uo_lat","")
                .addParams("uo_high","")
                .addParams("cc_id",newsinfoId+"")
                .addParams("cc_agr",1+"")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("dianZan="+e);
                CommonUtils.toastMessage("点赞失败，请重试");
            }

            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("dianZan="+response);
                ZanResult zanResult = mGson.fromJson(response, ZanResult.class);
                if(zanResult.result.code.equals("02")){
                    CommonUtils.toastMessage("点赞失败，请重试");
                }else{
                    resultListener.onResponseMessage(zanResult.cc_agr);
                }
            }
        });
    }

    /**
     * 获取资讯列表信息
     * @param headType 列表类型 0精选 1快讯 2公告
     * @param page 访问页数
     * @param lastPageId
     * @param policylistener
     * @param eventlistener
     * @param pointlistener
     */
    public static void getNewsListInformation(final int headType, String page, String lastPageId,ForResultPolicyInfoListener policylistener
            , ForResultEventInfoListener eventlistener, ForResultPointInfoListener pointlistener) {
        String url = context.getResources().getString(R.string.service_host_address)
                .concat(context.getResources().getString(R.string.zixunlist));
        if(policylistener !=null){
            policyInfoListener = policylistener;
        }
        if(eventlistener !=null){
            eventInfoListener = eventlistener;
        }
        if(pointlistener !=null){
            pointInfoListener = pointlistener;
        }
        final List<Map<String,String>> result_lists = new ArrayList<>();
        OkHttpUtils.post().url(url)
                .addParams("sid","")
                .addParams("index",(index++)+"")
                .addParams("ub_id",SpTools.getString(context, Constants.userId,"0"))
                .addParams("uo_long","")
                .addParams("uo_lat","")
                .addParams("uo_high","")
                .addParams("type",headType+"")
                .addParams("page",page)
                .addParams("last_cc_id",lastPageId)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("getNewsListInformation="+e);
                if(headType == 0){
                    policyInfoListener.onResponseMessage(null,0+"");
                }else if(headType == 1){
                    eventInfoListener.onResponseMessage(null,0+"");
                }else if(headType == 2){
                    pointInfoListener.onResponseMessage(null,0+"");
                }
            }

            @Override

            public void onResponse(String response, int id) {
                CommonUtils.logMes("getNewsListInformation="+response);
                NewsResult result = mGson.fromJson(response, NewsResult.class);
                List<NewsNewsResult> lists = result.news_list;
                String total = result.total;
                for (NewsNewsResult list :lists) {
                    Map<String,String> map = new HashMap<>();
                    map.put("cc_id",list.cc_id);
                    map.put("cc_title",list.cc_title);
                    map.put("cc_datetime",list.cc_datetime);
                    map.put("cc_from",list.cc_from);
                    map.put("cc_fielid",list.cc_fielid);
                    map.put("this_date",list.this_date);
                    map.put("cc_context",list.cc_context);
                    result_lists.add(map);
                }
                if(headType == 0){
                    policyInfoListener.onResponseMessage(result_lists,total);
                }else if(headType == 1){
                    eventInfoListener.onResponseMessage(result_lists,total);
                }else if(headType == 2){
                    pointInfoListener.onResponseMessage(result_lists,total);
                }
            }
        });
    }

    /**
     * 获取斗牛吧 列表
     * @param headType 0:热度 1:大盘 2:杂谈
     * @param page 访问页数
     * @param policylistener
     */
    public static void getBarListInformation(final int headType, String page, ForResultPolicyInfoListener policylistener) {
        String url = context.getResources().getString(R.string.service_host_address)
                .concat(context.getResources().getString(R.string.douniubarlist));
        policyInfoListener = policylistener;
        final List<Map<String,String>> result_lists = new ArrayList<>();
        OkHttpUtils.post().url(url)
                .addParams("sid","")
                .addParams("index",(index++)+"")
                .addParams("ub_id",SpTools.getString(context, Constants.userId,"0"))
                .addParams("uo_long","")
                .addParams("uo_lat","")
                .addParams("uo_high","")
                .addParams("type",headType+"")
                .addParams("page",page)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("getBarListInformation="+e);
                policyInfoListener.onResponseMessage(null,0+"");
            }

            @Override

            public void onResponse(String response, int id) {
                CommonUtils.logMes("getBarListInformation="+response);
                OtherResult result = mGson.fromJson(response, OtherResult.class);
                List<NewsListResult> lists = result.news_list;
                String total = result.total;
                for (NewsListResult list :lists) {
                    Map<String,String> map = new HashMap<>();
                    map.put("cc_id",list.cc_id);
                    map.put("cc_title",list.cc_title);
                    map.put("cc_datetime",list.cc_datetime);
                    map.put("cc_count",list.cc_count);
                    map.put("headImg",list.headImg);
                    map.put("ud_nickname",list.ud_nickname);
                    map.put("cc_description",list.cc_description);
                    result_lists.add(map);
                }
                policyInfoListener.onResponseMessage(result_lists,total);
            }
        });
    }

    public static void postBar(String title, String content, int flag,ForResultListener listener) {
        String url = context.getResources().getString(R.string.service_host_address)
                .concat(context.getResources().getString(R.string.newsfbtz));
        resultListener = listener;
        OkHttpUtils.post().url(url)
                .addParams("sid","")
                .addParams("index",(index++)+"")
                .addParams("ub_id",SpTools.getString(context, Constants.userId,"0"))
                .addParams("uo_long","")
                .addParams("uo_lat","")
                .addParams("uo_high","")
                .addParams("cc_title",title)
                .addParams("cc_context",content)
                .addParams("type",flag+"")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("postBar="+e);
            }

            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("postBar="+response);
                OtherResult result = mGson.fromJson(response, OtherResult.class);
                if(result.result.code.equals("10")){
                    resultListener.onResponseMessage("成功");
                }
            }
        });
    }

    public interface ForResultPolicyInfoListener {
        void onResponseMessage(List<Map<String,String>> lists, String totalpage);
    }
    public interface ForResultEventInfoListener {
        void onResponseMessage(List<Map<String,String>> lists, String totalpage);
    }
    public interface ForResultPointInfoListener {
        void onResponseMessage(List<Map<String,String>> lists, String totalpage);
    }
    public interface ForResultNewsInfoListener {
        void onResponseMessage(OtherResult otherResult);
    }
    public interface ForResultListener{
        void onResponseMessage(String count);
    }
}
