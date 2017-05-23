package com.zfxf.douniu.internet;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.zfxf.douniu.R;
import com.zfxf.douniu.bean.CourseResult;
import com.zfxf.douniu.bean.IndexResult;
import com.zfxf.douniu.bean.LivingContent;
import com.zfxf.douniu.bean.LivingInfoResult;
import com.zfxf.douniu.bean.LivingSendMsg;
import com.zfxf.douniu.bean.NewsListResult;
import com.zfxf.douniu.bean.NewsNewsResult;
import com.zfxf.douniu.bean.NewsResult;
import com.zfxf.douniu.bean.OtherResult;
import com.zfxf.douniu.bean.ProjectListResult;
import com.zfxf.douniu.bean.XuanguDetail;
import com.zfxf.douniu.bean.XuanguResult;
import com.zfxf.douniu.bean.ZanResult;
import com.zfxf.douniu.utils.CommonUtils;
import com.zfxf.douniu.utils.Constants;
import com.zfxf.douniu.utils.SpTools;

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
    private static ForResultLivingInfoListener livingInfoListener;
    private static ForResultLivingInteractInfoListener livingInteractInfoListener;
    private static ForResultSendInteractInfoListener sendInteractInfoListener;
    private static ForResultNewsInfoListener newsInfoListener;
    private static ForResultCourseInfoListener courseInfoListener;
    private static ForResultIndexListener indexListener;
    private static ForResultAdvisorSXListener advisorSXListener;
    private static ForResultAdvisorJPListener advisorJPListener;
    private static ForResultListener resultListener;
    private static ForResultXuanGuListener xuanGuListener;
    private static ForResultXuanGuDetailListener xuanGuDetailListener;
    static {
        mGson = new Gson();
        context = CommonUtils.getContext();
    }

    /**
     * 获取好项目列表
     * @param page 访问页数
     * @param listener
     */
    public static void getProjectListInformation( String page, ForResultPolicyInfoListener listener) {
        policyInfoListener = listener;
        final List<Map<String,String>> result_lists = new ArrayList<>();
        Map<String ,String> params = new HashMap<>();
        params.put("page",page);
        BaseInternetRequest baseInternetRequest = new BaseInternetRequest(context, new BaseInternetRequest.HttpUtilsListener() {
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
        baseInternetRequest.post(context.getResources().getString(R.string.projectlist),true,params);
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
        Map<String ,String> params = new HashMap<>();
        params.put("type",headType+"");
        params.put("page",page);
        new BaseInternetRequest(context, new BaseInternetRequest.HttpUtilsListener() {
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
        }).post(context.getResources().getString(R.string.headlist),true,params);
    }

    /**
     *  获取新闻详情
     * @param newsinfoId 新闻ID
     * @param listener listener
     */
    public static void getNewsInformation(int newsinfoId,ForResultNewsInfoListener listener,String html) {
        newsInfoListener = listener;
        Map<String ,String> params = new HashMap<>();
        params.put("cc_id",newsinfoId+"");
        new BaseInternetRequest(context, new BaseInternetRequest.HttpUtilsListener() {
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
        }).post(html,true,params);
    }

    /**
     * 发表评论
     * @param newsinfoId 评论的新闻ID
     * @param str 评论的内容
     * @param listener
     */
    public static void publishComment(int newsinfoId, String str,ForResultNewsInfoListener listener) {
        newsInfoListener = listener;
        Map<String ,String> params = new HashMap<>();
        params.put("ccp_cc_id",newsinfoId+"");
        params.put("ccp_cs_id",0+"");
        params.put("ccp_info",str);
        new BaseInternetRequest(context, new BaseInternetRequest.HttpUtilsListener() {
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
        }).post(context.getResources().getString(R.string.newsfbpl),true,params);
    }

    /**
     * 点赞
     * @param newsinfoId 新闻ID
     * @param listener
     */
    public static void dianZan(int newsinfoId,ForResultListener listener) {
        resultListener = listener;
        Map<String ,String> params = new HashMap<>();
        params.put("cc_id",newsinfoId+"");
        params.put("cc_agr",1+"");
        new BaseInternetRequest(context, new BaseInternetRequest.HttpUtilsListener() {
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
        }).post(context.getResources().getString(R.string.newsdz),true,params);
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
        Map<String ,String> params = new HashMap<>();
        params.put("type",headType+"");
        params.put("page",page);
        params.put("last_cc_id",lastPageId);
        new BaseInternetRequest(context, new BaseInternetRequest.HttpUtilsListener() {
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
        }).post(context.getResources().getString(R.string.zixunlist),true,params);
    }

    /**
     * 获取斗牛吧 列表
     * @param headType 0:热度 1:大盘 2:杂谈
     * @param page 访问页数
     * @param policylistener
     */
    public static void getBarListInformation(final int headType, String page, ForResultPolicyInfoListener policylistener) {
        policyInfoListener = policylistener;
        final List<Map<String,String>> result_lists = new ArrayList<>();
        Map<String ,String> params = new HashMap<>();
        params.put("type",headType+"");
        params.put("page",page);
        new BaseInternetRequest(context, new BaseInternetRequest.HttpUtilsListener() {
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
        }).post(context.getResources().getString(R.string.douniubarlist),true,params);
    }

    /**
     * 发表观点
     * @param title 标题
     * @param content 内容
     * @param flag 观点类型 0大盘 1杂谈
     * @param listener
     */
    public static void postBar(String title, String content, int flag,ForResultListener listener) {
        resultListener = listener;
        Map<String ,String> params = new HashMap<>();
        params.put("cc_title",title);
        params.put("cc_context",content);
        params.put("type",flag+"");
        new BaseInternetRequest(context, new BaseInternetRequest.HttpUtilsListener() {
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
        }).post(context.getResources().getString(R.string.newsfbtz),true,params);
    }

    /**
     * 列表选项 公开课、大参考
     * @param page 访问页数
     * @param id 首席的id，默认null为全部
     * @param listener
     * @param html 访问的网络地址
     */
    public static void getListInformation(String page, String id,ForResultPolicyInfoListener listener,String html) {
        policyInfoListener = listener;
        final List<Map<String,String>> result_lists = new ArrayList<>();
        Map<String ,String> params = new HashMap<>();
        if(!TextUtils.isEmpty(id)){
            params.put("cc_ub_id",id);
        }
        params.put("page",page);
        BaseInternetRequest baseInternetRequest = new BaseInternetRequest(context, new BaseInternetRequest.HttpUtilsListener() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("getListInformation="+e);
            }

            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("getListInformation="+response);
                OtherResult result = mGson.fromJson(response, OtherResult.class);
                if(result.result.info.equals("没有数据")){
                    CommonUtils.toastMessage("没有加载到数据，请您重新再试");
                    return;
                }
                List<NewsListResult> lists = result.news_list;
                String total = result.total;
                for (NewsListResult list :lists) {
                    Map<String,String> map = new HashMap<>();
                    map.put("cc_id",list.cc_id);
                    map.put("cc_title",list.cc_title);
                    map.put("cc_from",list.cc_from);
                    map.put("cc_fielid",list.cc_fielid);
                    map.put("cc_fee",list.cc_fee);
                    map.put("buy_count",list.buy_count);
                    map.put("has_buy",list.has_buy);
                    map.put("cc_auth",list.cc_auth);
                    map.put("cc_datetime",list.cc_datetime);
                    map.put("has_dy",list.has_dy);
                    map.put("dy_count",list.dy_count);
                    result_lists.add(map);
                }
                policyInfoListener.onResponseMessage(result_lists,total);
            }
        });
        baseInternetRequest.post(html,true,params);
    }
    /**
     * 列表选项 私密课
     * @param page 访问页数
     * @param id 首席的id，默认null为全部
     * @param listener
     * @param html 访问的网络地址
     */
    public static void getSimikeListInformation(String page, String id,ForResultPointInfoListener listener,String html) {
        pointInfoListener = listener;
        final List<Map<String,String>> result_lists = new ArrayList<>();
        Map<String ,String> params = new HashMap<>();
        if(!TextUtils.isEmpty(id)){
            params.put("cc_ub_id",id);
        }
        params.put("page",page);
        BaseInternetRequest baseInternetRequest = new BaseInternetRequest(context, new BaseInternetRequest.HttpUtilsListener() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("getSimikeListInformation="+e);
            }

            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("getSimikeListInformation="+response);
                OtherResult result = mGson.fromJson(response, OtherResult.class);
                if(result.result.info.equals("没有数据")){
                    CommonUtils.toastMessage("没有加载到数据，请您重新再试");
                    return;
                }
                List<NewsListResult> lists = result.news_list;
                String total = result.total;
                for (NewsListResult list :lists) {
                    Map<String,String> map = new HashMap<>();
                    map.put("cc_id",list.cc_id);
                    map.put("cc_title",list.cc_title);
                    map.put("cc_from",list.cc_from);
                    map.put("cc_fielid",list.cc_fielid);
                    map.put("cc_fee",list.cc_fee);
                    map.put("buy_count",list.buy_count);
                    map.put("has_buy",list.has_buy);
                    map.put("cc_auth",list.cc_auth);
                    map.put("cc_datetime",list.cc_datetime);
                    map.put("has_dy",list.has_dy);
                    map.put("dy_count",list.dy_count);
                    result_lists.add(map);
                }
                pointInfoListener.onResponseMessage(result_lists,total);
            }
        });
        baseInternetRequest.post(html,true,params);
    }
    /**
     * 列表选项 直播课
     * @param headType 类型 0互动多 1人气高 2观点多
     * @param page 访问页数
     * @param id 首席的id，默认null为全部
     * @param listener
     * @param html 访问的网络地址
     */
    public static void getLivingListInformation(int headType, String page, String id,ForResultEventInfoListener listener,String html) {
        eventInfoListener = listener;
        final List<Map<String,String>> result_lists = new ArrayList<>();
        Map<String ,String> params = new HashMap<>();
        if(!TextUtils.isEmpty(id)){
            params.put("zt_ub_id",id);
        }
        params.put("order",headType+"");
        params.put("page",page);
        BaseInternetRequest baseInternetRequest = new BaseInternetRequest(context, new BaseInternetRequest.HttpUtilsListener() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("getLivingListInformation="+e);
            }

            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("getLivingListInformation="+response);
                OtherResult result = mGson.fromJson(response, OtherResult.class);
                List<NewsListResult> lists = result.news_list;
                String total = result.total;
                if(result.result.info.equals("没有数据")){
                    CommonUtils.toastMessage("没有加载到数据，请您重新再试");
                    eventInfoListener.onResponseMessage(null,total);
                    return;
                }
                for (NewsListResult list :lists) {
                    Map<String,String> map = new HashMap<>();
                    map.put("zt_id",list.zt_id);
                    map.put("zt_name",list.zt_name);
                    map.put("zt_clicks",list.zt_clicks);
                    map.put("headImg",list.headImg);
                    map.put("zt_start",list.zt_start);
                    map.put("ud_nickname",list.ud_nickname);
                    map.put("ud_ub_id",list.ud_ub_id);
                    map.put("status",list.status);
                    result_lists.add(map);
                }
                eventInfoListener.onResponseMessage(result_lists,total);
            }
        });
        baseInternetRequest.post(html,true,params);
    }
    /**
     *  订阅（取消） 和 关注（取消）
     * @param infoId 订阅/关注的id
     * @param type  0订阅 6关注人
     * @param action 0订阅 1取消
     * @param listener
     */
    public static void subscribeAndCannel(String infoId,int type,int action,ForResultListener listener,String html) {
        resultListener = listener;
        Map<String ,String> params = new HashMap<>();
        params.put("id_param",infoId);
        params.put("type",type+"");
        params.put("action",action+"");
        new BaseInternetRequest(context, new BaseInternetRequest.HttpUtilsListener() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("subscribeAndCannel="+e);
                CommonUtils.toastMessage("失败，请重试");
            }

            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("subscribeAndCannel="+response);
                ZanResult zanResult = mGson.fromJson(response, ZanResult.class);
                if(zanResult.result.code.equals("02")){
                    CommonUtils.toastMessage("失败，请重试");
                }else{
                    resultListener.onResponseMessage(zanResult.dy_count);
                }
            }
        }).post(html,true,params);
    }

    /**
     *  获取公开课、私密课详情
     * @param infoId 新闻ID
     * @param listener listener
     */
    public static void getCourseInformation(int infoId,ForResultCourseInfoListener listener,String html) {
        courseInfoListener = listener;
        Map<String ,String> params = new HashMap<>();
        params.put("cc_id",infoId+"");
        new BaseInternetRequest(context, new BaseInternetRequest.HttpUtilsListener() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("getCourseInformation="+e);
            }

            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("getCourseInformation="+response);
                CourseResult result = mGson.fromJson(response, CourseResult.class);
                courseInfoListener.onResponseMessage(result);
            }
        }).post(html,true,params);
    }

    /**
     * 直播列表
     * @param page 访问页数
     * @param type 类型 0从列表进入 1自动刷新，不增加访问量
     * @param id 直播id
     * @param listener
     */
    public static void getLivingInformation(String page,int type,int id,ForResultLivingInfoListener listener){
        livingInfoListener = listener;
        Map<String ,String> params = new HashMap<>();
        params.put("zt_id",id+"");
        params.put("page",page);
        params.put("type",type+"");
        new BaseInternetRequest(context, new BaseInternetRequest.HttpUtilsListener() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("getLivingInformation="+e);
            }

            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("getLivingInformation="+response);
                LivingInfoResult result = mGson.fromJson(response, LivingInfoResult.class);
                if(result.result.info.equals("没有数据")){
                    CommonUtils.toastMessage("网络出现问题，请重新刷新");
                }else{
                    livingInfoListener.onResponseMessage(result.news_info);
                }
            }
        }).post(context.getResources().getString(R.string.zhiboinfo),true,params);
    }

    /**
     * 直播互动列表
     * @param page 访问页数
     * @param id 直播id
     * @param listener
     */
    public static void getLivingInteractInformation(String page,int id,ForResultLivingInteractInfoListener listener){
        livingInteractInfoListener = listener;
        Map<String ,String> params = new HashMap<>();
        params.put("zt_id",id+"");
        params.put("page",page);
        new BaseInternetRequest(context, new BaseInternetRequest.HttpUtilsListener() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("getLivingInteractInformation="+e);
            }

            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("getLivingInteractInformation="+response);
                LivingInfoResult result = mGson.fromJson(response, LivingInfoResult.class);
                livingInteractInfoListener.onResponseMessage(result.news_pl);
            }
        }).post(context.getResources().getString(R.string.zhibopl),true,params);
    }

    /**
     * 直播互动发送评论
     * @param content 评论内容
     * @param id 直播id
     * @param listener
     */
    public static void sendInteractInformation(String content,int id,ForResultSendInteractInfoListener listener){
        sendInteractInfoListener = listener;
        Map<String ,String> params = new HashMap<>();
        params.put("zp_zt_id",id+"");
        params.put("zp_pl",content);
        new BaseInternetRequest(context, new BaseInternetRequest.HttpUtilsListener() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("sendInteractInformation="+e);
            }

            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("sendInteractInformation="+response);
                LivingInfoResult result = mGson.fromJson(response, LivingInfoResult.class);
                if(result.result.code.equals("10")){
                    sendInteractInfoListener.onResponseMessage(result.pl_info);

                }
            }
        }).post(context.getResources().getString(R.string.zhibofbpl),true,params);

    }

    /**
     * 获取主页信息
     * @param listener
     */
    public static void getIndexInformation(ForResultIndexListener listener){
        indexListener = listener;
        final String str = SpTools.getString(context, Constants.getIndexInformation, "");
        if(!TextUtils.isEmpty(str)){
            indexListener.onResponseMessage(mGson.fromJson(str, IndexResult.class));
        }
        new BaseInternetRequest(context, new BaseInternetRequest.HttpUtilsListener() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("getIndexInformation="+e);
            }

            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("getIndexInformation="+response);
                if(TextUtils.isEmpty(str)){
                    IndexResult result = mGson.fromJson(response, IndexResult.class);
                    if(result.result.info.equals("显示数据")){
                        indexListener.onResponseMessage(result);
                    }
                }
                SpTools.setString(context, Constants.getIndexInformation,response);
            }
        }).post(context.getResources().getString(R.string.index),true,null);

    }

    /**
     * 获取分析师列表
     * @param type 1首席 2金牌
     * @param page 页数
     * @param sxListener
     * @param jpListener
     */
    public static void getAdvisorListInformation(final int type, int page, ForResultAdvisorSXListener sxListener, ForResultAdvisorJPListener jpListener){
        if(sxListener != null){
            advisorSXListener = sxListener;
        }
        if(jpListener != null){
            advisorJPListener = jpListener;
        }
        Map<String ,String> params = new HashMap<>();
        params.put("type",type+"");
        params.put("page",page+"");
        new BaseInternetRequest(context, new BaseInternetRequest.HttpUtilsListener() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("getAdvisorListInformation="+e);
            }

            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("getAdvisorListInformation="+response);
                IndexResult result = mGson.fromJson(response, IndexResult.class);
                if(result.result.code.equals("10")){
                    if(type == 1){
                        advisorSXListener.onResponseMessage(result);
                    }else if(type == 2){
                        advisorJPListener.onResponseMessage(result);
                    }
                }
            }
        }).post(context.getResources().getString(R.string.shouxilist),true,params);
    }

    /**
     * 首席首页个人详细信息
     * @param id 首席的id
     * @param listener
     */
    public static void getAdvisorDetailInformation(int id,ForResultIndexListener listener){
        indexListener = listener;
        Map<String ,String> params = new HashMap<>();
        params.put("df_ub_id",id+"");
        new BaseInternetRequest(context, new BaseInternetRequest.HttpUtilsListener() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("getAdvisorDetailInformation="+e);
            }

            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("getAdvisorDetailInformation="+response);
                IndexResult result = mGson.fromJson(response, IndexResult.class);
                if(result.result.info.equals("显示数据")){
                    indexListener.onResponseMessage(result);
                }else {
                    CommonUtils.toastMessage("网络出现问题，请重新刷新");
                }

            }
        }).post(context.getResources().getString(R.string.shouxiinfo),true,params);
    }

    /**
     * 智能选股王详情页
     * @param id 选股id
     * @param listener
     */
    public static void getXuanGuDetailInformation(int id,ForResultXuanGuListener listener){
        xuanGuListener = listener;
        Map<String ,String> params = new HashMap<>();
        params.put("zf_id",id+"");
        new BaseInternetRequest(context, new BaseInternetRequest.HttpUtilsListener() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("getXuanGuDetailInformation="+e);
            }

            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("getXuanGuDetailInformation="+response);
                XuanguResult result = mGson.fromJson(response, XuanguResult.class);
                if(result.result.info.equals("显示数据")){
                    xuanGuListener.onResponseMessage(result);
                }else {
                    CommonUtils.toastMessage("网络出现问题，请重新刷新");
                }

            }
        }).post(context.getResources().getString(R.string.xuanguinfo),true,params);
    }

    /**
     * 选股王列表信息
     * @param id
     * @param listener
     */
    public static void getXuanGuDetailList(String id,ForResultXuanGuDetailListener listener){
        xuanGuDetailListener = listener;
        Map<String ,String> params = new HashMap<>();
        params.put("zi_id",id);
        new BaseInternetRequest(context, new BaseInternetRequest.HttpUtilsListener() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("getXuanGuDetailList="+e);
            }

            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("getXuanGuDetailList="+response);
                XuanguResult result = mGson.fromJson(response, XuanguResult.class);
                if(result.result.info.equals("显示数据")){
                    xuanGuDetailListener.onResponseMessage(result.news_info);
                }else {
                    CommonUtils.toastMessage("网络出现问题，请重新刷新");
                }

            }
        }).post(context.getResources().getString(R.string.zixianginfo),true,params);
    }

    public static void getXuanGuHistoryInformation(int id,String page,ForResultXuanGuDetailListener listener){
        xuanGuDetailListener = listener;
        Map<String ,String> params = new HashMap<>();
        params.put("zi_id",id+"");
        params.put("page",page);
        new BaseInternetRequest(context, new BaseInternetRequest.HttpUtilsListener() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("getXuanGuHistoryInformation="+e);
            }

            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("getXuanGuHistoryInformation="+response);
                XuanguResult result = mGson.fromJson(response, XuanguResult.class);
                if(result.result.info.equals("显示数据")){
                    xuanGuDetailListener.onResponseMessage(result.news_info);
                }else {
                    CommonUtils.toastMessage("网络出现问题，请重新刷新");
                }

            }
        }).post(context.getResources().getString(R.string.historygupiao),true,params);
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
    public interface ForResultLivingInfoListener {
        void onResponseMessage(LivingContent content);
    }
    public interface ForResultLivingInteractInfoListener {
        void onResponseMessage(LivingContent content);
    }
    public interface ForResultSendInteractInfoListener {
        void onResponseMessage(LivingSendMsg sendMsg);
    }
    public interface ForResultNewsInfoListener {
        void onResponseMessage(OtherResult otherResult);
    }
    public interface ForResultCourseInfoListener {
        void onResponseMessage(CourseResult courseResult);
    }
    public interface ForResultIndexListener {
        void onResponseMessage(IndexResult indexResult);
    }
    public interface ForResultAdvisorSXListener {
        void onResponseMessage(IndexResult indexResult);
    }
    public interface ForResultAdvisorJPListener {
        void onResponseMessage(IndexResult indexResult);
    }
    public interface ForResultListener{
        void onResponseMessage(String count);
    }
    public interface ForResultXuanGuListener{
        void onResponseMessage(XuanguResult result);
    }
    public interface ForResultXuanGuDetailListener{
        void onResponseMessage(XuanguDetail result);
    }
}
