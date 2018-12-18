package com.jizhang.mjtzn.jizhang;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.LinkedList;

public class myNotificationListenerService extends NotificationListenerService {

    @Override
    public void onCreate() {
        super.onCreate();
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel Channel = new NotificationChannel("1", "监听通知服务", NotificationManager.IMPORTANCE_NONE);
        manager.createNotificationChannel(Channel);

        Notification notification = new NotificationCompat.Builder(this,"1")
                .setWhen(System.currentTimeMillis())
                .build();
        startForeground(1,notification);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        Notification notification = sbn.getNotification();
        if (notification == null) {
            return;
        }
        if(!isLegalPackageName(sbn.getPackageName())){
            return;
        }
        Bundle extras = notification.extras;
        String content = "";
        String title = "";
        if (extras != null) {
            title = extras.getString(Notification.EXTRA_TITLE);
            content = extras.getString(Notification.EXTRA_TEXT);

            if(title==null && content==null){ return ; }

            //Log.i("包名:", sbn.getPackageName() + "标题：" + title + "内容:" + content);

            RecordBean record=Util.getInstance().notification2record(title,content);
            if(record.getAmount()<0.00001 && record.getAmount()>-0.00001){
                return;
            }
            Util.getInstance().databaseHelper.addRecord(record);
        }
    }

    private boolean isLegalPackageName(String PackageName){
        String legalPackname[]={"com.eg.android.AlipayGphone","com.example.azn.helloworld"};
        for(int i=0;i<legalPackname.length;i++){
            if(PackageName.equals(legalPackname[i])){
                return true;
            }
        }
        return false;
    }
}
