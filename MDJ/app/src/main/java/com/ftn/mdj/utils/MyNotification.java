package com.ftn.mdj.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.ftn.mdj.R;
import com.ftn.mdj.activities.MainActivity;

public class MyNotification {

    public void showNotification(Context context, String body) {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        System.out.println("Primio poruku ===============================================================");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("M_CH_ID", "Location Notifications", NotificationManager.IMPORTANCE_DEFAULT);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "M_CH_ID")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("MDJ : Shopping expert")
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo))
                .setContentText(body)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.logo)
                .setVibrate(new long[]{100, 100, 100, 100})
                .setPriority(android.app.Notification.PRIORITY_MAX)
                .setSound(defaultSoundUri);
        Intent resultIntent = new Intent(context, MainActivity.class);


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );


        notificationBuilder.setContentIntent(resultPendingIntent);
//        NotificationManager mNotificationManager =
//                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


//        mNotificationManager.notify(1, notificationBuilder.build());
        notificationManager.notify(1, notificationBuilder.build());

    }
}
