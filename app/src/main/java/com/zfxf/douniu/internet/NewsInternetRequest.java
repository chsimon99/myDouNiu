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
import com.zfxf.douniu.bean.LunBoListInfo;
import com.zfxf.douniu.bean.MatadorResult;
import com.zfxf.douniu.bean.MyContentResult;
import com.zfxf.douniu.bean.NewsListResult;
import com.zfxf.douniu.bean.NewsNewsResult;
import com.zfxf.douniu.bean.NewsResult;
import com.zfxf.douniu.bean.OtherResult;
import com.zfxf.douniu.bean.ProjectListResult;
import com.zfxf.douniu.bean.SimulationResult;
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
    private static ForResultAnswerIndexListener answerIndexListener;
    private static ForResultMyPublicListener myPublicListener;
    private static ForResultMySecretListener mySecretListener;
    private static ForResultMyReferenceListener myReferenceListener;
    private static ForResultMatadorIndexListener matadorIndexListener;
    private static ForResultSimulationIndexListener simulationIndexListener;
    private static ForResultZiXuanStockListener ziXuanStockListener;
    private static ForResultMyAskDoneListener myAskDoneListener;
    private static ForResultMyAskWaitListener myAskWaitListener;
    private static ForResultHeadListLunboInfoListener headListLunboInfoListener;
    private static ForResultGoldPoneShortStockListener goldPoneShortStockListener;
    private static ForResultGoldPoneLongStockListener goldPoneLongStockListener;
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
                policyInfoListener.onResponseMessage(result_lists,total, result.lunbo_list);
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
                    policyInfoListener.onResponseMessage(null,0+"",null);
                }else if(headType == 1){
                    eventInfoListener.onResponseMessage(null,0+"",null);
                }else if(headType == 2){
                    pointInfoListener.onResponseMessage(null,0+"",null);
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
                    policyInfoListener.onResponseMessage(result_lists,total,null);
                }else if(headType == 1){
                    eventInfoListener.onResponseMessage(result_lists,total,null);
                }else if(headType == 2){
                    pointInfoListener.onResponseMessage(result_lists,total,null);
                }
            }
        }).post(context.getResources().getString(R.string.headlist),true,params);
    }

    public static void getHeadListLunboInformation(ForResultHeadListLunboInfoListener listener){
        headListLunboInfoListener = listener;
        new BaseInternetRequest(context, new BaseInternetRequest.HttpUtilsListener() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("getHeadListLunboInformation="+e);
            }

            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("getHeadListLunboInformation="+response);
                OtherResult result = mGson.fromJson(response, OtherResult.class);
                if(result.result.code.equals("10")){
                    headListLunboInfoListener.onResponseMessage(result.lunbo_list);
                }else {
                    CommonUtils.toastMessage("网络出现问题，请重新刷新");
                }
            }
        }).post(context.getResources().getString(R.string.headlunbo),true,null);
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
     * 新闻的点赞
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

    public static void dianZanAnswer(int id,ForResultListener listener) {
        resultListener = listener;
        Map<String ,String> params = new HashMap<>();
        params.put("zc_id",id+"");
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
                if(zanResult.result.code.equals("10")){
                    resultListener.onResponseMessage(zanResult.dz_count);
                }
            }
        }).post(context.getResources().getString(R.string.dzBright),true,params);
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
                    policyInfoListener.onResponseMessage(null,0+"",null);
                }else if(headType == 1){
                    eventInfoListener.onResponseMessage(null,0+"",null);
                }else if(headType == 2){
                    pointInfoListener.onResponseMessage(null,0+"",null);
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
                    policyInfoListener.onResponseMessage(result_lists,total,null);
                }else if(headType == 1){
                    eventInfoListener.onResponseMessage(result_lists,total,null);
                }else if(headType == 2){
                    pointInfoListener.onResponseMessage(result_lists,total,null);
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
                policyInfoListener.onResponseMessage(null,0+"", null);
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
                policyInfoListener.onResponseMessage(result_lists,total, result.lunbo_list);
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
                String total = result.total;

                List<NewsListResult> lists = result.news_list;
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
                policyInfoListener.onResponseMessage(result_lists,total,result.lunbo_list);
            }
        });
        baseInternetRequest.post(html,true,params);
    }

    /**
     * 微问答主页信息
     * @param listener
     */
    public static void getAnswerIndexInformation(ForResultAnswerIndexListener listener){
        answerIndexListener = listener;
        new BaseInternetRequest(context, new BaseInternetRequest.HttpUtilsListener() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("getAnswerIndexInformation="+e);
            }

            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("getAnswerIndexInformation="+response);
                IndexResult result = mGson.fromJson(response, IndexResult.class);
                if(result.result.code.equals("10")){
                    answerIndexListener.onResponseMessage(result);
                }else {
                    CommonUtils.toastMessage("网络出现问题，请重新刷新");
                }
            }
        }).post(context.getResources().getString(R.string.answerindex),true,null);
    }

    /**
     * 在线分析师列表
     * @param page 页数
     * @param listener
     */
    public static void getAnswerAdvisorListInformation(int page,ForResultAnswerIndexListener listener){
        answerIndexListener = listener;
        Map<String ,String> params = new HashMap<>();
        params.put("page",page+"");
        new BaseInternetRequest(context, new BaseInternetRequest.HttpUtilsListener() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("getAnswerAdvisorListInformation="+e);
            }

            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("getAnswerAdvisorListInformation="+response);
                IndexResult result = mGson.fromJson(response, IndexResult.class);
                if(result.result.code.equals("10")){
                    answerIndexListener.onResponseMessage(result);
                }
            }
        }).post(context.getResources().getString(R.string.analystList),true,params);
    }

    /**
     * 精彩回答列表
     * @param page 页数
     * @param id 首席的id，默认null为全部
     * @param listener
     */
    public static void getAnswerLIstInformation(int page,String id,ForResultAnswerIndexListener listener){
        answerIndexListener = listener;
        Map<String ,String> params = new HashMap<>();
        if(!TextUtils.isEmpty(id)){
            params.put("df_ub_id",id);
        }
        params.put("page",page+"");
        new BaseInternetRequest(context, new BaseInternetRequest.HttpUtilsListener() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("getAnswerLIstInformation="+e);
            }

            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("getAnswerLIstInformation="+response);
                IndexResult result = mGson.fromJson(response, IndexResult.class);
                if(result.result.code.equals("10")){
                    answerIndexListener.onResponseMessage(result);
                }
            }
        }).post(context.getResources().getString(R.string.brightList),true,params);

    }

    /**
     * 微问答回答详情
     * @param id 精彩回答id
     * @param listener
     */
    public static void getAnswerDetailInformation(String id,ForResultAnswerIndexListener listener){
        answerIndexListener = listener;
        Map<String ,String> params = new HashMap<>();
        params.put("zc_id",id);
        new BaseInternetRequest(context, new BaseInternetRequest.HttpUtilsListener() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("getAnswerDetailInformation="+e);
            }

            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("getAnswerDetailInformation="+response);
                IndexResult result = mGson.fromJson(response, IndexResult.class);
                if(result.result.code.equals("10")){
                    answerIndexListener.onResponseMessage(result);
                }
            }
        }).post(context.getResources().getString(R.string.stockInfo),true,params);
    }

    /**
     * 金股池首页信息
     * @param id 首席id 没有时传""
     * @param type 0短线  1中线
     * @param shortStockListener 短线的接口
     * @param longStockListener 中线的接口
     */
    public static void getGoldPondListInformation(String id, int type
            , final ForResultGoldPoneShortStockListener shortStockListener, final ForResultGoldPoneLongStockListener longStockListener){
        if(shortStockListener !=null){
            goldPoneShortStockListener = shortStockListener;
        }
        if(longStockListener !=null){
            goldPoneLongStockListener = longStockListener;
        }
        Map<String ,String> params = new HashMap<>();
        params.put("type",type+"");
        params.put("sx_ub_id",id);
        new BaseInternetRequest(context, new BaseInternetRequest.HttpUtilsListener() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("getGoldPondListInformation="+e);
            }

            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("getGoldPondListInformation="+response);
                SimulationResult result = mGson.fromJson(response, SimulationResult.class);
                if(result.result.code.equals("10")){
                    if(shortStockListener !=null){
                        goldPoneShortStockListener.onResponseMessage(result);
                    }else if(longStockListener !=null){
                        goldPoneLongStockListener.onResponseMessage(result);
                    }
                }
            }
        }).post(context.getResources().getString(R.string.gp),true,params);
    }

    /**
     * 金股池详情页信息
     * @param sx_id 首席的id
     * @param jgc_id 方案的id
     * @param listener
     */
    public static void getGoldPondDetailInformation(int sx_id,int jgc_id,ForResultXuanGuListener listener){
        xuanGuListener = listener;
        Map<String ,String> params = new HashMap<>();
        params.put("djf_id",jgc_id+"");
        params.put("sx_ub_id",sx_id+"");
        new BaseInternetRequest(context, new BaseInternetRequest.HttpUtilsListener() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("getGoldPondDetailInformation="+e);
            }

            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("getGoldPondDetailInformation="+response);
                XuanguResult result = mGson.fromJson(response, XuanguResult.class);
                if(result.result.code.equals("10")){
                    xuanGuListener.onResponseMessage(result);
                }
            }
        }).post(context.getResources().getString(R.string.gpinfo),true,params);
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
                String total = result.total;
                List<NewsListResult> lists = result.news_list;
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
                pointInfoListener.onResponseMessage(result_lists,total,result.lunbo_list);
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
                    eventInfoListener.onResponseMessage(null,total,null);
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
                eventInfoListener.onResponseMessage(result_lists,total,null);
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
     * 智能选股王订阅
     * @param infoId 选股王id
     * @param fee 费用
     * @param action 0订阅 1取消
     * @param listener
     * @param html
     */
    public static void XuanGusubscribeAndCannel(String infoId,int fee,int action,ForResultListener listener,String html){
        resultListener = listener;
        Map<String ,String> params = new HashMap<>();
        params.put("zf_id",infoId);
        params.put("zs_fee",fee+"");
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
                if(zanResult.result.code.equals("10")){
                    resultListener.onResponseMessage(zanResult.dy_count);
                }else{
                    CommonUtils.toastMessage("失败，请重试");
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
     * @param id 请求的最后一个评论的id，刷新的时候传最上面的id，请求历史的时候传最下面的id
     * @param type 类型 0从列表进入 1自动刷新，不增加访问量
     * @param zt_id 直播id
     * @param listener
     */
    public static void getLivingInformation(String type,int zt_id,int id,ForResultLivingInfoListener listener){
        livingInfoListener = listener;
        Map<String ,String> params = new HashMap<>();
        params.put("zt_id",zt_id+"");
        params.put("last_id",id+"");
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
                if(result.result.code.equals("10")){
                    livingInfoListener.onResponseMessage(result.news_info);
                }
            }
        }).post(context.getResources().getString(R.string.zhiboinfo),true,params);
    }

    /**
     * 直播互动列表
     * @param type 0请求新数据 1请假历史数据
     * @param zt_id 直播id
     * @param id 请求的最后一个评论的id，刷新的时候传最下面的id，请求历史的时候传最上面的id
     * @param listener
     */
    public static void getLivingInteractInformation(String type,int zt_id,int id,ForResultLivingInteractInfoListener listener){
        livingInteractInfoListener = listener;
        Map<String ,String> params = new HashMap<>();
        params.put("zt_id",zt_id+"");
        params.put("type",type);
        params.put("last_id",id+"");
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
                IndexResult result = mGson.fromJson(response, IndexResult.class);
                if(TextUtils.isEmpty(str)){
                    if(result.result.info.equals("显示数据")){
                        indexListener.onResponseMessage(result);
                    }
                }
                if(result.result.info.equals("显示数据")){
                    SpTools.setString(context, Constants.getIndexInformation,response);
                }
            }
        }).post(context.getResources().getString(R.string.index),true,null);

    }

    /**
     * 获取分析师列表
     * @param type 1首席 2金牌 3我的关注
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
                    }else if(type == 3){
                        advisorJPListener.onResponseMessage(result);
                    }
                }
            }
        }).post(context.getResources().getString(R.string.shouxilist),true,params);
    }

    /**
     * 我的关注斗牛士
     * @param page 页数
     * @param listener
     */
    public static void getMySubscribeMadatorListInformation(int page,ForResultMatadorIndexListener listener){
        matadorIndexListener = listener;
        Map<String ,String> params = new HashMap<>();
        params.put("page",page+"");
        new BaseInternetRequest(context, new BaseInternetRequest.HttpUtilsListener() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("getMySubscribeMadatorListInformation="+e);
            }

            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("getMySubscribeMadatorListInformation="+response);
                MatadorResult result = mGson.fromJson(response, MatadorResult.class);
                if(result.result.code.equals("10")){
                    matadorIndexListener.onResponseMessage(result);
                }else {
                    CommonUtils.toastMessage("网络出现问题，请重新刷新");
                }
            }
        }).post(context.getResources().getString(R.string.attention),true,params);
    }

    /**
     * 我的问股
     * @param page 页数
     * @param type 0已回答 1未回答
     * @param doneListener 已回答
     * @param waitListener 未回答
     */
    public static void getMyAskDoneInformation(int page , final int type, ForResultMyAskDoneListener doneListener, ForResultMyAskWaitListener waitListener){
        if(doneListener != null){
            myAskDoneListener = doneListener;
        }
        if(waitListener != null){
            myAskWaitListener = waitListener;
        }
        Map<String ,String> params = new HashMap<>();
        params.put("page",page+"");
        params.put("type",type+"");
        new BaseInternetRequest(context, new BaseInternetRequest.HttpUtilsListener() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("getMyAskDoneInformation="+e);
            }

            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("getMyAskDoneInformation="+response);
                NewsResult result = mGson.fromJson(response, NewsResult.class);
                if(result.result.code.equals("10")){
                    if(type == 0){
                        myAskDoneListener.onResponseMessage(result);
                    }else if(type == 1){
                        myAskWaitListener.onResponseMessage(result);
                    }
                }else {
                    CommonUtils.toastMessage("网络出现问题，请重新刷新");
                }
            }
        }).post(context.getResources().getString(R.string.myAskStock),true,params);

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

    /**
     * 智能选股王历史数据
     * @param id 数据源id
     * @param page 页数
     * @param listener
     */
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

    /**
     * 我的订阅智能选股王
     * @param type
     * @param listener
     */
    public static void getMyXuanGuListInformation(int type,ForResultXuanGuListener listener){
        xuanGuListener = listener;
        Map<String ,String> params = new HashMap<>();
        params.put("type",type+"");
        new BaseInternetRequest(context, new BaseInternetRequest.HttpUtilsListener() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("getMyXuanGuListInformation="+e);
            }

            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("getMyXuanGuListInformation="+response);
                XuanguResult result = mGson.fromJson(response, XuanguResult.class);
                if(result.result.code.equals("10")){
                    xuanGuListener.onResponseMessage(result);
                }else {
                    CommonUtils.toastMessage("网络出现问题，请重新刷新");
                }

            }
        }).post(context.getResources().getString(R.string.xuangulist),true,params);
    }

    /**
     * 我的订阅，公开课、私密课、参考
     * @param type 0公共课 1私密课 2参考
     * @param listener 公共课
     * @param listeners 私密课
     * @param listenert 参考
     */
    public static void getMySubscribeListInfromation(final int type, ForResultMyPublicListener listener
            , ForResultMySecretListener listeners, ForResultMyReferenceListener listenert){
        if(listener !=null){
            myPublicListener = listener;
        }
        if(listeners !=null){
            mySecretListener = listeners;
        }
        if(listenert !=null){
            myReferenceListener = listenert;
        }
        Map<String ,String> params = new HashMap<>();
        params.put("type",type+"");
        new BaseInternetRequest(context, new BaseInternetRequest.HttpUtilsListener() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("getMySubscribeListInfromation="+e);
            }

            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("getMySubscribeListInfromation="+response);
                MyContentResult result = mGson.fromJson(response, MyContentResult.class);
                if(result.result.code.equals("10")){
                    if(type == 0){
                        myPublicListener.onResponseMessage(result);
                    }else if (type == 1){
                        mySecretListener.onResponseMessage(result);
                    }else if (type == 2){
                        myReferenceListener.onResponseMessage(result);
                    }
                }else {
                    CommonUtils.toastMessage("网络出现问题，请重新刷新");
                }

            }
        }).post(context.getResources().getString(R.string.dingyuelist),true,params);

    }

    /**
     * 斗牛士评级信息
     * @param listener
     */
    public static void getMatadorIndexInformation(ForResultMatadorIndexListener listener){
        matadorIndexListener = listener;
        new BaseInternetRequest(context, new BaseInternetRequest.HttpUtilsListener() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("getMatadorIndexInformation="+e);
            }

            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("getMatadorIndexInformation="+response);
                MatadorResult result = mGson.fromJson(response, MatadorResult.class);
                if(result.result.info.equals("显示数据")){
                    matadorIndexListener.onResponseMessage(result);
                }else {
                    CommonUtils.toastMessage("网络出现问题，请重新刷新");
                }
            }
        }).post(context.getResources().getString(R.string.matadorlist),true,null);

    }

    /**
     * 斗牛士排行
     * @param type 0周排行 1月排行 2总排行
     * @param page 页数
     * @param listener
     */
    public static void getMatadorListInformation(int type,int page,ForResultMatadorIndexListener listener){
        matadorIndexListener = listener;
        Map<String ,String> params = new HashMap<>();
        params.put("type",type+"");
        params.put("page",type+"");
        new BaseInternetRequest(context, new BaseInternetRequest.HttpUtilsListener() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("getMatadorListInformation="+e);
            }

            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("getMatadorListInformation="+response);
                MatadorResult result = mGson.fromJson(response, MatadorResult.class);
                if(result.result.code.equals("10")){
                    matadorIndexListener.onResponseMessage(result);
                }else {
                    CommonUtils.toastMessage("网络出现问题，请重新刷新");
                }

            }
        }).post(context.getResources().getString(R.string.matadororder),true,params);
    }

    /**
     * 模拟炒股首页
     * @param listener
     */
    public static void getSimulationIndexInformation(String id,ForResultSimulationIndexListener listener){
        simulationIndexListener = listener;
        Map<String ,String> params = new HashMap<>();
        if(!TextUtils.isEmpty(id)){
            params.put("dn_ub_id",id);
        }
        new BaseInternetRequest(context, new BaseInternetRequest.HttpUtilsListener() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("getSimulationIndexInformation="+e);
            }

            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("getSimulationIndexInformation="+response);
                SimulationResult result = mGson.fromJson(response, SimulationResult.class);
                if(result.result.code.equals("10")){
                    simulationIndexListener.onResponseMessage(result);
                }else {
                    CommonUtils.toastMessage("网络出现问题，请重新刷新");
                }
            }
        }).post(context.getResources().getString(R.string.mnIndex),true,params);
    }

    /**
     * 模拟炒股买入
     * @param code 股票代码
     * @param listener
     */
    public static void getSimulationBuyInformation(String code,ForResultSimulationIndexListener listener){
        simulationIndexListener = listener;
        Map<String ,String> params = new HashMap<>();
        if(!TextUtils.isEmpty(code)){
            params.put("mg_code",code);
        }
        new BaseInternetRequest(context, new BaseInternetRequest.HttpUtilsListener() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("getSimulationBuyInformation="+e);
            }

            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("getSimulationBuyInformation="+response);
                SimulationResult result = mGson.fromJson(response, SimulationResult.class);
                if(result.result.code.equals("10")){
                    simulationIndexListener.onResponseMessage(result);
                }else {
                    CommonUtils.toastMessage("网络出现问题，请重新刷新");
                }
            }
        }).post(context.getResources().getString(R.string.buygp),true,params);
    }

    /**
     * 模拟炒股卖出
     * @param code 股票代码
     * @param listener
     */
    public static void getSimulationSellInformation(String code,ForResultSimulationIndexListener listener){
        simulationIndexListener = listener;
        Map<String ,String> params = new HashMap<>();
        if(!TextUtils.isEmpty(code)){
            params.put("mg_code",code);
        }
        new BaseInternetRequest(context, new BaseInternetRequest.HttpUtilsListener() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("getSimulationSellInformation="+e);
            }

            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("getSimulationSellInformation="+response);
                SimulationResult result = mGson.fromJson(response, SimulationResult.class);
                if(result.result.code.equals("10")){
                    simulationIndexListener.onResponseMessage(result);
                }else {
                    CommonUtils.toastMessage("网络出现问题，请重新刷新");
                }
            }
        }).post(context.getResources().getString(R.string.sellgp),true,params);
    }

    /**
     * 模拟炒股委托
     * @param listener
     */
    public static void getSimulationEntrustInformation(ForResultSimulationIndexListener listener){
        simulationIndexListener = listener;
        new BaseInternetRequest(context, new BaseInternetRequest.HttpUtilsListener() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("getSimulationEntrustInformation="+e);
            }

            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("getSimulationEntrustInformation="+response);
                SimulationResult result = mGson.fromJson(response, SimulationResult.class);
                if(result.result.code.equals("10")){
                    simulationIndexListener.onResponseMessage(result);
                }else {
                    CommonUtils.toastMessage("网络出现问题，请重新刷新");
                }
            }
        }).post(context.getResources().getString(R.string.consign),true,null);
    }

    /**
     * 股票搜索
     * @param code 搜索内容
     * @param listener
     */
    public static void getSearchStockInformation(String code,ForResultSimulationIndexListener listener){
        simulationIndexListener = listener;
        Map<String ,String> params = new HashMap<>();
        params.put("mg_code",code);
        new BaseInternetRequest(context, new BaseInternetRequest.HttpUtilsListener() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("getSearchStockInformation="+e);
            }

            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("getSearchStockInformation="+response);
                SimulationResult result = mGson.fromJson(response, SimulationResult.class);
                if(result.result.code.equals("10")){
                    simulationIndexListener.onResponseMessage(result);
                }else {
                    CommonUtils.toastMessage("网络出现问题，请重新刷新");
                }
            }
        }).post(context.getResources().getString(R.string.searchgp),true,params);
    }

    /**
     * 模拟炒股委托删除
     * @param id 删除的股票id
     * @param listener
     */
    public static void getSimulationEntrustDelete(String id,ForResultSimulationIndexListener listener){
        simulationIndexListener = listener;
        Map<String ,String> params = new HashMap<>();
        params.put("mc_id",id);
        new BaseInternetRequest(context, new BaseInternetRequest.HttpUtilsListener() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("getSimulationEntrustDelete="+e);
            }

            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("getSimulationEntrustDelete="+response);
                SimulationResult result = mGson.fromJson(response, SimulationResult.class);
                if(result.result.code.equals("10")){
                    simulationIndexListener.onResponseMessage(result);
                }else {
                    CommonUtils.toastMessage("网络出现问题，请重新刷新");
                }
            }
        }).post(context.getResources().getString(R.string.del),true,params);
    }

    /**
     * 行情首页信息
     * @param listener
     */
    public static void getQuotationIndexInformation(ForResultSimulationIndexListener listener){
        simulationIndexListener = listener;
        new BaseInternetRequest(context, new BaseInternetRequest.HttpUtilsListener() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("getQuotationIndexInformation="+e);
            }

            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("getQuotationIndexInformation="+response);
                SimulationResult result = mGson.fromJson(response, SimulationResult.class);
                if(result.result.code.equals("10")){
                    simulationIndexListener.onResponseMessage(result);
                }else {
                    CommonUtils.toastMessage("网络出现问题，请重新刷新");
                }
            }
        }).post(context.getResources().getString(R.string.quotes),true,null);
    }

    /**
     * 首页涨跌幅详情
     * @param page 页数
     * @param type 0降序 1升序
     * @param station 0跌幅榜 1涨幅榜
     * @param listener
     */
    public static void getStockListInformation(int page,int type,int station,ForResultZiXuanStockListener listener){
        ziXuanStockListener = listener;
        Map<String ,String> params = new HashMap<>();
        params.put("page",page+"");
        params.put("type",type+"");
        params.put("station",station+"");
        new BaseInternetRequest(context, new BaseInternetRequest.HttpUtilsListener() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("getStockListInformation="+e);
            }

            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("getStockListInformation="+response);
                SimulationResult result = mGson.fromJson(response, SimulationResult.class);
                if(result.result.code.equals("10")){
                    ziXuanStockListener.onResponseMessage(result);
                }else {
                    CommonUtils.toastMessage("网络出现问题，请重新刷新");
                }
            }
        }).post(context.getResources().getString(R.string.increaseList),true,params);
    }
    /**
     * 自选股票详情
     * @param page 页数
     * @param type 类型 0综合 2 市价升序  1市价降序 3跌幅 4涨幅
     * @param listener
     */
    public static void getZiXuanStockInformation(int page,int type,ForResultZiXuanStockListener listener) {
        ziXuanStockListener = listener;
        Map<String ,String> params = new HashMap<>();
        params.put("page",page+"");
        params.put("type",type+"");
        new BaseInternetRequest(context, new BaseInternetRequest.HttpUtilsListener() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("getZiXuanStockInformation="+e);
            }

            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("getZiXuanStockInformation="+response);
                SimulationResult result = mGson.fromJson(response, SimulationResult.class);
                if(result.result.code.equals("10")){
                    ziXuanStockListener.onResponseMessage(result);
                }else if(result.result.code.equals("01")){
                    ziXuanStockListener.onResponseMessage(result);
                }else {
                    CommonUtils.toastMessage("网络出现问题，请重新刷新");
                }
            }
        }).post(context.getResources().getString(R.string.optional),true,params);
    }

    /**
     * 编辑自选股列表
     * @param listener
     */
    public static void getMyZiXuanStockInformation(ForResultZiXuanStockListener listener){
        ziXuanStockListener = listener;
        new BaseInternetRequest(context, new BaseInternetRequest.HttpUtilsListener() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("getMyZiXuanStockInformation="+e);
            }

            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("getMyZiXuanStockInformation="+response);
                SimulationResult result = mGson.fromJson(response, SimulationResult.class);
                if(result.result.code.equals("10")){
                    ziXuanStockListener.onResponseMessage(result);
                }else {
                    CommonUtils.toastMessage("网络出现问题，请重新刷新");
                }
            }
        }).post(context.getResources().getString(R.string.editZx),true,null);
    }

    /**
     * 自选股的添加和删除
     * @param code 股票的代码 可多个用，隔开
     * @param type 0添加 1删除
     * @param listener
     */
    public static void deleteZiXuanStockInformation(String code,int type,ForResultListener listener){
        resultListener = listener;
        Map<String ,String> params = new HashMap<>();
        params.put("mg_code",code);
        params.put("type",type+"");
        new BaseInternetRequest(context, new BaseInternetRequest.HttpUtilsListener() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("deleteZiXuanStockInformation="+e);
            }

            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("deleteZiXuanStockInformation="+response);
                SimulationResult result = mGson.fromJson(response, SimulationResult.class);
                if(result.result.code.equals("10")){
                    resultListener.onResponseMessage(result.result.code);
                }else {
                    CommonUtils.toastMessage("网络出现问题，请重新刷新");
                }
            }
        }).post(context.getResources().getString(R.string.addZx),true,params);
    }

    public interface ForResultPolicyInfoListener {
        void onResponseMessage(List<Map<String, String>> lists, String totalpage, List<LunBoListInfo> lunbo_list);
    }
    public interface ForResultEventInfoListener {
        void onResponseMessage(List<Map<String,String>> lists, String totalpage, List<LunBoListInfo> lunbo_list);
    }
    public interface ForResultPointInfoListener {
        void onResponseMessage(List<Map<String,String>> lists, String totalpage, List<LunBoListInfo> lunbo_list);
    }
    public interface ForResultHeadListLunboInfoListener {
        void onResponseMessage(List<LunBoListInfo> lunbo_list);
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
    public interface ForResultAnswerIndexListener{
        void onResponseMessage(IndexResult result);
    }
    public interface ForResultMyPublicListener{
        void onResponseMessage(MyContentResult result);
    }
    public interface ForResultMySecretListener{
        void onResponseMessage(MyContentResult result);
    }
    public interface ForResultMyReferenceListener{
        void onResponseMessage(MyContentResult result);
    }
    public interface ForResultMatadorIndexListener{
        void onResponseMessage(MatadorResult result);
    }
    public interface ForResultSimulationIndexListener {
        void onResponseMessage(SimulationResult result);
    }
    public interface ForResultMyAskDoneListener {
        void onResponseMessage(NewsResult result);
    }
    public interface ForResultMyAskWaitListener {
        void onResponseMessage(NewsResult result);
    }
    public interface ForResultZiXuanStockListener {
        void onResponseMessage(SimulationResult result);
    }
    public interface ForResultGoldPoneShortStockListener {
        void onResponseMessage(SimulationResult result);
    }
    public interface ForResultGoldPoneLongStockListener {
        void onResponseMessage(SimulationResult result);
    }
}
