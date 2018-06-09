package com.ftn.mdj.firebaseMsg;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.ftn.mdj.R;
import com.ftn.mdj.activities.MainActivity;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FireIDService extends FirebaseInstanceIdService {


    @Override
    public void onTokenRefresh() {


        String tkn = FirebaseInstanceId.getInstance().getToken();
        System.out.println("Poslao token na server ===============================================================");

        sendRegistrationToServer(tkn);
    }


    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
        System.out.println("Registrovao se na server ===============================================================");

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1410 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "M_CH_ID")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("FCM Message")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1410 /* ID of notification */, notificationBuilder.build());
    }

}
