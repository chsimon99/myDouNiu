package com.zfxf.douniu.internet;

import android.content.Context;

import com.google.gson.Gson;
import com.zfxf.douniu.R;
import com.zfxf.douniu.bean.newsexpress.NewsExpressItem;
import com.zfxf.douniu.bean.newsexpress.NewsExpressResult;
import com.zfxf.douniu.utils.CommonUtils;
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

public class NewsInterRequestUtils {

    private static Context context;
    private static Gson mGson;
    static {
        mGson = new Gson();
        context = CommonUtils.getContext();
    }

    public static void getNewsExpressInformation(String page, String num, String autoNew, final ForNewsExpressInfoListener listener) {
        String url = context.getResources().getString(R.string.newsExpress);
        final List<Map<String,String>> result_lists = new ArrayList<>();
        OkHttpUtils.post().url(url)
                .addParams("page",page)
                .addParams("num",num)
                .addParams("autoNew",autoNew)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CommonUtils.logMes("getNewsExpressInformation="+e);
            }

            @Override
            public void onResponse(String response, int id) {
                CommonUtils.logMes("getNewsExpressInformation="+response);
                NewsExpressResult result = mGson.fromJson(response, NewsExpressResult.class);
                if("success".equals(result.msg)){
                    for (NewsExpressItem list :result.item) {
                        Map<String,String> map = new HashMap<>();
                        map.put("desc",list.desc);
                        map.put("dateline",list.dateline);
                        result_lists.add(map);
                    }
                    listener.onResponseMessage(result_lists,"50");
                }else {
                    listener.onResponseMessage(null,"50");
                }
            }
        });
    }

    public interface ForNewsExpressInfoListener {/** 资讯的快讯接口*/
        void onResponseMessage(List<Map<String, String>> lists, String totalpage);
    }

}
