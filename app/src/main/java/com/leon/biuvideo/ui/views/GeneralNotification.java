package com.leon.biuvideo.ui.views;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.leon.biuvideo.ui.activitys.DownloadedActivity;

/**
 * 创建通知推送
 */
public class GeneralNotification {
    private final Context context;
    private static NotificationManager manager;
    public Notification notification;

    private final String description; //描述
    private int tag;    //该通知的id，用于取消通知的推送

    public GeneralNotification(Context context, String description, int tag) {
        this.context = context;
        this.description = description;
        this.tag = tag;
        GeneralNotification.manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    /**
     * 创建推送通知
     *
     * @param title       通知标题
     * @param contentText 通知内容
     * @param smallIcon   设置通知小图标，即状态栏图标
     */
    public void setNotificationOnSDK26(String title, String contentText, int smallIcon) {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel("BiuVideoNotification", description, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);//设置闪烁灯光
            notificationChannel.enableVibration(true);//设置手机震动

            manager.createNotificationChannel(notificationChannel);

            Notification.Builder builder = new Notification.Builder(context, "BiuVideoNotification")
                    .setCategory(Notification.CATEGORY_MESSAGE)
                    .setContentTitle(title)
                    .setContentText(contentText)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(smallIcon)
                    .setShowWhen(true)
                    .setProgress(0, 0, true)// 设置模糊进度条
                    .setOnlyAlertOnce(true);//设置只响铃一次

            // 设置点击跳转
            Intent intent = new Intent(context, DownloadedActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            builder.setContentIntent(pendingIntent);

            notification = builder.build();
            manager.notify(tag, notification);
        }
    }

    /**
     * 取消通知
     *
     * @param tag   subId
     */
    public static void cancel(int tag) {
        if (manager != null) {
            manager.cancel(tag);
        }
    }
}
