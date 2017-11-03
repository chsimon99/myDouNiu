package com.zfxf.douniu.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.zfxf.douniu.activity.ActivityGoldPond;
import com.zfxf.douniu.activity.ActivityLiving;
import com.zfxf.douniu.activity.MainActivityTabHost;
import com.zfxf.douniu.utils.CommonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * @author IMXU
 * @time 2017/8/8 13:18
 * @des ${TODO}
 * 邮箱：butterfly_xu@sina.com
 */

public class JupushReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
//        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            String hqfjzd = bundle.getString(JPushInterface.EXTRA_EXTRA);//接收附加字段
            JSONObject object = null;
            try {
                object = new JSONObject(hqfjzd);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            CommonUtils.logMes("------MyReceiver---自定义消息----="+hqfjzd);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
//            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
//            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
            String hqfjzd = bundle.getString(JPushInterface.EXTRA_EXTRA);//接收附加字段
            JSONObject object = null;
            try {
                object = new JSONObject(hqfjzd);
                CommonUtils.logMes("------MyReceiver---系统消息----="+hqfjzd);
                String message = object.getString("message");//获取附加字段
                String sx_ub_id = object.getString("sx_ub_id");//获取附加字段
                String type = object.getString("type");//获取附加字段
                String id = object.getString("id");//获取附加字段
                CommonUtils.logMes("------MyReceiver--message--="+message);
                CommonUtils.logMes("------MyReceiver--sx_ub_id--="+sx_ub_id);
                CommonUtils.logMes("------MyReceiver--type--="+type);
                CommonUtils.logMes("------MyReceiver--id--="+id);
                //根据附加字段的值判断跳相应的界面
                if (type.equals("1")) {//直播课
                    Intent i = new Intent(context, ActivityLiving.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("id",Integer.parseInt(id));
                    i.putExtra("sx_id",sx_ub_id);
                    context.startActivity(i);
                    //通知栏跳转APP消息界面
//                    Intent i = new Intent(context, ReceiverCS.class);
//                    i.putExtras(bundle);
//                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    context.startActivity(i);
                } else if(type.equals("2")){//金股池
                    Intent i = new Intent(context, ActivityGoldPond.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("jgcId",Integer.parseInt(id));
                    i.putExtra("id",Integer.parseInt(sx_ub_id));


                    context.startActivity(i);
                }else if(type.equals("0")){//普通推送
                    Intent i = new Intent(context, MainActivityTabHost.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }
}
