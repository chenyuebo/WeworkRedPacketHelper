package com.cyb.wework.service;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import com.cyb.wework.utils.LogUtil;

/**
 * Created by cyb on 2018/2/1.
 */

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
@SuppressLint("OverrideAbstract")
public class MyNotificationListenerService extends NotificationListenerService {

    private SharedPreferences sharedPreferences;

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        LogUtil.d("MyNotificationListenerService onListenerConnected 通知使用权已开启");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        LogUtil.d("MyNotificationListenerService onNotificationPosted sbn=" + sbn);
        if(sharedPreferences != null){
            if(!sharedPreferences.getBoolean("pref_watch_notification", true)){
                return;
            }
        }
        String packageName = sbn.getPackageName();
        if("com.tencent.wework".equals(packageName)){
            Notification notification = sbn.getNotification();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                final Bundle extras = notification.extras;
                CharSequence notificationExtraText = extras.getCharSequence(Notification.EXTRA_TEXT);
                CharSequence notificationExtraSubText = extras.getCharSequence(Notification.EXTRA_SUB_TEXT);
                LogUtil.d("notificationExtraText=" + notificationExtraText);
                LogUtil.d("notificationExtraSubText=" + notificationExtraSubText);
                final String str = "" + notificationExtraText;

                String keywords = sharedPreferences.getString("pref_notification_keyword", "拼手气红包");
                LogUtil.d("pref_notification_keyword=" + keywords);
                String[] keywordArray = keywords.split(";");
                for(String keyword : keywordArray){
                    if(keyword != null && keyword.length() > 0){
                        if(str.contains(keyword)){
                            PendingIntent pendingIntent = notification.contentIntent;
                            try {
                                pendingIntent.send();
                            } catch (PendingIntent.CanceledException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

            }
        }
    }
}
