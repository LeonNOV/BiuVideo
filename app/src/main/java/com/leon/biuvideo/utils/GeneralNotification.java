package com.leon.biuvideo.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public class GeneralNotification {
    private Context context;
    private NotificationManager manager;    //getSystemService(Context.NOTIFICATION_SERVICE);
    public Notification notification;

    private String channelId;   //channel的ID
    private String description; //描述
    private int tag;    //该通知的id，用于取消通知的推送

    public GeneralNotification(Context context, Object manager, String channelId, String description, int tag) {
        this.context = context;
        this.manager = (NotificationManager) manager;
        this.channelId = channelId;
        this.description =description;
        this.tag = tag;
    }

    /**
     * 创建推送通知
     *
     * @param title 通知标题
     * @param contentText   通知内容
     * @param smallIcon 设置通知小图标，即状态栏图标
     */
    public void setNotificationOnSDK26(String title, String contentText, int smallIcon) {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.enableLights(true);//设置闪烁灯光
            notificationChannel.enableVibration(true);//设置手机震动

            manager.createNotificationChannel(notificationChannel);

            Notification.Builder builder = new Notification.Builder(context, channelId);
            builder.setCategory(Notification.CATEGORY_MESSAGE);
            builder.setContentTitle(title);
            builder.setContentText(contentText);
            builder.setWhen(System.currentTimeMillis());
            builder.setSmallIcon(smallIcon);
            builder.setShowWhen(true);
            builder.setAutoCancel(true);//设置点击通知后自动清除对应的Notification
            builder.setOnlyAlertOnce(true);//设置只响铃一次

            notification = builder.build();
            manager.notify(tag, notification);
        }
    }

    /**
     * 取消通知
     */
    public void cancel() {
        manager.cancel(tag);
    }
}
