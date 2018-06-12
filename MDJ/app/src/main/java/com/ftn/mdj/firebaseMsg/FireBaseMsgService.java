package com.ftn.mdj.firebaseMsg;

import com.ftn.mdj.utils.MyNotification;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class FireBaseMsgService  extends FirebaseMessagingService
{

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        System.out.println("From: " + remoteMessage.getFrom());
        System.out.println("Notification Message Body: " + remoteMessage.getNotification());
        System.out.println("Notification Message Body: " + remoteMessage.getData().get("targetActivity"));

        MyNotification notification = new MyNotification();
        notification.showNotification(this, remoteMessage.getNotification().getBody());
    }

}
